import javax.swing.*;
import java.awt.*;

public class UserManualPanel extends JPanel {
    
    // Variables
    private final MainFrame frame;
    private final JTextArea manualText = new JTextArea();
    
    // Constructor
    public UserManualPanel(MainFrame frame)
    {
        this.frame = frame;
        initUI();
        loadManualText();
    }
    
    private void initUI()
    {
        setLayout(new BorderLayout(10, 20));
        setBackground(Theme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title (top)
        JLabel title = new JLabel("User Manual", SwingConstants.CENTER);
        title.setFont(Theme.TITLE_FONT);
        title.setForeground(Theme.PRIMARY);
        add(title, BorderLayout.NORTH);

        // Text area (center)
        manualText.setEditable(false);
        manualText.setLineWrap(true);
        manualText.setWrapStyleWord(true);
        manualText.setFont(Theme.BODY_FONT);
        manualText.setForeground(Theme.TEXT);
        manualText.setBackground(Theme.SURFACE);
        manualText.setMargin(new Insets(15, 15, 15, 15));

        JScrollPane scroll = new JScrollPane(manualText);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.PRIMARY));
        add(scroll, BorderLayout.CENTER);

        // Back button (bottom)
        JButton backButton = new JButton("Back to Home");
        Theme.styleButton(backButton);
        backButton.addActionListener(e -> frame.showPanel(MainFrame.HOME));

        JPanel bottom = new JPanel(new GridLayout(1, 1));
        bottom.setOpaque(false);
        bottom.add(backButton);
        add(bottom, BorderLayout.SOUTH);
    }

    // Fills the text area with short steps for every feature
    private void loadManualText()
    {
        String text =
            "1. Login\n" +
            "   Enter the username and password, then press Login.\n\n" +
            "2. Add Employee\n" +
            "   Open Add Employee, fill in name, age, department and position, " +
            "choose Full-Time or Part-Time, enter the bonus or hours worked, then press Save. " +
            "The employee ID is generated automatically.\n\n" +
            "3. View Employees\n" +
            "   Open View Employees to see all employees with their calculated salaries. " +
            "Use the department filter to narrow the list.\n\n" +
            "4. Update Employee\n" +
            "   Select a row in the table, edit the details in the form, then press Save Changes. " +
            "The ID and the employee type cannot be changed.\n\n" +
            "5. Delete Employee\n" +
            "   Select a row and press Delete, then confirm. Choosing No keeps the employee.\n\n" +
            "6. Search\n" +
            "   Choose Employee ID or Department, type the value, then press Search. " +
            "Department search ignores letter case.\n\n" +
            "7. Departments\n" +
            "   Add a new department with its monthly salary and hourly rate, or select a department " +
            "and press Update Rates. All its employees get the new rates immediately.\n\n" +
            "8. Salary Info\n" +
            "   Shows each department's rates and the formulas:\n" +
            "   Full-Time = department monthly salary + bonus\n" +
            "   Part-Time = department hourly rate x hours worked\n\n" +
            "9. Logout\n" +
            "   Press Logout on the home screen to return to the login screen.\n";

        manualText.setText(text);
        manualText.setCaretPosition(0);  
    }
}
