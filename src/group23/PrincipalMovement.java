package group23;

import group23.ai.AStar;
import group23.ai.Node;

import java.util.ArrayList;

public class PrincipalMovement implements Runnable {

    private Principal boss;
    private Node start;
    private Node goal;

    public PrincipalMovement(Principal boss, Node start, Node goal) {
        this.boss = boss;
        this.start = start;
        this.goal = goal;
    }

    public void run() {

        start = new Node((boss.getX() / 32), (boss.getY() / 32));
        goal = new Node(((Main.getModel().getPlayer1().getX() + 5) / 32), ((Main.getModel().getPlayer1().getY() + 5) / 32));

        boss.setThreadStarted(true);

        //A* algorithm
        AStar aStar = new AStar(Main.getModel().getCanvas().getGamePanel().getTiles());
        ArrayList<Node> pathFound = aStar.findPath(start, goal); //INPUT START & GOAL NODES

        if (pathFound != null) {
            for (int a = 0; a < pathFound.size(); a++) {

                //X Movement
                if (pathFound.get(a).getX() * 32 - boss.getX() > 0) {

                    boss.setDx(1);
                    boss.setDy(0);

                    try {

                        Thread.sleep(100);
                        boss.move();
                        Thread.sleep(100);
                        boss.move();

                    } catch (InterruptedException ex) {

                    }
                } else if (pathFound.get(a).getX() * 32 - boss.getX() < 0) {

                    boss.setDx(-1);
                    boss.setDy(0);

                    try {

                        Thread.sleep(100);
                        boss.move();
                        Thread.sleep(100);
                        boss.move();

                    } catch (InterruptedException ex) {

                    }
                }

                //Y Movement
                if (pathFound.get(a).getY() * 32 - boss.getY() > 0) {
                    boss.setDy(1);
                    boss.setDx(0);
                    try {

                        Thread.sleep(100);
                        boss.move();
                        Thread.sleep(100);
                        boss.move();

                    } catch (InterruptedException ex) {

                    }
                } else if (pathFound.get(a).getY() * 32 - boss.getY() < 0) {
                    boss.setDy(-1);
                    boss.setDx(0);
                    try {

                        Thread.sleep(100);
                        boss.move();
                        Thread.sleep(100);
                        boss.move();

                    } catch (InterruptedException ex) {

                    }
                }
            }
        }

        boss.setThreadStarted(false);
    }
}
