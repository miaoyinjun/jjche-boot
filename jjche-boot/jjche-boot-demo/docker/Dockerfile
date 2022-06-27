FROM openjdk:8-jre-alpine

ADD ./docker/wait-for.sh /wait-for.sh

#作者MAINTAINER Wu Jize <wujize188@163.com>#指定阿里镜象
RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories

# #增加字体，解决验证码没有字体报空指针问题
RUN set -xe \
&& apk --no-cache add ttf-dejavu fontconfig

#系统编码
ENV LANG=C.UTF-8 LC_ALL=C.UTF-8

ARG JAR_FILE
ADD target/${JAR_FILE} /opt/app.jar

ARG TZ="Asia/Shanghai"

ENV TZ ${TZ}

RUN apk upgrade --update \
    && apk add bash tzdata \
    && ln -sf /usr/share/zoneinfo/${TZ} /etc/localtime \
    && echo ${TZ} > /etc/timezone \
    && rm -rf /var/cache/apk/*

ENTRYPOINT java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -Duser.timezone=${TZ} -Dfile.encoding=UTF-8 -jar /opt/app.jar ${JAVA_WEB_SERVICE_OPTS}