package group23;

import group23.ai.AStar;
import group23.ai.Node;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
//import java.lang.*;

public class Snitch extends Entity {

    private boolean playerSeen = false;
    private boolean threadStarted = false;


    private int randSquare = (int)(Math.random() * 100 + 40);
    private int numberMoved = 9;
    private int moveUp = randSquare;
    private int upAmount = 0;
    private int moveDown = randSquare;
    private int downAmount = 0;
    private int moveLeft = randSquare;
    private int leftAmount = 0;
    private int moveRight = randSquare;
    private int rightAmount = 0;
    private int moveDirection = 1;

    protected static int initialHealth = 30;

    ArrayList<Node> pathFound;

    Node start;
    Node goal;
    AStar aStar;

    Player player1;


    public Snitch(int x, int y){
        health = 30;
        this.x = x;
        this.y = y;

        loadSprite();
    }


    //protected Image sprite;
    protected Image rest_up;
    protected Image rest_down;
    protected Image rest_left;
    protected Image rest_right;
    protected Image walkingRight;
    protected Image walkingLeft;
    protected Image walkingUp;
    protected Image walkingDown;


    protected static Animation animationState = Animation.REST_DOWN;

    private enum Animation {
        REST_DOWN,
        REST_UP,
        REST_LEFT,
        REST_RIGHT,
        RIGHT,
        LEFT,
        UP,
        DOWN
    }
    public static Animation changeAnimationState(int newState) {

        if (newState == 1) {
            animationState = Animation.REST_DOWN;
        } else if (newState == 2){
            animationState = Animation.RIGHT;
        } else if (newState == 3) {
            animationState = Animation.LEFT;
        } else if (newState == 4) {
            animationState = Animation.UP;
        } else if (newState == 5) {
            animationState = Animation.DOWN;
        } else if (newState == 6) {
            animationState = Animation.REST_UP;
        } else if (newState == 7) {
            animationState = Animation.REST_LEFT;
        } else if (newState == 8) {
            animationState = Animation.REST_RIGHT;
        }
        return animationState;
    }

    private void loadSprite() {
        //System.out.println("Loading Sprite");

        ImageIcon sprite = new ImageIcon("res/images/character/snitch.gif");
        image = sprite.getImage();

        w = image.getWidth(null);
        h = image.getHeight(null);
    }

    public void updateSnitchPosition() {

        if (getHealth() < 0.2) {
            return;
        }

        player1 = Main.getModel().getPlayer1();

        //Check proximity to player
        if(Math.abs(player1.getX() - this.getX()) < 200) {
            if(Math.abs(player1.getY() - this.getY()) < 200) {
                this.playerSeen = true;
            }
        }

        if (playerSeen == false) {
            ///////////////////////
            /*predefined path until player is seen*/
            switch(moveDirection) {
                case 1:
                    if (this.moveUp == this.upAmount) {
                        this.moveDirection = 2;
                        this.upAmount = 0;
                    } else {
                        this.setDy(-1);
                        this.setDx(0);
                        this.upAmount++;
                    }
                    break;
                case 2:
                    if (this.moveRight == this.rightAmount) {
                        this.moveDirection = 3;
                        this.rightAmount = 0;
                    } else {
                        this.setDx(1);
                        this.setDy(0);
                        this.rightAmount++;
                    }
                    break;
                case 3:
                    if (this.moveDown == this.downAmount) {
                        this.moveDirection = 4;
                        this.downAmount = 0;
                    } else {
                        this.setDy(1);
                        this.setDx(0);
                        this.downAmount++;
                    }
                    break;
                case 4:
                    if (this.moveLeft == this.leftAmount) {
                        this.moveDirection = 1;
                        this.leftAmount = 0;
                    } else {
                        this.setDx(-1);
                        this.setDy(0);
                        this.leftAmount++;
                    }
                    break;

            }

            ///////////////////////
        } else if (playerSeen) {

            //////////////////////

            if (!threadStarted) {
                //A* algorithm to find player
                Thread thread = new Thread(new SnitchMovement(this, start, goal));
                thread.start();
                threadStarted = true;

            }


            //////////////////////
        }

    }


    //SETTERS & GETTERS

    public boolean isPlayerSeen(){
        return isPlayerSeen();
    }

    public static int getInitialHealth() {
        return initialHealth;
    }

    public Image getSprite() {
        return image;
    }

    public void setThreadStarted(Boolean state) {
        threadStarted = state;
    }

    public static Animation getAnimationState() {
        return animationState;
    }
}
