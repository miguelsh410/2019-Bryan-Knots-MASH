# This function graphs the analysis of components after the file is analyzed
# by the alexander polynomial function in Mathematica.
# In other words, it graphs the components proportion and the splittable links
# proportion.
graph_analysis_split <- function(filename, xlimit = NULL, ylimit = NULL, color = "green", fill = "black", title = "Component proportions",
                                 subtitle = NULL, caption = NULL, PNG = TRUE, output_name = NULL){
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
  View(df)
  #This will be the name of the image when it is saved
  filename <- paste(strsplit(filename, ".csv"), "_graph", ".png", sep = "")
  
  # Here is where the graphing happens, the number of components is on the 
  # x-axis, and the proportion on the y-axis
  # All the if statemenst are for fixing the graph x and y axis
  if (!is.null(xlimit) & !is.null(ylimit)){
    graph<-ggplot(df, aes(x = Components)) +
      geom_bar(aes(y=Proportion), stat = "identity", color = 'green', fill = 'red') + xlim(xlimit) + ylim(ylimit)+
      geom_bar(aes(y=split*Proportion), stat = "identity", color = 'green', fill = 'black') + 
      geom_bar(aes(fill=value))
  }
  else if (!is.null(xlimit)){
    graph<-ggplot(df, aes(x = Components)) +
      geom_bar(aes(y=Proportion), stat = "identity", color = 'green', fill = 'red') + xlim(xlimit) +
      geom_bar(aes(y=split*Proportion), stat = "identity", color = 'green', fill = 'black') + 
      geom_bar(aes(fill=value))
  }
  else if (!is.null(ylimit)){
    graph<-ggplot(df, aes(x = Components)) +
      geom_bar(aes(y=Proportion), stat = "identity", color = 'green', fill = 'red') + ylim(ylimit)+
      geom_bar(aes(y=split*Proportion), stat = "identity", color = 'green', fill = 'black') + 
      geom_bar(aes(fill=value))
  }
  else{
    graph<-ggplot(df, aes(x = Components)) +
      geom_bar(aes(y=Proportion), stat = "identity", color = 'green', fill = 'red') +
      geom_bar(aes(y=split*Proportion), stat = "identity", color = 'green', fill = 'black') + 
      geom_bar(aes(fill=value))
  }
  
  # If PNG is se to TRUE, save the image to the working directiory
  if (PNG) {
    ggsave(filename, device = "png")
    shell.exec(filename)
  }
  # If PNG is set to FALSE just display the graph in R
  else graph
}