package group23;

import group23.ai.AStar;
import group23.ai.Node;
import group23.mapGraphics.*;

import javax.sound.sampled.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static group23.Main.controller;

public class Model {

    Model model;
    Canvas canvas;
    Player player1;
    AStar aStar;
    ArrayList<Node> path;
    MapArray mapArray;
    Thread thread;

    public int score = 0;
    private int imageSize = 32;
    int s = imageSize;

    public boolean playerHurt = false;
    private boolean roomOneCleared = false;
    private boolean roomTwoCleared = false;
    private boolean roomThreeCleared = false;
    private boolean healthPickedUp = false;
    private boolean rangedAttackPickedUp = false;
    private boolean advanceConditionSound = false;
    private boolean bossIsDead = false;
    protected boolean isPaused = false;

    protected LinkedList<Projectile> bullet = new LinkedList<Projectile>();
    protected LinkedList<Projectile> enemyBullet = new LinkedList<>();
    protected List<Wall> walls;
    protected List<WallOver> wallBottom;
    protected List<Locker> lockers;
    protected List<Floor> tiles;
    protected List<AdvanceTile> advanceTile;
    protected List<ReturnTile> returnTile;
    protected List<Snitch> snitches;
    protected List<HealthItem> healthItem;
    protected List<RangedAttackItem> rangedAttack;
    protected Principal boss;

    public static CurrentLevel currentLevel = CurrentLevel.HALLWAY_ONE;

    public enum CurrentLevel {
        HALLWAY_ONE,
        ROOM_ONE,
        HALLWAY_TWO,
    }

    public Model() {
        model = this;
        player1 = new Player();
        mapArray = new MapArray();
        initArrays();
        //System.out.println("Size of tilepos " + tilePos3.length);
    }

    private void initArrays() {
        snitches = new ArrayList<>();
        tiles = new ArrayList<>();
        walls = new ArrayList<>();
        lockers = new ArrayList<>();
        advanceTile = new ArrayList<>();
        returnTile = new ArrayList<>();
        wallBottom = new ArrayList<>();
        healthItem = new ArrayList<>();
        rangedAttack = new ArrayList<>();
        initGraphics();
    }

    private void initGraphics() {
        /*
        initialise all position where map items will be drawn
         */
        initLocker();
        initTile();
        initWall();
        initWallBottom();
        initAdvanceTile();
        initReturnTile();
        initSnitches();
        initHealth();
        initRangedAttack();
    }

    private void initRangedAttack() {
        if (getCurrentLevel() == CurrentLevel.HALLWAY_ONE) {
            for (int[] p : rangedAttackPos) {
                rangedAttack.add(new RangedAttackItem(p[0], p[1]));
            }
        }
    }

    private void initHealth() {
        if (getCurrentLevel() == CurrentLevel.ROOM_ONE) {
            for (int[] p : healthPos) {
                healthItem.add(new HealthItem(p[0], p[1]));
            }
        }
    }

    private void initLocker() {
        if (getCurrentLevel() == CurrentLevel.HALLWAY_ONE) {
            for (int[] p : lockerPos) {
                lockers.add(new Locker(p[0], p[1]));
            }
        } else if (getCurrentLevel() == CurrentLevel.ROOM_ONE) {
            for (int[] p : lockerPos2) {
                lockers.add(new Locker(p[0], p[1]));
            }
        } else if (getCurrentLevel() == CurrentLevel.HALLWAY_TWO) {
            for (int[] p : lockerPos3) {
                lockers.add(new Locker(p[0], p[1]));
            }
        }
    }

    private void initTile() {

    /*
    depending on current level state tiles array is re initialised to make new level
     */

        if (getCurrentLevel() == CurrentLevel.HALLWAY_ONE) {
            tilePos = mapArray.readTilePos(1, tilePos);
            for (int[] p : tilePos) {
                tiles.add(new Floor(p[0], p[1]));
            }
        } else if (getCurrentLevel() == CurrentLevel.ROOM_ONE) {
            tilePos2 = mapArray.readTilePos(2, tilePos2);
            for (int[] p : tilePos2) {
                tiles.add(new Floor(p[0], p[1]));
            }
        } else if (getCurrentLevel() == CurrentLevel.HALLWAY_TWO) {
            tilePos3 = mapArray.readTilePos(3, tilePos3);
            for (int[] p : tilePos3) {
                tiles.add(new Floor(p[0], p[1]));
            }
        }
        //System.out.println("dbg");
        arrayStuff();
        //System.out.println(tilePos3[0][1]);
    }

    private void initWallBottom() {
        /*
        type 1 - wallBottom
        type 2 - wallIslandTopLeft
        type 3 - wallIslandTopRight

        */
        if (getCurrentLevel() == CurrentLevel.HALLWAY_ONE) {
            for (int[] p : wallOverPos) {
                wallBottom.add(new WallOver(p[0], p[1], 1));
            }
            for (int[] p : wallIslandTopRight) {
                wallBottom.add(new WallOver(p[0], p[1], 3));
            }
        } else if (getCurrentLevel() == CurrentLevel.ROOM_ONE) {
            for (int[] p : wallOverPos2) {
                wallBottom.add(new WallOver(p[0], p[1], 1));
            }
            for (int[] p : wallIslandTopLeft2) {
                wallBottom.add(new WallOver(p[0], p[1], 2));
            }
            for (int[] p : wallIslandTopRight2) {
                wallBottom.add(new WallOver(p[0], p[1], 3));
            }
        } else if (getCurrentLevel() == CurrentLevel.HALLWAY_TWO) {
            for (int[] p : wallOverPos3) {
                wallBottom.add(new WallOver(p[0], p[1], 1));
            }
            for (int[] p : wallIslandTopLeft3) {
                //System.out.println("adding wall to list: " + p[0] + "," + p[1]);
                wallBottom.add(new WallOver(p[0], p[1], 2));
            }
            for (int[] p : wallIslandTopRight3) {
                wallBottom.add(new WallOver(p[0], p[1], 3));
            }
        }
    }

    private void initWall() {
        /*
                type 1 -- wall
                type 2 -- wallLeft
                type 3 -- wallRight
                type 4 -- wallBottomLeft
                type 5 -- wallBottomRight
                type 6 -- wallTopLeft
                type 7 -- wallTopRight
                type 8 -- wallIslandBottomLeft
                type 9 -- wallIslandBottomRight
                type 10 - backgroundFiller

                loads different images based on which map feature we wish to draw
         */

        if (getCurrentLevel() == CurrentLevel.HALLWAY_ONE) {
            for (int[] p : wallPos) {
                //System.out.println("adding wall to list: " + p[0] + "," + p[1]);
                walls.add(new Wall(p[0], p[1], 1));
            }
            for (int[] p : wallLeftPos) {
                //System.out.println("adding wall to list: " + p[0] + "," + p[1]);
                walls.add(new Wall(p[0], p[1], 2));
            }
            for (int[] p : wallRightPos) {
                //System.out.println("adding wall to list: " + p[0] + "," + p[1]);
                walls.add(new Wall(p[0], p[1], 3));
            }
            for (int[] p : wallBottomLeftPos) {
                //System.out.println("adding wall to list: " + p[0] + "," + p[1]);
                walls.add(new Wall(p[0], p[1], 4));
            }
            for (int[] p : wallBottomRightPos) {
                //System.out.println("adding wall to list: " + p[0] + "," + p[1]);
                walls.add(new Wall(p[0], p[1], 5));
            }
            for (int[] p : wallTopRightPos) {
                //System.out.println("adding wall to list: " + p[0] + "," + p[1]);
                walls.add(new Wall(p[0], p[1], 7));
            }
            for (int[] p : wallIslandBottomLeft) {
                //System.out.println("adding wall to list: " + p[0] + "," + p[1]);
                walls.add(new Wall(p[0], p[1], 8));
            }
            backgroundFillerPos = mapArray.readFillerPos(1, backgroundFillerPos);
            for (int[] p : backgroundFillerPos) {
                //System.out.println("adding wall to list: " + p[0] + "," + p[1]);
                walls.add(new Wall(p[0], p[1], 10));
            }
        } else if (getCurrentLevel() == CurrentLevel.ROOM_ONE) {
            for (int[] p : wallPos2) {
                //System.out.println("adding wall to list: " + p[0] + "," + p[1]);
                walls.add(new Wall(p[0], p[1], 1));
            }
            for (int[] p : wallLeftPos2) {
                //System.out.println("adding wall to list: " + p[0] + "," + p[1]);
                walls.add(new Wall(p[0], p[1], 2));
            }
            for (int[] p : wallRightPos2) {
                //System.out.println("adding wall to list: " + p[0] + "," + p[1]);
                walls.add(new Wall(p[0], p[1], 3));
            }
            for (int[] p : wallBottomLeftPos2) {
                //System.out.println("adding wall to list: " + p[0] + "," + p[1]);
                walls.add(new Wall(p[0], p[1], 4));
            }
            for (int[] p : wallBottomRightPos2) {
                //System.out.println("adding wall to list: " + p[0] + "," + p[1]);
                walls.add(new Wall(p[0], p[1], 5));
            }
            for (int[] p : wallTopLeftPos2) {
                //System.out.println("adding wall to list: " + p[0] + "," + p[1]);
                walls.add(new Wall(p[0], p[1], 6));
            }
            for (int[] p : wallIslandBottomLeft2) {
                //System.out.println("adding wall to list: " + p[0] + "," + p[1]);
                walls.add(new Wall(p[0], p[1], 8));
            }
            for (int[] p : wallIslandBottomRight2) {
                //System.out.println("adding wall to list: " + p[0] + "," + p[1]);
                walls.add(new Wall(p[0], p[1], 9));
            }
            backgroundFillerPos2 = mapArray.readFillerPos(2, backgroundFillerPos2);
            for (int[] p : backgroundFillerPos2) {
                walls.add(new Wall(p[0], p[1], 10));
            }
        } else if (getCurrentLevel() == CurrentLevel.HALLWAY_TWO) {
            for (int[] p : wallPos3) {
                //System.out.println("adding wall to list: " + p[0] + "," + p[1]);
                walls.add(new Wall(p[0], p[1], 1));
            }
            for (int[] p : wallLeftPos3) {
                //System.out.println("adding wall to list: " + p[0] + "," + p[1]);
                walls.add(new Wall(p[0], p[1], 2));
            }
            for (int[] p : wallRightPos3) {
                //System.out.println("adding wall to list: " + p[0] + "," + p[1]);
                walls.add(new Wall(p[0], p[1], 3));
            }
            for (int[] p : wallBottomLeftPos3) {
                //System.out.println("adding wall to list: " + p[0] + "," + p[1]);
                walls.add(new Wall(p[0], p[1], 4));
            }
            for (int[] p : wallBottomRightPos3) {
                //System.out.println("adding wall to list: " + p[0] + "," + p[1]);
                walls.add(new Wall(p[0], p[1], 5));
            }
            for (int[] p : wallTopLeftPos3) {
                //System.out.println("adding wall to list: " + p[0] + "," + p[1]);
                walls.add(new Wall(p[0], p[1], 6));
            }
            for (int[] p : wallTopRightPos3) {
                //System.out.println("adding wall to list: " + p[0] + "," + p[1]);
                walls.add(new Wall(p[0], p[1], 7));
            }
            for (int[] p : wallIslandBottomLeft3) {
                //System.out.println("adding wall to list: " + p[0] + "," + p[1]);
                walls.add(new Wall(p[0], p[1], 8));
            }
            for (int[] p : wallIslandBottomRight3) {
                //System.out.println("adding wall to list: " + p[0] + "," + p[1]);
                walls.add(new Wall(p[0], p[1], 9));
            }
            backgroundFillerPos3 = mapArray.readFillerPos(3, backgroundFillerPos3);
            for (int[] p : backgroundFillerPos3) {
                //System.out.println("adding wall to list: " + p[0] + "," + p[1]);
                walls.add(new Wall(p[0], p[1], 10));
            }
        }

    }

    private void initAdvanceTile() {

        /*
        type 1 - vertical
        type 2 - horizontal
         */

        if (getCurrentLevel() == CurrentLevel.HALLWAY_ONE) {
            for (int[] p : advancePos) {
                //System.out.println("adding wall to list: " + p[0] + "," + p[1]);
                advanceTile.add(new AdvanceTile(p[0], p[1], 1));
            }
        } else if (getCurrentLevel() == CurrentLevel.ROOM_ONE) {
            for (int[] p : advancePos2) {
                //System.out.println("adding wall to list: " + p[0] + "," + p[1]);
                advanceTile.add(new AdvanceTile(p[0], p[1], 2));
            }
        } else if (getCurrentLevel() == CurrentLevel.HALLWAY_TWO) {
            for (int[] p : advancePos2) {
                //System.out.println("adding wall to list: " + p[0] + "," + p[1]);
                advanceTile.add(new AdvanceTile(p[0], p[1], 2));
            }
        }
    }

    private void initReturnTile() {

        /*
        type 1 - vertical
        type 2 - horizontal
         */

        if (getCurrentLevel() == CurrentLevel.HALLWAY_ONE) {
            for (int[] p : returnPos) {
                //System.out.println("adding wall to list: " + p[0] + "," + p[1]);
                returnTile.add(new ReturnTile(p[0], p[1], 1));
            }
        } else if (getCurrentLevel() == CurrentLevel.ROOM_ONE) {
            for (int[] p : returnPos2) {
                //System.out.println("adding wall to list: " + p[0] + "," + p[1]);
                returnTile.add(new ReturnTile(p[0], p[1], 1));
            }
        } else if (getCurrentLevel() == CurrentLevel.HALLWAY_TWO) {
            for (int[] p : returnPos3) {
                //System.out.println("adding wall to list: " + p[0] + "," + p[1]);
                returnTile.add(new ReturnTile(p[0], p[1], 2));
            }
        }
    }

    private void initSnitches() {

        //generates starting array of snitches

        if (getCurrentLevel() == CurrentLevel.HALLWAY_ONE) {
            for (int[] p : snitchPos1) {
                snitches.add(new Snitch(p[0], p[1]));
            }

            aStar = new AStar(model.getTilePos());
        } else if (getCurrentLevel() == CurrentLevel.ROOM_ONE) {
            for (int[] p : snitchPos1) {
                snitches.add(new Snitch(p[0], p[1]));
            }

        } else if (getCurrentLevel() == CurrentLevel.HALLWAY_TWO) {

        }
    }

    public void initBoss() {

        boss = new Principal(6 * s, 14 * s);
    }

    public void checkCollisions() {

        /*
        Each collision method is only called when there are valid parameters
        Each call loops though a List to check weither certain objects or points intersect and if so what action to take
         */

        checkPlayerCollisions();

        if (snitches.size() > 0) {
            checkEnemyCollisions();
        }
        if (bullet.size() > 0) {
            checkProjectileCollisions();
        }
        if (!healthPickedUp && healthItem.size() > 0) {
            checkHealthPickedUp();
        }
        if (!rangedAttackPickedUp && rangedAttack.size() > 0 && !model.snitchesAlive()) {
            checkRangedPickedUp();
        }
        if (model.getCurrentLevel() == CurrentLevel.HALLWAY_TWO && boss != null && boss.getHealth() > 0) {
            checkBossCollisions();
        }
    }

    private void checkBossCollisions() {
        Rectangle rPlayer = player1.getBounds();

        Rectangle rBoss = boss.getBounds();

        //uses object bounds to set up points to use as conditions in collision detection
        //if conditions are met then the object is stopped from moving through the wall

        int bossLeft = (int) rBoss.getMinX() + 30;
        int bossRight = (int) rBoss.getMaxX() - 30;
        int bossTop = (int) rBoss.getMinY() + 20;
        int bossBottom = (int) rBoss.getMaxY() - 20;

        Point pointRightTop = new Point(bossRight + 1, bossTop);
        Point pointRightMiddle = new Point(bossRight + 1, bossBottom + boss.getHeight() / 2);

        Point pointLeftTop = new Point(bossLeft - 1, bossTop);
        Point pointLeftMiddle = new Point(bossLeft - 1, bossBottom + boss.getHeight() / 2);

        Point pointTopLeft = new Point(bossLeft, bossTop - 1);
        Point pointTopRight = new Point(bossRight, bossTop - 1);

        Point pointBottomLeft = new Point(bossLeft, bossBottom + 1);
        Point pointBottomRight = new Point(bossRight, bossBottom + 1);

        if (rPlayer.intersects(rBoss)) {
            if (rPlayer.contains(pointRightTop) || rPlayer.contains(pointBottomRight) || rPlayer.contains(pointRightMiddle)) {
                //System.out.println("pR");

                boss.setX(boss.getX() - 3);
                boss.setDx(-1);
                bossPlayerDamage(boss);

            } else if (rPlayer.contains(pointLeftTop) || rPlayer.contains(pointBottomLeft) || rPlayer.contains(pointLeftMiddle)) {
                //System.out.println("pL");

                boss.setX(boss.getX() + 3);
                boss.setDx(1);
                boss.damage(20);
                bossPlayerDamage(boss);
            }

            if (rPlayer.contains(pointTopLeft) || rPlayer.contains(pointTopRight)) {

                boss.setY(boss.getY() + 3);
                boss.setDy(1);
                boss.damage(20);
                bossPlayerDamage(boss);

            } else if (rPlayer.contains(pointBottomLeft) || rPlayer.contains(pointBottomRight)) {

                boss.setY(boss.getY() - 3);
                boss.setDy(-1);
                boss.damage(20);
                bossPlayerDamage(boss);
            }
        }

        for (Projectile bullet : enemyBullet) {

            //loops through bosses bullets to assess collisions in playing window with different objects

            Rectangle rBullet = bullet.getBounds();

            for (Wall wall : walls) {
                Rectangle rWall = wall.getBounds();
                if (rBullet.intersects(rWall)) {
                    bullet.setTargetHit(true);
                }
            }

            for (AdvanceTile aTile : advanceTile) {
                Rectangle rATile = aTile.getBounds();
                if (rBullet.intersects(rATile)) {
                    bullet.setTargetHit(true);
                }
            }

            for (ReturnTile rTile : returnTile) {
                Rectangle rRTile = rTile.getBounds();
                if (rBullet.intersects(rRTile)) {
                    bullet.setTargetHit(true);
                }
            }

            if (rBullet.intersects(rPlayer)) {
                if (!bullet.getTargetHit()) {
                    player1.damage(20);
                    player1.changeAnimationState(13);
                }
                bullet.setTargetHit(true);
            }
        }


        for (Wall wall : walls) {

            Rectangle rWall = wall.getBounds();

            if (rBoss.intersects(rWall)) {

                if (rWall.contains(pointRightTop) || rWall.contains(pointBottomRight) || rWall.contains(pointRightMiddle)) {
                    //System.out.println("pR");
                    boss.setDx(0);
                    boss.setX(boss.getX() - 2);
                } else if (rWall.contains(pointLeftTop) || rWall.contains(pointBottomLeft) || rWall.contains(pointLeftMiddle)) {
                    //System.out.println("pL");
                    boss.setDx(0);
                    boss.setX(boss.getX() + 2);
                }

                if (rWall.contains(pointTopLeft) || rWall.contains(pointTopRight)) {
                    boss.setDy(0);
                    boss.setY(boss.getY() + 2);
                } else if (rWall.contains(pointBottomLeft) || rWall.contains(pointBottomRight)) {
                    boss.setDy(0);
                    boss.setY(boss.getY() - 2);
                }
            }
        }
    }

    private void checkRangedPickedUp() {
        //checks for player picking up item
        Rectangle rPlayer = player1.getBounds();

        for (RangedAttackItem item : rangedAttack) {

            Rectangle rItem = item.getBounds();

            if (rPlayer.intersects(rItem)) {
                player1.attackPickedUp();
                thread = new Thread(new Sounds("res/sounds/rangedAttack.wav"));
                thread.start();
                rangedAttackPickedUp = true;
            }
        }
    }

    private void checkHealthPickedUp() {
        //checks for player picking up item
        Rectangle rPlayer = player1.getBounds();

        for (HealthItem health : healthItem) {

            Rectangle rHealth = health.getBounds();

            if (rPlayer.intersects(rHealth)) {
                player1.heal(80);
                thread = new Thread(new Sounds("res/sounds/healthPickedUp.wav"));
                thread.start();
                healthPickedUp = true;
            }
        }

    }

    private void checkProjectileCollisions() {

        for (Projectile bullet : bullet) {
            //loops through players projectiles fired to check collisions with other objects
            //sets properties according to collisions -- one setTarget is triggered bullet isnt drawn again
            Rectangle rBullet = bullet.getBounds();

            for (Wall wall : walls) {
                Rectangle rWall = wall.getBounds();
                if (rBullet.intersects(rWall)) {
                    bullet.setTargetHit(true);
                }
            }

            for (AdvanceTile aTile : advanceTile) {
                Rectangle rATile = aTile.getBounds();
                if (rBullet.intersects(rATile)) {
                    bullet.setTargetHit(true);
                }
            }

            for (ReturnTile rTile : returnTile) {
                Rectangle rRTile = rTile.getBounds();
                if (rBullet.intersects(rRTile)) {
                    bullet.setTargetHit(true);
                }
            }

            if (snitches.size() > 0) {
                for (Snitch snitch : snitches) {
                    if (snitch.getHealth() > 0) {
                        Rectangle rSnitch = snitch.getBounds();

                        if (rBullet.intersects(rSnitch)) {
                            if (!bullet.getTargetHit()) {
                                snitch.damage(10);
                                if (snitch.getHealth() > 0) {
                                    thread = new Thread(new Sounds("res/sounds/hit_damage.wav"));
                                    thread.start();
                                } else {
                                    thread = new Thread(new Sounds("res/sounds/snitchDying.wav"));
                                    thread.start();
                                }
                            }
                            bullet.setTargetHit(true);
                        }
                    }
                }
            }

            if (boss != null && getCurrentLevel() == CurrentLevel.HALLWAY_TWO) {
                Rectangle rBoss = boss.getBounds();

                if (rBullet.intersects(rBoss)) {
                    if (!bullet.getTargetHit()) {
                        boss.damage(15);
                        if (boss.getHealth() > 0) {
                            thread = new Thread(new Sounds("res/sounds/hit_damage.wav"));
                            thread.start();
                        } else {
                            thread = new Thread(new Sounds("res/sounds/snitchDying.wav"));
                            thread.start();
                        }
                    }
                    bullet.setTargetHit(true);
                }
            }

        }
    }

    private void checkEnemyCollisions() {

        int snitchLeft;
        int snitchRight;
        int snitchTop;
        int snitchBottom;

        Rectangle rPlayer = player1.getBounds();

        for (Snitch snitch : snitches) {
            if (snitch.getHealth() > 0) {
                Rectangle rSnitch = snitch.getBounds();

                if (Player.getAnimationState() == Player.Animation.ATTACK_UP || Player.getAnimationState() == Player.Animation.ATTACK_DOWN || Player.getAnimationState() == Player.Animation.ATTACK_LEFT || Player.getAnimationState() == Player.Animation.ATTACK_RIGHT) {
                    snitchLeft = (int) rSnitch.getMinX();
                    snitchRight = (int) rSnitch.getMaxX();
                    snitchTop = (int) rSnitch.getMinY();
                    snitchBottom = (int) rSnitch.getMaxY();
                } else {
                    snitchLeft = (int) rSnitch.getMinX() + 30;
                    snitchRight = (int) rSnitch.getMaxX() - 30;
                    snitchTop = (int) rSnitch.getMinY() + 20;
                    snitchBottom = (int) rSnitch.getMaxY() - 20;
                }

                Point pointRightTop = new Point(snitchRight + 1, snitchTop);
                Point pointRightMiddle = new Point(snitchRight + 1, snitchBottom + snitch.getHeight() / 2);

                Point pointLeftTop = new Point(snitchLeft - 1, snitchTop);
                Point pointLeftMiddle = new Point(snitchLeft - 1, snitchBottom + snitch.getHeight() / 2);

                Point pointTopLeft = new Point(snitchLeft, snitchTop - 1);
                Point pointTopRight = new Point(snitchRight, snitchTop - 1);

                Point pointBottomLeft = new Point(snitchLeft, snitchBottom + 1);
                Point pointBottomRight = new Point(snitchRight, snitchBottom + 1);

                if (rPlayer.intersects(rSnitch)) {
                    //System.out.println("OUCH");

                    if (rPlayer.contains(pointRightTop) || rPlayer.contains(pointBottomRight) || rPlayer.contains(pointRightMiddle)) {
                        //System.out.println("pR");

                        snitch.setX(snitch.getX() - 3);
                        snitch.setDx(-1);
                        snitchPlayerDamage(snitch);

                    } else if (rPlayer.contains(pointLeftTop) || rPlayer.contains(pointBottomLeft) || rPlayer.contains(pointLeftMiddle)) {
                        //System.out.println("pL");

                        snitch.setX(snitch.getX() + 3);
                        snitch.setDx(1);
                        snitchPlayerDamage(snitch);
                    }

                    if (rPlayer.contains(pointTopLeft) || rPlayer.contains(pointTopRight)) {

                        snitch.setY(snitch.getY() + 3);
                        snitch.setDy(1);
                        snitchPlayerDamage(snitch);
                    } else if (rPlayer.contains(pointBottomLeft) || rPlayer.contains(pointBottomRight)) {

                        snitch.setY(snitch.getY() - 3);
                        snitch.setDy(-1);
                        snitchPlayerDamage(snitch);
                    }
                    //System.out.println(player1.getHealth());

                }

                for (Wall wall : walls) {

                    Rectangle rWall = wall.getBounds();

                    if (rSnitch.intersects(rWall)) {

                        if (rWall.contains(pointRightTop) || rWall.contains(pointBottomRight) || rWall.contains(pointRightMiddle)) {
                            //System.out.println("pR");
                            snitch.setDx(0);
                            snitch.setX(snitch.getX() - 2);
                        } else if (rWall.contains(pointLeftTop) || rWall.contains(pointBottomLeft) || rWall.contains(pointLeftMiddle)) {
                            //System.out.println("pL");
                            snitch.setDx(0);
                            snitch.setX(snitch.getX() + 2);
                        }

                        if (rWall.contains(pointTopLeft) || rWall.contains(pointTopRight)) {
                            snitch.setDy(0);
                            snitch.setY(snitch.getY() + 2);
                        } else if (rWall.contains(pointBottomLeft) || rWall.contains(pointBottomRight)) {
                            snitch.setDy(0);
                            snitch.setY(snitch.getY() - 2);
                        }
                    }
                }

            }
        }

    }

    public void checkPlayerCollisions() {

        // System.out.println("Checking collisions");

        Rectangle rPlayer = player1.getBounds();
        //System.out.println(rPlayer);

        int playerLeft = (int) rPlayer.getMinX() + 20;
        int playerRight = (int) rPlayer.getMaxX() - 20;
        int playerTop = (int) rPlayer.getMinY() + 8;
        int playerBottom = (int) rPlayer.getMaxY() - 13;

        Point pointRightTop = new Point(playerRight + 1, playerTop);
        Point pointRightMiddle = new Point(playerRight + 1, playerBottom - player1.getHeight() / 2);


        Point pointLeftTop = new Point(playerLeft - 1, playerTop);
        Point pointLeftMiddle = new Point(playerLeft - 1, playerBottom - player1.getHeight() / 2);

        Point pointTopLeft = new Point(playerLeft, playerTop - 1);
        Point pointTopRight = new Point(playerRight, playerTop - 1);

        Point pointBottomLeft = new Point(playerLeft, playerBottom + 1);
        Point pointBottomRight = new Point(playerRight, playerBottom + 1);
        //System.out.println(pointBottomRight + " " + pointRightMiddle + " " + pointRightTop);
        for (Wall wall : walls) {

            Rectangle rWall = wall.getBounds();

            if (rPlayer.intersects(rWall)) {

                if (rWall.contains(pointRightTop) || rWall.contains(pointBottomRight) || rWall.contains(pointRightMiddle)) {
                    //System.out.println("pR");
                    player1.setDx(0);
                    player1.setX(player1.getX() - 2);
                } else if (rWall.contains(pointLeftTop) || rWall.contains(pointBottomLeft) || rWall.contains(pointLeftMiddle)) {
                    //System.out.println("pL");
                    player1.setDx(0);
                    player1.setX(player1.getX() + 2);
                }

                if (rWall.contains(pointTopLeft) || rWall.contains(pointTopRight)) {
                    player1.setDy(0);
                    player1.setY(player1.getY() + 2);
                } else if (rWall.contains(pointBottomLeft) || rWall.contains(pointBottomRight)) {
                    player1.setDy(0);
                    player1.setY(player1.getY() - 2);
                }
            }
        }

        for (AdvanceTile advanceTile : advanceTile) {

            Rectangle rAdv = advanceTile.getBounds();

            if (rPlayer.intersects(rAdv)) {
                if (snitchesAlive() == false) {
                    if (rAdv.contains(pointTopRight) || rAdv.contains(pointTopLeft) || rAdv.contains(pointBottomLeft) || rAdv.contains(pointBottomRight)) {
                        if (boss == null) {
                            advanceConditionSound = false;
                        }
                        switch (getCurrentLevel()) {
                            case HALLWAY_ONE:
                                changeCurrentLevel(2);
                                roomOneCleared = true;
                                player1.setX(42);
                                player1.setY(512);
                                break;
                            case ROOM_ONE:
                                changeCurrentLevel(3);
                                advanceConditionSound = true;
                                roomTwoCleared = true;
                                if (!bossIsDead) {
                                    initBoss();
                                }
                                player1.setX((int) 38.5 * s);
                                player1.setY((int) 19 * s);
                        }

                        getWalls().clear();
                        getWallsOver().clear();
                        getLockers().clear();
                        getTile().clear();
                        getAdvanceTile().clear();
                        getReturnTile().clear();
                        getSnitches().clear();
                        getProjectiles().clear();
                        getHealthItem().clear();
                        getRangedAttackItem().clear();

                        initWall();
                        initWallBottom();
                        initLocker();
                        initTile();
                        initAdvanceTile();
                        initReturnTile();

                        switch (getCurrentLevel()) {
                            case ROOM_ONE:
                                if (!roomTwoCleared) {
                                    initSnitches();
                                }
                                if (!healthPickedUp) {
                                    initHealth();
                                    Thread thread = new Thread(new Sounds("res/sounds/medKitWarning.wav"));
                                    thread.start();
                                }
                                break;
                            case HALLWAY_TWO:
                                if (!roomThreeCleared) {
                                    initSnitches();
                                }
                        }

                        // System.out.println(getCurrentLevel());
                    }
                } else {
                    if (!advanceConditionSound) {
                        Sounds sound = new Sounds("res/sounds/progressCondition.wav");
                        Thread thread = new Thread(sound);
                        thread.start();
                    }
                    advanceConditionSound = true;
                }
            }
        }

        for (ReturnTile returnTile : returnTile) {

            Rectangle rReturn = returnTile.getBounds();

            if (rPlayer.intersects(rReturn) && getCurrentLevel() != CurrentLevel.HALLWAY_ONE) {
                if (snitchesAlive() == false) {
                    if (rReturn.contains(pointTopRight) || rReturn.contains(pointTopLeft) || rReturn.contains(pointBottomLeft) || rReturn.contains(pointBottomRight)) {

                        if (boss == null) {
                            advanceConditionSound = false;
                        }

                        switch (getCurrentLevel()) {
                            case ROOM_ONE:
                                changeCurrentLevel(1);
                                roomTwoCleared = true;
                                player1.setX(1340);
                                player1.setY(512);
                                break;
                            case HALLWAY_TWO:
                                changeCurrentLevel(2);
                                roomThreeCleared = true;
                                player1.setX((int) 38.5 * s);
                                player1.setY(1 * s);
                                break;
                        }

                        getWalls().clear();
                        getWallsOver().clear();
                        getLockers().clear();
                        getTile().clear();
                        getAdvanceTile().clear();
                        getReturnTile().clear();
                        getSnitches().clear();
                        getProjectiles().clear();
                        getHealthItem().clear();
                        getRangedAttackItem().clear();

                        initWall();
                        initWallBottom();
                        initLocker();
                        initTile();
                        initAdvanceTile();
                        initReturnTile();

                        switch (getCurrentLevel()) {
                            case HALLWAY_ONE:
                                if (!rangedAttackPickedUp) {
                                    initRangedAttack();
                                }
                                break;
                            case ROOM_ONE:
                                if (!healthPickedUp) {
                                    initHealth();
                                }
                                break;
                        }

                        //System.out.println(getCurrentLevel());
                    }
                } else {
                    if (!advanceConditionSound) {
                        Sounds sound = new Sounds("res/sounds/progressCondition.wav");
                        Thread thread = new Thread(sound);
                        thread.start();
                    }
                    advanceConditionSound = true;
                }
            }
        }
    }

    public static CurrentLevel changeCurrentLevel(int newState) {
        if (newState == 1) {
            currentLevel = CurrentLevel.HALLWAY_ONE;
        } else if (newState == 2) {
            currentLevel = CurrentLevel.ROOM_ONE;
        } else if (newState == 3) {
            currentLevel = CurrentLevel.HALLWAY_TWO;
        }
        return currentLevel;
    }

    public boolean getPlayerHurt() {
        return playerHurt;
    }

    public boolean getHealthStatus() {
        return healthPickedUp;
    }

    public void checkHealth() {

        //Once the player or boss has died the game state is changed to gameOver

        //System.out.println("Check health");

        if (player1.getHealth() <= 0 || bossIsDead) {
            //System.out.println("Change State");
            Main.changeState(5);
            canvas.gameOverPanel.setWin(bossIsDead);
            canvas.panelState();
        }
    }

    public void setBossIsDead() {
        bossIsDead = true;
    }

    public boolean getBossIsDead() { return bossIsDead; }

    public void setPlayerHurt() {
        playerHurt = false;
    }

    private void snitchPlayerDamage(Snitch snitch) {

        if (Player.getAnimationState() == Player.Animation.ATTACK_UP || Player.getAnimationState() == Player.Animation.ATTACK_DOWN || Player.getAnimationState() == Player.Animation.ATTACK_LEFT || Player.getAnimationState() == Player.Animation.ATTACK_RIGHT) {
            snitch.damage(15);
        } else {
            player1.damage(10);
            playerHurt = true;
            snitch.damage(5);

        }

        if (snitch.getHealth() > 0) {
            thread = new Thread(new Sounds("res/sounds/hit_damage.wav"));
            thread.start();
        } else {
            thread = new Thread(new Sounds("res/sounds/snitchDying.wav"));
            thread.start();
        }

    }

    private void bossPlayerDamage(Principal snitch) {

        if (Player.getAnimationState() == Player.Animation.ATTACK_UP || Player.getAnimationState() == Player.Animation.ATTACK_DOWN || Player.getAnimationState() == Player.Animation.ATTACK_LEFT || Player.getAnimationState() == Player.Animation.ATTACK_RIGHT) {
            snitch.damage(15);
        } else {
            player1.damage(10);
            playerHurt = true;
            snitch.damage(5);

        }

        if (snitch.getHealth() > 0) {
            thread = new Thread(new Sounds("res/sounds/hit_damage.wav"));
            thread.start();
        } else {
            thread = new Thread(new Sounds("res/sounds/snitchDying.wav"));
            thread.start();
        }

    }

    public void cheat() {

        //function called to advance to boss level with picked up items

        changeCurrentLevel(3);
        roomTwoCleared = true;
        initBoss();
        player1.setX((int) 38.5 * s);
        player1.setY(19 * s);

        player1.attackPickedUp();
        player1.heal(100);

        getWalls().clear();
        getWallsOver().clear();
        getLockers().clear();
        getTile().clear();
        getAdvanceTile().clear();
        getReturnTile().clear();
        getSnitches().clear();
        getProjectiles().clear();
        getHealthItem().clear();
        getRangedAttackItem().clear();

        initWall();
        initWallBottom();
        initLocker();
        initTile();
        initAdvanceTile();
        initReturnTile();
    }

    public boolean snitchesAlive() {
        for (Snitch snitch : snitches) {
            if (snitch.getHealth() > 0) {
                return true;
            }
        }
        return false;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public void setSnitches(List<Snitch> snitches) {
        this.snitches = snitches;
    }

    public LinkedList<Projectile> getProjectiles() {
        return bullet;
    }

    public LinkedList<Projectile> getEnemyBullet() {
        return enemyBullet;
    }

    public LinkedList<Projectile> addProjectiles(int enemyType) {
        //1 -- up
        //2 -- right
        //3 -- down
        //4 -- left

        if (enemyType == 1) {
            if (Player.getAnimationState() == Player.Animation.UP || Player.getAnimationState() == Player.Animation.REST_UP) {
                bullet.add(new Projectile(player1.getX() + 25, player1.getY(), 1, 1));
            } else if (Player.getAnimationState() == Player.Animation.RIGHT || Player.getAnimationState() == Player.Animation.REST_RIGHT) {
                bullet.add(new Projectile(player1.getX() + 25, player1.getY() + 30, 2, 1));
            } else if (Player.getAnimationState() == Player.Animation.DOWN || Player.getAnimationState() == Player.Animation.REST_DOWN) {
                bullet.add(new Projectile(player1.getX() + 25, player1.getY() + 25, 3, 1));
            } else if (Player.getAnimationState() == Player.Animation.LEFT || Player.getAnimationState() == Player.Animation.REST_LEFT) {
                bullet.add(new Projectile(player1.getX(), player1.getY() + 30, 4, 1));
            }
        } else if (enemyType == 2) {
            enemyBullet.add(new Projectile(boss.getX() + 25, boss.getY(), 1, 2));
            enemyBullet.add(new Projectile(boss.getX() + 25, boss.getY(), 2, 2));
            enemyBullet.add(new Projectile(boss.getX() + 25, boss.getY(), 3, 2));
            enemyBullet.add(new Projectile(boss.getX() + 25, boss.getY(), 4, 2));

        }
        return bullet;
    }

    public int getScore() {
        return score;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Principal getBoss() {
        return boss;
    }

    public List<RangedAttackItem> getRangedAttackItem() {
        return rangedAttack;
    }

    public List<HealthItem> getHealthItem() {
        return healthItem;
    }

    public List<Locker> getLockers() {
        return lockers;
    }

    public List<Floor> getTile() {
        return tiles;
    }

    public List<Wall> getWalls() {
        return walls;
    }

    public List<WallOver> getWallsOver() {
        return wallBottom;
    }

    public List<ReturnTile> getReturnTile() {
        return returnTile;
    }

    public List<AdvanceTile> getAdvanceTile() {
        return advanceTile;
    }

    public List<Snitch> getSnitches() {
        return snitches;
    }

    public int[][] getBossPos() {
        return bossPos;
    }

    public int[][] getRangedAttackPos() {
        return rangedAttackPos;
    }

    public int[][] getHealthPos() {
        return healthPos;
    }

    public int[][] getLockerPos() {
        return lockerPos;
    }

    public int[][] getWallPos() {
        return wallPos;
    }

    public int[][] getWallOverPos() {
        return wallOverPos;
    }

    public static CurrentLevel getCurrentLevel() {
        return currentLevel;
    }

    public int[][] getTilePos() {

        if (getCurrentLevel() == CurrentLevel.HALLWAY_ONE) {
            return tilePos;
        } else if (getCurrentLevel() == CurrentLevel.ROOM_ONE) {
            return tilePos2;
        } else {
            return tilePos3;
        }
    }

    public int[][] getReturnPos() {
        return returnPos;
    }

    public int[][] getAdvancePos() {
        return advancePos;
    }

    public final int[][] bossPos = {
            {21 * s, 9 * s}
    };

    public final int[][] healthPos = {
            {7 * s, 6 * s}
    };

    public final int[][] rangedAttackPos = {
            {40 * s, 17 * s}
    };

    public int[][] backgroundFillerPos = new int[487][2];

    public int[][] backgroundFillerPos2 = new int[376][2];

    public int[][] backgroundFillerPos3 = new int[41][2];

    private final int[][] wallPos = {
            {4 * s, 2 * s}, {5 * s, 2 * s}, {6 * s, 2 * s}, {7 * s, 2 * s}, {8 * s, 2 * s}, {9 * s, 2 * s}, {10 * s, 2 * s}, {11 * s, 2 * s}, {12 * s, 2 * s}, {13 * s, 2 * s}, {14 * s, 2 * s}, {15 * s, 2 * s}, {16 * s, 2 * s}, {17 * s, 2 * s}, {18 * s, 2 * s}, {19 * s, 2 * s}, {20 * s, 2 * s}, {21 * s, 2 * s}, {22 * s, 2 * s}, {23 * s, 2 * s}, {24 * s, 2 * s}, {25 * s, 2 * s}, {26 * s, 2 * s}, {27 * s, 2 * s}, {28 * s, 2 * s}, {29 * s, 2 * s}, {30 * s, 2 * s},
            {32 * s, 13 * s}, {33 * s, 13 * s}, {34 * s, 13 * s}, {35 * s, 13 * s}, {36 * s, 13 * s}, {37 * s, 13 * s}, {38 * s, 13 * s}, {39 * s, 13 * s}, {40 * s, 13 * s}, {41 * s, 13 * s}, {42 * s, 13 * s}, {43 * s, 13 * s}, {44 * s, 13 * s},
    };

    private final int[][] wallPos2 = {
            {5 * s, 2 * s}, {6 * s, 2 * s}, {7 * s, 2 * s}, {8 * s, 2 * s}, {9 * s, 2 * s}, {16 * s, 2 * s}, {17 * s, 2 * s}, {18 * s, 2 * s}, {19 * s, 2 * s}, {20 * s, 2 * s}, {21 * s, 2 * s}, {22 * s, 2 * s}, {23 * s, 2 * s}, {24 * s, 2 * s}, {25 * s, 2 * s}, {26 * s, 2 * s}, {27 * s, 2 * s}, {28 * s, 2 * s}, {29 * s, 2 * s}, {30 * s, 2 * s}, {31 * s, 2 * s}, {32 * s, 2 * s}, {33 * s, 2 * s}, {34 * s, 2 * s}, {35 * s, 2 * s},
            {0 * s, 13 * s}, {1 * s, 13 * s}, {2 * s, 13 * s}, {3 * s, 13 * s}, {11 * s, 13 * s}, {12 * s, 13 * s}, {13 * s, 13 * s}, {14 * s, 13 * s}, {25 * s, 13 * s}, {26 * s, 13 * s}, {27 * s, 13 * s}, {28 * s, 13 * s},

    };

    private final int[][] wallPos3 = {
            {5 * s, 4 * s}, {6 * s, 4 * s}, {7 * s, 4 * s}, {8 * s, 4 * s}, {9 * s, 4 * s}, {10 * s, 4 * s}, {11 * s, 4 * s}, {12 * s, 4 * s}, {13 * s, 4 * s}, {14 * s, 4 * s}, {15 * s, 4 * s}, {16 * s, 4 * s}, {17 * s, 4 * s}, {26 * s, 4 * s}, {27 * s, 4 * s}, {28 * s, 4 * s}, {29 * s, 4 * s}, {30 * s, 4 * s}, {31 * s, 4 * s}, {32 * s, 4 * s}, {33 * s, 4 * s}, {34 * s, 4 * s}, {35 * s, 4 * s}, {36 * s, 4 * s}, {37 * s, 4 * s}, {38 * s, 4 * s}, {39 * s, 4 * s},
            {4 * s, 17 * s}, {5 * s, 17 * s}, {6 * s, 17 * s}, {7 * s, 17 * s}, {8 * s, 17 * s}, {9 * s, 17 * s}, {10 * s, 17 * s}, {11 * s, 17 * s}, {12 * s, 17 * s}, {13 * s, 17 * s}, {14 * s, 17 * s}, {15 * s, 17 * s}, {16 * s, 17 * s}, {17 * s, 17 * s}, {26 * s, 17 * s}, {27 * s, 17 * s}, {28 * s, 17 * s}, {29 * s, 17 * s}, {30 * s, 17 * s}, {31 * s, 17 * s}, {32 * s, 17 * s}, {33 * s, 17 * s}, {34 * s, 17 * s}, {35 * s, 17 * s}, {36 * s, 17 * s}, {37 * s, 17 * s}, {38 * s, 17 * s}, {39 * s, 17 * s}, {40 * s, 17 * s},
            {1 * s, 0 * s}, {2 * s, 0 * s}, {3 * s, 0 * s}, {4 * s, 0 * s}, {5 * s, 0 * s}, {6 * s, 0 * s}, {7 * s, 0 * s}, {8 * s, 0 * s}, {9 * s, 0 * s}, {10 * s, 0 * s}, {11 * s, 0 * s}, {12 * s, 0 * s}, {13 * s, 0 * s}, {14 * s, 0 * s}, {15 * s, 0 * s}, {16 * s, 0 * s}, {17 * s, 0 * s}, {18 * s, 0 * s}, {19 * s, 0 * s}, {20 * s, 0 * s}, {21 * s, 0 * s}, {22 * s, 0 * s}, {23 * s, 0 * s}, {24 * s, 0 * s}, {25 * s, 0 * s}, {26 * s, 0 * s}, {27 * s, 0 * s}, {28 * s, 0 * s}, {29 * s, 0 * s}, {30 * s, 0 * s}, {31 * s, 0 * s}, {32 * s, 0 * s}, {33 * s, 0 * s}, {34 * s, 0 * s}, {35 * s, 0 * s}, {36 * s, 0 * s}, {37 * s, 0 * s}, {38 * s, 0 * s}, {39 * s, 0 * s}, {40 * s, 0 * s}, {41 * s, 0 * s}, {42 * s, 0 * s}, {43 * s, 0 * s}


    };

    public final int[][] wallOverPos = {
            {4 * s, 8 * s}, {5 * s, 8 * s}, {6 * s, 8 * s}, {7 * s, 8 * s}, {8 * s, 8 * s}, {9 * s, 8 * s}, {10 * s, 8 * s}, {11 * s, 8 * s}, {12 * s, 8 * s},
            {14 * s, 19 * s}, {15 * s, 19 * s}, {16 * s, 19 * s}, {17 * s, 19 * s}, {18 * s, 19 * s}, {19 * s, 19 * s}, {20 * s, 19 * s}, {21 * s, 19 * s}, {22 * s, 19 * s}, {23 * s, 19 * s}, {24 * s, 19 * s}, {25 * s, 19 * s}, {26 * s, 19 * s}, {27 * s, 19 * s}, {28 * s, 19 * s}, {29 * s, 19 * s}, {30 * s, 19 * s}, {31 * s, 19 * s}, {32 * s, 19 * s}, {33 * s, 19 * s}, {34 * s, 19 * s}, {35 * s, 19 * s}, {36 * s, 19 * s}, {37 * s, 19 * s}, {38 * s, 19 * s}, {39 * s, 19 * s}, {40 * s, 19 * s}, {41 * s, 19 * s}, {42 * s, 19 * s}, {43 * s, 19 * s}, {44 * s, 19 * s},

    };

    public final int[][] wallOverPos2 = {
            {0 * s, 18 * s}, {1 * s, 18 * s}, {2 * s, 18 * s}, {3 * s, 18 * s}, {4 * s, 18 * s}, {5 * s, 18 * s}, {6 * s, 18 * s}, {7 * s, 18 * s}, {8 * s, 18 * s}, {9 * s, 18 * s}, {10 * s, 18 * s}, {11 * s, 18 * s}, {12 * s, 18 * s}, {13 * s, 18 * s}, {14 * s, 18 * s}, {15 * s, 18 * s}, {16 * s, 18 * s}, {17 * s, 18 * s}, {18 * s, 18 * s}, {19 * s, 18 * s}, {20 * s, 18 * s}, {21 * s, 18 * s}, {22 * s, 18 * s}, {23 * s, 18 * s},
            {24 * s, 18 * s}, {25 * s, 18 * s}, {26 * s, 18 * s}, {27 * s, 18 * s}, {28 * s, 18 * s}, {29 * s, 18 * s}, {30 * s, 18 * s}, {31 * s, 18 * s}, {32 * s, 18 * s}, {33 * s, 18 * s}, {34 * s, 18 * s}, {35 * s, 18 * s}, {36 * s, 18 * s}, {37 * s, 18 * s}, {38 * s, 18 * s}, {39 * s, 18 * s}, {40 * s, 18 * s},

            {25 * s, 6 * s}, {26 * s, 6 * s}, {27 * s, 6 * s}, {28 * s, 6 * s},
    };

    public final int[][] wallOverPos3 = {
            {4 * s, 3 * s}, {5 * s, 3 * s}, {6 * s, 3 * s}, {7 * s, 3 * s}, {8 * s, 3 * s}, {9 * s, 3 * s}, {10 * s, 3 * s}, {11 * s, 3 * s}, {12 * s, 3 * s}, {13 * s, 3 * s}, {14 * s, 3 * s}, {15 * s, 3 * s}, {16 * s, 3 * s}, {17 * s, 3 * s}, {26 * s, 3 * s}, {27 * s, 3 * s}, {28 * s, 3 * s}, {29 * s, 3 * s}, {30 * s, 3 * s}, {31 * s, 3 * s}, {32 * s, 3 * s}, {33 * s, 3 * s}, {34 * s, 3 * s}, {35 * s, 3 * s}, {36 * s, 3 * s}, {37 * s, 3 * s}, {38 * s, 3 * s}, {39 * s, 3 * s}, {40 * s, 3 * s},
            {5 * s, 16 * s}, {6 * s, 16 * s}, {7 * s, 16 * s}, {8 * s, 16 * s}, {9 * s, 16 * s}, {10 * s, 16 * s}, {11 * s, 16 * s}, {12 * s, 16 * s}, {13 * s, 16 * s}, {14 * s, 16 * s}, {15 * s, 16 * s}, {16 * s, 16 * s}, {17 * s, 16 * s}, {26 * s, 16 * s}, {27 * s, 16 * s}, {28 * s, 16 * s}, {29 * s, 16 * s}, {30 * s, 16 * s}, {31 * s, 16 * s}, {32 * s, 16 * s}, {33 * s, 16 * s}, {34 * s, 16 * s}, {35 * s, 16 * s}, {36 * s, 16 * s}, {37 * s, 16 * s}, {38 * s, 16 * s}, {39 * s, 16 * s},
            {1 * s, 20 * s}, {2 * s, 20 * s}, {3 * s, 20 * s}, {4 * s, 20 * s}, {5 * s, 20 * s}, {6 * s, 20 * s}, {7 * s, 20 * s}, {8 * s, 20 * s}, {9 * s, 20 * s}, {10 * s, 20 * s}, {11 * s, 20 * s}, {12 * s, 20 * s}, {13 * s, 20 * s}, {14 * s, 20 * s}, {15 * s, 20 * s}, {16 * s, 20 * s}, {17 * s, 20 * s}, {18 * s, 20 * s}, {19 * s, 20 * s}, {20 * s, 20 * s}, {21 * s, 20 * s}, {22 * s, 20 * s}, {23 * s, 20 * s}, {24 * s, 20 * s}, {25 * s, 20 * s}, {26 * s, 20 * s}, {27 * s, 20 * s}, {28 * s, 20 * s}, {29 * s, 20 * s}, {30 * s, 20 * s}, {31 * s, 20 * s}, {32 * s, 20 * s}, {33 * s, 20 * s}, {34 * s, 20 * s}, {35 * s, 20 * s}, {36 * s, 20 * s}, {41 * s, 20 * s}, {42 * s, 20 * s}, {43 * s, 20 * s}

    };

    private final int[][] wallRightPos = {
            {31 * s, 3 * s}, {31 * s, 4 * s}, {31 * s, 5 * s}, {31 * s, 6 * s}, {31 * s, 7 * s}, {31 * s, 8 * s}, {31 * s, 9 * s}, {31 * s, 10 * s}, {31 * s, 11 * s}, {31 * s, 12 * s},


    };

    private final int[][] wallRightPos2 = {
            {41 * s, 0 * s}, {41 * s, 1 * s}, {41 * s, 2 * s}, {41 * s, 3 * s}, {41 * s, 4 * s}, {41 * s, 5 * s}, {41 * s, 6 * s}, {41 * s, 7 * s}, {41 * s, 8 * s}, {41 * s, 9 * s}, {41 * s, 10 * s}, {41 * s, 11 * s}, {41 * s, 12 * s}, {41 * s, 13 * s}, {41 * s, 14 * s}, {41 * s, 15 * s}, {41 * s, 16 * s}, {41 * s, 17 * s},
            {24 * s, 7 * s}, {24 * s, 8 * s}, {24 * s, 9 * s}, {24 * s, 10 * s}, {24 * s, 11 * s}, {24 * s, 12 * s},
            {10 * s, 3 * s}, {10 * s, 4 * s}, {10 * s, 5 * s}, {10 * s, 6 * s}, {10 * s, 7 * s}, {10 * s, 8 * s}, {10 * s, 9 * s}, {10 * s, 10 * s}, {10 * s, 11 * s}, {10 * s, 12 * s},

    };

    private final int[][] wallRightPos3 = {
            {3 * s, 4 * s}, {3 * s, 5 * s}, {3 * s, 6 * s}, {3 * s, 14 * s}, {3 * s, 15 * s}, {3 * s, 16 * s},
            {40 * s, 5 * s}, {40 * s, 6 * s}, {40 * s, 14 * s}, {40 * s, 15 * s},
            {44 * s, 1 * s}, {44 * s, 2 * s}, {44 * s, 3 * s}, {44 * s, 4 * s}, {44 * s, 5 * s}, {44 * s, 6 * s}, {44 * s, 7 * s}, {44 * s, 8 * s}, {44 * s, 9 * s}, {44 * s, 10 * s}, {44 * s, 11 * s}, {44 * s, 12 * s}, {44 * s, 13 * s}, {44 * s, 14 * s}, {44 * s, 15 * s}, {44 * s, 16 * s}, {44 * s, 17 * s}, {44 * s, 18 * s}, {44 * s, 19 * s}
    };

    private final int[][] wallLeftPos = {
            {3 * s, 3 * s}, {3 * s, 4 * s}, {3 * s, 5 * s}, {3 * s, 6 * s}, {3 * s, 7 * s},
            {13 * s, 9 * s}, {13 * s, 10 * s}, {13 * s, 11 * s}, {13 * s, 12 * s}, {13 * s, 13 * s}, {13 * s, 14 * s}, {13 * s, 15 * s}, {13 * s, 16 * s}, {13 * s, 17 * s}, {13 * s, 18 * s}
    };

    private final int[][] wallLeftPos2 = {
            {29 * s, 7 * s}, {29 * s, 8 * s}, {29 * s, 9 * s}, {29 * s, 10 * s}, {29 * s, 11 * s}, {29 * s, 12 * s},
            {4 * s, 3 * s}, {4 * s, 4 * s}, {4 * s, 5 * s}, {4 * s, 6 * s}, {4 * s, 7 * s}, {4 * s, 8 * s}, {4 * s, 9 * s}, {4 * s, 10 * s}, {4 * s, 11 * s}, {4 * s, 12 * s},
            {15 * s, 3 * s}, {15 * s, 4 * s}, {15 * s, 5 * s}, {15 * s, 6 * s}, {15 * s, 7 * s}, {15 * s, 8 * s}, {15 * s, 9 * s}, {15 * s, 10 * s}, {15 * s, 11 * s}, {15 * s, 12 * s},
            {36 * s, 0 * s}, {36 * s, 1 * s},

    };

    private final int[][] wallLeftPos3 = {
            {4 * s, 5 * s}, {4 * s, 6 * s}, {4 * s, 14 * s}, {4 * s, 15 * s},
            {41 * s, 4 * s}, {41 * s, 5 * s}, {41 * s, 6 * s}, {41 * s, 14 * s}, {41 * s, 15 * s}, {41 * s, 16 * s},
            {0 * s, 1 * s}, {0 * s, 2 * s}, {0 * s, 3 * s}, {0 * s, 4 * s}, {0 * s, 5 * s}, {0 * s, 6 * s}, {0 * s, 7 * s}, {0 * s, 8 * s}, {0 * s, 9 * s}, {0 * s, 10 * s}, {0 * s, 11 * s}, {0 * s, 12 * s}, {0 * s, 13 * s}, {0 * s, 14 * s}, {0 * s, 15 * s}, {0 * s, 16 * s}, {0 * s, 17 * s}, {0 * s, 18 * s}, {0 * s, 19 * s}

    };

    private final int[][] wallTopRightPos = {
            {3 * s, 8 * s}, {13 * s, 19 * s}
    };

    private final int[][] wallTopRightPos3 = {
            {0 * s, 21 * s}, {4 * s, 16 * s}, {0 * s, 20 * s}
    };

    private final int[][] wallBottomLeftPos = {
            {31 * s, 2 * s}
    };

    private final int[][] wallBottomLeftPos2 = {
            {10 * s, 2 * s}
    };
    private final int[][] wallBottomLeftPos3 = {
            {44 * s, 0 * s}, {40 * s, 4 * s}
    };

    private final int[][] wallBottomRightPos = {
            {3 * s, 2 * s}
    };

    private final int[][] wallBottomRightPos2 = {
            {4 * s, 2 * s}, {15 * s, 2 * s}
    };
    private final int[][] wallBottomRightPos3 = {
            {0 * s, 0 * s}, {4 * s, 4 * s}
    };

    private final int[][] wallTopLeftPos2 = {
            {41 * s, 18 * s}
    };

    private final int[][] wallTopLeftPos3 = {
            {44 * s, 21 * s}, {40 * s, 16 * s}, {44 * s, 20 * s}
    };

    private final int[][] wallIslandBottomLeft = {
            {31 * s, 13 * s}
    };

    private final int[][] wallIslandBottomRight2 = {
            {4 * s, 13 * s}, {15 * s, 13 * s}, {29 * s, 13 * s}, {36 * s, 2 * s}
    };

    private final int[][] wallIslandBottomRight3 = {
            {18 * s, 4 * s}, {18 * s, 17 * s}, {4 * s, 7 * s}, {41 * s, 7 * s}, {41 * s, 17 * s}
    };

    private final int[][] wallIslandBottomLeft2 = {
            {10 * s, 13 * s}, {24 * s, 13 * s},
    };

    private final int[][] wallIslandBottomLeft3 = {
            {25 * s, 4 * s}, {3 * s, 7 * s}, {40 * s, 7 * s}, {3 * s, 17 * s}, {25 * s, 17 * s}
    };

    private final int[][] wallIslandTopRight = {
            {13 * s, 8 * s}
    };

    private final int[][] wallIslandTopRight2 = {
            {29 * s, 6 * s}
    };

    private final int[][] wallIslandTopRight3 = {
            {18 * s, 3 * s}, {41 * s, 3 * s}, {4 * s, 13 * s}, {41 * s, 13 * s}, {18 * s, 16 * s}
    };


    private final int[][] wallIslandTopLeft2 = {
            {24 * s, 6 * s}
    };
    private final int[][] wallIslandTopLeft3 = {
            {3 * s, 3 * s}, {25 * s, 3 * s}, {3 * s, 13 * s}, {40 * s, 13 * s}, {25 * s, 16 * s}
    };

    private final int[][] lockerPos = {
            {4 * s, 3 * s}, {5 * s, 3 * s}, {6 * s, 3 * s}, {7 * s, 3 * s}, {8 * s, 3 * s}, {9 * s, 3 * s}, {10 * s, 3 * s}, {11 * s, 3 * s}, {12 * s, 3 * s}, {13 * s, 3 * s}, {14 * s, 3 * s}, {15 * s, 3 * s}, {16 * s, 3 * s}, {17 * s, 3 * s}, {18 * s, 3 * s}, {19 * s, 3 * s}, {20 * s, 3 * s}, {21 * s, 3 * s}, {22 * s, 3 * s}, {23 * s, 3 * s}, {24 * s, 3 * s}, {25 * s, 3 * s}, {26 * s, 3 * s}, {27 * s, 3 * s}, {28 * s, 3 * s}, {29 * s, 3 * s}, {30 * s, 3 * s},
            {31 * s, 14 * s}, {32 * s, 14 * s}, {33 * s, 14 * s}, {34 * s, 14 * s}, {35 * s, 14 * s}, {36 * s, 14 * s}, {37 * s, 14 * s}, {38 * s, 14 * s}, {39 * s, 14 * s}, {40 * s, 14 * s}, {41 * s, 14 * s}, {42 * s, 14 * s}, {43 * s, 14 * s}, {44 * s, 14 * s},

    };

    private final int[][] lockerPos2 = {
            {5 * s, 3 * s}, {6 * s, 3 * s}, {7 * s, 3 * s}, {8 * s, 3 * s}, {9 * s, 3 * s}, {16 * s, 3 * s}, {17 * s, 3 * s}, {18 * s, 3 * s}, {19 * s, 3 * s}, {20 * s, 3 * s}, {21 * s, 3 * s}, {22 * s, 3 * s}, {23 * s, 3 * s}, {24 * s, 3 * s}, {25 * s, 3 * s}, {26 * s, 3 * s}, {27 * s, 3 * s}, {28 * s, 3 * s}, {29 * s, 3 * s}, {30 * s, 3 * s}, {31 * s, 3 * s}, {32 * s, 3 * s}, {33 * s, 3 * s}, {34 * s, 3 * s}, {35 * s, 3 * s},
            {0 * s, 14 * s}, {1 * s, 14 * s}, {2 * s, 14 * s}, {3 * s, 14 * s}, {11 * s, 14 * s}, {12 * s, 14 * s}, {13 * s, 14 * s}, {14 * s, 14 * s}, {25 * s, 14 * s}, {26 * s, 14 * s}, {27 * s, 14 * s}, {28 * s, 14 * s},
            {4 * s, 14 * s}, {15 * s, 14 * s}, {29 * s, 14 * s}, {36 * s, 3 * s},
            {10 * s, 14 * s}, {24 * s, 14 * s},

    };

    private final int[][] lockerPos3 = {
            {1 * s, 1 * s}, {2 * s, 1 * s}, {3 * s, 1 * s}, {4 * s, 1 * s}, {5 * s, 1 * s}, {6 * s, 1 * s}, {7 * s, 1 * s}, {8 * s, 1 * s}, {9 * s, 1 * s}, {10 * s, 1 * s}, {11 * s, 1 * s}, {12 * s, 1 * s}, {13 * s, 1 * s}, {14 * s, 1 * s}, {15 * s, 1 * s}, {16 * s, 1 * s}, {17 * s, 1 * s}, {18 * s, 1 * s}, {19 * s, 1 * s}, {20 * s, 1 * s}, {21 * s, 1 * s}, {22 * s, 1 * s}, {23 * s, 1 * s}, {24 * s, 1 * s}, {25 * s, 1 * s}, {26 * s, 1 * s}, {27 * s, 1 * s}, {28 * s, 1 * s}, {29 * s, 1 * s}, {30 * s, 1 * s}, {31 * s, 1 * s}, {32 * s, 1 * s}, {33 * s, 1 * s}, {34 * s, 1 * s}, {35 * s, 1 * s}, {36 * s, 1 * s}, {37 * s, 1 * s}, {38 * s, 1 * s}, {39 * s, 1 * s}, {40 * s, 1 * s}, {41 * s, 1 * s}, {42 * s, 1 * s}, {43 * s, 1 * s},
            {3 * s, 18 * s}, {4 * s, 18 * s}, {5 * s, 18 * s}, {6 * s, 18 * s}, {7 * s, 18 * s}, {8 * s, 18 * s}, {9 * s, 18 * s}, {10 * s, 18 * s}, {11 * s, 18 * s}, {12 * s, 18 * s}, {13 * s, 18 * s}, {14 * s, 18 * s}, {15 * s, 18 * s}, {16 * s, 18 * s}, {17 * s, 18 * s}, {18 * s, 18 * s}, {25 * s, 18 * s}, {26 * s, 18 * s}, {27 * s, 18 * s}, {28 * s, 18 * s}, {29 * s, 18 * s}, {30 * s, 18 * s}, {31 * s, 18 * s}, {32 * s, 18 * s}, {33 * s, 18 * s}, {34 * s, 18 * s}, {35 * s, 18 * s}, {36 * s, 18 * s}, {37 * s, 18 * s}, {38 * s, 18 * s}, {39 * s, 18 * s}, {40 * s, 18 * s}, {41 * s, 18 * s},
            {5 * s, 5 * s}, {6 * s, 5 * s}, {7 * s, 5 * s}, {8 * s, 5 * s}, {9 * s, 5 * s}, {10 * s, 5 * s}, {11 * s, 5 * s}, {12 * s, 5 * s}, {13 * s, 5 * s}, {14 * s, 5 * s}, {15 * s, 5 * s}, {16 * s, 5 * s}, {17 * s, 5 * s}, {18 * s, 5 * s}, {25 * s, 5 * s}, {26 * s, 5 * s}, {27 * s, 5 * s}, {28 * s, 5 * s}, {29 * s, 5 * s}, {30 * s, 5 * s}, {31 * s, 5 * s}, {32 * s, 5 * s}, {33 * s, 5 * s}, {34 * s, 5 * s}, {35 * s, 5 * s}, {36 * s, 5 * s}, {37 * s, 5 * s}, {38 * s, 5 * s}, {39 * s, 5 * s},
            {3 * s, 8 * s}, {4 * s, 8 * s}, {40 * s, 8 * s}, {41 * s, 8 * s},


    };


    private int[][] tilePos = new int[428][2];

    private int[][] tilePos2 = new int[536][2];


    private int[][] tilePos3 = new int[770][2];


    public void arrayStuff() {
        /*
        TO USE:
        Pick a row or column to generate, uncomment that section
        Change 'row' or column variable to whatever you need
        Change the start and end conditions of index in the loop (ie (int j = 4; j<= 16;j++) so it runs between 4 and 16)
        Run game
        Scroll up in console till you find printout
        Paste into array above
        Repeat


        EXAMPLE
        For Row 4, Columns 6-15 - it would look like so

        String row = "4";
        for(int col = 6; col <= 15; col++){

            System.out.print("{"+ col + "*s," + row + "*s},");
        }

        and it would print out
        {6*s,4*s},{7*s,4*s},{8*s,4*s},{9*s,4*s},{10*s,4*s},{11*s,4*s},{12*s,4*s},{13*s,4*s},{14*s,4*s},{15*s,4*s},
         */


        //GENERATE ROW
/*
        String row = "5";
        for (int col = 3; col <= 41; col++) {

            System.out.print("{" + col + "*s," + row + "*s},");
        }*/


        //GENERATE COLUMN
/*
        String col = "44";
        for(int row = 1; row <= 19; row++){
            System.out.print("{"+ col + "*s," + row + "*s},");
        }*/

        //System.out.println("");

        //System.out.println("size1 " + backgroundFillerPos.length );
        //System.out.println("size2 " + backgroundFillerPos2.length );
        //System.out.println("size3 " + backgroundFillerPos3.length );
        //System.out.println(" ");

    }


    private final int[][] advancePos = {
            {44 * s, 15 * s},
    };

    private final int[][] advancePos2 = {
            {37 * s, 0 * s}
    };

    private final int[][] returnPos = {
            {0 * s, 0 * s}
    };

    private final int[][] returnPos2 = {
            {0 * s, 15 * s}
    };
    private final int[][] returnPos3 = {
            {37 * s, 21 * s}
    };


    private final int[][] snitchPos1 = {
            {18 * s, 8 * s}, {18 * s, 15 * s}, {20 * s, 17 * s}
    };

    private final int[][] snitchPos2 = {
            {18 * s, 8 * s}, {18 * s, 15 * s}, {20 * s, 17 * s}
    };

}
