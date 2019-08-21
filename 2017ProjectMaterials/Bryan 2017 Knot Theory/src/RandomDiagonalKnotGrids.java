import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class RandomDiagonalKnotGrids {
	
	public static void main(String[] args) {
		
		//Ask user for size of grid diagram
		Scanner scan = new Scanner(System.in);
		System.out.println("What is the size of your grid diagram? Enter the number of rows/columns.");
		//Set size of grid diagram to user input
		int n = scan.nextInt();		
		System.out.println("How many X permutations do you want printed?");
		int firstn = scan.nextInt();
		scan.close();
		
		int[][] sigmaXGrid;
		
		//Initialize sigmaO
		int[] sigmaO = new int[n];
						
		//Set O's to be on diagonal
		for(int i = 0; i < n; i++) {
			sigmaO[i] = n-i;
		}
		
		//Create grid diagram
		sigmaXGrid = randomDiagonalKnotGrids(n, firstn);
		int[] gridSizes = new int[firstn];
		
		//Simplifies all knots of size n and returns an array of grid sizes after simplification
		for(int i = 0; i < firstn; i++) {
			gridSizes[i] = CyclicSimplify.simplifyKnot(sigmaO,sigmaXGrid[i]).length;
					
		}
				
		//Create an array of the number of a particular sized grid diagram after simplification
		int[] numSizes = new int[n];
		int[] tauSizes = new int[n];
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < firstn; j++) {
				if(gridSizes[j] == i+1)
					numSizes[i]++;
				if(Tau.tauInvariant(sigmaXGrid[j]) == i)
					tauSizes[i]++;
			}
		}
				
		//Prints unsimplified X permutations of a certain simplification size with tau invariants based on user input
		for(int i = 0; i < firstn; i++) {
				System.out.println(Arrays.toString(sigmaXGrid[i]) + " -> " + Arrays.toString(CyclicSimplify.simplifyKnot(sigmaO,sigmaXGrid[i])) + " - " + gridSizes[i] + ", " + Tau.tauInvariant(sigmaXGrid[i]));
		}
		
		System.out.println(Arrays.toString(numSizes));
		System.out.println(Arrays.toString(tauSizes));
		
	}
	
	public static int[][] randomDiagonalKnotGrids(int n, int firstn) {
		
		//Initialize sigmaXList to an n! x n matrix
		int[][] sigmaXList = new int[firstn][n];
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
		for(int r = 0; r < firstn; r++) {
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
		if(numSigmaX == firstn) {
			break;
		}
		}
		return sigmaXList;
		
		
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
		*/	
		}
	
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
