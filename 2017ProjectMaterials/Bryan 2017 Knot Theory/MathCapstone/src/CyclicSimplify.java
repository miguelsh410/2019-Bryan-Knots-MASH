import java.util.Arrays;
import java.util.Scanner;
public class CyclicSimplify {	

	public static int[] sigmaOperm;

	public static void main(String[] args) {

		//Create variable for user input
		Scanner input = new Scanner(System.in);
		System.out.println("What size is the grid diagram?");

		//Get size of grid diagram from user
		int size = input.nextInt();
		int[] xPerm = new int[size];
		int[] oPerm = new int[size];

		System.out.println("Enter the X permutation: ");
		//User enters sequence of integers to represent X permutation
		for (int i = 0; i < size; i++)
		{
			xPerm[i] = input.nextInt();
		}
		System.out.println("Enter the O permutation: ");

		//User enters sequence of integers to represent O permutation
		for (int i = 0; i < size; i++)
		{
			oPerm[i] = input.nextInt();
		}

		input.close();
		printGrid(createGrid(size,xPerm,oPerm));
		//printGrid(cyclicPermutationRowTop(createGrid(size,xPerm,oPerm)));
		//printGrid(cyclicPermutationRowBottom(createGrid(size,xPerm,oPerm)));
		//printGrid(searchForStabilization(cyclicPermutationRowBottom(createGrid(size,xPerm,oPerm))));
		//printGrid(cyclicPermutationColLeft(createGrid(size,xPerm,oPerm)));
		//printGrid(cyclicPermutationColRight(createGrid(size,xPerm,oPerm)));
		//printGrid(searchForStabilization(cyclicPermutationColRight(createGrid(size,xPerm,oPerm))));
		int[] sigmaXperm = simplifyKnot(oPerm,xPerm);
		printGrid(createGrid(sigmaOperm.length,sigmaXperm,sigmaOperm));
	}

	public static int[] simplifyKnot(int[] sigmaO, int[] sigmaX)
	{
		//Create arrays for X permutation and O permutation
		int size = sigmaX.length;
		int[] xPerm = new int[size];
		int[] oPerm = new int[size];

		xPerm = sigmaX;
		oPerm = sigmaO;

		//Create 2D array from size and permutations
		String[][] grid = createGrid(size, xPerm, oPerm);


		//Print original 2D array
		//printGrid(grid);


		//Initialize variable for while loop. Used to make the program automatic.
		while (true)
		{
			//The program creates a new grid that is a copy of the original one. Then it searches to see
			//if a stabilization occurs in it. If it does, it is automatically destabilized and returned. If
			//it does not, then it is returned. Whatever is returned is stored in a new 2D array.
			String[][] matrix = searchForStabilization(createGrid(size, xPerm, oPerm));
			//The new 2D array is compared with the old one. If they are the same, then no
			//stabilization was found.
			if (Arrays.deepEquals(matrix, grid))
			{
				//The program checks to see if it can obtain a destabilization opportunity through
				//grid moves of one direction. The grid variable is reset to what is returned by
				//this function: the 2D array that is passed into it if it cannot obtain a
				//destabilization opportunity or the 2D array that has obtained a destabilization
				//opportunity
				grid = searchGridMovesOneDirection(grid);
				//Another comparison between the two 2D arrays. If they are the same, then a
				//destabilization opportunity was not obtained through grid moves of one
				//direction.
				if (Arrays.deepEquals(matrix, grid))
				{
					//The program determines if it can obtain a destabilization opportunity
					//through grid moves of two directions. The grid variable is reset to
					//what is returned by this function: the 2D array that is passed into it if
					//it cannot obtain a destabilization opportunity or the 2D array that has
					//obtained a destabilization opportunity
					grid = searchGridMovesTwoDirection(grid);
					//If both 2D arrays are the same then no stabilization was found, the program
					//moves on to the next step
					if (Arrays.deepEquals(matrix, grid))
					{
						//Cyclically permutes the top row to the bottom then searches for a destabilization.
						//The grid variable is reset to what is returned by this function: the 2D array that
						//is passed into it if it cannot obtain a destabilization opportunity after the cyclic
						//permutation or the 2D array that has obtained a destabilization after a cyclic permutation.
						grid = cyclicPermutationRowTop(grid);
						//If both 2D arrays are the same then no stabilization was found, the program
						//moves on to the next step
						if(Arrays.deepEquals(matrix, grid))
						{
							//Cyclically permutes the bottom row to the top then searches for a destabilization.
							//The grid variable is reset to what is returned by this function: the 2D array that
							//is passed into it if it cannot obtain a destabilization opportunity after the cyclic
							//permutation or the 2D array that has obtained a destabilization after a cyclic permutation.
							grid = cyclicPermutationRowBottom(grid);
							//If both 2D arrays are the same then no stabilization was found, the program
							//moves on to the next step
							if(Arrays.deepEquals(matrix, grid))
							{
								//Cyclically permutes the left column to the right then searches for a destabilization.
								//The grid variable is reset to what is returned by this function: the 2D array that
								//is passed into it if it cannot obtain a destabilization opportunity after the cyclic
								//permutation or the 2D array that has obtained a destabilization after a cyclic permutation.
								grid = cyclicPermutationColLeft(grid);
								//If both 2D arrays are the same then no stabilization was found, the program
								//moves on to the next step
								if(Arrays.deepEquals(matrix, grid))
								{
									//Cyclically permutes right column to the left then searches for a destabilization.
									//The grid variable is reset to what is returned by this function: the 2D array that
									//is passed into it if it cannot obtain a destabilization opportunity after the cyclic
									//permutation or the 2D array that has obtained a destabilization after a cyclic permutation.
									grid = cyclicPermutationColRight(grid);
									//If both 2D arrays are the same then no stabilization was found and the program
									//is done, terminate the program
									if(Arrays.deepEquals(matrix, grid))
									{
										//Perform cyclic permutations on the grid until an O is in the top left cell
										while(grid[0][0] != "O") {
											String[][] cyclicGrid =  new String[size][size];
											for(int r = 0; r < size; r++) {
												for(int c = 0; c < size; c++) {
													if(r == 0) {
														cyclicGrid[r][c] = grid[size-1][c];
													}
													else {
														cyclicGrid[r][c] = grid[r-1][c];
													}
												}
											}
											grid = cyclicGrid;
										}
										//printGrid(grid);
										
										//Get the X permutation from the grid diagram and set it equal to x and set O
										//permutation equal to sigmaOperm, then return the x permutation
										int[] x = new int[size];
										sigmaOperm = new int[size];
										for(int r = 0; r < size; r++) {
											for(int c = 0; c < size; c++) {
												if(grid[r][c] == "X") 
													x[c] = size-r;
												else if(grid[r][c] == "O")
													sigmaOperm[c] = size-r;
											}
										}
										return x;
									}
									//Otherwise, a destabilization opportunity was obtained after a cyclic 
									//permutation. The changed grid is printed, and the size, X
									//permutation, and O permutation are stored in order to call the
									//createGrid function the next time through the while loop.
									else
									{
										//printGrid(grid);
										size = grid.length;
										xPerm = findXPerm(grid);
										oPerm = findOPerm(grid);
									}
								}
								//Otherwise, a destabilization opportunity was obtained after a cyclic 
								//permutation. The changed grid is printed, and the size, X
								//permutation, and O permutation are stored in order to call the
								//createGrid function the next time through the while loop.
								else
								{
									//printGrid(grid);
									size = grid.length;
									xPerm = findXPerm(grid);
									oPerm = findOPerm(grid);
								}
							}
							//Otherwise, a destabilization opportunity was obtained after a cyclic 
							//permutation. The changed grid is printed, and the size, X
							//permutation, and O permutation are stored in order to call the
							//createGrid function the next time through the while loop.
							else
							{
								//printGrid(grid);
								size = grid.length;
								xPerm = findXPerm(grid);
								oPerm = findOPerm(grid);
							}
						}
						//Otherwise, a destabilization opportunity was obtained after a cyclic 
						//permutation. The changed grid is printed, and the size, X
						//permutation, and O permutation are stored in order to call the
						//createGrid function the next time through the while loop.
						else 
						{
							//printGrid(grid);
							size = grid.length;
							xPerm = findXPerm(grid);
							oPerm = findOPerm(grid);
						}
						//************************************************************************************************
					}
					//Otherwise, a destabilization opportunity was obtained using grid
					//moves in both directions. The changed grid is printed, and the size, X
					//permutation, and O permutation are stored in order to call the
					//createGrid function the next time through the while loop.
					else
					{
						//printGrid(grid);
						size = grid.length;
						xPerm = findXPerm(grid);
						oPerm = findOPerm(grid);
					}
				}
				//If the 2D arrays are different, then a destabilization opportunity was obtained
				//using grid moves in one direction. The changed grid is printed, and the size, X
				//permutation, and O permutation are stored in order to call the createGrid
				//function the next time through the while loop.
				else
				{
					//printGrid(grid);
					//System.out.println("8");
					size = grid.length;
					xPerm = findXPerm(grid);
					oPerm = findOPerm(grid);
				}
			}
			//If the 2D arrays are different, then a stabilization was found and the original 2D array
			//gets set to the changed one. The original grid (now with the value of the changed grid)
			//is printed, and the size, X permutation, and O permutation are stored in order to call the
			//createGrid function the next time through the while loop.
			else
			{
				grid = matrix;
				//printGrid(grid);
				//System.out.println("9");
				size = grid.length;
				xPerm = findXPerm(grid);
				oPerm = findOPerm(grid);
			}
		}
	}


	//function to determine if grid moves in one direction will lead to a destabilization opportunity
	private static String[][] searchGridMovesOneDirection(String[][] grid)
	{
		//loop through the 2D array
		for (int i = 0; i < grid.length; i++)
		{
			for (int j = 0; j < grid[0].length; j++)
			{
				//prevents out of bounds error
				if (j > 0)
				{
					//if current cell is O and the cell to its immediate left is X, then
					//automatically do row grid moves to bring the O's row adjacent to the
					//row of the X in the O's column
					if (grid[i][j].equals("O") && grid[i][j-1].equals("X"))
					{
						grid = doGridMovesRow(grid, i, j);
						return grid;
					}
				}
				//prevents out of bounds error
				if (j < grid[0].length - 1)
				{
					//if current cell is O and the cell to its immediate right is X, then
					//automatically do row grid moves to bring the O's row adjacent to the
					//row of the X in the O's column
					if (grid[i][j].equals("O") && grid[i][j+1].equals("X"))
					{
						grid = doGridMovesRow(grid, i, j);
						return grid;
					}
				}
				//prevents out of bounds error
				if (i > 0)
				{
					if (grid[i][j].equals("O") && grid[i-1][j].equals("X"))
					{
						//if current cell is O and the cell immediately above it is X,
						//then automatically do column grid moves to bring the O's
						//column adjacent to the column of the X in the O's row
						grid = doGridMovesCol(grid, i, j);
						return grid;
					}
				}
				//prevents out of bounds error
				if (i < grid[0].length - 1)
				{
					//if current cell is O and the cell immediately below it is X, then
					//automatically do column grid moves to bring the O's column adjacent
					//to the column of the X in the O's row
					if (grid[i][j].equals("O") && grid[i+1][j].equals("X"))
					{
						grid = doGridMovesCol(grid, i, j);
						return grid;
					}
				}
			}
		}
		return grid;
		//will return original grid if no grid moves in one direction are possible
	}


	//function to automatically use row grid moves to bring the row of the O adjacent to the row of the X in the
	//O's column
	private static String[][] doGridMovesRow(String[][] grid, int oRowPos, int oColPos)
	{
		//set variable equal to the row of the X in the O's column
		int xRowPos = findXVertically(grid, oColPos);
		//check if the X is above the O
		if (xRowPos < oRowPos)
		{
			//Loop from the row above the O to the row below the X and call the function to swap the
			//current row with the one below it. This will bring the O adjacent to the X.
			for (int k = oRowPos - 1; k > xRowPos; k--)
			{
				grid = swapRows(grid, k);
			}
		}
		//if the X is below the O
		else
		{
			//Loop from the O's row to the row that is two above the X, and call the function to swap
			//the current row with the one below it. This will bring the O adjacent to the X.
			for (int k = oRowPos; k < xRowPos - 1; k++)
			{
				grid = swapRows(grid, k);
			}
		}
		return grid;
		//return the manipulated 2D array representing the grid diagram
	}


	//function to automatically use column grid moves to bring the column of the O adjacent to the column of
	//the X in the O's row
	private static String[][] doGridMovesCol(String[][] grid, int oRowPos, int oColPos)
	{
		//set variable equal to the column of the X in the O's row
		int xColPos = findXHorizontally(grid, oRowPos);
		//check if the X is to the left of the O
		if (xColPos < oColPos)
		{
			//Loop from the column to the left of the O to the column to the right of the X and call the
			//function to swap the current column with the one to its right. This will bring the O
			//adjacent to the X.
			for (int k = oColPos - 1; k > xColPos; k--)
			{
				grid = swapCols(grid, k);
			}
		}
		//if the X is to the right of the O
		else
		{
			//Loop from the O's column to two columns to the left of the X and call the function to
			//swap the current column with the one to its right. This will bring the O adjacent to the
			//X.
			for (int k = oColPos; k < xColPos - 1; k++)
			{
				grid = swapCols(grid, k);
			}
		}
		return grid;
		//return the manipulated 2D array representing the grid diagram
	}


	//function to determine if valid grid moves in both directions can lead to a destabilization opportunity
	private static String[][] searchGridMovesTwoDirection(String[][] grid)
	{
		//loop through the 2D array
		for (int i = 0; i < grid.length; i++)
		{
			for (int j = 0; j < grid[0].length; j++)
			{
				//if the current cell is O
				if (grid[i][j].equals("O"))
				{
					//Create a new grid that is a copy of the grid passed into the function
					//Call a function to see if valid row grid moves can bring the X in the
					//O's column adjacent to the O or if valid row grid moves can bring the
					//O adjacent to X in the O's column. If valid row grid moves can bring
					//the X and O together, do them and return the manipulated 2D array.
					String[][] newGrid = checkGridMovesRow(createGrid(grid.length,
							findXPerm(grid), findOPerm(grid)), i, j);
					//If the original grid does not equal the new grid, then valid row grid
					//moves were possible, and we now need to bring the O adjacent to the
					//X in the O's row
					if (!Arrays.deepEquals(grid, newGrid))
					{
						//find the row of the O in the current column (this is necessary
						//because the O's row could have changed)
						int newORow = findNewORow(newGrid, j);
						//Call function to bring O adjacent to the X in the O's row
						newGrid = doGridMovesCol(newGrid, newORow, j);
						//return the changed grid diagram
						return newGrid;
					}
					//the original grid equals the new grid so valid row grid moves were not
					//possible
					else
					{
						//Create a new grid that is a copy of the grid passed into the
						//function. Call a function to check if valid column grid moves
						//can bring the X in the O's row adjacent to the O or if valid
						//column grid moves can bring the O adjacent to the X in the
						//O's row
						newGrid = checkGridMovesCol(createGrid(grid.length,
								findXPerm(grid), findOPerm(grid)), i, j);
						//If the original grid does not equal the new grid, then valid
						//column grid moves were possible, and we now need to bring
						//the O adjacent to the X in the O's column
						if (!Arrays.deepEquals(grid, newGrid))
						{
							//find the column of the O in the current row (this is
							//necessary because the O's column could have
							//changed)
							int newOCol = findNewOCol(newGrid, i);
							//Call function to bring O adjacent to the X in the O's
							//column
							newGrid = doGridMovesRow(newGrid, i, newOCol);
							//return the changed grid diagram
							return newGrid;
						}
					}
				}
			}
		}
		//return original grid diagram if cannot obtain destabilization opportunity
		return grid;
	}





	//function to find which column the O is currently in
	private static int findNewOCol(String[][] grid, int oRow)
	{
		//search through the row of the O and return the column position if the current element is O
		for (int k = 0; k < grid.length; k++)
		{
			if (grid[oRow][k].equals("O"))
			{
				return k;
			}
		}
		//mandatory return statement
		return -1;
	}


	//function to find which row the O is currently in
	private static int findNewORow(String[][] grid, int oCol)
	{
		//search through the column of the O and return the row position if the current element is O
		for (int k = 0; k < grid[0].length; k++)
		{
			if (grid[k][oCol].equals("O"))
			{
				return k;
			}
		}
		//mandatory return statement
		return -1;
	}


	//function to determine if valid row grid moves can bring an O adjacent to the X in its column or vice versa
	private static String[][] checkGridMovesRow(String[][] originalGrid, int oRowPos, int oColPos)
	{
		//create variable containing the row of the X in the O's column
		int xRowPos = findXVertically(originalGrid, oColPos);
		//create a new 2D array that is a copy of the 2D array that has been passed in
		String[][] changingGrid = createGrid(originalGrid.length, findXPerm(originalGrid),
				findOPerm(originalGrid));
		//check if the X is above the O
		if (xRowPos < oRowPos)
		{
			//loop from the X to two rows above the O
			for (int k = xRowPos; k < oRowPos-1; k++)
			{
				//if no cross-commutation is found between the current row and the row below
				//it, then swap the two rows
				if (checkCrossComRow(changingGrid, k))
				{
					swapRows(changingGrid, k);
				}
				//if a cross-commutation is found
				else
				{
					//loop from the row above the O to the row below the X
					for (int m = oRowPos-1; m > xRowPos; m--)
					{
						//if no cross-commutation is found between the current row
						//and the row below it, then swap the two rows
						if (checkCrossComRow(changingGrid, m))
						{
							swapRows(changingGrid, m);
						}
						//if a cross-commutation is found, return the original grid
						else
						{
							return originalGrid;
						}
					}
					//if a cross commutation is not found, break out of the for-loop that runs
					//from X to two rows above the O because we will have already
					//swapped the rows to bring the O adjacent to the X in its column
					break;
				}
			}
		}
		//if the X is below the O
		else
		{
			//Loop from the O to two rows above the X
			for (int k = oRowPos; k < xRowPos-1; k++)
			{
				//if no cross-commutation is found between the current row and the row below
				//it, then swap the two rows
				if (checkCrossComRow(changingGrid, k))
				{
					swapRows(changingGrid, k);
				}
				//if a cross-commutation is found
				else
				{
					//loop from the row above the X to the row below the O
					for (int m = xRowPos - 1; m > oRowPos; m--)
					{
						//if no cross-commutation is found between the current row
						//and the row below it, then swap the two rows
						if (checkCrossComRow(changingGrid,m))
						{
							swapRows(changingGrid, m);
						}
						//if a cross-commutation is found, return the original grid
						else
						{
							return originalGrid;
						}
					}
					//if a cross commutation is not found, break out of the for-loop that runs
					//from O to two rows above the X because we will have already
					//swapped the rows to bring the O adjacent to the X in its column
					break;
				}
			}
		}
		//return the changed grid
		return changingGrid;
	}
	
	
	//function to determine if valid column grid moves can bring an O adjacent to the X in its column or vice
	//versa
	private static String[][] checkGridMovesCol(String[][] originalGrid, int oRowPos, int oColPos)
	{
		//Create variable containing the column of the X in the O's row
		int xColPos = findXHorizontally(originalGrid, oRowPos);
		//Create a new 2D array that is a copy of the 2D array that has been passed in
		String[][] changingGrid = createGrid(originalGrid.length, findXPerm(originalGrid),
				findOPerm(originalGrid));
		//Check if the X is to the left of the O
		if (xColPos < oColPos)
		{
			//Loop from the X to two columns to the left of the O
			for (int k = xColPos; k < oColPos-1; k++)
			{
				//If no cross-commutation is found between the current column and the column
				//to its right, then swap the two columns
				if (checkCrossComCol(changingGrid, k))
				{
					swapCols(changingGrid, k);
				}
				//If a cross-commutation is found
				else
				{
					//Loop from the column to the left of the O to the column to the right of
					//the X
					for (int m = oColPos-1; m > xColPos; m--)
					{
						//If no cross-commutation is found between the current
						//column and the column to its right, then swap the two
						//columns
						if (checkCrossComCol(changingGrid, m))
						{
							swapCols(changingGrid, m);
						}
						//If a cross-commutation is found, return the original grid
						else
						{
							return originalGrid;
						}
					}
					//If a cross commutation is not found, break out of the for-loop that
					//runs from X to two columns to the left of the O because we will have
					//already swapped the columns to bring the O adjacent to the X in its
					//row
					break;
				}
			}
		}
		//If the X is to the right of the O
		else
		{
			//Loop from the O to two columns to the left of the X
			for (int k = oColPos; k < xColPos-1; k++)
			{
				//If no cross-commutation is found between the current column and the column
				//to its right, then swap the two columns
				if (checkCrossComCol(changingGrid, k))
				{
					swapCols(changingGrid, k);
				}
				//If a cross-commutation is found
				else
				{
					//Loop from the column to the left of the X to the column to the right of
					//the O
					for (int m = xColPos - 1; m > oColPos; m--)
					{
						//If no cross-commutation is found between the current
						//column and the column to its right, then swap the two
						//columns
						if (checkCrossComCol(changingGrid,m))
						{
							swapCols(changingGrid, m);
						}
						//If a cross-commutation is found, return the original grid
						else
						{
							return originalGrid;
						}
					}
					//If a cross commutation is not found, break out of the for-loop that
					//runs from O to two columns to the left of the X because we will have
					//already swapped the columns to bring the O adjacent to the X in its
					//row
					break;
				}
			}
		}
		//Return the changed grid
		return changingGrid;
	}

	//*******************************************************************************************************************************************
		//function to determine if valid row grid moves can bring an O adjacent to the X in its column or vice versa
		private static String[][] cyclicPermutationRowTop(String[][] originalGrid)
		{
			int size = originalGrid.length;
			String[][] cyclicGrid =  new String[size][size];
			for(int r = 0; r < size; r++) 
			{
				for(int c = 0; c < size; c++) 
				{
					if(r == size-1) 
					{
						cyclicGrid[r][c] = originalGrid[0][c];
					}
					else 
					{
						cyclicGrid[r][c] = originalGrid[r+1][c];
					}
				}
			}
			String[][] newGrid = searchGridMovesOneDirection(cyclicGrid);
			
			if(Arrays.deepEquals(newGrid, cyclicGrid))
			{
				newGrid = searchGridMovesTwoDirection(cyclicGrid);
				if(Arrays.deepEquals(newGrid, cyclicGrid))
				{
					return originalGrid;
				}
				else
				{
					return newGrid;
				}
			}
			else
			{
				return newGrid;
			}		
		}
		//***************************************************************************************************************************
	
	//*******************************************************************************************************************************************
	//function to determine if valid row grid moves can bring an O adjacent to the X in its column or vice versa
	private static String[][] cyclicPermutationRowBottom(String[][] originalGrid)
	{
		int size = originalGrid.length;
		String[][] cyclicGrid =  new String[size][size];
		for(int r = 0; r < size; r++) 
		{
			for(int c = 0; c < size; c++) 
			{
				if(r == 0) 
				{
					cyclicGrid[r][c] = originalGrid[size-1][c];
				}
				else 
				{
					cyclicGrid[r][c] = originalGrid[r-1][c];
				}
			}
		}
		String[][] newGrid = searchGridMovesOneDirection(cyclicGrid);
		
		if(Arrays.deepEquals(newGrid, cyclicGrid))
		{
			newGrid = searchGridMovesTwoDirection(cyclicGrid);
			if(Arrays.deepEquals(newGrid, cyclicGrid))
			{
				return originalGrid;
			}
			else
			{
				return newGrid;
			}
		}
		else
		{
			return newGrid;
		}		
	}
	//***************************************************************************************************************************

	//*******************************************************************************************************************************************
			//function to determine if valid row grid moves can bring an O adjacent to the X in its column or vice versa
			private static String[][] cyclicPermutationColLeft(String[][] originalGrid)
			{
				int size = originalGrid.length;
				String[][] cyclicGrid =  new String[size][size];
				for(int r = 0; r < size; r++) 
				{
					for(int c = 0; c < size; c++) 
					{
						if(c == size-1) 
						{
							cyclicGrid[r][c] = originalGrid[r][0];
						}
						else 
						{
							cyclicGrid[r][c] = originalGrid[r][c+1];
						}
					}
				}
				String[][] newGrid = searchGridMovesOneDirection(cyclicGrid);
				
				if(Arrays.deepEquals(newGrid, cyclicGrid))
				{
					newGrid = searchGridMovesTwoDirection(cyclicGrid);
					if(Arrays.deepEquals(newGrid, cyclicGrid))
					{
						return originalGrid;
					}
					else
					{
						return newGrid;
					}
				}
				else
				{
					return newGrid;
				}		
			}
			//***************************************************************************************************************************
		
		//*******************************************************************************************************************************************
		//function to determine if valid row grid moves can bring an O adjacent to the X in its column or vice versa
		private static String[][] cyclicPermutationColRight(String[][] originalGrid)
		{
			int size = originalGrid.length;
			String[][] cyclicGrid =  new String[size][size];
			for(int r = 0; r < size; r++) 
			{
				for(int c = 0; c < size; c++) 
				{
					if(c == 0) 
					{
						cyclicGrid[r][c] = originalGrid[r][size-1];
					}
					else 
					{
						cyclicGrid[r][c] = originalGrid[r][c-1];
					}
				}
			}
			String[][] newGrid = searchGridMovesOneDirection(cyclicGrid);

			if(Arrays.deepEquals(newGrid, cyclicGrid))
			{
				newGrid = searchGridMovesTwoDirection(cyclicGrid);
				if(Arrays.deepEquals(newGrid, cyclicGrid))
				{
					return originalGrid;
				}
				else
				{
					return newGrid;
				}
			}
			else
			{
				return newGrid;
			}		
		}
		//***************************************************************************************************************************
	
	//Function to swap the elements of two rows - the row of the second parameter and the row below it
	private static String[][] swapRows(String[][] grid, int topRow)
	{
		//Loop through all of the columns
		for (int j = 0; j < grid[0].length; j++)
		{
			//Temporary variable to store the value of the cell at the top row and current column
			String temp = grid[topRow][j];
			//Set the cell at the top row and current column to the value of the cell at the bottom row
			//and the current column
			grid[topRow][j] = grid[topRow+1][j];
			//Set the cell at the bottom row and current column to the value of the temporary variable
			grid[topRow+1][j] = temp;
		}
		//Return the changed grid
		return grid;
	}
	//Function to swap the elements of two columns - the column of the second parameter and the column to
	//its right


	private static String[][] swapCols(String[][] grid, int leftCol)
	{
		//loop through all of the rows
		for (int i = 0; i < grid.length; i++) //going through all the rows
		{
			//Temporary variable to store the value of the cell at the current row and left column
			String temp = grid[i][leftCol];
			//Set the cell at the current row and left column to the value of the cell at the current row
			//and the right column
			grid[i][leftCol] = grid[i][leftCol+1];
			//Set the cell at the current row and right column to the value of the temporary variable
			grid[i][leftCol+1] = temp;
		}
		//Return the changed grid
		return grid;
	}


	//Function to find the column of the X in the row passed in
	private static int findXHorizontally(String[][] grid, int row)
	{
		//Loop through the columns
		for (int j = 0; j < grid[0].length; j++)
		{
			//If the row and current column equal X, return the current column position
			if (grid[row][j].equals("X"))
			{
				return j;
			}
		}
		//Mandatory return statement
		return -1;
	}


	//Function to find the row of the X in the column passed in
	private static int findXVertically(String[][] grid, int col)
	{
		//Loop through the rows
		for (int i = 0; i < grid.length; i++)
		{
			//If the current row and column equal X, return the current row position
			if (grid[i][col].equals("X"))
			{
				return i;
			}
		}
		//Mandatory return statement
		return -1;
	}
	//Function to check if there is a cross-commutation between two rows - the row passed in and the row
	//below it


	private static boolean checkCrossComRow(String[][] grid, int topRow)
	{
		//Initialize 4 variables for the leftmost marking in the top row, the rightmost marking in the top
		//row, the leftmost marking in the bottom row, and the rightmost marking in the bottom row,
		//respectively
		int leftTop = -1, rightTop = -1, leftBot = -1, rightBot = -1;
		//Loop through each column
		for (int j = 0; j < grid[0].length; j++)
		{
			//If the leftmost marking in the top row has not been set and the current column of the top
			//row is X or O, set the variable for the leftmost marking in the top row equal to the
			//current column
			if ((grid[topRow][j].equals("X") || grid[topRow][j].equals("O")) && leftTop == -1)
			{
				leftTop = j;
			}
			//If the leftmost marking in the top row has been set and the current column of the top
			//row is X or O, set the variable for the rightmost marking in the top row equal to the
			//current column
			else if ((grid[topRow][j].equals("X") || grid[topRow][j].equals("O")) && leftTop != -1)
			{
				rightTop = j;
			}
			//If the leftmost marking in the bottom row has not been set and the current column of
			//the top row is X or O, set the variable for the leftmost marking in the bottom row equal
			//to the current column
			if ((grid[topRow+1][j].equals("X") || grid[topRow+1][j].equals("O")) && leftBot == -1)
			{
				leftBot = j;
			}
			//If the leftmost marking in the bottom row has been set and the current column of the top
			//row is X or O, set the variable for the rightmost marking in the bottom row equal to the
			//current column
			else if((grid[topRow+1][j].equals("X") || grid[topRow+1][j].equals("O")) && leftBot
					!= -1)
			{
				rightBot = j;
			}
		}
		//Based on the positions of each of the four variables in relation to each other, the program returns
		//true if no cross-commutation was found and it returns false if a cross-commutation was found
		if (!(leftTop < leftBot && rightTop > leftBot && rightTop < rightBot) && !(rightTop > rightBot
				&& leftTop < rightBot && leftTop > leftBot))
		{
			return true;
		}
		return false;
	}


	//Function to check if there is a cross-commutation between two columns - the column passed in and the
	//column to its right
	private static boolean checkCrossComCol(String[][] grid, int leftCol)
	{
		//Initialize 4 variables for the highest marking in the left column, the lowest marking in the left
		//column, the highest marking in the right column, and the lowest marking in the right column,
		//respectively
		int topLeft = -1, botLeft = -1, topRight = -1, botRight = -1;
		//Loop through each row
		for (int i = 0; i < grid.length; i++)
		{
			//If the highest marking in the left column has not been set and the current row of the left
			//column is X or O, set the variable for the highest marking in the left column equal to the
			//current row
			if ((grid[i][leftCol].equals("X") || grid[i][leftCol].equals("O")) && topLeft == -1)
			{
				topLeft = i;
			}
			//If the highest marking in the left column has been set and the current row of the left
			//column is X or O, set the variable for the lowest marking in the left column equal to the
			//current row
			else if ((grid[i][leftCol].equals("X") || grid[i][leftCol].equals("O")) && topLeft != -1)
			{
				botLeft = i;
			}
			//If the highest marking in the right column has not been set and the current row of the
			//right column is X or O, set the variable for the highest marking in the right column
			//equal to the current row
			if ((grid[i][leftCol+1].equals("X") || grid[i][leftCol+1].equals("O")) && topRight == -1)
			{
				topRight = i;
			}
			//If the highest marking in the right column has been set and the current row of the right
			//column is X or O, set the variable for the lowest marking in the right column equal to
			//the current row
			if ((grid[i][leftCol+1].equals("X") || grid[i][leftCol+1].equals("O")) && topRight != -1)
			{
				botRight = i;
			}
		}
		//Based on the positions of each of the four variables in relation to each other, the program returns
		//true if no cross-commutation was found and it returns false if a cross-commutation was found
		if (!(topLeft < topRight && botLeft > topRight && botLeft < botRight) && !(topRight < topLeft
				&& botRight > topLeft && botRight < botLeft))
		{
			return true;
		}
		return false;
	}


	//Function to determine if there are any stabilizations in the 2D array
	private static String[][] searchForStabilization(String[][] grid)
	{
		//Loop through the 2D array
		for (int i = 0; i < grid.length; i++)
		{
			for (int j = 0; j < grid[0].length; j++)
			{
				//Prevents out of bounds error
				if (i > 0)
				{
					//Prevents out of bounds error
					if (j < grid[0].length - 1)
					{
						//If current cell is O, cell directly above it is X, cell directly to
						//the right is X, and cell to the upper right diagonal is -, then a
						//"NE" stabilization is found, and the indices of the - are
						//stored. The grid is then destabilized and returned.
						if (grid[i][j].equals("O") && grid[i-1][j].equals("X") &&
								grid[i][j+1].equals("X") && grid[i-1][j+1].equals("-"))
						{
							grid = destabilize(grid, "NE", i-1, j+1);
							return grid;
						}
					}
					//Prevents out of bounds error
					if (j > 0)
					{
						//If current cell is O, cell directly above it is X, cell directly to
						//the left is X, and cell to the upper left diagonal is -, then a
						//"NW" stabilization is found, and the indices of the - are
						//stored. The grid is then destabilized and returned.
						if (grid[i][j].equals("O") && grid[i-1][j].equals("X") &&
								grid[i][j-1].equals("X") && grid[i-1][j-1].equals("-"))
						{
							grid = destabilize(grid, "NW", i-1, j-1);
							return grid;
						}
					}
				}
				//Prevents out of bounds error
				if (i < grid.length - 1)
				{
					//Prevents out of bounds error
					if (j < grid[0].length - 1)
					{
						//If current cell is O, cell directly below it is X, cell directly to
						//the right is X, and cell to the lower right diagonal is -, then a
						//"SE" stabilization is found, and the indices of the - are
						//stored. The grid is then destabilized and returned.
						if (grid[i][j].equals("O") && grid[i+1][j].equals("X") &&
								grid[i][j+1].equals("X") && grid[i+1][j+1].equals("-"))
						{
							grid = destabilize(grid, "SE", i+1, j+1);
							return grid;
						}
					}
					//Prevents out of bounds error
					if (j > 0)
					{
						//If current cell is O, cell directly below it is X, cell directly to
						//the left is X, and cell to the lower left diagonal is -, then a
						//"SW" stabilization is found, and the indices of the - are
						//stored. The grid is then destabilized and returned.
						if (grid[i][j].equals("O") && grid[i+1][j].equals("X") &&
								grid[i][j-1].equals("X") && grid[i+1][j-1].equals("-"))
						{
							grid = destabilize(grid, "SW", i+1, j-1);
							return grid;
						}
					}
				}
			}
		}
		//Returns the original grid if no stabilizations are detected anywhere
		return grid;
	}


	//Function to create grid diagrams
	public static String[][] createGrid(int size, int[] xPerm, int[] oPerm)
	{
		//Initialize 2D array of size that is passed in
		String[][] grid = new String[size][size];
		//Loop through each row and fill the array in each row with -'s
		for (int i = 0; i < size; i ++)
		{
			Arrays.fill(grid[i], "-");
		}
		//Loop through each permutation and appropriately assign the X's and O's according to the X
		//permutations. The subtractions are there to account for the numbering convention of grid
		//diagrams.
		for (int i = 0; i < size; i++)
		{
			grid[size - xPerm[i]][i] = "X";
			grid[size - oPerm[i]][i] = "O";
		}
		//Return the created 2D array
		return grid;
	}


	//Function to print the grid diagram
	public static void printGrid(String[][] grid)
	{
		//Use two for each loops, one to get each row, and another to get each row's element
		for (String[] line : grid)
		{
			for (String element : line)
			{
				//Separate each element with a space
				System.out.print(element + " ");
			}
			//Separate each line with a carriage return
			System.out.println();
		}
		//Create a space between each time a grid is printed
		System.out.println();
	}


	//Function to destabilize a grid diagram
	private static String[][] destabilize(String[][] grid, String direction, int blankPosX, int blankPosY)
	{
		//Create a new 2D array of size n - 1, where n is the size of the grid diagram being destabilized
		String[][] matrix = new String[grid.length - 1][grid[0].length - 1];
		//Create iterator variables for the new 2D array when copying elements over
		int matrixPosX = 0;
		int matrixPosY = 0;
		//Initialize variables of the row/column to remove
		int removeRowPos = -1;
		int removeColPos = -1;
		//Initialize variables of where to place the X at the end
		int addXRow = -1;
		int addXCol = -1;
		//If 'NE' stabilization, then the row to remove is the one below the '-' involved, the column to
		//remove is the one to the left of the '-', the row where the X is placed is the same as the '-', and the
		//column where the X is placed is the one to the left of the '-'
		if (direction.equals("NE"))
		{
			removeRowPos = blankPosX + 1;
			removeColPos = blankPosY - 1;
			addXRow = blankPosX;
			addXCol = blankPosY - 1;
		}
		//If 'NW' stabilization, then the row to remove is the one below the '-' involved, the column to
		//remove is the one to the right of the '-', the row where the X is placed is the same as the '-', and
		//the column where the X is placed is the same as the '-'
		else if (direction.equals("NW"))
		{
			removeRowPos = blankPosX + 1;
			removeColPos = blankPosY + 1;
			addXRow = blankPosX;
			addXCol = blankPosY;
		}
		//If 'SE' stabilization, then the row to remove is the one above the '-' involved, the column to
		//remove is the one to the left of the '-', the row where the X is placed is the one above the '-', and
		//the column where the X is placed is the one to the left of the '-'
		else if (direction.equals("SE"))
		{
			removeRowPos = blankPosX - 1;
			removeColPos = blankPosY - 1;
			addXRow = blankPosX - 1;
			addXCol = blankPosY - 1;
		}
		//If 'SW' stabilization, then the row to remove is the one above the '-' involved, the column to
		//remove is the one to the right of the '-', the row where the X is placed is the one above the '-', and
		//the column where the X is placed is the same as the '-'
		else
		{
			removeRowPos = blankPosX - 1;
			removeColPos = blankPosY + 1;
			addXRow = blankPosX - 1;
			addXCol = blankPosY;
		}
		//loop through the original 2D array
		for (int i = 0; i < grid.length; i++)
		{
			//ignore the row to be removed
			if (i != removeRowPos)
			{
				for (int j = 0; j < grid[0].length; j++)
				{
					//ignore the column to be removed
					if (j != removeColPos)
					{
						//copy the current element of the original 2D array to the
						//current position of the new 2D array
						matrix[matrixPosX][matrixPosY] = grid[i][j];
						//increase column position of new 2D array
						matrixPosY++;
					}
				}
				//Increase row position of new 2D array
				matrixPosX++;
				//Reset column position of new 2D array to 0, so it starts at the left column of
				//the new row
				matrixPosY = 0;
			}
		}
		//Place the new X where it needs to go
		matrix[addXRow][addXCol] = "X";
		//Return the new 2D array
		return matrix;
	}


	//Function to find the X permutation of a given grid diagram
	private static int[] findXPerm(String[][] grid)
	{
		//Create new array
		int[] xPerm = new int[grid.length];
		//Initial position of array is the first element
		int xPermPos = 0;
		//Loop through 2D array
		for (int i = 0; i < grid.length; i++)
		{
			for (int j = 0; j < grid[0].length; j++)
			{
				//If current element is X, place the appropriate value in the array and increase
				//array position variable. Subtraction is to account for numbering convention of
				//grid diagrams.
				if (grid[j][i].equals("X"))
				{
					xPerm[xPermPos] = grid.length - j;
					xPermPos++;
				}
			}
		}
		//Return the array representing the X permutation
		return xPerm;
	}


	//Function to find the X permutation of a given grid diagram
	private static int[] findOPerm(String[][] grid)
	{
		//Create new array
		int[] oPerm = new int[grid.length];
		//Initial position of array is the first element
		int oPermPos = 0;
		//Loop through 2D array
		for (int i = 0; i < grid.length; i++)
		{
			for (int j = 0; j < grid[0].length; j++)
			{
				//If current element is O, place the appropriate value in the array and increase
				//array position variable. Subtraction is to account for numbering convention of grid diagrams.
				if (grid[j][i].equals("O"))
				{
					oPerm[oPermPos] = grid.length - j;
					oPermPos++;
				}
			}
		}
		//Return the array representing the X permutation
		return oPerm;
	}
}