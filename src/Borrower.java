import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Borrower {
    static Statement statement;
    static Scanner scanner = new Scanner(System.in);
    public static void borrowerScreen(){
        Main.clearScr();
        signingOption();
    }
    
    public static void signingOption(){
        System.out.println("    Select your choice..");
        System.out.println("    1. SignUp");
        System.out.println("    2. SignIn");
        System.out.print("    Enter (1/2)  :  ");
        int option = scanner.nextInt();
        if(option == 1){
            Main.clearScr();
            bSignUp();
        }
        else{
            Main.clearScr();
            bSignIn();
        }
    }
    public static void bSignUp(){
        System.out.print("    Enter your Name (less than 100 characters)     : ");
        String bName = scanner.nextLine();
        System.out.println();
        System.out.print("    Enter your User Name (less than 50 characters) : ");
        String bUserName = scanner.nextLine();
        System.out.println();
        try {
            DbConnection c = new DbConnection();
            Connection connect = c.getConnection();
            statement = connect.createStatement();
            ResultSet res = statement.executeQuery("select borrower_id from borrower_info where borrower_username = '"+bUserName+"'");
            if(res.next()){
                System.out.println("    UserName Exists !!");
                bSignUp();
            }
            else{
                System.out.print("    Enter the password (less than 20 characters) : ");
                String password = scanner.nextLine();
                System.out.println();
                System.out.print("    Enter your Mobile number                      : ");
                String phoneNumber = scanner.nextLine();
                System.out.println();
            }
        } 
        catch (Exception e) {
            System.out.println(e);
        }
    }
    public static void bSignIn(){

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
