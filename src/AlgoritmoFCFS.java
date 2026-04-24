import java.util.*;

public class AlgoritmoFCFS implements Escalonador {

    @Override
    public void executar(List<Processo> original) {
        System.out.println("\n========================================");
        System.out.println("  ALGORITMO: FCFS – First-Come, First-Served");
        System.out.println("========================================");

        // Faz cópia para não alterar os originais
        List<Processo> processos = copiar(original);

        // Ordena por tempo de chegada
        processos.sort(Comparator.comparingInt(Processo::getChegada));

        int tempo = 0;
        List<String> log = new ArrayList<>();

        for (Processo p : processos) {

            if (tempo < p.getChegada()) {
                log.add(String.format("  [t=%d] CPU ociosa até t=%d", tempo, p.getChegada()));
                tempo = p.getChegada();
            }

            p.setInicio(tempo);
            log.add(String.format("  [t=%d] %s iniciado (burst=%d)",
                    tempo, p.getNome(), p.getBurst()));

            tempo += p.getBurst();

            p.setFim(tempo);
            p.setRetorno(p.getFim() - p.getChegada());
            p.setEspera(p.getRetorno() - p.getBurst());

            log.add(String.format("  [t=%d] %s concluído", tempo, p.getNome()));
        }

        // Imprime log
        System.out.println("\n--- Registro de Execução ---");
        for (String s : log) System.out.println(s);

        imprimirEstatisticas(processos);
    }

    private List<Processo> copiar(List<Processo> lista) {
        List<Processo> copia = new ArrayList<>();

        for (Processo p : lista) {
            copia.add(new Processo(
                    p.getNome(),
                    p.getChegada(),
                    p.getBurst(),
                    p.getPrioridade(),
                    p.getQuantum()
            ));
        }

        return copia;
    }

    // ─── Estatísticas ──────────────────────────────────────────────────
    private void imprimirEstatisticas(List<Processo> processos) {
        System.out.println("\n--- Estatísticas Finais ---");

        System.out.printf("%-10s %-10s %-10s %-12s %-12s %-12s %-10s%n",
                "Processo", "Chegada", "Burst", "Início", "Fim", "Espera", "Retorno");

        processos.sort(Comparator.comparingInt(Processo::getInicio));

        double somaEspera = 0, somaRetorno = 0;

        for (Processo p : processos) {
            System.out.printf("%-10s %-10d %-10d %-12d %-12d %-12d %-10d%n",
                    p.getNome(),
                    p.getChegada(),
                    p.getBurst(),
                    p.getInicio(),
                    p.getFim(),
                    p.getEspera(),
                    p.getRetorno());

            somaEspera += p.getEspera();
            somaRetorno += p.getRetorno();
        }

        int n = processos.size();

        System.out.printf("%nTempo médio de espera  : %.2f ms%n", somaEspera / n);
        System.out.printf("Tempo médio de retorno : %.2f ms%n", somaRetorno / n);
    }
}
