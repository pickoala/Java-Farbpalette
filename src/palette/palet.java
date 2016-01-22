import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class palet extends JFrame {

    private static final long serialVersionUID = 1L;
    public String location = "location.txt";
    public String colors = "colors.txt";
    JButton changer = new JButton();

    int aktuell = 60;
    int bounds = 8;
    int boundsX = 108;
    int hoeheFarb = 40;
    int abs = 20;
    int anz = 0;

    String newColor;
    private JLabel jLabelPalet = new JLabel();
    private String AIO = new String("");
    private JButton jButton1 = new JButton();

    public palet(String title) {

        super(title);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        int frameWidth = 300;
        int frameHeight = 700;
        setSize(frameWidth, frameHeight);

        try {

            InputStream ips = new FileInputStream(location);
            InputStreamReader ipsr = new InputStreamReader(ips);
            BufferedReader br = new BufferedReader(ipsr);
            String line = br.readLine();
            System.out.println("Fenster geöffnet bei " + line);

            String[] locA;
            locA = line.split(",");
            setLocation(Integer.parseInt(locA[0]), Integer.parseInt(locA[1]));

            br.close();

        } catch (Exception e) {

            System.out.println(e.toString());

        }

        setResizable(false);
        Container cp = getContentPane();
        cp.setLayout(null);

        try {

            loadAllColors();

        } catch (IOException e) {

            e.printStackTrace();

        }

        jLabelPalet.setBounds(8, 8, 163, 39);
        jLabelPalet.setText("Palet");
        jLabelPalet.setFont(new Font("Dialog", Font.BOLD, 30));
        jLabelPalet.setForeground(new Color(0x1A237E));
        cp.add(jLabelPalet);

        addWindowListener(new WindowAdapter() {

            public void windowClosed(WindowEvent evt) {

                palet_WindowClosed(evt);

            }

        });

        jButton1.setBounds(100, 16, 105, 25);
        jButton1.setText("Neu...");
        jButton1.setMargin(new Insets(2, 2, 2, 2));
        jButton1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {

                colorCreator(getX() + jButton1.getX(), getY() + jButton1.getY() + jButton1.getHeight() * 2);

            }

        });

        cp.add(jButton1);

        setVisible(true);
    }

    public static void main(String[] args) {

        new palet("palet");

    }

    public void palet_WindowClosed(WindowEvent evt) {

        int x = getX();
        int y = getY();

        //speichere, wo das Fenster geschlossen wurde.

        try {

            FileWriter fw = new FileWriter(location);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter fileOut = new PrintWriter(bw);
            fileOut.println(x + "," + y);
            fileOut.close();
            System.out.println("Smashed the file " + location + " with " + x + "," + y);

        } catch (Exception e) {

            System.out.println(e.toString());

        }

    }

    public void loadAllColors() throws IOException {

        int count = 0;
        InputStream is = new BufferedInputStream(new FileInputStream(colors));

        try {

            InputStream ips = new FileInputStream(colors);
            InputStreamReader ipsr = new InputStreamReader(ips);
            BufferedReader br = new BufferedReader(ipsr);
            String line;

            while ((line = br.readLine()) != null) {

                AIO += line + "\n";
                ++count;

            }

            br.close();

        } catch (Exception e) {

            System.out.println(e.toString());

        } finally {

            is.close();

        }


        String[] mains = new String[count];
        mains = AIO.split("\n");

        for (int i = 0; i < mains.length; i++) {

            if (mains[i].contains("#")) {

                int ka = mains[i].length() - mains[i].replaceAll("\\,", "").length();
                String gruppenInhalt[] = new String[ka + 1];
                gruppenInhalt = mains[i].split("\\,");

                drawGroup("" + gruppenInhalt[0].replaceAll("\\#", ""), gruppenInhalt);

            } else {

                String update = new String("");
                update = mains[i].replaceAll("\\$,", "");
                drawColor(update);

            }

        }

    }

    public Color rgbColor(String colorStr) {

	  /*
       * Diese Funktion nimmt sechs-stellige Hex-Farbcodes an
	   * und gibt fuer Java nutzbare Farben zurueck.
	   *
	   */

        return new Color(

                Integer.valueOf(colorStr.substring(0, 2), 16),
                Integer.valueOf(colorStr.substring(2, 4), 16),
                Integer.valueOf(colorStr.substring(4, 6), 16)
        );

    }

    public void drawGroup(String name, String[] gruppenInhalt) {

        setVisible(false);

        Container cp = getContentPane();
        int size = 0;

        JLabel jName = new JLabel(name);
        jName.setBounds(bounds, aktuell, 160, 18);
        jName.setForeground(Color.BLACK);
        cp.add(jName);

        JPanel panel = new JPanel();
        panel.setBounds(boundsX, aktuell, 180, 5);
        panel.setBackground(new Color(0x222222));
        size += panel.getHeight();
        cp.add(panel);

        int anzahlColor = gruppenInhalt.length - 1;
        int breite = panel.getWidth() / anzahlColor;

        int farbGes = hoeheFarb;

        if (anzahlColor > 3) {

            if (anzahlColor > 6) {

                breite = panel.getWidth() / 3;

                for (int i = 0; i < 3; i++) {

                    JPanel farb = new JPanel();
                    farb.setBounds(boundsX + i * breite, aktuell + panel.getHeight(), breite, hoeheFarb);
                    farb.setBackground(rgbColor(gruppenInhalt[i + 1]));
                    farb.setToolTipText("#" + gruppenInhalt[i + 1]);
                    farb.addMouseListener(new java.awt.event.MouseAdapter() {

                        public void mouseClicked(MouseEvent evt) {

                            groupDetailWindow(gruppenInhalt, evt);

                        }

                    });

                    cp.add(farb);

                }

                for (int i = 3; i < 6; i++) {

                    JPanel farb = new JPanel();
                    farb.setBounds(boundsX + (i - 3) * breite, aktuell + panel.getHeight() + hoeheFarb, breite, hoeheFarb);
                    farb.setBackground(rgbColor(gruppenInhalt[i + 1]));
                    farb.setToolTipText("#" + gruppenInhalt[i + 1]);
                    farb.addMouseListener(new java.awt.event.MouseAdapter() {

                        public void mouseClicked(java.awt.event.MouseEvent evt) {

                            groupDetailWindow(gruppenInhalt, evt);

                        }

                    });

                    cp.add(farb);

                }

                JPanel more = new JPanel();
                more.setBounds(boundsX, aktuell + panel.getHeight() + hoeheFarb * 2, panel.getWidth(), 24);
                more.setBackground(new Color(0x777777));
                more.setToolTipText("" + (anzahlColor - 6) + " weitere.");
                cp.add(more);
                JLabel lMore = new JLabel("•••");
                more.setForeground(Color.WHITE);
                more.add(lMore);

                farbGes = hoeheFarb * 2 + more.getHeight();
                
            } else {

                breite = panel.getWidth() / 3;

                for (int i = 0; i < 3; i++) {

                    JPanel farb = new JPanel();
                    farb.setBounds(boundsX + i * breite, aktuell + panel.getHeight(), breite, hoeheFarb);
                    farb.setBackground(rgbColor(gruppenInhalt[i + 1]));
                    farb.setToolTipText("#" + gruppenInhalt[i + 1]);
                    farb.addMouseListener(new java.awt.event.MouseAdapter() {

                        public void mouseClicked(java.awt.event.MouseEvent evt) {

                            groupDetailWindow(gruppenInhalt, evt);

                        }

                    });

                    cp.add(farb);

                }

                breite = panel.getWidth() / (anzahlColor - 3);

                for (int i = 3; i < anzahlColor; i++) {

                    JPanel farb = new JPanel();
                    farb.setBounds(boundsX + (i - 3) * breite, aktuell + panel.getHeight() + hoeheFarb, breite, hoeheFarb);
                    farb.setBackground(rgbColor(gruppenInhalt[i + 1]));
                    farb.setToolTipText("#" + gruppenInhalt[i + 1]);
                    farb.addMouseListener(new java.awt.event.MouseAdapter() {

                        public void mouseClicked(java.awt.event.MouseEvent evt) {

                            groupDetailWindow(gruppenInhalt, evt);

                        }

                    });

                    cp.add(farb);

                }

                farbGes = hoeheFarb * 2;

            }

        } else {

            for (int i = 0; i < anzahlColor; i++) {

                JPanel farb = new JPanel();
                farb.setBounds(boundsX + i * breite, aktuell + panel.getHeight(), breite, hoeheFarb);
                farb.setBackground(rgbColor(gruppenInhalt[i + 1]));
                farb.setToolTipText("#" + gruppenInhalt[i + 1]);
                farb.addMouseListener(new java.awt.event.MouseAdapter() {

                    public void mouseClicked(java.awt.event.MouseEvent evt) {

                        groupDetailWindow(gruppenInhalt, evt);

                    }

                });

                cp.add(farb);

            }

        }

        size += farbGes;

        JPanel end = new JPanel();
        end.setBounds(boundsX, aktuell + panel.getHeight() + farbGes, 180, 5);
        end.setBackground(new Color(0x222222));
        cp.add(end);

        size += end.getHeight();

        size += abs;

        aktuell += size;

        setVisible(true);

    }

    public void drawColor(String col) {

        setVisible(false);

        Container cp = getContentPane();

        JPanel panel = new JPanel();
        panel.setBounds(boundsX, aktuell, 180, 5);
        panel.setBackground(new Color(0x222222));
        cp.add(panel);

        JPanel farb = new JPanel();
        farb.setBounds(boundsX, aktuell + 5, 180, hoeheFarb);
        farb.setBackground(rgbColor(col));
        farb.setToolTipText("#" + col);
        String activeColor = col;
        farb.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {

                colorDetailWindow(activeColor, evt);

            }

        });

        cp.add(farb);

        aktuell += hoeheFarb + panel.getHeight() + abs;

        setVisible(true);

    }

    public void colorDetailWindow(String color, java.awt.event.MouseEvent evt) {

        int fullBreite = 182;
        final int anzahl = anz;

        JFrame detailColor = new JFrame("Details");
        detailColor.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        detailColor.setSize(200, 225);
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        detailColor.setLocation(x, y);
        Container cp = detailColor.getContentPane();
        cp.setLayout(null);

        JLabel colorName = new JLabel("#" + color);
        colorName.setBounds(8, 8, fullBreite, 25);
        cp.add(colorName);

        JPanel colorReview = new JPanel();
        colorReview.setBounds(8, 41, fullBreite, 100);
        colorReview.setBackground(rgbColor(color));
        cp.add(colorReview);

        JTextField change = new JTextField();
        change.setBounds(8, 149, fullBreite, 25);
        change.setText(color);
        cp.add(change);

        changer = new JButton();
        changer.setText("Ersetzen");
        changer.setBounds(8, 174, fullBreite / 2, 25);
        changer.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {

                newColor = change.getText();
                String newAIO = AIO.replaceAll(color, newColor);
                System.out.println(color + " " + newColor);

                try {

                    FileWriter fw = new FileWriter(colors);
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter fileOut = new PrintWriter(bw);

                    int count = newAIO.split("\n").length;
                    String[] mains = new String[count];
                    mains = newAIO.split("\n");

                    for (int i = 0; i < count; i++) {

                        fileOut.println(mains[i]);
                        System.out.println(mains[i]);

                    }

                    fileOut.close();

                } catch (Exception e) {

                    System.out.println(e.toString());

                } finally {

                    System.out.println("Changed the colors.txt! " + color + " → " + newColor);

                    detailColor.dispose();
                    dispose();

                    new palet("palet");

                }


            }
        });

        cp.add(changer);

        JButton deletor = new JButton();
        deletor.setText("Delete");
        deletor.setBounds(fullBreite / 2 + 2, 174, fullBreite / 2, 25);
        deletor.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {

                String newAIO = AIO.replaceAll("," + color, "");
                System.out.println(color + " " + newColor);

                try {

                    FileWriter fw = new FileWriter(colors);
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter fileOut = new PrintWriter(bw);

                    int count = newAIO.split("\n").length;
                    String[] mains = new String[count];
                    mains = newAIO.split("\n");

                    for (int i = 0; i < count; i++) {

                        if (mains[i].contains(",")) {

                            fileOut.println(mains[i]);
                            System.out.println(mains[i]);

                        } else {

                            //nix

                        }

                    }

                    fileOut.close();

                } catch (Exception e) {

                    System.out.println(e.toString());

                } finally {

                    System.out.println("deleted: " + color);

                    detailColor.dispose();
                    dispose();

                    new palet("palet");
                }

            }

        });

        cp.add(deletor);

        System.out.println("Shit is working, dude: " + color);

        detailColor.setVisible(true);

    }

    public void colorCreator(int x, int y) {

        JFrame newColor = new JFrame();
        newColor.setLocation(x, y);
        Container cp = newColor.getContentPane();
        cp.setLayout(null);
        newColor.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        newColor.setSize(200, 250);

        JLabel groupName = new JLabel("Gruppe:");
        groupName.setBounds(8, 8, 182, 25);
        cp.add(groupName);

        JTextField groupInput = new JTextField();
        groupInput.setBounds(8, 33, 182, 25);
        cp.add(groupInput);

        JCheckBox ifGroup = new JCheckBox("Neue Einzelne Farbe");
        ifGroup.setBounds(8, 58, 182, 25);
        ifGroup.setSelected(true);
        cp.add(ifGroup);

        JLabel colorDeclare = new JLabel("Farbe:");
        colorDeclare.setBounds(8, 66 + 25, 182, 25);
        cp.add(colorDeclare);

        JTextField colorString = new JTextField();
        colorString.setBounds(8, 91 + 25, 182, 25);
        cp.add(colorString);

        JButton createColor = new JButton();
        createColor.setBounds(8, 124 + 25, 182, 25);
        createColor.setText("Erstellen");
        createColor.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {

                boolean allowed = true;

                try {

                    Color lol = rgbColor(colorString.getText());

                } catch (StringIndexOutOfBoundsException e) {

                    System.out.println(e);
                    allowed = false;

                } finally {

                    if (ifGroup.isSelected() && allowed == true) {

                        String newAIO = AIO;

                        try {

                            FileWriter fw = new FileWriter(colors);
                            BufferedWriter bw = new BufferedWriter(fw);
                            PrintWriter fileOut = new PrintWriter(bw);

                            int count = newAIO.split("\n").length;
                            String[] mains = new String[count];
                            mains = newAIO.split("\n");

                            for (int i = 0; i < count; i++) {

                                fileOut.println(mains[i]);
                                System.out.println(mains[i]);

                            }

                            fileOut.println("$," + colorString.getText());

                            fileOut.close();

                        } catch (Exception e) {

                            System.out.println(e.toString());

                        } finally {

                            System.out.println("created: " + colorString.getText());
                            newColor.dispose();
                            dispose();

                            new palet("palet");

                        }

                    } else {

                        String newGroupName = groupInput.getText();
                        String newColorInput = colorString.getText();
                        System.out.println("Gruppenname: " + newGroupName);

                        String mains[] = new String[AIO.split("\n").length];
                        mains = AIO.split("\n");

                        boolean used = false;
                        int theOne = 0;

                        for (int i = 0; i < mains.length; i++) {

                            if (mains[i].contains("asdfsg")) {

                                used = true;
                                theOne = i;

                            }

                        }

                        try {

                            FileWriter fw = new FileWriter(colors);
                            BufferedWriter bw = new BufferedWriter(fw);
                            PrintWriter fileOut = new PrintWriter(bw);

                            if (used == true) {

                                for (int i = 0; i < AIO.split("\n").length; i++) {

                                    if (i == theOne) {

                                        fileOut.println(mains[i] + "," + newColorInput);

                                    } else {

                                        fileOut.println(mains[i]);
                                    }

                                }

                            } else {

                                boolean injected = false;

                                for (int i = 0; i < AIO.split("\n").length + 1; i++) {

                                    if (injected == false) {

                                        System.out.println("false for now " + i);
                                        if (mains[i].contains("#")) {

                                            fileOut.println(mains[i]);

                                        } else {

                                            fileOut.println("#" + newGroupName + "," + newColorInput);
                                            injected = true;
                                        }

                                    } else {

                                        System.out.println("true for now " + i);
                                        fileOut.println(mains[i - 1]);

                                    }

                                }

                            }

                            fileOut.close();

                        } catch (Exception e) {

                            System.out.println(e.toString());

                        } finally {

                            System.out.println("created in " + newGroupName);
                            newColor.dispose();
                            dispose();

                            new palet("palet");

                        }

                    }

                }

            }

        });

        cp.add(createColor);

        newColor.setVisible(true);
    }

    public void groupDetailWindow(String[] gruppenInhalt, MouseEvent evt) {

        JFrame groupDetail = new JFrame();
        groupDetail.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        Container cp = groupDetail.getContentPane();
        cp.setLayout(null);
        groupDetail.setBounds(evt.getXOnScreen(), evt.getYOnScreen(), 220, gruppenInhalt.length * 50 + 75);

        JLabel name = new JLabel("" + gruppenInhalt[0].replaceAll("\\#", ""));
        name.setBounds(10, 10, 200, 30);
        cp.add(name);

        int situation = 40;

        for (int i = 1; i < gruppenInhalt.length; i++) {

            JPanel farb = new JPanel();
            farb.setBounds(10, situation, 200, hoeheFarb);
            farb.setBackground(rgbColor(gruppenInhalt[i]));
            farb.setToolTipText("#" + gruppenInhalt[i]);
            String activeColor = gruppenInhalt[i];
            farb.addMouseListener(new java.awt.event.MouseAdapter() {

                public void mouseClicked(java.awt.event.MouseEvent evt) {

                    colorDetailWindow(activeColor, evt);

                }

            });

            cp.add(farb);

            situation += 50;

        }

        JButton addToGroup = new JButton("Neue Farbe");
        addToGroup.setBounds(10, situation, 200, 25);
        addToGroup.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {

                JFrame addDC = new JFrame(); //DirectedColor
                addDC.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                addDC.setBounds(groupDetail.getX() + addToGroup.getX(), groupDetail.getY() + addToGroup.getY(), 200, 200);
                Container cpDC = addDC.getContentPane();
                cpDC.setLayout(null);

                JLabel wert = new JLabel("Wert");
                wert.setBounds(8, 8, 182, 25);
                cpDC.add(wert);

                JTextField input = new JTextField();
                input.setBounds(8, 33, 182, 25);
                cpDC.add(input);

                JButton add = new JButton("Hinzufügen");
                add.setBounds(8, 66, 182, 25);
                add.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {

                        String col = input.getText();

                        CharSequence cs = gruppenInhalt[0];
                        boolean allowed = true;

                        try {

                            Color lol = rgbColor(col);

                        } catch (StringIndexOutOfBoundsException ex) {

                            System.out.println(ex);
                            allowed = false;

                        } finally {

                            if (allowed == true) {

                                try {

                                    FileWriter fw = new FileWriter(colors);
                                    BufferedWriter bw = new BufferedWriter(fw);
                                    PrintWriter fileOut = new PrintWriter(bw);

                                    int count = AIO.split("\n").length;
                                    String[] mains = new String[count];
                                    mains = AIO.split("\n");

                                    for (int i = 0; i < count; i++) {

                                        if (mains[i].contains(cs)) {
                                            fileOut.println(mains[i] + "," + col);
                                        } else {
                                            fileOut.println(mains[i]);
                                        }

                                    }

                                    fileOut.close();

                                } catch (Exception e1) {

                                    System.out.println(e1.toString());


                                } finally {

                                    System.out.println("created: " + newColor);
                                    addDC.dispose();
                                    groupDetail.dispose();
                                    dispose();
                                    new palet("palet");

                                }

                            }

                        }

                    }

                });

                cpDC.add(add);

                addDC.setVisible(true);

            }

        });

        cp.add(addToGroup);

        groupDetail.setVisible(true);

    }

}
