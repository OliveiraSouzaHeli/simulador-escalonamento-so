# 💻 Simulador de Algoritmos de Escalonamento

**Trabalho Acadêmico | Desenvolvido em Dupla** Projeto prático desenvolvido para a disciplina de **Sistemas Operacionais** da Faculdade de Computação da **UFMS** (Universidade Federal de Mato Grosso do Sul).

**Autores:**
* **Heli Souza**
* **Junior Rosa**

---

## 🚀 Sobre o Projeto
Este é um simulador desenvolvido em Java para demonstrar e comparar o comportamento de diferentes algoritmos de escalonamento de processos da CPU. A ferramenta processa um arquivo de texto contendo a lista de processos e gera um log detalhado da execução, além de métricas finais de desempenho.

## ⚙️ Algoritmos Implementados

1. **FCFS (First-Come, First-Served):** Executa na ordem de chegada. Sem preempção.
2. **SJF (Shortest Job First - Não Preemptivo):** Escolhe o processo com menor burst na fila de prontos. Uma vez iniciado, não é interrompido.
3. **SJF Preemptivo (SRTF - Shortest Remaining Time First):** A cada instante, o processo com menor tempo restante é executado. Pode antecipar o processo em execução.
4. **Prioridade (Não Preemptivo):** Menor número = maior prioridade. Em caso de empate, desempata pelo tempo de chegada.
5. **Round Robin:** Cada processo recebe o tempo do *quantum* definido no arquivo. Ao esgotar o quantum, vai para o fim da fila.
6. **Modo Completo:** Executa todos os algoritmos de uma vez para fins de comparação.

## 🛠️ Como Compilar e Executar

**Requisito:** Java 8 ou superior instalado.

### Linux / Mac
```bash
chmod +x compile.sh
./compile.sh exemplos/processos.txt
