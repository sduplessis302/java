package group23;

import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;

public class Entity {

    protected int dx;
    protected int dy;
    protected int x ;
    protected int y;
    protected int w;
    protected int h;
    protected Model model;

    protected  int health;

    protected Image image;

    public static final int WIDTH = 1440;
    public static final int HEIGHT = 900;

    public Entity() {
        loadSprite();

    }

    private void loadSprite() {

        ImageIcon i = new ImageIcon("res/images/character/rest_down.png");

        image = i.getImage();

        w = image.getWidth(null);
        h = image.getHeight(null);
    }


    public void move() {
        //Move player based on dy & dx values
        x += dx;
        y += dy;

        //Ensure sprite doesnt leave window bounds
        if(x < 0){
            x = 0;
        }
        if(x > (WIDTH-w)){
            x = (WIDTH-w);
        }
        if(y < 0){
            y = 0;
        }
        if(y > (HEIGHT-h)){
            y = (HEIGHT-h);
            //System.out.println(y+h);
        }

    }

    public void heal(int heal){
        if (health + heal > 100) {
            health = 100;
        } else {
            health += 60;
        }
    }

    public void damage(int damage){
        health -= damage;
    }



    //SETTERS & GETTERS

    public void setDx(int dx){
        this.dx = dx;
    }
    public void setDy(int dy){
        this.dy = dy;
    }

    public int getX() {

        return x;
    }

    public int getY() {

        return y;
    }

    public int getWidth() {

        return w;
    }

    public int getHeight() {

        return h;
    }

    public Rectangle getBounds() {
        //System.out.println("X: " + x + ", Y: " + y);
        return new Rectangle(x, y, w, h);
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setX(int x){

        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public Image getSprite() {

        return image;
    }

}
