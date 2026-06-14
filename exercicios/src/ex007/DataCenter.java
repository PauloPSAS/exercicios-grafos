package src.ex007;

import java.util.*;

public class DataCenter {

    // Número fixo de nós (Vértices) da topologia global de nuvem
    static final int NUM_DC = 15;

    // Vetor indexado (0 a 14) que mapeia o ID numérico para a região física do Data Center
    static final String[] DATA_CENTERS = {
            "US-Leste        (Virginia)",     // ID 0
            "US-Oeste       (Oregon)",       // ID 1
            "US-Central     (Iowa)",         // ID 2
            "Canada         (Montreal)",     // ID 3
            "South America  (São Paulo)",    // ID 4
            "Europe-Oeste    (Irlanda)",     // ID 5
            "Europe-Central (Frankfurt)",    // ID 6
            "Europe-South   (Milão)",        // ID 7
            "Middle East    (Dubai)",        // ID 8
            "Africa         (Johannesburg)", // ID 9
            "Asia-South     (Mumbai)",       // ID 10
            "Asia-East      (Singapura)",    // ID 11
            "Asia-NE        (Tóquio)",       // ID 12
            "Asia-Pacific   (Sydney)",       // ID 13
            "Asia-Pacific   (Seoul)",        // ID 14
    };

    // Constantes de categorização para análise de infraestrutura
    static final String SUBMARINO  = "Submarino ";
    static final String TERRESTRE  = "Terrestre ";
    static final String HIBRIDO    = "Híbrido   ";

    // Matriz de objetos que representa o grafo. Cada linha é uma aresta: { ID_origem, ID_destino, Custo, Tipo }
    // O custo está simulado em Milhões de Dólares (USD M)
    static final Object[][] LINKS = {
            {  0,  1,   18, TERRESTRE  },  // US-East  → US-West
            {  0,  2,   12, TERRESTRE  },  // US-East  → US-Central
            {  0,  3,   15, TERRESTRE  },  // US-East  → Canada
            {  0,  4,   75, SUBMARINO  },  // US-East  → São Paulo
            {  0,  5,   95, SUBMARINO  },  // US-East  → Irlanda
            {  1,  2,   14, TERRESTRE  },  // US-West  → US-Central
            {  1, 12,   85, SUBMARINO  },  // US-West  → Tóquio
            {  1, 11,   90, SUBMARINO  },  // US-West  → Singapura
            {  2,  3,   17, TERRESTRE  },  // US-Central → Canada
            {  3,  5,   88, SUBMARINO  },  // Canada   → Irlanda
            {  4,  5,  105, SUBMARINO  },  // São Paulo → Irlanda
            {  4,  9,   98, SUBMARINO  },  // São Paulo → Johannesburg
            {  5,  6,   22, TERRESTRE  },  // Irlanda  → Frankfurt
            {  5,  7,   28, TERRESTRE  },  // Irlanda  → Milão
            {  6,  7,   19, TERRESTRE  },  // Frankfurt → Milão
            {  6,  8,   55, HIBRIDO    },  // Frankfurt → Dubai
            {  7,  8,   48, HIBRIDO    },  // Milão    → Dubai
            {  7,  9,   72, SUBMARINO  },  // Milão    → Johannesburg
            {  8,  9,   60, SUBMARINO  },  // Dubai    → Johannesburg
            {  8, 10,   35, SUBMARINO  },  // Dubai    → Mumbai
            {  9, 10,   68, SUBMARINO  },  // Johannesburg → Mumbai
            { 10, 11,   40, SUBMARINO  },  // Mumbai   → Singapura
            { 10, 12,   65, SUBMARINO  },  // Mumbai   → Tóquio
            { 11, 12,   30, SUBMARINO  },  // Singapura → Tóquio
            { 11, 13,   25, SUBMARINO  },  // Singapura → Sydney
            { 11, 14,   28, SUBMARINO  },  // Singapura → Seoul
            { 12, 13,   45, SUBMARINO  },  // Tóquio   → Sydney
            { 12, 14,   20, SUBMARINO  },  // Tóquio   → Seoul
            { 13, 14,   80, SUBMARINO  },  // Sydney   → Seoul
            {  4,  7,  115, SUBMARINO  },  // São Paulo → Milão
    };

    /**
     * Estrutura Disjoint Set Union (Union-Find).
     * Controla os componentes conectados para evitar ciclos de forma ultra eficiente.
     */
    static class DSU {
        int[] parent, rank;

        // Construtor: Inicializa cada nó como seu próprio pai (subárvores isoladas de tamanho 0)
        DSU(int n) {
            parent = new int[n];
            rank   = new int[n];
            for (int i = 0; i < n; i++) parent[i] = i;
        }

        /** * Operação FIND: Encontra o "líder"/raiz do conjunto ao qual o nó 'x' pertence.
         * Utiliza Path Halving (compressão de caminhos em dois passos) encurtando a árvore a cada busca.
         */
        int find(int x) {
            while (parent[x] != x) {
                parent[x] = parent[parent[x]]; // Faz o nó apontar para o seu "avô", achatando a estrutura
                x = parent[x];
            }
            return x;
        }

        /**
         * Operação UNION: Tenta conectar o conjunto do nó 'x' com o conjunto do nó 'y'.
         * @return true se a união foi feita com sucesso; false se eles já pertenciam ao mesmo conjunto (ciclo detectado).
         */
        boolean union(int x, int y) {
            int rx = find(x), ry = find(y);
            if (rx == ry) return false; // Se possuem a mesma raiz, fecharia um ciclo! Operação rejeitada.

            // Heurística de Union by Rank: Conecta a menor subárvore por baixo da maior raiz para manter o equilíbrio
            if (rank[rx] < rank[ry])      { parent[rx] = ry; }
            else if (rank[rx] > rank[ry]) { parent[ry] = rx; }
            else                          { parent[ry] = rx; rank[rx]++; } // Empate: decide um líder e sobe o rank dele

            return true;
        }
    }

    /**
     * Executa o Algoritmo de Kruskal para construir a MST global.
     */
    static void kruskal(int n, Object[][] links) {

        // PASSO 1: Extrair os dados da matriz de objetos para uma lista de inteiros e ordenar por custo (Greedy)
        List<int[]> arestas = new ArrayList<>();
        for (Object[] link : links) {
            arestas.add(new int[]{ (int) link[0], (int) link[1], (int) link[2] });
        }
        // Ordenação crucial para a estratégia Gulosa do Kruskal: Avaliar as mais baratas primeiro. Complexidade: O(E log E)
        arestas.sort(Comparator.comparingInt(a -> a[2]));

        // PASSO 2: Inicializar o DSU e as listas de controle
        DSU dsu = new DSU(n);
        List<int[]> mst        = new ArrayList<>(); // Armazena a infraestrutura de fibra ótica aceita
        List<int[]> descartadas = new ArrayList<>(); // Guarda links redundantes ignorados

        // Varre todas as arestas do grafo na ordem de custo crescente
        for (int[] aresta : arestas) {
            int u = aresta[0], v = aresta[1];

            // Se o DSU conseguir unir os nós sem gerar ciclos, a aresta é aceita
            if (dsu.union(u, v)) {
                mst.add(aresta);        // ✔ Inserida na MST
                if (mst.size() == n - 1) break; // Otimização: Uma MST sempre possui exatamente V - 1 arestas
            } else {
                descartadas.add(aresta); // ✖ Rejeitada porque fecharia uma malha em anel redundante
            }
        }

        // Validação de segurança: se terminou o laço e não coletamos V-1 arestas, o grafo original é fragmentado
        if (mst.size() < n - 1) {
            System.err.println("ERRO: Grafo desconexo!");
            return;
        }

        // Imprime os resultados na tela
        printResult(n, mst, descartadas, links);
    }

    /**
     * Varre a base de dados original para recuperar a string descritiva do tipo do cabo
     */
    static String getTipo(int u, int v) {
        for (Object[] link : LINKS) {
            int a = (int) link[0], b = (int) link[1];
            if ((a == u && b == v) || (a == v && b == u)) return (String) link[3];
        }
        return "?";
    }

    /**
     * Renderiza o relatório final consolidando as métricas financeiras
     */
    static void printResult(int n, List<int[]> mst, List<int[]> descartadas, Object[][] allLinks) {

        // Somatórios matemáticos via Stream API para apurar os investimentos
        int totalCusto = mst.stream().mapToInt(a -> a[2]).sum();

        // Coleta estatística de cabos usando filtros do Stream (Útil para relatórios gerenciais)
        long qtdSub   = mst.stream().filter(a -> getTipo(a[0],a[1]).equals(SUBMARINO)).count();
        long qtdTerr  = mst.stream().filter(a -> getTipo(a[0],a[1]).equals(TERRESTRE)).count();
        long qtdHib   = mst.stream().filter(a -> getTipo(a[0],a[1]).equals(HIBRIDO  )).count();

        System.out.printf ("  %-32s  %-24s  %-10s  %5s  %n", "Origem", "Destino", "Tipo", "USD M");

        // Varre a lista imprimindo o cabeamento otimizado (Nota: as arestas saem ordenadas por custo por herança do sort)
        for (int[] a : mst) {
            String orig  = DATA_CENTERS[a[0]].trim();
            String dest  = DATA_CENTERS[a[1]].trim();
            System.out.printf("  %-32s até %-24s   %10d  %n", orig, dest, a[2]);
        }
        System.out.printf ("  %1s %1d M%n", "CUSTO TOTAL:", totalCusto);
    }

    public static void main(String[] args) {
        // Inicializa o processo mapeando os 15 Data Centers mundiais
        kruskal(NUM_DC, LINKS);
    }
}