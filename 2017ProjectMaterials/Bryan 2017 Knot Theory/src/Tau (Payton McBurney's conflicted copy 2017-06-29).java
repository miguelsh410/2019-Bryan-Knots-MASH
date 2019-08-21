public class Tau {
	public static void main(String[] args) {
		int[] sigmaX = {3,2,1,5,4};

		// Calls the function tauInvariant and prints
		float tau = tauInvariant(sigmaX);
		System.out.println(tau);
		System.out.println("This is a test.");
		System.out.println("this is another test");
	}
	
	public static float tauInvariant(int[] sigmaX) {
		//Initialize size to size of sigmaX
		int size = sigmaX.length;
		//Initialize tau to 0
		float tau = 0;
		//Create sigmaO's on the diagonal
		int[] sigmaO = new int[size];
		for(int i = 0; i < size; i++) {
			sigmaO[i] = size - i;
		}
		
		//Create grid using X and O permutations, based on the length of sigmaX
		String[][] grid = Simplify.createGrid(sigmaX.length, sigmaX, sigmaO);
		
		
		//Changes the numbers on the O-permutation to number them correctly (count from bottom to top)
		for(int i = 0; i < size; i++) {
			sigmaO[i] = i-size;
		}
		
		//Counts the number of X's to the bottom left of little x's
		//Two nested loops that search through the rows and columns of the grid
		//Since the little x points are difficult to place, if the column is one less than the row,
		//then call the bottomleft function and add one to leftcountxX
		float leftcountxX = 0;
		for(int r = 0; r < size; r++) {
			for(int c = 0; c < size; c++) {
				if (r==c-1) {
					leftcountxX += bottomLeft(grid,r,c,"X");
				}
			}
		}
		
		//Counts the number of X's to the top right of little x's
		//This loop works the same as the leftcountxX loop, except uses the column one above the row and 
		//calls the function topright and adds one to rightcountxX
		float rightcountxX = 0;
		for(int r = 0; r < size; r++) {
			for(int c = 0; c < size; c++) {
				if (r==c+1) {
					rightcountxX += topRight(grid,r,c,"X");
				}
			}
		}
		
		//Total the amount of X's to the top right and bottom left of little x's
		float countxX = leftcountxX + rightcountxX + size;
		
		//The number of O's to the bottom left of little x's will always be equal to the grid size
		float countxO = size;
				
		//Counts the number of X's to the top right and bottom left of all X's
		//The following two for loops search through the rows and columns until it finds an X.
		//Once found, the topright and bottomleft functions are called to look for other X's to
		//the top right and bottom left of the initial X. Then cycles through all of the X markings.
		float countXX = 0;
		for(int r = 0; r < size; r++) {
			for(int c = 0; c < size; c++) {
				if (grid[r][c].equals("X")) {
					countXX += topRight(grid,r,c,"X");
					countXX += bottomLeft(grid,r,c,"X");
				}
			}
		}
		//System.out.println(countXX);
		
/*		
**************** THE FOLLOWING TWO COUNTS WILL ALWAYS BE EQUAL, AND CANCEL OUT ************** 
		//Counts the number of X's to the top right and bottom left of all O's
		//Runs the same way as the previous loops
		float countXO = 0;
		for(int r = 0; r < size; r++) {
			for(int c = 0; c < size; c++) {
				if (grid[r][c].equals("X")) {
					countXO += topRight(grid,r,c,"O");
					countXO += bottomLeft(grid,r,c,"O");
				}
			}
		}
		//System.out.println(countXO);
		
		//Counts the number of O's to the top right and bottom left of all X's
		//Runs the same way as the previous loops
		float countOX = 0;
		for(int r = 0; r < size; r++) {
			for(int c = 0; c < size; c++) {
				if (grid[r][c].equals("O")) {
					countOX += topRight(grid,r,c,"X");
					countOX += bottomLeft(grid,r,c,"X");
				}
			}
		}
		//System.out.println(countOX);
*********************************************************************************************
*/
		
		//Calculates and returns tau
		tau = countxX/2 - countxO/2 - countXX/4 - ((float) size-1)/2;
		return tau;
	}
	
	//Function that counts the number of Strings to the top right 
	public static int topRight(String[][] grid, int row, int col, String lookFor) {
		int size = grid.length;
		//Initialize the number of Strings to the top right of starting position to 0
		int rightcount = 0;
		//Goes through the grid
		for(int r = 0; r < size; r++) {
			for(int c = 0; c < size; c++) {
				//Increases rightcount by 1 if the current position is to the top right of starting position and contains lookFor
				if(r < row && c > col && grid[r][c].equals(lookFor))
					rightcount++;
			}
		}
		return rightcount;
	}
	
	//Function that counts the number of items to the bottom left
	public static int bottomLeft(String[][] grid, int row, int col, String lookFor) {
		int size = grid.length;
		//Initialize the number of Strings to the bottom left of starting position to 0
		int leftcount = 0;
		//Goes through the grid
		for(int r = 0; r < size; r++) {
			for(int c = 0; c < size; c++) {
				//Increases leftcount by 1 if the current position is to the bottom left of starting position and contains lookFor
				if(r > row && c < col && grid[r][c].equals(lookFor))
					leftcount++;
			}
		}
		return leftcount;
	}
}
