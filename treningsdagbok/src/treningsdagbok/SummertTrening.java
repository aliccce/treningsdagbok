package treningsdagbok;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;


public class SummertTrening extends Sporring{
	public SummertTrening(Connection myCon) {
		super(myCon);
	}
	
	public void run(){
		
		try {
			
			Statement s = myCon.createStatement();
			
			Scanner scanner = new Scanner(System.in);
			 
			System.out.println("Skriv inn startdatoen på treningsperioden du vil ha summert oversiktfra [åååå-mm-dd]: ");
			
			String summert = scanner.nextLine();
			while (! summert.matches("\\d{4}-\\d{2}-\\d{2}")) {
				System.out.print("Feil datoformat. Prøv igjen. Format: åååå-mm-dd\n>");
				summert = scanner.nextLine();
			}
			
			
			ResultSet rs = s.executeQuery("select count(distinct Treningsøkt.TreningsID) as ant_okt, "
					+ "count(distinct TreningsøktØvelse.ØvingsID) as ant_ovelser, "
					+ "SUM(Varighet) as total_varighet "
					+ "from Treningsøkt natural join TreningsøktØvelse natural join Øvelse "
					+ "where datediff('"+summert+"', Treningsøkt.Dato) < 0;");
			
			while(rs.next()) {
				System.out.println("Antall treningsøkter innenfor perioden:  " + rs.getInt("ant_okt")+ "\n" + 
							"Antall øvelser du har utført innenfor den gitte perioden: " + rs.getInt("ant_ovelser") + "\n" +
							"Antall minutter du har trent innen for den gitte perioden: "+ rs.getInt("total_varighet"));
		
			}
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public static void main(String[] args) {
		
	}
}
