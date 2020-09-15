#!/bin/bash
#这里可替换为你自己的执行程序，其他代码无需更改 
#将需要依赖的lib包文件夹 与执行jar放在一个目录下
# 如/usr/local/jar/xxxx.jar /usr/local/jar/lib

ROOT_PATH=/usr/local/jar/

#
#LIB_PATH=

cd $ROOT_PATH
export LANG=zh_CN.UTF-8
APP_NAME=xxxx.jar
export JAVA_HOME=/home/broadxt/stream/java/jdk1.8.0_201
export PATH=$JAVA_HOME/bin:$PATH
 
PID_FILE=$ROOT_PATH/xxxx.pid
 
#使用说明，用来提示输入参数
usage() {
    echo "Usage: sh 执行脚本.sh [start|stop|restart|status]"
    exit 1
}
 
#检查程序是否在运行
is_exist(){
 # pid=`ps -ef|grep $APP_NAME|grep -v grep|awk '{print $2}' `
  #netstat -anp|grep 8090|awk '{printf $7}'|cut -d/ -f1  根据端口8090 获取pid

  
  #如果不存在返回1，存在返回0     
  if [ -e $PID_FILE ]; then
  
	echo "$PID_FILE already running...."
   return 0
  else
  
    return 1
  fi
  

}
 RETVAL=0
#启动方法
start(){
  is_exist
  if [ $? -eq "0" ]; then
    echo "${APP_NAME} is already running. pid=${pid} ."
  else
    nohup java -Dloader.path=./lib -jar $APP_NAME > /dev/null 2>&1 &
	
	   RETVAL=$?
   if [ $RETVAL = 0 ]; then
     echo $!>$PID_FILE
   fi
    echo "${APP_NAME}  start."
  fi
}
 
#停止方法
stop(){
  is_exist
  if [ $? -eq "0" ]; then
  pid=`cat $PID_FILE`
  rm -f $PID_FILE
        P=`ps -p $pid|wc -l`
      if [ 2 -eq $P ]; then
         echo "kill process"
         kill  $pid
         sleep 3
      fi
	        #double check
      P=`ps -p $pid|wc -l`
      if [ 2 -eq $P ]; then
         echo "kill process forcely"
         kill  -9 $pid
         sleep 1
      fi
   echo  "已杀死---- ${APP_NAME}  pid=${pid} ."
  else
    echo "${APP_NAME} is dead"
  fi  
}
 
#输出运行状态
status(){
  is_exist
  if [ $? -eq "0" ]; then
  pid=`cat $PID_FILE`
    echo -e "》》》》》${APP_NAME}《《《《《\n》》》》》Active:active(running)-- Pid is ${pid}《《《《《"
  else
    echo -e  "》》》》》${APP_NAME}《《《《《\n》》》》》Active:inactive(dead)《《《《《"
  fi
}
 
#重启
restart(){
  stop
  start
}
 
#根据输入参数，选择执行对应方法，不输入则执行使用说明
case "$1" in
  "start")
    start
    ;;
  "stop")
    stop
    ;;
  "status")
    status
    ;;
  "restart")
    restart
    ;;
  *)
    usage
    ;;
esac
