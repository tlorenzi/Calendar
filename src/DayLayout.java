import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JTextArea;
/**
 * DayView.java
 * @author Tyler Lorenzi, Gregory Mayo, Ealrada Piroyan
 * @version 1.0 08/02/19
 */
public class DayLayout implements ViewLayout{
	/**
     * format the layout for day  display
     * @param output 
     * @param dates
     * @param events
     */
    public void format(JTextArea output, List<LocalDate> dates, List<List<Event>> events){
        //no dates, do not format
        if (dates.isEmpty()){
            return;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d yyyy");          
        final int length = 50;//length of each line
        StringBuilder content = new StringBuilder();//content of output
        //-----------------
        for (int i = 0; i < length; i++){
            content.append("-");  
        }
        content.append('\n');
        content.append(String.format("|%" + (length - 2) + "s|\n", "Events"));
        //-----------------
        for (int i = 0; i < length; i++){
            content.append("-");  
        }
        content.append('\n');
        content.append(String.format("|%" + (length - 2) + "s|\n", formatter.format(dates.get(0))));
        //-----------------
        for (int i = 0; i < length; i++){
            content.append("-");  
        }
        content.append('\n');
        for(Event event : events.get(0)) {
            content.append(String.format("|%-" + (length - 2) + "s|\n", event.getStartTime() + ":00" + " - " + event.getEndTime() + ":00  " + event.getName()));
        }
        output.setText(content.toString());
    }
}
