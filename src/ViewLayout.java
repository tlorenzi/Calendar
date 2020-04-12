import java.time.LocalDate;
import java.util.List;
import javax.swing.JTextArea;
/**
 * View.java
 * @author Tyler Lorenzi, Gregory Mayo, Ealrada Piroyan
 * @version 1.0 08/02/19
 */
public interface ViewLayout{
	/**
	 * format the layout for day, week,and month display
	 * @param output
	 * @param dates
	 * @param events
	 */
	public void format(JTextArea output, List<LocalDate> dates, List< List<Event>> events);
}
