package spiderman; 

public class Graph {
    public int V;
    public Node[] adj; 


    public Graph (int V){
        this.V = V; 
        adj = new Node[V];
        for (int i =0;i<V; i++)
        {
            adj[i] = new Node(null, null);
        }
    }

    public void addEdge(int v, Dimension w){

        Node node = new Node(w, null);
        if (adj[v]==null){
            adj[v] = node;
        }
        else 
        {
            adj[v].addToEnd(node);

        }
    }
  

    public Node[] getArr(){
        return adj;
    }
}