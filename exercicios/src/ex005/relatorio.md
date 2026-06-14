# Relatório de Análise Técnica — Questão 005 (Projeto Nordeste Conectado)

## 1. Contextualização do Problema
O objetivo do projeto **Nordeste Conectado** consiste em interconectar as 9 capitais da Região Nordeste através de uma rede de fibra ótica. Matematicamente, o problema é modelado como um **Grafo Não-Direcionado, Conexo e Valorado**, onde:
* **Vértices (V):** Representam as 9 capitais (|V| = 9).
* **Arestas (E):** Representam as conexões físicas diretas disponíveis entre as capitais.
* **Pesos (W):** Representam a distância rodoviária em quilômetros (km) entre as cidades.

Para minimizar o custo total de implantação dos cabos de fibra ótica garantindo que todas as cidades consigam se comunicar entre si, o problema exige a descoberta de uma **Árvore Geradora Mínima (MST — Minimum Spanning Tree)**.

---

## 2. Análise da Solução Implementada
A solução desenvolvida para a Questão 005 utiliza o **Algoritmo de Prim** estruturado sobre uma **Matriz de Adjacência**.

### Características da Implementação:
* **Estratégia:** Gulosa (Greedy). O algoritmo inicia em um vértice arbitrário (São Luís / ID 0) e, a cada iteração, anexa à árvore a aresta de menor peso que conecte um vértice já pertencente à MST a um vértice ainda isolado.
* **Estrutura de Dados:** Matriz de adjacência estática de tamanho 9x9 (`int[][] DIST`) e vetores auxiliares de controle (`key[]`, `parent[]`, `inMST[]`).
* **Complexidade Assintótica:** **O(V²)**. A cada rodada, o algoritmo realiza uma varredura linear por meio do método `minKey` para encontrar o vértice com a menor chave disponível.

### Justificativa de Eficiência:
Embora a complexidade O(V²) seja ineficiente para grafos massivos, ela é **altamente eficiente e perfeitamente adequada** para este cenário específico. Como o grafo é muito pequeno (|V| = 9) e **denso** (existem conexões mapeadas entre quase todas as capitais), o custo computacional de varrer os arrays linearmente é desprezível e consome menos memória do que estruturas dinâmicas complexas.

---

## 3. Alternativas de Solução: É possível utilizar outro algoritmo?

**Sim**, é totalmente possível resolver o mesmo problema utilizando outros algoritmos clássicos de Árvore Geradora Mínima. Abaixo estão listadas as duas principais alternativas, acompanhadas de suas respectivas viabilidades e justificativas.

### Alternativa A: Algoritmo de Kruskal (Com DSU)
O Algoritmo de Kruskal foca na ordenação global das arestas em vez do crescimento a partir de um vértice inicial. Ele varre as arestas da mais barata para a mais cara, utilizando uma estrutura de dados de conjuntos disjuntos (**DSU — Disjoint Set Union**) para garantir que a inclusão de uma aresta não crie circuitos fechados (ciclos).

* **Complexidade:** O(E log E) ou O(E log V), devido ao custo de ordenação inicial das arestas.
* **Viabilidade no Projeto:** **Alta**.
* **Justificativa:** O Kruskal funcionaria perfeitamente. No entanto, para o cenário da Questão 005, onde os dados brutos foram fornecidos no formato de uma matriz de adjacência densa (9x9), o Kruskal exigiria um passo intermediário para extrair as coordenadas da matriz e transformá-las em uma lista de objetos do tipo `Aresta` antes de ordenar, gerando um overhead de código desnecessário para apenas 9 vértices. Ele passa a ser vantajoso em grafos esparsos (como o da Questão 006).

### Alternativa B: Algoritmo de Prim Otimizado (Com Fila de Prioridade / Min-Heap)
Esta abordagem modifica a forma como o vértice de menor chave é selecionado a cada iteração. Em vez de varrer o vetor `key[]` linearmente em tempo O(V), as arestas promissoras são injetadas em uma Fila de Prioridade (`PriorityQueue`), reduzindo o custo de extração do menor elemento para O(log V).

* **Complexidade:** O(E log V), utilizando uma Lista de Adjacência.
* **Viabilidade no Projeto:** **Média/Baixa**.
* **Justificativa:** Matematicamente o resultado seria idêntico. Contudo, em termos de engenharia de software, introduzir uma `PriorityQueue` e objetos de gerenciamento de nós para um grafo com apenas 9 cidades geraria um fenômeno de *overengineering* (complexidade desnecessária de código). O ganho logarítmico não se paga em instâncias pequenas, tornando o Prim com matriz (O(V²)) preferível pela simplicidade de leitura e manutenção.

---

## 4. Tabela Comparativa de Abordagens

| Algoritmo / Estrutura | Complexidade | Vantagem no Cenário Atual | Desvantagem no Cenário Atual | Recomendação |
| :--- | :--- | :--- | :--- | :--- |
| **Prim (Matriz de Adjacência)** | O(V²) | Simplicidade extrema de código; consome a matriz `DIST` nativamente. | Desempenho cai drasticamente se a rede expandir para milhares de cidades. | **Ideal (Escolha Atual)** |
| **Prim (Fila de Prioridade)** | O(E log V) | Escalável para grafos gigantescos. | Código muito verboso; overhead de alocação de objetos para instâncias pequenas. | **Overengineering** |
| **Kruskal (DSU)** | O(E log E) | Excelente para focar nas menores distâncias de forma isolada. | Exige conversão manual da matriz para lista de arestas antes do processamento. | **Viável, mas secundário** |

---

## 5. Conclusão
A escolha do **Algoritmo de Prim baseado em matriz de adjacência** para a Questão 005 foi a decisão arquitetural mais acertada. Ela demonstra o entendimento de que a eficiência de um algoritmo não deve ser julgada apenas pela sua equação assintótica no pior caso teórico, mas sim pela simetria entre a complexidade do código e a volumetria dos dados reais.

Para uma rede de infraestrutura com 9 nós, a legibilidade do código e o consumo direto da tabela de distâncias rodoviárias sobrepõem-se às vantagens estruturais de filas de prioridade ou conjuntos disjuntos.