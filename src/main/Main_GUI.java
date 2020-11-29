package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import render.format.FormattedPage;
import render.paint.PrintPageGUI;

public class Main_GUI extends JFrame {

	private static final long serialVersionUID = -7811488186091705501L;

	private static final String WELCOME_PAGE_PATH = "res/html/welcome.html";
	private static final String URL_HINT = "res/html/...";

	// UI
	private JPanel mainPane;
	private JTabbedPane tabbedPane;

	// Tab control
	private int lastTabIndex = 0;
	private Map<JPanel, String> currentUrls;
	private Map<JPanel, JLabel> tabLabels;
	private Map<JPanel, JTextField> urlInputs;
	private Map<JPanel, JPanel> tabContents;

	// History
	private Map<JPanel, History> histories;
	private Map<JPanel, JButton> backButtons;
	private Map<JPanel, JButton> nextButtons;
	private static final String KEY_BACK = "back";
	private static final String KEY_NEXT = "next";
	private static final String KEY_GO = "go";

	// Styling
	private final static Font ERROR_FONT = new Font(Font.DIALOG, Font.ITALIC,
			18);
	private final static Color ERROR_COLOR = Color.RED;

	/**
	 * @author UO251436 Launch the application and configure basic window
	 *         parameters.
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

					frame.setMinimumSize(new Dimension(600, 400));

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * @author UO251436 Create the frame and initialize variables.
	 */
	public Main_GUI() {

		currentUrls = new HashMap<JPanel, String>();
		urlInputs = new HashMap<JPanel, JTextField>();
		tabContents = new HashMap<JPanel, JPanel>();
		tabLabels = new HashMap<JPanel, JLabel>();

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
		newTab(true);

	}

	/**
	 * @author UO251436 Create the tabbed pane that serves as the core of the
	 *         UI. Store the pane as an attribute.
	 */
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

	/**
	 * @author UO251436 Creates a new JButton with the expected style and
	 *         behavior to create new tabs and links it to the application
	 *         tabbed panel.
	 */
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
				newTab(false);
			}
		});

		tabbedPane.setTabComponentAt(0, newTabBtn);
	}

	/**
	 * @author UO251436 Create new tabs and their underlying structure and
	 *         panel.
	 */
	private void newTab(boolean welcome) {

		System.out.println("Opening new tab.");

		int newTabIndex = tabbedPane.getTabCount() - 1;

		// Body content

		JPanel tabContent = new JPanel();
		tabbedPane.addTab("New tab", null, tabContent, null);
		tabContent.setLayout(new BorderLayout(0, 0));

		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

		JScrollPane scrollPane = new JScrollPane(contentPanel);
		tabContent.add(scrollPane, BorderLayout.CENTER);

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
		JLabel tabLabel = new JLabel("New tab");
		newTabTabPanel.add(tabLabel);

		tabLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				tabbedPane.setSelectedComponent(tabContent);
			}
		});

		newTabTabPanel.add(createCloseButton(tabContent));
		newTabTabPanel.setBackground(new Color(0, 0, 0, 0));

		tabbedPane.setTabComponentAt(newTabIndex, newTabTabPanel);

		setCurrentTab(tabbedPane.getTabCount() - 2);

		// History setup
		histories.put(tabContent, new History());
		backButtons.put(tabContent, backBtn);
		nextButtons.put(tabContent, nextBtn);
		urlInputs.put(tabContent, urlInput);
		tabContents.put(tabContent, contentPanel);
		tabLabels.put(tabContent, tabLabel);

		// Open welcome page on first tab
		if (welcome) {
			openHtml(KEY_GO, WELCOME_PAGE_PATH, tabContent);
		}

	}

	/**
	 * @author UO251436 Factory called when a new back button is needed.
	 * @return Returns a JButton with the expected style and behavior to return
	 *         the browser to a previous page.
	 */
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

	/**
	 * @author UO251436 Factory called when a new next button is needed.
	 * @return Returns a JButton with the expected style and behavior to return
	 *         the browser to the next page (if available).
	 */
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

	/**
	 * @author UO251436 Factory called when a new input is needed.
	 * @return Returns a JText field with the expected style and behavior to act
	 *         as the browser navbar.
	 */
	private JTextField createUrlInput(JPanel panel) {
		JTextField urlInput = new JTextField();
		urlInput.setText(URL_HINT);
		urlInput.setToolTipText("File to open");
		urlInput.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				if (urlInput.getText().equals(URL_HINT)) {
					urlInput.setText("");
				}
				urlInput.setCaretPosition(urlInput.getText().length());

			}

			@Override
			public void focusLost(FocusEvent arg0) {
				if (urlInput.getText().equals("")) {
					urlInput.setText(URL_HINT);
				}
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

	/**
	 * @author UO251436 Factory called when a new GO button is needed.
	 * @return Returns a JButton with the expected style and behavior to act as
	 *         the browser go button that triggers the html load.
	 */
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

	/**
	 * @author UO251436 Factory called when a new close button for the tabs is
	 *         needed.
	 * @return Returns a JButton with the expected style and behavior to act as
	 *         a tab-closer.
	 */
	private JButton createCloseButton(JPanel panel) {
		ImageIcon icon = (ImageIcon) UIManager.getIcon("OptionPane.errorIcon");
		Image image = icon.getImage();
		Image scaledImage = image.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
		Icon scaledIcon = new ImageIcon(scaledImage);

		JButton closeBtn = new JButton(scaledIcon);
		closeBtn.setBackground(new Color(0, 0, 0, 0));
		closeBtn.setBorder(BorderFactory.createEmptyBorder());
		closeBtn.setFocusPainted(false);
		closeBtn.setToolTipText("Close tab");

		closeBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				closeTab(panel);
			}
		});

		return closeBtn;
	}

	/**
	 * @author UO251436 Closes a specific tab, given the JPanel it contains.
	 */
	private void closeTab(JPanel panel) {

		if (tabbedPane.getSelectedComponent() == panel) {
			tabbedPane.setSelectedIndex(0);
		}

		// Close designated tab and clear history
		tabbedPane.remove(panel);
		histories.remove(panel);
		backButtons.remove(panel);
		nextButtons.remove(panel);
		currentUrls.remove(panel);
		urlInputs.remove(panel);
		tabContents.remove(panel);
		tabLabels.remove(panel);

		// If no tabs remain, close application
		if (tabbedPane.getTabCount() - 1 == 0) {
			System.out.println("No tabs left, closing browser.");
			System.exit(0);
		}

	}

	/**
	 * @author UO251436 Sets the currently focused tab, given its index.
	 */
	private void setCurrentTab(int index) {
		tabbedPane.setSelectedIndex(index);
	}

	/**
	 * @author UO251436 Loads the HTML passed as a parameter and renders its
	 *         contents in the corresponding tab.
	 * @param path   Location of the HTML file.
	 * @param source JPanel of the tab in which to render the HTML.
	 */
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

		// Set url input text to new url
		currentUrls.put(source, path);
		urlInputs.get(source).setText(path);

		// Process the file and print
		System.out.println(String.format("Opening file: %s.\n", path));
		try {

			FormattedPage formattedPage = Main_NoGUI.processFile(path);
			PrintPageGUI printer = new PrintPageGUI(tabContents.get(source));
			printer.printPage(formattedPage, null);

			// Change page Title and Tooltip
			JLabel tabLabel = tabLabels.get(source);
			tabLabel.setText(formattedPage.getTitle());
			tabLabel.setToolTipText(String.format("HTML file at '%s'", path));
			tabbedPane.getSelectedComponent().setName(formattedPage.getTitle());

		} catch (Exception e) {
			e.printStackTrace();
			String msg = buildErrorMessage(e.getMessage());

			// Print error in console
			System.err.println(msg);

			// Show error in browser
			JPanel contentsPanel = tabContents.get(source);

			contentsPanel.removeAll();

			JTextPane textPane = new JTextPane();
			textPane.setEditable(false);

			textPane.setFont(ERROR_FONT);
			textPane.setForeground(ERROR_COLOR);

			contentsPanel.add(textPane);
			textPane.setText(msg);
		}

	}

	private String buildErrorMessage(String e) {
		String ret = "Could not process the HTML page requested:\n";
		ret += "\t" + e;
		return ret;
	}

	/**
	 * 
	 * @author UO251436 Abstraction of a simple history functionality consisting
	 *         supported by two stacks.
	 */
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
				return ret;
			}
			return null;
		}

		public String getNext() {
			if (nextRecords.size() > 0) {
				String ret = nextRecords.pop();
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
