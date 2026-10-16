@echo off
rem Uso: compilar_e_executar.bat [Main|TestesCentral|Desafios]   (padrao: Main)
cd /d "%~dp0"
if not exist out mkdir out
javac -encoding UTF-8 -d out src\central\*.java || exit /b 1
set CLASSE=%1
if "%CLASSE%"=="" set CLASSE=Main
chcp 65001 >nul
java -Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -cp out central.%CLASSE%
