random_grids_v2 <- function(size_of_grid, number_of_grids){
  
  for(i in 1:number_of_grids){
    
    sigma_o <- sample(size_of_grid, size = size_of_grid)
    
    sigma_x <- sample(size_of_grid, size = size_of_grid)
    
    print("sigma_0: ")
    print(sigma_o)
    
    print("sigma_x_1: ")
    print(sigma_x)
    
    for(i in 1:size_of_grid){
      while (sigma_x[i] == sigma_o[i]){
        sigma_x[i] <- sample(size_of_grid, 1)
      }
      #if(sigma_x[i] %in% sigma_x[-sigma_x[i]]){
       # if ((sigma_x[i+1] != sigma_o[i]) & (sigma_x[i] != sigma_o[i+1])){
        #  replace(sigma_x, i, sigma_x[i+1])
        #}
        #else if((sigma_x[i-1] != sigma_o[i]) & (sigma_x[i] != sigma_o[i-1])){
         # replace(sigma_x, i, sigma_x[i-1])
        #}
    #}
    print("sigma_x_2: ")
    print(sigma_x)
  }
  }
}