/**
 * SimpleCalendar.java
 * @author Tyler Lorenzi, Gregory Mayo, Ealrada Piroyan
 * @version 1.0 08/02/19
 */
public class SimpleCalendar {
	public static void main(String[] args) {
		CalendarModel cm = new CalendarModel();
		CalendarView cv = new CalendarView(cm);
		cm.attach(cv);
	}

}