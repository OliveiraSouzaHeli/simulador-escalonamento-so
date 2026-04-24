import java.util.List;

public class Entrada {
    private List<Processo> processos;
    private int quantum;

    public Entrada(List<Processo> processos, int quantum) {
        this.processos = processos;
        this.quantum = quantum;
    }

    public List<Processo> getProcessos() {
        return processos;
    }

    public int getQuantum() {
        return quantum;
    }
}