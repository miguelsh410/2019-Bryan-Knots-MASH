//import java.util.Random;

public class AllDiagonalKnotGrids {
	
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
