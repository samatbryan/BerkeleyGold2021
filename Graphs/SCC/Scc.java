import java.util.*;

public class StronglyConnectedComponents {

    /**
     * Given a directed graph, returns the scc table in O(N) time using Kosaraju
     * 
     * @param graph A adjacency list graph
     * @return An integer array where each index i represents which connected
     *         component the node at i is in
     */
    public int[] scc(ArrayList<Integer>[] graph) {
        int n = graph.length;

        Stack<Integer> stack = new Stack();
        HashSet<Integer> first_visited = new HashSet();
        for (int i = 0; i < n; i++) {
            if (first_visited.contains(i))
                continue;
            first_dfs(graph, stack, first_visited, i);
        }

        HashSet<Integer> second_visited = new HashSet();
        ArrayList<Integer>[] reverse_graph = reverse(graph);
        int scc_num = 0;
        int[] scc = new int[n];

        while (stack.size() > 0) {
            int cur_node = stack.pop();
            if (second_visited.contains(cur_node))
                continue;
            second_dfs(reverse_graph, second_visited, scc, cur_node, scc_num);
            scc_num++;
        }
        return scc;

    }

    /**
     * Helper function for Kosaraju that will return the reverse of the graph
     * 
     * @param graph Adj list graph
     * @return The reverse adj list graph
     */
    public ArrayList<Integer>[] reverse(ArrayList<Integer>[] graph) {
        int n = graph.length;
        ArrayList<Integer>[] reverse_graph = new ArrayList<Integer>[n];
        for (int i = 0; i < n; i++)
            reverse_graph[i] = new ArrayList();
        for (int i = 0; i < n; i++) {
            for (int neighbor : graph[i]) {
                reverse_graph[neighbor].add(i);
            }
        }
        return reverse_graph;
    }

    /**
     * Helper function for Kosarajus. Adds every element visited to a stack using
     * DFS
     * 
     * @param graph    Adj List graph
     * @param stack    Stack for using kosarajus
     * @param visited  Set of visited nodes
     * @param cur_node The current node
     */
    public void first_dfs(ArrayList<Integer>[] graph, Stack<Integer> stack, HashSet<Integer> visited, int cur_node) {
        if (visited.contains(cur_node))
            return;
        visited.add(cur_node);
        for (int neighbor : graph[cur_node]) {
            if (visited.contains(neighbor))
                continue;
            first_dfs(graph, stack, visited, neighbor);
        }
        stack.push(cur_node);
    }

    /**
     * Second DFS iteration for Kosarajus. It starts at some node and DFS from the
     * reverse graph
     * 
     * @param reverse_graph The adj list reverse graph
     * @param visited       The visited set
     * @param scc           The scc integer array where each index i represents
     *                      which scc node i belongs to
     * @param cur_node      The current node
     * @param scc_num       The current scc number that we will fill
     */
    public void second_dfs(ArrayList<Integer>[] reverse_graph, HashSet<Integer> visited, int[] scc, int cur_node,
            int scc_num) {
        if (visited.contains(cur_node))
            return;
        visited.add(cur_node);
        scc[cur_node] = scc_num;
        for (int neighbor : reverse_graph[cur_node]) {
            if (visited.contains(neighbor))
                continue;
            second_dfs(reverse_graph, visited, scc, neighbor, scc_num);
        }
    }
}
