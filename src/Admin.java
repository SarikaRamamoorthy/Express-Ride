import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class Admin {

    /*variables used for decorations */
    public static final String ANSI_RESET = "\u001B[0m"; 
    public static final String ANSI_MAGENTA = "\u001b[36;1m";

    static Scanner sc = new Scanner(System.in);
    static String userName;
    static String password;
    static Statement statement;
    static String sqlVehiclesList = "select v.vehicle_id,v.vehicle_name,v.Number_plate,t.type_name,t.security_deposit,v.rent,v.total_distance,v.serviced,v.borrower_id,v.rented_date,v.return_date from vehicles_info v inner join type_info t on v.type_id = t.type_id where v.isdeleted = 'N' order by v.vehicle_id;";


    public static void adminScreen(){
        
        clearScr();
        signIn();
        
        try{
            DbConnection c = new DbConnection();
            Connection connect = c.getConnection();
            statement = connect.createStatement();
            ResultSet res = statement.executeQuery("select user_password from admin_info"+" where user_name = '"+userName+"'");
            System.out.println();
            if(!res.next() || !res.getString(1).equals(password)){
                invalidSignIn();
            }
            else{
                clearScr();
                validSignIn();
            }
        }
        catch(Exception e){
            System.out.println(e);
        }

        sc.close();

    }



    public static void signIn(){
        
        System.out.println();
        System.out.println("       SIGN IN");
        System.out.println();

        System.out.print("    User Name :  ");
        userName = sc.next();

        System.out.print("    Password  :  ");
        password = sc.next();

    }

    public static void invalidSignIn(){

        System.out.println("      Invalid SignIn :( ");
        System.out.println();
        System.out.print("   Press enter to SignIn : ");
        sc.nextLine(); // for the remaining1aige return in the buffer
        sc.nextLine(); // for the enter key
        adminScreen();  

    }

    public static void validSignIn(){

        System.out.println();
        System.out.println(ANSI_MAGENTA);
        System.out.println("    Glad to have you !!");
        System.out.println(ANSI_RESET);
        Main.decor();
        System.out.println();
        boolean eventLoop = true;
        while (eventLoop) {
            eventLoop = choiceList();
        }

    }

    public static boolean choiceList(){
        System.out.println("    1. vehicles list");
        System.out.println("    2. Add vehicles");
        System.out.println("    3. Remove vehicles");
        System.out.println("    4. Modify vehicles");
        System.out.println("    5. Search a vehicle");
        System.out.println("    6. View vehicles details sorted by rent");
        System.out.println("    7. Rented vehicles");
        System.out.println("    8. Unserviced vehicles");
        System.out.println("    9. Sign Out");
        System.out.println();
        System.out.print("    Enter your choice(1/2/3/4/5/6/7/8) :  ");

        int choice = sc.nextInt();
        System.out.println();
        if(choice == 1){
            clearScr();
            vehiclesList(sqlVehiclesList);
        }
        else if(choice == 2){
            clearScr();
            sc.nextLine(); // empty line
            System.out.println();
            System.out.print("    Enter the Name of the vehicle                    :  ");
            String vehicleName = sc.nextLine();
            System.out.println();
            System.out.print("    Enter the NumberPlate of the vehicle             :  ");
            String vehicleNumberPlate = sc.nextLine();
            System.out.println();
            System.out.print("    Enter the type of the vehicle (Car/Bike)         :  ");
            String vehicleInputType = sc.nextLine();
            int vehicleType = vehicleInputType.equalsIgnoreCase("car") ? 1 : 2;
            System.out.println();
            System.out.print("    Enter the rent of the vehicle                    :  ");
            int vehicleRent = sc.nextInt();
            System.out.println();
            System.out.print("    Enter the distance travelled by the vehicle      :  ");
            int vehicleDistance = sc.nextInt();
            System.out.println();
            System.out.print("    Enter whether vehicle is serviced or not(Yes/No) :  ");
            String vehicleIsServiced = sc.next();
            System.out.println();
            try {
                ResultSet set = statement.executeQuery("select vehicle_id from vehicles_info where number_plate = '"+vehicleNumberPlate+"'");
                if(set.next()){
                    ResultSet isPresentAndDeleted = statement.executeQuery("select vehicle_id from vehicles_info where number_plate = '"+vehicleNumberPlate+"' and isDeleted = 'Y'");
                    if(isPresentAndDeleted.next()){
                        statement.execute("Update vehicles_info set isDeleted = 'N' where number_plate = '"+vehicleNumberPlate+"'");
                        clearScr();
                        System.out.println();
                        System.out.println("        Vehicle "+vehicleNumberPlate+" added Successfully ^_^");
                    }
                    else{
                        System.out.println("    !! Vehicle "+vehicleNumberPlate+" Already Exists !!");
                    }
                }
                else{
                    String insert = "insert into vehicles_info(vehicle_name, Number_plate, type_id, rent, total_distance,Serviced,borrower_id,Rented_date,Return_date) values('"+vehicleName+"','"+vehicleNumberPlate+"','"+vehicleType+"',"+vehicleRent+","+vehicleDistance+",'"+vehicleIsServiced+"',null,null,null);";
                    statement.execute(insert);
                    clearScr();
                    System.out.println();
                    System.out.println("        Vehicle "+vehicleNumberPlate+" added Successfully ^_^");
                }
            } catch (Exception e) {
                System.out.println(e);
            }
            System.out.println();
            vehiclesList(sqlVehiclesList);
        }
        else if(choice == 3){
            deleteVehicle();
        }
        else if(choice == 4){
            clearScr();
            vehiclesList(sqlVehiclesList);
            System.out.println();
            System.out.println("    Select the property to be modified : ");
            System.out.println();
            System.out.println("    1.  Vehicle Name");
            System.out.println("    2.  Number Plate");
            System.out.println("    3.  Vehicle Type");
            System.out.println("    4.  Distance Travelled");
            System.out.println("    5.  Serviced / Not Serviced");
            System.out.println("    6.  Borrower ID");
            System.out.println("    7.  Rented Date");
            System.out.println("    8.  Return Date");
            System.out.println("    9.  Rent");
            System.out.println("    10. Security Deposit");
            System.out.println("    11. Payment Details");
            System.out.println();
            System.out.print("    Choose any One(1 to 11) : ");
            int property = sc.nextInt();
            clearScr();
            if(property >= 1 && property <= 9){
                System.out.println();
                System.out.print("    Enter the Vehicle ID to be modified : ");
                int id = sc.nextInt();
                System.out.println();
                try {
                    ResultSet vehicle = statement.executeQuery("select * from vehicles_info where vehicle_id = "+id+";");
                    boolean  flag = false;
                    if(vehicle.next()){
                        flag = true;
                    }
                    if(flag){
                        if(property == 1){
                            sc.nextLine();
                            System.out.print("    Enter the updated vehicle name : ");
                            String updatedVehicleName = sc.nextLine();
                            statement.executeUpdate("update vehicles_info set vehicle_name = '"+updatedVehicleName+"' where vehicle_id = "+id);
                            System.out.println();
                            System.out.println("    ✓ Vehicle Name Modified ✓ ");
                            System.out.println();
                            vehiclesList(sqlVehiclesList);
                        }
                        else if(property == 2){
                            sc.nextLine();
                            System.out.print("    Enter the updated vehicle number plate : ");
                            String updatedNumberPlate = sc.nextLine();
                            statement.executeUpdate("update vehicles_info set number_plate = '"+updatedNumberPlate+"' where vehicle_id = "+id);
                            System.out.println();
                            System.out.println("    ✓ Vehicle Number Plate Modified ✓ ");
                            System.out.println();
                            vehiclesList(sqlVehiclesList);
                        }
                        else if(property == 3){
                            sc.nextLine();
                            System.out.print("    Enter the updated vehicle type(Car/Bike) : ");
                            String updatedType = sc.nextLine();
                            int updatedTypeId = updatedType.equalsIgnoreCase("car") ? 1:2;
                            statement.executeUpdate("update vehicles_info set type_id = "+updatedTypeId+" where vehicle_id = "+id);
                            System.out.println();
                            System.out.println("    ✓ Vehicle Type Modified ✓ ");
                            System.out.println();
                            vehiclesList(sqlVehiclesList);
                        }
                        else if(property == 4){
                            System.out.print("    Enter the distance travelled by the vehicle : ");
                            int updatedDistance = sc.nextInt();
                            statement.executeUpdate("update vehicles_info set total_distance = "+updatedDistance+" where vehicle_id = "+id);
                            System.out.println();
                            System.out.println("    ✓ Distance Travelled Modified ✓ ");
                            System.out.println();
                            vehiclesList(sqlVehiclesList);
                        }
                        else if(property == 5){
                            sc.nextLine();
                            System.out.print("    Enter whether the vehicle is serviced or not (Yes/No): ");
                            String updatedService = sc.nextLine();
                            statement.executeUpdate("update vehicles_info set serviced = '"+updatedService+"' where vehicle_id = "+id);
                            System.out.println();
                            System.out.println("    ✓ Service Status Modified ✓ ");
                            System.out.println();
                            vehiclesList(sqlVehiclesList);
                        }
                        else if(property == 6){
                            System.out.print("    Enter the Borrower ID of the vehicle : ");
                            int updatedBorrower = sc.nextInt();
                            statement.executeUpdate("update vehicles_info set borrower_id = "+updatedBorrower+" where vehicle_id = "+id);
                            System.out.println();
                            System.out.println("    ✓ Borrower ID Modified ✓ ");
                            System.out.println();
                            vehiclesList(sqlVehiclesList);
                        }
                        else if(property == 7){
                            sc.nextLine();
                            System.out.print("    Enter the Date of rent of the vehicle(yyyy-mm-dd) : ");
                            String updatedRentDate = sc.nextLine();
                            if(updatedRentDate.equals("null")){
                                statement.executeUpdate("update vehicles_info set rented_date = "+updatedRentDate+" where vehicle_id = "+id);
                            }
                            else{
                                statement.executeUpdate("update vehicles_info set rented_date = '"+updatedRentDate+"' where vehicle_id = "+id);
                            }
                            System.out.println();
                            System.out.println("    ✓ Rent Date Modified ✓ ");
                            System.out.println();
                            vehiclesList(sqlVehiclesList);
                        }
                        else if(property == 8){
                            sc.nextLine();
                            System.out.print("    Enter the Date of return of the vehicle : ");
                            String updatedReturnDate = sc.nextLine();
                            if(updatedReturnDate.equals("null")){
                                statement.executeUpdate("update vehicles_info set return_date = "+updatedReturnDate+" where vehicle_id = "+id);
                            }
                            else{
                                statement.executeUpdate("update vehicles_info set return_date = '"+updatedReturnDate+"' where vehicle_id = "+id);
                            }
                            System.out.println();
                            System.out.println("    ✓ Return Date Modified ✓ ");
                            System.out.println();
                            vehiclesList(sqlVehiclesList);
                        }
                        else if(property == 9){
                            sc.nextLine();
                            System.out.print("    Enter the rent of the vehicle : ");
                            int updatedRent = sc.nextInt();
                            statement.executeUpdate("update vehicles_info set rent = "+updatedRent+" where vehicle_id = "+id);
                            System.out.println();
                            System.out.println("    ✓ Vehivle Rent Modified ✓ ");
                            System.out.println();
                            vehiclesList(sqlVehiclesList);
                        }
                    }
                    else {
                        System.out.print("    Vehicle ID not found !! Press Enter..");
                        sc.nextLine();
                        sc.nextLine();
                        System.out.println();
                        clearScr();
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            else if(property >= 10 && property <= 11){
                try {
                    if (property == 10) {
                        sc.nextLine();
                        System.out.println("    Select the vehicle type : ");
                        System.out.println();
                        System.out.println("    1. Car");
                        System.out.println("    2. Bike");
                        System.out.println();
                        System.out.print("    Enter (1/2) : ");
                        int typeId = sc.nextInt();
                        System.out.println();
                        System.out.print("    Enter the updated security deposit amount : ");
                        int updatedSecurityDeposit =  sc.nextInt();
                        statement.executeUpdate("update type_info set security_deposit = "+updatedSecurityDeposit+" where type_id = "+typeId);
                        System.out.println();
                        System.out.println("    ✓ Security deposit Modified ✓ ");
                        System.out.println();
                        vehiclesList(sqlVehiclesList);
                    }
                    else if(property == 11){
                        
                        if(borrowerPayment()){
                            System.out.print("    Would you like to continue processing (Yes/No) ? ");
                            sc.nextLine();
                            String proceed = sc.nextLine();
                            System.out.println();
                            if(proceed.equalsIgnoreCase("yes")) {
                                clearScr();
                                System.out.println();
                                System.out.print("    Enter the borrower ID : ");
                                int borrowerid = sc.nextInt();
                                System.out.println();
                                ResultSet isPresent = statement.executeQuery("select payment_id,amount_pending from borrower_paymentdetails where borrower_id = "+borrowerid +" and amount_pending > 0;");
                                boolean flag = true;
                                ArrayList<Integer> amountPending = new ArrayList<Integer>();
                                ArrayList<Integer> paymentId = new ArrayList<Integer>();
                                while(isPresent.next()){
                                    amountPending.add(isPresent.getInt(2));
                                    paymentId.add(isPresent.getInt(1));
                                    
                                    flag = false;
                                }
                                if(flag){
                                    System.out.print("    Borrower ID not found !! Press Enter..");
                                    sc.nextLine();
                                    sc.nextLine();
                                    System.out.println();
                                    clearScr();
                                }
                                else{
                                    for(int i=0;i<amountPending.size();i++){
                                        String query = "update borrower_paymentdetails set payment_status = true, amount_paid = "+amountPending.get(i)+" , amount_pending = 0 where payment_id = "+paymentId.get(i)+";";
                                        statement.execute(query);
                                    }
                                    borrowerPayment();
                                }
                            }
                            else if(proceed.equalsIgnoreCase("no")) {
                                System.out.print("    Processing cancelled ! Press Enter..");
                                sc.nextLine();
                                clearScr();
                            }
                            else {
                                System.out.print("    Invalid !! Press Enter.. ");
                                sc.nextLine();
                                clearScr();
                            }
                        }
                    }

                } catch (Exception e) {
                    System.out.println(e);
                }

            }
            else{
                System.out.println("    Invalid Selection !!");
            }
        }
        else if(choice == 5){

            clearScr();
            System.out.println();
            System.out.println("    Select your choice");
            System.out.println();
            System.out.println("    1. Car ");
            System.out.println("    2. Bike ");
            System.out.println();
            System.out.print("    Enter your choice(1/2) : ");
            int type = sc.nextInt();
            if(type == 1 || type == 2){
                clearScr();
                System.out.println();
                System.out.println("    Select your choice");
                System.out.println();
                System.out.println("    1. Search by vehicle name ");
                System.out.println("    2. Search by vehicle number plate ");
                System.out.println();
                System.out.print("    Enter your choice(1/2) : ");
                int select = sc.nextInt();
                if(select == 1){
    
                    clearScr();
                    System.out.println();
                    System.out.print("    Enter the Name of the Vehicle to search : ");
                    sc.nextLine();//empty
                    String vehicleName = sc.nextLine();
                    System.out.println();
                    String sqlSearchByName = "select v.vehicle_id,v.vehicle_name,v.Number_plate,t.type_name,t.security_deposit,v.rent,v.total_distance,v.serviced,v.borrower_id,v.rented_date,v.return_date from vehicles_info v inner join type_info t on v.type_id = t.type_id where vehicle_name = '"+vehicleName+"' and isdeleted = 'N' and t.type_id = "+type+" order by v.vehicle_id";
                    vehiclesList(sqlSearchByName);
    
                }
                else if(select == 2){
    
                    clearScr();
                    System.out.println();
                    System.out.print("    Enter the Number Plate of the Vehicle to search : ");
                    sc.nextLine();//empty
                    String vehicleNumberPlate = sc.nextLine();
                    System.out.println(); 
                    String sqlSearchByNumberPlate = "select v.vehicle_id,v.vehicle_name,v.Number_plate,t.type_name,t.security_deposit,v.rent,v.total_distance,v.serviced,v.borrower_id,v.rented_date,v.return_date from vehicles_info v inner join type_info t on v.type_id = t.type_id where Number_plate = '"+vehicleNumberPlate+"' and isdeleted = 'N' and t.type_id = "+type;
                    vehiclesList(sqlSearchByNumberPlate);
    
                }
                else {
                    System.out.print("    Invalid Choice ! Press Enter ");
                    sc.nextLine();
                    sc.nextLine();
                }   
            }
            else {
                System.out.println();
                System.out.print("    Invalid Choice ! Press Enter ");
                sc.nextLine();
                sc.nextLine();
                Main.clearScr();
            }
        }
        else if(choice == 6){
            try {
                ResultSet orderedByRent = statement.executeQuery("select * from vehicles_info order by rent");
                Borrower.displayTable(orderedByRent);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        else if(choice == 7) {
            clearScr();
            boolean marker = true;
            while(marker){
                System.out.println();
                System.out.println("    Rented Vehicles");
                System.out.println();
                System.out.println("    1. Previously Rented");
                System.out.println("    2. Currently Rented");
                System.out.println("    3. Not Rented");
                System.out.println("    4. Exit");
                System.out.println();
                System.out.print("    Enter the choice(1/2/3) : ");
                int status = sc.nextInt();
                if(status == 1){
                    clearScr();
                    try {
                        ResultSet set = statement.executeQuery("select vehicle_id,Vehicle_name from vehicles_info where vehicle_id in(select distinct vehicle_id from rented_vehicles where rented_returned = 3 ) order by vehicle_id");
                        if(!Borrower.displayTable(set)){
                            System.out.println();
                            System.out.print("    No vehicles have been rented Previously. Press Enter ");
                            sc.nextLine();
                            sc.nextLine();
                            System.out.println();
                        }
                        System.out.print("    Press Enter to exit ");
                        sc.nextLine();
                        sc.nextLine();
                        clearScr();
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
                else if(status == 2){
                    boolean flag = true;
                    while(flag){
                        Main.clearScr();
                        System.out.println();
                        System.out.println("    1. View List");
                        System.out.println("    2. Calculate Fine");
                        System.out.println("    3. Exit");
                        System.out.println();
                        System.out.print("    Enter the choice(1/2/3) : ");
                        int choiceRent = sc.nextInt();
                        if(choiceRent == 1){
                            clearScr();
                            try {
                                ResultSet set = statement.executeQuery("select vehicle_id,Vehicle_name from vehicles_info where vehicle_id in(select distinct vehicle_id from rented_vehicles where rented_returned != 3 ) order by vehicle_id");
                                if(!Borrower.displayTable(set)){
                                    System.out.println();
                                    System.out.print("    No vehicles have been rented currently ");
                                    sc.nextLine();
                                    sc.nextLine();
                                }
                                System.out.println();
                                System.out.print("    Press Enter to exit ");
                                sc.nextLine();
                                sc.nextLine();
                                clearScr();
                            } catch (Exception e) {
                                System.out.println(e);
                            }
                        }
                        else if(choiceRent == 2){
                            // get rented_returned value for given vehicle id
                            // if rented_returned = 0 => display vehicle not yet returned ipo
                            // if rented_returned = 1 calculate fine ok
                            clearScr();
                            System.out.println();
                            System.out.print("    Enter the vehicle ID : ");
                            int vehicle_id = sc.nextInt();
                            System.out.println();
                            boolean mark = false;
                            try {
                                ResultSet set = statement.executeQuery("select * from rented_vehicles where vehicle_id = "+vehicle_id+" and rented_returned = 1");
                                if(set.next()){
                                    mark = true;
                                }
            
                            } catch (Exception e) {
                                System.out.println(e);
                            }
                            if(mark) {
                                System.out.println("    Enter the Damage Level ");
                                System.out.println();
                                System.out.println("    0 for no damage");
                                System.out.println("    1 for low damage");
                                System.out.println("    2 for medium damage");
                                System.out.println("    3 for high damage");
                                System.out.println();
                                System.out.print("    Enter (0/1/2/3) : ");
                                int dlevel = sc.nextInt();
                                System.out.println();
                                System.out.print("    Enter the distance travelled during the period of rent : ");
                                int dtravelled = sc.nextInt();
                                try {
                                    statement.execute("update rented_vehicles set damage_level = "+dlevel+" , travelled_distance = "+dtravelled+" , rented_returned = 2 where vehicle_id = "+vehicle_id+" and rented_returned = 1");
                                    displayRentedVehicles();
                                    System.out.println();
                                    System.out.println("    Successfully updated !! Press Enter..");
                                    sc.nextLine();
                                    sc.nextLine();
                                } catch (Exception e) {
                                    System.out.println(e);
                                }
                                System.out.println();
                            }
                            else{
                                System.out.print("    Vehicle not found !! Press Enter ");
                                sc.nextLine();
                                sc.nextLine();
                                clearScr();
                            }
                        }
                        else if(choiceRent == 3){
                            clearScr();
                            System.out.println();
                            flag = false;
                        }
                    }
    
                }
                else if(status == 3){
                    clearScr();
                    try {
                        ResultSet set = statement.executeQuery("select vehicle_id,Vehicle_name from vehicles_info  where vehicle_id not in (select vehicle_id from rented_vehicles) order by vehicle_id");
                        if(!Borrower.displayTable(set)){
                            System.out.println();
                            System.out.print("    All vehicles have been rented. Press Enter ");
                            sc.nextLine();
                            sc.nextLine();
                        }
                        System.out.println();
                        System.out.print("    Press Enter to exit ");
                        sc.nextLine();
                        sc.nextLine();


                        clearScr();
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
                else if(status == 4){
                    clearScr();
                    marker = false;
                }
                else {
                    System.out.print("    Invalid Choice! Press Enter ");
                    sc.nextLine();
                    sc.nextLine();
                    clearScr();
                }

            }
        }
        else if(choice == 8){
            Main.clearScr();
            try {
                ResultSet uns = statement.executeQuery("select vehicle_id,vehicle_name,serviced from vehicles_info where serviced = 'No';");
                if(!Borrower.displayTable(uns)){
                    System.out.println();
                    System.out.println("    list is empty :) ");
                    System.out.println();
                }
                else{
                    System.out.println();
                    System.out.println("    Select your choice ");
                    System.out.println("    1. Send vehicle to service");
                    System.out.println("    2. Exit                    ");
                    System.out.println();
                    System.out.print("    Enter choice(1/2) : ");
                    int select = sc.nextInt();
                    if(select == 1){
                        Main.clearScr();
                        System.out.println();
                        System.out.print("    Enter the vehicle ID to be serviced : ");
                        int uns_vehicleId = sc.nextInt();
                        System.out.println();
                        ResultSet unser = statement.executeQuery("select * from vehicles_info where serviced = 'No' and vehicle_id = "+uns_vehicleId);
                        if(unser.next()){
                            String query = "update vehicles_info set Serviced = 'Yes' , total_distance = 0 where vehicle_id = "+uns_vehicleId;
                            statement.execute(query);
                            System.out.print("    Vehicle sent to Service (Press Enter) ");
                            sc.nextLine();
                            sc.nextLine();
                            Main.clearScr();
                            System.out.println();
                        }
                        else{
                            System.out.println("    Invalid vehicle ID");
                        }

                    }
                    else if(select == 2){
                        Main.clearScr();
                        System.out.println();
                        choiceList();
                    }
                    else{
                        System.out.print("    Invalid choice ! Press Enter ");
                        sc.nextLine();
                        sc.nextLine();
                        System.out.println();
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        else if(choice == 9){
            System.out.print("    Signing out...(Press Enter)");
            sc.nextLine();
            sc.nextLine();
            System.out.println();
            clearScr();
            Main.decor();
            Main.selectChoice();
            return false;
        }
        else{
            System.out.print("    Invalid Choice !! Press Enter ... ");
            sc.nextLine();
            sc.nextLine();
            clearLine(11);
            return true;
        }
        System.out.println();
        return true;
    }

    public static void displayRentedVehicles(){
        System.out.println();
        System.out.println("+------------+-------------+--------------+--------------------+-----------+-----------------+");
        System.out.println("| vehicle_id | borrower_id | damage_level | Travelled_distance | Extension | rented_returned |");
        System.out.println("+------------+-------------+--------------+--------------------+-----------+-----------------+");
        try {
            ResultSet set = statement.executeQuery("select * from rented_vehicles");
            while(set.next()){
                String id = String.format("%02d", set.getInt(1));
                String vehicle_id = String.format("|%-12s", " "+id);
                String bid = String.format("%02d", set.getInt(2));
                String borrower_id = String.format("|%-13s", " "+bid);
                String damage = String.format("|%-14s", " "+set.getInt(3));
                String distance = String.format("|%-20s", " "+set.getInt(4));
                String extension = String.format("|%-11s", " "+set.getInt(5));
                String returnReturned = String.format("|%-17s|", " "+set.getInt(6));
                System.out.println(vehicle_id+""+borrower_id+""+damage+""+distance+""+extension+""+returnReturned);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("+------------+-------------+--------------+--------------------+-----------+-----------------+");
    }

    public static void mainMenu(){
        int limitCounter = 0;
        System.out.println();
        while(limitCounter < 500){
            System.out.print("    Type Exit for Main Menu :  ");
            String choiceString = sc.next();
            if("Exit".equalsIgnoreCase(choiceString)){
                clearScr();
                Main.decor();
                choiceList();
                break;
            }
            else{
                clearLine(1);
                limitCounter++;
            }
        }
    }

    public static void vehiclesList(String query){
        try {
            
            ResultSet result = statement.executeQuery(query);
            if(!result.next()){
                System.out.println("    No vehicle found !!");
            }
            else{
                System.out.println("+------------+------------------------+--------------+-----------+------------------+------+----------------+----------+-------------+-------------+-------------+");
                System.out.println("| Vehicle Id |    Vehicle Name        | Number Plate | Type Name | Security Deposit | Rent | Total Distance | Serviced | Borrower Id | Rented Date | Return Date |");
                System.out.println("+------------+------------------------+--------------+-----------+------------------+------+----------------+----------+-------------+-------------+-------------+");
                do {
                    String id = String.format("%02d", result.getInt(1));
                    String vehicle_id = String.format("|%-12s|", " "+id);
                    String vehicle_name = String.format("%-24s|"," "+result.getString(2));
                    String Number_plate = String.format("%-14s|"," "+result.getString(3));
                    String type_name = String.format("%-11s|"," "+result.getString(4));
                    String security_deposit = String.format("%-18s|"," "+result.getString(5));
                    String rent = String.format("%-6s|"," "+result.getString(6));
                    String total_distance = String.format("%-16s|"," "+result.getString(7));
                    String serviced = String.format("%-10s|"," "+result.getString(8));
                    String borrower_id = String.format("%-13s|"," "+result.getInt(9));
                    String rented_date = String.format("%-13s|"," "+result.getString(10));
                    String return_date = String.format("%-13s|"," "+result.getString(11));
                    System.out.println(vehicle_id+""+vehicle_name+""+Number_plate+""+type_name+""+security_deposit+""+rent+""+total_distance+""+serviced+""+borrower_id+""+rented_date+""+return_date);
                } while (result.next());
                System.out.println("+------------+------------------------+--------------+-----------+------------------+------+----------------+----------+-------------+-------------+-------------+");
            }
            
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public static void deleteVehicle(){
        clearScr();
        System.out.println();
        System.out.print("    Enter the vehicleID to be removed :  ");
        String remove = sc.next();
        System.out.println();
        System.out.print("    Are you sure to remove the vehicle "+ remove +" ? Type 'sure' to remove : ");
        String verify = sc.next();
        System.out.println();
        if("sure".equalsIgnoreCase(verify)){

            try{
                int vehicle = statement.executeUpdate("update vehicles_info set isdeleted = 'Y' where vehicle_id = '"+remove+"' and isdeleted = 'N';");
                if(vehicle == 0){
                    clearScr();
                    System.out.println();
                    System.out.println("    OOPS !! No vehicle found");
                    System.out.println();
                    System.out.print("   Press Enter to continue...");
                    sc.nextLine();
                    sc.nextLine();
                    deleteVehicle();
                }
                else{
                    System.out.println("    Successfully Removed :)");
                    System.out.println();
                }
            }
            catch(Exception e){
                System.out.println(e);
            }
        }
        else{
            mainMenu();
        }
    }

    public static boolean borrowerPayment() {
        try {
            ResultSet result = statement.executeQuery("select * from borrower_paymentdetails");
            if(!result.next()){
                System.out.println("    Empty Payment Details !!");
                return false;
            }
            else{
                System.out.println();
                System.out.println("    -1 in vehicleID represents the Caution Deposit");
                System.out.println();
                System.out.println("+------------+------------+-------------+----------------+-------------+----------------+");
                System.out.println("| Payment Id | Vehicle Id | Borrower Id | Payment Status | Amount Paid | amount Pending |");
                System.out.println("+------------+------------+-------------+----------------+-------------+----------------+");
                do{
                    String paymentId = String.format("%02d", result.getInt(1));
                    String payment_id = String.format("|%-12s", " "+paymentId);
                    String vehicleId = String.format("%02d", result.getInt(2));
                    String vehicle_id = String.format("|%-12s", " "+vehicleId);
                    String borrowerId = String.format("%02d", result.getInt(3));
                    String borrower_id = String.format("|%-13s", " "+borrowerId);
                    String paymentStatus = String.format("|%-16s", " "+result.getString(4));
                    String PaidAmount = String.format("%-6s", result.getInt(5));
                    String Paid_Amount = String.format("|%-13s", " "+PaidAmount);
                    String PendingAmount = String.format("%-6s", result.getInt(6));
                    String Pending_Amount = String.format("|%-16s|", " "+PendingAmount);
                    System.out.println(payment_id+""+vehicle_id+""+borrower_id+""+paymentStatus+""+Paid_Amount+""+Pending_Amount);
                } while (result.next());
                System.out.println("+------------+------------+-------------+----------------+-------------+----------------+");
                System.out.println();
                return true;
            }
            
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    public static void clearScr(){

        System.out.print("\033[H\033[2J");
        System.out.flush();

    }

    private static void clearLine(int lineCount){
        for (int i = 0; i < lineCount; i++) {
            System.out.print(String.format("\033[%dA",1));
            System.out.print("\033[2K");
        }
    }

}