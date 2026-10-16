package central;

import java.util.ArrayList;

/** Heap máximo de chamados: maior prioridade na raiz; empate resolvido pela menor ordem de chegada. */
public class MaxHeapChamados {

    private final ArrayList<Chamado> heap = new ArrayList<>();

    public void offer(Chamado chamado) {
        if (chamado == null) throw new IllegalArgumentException("Chamado nulo");
        heap.add(chamado);
        subir(heap.size() - 1);
    }

    /** Consulta a raiz sem remover; null se vazio. */
    public Chamado peek() { return heap.isEmpty() ? null : heap.get(0); }

    /** Remove e retorna a raiz; null se vazio. */
    public Chamado poll() {
        if (heap.isEmpty()) return null;
        Chamado raiz = heap.get(0);
        Chamado ultimo = heap.remove(heap.size() - 1);
        if (!heap.isEmpty()) {
            heap.set(0, ultimo);
            descer(0);
        }
        return raiz;
    }

    /** Remove um chamado pelo ID (usado na alteração de prioridade). O(n). */
    public boolean remover(int id) {
        for (int i = 0; i < heap.size(); i++) {
            if (heap.get(i).getId() == id) {
                Chamado ultimo = heap.remove(heap.size() - 1);
                if (i < heap.size()) {
                    heap.set(i, ultimo);
                    subir(i);
                    descer(i);
                }
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty() { return heap.isEmpty(); }

    public int size() { return heap.size(); }

    private void subir(int indice) {
        while (indice > 0) {
            int pai = (indice - 1) / 2;
            if (temMaiorPrioridade(heap.get(indice), heap.get(pai))) {
                trocar(indice, pai);
                indice = pai;
            } else {
                break;
            }
        }
    }

    private void descer(int indice) {
        int n = heap.size();
        while (true) {
            int esq = 2 * indice + 1, dir = esq + 1, maior = indice;
            if (esq < n && temMaiorPrioridade(heap.get(esq), heap.get(maior))) maior = esq;
            if (dir < n && temMaiorPrioridade(heap.get(dir), heap.get(maior))) maior = dir;
            if (maior == indice) break;
            trocar(indice, maior);
            indice = maior;
        }
    }

    private void trocar(int a, int b) {
        Chamado tmp = heap.get(a);
        heap.set(a, heap.get(b));
        heap.set(b, tmp);
    }

    /** true se a deve ficar acima de b. */
    private boolean temMaiorPrioridade(Chamado a, Chamado b) {
        if (a.getPrioridade() != b.getPrioridade()) return a.getPrioridade() > b.getPrioridade();
        return a.getOrdemChegada() < b.getOrdemChegada();
    }
}
