package forensic;

import java.util.ArrayList;

import org.w3c.dom.Node;



/**
 * This class represents a forensic analysis system that manages DNA data using
 * BSTs.
 * Contains methods to create, read, update, delete, and flag profiles.
 * 
 * @author Kal Pandit
 */
public class ForensicAnalysis {

    private TreeNode treeRoot;            // BST's root
    private String firstUnknownSequence;
    private String secondUnknownSequence;

    public ForensicAnalysis () {
        treeRoot = null;
        firstUnknownSequence = null;
        secondUnknownSequence = null;
    }

    /**
     * Builds a simplified forensic analysis database as a BST and populates unknown sequences.
     * The input file is formatted as follows:
     * 1. one line containing the number of people in the database, say p
     * 2. one line containing first unknown sequence
     * 3. one line containing second unknown sequence
     * 2. for each person (p), this method:
     * - reads the person's name
     * - calls buildSingleProfile to return a single profile.
     * - calls insertPerson on the profile built to insert into BST.
     *      Use the BST insertion algorithm from class to insert.
     * 
     * DO NOT EDIT this method, IMPLEMENT buildSingleProfile and insertPerson.
     * 
     * @param filename the name of the file to read from
     */
    public void buildTree(String filename) {
        // DO NOT EDIT THIS CODE
        StdIn.setFile(filename); // DO NOT remove this line

        // Reads unknown sequences
        String sequence1 = StdIn.readLine();
        firstUnknownSequence = sequence1;
        String sequence2 = StdIn.readLine();
        secondUnknownSequence = sequence2;
        
        int numberOfPeople = Integer.parseInt(StdIn.readLine()); 

        for (int i = 0; i < numberOfPeople; i++) {
            // Reads name, count of STRs
            String fname = StdIn.readString();
            String lname = StdIn.readString();
            String fullName = lname + ", " + fname;
            // Calls buildSingleProfile to create
            Profile profileToAdd = createSingleProfile();
            // Calls insertPerson on that profile: inserts a key-value pair (name, profile)
            insertPerson(fullName, profileToAdd);
        }
    }

    /** 
     * Reads ONE profile from input file and returns a new Profile.
     * Do not add a StdIn.setFile statement, that is done for you in buildTree.
    */
    public Profile createSingleProfile() {

        // WRITE YOUR CODE HERE
        int s = StdIn.readInt();
        STR array[] = new STR[s];

       for (int i = 0; i<array.length;i++)
       {
        STR str = new STR(StdIn.readString(), StdIn.readInt());
        array[i] = str;
       }

       Profile prof = new Profile(array);
        
        
        return prof; // update this line
    }

    /**
     * Inserts a node with a new (key, value) pair into
     * the binary search tree rooted at treeRoot.
     * 
     * Names are the keys, Profiles are the values.
     * USE the compareTo method on keys.
     * 
     * @param newProfile the profile to be inserted
     */

    private TreeNode insert(TreeNode root, String name, Profile prof)
    {
        if (root==null)
        {
            root = new TreeNode(name, prof, null, null);
            return root;
        }

        if (name.compareTo(root.getName())<0)
        {
            root.setLeft(insert(root.getLeft(), name, prof));
        }
        else 
        if (name.compareTo(root.getName())>0)
        {
            root.setRight(insert(root.getRight(), name,prof));
        }
        return root;
    }

    public void insertPerson(String name, Profile newProfile) {

        // WRITE YOUR CODE HERE
        TreeNode node = new TreeNode(name, newProfile, null, null);

        if (treeRoot == null)
        {
            treeRoot = node;
        }
        else 
        {
            insert(treeRoot, name, newProfile);
        }
    }

    /**
     * Finds the number of profiles in the BST whose interest status matches
     * isOfInterest.
     *
     * @param isOfInterest the search mode: whether we are searching for unmarked or
     *                     marked profiles. true if yes, false otherwise
     * @return the number of profiles according to the search mode marked
     */

    private int match(TreeNode n, boolean b){
        
        if (n==null) return 0;
        int count=0;
        count += match(n.getLeft(), b);
        
        if (n.getProfile().getMarkedStatus()==b)
        {
            count++;
        }
        count += match(n.getRight(), b);    
        
        return count;
    }

    
    public int getMatchingProfileCount(boolean isOfInterest) {
        
        // WRITE YOUR CODE HERE
       
        return match(treeRoot, isOfInterest); // update this line
    }

    /**
     * Helper method that counts the # of STR occurrences in a sequence.
     * Provided method - DO NOT UPDATE.
     * 
     * @param sequence the sequence to search
     * @param STR      the STR to count occurrences of
     * @return the number of times STR appears in sequence
     */
    private int numberOfOccurrences(String sequence, String STR) {
        
        // DO NOT EDIT THIS CODE
        
        int repeats = 0;
        // STRs can't be greater than a sequence
        if (STR.length() > sequence.length())
            return 0;
        
            // indexOf returns the first index of STR in sequence, -1 if not found
        int lastOccurrence = sequence.indexOf(STR);
        
        while (lastOccurrence != -1) {
            repeats++;
            // Move start index beyond the last found occurrence
            lastOccurrence = sequence.indexOf(STR, lastOccurrence + STR.length());
        }
        return repeats;
    }

    /**
     * Traverses the BST at treeRoot to mark profiles if:
     * - For each STR in profile STRs: at least half of STR occurrences match (round
     * UP)
     * - If occurrences THROUGHOUT DNA (first + second sequence combined) matches
     * occurrences, add a match
     */

    private void traverse(TreeNode n)
    {
        if(n==null) return;
        traverse(n.getLeft());

        STR[] str = n.getProfile().getStrs();
        int count = 0; 
        for (int i = 0; i<str.length;i++)
        {
            int occurrenceCounter = numberOfOccurrences(firstUnknownSequence, str[i].getStrString());
            occurrenceCounter += numberOfOccurrences(secondUnknownSequence, str[i].getStrString());

            if (occurrenceCounter == str[i].getOccurrences())
            {
                count++;
            }

            int numOfSTRs = str.length;
            if (str.length%2!=0)
            {
                numOfSTRs = (str.length/2)+1;
            }
            else 
            {
                numOfSTRs = numOfSTRs/2;
            }

            if (count>=numOfSTRs)
            {
                n.getProfile().setInterestStatus(true);
            }
            else{
                n.getProfile().setInterestStatus(false);
            }
        }
       
        traverse(n.getRight());
    }
    
    public void flagProfilesOfInterest() {

        // WRITE YOUR CODE HERE
        traverse(treeRoot);


    }

    /**
     * Uses a level-order traversal to populate an array of unmarked Strings representing unmarked people's names.
     * - USE the getMatchingProfileCount method to get the resulting array length.
     * - USE the provided Queue class to investigate a node and enqueue its
     * neighbors.
     * 
     * @return the array of unmarked people
     */
    public String[] getUnmarkedPeople() {

        // WRITE YOUR CODE HERE
        int arraySize = getMatchingProfileCount(false);
        String[] array = new String[arraySize];

        Queue<TreeNode> queue = new Queue<>();
        Queue<TreeNode> queue2 = new Queue<>();
        TreeNode temp = treeRoot;

        if (treeRoot == null)
        {
            return array;
        }
        else
        {
            queue.enqueue(temp);
            queue2.enqueue(temp);
            while (!queue.isEmpty()&&temp!=null)
            {
                temp = queue.dequeue();
                if (temp.getLeft()!=null)queue.enqueue(temp.getLeft());
                if (temp.getLeft()!=null)queue2.enqueue(temp.getLeft());
                if (temp.getRight()!=null)queue.enqueue(temp.getRight());
                if (temp.getRight()!=null)queue2.enqueue(temp.getRight());
            }
        }

        Queue<TreeNode> queue3 = new Queue<>();
        while(!queue2.isEmpty())
        {
            if(queue2.peek().getProfile().getMarkedStatus()==false){
                queue3.enqueue(queue2.dequeue());
            }
            else 
            {
                queue2.dequeue();
            }
        }

        for (int i =0; i<array.length;i++)
        {
            array[i] = queue3.dequeue().getName();
            
        }



        return array; // update this line
    }

    /**
     * Removes a SINGLE node from the BST rooted at treeRoot, given a full name (Last, First)
     * This is similar to the BST delete we have seen in class.
     * 
     * If a profile containing fullName doesn't exist, do nothing.
     * You may assume that all names are distinct.
     * 
     * @param fullName the full name of the person to delete
     */

     

    private TreeNode deleteMin(TreeNode node)
    {
    if (node.getLeft() == null) return node.getRight();
        node.setLeft(deleteMin(node.getLeft()));
        return node;
    }

    private TreeNode min (TreeNode x)
    {
        if (x.getLeft()==null) return x;
         return min(x.getLeft()); 
    }

   

    private TreeNode delete(TreeNode n, String name)
    {
        
        if (n==null) return null;
        int cmp = name.compareTo(n.getName());
        if (cmp<0) n.setLeft(delete(n.getLeft(), name));
        else if (cmp>0) n.setRight(delete(n.getRight(), name));
        else{
            if (n.getRight()==null)
            return n.getLeft();
            if (n.getLeft()==null)
            return n.getRight();
            
            TreeNode t = n;
            n= min(t.getRight());
            n.setRight(deleteMin(t.getRight()));
            n.setLeft(t.getLeft());
            if (t==treeRoot){
                treeRoot=n;
            }
        }
        return n;
    }

     
    


    public void removePerson(String fullName) {
        // WRITE YOUR CODE HERE

        delete(treeRoot, fullName);
        
    }

    /**
     * Clean up the tree by using previously written methods to remove unmarked
     * profiles.
     * Requires the use of getUnmarkedPeople and removePerson.
     */
    public void cleanupTree() {
        // WRITE YOUR CODE HERE
        String[] array = getUnmarkedPeople();

        for (int i= 0; i<array.length; i++){
            removePerson(array[i]);

        }

    }

    /**
     * Gets the root of the binary search tree.
     *
     * @return The root of the binary search tree.
     */
    public TreeNode getTreeRoot() {
        return treeRoot;
    }

    /**
     * Sets the root of the binary search tree.
     *
     * @param newRoot The new root of the binary search tree.
     */
    public void setTreeRoot(TreeNode newRoot) {
        treeRoot = newRoot;
    }

    /**
     * Gets the first unknown sequence.
     * 
     * @return the first unknown sequence.
     */
    public String getFirstUnknownSequence() {
        return firstUnknownSequence;
    }

    /**
     * Sets the first unknown sequence.
     * 
     * @param newFirst the value to set.
     */
    public void setFirstUnknownSequence(String newFirst) {
        firstUnknownSequence = newFirst;
    }

    /**
     * Gets the second unknown sequence.
     * 
     * @return the second unknown sequence.
     */
    public String getSecondUnknownSequence() {
        return secondUnknownSequence;
    }

    /**
     * Sets the second unknown sequence.
     * 
     * @param newSecond the value to set.
     */
    public void setSecondUnknownSequence(String newSecond) {
        secondUnknownSequence = newSecond;
    }


}
