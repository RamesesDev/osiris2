@echo off
if "%1" == "" (ant -lib lib/mysql-connector-java-5.0.4-bin.jar) else (ant -f %1 -lib lib\mysql-connector-java-5.0.4-bin.jar)