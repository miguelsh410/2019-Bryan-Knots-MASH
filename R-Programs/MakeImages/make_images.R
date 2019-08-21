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
