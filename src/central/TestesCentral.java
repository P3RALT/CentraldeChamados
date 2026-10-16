package central;

import java.util.ArrayList;
import java.util.List;

/** Testes automatizados simples (sem dependências externas). Retorna código 1 se algum falhar. */
public class TestesCentral {
    private static final List<String[]> resultados = new ArrayList<>();
    private static int falhas = 0;

    private static void registrar(String caso, String entrada, String obtido, boolean ok) {
        resultados.add(new String[]{caso, entrada, obtido, ok ? "APROVADO" : "REPROVADO"});
        if (!ok) falhas++;
    }

    private static CentralAtendimento centralCom(int... prioridades) {
        CentralAtendimento c = new CentralAtendimento();
        for (int i = 0; i < prioridades.length; i++) c.cadastrar(new Chamado(100 + i, "User" + i, "Desc " + i, prioridades[i]));
        return c;
    }

    public static void main(String[] args) {
        // 1 - Cadastro de seis chamados
        CentralAtendimento c1 = centralCom(2, 5, 3, 1, 4, 5);
        registrar("Cadastro", "6 chamados (IDs 100-105)", c1.quantidade() + " armazenados", c1.quantidade() == 6);

        // 2 - Colisão
        CentralAtendimento c2 = new CentralAtendimento();
        c2.cadastrar(new Chamado(10, "A", "a", 1));
        c2.cadastrar(new Chamado(17, "B", "b", 2));
        c2.cadastrar(new Chamado(24, "C", "c", 3));
        TabelaHashChamados t = c2.getTabela();
        boolean mesmoIndice = t.indiceDe(10) == 3 && t.indiceDe(17) == 3 && t.indiceDe(24) == 3;
        boolean todos = c2.buscarPorId(10) != null && c2.buscarPorId(17) != null && c2.buscarPorId(24) != null
                && t.size() == 3;
        registrar("Colisão", "IDs 10, 17, 24 (índice 3 com 7 buckets)",
                "índices=" + t.indiceDe(10) + "," + t.indiceDe(17) + "," + t.indiceDe(24) + "; todos encontrados=" + todos,
                mesmoIndice && todos);

        // 3 - Busca existente
        Chamado b = c1.buscarPorId(102);
        registrar("Busca", "ID 102", b == null ? "null" : "ID " + b.getId() + " prio " + b.getPrioridade(),
                b != null && b.getId() == 102 && b.getPrioridade() == 3);

        // 4 - Busca inválida
        Chamado inv = c1.buscarPorId(999);
        registrar("Busca inválida", "ID 999", inv == null ? "null (sem exceção)" : "encontrou?!", inv == null);

        // 5 - Prioridade (consultar não remove)
        Chamado prox = c1.consultarProximo();
        c1.consultarProximo();
        registrar("Prioridade", "consultarProximo() 2x",
                "ID " + prox.getId() + " prio " + prox.getPrioridade() + "; quantidade=" + c1.quantidade(),
                prox.getPrioridade() == 5 && c1.quantidade() == 6);

        // 6 - Empate: dois de prioridade 5 -> 101 chegou antes de 105
        registrar("Empate", "prio 5: IDs 101 (1º) e 105 (6º)", "próximo = ID " + prox.getId(), prox.getId() == 101);

        // 7 - Remoção: atender e buscar o mesmo ID
        Chamado at = c1.atenderProximo();
        boolean removido = c1.buscarPorId(at.getId()) == null && c1.quantidade() == 5
                && c1.consultarProximo().getId() == 105;
        registrar("Remoção", "atenderProximo() e buscar ID " + at.getId(),
                "buscarPorId=" + c1.buscarPorId(at.getId()) + "; novo próximo=ID " + c1.consultarProximo().getId(), removido);

        // 8 - Fila vazia
        CentralAtendimento vazia = new CentralAtendimento();
        boolean ok8;
        String obtido8;
        try {
            ok8 = vazia.atenderProximo() == null && vazia.consultarProximo() == null && vazia.estaVazia();
            obtido8 = "null, sem exceção";
        } catch (RuntimeException e) { ok8 = false; obtido8 = "exceção " + e; }
        registrar("Fila vazia", "atenderProximo() sem chamados", obtido8, ok8);

        // 9 - ID duplicado
        CentralAtendimento c9 = centralCom(2);
        boolean dup = c9.cadastrar(new Chamado(100, "X", "dup", 5));
        registrar("ID duplicado", "cadastrar ID 100 duas vezes", "segunda=" + dup + "; qtd=" + c9.quantidade()
                + "; prio original=" + c9.buscarPorId(100).getPrioridade(), !dup && c9.quantidade() == 1
                && c9.buscarPorId(100).getPrioridade() == 2);

        // 10 - Prioridade inválida
        boolean lancou = false;
        try { new Chamado(1, "x", "y", 6); } catch (IllegalArgumentException e) { lancou = true; }
        try { new Chamado(1, "x", "y", 0); } catch (IllegalArgumentException e) { lancou &= true; }
        registrar("Prioridade inválida", "prioridades 0 e 6", lancou ? "IllegalArgumentException" : "aceitou", lancou);

        // 11 - Ordem completa de atendimento (heap sort)
        CentralAtendimento c11 = centralCom(3, 1, 5, 5, 2, 4, 3, 1, 5);
        StringBuilder sb = new StringBuilder();
        int anteriorPrio = 6; long anteriorChegada = -1; boolean ordenado = true; Chamado x;
        while ((x = c11.atenderProximo()) != null) {
            sb.append(x.getId()).append("(p").append(x.getPrioridade()).append(") ");
            if (x.getPrioridade() > anteriorPrio) ordenado = false;
            if (x.getPrioridade() == anteriorPrio && x.getOrdemChegada() < anteriorChegada) ordenado = false;
            anteriorPrio = x.getPrioridade(); anteriorChegada = x.getOrdemChegada();
        }
        registrar("Ordem completa", "9 chamados variados", sb.toString().trim(), ordenado && c11.quantidade() == 0);

        // 12 - Muitos dados com redimensionamento
        TabelaHashChamados grande = new TabelaHashChamados(7, true);
        for (int i = 0; i < 1000; i++) grande.put(i * 7, new Chamado(i * 7, "s", "d", 1));
        boolean tudo = true;
        for (int i = 0; i < 1000; i++) if (grande.get(i * 7) == null) tudo = false;
        registrar("Redimensionamento", "1000 IDs múltiplos de 7", grande.estatisticas(), tudo && grande.size() == 1000);

        // 13 - Alteração de prioridade
        CentralAtendimento c13 = centralCom(1, 3, 2);
        c13.alterarPrioridade(100, 5);
        registrar("Alterar prioridade", "ID 100 de 1 para 5", "próximo = ID " + c13.consultarProximo().getId(),
                c13.consultarProximo().getId() == 100);

        // 14 - Remoção de chave no meio da cadeia (hash)
        TabelaHashChamados th = new TabelaHashChamados();
        th.put(10, new Chamado(10, "a", "a", 1)); th.put(17, new Chamado(17, "b", "b", 1)); th.put(24, new Chamado(24, "c", "c", 1));
        th.remove(17);
        registrar("Remoção em cadeia", "remover 17 da cadeia 10->17->24",
                "10=" + th.containsKey(10) + ", 17=" + th.containsKey(17) + ", 24=" + th.containsKey(24),
                th.containsKey(10) && !th.containsKey(17) && th.containsKey(24) && th.size() == 2);

        System.out.printf("%-3s %-20s %-42s %s%n", "#", "Caso", "Entrada", "Situação");
        int i = 1;
        for (String[] r : resultados) {
            System.out.printf("%-3d %-20s %-42s %s%n", i++, r[0], r[1], r[3]);
            System.out.println("      obtido: " + r[2]);
        }
        System.out.println("\nTotal: " + resultados.size() + " | Aprovados: " + (resultados.size() - falhas) + " | Reprovados: " + falhas);
        if (falhas > 0) System.exit(1);
    }
}
