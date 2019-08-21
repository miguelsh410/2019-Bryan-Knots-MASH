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
