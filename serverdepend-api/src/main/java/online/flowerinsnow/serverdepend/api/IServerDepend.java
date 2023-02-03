package online.flowerinsnow.serverdepend.api;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;

/**
 * 本类中的内容设置后就会自动保存（除非事件被取消）
 */
public interface IServerDepend {
    /**
     * 获取当前模式
     *
     * @return 当前模式
     */
    @NotNull ServerDependMode getMode();

    /**
     * 设置当前模式
     *
     * @param mode 当前模式
     */
    void setMode(@NotNull ServerDependMode mode);

    /**
     * 获取白名单列表，可能是玩家名也可能是UUID
     *
     * @return 白名单列表
     */
    @NotNull Set<String> getWhitelist();

    /**
     * 设置白名单列表，可以是玩家名也可以是UUID
     *
     * @param whitelist 白名单列表
     */
    void setWhitelist(@NotNull Collection<String> whitelist);

    /**
     * 从配置文件重载
     */
    void reload();
}
