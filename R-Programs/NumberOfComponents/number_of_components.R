# This function takes two lists as arguments
# Then compares the two lists and checks if they represent a knot or a link
# It also returns the number of components.
# If only_numeric is TRUE, it will return only numbers instead of character
number_of_components <- function(sigma_o, sigma_x, only_numeric = TRUE, components = 0){
  # Check if both arrays are the same length
  if (length(sigma_o) != length(sigma_x)){
    if (only_numeric) -1
    else stop("Please input arrays of the same length")
  }
  # Check that there are no numbers that are the same in the same posiiton of both lists.
  else if (sum(sigma_o == sigma_x) > 0){
    if (only_numeric) -1
    else stop("There is at least one X and one O in the same list position.")
  }
  # Check that the numbers in both lists are the same
  else if (sum(sigma_o) != sum(sigma_x)){
    if (only_numeric) -1
    else stop("Both lists must contain the same numbers.")
  } 
  else{
    # Set the index to first element in sigma_o
    index <- sigma_o[1]
    
    # Starting position
    n <- 1
    # Start at the first element of sigma_x
    current_number <- sigma_x[1]
    # Variable that will store the numbers that are compared
    past_numbers <- c()
    
    # Go through this loop until we match the index with the current number
    while (current_number != index) {
      # Go to the nth element of sigma_x
      current_number <- sigma_x[n]
      
      # Put the number in the past_numbers list
      past_numbers <- c(past_numbers, current_number)
      
      # Get the position of that same number (current_number) in sigma_o
      n <- which(sigma_o == current_number) 
    }
    
    # Get the numbers that were not seen in the loop and only leave those.
    for (i in past_numbers){
      sigma_o <- c(sigma_o[sigma_o != i])
      sigma_x <- c(sigma_x[sigma_x != i]) 
    }
    
    components <- components + 1
    
    # If there are remainig numbers that were not seen in the past loop, run the function again.
    if ((length(sigma_o) == length(sigma_x)) & (length(sigma_x) > 0)){
      number_of_components(sigma_o, sigma_x, only_numeric, components)
    }
    # If there are no remaining numbers, check if it is knot or link
    else {
      if (only_numeric) components
      else{
        if (components == 1) return(writeLines(paste("This is a knot. \nNumber of components:", components)))
        else if (components > 1) return(writeLines(paste("This is a link. \nNumber of components:", components)))
      }
    }
  }
}
