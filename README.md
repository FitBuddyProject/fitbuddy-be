# FitBuddy

## stack 

- Spring 3.2.8
- Security
- Mongo
- Redis


## RDB 기준으로 작성
> 
> ```mermaid
> erDiagram
>     User {
>         VARCHAR(512)  uuid
>         VARCHAR(16)   phone
>         VARCHAR(512) password
>         VARCHAR(32)   nickname
>         VARCHAR(64)   email
>         
>         VARCHAR(1024) refreshToken
>         
>         VARCHAR(512)  pushToken
>         BOOLEAN       sendable
>         INT           tired
> 
>         DATETIME      joinDate
>         DATETIME      lastModifiedDate
>         DATETIME      lastSignInDate
>         
> %%      MyBuddy[]     buddies  
>     }
>     
>     MyBuddy {
>         VARCHAR(512)   uuid
>         BOOLEAN        isPrimary
>         VARCHAR(512)   buddy     
> %%      ENUM          buddy
>         VARCHAR(32)    name
>         DATETIME       whenWeMet
>         INT           exp
> 
>         ENUM           action
>         DATETIME       whenStart
>         DATETIME       whenEnd
> %%      ActionHistory[]       actionHistories  
>     }
>     
> 
> 
>     Action {
>         VARCHAR(512)   uuid
>         ENUM           action
>         ENUM           actionStatus
>         DATETIME       start
>         DATETIME       end
>     }
>     
>     Fitness {
>         VARCHAR(512)  uuid
>         VARCHAR(512)  user
>         VARCHAR(32)   type
>         INT           duration
>         ENUM          intensity
>         VARCHAR(512)  memo
>         DATETIME      when
>     }
> ```

## Settings
> 
> ## Mongo(Test)
> https://github.com/themattman/mongodb-raspberrypi-docker
> ```docker
> docker create -it \
> --name mongo \
> -p 27017:27017 \
> -e MONGODB_INITDB_ROOT_USERNAME=#### \
> -e MONGODB_INITDB_ROOT_PASSWORD=##### \
> -v /home/mongo:/data/db \
> --log-driver json-file \
> --log-opt max-size=10m \
> --log-opt max-file=3 \
> --restart always \
> mongodb-raspberrypi4-unofficial-r7.0.4:latest
> ```
> 
> ## Redis
> ```docker
> docker create -it \
> --name redis \
> -p 6379:6379 \
> -v /home/redis/data:/data \
> -v /home/redis/redis.conf:/usr/local/etc/redis/redis.conf \
> --log-driver json-file \
> --log-opt max-size=10m \
> --log-opt max-file=3 \
> --restart always \
> redis:7.4-rc2
> ```