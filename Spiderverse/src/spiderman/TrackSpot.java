package spiderman;
import java.util.ArrayList;

/**
 * Steps to implement this class main method:
 * 
 * Step 1:
 * DimensionInputFile name is passed through the command line as args[0]
 * Read from the DimensionsInputFile with the format:
 * 1. The first line with three numbers:
 *      i.    a (int): number of dimensions in the graph
 *      ii.   b (int): the initial size of the cluster table prior to rehashing
 *      iii.  c (double): the capacity(threshold) used to rehash the cluster table 
 * 2. a lines, each with:
 *      i.    The dimension number (int)
 *      ii.   The number of canon events for the dimension (int)
 *      iii.  The dimension weight (int)
 * 
 * Step 2:
 * SpiderverseInputFile name is passed through the command line as args[1]
 * Read from the SpiderverseInputFile with the format:
 * 1. d (int): number of people in the file
 * 2. d lines, each with:
 *      i.    The dimension they are currently at (int)
 *      ii.   The name of the person (String)
 *      iii.  The dimensional signature of the person (int)
 * 
 * Step 3:
 * SpotInputFile name is passed through the command line as args[2]
 * Read from the SpotInputFile with the format:
 * Two integers (line seperated)
 *      i.    Line one: The starting dimension of Spot (int)
 *      ii.   Line two: The dimension Spot wants to go to (int)
 * 
 * Step 4:
 * TrackSpotOutputFile name is passed in through the command line as args[3]
 * Output to TrackSpotOutputFile with the format:
 * 1. One line, listing the dimenstional number of each dimension Spot has visited (space separated)
 * 
 * @author Seth Kelley
 */

public class TrackSpot {
    
    public static void main(String[] args) {

        if ( args.length < 4 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.TrackSpot <dimension INput file> <spiderverse INput file> <spot INput file> <trackspot OUTput file>");
                return;
        }

        // WRITE YOUR CODE HERE
        Node[] adjList = adjList(args[0]);

        StdIn.setFile(args[2]);
        int start = StdIn.readInt();
        int end = StdIn.readInt();

        Dimension dim = new Dimension(start, -1, -1);
        Node node = new Node(dim, null);

        int startIndex = findIndex(node, adjList);
        ArrayList<Integer> path = new ArrayList<>(); 
        boolean[] marked = new boolean[adjList.length];
        path = dfs(startIndex, end, marked, adjList, path); 

        StdOut.setFile(args[3]);
        StdOut.print(start + " ");
        for (int i=0; i<path.size(); i++){
            if (path.get(i)!=end ){
            StdOut.print(path.get(i) + " ");
            }
            else 
            {
                StdOut.print(path.get(i) + " ");
                break;}
        }
        
    }

    

    public static ArrayList<Integer> dfs(int start, int end, boolean[] marked, Node[] adjList, ArrayList<Integer> path){
        marked[start] = true; 
        Node ptr = adjList[start].getNextNode();
        while (ptr!=null){
            
            int index = findIndex(ptr, adjList);

            if (!marked[index]){
                path.add(ptr.getDimension().getDimensionNumber());
                dfs(index, end, marked, adjList, path);
            }
            ptr = ptr.getNextNode();
        }
        return path; 
        
        
    }

    public static Node[] adjList(String inputFile){

        Node[] cluster = put(inputFile);
        connect(cluster);

        StdIn.setFile(inputFile);
        int V = StdIn.readInt(); 
        StdIn.readLine();
        Graph graph = new Graph(V);
        Node[] arr = graph.getArr();
        

        //populate adjlist with one distint dimension in each line 
        for (int i =0; i<V; i++)
        {
            Dimension dimension = new Dimension(StdIn.readInt(), StdIn.readInt(), StdIn.readInt());
            arr[i] = new Node(dimension, null);
        }

        //populate adjlist with each connection 
        for (int i=0; i<cluster.length;i++){
            Node first = cluster[i];
            Node ptr = cluster[i].getNextNode();
            int index = findIndex(first, graph.getArr());

            while(ptr!=null)
            {
                graph.addEdge(index, ptr.getDimension()); 
                int index2 = findIndex(ptr, graph.getArr());
                graph.addEdge(index2, first.getDimension());
                ptr=ptr.getNextNode();
            }
        }

        return graph.getArr();
    }


    public static int findIndex(Node node, Node[] adj)
    {
        int index=-1;
        for (int i =0; i<adj.length; i++)
        {
            if (adj[i]!=null && adj[i].getDimension().getDimensionNumber() == node.getDimension().getDimensionNumber()){
                index =i;
                break;
            }
        }
        return index;
    }

    public static Node[] connect (Node[] cluster)
    {
        Node ptr = cluster[0];
        Node ptrLast = new Node(cluster[cluster.length-1].getDimension(), null);
       
        ptr.addToEnd(ptrLast);
        Node ptr2Last = new Node(cluster[cluster.length-2].getDimension(), null);
        
        ptr.addToEnd(ptr2Last);
        cluster[0] = ptr;

        //for index 1
        Node ptr1 = cluster[1];

        Node ptr0 = new Node(cluster[0].getDimension(), null);
       
        ptr1.addToEnd(ptr0);

        ptr1.addToEnd(ptrLast);
        cluster[1] = ptr1;

        // for rest 
        for (int i= 2; i<cluster.length;i++){
            Node newPtr = cluster[i];
            Node prevPtr = new Node(cluster[i-1].getDimension(), null);
            Node prevPrevPtr = new Node(cluster[i-2].getDimension(), null);
            

            newPtr.addToEnd(prevPtr);
            newPtr.addToEnd(prevPrevPtr);
            cluster[i] = newPtr;
        }
        return cluster; 
    }

    public static Node[] put(String inputfile){
        StdIn.setFile(inputfile);

        int numOfdimension = StdIn.readInt();
        int size = StdIn.readInt();
        double capacity = StdIn.readDouble();

        Node[] clusters = new Node[size];
        int count = 0; 

        for (int i =0; i<numOfdimension;i++){
            int dimensionNumber = StdIn.readInt(); 
            int numOfCanonEvents = StdIn.readInt(); 
            int dimensionWeight = StdIn.readInt();
            
            Dimension dimension = new Dimension(dimensionNumber, numOfCanonEvents, dimensionWeight);
            Node node = new Node(dimension, null);

            int index = dimensionNumber%clusters.length;
           
            
            if (clusters[index]==null)
            {
                clusters[index] = node; 
                count++;
            }
            else 
            {
                node.setNextNode(clusters[index]);
                clusters[index] = node;
                count++;
            }

            if ((double)count/clusters.length>=capacity)
            {
                clusters = rehash(clusters);
            }
        }
        return clusters;
    }

    public static Node[] rehash(Node[] clusters)
    {
        int size = 2 * clusters.length;
        Node[] newClusters = new Node[size];

        for (int i=0; i<clusters.length;i++)
        {

            Node ptr = clusters[i];
            
            
            while (ptr!=null){
            int index = ptr.getDimension().getDimensionNumber()%size; 
            Node ptr2 = new Node(ptr.getDimension(), null);
            if (newClusters[index]==null)
            {
                newClusters[index] = ptr2;
            }
            else {

                ptr2.setNextNode(newClusters[index]);
                newClusters[index] = ptr2;
            }
            ptr = ptr.getNextNode();
        }
        }

        return newClusters;
    }
}
