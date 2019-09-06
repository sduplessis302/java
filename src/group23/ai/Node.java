package group23.ai;

import java.util.List;

public class Node {

    int totalCost; //f(n)
    int currentCostFromStart; //g(n)
    int estimatedCostToGoal; //h(n)

    int x = 0;
    int y = 0;

    List neighbours;

    Node previousNode;

    public Node(int x, int y){
        currentCostFromStart = Integer.MAX_VALUE-10;
        totalCost = currentCostFromStart;
        this.x = x;
        this.y = y;
    }

    //Comparator function to check if 2 nodes have the same cost
    public int compareTo(Object object){
        Node node = (Node)object;
        int otherTotal = node.getTotalCost();
        if(totalCost == otherTotal){
            return 0;
        }
        else if(totalCost < otherTotal){
            return -1;
        }
        else{
            return 1;
        }
    }

    //Estimate cost to goal via Manhattan number as heuristic
    public void estimateCost(Node goal){
        int D = 1;
        int dx = Math.abs(this.x-goal.x);
        int dy = Math.abs(this.y - goal.y);
        estimatedCostToGoal =  D * (dx + dy);
        totalCost = currentCostFromStart + estimatedCostToGoal;
    }


    //OVERRIDES

    @Override
    public boolean equals(Object obj) {
        Node temp = (Node)obj;

        return temp.getX() == this.x && temp.getY() == this.y;
    }

    @Override
    public String toString(){
        String string = "(" + x + "," + y + ")";
        return string;
    }




    //GETTERS & SETTERS

    public void addNeighbour(Node neighbour){
        neighbours.add(neighbour);
    }


    public List getNeighbours(){
        return  neighbours;

    }

    public int getTotalCost() {
        totalCost = currentCostFromStart + estimatedCostToGoal;
        return totalCost;
    }

    public int getCurrentCostFromStart() {
        return currentCostFromStart;
    }

    public void setCurrentCostFromStart(int currentCostFromStart) {
        this.currentCostFromStart = currentCostFromStart;
    }

    public int getEstimatedCostToGoal() {
        return estimatedCostToGoal;
    }

    public void setEstimatedCostToGoal(int estimatedCostToGoal) {
        this.estimatedCostToGoal = estimatedCostToGoal;
    }

    public Node getPreviousNode() {
        return previousNode;
    }

    public void setPreviousNode(Node previousNode) {
        this.previousNode = previousNode;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }
}
