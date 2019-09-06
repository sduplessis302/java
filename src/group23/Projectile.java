package group23;

import javax.swing.*;
import java.awt.*;

public class Projectile extends Entity {

    protected int direction;
    protected boolean targetHit = false;
    protected int enemyType;

    public Projectile(int x, int y, int direction, int enemyType) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.enemyType = enemyType;
        loadSprite();
    }

    //Move projectile
    public int updateProjectile() {
        switch(direction) {
            case 1:
                this.setDy(-4);
                break;
            case 2:
                this.setDx(4);
                break;
            case 3:
                this.setDy(4);
                break;
            case 4:
                this.setDx(-4);
                break;
        }
        return direction;
    }

    //Load sprite based on direction & entityType
    private void loadSprite() {

        ImageIcon i;
       /* ImageIcon ii;
        ImageIcon iii;
        ImageIcon iv; */

       //Player projectile
        if (enemyType == 1) {
            switch (direction) {
                case 1:
                    i = new ImageIcon("res/images/character/bullet_up.png");
                    image = i.getImage();
                    w = image.getWidth(null);
                    h = image.getHeight(null);
                    break;
                case 2:
                    i = new ImageIcon("res/images/character/bullet_right.png");
                    image = i.getImage();
                    w = image.getWidth(null);
                    h = image.getHeight(null);
                    break;
                case 3:
                    i = new ImageIcon("res/images/character/bullet_down.png");
                    image = i.getImage();
                    w = image.getWidth(null);
                    h = image.getHeight(null);
                    break;
                case 4:
                    i = new ImageIcon("res/images/character/bullet_left.png");
                    image = i.getImage();
                    w = image.getWidth(null);
                    h = image.getHeight(null);
                    break;
            }

            //Boss projectile
        } else if (enemyType == 2) {
            switch(direction) {
                case 1:
                    i = new ImageIcon("res/images/character/bossBullet.png");
                    image = i.getImage();
                    w = image.getWidth(null);
                    h = image.getHeight(null);
                    break;
                case 2:
                    i = new ImageIcon("res/images/character/bossBullet_right.png");
                    image = i.getImage();
                    w = image.getWidth(null);
                    h = image.getHeight(null);
                    break;
                case 3:
                    i = new ImageIcon("res/images/character/bossBullet_down.png");
                    image = i.getImage();
                    w = image.getWidth(null);
                    h = image.getHeight(null);
                    break;
                case 4:
                    i = new ImageIcon("res/images/character/bossBullet_left.png");
                    image = i.getImage();
                    w = image.getWidth(null);
                    h = image.getHeight(null);
                    break;
            }
        }

    }

    public boolean getTargetHit() { return targetHit; }

    public void setTargetHit(boolean bool) {
        this.targetHit = bool;
    }

}
