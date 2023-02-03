package online.flowerinsnow.serverdepend.util;

import cc.carm.lib.mineconfiguration.common.utils.ColorParser;

import java.util.List;

public abstract class StringUtils {
    private StringUtils() {
    }

    /**
     * 拼接字符串
     *
     * @param src 源字符串
     * @param catWith 拼接字符
     * @param lastChar 是否在字符串最后插入拼接字符
     * @param parseColour 解析颜色代码
     * @return 拼接后的字符串
     */
    public static String strcat(List<String> src, String catWith, boolean lastChar, boolean parseColour) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < src.size(); i++) {
            sb.append(parseColour ? ColorParser.parseBaseColor(src.get(i)) : src.get(i));
            if (lastChar || i + 1 < src.size()) {
                sb.append(catWith);
            }
        }
        return sb.toString();
    }
}
