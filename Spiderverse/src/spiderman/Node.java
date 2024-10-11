package spiderman; 

public class Node {
    private Dimension dimension; 
    private Node next; 

    public Node (Dimension dimension, Node next){
        this.dimension = dimension; 
        this.next = next; 
    }


    public Dimension getDimension(){return dimension;}
    public void setDimension(Dimension dimension){this.dimension = dimension;}

    public Node getNextNode(){return next;}
    public void setNextNode(Node next){this.next = next;}

    
    public void addToEnd (Node node)
    {
        if (node==null)
        {
            return;
        }
        Node ptr = this; 
        while(ptr.getNextNode()!=null)
        {
            ptr = ptr.getNextNode();
        }
        ptr.setNextNode(node);
    }

}

