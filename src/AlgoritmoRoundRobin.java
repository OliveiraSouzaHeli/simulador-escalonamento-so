import java.util.*;

public class AlgoritmoRoundRobin implements Escalonador {

    private int quantum;

    public AlgoritmoRoundRobin(int quantum) {
        this.quantum = quantum;
    }

    @Override
    public void executar(List<Processo> original) {
        System.out.println("\n========================================");
        System.out.println("  ALGORITMO: Round Robin");
        System.out.println("  Quantum = " + quantum + " ms");
        System.out.println("========================================");

        List<Processo> processos = copiar(original);
        List<Processo> pendentes = new ArrayList<>(processos);

        pendentes.sort(Comparator.comparingInt(Processo::getChegada));

        Queue<Processo> fila = new LinkedList<>();
        List<String> log = new ArrayList<>();

        int tempo = 0;

        while (!pendentes.isEmpty() || !fila.isEmpty()) {

            // ─── Carrega processos que chegaram ─────────────────────────
            Iterator<Processo> it = pendentes.iterator();
            while (it.hasNext()) {
                Processo p = it.next();
                if (p.getChegada() <= tempo) {
                    fila.add(p);
                    log.add(String.format(
                            "  [t=%d] %s chegou e entrou na fila",
                            tempo,
                            p.getNome()
                    ));
                    it.remove();
                }
            }

            // ─── CPU ociosa ─────────────────────────────────────────────
            if (fila.isEmpty()) {
                int prox = pendentes.get(0).getChegada();
                log.add(String.format("  [t=%d] CPU ociosa até t=%d", tempo, prox));
                tempo = prox;
                continue;
            }

            Processo atual = fila.poll();

            if (atual.getInicio() == -1) {
                atual.setInicio(tempo);
            }

            int execucao = Math.min(quantum, atual.getTempoRestante());

            log.add(String.format(
                    "  [t=%d] %s iniciou execução (quantum=%d, restante=%d)",
                    tempo,
                    atual.getNome(),
                    quantum,
                    atual.getTempoRestante()
            ));

            int tempoInicial = tempo;

            // ─── Execução passo a passo ────────────────────────────────
            for (int i = 0; i < execucao; i++) {
                tempo++;
                atual.setTempoRestante(atual.getTempoRestante() - 1);

                // Verifica novas chegadas
                it = pendentes.iterator();
                while (it.hasNext()) {
                    Processo p = it.next();
                    if (p.getChegada() == tempo) {
                        fila.add(p);
                        log.add(String.format(
                                "  [t=%d] %s chegou e entrou na fila",
                                tempo,
                                p.getNome()
                        ));
                        it.remove();
                    }
                }

                // Finalizou antes do quantum acabar
                if (atual.getTempoRestante() == 0) {
                    atual.setFim(tempo);
                    atual.setRetorno(atual.getFim() - atual.getChegada());
                    atual.setEspera(atual.getRetorno() - atual.getBurst());

                    log.add(String.format(
                            "  [t=%d] %s concluído",
                            tempo,
                            atual.getNome()
                    ));
                    break;
                }
            }

            // ─── Se NÃO terminou → PREEMPÇÃO ───────────────────────────
            if (atual.getTempoRestante() > 0) {
                log.add(String.format(
                        "  [t=%d] %s preemptado (executou %d ms, restante=%d)",
                        tempo,
                        atual.getNome(),
                        (tempo - tempoInicial),
                        atual.getTempoRestante()
                ));

                fila.add(atual);
            }
        }

        System.out.println("\n--- Registro de Execução ---");
        for (String s : log) System.out.println(s);

        imprimirEstatisticas(processos);
    }

    // ─── Cópia segura ─────────────────────────────────────────────────
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
