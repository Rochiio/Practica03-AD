@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  TennisLabMongo startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Resolve any "." and ".." in APP_HOME to make it shorter.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@rem Add default JVM options here. You can also use JAVA_OPTS and TENNIS_LAB_MONGO_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto execute

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto execute

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\TennisLabMongo-1.0-SNAPSHOT.jar;%APP_HOME%\lib\sqldelight-coroutines-extensions-jvm-1.5.4.jar;%APP_HOME%\lib\sqlite-driver-1.5.4.jar;%APP_HOME%\lib\jdbc-driver-1.5.4.jar;%APP_HOME%\lib\runtime-jvm-1.5.4.jar;%APP_HOME%\lib\kotlin-logging-jvm-3.0.4.jar;%APP_HOME%\lib\ktor-client-serialization-jvm-2.1.3.jar;%APP_HOME%\lib\ktor-serialization-kotlinx-json-jvm-2.1.3.jar;%APP_HOME%\lib\kotlinx-serialization-json-jvm-1.4.1.jar;%APP_HOME%\lib\cache4k-jvm-0.9.0.jar;%APP_HOME%\lib\mordant-jvm-2.0.0-beta8.jar;%APP_HOME%\lib\ktorfit-lib-jvm-1.0.0-beta16.jar;%APP_HOME%\lib\ktor-client-content-negotiation-jvm-2.1.3.jar;%APP_HOME%\lib\kmongo-coroutine-4.7.2.jar;%APP_HOME%\lib\kmongo-coroutine-core-4.7.2.jar;%APP_HOME%\lib\ktor-client-cio-jvm-2.1.3.jar;%APP_HOME%\lib\ktor-client-json-jvm-2.1.3.jar;%APP_HOME%\lib\ktor-client-core-jvm-2.1.3.jar;%APP_HOME%\lib\ktor-serialization-kotlinx-jvm-2.1.3.jar;%APP_HOME%\lib\ktor-websocket-serialization-jvm-2.1.3.jar;%APP_HOME%\lib\ktor-serialization-jvm-2.1.3.jar;%APP_HOME%\lib\ktor-http-cio-jvm-2.1.3.jar;%APP_HOME%\lib\ktor-websockets-jvm-2.1.3.jar;%APP_HOME%\lib\ktor-events-jvm-2.1.3.jar;%APP_HOME%\lib\ktor-http-jvm-2.1.3.jar;%APP_HOME%\lib\ktor-network-tls-jvm-2.1.3.jar;%APP_HOME%\lib\ktor-network-jvm-2.1.3.jar;%APP_HOME%\lib\ktor-utils-jvm-2.1.3.jar;%APP_HOME%\lib\ktor-io-jvm-2.1.3.jar;%APP_HOME%\lib\kotlinx-coroutines-jdk8-1.6.4.jar;%APP_HOME%\lib\kotlinx-coroutines-slf4j-1.6.4.jar;%APP_HOME%\lib\kotlinx-coroutines-core-jvm-1.6.4.jar;%APP_HOME%\lib\kotlinx-coroutines-reactive-1.6.4.jar;%APP_HOME%\lib\kotlinx-serialization-core-jvm-1.4.1.jar;%APP_HOME%\lib\stately-iso-collections-jvm-1.2.2.jar;%APP_HOME%\lib\colormath-jvm-3.2.1.jar;%APP_HOME%\lib\ktorfit-annotations-jvm-1.0.0-beta16.jar;%APP_HOME%\lib\stately-isolate-jvm-1.2.2.jar;%APP_HOME%\lib\stately-concurrency-jvm-1.2.2.jar;%APP_HOME%\lib\stately-common-jvm-1.2.2.jar;%APP_HOME%\lib\kotlin-stdlib-jdk8-1.7.21.jar;%APP_HOME%\lib\kmongo-async-4.7.2.jar;%APP_HOME%\lib\logback-classic-1.4.4.jar;%APP_HOME%\lib\junit-4.13.1.jar;%APP_HOME%\lib\junit-jupiter-params-5.8.1.jar;%APP_HOME%\lib\junit-jupiter-engine-5.8.1.jar;%APP_HOME%\lib\junit-jupiter-api-5.8.1.jar;%APP_HOME%\lib\junit-platform-engine-1.8.1.jar;%APP_HOME%\lib\junit-platform-commons-1.8.1.jar;%APP_HOME%\lib\junit-jupiter-5.8.1.jar;%APP_HOME%\lib\guava-31.1-jre.jar;%APP_HOME%\lib\kotlin-stdlib-jdk7-1.7.21.jar;%APP_HOME%\lib\kmongo-async-core-4.7.2.jar;%APP_HOME%\lib\kmongo-jackson-mapping-4.7.2.jar;%APP_HOME%\lib\kmongo-async-shared-4.7.2.jar;%APP_HOME%\lib\kmongo-property-4.7.2.jar;%APP_HOME%\lib\kmongo-shared-4.7.2.jar;%APP_HOME%\lib\kmongo-id-jackson-4.7.2.jar;%APP_HOME%\lib\jackson-module-loader-0.4.0.jar;%APP_HOME%\lib\kmongo-id-4.7.2.jar;%APP_HOME%\lib\jackson-databind-2.13.4.2.jar;%APP_HOME%\lib\jackson-annotations-2.13.4.jar;%APP_HOME%\lib\jackson-core-2.13.4.jar;%APP_HOME%\lib\jackson-module-kotlin-2.13.4.jar;%APP_HOME%\lib\kreflect-1.0.0.jar;%APP_HOME%\lib\kotlin-reflect-1.7.20.jar;%APP_HOME%\lib\kmongo-data-4.7.2.jar;%APP_HOME%\lib\atomicfu-jvm-0.18.5.jar;%APP_HOME%\lib\markdown-jvm-0.3.1.jar;%APP_HOME%\lib\kotlin-stdlib-1.7.21.jar;%APP_HOME%\lib\kotlin-stdlib-common-1.7.21.jar;%APP_HOME%\lib\slf4j-api-2.0.3.jar;%APP_HOME%\lib\logback-core-1.4.4.jar;%APP_HOME%\lib\hamcrest-core-1.3.jar;%APP_HOME%\lib\failureaccess-1.0.1.jar;%APP_HOME%\lib\listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar;%APP_HOME%\lib\jsr305-3.0.2.jar;%APP_HOME%\lib\checker-qual-3.12.0.jar;%APP_HOME%\lib\error_prone_annotations-2.11.0.jar;%APP_HOME%\lib\j2objc-annotations-1.3.jar;%APP_HOME%\lib\sqlite-jdbc-3.34.0.jar;%APP_HOME%\lib\annotations-13.0.jar;%APP_HOME%\lib\bson4jackson-2.13.1.jar;%APP_HOME%\lib\opentest4j-1.2.0.jar;%APP_HOME%\lib\mongodb-driver-reactivestreams-4.7.2.jar;%APP_HOME%\lib\reactor-core-3.2.22.RELEASE.jar;%APP_HOME%\lib\reactive-streams-1.0.3.jar;%APP_HOME%\lib\mongodb-driver-core-4.7.2.jar;%APP_HOME%\lib\bson-record-codec-4.7.2.jar;%APP_HOME%\lib\bson-4.7.2.jar


@rem Execute TennisLabMongo
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %TENNIS_LAB_MONGO_OPTS%  -classpath "%CLASSPATH%" MainKt %*

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable TENNIS_LAB_MONGO_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%TENNIS_LAB_MONGO_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
