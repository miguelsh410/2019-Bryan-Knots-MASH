import java.util.Random;
import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;

public class AllRandomKnotGrids {
	
	public static void main(String[] args) {
		//Ask user for size of grid diagram
		Scanner scan = new Scanner(System.in);
		System.out.println("What is the size of your grid diagram? Enter the number of rows/columns.");
		//Set size of grid diagram to user input
		int n = scan.nextInt();
		scan.close();
		//Create grid diagram
		int[][] sigmaXGrid = diagonalknotgrid(n);
		for(int r = 0; r < factorial(n-1); r++) {
			for(int c = 0; c < n; c++) {
				System.out.print(sigmaXGrid[r][c]);
			}
			System.out.println(" ");
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
	
	//Creates random diagonal n x n grid diagram
	public static int[][] diagonalknotgrid(int n) {
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
			if(testForKnot(sigmaX, sigmaO)) {
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
	
	public static int factorial(int num) {
		if(num == 0) {
			return 1;
		}
		else {
		return num * factorial(num-1);
		}
	}
	
	//Returns true if a knot and false if a link
	public static boolean testForKnot(int[] sigmaO, int[] sigmaX){
		
		//initialize array-list to keep track of where we have looked before.
		ArrayList<Integer> tested=new ArrayList<Integer>();
		
		//begin at index 0
		int index=0;
		
		//add index 0 to "tested"
		
		
		//Don't go out of the bounds of SigmaO
				for (int n=0; n<sigmaO.length-1; n++){
					
					tested.add(index);
				
					//We need to look for the sigmaX[index] in sigmaO
					int toLookFor=sigmaX[index];
					
					//Check for toLookFor in sigmaO. Once you find it, set the index to that so the cycle
					//can continue.
					for (int count=0; count<sigmaO.length; count++){
						if (sigmaO[count]==toLookFor){
							for (int element : tested){
								if (element==count){
									return false;
								}
								else{
									index=count;
									
								}
							}							
						}
				}
				}
				return true;
		
	}
}
