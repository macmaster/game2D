package games;

import java.awt.*;

import javax.swing.JFrame;

public class FullScreen extends JFrame {

    private final static int RESOLUTION_WIDTH = 1360;
    private final static int RESOLUTION_HEIGHT = 768;
    private final static int RESOLUTION_COLOR_DEPTH = 768;

    private DisplayMode displayMode;
    private GraphicsEnvironment environment;
    private GraphicsDevice device;

    public static void main(String[] args) {
        System.out.println("Hello World!");
        DisplayMode displayMode = new DisplayMode(FullScreen.RESOLUTION_WIDTH, FullScreen.RESOLUTION_HEIGHT,
                FullScreen.RESOLUTION_COLOR_DEPTH, DisplayMode.REFRESH_RATE_UNKNOWN);
        FullScreen screen = new FullScreen(displayMode);
        screen.display(1000);
        System.out.println("Closing...");
    }

    public FullScreen(DisplayMode displayMode) {
        this.displayMode = displayMode;
        this.environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        this.device = environment.getDefaultScreenDevice();
        
        // extra specs
        this.setUndecorated(true);
        this.setResizable(false);
        

        this.setForeground(Color.WHITE);
        
        this.setFont(new Font("Times new roman", Font.BOLD, 24));
    }
    
    public void display(){
        this.display(3000);
    }
    
    public void display(Integer milliseconds){
        try{
            this.device.setFullScreenWindow(this);
            while(true){
                Thread.sleep(milliseconds);
                this.setRandomBackground();
                repaint();
            }
        } catch(Exception err){
            err.printStackTrace();
        } finally{
            this.device.setFullScreenWindow(null);
            this.dispose();
        }
    }
    
    public void paint(Graphics g){
        g.drawString("My Full Screen Window...", RESOLUTION_WIDTH / 2, RESOLUTION_HEIGHT / 2);
    }
    
    private void setRandomBackground(){
        float r = (float) Math.random();
        float g = (float) Math.random();
        float b = (float) Math.random();
        Color bgcolor = new Color(r,g,b);
        this.setBackground(bgcolor);
    }
    
}
