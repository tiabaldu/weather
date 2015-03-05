@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  WeatherStore startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

@rem Add default JVM options here. You can also use JAVA_OPTS and WEATHER_STORE_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windowz variants

if not "%OS%" == "Windows_NT" goto win9xME_args
if "%@eval[2+2]" == "4" goto 4NT_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*
goto execute

:4NT_args
@rem Get arguments from the 4NT Shell from JP Software
set CMD_LINE_ARGS=%$

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\WeatherStore.jar;%APP_HOME%\lib\slf4j-log4j12-1.5.6.jar;%APP_HOME%\lib\slf4j-simple-1.7.10.jar;%APP_HOME%\lib\quartz-2.1.5.jar;%APP_HOME%\lib\javaee-api-7.0.jar;%APP_HOME%\lib\javax.servlet-api-3.0.1.jar;%APP_HOME%\lib\jersey-container-grizzly2-servlet-2.0-m07-1.jar;%APP_HOME%\lib\jersey-container-servlet-core-2.16.jar;%APP_HOME%\lib\json-20141113.jar;%APP_HOME%\lib\postgresql-9.3-1100-jdbc41.jar;%APP_HOME%\lib\tomcat-api-7.0.59.jar;%APP_HOME%\lib\owm-japis-2.5.0.3.jar;%APP_HOME%\lib\log4j-1.2.14.jar;%APP_HOME%\lib\c3p0-0.9.1.1.jar;%APP_HOME%\lib\javax.mail-1.5.0.jar;%APP_HOME%\lib\servlet-api-2.4.jar;%APP_HOME%\lib\jersey-container-grizzly2-http-2.0-m07-1.jar;%APP_HOME%\lib\grizzly-http-servlet-2.2.10.jar;%APP_HOME%\lib\tomcat-servlet-api-7.0.59.jar;%APP_HOME%\lib\activation-1.1.jar;%APP_HOME%\lib\grizzly-http-server-2.2.10.jar;%APP_HOME%\lib\grizzly-http-2.2.10.jar;%APP_HOME%\lib\grizzly-rcm-2.2.10.jar;%APP_HOME%\lib\osgi-resource-locator-1.0.1.jar;%APP_HOME%\lib\javax.inject-1.jar;%APP_HOME%\lib\grizzly-framework-2.2.10.jar;%APP_HOME%\lib\gmbal-api-only-3.0.0-b023.jar;%APP_HOME%\lib\management-api-3.0.0-b012.jar;%APP_HOME%\lib\slf4j-api-1.7.10.jar;%APP_HOME%\lib\javax.inject-2.4.0-b09.jar;%APP_HOME%\lib\jersey-common-2.16.jar;%APP_HOME%\lib\javax.annotation-api-1.2.jar;%APP_HOME%\lib\jersey-guava-2.16.jar;%APP_HOME%\lib\jersey-server-2.16.jar;%APP_HOME%\lib\jersey-media-jaxb-2.16.jar;%APP_HOME%\lib\validation-api-1.1.0.Final.jar;%APP_HOME%\lib\javax.ws.rs-api-2.0.1.jar;%APP_HOME%\lib\hk2-api-2.4.0-b09.jar;%APP_HOME%\lib\aopalliance-repackaged-2.4.0-b09.jar;%APP_HOME%\lib\hk2-locator-2.4.0-b09.jar;%APP_HOME%\lib\javassist-3.18.1-GA.jar;%APP_HOME%\lib\jersey-client-2.16.jar;%APP_HOME%\lib\hk2-utils-2.4.0-b09.jar

@rem Execute WeatherStore
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %WEATHER_STORE_OPTS%  -classpath "%CLASSPATH%" com.tia.weatherStorePack.testMain %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable WEATHER_STORE_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%WEATHER_STORE_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
