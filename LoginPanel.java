package com.mycompany.employeemanagmentsystem;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.net.URL;

public class LoginPanel extends JPanel implements Refreshable {

    private JTextField userField;
    private JPasswordField passField;
    private JButton loginButton, exitButton;
    private Manager manager;
    private MainFrame frame;

    //monster gui
    private enum Mood {
        IDLE, ANGRY, HAPPY
    }

    private ImageIcon idleGif, angryGif, happyGif;
    private Mood mood = Mood.IDLE;
    private MonsterPanel monsterPanel;

    public LoginPanel(MainFrame frame, Manager manager) {
        this.frame = frame;
        this.manager = manager;
        initUI();
    }

    private void onLogin() {
        String user = userField.getText().trim(); //gets username as string
        String pass = new String(passField.getPassword()); //gets passwrod as array of char and 
        //converts it to string
        try {
            Validator.validateCredentials(user, pass);
            if (manager.login(user, pass)) {
                setMood(Mood.HAPPY);
                Timer t = new Timer(800, e -> frame.showPanel(MainFrame.HOME));
                t.setRepeats(false);
                t.start();
            } else {
                setMood(Mood.ANGRY);
                JOptionPane.showMessageDialog(this,
                        "Incorrect username or password.",
                        "Login Failed", JOptionPane.ERROR_MESSAGE);
                passField.setText(""); // remove the passwrod from memory
                resetMoodLater();

            }
        } catch (ValidationException ex) {
            setMood(Mood.ANGRY);
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Invalid Input", JOptionPane.WARNING_MESSAGE);
            resetMoodLater();
        }
    }

    private void onExit() {
        System.exit(0);
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(Theme.BACKGROUND);
        loadGifs();

        JLabel title = new JLabel("Employee Management System", SwingConstants.CENTER);
        title.setFont(Theme.TITLE_FONT);
        title.setForeground(Theme.PRIMARY);
        title.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));
        add(title, BorderLayout.NORTH);

        //fields that sets on the monster
        userField = new HintTextField("Username");
        passField = new HintPasswordField("Password");
        loginButton = new JButton("Login");
        exitButton = new JButton("Exit");

        Theme.styleButton(loginButton);
        Theme.styleButton(exitButton);
        exitButton.setBackground(Theme.PRIMARY);

        loginButton.addActionListener(e -> onLogin());
        exitButton.addActionListener(e -> onExit());
        passField.addActionListener(e -> onLogin());
        // Enter in password = login
        userField.addActionListener(e -> passField.requestFocusInWindow());
        //Enter in username->password field

        //monster panel
        monsterPanel = new MonsterPanel();
        monsterPanel.add(userField);
        monsterPanel.add(passField);
        monsterPanel.add(loginButton);
        monsterPanel.add(exitButton);
        add(monsterPanel, BorderLayout.CENTER);

    }
    //monster helper methods

    private void loadGifs() {
        idleGif = loadIcon("/images/monster_idle.gif");
        angryGif = loadIcon("/images/monster_angry.gif");
        happyGif = loadIcon("/images/monster_happy.gif");
    }

    private ImageIcon loadIcon(String path) {
        URL url = getClass().getResource(path);
        if (url == null) {
            System.err.println("Image not found: " + path);
            return null;   // the panel still works, just without the monster
        }
        return new ImageIcon(url);
    }

    private void setMood(Mood m) {
        mood = m;
        monsterPanel.repaint();
    }

    private void resetMoodLater() {
        Timer t = new Timer(1200, e -> setMood(Mood.IDLE));
        t.setRepeats(false);
        t.start();
    }

    private ImageIcon currentGif() {
        switch (mood) {
            case ANGRY:
                return angryGif;
            case HAPPY:
                return happyGif;
            default:
                return idleGif;
        }
    }

    //shpws the word password in the password field
    private static class HintPasswordField extends JPasswordField {

        private final String hint;

        HintPasswordField(String hint) {
            this.hint = hint;
            setMargin(new Insets(0, 8, 0, 8));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            paintHint(g, this, hint);
        }
    }
    //shows the word username in the username field

    private static class HintTextField extends JTextField {

        private final String hint;

        HintTextField(String hint) {
            this.hint = hint;
            setMargin(new Insets(0, 8, 0, 8));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            paintHint(g, this, hint);
        }
    }

    private static void paintHint(Graphics g, JTextComponent c, String hint) {
        if (c.getDocument().getLength() > 0) {
            return;
        }
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(Color.GRAY);
        g2.setFont(c.getFont());
        Insets in = c.getInsets();
        FontMetrics fm = g2.getFontMetrics();
        int y = (c.getHeight() - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(hint, in.left, y);
        g2.dispose();
    }

    private class MonsterPanel extends JPanel {

        // the sign position inside the original GIF
        private static final int SIGN_X = 124, SIGN_Y = 266, SIGN_W = 244, SIGN_H = 92;

        MonsterPanel() {
            super(null);          // absolute layout, we place things in doLayout()
            setOpaque(false);     // lilac of the parent shows through
        }

        private double scale() {
            return Math.min(getWidth(), getHeight()) / 512.0;//GIF =512x512
        }

        @Override
        public void doLayout() {
            double s = scale();
            int imgSize = (int) (512 * s);
            int ox = (getWidth() - imgSize) / 2;
            int oy = (getHeight() - imgSize) / 2;

            int pad = Math.max(4, (int) (6 * s));
            int gap = Math.max(3, (int) (5 * s));
            int x = ox + (int) (SIGN_X * s) + pad;
            int y = oy + (int) (SIGN_Y * s) + pad;
            int w = (int) (SIGN_W * s) - 2 * pad;
            int h = (int) (SIGN_H * s) - 2 * pad;
            int rowH = (h - 2 * gap) / 3;

            userField.setBounds(x, y, w, rowH);
            passField.setBounds(x, y + rowH + gap, w, rowH);

            int btnW = (w - gap) / 2;
            int btnY = y + 2 * (rowH + gap);
            loginButton.setBounds(x, btnY, btnW, rowH);
            exitButton.setBounds(x + btnW + gap, btnY, btnW, rowH);

            // fonts follow the size so everything stays readable when resized
            float fs = Math.max(11f, rowH * 0.5f);
            userField.setFont(Theme.BODY_FONT.deriveFont(fs));
            passField.setFont(Theme.BODY_FONT.deriveFont(fs));
            loginButton.setFont(Theme.LABEL_FONT.deriveFont(fs));
            exitButton.setFont(Theme.LABEL_FONT.deriveFont(fs));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            ImageIcon gif = currentGif();
            if (gif == null) {
                return;
            }

            double s = scale();
            int size = (int) (512 * s);
            int ox = (getWidth() - size) / 2;
            int oy = (getHeight() - size) / 2;

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2.drawImage(gif.getImage(), ox, oy, size, size, this);
            g2.dispose();
        }

        @Override
        public boolean imageUpdate(Image img, int flags, int x, int y, int w, int h) {
            if ((flags & (FRAMEBITS | ALLBITS)) != 0) {
                repaint();
            }
            return (flags & ALLBITS) == 0;
        }
    }

    @Override
    public void refresh() {
        userField.setText("");
        passField.setText("");
        setMood(Mood.IDLE);
        userField.requestFocusInWindow();
    }
}
