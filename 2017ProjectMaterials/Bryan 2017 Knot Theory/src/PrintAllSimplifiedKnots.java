import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class PrintAllSimplifiedKnots {
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
		
		scan.close();
		
		int factorial = AllDiagonalKnotGrids.factorial(n-1);
		int[][] simpleSigmaXList = new int[factorial][];
		int[][] simpleSigmaOList = new int[factorial][];
		int[][] simpleSigmaXList2 = new int[factorial][];
		int[][] simpleSigmaOList2 = new int[factorial][];
		int[] tauList = new int[factorial];
		int[] numSizes = new int[n];
		int[] tauSizes = new int[n]; // CHANGE TAU HERE *********************************************
		
		//Create grid diagram
		DiagonalKnotGrids.diagonalKnots(n);
		ArrayList<int[]> sigmaXGrid = DiagonalKnotGrids.sigmaXList;
				
		//Initialize sigmaO and set O's to be on diagonal
		int[] sigmaO = new int[n];
		for(int i = 0; i < n; i++) {
			sigmaO[i] = n-i;
		}
		
		//Simplifies all knots of size n and stores them in an array
		for(int i = 0; i < factorial; i++) {
			int[] simpleSigmaX = CyclicSimplify.simplifyKnot(sigmaO,sigmaXGrid.get(i));
			simpleSigmaXList[i] = new int[simpleSigmaX.length];
			simpleSigmaOList[i] = new int[CyclicSimplify.sigmaOperm.length];
			simpleSigmaXList[i] = simpleSigmaX;
			simpleSigmaOList[i] = CyclicSimplify.sigmaOperm;
			
			int[] simpleSigmaX2 = Simplify.simplifyKnot(sigmaO,sigmaXGrid.get(i));
			simpleSigmaXList2[i] = new int[simpleSigmaX2.length];
			simpleSigmaOList2[i] = new int[Simplify.sigmaOperm.length];
			simpleSigmaXList2[i] = simpleSigmaX2;
			simpleSigmaOList2[i] = Simplify.sigmaOperm;
		}
		
		//Calculates tau for every knot and stores them in an array
		for(int i = 0; i < tauList.length; i++) {
			tauList[i] = (int) Tau.tauInvariant(sigmaXGrid.get(i));
		}
		
		//Create an array of the number of a particular sized grid diagram after simplification
		//as well as the number of each case of tau
		
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < factorial; j++) {
				if(simpleSigmaXList[j].length == i+1) {
					numSizes[i]++;
				}
				if(tauList[j] == i) {
					tauSizes[i]++;
					if(i == 11) {
						//System.out.print(Arrays.toString(sigmaXGrid.get(i)));
					}
				}
			}
		}
		
		for(int j = 0; j < factorial; j++) {
			if(simpleSigmaXList[j].length != simpleSigmaXList2[j].length) {
				System.out.println(Arrays.toString(sigmaXGrid.get(j)) + " - " + Arrays.toString(simpleSigmaXList2[j]) + ", " + Arrays.toString(simpleSigmaOList2[j]) + " - " + Arrays.toString(simpleSigmaXList[j]) + ", " + Arrays.toString(simpleSigmaOList[j]));
			}
		}
		
		/*
		//Prints unsimplified X permutations of a certain simplification size with tau invariants based on user input
		for(int i = 0; i < factorial; i++) {
			if(simpleSigmaXList[i].length == printX)
				System.out.println(Arrays.toString(sigmaXGrid.get(i)) + " -> " + Arrays.toString(simpleSigmaXList[i]) + " - " + simpleSigmaXList[i].length + ", " + tauList[i]);
		}
		*/
		
		//Prints the arrays that contain the number of diagrams of each size and of each Tau value
		System.out.println("Number of knots with certain grid size: " + Arrays.toString(numSizes));
		System.out.println("Number of knots with certain Tau:       " + Arrays.toString(tauSizes));
		
		
		//Writes numSize and unsimplified X permutations of a certain simplification size with tau invariants based on user input into a file
		try{
		    PrintWriter writer = new PrintWriter("X_Permutations_Size_" + n, "UTF-8");
		    //writer.println(Arrays.toString(numSizes));
		    for(int i = 0; i < factorial; i++) {
				//if(gridSizes[i] == printX)
					writer.println(Arrays.toString(sigmaXGrid.get(i)).replace("[","{").replace("]", "}") + "\t" + Arrays.toString(simpleSigmaXList[i]).replace("[","{").replace("]", "}") + "\t" + Arrays.toString(simpleSigmaOList[i]).replace("[","{").replace("]", "}") + "\t" + simpleSigmaXList[i].length + "\t" + tauList[i]);
			}  
		    writer.close();
		} catch (IOException e) {
		   System.out.println("There is an error!");
		}
	}
}
