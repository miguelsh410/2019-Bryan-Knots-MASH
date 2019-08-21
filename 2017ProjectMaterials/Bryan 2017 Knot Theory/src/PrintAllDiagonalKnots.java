import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class PrintAllDiagonalKnots {
	public static void main(String[] args) {
		
		//Ask user for size of grid diagram
		Scanner scan = new Scanner(System.in);
		System.out.println("What is the size of your grid diagram? Enter the number of rows/columns.");
		//Set size of grid diagram to user input
		int n = scan.nextInt();		
		scan.close();
		
		int factorial = AllDiagonalKnotGrids.factorial(n-1);
		
		//Create grid diagram
		DiagonalKnotGrids.diagonalKnots(n);
		ArrayList<int[]> sigmaXGrid = DiagonalKnotGrids.sigmaXList;
		
		/*
		//Writes numSize and unsimplified X permutations of a certain simplification size with tau invariants based on user input into a file
		try{
		    PrintWriter writer = new PrintWriter("all_X_permutations_size_" + n + "_simplified_size_", "UTF-8");
		    for(int i = 0; i < factorial; i++) {
					writer.println(Arrays.toString(sigmaXGrid[i]) + " - " + Tau.tauInvariant(sigmaXGrid[i]));
			}  
		    writer.close();
		} catch (IOException e) {
		   System.out.println("There is an error!");
		}
		*/
		
		for(int i = 0; i < factorial; i++) {
			System.out.println(Arrays.toString(sigmaXGrid.get(i)) + " " + Tau.tauInvariant(sigmaXGrid.get(i)));
		}
	}
}
