package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.border.EmptyBorder;

import com.google.appengine.api.images.Image;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JLayeredPane;
import javax.swing.Box;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Scrollbar;

public class Kio_Project extends JFrame {

	private JPanel contentPane = new JPanel();
	private java.awt.Image background = new ImageIcon(Kio_Project.class.getResource("../image/FirstPage.PNG")).getImage();
	private JButton Button1 = new JButton("주문하러가기");
	
	public Kio_Project() {
		homeframe();
	}
	/**
	 * Launch the application.
	 */
	
	public void homeframe() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1824, 2736);
		setTitle("KioProject");
		setResizable(false);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JScrollBar scrollbar = new JScrollBar();
		getContentPane().add(scrollbar, BorderLayout.EAST);
		
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
		g.drawImage(background, 0,0,null);
	}
	public static void main(String[] args) {
		new Kio_Project();
	}

}
