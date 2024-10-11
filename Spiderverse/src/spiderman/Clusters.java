package spiderman;

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
 * 
 * Step 2:
 * ClusterOutputFile name is passed in through the command line as args[1]
 * Output to ClusterOutputFile with the format:
 * 1. n lines, listing all of the dimension numbers connected to 
 *    that dimension in order (space separated)
 *    n is the size of the cluster table.
 * 
 * @author Seth Kelley
 */

public class Clusters {

    public static void main(String[] args) {

        if ( args.length < 2 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.Clusters <dimension INput file> <collider OUTput file>");
                return;
        }

    
        Node[] cluster = put(args[0]);


        //for index 0
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
        Node ptrLast2 = new Node(cluster[cluster.length-1].getDimension(), null);
        ptr1.addToEnd(ptrLast2);
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


        //output file
        StdOut.setFile(args[1]);
        for (int i=0; i<cluster.length; i++)
        {
            Node current = cluster[i];
            while (current!=null){
                StdOut.print(current.getDimension().getDimensionNumber() + " ");

                current = current.getNextNode();
            }
            StdOut.println();
        }
    

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
