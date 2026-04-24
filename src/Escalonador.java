import java.util.List;

public interface Escalonador {
    void executar(List<Processo> processos);
}