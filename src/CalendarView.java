import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Font;
import java.time.DayOfWeek;
/**
 * CalendarView.java
 * @author Tyler Lorenzi, Gregory Mayo, Ealrada Piroyan
 * @version 1.0 08/02/19
 */
/*
 * This class is for following the MVC pattern, which this class is for the view method.
 * In this class, we set up the view of our button, panel and frame for our calendar.
 */
public class CalendarView implements ChangeListener {
	private CalendarModel model;
	private MONTHS[] months = MONTHS.values();
	private int currentlyPickedDay = -1;
	private int numOfDaysinMonth;
	private JFrame frame = new JFrame();
	private JPanel monthPanel = new JPanel();
	private JLabel nameOfMonth = new JLabel();
	private JButton create = new JButton("Create");
	private JButton file = new JButton("File");
	private JButton agenda = new JButton("Agenda");
	private JTextArea textArea = new JTextArea();
	private ArrayList<JButton> dayButtons = new ArrayList<JButton>();
	private JButton monthButton = new JButton("Month");
	private JButton weekButton = new JButton("Week");
	private JButton dayButton = new JButton("Day");
	private JButton prev = new JButton("<");
	private JButton next = new JButton(">");
	private JButton today = new JButton("Today");
	boolean onDayView;
	boolean onMonthView;
	boolean onWeekView;
	LocalDate curerntDayOfView; /// NOTE: this doesn't have to be the same as the currentlySelectedDay
	//view layout that shows different format based on day, week or month
    private ViewLayout viewLayout;
	// CONTROLLER
	/**
	 * Creates buttons representing all the days in the current month and adds them
	 * to an array list and adds them to the month panel.
	 */
	private void createDayButtons() {
		for (int x = 0; x < model.getDayInWeek(1) - 1; x++) { // this adds blank buttons to make sure the days are in the correct spots
			JButton filling = new JButton();
				filling.setEnabled(false);
				monthPanel.add(filling);
			}
			for (int i = 1; i <= numOfDaysinMonth; i++) {
				final int j = i;
				JButton day = new JButton(Integer.toString(i));
				day.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						onDayView = false;
                    	onMonthView = false;
                    	onWeekView = false;
						prev.setEnabled(false);
						next.setEnabled(false);
						pickDay(j - 1); // Local variable i defined in an enclosing scope must be final or effectively final
						displayDate(j);   // Local variable i defined in an enclosing scope must be final or effectively final
						create.setEnabled(true);
					}
				});
				monthPanel.add(day);  // add each button to month panel
				dayButtons.add(day); // add each button to arraylist containing the month's day buttons
			}
		}
	/**
	 * Constructs the calendar.
	 * @param model the  model that stores and manipulates calendar data
	 */
	//CONTROLLER
	public CalendarView(CalendarModel m) { 
		this.model = m;
		numOfDaysinMonth = model.getNumberOfDaysInMonth();
		monthPanel.setLayout(new GridLayout(0, 7));
		textArea = new JTextArea(30, 50);
		textArea.setEditable(false);
		textArea.setFont(new Font("monospaced", Font.PLAIN, 18));
		createDayButtons();
		displayDate(model.getCurrentlySelectedDay());  
		pickDay(model.getCurrentlySelectedDay() - 1);   
		prev.setEnabled(false);
		next.setEnabled(false);
		onDayView = true;
		today.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						LocalDate current = LocalDate.now();
						curerntDayOfView = current;
						if (onMonthView)
                            monthView(current);
                        else if (onWeekView)
                            weekView(current);
                        else { // on day view
                            dayView(current);
                        }
                    }
		});
		next.addActionListener(new ActionListener()  {
					@Override
						public void actionPerformed(ActionEvent e) {
						if (onMonthView){
                            curerntDayOfView = curerntDayOfView.plusMonths(1).withDayOfMonth(1);
                            monthView(curerntDayOfView);
                        }
                        else if (onWeekView){
                            curerntDayOfView = curerntDayOfView.plusDays(7);
                            weekView(curerntDayOfView);
                        }
                        else { // on day view
                            curerntDayOfView = curerntDayOfView.plusDays(1);
                            dayView(curerntDayOfView);
                        }
					}
					
		});
		prev.addActionListener(new ActionListener()  {
					@Override
						public void actionPerformed(ActionEvent e) {
						prev.setEnabled(true);
						next.setEnabled(true);
						if (onMonthView) {
                            curerntDayOfView = curerntDayOfView.minusMonths(1).withDayOfMonth(1);       
                            monthView(curerntDayOfView);
                        }
                        else if (onWeekView) {
                            curerntDayOfView = curerntDayOfView.minusDays(7);
                            weekView(curerntDayOfView);
                        }
                        else { // on day view
                            curerntDayOfView = curerntDayOfView.minusDays(1);
                            dayView(curerntDayOfView);
                        }
					}
		});
		monthButton.addActionListener(new ActionListener() {
					@Override
						public void actionPerformed(ActionEvent e) {
						monthView(LocalDate.of(model.getCurrentYear(),  model.getCurrentMonth() + 1, model.getCurrentlySelectedDay()));
                        prev.setEnabled(true);
                        next.setEnabled(true);
						}
		});
		weekButton.addActionListener(new ActionListener() {
					@Override
						public void actionPerformed(ActionEvent e) {
                        prev.setEnabled(true);
                        next.setEnabled(true);
                        weekView(LocalDate.of(model.getCurrentYear(),  model.getCurrentMonth() + 1, model.getCurrentlySelectedDay()));
                    }
		});
		dayButton.addActionListener(new ActionListener()  {
					@Override
						public void actionPerformed(ActionEvent e) {
						prev.setEnabled(true);
                        next.setEnabled(true);
                        dayView(LocalDate.of(model.getCurrentYear(),  model.getCurrentMonth() + 1, model.getCurrentlySelectedDay()));
                    }
		});
		create.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createEventDialog("");
			}
		});
		JButton prevMonth = new JButton("<");
		prevMonth.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				model.previousMonth();
			}
		});
		JButton nextMonth = new JButton(">");
		nextMonth.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				model.nextMonth();
			}
		});
		agenda.addActionListener(new  ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final JDialog dialog = new JDialog();
				dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
				final JTextField startDay = new JTextField(15);
				final JTextField endDay = new JTextField(15);
				JButton submit = new JButton("Submit");
				submit.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (startDay.getText().isEmpty() || endDay.getText().isEmpty()) 
							return;
						else {
							dialog.dispose();							
							String[] start = startDay.getText().split("/");
							String[] end = endDay.getText().split("/");
							if (start[2].length() == 2) {
								start[2] = "20" + start[2];
							}
							if (end[2].length() == 2) {
								end[2] = "20" + end[2];
							}
							int[] startArray = new int[] {0,0,0};
							for (int i = 0; i < start.length; i++) {
								startArray[i] = Integer.parseInt(start[i]);
							}
							int[] endArray = new int[] {0,0,0};
							for (int i = 0; i < end.length; i++) {
								endArray[i] = Integer.parseInt(end[i]);
							}
							LocalDate d1 = LocalDate.of(startArray[2], startArray[0], startArray[1]);
							LocalDate d2 = LocalDate.of(endArray[2], endArray[0], endArray[1]);
							model.getEvents(d1,d2);	
						}
					}
				});
				dialog.setLayout(new GridBagLayout());
				GridBagConstraints gridContraints = new GridBagConstraints();
				dialog.add(new JLabel("start day mm/dd/yyyy"), gridContraints);
				dialog.add(new JLabel("End day mm/dd/yyyy"), gridContraints);
				gridContraints.gridy = 2;
				dialog.add(startDay, gridContraints);
				dialog.add(endDay, gridContraints);
				dialog.add(submit, gridContraints);
				dialog.pack();
				dialog.setVisible(true);
			}
		});
		file.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JDialog dialog = new JDialog();
				dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
				final JTextField fileNameInput = new JTextField(30);
				JButton submit = new JButton("Submit");
				submit.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (fileNameInput.getText().isEmpty()) {
							return;
						}
						else {
							dialog.dispose();
							String fileName = fileNameInput.getText();
							try {
								model.fileReader(fileName);
							} catch (FileNotFoundException e1) {
								// TODO Auto-generated catch block
							}
						}
					}
				});
				dialog.setLayout(new GridBagLayout());
				GridBagConstraints gridContraints = new GridBagConstraints();
				gridContraints.weightx = 1.0;
				gridContraints.anchor = GridBagConstraints.LINE_START;
				dialog.add(fileNameInput, gridContraints);
				gridContraints.gridy = 3;
				gridContraints.weightx = 0.0;
				gridContraints.anchor = GridBagConstraints.LINE_START;
				dialog.add(new JLabel("Path to File"), gridContraints);
				gridContraints.anchor = GridBagConstraints.CENTER;
				gridContraints.gridy = 4;
				dialog.add(submit, gridContraints);
				dialog.pack();
				dialog.setVisible(true);
			}
		}); // END OF CONTROLLER
		JPanel titleContainer = new JPanel();
		titleContainer.setLayout(new FlowLayout());
		JPanel monthContainer = new JPanel();
		monthContainer.setLayout(new BorderLayout());
		JPanel finalContainer = new JPanel();
		finalContainer.setLayout(new BorderLayout());
		nameOfMonth.setText("" + months[model.getCurrentMonth()] + " " + model.getCurrentYear());
		titleContainer.add(nameOfMonth);
		titleContainer.add(prevMonth);
		titleContainer.add(nextMonth);
		monthContainer.add(titleContainer, BorderLayout.NORTH);
		monthContainer.add(new JLabel("         S                M                T                 W                T                 F                 S"), BorderLayout.CENTER);
		monthContainer.add(monthPanel, BorderLayout.SOUTH);
		GridBagConstraints constraints = new GridBagConstraints();
		JPanel dayViewPanel = new JPanel();
		dayViewPanel.setLayout(new GridBagLayout());
		JPanel btnsPanel = new JPanel((new FlowLayout(FlowLayout.CENTER, 0, 0)));
		JPanel btnsPanel2 = new JPanel((new FlowLayout(FlowLayout.CENTER, 0, 0 )));
		btnsPanel2.add(prev);
		btnsPanel2.add(today);
		btnsPanel2.add(next);
		btnsPanel2.add(dayButton);
		btnsPanel2.add(weekButton);
		btnsPanel2.add(monthButton);
		btnsPanel2.add(agenda);
		btnsPanel2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 30)); 
		FlowLayout layout = (FlowLayout)btnsPanel2.getLayout();
		layout.setVgap(0);
		btnsPanel.add(create);
		btnsPanel.add(file);
		//Agenda, create, file view
		constraints.gridx = 0;
		constraints.gridy = 1;
		dayViewPanel.add(btnsPanel, constraints);
		//today, day, week, month view
		constraints.gridx = 0;
		constraints.gridy = 2;
		dayViewPanel.add(btnsPanel2, constraints);
		//Event view
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 3;
		JScrollPane dayScrollPane = new JScrollPane(textArea);
		dayScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		dayViewPanel.add(dayScrollPane, constraints);
		finalContainer.add(monthContainer, BorderLayout.NORTH);
		frame.add(finalContainer);
		frame.add(dayViewPanel);
		frame.setLayout(new FlowLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	// CONTROLLER HELPER METHODS
	/**
	 * creates a border around selected date and puts a border around current date 
	 * when the program first starts up
	 * @param dayToSelect day to select
	 */
	private void pickDay(int dayToSelect) {  
		if (currentlyPickedDay != -1)  // DONT REMOVE
			dayButtons.get(currentlyPickedDay).setBorder(new JButton().getBorder());
		currentlyPickedDay = dayToSelect;
		dayButtons.get(dayToSelect).setBorder(new LineBorder(Color.RED, 1));
	}	
	/**
	 * Creates an event on the selected date through user input.
	 * @param txt String
	 */
	private void createEventDialog(String txt) {
		final JDialog dialog = new JDialog();
		dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		final JTextField date = new JTextField(30);
		final JTextField eventText = new JTextField(30);
		final JTextField timeStart = new JTextField(10);
		final JTextField timeEnd = new JTextField(10);
		JButton submit = new JButton("Submit");
		submit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
				if (date.getText().split("/").length != 3 ||eventText.getText().isEmpty() || timeStart.getText().isEmpty() || timeEnd.getText().isEmpty() || date.getText().isEmpty()) {
					return;
				} else {
					String start = timeStart.getText();
					String end = timeEnd.getText();
					String[] dateArray = date.getText().split("/");
					int[] dayOfEvent = new int[] {0,0,0};
					if (dateArray[2].length() == 2)
						dateArray[2] = "20" + dateArray[2];
					for (int i = 0; i < dayOfEvent.length; i++) {
						dayOfEvent[i] = Integer.parseInt(dateArray[i]);
					}
					Event ev = new Event(eventText.getText(), Integer.valueOf(start), Integer.valueOf(end));
					LocalDate d = LocalDate.of(dayOfEvent[2], dayOfEvent[0], dayOfEvent[1]);
					boolean timeConflict = model.overlap(d, ev);
					if (!timeConflict)
						model.addEvent(d, ev);
					else {
						createEventDialog("Try again, TIME CONFLICT ERROR: ");
					}
					if (!timeConflict && onDayView && d.equals(model.getCurrentLocalDate())) {
                        model.getEvents(d);
                    }
                    else if (!timeConflict && onMonthView && dayOfEvent[0] == model.getCurrentMonth() + 1) {
                        LocalDate last = d.plusMonths(1).withDayOfMonth(1).minusDays(1);
                        model.getEvents(LocalDate.of(dayOfEvent[2], dayOfEvent[0], 1), last);
                    }
                    else if (!timeConflict && onWeekView) {
                        weekView(LocalDate.of(model.getCurrentYear(), model.getCurrentMonth() + 1, model.getCurrentlySelectedDay()));
                    }
                    else if (!timeConflict && model.getCurrentYear() == dayOfEvent[2] && model.getCurrentMonth() + 1 == dayOfEvent[0] && model.getCurrentlySelectedDay() == dayOfEvent[1])
                        displayDate(dayOfEvent[1]);
				}
			}
		});
		dialog.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(3, 3, 3, 3);
		c.gridx = 0;
		c.weightx = 1.0;
		c.gridy = 1;
	    c.anchor = GridBagConstraints.LINE_START;
	    JLabel l4 = new JLabel(txt + "date of event in following format: mm/dd/yyyy");
	    if (!txt.contentEquals(""))
	    	l4.setForeground(Color.RED);
		dialog.add(l4, c);
	    c.anchor = GridBagConstraints.CENTER;
		c.gridy = 2;
	    c.anchor = GridBagConstraints.LINE_START;
		dialog.add(date, c);
		c.gridy = 4;
		c.weightx = 1.0;
	    c.anchor = GridBagConstraints.LINE_START;
	    JLabel l1 = new JLabel("Name of Event");
	    if (!txt.contentEquals(""))
	    	l1.setForeground(Color.RED);
		dialog.add(l1, c);
		c.gridy = 5;
		c.weightx = 1.0;
		dialog.add(eventText, c);
	    c.anchor = GridBagConstraints.CENTER;
	    c.anchor = GridBagConstraints.CENTER;
		c.gridy = 7;
		c.weightx = 0.0;
	    c.anchor = GridBagConstraints.LINE_START;
	    JLabel l2 = new JLabel(txt + "Start Hour (0-24)");
	    if (!txt.contentEquals(""))
	    	l2.setForeground(Color.RED);
		dialog.add(l2, c);
	    c.gridy = 8;
		dialog.add(timeStart, c);
		c.gridy = 10;
		JLabel l3 = new JLabel(txt + "End Hour (0-24)");
	    if (!txt.contentEquals(""))
	    	l3.setForeground(Color.RED);
		dialog.add(l3, c);
		c.gridy = 11;
	    c.anchor = GridBagConstraints.LINE_START;
		dialog.add(timeEnd, c);
		c.anchor = GridBagConstraints.LINE_END;
		dialog.add(submit, c);
		dialog.pack();
		dialog.setVisible(true);
	}
	/**
     * to get adds a day's events to the GUI's text area
     * @param d LocalDate of day's events 
     *  
     */
    public void dayView(LocalDate d) { 
        //set layout for week view
        setViewLayout(new DayLayout());   
        onWeekView = false;
        onMonthView = false;
        onDayView = true;
        model.getEvents(d); 
    }
    /**
     *  @param date LocalDate in a week to get the entire week's events adds a week's 
     *  events to the GUI's text area
     */
    public void weekView(LocalDate date) {
        //set layout for week view
        setViewLayout(new WeekLayout());
        onWeekView = true;
        onMonthView = false;
        onDayView = false;
        LocalDate start = date;
        LocalDate end = date;
        DayOfWeek day = date.getDayOfWeek();
        int d = day.getValue();
        if (d == 7) 
            d = 0;   
        start = start.minusDays(d); 
        end = end.plusDays(7 - d - 1);
        model.getEvents(start,end);
    }
    /**
     * @param date LocalDate in a month to get the entire month's events
     * adds a month's events to the GUI's text area
     */
    public void monthView(LocalDate date) {
        //set layout for week view
        setViewLayout(new MonthLayout());
        onWeekView = false;
        onMonthView = true;
        onDayView = false;
        LocalDate start = LocalDate.of(date.getYear(), date.getMonth(), 1);
        LocalDate end = date.plusMonths(1).withDayOfMonth(1).minusDays(1);
        model.getEvents(start, end);
    }
	/**
	 * Shows the selected date and events on that date.
	 * @param i the selected date
	 */
	private void displayDate(int i) {  
		LocalDate d = LocalDate.of(model.getCurrentYear(), model.getCurrentMonth() + 1, i);
		curerntDayOfView = d;
		model.setCurrentlySelectedDay(i);
        model.getEvents(d);
    }
	@Override
	public void stateChanged(ChangeEvent e) {
		if (model.monthStateChanged()|| model.getChangeInDisplay() || model.getChangeInSelectedDay()) {
			textArea.setText(model.getTextDisplay());
			//format the layout
            if (viewLayout != null){
                viewLayout.format(textArea, model.getDates(), model.getEvents());
            }
            model.resetChangeInTestDisplay();
            model.resetChangeInSelectedDay();
			numOfDaysinMonth = model.getNumberOfDaysInMonth();
			dayButtons.clear();
			monthPanel.removeAll();
			nameOfMonth.setText("" + months[model.getCurrentMonth()] + " " + model.getCurrentYear());
			createDayButtons();
			currentlyPickedDay = -1;
			model.resetChangeOfMonth();
			frame.pack();
			frame.repaint();
			pickDay(model.getCurrentlySelectedDay() - 1);
		} else {
			displayDate(model.getCurrentlySelectedDay());
			pickDay(model.getCurrentlySelectedDay() - 1);
		}
	}
	/**
     * sets view layout for day, week, and month views
     * @param vl Viewlayout 
     */
    public void setViewLayout(ViewLayout vl){
        this.viewLayout = vl;
    }
    /**
     * get text area 
     * @return text area
     */
    public JTextArea getTextArea() {
        return textArea;
    }
}