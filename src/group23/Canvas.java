package group23;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Canvas extends JFrame {

    MenuPanel menuPanel;
    IntroPanel introPanel;
    GamePanel gamePanel;
    JPanel jPanel;
    UIPanel uiPanel;
    PausePanel pausePanel;
    ExitPanel exitPanel;
    GameOverPanel gameOverPanel;

    public boolean introVoicePlayed = false;

    Thread thread;
    Sounds sounds;

    static Canvas initCanvas;

    Model model;
    Controller controller;

    //Setting up the JFrame
    public void initUI(Canvas canvas) {

        initCanvas = canvas;

        setTitle("Hall Pass");
        setSize(1440, 896);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initPanels();
    }

    //Initialising JPanels & setting the controller
    private void initPanels(){
        menuPanel = new MenuPanel(model);
        introPanel = new IntroPanel();
        uiPanel = new UIPanel(model);
        gamePanel = new GamePanel(model, uiPanel);
        pausePanel = new PausePanel();
        gameOverPanel = new GameOverPanel();
        exitPanel = new ExitPanel();


        jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.PAGE_AXIS));

        gamePanel.setController(controller);
        pausePanel.setController(controller);
        exitPanel.setController(controller);

        panelState();
        repaint();
        setResizable(false);
    }

    //Displayed panel depends on the Main state machine
    public void panelState() {

        Sounds sounds = new Sounds("res/sounds/menu.wav");

        switch(Main.getState()) {
            case MENU:
                //Display menu panel, hide other panels, start sound thread
                add(menuPanel);
                menuPanel.setVisible(true);
                introPanel.setVisible(false);
                gamePanel.setVisible(false);
                thread = new Thread(sounds);
                thread.start();
                break;

            case RUNNING:
                //Display JPanel(contains GamePanel & UIPanel)
                sounds.changeState();
                pausePanel.setPaused(false);
                menuPanel.setVisible(false);
                introPanel.setVisible(false);
                pausePanel.setVisible(false);
                exitPanel.setVisible(false);

                add(jPanel);
                jPanel.add(gamePanel);
                uiPanel.setMaximumSize(new Dimension(1440,160));
                uiPanel.setMinimumSize(new Dimension(1440,160));
                uiPanel.setPreferredSize(new Dimension(1440,160));
                uiPanel.setLocation(0,0);

                uiPanel.setVisible(true);
                jPanel.setVisible(true);

                jPanel.add(uiPanel);
                uiPanel.repaint();


                gamePanel.setVisible(true);
                //System.out.println("RUNNING");

                //Ensure intro voice only plays once
                if (!introVoicePlayed) {
                    Thread voiceOver = new Thread(new Sounds("res/sounds/introVoice.wav"));
                    voiceOver.start();
                    introVoicePlayed = true;
                }

                //Ensure game elements are running
                gamePanel.setPaused(false);
                pausePanel.setPaused(false);
                exitPanel.setPaused(false);
                break;

            case PAUSE:
                //Hide panels, show pause panel
                menuPanel.setVisible(false);
                introPanel.setVisible(false);
                jPanel.setVisible(false);

                add(pausePanel);
                pausePanel.setVisible(true);

                //Pause game logic
                pausePanel.setPaused(true);
                //System.out.println("PAUSED");
                break;

            case GAME_OVER:
                //Display gameOverPanel & hide others
                jPanel.setVisible(false);
                Dimension screen = new Dimension(1440,900);
                gameOverPanel.setMaximumSize(screen);
                gameOverPanel.setMinimumSize(screen);
                gameOverPanel.setSize(screen);
                gamePanel.gameOver();
                gameOverPanel.setVisible(true);

                //Calculate score
                if(model.getPlayer1().getHealth() > 0){
                    gameOverPanel.gameOver(model.getScore()*model.getPlayer1().getHealth());
                } else{
                    gameOverPanel.gameOver(model.getScore());
                }

                add(gameOverPanel);

                //Play failure sound if you lose
                if (!model.getBossIsDead()) {
                    Thread voiceOver = new Thread(new Sounds("res/sounds/missionFailed.wav"));
                    voiceOver.start();
                }
                break;

            case INTRO:
                //Add intro panel
                menuPanel.setVisible(false);
                gamePanel.setVisible(false);

                add(introPanel);
                introPanel.setVisible(true);

                break;

            case EXIT:
                //Hide all panels and display exit confirmation
                menuPanel.setVisible(false);
                introPanel.setVisible(false);
                jPanel.setVisible(false);
                uiPanel.setVisible(false);
                gamePanel.setVisible(false);

                add(exitPanel);

                //Pause game while confirmation screen displayed
                exitPanel.setVisible(true);
                exitPanel.setPaused(true);
                gamePanel.setPaused(true);
        }
    }


    //SETTERS & GETTERS
    public void setModel(Model model) {
        this.model = model;
    }
    public void setController(Controller controller){this.controller = controller;}
    public GamePanel getGamePanel() { return gamePanel; }
    public static Canvas getCanvas() {
        return initCanvas;
    }



}


