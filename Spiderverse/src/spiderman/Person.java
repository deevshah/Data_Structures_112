package spiderman;
public class Person{
    int currentDimension; 
    String name; 
    int dimensionSign; 
    Person next; 
    
    public Person(int currentDimension, String name, int dimensionSign, Person next){
        this.currentDimension = currentDimension;
        this.name = name; 
        this.dimensionSign = dimensionSign; 
        this.next = next; 
    }


    public  int getCurrentDimension(){return currentDimension;}
    public String getName() {return name;}
    public int getDimensionSign(){return dimensionSign;}
    public Person getNext(){return next;}

    public void setCurrentDimension(int currentDimension){this.currentDimension = currentDimension;}
    public void setName(String name){this.name = name;}
    public void setDimensionSign(int dimensionSign){this.dimensionSign = dimensionSign;}
    public void setNext(Person person){this.next=person;}

    public void addEnd (Person node)
    {
        if (node==null)
        {
            return;
        }
        Person ptr = this; 
        while(ptr.getNext()!=null)
        {
            ptr = ptr.getNext();
        }
        ptr.setNext(node);
    }

    
}