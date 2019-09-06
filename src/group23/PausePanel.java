package group23;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PausePanel extends JPanel implements ActionListener {

    static Graphics2D g2d;
    protected Image background;
    protected Image title;
    protected Controller controller;

    protected boolean isPaused;

    private Timer timer;
    private final int DELAY = 10;

    public PausePanel(){
        initPause();

    }

    //Load images, timer & key listener
    private void initPause() {
        background = Toolkit.getDefaultToolkit().createImage("res/images/ui/pause_background.png");
        title = Toolkit.getDefaultToolkit().createImage("res/images/ui/pause_title.png");

        timer = new Timer(DELAY, this);
        timer.start();

        addKeyListener(new TAdapter());
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);

        Toolkit.getDefaultToolkit().sync();
    }

    private void doDrawing(Graphics g) {

        g2d = (Graphics2D) g;

        g2d.drawImage(background,0,0,this);
        g2d.drawImage(title, 0, 0, this);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //System.out.println("Action performed");

        if(isPaused){
            requestFocusInWindow();
            repaint();
        }

    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            controller.keyIsReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            controller.keyIsPressed(e);
        }
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }
}
