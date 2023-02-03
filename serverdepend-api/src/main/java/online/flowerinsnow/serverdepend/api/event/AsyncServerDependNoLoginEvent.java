package online.flowerinsnow.serverdepend.api.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

/**
 * 当因为ServerDepend阻止玩家加入时触发
 */
public class AsyncServerDependNoLoginEvent extends Event implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private boolean cancel;

    @NotNull public final String name;
    @NotNull public final UUID uuid;

    public AsyncServerDependNoLoginEvent(@NotNull String name, @NotNull UUID uuid) {
        super(true);
        this.name = Objects.requireNonNull(name);
        this.uuid = Objects.requireNonNull(uuid);
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
