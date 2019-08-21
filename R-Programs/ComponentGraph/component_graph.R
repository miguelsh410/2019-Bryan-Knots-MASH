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
