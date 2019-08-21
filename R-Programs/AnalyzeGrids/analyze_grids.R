# This function analyzes the output from the random_grids function or
# it can analyze the output from the Mathematica alexander polynomial calculator
# created in 2017
# This function reads .csv files only
# The output will be a .csv file with the different number of components, their proportion,
# and in case it is the output from Mathematica, it will give information about the splittable links
# It will also give a confidence interval and confidence level
# This function is a modified version of analyze_components
analyze_grids <- function(filename, conf_level = 0.95){
  # If the file is the output of the Mathematica function to get the
  # alexander polynomial, run this analysis
  if (grepl("alex", filename) | grepl("mathe", filename)){
    # Read in the csv file
    my_file <- read.csv(filename)
    
    # Count the number compon numbers and store in table
    component_amounts <- table(my_file[,3])
    
    # Store the table as a data frame
    component_amounts.df <- as.data.frame(component_amounts)
    
    # Create an empty data fram
    analysis_df <- data.frame()
    
    # Loop through each component number
    for (i in 1:length(component_amounts.df[,1])){
      # Get the number of components
      component_number <- as.numeric(as.character(component_amounts.df[i, 1]))
      
      # Find number of splittable links for each component number
      number_of_split <- sum(my_file[which(my_file[,3] == i),5])
      
      # This is the frequency or number of times the ith number of component appears
      freq <- component_amounts.df[i, 2]
      
      # This is how many rows the file has
      row_length <- length(my_file[,3])
      
      # Calculate the decimal proportion of the number of he current component number
      component_decimal <- (freq / row_length)
      
      # Turn decimal into a percentage
      component_percent <- (component_decimal * 100)
      
      #Print that percentage to the console
      print(paste(component_number, "Component Percentage:", component_percent, "%"))
      
      
      # Perform prop.test on that component number value
      proportion_test <- prop.test(freq, row_length, conf.level = conf_level)
      
      # Store p-estimate from prop.test result
      p_estimate <- as.numeric(proportion_test[4])
      
      # Stroe the confidence interval in a useful way
      conf_int <- strsplit(sub("[)]", "", sub("c[(]", "", as.character(proportion_test[6]))), ", ")
      
      
      # Get the lower and upper endpoints of the confidence intervals separately
      conf_int_lower <- conf_int[[1]][1]
      conf_int_upper <- conf_int[[1]][2]
      
      
      # Store the splittable link per component p-estimate from prop.test result
      split_p_estimate <- number_of_split/freq
      
      #if the number of a certain component amount in less than ten 
      #we use a binom.test to find our confidence interval
      if (i != 1) {
        if (freq < 10) {
          split_binom_test <- binom.test(number_of_split, freq, conf.level = conf_level)
          
          
          # Store the splittable link per component confidence interval in a useful way
          split_conf_int <- strsplit(sub("[)]", "", sub("c[(]", "", as.character(split_binom_test[4]))), ", ")
          
          split_conf_int_lower <- split_conf_int[[1]][1]
          split_conf_int_upper <- split_conf_int[[1]][2]
          
        }
        # here is the code to use the prop.test to get our confidence interval
        else {
          split_prop_test <- prop.test(number_of_split, freq, conf.level = conf_level)
          
          # Store the splittable link per component confidence interval in a useful way
          split_conf_int <- strsplit(sub("[)]", "", sub("c[(]", "", as.character(split_prop_test[6]))), ", ")
          
          split_conf_int_lower <- split_conf_int[[1]][1]
          split_conf_int_upper <- split_conf_int[[1]][2]
        }}
      #this is used when we anallyze the one component grids
      else {
        split_p_estimate <- 0
        split_conf_int_lower <- 0
        split_conf_int_upper <- 0
      }
      
      # Create a row for the data frame that contains the data from the prop.test
      data_frame_row <- cbind(component_number, p_estimate, conf_int_lower, conf_int_upper, split_p_estimate, 
                              split_conf_int_lower, split_conf_int_upper, attr(proportion_test$conf.int, "conf.level"))
      
      # Bind that row to the datat frame
      analysis_df <- rbind(analysis_df, data_frame_row)
    }
    
    # Create the filename of the csv file that will be returned
    filename <- paste(strsplit(filename, ".csv"), "_analyzed", ".csv", sep = "")
    
    # Clear any row names of the data frame
    row.names(analysis_df) <- c()
    
    # Create the column names for the data frame
    colnames(analysis_df) <- c("Component number", "Component Proportion", "Component Confidence Interval Lower", 
                               "Component Confidence Interval Upper", "Splittable Links Proportion", 
                               "Splittable Link Confidence Interval Lower", "Splittable Link Confidence Interval Upper",
                               "Confidence Level")
    
    # Turn the data frame into a csv file and return it
    write.csv(as.matrix(analysis_df), filename, row.names = FALSE)
    return(filename)
  }
  # Do a normal analyze_components is the file is not the mathematica alexander polynomial output
  else analyze_components(filename = filename, conf_level = conf_level)
}