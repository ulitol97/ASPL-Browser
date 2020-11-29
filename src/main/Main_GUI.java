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
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Main_GUI extends JFrame {

	private static final long serialVersionUID = -7811488186091705501L;

	// UI
	private JPanel mainPane;
	private JTabbedPane tabbedPane;

	// Tab control
	private int lastTabIndex = 0;
	private Map<JPanel, String> currentUrls;
	private Map<JPanel, JTextField> urlInputs;
	
	// History
	private Map<JPanel, History> histories;
	private Map<JPanel, JButton> backButtons;
	private Map<JPanel, JButton> nextButtons;
	private static final String KEY_BACK = "back";
	private static final String KEY_NEXT = "next";
	private static final String KEY_GO = "go";

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

					frame.setMinimumSize(new Dimension(800, 600));

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
		
		currentUrls = new HashMap<JPanel, String>();
		urlInputs = new HashMap<JPanel, JTextField>();

		histories = new HashMap<JPanel, History>();
		backButtons = new HashMap<JPanel, JButton>();
		nextButtons = new HashMap<JPanel, JButton>();

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

		// Body content

		JPanel tabContent = new JPanel();
		tabbedPane.addTab("New tab", null, tabContent, null);
		tabContent.setLayout(new BorderLayout(0, 0));

		JPanel addressBar = new JPanel();
		addressBar.setMinimumSize(new Dimension(150, 150));
		tabContent.add(addressBar, BorderLayout.NORTH);
		addressBar.setLayout(new BoxLayout(addressBar, BoxLayout.X_AXIS));

		JButton backBtn = createBackButton(tabContent);
		addressBar.add(backBtn);

		JButton nextBtn = createNextButton(tabContent);
		addressBar.add(nextBtn);

		JTextField urlInput = createUrlInput(tabContent);
		addressBar.add(urlInput);
		urlInput.setColumns(10);

		JButton goBtn = createGoButton(urlInput, tabContent);
		addressBar.add(goBtn);

		tabbedPane.insertTab("New tab",
				new ImageIcon(Main_GUI.class.getResource("/img/eii.jpg")),
				tabContent, "New tab", newTabIndex);

		// Tab content
		JPanel newTabTabPanel = new JPanel();
		newTabTabPanel
				.add(new JLabel("New tab - " + (tabbedPane.getTabCount() - 1)));

		newTabTabPanel.add(createCloseButton(tabContent));
		newTabTabPanel.setBackground(new Color(0, 0, 0, 0));

		tabbedPane.setTabComponentAt(newTabIndex, newTabTabPanel);

		setCurrentTab(tabbedPane.getTabCount() - 2);

		// History setup
		histories.put(tabContent, new History());
		backButtons.put(tabContent, backBtn);
		nextButtons.put(tabContent, nextBtn);
		urlInputs.put(tabContent, urlInput);

	}

	private JButton createBackButton(JPanel panel) {
		JButton backBtn = new JButton("<-");
		backBtn.setFocusPainted(false);
		backBtn.setEnabled(false);

		backBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				History history = histories.get(panel);
				String prevUrl = history.getPrevious();

				if (history.nPrevRecords() == 0)
					backBtn.setEnabled(false);
				
				String currentUrl = currentUrls.get(panel);
				if (currentUrl != null) {
					history.pushRecordNext(currentUrl);
				}

				if (prevUrl != null) {
					openHtml(KEY_BACK, prevUrl, panel);
					nextButtons.get(panel).setEnabled(true);
				}

			}
		});

		return backBtn;
	}

	private JButton createNextButton(JPanel panel) {
		JButton nextBtn = new JButton("->");
		nextBtn.setFocusPainted(false);
		nextBtn.setEnabled(false);

		nextBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				History history = histories.get(panel);
				String nextUrl = history.getNext();

				if (history.nNextRecords() == 0)
					nextBtn.setEnabled(false);
				
				String currentUrl = currentUrls.get(panel);
				if (currentUrl != null) {
					history.pushRecordBack(currentUrl);
				}

				if (nextUrl != null) {
					openHtml(KEY_NEXT, nextUrl, panel);
					backButtons.get(panel).setEnabled(true);
				}

			}
		});

		return nextBtn;
	}

	private JTextField createUrlInput(JPanel panel) {
		JTextField urlInput = new JTextField();
		urlInput.setText("res/Welcome.html");
		urlInput.setToolTipText("File to open");
		urlInput.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent arg0) {
				// Do nothing
			}

			@Override
			public void focusGained(FocusEvent arg0) {
				urlInput.setCaretPosition(urlInput.getText().length());

			}
		});

		urlInput.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				openHtml(KEY_GO, urlInput.getText().strip(), panel);
			}
		});

		return urlInput;

	}

	private JButton createGoButton(JTextField urlInput, JPanel panel) {
		JButton goBtn = new JButton("Go!");
		goBtn.setFocusPainted(false);

		goBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				openHtml(KEY_GO, urlInput.getText().strip(), panel);
			}
		});

		return goBtn;
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
				closeTab(panel);
			}
		});

		return closeBtn;
	}

	private void closeTab(JPanel panel) {

		if (tabbedPane.getSelectedComponent() == panel) {
			tabbedPane.setSelectedIndex(0);
		}

		// Close designated tab and clear history
		tabbedPane.remove(panel);
		histories.remove(panel);
		backButtons.remove(panel);
		nextButtons.remove(panel);

		// If no tabs remain, close application
		if (tabbedPane.getTabCount() - 1 == 0) {
			System.out.println("No tabs left, closing browser.");
			System.exit(0);
		}

	}

	private void setCurrentTab(int index) {
		tabbedPane.setSelectedIndex(index);
	}

	private void openHtml(String key, String path, JPanel source) {

		String currentUrl = currentUrls.get(source);
		if (path.equals(currentUrl))
			return;
		

		switch (key) {
		// If we accessed from url bar, enable back button, push to history
		case KEY_GO:
			if (currentUrl != null) {
				// Push to history the current url
				histories.get(source).pushRecordBack(currentUrl);
				// Clear stack of next urls
				histories.get(source).nextRecords.clear();
				nextButtons.get(source).setEnabled(false);
				
				backButtons.get(source).setEnabled(true);
			}
			break;

		case KEY_BACK:
			nextButtons.get(source).setEnabled(true);
			break;

		case KEY_NEXT:
			backButtons.get(source).setEnabled(true);
			break;

		default:
			break;
		}

		currentUrls.put(source, path);
		urlInputs.get(source).setText(path);
		System.out.println(String.format("Opening file: %s.", path));
		
		// Set url input text to new url

	}

	private class History {

		private Stack<String> backRecords;
		private Stack<String> nextRecords;

		public History() {
			backRecords = new Stack<String>();
			nextRecords = new Stack<String>();
		}

		public String getPrevious() {
			if (backRecords.size() > 0) {
				String ret = backRecords.pop();
				System.err.println(String.format("Prev.: %s", ret));
				return ret;
			}
			return null;
		}

		public String getNext() {
			if (nextRecords.size() > 0) {
				String ret = nextRecords.pop();
				System.err.println(String.format("Next.: %s", ret));
				return ret;
			}
			return null;
		}

		public void pushRecordBack(String rec) {
			backRecords.push(rec);
		}
		
		public void pushRecordNext(String rec) {
			nextRecords.push(rec);
		}

		public int nPrevRecords() {
			return this.backRecords.size();
		}

		public int nNextRecords() {
			return this.nextRecords.size();
		}

	}
}
