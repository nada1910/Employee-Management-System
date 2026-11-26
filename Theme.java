package com.mycompany.employeemanagmentsystem;

import java.awt.*;
import java.net.URL;
import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public class Theme {
    //colors and fonts

    public static final Color PRIMARY = new Color(126, 87, 194);   // #7E57C2 deep violet (buttons)
    public static final Color PRIMARY_HOVER = new Color(149, 117, 205); // #9575CD lighter violet (hover)
    public static final Color TITLE_COLOR = new Color(74, 44, 130);    // #4A2C82 dark purple (titles)
    public static final Color BACKGROUND = new Color(230, 220, 245);  // #E6DCF5 fallback if image fails
    public static final Color SURFACE = new Color(250, 247, 255);  // #FAF7FF near-white lavender (cards, text areas)
    public static final Color TEXT = new Color(59, 42, 90);     // #3B2A5A dark purple-gray (body text)
    public static final Color ACCENT = new Color(212, 175, 55);   // #D4AF37 gold, matches the stars
    public static final Color DANGER = new Color(214, 69, 101);   // #D64565 soft rose red (Exit, Delete)
    public static final Color SUCCESS = new Color(47, 133, 90);
    public static final Color WARNING = new Color(233, 162, 59);
    public static final Color SELECTION = new Color(220, 206, 242);

    public static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 32);
    public static final Font BODY_FONT = new Font("SansSerif", Font.PLAIN, 16);
    public static final Font LABEL_FONT = new Font("SansSerif", Font.BOLD, 16);
    //background image
    public static final Image BACKGROUND_IMG = loadImage("bg.jpg");

    private static Image loadImage(String fileName) {
        URL url = Theme.class.getResource("/" + fileName);
        if (url == null) {
            System.err.println("Image not found: " + fileName);
            return null;
        }
        return new ImageIcon(url).getImage();
    }

    private static class RoundedButtonUI extends BasicButtonUI {
        //the UI class on windows ignores setbackground and draws a rec buttons 
        //so we write our own UI class by extending basicbuttonui and styleButton
        //install it using b.setUI(new RoundedButtonUI())
        //swing calls paint(graphics   ....)everytime a button needs drawing 
        private static final int ARC = 16;
        //
        @Override
        //swing calls paint everytime a button needs drawing
        public void paint(Graphics g, JComponent c) {
            AbstractButton b = (AbstractButton) c;//c is the component being drawn
            //cast to AbstractButton so we can ask about its state
            ButtonModel model = b.getModel();//whether the button is pressed roll over
            //color depends on button state
            Color fill = b.getBackground();
            if (!b.isEnabled()) {
                fill = new Color(200, 200, 200);
            } else if (model.isPressed()) {
                fill = fill.darker();           
            } else if (model.isRollover()) {
                fill = lighten(fill);             
            }

            Graphics2D g2 = (Graphics2D) g.create();//makes copy of the drawnong tool
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setColor(fill);
            g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), ARC, ARC);//draws a rectangle with rounded corner

             if (b.isFocusOwner() && b.isEnabled()) {
                g2.setColor(TITLE_COLOR);
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(1, 1, c.getWidth() - 3, c.getHeight() - 3, ARC, ARC);
            }

            super.paint(g2, c);                  // draws the text on top of the shape
            g2.dispose();
        }

        //mixes white into the color
        private static Color lighten(Color col) { //static because it doesnt need a button
            return new Color(col.getRed() + (255 - col.getRed()) / 4,//distance to white over 4 takes 25% of it
                    col.getGreen() + (255 - col.getGreen()) / 4,
                    col.getBlue() + (255 - col.getBlue()) / 4);
        }


        @Override
        //where the text should go
        protected void paintText(Graphics g, AbstractButton b, Rectangle textRect, String text) {
            g.setFont(b.getFont());
            FontMetrics fm = g.getFontMetrics();
            g.setColor(b.isEnabled() ? b.getForeground() : new Color(120, 120, 120));
            //if enabled forground if not ->disabled grey
            g.drawString(text, textRect.x, textRect.y + fm.getAscent());
            //bt7aded el satr el 3aleh el kalam 34an mayb2ash fo2 awi

        BasicGraphicsUtils.drawStringUnderlineCharAt(g, text, b.getDisplayedMnemonicIndex(),
                    textRect.x, textRect.y + fm.getAscent());
        }
    }

    //draw background
    public static class BackgroundPanel extends JPanel {

        public BackgroundPanel() {
            setBackground(BACKGROUND); // shown when the image is missing
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (BACKGROUND_IMG == null) {
                return;
            }
            int iw = BACKGROUND_IMG.getWidth(this);
            int ih = BACKGROUND_IMG.getHeight(this);
            if (iw <= 0 || ih <= 0) {
                return;
            }//image not ready skip
            double scale = Math.max((double) getWidth() / iw, (double) getHeight() / ih);
            int w = (int) (iw * scale);
            int h = (int) (ih * scale);
            g.drawImage(BACKGROUND_IMG, (getWidth() - w) / 2, (getHeight() - h) / 2, w, h, this);
        }//scale to cover the panel
        //image is redrawn whenever the tab is resized
    }

    //buttons
    public static void styleButton(JButton b) {
        b.setUI(new RoundedButtonUI());//reset properties
        b.setRolloverEnabled(true);//to know when clicked
        b.setBackground(ACCENT);
        b.setForeground(TEXT);
        b.setFont(LABEL_FONT);
        b.setOpaque(false);
        b.setContentAreaFilled((false));
        b.setBorderPainted((false));
        b.setFocusPainted(false);//last 4 calls switch off default button so that the round edges show
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(160, 40));

    }

    public static void styleDangerButton(JButton b) {
        styleButton(b);
        b.setBackground(DANGER);
        b.setForeground(Color.WHITE);
    }

    public static void styleTable(JTable t) {
        t.setFont(BODY_FONT);
        t.setBackground(SURFACE);
        t.setForeground(TEXT);
        t.setRowHeight(28);
        t.setFillsViewportHeight(true); //fill rest with surface color
        t.setShowVerticalLines(false);
        t.setGridColor(new Color(225, 220, 235));
        //color of selected rows
        t.setSelectionBackground(SELECTION);
        t.setSelectionForeground(TEXT);

        JTableHeader header = t.getTableHeader();
        header.setReorderingAllowed(false);//makes user not able to drag header around
        header.setOpaque(true);
        header.setBackground(PRIMARY);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 34));
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override //same problem as buttons windows ignores header's background
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(PRIMARY);
                setForeground(Color.WHITE);
                setFont(LABEL_FONT);
                setHorizontalAlignment(LEFT);
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                return this;
            }
        });

    }
}
