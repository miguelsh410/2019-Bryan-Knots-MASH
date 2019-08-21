import java.util.Random;
import java.util.Scanner;

public class RandomGrid {
	
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		System.out.println("What is the size of your grid diagram? Enter the number of rows/columns.");
		int n = scan.nextInt();
		scan.close();
		char[][] knot = diagonalknotgrid(n);
		for(int r = 0; r < n; r++) {
			for(int c = 0; c < n; c++) {
				System.out.print(knot[r][c]);
			}
			System.out.println(" ");
		}

	}
	
	//Creates random diagonal n x n grid diagram
	public static char[][] diagonalknotgrid(int n) {
		char[][] knotgrid = new char[n][n];
		int[] sigmaO = new int[n];
		int[] sigmaX = new int[n];
		
		//Set O's to be on diagonal and create default X's
		for(int i = 0; i < n; i++) {
			sigmaO[i] = i;
			sigmaX[i] = i;
		}
		
		//Shuffles the sigmaX array and checks if it overlaps the sigmaO array, then shuffles again if so
		while(true) {
			shuffleArray(sigmaX);
			int temp = 0;
			for(int k = 0; k < n; k++) {
				if(sigmaX[k] == sigmaO[k]) {
					temp++;
				}
			}
			if(temp == 0) {
				break;
			}
		}
		
		//Creates knot grid from sigmaX and sigmaO
		for(int r = 0; r < n; r++) {
			for(int c = 0; c < n; c++) {
				if(sigmaO[c] == r) {
					knotgrid[r][c] = 'O';
				}
				else if(sigmaX[c] == r) {
					knotgrid[r][c] = 'X';
				}
				else {
					knotgrid[r][c] = '*';
				}
			}
		}
	    
		return knotgrid;
	    
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

}
