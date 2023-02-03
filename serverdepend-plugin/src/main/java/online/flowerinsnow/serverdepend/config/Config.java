package online.flowerinsnow.serverdepend.config;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.configuration.core.annotation.HeaderComment;
import cc.carm.lib.configuration.core.value.type.ConfiguredList;
import cc.carm.lib.configuration.core.value.type.ConfiguredValue;

public class Config extends ConfigurationRoot {
    @HeaderComment({
            "模式：",
            "0 - 关闭",
            "1 - 失败时关服",
            "2 - 不允许任何玩家进入服务器",
            "3 - 仅允许白名单玩家进入服务器",
            "其他 - 默认0，不会报错"
    })
    public static final ConfiguredValue<Integer> MODE = ConfiguredValue.of(Integer.class, 1);

    @HeaderComment("必须的插件列表，不区分大小写")
    public static final ConfiguredList<String> PLUGINS = ConfiguredList.builder(String.class).fromString()
            .defaults("ServerDepend").build();

    @HeaderComment("白名单列表，可以是ID也可以是UUID，不区分大小写")
    public static final ConfiguredList<String> WHITELIST = ConfiguredList.builder(String.class).fromString()
            .defaults("d73a11c3-da45-4e31-ab77-35f454755fc0").build();
}
