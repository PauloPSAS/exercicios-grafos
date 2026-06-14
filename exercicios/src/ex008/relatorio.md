# Relatório de análise — Problema 8: Recuperação de trilhas ecológicas após desastre

## 1. Resumo da solução

O problema consiste em reconectar todos os mirantes de um parque nacional após um desastre ambiental, reabrindo apenas as trilhas necessárias e minimizando o total de horas de trabalho. O grafo é ponderado, onde os vértices representam os mirantes e as arestas representam as trilhas existentes, com pesos correspondentes às horas necessárias para sua recuperação.

A solução utiliza o algoritmo de **Kruskal**, que constrói uma Árvore Geradora Mínima (Minimum Spanning Tree - MST). Para isso, as trilhas são ordenadas pelo menor custo e adicionadas progressivamente, desde que não formem ciclos, utilizando uma estrutura Union-Find para controle dos conjuntos.

---

## 2. Justificativa da escolha: Kruskal

O algoritmo de Kruskal é adequado para este problema por três razões principais:

**a) O objetivo é minimizar o custo total de conexão.** O interesse não é encontrar um caminho entre dois pontos específicos, mas conectar todos os mirantes gastando o menor número possível de horas de trabalho.

**b) Evita ciclos automaticamente.** Ao utilizar a estrutura Union-Find, o algoritmo garante que nenhuma trilha redundante seja selecionada, evitando desperdício de recursos na recuperação.

**c) Excelente desempenho para listas de arestas.** Como o problema fornece aproximadamente 20 trilhas possíveis para 12 mirantes, ordenar as arestas e selecioná-las em ordem crescente torna a implementação simples e eficiente.

---

## 3. Complexidade da solução

| Métrica            | Valor                     |
|--------------------|---------------------------|
| Tempo              | O(E log E)                |
| Espaço             | O(V)                      |
| Vértices           | 12 mirantes               |
| Arestas            | ~20 trilhas               |
| Estrutura auxiliar | Union-Find (Disjoint Set) |

---

## 4. Seria possível usar outro algoritmo?

### Prim

**Sim.** O algoritmo de Prim também encontra uma Árvore Geradora Mínima e produziria um resultado equivalente em termos de custo total.

**Diferença prática:** Prim expande a árvore a partir de um vértice inicial, adicionando sempre a aresta de menor custo conectada ao conjunto já construído. Já Kruskal trabalha globalmente, ordenando todas as arestas antes de iniciar a seleção.

**Quando Prim seria preferível:** em grafos mais densos ou representados por matriz de adjacência, Prim costuma apresentar implementação mais conveniente e bom desempenho.

### Dijkstra

Não é apropriado para este problema. O algoritmo busca o menor caminho entre uma origem e um destino específico, enquanto aqui o objetivo é conectar todos os vértices minimizando o custo global da rede.

### BFS e DFS

Também não são indicados. Esses algoritmos realizam percursos no grafo, mas não consideram pesos nas arestas nem garantem uma solução de custo mínimo para conectar todos os vértices.

---

## 5. Comparativo de algoritmos para este problema

| Algoritmo           | Resolve o problema? | Complexidade     | Observação                                                         |
|---------------------|---------------------|------------------|--------------------------------------------------------------------|
| Kruskal (escolhido) | Sim                 | O(E log E)       | Ideal para construir a árvore geradora mínima ordenando as arestas |
| Prim                | Sim                 | O(E log V)       | Também encontra a MST com excelente desempenho                     |
| Dijkstra            | Não                 | O((V + E) log V) | Resolve menor caminho, não conexão global                          |
| BFS                 | Não                 | O(V + E)         | Ignora pesos das trilhas                                           |
| DFS                 | Não                 | O(V + E)         | Percorre o grafo, mas não minimiza custos                          |

---

## 6. Conclusão

O algoritmo de Kruskal é uma escolha adequada para o problema de recuperação das trilhas ecológicas, pois conecta todos os mirantes utilizando o menor total de horas de trabalho possível e evita ciclos automaticamente. Embora o algoritmo de Prim também pudesse ser empregado para construir uma árvore geradora mínima, Kruskal apresenta uma abordagem simples e eficiente para esse cenário, especialmente quando se trabalha com uma lista de trilhas já associadas aos seus respectivos custos.
