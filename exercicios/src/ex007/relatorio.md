# Relatório de Análise Técnica — Questão 007 (Rede Global de Data Centers)

## 1. Contextualização do Problema
O objetivo do projeto da **Questão 007** é desenhar uma topologia de rede global otimizada para interconectar 15 Data Centers estratégicos espalhados pelo mundo (Américas, Europa, África, Oriente Médio e Ásia-Pacífico). O problema é modelado como um **Grafo Não-Direcionado, Conexo e de Média Densidade**, onde:
* **Vértices (V):** Representam os 15 Data Centers mundiais (|V| = 15).
* **Arestas (E):** Representam as rotas físicas de fibra ótica disponíveis (|E| = 30).
* **Pesos (W):** Representam o custo estimado de lançamento e concessão do link em milhões de dólares (USD M).
* **Atributos Especiais:** Classificação física das rotas em links Terrestres, Submarinos (cabos transoceânicos) ou Híbridos.

Por se tratar de um problema de otimização de infraestrutura de alta fidelidade e custo crítico, o objetivo principal é extrair a **Árvore Geradora Mínima (MST)** para garantir conectividade global com o menor orçamento possível.

---

## 2. Análise da Solução Implementada
A solução desenvolvida para a Questão 007 utiliza o **Algoritmo de Kruskal** integrado a uma estrutura de dados de conjuntos disjuntos (**DSU — Disjoint Set Union** / *Union-Find*).

### Características da Implementação:
* **Estratégia:** Gulosa (Greedy). O algoritmo extrai todas as 30 arestas do grafo, ordena-as de forma crescente pelo custo em milhões de dólares e processa-as individualmente.
* **Otimizações no DSU:**
    * **Path Halving (Compressão de Caminhos):** No método `find`, o laço `parent[x] = parent[parent[x]]` chata a estrutura da árvore a cada busca, fazendo os nós apontarem para seus "avós".
    * **Union by Rank (União por Classificação):** No método `union`, o DSU evita o pior caso de árvores degeneradas (em linha) conectando a subárvore de menor altura sob a raiz da árvore mais alta.
* **Complexidade Assintótica:** **O(E log E)**. A ordenação inicial das arestas domina o custo do algoritmo. Graças às otimizações de *Path Halving* e *Rank*, as operações de união e busca no DSU rodam em tempo quase constante **O(E · α(V))**, onde α é a função inversa de Ackermann.

### Justificativa de Eficiência:
A escolha do Kruskal foi perfeita para a engenharia deste problema. A entrada bruta foi fornecida como uma tabela de dados desestruturada (`Object[][] LINKS`). O Kruskal tira vantagem disso porque ele não precisa de uma topologia de rede mapeada previamente (como uma Lista de Adjacência). Ele consome os registros brutos, ordena-os e toma decisões globais baseando-se estritamente nos custos dos cabos submarinos e terrestres.

---

## 3. Alternativas de Solução: É possível utilizar outro algoritmo?

**Sim**, o problema de redes globais de nuvem poderia ser resolvido utilizando as variações do Algoritmo de Prim abordadas nos exercícios anteriores.

### Alternativa A: Algoritmo de Prim Otimizado (Com Fila de Prioridade)
Esta abordagem utilizaria a estrutura construída na Questão 006, partindo de um Data Center específico (ex: US-Leste) e expandindo a rede através de uma lista de adjacência controlada por um Min-Heap (`PriorityQueue`).

* **Complexidade:** O(E log V).
* **Viabilidade no Projeto:** **Alta**.
* **Justificativa:** Computacionalmente, o Prim Otimizado teria um desempenho excelente e muito próximo ao do Kruskal para um cenário de 15 nós e 30 arestas. Contudo, para este exercício específico, o Prim exigiria que criássemos uma estrutura complexa de mapeamento para traduzir a matriz bidimensional de objetos (`Object[][]`) em uma lista de listas de adjacência. O Kruskal se mostra mais direto e elegante por processar a matriz de links de forma nativa.

### Alternativa B: Algoritmo de Prim Clássico (Com Matriz de Adjacência)
Mapeamento de toda a infraestrutura global em uma matriz estática de 15x15 posições, realizando buscas lineares O(V²) para encontrar o menor cabo conectado à rede ativa.

* **Complexidade:** O(V²).
* **Viabilidade no Projeto:** **Média**.
* **Justificativa:** Como o grafo possui 15 nós e 30 arestas, ele possui uma densidade intermediária. Uma matriz 15x15 ocuparia pouquíssimo espaço em memória e o desempenho seria instantâneo. No entanto, o algoritmo seria inflexível: se a empresa de nuvem decidisse expandir a topologia adicionando 500 novos mini Data Centers regionais pelo mundo, a matriz cresceria de forma quadrática e as buscas lineares gerariam gargalos desnecessários.

---

## 4. Tabela Comparativa de Abordagens

| Algoritmo / Estrutura | Complexidade | Vantagem no Cenário de Data Centers | Desvantagem no Cenário de Data Centers | Recomendação |
| :--- | :--- | :--- | :--- | :--- |
| **Kruskal (DSU com Rank/Halving)** | O(E log E) | Consome a tabela de dados de objetos nativamente; detecção de ciclos ultra veloz. | Depende fortemente do custo de ordenação se o número de cabos explodir. | **Ideal (Escolha Atual)** |
| **Prim (Fila de Prioridade)** | O(E log V) | Excelente para expansão geográfica radial a partir de um hub central. | Exige a criação de estruturas de dados intermediárias (Lista de Adjacência). | **Viável, mas gera mais código** |
| **Prim (Matriz de Adjacência)** | O(V²) | Simples de codificar para instâncias estáticas e controladas. | Desperdiça memória e carece de escalabilidade para redes dinâmicas. | **Secundário** |

---

## 5. Conclusão
O uso do **Algoritmo de Kruskal com DSU** para a Questão 007 é a solução mais refinada do ponto de vista de arquitetura de dados. Ao implementar o *Path Halving* e o *Union by Rank*, o código atingiu o estado da arte em termos de otimização de conjuntos disjuntos.

O Kruskal provou ser a ferramenta correta para o problema por alinhar a natureza dos dados de entrada (uma lista linear de cabos submarinos e terrestres) com a mecânica do algoritmo, resultando em um código enxuto, sem estruturas redundantes de acoplamento e altamente escalável para topografias mundiais de infraestrutura de TI.