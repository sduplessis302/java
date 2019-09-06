package group23.mapGraphics;

import group23.Entity;

import javax.swing.*;
import java.awt.*;

public class WallOver extends Entity {

    protected Image wallOver;
    protected int type;

    public WallOver(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
        loadSprite();
    }

    private void loadSprite() {

        ImageIcon ii;

        switch(type) {
            case 1:
                ii = new ImageIcon("res/images/map/wall_bottom.png");
                wallOver = ii.getImage();
                break;
            case 2:
                ii = new ImageIcon("res/images/map/wall_islandTopLeft.png");
                wallOver = ii.getImage();
                break;
            case 3:
                ii = new ImageIcon("res/images/map/wall_islandTopRight.png");
                wallOver = ii.getImage();
                break;
        }

        w = wallOver.getWidth(null);
        h = wallOver.getHeight(null);
    }

    public Image getWallOver() {
        return wallOver;
    }
}
