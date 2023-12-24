import java.sql.Connection;
import java.sql.DriverManager;

public class DbConnection {

    private Connection connect = null;

    DbConnection(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/ExpressRide","root","123456789");
        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    public Connection getConnection(){
        return this.connect;
    }

}
