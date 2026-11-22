import javax.swing.*;
import java.awt.*;
import java.lang.IllegalArgumentException;

public class AddEmployeePanel extends JPanel implements Refreshable {
    
    // Variables
    private static final String FULL_TIME = "Full-Time";
    private static final String PART_TIME = "Part-Time";
    
    private final MainFrame frame;
    private final EmployeeService es;
    private final DepartmentService ds;
    
    private final JTextField nameField     = new JTextField(20);
    private final JTextField ageField      = new JTextField(20);
    private final JTextField positionField = new JTextField(20);
    private final JComboBox<Department> departmentCombo = new JComboBox<>();
    private final JComboBox<String> typeCombo = new JComboBox<>(new String[]{FULL_TIME, PART_TIME});
    
    
    private final JLabel bonusLabel = new JLabel("Bonus (EGP):");
    private final JTextField bonusField = new JTextField(20);     
    private final JLabel hoursLabel = new JLabel("Hours Worked:");
    private final JTextField hoursField = new JTextField(20);     
    private final JLabel rateLabel = new JLabel(" ");         

    private final JButton saveButton = new JButton("Save");
    private final JButton clearButton = new JButton("Clear");
    private final JButton backButton = new JButton("Back to Home");
    
    public AddEmployeePanel(MainFrame frame, EmployeeService es, DepartmentService ds)
    {
        this.frame = frame;
        this.es = es;
        this.ds = ds;
        initUI();
        refresh();
    }
    
    private void initUI()
    {
        setLayout(new BorderLayout(10, 20));
        setBackground(Theme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title (top)
        JLabel title = new JLabel("Add Employee", SwingConstants.CENTER);
        title.setFont(Theme.TITLE_FONT);
        title.setForeground(Theme.PRIMARY);
        add(title, BorderLayout.NORTH);

        // Form (center)
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Theme.SURFACE);
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);  //spaces between sides
        gbc.anchor = GridBagConstraints.WEST; //placed at left
        gbc.fill = GridBagConstraints.HORIZONTAL; //expand horizontal
        
        int row = 0;
        addRow(form, gbc, row++, new JLabel("Full Name:"), nameField);
        addRow(form, gbc, row++, new JLabel("Age:"), ageField);
        addRow(form, gbc, row++, new JLabel("Department:"), departmentCombo);
        addRow(form, gbc, row++, new JLabel("Position:"), positionField);
        addRow(form, gbc, row++, new JLabel("Employee Type:"), typeCombo);
        addRow(form, gbc, row++, bonusLabel, bonusField);
        addRow(form, gbc, row++, hoursLabel, hoursField);
        addRow(form, gbc, row++, new JLabel("Department Rate:"), rateLabel);
        
        // Style every label and field
        for (Component c : form.getComponents()) {
            if (c instanceof JLabel) {
                c.setFont(Theme.LABEL_FONT);
                c.setForeground(Theme.TEXT);
            } else {
                c.setFont(Theme.BODY_FONT);
            }
        }
        rateLabel.setFont(Theme.BODY_FONT);
        rateLabel.setForeground(Theme.PRIMARY);

        add(form, BorderLayout.CENTER);
        
        // Buttons (bottom)
        Theme.styleButton(saveButton);
        Theme.styleButton(clearButton);
        Theme.styleButton(backButton);

        JPanel buttons = new JPanel(new GridLayout(1, 3, 10, 10));
        buttons.setOpaque(false);
        buttons.add(saveButton);
        buttons.add(clearButton);
        buttons.add(backButton);
        
        add(buttons, BorderLayout.SOUTH);
        
        // Events
        typeCombo.addActionListener(e -> onTypeChanged());
        departmentCombo.addActionListener(e -> updateRateLabel());
        saveButton.addActionListener(e -> onSave());
        clearButton.addActionListener(e -> clearForm());
        backButton.addActionListener(e -> frame.showPanel(MainFrame.HOME));

        onTypeChanged();
        
    }
    
     // Save button
    private void onSave()
    {
        try {
            Employee emp = buildEmployeeFromForm();
            es.addEmployee(emp);
            JOptionPane.showMessageDialog(this, "Employee added successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
        } 
        catch (ValidationException | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Invalid input", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Empties the form
    private void clearForm()
    {
        nameField.setText("");
        ageField.setText("");
        positionField.setText("");
        bonusField.setText("");
        hoursField.setText("");
        typeCombo.setSelectedIndex(0);
        if (departmentCombo.getItemCount() > 0) departmentCombo.setSelectedIndex(0);
        onTypeChanged();
    }
    
    private void updateRateLabel()
    {
        Department d = (Department) departmentCombo.getSelectedItem();
        if (d == null) {
            rateLabel.setText(" ");
            return;
        }
        if (FULL_TIME.equals(typeCombo.getSelectedItem())) {
            rateLabel.setText(String.format("Monthly salary: %.2f EGP", ds.getRates().getFullTimeSalary(d)));
        } else {
            rateLabel.setText(String.format("Hourly rate: %.2f EGP", ds.getRates().getHourlyRate(d)));
        }
    }
    
    private void onTypeChanged()
    {
        boolean fullTime = FULL_TIME.equals(typeCombo.getSelectedItem());
        bonusLabel.setVisible(fullTime);
        bonusField.setVisible(fullTime);
        hoursLabel.setVisible(!fullTime);
        hoursField.setVisible(!fullTime);
        updateRateLabel();
        revalidate();
        repaint();
    }
    
    private void addRow(JPanel form, GridBagConstraints gbc, int row, JComponent label, JComponent field)
    {
        // Label
        gbc.gridy = row; // row
        gbc.gridx = 0; // column
        gbc.weightx = 0; // horizontal space 
        
        // Input Field
        form.add(label, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        form.add(field, gbc);
    }
    
    // Called by MainFrame each time the panel is shown: reloads departments
    @Override
    public void refresh()
    {
        Department previous = (Department) departmentCombo.getSelectedItem();
        departmentCombo.removeAllItems();
        for (Department d : ds.getAll()) {
            departmentCombo.addItem(d);
        }
        if (previous != null) departmentCombo.setSelectedItem(previous);  // uses Department.equals (by id)
        updateRateLabel();
    }
    
    private Employee buildEmployeeFromForm() throws ValidationException
    {
        String name = nameField.getText().trim();
        String position = positionField.getText().trim();
        String type = (String) typeCombo.getSelectedItem();
        Department department = (Department) departmentCombo.getSelectedItem();

        Validator.requireNotEmpty(name, "Full Name");
        int age = Validator.parseInt(ageField.getText().trim(), "Age");
        Validator.validateAge(age);
        Validator.validateDepartment(department, ds.getAll());
        Validator.requireNotEmpty(position, "Position");
        Validator.validateType(type);

        if (FULL_TIME.equals(type)) {
            double bonus = Validator.parseDouble(bonusField.getText().trim(), "Bonus");
            Validator.validateNonNegative(bonus, "Bonus");
            return new FullTimeEmployee(name, age, department, position, bonus);
        } else {
            double hours = Validator.parseDouble(hoursField.getText().trim(), "Hours Worked");
            Validator.validateNonNegative(hours, "Hours Worked");
            return new PartTimeEmployee(name, age, department, position, hours);
        }
    }
    
}
