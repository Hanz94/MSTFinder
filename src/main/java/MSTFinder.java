import java.util.LinkedList;
import java.util.Random;

/**
 * created by hansikaweerasena
 * Date: 10/11/21
 **/
public class MSTFinder {

    public static void main(String[] args)
    {

        Graph g = new Graph(5);
        g.addEdge(0,1,2);
        g.addEdge(0, 3, 6);
        g.addEdge(1,2,3);
        g.addEdge(1,3,8);
        g.addEdge(1,4,5);
        g.addEdge(2,4,7);
        g.addEdge(3,4,9);

        SpanningTree mst = generateMST(g);
        mst.print();

        RandomGraphGenerator gg = RandomGraphGenerator.getRandomGraphGenerator();
        Graph randomGraph = gg.generateRandomTree(5);
        randomGraph.print();
    }

    private static int minKey(int[] key, Boolean[] mstSet)
    {
        int min = Integer.MAX_VALUE, min_index = -1;

        for (int v = 0; v < mstSet.length; v++)
            if (!mstSet[v] && key[v] < min) {
                min = key[v];
                min_index = v;
            }

        return min_index;
    }

    private static SpanningTree generateMST(Graph graph )
    {
        int noOfNodes = graph.getNoOfNodes();

        int[] parent = new int[ noOfNodes ];
        int[] key = new int[ noOfNodes ];
        Boolean[] mstSet = new Boolean[ noOfNodes ];

        for (int i = 0; i < noOfNodes; i++) {
            key[i] = Integer.MAX_VALUE;
            mstSet[i] = false;
        }

        // initializing first node as the root of the MST
        key[0] = 0;
        parent[0] = -1;

        for (int i = 0; i < noOfNodes - 1; i++) {

            int u = minKey(key, mstSet);
            mstSet[u] = true;

            for ( Edge e : graph.getAdjLst()[u]) {
                if (!mstSet[e.getNodeId()] && e.getWeight() < key[e.getNodeId()]) {
                    parent[e.getNodeId()] = u;
                    key[e.getNodeId()] = e.getWeight();
                }
            }
        }

        return new SpanningTree(parent, key);
    }
}


class Edge {

    private int nodeId;
    private int weight;

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Edge(int v, int weight) {
        this.nodeId = v;
        this.weight = weight;
    }
}


class Graph {

    private int noOfNodes;
    private LinkedList<Edge> adjLst[];

    public Graph(int noOfNodes) {
        this.noOfNodes = noOfNodes;
        adjLst = new LinkedList[noOfNodes];
        for (int i = 0; i < noOfNodes; ++i)
            adjLst[i] = new LinkedList();
    }

    public void addEdge(int u, int v, int weight) {
        adjLst[u].add(new Edge(v, weight));
        adjLst[v].add(new Edge(u, weight));
    }

    public void print() {
        System.out.println("The Graph");
        for (int i = 0; i < adjLst.length; i++) {
            System.out.print(i + " -> { ");

            LinkedList<Edge> list = adjLst[i];

            if (list.isEmpty())
                System.out.print(" No adjacent vertices ");
            else {
                int size = list.size();
                for (int j = 0; j < size; j++) {

                    System.out.print(list.get(j).getNodeId());
                    if (j < size - 1)
                        System.out.print(" , ");
                }
            }

            System.out.println("}");
        }
    }

    public int getNoOfNodes() {
        return noOfNodes;
    }

    public LinkedList<Edge>[] getAdjLst() {
        return adjLst;
    }
}


class SpanningTree {

    private int[] parent;
    private int[] key;

    public SpanningTree(int[] parent, int[] key) {
        this.parent = parent;
        this.key = key;
    }

    public int[] getParent() {
        return parent;
    }

    public void setParent(int[] parent) {
        this.parent = parent;
    }

    public int[] getKey() {
        return key;
    }

    public void setKey(int[] key) {
        this.key = key;
    }

    public void print()
    {
        System.out.println("Edge \tWeight");
        for (int i = 1; i < parent.length; i++)
            System.out.println(parent[i] + " - " + i + "\t" + key[i]);
    }
}

class RandomGraphGenerator {

    private Random random;
    private static RandomGraphGenerator randomGraphGenerator;

    public static RandomGraphGenerator getRandomGraphGenerator() {
        if (randomGraphGenerator == null) {
            randomGraphGenerator = new RandomGraphGenerator();
        }
        return randomGraphGenerator;
    }

    private RandomGraphGenerator() {
        random = new Random();
    }

    // Function to Generate Random Tree using Prufer Sequence
    // Taken from https://www.geeksforgeeks.org/random-tree-generator-using-prufer-sequence-with-examples/ and modified
    public Graph generateRandomTree(int n)
    {
        Graph graph = new Graph(n);
        int length = n - 2;
        int[] arr = new int[length];

        // Loop to Generate Random Array
        for (int i = 0; i < length; i++) {
            arr[i] = random.nextInt(length + 1) + 1;
        }
        generateTreeEdges(arr, length, graph);
        return graph;
    }

    private void generateTreeEdges(int prufer[], int m, Graph graph)
    {
        int vertices = m + 2;
        int vertex_set[] = new int[vertices];

        // Initialize the array of vertices
        for (int i = 0; i < vertices; i++)
            vertex_set[i] = 0;

        // Number of occurrences of vertex in code
        for (int i = 0; i < vertices - 2; i++)
            vertex_set[prufer[i] - 1] += 1;

        int j = 0;

        // Find the smallest label not present in
        // prufer[].
        for (int i = 0; i < vertices - 2; i++) {
            for (j = 0; j < vertices; j++) {

                // If j+1 is not present in prufer set
                if (vertex_set[j] == 0) {

                    // Remove from Prufer set and print
                    // pair.
                    vertex_set[j] = -1;
                    graph.addEdge(j, prufer[i] - 1, 1);
                    vertex_set[prufer[i] - 1]--;

                    break;
                }
            }
        }

        j = 0;

        int temp_u = -1;
        int temp_v = -1;
        // For the last element
        for (int i = 0; i < vertices; i++) {
            if (vertex_set[i] == 0 && j == 0) {
                temp_u = i;
                j++;
            }
            else if (vertex_set[i] == 0 && j == 1) {
                temp_v = i;
            }
        }
        if(temp_u > 0 && temp_v >0){
            graph.addEdge(temp_u,temp_v, 1);
        }
    }
}