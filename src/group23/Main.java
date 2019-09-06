package group23;


//import group23.Canvas;

import javax.swing.*;
import java.awt.*;

public class Main {

    public enum State {
        MENU, //newState 1
        RUNNING, //newState 2
        PAUSE, //newState 3
        GAME_OVER, //newState 4
        INTRO,//5
        EXIT
    }

    public  static Model model;
    public static Controller controller;
    public static Canvas canvas;

    public static State state = State.MENU;

    public static State getState() {
        return state;
    }

    public static State changeState(int newState) {

        if (newState == 1) {
            state = State.MENU;
        } else if (newState == 2){
            state = State.INTRO;
        } else if (newState == 3) {
            state = State.RUNNING;
        } else if (newState == 4) {
            state = State.PAUSE;
        } else if (newState == 5) {
            state = State.GAME_OVER;
        } else if(newState == 6){
            state = State.EXIT;
        }
        canvas.panelState();
        return state;
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            model = new Model();
            canvas = new Canvas();
            controller = new Controller(model);

            controller.setCanvas(canvas);

            canvas.setModel(model);
            canvas.setController(controller);
            canvas.setVisible(true);
            canvas.initUI(canvas);

            model.setCanvas(canvas);


        });


    }

    protected void initGame(){

    }

    public static Model getModel() { return model; }

}
