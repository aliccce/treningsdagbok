package treningsdagbok;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

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
	
	public void run(){
		Scanner scanner = new Scanner(System.in);
		tui.run();
		System.out.println("Vil du gjøre noe annet? ja/nei");
		String svar = scanner.nextLine();
		if (svar.toLowerCase().equals("ja")){
			this.run();
		} else{
			scanner.close();
			System.out.println("Programmet avsluttes.");
		}
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
