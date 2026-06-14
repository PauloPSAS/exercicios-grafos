package ex010;

import java.util.*;

/**
 * PLANEJAMENTO DE VOOS COM ESCALAS - DIJKSTRA
 * Contexto: Companhias aéreas brasileiras (GOL/Azul)

 * IDEIA CENTRAL:
 * Um passageiro deseja viajar pagando o menor valor possível.
 * Caso não exista um voo direto barato, o algoritmo pode encontrar
 * uma rota com escalas que reduza o custo total da viagem.

 * O algoritmo de Dijkstra percorre o grafo calculando o menor custo
 * acumulado até cada aeroporto, garantindo a rota mais econômica.

 * ESTRUTURA:
 * - Grafo direcionado
 * - Vértices = aeroportos
 * - Arestas = voos disponíveis
 * - Peso = preço da passagem em reais
 */

public class PlanejamentoVoos {

    /**
     * Representa um voo para outro aeroporto.
     */
    static class Voo {
        String destino;
        int preco;

        Voo(String destino, int preco) {
            this.destino = destino;
            this.preco = preco;
        }
    }

    /**
     * Elemento utilizado na fila de prioridade.
     */
    static class Estado implements Comparable<Estado> {

        String aeroporto;
        int custo;

        Estado(String aeroporto, int custo) {
            this.aeroporto = aeroporto;
            this.custo = custo;
        }

        @Override
        public int compareTo(Estado outro) {
            return Integer.compare(this.custo, outro.custo);
        }
    }

    /**
     * Grafo de voos.
     */
    static class Grafo {

        Map<String, List<Voo>> adj = new HashMap<>();

        void adicionarAeroporto(String nome) {
            adj.putIfAbsent(nome, new ArrayList<>());
        }

        /**
         * Adiciona um voo direcionado.
         */
        void adicionarVoo(String origem, String destino, int preco) {
            adj.get(origem).add(new Voo(destino, preco));
        }

        /**
         * Executa Dijkstra para encontrar a rota mais barata.

         * Complexidade:
         * O((V + E) log V)
         */
        void menorPreco(String origem, String destino) {

            Map<String, Integer> distancia = new HashMap<>();
            Map<String, String> anterior = new HashMap<>();

            for (String aeroporto : adj.keySet()) {
                distancia.put(aeroporto, Integer.MAX_VALUE);
            }

            distancia.put(origem, 0);

            PriorityQueue<Estado> fila = new PriorityQueue<>();
            fila.offer(new Estado(origem, 0));

            while (!fila.isEmpty()) {

                Estado atual = fila.poll();

                if (atual.custo > distancia.get(atual.aeroporto))
                    continue;

                for (Voo voo : adj.get(atual.aeroporto)) {

                    int novoCusto =
                            distancia.get(atual.aeroporto) + voo.preco;

                    if (novoCusto < distancia.get(voo.destino)) {

                        distancia.put(voo.destino, novoCusto);
                        anterior.put(voo.destino, atual.aeroporto);

                        fila.offer(new Estado(voo.destino, novoCusto));
                    }
                }
            }

            List<String> rota = new ArrayList<>();

            String atual = destino;

            while (atual != null) {
                rota.add(atual);
                atual = anterior.get(atual);
            }

            Collections.reverse(rota);

            System.out.println("\n===========================================");
            System.out.println(" PLANEJAMENTO DE VIAGEM AÉREA");
            System.out.println("===========================================");

            System.out.println("Origem : " + origem);
            System.out.println("Destino: " + destino);

            System.out.print("Rota escolhida: ");

            for (int i = 0; i < rota.size(); i++) {

                System.out.print(rota.get(i));

                if (i < rota.size() - 1)
                    System.out.print(" -> ");
            }

            System.out.println();

            System.out.println("Preço total: R$ "
                    + distancia.get(destino));
        }
    }

    public static void main(String[] args) {

        System.out.println("===========================================");
        System.out.println(" SIMULAÇÃO DE COMPRA DE PASSAGENS");
        System.out.println("===========================================");

        Grafo malha = new Grafo();

        // Alguns aeroportos brasileiros
        String[] aeroportos = {
                "GRU", // São Paulo
                "CNF", // Belo Horizonte
                "BSB", // Brasília
                "SSA", // Salvador
                "REC", // Recife
                "FOR"  // Fortaleza
        };

        for (String aeroporto : aeroportos) {
            malha.adicionarAeroporto(aeroporto);
        }

        // Cadastro dos voos disponíveis
        malha.adicionarVoo("GRU", "CNF", 300);
        malha.adicionarVoo("GRU", "BSB", 450);

        malha.adicionarVoo("CNF", "SSA", 280);
        malha.adicionarVoo("CNF", "REC", 500);

        malha.adicionarVoo("BSB", "SSA", 220);
        malha.adicionarVoo("BSB", "FOR", 700);

        malha.adicionarVoo("SSA", "FOR", 250);
        malha.adicionarVoo("SSA", "REC", 180);

        malha.adicionarVoo("REC", "FOR", 150);

        // Também existe voo direto, porém mais caro
        malha.adicionarVoo("GRU", "FOR", 1200);

        // Busca a rota mais econômica
        malha.menorPreco("GRU", "FOR");
    }
}