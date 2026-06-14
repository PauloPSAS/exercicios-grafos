import java.util.*;

/**
 *   INVESTIGAÇÃO DE REDE DE CORRUPÇÃO - DFS (Busca em Profundidade)
 *   Contexto: Operação Lava Jato
 *
 * IDEIA CENTRAL:
 *   Imagine que você é um investigador com uma lista de suspeitos.
 *   Você começa por um nome (ex: um doleiro) e vai seguindo cada
 *   conexão o mais fundo possível — rastreando doleiros, empreiteiras
 *   e políticos — antes de voltar e checar outras ramificações.
 *   Isso é exatamente o que o DFS faz.
 *
 * ESTRUTURA DE DADOS:
 *   - Grafo representado por lista de adjacência (dicionário)
 *   - 50 vértices (pessoas/empresas)
 *   - ~200 arestas bidirecionais (transações suspeitas)
 *   - Conjunto "visitados" para não repetir nós
 */
public class Corrupcao {
    /**
     * Representa a rede de suspeitos.
     * Usamos lista de adjacência porque é eficiente para grafos
     * esparsos como este (200 arestas em 50 vértices).
     */
    static class GrafoCorrupcao {

        // HashMap com lista de vizinhos para cada nó
        // equivalente ao defaultdict(list) do Python
        Map<String, List<String>> adjacencia;
        int numVertices;

        GrafoCorrupcao() {
            this.adjacencia = new HashMap<>();
            this.numVertices = 0;
        }

        /** Registra um suspeito na rede (adiciona vértice). */
        void adicionarPessoa(String nome) {
            if (!adjacencia.containsKey(nome)) {
                adjacencia.put(nome, new ArrayList<>());
                numVertices++;
            }
        }

        /**
         * Registra uma transação suspeita entre dois envolvidos.
         * Como o grafo é NÃO DIRECIONADO (bidirecional), adicionamos
         * a conexão nos dois sentidos — se A conhece B, B conhece A.
         */
        void adicionarTransacao(String pessoaA, String pessoaB) {
            adjacencia.get(pessoaA).add(pessoaB);
            adjacencia.get(pessoaB).add(pessoaA);
        }

        /**
         * Percorre toda a rede a partir de um suspeito inicial usando DFS.
         *
         * Por que iterativo (e não recursivo)?
         * - Com 50 vértices e ~200 arestas, a recursão funcionaria,
         *   mas em grafos maiores ela pode estourar a pilha de chamadas
         *   do Python (RecursionError). O iterativo é mais robusto.
         *
         * Complexidade:
         *   - Tempo:  O(V + E) — visitamos cada vértice e aresta uma vez
         *   - Espaço: O(V)     — para o conjunto visitados e a pilha
         *
         * @return array com [Set visitados, List ordemDescoberta]
         */
        Object[] dfsIterativo(String origem) {

            // Conjunto de visitados: O(1) para inserção e busca
            Set<String> visitados = new HashSet<>();

            // Pilha explícita: simula o comportamento recursivo do DFS
            // Começamos empurrando o suspeito inicial
            Deque<String> pilha = new ArrayDeque<>();
            pilha.push(origem);

            // Lista para registrar a ordem em que os envolvidos são descobertos
            List<String> ordemDescoberta = new ArrayList<>();

            System.out.println("\n" + "=".repeat(55));
            System.out.println("  INICIANDO INVESTIGAÇÃO A PARTIR DE: " + origem);
            System.out.println("=".repeat(55));

            while (!pilha.isEmpty()) {
                // Retira o último elemento da pilha (LIFO — Last In, First Out)
                // Isso garante o comportamento "vá fundo antes de voltar"
                String suspeitoAtual = pilha.pop();

                // Se já visitamos este suspeito, ignoramos (evita loops)
                if (visitados.contains(suspeitoAtual)) {
                    continue;
                }

                // Marca como visitado e registra a descoberta
                visitados.add(suspeitoAtual);
                ordemDescoberta.add(suspeitoAtual);
                System.out.println("  → Investigando: " + suspeitoAtual);

                // Explora todos os contatos diretos do suspeito atual
                for (String contato : adjacencia.get(suspeitoAtual)) {
                    if (!visitados.contains(contato)) {
                        // Empurra na pilha para ser investigado depois
                        pilha.push(contato);
                    }
                }
            }

            return new Object[]{visitados, ordemDescoberta};
        }

        /**
         * Versão recursiva do DFS.
         *
         * A recursão funciona naturalmente como uma pilha:
         * cada chamada recursiva 'mergulha' mais fundo na rede antes
         * de retornar e explorar outros caminhos.
         *
         * ATENÇÃO: Em Python, o limite padrão de recursão é ~1000.
         * Para grafos maiores, use a versão iterativa acima.
         */
        Set<String> dfsRecursivo(String suspeito, Set<String> visitados) {
            // Na primeira chamada, inicializa o conjunto de visitados
            if (visitados == null) {
                visitados = new HashSet<>();
            }

            // Marca o nó atual como visitado
            visitados.add(suspeito);

            // Para cada vizinho não visitado, chama recursivamente
            for (String contato : adjacencia.get(suspeito)) {
                if (!visitados.contains(contato)) {
                    dfsRecursivo(contato, visitados);
                }
            }

            return visitados;
        }

        /** Imprime um relatório formatado da investigação. */
        void exibirResultado(String origem, Set<String> visitados, List<String> ordemDescoberta) {
            Set<String> naoAlcancados = new HashSet<>(adjacencia.keySet());
            naoAlcancados.removeAll(visitados);

            System.out.println("\n" + "=".repeat(55));
            System.out.println("  RELATÓRIO DE INVESTIGAÇÃO");
            System.out.println("=".repeat(55));
            System.out.println("  Origem da investigação : " + origem);
            System.out.println("  Total de envolvidos    : " + visitados.size());
            System.out.println("  Não alcançados         : " + naoAlcancados.size());

            List<String> visitadosOrdenados = new ArrayList<>(visitados);
            Collections.sort(visitadosOrdenados);
            System.out.println("\n  TODOS OS ENVOLVIDOS (" + visitados.size() + "):");
            for (int i = 0; i < visitadosOrdenados.size(); i++) {
                System.out.printf("    %2d. %s%n", i + 1, visitadosOrdenados.get(i));
            }

            if (!naoAlcancados.isEmpty()) {
                List<String> naoAlcancadosOrdenados = new ArrayList<>(naoAlcancados);
                Collections.sort(naoAlcancadosOrdenados);
                System.out.println("\n  SEM LIGAÇÃO COM A REDE INVESTIGADA:");
                for (String nome : naoAlcancadosOrdenados) {
                    System.out.println("    • " + nome);
                }
            }

            System.out.println("\n  ORDEM DE DESCOBERTA:");
            List<String> primeiros = ordemDescoberta.subList(0, Math.min(10, ordemDescoberta.size()));
            System.out.println("    " + String.join(" → ", primeiros) + "...");
            System.out.println("=".repeat(55) + "\n");
        }
    }
    /**
     * Constrói um exemplo prático fundamentado na Operação Lava Jato.
     *
     * Grupos de suspeitos:
     *   - Doleiros (intermediários financeiros)
     *   - Empreiteiras (construtoras que pagavam propinas)
     *   - Políticos (recebiam propinas ou desviavam verbas)
     *   - Empresas de fachada (laranjas para lavar dinheiro)
     *
     * Estrutura do grafo:
     *   - 50 vértices distribuídos entre esses grupos
     *   - ~200 arestas representando transações suspeitas
     *   - 1 componente isolado (suspeito sem conexão com a rede principal)
     */
    static Object[] criarRedeCorrupcao() {
        GrafoCorrupcao grafo = new GrafoCorrupcao();

        // --- Define os suspeitos por categoria ---
        String[] doleiros     = new String[8];   // A–H
        String[] empreiteiras = new String[10];  // A–J
        String[] politicos    = new String[15];  // A–O
        String[] laranjas     = new String[10];  // A–J
        String[] empresas     = new String[6];   // A–F

        for (int i = 0; i < doleiros.length;     i++) doleiros[i]     = "Doleiro_"     + (char)('A' + i);
        for (int i = 0; i < empreiteiras.length; i++) empreiteiras[i] = "Empreiteira_" + (char)('A' + i);
        for (int i = 0; i < politicos.length;    i++) politicos[i]    = "Politico_"    + (char)('A' + i);
        for (int i = 0; i < laranjas.length;     i++) laranjas[i]     = "Laranja_"     + (char)('A' + i);
        for (int i = 0; i < empresas.length;     i++) empresas[i]     = "Empresa_"     + (char)('A' + i);

        List<String> todos = new ArrayList<>();
        todos.addAll(Arrays.asList(doleiros));
        todos.addAll(Arrays.asList(empreiteiras));
        todos.addAll(Arrays.asList(politicos));
        todos.addAll(Arrays.asList(laranjas));
        todos.addAll(Arrays.asList(empresas));

        // Adiciona um suspeito isolado (sem conexão com ninguém)
        String suspeitoIsolado = "Suspeito_Isolado_X";
        todos.add(suspeitoIsolado);

        // Registra todos os vértices no grafo
        for (String pessoa : todos) {
            grafo.adicionarPessoa(pessoa);
        }

        // --- Cria as arestas (transações suspeitas) ---

        // Doleiros conectados entre si (rede de câmbio ilegal)
        for (int i = 0; i < doleiros.length - 1; i++) {
            grafo.adicionarTransacao(doleiros[i], doleiros[i + 1]);
        }

        // Doleiros conectados a empreiteiras (transferência de recursos)
        int[][] conexoesDe = {
            {0,0},{0,1},{1,2},{1,3},{2,4},{2,5},{3,6},{3,7},{4,8},{4,9},
            {5,0},{5,2},{6,4},{6,6},{7,1},{7,5}
        };
        for (int[] c : conexoesDe) {
            grafo.adicionarTransacao(doleiros[c[0]], empreiteiras[c[1]]);
        }

        // Empreiteiras conectadas a políticos (propinas por contratos)
        int[][] conexoesEp = {
            {0,0},{0,1},{1,2},{1,3},{2,4},{2,5},{3,6},{3,7},{4,8},{4,9},
            {5,10},{5,11},{6,12},{6,13},{7,14},{7,0},{8,2},{8,5},{9,8},{9,11}
        };
        for (int[] c : conexoesEp) {
            grafo.adicionarTransacao(empreiteiras[c[0]], politicos[c[1]]);
        }

        // Políticos conectados a laranjas (ocultação de patrimônio)
        int[][] conexoesPl = {
            {0,0},{1,1},{2,2},{3,3},{4,4},{5,5},{6,6},{7,7},{8,8},{9,9},
            {10,0},{11,2},{12,4},{13,6},{14,8}
        };
        for (int[] c : conexoesPl) {
            grafo.adicionarTransacao(politicos[c[0]], laranjas[c[1]]);
        }

        // Laranjas conectadas a empresas de fachada
        int[][] conexoesLe = {
            {0,0},{1,1},{2,2},{3,3},{4,4},{5,5},{6,0},{7,1},{8,3},{9,5}
        };
        for (int[] c : conexoesLe) {
            grafo.adicionarTransacao(laranjas[c[0]], empresas[c[1]]);
        }

        // Conexões aleatórias para atingir ~200 arestas (aumenta densidade)
        Random random = new Random(42); // seed fixa = resultados reproduzíveis
        List<String> todosExcIsolado = todos.subList(0, todos.size() - 1); // exclui o isolado
        int arestasExtras = 0;
        while (arestasExtras < 80) {
            String a = todosExcIsolado.get(random.nextInt(todosExcIsolado.size()));
            String b = todosExcIsolado.get(random.nextInt(todosExcIsolado.size()));
            if (!a.equals(b) && !grafo.adjacencia.get(a).contains(b)) {
                grafo.adicionarTransacao(a, b);
                arestasExtras++;
            }
        }

        return new Object[]{grafo, doleiros[0]}; // retorna grafo e o primeiro doleiro como origem
    }

    public static void main(String[] args) {

        System.out.println("\n" + "=".repeat(55));
        System.out.println("  SIMULAÇÃO: OPERAÇÃO INVESTIGAÇÃO LAVA JATO");
        System.out.println("=".repeat(55));

        // Constrói a rede de corrupção
        Object[] resultado = criarRedeCorrupcao();
        GrafoCorrupcao grafo = (GrafoCorrupcao) resultado[0];
        String origem = (String) resultado[1];

        int totalVertices = grafo.numVertices;
        int totalArestas = grafo.adjacencia.values().stream()
            .mapToInt(List::size).sum() / 2;

        System.out.println("\n  Rede carregada:");
        System.out.println("  • Suspeitos (vértices): " + totalVertices);
        System.out.println("  • Transações (arestas): " + totalArestas);
        System.out.println("  • Investigado inicial : " + origem);

        // Executa DFS iterativo
        Object[] dfsResultado = grafo.dfsIterativo(origem);
        @SuppressWarnings("unchecked")
        Set<String> visitados = (Set<String>) dfsResultado[0];
        @SuppressWarnings("unchecked")
        List<String> ordem = (List<String>) dfsResultado[1];

        // Exibe relatório
        grafo.exibirResultado(origem, visitados, ordem);

        // Demonstra também a versão recursiva (para fins didáticos)
        System.out.println("  [RECURSIVO] Verificando resultado com DFS recursivo...");
        Set<String> visitadosRec = grafo.dfsRecursivo(origem, null);
        assert visitados.equals(visitadosRec) : "ERRO: resultados divergentes!";
        System.out.println("  ✓ Ambas as versões encontram " + visitadosRec.size() + " nós. Resultados idênticos.\n");
    }
}
