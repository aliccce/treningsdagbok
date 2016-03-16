package treningsdagbok;
import java.util.ArrayList;

import java.sql.*;

import treningsdagbok.Resultat;

public class Treningsøkt {
	
	static private String TRENINGSØKT = "Treningsøkt";
	static private String TRENINGS_ID = "TreningsID";
	static private String MAL = "Mal";
	static private String MAL_ID = "MalID";
	
	
	// Connection
	Connection myCon;
	
	// General
	public int treningsID;
	private String dato;
	private String tidspunkt;
	private int varighet;
	private int personligForm;
	private int prestasjon;
	
	// Utendørs
	private boolean utendørs;
	private int temperatur;
	private String værtype;
	
	// Innendørs
	private String ventilasjon;
	private int antallTilskuere;
	
	// �velser og grupper
	private ArrayList<Integer> øvelser;
	private ArrayList<Integer> grupper;
	
	// Notater og Mal
	private int malID;
	private boolean mal;
	private String malName;
	private ArrayList<String[]> notater;
	private int notatCounter = 1;
		
	// Resultat
	private ArrayList<Resultat> resultater;
	
	
	public Treningsøkt(Connection myCon){
		this.myCon = myCon;
		øvelser = new ArrayList<Integer>();
		grupper = new ArrayList<Integer>();
		mal = false;
		notater = new ArrayList<String[]>();
		resultater = new ArrayList<Resultat>();
		
	}
	
	public void setGeneral(String dato, String tidspunkt, int varighet, int personligForm, int prestasjon) throws Exception{
		this.treningsID = setID(TRENINGS_ID, TRENINGSØKT);
		this.dato = dato;
		this.tidspunkt = tidspunkt;
		this.varighet = varighet;
		this.personligForm = personligForm;
		this.prestasjon = prestasjon;
	}
	
	public void setUtendørs(int temperatur, String værtype){
		this.utendørs = true;
		this.temperatur = temperatur;
		this.værtype = værtype;
	}
	
	public void setInnendørs(String ventilasjon, int antallTilskuere){
		this.utendørs = false;
		this.ventilasjon = ventilasjon;
		this.antallTilskuere = antallTilskuere;
	}
	
	public boolean addØvelse(int øvingsID){
		if (!øvelser.contains(øvingsID)){
			øvelser.add(øvingsID);
			return true;
		}
		return false;
	}
	
	public boolean addGruppe(int gruppeID){
		if (!grupper.contains(gruppeID)){
			grupper.add(gruppeID);
			return true;
		}
		return false;
	}
	
	public boolean setMal(String malName) throws Exception{
		if (!this.mal){
			this.malID = setID(MAL_ID, MAL);
			this.malID = findMaxID(MAL_ID, MAL);
			this.malName = malName;
			this.mal = true;
			return true;
		}
		return false;
	}
	
	public void addNotat(String type, String beskrivelse){
		String[] temp = new String[2];
		temp[0] = type;
		temp[1] = beskrivelse;
		this.notater.add(temp);
	}
	
	
	public boolean addResultat(String type, double verdi, int øvingsID){
		/* Per nå sjekker denne metoden bare opp mot øvelser-lista, ikke grupper. Dette kan legges til seinere,
		men det krever kanskje at vi må lese fra databasen hvilke øvelser gruppen inneholder. Dette kan fikses
		i checkØvingsID.*/
		boolean validØvingsID = this.checkØvingsID(øvingsID);
		if (validØvingsID){
			Resultat res = new Resultat(this.treningsID, øvingsID, type, verdi);
			this.resultater.add(res);
		}
		return validØvingsID;
	}
	
	public void fillDatabase(){
		insertTreningsøkt();
		insertInnendørsUtendørs();
		insertMal();
		insertNotat();
		insertØvelser();
		insertGrupper();
		insertResultat();
		
		System.out.println("Oppretting av ny treningsøkt vellykket.");
		
		checkResults();
		
		
	}
	
	public void regret(){
		try{
			Statement stmt = myCon.createStatement();
			stmt.execute("delete from Resultat where TreningsID = " + treningsID + ";");
			stmt.execute("delete from Mal where TreningsID = " + treningsID + ";");
			stmt.execute("delete from Treningsøkt where TreningsID = " + treningsID + ";");
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void printØvelser(){
		try{
			Statement stmt = myCon.createStatement();
			for (int øvingsID: øvelser){
				ResultSet result = stmt.executeQuery("select Øvingsnavn as navn from Øvelse where øvingsID = " + øvingsID + ";");
				if(result.first()){
					System.out.println(øvingsID + " ... " + result.getString("navn"));
				}
			}
			for (int gruppeID: grupper){
				
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	// PRIVATE METHODS
	private boolean checkØvingsID(int øvingsID){
		return this.øvelser.stream().anyMatch(øvid -> øvid == øvingsID);
	}
	
	private void insertTreningsøkt(){
		try{
			String insertion = "insert into Treningsøkt values (?, ?, ?, ?, ?, ?);";
			PreparedStatement prepStmt = myCon.prepareStatement(insertion);
			prepStmt.setInt(1, treningsID);
			prepStmt.setDate(2, Date.valueOf(this.dato));
			prepStmt.setTime(3, Time.valueOf(this.tidspunkt));
			prepStmt.setDouble(4, this.varighet);
			prepStmt.setInt(5, this.personligForm);
			prepStmt.setInt(6,  prestasjon);
			prepStmt.execute();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void insertInnendørsUtendørs(){
		try{
			if(this.utendørs){
				String insertion = "insert into Utendørs values (?, ?, ?);";
				PreparedStatement prepStmt = myCon.prepareStatement(insertion);
				prepStmt.setInt(1,  treningsID);
				prepStmt.setInt(2, temperatur);
				prepStmt.setString(3, værtype);
				prepStmt.execute();
			} else{
				String insertion = "insert into Innendørs values (?, ?, ?);";
				PreparedStatement prepStmt = myCon.prepareStatement(insertion);
				prepStmt.setInt(1, treningsID);
				prepStmt.setString(2, ventilasjon);
				prepStmt.setInt(3, antallTilskuere);
				prepStmt.execute();
			} 
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void insertMal(){
		try{
			String insertion = "insert into Mal values (?, ?, ?);";
			PreparedStatement prepStmt = myCon.prepareStatement(insertion);
			prepStmt.setInt(1, malID);
			prepStmt.setInt(2,  treningsID);
			prepStmt.setString(3, malName);
			prepStmt.execute();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void insertNotat(){
		try{
			String insertion = "insert into Notat values (?, ?, ?, ?);";
			for(String[] notat: this.notater){
				PreparedStatement prepStmt = myCon.prepareStatement(insertion);
				prepStmt.setInt(1, notatCounter++);
				prepStmt.setInt(2, treningsID);
				prepStmt.setString(3, notat[0]);
				prepStmt.setString(4, notat[1]);
				prepStmt.execute();
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void insertØvelser(){
		try{
			String insertion = "insert into TreningsøktØvelse values (?, ?);";
			for(int øvingsID: øvelser){
				PreparedStatement prepStmt = myCon.prepareStatement(insertion);
				prepStmt.setInt(1, treningsID);
				prepStmt.setInt(2, øvingsID);
				prepStmt.execute();
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void insertGrupper(){
		try{
			String insertion = "insert into TreningsøktGruppe values (?, ?);";
			for(int gruppeID: grupper){
				PreparedStatement prepStmt = myCon.prepareStatement(insertion);
				prepStmt.setInt(1, treningsID);
				prepStmt.setInt(2,  gruppeID);
				prepStmt.execute();
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void insertResultat(){
		try{
			String insertion = "insert into Resultat values (?, ?, ?, ?);";
			for(Resultat resultat: resultater){
				PreparedStatement prepStmt = myCon.prepareStatement(insertion);
				prepStmt.setInt(1, treningsID);
				prepStmt.setInt(2, resultat.øvingsID);
				prepStmt.setString(3, resultat.type);
				prepStmt.setDouble(4, resultat.verdi);
				prepStmt.execute();
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private int checkReachedGoal(Resultat res){
		try{
			Statement stmt = myCon.createStatement();
			ResultSet result = stmt.executeQuery("select * from Mål natural join Øvelse where øvingsID = " + res.øvingsID
					+ " and typ = \"" + res.type + "\";");
					
			if (result.first()){
				boolean opptil = result.getBoolean("Opptil");
				int verdi = result.getInt("Verdi");
				if (opptil){
					if(res.verdi >= verdi){
						return verdi;
					}
					return -1;
				} else{
					if(res.verdi <= verdi){
						return verdi;
					}
					return -1;
				}
			}
			return -1;	
		} catch(Exception e){
			e.printStackTrace();
			return -1;
		}
	}
	
	private void checkResults(){
		for(Resultat res: resultater){
			int mål = checkReachedGoal(res);
			if (mål != -1){
				System.out.println("Gratulerer! Du har nådd målet ditt på " + mål + "!");
				System.out.println("Din verdi var på " + res.verdi + ".");
				try{
					Statement stmt = myCon.createStatement();
					ResultSet result = stmt.executeQuery("select Øvingsnavn as navn from Øvelse where ØvingsID = " + res.øvingsID + ";");
					if (result.first()){
						System.out.println("Øvelsen var " + result.getString("navn") + ", og måltypen var " + res.type + ".");
					}
				} catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	
	private int findMaxID(String idName, String tableName){
		/* This method generates a new ID, by finding the highest id in the relevant table, and returning that + 1. 
		 * idName: The name of the column that defines the ID
		 * tableName: The name of the table
		 */
		try{
			Statement stmt = myCon.createStatement();
			ResultSet result = stmt.executeQuery("select max(" + idName + ") as max_id from " + tableName +";");
			if (result.first()){
				return result.getInt("max_id") + 1;
			}
			return -1;
		} catch(Exception e){
			e.printStackTrace();
			return -1;
		}
	}

	private int setID(String idName, String table) throws Exception{
		/* This method returns a new ID. It uses findMaxID and throws an exception if something goes wrong there.
		 */
		int newID = findMaxID(idName, table);
		if (newID == -1){
			throw new Exception("Could not find an appropriate ID. Fix your program");
		}
		return newID;
	}
}
