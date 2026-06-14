# Relatório de análise — Problema 2: Detecção de ciclos em dependências npm

## 1. Resumo da solução

O problema exige verificar se um grafo **dirigido** de dependências de pacotes contém ciclo.
A solução usa DFS com 3 estados (branco/cinza/preto) implementado em Java com `HashMap`
para lista de adjacência e `ArrayDeque` como pilha de chamadas.

---

## 2. Por que 3 estados são necessários?

Esta é a diferença central em relação à questão 1. O grafo aqui é **dirigido**, o que muda
tudo para detecção de ciclos.

Com apenas 2 estados (visitado/não visitado), o algoritmo falha em grafos dirigidos:

```
A → B
A → C → B   ← B é alcançado 2 vezes, mas NÃO há ciclo
```

Se B já foi visitado e chegamos nele de novo, com 2 estados concluiríamos incorretamente que
há um ciclo. Os 3 estados resolvem isso:

- **Branco**: pacote ainda não analisado
- **Cinza**: pacote está na pilha de chamadas atual — ainda sendo explorado
- **Preto**: pacote e todas as suas dependências foram completamente exploradas

A regra é precisa: **ciclo existe se e somente se encontrarmos um nó cinza durante o DFS**.
Um nó preto já visitado não indica ciclo — ele simplesmente foi explorado por outro caminho
antes, sem loop.

---

## 3. Estruturas de dados usadas em Java

| Conceito | Python (questão 1) | Java (questão 2) |
|---|---|---|
| Grafo | `defaultdict(list)` | `HashMap<String, List<String>>` |
| Estado dos nós | `dict` com inteiros | `HashMap<String, Integer>` |
| Pilha | `list` com `append/pop` | `ArrayDeque<String>` |
| Conjunto visitados | `set()` | substituído pelos 3 estados |

---

## 4. Complexidade

| Métrica | Valor |
|---|---|
| Tempo | O(V + E) |
| Espaço | O(V) para estados + O(V) para pilha de chamadas |
| Vértices | até N = 100 pacotes |
| Para N=100 | praticamente instantâneo |

---

## 5. Seria possível usar outro algoritmo?

### Ordenação Topológica (Kahn's Algorithm — BFS)

**Possível? Sim, e é uma alternativa clássica.** O algoritmo de Kahn calcula o grau de
entrada (in-degree) de cada vértice e processa na ordem BFS. Se ao final sobrar algum
vértice não processado, há um ciclo.

**Vantagem sobre o DFS:** não usa recursão, evitando o risco de estouro de pilha em
grafos muito grandes. Também é mais intuitivo para quem vem de BFS.

**Desvantagem:** não identifica *qual* é o ciclo — apenas confirma sua existência.
O DFS com 3 estados pode extrair o caminho exato do ciclo, o que é mais útil para
ferramentas de diagnóstico real.

### DFS com 2 estados

**Não funciona para grafos dirigidos.** Gera falsos positivos confundindo múltiplos
caminhos para o mesmo nó com ciclos. Funciona apenas em grafos não-dirigidos como na
questão 1.

### Floyd-Warshall / Bellman-Ford

**Não aplicáveis.** Esses algoritmos são para caminhos mínimos em grafos ponderados.
Não detectam ciclos de forma eficiente.

---

## 6. Comparativo de algoritmos para este problema

| Algoritmo | Detecta ciclo? | Identifica o ciclo? | Complexidade |
|---|---|---|---|
| DFS 3 estados ✅ (escolhido) | Sim | Sim (extrai o caminho) | O(V + E) |
| Kahn (BFS topológico) | Sim | Não diretamente | O(V + E) |
| DFS 2 estados | Falsos positivos | — | — |
| Floyd-Warshall | Indiretamente | Não | O(V³) |

---

## 7. Diferença fundamental entre questão 1 e questão 2

| Aspecto | Questão 1 (corrupção) | Questão 2 (npm) |
|---|---|---|
| Tipo de grafo | Não-dirigido | Dirigido |
| Objetivo | Alcançabilidade | Detecção de ciclo |
| Estados necessários | 2 (visitado/não) | 3 (branco/cinza/preto) |
| Motivo | Qualquer conexão é mútua | Direção importa |

---

## 8. Conclusão

O DFS com 3 estados é a escolha padrão da indústria para detecção de ciclos em grafos
dirigidos — inclusive é o algoritmo usado internamente por ferramentas como npm, Maven e
pip ao verificar dependências. A única alternativa igualmente válida é o algoritmo de Kahn,
que tem a vantagem de evitar recursão, mas perde a capacidade de identificar o caminho
exato do ciclo sem processamento adicional.
