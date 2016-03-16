package treningsdagbok;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;


public class BesteResultat extends Sporring{

	public BesteResultat(Connection myCon) {
		super(myCon);
	}
	
	public void run(){
		
		try {
			
			Statement s = super.myCon.createStatement();
			
			Scanner scanner = new Scanner(System.in);
			 
			System.out.print("Skriv inn type (Tid eller Distanse): ");
			String type = scanner.nextLine();
			while (! (type.equals("Tid") || type.equals("Distanse"))) {
				System.out.print("Feil, du må skrive enten Tid eller Distanse. Prøv igjen: ");
				type = scanner.nextLine();
			}
			
			
			ArrayList<Integer> øvelser = printØvelser(type);
			øvelser.toString();
			
			System.out.print("Skriv inn øvingsID: ");
			String input = scanner.nextLine();
			int øvingsid = Integer.parseInt(input);
			while (!listContains(øvelser, øvingsid))
			{
				System.out.print("Øvingsid-en må være i listen. Prøv igjen: ");
				input = scanner.nextLine();
				øvingsid = Integer.parseInt(input);
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
	
	private ArrayList<Integer> printØvelser(String type){
		try{
			Statement stmt = myCon.createStatement();
			ResultSet rs = stmt.executeQuery("select øvingsID as id, øvingsnavn as navn "
					+ "from Øvelse natural join Resultat where Resultat.typ = \"" + type + "\";");
			ArrayList<Integer> øvelser = new ArrayList<Integer>();
			while(rs.next()){
				int id = rs.getInt("id");
				String navn = rs.getString("navn");
				if (!listContains(øvelser, id)){
					System.out.println(id + " ... " + navn);
					øvelser.add(id);
				}
			}
			return øvelser;
		} catch(Exception e){
			e.printStackTrace();
		} 
		return null;
		
	}
	
	private boolean listContains(ArrayList<Integer> list, int num){
		return list.stream().anyMatch(n -> n.equals(num));
	}

}
