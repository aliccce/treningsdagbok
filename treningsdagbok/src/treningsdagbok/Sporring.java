package treningsdagbok;
import java.sql.Connection;


public abstract class Sporring {
	Connection myCon;

	public Sporring(Connection myCon){
		this.myCon = myCon;
	}
	
	public abstract void run();
}