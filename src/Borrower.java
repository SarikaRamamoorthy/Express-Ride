import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
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
            ResultSet signing = statement.executeQuery("select borrower_password from borrower_info where borrower_username = '"+borrowerUserName+"';");
            if(signing.next()){
                if(signing.getString(1).equals(borrowerPassword)){
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
            System.out.println("    4. Cart");
            System.out.println("    5. Sign Out");
            System.out.println();

            System.out.print("    Enter (1/2/3)  : ");
            int option = scanner.nextInt();
            System.out.println();

            if(option == 1){
                vehicleList();
            }
            else if(option == 2){
                
            }
            else if(option == 3){
    
            }
            else if(option == 4){
    
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

    public static void selectVehicle(){

        boolean carRented = false,bikeRented = false;
        int limitCounter = 0;

        while (limitCounter < 500) {

            System.out.println("    You can choose upto a car and a bike");
            System.out.println();

            System.out.println("    Choose what would you like rent");
            System.out.println();

            System.out.println("    1. Car");
            System.out.println("    2. Bike");
            System.out.println();
            System.out.print("    Enter (1/2) : ");
            int choice = scanner.nextInt();

            if(choice == 1){

                if(carRented){

                    System.out.print("    You already had a car in your cart. Would you like to replace it ? (Yes/No) : ");

                    String replace = scanner.nextLine();

                    if(replace.equalsIgnoreCase("yes")){
                        // replace car in cart
                    }
                    else{

                    }

                }
                else{

                    carRented = true;
                    // insert into cart

                }
            }
            else if(choice == 2){

                if(bikeRented){

                    System.out.print("    You already had a bike in your cart. Would you like to replace it ? (Yes/No) : ");

                    String replace = scanner.nextLine();

                    if(replace.equalsIgnoreCase("yes")){
                        // replace bike in cart
                    }
                    else{
    
                    }

                }
                else{

                    bikeRented = true;
                    // insert into cart

                }

            }
            else{
                // for other options
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
