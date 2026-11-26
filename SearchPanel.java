package com.mycompany.employeemanagmentsystem;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class SearchPanel extends JPanel implements Refreshable {

    private static final int NONE = 0;
    private static final int BY_ID = 1;
    private static final int BY_DEPARTMENT = 2;

    private MainFrame frame;
    private EmployeeService es;
    private JComboBox<String> searchBy;
    private JLabel queryLabel;
    private JTextField queryField;
    private DefaultTableModel tableModel;
    private JTable resultTable;
    private JButton searchButton, clearButton, backButton;

    public SearchPanel(MainFrame frame, EmployeeService es) {
        this.frame = frame;
        this.es = es;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Theme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Search Employee", SwingConstants.CENTER);
        title.setFont(Theme.TITLE_FONT);
        title.setForeground(Theme.PRIMARY);

        //components
         searchBy = new JComboBox<>(new String[]{
            "-- Choose search type --", "Employee ID", "Department"});
        queryLabel = new JLabel("Choose a search type first");
        queryField = new JTextField(20);
        searchButton = new JButton("Search");
        clearButton = new JButton("Clear");
        backButton = new JButton("Back to Home");

        String[] columns = {"ID", "Name", "Age", "Department", "Position", "Type", "Salary"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;   
            }
        };
        resultTable = new JTable(tableModel);
        
        //search bar 
        searchBy.setFont(Theme.BODY_FONT);
        queryField.setFont(Theme.BODY_FONT);
        queryLabel.setFont(Theme.LABEL_FONT);
        queryLabel.setForeground(Theme.TEXT);
        Theme.styleButton(searchButton);
        Theme.styleButton(clearButton);
        clearButton.setBackground(Theme.PRIMARY);
        
        JLabel byLabel = new JLabel("Search by:");
        byLabel.setFont(Theme.LABEL_FONT);
        byLabel.setForeground(Theme.TEXT);

        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        searchBar.setOpaque(false);
        searchBar.add(byLabel);
        searchBar.add(searchBy);
        searchBar.add(queryLabel);
        searchBar.add(queryField);
        searchBar.add(searchButton);
        searchBar.add(clearButton);

        JPanel top = new JPanel(new BorderLayout(10, 10));
        top.setOpaque(false);
        top.add(title, BorderLayout.NORTH);
        top.add(searchBar, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        //result table
        Theme.styleTable(resultTable);
        JScrollPane scroll = new JScrollPane(resultTable);
        scroll.getViewport().setBackground(Theme.SURFACE);
        add(scroll, BorderLayout.CENTER);

        //back bottom
        Theme.styleButton(backButton);
        backButton.setBackground(Theme.PRIMARY);
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.setOpaque(false);
        bottom.add(backButton);
        add(bottom, BorderLayout.SOUTH);

        //events
        searchBy.addActionListener(e -> onSearchTypeChanged());
        searchButton.addActionListener(e -> onSearch());
        queryField.addActionListener(e -> onSearch());
        clearButton.addActionListener(e -> refresh());
        backButton.addActionListener(e -> frame.showPanel(MainFrame.HOME));
        onSearchTypeChanged(); //start with search disabled until user choose the type

    }

    public void onSearch() {
        int type = searchBy.getSelectedIndex();
        if (type == NONE) {
            JOptionPane.showMessageDialog(this,
                    "Please choose a search type first.",
                    "Search", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String query = queryField.getText().trim();//trim spaces at the start and end
        try {
            
            Validator.validateSearchQuery(query);//check the query is nit empty
          
            List<Employee> results;
            if (type == BY_ID) {
                int id = Validator.parseInt(query, "Employee ID");
                Employee found = es.searchById(id);
                if (found == null) {
                    results = new ArrayList<>();//empty list
                } else {
                    results = Collections.singletonList(found);//list containing one item for the search by id
                }
            } else {
                results = es.searchByDepartment(query);
            }

            loadTable(results);

            if (results.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No matching employees found.",
                        "Search", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (ValidationException ex) {

            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Invalid Input", JOptionPane.WARNING_MESSAGE);

        }

    }

    private void onSearchTypeChanged() {
        int type = searchBy.getSelectedIndex();
        boolean chosen = (type != NONE);

        queryField.setEnabled(chosen);
        searchButton.setEnabled(chosen);
        queryField.setText("");

        switch (type) {
            case BY_ID:
                queryLabel.setText("Enter employee ID:");
                break;
            case BY_DEPARTMENT:
                queryLabel.setText("Enter department name:");
                break;
            default:
                queryLabel.setText("Choose a search type first");
                break;
        }
        if (chosen) {
            queryField.requestFocusInWindow();
        }
    }

    private void loadTable(List<Employee> list) {
        tableModel.setRowCount(0);
        for (Employee e : list) {
            tableModel.addRow(new Object[]{
                e.getId(),
                e.getName(),
                e.getAge(),
                e.getDepartment().getName(),
                e.getPosition(),
                e.getType(),
                String.format("%.2f EGP", es.getSalary(e))
            });
        }
    }

    @Override
    public void refresh() {
        searchBy.setSelectedIndex(NONE);   //also clears and disables the query field
        tableModel.setRowCount(0);
    }

}
