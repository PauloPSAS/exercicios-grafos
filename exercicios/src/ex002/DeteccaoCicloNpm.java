/*
 *  DETECÇÃO DE CICLOS EM DEPENDÊNCIAS NPM — DFS com 3 estados
 *  Contexto: pacotes npm podem ter dependências circulares
 *  que travam o processo de build (ex: A depende de B,
 *  B depende de C, C depende de A).
 *
 *  POR QUE 3 ESTADOS E NÃO 2?
 *  Em um grafo DIRIGIDO, "visitado" não basta.
 *  Imagine: A→B, A→C, C→B (sem ciclo, mas B é alcançado 2x).
 *  Se usarmos só "visitado/não visitado", quando chegamos em B
 *  pela segunda vez poderíamos confundi-lo como ciclo — mas não é.
 *
 *  A solução são 3 estados:
 *    BRANCO (0) = ainda não visitado
 *    CINZA  (1) = está NA pilha de chamadas atual (sendo explorado)
 *    PRETO  (2) = completamente explorado (e todos seus vizinhos também)
 *
 *  Um ciclo existe SE E SOMENTE SE encontrarmos um nó CINZA
 *  durante o DFS. Isso significa que chegamos nele novamente
 *  enquanto ainda estávamos explorando a partir dele — loop!
 *
 *  Complexidade: O(V + E) — visitamos cada vértice e aresta uma vez.
 */

import java.util.*;

public class DeteccaoCicloNpm {

    // Os 3 estados possíveis de cada nó durante o DFS
    static final int BRANCO = 0; // não visitado
    static final int CINZA  = 1; // na pilha atual (visitando)
    static final int PRETO  = 2; // totalmente explorado

    // Representação do grafo: mapa de pacote → lista de dependências
    // Ex: "express" → ["body-parser", "mime"]
    private final Map<String, List<String>> adjacencia;

    // Controle de estado de cada pacote durante o DFS
    private final Map<String, Integer> estado;

    // Guarda o ciclo encontrado para exibição no relatório
    private final List<String> caminhoDosCiclo;

    public DeteccaoCicloNpm() {
        this.adjacencia    = new HashMap<>();
        this.estado        = new HashMap<>();
        this.caminhoDosCiclo = new ArrayList<>();
    }

    /** Registra um pacote no grafo (vértice). */
    public void adicionarPacote(String pacote) {
        adjacencia.putIfAbsent(pacote, new ArrayList<>());
    }

    /**
     * Registra que 'origem' depende de 'destino' (aresta DIRIGIDA).
     * Diferente do grafo bidirecional da questão 1 — aqui a direção
     * importa: se A depende de B, não significa B depende de A.
     */
    public void adicionarDependencia(String origem, String destino) {
        adicionarPacote(origem);
        adicionarPacote(destino);
        adjacencia.get(origem).add(destino);
    }

    /**
     * Verifica se o grafo de dependências contém algum ciclo.
     * Inicia o DFS de todos os nós não visitados (necessário porque
     * o grafo pode ter componentes desconexos).
     *
     * @return true se há ciclo, false caso contrário
     */
    public boolean temCiclo() {
        // Inicializa todos os pacotes como BRANCO (não visitados)
        for (String pacote : adjacencia.keySet()) {
            estado.put(pacote, BRANCO);
        }
        caminhoDosCiclo.clear();

        // Tenta DFS a partir de cada nó ainda não visitado
        for (String pacote : adjacencia.keySet()) {
            if (estado.get(pacote) == BRANCO) {
                // Usamos uma pilha para rastrear o caminho atual
                Deque<String> pilhaCaminho = new ArrayDeque<>();
                if (dfs(pacote, pilhaCaminho)) {
                    return true; // ciclo encontrado, para imediatamente
                }
            }
        }
        return false; // nenhum ciclo em nenhum componente
    }

    /**
     * DFS recursivo com 3 estados.
     *
     * @param atual       pacote que estamos visitando agora
     * @param pilhaCaminho rastro do caminho atual (para exibir o ciclo)
     * @return true se detectou ciclo a partir deste nó
     */
    private boolean dfs(String atual, Deque<String> pilhaCaminho) {
        // Pinta o nó de CINZA: "estou visitando este agora"
        estado.put(atual, CINZA);
        pilhaCaminho.push(atual);

        // Examina cada dependência do pacote atual
        for (String dependencia : adjacencia.getOrDefault(atual, List.of())) {
            int estadoDep = estado.getOrDefault(dependencia, BRANCO);

            if (estadoDep == CINZA) {
                // CICLO ENCONTRADO!
                // Encontramos um nó que está na pilha atual — voltamos para ele.
                // Extraímos o caminho do ciclo para exibir no relatório.
                extrairCiclo(pilhaCaminho, dependencia);
                return true;
            }

            if (estadoDep == BRANCO) {
                // Ainda não visitado: continua o DFS por este caminho
                if (dfs(dependencia, pilhaCaminho)) {
                    return true; // propaga a detecção de ciclo
                }
            }
            // Se PRETO: já explorado completamente antes, sem ciclo — ignora
        }

        // Todos os vizinhos foram explorados sem ciclo
        // Pinta de PRETO: "terminei de explorar este nó"
        estado.put(atual, PRETO);
        pilhaCaminho.pop();
        return false;
    }

    /**
     * Extrai do rastro da pilha o caminho que forma o ciclo.
     * Ex: pilha = [C, B, A] e voltamos para B → ciclo é B→C→B
     */
    private void extrairCiclo(Deque<String> pilha, String inicioCiclo) {
        List<String> caminho = new ArrayList<>(pilha);
        Collections.reverse(caminho); // pilha está invertida
        int idx = caminho.indexOf(inicioCiclo);
        if (idx >= 0) {
            caminhoDosCiclo.addAll(caminho.subList(idx, caminho.size()));
            caminhoDosCiclo.add(inicioCiclo); // fecha o loop visualmente
        }
    }

    public List<String> getCaminhoDosCiclo() {
        return Collections.unmodifiableList(caminhoDosCiclo);
    }

    public static void main(String[] args) {
        System.out.println("=".repeat(55));
        System.out.println("  DETECTOR DE DEPENDÊNCIAS CIRCULARES npm");
        System.out.println("=".repeat(55));

        // EXEMPLO 1: Grafo SEM ciclo
        // react → webpack → acorn → lodash
        //       -> babel  → core-js
        System.out.println("\n--- Exemplo 1: Projeto sem dependências circulares ---");
        DeteccaoCicloNpm grafo1 = new DeteccaoCicloNpm();

        grafo1.adicionarDependencia("react",   "webpack");
        grafo1.adicionarDependencia("react",   "babel");
        grafo1.adicionarDependencia("webpack", "acorn");
        grafo1.adicionarDependencia("webpack", "lodash");
        grafo1.adicionarDependencia("babel",   "core-js");
        grafo1.adicionarDependencia("acorn",   "lodash");

        boolean resultado1 = grafo1.temCiclo();
        System.out.println("  Tem ciclo? " + resultado1);
        System.out.println("  Saída esperada: false");

        // EXEMPLO 2: Grafo COM ciclo
        // express → body-parser → qs → iconv → bytes → qs  (CICLO!)
        //         -> mime
        System.out.println("\n--- Exemplo 2: Dependência circular detectada ---");
        DeteccaoCicloNpm grafo2 = new DeteccaoCicloNpm();

        grafo2.adicionarDependencia("express",     "body-parser");
        grafo2.adicionarDependencia("express",     "mime");
        grafo2.adicionarDependencia("body-parser", "qs");
        grafo2.adicionarDependencia("body-parser", "bytes");
        grafo2.adicionarDependencia("qs",          "iconv");
        grafo2.adicionarDependencia("iconv",       "bytes");
        grafo2.adicionarDependencia("bytes",       "qs");   // fecha o ciclo: qs→iconv→bytes→qs

        boolean resultado2 = grafo2.temCiclo();
        System.out.println("  Tem ciclo? " + resultado2);
        System.out.println("  Saída esperada: true");
        if (resultado2) {
            System.out.println("  Ciclo encontrado: "
                + String.join(" → ", grafo2.getCaminhoDosCiclo()));
        }

        // EXEMPLO 3: Grafo com múltiplos componentes (1 com ciclo)
        // left-pad (isolado) + A→B→C→A (com ciclo)
        System.out.println("\n--- Exemplo 3: Múltiplos componentes, um com ciclo ---");
        DeteccaoCicloNpm grafo3 = new DeteccaoCicloNpm();

        grafo3.adicionarPacote("left-pad"); // pacote isolado sem dependências
        grafo3.adicionarDependencia("pkgA", "pkgB");
        grafo3.adicionarDependencia("pkgB", "pkgC");
        grafo3.adicionarDependencia("pkgC", "pkgA"); // A→B→C→A

        boolean resultado3 = grafo3.temCiclo();
        System.out.println("  Tem ciclo? " + resultado3);
        System.out.println("  Saída esperada: true");
        if (resultado3) {
            System.out.println("  Ciclo encontrado: "
                + String.join(" → ", grafo3.getCaminhoDosCiclo()));
        }

        System.out.println("\n" + "=".repeat(55));
        System.out.println("  RESUMO: " + (resultado1?"ERRO":"OK") +
            " | " + (resultado2?"CICLO DETECTADO":"ERRO") +
            " | " + (resultado3?"CICLO DETECTADO":"ERRO"));
        System.out.println("=".repeat(55));
    }
}
