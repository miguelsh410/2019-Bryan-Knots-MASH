import java.util.Arrays;
import org.apache.commons.math3.analysis.polynomials.*;

public class AlexanderPolynomial {
	
	//Calculates the winding number of every point on the grid diagram
	public static int[][] windingNums(int[] sigmaO, int[] sigmaX) {
		//Initialize the size of the grid and create the knot grid
		int size = sigmaO.length;
		String[][] knotGrid = CyclicSimplify.createGrid(size, sigmaX, sigmaO);
		CyclicSimplify.printGrid(knotGrid);
		
		int[][] windingNums = new int[size][size];
		
		for(int r = 0; r < size; r++) {
			for(int c = 0; c < size; c++) {
				windingNums[r][c] = windingNum(r,c, knotGrid);
			}
		}
		
		//Find the negative of the winding numbers, which will be used in another matrix
				for(int r = 0; r < size; r++) {
					for(int c = 0; c < size; c++) {
						windingNums[r][c] = -windingNums[r][c];
					}
				}
		
		return windingNums;
	}						
	
	
	//Calculates the winding number of a single point on the grid diagram
	public static int windingNum(int row, int col, String[][] grid) {
		//Initialize the size of the grid
		int size = grid.length;
		int windingNum = 0;
		
		for(int c = col; c < size; c++) {
			int colXPos = 0;
			int colOPos = 0;
			for(int r = 0; r < size; r++) {
				if(grid[r][c] == "X")
					colXPos = r;
				if(grid[r][c] == "O")
					colOPos = r;
			}
			if(colXPos > row && colOPos <= row)
				windingNum++;
			if(colOPos > row && colXPos <= row)
				windingNum--;
		}
		
		return windingNum;	
	}
	
	public static PolynomialFunction[][] createMineSweeper(int[][] winding) {
		int size = winding.length;
		
		//Create the mine sweeper matrix by taking x to the power of the negative winding number
		PolynomialFunction[][] mineSweeperMatrix = new PolynomialFunction[size][size];
		for(int r = 0; r < size; r++) {
			for(int c = 0; c < size; c++) {
					double[] coef = new double[winding[r][c] + 1];
					for(int i = 0; i <= winding[r][c]; i++) {
						if(i == winding[r][c])
							coef[i] = 1;
					}
					mineSweeperMatrix[r][c] = new PolynomialFunction(coef);
			}
		}
		
		return mineSweeperMatrix;
	}
	
	public static PolynomialFunction[][] simplifyMineSweeper(PolynomialFunction[][] mineSweeper) {
		int size = mineSweeper.length;
		PolynomialFunction[][] polyGrid = new PolynomialFunction[size][size];
		for(int r = 0; r < size; r++) {
			for(int c = 1; c < size; c++) {
				polyGrid[r][c] = mineSweeper[r][c].subtract(mineSweeper[r][c-1]);
			}
			polyGrid[r][0] = mineSweeper[r][0];
		}
		
		
		int[][] degrees = new int[size][size];
		for(int r = 0; r < size; r++) {
			for(int c = 0; c < size; c++) {
				degrees[r][c] = polyGrid[r][c].degree()-1;
				if(degrees[r][c] == -1) 
					degrees[r][c] = 0;
			}
		}
		
		PolynomialFunction[][] factoredPolyGrid = createMineSweeper(degrees);
		double[] zeroArray = {0};
		PolynomialFunction zero = new PolynomialFunction(zeroArray);
		for(int r = 0; r < size; r++) {
			for(int c = 0; c < size; c++) {
				if(polyGrid[r][c].equals(zero)) {
					factoredPolyGrid[r][c] = zero;
				}
			}
		}
		
		return factoredPolyGrid;
	}
	
	//Calculates the determinant for a given polynomial matrix
	//Code for determinant taken from http://www.sanfoundry.com/java-program-compute-determinant-matrix/
	//and adapted for polynomial functions
	public static PolynomialFunction determinant(PolynomialFunction[][] A,int N) {
		double[] zero = {0};
        PolynomialFunction det = new PolynomialFunction(zero);
        if(N == 1) {
            det = A[0][0];
        }
        else if (N == 2) {
            det = A[0][0].multiply(A[1][1]).subtract(A[1][0].multiply(A[0][1]));
        }
        else {
            det = new PolynomialFunction(zero);
            for(int j1=0;j1<N;j1++) {
                PolynomialFunction[][] m = new PolynomialFunction[N-1][];
                for(int k=0;k<(N-1);k++) {
                    m[k] = new PolynomialFunction[N-1];
                }
                for(int i=1;i<N;i++) {
                    int j2=0;
                    for(int j=0;j<N;j++) {
                        if(j == j1)
                            continue;
                        m[i-1][j2] = A[i][j];
                        j2++;
                    }
                }
                if(Math.pow(-1.0,1.0+j1+1.0) > 0) {
                	det = det.add(A[0][j1].multiply(determinant(m,N-1)));
                }
                else {
                	det = det.add(A[0][j1].negate().multiply(determinant(m,N-1)));
                }
            }
        }
        return det;
    }
	
	public static void main(String[] args) {
		
		/*
		double[] coef = {1,2,3};
		double[] coef2 = {2,3};
		PolynomialFunction poly = new PolynomialFunction(coef);
		PolynomialFunction poly2 = new PolynomialFunction(coef2);
		System.out.println(poly.multiply(poly2));
		System.out.println(poly.multiply(poly2).negate());
		PolynomialFunction[][] polyGrid = new PolynomialFunction[3][3];
		polyGrid[0][0] = poly;
		polyGrid[0][1] = poly2;
		polyGrid[0][2] = poly;
		polyGrid[1][0] = poly2;
		polyGrid[1][1] = poly;
		polyGrid[1][2] = poly2;
		polyGrid[2][0] = poly2;
		polyGrid[2][1] = poly;
		polyGrid[2][2] = poly;
		for(int r = 0; r < polyGrid.length; r++) {
        	for(int c = 0; c < polyGrid[0].length; c++) {
                System.out.print(polyGrid[r][c] + "|");
        	}
        	System.out.println();
        }

		System.out.println(determinant(polyGrid,3));
		*/
		
		int[] sigmaO = {11,10,9,8,7,6,5,4,3,2,1};
		int[] sigmaX = {5,4,3,2,11,1,10,9,8,6,7};
		int[][] wind = windingNums(sigmaO,sigmaX);
		for(int i = 0; i < wind.length; i++) {
			System.out.println(Arrays.toString(wind[i]));
		}
		
		PolynomialFunction[][] polygrid = simplifyMineSweeper(createMineSweeper(wind));
		for(int r = 0; r < polygrid.length; r++) {
			for(int c = 0; c < polygrid.length; c++) {
				System.out.print(polygrid[r][c] + " ");
			}
			System.out.println();
		}
		System.out.println(determinant(polygrid, polygrid.length));
		
	}
}
