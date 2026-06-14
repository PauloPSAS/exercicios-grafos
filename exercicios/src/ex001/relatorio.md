# Relatório de análise — Problema 1: Investigação de rede de corrupção

## 1. Resumo da solução

O problema exige listar todos os vértices alcançáveis a partir de um nó de origem em um grafo não ponderado, não direcionado, com 50 vértices e ~200 arestas. A solução utiliza DFS iterativo implementado em Python, com lista de adjacência como estrutura do grafo e conjunto (`set`) para controle de visitados.

---

## 2. Justificativa da escolha: DFS

O DFS é adequado para este problema por três razões principais:

**a) O objetivo é alcançabilidade, não menor caminho.** Queremos saber quem está conectado ao investigado, não qual o caminho mais curto até eles. O DFS resolve isso em O(V + E) sem custo adicional de ordenação ou fila de prioridade.

**b) O problema pede exploração completa.** O DFS vai até o fundo de cada ramificação antes de voltar — isso mapeia cadeias de intermediários de forma natural (doleiro → empreiteira → laranja → empresa de fachada).

**c) Implementação simples e eficiente.** Para grafos da escala proposta (50 vértices, ~200 arestas), DFS tem complexidade O(V + E) = O(50 + 200) = O(250), praticamente instantâneo.

---

## 3. Complexidade da solução

| Métrica | Valor |
|---|---|
| Tempo | O(V + E) |
| Espaço | O(V) |
| Vértices | 50 |
| Arestas | ~200 |
| Componente alcançado | 49/50 nós |

---

## 4. Seria possível usar outro algoritmo?

Sim, e vale analisar as alternativas:

### BFS (Busca em Largura)

**Possível? Sim.** O BFS também resolve o problema de alcançabilidade em O(V + E), com a mesma complexidade do DFS para este caso.

**Diferença prática:** o BFS visita os nós por "camadas" (vizinhos diretos primeiro, depois vizinhos dos vizinhos). No contexto da investigação, isso significaria descobrir primeiro todos os contatos diretos do investigado, depois os contatos dos contatos — uma abordagem mais "por graus de separação".

**Quando BFS seria preferível:** se o enunciado pedisse também o menor número de "saltos" entre o investigado e cada envolvido (grau de separação na rede), o BFS já fornece essa informação de forma natural. O DFS não.

**Quando DFS é melhor:** quando a memória é limitada, pois o BFS precisa manter toda uma "camada" na fila simultaneamente, enquanto o DFS mantém apenas o caminho atual na pilha.

### Componentes Conexos (Union-Find / Disjoint Set Union)

**Possível? Sim, mas excessivo para este caso.** O algoritmo Union-Find identifica todos os componentes conexos do grafo de uma vez, em O(V × α(V)) — quase O(V). Seria útil se a pergunta fosse "quantos grupos distintos existem na rede?" ou se fosse necessário processar muitas consultas diferentes. Para uma única origem, DFS é mais direto.

### Dijkstra / Bellman-Ford

**Não aplicável aqui.** Esses algoritmos calculam o menor caminho ponderado entre vértices. Como o grafo deste problema não tem pesos nas arestas e a pergunta é apenas de alcançabilidade (não de distância), usá-los seria um desperdício computacional — Dijkstra tem complexidade O((V + E) log V), pior que o DFS para este caso.

---

## 5. Comparativo de algoritmos para este problema

| Algoritmo | Resolve o problema? | Complexidade | Vantagem adicional |
|---|---|---|---|
| DFS ✅ (escolhido) | Sim | O(V + E) | Simples, pouco uso de memória |
| BFS | Sim | O(V + E) | Fornece grau de separação |
| Union-Find | Sim (indireto) | O(V × α(V)) | Útil para múltiplas consultas |
| Dijkstra | Não (excessivo) | O((V+E) log V) | Desnecessário sem pesos |

---

## 6. Conclusão

O DFS é a escolha mais adequada e direta para o problema proposto. Ele resolve a questão de alcançabilidade com complexidade ótima, sem overhead desnecessário. A única alternativa igualmente válida seria o BFS, que teria a vantagem adicional de identificar os graus de separação entre o investigado e cada envolvido — informação útil em investigações reais para priorizar alvos mais próximos da origem.

Para grafos muito maiores (milhares de vértices), o BFS poderia ser preferível por ser mais previsível em uso de memória, mas para 50 vértices a diferença é irrelevante.
