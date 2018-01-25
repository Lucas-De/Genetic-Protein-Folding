import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class GraphicsOnly extends JComponent implements ChangeListener {
	
	final int FOOD=1;
	final int ORG=2;
	final int NOTHING=0;
	
	JFrame frame;
	char[][] matrix;
	BufferedImage img;
	int width;
	int height;

	
	BufferedImage newImage(char[][] matrix2) {
		//create random image pixel by pixel
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		int a, r, g, b;
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				a = 255;
				char code = matrix2[y][x];
				if(code == 'H') {
					r = 104;   //red
					g = 190;   //green
					b = 191;   //blue
					//gives red
				} else if (code == 'P') {
					r = 200;   //red
					g = 102;   //green
					b = 107;   //blue
					//gives yellow
				}else{
					r = 96;   //red
					g = 105;   //green
					b = 123;   //blue
					//gives orange
				}
				int p = (a<<24) | (r<<16) | (g<<8) | b; //pixel
				img.setRGB(x, y, p);
			}
		}
		return img;
	}

	
	
    JPanel gui;
    /**
     * Displays the image.
     */
    JLabel imageCanvas;
    Dimension size;
    double scale = 1.0;
    BufferedImage image;

    public GraphicsOnly(JFrame frame,char[][] matrix,int size) {
    		this.frame=frame;
   
		width=size;
		height=size;
		this.matrix= matrix;
		image = newImage(matrix);
		
        this.size = new Dimension(10, 10);
        setBackground(Color.black);
    }


    public void setImage(Image image) {

        imageCanvas.setIcon(new ImageIcon(image));
    }

    public void initComponents() {
        if (gui == null) {
            gui = new JPanel(new BorderLayout());
            gui.setBorder(new EmptyBorder(5, 5, 5, 5));
            imageCanvas = new JLabel();
            JPanel imageCenter = new JPanel(new GridBagLayout());
            imageCenter.add(imageCanvas);
            JScrollPane imageScroll = new JScrollPane(imageCenter);
            imageScroll.setPreferredSize(new Dimension(300, 100));
            gui.add(imageScroll, BorderLayout.CENTER);
        }
    }

    public Container getGui() {
        initComponents();
        return gui;
    }

    public void stateChanged(ChangeEvent e) {
        int value = ((JSlider) e.getSource()).getValue();
        scale = value / 100.0;
        paintImage();
    }

    protected void paintImage() {
        int w = getWidth();
        int h = getHeight();
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        BufferedImage bi = new BufferedImage(
                (int)(imageWidth*scale), 
                (int)(imageHeight*scale), 
                image.getType());
        Graphics2D g2 = bi.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        double x = (w - scale * imageWidth) / 2;
        double y = (h - scale * imageHeight) / 2;
        AffineTransform at = AffineTransform.getTranslateInstance(0, 0);
        at.scale(scale, scale);
        g2.drawRenderedImage(image, at);
        setImage(bi);
    }

    public Dimension getPreferredSize() {
        int w = (int) (scale * size.width);
        int h = (int) (scale * size.height);
        return new Dimension(w, h);
    }
    
    void refreshImage(char[][] matrix,String title){

		 img = newImage(matrix);
		 image=img;
	     frame.setTitle(title);
	
		 int w = getWidth();
         int h = getHeight();
         int imageWidth = img.getWidth();
         int imageHeight = img.getHeight();
         BufferedImage bi = new BufferedImage(
                 (int)(imageWidth*scale), 
                 (int)(imageHeight*scale), 
                 img.getType());
         Graphics2D g2 = bi.createGraphics();
         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                 RenderingHints.VALUE_ANTIALIAS_ON);
         double x = (w - scale * imageWidth) / 2;
         double y = (h - scale * imageHeight) / 2;
         AffineTransform at = AffineTransform.getTranslateInstance(0, 0);
         at.scale(scale, scale);
         g2.drawRenderedImage(img, at);
         setImage(bi);
    }
    
    JSlider getControl() {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 1000, 100);
        slider.setMajorTickSpacing(200);
        slider.setMinorTickSpacing(25);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.addChangeListener(this);
        return slider;
    }
//    void startGraphicSimulation(){
//    	Timer timerOne = new Timer(1, this::refreshImage);
//		timerOne.start();
//    }
//    
   
}