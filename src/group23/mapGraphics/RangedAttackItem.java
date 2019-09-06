package group23.mapGraphics;

import group23.Entity;

import javax.swing.*;
import java.awt.*;

public class RangedAttackItem extends Entity {

    protected Image rangedItem;

    public RangedAttackItem(int x, int y) {
        this.x = x;
        this.y = y;
        loadSprite();
    }

    private void loadSprite() {


        ImageIcon ii = new ImageIcon("res/images/items/rangedAttack.png");
        rangedItem = ii.getImage();

        w = rangedItem.getWidth(null);
        h = rangedItem.getHeight(null);
    }

    public Image getRangedItem() {
        return rangedItem;
    }
}
