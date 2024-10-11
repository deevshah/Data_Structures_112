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
 * Read from the SpotInputFile with the format:
 * One integer
 *      i.    The dimensional number of the starting hub (int)
 * 
 * Step 4:
 * AnomaliesInputFile name is passed through the command line as args[3]
 * Read from the AnomaliesInputFile with the format:
 * 1. e (int): number of anomalies in the file
 * 2. e lines, each with:
 *      i.   The Name of the anomaly which will go from the hub dimension to their home dimension (String)
 *      ii.  The time allotted to return the anomaly home before a canon event is missed (int)
 * 
 * Step 5:
 * ReportOutputFile name is passed in through the command line as args[4]
 * Output to ReportOutputFile with the format:
 * 1. e Lines (one for each anomaly), listing on the same line:
 *      i.   The number of canon events at that anomalies home dimensionafter being returned
 *      ii.  Name of the anomaly being sent home
 *      iii. SUCCESS or FAILED in relation to whether that anomaly made it back in time
 *      iv.  The route the anomaly took to get home
 * 
 * @author Seth Kelley
 */

public class GoHomeMachine {


   
    
    public static void main(String[] args) {

        if ( args.length < 5 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.GoHomeMachine <dimension INput file> <spiderverse INput file> <hub INput file> <anomalies INput file> <report OUTput file>");
                return;
        }
        //java -cp bin spiderman.GoHomeMachine dimension.in spiderverse.in hub.in anomalies.in report.out

        // WRITE YOUR CODE HERE
        String dimension = args[0]; 
        String spiderverse = args[1]; 
        String hub = args[2]; 
        StdIn.setFile(hub);
        int Hub = StdIn.readInt(); 
        String anomaliesIn = args[3]; 
        //creates parallel arrays with dimensions and people in that dimension 
        Node[] adjList = adjList(dimension);
        Person[] list = spiderverse(dimension, spiderverse); 

        StdIn.setFile(anomaliesIn);
        int num = StdIn.readInt(); 
        ArrayList<ArrayList<Object>> toPrint = new ArrayList<>(); 
        for (int i =0; i<num;i++){
            String name = StdIn.readString(); 
            int time = StdIn.readInt(); 
            boolean b = false; 
            Person person = findPerson(name, list); 
            ArrayList<Object> toAdd = new ArrayList<>(); 

            ArrayList<Integer> path = reconstruct(person.getDimensionSign(), dijkstra(Hub, adjList)); 
            
            int weight = 0; 
            for (int j= 0; j<path.size(); j++){
                if (j!=0 && j!=path.size())
                {
                    weight += adjList[findIndex(path.get(j), adjList)].getDimension().getDimensionWeight();
                    weight += adjList[findIndex(path.get(j), adjList)].getDimension().getDimensionWeight();
                }
                else {
                    weight += adjList[findIndex(path.get(j), adjList)].getDimension().getDimensionWeight();
                }
                 
            }
            if (weight<time) b =true; 

            toAdd.add(2); 
            toAdd.add(name); 
            if (b){
                toAdd.add("SUCCESS");
            }
            else {
                toAdd.add("FAILED");
            }
            for (int p =0; p<path.size(); p++)
            {
                toAdd.add(path.get(p)); 
            }
            toPrint.add(toAdd); 
            

        }

        StdOut.setFile(args[4]);
        for (int i =0; i<toPrint.size(); i++){
            for (int j =0; j<toPrint.get(i).size(); j++){
                ArrayList<Object> print = toPrint.get(i); 
                StdOut.print(print.get(j) + " ");
            }
            StdOut.println();
        }
       

    
    }


    private static Person findPerson(String name, Person[] list){
        

        for (int i =0; i<list.length; i++){
            if(list[i]!=null){
                Person ptr = list[i]; 
                while (ptr!=null){
                    if (ptr.getName().equals(name)){
                        Person person = new Person(ptr.getCurrentDimension(),ptr.getName(),ptr.dimensionSign, null);
                        return person; 
                    }
                    ptr = ptr.getNext();
                }
            }
        }
        return null; 
    }

        public static HashMap<Integer, Integer> dijkstra(int start, Node[] adjList) {
            HashMap<Integer, Double> dist = new HashMap<>();
            for (int i=0; i<adjList.length;i++){
                Node ptr = adjList[i];
                while (ptr!=null){
                    dist.put(ptr.getDimension().getDimensionNumber(), Double.POSITIVE_INFINITY);
                    ptr = ptr.getNextNode(); 
                }
                
            }
            dist.put(start, 0.0);
            HashMap<Integer, Integer> prev = new HashMap<>();
            PriorityQueue<Integer> fringe = new PriorityQueue<>();
            fringe.add(start);

            while (!fringe.isEmpty()) {
                int currentDimension = fringe.remove();
                Node current = adjList[findIndex(currentDimension, adjList)];
                // go through neighbors of current dimension 
                while (current != null) {
                    int dimension = current.getDimension().getDimensionNumber();
                    double updatedDistance = dist.get(currentDimension) + current.getDimension().getDimensionWeight();
                    //update if more cost effective path is found 
                    if (updatedDistance < dist.get(dimension)) {
                        prev.put(dimension, currentDimension);
                        dist.put(dimension, updatedDistance);
                        fringe.add(dimension);
                    }
                    current = current.getNextNode();
                }
            }
        //return hashmap of parents
            return prev;
        }

        private static ArrayList<Integer> reconstruct(int end, HashMap<Integer, Integer> prev){
            ArrayList<Integer> path = new ArrayList<>();
            Integer activeNode = end;
            while (activeNode != null) {
                path.add(activeNode);
                activeNode = prev.get(activeNode);
            }

            //reverse path and return path 
            ArrayList<Integer> newPath = new ArrayList<>();
            for (int i =0; i<path.size(); i++)
            {
                newPath.add(i, path.get(path.size()-1-i));
            }
            return newPath; 
        }

public static int edgeCount(Node[] adjList){
    int count = 0;
    for (int i =0; i<adjList.length; i++){
        Node ptr = adjList[i].getNextNode();
        while (ptr!=null)
    {
        count++;
        ptr = ptr.getNextNode();
    }   
    }
    return count/2;
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

