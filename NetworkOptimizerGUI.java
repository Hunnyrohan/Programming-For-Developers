import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class NetworkOptimizerGUI extends JFrame {
    private GraphPanel graphPanel;
    private ArrayList<Node> nodes = new ArrayList<>();
    private ArrayList<Edge> edges = new ArrayList<>();
    private JLabel costLabel, latencyLabel;

    public NetworkOptimizerGUI() {
        setTitle("Network Topology Optimizer");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Main panel setup
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Graph visualization panel
        graphPanel = new GraphPanel();
        mainPanel.add(graphPanel, BorderLayout.CENTER);
        
        // Control panel
        JPanel controlPanel = new JPanel(new GridLayout(6, 1));
        
        JButton addNodeButton = new JButton("Add Node");
        JButton addEdgeButton = new JButton("Add Edge");
        JButton optimizeButton = new JButton("Optimize Network");
        JButton shortestPathButton = new JButton("Find Shortest Path");
        
        costLabel = new JLabel("Total Cost: 0");
        latencyLabel = new JLabel("Average Latency: 0");
        
        controlPanel.add(addNodeButton);
        controlPanel.add(addEdgeButton);
        controlPanel.add(optimizeButton);
        controlPanel.add(shortestPathButton);
        controlPanel.add(costLabel);
        controlPanel.add(latencyLabel);
        
        mainPanel.add(controlPanel, BorderLayout.EAST);
        add(mainPanel);
        
        // Event listeners
        addNodeButton.addActionListener(e -> addNode());
        addEdgeButton.addActionListener(e -> addEdge());
        optimizeButton.addActionListener(e -> optimizeNetwork());
        shortestPathButton.addActionListener(e -> findShortestPath());
        
        graphPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                graphPanel.handleClick(e);
            }
        });
    }

    // Node class
    class Node {
        int x, y;
        String id;
        boolean isServer;
        
        Node(int x, int y, String id, boolean isServer) {
            this.x = x;
            this.y = y;
            this.id = id;
            this.isServer = isServer;
        }
    }

    // Edge class
    class Edge {
        Node from, to;
        int cost, bandwidth;
        
        Edge(Node from, Node to, int cost, int bandwidth) {
            this.from = from;
            this.to = to;
            this.cost = cost;
            this.bandwidth = bandwidth;
        }
    }

    // Graph visualization panel
    class GraphPanel extends JPanel {
        Node selectedNode = null;
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Draw edges
            for (Edge edge : edges) {
                g.setColor(Color.BLACK);
                g.drawLine(edge.from.x, edge.from.y, edge.to.x, edge.to.y);
                int midX = (edge.from.x + edge.to.x) / 2;
                int midY = (edge.from.y + edge.to.y) / 2;
                g.drawString("C:" + edge.cost + " B:" + edge.bandwidth, midX, midY);
            }
            // Draw nodes
            for (Node node : nodes) {
                g.setColor(node.isServer ? Color.BLUE : Color.GREEN);
                g.fillOval(node.x - 10, node.y - 10, 20, 20);
                g.setColor(Color.BLACK);
                g.drawString(node.id, node.x - 5, node.y - 15);
            }
        }
        
        void handleClick(MouseEvent e) {
            repaint();
        }
    }

    private void addNode() {
        String id = JOptionPane.showInputDialog("Enter node ID:");
        boolean isServer = JOptionPane.showConfirmDialog(null, 
            "Is this a server?", "Node Type", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
        nodes.add(new Node(100 + nodes.size() * 50, 100, id, isServer));
        graphPanel.repaint();
    }

    private void addEdge() {
        if (nodes.size() < 2) {
            JOptionPane.showMessageDialog(this, "Need at least 2 nodes!");
            return;
        }
        
        String[] nodeIds = nodes.stream().map(n -> n.id).toArray(String[]::new);
        String from = (String)JOptionPane.showInputDialog(this, "From:", "Add Edge", 
            JOptionPane.PLAIN_MESSAGE, null, nodeIds, nodeIds[0]);
        String to = (String)JOptionPane.showInputDialog(this, "To:", "Add Edge", 
            JOptionPane.PLAIN_MESSAGE, null, nodeIds, nodeIds[1]);
        
        int cost = Integer.parseInt(JOptionPane.showInputDialog("Enter cost:"));
        int bandwidth = Integer.parseInt(JOptionPane.showInputDialog("Enter bandwidth:"));
        
        Node fromNode = nodes.stream().filter(n -> n.id.equals(from)).findFirst().get();
        Node toNode = nodes.stream().filter(n -> n.id.equals(to)).findFirst().get();
        
        edges.add(new Edge(fromNode, toNode, cost, bandwidth));
        updateMetrics();
        graphPanel.repaint();
    }

    private void optimizeNetwork() {
        // Simple MST-based optimization (Kruskal's algorithm could be implemented here)
        ArrayList<Edge> mst = new ArrayList<>();
        Collections.sort(edges, Comparator.comparingInt(e -> e.cost));
        
        // Very basic optimization - takes lowest cost edges
        Set<String> connected = new HashSet<>();
        for (Edge e : edges) {
            if (mst.size() == nodes.size() - 1) break;
            if (!connected.contains(e.from.id) || !connected.contains(e.to.id)) {
                mst.add(e);
                connected.add(e.from.id);
                connected.add(e.to.id);
            }
        }
        
        edges = mst;
        updateMetrics();
        graphPanel.repaint();
    }

    private void findShortestPath() {
        // Simple Dijkstra's algorithm implementation could go here
        JOptionPane.showMessageDialog(this, "Shortest path calculation not fully implemented yet!");
    }

    private void updateMetrics() {
        int totalCost = edges.stream().mapToInt(e -> e.cost).sum();
        double avgLatency = edges.stream().mapToDouble(e -> 1000.0 / e.bandwidth).average().orElse(0);
        
        costLabel.setText("Total Cost: " + totalCost);
        latencyLabel.setText(String.format("Average Latency: %.2f ms", avgLatency));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new NetworkOptimizerGUI().setVisible(true);
        });
    }
}