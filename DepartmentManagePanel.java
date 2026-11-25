import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DepartmentManagePanel extends JPanel implements Refreshable {
    private final MainFrame frame;
    private final DepartmentService ds;

    private DefaultTableModel tableModel;
    private JTable deptTable;
    private final JTextField nameField = new JTextField(20);
    private final JTextField monthlyField = new JTextField(20);
    private final JTextField hourlyField = new JTextField(20);

    private final JButton addButton = new JButton("Add Department");
    private final JButton updateButton = new JButton("Update Rates");
    private final JButton backButton = new JButton("Back to Home");

    public DepartmentManagePanel(MainFrame frame, DepartmentService ds) {
        this.frame = frame;
        this.ds = ds;
        initUI();
        refresh();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 20));
        setBackground(Theme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // title
        JLabel title = new JLabel("Department Management", SwingConstants.CENTER);
        title.setFont(Theme.TITLE_FONT);
        title.setForeground(Theme.PRIMARY);
        add(title, BorderLayout.NORTH);

        // table (read-only)
        String[] columns = {"ID", "Department", "Full-Time Monthly Salary", "Part-Time Hourly Rate"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        deptTable = new JTable(tableModel);
        deptTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Theme.styleTable(deptTable);

        JScrollPane scrollPane = new JScrollPane(deptTable);

        // form
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Theme.SURFACE);
        form.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        addRow(form, gbc, 0, new JLabel("Department Name:"), nameField);
        addRow(form, gbc, 1, new JLabel("Full-Time Monthly Salary (EGP):"), monthlyField);
        addRow(form, gbc, 2, new JLabel("Part-Time Hourly Rate (EGP):"), hourlyField);
        for (Component c : form.getComponents()) {
            if (c instanceof JLabel) {
                c.setFont(Theme.LABEL_FONT);
                c.setForeground(Theme.TEXT);
            } else {
                c.setFont(Theme.BODY_FONT);
            }
        }

        // center area: table above form
        JPanel center = new JPanel(new BorderLayout(10, 15));
        center.setOpaque(false);
        center.add(scrollPane, BorderLayout.CENTER);
        center.add(form, BorderLayout.SOUTH);
        add(center, BorderLayout.CENTER);

        // buttons
        Theme.styleButton(addButton);
        Theme.styleButton(updateButton);
        Theme.styleButton(backButton);
        JPanel buttons = new JPanel(new GridLayout(1, 3, 10, 10));
        buttons.setOpaque(false);
        buttons.add(addButton);
        buttons.add(updateButton);
        buttons.add(backButton);
        add(buttons, BorderLayout.SOUTH);

        // event listeners
        deptTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                onRowSelected();
            }
        });
        addButton.addActionListener(e -> onAddDepartment());
        updateButton.addActionListener(e -> onUpdateRates());
        backButton.addActionListener(e -> frame.showPanel(MainFrame.HOME));
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row, JLabel label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.0;
        panel.add(label, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(field, gbc);
    }

    @Override
    public void refresh() {
        tableModel.setRowCount(0);
        SalaryRates rates = ds.getRates();
        for (Department d : ds.getAll()) {
            tableModel.addRow(new Object[] {
                    d.getId(),
                    d.getName(),
                    String.format("%.2f EGP", rates.getFullTimeSalary(d)),
                    String.format("%.2f EGP", rates.getHourlyRate(d))
            });
        }
        deptTable.clearSelection();
        nameField.setText("");
        monthlyField.setText("");
        hourlyField.setText("");
    }

    private void onRowSelected() {
        int row = deptTable.getSelectedRow();
        if (row < 0) return;
        int id = (int) tableModel.getValueAt(row, 0);
        Department d = ds.findById(id);
        if (d != null) {
            SalaryRates rates = ds.getRates();
            nameField.setText(d.getName());
            monthlyField.setText(String.valueOf(rates.getFullTimeSalary(d)));
            hourlyField.setText(String.valueOf(rates.getHourlyRate(d)));
        }
    }

    private void onAddDepartment() {
        try {
            double monthly = Validator.parseDouble(monthlyField.getText(), "Full-Time Monthly Salary");
            double hourly = Validator.parseDouble(hourlyField.getText(), "Part-Time Hourly Rate");
            ds.addDepartment(nameField.getText(), monthly, hourly);
            refresh();
            JOptionPane.showMessageDialog(this, "Department added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (ValidationException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onUpdateRates() {
        try {
            int row = deptTable.getSelectedRow();
            Validator.validateSelection(row);
            int id = (int) tableModel.getValueAt(row, 0);
            Department selected = ds.findById(id);
            double monthly = Validator.parseDouble(monthlyField.getText(), "Full-Time Monthly Salary");
            double hourly = Validator.parseDouble(hourlyField.getText(), "Part-Time Hourly Rate");
            ds.updateRates(selected, monthly, hourly);
            refresh();
            JOptionPane.showMessageDialog(this, "Department rates updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (ValidationException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

}
