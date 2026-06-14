# Relatório de análise — Problema 10: Planejamento de voos com escalas e preços

## 1. Resumo da solução

O problema consiste em determinar a rota aérea de menor custo entre dois aeroportos, permitindo escalas intermediárias. Os vértices representam aeroportos e as arestas representam voos disponíveis, cujo peso corresponde ao preço da passagem.

A solução utiliza o algoritmo de Dijkstra para identificar o caminho com menor custo total, levando em consideração todas as possíveis combinações de escalas.

---

## 2. Justificativa da escolha: Dijkstra

O algoritmo de Dijkstra é apropriado para este problema pelos seguintes motivos:

**a) Busca pelo menor custo acumulado.** O objetivo não é minimizar a quantidade de escalas, mas sim o valor total pago pelo passageiro.

**b) Pesos sempre positivos.** Os preços das passagens são valores positivos, característica que atende às premissas do algoritmo.

**c) Escalas são tratadas naturalmente.** Durante a exploração do grafo, o algoritmo pode descobrir que uma sequência de voos com conexões resulta em custo menor do que um voo direto.

---

## 3. Complexidade da solução

| Métrica       | Valor                   |
|---------------|-------------------------|
| Tempo         | O((V + E) log V)        |
| Espaço        | O(V)                    |
| Vértices      | ~30 aeroportos          |
| Arestas       | ~120 voos               |
| Tipo de grafo | Ponderado e direcionado |

---

## 4. Seria possível usar outro algoritmo?

### BFS

Não é indicado, pois considera apenas o número de etapas do percurso, ignorando o preço das passagens.

### DFS

Também não resolve o problema de forma adequada, já que não garante encontrar a rota de menor custo.

### Bellman-Ford

É uma alternativa viável para cálculo de menores caminhos, porém apresenta maior custo computacional e só oferece vantagem prática quando existem pesos negativos, situação inexistente no contexto de passagens aéreas.

### Floyd-Warshall

Poderia ser utilizado caso fosse necessário conhecer previamente o menor custo entre todos os pares de aeroportos, mas seu custo O(V³) torna seu uso desnecessário para consultas individuais.

---

## 5. Comparativo de algoritmos

| Algoritmo      | Resolve o problema? | Complexidade     | Observação                               |
|----------------|---------------------|------------------|------------------------------------------|
| Dijkstra       | Sim                 | O((V + E) log V) | Melhor opção para preços positivos       |
| Bellman-Ford   | Sim                 | O(V × E)         | Mais lento e desnecessário neste cenário |
| BFS            | Não                 | O(V + E)         | Não considera preços                     |
| DFS            | Não                 | O(V + E)         | Não encontra o menor custo               |
| Floyd-Warshall | Sim                 | O(V³)            | Adequado apenas para múltiplas consultas |

---

## 6. Conclusão

O algoritmo de Dijkstra é a solução mais eficiente para o planejamento de voos com escalas quando o objetivo é minimizar o custo total da viagem. Ele considera corretamente os preços de cada trecho e pode identificar combinações de voos mais econômicas do que opções diretas. Para o cenário proposto, com aproximadamente 30 aeroportos e 120 voos, sua complexidade e desempenho tornam sua utilização plenamente justificada.
