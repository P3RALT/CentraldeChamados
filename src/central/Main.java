package central;

public class Main {

    private static void imprimir(String titulo, Chamado c) {
        System.out.println(titulo);
        System.out.println(c == null ? "  (nenhum chamado)" : "  " + c.getId() + " - prioridade " + c.getPrioridade());
        System.out.println();
    }

    public static void main(String[] args) {
        CentralAtendimento central = new CentralAtendimento();

        System.out.println("=== Resultado esperado da atividade ===");
        Chamado[] basicos = {
            new Chamado(101, "Ana", "Sem acesso ao e-mail", 2),
            new Chamado(102, "Bruno", "Servidor fora do ar", 5),
            new Chamado(103, "Carla", "Impressora sem toner", 3)
        };
        for (Chamado c : basicos) {
            central.cadastrar(c);
            System.out.println("Cadastrar chamado: " + c.getId() + " - prioridade " + c.getPrioridade());
        }
        System.out.println();
        Chamado achado = central.buscarPorId(103);
        System.out.println("Buscar ID 103:\nChamado encontrado: " + (achado == null ? "não encontrado" : achado.getId()) + "\n");
        imprimir("Próximo chamado:", central.consultarProximo());
        imprimir("Chamado atendido:", central.atenderProximo());
        imprimir("Nova prioridade da fila:", central.consultarProximo());
        while (central.atenderProximo() != null) { /* esvazia a fila */ }

        System.out.println("=== Demonstração completa ===");
        central = new CentralAtendimento();
        Chamado[] todos = {
            new Chamado(10, "Diego", "Senha bloqueada", 2),
            new Chamado(17, "Elisa", "Wi-Fi instável", 4),
            new Chamado(24, "Fábio", "Monitor sem imagem", 2),
            new Chamado(5, "Gabi", "Banco de dados indisponível", 5),
            new Chamado(8, "Hugo", "Instalar software", 1),
            new Chamado(15, "Iara", "Sistema de pagamentos parado", 5),
            new Chamado(20, "João", "Atualização do antivírus", 3)
        };
        for (Chamado c : todos) {
            boolean ok = central.cadastrar(c);
            System.out.println("Cadastro ID " + c.getId() + ": " + (ok ? "OK" : "REJEITADO") + " | " + c);
        }
        System.out.println("\nTentativa de ID duplicado (17): "
                + (central.cadastrar(new Chamado(17, "Kleber", "Duplicado", 3)) ? "aceito (ERRO)" : "rejeitado corretamente"));

        System.out.println("\nTabela hash (7 buckets) - IDs 10, 17, 24 (e 8, 15) colidem:");
        System.out.print(central.getTabela().mostrarBuckets());
        System.out.println("Estatísticas: " + central.getTabela().estatisticas());

        System.out.println("\nBusca ID 24: " + central.buscarPorId(24));
        System.out.println("Busca ID 999: " + (central.buscarPorId(999) == null ? "chamado não encontrado" : "?"));
        System.out.println("Próximo (sem remover): " + central.consultarProximo());
        System.out.println("Próximo (de novo, não mudou): " + central.consultarProximo());

        System.out.println("\nAlterando prioridade do chamado 8 de 1 para 5...");
        central.alterarPrioridade(8, 5);

        System.out.println("\n--- Atendimento em ordem de prioridade ---");
        Chamado c;
        int pos = 1;
        while ((c = central.atenderProximo()) != null) {
            System.out.println(pos++ + "º atendido: ID " + c.getId() + " | prioridade " + c.getPrioridade()
                    + " | chegada " + c.getOrdemChegada() + " | ainda na tabela? " + (central.buscarPorId(c.getId()) != null));
        }
        System.out.println("\nFila vazia -> atenderProximo(): " + central.atenderProximo());
        System.out.println("Fila vazia -> consultarProximo(): " + central.consultarProximo());
    }
}
