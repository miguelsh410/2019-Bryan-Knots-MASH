import random
import tkinter as tk


def main():

    def make_grid(n, fixed):
        if n < 2:
            return print("enter value greater than 1")

        sigma_o = []
        sigma_x = []
        grid = []

        for list in range(n):
            grid_list = []
            for number in range(n):
                grid_list.append(0)
            grid.append(grid_list)

        used_n_values = []
        for i in range(n):
            used_n_values.append(i)

        if fixed:
            for i in range(n):
                sigma_o.append(i + 1)
                grid[n-i-1][i] = 1

        else:
            for i in range(n):
                rand = random.choice(used_n_values)
                used_n_values.remove(rand)
                sigma_o.append(rand+1)
                grid[n-i-1][rand] = 1

        #print(sigma_o)
        used_n_values = []
        for i in range(n):
            used_n_values.append(i+1)

        for i in range(n):
            found = False
            while not found:
                x = random.choice(used_n_values)
                if sigma_o[i] != x:
                    used_n_values.remove(x)
                    sigma_x.append(x)
                    found = True
                    grid[n-i-1][x-1] = 2

        for i in range(n):
            if sigma_o[i] > sigma_x[i] and sigma_o[i] - sigma_x[i] > 1:
                for j in range(sigma_o[i] - sigma_x[i] - 1):
                    grid[n-i-1][j+sigma_x[i]] = 3
            if sigma_x[i] > sigma_o[i] and sigma_x[i] - sigma_o[i] > 1:
                for j in range(sigma_x[i] - sigma_o[i] - 1):
                    grid[n-i-1][j+sigma_o[i]] = 3

        all_column = []
        for i in range(n):
            column = []
            for j in range(n):
                column.append(grid[j][i])
            firstNode = False
            firstfound = 0

            for k in range(n):
                if firstNode and column[k] == 0:
                    column[k] = 4
                if firstNode and column[k] == 3:
                    column[k] = 5


                if (column[k] == 1 or column[k] == 2) and firstfound < 2:
                    firstNode = True
                    firstfound += 1
                if firstfound ==  2:
                    firstNode = False
            all_column.append(column)

        print(sigma_o)
        print(sigma_x)

        return all_column



    def draw_grid(list):
        master = tk.Tk()

        # tk.Label(master, text="simgaO = " + str(sigma_o)).grid(row=0)
        # tk.Label(master, text="simgaX = " + str(sigma_x)).grid(row=1)

        for o in range(len(list)):
            for x in range(len(list)):
                if list[x][o] == 1:
                    tk.Label(master, text=" O ").grid(row=o, column=x + 2)
                if list[x][o] == 2:
                    tk.Label(master, text=" X ").grid(row=o, column=x + 2)
                if list[x][o] == 3:
                    tk.Label(master, text="---").grid(row=o, column=x + 2)
                if list[x][o] == 4:
                    tk.Label(master, text=" | ").grid(row=o, column=x + 2)
                if list[x][o] == 5:
                    tk.Label(master, text="-|-").grid(row=o, column=x + 2)

        # e1 = tk.Entry(master)
        # e2 = tk.Entry(master)
        #
        # e1.grid(row=0, column=1)
        # e2.grid(row=1, column=1)

        master.mainloop()

#write actions below

    draw_grid(make_grid(40, False))

if __name__ == '__main__':
    main()
