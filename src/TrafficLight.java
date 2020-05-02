import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class TrafficLight {

    final static int CIRCLE_DIAMETER = 75;
    static Icon[] icons = new Icon[4];

    static {
        icons[0] = new CurveIcon(Color.red, CIRCLE_DIAMETER);
        icons[1] = new CurveIcon(Color.yellow, CIRCLE_DIAMETER);
        icons[2] = new CurveIcon(Color.green, CIRCLE_DIAMETER);
        icons[3] = new CurveIcon(Color.darkGray, CIRCLE_DIAMETER);
    }


    boolean action = false;
    JPanel mainPanel, lightPanel;
    JFrame mainFrame;
    ButtonGroup groupConfiguration;
    ArrayList<JRadioButton> radioButtonList = new ArrayList<>(2);
    ArrayList<JLabel> circleList = new ArrayList<>(3);
    String[] configurationName = {"Normal", "Standby"};
    JButton startButton = new JButton("Start");


    public void buildGUI() {
        mainFrame = new JFrame("Traffic Light");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        BorderLayout layout = new BorderLayout();
        JPanel background = new JPanel(layout);
        background.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        
        Box buttonBox = new Box(BoxLayout.LINE_AXIS);

        startButton.addActionListener(event -> runLight());
        buttonBox.add(startButton);
        buttonBox.add(Box.createHorizontalStrut(10));

        JButton stopButton = new JButton("Stop");
        stopButton.addActionListener(event -> stopLight());
        buttonBox.add(stopButton);


        background.add(BorderLayout.SOUTH, buttonBox);


        mainFrame.getContentPane().add(background);

        GridLayout grid = new GridLayout(3, 3);
        grid.setVgap(1);
        grid.setHgap(2);
        mainPanel = new JPanel(grid);

        background.add(BorderLayout.WEST, mainPanel);
        groupConfiguration = new ButtonGroup();

        for (String configurationNameItem: configurationName) {
            JRadioButton radioButton = new JRadioButton(configurationNameItem);
            radioButton.setSelected(false);
            radioButtonList.add(radioButton);
            groupConfiguration.add(radioButton);
            mainPanel.add(radioButton);
        }

        lightPanel = new JPanel(grid);
        background.add(BorderLayout.CENTER, lightPanel);

        for (int i = 0; i < 3; i++) {
            JLabel circle = new JLabel(icons[3]);
            circleList.add(circle);
            lightPanel.add(circle);
        }

        mainFrame.setBounds(30, 30, 300, 300);
        mainFrame.pack();
        mainFrame.setVisible(true);
        mainFrame.setResizable(false);
    }

    public ArrayList getList() {
        return circleList;
    }

    private void runLight() {
        switch (showMode()) {
            case 1:
                action = true;
                runModeNormal();
                break;
            case 2:
                action = true;
                runModeBlink();
                break;
            default:
                action = false;
                JOptionPane.showMessageDialog(null, "Please select mode!");
                break;
        }
    }

    private void stopLight() {
        if (showMode() == 1) {
            JOptionPane.showMessageDialog(null, "Please wait traffic light");
        } else {
            elementsEnabled(true);
        }

        action = false;
    }

    private int showMode() {
        int mode = 0;


        for (int i = 0; i < radioButtonList.size(); i++) {
            JRadioButton radioButton = radioButtonList.get(i);

            if (radioButton.isSelected()) {
                mode = i + 1;
                break;
            }

        }

        return mode;
    }



    public void runModeNormal() {
        elementsEnabled(false);

        new Thread(() -> {
                while (true) {
                    try {
                        setColor(true, false,false);
                        Thread.sleep(6000);
                        setColor(false, true,false);
                        Thread.sleep(1800);
                        setColor(false, false,true);
                        Thread.sleep(4000);

                        if (!action) {
                            clearColor();
                            elementsEnabled(true);
                            break;
                        }
                        setColor(false, true,false);
                        Thread.sleep(1800);


                    } catch (InterruptedException ex) {

                    }
                }
        }).start();
        System.out.println("Mode 1");
    }

    public void elementsEnabled(boolean enabled) {
        SwingUtilities.invokeLater( () -> {
            startButton.setEnabled(enabled);

            for (JRadioButton radioButton : radioButtonList) {
                radioButton.setEnabled(enabled);
            }
        });
    }



    public void setColor (boolean red, boolean yellow,boolean green) {
        clearColor();
        SwingUtilities.invokeLater(() -> {
            if (red) circleList.get(0).setIcon(icons[0]);

            if (yellow) circleList.get(1).setIcon(icons[1]);

            if (green) circleList.get(2).setIcon(icons[2]);
        });
    }

    public void clearColor() {
        SwingUtilities.invokeLater(() -> {
            for (JLabel circles : circleList) {
                circles.setIcon(icons[3]);
            }
        });
    }

    public void runModeBlink() {
        elementsEnabled(false);

        new Thread(() -> {
                while (true) {
                    try {
                        setColor(false, true, false);
                        Thread.sleep(720);
                        setColor(false, false, false);
                        Thread.sleep(720);
                        if (!action) {
                            clearColor();
                            elementsEnabled(true);
                            break;
                        }
                    } catch (InterruptedException ex) {

                    }
                }

        }).start();
        System.out.println("Mode 2");
    }

    static class CurveIcon implements Icon {
        int width;
        int height;
        Color useColor;

        CurveIcon(Color c, int s) {
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
}
