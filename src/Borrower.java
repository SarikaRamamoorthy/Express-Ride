import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class Borrower {
    /*variables used for decorations */
    public static final String ANSI_RESET = "\u001B[0m"; 
    public static final String ANSI_BOLD = "\u001b[5m";
    public static final String ANSI_LIGHTGREY = "\u001b[2m";
    public static final String ANSI_MAGENTA = "\u001b[36;1m";

    static Statement statement;
    static Scanner scanner = new Scanner(System.in);
    static String borrowerUserName;
    static String borrowerPassword;
    static int borrowerID;
    static int cautionDeposit = 30000;
    static int carId;
    static int bikeId;
    static int bikeRent;
    static int carRent;
    static int bikeSecurity;
    static int carSecurity;
    static int totalAmount;


    public static void borrowerScreen(){
        Main.clearScr();
        decor();
        signingOption();
    }
    
    public static void signingOption(){
        try {
            DbConnection c = new DbConnection();
            Connection connect = c.getConnection();
            statement = connect.createStatement();
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("    Select your choice..");
        System.out.println();
        System.out.println("    1. SignUp");
        System.out.println("    2. SignIn");
        System.out.println();
        int limit = 0;
        while(limit < 100){
            System.out.print("    Enter (1/2)  :  ");
            int option = scanner.nextInt();
            if(option == 1){
                bSignUp();
            }
            else if(option == 2){
                bSignIn();
            }
            else{
                Main.clearLine(1);
                limit++;
            }
        }
    }

    public static void bSignUp(){

        // TODO: Check signup Details
        int limitCounter = 0;
        while (limitCounter < 100) {
            
            Main.clearScr();
            System.out.println();
            scanner.nextLine();
            System.out.println();
            System.out.println("       SIGN UP");
            System.out.println();
            System.out.print("    Enter your Name (less than 100 characters)                : ");
            String bName = scanner.nextLine();
            System.out.println();
            System.out.print("    Enter your User Name (less than 50 characters)            : ");
            String bUserName = scanner.nextLine();
            System.out.println();
            try {
                ResultSet res = statement.executeQuery("select borrower_id from borrower_info where borrower_username = '"+bUserName+"'");
                if(res.next()){
                    System.out.println("    UserName Exists !!");
                    System.out.println();
                    System.out.print("    Press Enter to SignUp again..");
                    scanner.nextLine();
                    limitCounter++;
                    continue;
                }
                else{
                    System.out.print("    Enter the password (less than 20 characters)              : ");
                    String bpassword = scanner.nextLine();
                    System.out.println();
                    System.out.print("    Enter your Mobile number (Exactly 10 characters)          : ");
                    String bphoneNumber = scanner.nextLine();
                    System.out.println();
                    System.out.print("    Enter your Aadhar Number(Exactly 12 characters)           : ");
                    String bAadharNumber = scanner.nextLine();
                    System.out.println();
                    System.out.print("    Enter your Driving Licence Number(16 digit DL Number)     : ");
                    String bDrivingLicence = scanner.nextLine();
                    System.out.println();
                    System.out.print("    Enter your Residencial Address(less than 300 characters)  : ");
                    String bAddress = scanner.nextLine();
                    System.out.println();
                    String insertNewBorrower = "insert into borrower_info(Borrower_name, Borrower_userName, Borrower_password, Borrower_PhoneNumber, Borrower_AadharNumber, Borrower_DrivingLicence, Borrower_Address) values('"+bName+"','"+bUserName+"','"+bpassword+"','"+bphoneNumber+"','"+bAadharNumber+"','"+bDrivingLicence+"','"+bAddress+"')";
                    statement.execute(insertNewBorrower);
                    System.out.println("    Thanks! your account has been successfully created.");
                    System.out.println();
                    System.out.print("    Please SignIn again to continue(Press Enter)..");
                    scanner.nextLine();
                    borrowerScreen();
                }
            } 
            catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public static void bSignIn(){
        
        Main.clearScr();
        System.out.println();
        System.out.println("       SIGN IN");
        System.out.println();

        System.out.print("    User Name :  ");
        borrowerUserName = scanner.next();

        System.out.print("    Password  :  ");
        borrowerPassword = scanner.next();

        try {
            ResultSet signing = statement.executeQuery("select borrower_password,borrower_id from borrower_info where borrower_username = '"+borrowerUserName+"';");
            if(signing.next()){
                if(signing.getString(1).equals(borrowerPassword)){
                    borrowerID = signing.getInt(2);
                    validSignIn();
                }
                else{
                    invalidSignIn();
                }
            }
            else{
                invalidSignIn();
            }
        } catch (Exception e) {
            System.out.println(e);
        }


    }

    public static void invalidSignIn(){
        System.out.println();
        System.out.println("      Invalid SignIn :( ");
        System.out.println();
        System.out.print("   Press enter to SignIn : ");
        scanner.nextLine(); // for the remaining1aige return in the buffer
        scanner.nextLine(); // for the enter key
        bSignIn();  

    }

    public static void validSignIn(){
        Main.clearScr();
        System.out.println();
        System.out.println(ANSI_MAGENTA);
        System.out.println("           Welcome to the City of Super Cars and Bikes !!");
        System.out.println(ANSI_RESET);
        decor();
        System.out.println();
        list();

    }

    public static void decor(){

        System.out.println(ANSI_BOLD);

        System.out.println("    ____  _  _  ____  ____  ____  ___  ___    ____  ____  ____  ____ ");
        System.out.println("   ( ___)( \\/ )(  _ \\(  _ \\( ___)/ __)/ __)  (  _ \\(_  _)(  _ \\( ___)");
        System.out.println("    )__)  )  (  )___/ )   / )__) \\__ \\\\__ \\   )   / _)(_  )(_) ))__) ");
        System.out.println("   (____)(_/\\_)(__)  (_)\\_)(____)(___/(___/  (_)\\_)(____)(____/(____)");

        System.out.print(ANSI_RESET); 

    }



    public static void list(){

        int loopLimiter = 0;

        while (loopLimiter < 1000) {

            System.out.println();
            System.out.println("    Select your choice  ");
            System.out.println();

            System.out.println("    1. Vehicles list");
            System.out.println("    2. Select a vehicle");
            System.out.println("    3. Profile");
            System.out.println("    4. View Cart");
            System.out.println("    5. Sign Out");
            System.out.println();

            System.out.print("    Enter (1/2/3/4/5)  : ");
            int option = scanner.nextInt();
            System.out.println();

            if(option == 1){
                vehicleList();
            }
            else if(option == 2){
                selectVehicle();
            }
            else if(option == 3){
                
            }
            else if(option == 4){
                viewCart();
            }
            else if(option == 5){
                borrowerScreen();
            }
            else{
                System.out.println("    Invalid choice !!");
                System.out.println();
                System.out.print("    Press Enter for choice list");
                scanner.nextLine();
                scanner.nextLine();
                Main.clearLine(13);
                loopLimiter++;
                continue;
            }

        }

    }

    public static void viewCart() {
        int limit = 0;
        while(limit < 200){

            Main.clearScr();            
            cartDisplay();

            System.out.println();
            System.out.println("      CART");
            System.out.println();

            System.out.println("    1. Remove");
            System.out.println("    2. Book");
            System.out.println("    3. Exit");
            System.out.println();
            System.out.print("    Enter(1/2/3) : ");
            int cartChoice = scanner.nextInt();
            System.out.println();
            if(cartChoice == 1){
                Main.clearScr();
                cartChoiceOne();
            }
            else if(cartChoice == 2){
                Main.clearScr();
                try {
                    ArrayList<Integer> vehicleIdList = new ArrayList<>();
                    ArrayList<Integer> typeIdList = new ArrayList<>();
                    ResultSet Deposit = statement.executeQuery("select borrower_deposit from borrower_info where borrower_id = "+borrowerID+";");
                    if(Deposit.next()) {
                        int borrowerDeposit = Deposit.getInt(1);
                        cautionDeposit = cautionDeposit - borrowerDeposit;
                    }
                    ResultSet vehicleId  = statement.executeQuery("select vehicle_id,type_id from borrower_cart where borrower_id = "+borrowerID+";");
                    while(vehicleId.next()){
                        vehicleIdList.add(vehicleId.getInt(1));
                        typeIdList.add(vehicleId.getInt(2));
                    }
                    for(int i=0;i<vehicleIdList.size();i++){
                        ResultSet rent  = statement.executeQuery("select rent from vehicles_info where vehicle_id = "+vehicleIdList.get(i)+";");
                        if(rent.next()){
                            if(typeIdList.get(i) == 1){
                                carId = vehicleIdList.get(i);
                                carRent = rent.getInt(1);
                            }
                            else {
                                bikeId = vehicleIdList.get(i);
                                bikeRent = rent.getInt(1);       
                            }
                        }
                        ResultSet type  = statement.executeQuery("select security_deposit from type_info where type_id = "+typeIdList.get(i)+";");
                        if(type.next()){
                            if(typeIdList.get(i) == 1)
                                carSecurity = type.getInt(1);
                            else
                                bikeSecurity = type.getInt(1);
                        }
                    }
                    totalAmount = cautionDeposit+bikeRent+bikeSecurity+carRent+carSecurity;
                    System.out.println("    Caution Deposit to be paid        :   "+cautionDeposit);
                    if(bikeRent > 0){
                        System.out.println();
                        System.out.println("    Bike Rent to be paid              :   "+bikeRent);
                        System.out.println();
                        System.out.println("    Bike Security Deposit to be paid  :   "+bikeSecurity);
                    }
                    if(carRent > 0){
                        System.out.println();
                        System.out.println("    Car Rent to be paid               :   "+carRent);
                        System.out.println();
                        System.out.println("    Car Security Deposit to be paid   :   "+carSecurity);
                    }
                    System.out.println("                                          --------------");
                    System.out.println("    Total Amount to be paid           :   "+totalAmount);
                    System.out.println();
                    int limitcount = 0;
                    while (limitcount < 200) {
                        
                        System.out.println();
                        System.out.print("    Would you like to conform booking ? (Y/N): ");
                        String conform = scanner.next().toLowerCase();
                        System.out.println();
                        if (conform.length() == 1) {
                            if(conform.charAt(0) == 'y'){
                                    Payment.addRecord(borrowerID,cautionDeposit,-1);
                                if(carRent > 0)
                                    Payment.addRecord(borrowerID,carRent+carSecurity,carId);
                                if(bikeRent > 0)
                                    Payment.addRecord(borrowerID,bikeRent+bikeSecurity,bikeId);
                                System.out.print("    Your Payment is being processed. Please wait..");
                                scanner.nextLine();
                                scanner.nextLine();
                            }
                            else if(conform.charAt(0) == 'n'){
                                System.out.println();
                                System.out.print("    Booking Cancelled ! Press Enter");
                                scanner.nextLine();
                                scanner.nextLine();
                                break;
                            }
                        } else {
                            System.out.print("    Invalid ! Press Enter..");
                            scanner.nextLine();
                            scanner.nextLine();
                            Main.clearLine(4);
                            limitcount++;
                        }
                    }
                    
                    
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            else if(cartChoice == 3){
                Main.clearScr();
                list();
            }
            else{
                System.out.print("    Invalid Choice! Press Enter..");
                scanner.nextLine();
                scanner.nextLine();
                limit++;
            }

        }
    }

    public static void cartChoiceOne(){
        System.out.println();
        System.out.print("    Enter the Vehicle ID to be removed : ");
        int removeVehicleId = scanner.nextInt();
        System.out.println();
        try {
            ResultSet isValid = statement.executeQuery("select * from borrower_cart where borrower_id = "+borrowerID+" and vehicle_id = "+removeVehicleId+";");
            if (isValid.next()) {
                statement.execute("delete from borrower_cart where vehicle_id = "+removeVehicleId);
                System.out.println();
                cartDisplay();
                System.out.println();
                System.out.print("    Vehicle removed from the cart. Press Enter..");
                scanner.nextLine();
                scanner.nextLine();
            }
            else{
                System.out.println();
                cartDisplay();
                System.out.println();
                System.out.print("    Invalid Vehicle ID! Press Enter..");
                scanner.nextLine();
                scanner.nextLine();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void cartDisplay(){
        try {
            ResultSet cartItem = statement.executeQuery("select b.vehicle_id,v.vehicle_name,v.number_plate,t.type_name,v.Rent,t.Security_deposit from borrower_cart b inner join vehicles_info v on b.vehicle_id = v.vehicle_id inner join type_info t on b.type_id = t.type_id;");
            if(!cartItem.next()){
                System.out.println("    Cart Empty!");
            }
            else{
                System.out.println("+------------+--------------------+--------------+------+------+------------------+");
                System.out.println("| vehicle_id | vehicle_name       | number_plate | type | Rent | Security_deposit |");
                System.out.println("+------------+--------------------+--------------+------+------+------------------+");
                do {
                    String id = String.format("%02d", cartItem.getInt(1));
                    String vehicle_id = String.format("|%-12s|", " "+id);
                    String vehicle_name = String.format("%-20s|"," "+cartItem.getString(2));
                    String Number_plate = String.format("%-14s|"," "+cartItem.getString(3));
                    String type_name = String.format("%-6s|"," "+cartItem.getString(4));
                    String rent = String.format("%-6s|"," "+cartItem.getString(5));
                    String security_deposit = String.format("%-18s|"," "+cartItem.getString(6));
                    System.out.println(vehicle_id+""+vehicle_name+""+Number_plate+""+type_name+""+rent+""+security_deposit);
                } while (cartItem.next());
                System.out.println("+------------+--------------------+--------------+------+------+------------------+");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void selectVehicle(){

        boolean carRented = false,bikeRented = false;
        int limitCounter = 0;

        while (limitCounter < 500) {
            
            Main.clearScr();
            
            System.out.println();
            System.out.println("    You can choose upto a car and a bike");
            System.out.println();

            System.out.println("    Choose what would you like to rent");
            System.out.println();

            System.out.println("    1. Car");
            System.out.println("    2. Bike");
            System.out.println("    3. Exit");
            System.out.println();
            System.out.print("    Enter (1/2/3) : ");
            int choice = scanner.nextInt();
            try {
                ResultSet cartItems = statement.executeQuery("Select type_id from borrower_cart where borrower_id = "+borrowerID);
                while(cartItems.next()){
                    if(cartItems.getInt(1) == 1){
                        carRented = true;
                    }
                    else{
                        bikeRented = true;
                    }
                }
                
            } catch (Exception e) {
                // System.out.println(e);
            }

            if(choice == 1){
                
                System.out.println();
                System.out.print("    Enter the vehicle ID : ");
                int vehicleId = scanner.nextInt();
                System.out.println();

                if(carRented){
                        
                    System.out.print("    You already had a car in your cart.Press Enter.. ");
                    scanner.nextLine();
                    scanner.nextLine();

                }
                else{

                    try {
                        ResultSet isPresent = statement.executeQuery("select * from vehicles_info where vehicle_id = "+vehicleId+" and ((isDeleted = 'N' and type_id = 1) and (serviced = 'Yes' and borrower_id is null));");
                        if(isPresent.next()){
                            carRented = true;
                            statement.execute("insert into borrower_cart values('"+borrowerID+"','"+vehicleId+"',null,1);");
                            System.out.print("    Vehicle successfully moved to your Cart(Press Enter)..");
                            scanner.nextLine();
                            scanner.nextLine();
                        }
                        else{
                            System.out.print("    Vehicle ID not found !! Press Enter..");
                            scanner.nextLine();
                            scanner.nextLine();
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                    }

                }
            }
            else if(choice == 2){

                System.out.println();
                System.out.print("    Enter the vehicle ID : ");
                int vehicleId = scanner.nextInt();
                System.out.println();

                if(bikeRented){

                    System.out.print("    You already had a bike in your cart.Press Enter.. ");
                    scanner.nextLine();
                    scanner.nextLine();

                }
                else{

                    try {
                        ResultSet isPresent = statement.executeQuery("select * from vehicles_info where vehicle_id = "+vehicleId+" and ((isDeleted = 'N' and type_id = 2) and (serviced = 'Yes' and borrower_id is null));");
                        if(isPresent.next()){
                            bikeRented = true;
                            statement.execute("insert into borrower_cart values('"+borrowerID+"','"+vehicleId+"',null,2);");
                            System.out.print("    Vehicle successfully moved to your Cart(Press Enter)..");
                            scanner.nextLine();
                            scanner.nextLine();
                        }
                        else{
                            System.out.print("    Vehicle ID not found !! Press Enter..");
                            scanner.nextLine();
                            scanner.nextLine();
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                    }

                }

            }
            else if(choice == 3){
                Main.clearScr();
                list();
                
            }
            else{
                // for other options
                System.out.println();
                System.out.print("    Invalid Choice! Press Enter..");
                scanner.nextLine();
                scanner.nextLine();
                limitCounter++;
            }

        }


    }



    public static void vehicleList(){

        Main.clearScr();

        try {

            ResultSet set = statement.executeQuery("select v.vehicle_id,v.vehicle_name,v.Number_plate,t.type_name,t.security_deposit,v.rent from vehicles_info v inner join type_info t on v.type_id = t.type_id where v.isdeleted = 'N' and (v.rented_date is null and serviced = 'Yes') order by v.vehicle_id;");

            System.out.println("+------------+------------------------+--------------+-----------+------------------+------+");

            System.out.println("| vehicle_id |     vehicle_name       | Number_plate | type_name | security_deposit | rent |");

            System.out.println("+------------+------------------------+--------------+-----------+------------------+------+");

            while(set.next()){

                String id = String.format("%02d", set.getInt(1));
                String vehicle_id = String.format("|%-12s|", " "+id);
                String vehicle_name = String.format("%-24s|"," "+set.getString(2));
                String Number_plate = String.format("%-14s|"," "+set.getString(3));
                String type_name = String.format("%-11s|"," "+set.getString(4));
                String security_deposit = String.format("%-18s|"," "+set.getString(5));
                String rent = String.format("%-6s|"," "+set.getString(6));


                System.out.println(vehicle_id+""+vehicle_name+""+Number_plate+""+type_name+""+security_deposit+""+rent);

            }

            System.out.println("+------------+------------------------+--------------+-----------+------------------+------+");
        } 
        
        catch (Exception e) {
            System.out.println(e);
        }

    }

}
/*column name
 *  Borrower_id
 *  Borrower_name
 *  Borrower_userName
 *  Borrower_password
 *  Borrower_PhoneNumber
 *  Borrower_AadharNumber
 *  Borrower_DrivingLicence
 *  Borrower_Address
 */
