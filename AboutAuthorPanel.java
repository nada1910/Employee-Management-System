import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AboutAuthorPanel extends JPanel {
    private final MainFrame frame;
    private JTable authorsTable;
    private final JButton backButton = new JButton("Back to Home");

    public AboutAuthorPanel(MainFrame frame) {
        this.frame = frame;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 20));
        setBackground(Theme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ritle
        JLabel title = new JLabel("About the Authors", SwingConstants.CENTER);
        title.setFont(Theme.TITLE_FONT);
        title.setForeground(Theme.PRIMARY);
        add(title, BorderLayout.NORTH);

        // read-only authors table
        String[] columns = {"Name"};
        Object[][] data = {
                {"Farah Elsaid"},
                {"Nada"},
                {"Amr"},
                {"Farah Ahmed"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        authorsTable = new JTable(model);
        Theme.styleTable(authorsTable);

        add(new JScrollPane(authorsTable), BorderLayout.CENTER);

        // back button
        Theme.styleButton(backButton);
        backButton.addActionListener(e -> frame.showPanel(MainFrame.HOME));
        add(backButton, BorderLayout.SOUTH);
    }
}