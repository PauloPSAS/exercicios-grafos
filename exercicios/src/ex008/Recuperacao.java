package ex008;

import java.util.*;

/**
 * RECUPERAÇÃO DE TRILHAS ECOLÓGICAS - KRUSKAL
 * Contexto: Parque Nacional após incêndios na Chapada Diamantina

 * IDEIA CENTRAL:
 * Após um desastre ambiental, diversas trilhas ficam bloqueadas.
 * O objetivo é reabrir apenas as trilhas necessárias para conectar
 * todos os mirantes do parque, gastando o menor número possível de
 * horas de trabalho das equipes de manutenção.

 * O algoritmo de Kruskal funciona ordenando todas as trilhas pelo
 * menor custo (horas de limpeza) e adicionando apenas aquelas que
 * não formam ciclos, produzindo uma Árvore Geradora Mínima (MST).

 * ESTRUTURA: * - 12 vértices (mirantes)
 * - 20 arestas (trilhas)
 * - Peso = horas necessárias para recuperar cada trilha
 */
public class Recuperacao {

        /**
         * Representa uma trilha entre dois mirantes.
         */
        static class Aresta implements Comparable<Aresta> {
            String origem;
            String destino;
            int horas;

            Aresta(String origem, String destino, int horas) {
                this.origem = origem;
                this.destino = destino;
                this.horas = horas;
            }

            // Ordenação crescente pelo peso.
            @Override public int compareTo(Aresta outra) {
                return Integer.compare(this.horas, outra.horas);
            }
        }

    /**
     * Estrutura Union-Find (Disjoint Set)
     * Utilizada pelo Kruskal para verificar rapidamente
     * se duas regiões já pertencem ao mesmo conjunto.
     */
    static class UnionFind {
        Map<String, String> pai = new HashMap<>();

        UnionFind(Set<String> vertices) {
            for (String v : vertices) {
                pai.put(v, v);
            }
        }

        String encontrar(String vertice) {
            if (!pai.get(vertice).equals(vertice)) {
                pai.put(vertice, encontrar(pai.get(vertice)));
            }
            return pai.get(vertice);
        }

        void unir(String a, String b) {
            String raizA = encontrar(a); String raizB = encontrar(b);
            if (!raizA.equals(raizB)) { pai.put(raizA, raizB);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("===============================================");
        System.out.println(" RECUPERAÇÃO DE TRILHAS - ALGORITMO DE KRUSKAL ");
        System.out.println("===============================================");

        List<Aresta> trilhas = new ArrayList<>();

        // Cadastro dos 12 mirantes.
        Set<String> mirantes = new HashSet<>();

        for (char c = 'A'; c <= 'L'; c++) {
            mirantes.add("Mirante_" + c);
        }

        // Aproximadamente 20 trilhas possíveis.
        trilhas.add(new Aresta("Mirante_A", "Mirante_B", 4));
        trilhas.add(new Aresta("Mirante_A", "Mirante_C", 3));
        trilhas.add(new Aresta("Mirante_B", "Mirante_D", 5));
        trilhas.add(new Aresta("Mirante_C", "Mirante_D", 2));
        trilhas.add(new Aresta("Mirante_C", "Mirante_E", 6));
        trilhas.add(new Aresta("Mirante_D", "Mirante_F", 4));
        trilhas.add(new Aresta("Mirante_E", "Mirante_F", 1));
        trilhas.add(new Aresta("Mirante_E", "Mirante_G", 5));
        trilhas.add(new Aresta("Mirante_F", "Mirante_H", 3));
        trilhas.add(new Aresta("Mirante_G", "Mirante_H", 7));
        trilhas.add(new Aresta("Mirante_G", "Mirante_I", 2));
        trilhas.add(new Aresta("Mirante_H", "Mirante_J", 4));
        trilhas.add(new Aresta("Mirante_I", "Mirante_J", 3));
        trilhas.add(new Aresta("Mirante_I", "Mirante_K", 5));
        trilhas.add(new Aresta("Mirante_J", "Mirante_L", 2));
        trilhas.add(new Aresta("Mirante_K", "Mirante_L", 4));
        trilhas.add(new Aresta("Mirante_B", "Mirante_E", 8));
        trilhas.add(new Aresta("Mirante_D", "Mirante_G", 6));
        trilhas.add(new Aresta("Mirante_F", "Mirante_I", 5));
        trilhas.add(new Aresta("Mirante_H", "Mirante_K", 7));

        // Ordena todas as trilhas pelo menor custo.
        Collections.sort(trilhas);

        UnionFind uf = new UnionFind(mirantes);

        List<Aresta> arvoreMinima = new ArrayList<>();
        int horasTotais = 0;

        /*
        * Percorre as arestas em ordem crescente.
        * Apenas adiciona uma trilha caso ela não gere ciclo.
        *
        * Complexidade:
        * O(E log E), devido à ordenação das arestas.
        */
        for (Aresta trilha : trilhas) {
            String raizOrigem = uf.encontrar(trilha.origem);
            String raizDestino = uf.encontrar(trilha.destino);
            if (!raizOrigem.equals(raizDestino)) {
                uf.unir(raizOrigem, raizDestino);
                arvoreMinima.add(trilha);
                horasTotais += trilha.horas;
                System.out.println( "Reabrindo: " + trilha.origem + " <--> "
                        + trilha.destino + " (" + trilha.horas + " horas)" );
            }
        }

        System.out.println("\n===============================================");
        System.out.println("RELATÓRIO FINAL");
        System.out.println("===============================================");
        System.out.println("Total de trilhas reabertas: " + arvoreMinima.size());
        System.out.println("Horas totais de trabalho: " + horasTotais);

        /*
        * Como existem 12 mirantes, a árvore geradora mínima
        * deverá possuir exatamente 11 trilhas.
        */
        System.out.println("Mirantes conectados: " + mirantes.size());
    }
}
