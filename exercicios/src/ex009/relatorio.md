# Relatório de análise — Problema 9: Rota mais econômica para entregas do iFood em São Paulo

## 1. Resumo da solução

O problema consiste em encontrar a rota de menor tempo entre um restaurante e um cliente em um grafo direcionado, onde os vértices representam cruzamentos e as arestas representam ruas. Cada aresta possui um peso correspondente ao tempo médio de deslocamento, considerando fatores como trânsito, semáforos e distância.

A solução utiliza o algoritmo de Dijkstra com fila de prioridade para calcular o caminho de menor custo acumulado entre a origem e o destino.

---

## 2. Justificativa da escolha: Dijkstra

O algoritmo de Dijkstra é adequado para este problema por três motivos principais:

**a) O objetivo é encontrar o menor caminho ponderado.** Diferentemente de um grafo não ponderado, cada rua possui um tempo diferente de percurso. O algoritmo considera esses pesos para calcular a rota ótima.

**b) Todos os pesos são positivos.** Tempos de deslocamento nunca assumem valores negativos, condição ideal para o funcionamento do algoritmo de Dijkstra.

**c) Boa eficiência computacional.** Utilizando fila de prioridade, a complexidade é O((V + E) log V), tornando-o apropriado mesmo para grafos maiores, como o cenário proposto com aproximadamente 200 cruzamentos.

---

## 3. Complexidade da solução

| Métrica       | Valor                   |
|---------------|-------------------------|
| Tempo         | O((V + E) log V)        |
| Espaço        | O(V)                    |
| Vértices      | ~200                    |
| Arestas       | ~600                    |
| Tipo de grafo | Ponderado e direcionado |

---

## 4. Seria possível usar outro algoritmo?

### BFS (Busca em Largura)

Não é adequado para este problema. O BFS encontra o caminho com menor número de arestas, mas ignora os pesos das ruas. Assim, poderia escolher uma rota com menos cruzamentos, porém mais lenta.

### DFS (Busca em Profundidade)

Também não é indicado. O DFS apenas percorre o grafo explorando caminhos possíveis, sem garantir que o percurso encontrado seja o de menor custo.

### Bellman-Ford

Poderia resolver o problema, pois também calcula menores caminhos em grafos ponderados. Entretanto, possui complexidade O(V × E), sendo menos eficiente que Dijkstra quando todos os pesos são positivos.

### Floyd-Warshall

Seria útil apenas se fosse necessário calcular previamente as menores distâncias entre todos os pares de cruzamentos. Para uma única consulta entre origem e destino, apresenta custo computacional desnecessariamente elevado.

---

## 5. Comparativo de algoritmos

| Algoritmo      | Resolve o problema? | Complexidade     | Observação                           |
|----------------|---------------------|------------------|--------------------------------------|
| Dijkstra       | Sim                 | O((V + E) log V) | Melhor escolha para pesos positivos  |
| Bellman-Ford   | Sim                 | O(V × E)         | Mais lento, útil com pesos negativos |
| BFS            | Não                 | O(V + E)         | Ignora os pesos das ruas             |
| DFS            | Não                 | O(V + E)         | Não calcula menor caminho            |
| Floyd-Warshall | Sim                 | O(V³)            | Exagerado para uma única rota        |

---

## 6. Conclusão

O algoritmo de Dijkstra é a escolha mais adequada para o problema de roteamento de entregas do iFood. Ele encontra a rota de menor tempo considerando os diferentes custos associados às ruas e apresenta excelente desempenho para grafos desse porte. Embora existam algoritmos alternativos para cálculo de menores caminhos, nenhum oferece uma combinação tão eficiente de simplicidade e desempenho nesse cenário específico.
