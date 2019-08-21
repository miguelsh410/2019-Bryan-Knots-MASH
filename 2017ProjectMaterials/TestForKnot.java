import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class TestForKnot {
	public static String testForKnot(int[] sigmaO, int[] sigmaX){
		
		//Check if arrays are of the same length
		if (sigmaO.length != sigmaX.length){
			return "Please input arrays of the same length";
		}
		
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
									return "Link";
								}
								else{
									index=count;
									
								}
							}							
						}
					}
				}
				return "Knot";
		
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		int[] diagonalO={1,2,3,4,5};
		int[] diagonalX={4,1,5,3,2};
		
		System.out.println(testForKnot(diagonalO, diagonalX));
		
	
		int[] knotO={3,5,4,2,1};
		int[] knotX={4,1,5,3,2};
		
		System.out.println(testForKnot(knotO, knotX));
		
		int[] linkO={2,5,4,3,1};
		int[] linkX={1,4,5,2,3};
		
		System.out.println(testForKnot(linkO, linkX));
		
		int[] notValidO={1,2,3};
		int[] notValidX={1,2,3,4};
		
		System.out.println(testForKnot(notValidO, notValidX));
		
		int[] fourO={3,2,4,1};
		int[] fourX={1,4,3,2};
		
		System.out.println(testForKnot(fourO, fourX));
		
		
		
		

	}

}
