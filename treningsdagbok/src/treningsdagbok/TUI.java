package treningsdagbok;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.sql.*;

public class TUI {
	
	private Scanner s;
	private Treningsokt t;
	private Database db;
	private Connection myCon;
	
	TUI(Connection myCon) {
		s = new Scanner(System.in);
		t = new Treningsokt(myCon);
		db = new Database(myCon);
		this.myCon = myCon;

	}
	
	public void run() {
		System.out.print("Velkommen til din treningsdagbok! \nHva vil du gjøre?\n1: Legge inn ny treningsøkt\n2: Se alle tidligere treningsøkter\n3: Få oversikt over treninger den siste perioden\n4: Finn beste resultat på en øvelse\n> ");
		velg_aksjon();
	}
	
	public void velg_aksjon() {
		String input = s.nextLine();
		if (input.equals("1")) {
			ny_treningsøkt();
		} else if (input.equals("2")) {
			alle_treningsøkter();
		} else if (input.equals("3")) {
			periode_oversikt();
		} else if (input.equals("4")) {
			beste_resultat();
		} else {
			System.out.print("Du må velge 1, 2, 3 eller 4. Prøv igjen.\n> ");
			velg_aksjon();
		}
	}
	
	public void ny_treningsøkt() {
		// DATO
		System.out.print("Dato [åååå-mm-dd]: ");
		String dato = s.nextLine();
		while (! dato.matches("\\d{4}-\\d{2}-\\d{2}")) {
			System.out.print("Feil datoformat. Prøv igjen. Format: åååå-mm-dd\n>");
			dato = s.nextLine();
		}
		// TIDSPUNKT
		System.out.print("Tidspunkt [tt:mm:ss]: ");
		String tidspunkt = s.nextLine();
		while (! tidspunkt.matches("\\d{2}:\\d{2}:\\d{2}")) {
			System.out.print("Feil tidsformat. Prøv igjen. Format: tt:mm:ss\n> ");
			tidspunkt = s.nextLine();
		}
		// VARIGHET
		System.out.print("Varighet (antall minutter): ");
		int varighet = get_positive_int_from_user();
		// PERSONLIG FORM
		System.out.print("Personlig form [1-10]: ");
		int personlig_form = get_int_between_1_and_10();
		// PRESTASJON
		System.out.print("Prestasjon [1-10]: ");
		int prestasjon = get_int_between_1_and_10();
		// INNENDØRS // UTENDØRS
		System.out.print("Utendørs eller innendørs? : ");
		velg_omgivelse();
		try {
			t.setGeneral(dato, tidspunkt, varighet, personlig_form, prestasjon);
		} catch(Exception e) {
			System.out.println(e);
		}
		// VELG ØVELSER
		System.out.print("Vil du legge til enkeltøvelser? : ");
		if (get_yes()) {
			System.out.println("\nVelg øvelser av følgende øvelser: (format: ØvelseID separtert med mellomrom)");
			velg_øvelser();
		}
		// VELG GRUPPER
		System.out.print("Vil du legge til grupper? : ");
		if (get_yes()) {
			System.out.println("\nVelg grupper av følgende grupper: (format: GruppeID separtert med mellomrom)");
			velg_grupper();
		}
		// MAL
		System.out.print("Skal treningsøkten brukes som mal?: ");
		if (get_yes()) {
			System.out.print("Navn på mal: ");
			String malName = s.nextLine();
			boolean mal;
			try {
				mal = t.setMal(malName);
				if (!mal) {
					System.out.println("Denne treningsøkten er allerede en mal.");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// NOTAT
		System.out.print("Vil du legge til notat?: ");
		while (get_yes()) {
			addNotat();
			System.out.print("Vil du legge til flere notater?: ");
		}
		// RESULTAT
		System.out.print("Vil du legge til resultat?: ");
		while (get_yes()) {
			t.printØvelser();
			System.out.print("Øvelse?: ");
			int øvingsID = get_positive_int_from_user();
			System.out.print("Type: ");
			String type = s.nextLine();
			System.out.print("Verdi: ");
			String answer = s.nextLine();
			double verdi = Double.parseDouble(answer);
			if (t.addResultat(type, verdi, øvingsID)) {
				System.out.print("Resultatet ble lagt til.\nVil du legge til flere resultater?: ");
			} else {
				System.out.print("ØvingsID er ikke gyldig øvelse. Vil du prøve igjen?: ");
			}
		}
		t.fillDatabase();
	}
	
	
	
	private void addNotat() {
		System.out.print("Notattype: ");
		String type = s.nextLine();
		System.out.print("Beskrivelse: ");
		String beskrivelse = s.nextLine();
		t.addNotat(type, beskrivelse);
	}

	private boolean get_yes() {
		String answer = s.nextLine();
		if (answer.toLowerCase().equals("ja")) {
			return true;
		}
		return false;
	}
	
	private void velg_grupper() {
		ResultSet rs = db.hent_gruppetabell();
		System.out.println("\nGruppeID\tNavn");
		List<Integer> tilgjengelig_ID = new ArrayList<Integer>();
		try {
			while (rs.next()) {
				int gruppeID = rs.getInt(1);
				tilgjengelig_ID.add(gruppeID);
				System.out.println(Integer.toString(gruppeID) + " \t\t" + rs.getString(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String grupper = s.nextLine(); 
		if (grupper.length() == 0) { // det kom av og til en tom først?
			grupper = s.nextLine();
		}
		String[] parts = grupper.split(" ");
		for (String p : parts) {
			if (p.matches("^[0-9]+$")) {
				int ID = Integer.parseInt(p);
				if (tilgjengelig_ID.contains(ID)) {
					if (t.addGruppe(ID)) {
						System.out.println("Gruppe " + p + " er lagt til.");
					} else {
						System.out.println("Gruppe " + p + " er allerede lagt til.");
					}
				} else {
					System.out.println(p + " eksisterer ikke. Skriv 'null' hvis du ikke vil erstatte med annen gruppe.");
					velg_grupper();
				}
			} else if (p.equals("null")) {
				continue;
			} else {
				System.out.println("Ugyldig: " + p + ". Må være heltall separert med mellomrom. Skriv 'null' hvis du ikke vil erstatte med annen gruppe.");
				velg_grupper();
			}
		}
	}
	
	private void velg_øvelser() {
		ResultSet rs = db.hent_øvingstabell();
		System.out.println("\nØvelseID\tNavn");
		List<Integer> tilgjengelig_ID = new ArrayList<Integer>();
		try {
			while (rs.next()) {
				int øvingsID = rs.getInt(1);
				tilgjengelig_ID.add(øvingsID);
				System.out.println(Integer.toString(øvingsID) + " \t\t" + rs.getString(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String øvelse = s.nextLine(); 
		if (øvelse.length() == 0) { // det kom av og til en tom først?
			øvelse = s.nextLine();
		}		String[] parts = øvelse.split(" ");
		for (String p : parts) {
			if (p.matches("^[0-9]+$")) {
				int ID = Integer.parseInt(p);
				if (tilgjengelig_ID.contains(ID)) {
					if (t.addØvelse(ID)) {
						System.out.println("Øvelse " + p + " er lagt til.");
					} else {
						System.out.println("Øvelse " + p + " er allerede lagt til.");
					}
				} else {
					System.out.println(p + " eksisterer ikke. Skriv 'null' hvis du ikke vil erstatte med annen øvelse.");
					velg_øvelser();
				}
			} else if (p.equals("null")) {
				continue;
			} else {
				System.out.println("Ugyldig: " + p + ". Må være heltall separert med mellomrom. Skriv 'null' hvis du ikke vil erstatte med annen øvelse.");
				velg_øvelser();
			}
		}
	}
	
	private void velg_omgivelse() {
		String input = s.nextLine();
		input = input.toLowerCase();
		if (input.equals("utendørs")) {
			System.out.print("Temperatur: ");
			int temperatur = get_int_from_user();
			System.out.print("Værtype: ");
			String værtype = s.nextLine();
			t.setUtendørs(temperatur, værtype);
		} else if (input.equals("innendørs")) {
			System.out.print("Ventilasjon: ");
			String ventilasjon = s.nextLine();
			System.out.print("Antall tilskuere: ");
			int antallTilskuere = get_positive_int_from_user();
			t.setInnendørs(ventilasjon, antallTilskuere);
		} else {
			System.out.println("Må være 'utendørs' eller 'innendørs'. Prøv igjen. \n> ");
			velg_omgivelse();
		}
	}
	
	private int get_int_from_user() {
		String input = s.nextLine();
		while (! input.matches("^-?\\d+$")) {
			System.out.println("Må være heltall. Prøv igjen.\n> ");
			input = s.nextLine();
		}
		return Integer.parseInt(input);
	}
	
	private int get_positive_int_from_user() {
		String input = s.nextLine();
		while (! input.matches("^[0-9]+$")) {
			System.out.print("Må være positivt heltall. Prøv igjen.\n> ");
			input = s.nextLine();
		}
		return Integer.parseInt(input);
	}
	
	private int get_int_between_1_and_10() {
		String input = s.nextLine();
		while (! input.matches("[1-9]|10")) {
			System.out.print("Må være heltall fra 1-10. Prøv igjen.\n> ");
			input = s.nextLine();
		}
		return Integer.parseInt(input);
	}
	
	public void alle_treningsøkter() {
		AlleTreningsøkter at = new AlleTreningsøkter(this.myCon);
		at.run();
	}
	
	public void periode_oversikt() {
		SummertTrening st = new SummertTrening(this.myCon);
		st.run();
	}
	
	public void beste_resultat() {
		BesteResultat br = new BesteResultat(this.myCon);
		br.run();
	}
}
