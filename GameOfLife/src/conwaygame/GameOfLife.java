package conwaygame;
import java.util.ArrayList;
/**
 * Conway's Game of Life Class holds various methods that will
 * progress the state of the game's board through it's many iterations/generations.
 *
 * Rules 
 * Alive cells with 0-1 neighbors die of loneliness.
 * Alive cells with >=4 neighbors die of overpopulation.
 * Alive cells with 2-3 neighbors survive.
 * Dead cells with exactly 3 neighbors become alive by reproduction.

 * @author Seth Kelley 
 * @author Maxwell Goldberg
 */
public class GameOfLife {

    // Instance variables
    private static final boolean ALIVE = true;
    private static final boolean  DEAD = false;

    private boolean[][] grid;    // The board has the current generation of cells
    private int totalAliveCells; // Total number of alive cells in the grid (board)

    /**
    * Default Constructor which creates a small 5x5 grid with five alive cells.
    * This variation does not exceed bounds and dies off after four iterations.
    */
    public GameOfLife() {
        grid = new boolean[5][5];
        totalAliveCells = 5;
        grid[1][1] = ALIVE;
        grid[1][3] = ALIVE;
        grid[2][2] = ALIVE;
        grid[3][2] = ALIVE;
        grid[3][3] = ALIVE;
    }

    /**
    * Constructor used that will take in values to create a grid with a given number
    * of alive cells
    * @param file is the input file with the initial game pattern formatted as follows:
    * An integer representing the number of grid rows, say r
    * An integer representing the number of grid columns, say c
    * Number of r lines, each containing c true or false values (true denotes an ALIVE cell)
    */
    public GameOfLife (String file) {
        StdIn.setFile(file);
        int r = StdIn.readInt();
        int c = StdIn.readInt();
        grid = new boolean[r][c];
        for (int i=0;i<grid.length;i++)
        {
            for (int j = 0; j<grid[0].length;j++)
            {
                grid[i][j] = StdIn.readBoolean();
            }
        }
        // WRITE YOUR CODE HERE
    }

    /**
     * Returns grid
     * @return boolean[][] for current grid
     */
    public boolean[][] getGrid () {
        return grid;
    }
    
    /**
     * Returns totalAliveCells
     * @return int for total number of alive cells in grid
     */
    public int getTotalAliveCells () {
        return totalAliveCells;
    }

    /**
     * Returns the status of the cell at (row,col): ALIVE or DEAD
     * @param row row position of the cell
     * @param col column position of the cell
     * @return true or false value "ALIVE" or "DEAD" (state of the cell)
     */
    public boolean getCellState (int row, int col) {

        // WRITE YOUR CODE HERE
        boolean state = grid[row][col];
        boolean state2;
        if (state==true)
        {
            state2=ALIVE;
        }
        else 
        {
            state2=DEAD;
        }

        return state2; // update this line, provided so that code compiles
    }

    /**
     * Returns true if there are any alive cells in the grid
     * @return true if there is at least one cell alive, otherwise returns false
     */
    public boolean isAlive () {

        // WRITE YOUR CODE HERE
        for (int i=0;i<grid.length;i++)
        {
            for (int j= 0; j<grid[0].length;j++)
            {
                if (grid[i][j]==true)
                {
                    return true;
                }
            }
        }


        return false; // update this line, provided so that code compiles
    }

    /**
     * Determines the number of alive cells around a given cell.
     * Each cell has 8 neighbor cells which are the cells that are 
     * horizontally, vertically, or diagonally adjacent.
     * 
     * @param col column position of the cell
     * @param row row position of the cell
     * @return neighboringCells, the number of alive cells (at most 8).
     */
    public int numOfAliveNeighbors (int row, int col) {

        // WRITE YOUR CODE HERE
        int count=0;
        for (int i=row-1;i<=row+1;i++)
        {
            for (int j=col-1;j<=col+1;j++)
            {
                int rowIndex=i;
                int colIndex=j;

                if (i<0)
                {
                    rowIndex=grid.length-1;
                }
                if (i>=grid.length)
                {
                    rowIndex=0;
                }
                if (j<0)
                {
                    colIndex=grid[0].length-1;
                }
                if (j>=grid[0].length)
                {
                    colIndex=0;
                }
                if (grid[rowIndex][colIndex] && !(row==rowIndex && col==colIndex))
                {
                    count++;
                }

            }
        }


        /*int count = 0;
        int rowIndex1=row-1;
        int colIndex1 = col-1;
        int rowIndex2=row+1;
        int colIndex2 = col+1;

        if (rowIndex1<0)
        {
            rowIndex1++;
        }
        
        if (rowIndex2>grid.length-1)
        {
            rowIndex2--;
        }

        if (colIndex1<0)
        {
            colIndex1++;
        }

      if (colIndex2>getGrid()[0].length-1)
        {
            colIndex2--;
        }

        for (int i=rowIndex1; i<=rowIndex2;i++)
        {
            for (int j = colIndex1;j<=colIndex2;j++)
            {
                if (i==row && j==col)
                {
                    count+=0;
                }
                else
                if (grid[i][j]==true)
                {
                    count++;
                }
            }
        }*/

        return count; // update this line, provided so that code compiles
    }

    /**
     * Creates a new grid with the next generation of the current grid using 
     * the rules for Conway's Game of Life.
     * 
     * @return boolean[][] of new grid (this is a new 2D array)
     */
    public boolean[][] computeNewGrid () {

        // WRITE YOUR CODE HERE
        boolean[][] newGrid = new boolean[getGrid().length][getGrid()[0].length]; 
        for (int i = 0; i<newGrid.length;i++)
        {
            for (int j = 0; j<newGrid[0].length;j++)
            {
                /*if (numOfAliveNeighbors(i,j)<2 && getGrid()[i][j])
                {
                    newGrid[i][j]=false;
                }
                else */
                if(numOfAliveNeighbors(i,j)==3 && !getGrid()[i][j])
                {
                    newGrid[i][j]=true;
                }
                else 
                if((numOfAliveNeighbors(i,j)==3||numOfAliveNeighbors(i,j)==2) && getGrid()[i][j])
                {
                    newGrid[i][j]=true;
                }
                else 
                if(numOfAliveNeighbors(i,j)>=4 && getGrid()[i][j])
                {
                    newGrid[i][j]=false;
        
                }
            }
        }
        return newGrid;// update this line, provided so that code compiles
    }

    /**
     * Updates the current grid (the grid instance variable) with the grid denoting
     * the next generation of cells computed by computeNewGrid().
     * 
     * Updates totalAliveCells instance variable
     */
    public void nextGeneration () {

        grid=computeNewGrid();

        
    }

    /**
     * Updates the current grid with the grid computed after multiple (n) generations. 
     * @param n number of iterations that the grid will go through to compute a new grid
     */
    public void nextGeneration (int n) {

        // WRITE YOUR CODE HERE
        for (int i=0; i<n;i++)
        {
            grid=computeNewGrid();
        }
    }

    /**
     * Determines the number of separate cell communities in the grid
     * @return the number of communities in the grid, communities can be formed from edges
     */
    public int numOfCommunities() {

        // WRITE YOUR CODE HERE
        WeightedQuickUnionUF wqu = new WeightedQuickUnionUF(grid.length, grid[0].length);
        /*for (int i=0;i<grid.length;i++)
        {
            for (int j=0;j<grid[0].length;j++)
            {
                wqu.union(i,j,i,j);
            }
        }*/
        for (int i = 0;i<grid.length;i++)
        {
            for (int j=0;j<grid[0].length;j++)
            {
                for (int k=i-1;k<=i+1;k++)
                {
                    for (int l=j-1;l<=j+1;l++)
                    {
                        int rowIndex=k;
                        int colIndex=l;
                        if (k<0)
                        {
                            rowIndex=grid.length-1;
                        }
                        else
                        if (k>=grid.length)
                        {
                            rowIndex=0;
                        }

                        if (l<0)
                        {
                            colIndex=grid[0].length-1;
                        }
                        else
                        if (l>=grid[0].length)
                        {
                            colIndex=0;
                        }
                        if ((grid[i][j] && grid[rowIndex][colIndex]) && (wqu.find(i,j)!=wqu.find(rowIndex,colIndex)))
                        {
                            wqu.union(i,j,rowIndex, colIndex);
        
                        }
                    }
                }
                
            }
        }
    

        ArrayList<Integer> communities = new ArrayList<Integer>();
        for (int i =0;i<grid.length;i++)
        {
            for (int j =0;j<grid[0].length;j++)
            {

                    if (grid[i][j])
                    {
                    if(communities.contains(wqu.find(i,j))==false)
                    {
                        communities.add(wqu.find(i,j));
                    }
                    }
            }
        }



        return communities.size(); // update this line, provided so that code compiles
}
}
    