

public class Test 
{
    public static void main(String[] args)
    {
        menu(menu.in);
        StdIn.setFile(menu.in);
        int length = StdIn.readInt();

        for (int i=0; i<length; i++)
        {
            System.out.println(categoryVar[i]);

            MenuNode ptr = menuVar[i];
            while (ptr!=null)
            {
                StdOut.println(ptr);
                ptr = ptr.getNextMenuNode();
            }
        }
    }
}