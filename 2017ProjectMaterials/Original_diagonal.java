//import java.util.Random;
import java.util.Scanner;
import java.util.Arrays;

public class AllDiagonalKnotGrids {
	
	public static void main(String[] args) {
		//Ask user for size of grid diagram
		Scanner scan = new Scanner(System.in);
		System.out.println("What is the size of your grid diagram? Enter the number of rows/columns.");
		//Set size of grid diagram to user input
		int n = scan.nextInt();
		
		//Ask user which X permutations to print
		System.out.println("What is the size of the X permutations you want printed?");
		//Set printX to user input
		int printX = scan.nextInt();
		
		//Ask user for number of X permutations to print
		//System.out.println("What Tau are you looking for?");
		//Set numX to user input
		//int numTau = scan.nextInt();
		
		scan.close();
		
		
		//Create grid diagram
		int[][] sigmaXGrid = diagonalknotgrid(n);
		int[] gridSizes = new int[factorial(n-1)];
		
		//Initialize sigmaO
		int[] sigmaO = new int[n];
				
		//Set O's to be on diagonal
		for(int i = 0; i < n; i++) {
			sigmaO[i] = n-i;
		}
		
		//Simplifies all knots of size n and returns an array of grid sizes after simplification
		for(int i = 0; i < factorial(n-1); i++) {
			gridSizes[i] = Simplify.simplifyKnot(sigmaO,sigmaXGrid[i]);
		}
		
		//Create an array of the number of a particular sized grid diagram after simplification
		//Create an array of the number of knots with specific values of Tau
		int[] tauSizes = new int[8];
		int[] numSizes = new int[n];
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < factorial(n-1); j++) {
				if(gridSizes[j] == i+1)
					numSizes[i]++;
				if(Tau.tauInvariant(sigmaXGrid[j]) == i) 
					tauSizes[i]++;
			}
		}
		//Prints the arrays that contain the number of diagrams of each size and of each Tau value
		System.out.println("Number of knots with certain grid size: " + Arrays.toString(numSizes));
		System.out.println("Number of knots with certain Tau:       " + Arrays.toString(tauSizes));
		
		//Prints a certain number of x permutations for a given size of grid diagram based on user input for printX and numX
		for(int i = 0; i < factorial(n-1); i++) {
			if(gridSizes[i] == printX) {
				System.out.println(Arrays.toString(sigmaXGrid[i]) + " - " + Tau.tauInvariant(sigmaXGrid[i]));
			}
		}
		
		/*
		//Print grid diagram
		for(int r = 0; r < n; r++) {
			for(int c = 0; c < n; c++) {
				System.out.print(knot[r][c]);
			}
			System.out.println(" ");
		}
		*/

	}
	
	//Creates all diagonal n x n grid diagram
	public static int[][] diagonalknotgrid(int n) {
		
		
		//Initialize sigmaO and sigmaX to arrays of length n 
		int[] sigmaO = new int[n];
		int[] sigmaX = new int[n];
				
		//Set O's to be on diagonal and create default X's
		for(int i = 0; i < n; i++) {
			sigmaO[i] = i;
			sigmaX[i] = i;
		}
		
		//Make O permutation correctly denoted
		for(int i = 0; i < n; i++) {
			sigmaO[i] = n-i;
		}
		
		//Initialize permutations to the correct size
		Permute.permutations = new int[factorial(sigmaX.length)][sigmaX.length];
		//Create all permutations of sigmaX
		Permute.permute(sigmaX);
		//Start count at 0
		int count = 0;
		//Initialize SigmaXGrid, all the diagonal knots from grid size n
		int[][] sigmaXGrid = new int[factorial(n-1)][n];
		
		//Check each permutation to see if it's a knot and put knots into sigmaXGrid
		for(int i = 0; i < Permute.permutations.length; i++) {
			if(TestForKnot.testForKnot(sigmaO,Permute.permutations[i])) {
				for(int k = 0; k < n; k++) {
					sigmaXGrid[count][k] = Permute.permutations[i][k];
				}
				count++;
			}
		}
		
		return sigmaXGrid;
		
		
		/*
		//Inefficient method of generating all grid diagrams randomly**********************
		
		//Initialize sigmaXList to an n! x n matrix
		int[][] sigmaXList = new int[factorial(n-1)][n];
		//Initialize sigmaO and sigmaX to arrays of length n 
		int[] sigmaO = new int[n];
		int[] sigmaX = new int[n];
		
		//Set O's to be on diagonal and create default X's
		for(int i = 0; i < n; i++) {
			sigmaO[i] = i;
			sigmaX[i] = i;
		}
		int numSigmaX = 0;
		while(true) {
		//Outer loop checks if generated grid diagram is a knot and generates another if it isn't
		while(true) {
			//Inner loop shuffles the sigmaX array and checks if it overlaps the sigmaO array, then shuffles again if it does overlap
			while(true) {
				shuffleArray(sigmaX);
				int temp = 0;
				//Checks if shuffled sigmaX overlaps sigmaO, and breaks if it doesn't
				for(int k = 0; k < n; k++) {
					if(sigmaX[k] == sigmaO[k]) {
						temp++;
					}
				}
				if(temp == 0) {
					break;
				}
			}
			//Checks if generated grid diagram is a knot, and if it is break
			if(TestForKnot.testForKnot(sigmaX, sigmaO)) {
				break;
			}
		}
		
		int[] correctSigmaX = new int[n];

		for(int i = 0; i < n; i++) {
			correctSigmaX[i] = n - sigmaX[i];
		}
		
		int temp2 = 0;
		for(int r = 0; r < factorial(n-1); r++) {
				if(Arrays.equals(sigmaXList[r], correctSigmaX)) {
					temp2++;
				}
		}

		if(temp2 == 0) {
			for(int k = 0; k < n; k++) {
				sigmaXList[numSigmaX][k] = correctSigmaX[k];
			}
			numSigmaX++;
		}
		if(numSigmaX == factorial(n-1)) {
			break;
		}
		}
		return sigmaXList;
		*/
		
		/*
		//Creates knot grid from sigmaX and sigmaO
		for(int r = 0; r < n; r++) {
			for(int c = 0; c < n; c++) {
				//Fill in the O's
				if(sigmaO[c] == r) {
					knotgrid[r][c] = 'O';
				}
				//Fill in the X's
				else if(sigmaX[c] == r) {
					knotgrid[r][c] = 'X';
				}
				//Fill in everything else with asterisks
				else {
					knotgrid[r][c] = '-';
				}
			}
			
		}
		
	    
		return knotgrid;
	    */
		
		//attempts to create a more efficient random grid diagram generator
		/*
		int numChoice = n-1;
		int row = 1;
		int col = 1;
		for(int k = 1; k <= n; k++) {
			row = ThreadLocalRandom.current().nextInt(1,numChoice+1);
			if(row < col) {
				sigmax[col] = row;
			}
			else {
				sigmax[col] = row+1;
			}
			col = row;
			numChoice--;
			
			
		}
		*/
		
	}
	
	/*
	//Shuffle an array
	public static void shuffleArray(int[] array) {
		int index, temp;
	    Random random = new Random();
	    for (int k = array.length - 1; k > 0; k--)
	    {
	        index = random.nextInt(k + 1);
	        temp = array[index];
	        array[index] = array[k];
	        array[k] = temp;
	    }
	}
	*/
	
	//Calculate the factorial of a given n recursively
	public static int factorial(int num) {
		if(num == 0) {
			return 1;
		}
		else {
		return num * factorial(num-1);
		}
	}
}