package ex009;

import java.util.*;

/**
 * ROTA MAIS ECONÔMICA PARA ENTREGAS - DIJKSTRA
 * Contexto: iFood em São Paulo

 * IDEIA CENTRAL:
 * Um entregador precisa sair de um restaurante e chegar ao cliente
 * gastando o menor tempo possível. Cada rua possui um custo em
 * minutos, considerando distância, trânsito, semáforos e subidas.

 * O algoritmo de Dijkstra encontra o caminho de menor custo em
 * grafos ponderados com pesos positivos.

 * ESTRUTURA:
 * - Grafo direcionado
 * - Vértices representam cruzamentos
 * - Arestas representam ruas
 * - Peso = tempo médio para percorrer a rua
 */
public class RotaIfood {

    // Representa uma rua ligando dois cruzamentos.
    static class Aresta {
        String destino;
        int tempo;
        Aresta(String destino, int tempo) {
            this.destino = destino;
            this.tempo = tempo;
        }
    }

    // Estrutura usada na fila de prioridade.
    static class No implements Comparable<No> {

        String vertice;
        int distancia;

        No(String vertice, int distancia) {
            this.vertice = vertice;
            this.distancia = distancia;
        }

        @Override
        public int compareTo(No outro) {
            return Integer.compare(this.distancia, outro.distancia);
        }
    }

    // Grafo das ruas.
    static class Grafo {
        Map<String, List<Aresta>> adj = new HashMap<>();

        void adicionarCruzamento(String nome) {
            adj.putIfAbsent(nome, new ArrayList<>());
        }

        /**
         * Adiciona uma rua.
         * Caso seja mão dupla, basta adicionar outra aresta invertida.
         */
        void adicionarRua(String origem, String destino, int minutos) {
            adj.get(origem).add(new Aresta(destino, minutos));
        }

        /**
         * Executa Dijkstra.

         * Complexidade:
         * O((V + E) log V)
         */
        void menorRota(String origem, String destino) {
            Map<String, Integer> distancia = new HashMap<>();
            Map<String, String> anterior = new HashMap<>();

            for (String v : adj.keySet()) {
                distancia.put(v, Integer.MAX_VALUE);
            }

            distancia.put(origem, 0);

            PriorityQueue<No> fila = new PriorityQueue<>();
            fila.add(new No(origem, 0));

            while (!fila.isEmpty()) {
                No atual = fila.poll();

                if (atual.distancia > distancia.get(atual.vertice))
                    continue;

                for (Aresta rua : adj.get(atual.vertice)) {
                    int novoTempo = distancia.get(atual.vertice) + rua.tempo;

                    if (novoTempo < distancia.get(rua.destino)) {
                        distancia.put(rua.destino, novoTempo);
                        anterior.put(rua.destino, atual.vertice);

                        fila.add(new No(rua.destino, novoTempo));
                    }
                }
            }

            List<String> caminho = new ArrayList<>();

            String atual = destino;

            while (atual != null) {
                caminho.add(atual);
                atual = anterior.get(atual);
            }

            Collections.reverse(caminho);

            System.out.println("\n=======================================");
            System.out.println(" MELHOR ROTA PARA ENTREGA");
            System.out.println("=======================================");
            System.out.println("Origem : " + origem);
            System.out.println("Destino: " + destino);
            System.out.println("Percurso:");

            for (int i = 0; i < caminho.size(); i++) {
                System.out.print(caminho.get(i));

                if (i < caminho.size() - 1)
                    System.out.print(" -> ");
            }

            System.out.println("\nTempo total: " + distancia.get(destino) + " minutos");
        }
    }

    public static void main(String[] args) {

        System.out.println("=======================================");
        System.out.println(" SIMULAÇÃO DE ENTREGA - IFOOD");
        System.out.println("=======================================");

        Grafo mapa = new Grafo();

        // Cruzamentos
        String[] pontos = {
                "Restaurante_A",
                "Cruzamento_1",
                "Cruzamento_2",
                "Cruzamento_3",
                "Cruzamento_4",
                "Cruzamento_5",
                "Cliente_B"
        };

        for (String p : pontos) {
            mapa.adicionarCruzamento(p);
        }

        // Ruas (grafo direcionado)
        mapa.adicionarRua("Restaurante_A", "Cruzamento_1", 5);
        mapa.adicionarRua("Restaurante_A", "Cruzamento_2", 8);

        mapa.adicionarRua("Cruzamento_1", "Cruzamento_3", 4);
        mapa.adicionarRua("Cruzamento_1", "Cruzamento_4", 7);

        mapa.adicionarRua("Cruzamento_2", "Cruzamento_4", 2);

        mapa.adicionarRua("Cruzamento_3", "Cliente_B", 6);

        mapa.adicionarRua("Cruzamento_4", "Cliente_B", 3);
        mapa.adicionarRua("Cruzamento_4", "Cruzamento_5", 5);

        mapa.adicionarRua("Cruzamento_5", "Cliente_B", 2);

        // Exemplo de rua de mão dupla
        mapa.adicionarRua("Cruzamento_3", "Cruzamento_1", 4);

        mapa.menorRota("Restaurante_A", "Cliente_B");
    }
}
