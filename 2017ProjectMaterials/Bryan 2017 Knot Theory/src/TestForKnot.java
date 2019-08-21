import java.util.ArrayList;

public class TestForKnot {
	
	public static void main(String[] args) {
		int[] sigmaO = {0,1,2,3};
		int[] sigmaX = {1,2,3,4};
		System.out.println(testForKnot(sigmaO,sigmaX));
	}
	//Returns true if knot and false otherwise
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
