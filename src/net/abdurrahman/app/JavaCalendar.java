package net.abdurrahman.app;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class JavaCalendar extends JFrame {
    //Instance Variables
    static JLabel monthLabel, yearLabel;
    static JButton previousButton, nextButton;
    static JTable calendarTable;
    static JComboBox yearComboBox;
    static JFrame mainFrame;
    static Container theContainer;
    static DefaultTableModel calendarTableModel;//the calendar table model
    static JScrollPane calendarScrollPane;//the scrollPane for the calendar
    static JPanel calendarPanel;
    static int realYear, realMonth, realDay, currentYear, currentMonth;

    /**
     * Calendar Constructor - Creates an instance of the Calendar Class
     */
    public JavaCalendar() {

        //Prepare frame
        mainFrame = new JFrame(" Calendar");
        mainFrame.setSize(325, 375);
        mainFrame.setLocation(550, 200);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(JavaCalendar.class.getResource("../img/calendar.png")));

        theContainer = mainFrame.getContentPane(); //Get content pane
        theContainer.setLayout(null);//Apply null layout
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create controls
        monthLabel = new JLabel("January");
        yearLabel = new JLabel("Change year:");
        yearComboBox = new JComboBox();
        previousButton = new JButton("<<");
        nextButton = new JButton(">>");
        calendarTableModel = new DefaultTableModel() {
            /**
             * isCellEditable Method -
             * @param rowIndex - integer representing the row index
             * @param mColIndex - integer representing the column index
             * @return Boolean
             */
            @Override
            public boolean isCellEditable(int rowIndex, int mColIndex) {
                return false;
            }
        };//end of the Anonymous DefaultTableModel

        monthLabel.setFont(monthLabel.getFont().deriveFont(Font.BOLD, 16.0f));

        nextButton.setBackground(Color.decode("#23476C"));
        nextButton.setForeground(Color.decode("#FAFAFA"));
        nextButton.setFont(nextButton.getFont().deriveFont(Font.BOLD));

        previousButton.setBackground(Color.decode("#23476C"));
        previousButton.setForeground(Color.decode("#FAFAFA"));
        previousButton.setFont(previousButton.getFont().deriveFont(Font.BOLD));

        calendarTable = new JTable(calendarTableModel);
        calendarScrollPane = new JScrollPane(calendarTable);

        calendarPanel = new JPanel(null);
        calendarPanel.setBackground(Color.decode("#E0E0E0"));


        //Set border
        calendarPanel.setBorder(BorderFactory.createTitledBorder(""));

        //Register action listeners
        previousButton.addActionListener(new PreviousButtonListener());
        nextButton.addActionListener(new NextButtonListener());
        yearComboBox.addActionListener(new YearComboBoxListener());

        //Add controls to contentPane container
        theContainer.add(calendarPanel);
        calendarPanel.add(monthLabel);
        calendarPanel.add(yearLabel);
        calendarPanel.add(yearComboBox);
        calendarPanel.add(previousButton);
        calendarPanel.add(nextButton);
        calendarPanel.add(calendarScrollPane);

        //Set bounds
        /*calendarPanel.setBounds(0, 0, 320, 335);*/
        calendarPanel.setBounds(0, 0, 500, 600);
        monthLabel.setBounds(160 - monthLabel.getPreferredSize().width / 2, 25, 100, 25);
        yearLabel.setBounds(10, 305, 80, 20);
        yearComboBox.setBounds(230, 305, 80, 20);
        previousButton.setBounds(10, 25, 50, 25);
        nextButton.setBounds(260, 25, 50, 25);
        calendarScrollPane.setBounds(10, 50, 300, 250);

        //Make frame visible
        mainFrame.setResizable(false);
        mainFrame.setVisible(true);

        //Get real month/year
        GregorianCalendar cal = new GregorianCalendar(); //Create calendar
        realDay = cal.get(GregorianCalendar.DAY_OF_MONTH); //Get day
        realMonth = cal.get(GregorianCalendar.MONTH); //Get month
        realYear = cal.get(GregorianCalendar.YEAR); //Get year
        currentMonth = realMonth; //Match month and year
        currentYear = realYear;

        //Add days of the week header
        String[] headers = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"}; //All headers
        for (int i = 0; i < 7; i++) {
            calendarTableModel.addColumn(headers[i]);
        }

        calendarTable.getParent().setBackground(calendarTable.getBackground()); //Set background

        //No resize/reorder
        calendarTable.getTableHeader().setResizingAllowed(false);
        calendarTable.getTableHeader().setReorderingAllowed(false);

        //Single cell selection
        calendarTable.setColumnSelectionAllowed(true);
        calendarTable.setRowSelectionAllowed(true);
        calendarTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //Set row/column count
        calendarTable.setRowHeight(38);
        calendarTableModel.setColumnCount(7);
        calendarTableModel.setRowCount(6);

        //Populate table
        for (int i = realYear - 100; i <= realYear + 100; i++) {
            yearComboBox.addItem(String.valueOf(i));
        }

        //Refresh calendar
        refreshCalendar(realMonth, realYear); //Refresh calendar

    }//end of the JavaCalendar Constructor

    /**
     * refreshCalendar Method -
     *
     * @param month - integer representing the month
     * @param year - integer representing the year
     */
    public static void refreshCalendar(int month, int year) {
        //Variables
        String[] months = {"JANUARY", "FEBRUARY", "MARCH",
                "APRIL", "MAY", "JUNE",
                "JULY", "AUGUST", "SEPTEMBER",
                "OCTOBER", "NOVEMBER", "DECEMBER"};

        int numberOfDays, startOfMonth; //Number Of Days, Start Of Month

        //Allow/disallow buttons
        previousButton.setEnabled(true);
        nextButton.setEnabled(true);
        if (month == 0 && year <= realYear - 10) {
            previousButton.setEnabled(false);
        } //Too early
        if (month == 11 && year >= realYear + 100) {
            nextButton.setEnabled(false);
        } //Too late
        monthLabel.setText(months[month]); //Refresh the month label (at the top)
        monthLabel.setBounds((160 - monthLabel.getPreferredSize().width / 2), 25, 180, 25); //Re-align label with calendar
        yearComboBox.setSelectedItem(String.valueOf(year)); //Select the correct year in the combo box

        //Clear table
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                calendarTableModel.setValueAt(null, i, j);
            }
        }

        //Get first day of month and number of days
        GregorianCalendar calendar = new GregorianCalendar(year, month, 1);
        numberOfDays = calendar.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
        startOfMonth = calendar.get(GregorianCalendar.DAY_OF_WEEK);

        //Draw calendar
        for (int i = 1; i <= numberOfDays; i++) {
            int row = (i + startOfMonth - 2) / 7;
            int column = (i + startOfMonth - 2) % 7;
            calendarTableModel.setValueAt(i, row, column);
        }

        //Apply renderers
        calendarTable.setDefaultRenderer(calendarTable.getColumnClass(0), new CalendarTableRenderer());

    }//end of the refreshCalendar Method


    /**
     * main Method - contains the command line arguments
     * @param args - the String[] command line arguments
     */
    public static void main(String[] args) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JavaCalendar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JavaCalendar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JavaCalendar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JavaCalendar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        EventQueue.invokeLater(() -> {
            /*new Calendar().setVisible(true);*/
            new JavaCalendar();
        });

    }//end of the main Method

    /**
     * CalendarTableRenderer Class renders the calendarTable
     */
    static class CalendarTableRenderer extends DefaultTableCellRenderer {
        /**
         * getTableCellRendererComponent Method -
         *
         * @param table  - the JTable table
         * @param value  - the Object value
         * @param selected - is boolean selected or not selected
         * @param focused - has boolean focus or has lost focus
         * @param row     - the int row of the calendar month
         * @param column     - the int column of the calendar month
         * @return Component
         */
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean selected,
                                                       boolean focused, int row, int column) {
            super.getTableCellRendererComponent(table, value, selected, focused, row, column);
            if (column == 0 || column == 6) {//the weekend
                setBackground(Color.decode("#D5E3F1"));
                setForeground(Color.decode("#1A1A1A"));
                setHorizontalAlignment(SwingConstants.CENTER);
            } else {//the weekdays
                setBackground(Color.decode("#FAFAFA"));
                setForeground(Color.decode("#1A1A1A"));
                setHorizontalAlignment(SwingConstants.CENTER);
            }
            if (value != null) {
                if (Integer.parseInt(value.toString()) == realDay && currentMonth == realMonth
                        && currentYear == realYear) {//the current day
                    setBackground(Color.decode("#316396"));
                    setForeground(Color.decode("#FAFAFA"));
                    setFont(getFont().deriveFont(Font.BOLD));
                    setHorizontalAlignment(SwingConstants.CENTER);
                }
            }

            setBorder(BorderFactory.createLineBorder(Color.black, CENTER));

            return this;
        }
    }//end of the CalendarTableRenderer Class

    /**
     * PreviousButtonListener Class responds to the events of the previousButton
     */
    static class PreviousButtonListener implements ActionListener {
        /**
         * actionPerformed Method - Redefines the actionPerformed method for the ActionListener
         * Interface, and responds to the events of the previousButton
         *
         * @param e - the ActionEvent of clicking the previousButton
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentMonth == 0) {//Move backwards one year
                currentMonth = 11;
                currentYear -= 1;

            } else {//Move backwards one month
                currentMonth -= 1;
            }

            refreshCalendar(currentMonth, currentYear);

        }//end of the actionPerformed Method for the PreviousButtonListener Class
    }//end of the PreviousButtonListener Class

    /**
     * NextButtonListener Class responds to the events of the nextButton
     */
    static class NextButtonListener implements ActionListener {
        /**
         * actionPerformed Method - Redefines the actionPerformed method of the ActionListener
         * Interface, and responds to the events of the nextButton
         *
         * @param e - the ActionEvent event of clicking the nextButton
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentMonth == 11) {//Move forward one year
                currentMonth = 0;
                currentYear += 1;

            } else { //Forward one month
                currentMonth += 1;
            }

            refreshCalendar(currentMonth, currentYear);

        }//end of the actionPerformed Method of the NextButtonListener Class
    }//end of the NextButtonListener Class

    /**
     * YearComboBoxListener Class responds to the events of the yearComboBox
     */
    static class YearComboBoxListener implements ActionListener {
        /**
         * actionPerformed Method - Redefines the actionPerformed method of the ActionListener
         * Interface, and responds to the events of the yearComboBox
         *
         * @param ae - the ActionEvent event of clicking the yearComboBox
         */
        @Override
        public void actionPerformed(ActionEvent ae) {
            if (yearComboBox.getSelectedItem() != null) {
                String year = yearComboBox.getSelectedItem().toString();
                currentYear = Integer.parseInt(year);
                refreshCalendar(currentMonth, currentYear);
            }

        }//end of the actionPerformed Method for the YearComboBoxListener Class
    }//end of the YearComboBoxListener Class

}// end of JavaCalendar Class
