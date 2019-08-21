#alexander_polynomial calls all of the functions we created at once
alexander_polynomial <- function(filename = NULL, row = NULL,  sigma_o = NULL, sigma_x = NULL){
  #we use polynom to take the determinant later
  require(polynom)
  
  if (!is.null(filename) & !is.null(row)){
    my_file <- read.csv(filename)
    sigma_o <- my_file[row, 1]
    sigma_x <- my_file[row, 2]
  }
  
  #we are creating a matrix filled with information represented by
  #the values, -1, 0, and 1, which is used in later steps.
  make_matrix<-function(sigma_x, sigma_o) {
    
    #make a "empty" matrix to be later filled
    #we couldn't figure out how to initialize the matrix without 
    #filling at least a row with useless data
    matrix_of_mine<-matrix(0, nrow = length(sigma_x))
    
    #initialize a variable that stores the position of the x's and o's in every column
    x_position<-c()
    o_position<-c()
    
    #loop through the columns
    for (i in 1:(length(sigma_x)-1)) {
      
      #loop through each value in each column
      for (j in 1:length(sigma_x)) {
        
        #stores the distance between the x value and the bottom of the column
        if (sigma_x[j] == i) {
          x_position <- j
          
        #stores the distance between the o value and the bottom of the column
        }
        if (sigma_o[j] == i) {
          o_position <- j
          
        }
      }
      # creates and empty list that will be used to fill each column
      list_of_rows <- c()
      for (i in 1:length(sigma_x)) {
        list_of_rows<- c(list_of_rows, 0)
      }
  
      
      #appends the list with values -1 or 1 depending on the index, i's, position relative to the x and the o
      for (i in 1:length(sigma_x)) {
        #fills the positions in the column with 1's for positions above x and below o
        if (i < o_position && i >= x_position) {
          list_of_rows[length(sigma_x)-i+1]<- 1
        }
        #fills the positions in the column with 1's for positions above o and below x
        if (i < x_position && i >= o_position) {
          list_of_rows[length(sigma_x)-i+1]<- -1
        }
        
      }
      #adds the new column to a matrix
      matrix_of_mine<-cbind(matrix_of_mine, list_of_rows)
      
      
    }
    #adds a bottom row filled with 0's to complete the matrix
    bottom_row<-c()
    for (i in length(sigma_o)-1) {
      bottom_row<-c(bottom_row, 0)
    }
    matrix_of_mine<-rbind(matrix_of_mine, bottom_row)

    #deleting the extra top row 
    matrix_of_mine<-matrix_of_mine[-1,]
    #calling the next function with the matrix we just created and returning it
    return(matrix_as_polynomials(matrix_of_mine))
  }
  
  
  #this function will take our matrix filled with 1's, 0's, and -1's, and create 
  #a new matrix with the appropriate exponents needed to copute the alexander polynomial
  matrix_as_polynomials<-function(my_matrix) {
    
    #again we are initializing the new matrix with an empty column of 0's. 
    #probably a better way to do this
    polynomial_matrix <- matrix(0, ncol = ncol(my_matrix))
    
    #loop through the matrix, row by row, counting the numbers we put into the
    #previous matrix which will give us the exponent needed when taking the determinate
    for (i in 1:nrow(my_matrix)){
      current_row<- my_matrix[i,]
      
      #counts the columns to track the exponent
      exponent_count <- 0
      
      #create the empty row that filled next
      exponent_of_t <- c()
      
      # inserts the exponent values based on the the addition of the preceiding values
      for (j in 1:ncol(my_matrix)) {
        exponent_count <- exponent_count + current_row[j]
        exponent_of_t <- c(exponent_of_t, exponent_count) 
      }
      
      #add the row to the new matrix
      polynomial_matrix<-rbind(polynomial_matrix, exponent_of_t)
    }
    
    #remove unnecessary top row and return the matrix fitted 
    #with numbers which represent the exponent x in the monomial t^x
    polynomial_matrix<-polynomial_matrix[-1,]
    polynomial_matrix<- apply(polynomial_matrix,c(1, 2), function(x) x - min(polynomial_matrix))
    return(polynomial_matrix)
  }
  
  #last function. returns the alexander polynomial of the matrix 
  determinant<-function(matrix) {
    
    #checks if the matrix is a 2 by 2 then calculates the determinant
    #of a 2 by 2 with the simple crossing algorithm
    if (ncol(matrix) < 3) {
      #here we add the values in the 2 by 2 matrix representing a multiplication of the two numbers
      first_ex<-matrix[1, 1] + matrix[2, 2]
      second_ex<-matrix[1, 2] + matrix[2, 1]
      
      # we then convert the numbers into monomials
      first_poly<-polynomial(c(rep.int(0, first_ex), 1))
      second_poly<-polynomial(c(rep.int(0, second_ex), 1))
      
      #return the difference of the monomials
      return(first_poly - second_poly)
    }
    
    #if the matrix is larger than 2 by 2 we loop through the top row, selecting
    #each value in the top row as the index, then multiplying it by the matrix created
    #by removing the top row along with the column that the selected value was in
    else {
      #gets top row and initializes the variable we are returning
      top_row<-matrix[1,]
      return_value<- c()
      
      #loops through the top row of the matrix
      for (j in 1:ncol(matrix)) {
        #starting from the left we select the singlular value to work with
        working_val<-top_row[j] 
        
        #remove the column and row
        new_matrix<- matrix[-1, -j]
        
        #take the determinate of the new matrix and multiply it by the current value
        determinant_of_smaller<-determinant(new_matrix)
        end_val <- determinant_of_smaller * polynomial(c(rep.int(0, working_val), 1))
        
        #add or subract the value based on its position in the top row
        if (j %% 2 == 1) x <- 1
        else x<- -1
        return_value <- return_value + (x * end_val)
      }
      return(return_value)
  
    }
  }
  #divides our final polynomial by (t-1)^(n-1) to get the final form of the alexander polynomial
  t_minus_1_poly <- polynomial(c(-1, 1))
  initial_polynomial <- determinant(make_matrix(sigma_o, sigma_x))
  for (i in 1:(length(sigma_o) - 1)) {
    initial_polynomial <- initial_polynomial / t_minus_1_poly
  }
  return(initial_polynomial)
}


