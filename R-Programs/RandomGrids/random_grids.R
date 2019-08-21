# BEFORE RUNNING THE FUNCTION YOU NEED TO SET THE WORKING DIRECTORY (setwd())
# TO THE FILES LOCATION, OTHERWISE THE OUTPUT FILE WILL GO TO OTHER LOCATION
# This function will generate two permutations of the same length
# The length should be specified in the size_of_grid argument
# You can also generate a ccertain number of list pairs by specifying it in the
# number_of_grids argument, which default is 1
# In the file_type, we can specify as a string if we want "csv" or "tsv"
# We can specify a seed, or not, it is optional
# If mathematica is set to TRUE, the output file will be formatted to be used in the
# mathematica alexander polynomial function created in 2017
random_grids <- function(size_of_grid, number_of_grids = 1, seed = FALSE, file_type = "csv", mathematica = FALSE, directory = NULL){
  # The next variable will store a data frame with sigma_o, and sigma_x
  grids <- c()
  
  # This creates a random seed is there is no specified seed
  if (seed == FALSE) {
    seed<-sample(1:2^24, 1)
    set.seed(seed)
  }
  # This will set the seed to the specified one in the 
  # argument only if the given seed is numeric
  else if (is.numeric(seed)) set.seed(seed)
  # If the given seed is not numeric, stop the function and return a message
  else stop("The seed given must be of numeric type")
  
  # This for-loop is to generate as many grids as we specify
  for (z in 1:number_of_grids){
    # These two variables are going to be used to store the pair of lists later
    string_sigma_o <- c()
    string_sigma_x <- c()
    # sigma_o is a list with numbers from 1 to the specified size
    sigma_o <- sample(1:size_of_grid, size_of_grid)
    
    # sigma_x will store a list with numbers based on sigma_o
    # for every number in sigma_o, sigma_x can't have the same number in the 
    # same position as sigma_o
    # Also, there can't be repeated numbers in the list
    sigma_x <- c()
    
    # Sometimes sigma_x will be a list shorter than sigma_o
    # This happens when at the end of the for-loop the only option left is the 
    # same number as in the same position in sigma_o
    # Therefore, we loop until both lists have the same length
    n <- 0
    done <- FALSE
    while ((length(sigma_o) != length(sigma_x)) & done == FALSE){
      # Empty sigma_x every time the loop runs
      sigma_x <- c()
      # This is to avoid the while loop to be an infinite loop
      n <- n + 1
      
      # Loop through every number in sigma_o
      for (i in sigma_o){
        # This will have a list of numbers excluding the ones we can't choose
        # i.e(excluding i, and the numbers already used in sigma_x)
        to_choose <- (1:size_of_grid)[-c(sigma_x, i)]
        
        # This if statement is to avoid an unexpected result that happens when
        # we call the "sample" function with only one option to choose from.
        if (length(to_choose) > 1){
          # Choose any number from to_choose and append it to sigma_x
          sigma_x <- c(sigma_x, sample(to_choose, 1))
        }
        # Append the only number left in to_choose
        else {
          sigma_x <- c(sigma_x, to_choose)
        }
      }
      
      # If the loop has run more than 10 times, stop the while loop
      if (n > 10) {
        done = TRUE
        return ("Run-time error.")
      }
    }
    
    # Put the first number of each list to convert them to strings
    # We put the first number separately because if we put it inside the next for-loop, we get a coma first, instead of a number
    string_sigma_o <- sigma_o[1]
    string_sigma_x <- sigma_x[1]
    
    # Convert every number to string 
    for (a in 2:size_of_grid){
      string_sigma_o <- paste(string_sigma_o, ", ", sigma_o[a], sep = "")
      string_sigma_x <- paste(string_sigma_x, ", ", sigma_x[a], sep = "")
    }
    
    # Before using the file_type, check if it is a character
    if (!is.character(file_type)) stop("The file_type argument must be a character indicating the type of file to output")
    
    # If the file is not to be formatted for mathematica, just put every row together 
    if (!mathematica){
      # Give appropriate file name
      filename <- paste(size_of_grid, "_", number_of_grids, "_", seed, ".", file_type, sep = "")
    }
    # If the file needs to be formatted for mathematica, add curly brackets,
    # and add "_math" to the filename
    else{
      # Add curly brackets
      string_sigma_o <- paste("{", string_sigma_o, "}", sep = "")
      string_sigma_x <- paste("{", string_sigma_x, "}", sep = "")
      
      # Give appropriate file name
      filename <- paste(size_of_grid, "_", number_of_grids, "_", seed, "_math", ".", file_type, sep = "")
    }
    # Put sigma_o and sigma_x together
    grids <- rbind(grids, c(string_sigma_o, string_sigma_x, number_of_components(sigma_o, sigma_x), seed))
  }
  # grids was a matrix, we make it be a data frame
  grids <- as.data.frame(grids)
  
  # Give appropriate column names
  colnames(grids) <- c("sigma_o", "sigma_x", "Number of Components", "Seed")
  
  # Set the working directory to store our file
  if (!is.null(directory)) setwd(directory)
  
  if (sum(list.files() == "2019-Bryan-Knots") >= 1) {
    setwd(paste(getwd(),"/2019-Bryan-Knots/R-Programs/Files", sep = ""))
  }
  else if (sum(list.files() == "R-Programs") >= 1){
    setwd(paste(getwd(),"/R-Programs/Files", sep = ""))
  }
  else if (sum(list.files() == "Files") >= 1){
    setwd(paste(getwd(),"/Files", sep = ""))
  }
  
  
  # If the file_type is "tsv", use write.table
  if (file_type == "tsv"){
    # If the file is for mathematica we delete the column names
    if (mathematica) write.table(grids, filename, row.names = FALSE, col.names = FALSE, quote = FALSE, sep = "\t")
    # We keep the column names if the file is not for mathematica
    else write.table(grids, filename, row.names = FALSE, quote = FALSE, sep = "\t")
  } 
  # If the file_type is "csv", we use write.csv
  else {
    write.csv(grids, filename, row.names = FALSE)
  }
  return(filename)
}