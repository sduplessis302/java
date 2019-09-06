package group23.ai;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AStar {

    protected ArrayList<Node> room;
    protected int imageSize = 32;


    public AStar(int[][]array){
        //System.out.println("Construct AStar");
        changeRoom(array);

    }

    //Pass in the tile array & turn it into a list of nodes
    public void changeRoom(int[][] array){
            room = new ArrayList<>();
            for(int i = 0; i < array.length;i++){
                int[] temp = array[i];
                int tempx = temp[0]/imageSize;
                int tempy = temp[1]/imageSize;

                room.add(new Node(tempx, tempy));
            }
    }

    //Creating a priority queue to keep nodes ordered
    public static class PriorityList extends LinkedList{
        public void add(Node node){
            for(int i = 0; i < size(); i++){
                if(node.compareTo(get(i)) <= 0){
                    add(i, node);
                    return;
                }
            }
            addLast(node);
        }
    }

    //Works backwards from goal node to start node, recording the path taken
    public ArrayList<Node> buildPath(Node node){
        LinkedList path = new LinkedList();
        while(node.getPreviousNode() != null){
            path.addFirst(node);
            node = node.getPreviousNode();

        }
        ArrayList<Node> aPath = new ArrayList<>(path);
        return aPath;
    }

    //Finds all the possible neighbours of a node and checks they exist in the room
    protected ArrayList<Node> findNeighbours(Node node){
        ArrayList<Node> neighbours = new ArrayList<>();

        int x = node.getX();
        int y = node.getY();

        Node up = null;
        Node down = null;
        Node left = null;
        Node right = null;

        if (room.contains(new Node(x, y - 2))) {
             up = new Node(x, y - 1);
        }
        if (room.contains(new Node(x, y + 2))) {
            down = new Node(x, y + 1);
        }
        if (room.contains(new Node(x - 2, y))) {
            left = new Node(x - 1, y);
        }
        if (room.contains(new Node(x + 2, y))) {
            right = new Node(x + 1, y);
        }

        neighbours.add(up);
        neighbours.add(down);
        neighbours.add(left);
        neighbours.add(right);

        return neighbours;

    }

    //A* algorithm, using Manhattan distance for the heuristic
    //Returns path as ArrayList<Node>
    public ArrayList<Node> findPath(Node start, Node goal) {

        //Check start node is in room
        if (!room.contains(start)) {
            start = new Node(start.getX() + 1, start.getY());
            if (!room.contains(start)) {
                start = new Node(start.getX(), start.getY() + 1);
            }
        }

        //Check goal node is in room
        if (!room.contains(goal)) {
            goal = new Node(goal.getX() + 1, goal.getY());
            if (!room.contains(goal)) {
                goal = new Node(goal.getX(), goal.getY() + 1);
            }
        }

        int startIndex = room.indexOf(start);
        int goalIndex = room.indexOf(goal);

        int currentIndex = startIndex;
        Node currentNode = room.get(currentIndex);

        // The set of nodes already evaluated
        ArrayList<Node> closedSet = new ArrayList<>();

        // The set of currently discovered nodes that are not evaluated yet.
        // Initially, only the start node is known.
        PriorityList openSet = new PriorityList();
        openSet.add(start);
        //System.out.println(openSet);

        // For each node, which node it can most efficiently be reached from.
        // If a node can be reached from many nodes, cameFrom will eventually contain the
        // most efficient previous step.
        //ArrayList<Node> cameFrom = new ArrayList<>();

        // For each node, the cost of getting from the start node to that node. gscore infinity
        for(int i = 0; i < room.size();i++){
            room.get(i).setCurrentCostFromStart(Integer.MAX_VALUE-10);
            //System.out.println(room.get(i).getCurrentCostFromStart());
        }

        // The cost of going from start to start is zero.
        room.get(startIndex).setCurrentCostFromStart(0);
        //System.out.println(room.get(startIndex).getCurrentCostFromStart());


        // For each node, the total cost of getting from the start node to the goal
        // by passing by that node. That value is partly known, partly heuristic. fscore infinity
        for(int i = 0; i < room.size();i++){
            room.get(i).setTotalCost(Integer.MAX_VALUE-10);
            //System.out.println(room.get(i).getTotalCost());
        }

        // For the first node, that value is completely heuristic.
        room.get(startIndex).estimateCost(goal);
        //System.out.println(room.get(startIndex).getEstimatedCostToGoal());

        while(!openSet.isEmpty()){

            currentNode = (Node)openSet.getFirst();
            if(currentNode.equals(goal)){
                ArrayList<Node> path = buildPath(room.get(goalIndex));
                return path;
            }

            openSet.removeFirst();
            closedSet.add(currentNode);

            //Get list of neighbours & loop
            ArrayList<Node> neighbours = findNeighbours(currentNode);
            for(int i = 0; i < neighbours.size();i++){
                Node neighbour = neighbours.get(i);

                //Check if neighbour is valid
                if(room.contains(neighbour)){

                    int index = room.indexOf(neighbour);
                    neighbour = room.get(index);

                    if(closedSet.contains(neighbour)){
                        continue; //Ignore neighbour as it has already been evaluated
                    }

                    //Dist from start to neighbour
                    int tempGScore = currentNode.getCurrentCostFromStart() + 1;

                    if(!openSet.contains(neighbour)){ //Discovered new node
                        neighbour.setPreviousNode(currentNode);
                        neighbour.setCurrentCostFromStart(tempGScore);
                        neighbour.estimateCost(goal);
                        openSet.add(neighbour);
                        continue;
                    } else if(tempGScore >= neighbour.getCurrentCostFromStart()){
                        continue;
                    }

                    //Best path found for this node
                    neighbour.setPreviousNode(currentNode);
                    neighbour.setCurrentCostFromStart(tempGScore);
                    neighbour.estimateCost(goal);

                }

            }

        }
        return null;

    }

}
