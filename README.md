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

> 如果 Github 预览图片比较困难，建议克隆项目之后本地查看 README

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

> 测试方法依赖数据支持不可能通过....

![](https://yec-dev.oss-cn-guangzhou.aliyuncs.com/DeepinScreenshot_select-area_20201209210659.png)

## 使用

### 配置启用环境

> 由于我有一套本地环境，因此需要手动切换成 dev （local 只是隐藏了一些隐私数据）

然后使用 idea 打开项目，将项目配置文件改为 `dev` ：

```yaml
spring:
  profiles:
    active: dev
```

### 配置数据库连接信息

配置 `application-dev.yml` 中数据库连接信息：

```yaml
spring:
  datasource:
    driver-class-name: org.mariadb.jdbc.Driver
    url: jdbc:mariadb://localhost:3306/genshin_gacha_demo
    username: root
    password: 123456
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    open-in-view: false

  redis:
    host: localhost
    port: 6379
```

### 配置 COOKIE

> 注意，由于项目所需要的数据均来自网络，该项目采取定时任务抓取资源，而其中两个网络接口需要个人米游社的 `cookie` 信息，所以需要填写正确的 cookie 确保项目启动抓取数据成功。
>
> 具体的获取方法是：打开[米游社官网](https://bbs.mihoyo.com/ys/)，登录之后 F12 调出控制台，切换到 `Application` 一栏，选中 `Cookies` ，在其中把 `cookie_token` 和 `account_id` 复制粘贴到项目的配置文件中：

```yaml
mihoyo:
  account-id: your account id
  cookie-token: your cookie token
  img-save-dir: ${user.home}/.genshin-gacha-demo/img/
```

### 配置定时任务

同时还需要注意的是，如果配置好数据库信息之后第一次启动项目 jpa
会自动生成数据表，而数据是没有的，而我设置的定时任务第一次延迟比较长（避免开发频繁启动项目然后频繁请求米哈游的接口），所以第一次启动将延迟时间修改短一点，之后再修改回来，只涉及两个类的配置修改：`schedule`
包下的 `FetchGenshinGachaPoolData` 和 `FetchGenshinItemData` 类，将其中的所有方法的 initailDelay 修改小一点（毫秒为单位），之后再将其改回来（数据半个月抓取一次就足够了）

```java
@Scheduled(initialDelay = 600, fixedDelay = 864000000)
```

### 启动项目

修改完这些配置之后就可以启动项目了，之后前端启动之后就OK了。

![](https://yec-dev.oss-cn-guangzhou.aliyuncs.com/DeepinScreenshot_select-area_20201209211023.png)



