
public class Permute {
	//Initialize count to 0
	public static int count = 0;
	
	//Create permutations
	public static int[][] permutations;
	
	/*
	public static void main(String[] args) {
		int[] array = {0,1,2,3};
		permutations = new int[AllRandomKnotGrids.factorial(array.length)][array.length];
		permute(array);
		System.out.println(count);
		
		
		for(int[] item : permutations) {
			for(int i : item) {
				System.out.print(i + " ");
			}
			System.out.println();
		}
		
		
	}
	*/
	
	public static void permute(int[] arr){
		//Start permutehelper at index=0
	    permuteHelper(arr, 0); 
	}
	
	private static void permuteHelper(int[] arr, int index){
		
	    if(index >= arr.length - 1){ //If we are at the last element - nothing left to permute	    	
	    	
	    	//Print the arrays
	        //System.out.println(Arrays.toString(arr));
	       
	        int[] newArr = new int[arr.length];
	        //Add the arrays to an array of all the permutations
	        for(int i = 0; i < permutations[0].length; i++) {
	        	for(int k = 0; k < arr.length; k++) {
	    			newArr[k] = arr.length - arr[k];
	    		}
	        	permutations[count][i] = newArr[i];
	        }
	        
	        //Increase count
	        count++;
	        return;
	        
	    }

	    for(int i = index; i < arr.length; i++){ //For each index in the sub array arr[index...end]

	        //Swap the elements at indices index and i
	        int t = arr[index];
	        arr[index] = arr[i];
	        arr[i] = t;

	        //Recurse on the sub array arr[index+1...end]
	        permuteHelper(arr, index+1);

	        //Swap the elements back
	        t = arr[index];
	        arr[index] = arr[i];
	        arr[i] = t;
	    }
	}

}
