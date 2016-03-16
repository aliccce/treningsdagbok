package treningsdagbok;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
	
	private Connection myCon;
	
	Database(Connection myCon) {
		this.myCon = myCon;
	}
	
	public ResultSet hent_øvingstabell() {
		try {
			Statement s = myCon.createStatement();
			ResultSet rs = s.executeQuery("Select * from Øvelse");
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ResultSet hent_gruppetabell() {
		try {
			Statement s = myCon.createStatement();
			ResultSet rs = s.executeQuery("Select * from Gruppe");
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
