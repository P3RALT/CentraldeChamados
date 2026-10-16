# Central Inteligente de Chamados (Tabela Hash + Heap Máximo)

Atividade prática de Estruturas de Dados (Prof. Silmar Vasconcelos). Sistema em Java que combina:

- **Tabela hash** com encadeamento separado: busca de chamado por ID, O(1) em média.
- **Heap máximo** sobre `ArrayList`: o chamado mais urgente na raiz, desempate pela ordem de chegada.

## Estrutura do projeto

```
central-chamados/
├── src/central/
│   ├── Chamado.java              # modelo (prioridade 1 a 5 validada)
│   ├── TabelaHashChamados.java   # put/get/remove/containsKey, colisões, resize, estatísticas
│   ├── MaxHeapChamados.java      # offer/peek/poll/isEmpty (+ remover por ID)
│   ├── CentralAtendimento.java   # integração das duas estruturas
│   ├── Main.java                 # programa principal (resultado esperado + demonstração)
│   ├── TestesCentral.java        # 14 testes automatizados (sem dependências)
│   └── Desafios.java             # PriorityQueue x heap próprio; ArrayList x hash
├── evidencias/                   # saídas reais das execuções
├── Respostas_Atividade_Tabelas_Hash_e_Heaps.docx   # documento de respostas
├── compilar_e_executar.sh / .bat
└── README.md
```

## Requisitos

JDK 11 ou superior (testado com JDK 21). Nenhuma biblioteca externa.

## Como compilar e executar

Linux/macOS:

```
./compilar_e_executar.sh              # Main (padrão)
./compilar_e_executar.sh TestesCentral
./compilar_e_executar.sh Desafios
```

Windows: `compilar_e_executar.bat [Main|TestesCentral|Desafios]`

Manualmente:

```
mkdir out
javac -encoding UTF-8 -d out src/central/*.java
java -Dfile.encoding=UTF-8 -cp out central.Main
```

## Decisões de projeto

- `cadastrar` devolve `false` para ID duplicado e atribui a `ordemChegada` (contador crescente).
- `buscarPorId` e `consultarProximo` devolvem `null` quando não há resultado; `atenderProximo` com fila vazia devolve `null` (sem exceção).
- `atenderProximo` remove o chamado do heap **e** da tabela, mantendo as estruturas consistentes.
- Tabela padrão com 7 posições e sem redimensionamento, para reproduzir a colisão obrigatória (10, 17, 24 → índice 3). O redimensionamento automático (fator de carga 0,75) é opcional: `new TabelaHashChamados(capacidade, true)`.
- Regra de desempate (`temMaiorPrioridade`): maior prioridade primeiro; se igual, menor `ordemChegada`.

## Complexidade

| Operação | Estrutura | Custo |
|---|---|---|
| cadastrar | hash + heap | O(1) médio + O(log n) |
| buscarPorId | hash | O(1) médio (O(n) no pior caso) |
| consultarProximo | heap | O(1) |
| atenderProximo | heap + hash | O(log n) + O(1) médio |
| alterarPrioridade | heap | O(n) (busca do elemento) + O(log n) |

## Resultados

- `TestesCentral`: 14 testes, 14 aprovados (8 da tabela da atividade + 6 de borda).
- `Desafios` (100.000 chamados): 1.000 buscas levaram ~240 ms em ArrayList e ~0 ms na tabela hash; o heap próprio e a `PriorityQueue` produzem a mesma ordem de saída.
- Desafios implementados: alteração de prioridade, redimensionamento, estatísticas de colisão, comparação com `PriorityQueue`, medição de tempo. Não implementado: menu interativo.


