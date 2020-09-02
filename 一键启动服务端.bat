@echo off
@title ÓÎÏ·ºÐµÚÈý°æ£¨ÌÔ±¦¶¹¶¹Ð¹Â¶°æ£©
Color 0A

xcopy /y /h  dist\*.jar* ".\jdk\jre\lib\ext"
xcopy /y /h  dist\jce\*.jar* ".\jdk\jre\lib\security"

set path=.\jdk\jre\bin;%SystemRoot%\system32;%SystemRoot%;%SystemRoot%
set JRE_HOME=.\jdk\jre
set JAVA_HOME=.\jdk\jre\bin
set CLASSPATH=.;dist\*
java -Xmx4000M -Xms4000M -server -Xmn500M -XX:PermSize=500M -XX:MaxPermSize=500M -Xss256K -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:CMSFullGCsBeforeCompaction=0 -XX:+CMSClassUnloadingEnabled -XX:+UseFastAccessorMethods -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintHeapAtGC -Xloggc:log/gc.log gui.YXH
pause