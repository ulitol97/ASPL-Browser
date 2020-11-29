package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.BorderFactory;
import javax.swing.Icon;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

public class Main_GUI extends JFrame {

	private static final long serialVersionUID = -7811488186091705501L;
	private JPanel mainPane;
	private JTabbedPane tabbedPane;
	private int lastTabIndex = 0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main_GUI frame = new Main_GUI();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
					frame.setTitle("UO251436 - Arquitecturas Software y "
							+ "Procesamiento de Lenguajes");

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Main_GUI() {
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(Main_GUI.class.getResource("/img/favicon.jpg")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 700);
		mainPane = new JPanel();
		mainPane.setBorder(new EmptyBorder(1, 1, 1, 1));
		mainPane.setLayout(new BorderLayout(0, 0));
		setContentPane(mainPane);

		createTabbedPane();
		createNewTabButton();
		newTab();

	}

	private void createTabbedPane() {

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

		// Set behavior
		tabbedPane.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				// If clicked the new tab tab, simulate nothing happened (return
				// to previous)
				if (tabbedPane.getSelectedIndex() == tabbedPane.getTabCount()
						- 1) {
					setCurrentTab(lastTabIndex);
				} else {
					// Behave normally and update last tab
					lastTabIndex = tabbedPane.getSelectedIndex();
				}

			}
		});

		mainPane.add(tabbedPane, BorderLayout.CENTER);

	}

	private void createNewTabButton() {

		// New tab
		tabbedPane.addTab("New tab", null);

		// Text and tooltip
		JButton newTabBtn = new JButton(" + ", null);
		newTabBtn.setToolTipText("Open a new browser tab");

		// Other attributes, layout
		newTabBtn.setFocusable(false);
		newTabBtn.setBackground(new Color(0, 0, 0, 0));
		newTabBtn.setBorder(BorderFactory.createEmptyBorder());
		newTabBtn.setMargin(new Insets(10, 10, 10, 10));

		// Set font // Add initial tab

		Map<TextAttribute, Object> attributes = new HashMap<>();
		attributes.put(TextAttribute.FAMILY, Font.SANS_SERIF);
		attributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
		attributes.put(TextAttribute.SIZE, 16);
		newTabBtn.setFont(Font.getFont(attributes));

		// Set behavior
		newTabBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				newTab();
			}
		});

		tabbedPane.setTabComponentAt(0, newTabBtn);
	}

	private void newTab() {

		System.out.println("Opening new tab.");

		int newTabIndex = tabbedPane.getTabCount() - 1;

		// Body pane
		JPanel newTabPanel = new JPanel();
		tabbedPane.insertTab("New tab",
				new ImageIcon(Main_GUI.class.getResource("/img/eii.jpg")),
				newTabPanel, "New tab", newTabIndex);

		// Tab pane

		JPanel newTabTabPanel = new JPanel();
		// Label
		newTabTabPanel
				.add(new JLabel("New tab - " + (tabbedPane.getTabCount() - 1)));

		// Close button
		newTabTabPanel.add(createCloseButton(newTabPanel));
		newTabTabPanel.setBackground(new Color(0, 0, 0, 0));

		tabbedPane.setTabComponentAt(newTabIndex, newTabTabPanel);

		setCurrentTab(tabbedPane.getTabCount() - 2);

	}

	private JButton createCloseButton(JPanel panel) {
		ImageIcon icon = (ImageIcon) UIManager.getIcon("OptionPane.errorIcon");
		Image image = icon.getImage();
		Image scaledImage = image.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
		Icon scaledIcon = new ImageIcon(scaledImage);

		JButton closeBtn = new JButton(scaledIcon);
		closeBtn.setBackground(new Color(0, 0, 0, 0));
		closeBtn.setBorder(BorderFactory.createEmptyBorder());
		closeBtn.setFocusPainted(false);

		closeBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Close designated tab
				tabbedPane.remove(panel);
				
				// If no tabs remain, close app
				if (tabbedPane.getTabCount() -1 == 0) {
					System.out.println("No tabs left, closing browser.");
					System.exit(0);
				}

			}
		});

		return closeBtn;
	}

	private void setCurrentTab(int index) {
		tabbedPane.setSelectedIndex(index);
	}

}
