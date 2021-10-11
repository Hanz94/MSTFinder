import java.util.LinkedList;

/**
 * created by hansikaweerasena
 * Date: 10/11/21
 **/
public class MSTFinder {

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

    private static MST generateMST(Graph graph )
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

        return new MST(parent, key);
    }

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

        MST mst = generateMST(g);
        mst.print();
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


class MST{

    private int[] parent;
    private int[] key;

    public MST(int[] parent, int[] key) {
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