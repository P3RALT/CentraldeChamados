package central;

/** Integra a tabela hash (busca por ID) e o heap máximo (prioridade). */
public class CentralAtendimento {
    private final TabelaHashChamados chamadosPorId;
    private final MaxHeapChamados filaPrioridade = new MaxHeapChamados();
    private long contadorChegada = 0;

    public CentralAtendimento() { this(new TabelaHashChamados()); }

    public CentralAtendimento(TabelaHashChamados tabela) { this.chamadosPorId = tabela; }

    /** Cadastra o chamado (atribui a ordem de chegada). @return false se o ID já existir. */
    public boolean cadastrar(Chamado chamado) {
        if (chamado == null) throw new IllegalArgumentException("Chamado nulo");
        if (chamadosPorId.containsKey(chamado.getId())) return false;
        chamado.setOrdemChegada(++contadorChegada);
        chamadosPorId.put(chamado.getId(), chamado);
        filaPrioridade.offer(chamado);
        return true;
    }

    /** @return o chamado ou null se não existir. */
    public Chamado buscarPorId(int id) { return chamadosPorId.get(id); }

    public Chamado consultarProximo() { return filaPrioridade.peek(); }

    /** Remove o próximo chamado do heap e da tabela. @return null se a fila estiver vazia. */
    public Chamado atenderProximo() {
        Chamado c = filaPrioridade.poll();
        if (c != null) chamadosPorId.remove(c.getId());
        return c;
    }

    /** Desafio: altera a prioridade (remove e reinsere no heap, mantendo a ordem de chegada). */
    public boolean alterarPrioridade(int id, int novaPrioridade) {
        Chamado c = chamadosPorId.get(id);
        if (c == null) return false;
        c.setPrioridade(novaPrioridade); // lança exceção se inválida, antes de mexer no heap
        filaPrioridade.remover(id);
        filaPrioridade.offer(c);
        return true;
    }

    public boolean estaVazia() { return filaPrioridade.isEmpty(); }

    public int quantidade() { return chamadosPorId.size(); }

    public TabelaHashChamados getTabela() { return chamadosPorId; }
}
