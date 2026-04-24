import java.util.*;

public class SJFPreemptivo implements Escalonador {

    @Override
    public void executar(List<Processo> original) {
        System.out.println("\n========================================");
        System.out.println("  ALGORITMO: SJF Preemptivo (SRTF)");
        System.out.println("========================================");

        List<Processo> processos = copiar(original);
        List<Processo> pendentes = new ArrayList<>(processos);

        pendentes.sort(Comparator.comparingInt(Processo::getChegada));

        List<Processo> prontos = new ArrayList<>();
        List<String> log = new ArrayList<>();

        Processo atual = null;
        int tempo = 0;
        int concluidos = 0;

        while (concluidos < processos.size()) {

            Iterator<Processo> it = pendentes.iterator();
            while (it.hasNext()) {
                Processo p = it.next();
                if (p.getChegada() <= tempo) {
                    prontos.add(p);
                    it.remove();
                }
            }

            if (prontos.isEmpty() && atual == null) {
                if (!pendentes.isEmpty()) {
                    log.add(String.format("  [t=%d] CPU ociosa", tempo));
                    tempo = pendentes.get(0).getChegada();
                }
                continue;
            }

            if (!prontos.isEmpty()) {
                prontos.sort(Comparator.comparingInt(Processo::getTempoRestante));
                Processo candidato = prontos.get(0);

                if (atual == null || candidato.getTempoRestante() < atual.getTempoRestante()) {

                    if (atual != null) {
                        log.add(String.format(
                            "  [t=%d] %s preemptado (restante=%d)",
                            tempo,
                            atual.getNome(),
                            atual.getTempoRestante()
                        ));
                        prontos.add(atual);
                    }

                    atual = candidato;
                    prontos.remove(candidato);

                    if (atual.getInicio() == -1) {
                        atual.setInicio(tempo);
                    }

                    log.add(String.format(
                        "  [t=%d] %s em execução (restante=%d)",
                        tempo,
                        atual.getNome(),
                        atual.getTempoRestante()
                    ));
                }
            }

            atual.setTempoRestante(atual.getTempoRestante() - 1);
            tempo++;

            if (atual.getTempoRestante() == 0) {
                atual.setFim(tempo);
                atual.setRetorno(atual.getFim() - atual.getChegada());
                atual.setEspera(atual.getRetorno() - atual.getBurst());

                log.add(String.format(
                    "  [t=%d] %s concluído",
                    tempo,
                    atual.getNome()
                ));

                concluidos++;
                atual = null;
            }
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