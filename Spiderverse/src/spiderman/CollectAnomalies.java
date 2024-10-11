package spiderman;
import java.util.*;

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
 * HubInputFile name is passed through the command line as args[2]
 * Read from the HubInputFile with the format:
 * One integer
 *      i.    The dimensional number of the starting hub (int)
 * 
 * Step 4:
 * CollectedOutputFile name is passed in through the command line as args[3]
 * Output to CollectedOutputFile with the format:
 * 1. e Lines, listing the Name of the anomaly collected with the Spider who
 *    is at the same Dimension (if one exists, space separated) followed by 
 *    the Dimension number for each Dimension in the route (space separated)
 * 
 * @author Seth Kelley
 */

public class CollectAnomalies {
    
    public static void main(String[] args) {

        if ( args.length < 4 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.CollectAnomalies <dimension INput file> <spiderverse INput file> <hub INput file> <collected OUTput file>");
                return;
        }

        // WRITE YOUR CODE HERE
        String dimension = args[0]; 
        //creates parallel arrays with dimensions and people in that dimension 
        Node[] adjList = adjList(dimension);
        Person[] list = spiderverse(dimension, args[1]); 

        //find all anomalies 
        ArrayList<Person> anomalies = new ArrayList<>(); 
        for (int i=0; i<list.length; i++)
        {
            
            if (list[i]!=null && list[i].getCurrentDimension()!=928)
            {
                Person ptr = list[i]; 
                boolean containsSpider = false; 
                Person spider = new Person (-1, null, -1, null);
                while (ptr!=null)
                {
                    
                    boolean containsAnamoly = false; 
                    
                    Person anamoly = new Person (-1, null, -1, null);
                    if (ptr.getCurrentDimension() == ptr.getDimensionSign() && !containsSpider){
                        containsSpider = true; 
                        spider = new Person(ptr.getCurrentDimension(), ptr.getName(), ptr.dimensionSign, null); 
                    }
                    else 
                    if (ptr.getCurrentDimension()!=ptr.getDimensionSign())
                    {
                        containsAnamoly = true; 
                        anamoly = new Person(ptr.getCurrentDimension(), ptr.getName(), ptr.dimensionSign, null); 
                    }

                    if (containsAnamoly)
                    {
                        if (!containsSpider){
                            anomalies.add(anamoly);
                        }
                        else 
                        if(containsSpider){
                            anamoly.setNext(spider);
                            anomalies.add(anamoly);
                        }
                    }
                    ptr = ptr.getNext(); 
                }
                
            } 
        }
        StdIn.setFile(args[2]); 
        int start = StdIn.readInt(); 
        ArrayList<ArrayList<Object>> output = new ArrayList<>(); 
        for (int i =0; i<anomalies.size(); i++)
        {
            Person person = anomalies.get(i); 
            Person ptr = person;
            int count = 0;  
            while (ptr!=null){
                count++;
                ptr = ptr.getNext(); 
            }
            if (count ==1){
                ArrayList<Object> toAdd = new ArrayList<>(); 
                toAdd.add(person.getName()); 
                int startIndex = findIndex(start, adjList); 
                int endIndex = findIndex(person.getCurrentDimension(), adjList); 
                ArrayList<Integer> path = bfs(startIndex, endIndex, adjList); 
                for (int j=0; j<path.size();j++){
                    toAdd.add(adjList[path.get(j)].getDimension().getDimensionNumber()); 
                }
                for (int k = path.size()-2; k>=0; k--){
                    toAdd.add(adjList[path.get(k)].getDimension().getDimensionNumber()); 
                }
                output.add(toAdd); 
            }
            else 
            if (count ==2){
                ArrayList<Object> toAdd2 = new ArrayList<>(); 
                Person person2 = person; 
                toAdd2.add(person.getName()); 
                toAdd2.add(person.getNext().getName()); 
                int startIndex2 = findIndex(start, adjList);
                int endIndex2 = findIndex(person2.getCurrentDimension(), adjList); 
                ArrayList<Integer> path2 = bfs(startIndex2, endIndex2, adjList); 
                for (int p = path2.size()-1; p>=0; p--){
                    toAdd2.add(adjList[path2.get(p)].getDimension().getDimensionNumber()); 
                }
                output.add(toAdd2); 
            }
        }


        StdOut.setFile(args[3]);
        for (int i=0;i<output.size();i++)
        {   
            ArrayList<Object> toPrint = output.get(i); 
            for (int j = 0; j<toPrint.size(); j++){
                StdOut.print(toPrint.get(j) + " ");
            }
            StdOut.println(); 
        }
   
    }




    public static ArrayList<Integer> helper(int[] parent, int start, int end){
        ArrayList<Integer> path = new ArrayList<>(); 
        int current = end; 

        while (current != -1) {
            path.add(0, current); 
            current = parent[current];
        }
        if (path.get(0) == start) {
            return path; 
        }
        return new ArrayList<>(); 
    }

    public static ArrayList<Integer> bfs(int start, int end, Node[] adjList){
        Queue<Integer> q = new LinkedList<>(); 
        int[] parent  = new int[adjList.length]; 
        q.add(start); 
        boolean[] visited = new boolean[adjList.length]; 
        for (int i = 0; i<visited.length; i++)
        {
            visited[i] = false; 
        }

        visited[start] = true; 
       
        
        for (int i=0; i<parent.length; i++){
            parent[i] = -1; 
        }

        ArrayList<Integer> path = new ArrayList<>(); 
        while (!q.isEmpty()){
            int current = q.remove();
            if (adjList[current].getDimension().getDimensionNumber()==adjList[end].getDimension().getDimensionNumber()){
                return helper(parent, start, end); 

            }  
            
            Node ptr = adjList[current];
            
            while (ptr!=null)
            {

                int index = findIndex(ptr, adjList);
                if (!visited[index]){
                    q.add(findIndex(ptr.getDimension().getDimensionNumber(), adjList));
                    visited[index] = true; 
                    parent[index] = current;
                    
                }
                ptr = ptr.getNextNode(); 
            }
        }
        return new ArrayList<>(); 

    }


    public static int[] dimensionArr(String input){
        StdIn.setFile(input); 
        int size = StdIn.readInt(); 
        int[] dimensionArr = new int[size]; 
        StdIn.readLine(); 
        for (int i=0; i<size; i++)
        {
            dimensionArr[i] = StdIn.readInt(); 
            StdIn.readLine(); 
        }
        return dimensionArr; 
    }
    private static  Person[] spiderverse(String dimensionInput, String spiderInput){
        Node[] dimensionArr = adjList(dimensionInput); 

        Person[] list = new Person[dimensionArr.length]; 
        StdIn.setFile(spiderInput);
        int num = StdIn.readInt();
        StdIn.readLine();
        for (int i=0; i<num; i++)
        {
            Person person = new Person(StdIn.readInt(), StdIn.readString(), StdIn.readInt(), null); 
            int index = findIndex(person.getCurrentDimension(), dimensionArr);
            if (list[index]==null)
            {
                list[index] = person; 
            }
            else{
            list[index].addEnd(person);
            }
        }
        return list; 

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
    public static int findIndex(int dimension, Node[] adj)
    {
        int index=-1;
        for (int i =0; i<adj.length; i++)
        {
            if (adj[i]!=null && adj[i].getDimension().getDimensionNumber() == dimension){
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
