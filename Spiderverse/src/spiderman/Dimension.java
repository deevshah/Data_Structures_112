package spiderman;
public class Dimension{
    int dimensionNumber; 
    int numOfCanonEvents; 
    int dimensionWeight; 

    public Dimension(int dimensionNumber, int numOfCanonEvents, int dimensionWeight)
    {
        this.dimensionNumber=dimensionNumber;
        this.numOfCanonEvents=numOfCanonEvents; 
        this.dimensionWeight = dimensionWeight;
    }

    public int getDimensionNumber(){return dimensionNumber;}
    public int getNumOfCanonEvetns(){return numOfCanonEvents;}
    public int getDimensionWeight(){return dimensionWeight;}

    public void setDimensionNumber(int dimensionNumber){this.dimensionNumber=dimensionNumber;}
    public void setNumOfCanonEvents(int numOfCanonEvents){this.numOfCanonEvents=numOfCanonEvents;}
    public void setDimensionWeight(int dimensionWeight){this.dimensionWeight=dimensionWeight;}


}