import java.util.Arrays;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

public class DiagonalKnotGrids {

	public static ArrayList<int[]> sigmaXList = new ArrayList<int[]>();
	public static ArrayList<int[]> simpleSigmaXList = new ArrayList<int[]>();
	public static ArrayList<int[]> simpleSigmaOList = new ArrayList<int[]>();
	public static ArrayList<Integer> tauList = new ArrayList<Integer>();
	public static int[] numSizes;
	public static int[] tauSizes;
	public static ArrayList<Integer> rowRemoved = new ArrayList<Integer>();
	public static ArrayList<Integer> colRemoved = new ArrayList<Integer>();
	public static String[][] grid;
	
	public static void diagonalKnots(int size) {
		
		//Initializes grid to the correct size
		grid = new String[size][size];
		
		//Fills diagonal of grid with "O"s
		for(int r = 0; r < size; r++) {
			for(int c = 0; c < size; c++) {
				if(r == c) {
					grid[r][c] = "O";
				}
				else {
					grid[r][c] = "-";
				}
			}
		}
		
		//Creates the grid we will be putting into recursionHelper with the top row 
		//removed and fills it with O's
		String[][] startGrid = new String[size-1][size];
		for(int r = 0; r < size-1; r++) {
			for(int c = 0; c < size; c++) {
				if(r == c-1) {
					startGrid[r][c] = "O";
				}
				else {
					startGrid[r][c] = "-";
				}
			}
		}
		rowRemoved.add(0);
		
		PrintWriter writer = null;
		try{
			writer = new PrintWriter("New_X_Permutations_Size_" + size, "UTF-8");	
		} catch (IOException e) {
			System.out.println("There is an error!");
		}
		
		diagonalKnotsHelper(startGrid, writer);
		
		tauSizes = new int[Collections.max(tauList)+1];
		numSizes = new int[size];
		for(int i = 0; i < tauSizes.length; i++) {
			for(int j = 0; j < tauList.size(); j++) {
				if(tauList.get(j) == i) {
					tauSizes[i]++;
				}
			}
		}
		
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < simpleSigmaXList.size(); j++) {
				if(simpleSigmaXList.get(j).length == i + 1) {
					numSizes[i]++;
				}
			}
		}
		
		//Prints the arrays that contain the number of diagrams of each size and of each Tau value
				//writer.println("Number of knots with certain grid size: " + Arrays.toString(numSizes));
				//System.out.println("Number of knots with certain grid size: " + Arrays.toString(numSizes));
				//writer.println("Number of knots with certain Tau:       " + Arrays.toString(tauSizes));
				//System.out.println("Number of knots with certain Tau:       " + Arrays.toString(tauSizes));
		
		writer.close();
	}
	
	public static void diagonalKnotsHelper(String[][] smallGrid, PrintWriter file) {
		
		//Count the number of "X"s that have been added to the overall grid
		int countX = 0;
		for(int r = 0; r < grid.length; r++) {
			for(int c = 0; c < grid[0].length; c++) {
				if(grid[r][c] == "X") {
					countX++;
				}
			}
		}
		
		//If we have enough "X"s in the grid then add the sigmaX to sigmaXList
		if(countX == grid.length-1) {
			int[] sigmaX = new int[grid.length];
			int[] sigmaO = new int[grid.length];
			for(int r = 0; r < grid.length; r++) {
				for(int c = 0; c < grid[0].length; c++) {
					if(grid[r][c] == "X") {
						sigmaX[c] = r;
					}
					if(grid[r][c] == "O") {
						sigmaO[c] = r;
					}
				}
			}
			
			//Display the X permutation correctly and add to the list
			for(int i = 0; i < sigmaX.length; i++) {
				sigmaX[i] = sigmaX.length-sigmaX[i];
				sigmaO[i] = sigmaO.length-sigmaO[i];
			}
			
			int[] simpleSigmaX = CyclicSimplify.simplifyKnot(sigmaO, sigmaX);
			int[] simpleSigmaO = CyclicSimplify.sigmaOperm;
			boolean contains = false;
			for(int i = 0; i < simpleSigmaXList.size(); i++) {
				if(Arrays.equals(simpleSigmaXList.get(i),simpleSigmaX) && Arrays.equals(simpleSigmaOList.get(i),simpleSigmaO)) {
					contains = true;
					break;
				}
			}
			if(!contains) {
				simpleSigmaXList.add(simpleSigmaX);
				simpleSigmaOList.add(simpleSigmaO);
				sigmaXList.add(sigmaX);
				tauList.add(Tau.tauInvariant(sigmaX));
				/*
				System.out.println(Arrays.toString(sigmaX) + 
						"\t" + Arrays.toString(simpleSigmaX) + 
						"\t" + Arrays.toString(simpleSigmaO) + 
						"\t" + simpleSigmaX.length + 
						"\t" + tauList.get(tauList.size()-1));
						*/
				file.println(Arrays.toString(sigmaX).replace("[","{").replace("]", "}") + 
						"\t" + Arrays.toString(simpleSigmaX).replace("[","{").replace("]", "}") + 
						"\t" + Arrays.toString(simpleSigmaO).replace("[","{").replace("]", "}") + 
						"\t" + simpleSigmaX.length + 
						"\t" + tauList.get(tauList.size()-1));
			}
			return;
		}
		
		//Find the column that does not contain any O's
		int emptyCol = 0;
		for(int c = 0; c < smallGrid[0].length; c++) {
			int temp = 0;
			for(int r = 0; r < smallGrid.length; r++) {
				if(smallGrid[r][c] == "O")
					temp++;
			}
			if(temp == 0) {
				emptyCol = c;
				break;
			}
		}
		
		//Loop that adds an "X" to the small Grid in each position in the empty column, adds the resulting small grid to
		//the overall grid, removes the row and column that contains the new "X" and calls the function on the new small
		//grid, then undoes the last actions performed
		for(int i = 0; i < smallGrid.length; i++) {
			
			//Adds an X to a position in the empty column
			smallGrid[i][emptyCol] = "X";
			
			//Adds the information found in the small grid to the overall grid
			int smallGridR = 0;
			int smallGridC = 0;
			for(int r = 0; r < grid.length; r++) {
				if(!rowRemoved.contains(r)) {
					for(int c = 0; c < grid[0].length; c++) {
						if(!colRemoved.contains(c)) {
							grid[r][c] = smallGrid[smallGridR][smallGridC];
							smallGridC++;
						}
					}
					smallGridR++;
					smallGridC = 0;
				}
			}
			
			//Adds row and col we are going to remove to rowRemoved and colRemoved and initializes rowAdd and colAdd so we can
			//remove these from rowRemoved and colRemoved later
			int rowAdd = 0;
			int colAdd = 0;
			for(int r = 0; r < grid.length; r++) {
				for(int c = 0; c < grid[0].length; c++) {
					if(grid[r][c] == "X" && !rowRemoved.contains(r) &&!colRemoved.contains(c)) {
						rowRemoved.add(r);
						rowAdd = r;
						colRemoved.add(c);
						colAdd = c;
					}
				}
			}
			
			//Create a new grid with one less row and column
			String[][] smallGridRemoved = new String[smallGrid.length-1][smallGrid[0].length-1];
			
			//Find the row and column that contains an "X"
			int xRow = 0;
			int xCol = 0;
			for(int r = 0; r < smallGrid.length; r++) {
				for(int c = 0; c < smallGrid[0].length; c++) {
					if(smallGrid[r][c] == "X") {
						xRow = r;
						xCol = c;
					}
				}
			}
			
			//Remove the row and column that contains the "X"
			int smallGridRemovedR = 0;
			int smallGridRemovedC = 0;
			for(int r = 0; r < smallGrid.length; r++) {
				if(r != xRow) {
					for(int c = 0; c < smallGrid[0].length; c++) {
						if(c != xCol) {
							smallGridRemoved[smallGridRemovedR][smallGridRemovedC] = smallGrid[r][c];
							smallGridRemovedC++;
						}
					}
					smallGridRemovedR++;
					smallGridRemovedC = 0;
				}
			}
			
			//Call the function on the reduced grid
			diagonalKnotsHelper(smallGridRemoved,file);
			
			//Removes rowAdd and colAdd from rowRemove and colRemove, respectively
			rowRemoved.remove(rowRemoved.indexOf(rowAdd));
			colRemoved.remove(colRemoved.indexOf(colAdd));
			
			//Removes the previously added X from smallGrid
			smallGrid[i][emptyCol] = "-";
			
			//Adds the information found in the small grid without the X to the overall grid
			int smallGridR1 = 0;
			int smallGridC1 = 0;
			for(int r = 0; r < grid.length; r++) {
				if(!rowRemoved.contains(r)) {
					for(int c = 0; c < grid[0].length; c++) {
						if(!rowRemoved.contains(c)) {
							grid[r][c] = smallGrid[smallGridR1][smallGridC1];
							smallGridC1++;
						}
					}
					smallGridR1++;
					smallGridC1 = 0;
				}
			}
			
		}
		
		
	}
	
	
	public static void main(String[] args) {
		for(int size = 12; size <= 12; size++) {
			diagonalKnots(size);
		}
	}
	
}
