package group23;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IntroPanel extends JPanel {

    JButton continue_button = new JButton();

    Image background = Toolkit.getDefaultToolkit().createImage("res/images/intro/intro_background.png");
    Image title = Toolkit.getDefaultToolkit().createImage("res/images/menu/menu_title.png");
    Image intro1 = Toolkit.getDefaultToolkit().createImage("res/images/intro/intro1.png");
    Image intro2 = Toolkit.getDefaultToolkit().createImage("res/images/intro/intro2.png");

    public enum State {
        INTRO1,
        INTRO2
    }

    protected State state = State.INTRO1;


    public IntroPanel(){

        initIntro();

    }

    private void initIntro(){
        setLayout(null);

        //Continue button
        continue_button.setContentAreaFilled(false);
        continue_button.setBorderPainted(false);
        continue_button.setRolloverEnabled(true);
        continue_button.setIcon(new ImageIcon("res/images/menu/play_button.png"));
        continue_button.setRolloverIcon(new ImageIcon("res/images/menu/play_button_hover.png"));
        continue_button.setBounds(600, 700, 250, 80);

        add(continue_button);

        continue_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //Move through intro screens
                if(state == State.INTRO1){
                    state = State.INTRO2;
                } else if(state == State.INTRO2){
                    //remove(continue_button);
                    removeAll();
                    //System.out.println("INTRO2");
                    Main.changeState(3);
                    Canvas.getCanvas().panelState();
                }

                repaint();

            }
        });


    }

    public void paintComponent(Graphics g){

        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(background, 0, 0, this);


        if(state == State.INTRO1){
            g2.drawImage(intro1, 0, 0, this);
        } else if(state == State.INTRO2){
            g2.drawImage(intro2, 0, 0, this);
        }

    }

}
