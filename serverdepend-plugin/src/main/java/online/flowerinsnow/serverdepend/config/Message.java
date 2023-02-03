package online.flowerinsnow.serverdepend.config;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.configuration.core.annotation.HeaderComment;
import cc.carm.lib.configuration.core.value.type.ConfiguredList;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessageList;

public class Message extends ConfigurationRoot {
    @HeaderComment("控制台日志显示内容，不支持颜色")
    public static final ConfiguredList<String> LOG = ConfiguredList.builder(String.class)
            .fromString().defaults("以下插件必须，但是它们没有被加载，所以服务器无法正常运行").build();

    public static class Kick extends ConfigurationRoot {
        @HeaderComment("正常玩家被踢出的原因")
        public static final ConfiguredList<String> PLAYER = ConfiguredList.builder(String.class).fromString()
                .defaults("&c服务器可能遇到了故障，请等待管理员处理").build();

        @HeaderComment("白名单玩家被踢出的原因")
        public static final ConfiguredList<String> WHITELIST = ConfiguredList.builder(String.class).fromString()
                .defaults("&c以下插件必须，但是它们没有被加载", "&c所以服务器无法正常运行", "&c请排查问题").build();
    }

    public static class Command extends ConfigurationRoot {
        public static final ConfiguredMessageList<String> USAGE = ConfiguredMessageList.ofStrings("&7[&a?&7]&b用法：&e/serverdepend <0/1/2/3> reload");

        @HeaderComment("变量：%(permission)")
        public static final ConfiguredMessageList<String> NO_PERMISSION = ConfiguredMessageList.asStrings()
                .defaults("&7[&c×&7]&c您没有使用该命令的权限").params("permission").build();

        public static final ConfiguredMessageList<String> MODE = ConfiguredMessageList.asStrings()
                .defaults("&7[&b!&7]&b模式已切换为 &e%(mode)").params("mode").build();

        public static final ConfiguredMessageList<String> RELOAD = ConfiguredMessageList.asStrings()
                .defaults("&7[&b!&7]&b重载成功").build();
    }
}
