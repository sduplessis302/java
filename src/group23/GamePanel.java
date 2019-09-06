package group23;

import group23.ai.AStar;
import group23.ai.Node;
import group23.mapGraphics.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.swing.*;


public class GamePanel extends JPanel implements ActionListener {

    private Timer timer;
    private Player player1;
    private final int DELAY = 10;

    protected List<Wall> walls;
    protected List<WallOver> wallBottom;
    protected List<Floor> tiles;
    protected List<Locker> lockers;
    protected List<AdvanceTile> advanceTiles;
    protected List<ReturnTile> returnTiles;
    protected List<Snitch> snitches;
    protected LinkedList<Projectile> bullets;
    protected LinkedList<Projectile> enemyBullet;
    protected List<HealthItem> healthItems;
    protected List<RangedAttackItem> rangedAttack;
    protected Principal boss;

    protected boolean isPaused = false;
    protected boolean gameOver = false;

    Controller controller;

    Model model;
    //not unused -- initialises array definitions
    private int[][] lockerPos;
    private int[][] wallPos;
    private int[][] wallOverPos;
    private int[][] tilePos;
    private int[][] advancePos;
    private int[][] returnPos;
    private int[][] healthPos;
    private int[][] rangedAttackPos;
    private int[][] bossPos;

    private int[][] snitchPos;

    private Wall wall1;

    static Graphics2D g2d;
    UIPanel uiPanel;

    public GamePanel(Model model, UIPanel uiPanel){
        this.model = model;
        this.uiPanel = uiPanel;
        initGame();

    }

    private void initGame() {

        //get player & snitches
        player1 = model.getPlayer1();
        player1.loadPlayer();
        player1.setHealth(100);
        //initial position
        player1.setX(4*32);
        player1.setY(4*32);
        snitches = model.getSnitches();

        //get all map entities
        lockers = model.getLockers();
        walls = model.getWalls();
        wallBottom = model.getWallsOver();
        tiles = model.getTile();
        advanceTiles = model.getAdvanceTile();
        returnTiles = model.getReturnTile();
        healthItems = model.getHealthItem();
        rangedAttack = model.getRangedAttackItem();

        //get map entity positions
        lockerPos = model.getLockerPos();
        advancePos = model.getAdvancePos();
        tilePos = model.getTilePos();
        wallPos = model.getWallPos();
        wallOverPos = model.getWallOverPos();
        returnPos = model.getReturnPos();
        bullets = model.getProjectiles();
        healthPos = model.getHealthPos();
        rangedAttackPos = model.getRangedAttackPos();


        setBackground(Color.white);
        setFocusable(true);

        timer = new Timer(DELAY, this);
        timer.start();

        addKeyListener(new TAdapter());
        //System.out.println("add listener");
    }


    private void doDrawing(Graphics g) {

        g2d = (Graphics2D) g;

        //Draw map

        for (AdvanceTile advanceTile : advanceTiles){
            g2d.drawImage(advanceTile.getAdvanceTile(), advanceTile.getX(), advanceTile.getY(), this);
        }
        for (ReturnTile returnTile : returnTiles) {
            g2d.drawImage(returnTile.getReturnTile(), returnTile.getX(), returnTile.getY(), this);
        }
        for (Floor tiles : tiles) {
            g2d.drawImage(tiles.getTile(), tiles.getX(), tiles.getY(), this);
        }
        for (Locker lockers : lockers) {
            g2d.drawImage(lockers.getLocker(), lockers.getX(), lockers.getY(), this);
        }


        uiPanel.repaint();

        //Draw snitches
        snitches = model.getSnitches();
        List<Snitch> copy = new ArrayList<>(snitches);
        int score = Snitch.getInitialHealth();
        for (Snitch snitch : snitches) {
            if (snitch.getHealth() <= 0) {
                copy.remove(snitch);
                snitches = copy;
                model.addScore(score);
            }
        }
        model.setSnitches(snitches);

        Iterator<Snitch> snitchCopy = copy.iterator();

        while (snitchCopy.hasNext()) {
            Snitch snitch = snitchCopy.next();
            snitch.updateSnitchPosition();
            g2d.drawRect(snitch.getX() + 18, snitch.getY(), 25, 4);
            g2d.setColor(Color.red);
            g2d.fillRect(snitch.getX() + 18, snitch.getY(), 25 * (snitch.getHealth()) / 30, 4);
            g2d.drawImage(snitch.getSprite(), snitch.getX(), snitch.getY(), this);
        }

        //Draw projectiles
        for (Projectile bullet : bullets) {
            if (bullets.size() > 0) {
                if (!bullet.getTargetHit()) {
                    bullet.move();
                    bullet.updateProjectile();
                    g2d.drawImage(bullet.getSprite(), bullet.getX(), bullet.getY(), this);
                }
            }
        }

        //Draw boss
        if (model.getCurrentLevel() == Model.CurrentLevel.HALLWAY_TWO) {
            boss = model.getBoss();
            bossPos = model.getBossPos();
            if (boss.getHealth() > 0) {
                boss.updateSnitchPosition();
                boss.move();
                g2d.drawRect(boss.getX() + 18, boss.getY(), 25, 4);
                g2d.setColor(Color.red);
                g2d.fillRect(boss.getX() + 18, boss.getY(), 25 * (boss.getHealth()) / 80, 4);
                g2d.drawImage(boss.getSprite(), boss.getX(), boss.getY(), this);

                enemyBullet = model.getEnemyBullet();

                for (Projectile bullet : enemyBullet) {
                    if (!bullet.getTargetHit()) {
                        bullet.move();
                        bullet.updateProjectile();
                        g2d.drawImage(bullet.getSprite(), bullet.getX(), bullet.getY(), this);
                    }
                }
            } else {
                model.addScore(80);
                model.setBossIsDead();
                model.addScore(80);
            }

        }

        //Player damage indicator
        if (model.getPlayerHurt()) {
            player1.changeAnimationState(13);
            g2d.drawImage(player1.getSprite(), player1.getX(), player1.getY(), this);
        } else {
            g2d.drawImage(player1.getSprite(), player1.getX(), player1.getY(), this);
        }
        model.setPlayerHurt();

        //Draw map
        for (WallOver wallBottom : wallBottom) {
            g2d.drawImage(wallBottom.getWallOver(), wallBottom.getX(), wallBottom.getY(), this);
        }
        for (Wall wall : walls){
            //System.out.println("Drawing Wall: " + wall.getX() + "," + wall.getY());
            g2d.drawImage(wall.getWall(), wall.getX(), wall.getY(),this);
        }
        for (HealthItem health : healthItems) {
            if (!model.getHealthStatus()) {
                g2d.drawImage(health.getHealthItem(), health.getX(), health.getY(), this);
            }
        }
        for (RangedAttackItem item : rangedAttack) {
            if (!player1.getRangedAttack() && !model.snitchesAlive()) {
                g2d.drawImage(item.getRangedItem(), item.getX(), item.getY(), this);
            }
        }

    }

    //Game tick
    private void step() {

        player1.move();

        for (Snitch snitch : snitches) {
            snitch.updateSnitchPosition();
            snitch.move();
            repaint(snitch.getX(), snitch.getY(), snitch.getWidth(), snitch.getHeight());
        }

        repaint(player1.getX()-2, player1.getY()-2,
                player1.getWidth()+4, player1.getHeight()+4);
    }


    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            //System.out.println("Key Released");
            controller.keyIsReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            //System.out.println("Key Pressed");
            controller.keyIsPressed(e);
        }
    }


    //OVERRIDES
    //
    @Override
    public void actionPerformed(ActionEvent e) {
        //System.out.println("Action performed");
        //grabFocus();
        isPaused = model.isPaused();
        controller.incrementCoolDown();
        if(!isPaused && !gameOver) {
            requestFocusInWindow();
            step();
            if (model != null) {
                model.checkHealth();
                model.checkCollisions();
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(!gameOver && !isPaused){
            doDrawing(g);
        }

        Toolkit.getDefaultToolkit().sync();
    }


    //SETTERS & GETTERS
    //
    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        model.setPaused(paused);
        isPaused = paused;
    }

    public int[][] getTiles() {

        tilePos = model.getTilePos();
        return tilePos;
    }

    public void gameOver(){
        gameOver = true;
    }

    public void setController(Controller controller){
        this.controller = controller;
    }
}
