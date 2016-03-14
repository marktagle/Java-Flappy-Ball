import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Game extends JPanel implements KeyListener{

    //Variables for the game objects.
    FlappyBall player = new FlappyBall();
    Rectangle floor;

    //List of active pipes.
    ArrayList<Pipe> pipes = new ArrayList<Pipe>();

    Pipe sample = new Pipe(0);

    int GameState = 0;
    //0 = idle
    //1 = play
    //2 = game over

    boolean[] keys = new boolean[256];

    public void keyTyped(KeyEvent e){}
    public void keyPressed(KeyEvent e){
        keys[e.getKeyCode()] = true;
        //System.out.println(e.getKeyCode());
    }
    public void keyReleased(KeyEvent e){
        keys[e.getKeyCode()] = false;
    }

    public Game(){
        addKeyListener(this);
        setFocusable(true);
        Thread td = new Thread(new Runnable(){
           public void run(){
               setup();
               while(true){
                   try {
                       update();
                       render();
                       Thread.sleep(16);
                   } catch(Exception ex){

                   }
               }
           }
        });
        td.start();
    }

    void setup(){
        //Position player.
        player.bounds.x = 20;
        player.bounds.y = 50;

    }

    //variables for generating pipe
    int interval = 70;
    int step = 0;

    void update(){

        getInput();

        //prepare floor

        //prepare floor
        floor = new Rectangle(0, getHeight(), getWidth(), 50);

        if(step >= interval){
            AddPipe();
            step = 0;
        }
        step++;

        //Game PLAY
        //Move ball.
        if(GameState == 1) {
            player.bounds.y -= player.VelocityY;
            player.VelocityY--;

            //Render pipes
            for(Pipe x : pipes){
                x.OffsetX -= 5;
                x.UpdatePipe();
            }
        }

        //Check if game over - hits floor
        if(player.bounds.intersects(floor)){
            Die();
        }
        //Check if game over - hits pipe
        for(Pipe x : pipes) {
            if (x.IsPlayerCollides(player)) {
                Die();
            }
        }

        RemoveOffscreenPipe();
    }

    void Die(){
        GameState = 2;
        pipes.clear();
    }

    void RemoveOffscreenPipe(){
        int toRemove = -1;
        for(Pipe x : pipes){
            if(x.OffsetX < -x.PipeThickness){
                toRemove = pipes.indexOf(x);
            }
        }
        if(toRemove > -1) pipes.remove(toRemove);
    }

    boolean spacedown = false;
    void getInput(){
        //Spacebar.
        if(keys[32]){
            if(GameState == 0) {
                if(spacedown == false) GameState = 1;
            }
            else if(GameState == 2) {
                spacedown = true;
                player.bounds.x = 20;
                player.bounds.y = 50;
                player.VelocityY = 0;
                GameState = 0;
            }
            player.VelocityY = 7;
        } else if(keys[32] == false){
            spacedown = false;
        }
    }

    void render(){
        repaint();
    }

    void AddPipe(){
        Random rd = new Random();
        int r = rd.nextInt(3);
        System.out.println(r);
        Pipe p = new Pipe(r);
        //set pipe to rightmost corner
        p.OffsetX = 600;
        pipes.add(p);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.RED);

        //Render player
        g.fillOval(player.bounds.x, player.bounds.y, player.bounds.width, player.bounds.height);

        //Render pipes
        for(Pipe x : pipes){
            x.RenderPipe(g);
        }

        //Start screen
        if(GameState == 0){

            Font fnt = new Font("Arial", Font.BOLD, 20);
            g.setFont(fnt);
            g.setColor(Color.BLACK);
            g.drawString("TAP TO START", getWidth() / 2 - 60, getHeight() / 2);
        }

        //Dim screen
        if(GameState == 2) {
            //Dim screen
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, getWidth(), getHeight());

            //ADd game over message
            Font fnt = new Font("Arial", Font.BOLD, 20);
            g.setFont(fnt);
            g.setColor(Color.WHITE);
            g.drawString("GAME OVER", getWidth() / 2 - 60, getHeight() / 2);

        }

    }

    public static void main(String[] args){
        JFrame wnd =  new JFrame("Flappy Ball");
        wnd.setSize(600, 400);
        wnd.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        wnd.setContentPane(new Game());
        wnd.setVisible(true);
    }


}
