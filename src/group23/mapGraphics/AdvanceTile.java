package group23.mapGraphics;

import group23.Entity;

import javax.swing.*;
import java.awt.*;

public class AdvanceTile extends Entity {

    protected Image advanceTile;
    protected int type;

    public AdvanceTile(int x, int y, int type) {
            this.x = x;
            this.y = y;
            this.type = type;
            loadSprite();
    }

    private void loadSprite() {

        ImageIcon ii;

        if (type == 1) {
            ii = new ImageIcon("res/images/map/advanceTile.png");
            advanceTile = ii.getImage();
        } else if (type == 2) {
            ii = new ImageIcon("res/images/map/advanceTileHorizontal.png");
            advanceTile = ii.getImage();
        }

        w = advanceTile.getWidth(null);
        h = advanceTile.getHeight(null);
    }

    public Image getAdvanceTile() {
        return advanceTile;
    }

}
