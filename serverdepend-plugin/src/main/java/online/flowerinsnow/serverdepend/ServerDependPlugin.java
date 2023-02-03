package online.flowerinsnow.serverdepend;

import cc.carm.lib.mineconfiguration.bukkit.MineConfiguration;
import cc.carm.lib.mineconfiguration.bukkit.source.BukkitConfigProvider;
import online.flowerinsnow.serverdepend.api.ServerDependAPI;
import online.flowerinsnow.serverdepend.api.ServerDependMode;
import online.flowerinsnow.serverdepend.api.event.AsyncServerDependNoLoginEvent;
import online.flowerinsnow.serverdepend.api.event.ServerDependKickEvent;
import online.flowerinsnow.serverdepend.api.event.ServerDependShutdownEvent;
import online.flowerinsnow.serverdepend.command.CommandMain;
import online.flowerinsnow.serverdepend.config.Config;
import online.flowerinsnow.serverdepend.config.Message;
import online.flowerinsnow.serverdepend.core.ServerDependCore;
import online.flowerinsnow.serverdepend.util.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class ServerDependPlugin extends JavaPlugin implements Listener {
    private static ServerDependPlugin instance;
    private BukkitConfigProvider configProvider;
    private BukkitConfigProvider messageProvider;

    private final ArrayList<String> missing = new ArrayList<>();

    private static ServerDependCore core;

    @Override
    public void onLoad() {
        instance = this;

        configProvider = MineConfiguration.from(this, "config.yml");
        configProvider.initialize(Config.class);
        messageProvider = MineConfiguration.from(this, "messages.yml");
        messageProvider.initialize(Message.class);
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        Optional.ofNullable(getCommand("serverdepend")).ifPresent(cmd -> {
            CommandMain executor = new CommandMain();
            cmd.setExecutor(executor);
            cmd.setTabCompleter(executor);
        });

        core = new ServerDependCore(ServerDependMode.getById(Config.MODE.getNotNull()), new HashSet<>(Config.WHITELIST.getNotNull()));
        ServerDependAPI.setInstance(core);

        getServer().getScheduler().runTask(this, this::testFor);
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        missing.clear();
    }

    @Override
    public void reloadConfig() {
        try {
            configProvider.reload();
            messageProvider.reload();

            core.mode = ServerDependMode.getById(Config.MODE.getNotNull());
            core.whitelist = new HashSet<>(Config.WHITELIST.getNotNull());
            testFor();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveConfig() {
        try {
            configProvider.save();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void testFor() {
        missing.clear();
        Config.PLUGINS.getOptional().ifPresent(pls -> {
            Config.PLUGINS.getNotNull().forEach(pl -> { // 查找未成功加载的插件
                if (Arrays.stream(getServer().getPluginManager().getPlugins()).noneMatch(plugin -> plugin.getName().equalsIgnoreCase(pl))) {
                    missing.add(pl);
                }
            });
            if (!missing.isEmpty()) { // 有未成功加载的插件
                Message.LOG.getNotNull().forEach(log -> getLogger().info(log));
                missing.forEach(miss -> getLogger().info(miss));
                if (1 == Config.MODE.getNotNull()) {
                    ServerDependShutdownEvent event = new ServerDependShutdownEvent();
                    getServer().getPluginManager().callEvent(event);
                    if (!event.isCancelled()) {
                        getServer().shutdown();
                    }
                } else if (2 == Config.MODE.getNotNull()) {
                    Bukkit.getOnlinePlayers().forEach(p ->
                        callEventAndKickPlayer(p, false)
                    );
                } else if (3 == Config.MODE.getNotNull()) {
                    Bukkit.getOnlinePlayers().forEach(p ->
                        callEventAndKickPlayer(p, true)
                    );
                }
            }
        });
    }

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent e) {
        if (missing.isEmpty()) {
            return;
        }
        if (2 == Config.MODE.getNotNull()) {
            AsyncServerDependNoLoginEvent event = new AsyncServerDependNoLoginEvent(e.getName(), e.getUniqueId());
            getServer().getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
                // 玩家是白名单
                if (isWhitelist(e.getName(), e.getUniqueId())) {
                    e.setKickMessage(getKickWhitelistMessage());
                } else { // 不是白名单
                    e.setKickMessage(getKickNotWhitelistMessage());
                }
            }
        } else if (3 == Config.MODE.getNotNull()) {
            if (!isWhitelist(e.getName(), e.getUniqueId())) {
                AsyncServerDependNoLoginEvent event = new AsyncServerDependNoLoginEvent(e.getName(), e.getUniqueId());
                getServer().getPluginManager().callEvent(event);
                if (!event.isCancelled()) {
                    e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
                    e.setKickMessage(getKickNotWhitelistMessage());
                }
            }
        }
    }

    private static boolean isWhitelist(String name, UUID uuid) {
        List<String> whitelist = Config.WHITELIST.get();
        for (String s : whitelist) {
            if (s.equalsIgnoreCase(name)) return true;
            if (uuid == null) continue;
            if (uuid.toString().equalsIgnoreCase(s)) return true;
        }
        return false;
    }

    private String getKickWhitelistMessage() {
        List<String> messages = Message.Kick.WHITELIST.getNotNull();
        StringBuilder sb = new StringBuilder(StringUtils.strcat(messages, "\n", false, true));

        missing.forEach(miss -> sb.append("\n§e").append(miss));
        return sb.toString();
    }

    private String getKickNotWhitelistMessage() {
        List<String> messages = Message.Kick.PLAYER.getNotNull();
        return StringUtils.strcat(messages, "§e", false, true);
    }

    /**
     * 调用事件，如果事件未被取消，踢出该玩家
     *
     * @param p 玩家
     * @param ignoreWhitelist 玩家是白名单时，忽略踢出
     * @return 是否踢出
     */
    @SuppressWarnings("UnusedReturnValue")
    private boolean callEventAndKickPlayer(Player p, boolean ignoreWhitelist) {
        if (isWhitelist(p.getName(), p.getUniqueId())) {
            if (ignoreWhitelist) {
                return false;
            }
            // 玩家是白名单 发送白名单的踢出消息
            ServerDependKickEvent event = new ServerDependKickEvent(p);
            getServer().getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                p.kickPlayer(getKickWhitelistMessage());
                return true;
            }
            return false;
        } else {
            // 玩家不是白名单 发送普通玩家的踢出消息
            ServerDependKickEvent event = new ServerDependKickEvent(p);
            getServer().getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                p.kickPlayer(getKickNotWhitelistMessage());
                return false;
            }
            return true;
        }
    }

    public static ServerDependPlugin getInstance() {
        return instance;
    }

    public static ServerDependCore getCore() {
        return core;
    }
}
