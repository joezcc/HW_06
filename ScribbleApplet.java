import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;

import java.util.LinkedList;

@SuppressWarnings("serial")
public class ScribbleApplet extends JFrame {
	
	JSlider red,blue,green,size;
	DrawingPad drawPad;
    JTextField txtField;
    JPanel adjust,colorPanel,sizeAndClear, palette;
    JButton clearButton;
	LinkedList<Image> drawing = new LinkedList<Image>();
	
   	public static void main(String[] args){
		ScribbleApplet scribble= new ScribbleApplet();
		scribble.setSize(900,700);
		scribble.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		scribble.setVisible(true);
	}
	
	public ScribbleApplet(){
		Container canvas =this.getContentPane();
	    drawPad = new DrawingPad();
	    drawPad.setBounds(0, 0, 900, 700);	    
	    canvas.add(drawPad, BorderLayout.CENTER);
   
	    //initialize color sliders
		red = new JSlider(JSlider.VERTICAL, 0, 255, 0);
		green = new JSlider(JSlider.VERTICAL, 0, 255, 0);
		blue = new JSlider(JSlider.VERTICAL, 0, 255, 0);
	    red.setBackground(Color.RED);
	    green.setBackground(Color.GREEN);
	    blue.setBackground(Color.BLUE);
	    
	    //initialize sample color
	    palette = new JPanel();
	    palette.setBackground(Color.BLACK);
	    	    
	    //initialize text field
		txtField = new JTextField();
		txtField.setFont(new Font("Monospaced", Font.PLAIN, 12));
		txtField.setText("Color(" + "0" + ", " + "0" + ", " + "0" +	")" );
		txtField.setEditable(false);

		//initialize size slider
		size = new JSlider(JSlider.HORIZONTAL,0,110,10);
		size.setMajorTickSpacing(20);
		size.setMinorTickSpacing(5);
		size.setPaintTicks(true);
		size.setPaintLabels(true);
		TitledBorder sizeBorder = new TitledBorder("Size Adjustment");
		size.setBorder(sizeBorder);
		
		//initialize clear button
		clearButton = new JButton("Clear Canvas");
			
		//whole panel
	    adjust = new JPanel(new GridLayout(2,1,0,0));
	    TitledBorder border = new TitledBorder("Adjustment Panel");
	    adjust.setBorder(border);
		
	    //top half color panel
	    colorPanel = new JPanel(new GridLayout(1,3,0,0));
		
		//bottom half panel
	    sizeAndClear = new JPanel(new GridLayout(4,1,5,0));
		
	    //adding changes listener
		red.addChangeListener(new SliderChanged());
		green.addChangeListener(new SliderChanged());
		blue.addChangeListener(new SliderChanged());
		size.addChangeListener(new SizeChange());
	    
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawPad.clear();
			}
		});
		
		//adding up upper part components
		colorPanel.add(red);	colorPanel.add(green);	colorPanel.add(blue);
	
		//adding up lower part components
		sizeAndClear.add(palette);
		sizeAndClear.add(txtField);
		sizeAndClear.add(size);
		sizeAndClear.add(clearButton);		
		
		//adding all together
		adjust.add(colorPanel);
		adjust.add(sizeAndClear);	
		
		add(adjust,BorderLayout.WEST);

	}
	
	class SliderChanged implements ChangeListener{
		public void stateChanged(ChangeEvent arg0) {
		updateRGB(red.getValue(),green.getValue(),blue.getValue());
		}
	}
	
	class SizeChange implements ChangeListener{
		public void stateChanged(ChangeEvent e) {
			JSlider source = (JSlider) e.getSource();
			if (!source.getValueIsAdjusting()) {
				drawPad.brushSize = (int) source.getValue();
			}
		}
	}
	
	public void updateRGB(int r,int g,int b){
		drawPad.rVal = r;
		drawPad.gVal = g;
		drawPad.bVal = b;
		txtField.setText("Color(" + r + ", " + g + ", " + b +")");
		updatePalette(r,g,b);
	}
	
	public void updatePalette(int r,int g,int b){
		palette.setBackground(new Color(r,g,b));
		sizeAndClear.add(palette);
		sizeAndClear.add(txtField);
		sizeAndClear.add(size);
		sizeAndClear.add(clearButton);
	}
	
	public void undo(){
		if(drawing.size()>1){
			drawing.removeLast();
			System.out.println(drawing.size());
			drawPad.image.getGraphics().drawImage(drawing.getLast(),0,0,null);
			drawPad.repaint();
		}else if(drawing.size()==1){
			drawPad.clear();
			drawing.removeLast();
		}
	}
    
	class DrawingPad extends JComponent {
	    Image image;
	    Graphics2D graphics2D;
	    int currentX, currentY, oldX, oldY;
	    int rVal,gVal,bVal;
	    int brushSize = 10;
	    
	    public DrawingPad() {
	    	setDoubleBuffered(false);
	       	addMouseListener(new MouseAdapter() {
	           	public void mousePressed(MouseEvent e) {
	           		oldX = e.getX();
	           		oldY = e.getY();
	           	}
	           	public void mouseReleased(MouseEvent e) {
	           	         updateLinkedList();
	            }
	       		}
	       	);
	        addMouseMotionListener(new MouseMotionAdapter() {
	           	public void mouseDragged(MouseEvent e) {
	           		currentX = e.getX();
	           		currentY = e.getY();
	           		graphics2D.setColor(new Color(rVal,gVal,bVal));
	           		if (graphics2D != null)
	           			graphics2D.setStroke(new BasicStroke(brushSize));
	           		
	           		graphics2D.drawLine(oldX, oldY, currentX, currentY);
	           		repaint();
	           		oldX = currentX;
	           		oldY = currentY;
	           	}
	        }
	        );
	    }
	    
	    public void paintComponent(Graphics g) {
	    	if (image == null) {
	    		image = createImage(getSize().width, getSize().height);
	    		graphics2D = (Graphics2D) image.getGraphics();
	    		graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	    				RenderingHints.VALUE_ANTIALIAS_ON);
	    		clear();
	        }
	        g.drawImage(image, 0, 0, null);
	    }

	    public void updateLinkedList(){
	        Image i = createImage(getSize().width, getSize().height);
	        Graphics g = i.getGraphics();
	        g.drawImage(image, 0, 0, null);
	        drawing.add(i);
	        System.out.println(drawing.size());
	    }
	    
	    public void clear() {
	        graphics2D.setPaint(Color.white);
	        graphics2D.fillRect(0, 0, 5000, 5000);
	        updateLinkedList();
	        repaint();
	    }
	}
}
