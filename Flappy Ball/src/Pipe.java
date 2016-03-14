import java.awt.*;
public class Pipe {

    public int OffsetX = 0;
    int PipeThickness = 80;
    int type = 0;

    Rectangle p1 = new Rectangle(0, 0, 0, 0);
    Rectangle p2 = new Rectangle(0, 0, 0, 0);

    public Pipe(int t){
        type = t;
    }

    public void RenderPipe(Graphics g){
        g.setColor(Color.GREEN);
        g.fillRect(p1.x, p1.y, p1.width, p1.height);
        g.fillRect(p2.x, p2.y, p2.width, p2.height);
    }

    public boolean IsPlayerCollides(FlappyBall p){
        if(p.bounds.intersects(p1) || p.bounds.intersects(p2)){
            return true;
        }
        return false;
    }

    public void UpdatePipe(){
        if(type == 0){
            p1 = new Rectangle(OffsetX, 150, PipeThickness, 500);
        } else if(type == 1){
            p1 = new Rectangle(OffsetX, 200, PipeThickness, 500);
        } else if(type == 2){
            p1 = new Rectangle(OffsetX, 300, PipeThickness, 500);
            p2= new Rectangle(OffsetX, 0, PipeThickness, 170);
        }
    }


}
