package src.ex006;

import java.util.*;

public class Luz {
    // Definição do número fixo de vértices (n) no grafo
    static final int NUM_COMUNIDADES = 20;

    // Mapeamento de IDs (0 a 19) para o nome das comunidades isoladas/ribeirinhas
    static final String[] COMUNIDADES = {
            "Vila Açaí",                // 0
            "Comunidade Igarapé",       // 1
            "Ribeirão das Antas",       // 2
            "Sítio Beira-Rio",          // 3
            "Ilha do Marajó Norte",     // 4
            "Ilha do Marajó Sul",       // 5
            "Canoa Quebrada do Meio",   // 6
            "Porto Velho Ribeirinho",   // 7
            "Várzea Grande",            // 8
            "São Francisco do Rio",     // 9
            "Vila das Pedras",          // 10
            "Barco Velho",              // 11
            "Poço Fundo",               // 12
            "Serra do Rio Azul",        // 13
            "Cachoeira Bonita",         // 14
            "Barreiro do Riacho",       // 15
            "Arroio Feitosa",           // 16
            "Lagoa da Esperança",       // 17
            "Ressaca do Lago",          // 18
            "Foz do Igarapé Grande",    // 19
    };

    static final int[][] ARESTAS = {
            //  u,  v, custo (mil R$)
            {  0,  1,  8 }, {  0,  3, 14 }, {  0,  4, 25 }, {  1,  2, 11 },
            {  1,  3,  9 }, {  1,  6, 17 }, {  2,  3, 12 }, {  2,  7, 22 },
            {  3,  5, 30 }, {  3,  8, 16 }, {  4,  5,  7 }, {  4,  9, 18 },
            {  5,  9, 21 }, {  5, 10, 28 }, {  6,  7, 10 }, {  6, 11, 13 },
            {  7,  8, 15 }, {  7, 12, 19 }, {  8,  9, 24 }, {  8, 13, 20 },
            {  9, 10, 26 }, {  9, 14, 23 }, { 10, 11, 32 }, { 10, 15, 27 },
            { 11, 12,  6 }, { 11, 16, 14 }, { 12, 13, 18 }, { 12, 17, 22 },
            { 13, 14,  9 }, { 13, 18, 16 }, { 14, 15, 11 }, { 14, 19, 29 },
            { 15, 16,  8 }, { 15, 19, 33 }, { 16, 17, 12 }, { 17, 18, 10 },
            { 17, 19, 21 }, { 18, 19, 15 },

            // extra
            {  0,  6, 20 }, {  1,  7, 31 }, {  2,  8, 13 }, {  3,  9, 37 },
            {  4, 10, 41 }, {  6, 12, 24 }, {  7, 11, 20 }, { 10, 16, 19 },
            { 13, 17, 28 }
    };


    static class Aresta implements Comparable<Aresta> {
        int destino, custo;

        Aresta(int destino, int custo) {
            this.destino = destino;
            this.custo   = custo;
        }

        @Override
        public int compareTo(Aresta outro) {
            // Garante que estruturas de dados de ordenação saibam comparar duas arestas pelo custo
            return Integer.compare(this.custo, outro.custo);
        }
    }

    static void prim(int n, List<List<Aresta>> adj) {

        boolean[] inMST   = new boolean[n]; // Rastreia quem já entrou na árvore geradora
        int[]     key     = new int[n];     // Armazena a aresta de menor peso para conectar cada vértice
        int[]     parent  = new int[n];     // Rastreia quem é o pai de quem na árvore final

        // Inicializa as estruturas com Infinito e sem pais definidos
        Arrays.fill(key,    Integer.MAX_VALUE);
        Arrays.fill(parent, -1);

        // Fila de Prioridade configurada para priorizar o menor peso.
        // Armazena arrays inteiros de tamanho 2: vetor[0] = peso da aresta, vetor[1] = ID do vértice u.
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));

        // Inicializa pela Vila Açaí
        key[0] = 0;
        pq.offer(new int[]{0, 0}); // Injeta o ponto de partida na fila

        while (!pq.isEmpty()) {
            // Extrai o elemento com menor custo acumulado da fila
            int[] topo = pq.poll();
            int   u    = topo[1]; // ID do vértice atual

            // Se o vértice já foi fechado/incluído na MST em um caminho melhor, ignora o registro duplicado da fila
            if (inMST[u]) continue;
            inMST[u] = true; // Inclui oficialmente o vértice 'u'

            // Varre apenas os vizinhos diretos de 'u'
            for (Aresta a : adj.get(u)) {
                int v = a.destino;
                int w = a.custo;

                // Se o vizinho 'v' não está na árvore e o peso 'w' desse trecho for menor que a menor chave ativa de 'v'
                if (!inMST[v] && w < key[v]) {
                    key[v]    = w;   // Atualiza com o menor custo de conexão até o momento
                    parent[v] = u;   // Define 'u' como o pai/provedor oficial de 'v'
                    pq.offer(new int[]{w, v}); // Adiciona o novo caminho promissor na Fila de Prioridade
                }
            }
        }

        // Verifica se o grafo original era desconexo
        for (int v = 0; v < n; v++) {
            if (!inMST[v]) {
                System.err.println("AVISO: comunidade " + COMUNIDADES[v] + " não alcançada");
            }
        }

        // Processa a exibição dos dados coletados
        printResult(parent, key, n);
    }
    static void printResult(int[] parent, int[] key, int n) {
        // Lista temporária para reordenar o resultado final pelo valor
        List<int[]> mst = new ArrayList<>();
        for (int v = 1; v < n; v++) {
            mst.add(new int[]{parent[v], v, key[v]}); // Guarda pai, destino e custo
        }

        // Ordena a lista de forma crescente pelo custo
        mst.sort(Comparator.comparingInt(a -> a[2]));

        // Calcula a soma de todas as chaves
        int totalCusto = mst.stream().mapToInt(a -> a[2]).sum();

        // Impressão cabeçalho formatado
        System.out.printf ("  %-30s  %-25s  %6s  %n", "Origem", "Destino", "Custo");
        System.out.printf ("  %-30s  %-25s  %6s  %n", "", "", "(mil R$)");

        // Varre a lista já ordenada
        for (int[] aresta : mst) {
            String origem  = COMUNIDADES[aresta[0]];
            String destino = COMUNIDADES[aresta[1]];
            int    custo   = aresta[2];

            System.out.printf("  %-30s até %-25s  %5d  %n", origem, destino, custo);
        }

        System.out.printf ("  %1s  %1d %n", "Custo Total:", totalCusto);
    }
    static List<List<Aresta>> construirGrafo(int n, int[][] arestas) {
        List<List<Aresta>> adj = new ArrayList<>();

        // Inicializa as listas internas para cada um dos vértices
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());

        // Mapeia cada linha da matriz crua
        for (int[] e : arestas) {
            int u = e[0], v = e[1], w = e[2];
            adj.get(u).add(new Aresta(v, w)); // Adiciona a ida
            adj.get(v).add(new Aresta(u, w)); // Adiciona a volta - Caracteriza Grafo Não-Direcionado
        }
        return adj;
    }

    public static void main(String[] args) {
        int n = NUM_COMUNIDADES;

        List<List<Aresta>> adj = construirGrafo(n, ARESTAS);

        prim(n, adj);
    }
}