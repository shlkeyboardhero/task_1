import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class TrafficLight {

    final static int SIZE = 75;
    static Icon[] icons = new Icon[4];

    static {
        icons[0] = new curveIcon(Color.red, SIZE);
        icons[1] = new curveIcon(Color.yellow, SIZE);
        icons[2] = new curveIcon(Color.GREEN, SIZE);
        icons[3] = new curveIcon(Color.darkGray, SIZE);
    }

    boolean action = false;
    JPanel mainPanel, lightpanel;
    JFrame theFrame;
    ButtonGroup groupConfiguration;
    ArrayList<JRadioButton> radiobuttonList = new ArrayList<>(2);
    ArrayList<JLabel> circlelists = new ArrayList<>(3);
    String[] configurationName = {"Normal", "Standby"};
    JButton start = new JButton("Start");

    public void buildGUI() {
        theFrame = new JFrame("Traffic Light");
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        BorderLayout layout = new BorderLayout();
        JPanel background = new JPanel(layout);
        background.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        /**
         * Реализация расположения кнопок (Старт, Стоп)!
         */
        Box buttonBox = new Box(BoxLayout.LINE_AXIS);


        start.addActionListener(new MyStartLight());
        buttonBox.add(start);
        buttonBox.add(Box.createHorizontalStrut(10));

        JButton stop = new JButton("Stop");
        stop.addActionListener(new MyStopLight());
        buttonBox.add(stop);
        /**
         * Закончена реализация
         */

        background.add(BorderLayout.SOUTH, buttonBox);


        theFrame.getContentPane().add(background);

        GridLayout grid = new GridLayout(3, 3);
        grid.setVgap(1);
        grid.setHgap(2);
        mainPanel = new JPanel(grid);

        background.add(BorderLayout.WEST, mainPanel);
        groupConfiguration = new ButtonGroup();

        for (int i = 0; i < configurationName.length; i++) {
            JRadioButton c = new JRadioButton(configurationName[i]);
            c.setSelected(false);
            radiobuttonList.add(c);
            groupConfiguration.add(c);
            mainPanel.add(c);
        }

        lightpanel = new JPanel(grid);
        background.add(BorderLayout.CENTER, lightpanel);

        for (int i = 0; i < 3; i++) {
            JLabel circle = new JLabel(icons[3]);
            circlelists.add(circle);
            lightpanel.add(circle);
        }

        theFrame.setBounds(30, 30, 300, 300);
        theFrame.pack();
        theFrame.setVisible(true);
        theFrame.setResizable(false);

    }

    public ArrayList getList() {
        return circlelists;
    }

    private void runLight() {
        switch (ShowMode()) {
            case 1:
                action = true;
                runLightMode1();
                break;
            case 2:
                action = true;
                runLightMode2();
                break;
            default:
                action = false;
                JOptionPane.showMessageDialog(null, "Please select mode!");
                break;
        }
    }

    private void stopLight() {
        if (ShowMode() == 1) {
            JOptionPane.showMessageDialog(null, "Please wait traffic light");
        } else {
            ElementsEnabled(true);
        }

        action = false;
    }

    private int ShowMode() {
        int mode = 0;

        for (int i = 0; i < configurationName.length; i++) {
            JRadioButton c = radiobuttonList.get(i);

            if (c.isSelected()) {
                mode = i + 1;
                break;
            }

        }

        return mode;
    }

    public void runLightMode1() {
        ElementsEnabled(false);

        Thread t = new Thread() {
            public void run() {
                while (true) {
                    try {
                        ArrayList lst = getList();
                        curveIcon cI1 = (curveIcon) ((JLabel) lst.get(0)).getIcon();
                        curveIcon cI2 = (curveIcon) ((JLabel) lst.get(1)).getIcon();
                        curveIcon cI3 = (curveIcon) ((JLabel) lst.get(2)).getIcon();

                        if (cI1.getColor().equals(Color.darkGray))
                            ((JLabel) lst.get(0)).setIcon(icons[0]);
                        ((JLabel) lst.get(1)).setIcon(icons[3]);
                        ((JLabel) lst.get(2)).setIcon(icons[3]);
                        sleep(6000);
                        if (cI2.getColor().equals(Color.darkGray))
                            ((JLabel) lst.get(0)).setIcon(icons[3]);
                        ((JLabel) lst.get(1)).setIcon(icons[1]);
                        ((JLabel) lst.get(2)).setIcon(icons[3]);
                        sleep(1800);
                        if (cI3.getColor().equals(Color.darkGray))
                            ((JLabel) lst.get(0)).setIcon(icons[3]);
                        ((JLabel) lst.get(1)).setIcon(icons[3]);
                        ((JLabel) lst.get(2)).setIcon(icons[2]);
                        sleep(4000);
                        if (!action) {
                            for (int i = 0; i < 3; i++) {
                                ((JLabel) lst.get(i)).setIcon(icons[3]);
                            }
                            ElementsEnabled(true);
                            this.interrupt();
                            break;
                        }
                    } catch (InterruptedException ex) {

                    }
                }
            }
        };
        t.start();
        System.out.println("Mode 1");
    }

    public void ElementsEnabled(boolean t) {
        start.setEnabled(t);

        for (int i = 0; i < 2; i++) {
            JRadioButton c = radiobuttonList.get(i);
            c.setEnabled(t);

            if (t) {
                c.setSelected(t);
            }
        }
    }

    public void runLightMode2() {
        ElementsEnabled(false);

        Thread t = new Thread() {
            public void run() {
                while (true) {
                    try {
                        sleep(720);
                        ArrayList lst = getList();
                        curveIcon cI2 = (curveIcon) ((JLabel) lst.get(1)).getIcon();
                        ((JLabel) lst.get(0)).setIcon(icons[3]);
                        ((JLabel) lst.get(2)).setIcon(icons[3]);
                        if (cI2.getColor().equals(Color.darkGray))
                            ((JLabel) lst.get(1)).setIcon(icons[1]);
                        else
                            ((JLabel) lst.get(1)).setIcon(icons[3]);
                        if (!action) {
                            for (int i = 0; i < 3; i++) {
                                ((JLabel) lst.get(i)).setIcon(icons[3]);
                            }
                            this.interrupt();
                            break;
                        }
                    } catch (InterruptedException ex) {

                    }
                }
            }

        };
        t.start();
        System.out.println("Mode 2");
    }

    static class curveIcon implements Icon {
        int width;
        int height;
        Color useColor;

        curveIcon(Color c, int s) {
            this.useColor = c;
            this.width = s;
            this.height = s;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(useColor);
            g.drawOval(x, y, width, height);
            g.setColor(useColor);
            g.fillOval(x, y, width, height);
        }

        public int getIconWidth() {
            return width;
        }

        public int getIconHeight() {
            return height;
        }

        public Color getColor() {
            return useColor;
        }
    }

    public class MyStartLight implements ActionListener {
        public void actionPerformed(ActionEvent a) {
            runLight();
        }
    }

    public class MyStopLight implements ActionListener {
        public void actionPerformed(ActionEvent a) {
            stopLight();
        }
    }
}
