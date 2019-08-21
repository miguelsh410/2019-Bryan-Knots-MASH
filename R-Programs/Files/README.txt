We are embedding some pieces of information about the file within the file name,
they are separated by the underscores.
The files' name structure is the following:
- The first number represents the size of each grid.
- The second number is the number of grids inside the file.
- The last number is the seed for generating the grids in the file.
- If the file name contains _math, this means the file is formatted for the alexander polynomial function in mathematica created in 2017.
- If the file name comtains _analyzed, this means that is the outpud of the analyze_components function, and contains only, the number of components and its proportion, confidence interval, and confidence level.
- If the file name contains _graph, this means it is a graph of the output from the analyze_components function, the graph only include number of components and the proportion.
- If the file name contains "_alex", this means it is the output from the Mathematica function created in 2017 to calculate the alexander polynomial, this file contains wether the link is splittable or not.
- When the file starts with either "allGraph", "meanGraph", "modeGraph", or "medianGraph" this means it is a line graph with information about components
	- In this case, the first number after the underscore is the starting size of the analized grid diagrams
	- The second number is the amount we are increasing the grid size by
	- The third number is the number of times we increase the grid diagram size