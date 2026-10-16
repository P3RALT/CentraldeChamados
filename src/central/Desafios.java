package central;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;

/** Desafios adicionais: comparação com PriorityQueue e medição de tempo de busca (ArrayList x tabela hash). */
public class Desafios {
    public static void main(String[] args) {
        int n = 100_000;
        Random rnd = new Random(42);

        // Heap próprio x PriorityQueue
        MaxHeapChamados proprio = new MaxHeapChamados();
        PriorityQueue<Chamado> pq = new PriorityQueue<>((a, b) -> a.getPrioridade() != b.getPrioridade()
                ? Integer.compare(b.getPrioridade(), a.getPrioridade())
                : Long.compare(a.getOrdemChegada(), b.getOrdemChegada()));
        ArrayList<Chamado> lista = new ArrayList<>();
        TabelaHashChamados tabela = new TabelaHashChamados(7, true);
        for (int i = 0; i < n; i++) {
            Chamado c = new Chamado(i, "s", "d", 1 + rnd.nextInt(5), i);
            proprio.offer(c); pq.offer(c); lista.add(c); tabela.put(i, c);
        }
        boolean iguais = true;
        long t0 = System.nanoTime();
        long somaProprio = 0;
        while (!proprio.isEmpty()) somaProprio = somaProprio * 31 + proprio.poll().getId();
        long tProprio = System.nanoTime() - t0;
        t0 = System.nanoTime();
        long somaPq = 0;
        while (!pq.isEmpty()) somaPq = somaPq * 31 + pq.poll().getId();
        long tPq = System.nanoTime() - t0;
        iguais = somaProprio == somaPq;
        System.out.printf("Heap próprio: %d ms | PriorityQueue: %d ms | mesma ordem de saída: %b%n",
                tProprio / 1_000_000, tPq / 1_000_000, iguais);

        // Busca: ArrayList x tabela hash (1.000 buscas)
        int buscas = 1000;
        t0 = System.nanoTime();
        int achados = 0;
        for (int i = 0; i < buscas; i++) {
            int id = rnd.nextInt(n);
            for (Chamado c : lista) if (c.getId() == id) { achados++; break; }
        }
        long tLista = System.nanoTime() - t0;
        t0 = System.nanoTime();
        for (int i = 0; i < buscas; i++) if (tabela.get(rnd.nextInt(n)) != null) achados++;
        long tHash = System.nanoTime() - t0;
        System.out.printf("%d buscas em %d chamados: ArrayList %d ms | Tabela hash %d ms%n",
                buscas, n, tLista / 1_000_000, tHash / 1_000_000);
        System.out.println("Estatísticas da tabela: " + tabela.estatisticas());
    }
}
