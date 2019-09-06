package group23;

import javax.swing.*;
import java.awt.*;

public class Player extends Entity {

    protected Image image;
    protected Image rest_up;
    protected Image rest_down;
    protected Image rest_left;
    protected Image rest_right;
    protected Image walkingRight;
    protected Image walkingLeft;
    protected Image walkingUp;
    protected Image walkingDown;
    protected Image hitUp;
    protected Image hitDown;
    protected Image hitLeft;
    protected Image hitRight;
    protected Image hurt;

    protected boolean rangedAttackGained = false;

    private static SelectedSprite selectedSprite = SelectedSprite.ONE;

    private enum SelectedSprite {
        ONE,
        TWO,
    }

    public static Player.Animation animationState = Player.Animation.REST_DOWN;

    protected enum Animation {
        REST_DOWN,
        REST_UP,
        REST_LEFT,
        REST_RIGHT,
        RIGHT,
        LEFT,
        UP,
        DOWN,
        ATTACK_UP,
        ATTACK_DOWN,
        ATTACK_LEFT,
        ATTACK_RIGHT,
        HURT,
    }

    public Player() {

        //loadSprite();
    }

    public static Animation changeAnimationState(int newState) {

        switch (newState) {
            case 1:
                animationState = Animation.REST_DOWN;
                break;
            case 2:
                animationState = Animation.RIGHT;
                break;
            case 3:
                animationState = Animation.LEFT;
                break;
            case 4:
                animationState = Animation.UP;
                break;
            case 5:
                animationState = Animation.DOWN;
                break;
            case 6:
                animationState = Animation.REST_UP;
                break;
            case 7:
                animationState = Animation.REST_LEFT;
                break;
            case 8:
                animationState = Animation.REST_RIGHT;
                break;
            case 9:
                animationState = Animation.ATTACK_UP;
                break;
            case 10:
                animationState = Animation.ATTACK_DOWN;
                break;
            case 11:
                animationState = Animation.ATTACK_LEFT;
                break;
            case 12:
                animationState = Animation.ATTACK_RIGHT;
                break;
            case 13:
                animationState = Animation.HURT;
        }

        return animationState;
    }


    public Image getSprite() {

        switch(getAnimationState()) {
            case REST_DOWN:
                image = rest_down;
                //System.out.println(getAnimationState());
                break;
            case RIGHT:
                image = walkingRight;
                //System.out.println(getAnimationState());
                break;
            case LEFT:
                image = walkingLeft;
                //System.out.println(getAnimationState());
                break;
            case UP:
                image = walkingUp;
                //System.out.println(getAnimationState());
                break;
            case DOWN:
                image = walkingDown;
                //System.out.println(getAnimationState());
                break;
            case REST_UP:
                image = rest_up;
                //System.out.println(getAnimationState());
                break;
            case REST_LEFT:
                image = rest_left;
                //System.out.println(getAnimationState());
                break;
            case REST_RIGHT:
                image = rest_right;
                //System.out.println(getAnimationState());
                break;
            case ATTACK_UP:
                image = hitUp;
                break;
            case ATTACK_DOWN:
                image = hitDown;
                break;
            case ATTACK_LEFT:
                image = hitLeft;
                break;
            case ATTACK_RIGHT:
                image = hitRight;
                break;
            case HURT:
                image = hurt;

        }

        w = image.getWidth(null);
        h = image.getHeight(null);

        return image;
    }

    //Loads different image based on selected character & current player state
    private void loadPlayerSprite() {

        ImageIcon i;
        ImageIcon ii;
        ImageIcon iii;
        ImageIcon iv;
        ImageIcon v;
        ImageIcon restUp;
        ImageIcon restLeft;
        ImageIcon restRight;
        ImageIcon attackUp;
        ImageIcon attackDown;
        ImageIcon attackLeft;
        ImageIcon attackRight;
        ImageIcon playerHurt;

        switch(getSelectedSprite()) {

            case ONE:
                i = new ImageIcon("res/images/character/rest_down.png");
                ii = new ImageIcon("res/images/character/walkingRIGHT.gif");
                iii = new ImageIcon("res/images/character/walkingUP.gif");
                iv = new ImageIcon("res/images/character/walkingLEFT.gif");
                v = new ImageIcon("res/images/character/walkingDOWN.gif");
                restUp = new ImageIcon("res/images/character/rest_up.png");
                restLeft = new ImageIcon("res/images/character/rest_left.png");
                restRight = new ImageIcon("res/images/character/rest_right.png");

                attackUp = new ImageIcon("res/images/character/hitUP.gif");
                attackDown = new ImageIcon("res/images/character/hitDOWN.gif");
                attackLeft = new ImageIcon("res/images/character/hitLEFT.gif");
                attackRight = new ImageIcon("res/images/character/hitRIGHT.gif");

                playerHurt = new ImageIcon("res/images/character/hurt.png");

                rest_down = i.getImage();
                image = rest_down;
                rest_up = restUp.getImage();
                rest_left = restLeft.getImage();
                rest_right = restRight.getImage();
                walkingRight = ii.getImage();
                walkingUp = iii.getImage();
                walkingLeft = iv.getImage();
                walkingDown = v.getImage();
                hitUp = attackUp.getImage();
                hitDown = attackDown.getImage();
                hitLeft = attackLeft.getImage();
                hitRight = attackRight.getImage();

                hurt = playerHurt.getImage();
                break;
            case TWO:
                i = new ImageIcon("res/images/character/rest_down_two.png");
                ii = new ImageIcon("res/images/character/walkingRIGHT_two.gif");
                iii = new ImageIcon("res/images/character/walkingUP_two.gif");
                iv = new ImageIcon("res/images/character/walkingLEFT_two.gif");
                v = new ImageIcon("res/images/character/walkingDOWN_two.gif");
                restUp = new ImageIcon("res/images/character/rest_up_two.png");
                restLeft = new ImageIcon("res/images/character/rest_left_two.png");
                restRight = new ImageIcon("res/images/character/rest_right_two.png");

                attackUp = new ImageIcon("res/images/character/hitUP_two.gif");
                attackDown = new ImageIcon("res/images/character/hitDOWN_two.gif");
                attackLeft = new ImageIcon("res/images/character/hitLEFT_two.gif");
                attackRight = new ImageIcon("res/images/character/hitRIGHT_two.gif");

                playerHurt = new ImageIcon("res/images/character/hurt_two.png");

                rest_down = i.getImage();
                image = rest_down;
                rest_up = restUp.getImage();
                rest_left = restLeft.getImage();
                rest_right = restRight.getImage();
                walkingRight = ii.getImage();
                walkingUp = iii.getImage();
                walkingLeft = iv.getImage();
                walkingDown = v.getImage();
                hitUp = attackUp.getImage();
                hitDown = attackDown.getImage();
                hitLeft = attackLeft.getImage();
                hitRight = attackRight.getImage();
                hurt = playerHurt.getImage();
                break;
        }

        w = image.getWidth(null);
        h = image.getHeight(null);
    }


    //GETTERS & SETTERS
    //
    public SelectedSprite getSelectedSprite() {
        //System.out.println(selectedSprite);
        return selectedSprite;
    }

    public SelectedSprite changeSelectedSprite(int newState) {
        if (newState == 2) {
            selectedSprite = SelectedSprite.TWO;
        }
        loadPlayer();
        return selectedSprite;
    }

    public boolean getRangedAttack() {
        return rangedAttackGained;
    }

    public void attackPickedUp() {
        this.rangedAttackGained = true;
    }

    public void loadPlayer() {
        loadPlayerSprite();
    }

    public static Player.Animation getAnimationState() {
        return animationState;
    }

}

