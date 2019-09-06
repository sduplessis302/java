package group23;

import java.awt.event.KeyEvent;
import java.util.Timer;

public class Controller {

    protected Model model;
    protected Player player1;
    protected Canvas canvas;
    protected boolean isPaused = false;
    protected int prevState = 1;
    private Timer time;

    public int coolDown = 0;

    public Controller(Model model){
        this.model = model;
        player1 = model.getPlayer1();

    }


    //Called if keyboard input is detected
    public void keyIsPressed(KeyEvent e) {


        int key = e.getKeyCode();

        //Move player left
        if (key == KeyEvent.VK_LEFT) {
            Player.changeAnimationState(3);
            prevState = 3;
            player1.setDx(-2);
            player1.setDy(0);

        }

        //Move player right
        if (key == KeyEvent.VK_RIGHT) {
            Player.changeAnimationState(2);
            prevState = 2;
            player1.setDx(2);
            player1.setDy(0);

        }

        //Move player up
        if (key == KeyEvent.VK_UP) {
            Player.changeAnimationState(4);
            prevState = 4;
            player1.setDy(-2);
            player1.setDx(0);
        }

        //Cheat inputs
        if (key == KeyEvent.VK_PAGE_DOWN) {
            model.cheat();
        }
        if (key == KeyEvent.VK_BACK_SPACE   ) {
            model.cheat();
        }

        //Move player down
        if (key == KeyEvent.VK_DOWN) {
            Player.changeAnimationState(5);
            prevState = 5;
            player1.setDy(2);
            player1.setDx(0);
        }

        //Melee Attack
        if (key == KeyEvent.VK_A && coolDown > 50) {
            coolDown = 0;
            if (Player.getAnimationState() == Player.Animation.REST_UP || Player.getAnimationState() == Player.Animation.UP) {
                Player.changeAnimationState(9);
            } else if (Player.getAnimationState() == Player.Animation.REST_DOWN || Player.getAnimationState() == Player.Animation.DOWN) {
                Player.changeAnimationState(10);
            } else if (Player.getAnimationState() == Player.Animation.REST_LEFT || Player.getAnimationState() == Player.Animation.LEFT) {
                Player.changeAnimationState(11);
            } else if (Player.getAnimationState() == Player.Animation.REST_RIGHT || Player.getAnimationState() == Player.Animation.RIGHT) {
                Player.changeAnimationState(12);
            }
        }

        //Ranged attack
        if (key == KeyEvent.VK_S && coolDown > 50) {
            coolDown = 0;
            if (player1.getRangedAttack()) {
                model.addProjectiles(1);
            }
        }

        //Pause game
        if(key == KeyEvent.VK_P){

            if(!isPaused){
                Main.changeState(4);
                model.setPaused(true);
                isPaused = true;
            } else{
                //System.out.println("unpausing");
                Main.changeState(3);
                model.setPaused(false);
                isPaused = false;
            }
            canvas.panelState();
        }

        //Exit confirmation
        if(key == KeyEvent.VK_ESCAPE){

            if(!isPaused){
                Main.changeState(6);
                model.setPaused(true);
                isPaused = true;
               // System.out.println("Paused");
            } else{
                //System.out.println("unpausing");
                Main.changeState(3);
                model.setPaused(false);
                isPaused = false;
            }
            canvas.panelState();
        }
    }

    //Stop moving & animations when key is released
    public void keyIsReleased(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            Player.changeAnimationState(7);
            prevState = 7;
            player1.setDx(0);
        }

        if (key == KeyEvent.VK_RIGHT) {
            Player.changeAnimationState(8);
            prevState = 8;
            player1.setDx(0);
        }

        if (key == KeyEvent.VK_UP) {
            Player.changeAnimationState(6);
            prevState = 6;
            player1.setDy(0);
        }

        if (key == KeyEvent.VK_DOWN) {
            Player.changeAnimationState(1);
            prevState = 1;
            player1.setDy(0);
        }

        if (key == KeyEvent.VK_A) {
            Player.changeAnimationState(prevState);
        }
    }


    //GETTERS & SETTERS

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public void incrementCoolDown() {
        this.coolDown++;
    }

    public int getPrevState() {
        return prevState;
    }
}
