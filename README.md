# genshin-gacha-demo-api

> 原神模拟抽卡后端 api
>
> 前端：[genshin-gacha-demo-web](https://github.com/imtin/genshin-gacha-demo-web)

项目采用 `Spring Boot` 技术栈

数据库使用了 MariaDB 以及 Redis

其中对 MariaDB 操作引入了 Spring Data Jpa

该项目前后端分离，未引入 shiro 以及 Spring Security 这种安全框架，对权限认证策略为：前端对 `/api/auth` 发起请求，后端获得当前 session
id，然后初始化一个用户，最后使用 `RedisTemplate` 将 session id 作为键，用户对象作为值存入 redis 数据库。认证成功后（默认未做限制）将返回当前的 session id，之后前端将每次都携带一个 vid
请求头访问接口，后端根据这个 vid 从 redis 数据库中取出对应的用户对象继续运行。

## 预览

当前项目只完成了抽奖、统计、仓库展示功能。

![DeepinScreenshot_select-area_20201209204838](https://yec-dev.oss-cn-guangzhou.aliyuncs.com/DeepinScreenshot_select-area_20201209204838.png)

![](https://yec-dev.oss-cn-guangzhou.aliyuncs.com/DeepinScreenshot_select-area_20201209204906.png)

## 安装

> 项目比较小，就不做打包处理了，感觉 BUG 有点多，各种耦合太多了容易出问题....

该项目由 `Maven` 做依赖管理，下载或者克隆当前项目到本地之后可以运行当前项目根目录下的 `mvnw` 或者 `mvnw.bat` 脚本，如果此前没有下载对应的 `wrapper` ，将会自动下载一个 `maven`
环境到当前用户的 `.m2/wrapper` 目录下。具体操作如下：

```shell
git clone https://github.com/imtin/genshin-gacha-demo-api.git
```

![](https://yec-dev.oss-cn-guangzhou.aliyuncs.com/DeepinScreenshot_select-area_20201209210405.png)

```shell
./mvnw -DskipTests clean install
```

![](https://yec-dev.oss-cn-guangzhou.aliyuncs.com/DeepinScreenshot_select-area_20201209210659.png)

## 使用

然后使用 idea 打开项目，将项目配置文件改为 `dev` ：

```yaml
spring:
  profiles:
    active: dev
```

配置 `application-dev.yml` 中数据库连接信息，然后启动项目，之后将前端跑起来就OK了。

![](https://yec-dev.oss-cn-guangzhou.aliyuncs.com/DeepinScreenshot_select-area_20201209211023.png)

