package group23.mapGraphics;

import group23.Entity;

import javax.swing.*;
import java.awt.*;

public class HealthItem extends Entity {

    protected Image healthItem;

    public HealthItem(int x, int y) {
        this.x = x;
        this.y = y;
        loadSprite();
    }

    private void loadSprite() {


        ImageIcon  ii = new ImageIcon("res/images/items/health.png");
        healthItem = ii.getImage();

        w = healthItem.getWidth(null);
        h = healthItem.getHeight(null);
    }

    public Image getHealthItem() {
        return healthItem;
    }
}
