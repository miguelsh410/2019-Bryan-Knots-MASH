# This function makes a line graph where the points are the 
# mean of the samples of random grid diagrams
meanComps <- function(size_of_grid, increase_by, number_of_times){
  # This will be for the functions required to graph
  require(ggplot2)
  
  # This wil store the data frame that we want to graph
  mean.df <- data.frame()
  
  # Loop as many times as we need to create the specified number of points
  for (i in 1:number_of_times){
    # This is the file with the random grid diagrams generated
    grids <- read.csv(random_grids(size_of_grid, 1000))
    
    # Add all the component numbers from the grids variable
    component_sum <- sum(grids$Number.of.Components)
    
    # Get the mean of the component numbers
    meanComp <- (component_sum/length(grids$Number.of.Components))
    
    # Store the mean and the size of the grid inside mean.df
    mean.df <- rbind(mean.df, c(size_of_grid, meanComp))
    
    # Increase the size of the grid by the specified number
    size_of_grid <- (size_of_grid + increase_by)
  }
  
  # Rename the columns
  colnames(mean.df) <- c("Grid Size", "Component Mean")
  
  # Create a file name
  filename <- paste("meanGraph_", size_of_grid, "_",increase_by, "_",number_of_times, ".png", sep="")
  
  # Start graphing the imformation that we got before
  graph <- ggplot(data=mean.df, aes(x=mean.df[,1], y=mean.df[,2], group=1)) +
    geom_line()+
    geom_point()+
    xlab("Grid Size")+
    ylab("Component Mean")
  
  # Save the png file in our working directory
  ggsave(filename, device = "png")
}
