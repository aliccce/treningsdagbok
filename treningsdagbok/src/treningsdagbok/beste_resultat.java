package treningsdagbok;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;


public class beste_resultat extends Spørring{

	public beste_resultat(Connection myCon) {
		super(myCon);
	}
	
	public void run(){
		
		try {
			
			Statement s = super.myCon.createStatement();
			
			Scanner scanner = new Scanner(System.in);
			 
			System.out.print("Skriv inn type (Tid eller Distanse): ");
			String type = scanner.nextLine();
			while (! (type.equals("tid") || type.equals("distanse"))) {
				System.out.print("Feil, du må skrive enten Tid eller Distanse. Prøv igjen: ");
				type = scanner.nextLine();
			}
			
			System.out.print("Skriv inn øvingsID: ");
			String input = scanner.nextLine();
			int øvingsid = Integer.parseInt(input);
			while (! (øvingsid >= 10000 && øvingsid<20000))
			{
				System.out.print("Du må skrive inn en øvingsID mellom 10000 og 19999. Prøv igjen: ");
				input = scanner.nextLine();
				øvingsid=Integer.parseInt(input);
			}
			
			scanner.close();
			
			ResultSet rs = s.executeQuery("select if(Mål.opptil = true, max(Resultat.verdi), min(Resultat.verdi)) as beste_resultat "
					+ "from Resultat natural join Øvelse inner join Mål on (Øvelse.øvingsID = Mål.øvingsID) "
					+ "where Resultat.typ = '" + type + "' and Øvelse.øvingsID = '" + øvingsid + "' and Resultat.typ = Mål.typ;");
			
			while(rs.next()) {
				System.out.println("Gratulerer! Ditt beste resultat er: " + Double.toString(rs.getDouble("beste_resultat")));
			}
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
