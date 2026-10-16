#!/bin/sh
# Uso: ./compilar_e_executar.sh [Main|TestesCentral|Desafios]   (padrão: Main)
set -e
cd "$(dirname "$0")"
mkdir -p out
javac -encoding UTF-8 -d out src/central/*.java
java -Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -cp out central.${1:-Main}
