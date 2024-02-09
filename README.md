

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

 - `Admin.java` -> This file comprises functions that correspond to operations on the admin screen.
The Admin has the ability to access the roster of rented vehicles, with the authority to add, remove, or modify vehicles as needed. Furthermore, the Admin can perform searches based on a vehicle's name or number plate, and is responsible for overseeing fine calculations and managing payments, in addition to implementing various other functionalities.

 - `Borrower.java` -> This file contains the display screen for the borrower and the borrwer cart, which has the vehicles to be rented

 - `DbConnection.java` -> This file contains the interface for connecting with the database in mysql

 - `Main.java` -> This is the main driver code that is used to run either the admin or the borrower side

 - `Payment.java` -> This file handles the payment between the rental owner and the borrower
