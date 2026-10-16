package central;

import java.util.LinkedList;

/** Tabela hash (ID -> Chamado) com encadeamento separado. */
public class TabelaHashChamados {

    private static class EntradaHash {
        final int chave;
        Chamado valor;
        EntradaHash(int chave, Chamado valor) { this.chave = chave; this.valor = valor; }
    }

    private static final double FATOR_CARGA_MAXIMO = 0.75;

    private LinkedList<EntradaHash>[] buckets;
    private int tamanho;
    private final boolean redimensionar;
    private int redimensionamentos;

    /** Tabela com 7 posições, sem redimensionamento (usada na demonstração de colisões). */
    public TabelaHashChamados() { this(7, false); }

    public TabelaHashChamados(int capacidade, boolean redimensionar) {
        if (capacidade < 1) throw new IllegalArgumentException("Capacidade deve ser positiva");
        this.buckets = criarBuckets(capacidade);
        this.redimensionar = redimensionar;
    }

    @SuppressWarnings("unchecked")
    private static LinkedList<EntradaHash>[] criarBuckets(int n) {
        LinkedList<EntradaHash>[] b = new LinkedList[n];
        for (int i = 0; i < n; i++) b[i] = new LinkedList<>();
        return b;
    }

    private int indice(int id, int capacidade) {
        return Math.floorMod(Integer.hashCode(id), capacidade);
    }

    public int indiceDe(int id) { return indice(id, buckets.length); }

    /** Insere ou substitui o chamado associado ao ID. */
    public void put(int id, Chamado chamado) {
        for (EntradaHash e : buckets[indiceDe(id)]) {
            if (e.chave == id) { e.valor = chamado; return; }
        }
        buckets[indiceDe(id)].add(new EntradaHash(id, chamado));
        tamanho++;
        if (redimensionar && (double) tamanho / buckets.length > FATOR_CARGA_MAXIMO) {
            redimensionar();
        }
    }

    /** @return o chamado ou null se o ID não existir. */
    public Chamado get(int id) {
        for (EntradaHash e : buckets[indiceDe(id)]) {
            if (e.chave == id) return e.valor;
        }
        return null;
    }

    /** @return o chamado removido ou null se o ID não existir. */
    public Chamado remove(int id) {
        LinkedList<EntradaHash> lista = buckets[indiceDe(id)];
        for (java.util.Iterator<EntradaHash> it = lista.iterator(); it.hasNext(); ) {
            EntradaHash e = it.next();
            if (e.chave == id) {
                it.remove();
                tamanho--;
                return e.valor;
            }
        }
        return null;
    }

    public boolean containsKey(int id) { return get(id) != null; }

    public int size() { return tamanho; }

    public int capacidade() { return buckets.length; }

    public int getRedimensionamentos() { return redimensionamentos; }

    private void redimensionar() {
        LinkedList<EntradaHash>[] antigos = buckets;
        buckets = criarBuckets(antigos.length * 2 + 1);
        for (LinkedList<EntradaHash> lista : antigos) {
            for (EntradaHash e : lista) buckets[indice(e.chave, buckets.length)].add(e);
        }
        redimensionamentos++;
    }

    /** Número de buckets com mais de uma entrada. */
    public int bucketsComColisao() {
        int c = 0;
        for (LinkedList<EntradaHash> l : buckets) if (l.size() > 1) c++;
        return c;
    }

    public int maiorCadeia() {
        int m = 0;
        for (LinkedList<EntradaHash> l : buckets) m = Math.max(m, l.size());
        return m;
    }

    public String estatisticas() {
        return String.format("capacidade=%d, elementos=%d, buckets com colisão=%d, maior cadeia=%d, redimensionamentos=%d",
                buckets.length, tamanho, bucketsComColisao(), maiorCadeia(), redimensionamentos);
    }

    /** Representação dos buckets, útil para depuração e demonstração. */
    public String mostrarBuckets() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < buckets.length; i++) {
            sb.append("[").append(i).append("]");
            for (EntradaHash e : buckets[i]) sb.append(" -> ").append(e.chave);
            sb.append('\n');
        }
        return sb.toString();
    }
}
