package central;

/** Chamado de suporte atendido pela central. */
public class Chamado {
    private int id;
    private String solicitante;
    private String descricao;
    private int prioridade;
    private long ordemChegada;

    public Chamado(int id, String solicitante, String descricao, int prioridade) {
        this(id, solicitante, descricao, prioridade, 0);
    }

    public Chamado(int id, String solicitante, String descricao, int prioridade, long ordemChegada) {
        validarPrioridade(prioridade);
        this.id = id;
        this.solicitante = solicitante;
        this.descricao = descricao;
        this.prioridade = prioridade;
        this.ordemChegada = ordemChegada;
    }

    private static void validarPrioridade(int p) {
        if (p < 1 || p > 5) {
            throw new IllegalArgumentException("Prioridade deve estar entre 1 e 5: " + p);
        }
    }

    public int getId() { return id; }
    public String getSolicitante() { return solicitante; }
    public String getDescricao() { return descricao; }
    public int getPrioridade() { return prioridade; }
    public long getOrdemChegada() { return ordemChegada; }

    public void setPrioridade(int prioridade) {
        validarPrioridade(prioridade);
        this.prioridade = prioridade;
    }

    public void setOrdemChegada(long ordemChegada) { this.ordemChegada = ordemChegada; }

    public String getClassificacao() {
        switch (prioridade) {
            case 1: return "Baixa";
            case 2: return "Normal";
            case 3: return "Média";
            case 4: return "Alta";
            default: return "Crítica";
        }
    }

    @Override
    public String toString() {
        return "Chamado{id=" + id + ", solicitante='" + solicitante + "', descricao='" + descricao
                + "', prioridade=" + prioridade + " (" + getClassificacao() + "), chegada=" + ordemChegada + "}";
    }
}
