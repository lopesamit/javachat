
import java.awt.Color;
import java.awt.Graphics;

public class WhiteBoard extends ChatMessage {

    int x1, y1, x2, y2;	
    static Color co;
    public String flag, privatname,drawflag;

    public WhiteBoard(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        setColors(co);
    }

    WhiteBoard() {

    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
    public String getDrawFlag() {
    	//drawflag="blue";
        return drawflag;
    }

    public void setDrawFlag(String drawflag) {
        this.drawflag = drawflag;
        //System.out.println(drawflag);
    }
    public String getPriname() {
        return privatname;
    }

    public void setPriname(String privatname) {
        this.privatname = privatname;
    }

    public void paint(Graphics g) {
    	//g.setColor(Color.blue);
        g.drawLine(x1, y1, x2, y2);
        
    }

    public int getx1() {
        return x1;
    }

    public int gety1() {
        return y1;
    }

    public int getx2() {
        return x2;
    }

    public int gety2() {
        return y2;
    }
    public void setColors(Color co){
    	WhiteBoard.co=co;
    	//System.out.println(" in white board   "+co);
    }
    public Color getColors(){
    	return co;
    }
    
}
