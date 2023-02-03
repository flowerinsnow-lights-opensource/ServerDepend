# ServerDepend
服务器前置，就是假如某个插件没有成功加载，就关服或阻止玩家进入

# 作用
有些关键插件（比如BW），如果没有正确加载，玩家可以任意破坏方块，让服务器变成2b2t生存；为避免这种事情发生，就不应该让玩家进来

# 模式
本插件有3个模式（可在`config.yml`）中配置
1. 当发现列表中的某个插件未成功加载时将服务器关闭
2. 当发现列表中的某个插件未成功加载时阻止所有玩家加入
3. 当发现列表中的某个插件未成功加载时阻止玩家加入，除非玩家在白名单中

# API
本项目提供了少量API，引入方式如下：

## Maven
```xml
<repository>
    <id>serverdepend</id>
    <url>https://maven.pkg.github.com/flowerinsnow-lights-opensource/ServerDepend</url>
</repository>
```

```xml
<dependency>
    <groupId>online.flowerinsnow</groupId>
    <artifactId>serverdepend-api</artifactId>
    <version>1.0.0</version>
    <scope>provided</scope>
</dependency>
```

## `plugin.yml`
```yaml
depend:
  - ServerDepend
```
或软依赖
```yaml
softdepend:
  - ServerDepend
```

# 使用的类库
* [MineConfiguration](https://github.com/CarmJos/MineConfiguration) By [CarmJos](https://github.com/CarmJos) (LGPL-3.0 license)

# 开源协议
[GNU通用公共许可证](https://www.gnu.org/licenses/gpl-3.0.html)