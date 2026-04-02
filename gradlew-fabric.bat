@echo off
rem Use Java 17 for Fabric Loom (required; set to your JDK 17 path if needed)
if not defined JAVA_HOME set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.17.10-hotspot"
if not exist "%JAVA_HOME%\bin\java.exe" (
    echo ERROR: JAVA_HOME does not point to a valid JDK 17. Edit gradlew-fabric.bat or set JAVA_HOME.
    exit /b 1
)
set "PATH=%JAVA_HOME%\bin;%PATH%"
set "GRADLE_OPTS=--add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.lang.reflect=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/java.io=ALL-UNNAMED --add-opens java.base/java.text=ALL-UNNAMED"
call "%~dp0gradlew.bat" %*
