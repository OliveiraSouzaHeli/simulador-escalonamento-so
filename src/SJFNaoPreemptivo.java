import java.util.*;

public class SJFNaoPreemptivo implements Escalonador {

    @Override
    public void executar(List<Processo> original) {
        System.out.println("\n========================================");
        System.out.println("  ALGORITMO: SJF – Não Preemptivo");
        System.out.println("========================================");

        List<Processo> processos = copiar(original);
        List<Processo> prontos   = new ArrayList<>();
        List<Processo> pendentes = new ArrayList<>(processos);

        pendentes.sort(Comparator.comparingInt(Processo::getChegada));

        int tempo = 0;
        List<String> log = new ArrayList<>();

        while (!pendentes.isEmpty() || !prontos.isEmpty()) {

            Iterator<Processo> it = pendentes.iterator();
            while (it.hasNext()) {
                Processo p = it.next();
                if (p.getChegada() <= tempo) {
                    prontos.add(p);
                    it.remove();
                }
            }

            if (prontos.isEmpty()) {
                int prox = pendentes.get(0).getChegada();
                log.add(String.format("  [t=%d] CPU ociosa até t=%d", tempo, prox));
                tempo = prox;
                continue;
            }

            prontos.sort(Comparator.comparingInt(Processo::getBurst));
            Processo atual = prontos.remove(0);

            atual.setInicio(tempo);

            log.add(String.format(
                "  [t=%d] %s selecionado (burst=%d)",
                tempo,
                atual.getNome(),
                atual.getBurst()
            ));

            tempo += atual.getBurst();

            atual.setFim(tempo);
            atual.setRetorno(atual.getFim() - atual.getChegada());
            atual.setEspera(atual.getRetorno() - atual.getBurst());

            log.add(String.format(
                "  [t=%d] %s concluído",
                tempo,
                atual.getNome()
            ));
        }

        imprimirEstatisticas(processos);
        imprimirLog(log);
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

    private void imprimirLog(List<String> log) {
        System.out.println("\n--- Registro de Execução ---");
        for (String s : log) System.out.println(s);
    }

    // ─── Estatísticas ──────────────────────────────────────────────────
    private void imprimirEstatisticas(List<Processo> processos) {
        System.out.println("\n--- Estatísticas Finais ---");

        System.out.printf("%-10s %-10s %-10s %-12s %-12s %-12s %-10s%n",
                "Processo", "Chegada", "Burst", "Início", "Fim", "Espera", "Retorno");

        // 🔥 melhoria opcional (igual ao SJF)
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