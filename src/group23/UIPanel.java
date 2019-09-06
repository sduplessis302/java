package group23;

import group23.mapGraphics.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UIPanel extends JPanel implements ActionListener {


    static Graphics2D g2d;
    protected Image background;
    protected ImageIcon icon;

    protected ImageIcon stick;
    protected Image stickPickedUp;

    protected ImageIcon noRanged;
    protected Image noRangedAttack;

    protected ImageIcon ranged;
    protected Image rangedAttack;

    protected ImageIcon wrap;
    protected Image wrapPic;

    protected ImageIcon health;
    protected Image healthTitle;

    protected ImageIcon a;
    protected Image aPic;

    protected ImageIcon s;
    protected Image sPic;

    protected ImageIcon v;
    protected Image vPic;

    protected ImageIcon w;
    protected Image wPic;

    protected ImageIcon r;
    protected Image rPic;

    protected ImageIcon t;
    protected Image tPic;

    protected  Controller controller;
    Model model;


    JButton menu_button = new JButton();

    public UIPanel(Model model){

        this.model = model;
        initUI();
    }

    //Load all images
    private void initUI(){

        icon = new ImageIcon("res/images/ui/ui_bottom.png");
        background = icon.getImage();

        stick = new ImageIcon("res/images/ui/stick_pickedUp.png");
        stickPickedUp = stick.getImage();

        ranged = new ImageIcon("res/images/ui/rangedAttackedPickedUp.png");
        rangedAttack = ranged.getImage();

        noRanged = new ImageIcon("res/images/ui/rangedAttackNotFound.png");
        noRangedAttack = noRanged.getImage();

        wrap = new ImageIcon("res/images/ui/wrap.png");
        wrapPic = wrap.getImage();

        health = new ImageIcon("res/images/ui/healthTitle.png");
        healthTitle = health.getImage();

        a = new ImageIcon("res/images/ui/a.png");
        aPic = a.getImage();

        s = new ImageIcon("res/images/ui/s.png");
        sPic = s.getImage();

        v = new ImageIcon("res/images/ui/roomOne.png");
        vPic = v.getImage();

        w = new ImageIcon("res/images/ui/roomTwo.png");
        wPic = w.getImage();

        r = new ImageIcon("res/images/ui/roomThree.png");
        rPic = r.getImage();

        t = new ImageIcon("res/images/ui/bossRoom.png");
        tPic = t.getImage();

    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);

        Toolkit.getDefaultToolkit().sync();
    }


    private void doDrawing(Graphics g) {

        g2d = (Graphics2D) g;

        g2d.drawImage(background,0,0,this);



        g2d.drawRect(580,22,238 ,110);
        g2d.setColor(Color.black);
        g2d.fillRect(580,22,238,110);


        //Weapon stats
        g2d.drawImage(stickPickedUp,600,37,this);

        if (!model.getPlayer1().getRangedAttack()) {
            g2d.drawImage(noRangedAttack, 720, 37, this);
        } else {
            g2d.drawImage(rangedAttack, 720, 37, this);
        }


        //UI elements
        g2d.drawImage(aPic, 612, 110, this );

        g2d.drawImage(sPic, 738, 110, this );

        g2d.drawImage(wrapPic, 580,12,this );

        g2d.drawImage(healthTitle, 77, 28, this);

        g2d.drawRect(77,80,256,31);
        g2d.setColor(Color.white);
        g2d.fillRect(77,80,256,31);


        //Room indicator
        if (model.getCurrentLevel() == Model.CurrentLevel.HALLWAY_ONE) {
            g2d.drawImage(vPic, 1000, 65, this );
        } else if (model.getCurrentLevel() == Model.CurrentLevel.ROOM_ONE) {
            g2d.drawImage(wPic, 1000, 65, this );
        } else if (model.getCurrentLevel() == Model.CurrentLevel.HALLWAY_TWO) {
            g2d.drawImage(tPic, 1000, 65, this );
        }

        //Health bar
        g2d.drawRect(80, 83, 250, 25);
        if (model.getPlayer1().getHealth() > 60) {
            g2d.setColor(Color.green);
        } else if (model.getPlayer1().getHealth() > 30) {
            g2d.setColor(Color.orange);
        } else {
            g2d.setColor(Color.red);
        }
        g2d.fillRect(80,83,250 *(model.getPlayer1().getHealth())/100,25);

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    public void setController(Controller controller){
        this.controller = controller;
    }
}
