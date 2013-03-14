import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

@SuppressWarnings("serial")
public class ScribbleShop extends JFrame{
	JDesktopPane desktop;
	JMenuBar menuBar;
	JMenu file,about;
	JMenuItem newMenu,close,closeAll,exit,help,author,undo;
	
	public static void main(String [] args){
		JFrame mainFrame = new ScribbleShop();
		mainFrame.setSize(1300,750);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
	}
	
	public ScribbleShop(){
		super("ScribbleShop, Scribble as much as you want!");
		setLayout(new FlowLayout());
        desktop = new JDesktopPane();
        setContentPane(desktop);
        createFrame();
        menu();
	}
	
	public void menu(){
		menuBar = new JMenuBar();
		file = new JMenu("File");
		about = new JMenu("About");
		
		menuBar.add(file);
		menuBar.add(about);
		
		newMenu = new JMenuItem("New",'N');
		newMenu.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N,KeyEvent.CTRL_MASK));
		newMenu.addActionListener(new NewFile());
	
		undo = new JMenuItem("Undo",'Z');
		undo.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z,KeyEvent.CTRL_MASK));
		undo.addActionListener(new Undo());
		
		close = new JMenuItem("Close");
		close.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W,KeyEvent.CTRL_MASK));
		close.addActionListener(new CloseWindow());

		closeAll = new JMenuItem("Close All");
		closeAll.addActionListener(new CloseAllWindows());	
		
		exit = new JMenuItem("Exit");
		exit.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q,KeyEvent.CTRL_MASK));
		exit.addActionListener(new ExitProgram());	
		
		file.add(newMenu);
		file.add(undo);
		file.add(close);
		file.add(closeAll);
		file.addSeparator();
		file.add(exit);
		
		help = new JMenuItem("Help");
		help.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1,0));
		help.addActionListener(new Help());
		
		author = new JMenuItem("Author");
		author.addActionListener(new FindAuthor());
	
		about.add(help);
		about.add(author);
	
		setJMenuBar(menuBar);
	}
	protected void createFrame() {
		InternalFrame frame = new InternalFrame();
		frame.setSize(650, 650);
		frame.setVisible(true);
		desktop.add(frame);
		try {
			frame.setSelected(true);
		} catch (java.beans.PropertyVetoException e) {}
	}
	
	class NewFile implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			System.out.println("create new file");
			createFrame();
		}
	}
	
	class CloseWindow implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			desktop.getSelectedFrame().setVisible(false);
		}
	}

	class CloseAllWindows implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			JInternalFrame[] temp = desktop.getAllFrames();
			InternalFrame.openFrameCount = 0 ;
			for(int i=0 ; i<temp.length ; i++){
				temp[i].setVisible(false);
			}
		}
	}

	class ExitProgram implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			System.exit(1);
		}
	}

	class FindAuthor implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			System.out.println("author");
			showAvatar();
		}
	}
	class Help implements ActionListener{
		
		public void actionPerformed(ActionEvent arg0) {
			System.out.println("Help");
			helpWindow();
		}
	}

	class Undo implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			((InternalFrame)(desktop.getSelectedFrame())).z.undo();
		}
	}
	
	protected void showAvatar(){
		AvatarFrame frame = new AvatarFrame();
		frame.setVisible(true);
		desktop.add(frame);
	}
	
	protected void helpWindow(){
		HelpFrame frame = new HelpFrame();
		frame.setVisible(true);
		desktop.add(frame);
	}
	
	
	static class InternalFrame extends JInternalFrame{
		static int openFrameCount = 0;
		static final int xOffset = 30, yOffset = 30;
		ScribbleApplet z;
		public InternalFrame() {
			super("ScribbleApplet as much as you want!",true, true,true,true);
			++openFrameCount;
			z = new ScribbleApplet();
			this.setSize(650,650);
			this.add(z.getContentPane());
			setLocation(xOffset*openFrameCount, yOffset*openFrameCount);
		}
	}
	
	static class AvatarFrame extends JDialog{
		public AvatarFrame(){
			super(new JFrame(),"Author",true);
			JLabel name = new JLabel("JoeZ");
			name.setFont(new Font("Calibri", Font.BOLD, 15));
			setLayout(new BorderLayout());
			setSize(550,560);
			add(new MyAvatar(),BorderLayout.CENTER);
			add(name,BorderLayout.SOUTH);
			setLocation(100,80);
		}
	}
	
	static class HelpFrame extends JDialog{
		public HelpFrame(){
			super(new JFrame(),"Help",true);
			JLabel helpMsg = new JLabel("lol you're doomed!");
			helpMsg.setFont(new Font("Calibri", Font.BOLD, 50));
			setSize(500,200);
			add(helpMsg);
			setLocation(300,300);
		}
	}
}	
