package com.mycompany.employeemanagmentsystem.gui;
import com.mycompany.employeemanagmentsystem.*;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;

public class ViewEmployeesPanel extends JPanel implements Refreshable {

    private EmployeeService es;
    private DepartmentService ds;

    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> filterCombo;

    private JTextField nameField;
    private JTextField ageField;
    private JTextField positionField;
    private JTextField bonusField;
    private JTextField hoursField;

    private JComboBox<Department> deptCombo;

    private JLabel bonusLabel;
    private JLabel hoursLabel;

    private JButton deleteButton;
    private JButton saveChangesButton;

    private int selectedEmployeeId = -1;

    public ViewEmployeesPanel(EmployeeService es, DepartmentService ds) {
        this.es = es;
        this.ds = ds;

        initUI();
        refresh();
    }

    private void initUI() {
        setLayout(new BorderLayout(15, 15));
        setBackground(Theme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel title = new JLabel("View Employees");
        title.setFont(Theme.TITLE_FONT);
        title.setForeground(Theme.TITLE_COLOR);
        JLabel filterLabel = new JLabel("Filter by Department");
        filterLabel.setFont(Theme.LABEL_FONT);
        filterLabel.setForeground(Theme.TEXT);
        filterCombo = new JComboBox<>();
        filterCombo.setFont(Theme.BODY_FONT);
        filterCombo.setBackground(Theme.SURFACE);
        filterCombo.setForeground(Theme.TEXT);
        filterCombo.addActionListener(e -> filterByDepartment());
        JPanel filterPanel = new JPanel(new BorderLayout(10, 5));
        filterPanel.setOpaque(false);
        filterPanel.add(filterLabel, BorderLayout.NORTH);
        filterPanel.add(filterCombo, BorderLayout.CENTER);
        JPanel topPanel = new JPanel(new BorderLayout(10, 15));
        topPanel.setOpaque(false);
        topPanel.add(title, BorderLayout.NORTH);
        topPanel.add(filterPanel, BorderLayout.SOUTH);
        String[] columns = {
            "ID", "Name", "Age", "Department",
            "Position", "Type", "Salary"
        };
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        Theme.styleTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.getSelectionModel().addListSelectionListener(
            this::onRowSelected
        );

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Theme.SURFACE);
        scrollPane.setBorder(BorderFactory.createLineBorder(
            new Color(210, 195, 230)
        ));
        JPanel editPanel = createEditPanel();
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(editPanel, BorderLayout.SOUTH);
    }

    private JPanel createEditPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Theme.SURFACE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 195, 230)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel heading = new JLabel("Edit Selected Employee");
        heading.setFont(Theme.LABEL_FONT);
        heading.setForeground(Theme.TITLE_COLOR);

        nameField = new JTextField(15);
        ageField = new JTextField(15);
        positionField = new JTextField(15);
        bonusField = new JTextField(15);
        hoursField = new JTextField(15);

        deptCombo = new JComboBox<>();
        deptCombo.setFont(Theme.BODY_FONT);

        bonusLabel = new JLabel("Bonus");
        hoursLabel = new JLabel("Hours Worked");

        JTextField[] fields = {
            nameField, ageField, positionField, bonusField, hoursField
        };

        for (JTextField field : fields) {
            field.setFont(Theme.BODY_FONT);
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        panel.add(heading, gbc);
        gbc.gridwidth = 1;
        addFormRow(panel, gbc, 1, "Full Name", nameField);
        addFormRow(panel, gbc, 2, "Age", ageField);
        addFormRow(panel, gbc, 3, "Department", deptCombo);
        addFormRow(panel, gbc, 4, "Position", positionField);
        addFormRow(panel, gbc, 5, bonusLabel, bonusField);
        addFormRow(panel, gbc, 6, hoursLabel, hoursField);

        saveChangesButton = new JButton("Save Changes");
        deleteButton = new JButton("Delete");

        Theme.styleButton(saveChangesButton);
        Theme.styleDangerButton(deleteButton);

        saveChangesButton.addActionListener(e -> onSaveChanges());
        deleteButton.addActionListener(e -> onDelete());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(saveChangesButton);
        buttonPanel.add(deleteButton);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 4;
        panel.add(buttonPanel, gbc);

        clearForm();

        return panel;
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc,
                            int row, String labelText, JComponent field) {
        JLabel label = new JLabel(labelText);
        label.setFont(Theme.LABEL_FONT);
        label.setForeground(Theme.TEXT);

        addFormRow(panel, gbc, row, label, field);
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc,
                            int row, JLabel label, JComponent field) {
        label.setFont(Theme.LABEL_FONT);
        label.setForeground(Theme.TEXT);

        gbc.gridy = row;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 3;
        gbc.weightx = 1;
        panel.add(field, gbc);
    }

    public void refresh() {
        filterCombo.removeAllItems();
        filterCombo.addItem("All Departments");

        for (Department d : ds.getAll()) {
            filterCombo.addItem(d.getName());
        }

        table.clearSelection();
        clearForm();
        loadTable(es.getAll());
    }

    private void loadTable(List<Employee> list) {
        tableModel.setRowCount(0);

        for (Employee e : list) {
            Object[] row = {
                e.getId(),
                e.getName(),
                e.getAge(),
                e.getDepartment().getName(),
                e.getPosition(),
                e.getType(),
                String.format("%.2f", es.getSalary(e))
            };

            tableModel.addRow(row);
        }
    }

    private void filterByDepartment() {
        String selected = (String) filterCombo.getSelectedItem();

        if (selected == null || selected.equals("All Departments")) {
            loadTable(es.getAll());
            return;
        }

        List<Employee> filtered = es.getAll();

        filtered.removeIf(e ->
            !e.getDepartment().getName().equals(selected)
        );

        table.clearSelection();
        clearForm();
        loadTable(filtered);
    }

    private void onRowSelected(ListSelectionEvent event) {
        if (event.getValueIsAdjusting()) {
            return;
        }

        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        int modelRow = table.convertRowIndexToModel(selectedRow);
        int id = (int) tableModel.getValueAt(modelRow, 0);

        Employee e = es.getById(id);

        if (e == null) {
            return;
        }

        selectedEmployeeId = e.getId();

        nameField.setText(e.getName());
        ageField.setText(String.valueOf(e.getAge()));
        positionField.setText(e.getPosition());
        deptCombo.setSelectedItem(e.getDepartment());

        if (e instanceof FullTimeEmployee) {
            FullTimeEmployee full = (FullTimeEmployee) e;

            bonusField.setText(String.valueOf(full.getBonus()));

            bonusLabel.setVisible(true);
            bonusField.setVisible(true);

            hoursLabel.setVisible(false);
            hoursField.setVisible(false);

        } else if (e instanceof PartTimeEmployee) {
            PartTimeEmployee part = (PartTimeEmployee) e;

            hoursField.setText(String.valueOf(part.getHoursWorked()));

            hoursLabel.setVisible(true);
            hoursField.setVisible(true);

            bonusLabel.setVisible(false);
            bonusField.setVisible(false);
        }

        saveChangesButton.setEnabled(true);
        deleteButton.setEnabled(true);
    }

    private void onSaveChanges() {
        if (selectedEmployeeId == -1) {
            JOptionPane.showMessageDialog(
                this, "Please select an employee first."
            );
            return;
        }

        try {
            Employee old = es.getById(selectedEmployeeId);

            if (old == null) {
                throw new ValidationException("Employee not found.");
            }

            String name = nameField.getText();
            int age = Integer.parseInt(ageField.getText().trim());
            String position = positionField.getText();
            Department department = (Department) deptCombo.getSelectedItem();

            Employee updated;

            if (old instanceof FullTimeEmployee) {
                double bonus = Double.parseDouble(bonusField.getText().trim());

                updated = new FullTimeEmployee(
                    old.getId(), name, age, department, position, bonus
                );

            } else {
                double hours = Double.parseDouble(hoursField.getText().trim());

                updated = new PartTimeEmployee(
                    old.getId(), name, age, department, position, hours
                );
            }

            es.updateEmployee(updated);

            JOptionPane.showMessageDialog(
                this, "Employee updated successfully!"
            );

            refresh();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                this, "Please enter valid numbers for Age, Bonus, and Hours Worked.",
                "Invalid Input", JOptionPane.ERROR_MESSAGE
            );

        } catch (ValidationException ex) {
            JOptionPane.showMessageDialog(
                this, ex.getMessage(),
                "Validation Error", JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void onDelete() {
        if (selectedEmployeeId == -1) {
            JOptionPane.showMessageDialog(
                this, "Please select an employee first."
            );
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this employee?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            es.deleteEmployee(selectedEmployeeId);

            JOptionPane.showMessageDialog(
                this, "Employee deleted successfully!"
            );

            refresh();

        } catch (ValidationException ex) {
            JOptionPane.showMessageDialog(
                this, ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void clearForm() {
        selectedEmployeeId = -1;

        nameField.setText("");
        ageField.setText("");
        positionField.setText("");
        bonusField.setText("");
        hoursField.setText("");

        if (deptCombo.getItemCount() > 0) {
            deptCombo.setSelectedIndex(0);
        }

        bonusLabel.setVisible(false);
        bonusField.setVisible(false);

        hoursLabel.setVisible(false);
        hoursField.setVisible(false);

        saveChangesButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }
}
