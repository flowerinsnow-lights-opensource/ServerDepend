package online.flowerinsnow.serverdepend.core;

import online.flowerinsnow.serverdepend.ServerDependPlugin;
import online.flowerinsnow.serverdepend.api.IServerDepend;
import online.flowerinsnow.serverdepend.api.ServerDependMode;
import online.flowerinsnow.serverdepend.api.event.ServerDependSettingsChangeEvent;
import online.flowerinsnow.serverdepend.config.Config;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ServerDependCore implements IServerDepend {
    @NotNull public ServerDependMode mode;
    @NotNull public Set<String> whitelist;

    public ServerDependCore(@NotNull ServerDependMode mode, @NotNull Set<String> whitelist) {
        this.mode = mode;
        this.whitelist = whitelist;
    }

    @Override
    public @NotNull ServerDependMode getMode() {
        return mode;
    }

    @Override
    public void setMode(@NotNull ServerDependMode mode) {
        setMode(mode, ServerDependSettingsChangeEvent.Reason.PLUGIN);
    }

    public void setMode(@NotNull ServerDependMode mode, @Nullable ServerDependSettingsChangeEvent.Reason reason) {
        Objects.requireNonNull(mode);
        if (reason != null) {
            ServerDependSettingsChangeEvent.Mode event = new ServerDependSettingsChangeEvent.Mode(reason, mode);
            if (event.isCancelled()) {
                return;
            }
            mode = event.getNewMode();
        }
        this.mode = mode;
        Config.MODE.set(mode.id);
        ServerDependPlugin.getInstance().saveConfig();
    }

    @Override
    public @NotNull Set<String> getWhitelist() {
        return new HashSet<>(whitelist);
    }

    @Override
    public void setWhitelist(@NotNull Collection<String> whitelist) {
        setWhitelist(whitelist, ServerDependSettingsChangeEvent.Reason.PLUGIN);
    }

    public void setWhitelist(@NotNull Collection<String> whitelist, @Nullable ServerDependSettingsChangeEvent.Reason reason) {
        Objects.requireNonNull(whitelist);
        if (reason != null) {
            HashSet<String> newWhitelist = new HashSet<>();
            ServerDependSettingsChangeEvent.Whitelist event = new ServerDependSettingsChangeEvent.Whitelist(reason, newWhitelist);
            if (!event.isCancelled()) {
                return;
            }
            whitelist = new HashSet<>(event.getWhitelist());
        }
        this.whitelist = new HashSet<>(whitelist);
        Config.WHITELIST.set(new ArrayList<>(this.whitelist));
        ServerDependPlugin.getInstance().saveConfig();
    }

    @Override
    public void reload() {
        ServerDependPlugin.getInstance().reloadConfig();
    }
}
