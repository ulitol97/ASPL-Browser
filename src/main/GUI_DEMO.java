package main;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.Dimension;
import javax.swing.BoxLayout;

public class GUI_DEMO extends JFrame {

	private JPanel contentPane;
	private JTextField urlInput;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI_DEMO frame = new GUI_DEMO();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUI_DEMO() {
		setMinimumSize(new Dimension(800, 600));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		JPanel tabContent = new JPanel();
		tabbedPane.addTab("New tab", null, tabContent, null);
		tabContent.setLayout(new BorderLayout(0, 0));
		
		JPanel addressBar = new JPanel();
		addressBar.setMinimumSize(new Dimension(150, 150));
		tabContent.add(addressBar, BorderLayout.NORTH);
		addressBar.setLayout(new BoxLayout(addressBar, BoxLayout.X_AXIS));
		
		JButton backBtn = new JButton("Back");
		addressBar.add(backBtn);
		
		JButton nextBtn = new JButton("Next");
		addressBar.add(nextBtn);
		
		urlInput = new JTextField();
		urlInput.setText("res/Welcome.html");
		urlInput.setToolTipText("File to open");
		addressBar.add(urlInput);
		urlInput.setColumns(10);
		
		JButton goBtn = new JButton("Go");
		addressBar.add(goBtn);
	}

}
