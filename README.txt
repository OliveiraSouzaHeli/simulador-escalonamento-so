╔══════════════════════════════════════════════════════════╗
║   SIMULADOR DE ALGORITMOS DE ESCALONAMENTO               ║
║   UFMS – Faculdade de Computação – Sistemas Operacionais ║
╚══════════════════════════════════════════════════════════╝

── COMO COMPILAR ────────────────────────────────────────────

  Linux/Mac:
    chmod +x compile.sh
    ./compile.sh exemplos/processos.txt

  Windows (CMD):
    mkdir bin
    javac -d bin src\*.java
    java -cp bin Simulador exemplos\processos.txt

  Requisito: Java 8 ou superior instalado.

── FORMATO DO ARQUIVO DE ENTRADA ────────────────────────────

  # Linhas com # são comentários e são ignoradas
  QUANTUM=20           ← quantum em ms para o Round Robin

  NOME  CHEGADA  BURST  PRIORIDADE
  P1    0        200    3
  P2    40       180    1
  ...

  Campos:
    NOME       → identificador do processo (texto sem espaços)
    CHEGADA    → tempo de chegada em milissegundos
    BURST      → tempo de CPU necessário em milissegundos
    PRIORIDADE → número inteiro (menor = maior prioridade)

── ALGORITMOS IMPLEMENTADOS ─────────────────────────────────

  [1] FCFS – First-Come, First-Served
      Executa na ordem de chegada. Sem preempção.

  [2] SJF – Shortest Job First (Não Preemptivo)
      Escolhe o processo com menor burst na fila de prontos.
      Uma vez iniciado, não é interrompido.

  [3] SJF Preemptivo (SRTF – Shortest Remaining Time First)
      A cada instante, o processo com menor tempo restante
      é executado. Pode preemptar o processo em execução.

  [4] Prioridade (Não Preemptivo)
      Menor número = maior prioridade.
      Em empate, desempata por tempo de chegada.

  [5] Round Robin
      Cada processo recebe o quantum definido no arquivo.
      Ao esgotar o quantum, vai para o fim da fila.

  [6] Todos os algoritmos de uma vez

── SAÍDA ─────────────────────────────────────────────────────

  O simulador exibe na tela:
  • Registro passo a passo da execução (quem executou, preemptou,
    entrou na fila de prontos, ficou pronto)
  • Tabela final com início, fim, espera e retorno de cada processo
  • Tempo médio de espera e retorno

── ARQUIVOS DE EXEMPLO ──────────────────────────────────────

  exemplos/processos.txt  → 7 processos (caso completo)
  exemplos/simples.txt    → 4 processos (fácil de verificar)

