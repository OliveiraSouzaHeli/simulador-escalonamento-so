public class Processo {

    // ─── Atributos privados ───────────────────────────────────────────────
    private String nome;
    private int chegada;
    private int burst;
    private int prioridade;
    private int quantum;

    private int tempoRestante;
    private int inicio;
    private int fim;
    private int espera;
    private int retorno;

    // ─── Construtor ───────────────────────────────────────────────────────
    public Processo(String nome, int chegada, int burst, int prioridade, int quantum) {
        this.nome = nome;
        this.chegada = chegada;
        this.burst = burst;
        this.prioridade = prioridade;
        this.quantum = quantum;
        this.tempoRestante = burst;
        this.inicio = -1;
        this.fim = 0;
        this.espera = 0;
        this.retorno = 0;
    }

    // ─── Getters ──────────────────────────────────────────────────────────
    public String getNome() { return nome; }
    public int getChegada() { return chegada; }
    public int getBurst() { return burst; }
    public int getPrioridade() { return prioridade; }
    public int getQuantum() { return quantum; }
    public int getTempoRestante() { return tempoRestante; }
    public int getInicio() { return inicio; }
    public int getFim() { return fim; }
    public int getEspera() { return espera; }
    public int getRetorno() { return retorno; }

    // ─── Setters ──────────────────────────────────────────────────────────
    public void setTempoRestante(int tempoRestante) {
        this.tempoRestante = tempoRestante;
    }

    public void setInicio(int inicio) {
        this.inicio = inicio;
    }

    public void setFim(int fim) {
        this.fim = fim;
    }

    public void setEspera(int espera) {
        this.espera = espera;
    }

    public void setRetorno(int retorno) {
        this.retorno = retorno;
    }

    // ─── MÉTODO EXTRA (ESSENCIAL) ─────────────────────────────────────────
    // Cria uma cópia do processo (evita conflito entre algoritmos)
    public Processo copiar() {
        Processo p = new Processo(
                this.nome,
                this.chegada,
                this.burst,
                this.prioridade,
                this.quantum
        );

        p.setTempoRestante(this.burst); // reset correto
        return p;
    }
}
