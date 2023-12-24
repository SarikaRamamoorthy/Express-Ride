import java.util.Scanner;

public class Main {

    /*variables used for decorations */
    public static final String ANSI_RESET = "\u001B[0m"; 
    public static final String ANSI_BOLD = "\u001b[5m";
    public static final String ANSI_LIGHTGREY = "\u001b[2m";


    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);
        clearScr();
        decor();
        selectChoice();

        int choice = scanner.nextInt();
        System.out.println();
        
        if(choice == 1){
            Admin.adminScreen();
        }
        else if(choice == 2){
            // todo
        }
        else if(choice != 1 && choice != 2){
            // todo
        }

        scanner.close();


    }

    
    public static void decor(){

        System.out.println(ANSI_BOLD);
        System.out.println("       EXPRESS RIDE");
        System.out.print(ANSI_RESET);
        System.out.print(ANSI_LIGHTGREY);
        System.out.println("            - Smiles Included :D");
        System.out.println(ANSI_RESET);

    }

    public static void selectChoice(){

        System.out.println("    Select your choice..");
        System.out.println();
        System.out.println("    1. Admin");
        System.out.println("    2. Renter");
        System.out.println();
        System.out.print("    Enter (1/2)  :  ");

    }

    public static void clearScr(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}






// public static final String ANSI_RESET = "\u001B[0m"; 
// public static final String ANSI_YELLOW = "\u001B[33m"; 
// System.out.println(ANSI_YELLOW + "This text is yellow"+ ANSI_RESET);