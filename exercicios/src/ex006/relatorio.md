# Relatório de Análise Técnica — Questão 006 (Rede de Comunidades Isoladas)

## 1. Contextualização do Problema
O objetivo do projeto da **Questão 006** é projetar uma infraestrutura de interconexão otimizada para 20 comunidades isoladas e ribeirinhas (como Vila Açaí, Comunidade Igarapé, entre outras). O problema é modelado como um **Grafo Não-Direcionado, Conexo e Esparso**, onde:
* **Vértices (V):** Representam as 20 comunidades (|V| = 20).
* **Arestas (E):** Representam os canais geográficos viáveis de ligação entre as comunidades.
* **Pesos (W):** Representam o custo de execução da obra de interconexão em milhares de reais (mil R$).

Por se tratar de uma malha onde o número de conexões viáveis é significativamente menor do que o total de conexões possíveis de um grafo completo, o problema exige uma abordagem de **Árvore Geradora Mínima (MST)** escalável para topologias esparsas.

---

## 2. Análise da Solução Implementada
A solução desenvolvida para a Questão 006 utiliza o **Algoritmo de Prim Otimizado**, combinando uma **Lista de Adjacência** com uma **Fila de Prioridade (Min-Heap)**.

### Características da Implementação:
* **Estratégia:** Gulosa (Greedy). O algoritmo expande a árvore a partir do nó inicial (Vila Açaí / ID 0), mas a seleção da aresta de menor peso é delegada para uma Fila de Prioridade (`PriorityQueue`), que mantém os caminhos candidatos ordenados pelo custo.
* **Estrutura de Dados:** Lista de adjacência dinâmica (`List<List<Aresta>>`) para armazenar o grafo, evitando o desperdício de memória, e uma estrutura de heap para extração instantânea do mínimo.
* **Complexidade Assintótica:** **O(E log V)**. A extração do vértice com a menor chave da fila leva tempo O(log V), e a atualização das chaves dos vizinhos (relaxamento) consome O(log V) para cada aresta processada.

### Justificativa de Eficiência:
Ao expandir o escopo do problema de 9 para 20 nós em uma topologia esparsa, a abordagem linear O(V²) do exercício anterior começa a demonstrar limitações estruturais. A utilização da `PriorityQueue` garante que o algoritmo avalie apenas as conexões reais existentes, ignorando os "vazios" do grafo. Além disso, a implementação incluiu um refinamento estético na saída (`printResult`), ordenando o relatório final pelo custo unitário de cada trecho através de `mst.sort()`, o que eleva a qualidade gerencial do relatório.

---

## 3. Alternativas de Solução: É possível utilizar outro algoritmo?

**Sim**, o problema pode ser solucionado por outras variantes e algoritmos de caminhos mínimos, com impactos distintos na arquitetura do código.

### Alternativa A: Algoritmo de Kruskal (Com DSU)
O Algoritmo de Kruskal foca no processamento direto das arestas de forma global, ordenando-as pelo custo e utilizando o mecanismo de conjuntos disjuntos (Union-Find) para unificar as comunidades sem fechar ciclos.

* **Complexidade:** O(E log E) devido à ordenação das arestas.
* **Viabilidade no Projeto:** **Altíssima (Excelente alternativa)**.
* **Justificativa:** No cenário da Questão 006, os dados brutos de entrada já foram fornecidos no formato de uma lista de arestas explícita (`int[][] ARESTAS`). Se o Kruskal com DSU fosse utilizado aqui, **ele eliminaria completamente a necessidade do método `construirGrafo`**, que gasta processamento alocando listas de listas e objetos `Aresta`. O Kruskal consumiria a matriz `ARESTAS` diretamente, ordenaria os dados e montaria a MST com excelente desempenho.

### Alternativa B: Algoritmo de Prim Clássico (Com Matriz de Adjacência)
Esta seria a aplicação direta do código usado na Questão 005, mapeando as 20 comunidades em uma matriz estática de 20x20 e fazendo uma busca linear pelo menor elemento.

* **Complexidade:** O(V²).
* **Viabilidade no Projeto:** **Baixa**.
* **Justificativa:** Como o grafo é esparso (muitas comunidades não possuem ligação direta entre si), uma matriz de adjacência 20x20 desperdiçaria memória armazenando dezenas de valores "zero" ou "infinito". Além disso, o laço de busca linear forçaria o processador a checar 20 posições a cada rodada, mesmo que a comunidade avaliada tivesse apenas 1 ou 2 vizinhos reais, tornando o código mecanicamente ineficiente.

---

## 4. Tabela Comparativa de Abordagens

| Algoritmo / Estrutura | Complexidade | Vantagem no Cenário Esparso | Desvantagem no Cenário Esparso | Recomendação |
| :--- | :--- | :--- | :--- | :--- |
| **Prim (Fila de Prioridade)** | O(E log V) | Varre apenas vizinhos reais; alta performance com o crescimento de arestas. | Exige a conversão dos dados brutos para Lista de Adjacência. | **Excelente (Escolha Atual)** |
| **Kruskal (DSU)** | O(E log E) | Consome a matriz de entrada `ARESTAS` nativamente, sem necessidade de adapters. | Desempenho atrelado estritamente à eficiência do algoritmo de ordenação (Sort). | **Altamente Recomendado** |
| **Prim (Matriz de Adjacência)** | O(V²) | Nenhuma para este cenário. | Desperdiça memória com posições vazias e processa buscas lineares desnecessárias. | **Inadequado** |

---

## 5. Conclusão
A migração da arquitetura para o **Algoritmo de Prim Otimizado com Min-Heap** na Questão 006 reflete uma evolução correta de engenharia de software para lidar com grafos maiores e esparsos. O algoritmo reage melhor à distribuição geográfica das comunidades ribeirinhas, onde cada ponto se conecta a poucos vizinhos próximos.

Contudo, cabe uma observação de projeto: dado que a entrada do problema foi estruturada como um array bruto de conexões (`int[][] ARESTAS`), o **Algoritmo de Kruskal** despontaria como uma solução ligeiramente mais elegante em termos de codificação, por dispensar a etapa de montagem da lista de adjacência, mantendo a mesma eficiência computacional prática.