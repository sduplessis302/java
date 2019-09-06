package group23.mapGraphics;

import group23.Entity;

import javax.swing.*;
import java.awt.*;

public class Locker extends Entity {

    protected Image locker;

    public Locker(int x, int y){
        this.x = x;
        this.y = y;
        loadSprite();
    }

    private void loadSprite() {

        ImageIcon ii = new ImageIcon("res/images/map/locker.png");
        locker = ii.getImage();

        w = locker.getWidth(null);
        h = locker.getHeight(null);
    }

    public Image getLocker() {
        return locker;
    }

}
