package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;

public class Kio_Project extends JFrame {

	private JPanel contentPanel = new JPanel();
	private Image background = new ImageIcon(Kio_Project.class.getResource("../image/FirstPage.png")).getImage();
	private JButton Button1 = new JButton("주문하러가기");
	
	public Kio_Project() {
		homeframe();
	}
	
	
	public void homeframe() {
	
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100,1080,1920);
		setTitle("KioProject");
		setResizable(true);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		
	//	FlowLayout layout = new FlowLayout(FlowLayout.CENTER,400,400);
	//	getContentPane().setLayout(layout);
	//	Button1.addActionListener(new ActionListener() {
	//		public void actionPerformed(ActionEvent e) {
		//	}
//		});
	//	Button1.setSize(100, 80);
	//	getContentPane().add(Button1, BorderLayout.CENTER);
		
		setVisible(true);
		
		
	}
	public void paint(Graphics g) {
		Dimension d = getSize();	
		g.drawImage(background, 0,0,d.width,d.height,null);
	}
	
	public static void main(String[] args) {
		new Kio_Project();
	}

}
