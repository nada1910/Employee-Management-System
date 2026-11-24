package com.mycompany.employeemanagmentsystem;

import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame {

    
    public static final String LOGIN = "LOGIN";
    public static final String HOME = "HOME";
    public static final String ADD = "ADD";
    public static final String VIEW = "VIEW";
    public static final String SEARCH = "SEARCH";
    public static final String DEPT = "DEPARTMENT";
    public static final String SALARY = "SALARY";
    public static final String MANUAL = "MANUAL";
    public static final String ABOUT = "ABOUT";

    
    private CardLayout cards;        // the layout manager that stacks the panels
    private JPanel container;  //where the panel lives
   

    private final EmployeeService es;
    //private final DepartmentService ds;
    private final Manager manager;

    public MainFrame(EmployeeService es, /*DepartmentService ds,*/ Manager manager) {

        this.es = es;
        //this.ds = ds;
        this.manager = manager; //stored to pass to other panels

        setTitle("Employee Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);

        cards = new CardLayout(); //stack panels on top of each other and shows one at a time
        container = new JPanel(cards);
        container.setBackground(Theme.BACKGROUND);
        buildPanels();//create every screen
        add(container);//put the container inside the frame

        showPanel(LOGIN);

    }

    private void buildPanels() {
        
        addCard(new LoginPanel(this, manager),         LOGIN);
        addCard(new HomePanel(this),                   HOME);
        //addCard(new AddEmployeePanel(this, es, ds),    ADD);
        //addCard(new ViewEmployeesPanel(this, es, ds),  VIEW);
        //addCard(new SearchPanel(this, es),             SEARCH);
        //addCard(new DepartmentManagePanel(this, ds),   DEPT);
        //addCard(new SalaryInfoPanel(this, ds),         SALARY);
        addCard(new UserManualPanel(this),             MANUAL);
        //addCard(new AboutAuthorPanel(this),            ABOUT);
    }
    //add a panel to the card layout
     private void addCard(JPanel panel, String name) {
        panel.setName(name);
        container.add(panel, name);
    }

    public void showPanel(String name) {
        //first we make sure that we are logged in to access anything 
        if (!name.equals(LOGIN) && !manager.isLoggedIn()) {
            name = LOGIN;
        }

        //find the panel and refresh it
        for (java.awt.Component c : container.getComponents()) {
            if (name.equals(c.getName()) && c instanceof Refreshable) {
                ((Refreshable) c).refresh();
            }
        }
        cards.show(container, name);
    }

    public void logout() {
        manager.logout();
        showPanel(LOGIN);
    }
    
}
