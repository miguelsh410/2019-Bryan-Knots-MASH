# This function analyzes the output from the random_grids function
# This function reads .csv files only
# The output will be a .csv file with the different number of components and their proportion
# It will also give a confidence interval and confidence level
analyze_components <- function(filename, conf_level = 0.95){
  # Read in the csv file
  my_file <- read.csv(filename)

  # Count the number compon numbers and store in table
  component_amounts <- table(my_file[,4])
  
  # Store the table as a data frame
  component_amounts.df <- as.data.frame(component_amounts)
  
  # Create an empty data fram
  analysis_df <- data.frame()
  
  # Loop through each component number
  for (i in 1:length(component_amounts.df[,1])){
    
    #find number of splits
    number_of_split<-sum(my_file[which(my_file[,4]==i),6])
    
    # Get the number of components
    component_number <- as.numeric(as.character(component_amounts.df[i, 1]))
  
    # Calculate the decimal proportion of the number of he current component number
    component_decimal <- (component_amounts.df[i, 2] / length(my_file[,3]))
    
    # Turn decimal into a percentage
    component_percent <- (component_decimal * 100)
    
    #Print that percentage to the console
    print(paste(component_number, "Component Percentage:", component_percent, "%"))
    
    # Perform prop.test on that component number value
    proportion_test <- prop.test(component_amounts.df[i, 2], length(my_file[,3]), conf.level = conf_level)
    
    # Store specific data from the prop.test into variables
    p_estimate <- as.numeric(proportion_test[4])
    
    # Stroe the confidence interval in a useful way
    conf_int <- strsplit(sub("[)]", "", sub("c[(]", "", as.character(proportion_test[6]))), ", ")
    
    # Get the lower and upper endpoints of the confidence intervals separately
    conf_int_lower <- conf_int[[1]][1]
    conf_int_upper <- conf_int[[1]][2]
    
    #find the porportion of number of splittable links for each number of components
    splitprop<-(number_of_split/component_amounts.df[i, 2])
    
    # Create a row for the data frame that contains the data from the prop.test
    data_frame_row <- cbind(component_number, p_estimate, conf_int_lower, conf_int_upper, attr(proportion_test$conf.int, "conf.level"), splitprop)
    
    # Bind that row to the datat frame
    analysis_df <- rbind(analysis_df, data_frame_row)
  }
  
  # Create the filename of the csv file that will be returned
  filename <- paste(strsplit(filename, ".csv"), "_analyzed", ".csv", sep = "")
  
  # Clear any row names of the data fram
  row.names(analysis_df) <- c()
  
  # Create the column names for the data frame
  colnames(analysis_df) <- c("Component number", "Proportion", "Confidence Interval Lower", "Confidence Interval Upper", "Confidence Level", "Splittable Links Proportion")
  
  # Turn the data frame into a csv file and return it
  write.csv(as.matrix(analysis_df), filename, row.names = FALSE)
  return(filename)
}