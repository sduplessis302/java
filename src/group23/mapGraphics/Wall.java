package group23.mapGraphics;

import group23.Entity;

import javax.swing.*;
import java.awt.*;

public class Wall extends Entity {

    protected int type;
    protected Image wall;

    public Wall(int x, int y, int type){
        this.x = x;
        this.y = y;
        this.type = type;
        loadSprite();
    }

    private void loadSprite() {

        ImageIcon ii;

        switch(type) {
            case 1:
                ii = new ImageIcon("res/images/map/wall.png");
                wall =ii.getImage();
                break;
            case 2:
                ii = new ImageIcon("res/images/map/wall_left.png");
                wall = ii.getImage();
                break;
            case 3:
                ii = new ImageIcon("res/images/map/wall_right.png");
                wall = ii.getImage();
                break;
            case 4:
                ii = new ImageIcon("res/images/map/wall_bottomLeft.png");
                wall = ii.getImage();
                break;
            case 5:
                ii = new ImageIcon("res/images/map/wall_bottomRight.png");
                wall = ii.getImage();
                break;
            case 6:
                ii = new ImageIcon("res/images/map/wall_topLeft.png");
                wall = ii.getImage();
                break;
            case 7:
                ii = new ImageIcon("res/images/map/wall_topRight.png");
                wall = ii.getImage();
                break;
            case 8:
                ii = new ImageIcon("res/images/map/wall_islandBottomLeft.png");
                wall = ii.getImage();
                break;
            case 9:
                ii = new ImageIcon("res/images/map/wall_islandBottomRight.png");
                wall = ii.getImage();
                break;
            case 10:
                ii = new ImageIcon("res/images/map/backgroundFiller.png");
                wall = ii.getImage();
                break;

        }


        w = wall.getWidth(null);
        h = wall.getHeight(null);
    }

    public Image getWall() {
        return wall;
    }

}
