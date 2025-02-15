import java.util.*;

public class NetworkDevices {

    public static int minCostToConnectDevices(int n, int[] modules, int[][] connections) {
        // Create a list to store all edges (connections and module installations)
        List<int[]> edges = new ArrayList<>();

        // Add module installation edges
        for (int i = 0; i < n; i++) {
            edges.add(new int[]{i, n, modules[i]}); // Connect device i to super node (n) with cost modules[i]
        }

        // Add connection edges
        for (int[] connection : connections) {
            int device1 = connection[0] - 1; // Convert to 0-based index
            int device2 = connection[1] - 1; // Convert to 0-based index
            int cost = connection[2];
            edges.add(new int[]{device1, device2, cost});
        }

        // Sort edges by cost
        edges.sort((a, b) -> a[2] - b[2]);

        // Initialize Union-Find data structure
        int[] parent = new int[n + 1]; // n devices + 1 super node
        for (int i = 0; i <= n; i++) {
            parent[i] = i;
        }

        int totalCost = 0;
        int edgesUsed = 0;

        // Kruskal's algorithm to find MST
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            int cost = edge[2];

            int parentU = find(parent, u);
            int parentV = find(parent, v);

            if (parentU != parentV) {
                totalCost += cost;
                union(parent, parentU, parentV);
                edgesUsed++;
                if (edgesUsed == n) { // All devices are connected
                    break;
                }
            }
        }

        return totalCost;
    }

    private static int find(int[] parent, int node) {
        if (parent[node] != node) {
            parent[node] = find(parent, parent[node]); // Path compression
        }
        return parent[node];
    }

    private static void union(int[] parent, int u, int v) {
        parent[v] = u;
    }

    public static void main(String[] args) {
        int n = 3;
        int[] modules = {1, 2, 2};
        int[][] connections = {{1, 2, 1}, {2, 3, 1}};
        System.out.println(minCostToConnectDevices(n, modules, connections)); // Output: 3
    }
}