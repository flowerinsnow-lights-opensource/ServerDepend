package online.flowerinsnow.serverdepend.api.event;

import online.flowerinsnow.serverdepend.api.ServerDependMode;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;

/**
 * ServerDepend设置变更时触发
 */
public abstract class ServerDependSettingsChangeEvent extends Event {
    /**
     * 修改原因
     */
    @NotNull public final Reason reason;

    protected ServerDependSettingsChangeEvent(@NotNull Reason reason) {
        this.reason = Objects.requireNonNull(reason);
    }

    /**
     * 模式修改时调用
     */
    public static class Mode extends ServerDependSettingsChangeEvent implements Cancellable {
        public static final HandlerList handlerList = new HandlerList();
        @NotNull private ServerDependMode newMode;

        private boolean cancel;

        public Mode(@NotNull Reason reason, @NotNull ServerDependMode newMode) {
            super(reason);
            this.newMode = Objects.requireNonNull(newMode);
        }

        public @NotNull ServerDependMode getNewMode() {
            return newMode;
        }

        public void setNewMode(@NotNull ServerDependMode newMode) {
            this.newMode = Objects.requireNonNull(newMode);
        }

        @NotNull
        @Override
        public HandlerList getHandlers() {
            return handlerList;
        }

        public static HandlerList getHandlerList() {
            return handlerList;
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

    /**
     * 白名单列表修改时调用
     */
    public static class Whitelist extends ServerDependSettingsChangeEvent implements Cancellable {
        private static final HandlerList handlerList = new HandlerList();
        private boolean cancel;
        @NotNull private Set<String> whitelist;

        public Whitelist(@NotNull Reason reason, @NotNull Set<String> whitelist) {
            super(reason);
            this.whitelist = Objects.requireNonNull(whitelist);
        }

        @Override
        public boolean isCancelled() {
            return cancel;
        }

        @Override
        public void setCancelled(boolean cancel) {
            this.cancel = cancel;
        }

        @NotNull
        @Override
        public HandlerList getHandlers() {
            return handlerList;
        }

        public static HandlerList getHandlerList() {
            return handlerList;
        }

        public @NotNull Set<String> getWhitelist() {
            return whitelist;
        }

        public void setWhitelist(@NotNull Set<String> whitelist) {
            this.whitelist = whitelist;
        }
    }

    /**
     * 变更的原因
     */
    public enum Reason {
        /**
         * 插件调用API
         */
        PLUGIN,
        /**
         * 命令修改
         */
        COMMAND,
        /**
         * 重载配置
         */
        RELOAD
    }
}
