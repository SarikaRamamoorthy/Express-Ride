import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Payment {
    static int paymentId;
    static int typeId;
    public static void addRecord(int borrowerId,int totalAmount,int vehicleId){
        DbConnection connnect = new DbConnection();
        Connection con = connnect.getConnection();
        try{
            Statement statement = con.createStatement();
            statement.execute("insert into borrower_paymentdetails(vehicle_id,borrower_id,amount_pending) values ("+vehicleId+","+borrowerId+","+totalAmount+");");
            if(vehicleId != -1){

                statement.execute("insert into rented_vehicles (vehicle_id,borrower_id) values("+vehicleId+","+borrowerId+");");

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String today = sdf.format(new Date());
                String tommorrow = sdf.format(new Date(System.currentTimeMillis() + 86400000));

                String query = "update vehicles_info set borrower_id = "+borrowerId+" , Rented_date = '"+today+"', Return_date = '"+tommorrow+"' where vehicle_id = "+vehicleId+";";

                statement.execute(query);

                ResultSet payment = statement.executeQuery("select payment_id from borrower_paymentdetails where vehicle_id = "+vehicleId);
                
                if(payment.next()){
                    paymentId = payment.getInt(1);
                }
                ResultSet type = statement.executeQuery("select type_id from vehicles_info where vehicle_id = "+vehicleId);
                if(type.next()){
                    typeId = type.getInt(1);
                }
                statement.execute("update borrower_cart set payment_id = "+paymentId+";");
            }
            
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
}
