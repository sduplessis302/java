package group23;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class GameOverPanel extends JPanel {
    JButton quit_button = new JButton();

    int score;

    Font font;
    GraphicsEnvironment ge;

    boolean win;
    boolean finished = false;

    Image background = Toolkit.getDefaultToolkit().createImage("res/images/ui/gameover_background.png");
    Image title = Toolkit.getDefaultToolkit().createImage("res/images/menu/menu_title.png");
    Image gameOver = Toolkit.getDefaultToolkit().createImage("res/images/ui/gameover_text.png");
    Image gameWin = Toolkit.getDefaultToolkit().createImage("res/images/ui/gamewin_text.png");

    public GameOverPanel(){
        initGameOver();
        win = false;

    }

    private void initGameOver(){
        setLayout(null);

        //Quit button
        quit_button.setContentAreaFilled(false);
        quit_button.setBorderPainted(false);
        quit_button.setRolloverEnabled(true);
        quit_button.setIcon(new ImageIcon("res/images/menu/quit_button.png"));
        quit_button.setRolloverIcon(new ImageIcon("res/images/menu/quit_button_hover.png"));
        quit_button.setBounds(580, 700, 250, 80);
        quit_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        add(quit_button);

        //Load in font
        try {
            ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            font = Font.createFont(Font.TRUETYPE_FONT, new File("res/font/RobotoSlab-Regular.ttf"));
            ge.registerFont(font);
        } catch (IOException |FontFormatException e) {
            //Handle exception
        }


    }

    public void gameOver(int score){
        if(!finished) {
            this.score = score;
            //System.out.println("Score: " + score);
            addHighscore(score); //Write score to file
            repaint();
            finished = true;
        }
    }

    public void paintComponent(Graphics g){

        Graphics2D g2 = (Graphics2D) g;

        //Draw 'you win' or 'game over' depending on game outcome
        g2.drawImage(background, 0, 0, this);
        if (win) {
            g2.drawImage(gameWin,0,0,this);
        }else{
            g2.drawImage(gameOver,0,0,this);
        }

        //Display player score
        String out = "Score: ";
        String strOut = Integer.toString(score);
        g2.setFont(font.deriveFont(60f));
        printSimpleString(g2,out,1440,0,460);
        printSimpleString(g2,strOut,1440,0,535);


    }

    public void setWin(boolean bool){
        win = bool;
    }

    //Method to centre align string from https://coderanch.com/t/336616/java/Center-Align-text-drawString
    private void printSimpleString(Graphics2D g2d, String s, int width, int XPos, int YPos){
        int stringLen = (int)
                g2d.getFontMetrics().getStringBounds(s, g2d).getWidth();
        int start = width/2 - stringLen/2;
        g2d.drawString(s, start + XPos, YPos);
    }

    //Saves player score to file
    //Adapted from https://caveofprogramming.com/java/java-file-reading-and-writing-files-in-java.html
    public void addHighscore(int score){

        String fileName = "res/data/scores.txt";

        try {
            // Assume default encoding.
            FileWriter fileWriter =
                    new FileWriter(fileName,true);

            // Always wrap FileWriter in BufferedWriter.
            BufferedWriter bufferedWriter =
                    new BufferedWriter(fileWriter);

            // Note that write() does not automatically
            // append a newline character.
            bufferedWriter.write(Integer.toString(score));
            bufferedWriter.newLine();

            // Always close files.
            bufferedWriter.close();
        }
        catch(IOException ex) {
            System.out.println(
                    "Error writing to file '"
                            + fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }
    }
}




