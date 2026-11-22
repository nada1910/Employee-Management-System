import javax.swing.*;
import java.awt.*;

// Buttons, labels, and text field inside a main window.
public class HomePanel extends JPanel {
    
    // Variables
    private final MainFrame frame;
    private final JLabel welcomeLabel;
    
    // Constructor
    public HomePanel(MainFrame frame)
    {
        this.frame = frame;
        this.welcomeLabel = new JLabel("Welcome, admin", SwingConstants.CENTER);
        initUI();
    }
    
    // BUTTON HELPER
        private JButton createNavButton(String text, String panelName) {
        JButton button = new JButton(text);
        Theme.styleButton(button);  
        // When this button is clicked, execute panelName
        button.addActionListener(actionEvent  -> frame.showPanel(panelName));
        return button;
    }
    
    // Called from Constructor and builds the gui
    private void initUI()
    {
        setLayout(new BorderLayout(10,20)); //NORTH / CENTER / SOUTH
        setBackground(Theme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title (top) 
        welcomeLabel.setFont(Theme.TITLE_FONT);                      
        welcomeLabel.setForeground(Theme.PRIMARY);
        add(welcomeLabel, BorderLayout.NORTH);
        
        
        
        // Buttons (center)
        JPanel buttonGrid = new JPanel(new GridLayout(4, 2, 10, 10));
        buttonGrid.setOpaque(false);         

        buttonGrid.add(createNavButton("Add Employee",      MainFrame.ADD));
        buttonGrid.add(createNavButton("View Employees",    MainFrame.VIEW));
        buttonGrid.add(createNavButton("Search",            MainFrame.SEARCH));
        buttonGrid.add(createNavButton("Departments",       MainFrame.DEPT));
        buttonGrid.add(createNavButton("Salary Info",       MainFrame.SALARY));
        buttonGrid.add(createNavButton("User Manual",       MainFrame.MANUAL));
        buttonGrid.add(createNavButton("About the Authors", MainFrame.ABOUT));
        
        // ---- Logout button (the 8th button) ----
        JButton logoutButton = new JButton("Logout");
        Theme.styleDangerButton(logoutButton);                               
        logoutButton.addActionListener(actionEvent -> frame.logout());                
        buttonGrid.add(logoutButton);
        add(buttonGrid, BorderLayout.CENTER);
    }
}
    
    

