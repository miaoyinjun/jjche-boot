#!/usr/bin/env bash
source ~/.bash_profile

#cd /home/user1/demo/
#mv -f jjche-boot-web-demo.jar  jjche-boot-web-demo.jar.bak
#cp -rf ./tmp/*.jar ./

ID=`ps -ef | grep jjche-boot-web-demo | grep -v "grep" | awk '{print $2}'`
echo $ID
for id in $ID
do
kill -9 $id
done

nohup java ${JAVA_OPTS} -jar jjche-boot-web-demo.jar ${JAVA_WEB_SERVICE_OPTS} &> /dev/null &