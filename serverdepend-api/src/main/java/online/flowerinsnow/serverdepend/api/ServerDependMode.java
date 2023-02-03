package online.flowerinsnow.serverdepend.api;

import org.jetbrains.annotations.NotNull;

public enum ServerDependMode {
    /**
     * 未开启
     */
    DISABLED(0),
    /**
     * 失败时关服
     */
    SHUTDOWN(1),
    /**
     * 失败时阻止所有玩家加入
     */
    KICK(2),
    /**
     * 失败时只允许白名单内的玩家加入
     */
    WHITELIST(3);
    public final int id;

    ServerDependMode(int id) {
        this.id = id;
    }

    public static @NotNull ServerDependMode getById(int id) {
        for (ServerDependMode value : values()) {
            if (value.id == id) {
                return value;
            }
        }
        return DISABLED;
    }
}
