package climate;

import java.util.ArrayList;

/**
 * This class contains methods which perform various operations on a layered 
 * linked list structure that contains USA communitie's Climate and Economic information.
 * 
 * @author Navya Sharma
 */

public class ClimateEconJustice {

    private StateNode firstState;
    
    /*
    * Constructor
    * 
    * **** DO NOT EDIT *****
    */
    public ClimateEconJustice() {
        firstState = null;
    }

    /*
    * Get method to retrieve instance variable firstState
    * 
    * @return firstState
    * 
    * **** DO NOT EDIT *****
    */ 
    public StateNode getFirstState () {
        // DO NOT EDIT THIS CODE
        return firstState;
    }

    /**
     * Creates 3-layered linked structure consisting of state, county, 
     * and community objects by reading in CSV file provided.
     * 
     * @param inputFile, the file read from the Driver to be used for
     * @return void
     * 
     * **** DO NOT EDIT *****
     */
    public void createLinkedStructure ( String inputFile ) {
        
        // DO NOT EDIT THIS CODE
        StdIn.setFile(inputFile);
        StdIn.readLine();
        
        // Reads the file one line at a time
        while ( StdIn.hasNextLine() ) {
            // Reads a single line from input file
            String line = StdIn.readLine();
            // IMPLEMENT these methods
            addToStateLevel(line);
            addToCountyLevel(line);
            addToCommunityLevel(line);
        }
    }

    /*
    * Adds a state to the first level of the linked structure.
    * Do nothing if the state is already present in the structure.
    * 
    * @param inputLine a line from the input file
    */
    @SuppressWarnings("unused")
    public void addToStateLevel ( String inputLine ) {

        // WRITE YOUR CODE HERE
        String [] input = inputLine.split(",");
        StateNode stateNode = new StateNode(input[2], null, null);
        StateNode ptr = firstState;
        if (ptr == null)
        {
            firstState = stateNode;
        }
        else 
        {
            while (ptr!= null)
            {
                if (ptr.getName().equals(input[2]))
                {
                    return;
                }
                
                if(ptr.getNext()==null)
                {
                    ptr.setNext(stateNode);
                }
                ptr=ptr.getNext();
            }
        }
    }

    /*
    * Adds a county to a state's list of counties.
    * 
    * Access the state's list of counties' using the down pointer from the State class.
    * Do nothing if the county is already present in the structure.
    * 
    * @param inputFile a line from the input file
    */
    public void addToCountyLevel ( String inputLine ) {

        String[] input = inputLine.split(",");
        String state=input[2];
        String county = input[1];

        StateNode ptr = firstState;
        CountyNode firstCounty = new CountyNode(null,null,null);
        StateNode tempstate = new StateNode(null, null,null);
        //find state corresponding to county
        while(ptr!=null)
        {
            if(ptr.getName().equals(state))
            {
                firstCounty = ptr.getDown();
                tempstate=ptr;
                break;
            }
            ptr=ptr.getNext();
        }

        CountyNode newCounty = new CountyNode(county,null,null);
        CountyNode ptr2 = firstCounty;
        if(ptr2==null)
        {
            firstCounty = newCounty;
            tempstate.setDown(firstCounty);
        }
        else 
        {
        while (ptr2!=null)
        {
            if (ptr2.getName().equals(county))
            {
                return;
            }

            if (ptr2.getNext()==null)
            {
                ptr2.setNext(newCounty);
            }
            ptr2 = ptr2.getNext();
        }
    }
    }

    /*
    * Adds a community to a county's list of communities.
    * 
    * Access the county through its state
    *      - search for the state first, 
    *      - then search for the county.
    * Use the state name and the county name from the inputLine to search.
    * 
    * Access the state's list of counties using the down pointer from the StateNode class.
    * Access the county's list of communities using the down pointer from the CountyNode class.
    * Do nothing if the community is already present in the structure.
    * 
    * @param inputFile a line from the input file
    */
    public void addToCommunityLevel ( String inputLine ) {

        //create data object
        String[] input = inputLine.split(",");
        Data data = new Data(Double.parseDouble(input[3]),Double.parseDouble(input[4]),Double.parseDouble(input[5]),Double.parseDouble(input[8]),Double.parseDouble(input[9]), input[19], Double.parseDouble(input[49]),Double.parseDouble(input[37]),Double.parseDouble(input[121]));
        CommunityNode newCommunity = new CommunityNode(input[0],null,data);

        StateNode ptr = firstState;
        CountyNode firstCounty = new CountyNode(null,null,null);
        //find state corresponding to county
        //store county in first county
        while(ptr!=null)
        {
            if(ptr.getName().equals(input[2]))
            {
                firstCounty = ptr.getDown();
                break;
            }
            ptr=ptr.getNext();
        }

        //get corresponding community
        
        CommunityNode firstCommunity = new CommunityNode(null,null,null);
        CountyNode tempCounty = new CountyNode(null,null,null);
        CountyNode ptr2 = firstCounty;
        while (ptr2!=null)
        {
            if (ptr2.getName().equals(input[1]))
            {
                firstCommunity = ptr2.getDown();
                tempCounty=ptr2;
                break;
                
            }
            ptr2 = ptr2.getNext();
        }

        CommunityNode ptr3 = firstCommunity;
        if(ptr3==null)
        {
            firstCommunity = newCommunity;
            tempCounty.setDown(firstCommunity);
        }
        else
        {
            while(ptr3!=null)
            {
                if(ptr3.getName().equals(input[0]))
                {
                    return;
                }
                if (ptr3.getNext()==null)
            {
                ptr3.setNext(newCommunity);
            }
            ptr3 = ptr3.getNext();
            }
        }



    }

    /**
     * Given a certain percentage and racial group inputted by user, returns
     * the number of communities that have that said percentage or more of racial group  
     * and are identified as disadvantaged
     * 
     * Percentages should be passed in as integers for this method.
     * 
     * @param userPrcntage the percentage which will be compared with the racial groups
     * @param race the race which will be returned
     * @return the amount of communities that contain the same or higher percentage of the given race
     */
    @SuppressWarnings("unused")
    public int disadvantagedCommunities ( double userPrcntage, String race ) {

        int count = 0;

        for (StateNode ptr1 = firstState; ptr1 != null; ptr1 = ptr1.getNext())
        {
            for (CountyNode ptr2 = ptr1.getDown();ptr2!=null;ptr2 = ptr2.getNext())
            {
                for (CommunityNode ptr3 = ptr2.getDown();ptr3!=null;ptr3=ptr3.getNext())
                {
                    Data data = ptr3.getInfo();
                    String disadvantaged = data.getAdvantageStatus();
                    double percent = 0;
                    if (disadvantaged.equals("True"))
                    {
                    if (race.equals("African American"))
                    {
                        percent = (data.getPrcntAfricanAmerican())*100;
                        if (percent>=userPrcntage)
                        {
                            count++;
                        }
                    }
                    else 
                    if (race.equals("Native American"))
                    {
                        percent = (data.getPrcntNative())*100;
                        if (percent>=userPrcntage)
                        {
                            count++;
                        }
                    }
                    else
                    if (race.equals("Asian American"))
                    {
                        percent = (data.getPrcntAsian())*100;
                        if (percent>=userPrcntage)
                        {
                            count++;
                        }
                    }
                    else 
                    if (race.equals("White American"))
                    {
                        percent = (data.getPrcntWhite())*100;
                        if (percent>=userPrcntage)
                        {
                            count++;
                        }
                    }
                    else 
                    if (race.equals("Hispanic American"))
                    {
                        percent = (data.getPrcntHispanic())*100;
                        if (percent>=userPrcntage)
                        {
                            count++;
                        }
                    }
                }
                }

            }
        }

        return count; // replace this line
    }

    /**
     * Given a certain percentage and racial group inputted by user, returns
     * the number of communities that have that said percentage or more of racial group  
     * and are identified as non disadvantaged
     * 
     * Percentages should be passed in as integers for this method.
     * 
     * @param userPrcntage the percentage which will be compared with the racial groups
     * @param race the race which will be returned
     * @return the amount of communities that contain the same or higher percentage of the given race
     */
    public int nonDisadvantagedCommunities ( double userPrcntage, String race ) {

        //WRITE YOUR CODE HERE
        int count = 0;

        for (StateNode ptr1 = firstState; ptr1 != null; ptr1 = ptr1.getNext())
        {
            for (CountyNode ptr2 = ptr1.getDown();ptr2!=null;ptr2 = ptr2.getNext())
            {
                for (CommunityNode ptr3 = ptr2.getDown();ptr3!=null;ptr3=ptr3.getNext())
                {
                    Data data = ptr3.getInfo();
                    String disadvantaged = data.getAdvantageStatus();
                    double percent = 0;
                    if (disadvantaged.equals("False"))
                    {
                    if (race.equals("African American"))
                    {
                        percent = (data.getPrcntAfricanAmerican())*100;
                        if (percent>=userPrcntage)
                        {
                            count++;
                        }
                    }
                    else 
                    if (race.equals("Native American"))
                    {
                        percent = (data.getPrcntNative())*100;
                        if (percent>=userPrcntage)
                        {
                            count++;
                        }
                    }
                    else
                    if (race.equals("Asian American"))
                    {
                        percent = (data.getPrcntAsian())*100;
                        if (percent>=userPrcntage)
                        {
                            count++;
                        }
                    }
                    else 
                    if (race.equals("White American"))
                    {
                        percent = (data.getPrcntWhite())*100;
                        if (percent>=userPrcntage)
                        {
                            count++;
                        }
                    }
                    else 
                    if (race.equals("Hispanic American"))
                    {
                        percent = (data.getPrcntHispanic())*100;
                        if (percent>=userPrcntage)
                        {
                            count++;
                        }
                    }
                }
                }

            }
        }
        return count; // replace this line
    }
    
    /** 
     * Returns a list of states that have a PM (particulate matter) level
     * equal to or higher than value inputted by user.
     * 
     * @param PMlevel the level of particulate matter
     * @return the States which have or exceed that level
     */ 
    public ArrayList<StateNode> statesPMLevels ( double PMlevel ) {

        // WRITE YOUR METHOD HERE
        ArrayList<StateNode> statesList = new ArrayList<StateNode>();

        int count = 0;

        for (StateNode ptr1 = firstState; ptr1 != null; ptr1 = ptr1.getNext())
        {
            for (CountyNode ptr2 = ptr1.getDown();ptr2!=null;ptr2 = ptr2.getNext())
            {
                for (CommunityNode ptr3 = ptr2.getDown();ptr3!=null;ptr3=ptr3.getNext())
                {
                    Data data = ptr3.getInfo();
                    double pm = data.getPMlevel();
                    if (pm>=PMlevel && !(statesList.contains(ptr1)))
                    {
                        statesList.add(ptr1);
                    }
                   
                }
            }
        }

            
        


        return statesList; // replace this line
    }

    /**
     * Given a percentage inputted by user, returns the number of communities 
     * that have a chance equal to or higher than said percentage of
     * experiencing a flood in the next 30 years.
     * 
     * @param userPercntage the percentage of interest/comparison
     * @return the amount of communities at risk of flooding
     */
    public int chanceOfFlood ( double userPercntage ) {

        // WRITE YOUR METHOD HERE
        int count = 0;
        for (StateNode ptr1 = firstState; ptr1 != null; ptr1 = ptr1.getNext())
        {
            for (CountyNode ptr2 = ptr1.getDown();ptr2!=null;ptr2 = ptr2.getNext())
            {
                for (CommunityNode ptr3 = ptr2.getDown();ptr3!=null;ptr3=ptr3.getNext())
                {
                    Data data = ptr3.getInfo();
                    double flood = data.getChanceOfFlood();
                    if (flood>=userPercntage)
                    {
                        count++;
                    }

                    
                   
                }
            }
        }
        return count; // replace this line
    }

    /** 
     * Given a state inputted by user, returns the communities with 
     * the 10 lowest incomes within said state.
     * 
     *  @param stateName the State to be analyzed
     *  @return the top 10 lowest income communities in the State, with no particular order
    */
    public ArrayList<CommunityNode> lowestIncomeCommunities ( String stateName ) {

        //WRITE YOUR METHOD HERE
        ArrayList<CommunityNode> list = new ArrayList<>();
        StateNode state = new StateNode(null,null,null);

        for (StateNode ptr1 = firstState; ptr1 != null; ptr1 = ptr1.getNext())
        {
            if (ptr1.getName().equals(stateName))
            {
                state=ptr1;
                break;
            }
        }
        double poverty=0;
            for (CountyNode ptr2 = state.getDown();ptr2!=null;ptr2 = ptr2.getNext())
            {
                for (CommunityNode ptr3 = ptr2.getDown();ptr3!=null;ptr3=ptr3.getNext())
                {
                    Data info = ptr3.getInfo();
                    poverty = info.getPercentPovertyLine();
                    
                    if (list.size()<10)
                    {
                        list.add(ptr3);
                    }
                    else 
                    {
                        double min = list.get(0).getInfo().getPercentPovertyLine();
                        for (int i=0;i<list.size();i++)
                        {
                            min = Math.min(list.get(i).getInfo().getPercentPovertyLine(), min);
                        }
                        for (int j=0; j<list.size();j++)
                        {
                            if (list.get(j).getInfo().getPercentPovertyLine() == min)
                            {
                                if (list.get(j).getInfo().getPercentPovertyLine()<poverty)
                                {
                                    list.set(j,ptr3);
                                    
                                }
                                break;
                            }
                        }

                    }
                }
            }


        return list; // replace this line
    }
}
    
