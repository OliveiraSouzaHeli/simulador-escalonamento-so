#!/bin/bash
# compile.sh – Compila e executa o Simulador de Escalonamento
# Uso: ./compile.sh [arquivo_de_entrada]

SRC="src"
BIN="bin"

mkdir -p "$BIN"

echo "=== Compilando o Simulador ==="
javac -d "$BIN" "$SRC"/*.java

if [ $? -ne 0 ]; then
    echo "ERRO: falha na compilação."
    exit 1
fi

echo "=== Compilação concluída ==="
echo ""

ARQUIVO=${1:-"exemplos/processos.txt"}
echo "=== Iniciando o Simulador (arquivo: $ARQUIVO) ==="
java -cp "$BIN" Simulador "$ARQUIVO"
