package treningsdagbok;
import java.sql.*;

public class Test {
	public static void main(String[] args) {
		try{
			Class.forName("com.mysql.jdbc.Driver");
			
			Connection myCon = DriverManager.getConnection("jdbc:mysql://mysql.stud.ntnu.no/aliceg_dbproj", "aliceg_db", "kulugle");
			
			Statement myStatement = myCon.createStatement();
			
			ResultSet rs = myStatement.executeQuery("select * from Treningsøkt natural join Utendørs");
			
			while(rs.next()){
				System.out.println(rs.getString("TreningsID") + " " + rs.getString("Dato") + " " + rs.getString("Tidspunkt"));
			}
			
			
		} catch (Exception e){
			System.out.println(e);
		}
	}
}
