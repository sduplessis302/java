package group23;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ExitPanel extends JPanel implements ActionListener {

    static Graphics2D g2d;
    protected Image background;
    protected Image title;
    protected Controller controller;

    JButton quit_button = new JButton();
    JButton resume_button = new JButton();

    protected boolean isPaused;

    private Timer timer;
    private final int DELAY = 10;

    public ExitPanel(){
        initExit();

    }

    private void initExit() {

        //Set up images & timer
        background = Toolkit.getDefaultToolkit().createImage("res/images/ui/pause_background.png");
        title = Toolkit.getDefaultToolkit().createImage("res/images/ui/quit_title.png");
        timer = new Timer(DELAY, this);
        timer.start();

        //Quit button
        setLayout(null);
        quit_button.setContentAreaFilled(false);
        quit_button.setBorderPainted(false);
        quit_button.setRolloverEnabled(true);
        quit_button.setIcon(new ImageIcon("res/images/menu/quit_button.png"));
        quit_button.setRolloverIcon(new ImageIcon("res/images/menu/quit_button_hover.png"));
        quit_button.setBounds(400, 600, 250, 80);
        quit_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        //Resume button
        resume_button.setContentAreaFilled(false);
        resume_button.setBorderPainted(false);
        resume_button.setRolloverEnabled(true);
        resume_button.setIcon(new ImageIcon("res/images/ui/resume_button.png"));
        resume_button.setRolloverIcon(new ImageIcon("res/images/ui/resume_button_hover.png"));
        resume_button.setBounds(750, 600, 250, 80);
        resume_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.changeState(3);
                isPaused = false;
                controller.setPaused(false);
            }
        });

        //Add components & keylistener
        add(quit_button);
        add(resume_button);
        addKeyListener(new TAdapter());
    }

    private void doDrawing(Graphics g) {

        g2d = (Graphics2D) g;

        g2d.drawImage(background,0,0,this);
        g2d.drawImage(title, 0, 0, this);


    }

    //Call controller methods if key action is detected
    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            //System.out.println("Key Released");
            controller.keyIsReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            //System.out.println("Key Pressed");
            controller.keyIsPressed(e);
        }
    }



    //OVERRIDES

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);

        Toolkit.getDefaultToolkit().sync();
    }

    //Game tick
    @Override
    public void actionPerformed(ActionEvent e) {
        //System.out.println("Action performed");

        if(isPaused){
            requestFocusInWindow();
            repaint();
        }

    }



    //SETTERS & GETTERS

    public void setController(Controller controller) {
        this.controller = controller;
    }
    public void setPaused(boolean paused) {
        isPaused = paused;
    }
}
