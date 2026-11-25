import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SalaryInfoPanel extends JPanel implements Refreshable {
    private final MainFrame frame;
    private final DepartmentService ds;

    private DefaultTableModel tableModel;
    private JTable ratesTable;
    private JTextArea formulasText;
    private final JButton backButton = new JButton("Back to Home");

    public SalaryInfoPanel(MainFrame frame, DepartmentService ds) {
        this.frame = frame;
        this.ds = ds;
        initUI();
        loadInfo();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 20));
        setBackground(Theme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // title
        JLabel title = new JLabel("Salary Information", SwingConstants.CENTER);
        title.setFont(Theme.TITLE_FONT);
        title.setForeground(Theme.PRIMARY);
        add(title, BorderLayout.NORTH);

        // read-only department rates table
        String[] columns = { "ID", "Department", "Full-Time Monthly Salary", "Part-Time Hourly Rate" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        ratesTable = new JTable(tableModel);
        Theme.styleTable(ratesTable);
        JScrollPane tableScroll = new JScrollPane(ratesTable);

        // read-only salary
        formulasText = new JTextArea(6, 40);
        formulasText.setEditable(false);
        formulasText.setLineWrap(true);
        formulasText.setWrapStyleWord(true);
        formulasText.setFont(Theme.BODY_FONT);
        formulasText.setForeground(Theme.TEXT);
        formulasText.setBackground(Theme.SURFACE);
        formulasText.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JScrollPane textScroll = new JScrollPane(formulasText);

        JPanel center = new JPanel(new BorderLayout(10, 15));
        center.setOpaque(false);
        center.add(tableScroll, BorderLayout.CENTER);
        center.add(textScroll, BorderLayout.SOUTH);
        add(center, BorderLayout.CENTER);

        // back button
        Theme.styleButton(backButton);
        backButton.addActionListener(e -> frame.showPanel(MainFrame.HOME));
        add(backButton, BorderLayout.SOUTH);
    }

    private void loadInfo() {
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

        formulasText.setText(
                "Salary Calculation Formulas:\n\n" +
                        "• Full-Time Employee:\n" +
                        "   Monthly Salary = Department Full-Time Monthly Salary + Monthly Bonus\n\n" +
                        "• Part-Time Employee:\n" +
                        "   Monthly Salary = Department Part-Time Hourly Rate × Hours Worked"
        );
    }

    @Override
    public void refresh() {
        loadInfo();
    }
}