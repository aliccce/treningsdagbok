package Unused;

import java.sql.Connection;
import java.sql.DriverManager;
import treningsdagbok.Treningsokt;
import treningsdagbok.DatabaseConnection;
import treningsdagbok.TUI;

public class MainTest {
	Connection myCon;
	Treningsokt tr;
	TUI tui;
	
	public void init(){
		try {
			// Activate driver and create connection with database.
			Class.forName("com.mysql.jdbc.Driver");
			DatabaseConnection db = new DatabaseConnection();
			myCon = DriverManager.getConnection(db.getDatabase(), db.getUser(), db.getPw());
			tr = new Treningsokt(myCon);
			
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void run() throws Exception{
		
		tr.setGeneral("2016-03-20", "17:00:00", 2, 8, 8);
		tr.setUtendørs(20, "ganske dårlig");
		tr.setMal("favoritt");
		tr.addØvelse(10002);
		tr.addØvelse(10003);
		tr.addØvelse(10004);
		tr.addGruppe(20000);
		tr.addNotat("Form", "Formen var velidg bra idag blabla");
		tr.addResultat("Tid", 100, 10003);
		tr.addResultat("Distanse", 1600, 10004);
		
		try{
			//tr.fillDatabase();
			tr.printØvelser();
		} catch(Exception e){
			e.printStackTrace();
			//tr.regret();
		}
		
		
		//tui.run();
		
	}
	
	public static void main(String[] args) {
		MainTest m = new MainTest();
		m.init();
		try{
			m.run();
		} catch(Exception e){
			e.printStackTrace();
		}
	}

}
