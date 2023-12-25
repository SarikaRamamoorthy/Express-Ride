import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
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
        System.out.println("    5. Search by name");
        System.out.println("    6. Search by Number plate");
        System.out.println("    7. Sign Out");
        System.out.println();
        System.out.print("    Enter your choice(1/2/3/4/5/6/7) :  ");

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
            System.out.println("    1. Vehicle Name");
            System.out.println("    2. Number Plate");
            System.out.println("    3. Vehicle Type");
            System.out.println("    4. Distance Travelled");
            System.out.println("    5. Serviced / Not Serviced");
            System.out.println("    6. Borrower ID");
            System.out.println("    7. Rented Date");
            System.out.println("    8. Return Date");
            System.out.println("    9. Security Deposit");
            System.out.println();
            System.out.print("    Choose any One(1 to 9) : ");
            int property = sc.nextInt();
            clearScr();
            if(property >= 1 && property <= 9){
                System.out.println();
                System.out.print("    Enter the Vehicle ID to be modified : ");
                int id = sc.nextInt();
                System.out.println();
                try {
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
                        System.out.print("    Enter whether the vehicle is serviced or not : ");
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
                        statement.executeUpdate("update vehicles_info set total_distance = "+updatedBorrower+" where vehicle_id = "+id);
                        System.out.println();
                        System.out.println("    ✓ Borrower ID Modified ✓ ");
                        System.out.println();
                        vehiclesList(sqlVehiclesList);
                    }
                    else if(property == 7){
                        sc.nextLine();
                        System.out.print("    Enter whether the vehicle is serviced or not : ");
                        String updatedService = sc.nextLine();
                        statement.executeUpdate("update vehicles_info set serviced = '"+updatedService+"' where vehicle_id = "+id);
                        System.out.println();
                        System.out.println("    ✓ Service Status Modified ✓ ");
                        System.out.println();
                        vehiclesList(sqlVehiclesList);
                    }
                    else if(property == 8){
                        sc.nextLine();
                        System.out.print("    Enter the Date of rent of the vehicle : ");
                        String updatedRentDate = sc.nextLine();
                        statement.executeUpdate("update vehicles_info set rented_date = '"+updatedRentDate+"' where vehicle_id = "+id);
                        System.out.println();
                        System.out.println("    ✓ Rent Date Modified ✓ ");
                        System.out.println();
                        vehiclesList(sqlVehiclesList);
                    }
                    else if(property == 9){
                        sc.nextLine();
                        System.out.print("    Enter the Date of return of the vehicle : ");
                        String updatedReturnDate = sc.nextLine();
                        statement.executeUpdate("update vehicles_info set return_date = '"+updatedReturnDate+"' where vehicle_id = "+id);
                        System.out.println();
                        System.out.println("    ✓ Return Date Modified ✓ ");
                        System.out.println();
                        vehiclesList(sqlVehiclesList);
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
            System.out.print("    Enter the Name of the Vehicle to search : ");
            sc.nextLine();//empty
            String vehicleName = sc.nextLine();
            System.out.println();
            String sqlSearchByName = "select v.vehicle_id,v.vehicle_name,v.Number_plate,t.type_name,t.security_deposit,v.rent,v.total_distance,v.serviced,v.borrower_id,v.rented_date,v.return_date from vehicles_info v inner join type_info t on v.type_id = t.type_id where vehicle_name = '"+vehicleName+"' and isdeleted = 'N' order by v.vehicle_id";
            vehiclesList(sqlSearchByName);
        }
        else if(choice == 6){
            clearScr();
            System.out.println();
            System.out.print("    Enter the Number Plate of the Vehicle to search : ");
            sc.nextLine();//empty
            String vehicleNumberPlate = sc.nextLine();
            System.out.println(); 
            String sqlSearchByNumberPlate = "select v.vehicle_id,v.vehicle_name,v.Number_plate,t.type_name,t.security_deposit,v.rent,v.total_distance,v.serviced,v.borrower_id,v.rented_date,v.return_date from vehicles_info v inner join type_info t on v.type_id = t.type_id where Number_plate = '"+vehicleNumberPlate+"' and isdeleted = 'N'";
            vehiclesList(sqlSearchByNumberPlate);
        }
        else if(choice == 7){
            System.out.println("    Signing out...");
            System.out.println();
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
                System.out.println("| vehicle_id |    vehicle_name        | Number_plate | type_name | security_deposit | rent | total_distance | serviced | borrower_id | rented_date | return_date |");
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