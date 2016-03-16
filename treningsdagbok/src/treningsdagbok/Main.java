package treningsdagbok;

import java.sql.Connection;
import java.sql.DriverManager;

public class Main {
	Connection myCon;
	TUI tui;
	DatabaseConnection db;
	
	public void init(){
		try {
			// Activate driver and create connection with database.
			Class.forName("com.mysql.jdbc.Driver");
			db = new DatabaseConnection();
			myCon = DriverManager.getConnection(db.getDatabase(), db.getUser(), db.getPw());
			tui = new TUI(myCon);
			
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void run() throws Exception{
		tui.run();
	}
	
	public static void main(String[] args) {
		Main m = new Main();
		m.init();
		/*
		try{
			m.run();
		} catch(Exception e){
			e.printStackTrace();
		}*/
	}

}
