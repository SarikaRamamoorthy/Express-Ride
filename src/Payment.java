import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Payment {
    public static void addRecord(int borrowerId,int totalAmount,ArrayList<Integer> vehicleIdList){
        DbConnection connnect = new DbConnection();
        Connection con = connnect.getConnection();
        try{
            Statement statement = con.createStatement();
            statement.execute("insert into borrower_paymentdetails(borrower_id,payment_status,amount_pending) values ("+borrowerId+",'Processing',"+totalAmount+");");
            ResultSet update = statement.executeQuery("select payment_id from borrower_paymentdetails where borrower_id = "+borrowerId+";");
            int payment_id = -1;
            if(update.next()){
                payment_id = update.getInt(1);
            }
            statement.execute("update borrower_cart set payment_Id = "+payment_id+";");
            for(int i=0;i<vehicleIdList.size();i++){
                statement.execute("insert into rented_vehicles(vehicle_id,borrower_id) values("+vehicleIdList.get(i)+","+borrowerId+";)");
                statement.execute("update vehicles_info set borrower_id = "+borrowerId+" where vehicles_id = "+vehicleIdList.get(i));
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
}
