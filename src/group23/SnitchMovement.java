package group23;

import group23.ai.AStar;
import group23.ai.Node;

import java.util.ArrayList;

public class SnitchMovement implements Runnable {

    private Snitch snitch;
    private Node start;
    private Node goal;

    public SnitchMovement(Snitch snitch, Node start, Node goal) {
        this.snitch = snitch;
        this.start = start;
        this.goal = goal;
    }

    public void run() {

        start = new Node((snitch.getX() / 32), (snitch.getY() / 32));
        goal = new Node(((Main.getModel().getPlayer1().getX() + 5) / 32), ((Main.getModel().getPlayer1().getY() + 5) / 32));

        snitch.setThreadStarted(true);

        //AStar algorithm
        AStar aStar = new AStar(Main.getModel().getCanvas().getGamePanel().getTiles());
        ArrayList<Node> pathFound = aStar.findPath(start, goal); //INPUT START & GOAL NODES

        if (pathFound != null) {
            for (int a = 0; a < pathFound.size(); a++) {

                //Move X direction
                if (pathFound.get(a).getX() * 32 - snitch.getX() > 0) {

                    snitch.setDx(1);
                    snitch.setDy(0);

                    try {

                        Thread.sleep(100);
                        snitch.move();
                        Thread.sleep(100);
                        snitch.move();

                    } catch (InterruptedException ex) {

                    }
                } else if (pathFound.get(a).getX() * 32 - snitch.getX() < 0) {

                    snitch.setDx(-1);
                    snitch.setDy(0);

                    try {

                        Thread.sleep(100);
                        snitch.move();
                        Thread.sleep(100);
                        snitch.move();

                    } catch (InterruptedException ex) {

                    }
                }

                //Move Y direction
                if (pathFound.get(a).getY() * 32 - snitch.getY() > 0) {
                    snitch.setDy(1);
                    snitch.setDx(0);
                    try {

                        Thread.sleep(100);
                        snitch.move();
                        Thread.sleep(100);
                        snitch.move();

                    } catch (InterruptedException ex) {

                    }
                } else if (pathFound.get(a).getY() * 32 - snitch.getY() < 0) {
                    snitch.setDy(-1);
                    snitch.setDx(0);
                    try {

                        Thread.sleep(100);
                        snitch.move();
                        Thread.sleep(100);
                        snitch.move();

                    } catch (InterruptedException ex) {

                    }
                }
            }
        }

        //System.out.println("Thread Complete");


        snitch.setThreadStarted(false);
    }
}
