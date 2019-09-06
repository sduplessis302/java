package group23;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;

public class MenuPanel extends JPanel {

    JButton skins_button = new JButton();
    JButton play_button = new JButton();
    JButton quit_button = new JButton();

    Image background = Toolkit.getDefaultToolkit().createImage("res/images/menu/menu_background2.png");
    Image title = Toolkit.getDefaultToolkit().createImage("res/images/menu/menu_title.png");

    Model model;
    Thread musicThread;
    Font font;
    GraphicsEnvironment ge;

    int[] highscores = new int[3];
    boolean skins = false;


    public MenuPanel(Model model) {
        this.model = model;

        menuButtons();

        readHighscore();

        //Loading in font
        try {
            ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            font = Font.createFont(Font.TRUETYPE_FONT, new File("res/font/RobotoSlab-Regular.ttf"));
            ge.registerFont(font);
        } catch (IOException |FontFormatException e) {
            //Handle exception
        }
    }


    //Panel with buttons to play,quit,select skin
    private void menuButtons() {
        skins = false;
        setLayout(null);

        //ADD BUTTONS

        skins_button.setContentAreaFilled(false);
        skins_button.setBorderPainted(false);
        skins_button.setRolloverEnabled(true);
        skins_button.setIcon(new ImageIcon("res/images/menu/skins_button.png"));
        skins_button.setRolloverIcon(new ImageIcon("res/images/menu/skins_button_hover.png"));
        skins_button.setBounds(350, 700, 250, 80);
        skins_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.exit(0);
                skins_button.setVisible(false);
                play_button.setVisible(false);
                quit_button.setVisible(false);
                SkinsPanel();
            }
        });

        play_button.setContentAreaFilled(false);
        play_button.setBorderPainted(false);
        play_button.setRolloverEnabled(true);
        play_button.setIcon(new ImageIcon("res/images/menu/play_button.png"));
        play_button.setRolloverIcon(new ImageIcon("res/images/menu/play_button_hover.png"));
        play_button.setBounds(600, 700, 250, 80);
        play_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.exit(0);
               // model.getPlayer1().changeSelectedSprite(1);
                Main.changeState(2);
                Canvas.getCanvas().panelState();
            }
        });

        quit_button.setContentAreaFilled(false);
        quit_button.setBorderPainted(false);
        quit_button.setRolloverEnabled(true);
        quit_button.setIcon(new ImageIcon("res/images/menu/quit_button.png"));
        quit_button.setRolloverIcon(new ImageIcon("res/images/menu/quit_button_hover.png"));
        quit_button.setBounds(850, 700, 250, 80);
        quit_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        add(skins_button);
        add(play_button);
        add(quit_button);

    }

    //Panel to allow player to select skin
    private void SkinsPanel() {
        skins = true;
        JButton menu_return = new JButton();
        JButton selectSkinOne = new JButton();
        JButton selectSkinTwo = new JButton();

        add(menu_return);
        add(selectSkinOne);
        add(selectSkinTwo);

        menu_return.setContentAreaFilled(false);
        menu_return.setBorderPainted(false);
        menu_return.setRolloverEnabled(true);
        menu_return.setIcon(new ImageIcon("res/images/menu/menu_button.png"));
        menu_return.setRolloverIcon(new ImageIcon("res/images/menu/menu_button_hover.png"));
        menu_return.setBounds(850, 700, 250, 80);

        selectSkinOne.setContentAreaFilled(false);
        selectSkinOne.setBorderPainted(false);
        selectSkinOne.setRolloverEnabled(true);
        selectSkinOne.setIcon(new ImageIcon("res/images/menu/skin1.png"));
        selectSkinOne.setRolloverIcon(new ImageIcon("res/images/menu/skin1_hover.png"));
        selectSkinOne.setBounds(470, 402, 200, 200);

        selectSkinTwo.setContentAreaFilled(false);
        selectSkinTwo.setBorderPainted(false);
        selectSkinTwo.setRolloverEnabled(true);
        selectSkinTwo.setIcon(new ImageIcon("res/images/menu/skin2.png"));
        selectSkinTwo.setRolloverIcon(new ImageIcon("res/images/menu/skin2_hover.png"));
        selectSkinTwo.setBounds(740, 400, 200, 200);

        selectSkinOne.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.changeState(2);
                Canvas.getCanvas().panelState();
                //model.getPlayer1().changeSelectedSprite(1);
            }
        });

        selectSkinTwo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.changeState(2);
                Canvas.getCanvas().panelState();
                model.getPlayer1().changeSelectedSprite(2);
            }
        });

        menu_return.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menu_return.setVisible(false);
                selectSkinOne.setVisible(false);
                selectSkinTwo.setVisible(false);
                skins_button.setVisible(true);
                play_button.setVisible(true);
                quit_button.setVisible(true);
                skins = false;
                repaint();
            }
        });
        repaint();

    }

    public void paintComponent(Graphics g){

        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(background, 0, 0, this);
        g2.drawImage(title, 450, 30, this);

        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF);
        //g2.setRenderingHints(rh);

        //Display highschores
        if(!skins) {
            g2.setFont(font.deriveFont(60f));
            printSimpleString(g2, "Highscores", 1440, 0, 350);
            g2.setFont(font.deriveFont(50f));
            printSimpleString(g2, Integer.toString(highscores[0]), 1440, 0, 420);
            printSimpleString(g2, Integer.toString(highscores[1]), 1440, 0, 490);
            printSimpleString(g2, Integer.toString(highscores[2]), 1440, 0, 560);
        }

    }

    //Method implemented from https://coderanch.com/t/336616/java/Center-Align-text-drawString
    private void printSimpleString(Graphics2D g2d, String s, int width, int XPos, int YPos){
        int stringLen = (int)
                g2d.getFontMetrics().getStringBounds(s, g2d).getWidth();
        int start = width/2 - stringLen/2;
        g2d.drawString(s, start + XPos, YPos);
    }

    //Read all scores from file, save top 3 to array
    //Adapted from https://caveofprogramming.com/java/java-file-reading-and-writing-files-in-java.html
    protected void readHighscore(){
        String fileName = "res/data/scores.txt";
        String line  = null;

        try{
            // FileReader reads text files in the default encoding.
            FileReader fileReader =
                    new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                //System.out.println(line);
                int score = Integer.parseInt(line);
                if(score > highscores[0]){
                    highscores[2] = highscores[1];
                    highscores[1] = highscores[0];
                    highscores[0] = score;
                } else if(score > highscores[1]){
                    highscores[2] = highscores[1];
                    highscores[1] = score;
                }
                else if(score > highscores[2]){
                    highscores[2] = score;
                }
            }

            // Always close files.
            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" +
                            fileName + "'");
        }
        catch(IOException ex) {
            System.out.println(
                    "Error reading file '"
                            + fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }

    }
}
