import java.util.*;

public class AlgoritmoPrioridade implements Escalonador {

    /**
     * Escalonamento por Prioridade (não preemptivo).
     * Menor número = maior prioridade.
     * Desempate por tempo de chegada.
     */
    @Override
    public void executar(List<Processo> original) {
        System.out.println("\n========================================");
        System.out.println("  ALGORITMO: Prioridade (Não Preemptivo)");
        System.out.println("  [Menor número = Maior prioridade]");
        System.out.println("========================================");

        List<Processo> processos = copiar(original);
        List<Processo> pendentes = new ArrayList<>(processos);

        pendentes.sort(Comparator.comparingInt(Processo::getChegada));

        List<Processo> prontos = new ArrayList<>();
        List<String> log = new ArrayList<>();

        int tempo = 0;

        while (!pendentes.isEmpty() || !prontos.isEmpty()) {

            // Carrega fila de prontos
            Iterator<Processo> it = pendentes.iterator();
            while (it.hasNext()) {
                Processo p = it.next();
                if (p.getChegada() <= tempo) {
                    prontos.add(p);
                    it.remove();
                }
            }

            // CPU ociosa
            if (prontos.isEmpty()) {
                int proxChegada = pendentes.get(0).getChegada();
                log.add(String.format("  [t=%d] CPU ociosa até t=%d", tempo, proxChegada));
                tempo = proxChegada;
                continue;
            }

            // Ordena por prioridade e chegada
            prontos.sort(
                Comparator.comparingInt(Processo::getPrioridade)
                          .thenComparingInt(Processo::getChegada)
            );

            Processo atual = prontos.remove(0);

            atual.setInicio(tempo);

            log.add(String.format(
                "  [t=%d] %s selecionado (prioridade=%d, burst=%d)",
                tempo,
                atual.getNome(),
                atual.getPrioridade(),
                atual.getBurst()
            ));

            int fim = tempo + atual.getBurst();

            // Verifica novos processos chegando (sem preempção)
            it = pendentes.iterator();
            while (it.hasNext()) {
                Processo p = it.next();
                if (p.getChegada() < fim) {
                    log.add(String.format(
                        "  [t=%d] %s entra na fila de prontos (prioridade=%d)",
                        p.getChegada(),
                        p.getNome(),
                        p.getPrioridade()
                    ));
                    prontos.add(p);
                    it.remove();
                }
            }

            tempo = fim;

            atual.setFim(tempo);
            atual.setRetorno(atual.getFim() - atual.getChegada());
            atual.setEspera(atual.getRetorno() - atual.getBurst());

            log.add(String.format(
                "  [t=%d] %s concluído",
                tempo,
                atual.getNome()
            ));
        }

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

    private void imprimirEstatisticas(List<Processo> processos) {
        System.out.println("\n--- Estatísticas Finais ---");

        System.out.printf("%-10s %-10s %-10s %-12s %-12s %-12s %-10s%n",
                "Processo", "Chegada", "Burst", "Início", "Fim", "Espera", "Retorno");

        System.out.printf("%-10s %-10s %-10s %-12s %-12s %-12s %-10s%n",
                "--------", "-------", "-----", "------", "---", "------", "-------");

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