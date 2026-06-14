package src.ex005;

import java.util.*;

public class Nordeste {

    // Vetor que mapeia o ID numérico para cada nome.
    static final String[] CAPITAIS = {
            "São Luís",
            "Teresina",
            "Fortaleza",
            "Natal",
            "João Pessoa",
            "Recife",
            "Maceió",
            "Aracaju",
            "Salvador"
    };

    // DIST[i][j] indica a distância direta entre a capital i e a capital j. O valor 0 indica a própria cidade.
    static final int[][] DIST = {
            { 0, 447, 1071, 1526, 1686, 1673, 1898, 2106, 2495 },    // Distâncias a partir de São Luís
            { 447, 0, 639, 1094, 1254, 1245, 1456, 1664, 2053 },    // Distâncias a partir de Teresina
            { 1071, 639, 0, 537, 697, 800, 1008, 1216, 1605 },      // Distâncias a partir de Fortaleza
            { 1526, 1094, 537, 0, 185, 300, 508, 716, 1105 },       // Distâncias a partir de Natal
            { 1686, 1254, 697, 185, 0, 119, 325, 533, 922 },        // Distâncias a partir de João Pessoa
            { 1673, 1245, 800, 300, 119, 0, 285, 527, 839 },        // Distâncias a partir de Recife
            { 1898, 1456, 1008, 508, 325, 285, 0, 278, 649 },       // Distâncias a partir de Maceió
            { 2106, 1664, 1216, 716, 533, 527, 278, 0, 356 },       // Distâncias a partir de Aracaju
            { 2495, 2053, 1605, 1105, 922, 839, 649, 356, 0 }       // Distâncias a partir de Salvador
    };

    static void prim(int n) {

        int[] parent = new int[n]; // Array que armazena a estrutura da árvore: parent[v] guarda o vértice pai de v
        int[] key = new int[n];    // Guarda o menor peso de aresta possível para conectar o vértice v à MST atual
        boolean[] inMST = new boolean[n];// Rastrear quais vértices já foram incluídos na MST

        // Define todas as chaves como "Infinito" e limpa a árvore de caminhos estabelecendo os pais como -1
        Arrays.fill(key, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);

        // Define São Luís como o ponto de partida do algoritmo
        key[0] = 0;

        // O laço principal precisa rodar 'n' vezes para incluir todos os 'n' vértices na árvore geradora
        for (int iter = 0; iter < n; iter++) {

            int u = minKey(key, inMST, n);

            // Inclui formalmente o vértice escolhido dentro do conjunto da Árvore Geradora Mínima
            inMST[u] = true;

            // Passo 3 (Relaxamento de Arestas): Atualiza os valores das chaves dos vizinhos do vértice 'u' recém-adicionado
            for (int v = 0; v < n; v++) {
                // Se o vértice 'v' ainda não está na MST, existe uma rota física entre 'u' e 'v' (dist != 0),
                // e a distância desse trecho for menor do que a menor distância registrada anteriormente para conectar 'v'...
                if (!inMST[v] && Nordeste.DIST[u][v] != 0 && Nordeste.DIST[u][v] < key[v]) {
                    key[v] = Nordeste.DIST[u][v];  // Atualiza a chave com o novo menor custo de conexão encontrado
                    parent[v] = u;               // Define 'u' como o provedor/pai do caminho para alcançar 'v'
                }
            }
        }

        // Exibe os caminhos de fibra ótica otimizados e calcula a quilometragem final
        printResult(parent, key, n);
    }

    static int minKey(int[] key, boolean[] inMST, int n) {
        int min = Integer.MAX_VALUE;
        int idx = -1;

        for (int v = 0; v < n; v++) {
            // Se o vértice ainda está disponível e sua chave de conexão atual for menor que o mínimo encontrado nesta rodada
            if (!inMST[v] && key[v] < min) {
                min = key[v];
                idx = v; // Guarda o ID do vértice candidato
            }
        }
        return idx;
    }

    static void printResult(int[] parent, int[] key, int n) {

        // Cabeçalho estruturado usando strings formatadas com preenchimento de espaços
        System.out.printf("%-28s  %-20s  %10s  %n",  "Trecho", "", "Dist.");

        int totalKm = 0;

        for (int v = 1; v < n; v++) {
            int u = parent[v]; // Recupera a origem do cabo
            int km = key[v];   // Recupera o peso da aresta (quilômetros consumidos)
            totalKm += km;     // Acumula no custo global do projeto

            // Origem -> Destino -> Peso da ligação
            System.out.printf(" %-28s até %-20s  %5d %n",
                    CAPITAIS[u], CAPITAIS[v], km);
        }

        // Exibe o somatório total de cabo de fibra ótica necessário
        System.out.printf("%1s  %1d km %n", "Custo Total:", totalKm);
    }

    public static void main(String[] args) {
        int n = CAPITAIS.length; // Determina a quantidade de capitais baseado no tamanho do array de nomes
        prim(n); // Inicializa a execução da Árvore Geradora Mínima
    }
}