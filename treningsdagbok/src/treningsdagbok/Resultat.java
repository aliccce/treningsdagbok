package treningsdagbok;

public class Resultat {
	public final int øvingsID;
	public final int treningsID;
	public final String type;
	public final double verdi;
	
	public Resultat(int treningsID, int øvingsID, String type, double verdi){
		this.øvingsID = øvingsID;
		this.treningsID = treningsID;
		this.type = type;
		this.verdi = verdi;
	}
}
