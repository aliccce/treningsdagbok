package treningsdagbok;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


public class AlleTreningsøkter extends Sporring {

	public AlleTreningsøkter(Connection myCon) {
		super(myCon);
	}

	@Override
	public void run() {
		try {
			Statement s = super.myCon.createStatement();
			ResultSet rs = s.executeQuery("select * from Treningsøkt natural join Innendørs");
			System.out.println("TRENINGER GJORT INNENDØRS: \n");
			System.out.format("%-4s%-12s%-12s%-12s%-12s%-12s%-12s%-12s", "ID", "Dato", "Tid", "Varighet", "Form", "Prestasjon", "Ventilasjon", "Tilskuere");
			System.out.println();
			System.out.println("----------------------------------------------------------------------------------------");
			while (rs.next()) {
				System.out.format("%-4s%-12s%-12s%-12s%-12s%-12s%-12s%-12s", Integer.toString(rs.getInt("TreningsID")), rs.getString("Dato"), rs.getString("Tidspunkt"), Integer.toString(rs.getInt("Varighet")), Integer.toString(rs.getInt("PersonligForm")), Integer.toString(rs.getInt("Prestasjon")), rs.getString("Ventilasjon"),  Integer.toString(rs.getInt("AntallTilskuere")));
				System.out.print("\n");
			}
			System.out.println("\n");
			rs = s.executeQuery("select TreningsID, Dato, Tidspunkt, Varighet, PersonligForm, Prestasjon, Temperatur, Værtype as vt from Treningsøkt natural join Utendørs");
			System.out.println("TRENINGER GJORT UTENDØRS: \n");
			System.out.format("%-4s%-12s%-12s%-12s%-12s%-12s%-12s%-12s", "ID", "Dato", "Tid", "Varighet", "Form", "Prestasjon", "Temperatur", "Værtype");
			System.out.println();
			System.out.println("----------------------------------------------------------------------------------------");
			while (rs.next()) {
				System.out.format("%-4s%-12s%-12s%-12s%-12s%-12s%-12s%-12s", Integer.toString(rs.getInt("TreningsID")), rs.getString("Dato"), rs.getString("Tidspunkt"), Integer.toString(rs.getInt("Varighet")), Integer.toString(rs.getInt("PersonligForm")), Integer.toString(rs.getInt("Prestasjon")), Integer.toString(rs.getInt("Temperatur")),  rs.getString("vt"));
				System.out.print("\n");
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
