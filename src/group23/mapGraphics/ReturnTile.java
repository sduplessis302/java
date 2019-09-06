package group23.mapGraphics;

import group23.Entity;

import javax.swing.*;
import java.awt.*;

public class ReturnTile extends Entity {

    protected Image returnTile;
    protected int type;

    public ReturnTile(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
        loadSprite();
    }

    private void loadSprite() {

        ImageIcon ii;


        if (type == 1) {
            ii = new ImageIcon("res/images/map/returnTile.png");
            returnTile = ii.getImage();
        } else if (type == 2) {
            ii = new ImageIcon("res/images/map/returnTileHorizontal.png");
            returnTile = ii.getImage();
        }

        w = returnTile.getWidth(null);
        h = returnTile.getHeight(null);
    }

    public Image getReturnTile() {
        return returnTile;
    }
}
