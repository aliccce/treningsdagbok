package treningsdagbok;

import java.sql.Connection;
import java.sql.DriverManager;
import treningsdagbok.Treningsøkt;
import treningsdagbok.TUI;

public class Main {
	Connection myCon;
	Treningsøkt tr;
	TUI tui;
	
	public void init(){
		try {
			// Activate driver and create connection with database.
			Class.forName("com.mysql.jdbc.Driver");
			myCon = DriverManager.getConnection("", "", "");
			tr = new Treningsøkt(myCon);
			tui = new TUI(myCon);
			
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
		Main m = new Main();
		m.init();
		try{
			m.run();
		} catch(Exception e){
			e.printStackTrace();
		}
	}

}
