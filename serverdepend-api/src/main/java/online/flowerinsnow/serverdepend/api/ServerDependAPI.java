package online.flowerinsnow.serverdepend.api;

import org.jetbrains.annotations.NotNull;

public abstract class ServerDependAPI {
    private ServerDependAPI() {
    }

    private static IServerDepend instance;

    public static void setInstance(@NotNull IServerDepend instance) {
        ServerDependAPI.instance = instance;
    }

    public static @NotNull IServerDepend getInstance() {
        return instance;
    }
}
