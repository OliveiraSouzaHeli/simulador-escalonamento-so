import java.util.*;
import java.io.*;

public class Simulador {

    public static void main(String[] args) throws IOException {
        Scanner teclado = new Scanner(System.in);
        String caminhoArquivo;

        // ─── Entrada do arquivo ─────────────────────────────────────
        if (args.length > 0) {
            caminhoArquivo = args[0];
        } else {
            System.out.print("Digite o caminho do arquivo de entrada: ");
            caminhoArquivo = teclado.nextLine().trim();
        }

        File arquivo = new File(caminhoArquivo);
        if (!arquivo.exists()) {
            System.out.println("Erro: arquivo não encontrado → " + caminhoArquivo);
            System.exit(1);
        }

        // ─── Leitura unificada ──────────────────────────────────────
        Entrada entradaDados = LeitorArquivo.ler(caminhoArquivo);
        List<Processo> processos = entradaDados.getProcessos();
        int quantum = entradaDados.getQuantum();

        if (processos.isEmpty()) {
            System.out.println("Nenhum processo encontrado no arquivo.");
            System.exit(1);
        }

        boolean continuar = true;

        // ─── Menu ───────────────────────────────────────────────────
        while (continuar) {

            System.out.println("\n╔══════════════════════════════════════════╗");
            System.out.println("║   Simulador de Escalonamento de Processos ║");
            System.out.println("║   UFMS – Sistemas Operacionais 2026       ║");
            System.out.println("╠══════════════════════════════════════════╣");
            System.out.printf ("║  Arquivo : %-31s║%n", arquivo.getName());
            System.out.printf ("║  Processos carregados: %-19d║%n", processos.size());
            System.out.printf ("║  Quantum (Round Robin): %-18d║%n", quantum);
            System.out.println("╠══════════════════════════════════════════╣");
            System.out.println("║  Escolha o algoritmo:                    ║");
            System.out.println("║  [1] FCFS                                ║");
            System.out.println("║  [2] SJF – Não Preemptivo                ║");
            System.out.println("║  [3] SJF – Preemptivo (SRTF)             ║");
            System.out.println("║  [4] Prioridade                          ║");
            System.out.println("║  [5] Round Robin                         ║");
            System.out.println("║  [6] Executar TODOS                      ║");
            System.out.println("║  [7] Exibir processos                    ║");
            System.out.println("║  [0] Sair                                ║");
            System.out.println("╚══════════════════════════════════════════╝");
            System.out.print("Opção: ");

            String opcao = teclado.nextLine().trim();

            switch (opcao) {

                case "1":
                    new AlgoritmoFCFS().executar(copiarProcessos(processos));
                    break;

                case "2":
                    new SJFNaoPreemptivo().executar(copiarProcessos(processos));
                    break;

                case "3":
                    new SJFPreemptivo().executar(copiarProcessos(processos));
                    break;

                case "4":
                    new AlgoritmoPrioridade().executar(copiarProcessos(processos));
                    break;

                case "5":
                    new AlgoritmoRoundRobin(quantum).executar(copiarProcessos(processos));
                    break;

                case "6":
                    System.out.println("\n>>> Executando todos os algoritmos...\n");

                    new AlgoritmoFCFS().executar(copiarProcessos(processos));
                    new SJFNaoPreemptivo().executar(copiarProcessos(processos));
                    new SJFPreemptivo().executar(copiarProcessos(processos));
                    new AlgoritmoPrioridade().executar(copiarProcessos(processos));
                    new AlgoritmoRoundRobin(quantum).executar(copiarProcessos(processos));
                    break;

                case "7":
                    exibirProcessos(processos, quantum);
                    break;

                case "0":
                    System.out.println("\nEncerrando o simulador. Até logo!");
                    continuar = false;
                    break;

                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }

            if (continuar && !opcao.equals("0")) {
                System.out.print("\nPressione Enter para voltar ao menu...");
                teclado.nextLine();
            }
        }

        teclado.close();
    }

    // 🔁 Cópia segura
    private static List<Processo> copiarProcessos(List<Processo> original) {
        List<Processo> copia = new ArrayList<>();

        for (Processo p : original) {
            copia.add(p.copiar());
        }

        return copia;
    }

    // Exibição
    private static void exibirProcessos(List<Processo> processos, int quantum) {
        System.out.println("\n--- Processos Carregados ---");

        System.out.printf("%-10s %-12s %-10s %-12s%n",
                "Nome", "Chegada(ms)", "Burst(ms)", "Prioridade");

        System.out.println("--------------------------------------------");

        for (Processo p : processos) {
            System.out.printf("%-10s %-12d %-10d %-12d%n",
                    p.getNome(),
                    p.getChegada(),
                    p.getBurst(),
                    p.getPrioridade());
        }

        System.out.printf("%nQuantum configurado: %d ms%n", quantum);
    }
}
