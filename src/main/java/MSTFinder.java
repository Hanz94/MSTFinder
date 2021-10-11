import java.util.LinkedList;

/**
 * created by hansikaweerasena
 * Date: 10/11/21
 **/
public class MSTFinder {


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

        public void addEdge(int u, Edge v) {
            adjLst[u].add(v);
            if (u != v.getNodeId()) {
                adjLst[v.getNodeId()].add(new Edge(u, v.getWeight()));
            }
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

    }
}
