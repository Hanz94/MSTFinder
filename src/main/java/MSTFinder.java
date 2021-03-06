import java.util.*;

/**
 * created by hansikaweerasena
 * Date: 10/11/21
 **/
public class MSTFinder {

    public static void main(String[] args)
    {

        if (args.length > 0 && args[0].toLowerCase(Locale.ROOT).startsWith("--ex")) {
            runExperiments();
        } else if(args.length > 0 && args[0].toLowerCase(Locale.ROOT).startsWith("--val")) {
            validate();
        }else {

            Graph g = new Graph(5);
            g.print();
            g.addEdge(0, 1, 2);
            g.addEdge(0, 3, 6);
            g.addEdge(1, 2, 3);
            g.addEdge(1, 3, 8);
            g.addEdge(1, 4, 5);
            g.addEdge(2, 4, 7);
            g.addEdge(3, 4, 9);

            SpanningTree mst = generateMST(g);
            mst.print();
        }
    }

    private static void runExperiments() {

        int noOFIterations = 10;
        int[] experimentParms = new int[]{ 10,100,10000,100000,10000000,100000000};

        for (int noOfNodes :experimentParms) {
            System.out.println( "For Nodes : " + noOfNodes );
            long totalTime = 0;
            for (int i = 0; i < noOFIterations; i ++){
                Graph g = RandomGraphGenerator.getRandomGraphGenerator().generateRandomGraph(noOfNodes);
                long startTime = System.nanoTime();
                generateMST(g);
                long endTime = System.nanoTime();
                long duration = (endTime - startTime);
                totalTime = totalTime + duration;
            }
            System.out.println("Average Time : " + totalTime/noOFIterations);
        }
    }

    private static void validate(){
        int noOFIterations = 10;
        Random random = new Random();
        int noOfNodes = random.nextInt(10000);
        for (int i = 0; i < noOFIterations; i ++){
            System.out.println( "For Nodes : " + noOfNodes );
            Graph g = RandomGraphGenerator.getRandomGraphGenerator().generateRandomGraph(noOfNodes);
            SpanningTree mst = generateMST(g);
            if(mst.getTotalWeight() == g.getMstWeight()){
                System.out.println("Validated");
            }
        }
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
    private long mstWeight;

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

    public boolean haveEdge(int u, int v) {
        return adjLst[u].contains(v);
    }

    public void print() {
        System.out.println("The Graph with MST Weight : " + this.mstWeight);
        for (int i = 0; i < adjLst.length; i++) {
            System.out.print(i + " -> { ");

            LinkedList<Edge> list = adjLst[i];

            if (list.isEmpty())
                System.out.print(" No adjacent vertices ");
            else {
                int size = list.size();
                for (int j = 0; j < size; j++) {

                    System.out.print(list.get(j).getNodeId() + "(" + list.get(j).getWeight() + ")");
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

    public long getMstWeight() {
        return mstWeight;
    }

    public void setMstWeight(long mst_weight) {
        this.mstWeight = mst_weight;
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
        System.out.println("The MST \n");
        System.out.println("Edge \tWeight");
        for (int i = 1; i < parent.length; i++)
            System.out.println(parent[i] + " - " + i + "\t" + key[i]);
    }

    public int getTotalWeight(){
        {
            int sum = 0;
            for (int i = 0; i < key.length; i++)
                sum += key[i];
            return sum;
        }
    }
}

class RandomGraphGenerator {

    private Random random;
    private static RandomGraphGenerator randomGraphGenerator;
    private final int MAX_WEIGHT = 20;

    public static RandomGraphGenerator getRandomGraphGenerator() {
        if (randomGraphGenerator == null) {
            randomGraphGenerator = new RandomGraphGenerator();
        }
        return randomGraphGenerator;
    }

    private RandomGraphGenerator() {
        random = new Random();
    }

    public Graph generateRandomGraph(int noOfNodes){
        Graph graph = generateRandomTree(noOfNodes);
        int additionalVertices = 1;
        if(noOfNodes > 5){
            additionalVertices = random.nextInt(8 ) + 1;
        }
        while(additionalVertices > 0){
            int u = random.nextInt(noOfNodes);
            int v = random.nextInt(noOfNodes);
            if( u != v && !graph.haveEdge(u,v)){
                int weight = random.nextInt(MAX_WEIGHT + 1) + MAX_WEIGHT;
                graph.addEdge(u,v,weight);
                additionalVertices --;
            }
        }
        return graph;
    }

    private int computeMaxEdges(int noOfNodes) {
        return (noOfNodes * (noOfNodes - 1)) / 2;
    }

    // Function to Generate Random Tree using Prufer Sequence
    // Taken from https://www.geeksforgeeks.org/random-tree-generator-using-prufer-sequence-with-examples/ and modified
    private Graph generateRandomTree(int n)
    {
        Graph graph = new Graph(n);

        int length = n - 2;
        int[] arr = new int[length];

        // Loop to Generate Random Array
        for (int i = 0; i < length; i++) {
            arr[i] = random.nextInt(length + 1) + 1;
        }
        long mstWeight = generateTreeEdges(arr, length, graph);
        graph.setMstWeight(mstWeight);
        return graph;
    }

    private long generateTreeEdges(int prufer[], int m, Graph graph)
    {
        long mst_weight = 0;
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
//                    System.out.print("(" + (j) + ", "
//                            + (prufer[i] - 1) + ") ");
                    int weight = random.nextInt(MAX_WEIGHT) + 1;
                    graph.addEdge(j, prufer[i] - 1, weight);
                    mst_weight +=  weight;
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
//                System.out.print("(" + (i) + ", ");
                j++;
            }
            else if (vertex_set[i] == 0 && j == 1) {
//                System.out.print((i) + ")\n");
                temp_v = i;
            }
        }
        if(temp_u >= 0 && temp_v >= 0){
            int weight = random.nextInt(MAX_WEIGHT) + 1;
            graph.addEdge(temp_u,temp_v, weight);
            mst_weight +=  weight;
        }
        return mst_weight;
    }
}