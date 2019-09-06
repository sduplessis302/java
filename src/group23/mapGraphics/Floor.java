package group23.mapGraphics;

import group23.Entity;

import javax.swing.*;
import java.awt.*;

public class Floor extends Entity {

    protected Image tile;

    public Floor(int x, int y){
        this.x = x;
        this.y = y;
        loadSprite();
    }

    private void loadSprite() {

        ImageIcon ii = new ImageIcon("res/images/map/tile.png");
        tile = ii.getImage();

        w = tile.getWidth(null);
        h = tile.getHeight(null);
    }

    public Image getTile() {
        return tile;
    }
}
