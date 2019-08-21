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

# This function analyzes the output from the random_grids function
# This function reads .csv files only
# The output will be a .csv file with the different number of components and their proportion
# It will also give a confidence interval and confidence level
analyze_components <- function(filename, conf_level = 0.95){
  # Read in the csv file
  my_file <- read.csv(filename)
  
  # Get the component numbers and store in table
  component_amounts <- table(my_file[,3])
  
  # Store the table as a data frame
  component_amounts.df <- as.data.frame(component_amounts)
  
  # Create an empty data fram
  analysis_df <- data.frame()
  
  # Loop through each component number
  for (i in 1:length(component_amounts.df[,1])){
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
    
    # Create a row for the data frame that contains the data from the prop.test
    data_frame_row <- cbind(component_number, p_estimate, conf_int_lower, conf_int_upper, attr(proportion_test$conf.int, "conf.level"))
    
    # Bind that row to the datat frame
    analysis_df <- rbind(analysis_df, data_frame_row)
  }
  
  # Create the filename of the csv file that will be returned
  filename <- paste(strsplit(filename, ".csv"), "_analyzed", ".csv", sep = "")
  
  # Clear any row names of the data fram
  row.names(analysis_df) <- c()
  
  # Create the column names for the data frame
  colnames(analysis_df) <- c("Component number", "Proportion", "Confidence Interval Lower", "Confidence Interval Upper", "Confidence Level")
  
  # Turn the data frame into a csv file and return it
  write.csv(as.matrix(analysis_df), filename, row.names = FALSE)
  
  return(filename)
}

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

# This function will graph the output of the analyze_component function
# More specifically, it will make a graph with the proportion of the components
# The file should be a .csv file in order for it to work correctly
# You can either save the graph as a .png file or have it displayed 
# directly on R, the default is to create the .png file
# Before running the function, you need to set the working directory to the desired location
# If the title is set to null, the title will be formatted differently
# Xlimit and ylimit must be a numeric character vector in the form of c(numeric, numeric)
graph_analysis <- function(filename, xlimit = NULL, ylimit = NULL, color = "royalblue4", fill = "gray9", title = "Component proportions",
                           subtitle = NULL, caption = NULL, PNG = TRUE, output_name = NULL, font_size = 22, split_fill = "seagreen3",
                           non_split_fill = "gray9", transparency = 0.65){
  # ggplot2 is rerquired to graph 
  require(ggplot2)
  # readr is required to use the parse_number function
  require(readr)
  
  # Get the size of the grid based on the file name
  size_of_grid <- parse_number(filename)
  
  # Get the number of grids generated based on the file name
  number_of_grids <- parse_number(strsplit(filename, 
                                           paste(size_of_grid, "_", sep = ""))[[1]][2])
  
  # Format how the title will look in the graph
  if (is.null(subtitle) & !is.null(title)) {
    if (title == "Component proportions"){
      subtitle = paste("of", number_of_grids, "grid diagrams of size", size_of_grid, "by", size_of_grid)
    }
    else{
      subtitle = paste("Analysis of", number_of_grids, "grid diagrams of size", size_of_grid, "by", size_of_grid)
    }
  }
  if (is.null(title)){
    title <- paste("Component proportions of", number_of_grids, "grid diagrams of size", size_of_grid, "by", size_of_grid)
  }
  
  # This is the file previously created with the analyze_components function
  my_file <- read.csv(filename)
  
  # We make a data frame with only the columns we need
  # We only need the Component Number and the proporition
  df <- data.frame(Proportion = my_file[,2], Components = my_file[,1], split = my_file[,5])
  
  if (grepl("alex", filename) | grepl("mathe", filename)){
    # Here is where the graphing happens, the number of components is on the 
    # x-axis, and the proportion on the y-axis
    graph<-ggplot(df, aes(x = Components)) +
      geom_bar(aes(y=Proportion, fill = "Not splittable"), stat = "identity", alpha=transparency) +
      geom_bar(aes(y=split*Proportion, fill = "Splittable"), stat = "identity", alpha=transparency) +
      labs(title = title, subtitle = subtitle, caption = caption)+
      scale_fill_manual("Splittable links", 
                        values=c("Not splittable" = non_split_fill, "Splittable" = split_fill))
  }
  else{
    # Here is where the graphing happens, the number of components is on the 
    # x-axis, and the proportion on the y-axis
    graph <- ggplot(df, aes(x = Components, y = Proportion)) + 
      geom_bar(stat = "identity", color = color, fill = fill, alpha = transparency) + 
      labs(title = title, subtitle = subtitle, caption = caption)
  }
  
  # This will be the name of the image when it is saved
  if (!is.null(output_name)) filename <- paste(output_name, ".png", sep = "")
  else{
    filename <- paste(strsplit(filename, ".csv"), "_graph", ".png", sep = "")
  }
  
  # Modify the font size
  graph <- graph + theme_bw(base_size = font_size)
  
  # Add x and y limists to the graph
  if (!is.null(xlimit)) graph <- graph + xlim(xlimit)
  if (!is.null(ylimit)) graph <- graph + ylim(ylimit)
  
  # If PNG is se to TRUE, save the image to the working directiory
  if (PNG) ggsave(filename, device = "png")
  # If PNG is set to FALSE just display the graph in R
  else graph
}

# This function will create as many images as desired
# It can increase the size of the grid diagram every time,
# or increase the number of grid diagrams created, or it can simply have a 
# fixed grid diagram size and a fixed number of grid diagrams and just create
# grid diagrams with different seed 
# Before running the function, you need to set the working directory to the desired location
# Xlimit and ylimit must be a numeric character vector in the form of c(numeric, numeric)
make_images <- function(number_of_images, grid_size = 2, number_of_grids = 500, seed = FALSE,
                        increase_grid_size_by = NULL, increase_number_of_grids_by = NULL, 
                        PNG = TRUE, sequence_names = TRUE, xlimit = NULL, ylimit = NULL, 
                        font_size = 15){
  # Loop as many times needed to create the specified number of images
  for (i in 1:number_of_images){
    print(paste(i, "/", number_of_images, sep = ""))
    
    # This will set a name for each image as a sequence easy to read
    if (sequence_names){
      # Use the graph_analysis function to create the images
      graph_analysis(analyze_grids(random_grids(grid_size, number_of_grids, seed = seed)),
                     PNG = PNG, output_name = paste("graph-", i, sep = ""), 
                     xlimit = xlimit, ylimit = ylimit, font_size = font_size)
    }
    # This will set the name of each image based on the file that is created from
    else{
      # Use the graph_analysis function to create the images
      graph_analysis(analyze_grids(random_grids(grid_size, number_of_grids, seed = seed)),
                     PNG = PNG, xlimit = xlimit, ylimit = ylimit)
    }
    
    # Increase the grid size 
    if (!is.null(increase_grid_size_by)) grid_size <- grid_size + increase_grid_size_by
    # Increase the number of grids created
    if (!is.null(increase_number_of_grids_by)) number_of_grids <- number_of_grids + increase_number_of_grids_by
  }
}

# This function makes a line graph where the points are the 
# analysis of the samples of random grid diagrams
component_graph <- function(size_of_grid, increase_by, number_of_times, sample_size = 1000,
                            type_of_analysis = "all", PNG = TRUE){
  # This will be for the function to graph
  require(ggplot2)
  
  # Starting grid size 
  starting_size <- size_of_grid
  
  # This wil store the data frame that we want to graph
  analysis.df <- data.frame()
  
  # Loop as many times as we need to create the specified number of points
  for (i in 1:number_of_times){
    # Print the progress
    print(paste("Working on grid size", size_of_grid))
    
    # This is the file with the random grid diagrams generated
    my_file <- read.csv(random_grids(size_of_grid, sample_size))
    
    # The following if statements are to porform the required calculations to get
    # the mean, mode, or median.
    if (grepl("mean", type_of_analysis, ignore.case = TRUE)){
      # Add all the component numbers from the grids variable
      component_sum <- sum(my_file$Number.of.Components)
      
      # Get the mean of the component numbers
      component_analysis <- (component_sum/length(my_file$Number.of.Components))
    }
    else if (grepl("mode", type_of_analysis, ignore.case = TRUE)){
      # Count the component numbers and store in table
      comp_num <- table(my_file[,3])
      
      # Store the table as a data frame
      comp_num <- as.data.frame(comp_num)
      
      # Get the mode of the component number
      component_analysis <- as.numeric(as.character(comp_num[which(comp_num[,2] == max(comp_num[,2])), 1]))
    }
    else if (grepl("median", type_of_analysis, ignore.case = TRUE)){
      # Get the median of the component numbers
      component_analysis <- median(my_file$Number.of.Components)
    }
    else if (grepl("all", type_of_analysis, ignore.case = TRUE)){
      # Add all the component numbers from the grids variable
      component_sum <- sum(my_file$Number.of.Components)
      # Get the mean of the component numbers
      component_mean <- (component_sum/length(my_file$Number.of.Components))
      
      
      # Count the component numbers and store in table
      comp_num <- table(my_file[,3])
      # Store the table as a data frame
      comp_num <- as.data.frame(comp_num)
      # Get the mode of the component number
      component_mode <- as.numeric(as.character(comp_num[which(comp_num[,2] == max(comp_num[,2])), 1]))
      
      # Get the median of the component numbers
      component_median <- median(my_file$Number.of.Components)
      
      analysis.df <- rbind(analysis.df, cbind("Grid Size" = size_of_grid, "Value" = component_mean, "Analysis" = "Mean"), 
                           cbind("Grid Size" = size_of_grid, "Value" = component_mode, "Analysis" = "Mode"), cbind("Grid Size" = size_of_grid, "Value" = component_median, "Analysis" = "Median"))
    }
    else stop("The type of analysis should be mean, mode, median, or all")
    
    # Store the analysis and the size of the grid inside analysis.df
    if (!grepl("all", type_of_analysis, ignore.case = TRUE)){
      analysis.df <- rbind(analysis.df, c(size_of_grid, component_analysis))
    }
    
    # Increase the size of the grid by the specified number
    size_of_grid <- (size_of_grid + increase_by)
  }
  
  # Rename the columns
  if (grepl("all", type_of_analysis, ignore.case = TRUE)) {
    colnames(analysis.df) <- c("Grid Size", "Value",  "Analysis")
  }
  else colnames(analysis.df) <- c("Grid Size", paste("Component", type_of_analysis))
  
  # Create a file name
  filename <- paste(type_of_analysis, "Graph_", starting_size, "_",increase_by, "_",number_of_times, ".png", sep="")
  
  # Start graphing the imformation that we got before
  if (grepl("all", type_of_analysis, ignore.case = TRUE)){
    analysis.df$Value=as.numeric(levels(analysis.df$Value))[analysis.df$Value]
    
    graph <- ggplot(data=analysis.df, aes(x=analysis.df[,1], y=analysis.df[,2], 
                                          group=Analysis, colour = Analysis))+
      geom_line(aes(colour = Analysis))+
      geom_point()+
      xlab("Grid size")+
      ylab(paste("Number of components"))
  }
  else{
    graph <- ggplot(data=analysis.df, aes(x=analysis.df[,1], y=analysis.df[,2], group=1)) +
      geom_line()+
      geom_point()+
      xlab("Grid size")+
      ylab(paste("Component", type_of_analysis))
  }
  
  # If PNG is se to TRUE, save the image to the working directiory
  if (PNG) ggsave(filename, device = "png")
  # If PNG is set to FALSE just display the graph in R
  else graph
}
