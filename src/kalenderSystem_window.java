import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.SwingConstants;

import org.jdesktop.swingx.JXDatePicker;
import org.json.JSONArray;
import org.json.JSONObject;

public class kalenderSystem_window extends JFrame
		implements ComponentListener, ActionListener, KeyListener, WindowListener {

	private JFrame addActivityFrame;
	private JFrame showActivityFrame;
	private JFrame showCalendarInviteFrame;
	private JFrame addCalendarFrame;
	private JFrame inviteUserFrame;
	private JFrame infoBoard;
	private JPanel breadCrumb;
	private JPanel contentPane;
	private JPanel containerFiller1;
	private JPanel containerFiller2;
	private JPanel menuPanel;
	private JScrollPane contentScroll;
	private JTextField usernameTextField;
	private JTextField emailTextField;
	private JTextField firstNameTextField;
	private JTextField lastNameTextField;
	private JPasswordField passwordTextField;
	private JPasswordField passwordConfirm;
	private JComboBox year;
	private JComboBox month;
	private JLabel info;
	private int startWidth = 1300;
	private int initialXSize = 300;
	private int initialYSize = 600;
	private boolean redopassword;
	private boolean redoemail;
	private boolean aafOpen = false;
	private boolean safOpen = false;
	private boolean acfOpen = false;
	private boolean iufOpen = false;
	private boolean scfOpen = false;
	private Font newFont = new Font("Arial", 0, 18);

	private JLabel activityName;
	private JLabel activityCreator;
	private JLabel activityCalendar;
	private JLabel activityStart;
	private JLabel activityEnd;
	private JButton removeActivity;

	private JLabel calendarName;
	private JLabel calendarCreator;
	private JLabel calendarEventQuantity;

	private GridBagConstraints c = new GridBagConstraints();
	private int x = 0;
	private int y = 0;
	private int userID = -1;
	private String hashkey = "";
	private String username = "";
	private String currentFrame = "Login";
	private String[] str_month = { "Januari", "Februari", "Mars", "April", "Maj", "Juni", "Juli", "Augusti",
			"September", "Oktober", "November", "December" };
	private JButton registerButton;
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private Vector<String> calendars = new Vector<String>();
	private Vector<Integer> calendarIDs = new Vector<Integer>();

	public kalenderSystem_window() {
		super("Kalender");
		super.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		super.addWindowListener(this);
		super.setLocation(100, 100);
		super.setPreferredSize(new Dimension(1200, 750));
		super.setLayout(new BorderLayout());
		super.addComponentListener(this);
		super.setName("main");

		kalenderSystem_connectToServer();

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0;
		c.weighty = 1;
		c.gridx = x;
		c.gridy = y;
		c.gridwidth = 1;
		c.insets = new Insets(15, 0, 0, 0);
		c.anchor = GridBagConstraints.NORTHWEST;

		menuPanel = new JPanel();
		menuPanel.setLayout(new GridBagLayout());

		c.fill = GridBagConstraints.HORIZONTAL;
		String[] menuNames = { "Logga in", "Registrera dig" };
		JButton[] buttons = new JButton[menuNames.length];

		for (int i = 0; i < buttons.length; i++) {

			c.gridx = x;
			c.gridy = y;

			buttons[i] = new JButton(menuNames[i]);
			buttons[i].addActionListener(this);
			buttons[i].setActionCommand("menu" + menuNames[i]);

			menuPanel.add(buttons[i], c);

			y++;

		}

		JPanel temp = new JPanel();
		temp.setLayout(new BorderLayout());
		temp.add(menuPanel, BorderLayout.NORTH);

		JScrollPane mainMenuPanel = new JScrollPane(temp);
		mainMenuPanel.setPreferredSize(new Dimension((int) temp.getPreferredSize().getWidth() + 50,
				(int) temp.getPreferredSize().getHeight()));
		super.add(mainMenuPanel, BorderLayout.WEST);

		JPanel container = new JPanel();
		container.setLayout(new BorderLayout());

		JPanel top = new JPanel();
		top.setLayout(new GridLayout(1, 2));

		breadCrumb = new JPanel();
		breadCrumb.setPreferredSize(new Dimension(1000, 50));
		breadCrumb.setLayout(new GridLayout(1, 10));

		JPanel breadCrumbFiller = new JPanel();

		contentPane = new JPanel();
		contentPane.setPreferredSize(new Dimension(345, 450));

		containerFiller1 = new JPanel();
		containerFiller1.setPreferredSize(new Dimension(initialXSize, initialYSize));
		containerFiller2 = new JPanel();
		containerFiller2.setPreferredSize(new Dimension(initialXSize, initialYSize));

		kalenderSystem_showLoginPane();

		contentScroll = new JScrollPane(contentPane);
		contentScroll.setPreferredSize(new Dimension(400, 575));
		contentScroll.setBorder(null);

		top.add(breadCrumb);
		top.add(breadCrumbFiller);

		container.add(top, BorderLayout.NORTH);

		container.add(contentScroll, BorderLayout.CENTER);
		container.add(containerFiller1, BorderLayout.WEST);
		container.add(containerFiller2, BorderLayout.EAST);

		super.add(container, BorderLayout.CENTER);
		pack();
		super.setVisible(true);
	}

	public void kalenderSystem_showLoginPane() {

		initialXSize = 300;
		breadCrumb.removeAll();
		menuPanel.removeAll();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0;
		c.weighty = 1;
		x = 0;
		y = 0;
		c.gridx = x;
		c.gridy = y;
		c.gridwidth = 1;
		c.insets = new Insets(15, 0, 0, 0);
		c.anchor = GridBagConstraints.NORTHWEST;

		menuPanel.setLayout(new GridBagLayout());

		c.fill = GridBagConstraints.HORIZONTAL;
		String[] menuNames = { "Logga in", "Registrera dig" };
		JButton[] buttons = new JButton[menuNames.length];
		Font f = new Font("Arial", Font.BOLD, 13);

		for (int i = 0; i < buttons.length; i++) {

			c.gridx = x;
			c.gridy = y;

			buttons[i] = new JButton(menuNames[i]);
			buttons[i].addActionListener(this);
			buttons[i].setActionCommand("menu" + menuNames[i]);
			buttons[i].setFont(f);

			menuPanel.add(buttons[i], c);

			y++;

		}

		contentPane.removeAll();
		contentPane.setLayout(new GridLayout(17, 1));
		currentFrame = "Login";
		String[] loginText = { "", "", "KalenderSystem", "Logga In", "", "Användarnamn", "Lösenord" };
		JLabel[] loginLabels = new JLabel[loginText.length];

		usernameTextField = new JTextField();
		passwordTextField = new JPasswordField();

		for (int i = 0; i < loginText.length; i++) {

			loginLabels[i] = new JLabel(loginText[i], SwingConstants.CENTER);
			Font newFont = new Font("Arial", 0, 32);
			loginLabels[i].setFont(newFont);
			contentPane.add(loginLabels[i]);

			if (loginText[i].equals("Användarnamn")) {

				newFont = new Font("Arial", 0, 20);
				loginLabels[i].setFont(newFont);
				loginLabels[i].setHorizontalAlignment(SwingConstants.LEFT);
				usernameTextField.setFont(newFont);
				usernameTextField.addKeyListener(this);
				contentPane.add(usernameTextField);

			} else if (loginText[i].equals("Lösenord")) {

				newFont = new Font("Arial", 0, 20);
				loginLabels[i].setFont(newFont);
				loginLabels[i].setHorizontalAlignment(SwingConstants.LEFT);
				passwordTextField.setFont(newFont);
				passwordTextField.addKeyListener(this);
				contentPane.add(passwordTextField);

			}
		}

		JButton loginButton = new JButton("Logga in");
		loginButton.addActionListener(this);
		loginButton.setActionCommand("Logga in");
		loginButton.setFont(newFont);
		contentPane.add(loginButton);

		Font newFont = new Font("Arial", 0, 20);
		info = new JLabel("");
		info.setFont(newFont);
		contentPane.add(info);

		containerFiller1.setPreferredSize(new Dimension(initialXSize, initialYSize));
		containerFiller2.setPreferredSize(new Dimension(initialXSize, initialYSize));

		contentPane.repaint();
		pack();
		super.repaint();

	}

	public void kalenderSystem_showRegisterPane() {

		menuPanel.removeAll();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0;
		c.weighty = 1;
		x = 0;
		y = 0;
		c.gridx = x;
		c.gridy = y;
		c.gridwidth = 1;
		c.insets = new Insets(15, 0, 0, 0);
		c.anchor = GridBagConstraints.NORTHWEST;

		menuPanel.setLayout(new GridBagLayout());

		c.fill = GridBagConstraints.HORIZONTAL;
		String[] menuNames = { "Logga in", "Registrera dig" };
		JButton[] buttons = new JButton[menuNames.length];
		Font f = new Font("Arial", Font.BOLD, 13);
		for (int i = 0; i < buttons.length; i++) {

			c.gridx = x;
			c.gridy = y;

			buttons[i] = new JButton(menuNames[i]);
			buttons[i].addActionListener(this);
			buttons[i].setActionCommand("menu" + menuNames[i]);
			buttons[i].setFont(f);

			menuPanel.add(buttons[i], c);

			y++;

		}

		contentPane.removeAll();
		contentPane.setLayout(new GridLayout(19, 1));
		currentFrame = "Register";

		String[] registerText = { "", "KalenderSystem", "Registrera dig", "", "Användarnamn", "Lösenord",
				"Bekräfta lösenord", "E-Mail", "Förnamn", "Efternamn", "" };

		JLabel[] registerLabels = new JLabel[registerText.length];

		usernameTextField = new JTextField();
		passwordTextField = new JPasswordField();
		passwordConfirm = new JPasswordField();
		emailTextField = new JTextField();
		firstNameTextField = new JTextField();
		lastNameTextField = new JTextField();

		passwordTextField.addKeyListener(this);
		passwordConfirm.addKeyListener(this);
		emailTextField.addKeyListener(this);

		for (int i = 0; i < registerText.length; i++) {

			Font newFont;
			if (i <= 2) {

				newFont = new Font("Arial", 0, 32);
			} else {

				newFont = new Font("Arial", 0, 20);
			}

			registerLabels[i] = new JLabel(registerText[i], SwingConstants.CENTER);

			registerLabels[i].setFont(newFont);
			contentPane.add(registerLabels[i]);

			if (registerText[i].equals("Användarnamn")) {

				registerLabels[i].setFont(newFont);
				registerLabels[i].setHorizontalAlignment(SwingConstants.LEFT);
				usernameTextField.setFont(newFont);
				contentPane.add(usernameTextField);

			} else if (registerText[i].equals("Lösenord")) {

				registerLabels[i].setFont(newFont);
				registerLabels[i].setHorizontalAlignment(SwingConstants.LEFT);
				passwordTextField.setFont(newFont);
				passwordTextField.setName(registerText[i]);
				contentPane.add(passwordTextField);

			} else if (registerText[i].equals("Bekräfta lösenord")) {

				registerLabels[i].setFont(newFont);
				registerLabels[i].setHorizontalAlignment(SwingConstants.LEFT);
				passwordConfirm.setName(registerText[i]);
				passwordConfirm.setFont(newFont);
				passwordConfirm.setName(registerText[i]);
				contentPane.add(passwordConfirm);

			} else if (registerText[i].equals("E-Mail")) {

				registerLabels[i].setFont(newFont);
				registerLabels[i].setHorizontalAlignment(SwingConstants.LEFT);
				emailTextField.setFont(newFont);
				emailTextField.setName(registerText[i]);
				contentPane.add(emailTextField);

			} else if (registerText[i].equals("Förnamn")) {

				registerLabels[i].setFont(newFont);
				registerLabels[i].setHorizontalAlignment(SwingConstants.LEFT);
				firstNameTextField.setFont(newFont);
				firstNameTextField.setName(registerText[i]);
				contentPane.add(firstNameTextField);

			} else if (registerText[i].equals("Efternamn")) {

				registerLabels[i].setFont(newFont);
				registerLabels[i].setHorizontalAlignment(SwingConstants.LEFT);
				lastNameTextField.setFont(newFont);
				lastNameTextField.setName(registerText[i]);
				contentPane.add(lastNameTextField);

			}
		}

		registerButton = new JButton("Registrera dig");
		registerButton.addActionListener(this);
		registerButton.setActionCommand("Registrera dig");
		registerButton.setFont(newFont);
		contentPane.add(registerButton);
		registerButton.setEnabled(false);

		Font newFont = new Font("Arial", 0, 20);
		info.setFont(newFont);
		contentPane.add(info);

		containerFiller1.setPreferredSize(new Dimension(initialXSize, initialYSize));
		containerFiller2.setPreferredSize(new Dimension(initialXSize, initialYSize));

		contentPane.repaint();
		pack();
		super.repaint();
	}

	public void kalenderSystem_showCalendarPane() {

		kalenderSystem_loggedInMenuPane();

		contentPane.removeAll();
		contentPane.setLayout(new GridLayout(1, 1));

		kalenderSystem_showDayView(Calendar.getInstance().get(Calendar.YEAR),
				Calendar.getInstance().get(Calendar.WEEK_OF_YEAR));

		contentPane.repaint();
		pack();
		super.repaint();
	}

	public void kalenderSystem_showInvitesPane() {

		contentPane.removeAll();
		breadCrumb.removeAll();
		kalenderSystem_loggedInMenuPane();

		contentPane.setLayout(new BorderLayout());
		initialXSize = 250;
		containerFiller1.setPreferredSize(new Dimension(initialXSize, 0));
		containerFiller2.setPreferredSize(new Dimension(initialXSize, 0));

		JPanel cPanel = new JPanel();

		Font f = new Font("Arial", Font.BOLD, 13);

		String SQL = "SELECT eventID FROM eventinvites WHERE userID = ? AND declined = ?";
		String types = "ii";
		Object[] params = { userID, 0 };

		Object[][] matrix = kalenderSystem_getData("kalenderSystem_getData.php", SQL, types, params);

		Vector<Integer> eventIDs = new Vector<Integer>();

		for (int i = 0; i < matrix.length; i++) {

			eventIDs.add((int) matrix[i][0]);

		}

		matrix = new Object[eventIDs.size()][];

		for (int i = 0; i < eventIDs.size(); i++) {

			SQL = "SELECT eventID, creatorID, name, startTime, endTime FROM events WHERE eventID = ?";
			types = "i";
			params = new Object[] { eventIDs.get(i) };

			Object[][] temp = kalenderSystem_getData("kalenderSystem_getData.php", SQL, types, params);
			matrix[i] = temp[0];

		}

		for (int i = 0; i < matrix.length; i++) {

			JPanel activityPanel = new JPanel();
			activityPanel.setLayout(new GridLayout(1, 5));

			JButton event = new JButton((String) matrix[i][2]);
			event.addActionListener(this);
			event.setName("" + matrix[i][0]);
			event.setActionCommand("viewActivity");
			activityPanel.add(event);

			activityPanel.add(new JLabel(""));

			JButton accept = new JButton("Acceptera");
			accept.addActionListener(this);
			accept.setActionCommand("acceptEvent" + matrix[i][0]);
			accept.setBackground(new Color(175, 225, 175));

			activityPanel.add(accept);

			JButton decline = new JButton("Avböj");
			decline.addActionListener(this);
			decline.setActionCommand("declineEvent" + matrix[i][0]);
			decline.setBackground(new Color(255, 175, 175));

			activityPanel.add(decline);

			cPanel.add(activityPanel);
		}

		SQL = "SELECT calendarInviteID FROM calendarinvites WHERE userID = ? AND declined = ?";
		types = "ii";
		params = new Object[] { userID, 0 };

		matrix = kalenderSystem_getData("kalenderSystem_getData.php", SQL, types, params);

		Vector<Integer> calendarID = new Vector<Integer>();

		for (int i = 0; i < matrix.length; i++) {

			calendarID.add((int) matrix[i][0]);

		}

		matrix = new Object[calendarID.size()][];
		for (int i = 0; i < calendarID.size(); i++) {

			SQL = "SELECT calendarID, creatorID, name FROM calendars WHERE calendarID = ?";
			types = "i";
			params = new Object[] { calendarID.get(i) };

			Object[][] temp = kalenderSystem_getData("kalenderSystem_getData.php", SQL, types, params);
			matrix[i] = temp[0];

		}

		for (int i = 0; i < matrix.length; i++) {

			JPanel activityPanel = new JPanel();
			activityPanel.setLayout(new GridLayout(1, 5));

			JButton event = new JButton("<html><p>" + (String) matrix[i][2] + "</p></html>");
			event.addActionListener(this);
			event.setActionCommand("checkCalendar");
			event.setName("" + matrix[i][0]);
			activityPanel.add(event);

			activityPanel.add(new JLabel(""));

			JButton accept = new JButton("Acceptera");
			accept.addActionListener(this);
			accept.setActionCommand("acceptCalendar" + matrix[i][0]);
			accept.setBackground(new Color(175, 225, 175));

			activityPanel.add(accept);

			JButton decline = new JButton("Avböj");
			decline.addActionListener(this);
			decline.setActionCommand("declineCalendar" + matrix[i][0]);
			decline.setBackground(new Color(255, 175, 175));

			activityPanel.add(decline);

			cPanel.add(activityPanel);
		}

		JScrollPane scroll = new JScrollPane(cPanel);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		contentPane.add(scroll, BorderLayout.CENTER);

		contentPane.repaint();
		pack();
		super.repaint();
	}

	public void kalenderSystem_addActivityFrame() {

		if (aafOpen) {
			addActivityFrame.setVisible(false);
		}
		addActivityFrame = new JFrame();
		Point p = super.getLocation();
		p.setLocation((int) p.getX() + 50, (int) p.getY() + 50);
		addActivityFrame.setLocation(p);
		addActivityFrame.setPreferredSize(new Dimension(500, 500));
		addActivityFrame.setTitle("Skapa ny aktivitet");
		addActivityFrame.setLayout(new BorderLayout());

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(16, 1));

		GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();

		JXDatePicker p1 = new JXDatePicker();
		p1.setName("DatePicker");
		p1.addActionListener(this);
		Component[] comp = p1.getComponents();
		Color dpc = comp[0].getForeground();
		comp[0].setEnabled(false);

		JFormattedTextField a = p1.getEditor();
		a.setEnabled(false);
		a.setDisabledTextColor(dpc);
		a.setFont(newFont);
		p1.setEditor(a);
		p1.setDate(cal.getTime());

		Component[] com = p1.getComponents();
		((JButton) com[1]).setIcon(null);
		((JButton) com[1]).setText("Välj ett datum");

		JXDatePicker p2 = new JXDatePicker();
		p2.setName("DatePicker");
		p2.addActionListener(this);
		comp = p2.getComponents();
		dpc = comp[0].getForeground();
		comp[0].setEnabled(false);

		a = p2.getEditor();
		a.setEnabled(false);
		a.setDisabledTextColor(dpc);
		a.setFont(newFont);
		p2.setEditor(a);
		p2.setDate(cal.getTime());

		com = p2.getComponents();
		((JButton) com[1]).setIcon(null);
		((JButton) com[1]).setText("Välj ett datum");

		String[] labelText = { " ", "", "Aktivitetstitel", "Välj startdatum", "Klockan:", "Välj sluttid", "Klockan:",
				"" };
		JLabel[] labels = new JLabel[labelText.length];
		kalenderSystem_spinner spinner = new kalenderSystem_spinner(0, 24, 12, 1, newFont, JTextField.RIGHT);

		Object[][] matrix = kalenderSystem_getCalendars();

		calendars = new Vector<String>();
		calendarIDs = new Vector<Integer>();
		calendars.add("Välj en kalender...");
		calendarIDs.add(0);

		for (int i = 0; i < matrix.length; i++) {

			calendars.add((String) matrix[i][1]);
			calendarIDs.add((int) matrix[i][0]);
		}

		JComboBox calendarSelect = new JComboBox(calendars);
		calendarSelect.setFont(newFont);
		for (int i = 0; i < labels.length; i++) {

			labels[i] = new JLabel(labelText[i], SwingConstants.CENTER);
			labels[i].setFont(newFont);
			panel.add(labels[i]);

			if (labelText[i].equals(" ")) {

				panel.add(calendarSelect);

			} else if (labelText[i].equals("Aktivitetstitel")) {

				JTextField textField = new JTextField();
				textField.setFont(newFont);
				panel.add(textField);

			} else if (labelText[i].equals("Välj startdatum")) {

				panel.add(p1);

			} else if (labelText[i].equals("Välj sluttid")) {

				// panel.add(p2);

			} else if (labelText[i].equals("Klockan:")) {

				JPanel clockPanel = new JPanel();
				clockPanel.setLayout(new GridLayout(1, 2));
				spinner = new kalenderSystem_spinner(0, 24, 12, 1, newFont, JTextField.RIGHT);
				clockPanel.add(spinner);
				spinner = new kalenderSystem_spinner(0, 59, 00, 1, newFont, JTextField.LEFT);
				clockPanel.add(spinner);
				panel.add(clockPanel);

			}
		}

		JButton button = new JButton("Skapa aktivitet");
		button.setActionCommand("Skapa aktivitet");
		button.addActionListener(this);
		panel.add(button);

		info.setText("");
		panel.add(info);

		JPanel fPane = new JPanel();
		fPane.setPreferredSize(new Dimension(80, 20));
		JPanel fPane2 = new JPanel();
		fPane2.setPreferredSize(new Dimension(80, 20));
		panel.setPreferredSize(new Dimension(300, 400));

		addActivityFrame.add(panel, BorderLayout.CENTER);
		addActivityFrame.add(fPane, BorderLayout.WEST);
		addActivityFrame.add(fPane2, BorderLayout.EAST);
		addActivityFrame.pack();
		addActivityFrame.setVisible(true);
		aafOpen = true;

	}

	public void kalenderSystem_showActivityPane(int eventID) {

		kalenderSystem_loggedInMenuPane();

		if (safOpen) {
			showActivityFrame.setVisible(false);
		}
		showActivityFrame = new JFrame();
		Point p = super.getLocation();
		p.setLocation((int) p.getX() + 50, (int) p.getY() + 50);
		showActivityFrame.setLocation(p);
		showActivityFrame.setPreferredSize(new Dimension(500, 500));
		showActivityFrame.setTitle("Visa aktivitet");
		showActivityFrame.setLayout(new BorderLayout());

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(16, 1));

		JPanel fPane = new JPanel();
		fPane.setPreferredSize(new Dimension(80, 20));
		JPanel fPane2 = new JPanel();
		fPane2.setPreferredSize(new Dimension(80, 20));
		panel.setPreferredSize(new Dimension(300, 400));

		showActivityFrame.add(panel, BorderLayout.CENTER);
		showActivityFrame.add(fPane, BorderLayout.WEST);
		showActivityFrame.add(fPane2, BorderLayout.EAST);
		showActivityFrame.pack();
		showActivityFrame.setVisible(true);
		safOpen = true;

		activityName = new JLabel();
		activityCreator = new JLabel();
		activityCalendar = new JLabel();
		activityStart = new JLabel();
		activityEnd = new JLabel();
		removeActivity = new JButton("Ta bort aktivitet");

		String SQL = "SELECT *FROM events WHERE eventID=?";
		String types = "i";
		Object[] params = { eventID };
		Object[][] matrix = kalenderSystem_getData("kalenderSystem_getData.php", SQL, types, params);

		String SQLUsers = "SELECT *FROM users WHERE userID=?";
		String typesUsers = "i";
		Object[] paramsUsers = { matrix[0][1] };
		Object[][] matrixUsers = kalenderSystem_getData("kalenderSystem_getData.php", SQLUsers, typesUsers,
				paramsUsers);

		String SQLCalendars = "SELECT *FROM calendars WHERE calendarID=?";
		String typesCalendars = "i";
		Object[] paramsCalendars = { matrix[0][2] };
		Object[][] matrixCalendars = kalenderSystem_getData("kalenderSystem_getData.php", SQLCalendars, typesCalendars,
				paramsCalendars);

		activityName.setText("Eventnamn: " + (String) matrix[0][3]);
		activityName.setFont(newFont);

		activityCreator.setText("Skapare: " + (String) matrixUsers[0][4] + " " + matrixUsers[0][5]);
		activityCreator.setFont(newFont);

		activityCalendar.setText("Kalender: " + (String) matrixCalendars[0][1]);
		activityCalendar.setFont(newFont);

		activityStart.setText("Starttid: " + (String) matrix[0][4]);
		activityStart.setFont(newFont);

		activityEnd.setText("Sluttid: " + (String) matrix[0][5]);
		activityEnd.setFont(newFont);

		removeActivity.setBackground(new Color(255, 175, 175));
		removeActivity.addActionListener(this);
		removeActivity.setName("" + matrix[0][0]);
		removeActivity.setActionCommand("removeActivity");

		panel.add(new JLabel(""));
		panel.add(activityName);

		panel.add(new JLabel(""));
		panel.add(activityCreator);

		panel.add(new JLabel(""));
		panel.add(activityCalendar);

		panel.add(new JLabel(""));
		panel.add(activityStart);

		panel.add(new JLabel(""));
		panel.add(activityEnd);

		if ((int) matrix[0][1] == userID) {

			JButton button = new JButton("Bjud in användare");
			button.addActionListener(this);
			button.setActionCommand("inviteUsers");
			button.setName("" + eventID);
			panel.add(new JLabel(""));
			panel.add(button);
		}

		panel.add(new JLabel(""));
		panel.add(removeActivity);

		pack();
	}

	public void kalenderSystem_showCalendarInvitePane(int calendarID) {

		kalenderSystem_loggedInMenuPane();

		if (scfOpen) {
			showCalendarInviteFrame.setVisible(false);
		}
		showCalendarInviteFrame = new JFrame();
		Point p = super.getLocation();
		p.setLocation((int) p.getX() + 50, (int) p.getY() + 50);
		showCalendarInviteFrame.setLocation(p);
		showCalendarInviteFrame.setPreferredSize(new Dimension(500, 500));
		showCalendarInviteFrame.setTitle("Visa aktivitet");
		showCalendarInviteFrame.setLayout(new BorderLayout());

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(16, 1));

		JPanel fPane = new JPanel();
		fPane.setPreferredSize(new Dimension(80, 20));
		JPanel fPane2 = new JPanel();
		fPane2.setPreferredSize(new Dimension(80, 20));
		panel.setPreferredSize(new Dimension(300, 400));

		showCalendarInviteFrame.add(panel, BorderLayout.CENTER);
		showCalendarInviteFrame.add(fPane, BorderLayout.WEST);
		showCalendarInviteFrame.add(fPane2, BorderLayout.EAST);
		showCalendarInviteFrame.pack();
		showCalendarInviteFrame.setVisible(true);
		scfOpen = true;

		calendarName = new JLabel();
		calendarCreator = new JLabel();
		calendarEventQuantity = new JLabel();

		String SQL = "SELECT *FROM calendars WHERE calendarID=?";
		String types = "i";
		Object[] params = { calendarID };
		Object[][] matrix = kalenderSystem_getData("kalenderSystem_getData.php", SQL, types, params);

		String SQLUsers = "SELECT *FROM users WHERE userID=?";
		String typesUsers = "i";
		Object[] paramsUsers = { matrix[0][2] };
		Object[][] matrixUsers = kalenderSystem_getData("kalenderSystem_getData.php", SQLUsers, typesUsers,
				paramsUsers);

		String SQLEvents = "SELECT *FROM events WHERE calendarID=?";
		String typesEvents = "i";
		Object[] paramsEvents = { matrix[0][2] };
		Object[][] matrixEvents = kalenderSystem_getData("kalenderSystem_getData.php", SQLEvents, typesEvents,
				paramsEvents);

		calendarName.setText("Eventnamn: " + (String) matrix[0][1]);
		calendarName.setFont(newFont);

		calendarCreator.setText("Skapare: " + (String) matrixUsers[0][4] + " " + (String) matrixUsers[0][5]);
		calendarCreator.setFont(newFont);

		calendarEventQuantity.setText("Eventantal: " + matrixEvents.length);
		calendarEventQuantity.setFont(newFont);

		panel.add(calendarName);
		panel.add(calendarCreator);
		panel.add(calendarEventQuantity);
		pack();
	}

	public void kalenderSystem_addCalendarFrame() {

		if (acfOpen) {
			addCalendarFrame.setVisible(false);
		}
		addCalendarFrame = new JFrame();
		Point p = super.getLocation();
		p.setLocation((int) p.getX() + 50, (int) p.getY() + 50);
		addCalendarFrame.setLocation(p);
		addCalendarFrame.setPreferredSize(new Dimension(500, 250));
		addCalendarFrame.setTitle("Skapa ny kalender");
		addCalendarFrame.setLayout(new BorderLayout());

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(6, 1));

		String[] str_labels = { "", "Namnet på din nya kalender", "" };
		JLabel[] labels = new JLabel[str_labels.length];

		JTextField calendarName = new JTextField();
		calendarName.setFont(newFont);
		calendarName.setName("calendarName");

		for (int i = 0; i < labels.length; i++) {

			labels[i] = new JLabel(str_labels[i], JTextField.CENTER);
			labels[i].setFont(newFont);
			panel.add(labels[i]);
			if (str_labels[i].contains("Namnet")) {
				panel.add(calendarName);
			}

		}

		JButton button = new JButton("Skapa kalender");
		button.setActionCommand("Skapa kalender");
		button.addActionListener(this);

		panel.add(button);

		info.setText("");
		panel.add(info);

		JPanel fPane = new JPanel();
		fPane.setPreferredSize(new Dimension(80, 20));
		JPanel fPane2 = new JPanel();
		fPane2.setPreferredSize(new Dimension(80, 20));
		panel.setPreferredSize(new Dimension(300, 400));

		addCalendarFrame.add(panel, BorderLayout.CENTER);
		addCalendarFrame.add(fPane, BorderLayout.WEST);
		addCalendarFrame.add(fPane2, BorderLayout.EAST);
		addCalendarFrame.pack();
		addCalendarFrame.setVisible(true);
		acfOpen = true;

	}

	public void kalenderSystem_showMonthView(int int_year, int int_month) {

		kalenderSystem_loggedInMenuPane();

		contentPane.removeAll();
		breadCrumb.removeAll();
		Vector<Integer> years = new Vector<Integer>();
		for (int i = 0; i <= (Calendar.getInstance().get(Calendar.YEAR) - 1970) + 100; i++) {

			years.add((1970 + i));

		}

		year = new JComboBox<Integer>(years);
		year.setFont(newFont);
		year.setSelectedItem(years.get(int_year - 1970));
		year.addActionListener(this);
		year.setName("monthYear");
		breadCrumb.add(year);

		month = new JComboBox(this.str_month);
		month.setFont(newFont);
		month.setSelectedItem(this.str_month[int_month]);
		month.addActionListener(this);
		month.setName("monthMonth");
		breadCrumb.add(month);

		contentPane.setLayout(new BorderLayout());
		initialXSize = 50;
		containerFiller1.setPreferredSize(new Dimension(initialXSize, 0));
		containerFiller2.setPreferredSize(new Dimension(initialXSize, 0));

		JPanel cPanel = new JPanel();

		int day = 1;
		int maxDays = 0;
		if ((int_month == 0 || int_month == 2 || int_month == 4 || int_month == 6 || int_month == 7 || int_month == 9
				|| int_month == 11) && int_year > 0) {

			maxDays = 31;

		} else if ((int_month == 3 || int_month == 5 || int_month == 8 || int_month == 10) && int_year > 0) {

			maxDays = 30;

		} else if (int_month == 1 && int_year % 4 == 0) {

			maxDays = 29;

		} else if (int_month == 1 && int_year % 4 != 0) {

			maxDays = 28;

		}

		GregorianCalendar cal = new GregorianCalendar();
		cal.set(int_year, int_month, 1, 0, 0, 0);
		int start = cal.get(Calendar.WEEK_OF_YEAR);
		cal.set(Calendar.DAY_OF_MONTH, maxDays);
		int end = cal.get(Calendar.WEEK_OF_YEAR);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		int weeks = 0;
		if (start > 52) {
			start = 0;

			weeks = end - (start - 1);

		} else if (start == 52) {

			start = 0;

			weeks = end - (start - 1);

			end = 54;

		} else if (end == 1) {

			end = 53;
			weeks = end - (start - 1);
			end = 54;

		} else {

			weeks = (end - (start - 1));

		}
		GregorianCalendar ca = new GregorianCalendar();
		ca = (GregorianCalendar) Calendar.getInstance();
		ca.set(Calendar.YEAR, 2015);
		ca.set(Calendar.DAY_OF_MONTH, 31);
		ca.set(Calendar.MONTH, 11);
		int weeksInYear = ca.get(Calendar.WEEK_OF_YEAR);
		if (end == 54) {
			weeksInYear = 52;
		}
		cPanel.setLayout(new GridLayout(weeks + 1, 7));
		JPanel fPanel = new JPanel();
		fPanel.setPreferredSize(new Dimension(400, initialXSize));

		String[] dagar = { "Måndag", "Tisdag", "Onsdag", "Torsdag", "Fredag", "Lördag", "Söndag" };
		for (int i = 0; i < dagar.length; i++) {
			JLabel dayLabel = new JLabel(dagar[i], JTextField.CENTER);
			dayLabel.setFont(newFont);
			cPanel.add(dayLabel);
		}

		Font f = new Font("Arial", Font.BOLD, 13);
		JPanel weekPanel = new JPanel();
		weekPanel.setLayout(new GridLayout(weeks + 1, 1));
		weekPanel.add(new JLabel(""));

		for (int i = 0; i < (weeks); i++) {
			int int_week = (cal.get(Calendar.WEEK_OF_YEAR) + i);
			if (int_week > weeksInYear) {
				int_week %= weeksInYear;
			}
			JLabel week = new JLabel("v. " + int_week);
			week.setFont(f);
			weekPanel.add(week);

			for (int j = 0; j < 7; j++) {
				int temp = (cal.get(Calendar.DAY_OF_WEEK) - 2);
				if (temp < 0) {
					temp = 6;
				}
				if (i == 0 && j < temp) {

					JButton tempButton = new JButton();
					tempButton.setEnabled(false);
					cPanel.add(tempButton);

				} else {

					if (day <= maxDays) {

						JButton dayButton = new JButton("" + day);
						dayButton.addActionListener(this);
						dayButton.setActionCommand("showActivitiesOfDay");
						dayButton.setName(int_year + "-" + (int_month + 1) + "-" + day + " 00:00:00");
						dayButton.setFont(f);
						cPanel.add(dayButton);
						day++;

					} else {

						JButton tempButton = new JButton();
						tempButton.setEnabled(false);
						cPanel.add(tempButton);

					}
				}
			}

		}

		contentPane.add(cPanel, BorderLayout.CENTER);
		contentPane.add(weekPanel, BorderLayout.WEST);
		contentPane.add(fPanel, BorderLayout.SOUTH);

		contentPane.repaint();
		pack();
		super.repaint();
	}

	public void kalenderSystem_showDayView(int int_year, int int_week) {

		kalenderSystem_loggedInMenuPane();

		contentPane.removeAll();
		breadCrumb.removeAll();
		Vector<Integer> years = new Vector<Integer>();
		for (int i = 0; i <= (Calendar.getInstance().get(Calendar.YEAR) - 1970) + 100; i++) {

			years.add((1970 + i));

		}

		year = new JComboBox<Integer>(years);
		year.setFont(newFont);
		year.setSelectedItem(years.get(int_year - 1970));
		year.addActionListener(this);
		year.setName("weekYear");
		breadCrumb.add(year);

		GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 31);
		cal.set(Calendar.YEAR, int_year);
		cal.set(Calendar.MONTH, 11);
		int end = cal.get(Calendar.WEEK_OF_YEAR);

		if (end == 1) {
			end = 52;
		}

		Vector<Integer> weeks = new Vector<Integer>();

		for (int i = 1; i <= end; i++) {

			weeks.add(i);

		}

		JComboBox week = new JComboBox<Integer>(weeks);
		week.setFont(newFont);
		week.setSelectedItem(int_week);
		week.addActionListener(this);
		week.setName("weekWeek");
		breadCrumb.add(week);

		contentPane.setLayout(new BorderLayout());
		initialXSize = 50;
		containerFiller1.setPreferredSize(new Dimension(0, 0));
		containerFiller2.setPreferredSize(new Dimension(0, 0));

		JPanel cPanel = new JPanel();

		cPanel.setLayout(new BorderLayout());

		JPanel dayNamePanel = new JPanel();
		dayNamePanel.setLayout(new GridLayout(1, 7));

		JPanel activityPanel = new JPanel();
		activityPanel.setLayout(new GridLayout(1, 7));

		cal = (GregorianCalendar) Calendar.getInstance();
		cal.set(Calendar.YEAR, int_year);
		cal.set(Calendar.WEEK_OF_YEAR, int_week);

		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

		JButton[][] activities = new JButton[7][];

		for (int i = 0; i < 7; i++) {

			Object[][] matrix = kalenderSystem_getActivities(cal);
			activities[i] = new JButton[matrix.length];

			for (int j = 0; j < matrix.length; j++) {
				for (int k = 0; k < matrix.length - (j + 1); k++) {
					try {

						if (df.parse((String) matrix[k + 1][2]).compareTo(df.parse((String) matrix[k][2])) < 0) {

							Object[] temp = matrix[k];
							matrix[k] = matrix[k + 1];
							matrix[k + 1] = temp;

						}
					} catch (Exception e) {

						e.printStackTrace();
					}
				}
			}

			for (int j = 0; j < matrix.length; j++) {

				activities[i][j] = new JButton();
				String name = ((String) matrix[j][1]);
				String startTime = ((String) matrix[j][2]).substring(((String) matrix[j][2]).indexOf(" "),
						((String) matrix[j][2]).length());
				String endTime = ((String) matrix[j][3]).substring(((String) matrix[j][3]).indexOf(" "),
						((String) matrix[j][3]).length());
				activities[i][j]
						.setText("<html>" + startTime + "<br /><br />" + name + "<br /><br />" + endTime + "</html>");
				activities[i][j].setName("" + matrix[j][0]);
				activities[i][j].addActionListener(this);
				activities[i][j].setActionCommand("viewActivity");

			}

			int day = cal.get(Calendar.DAY_OF_MONTH);
			day++;
			cal.set(Calendar.DAY_OF_MONTH, day);
		}

		String[] dagar = { "Måndag", "Tisdag", "Onsdag", "Torsdag", "Fredag", "Lördag", "Söndag" };
		for (int i = 0; i < dagar.length; i++) {

			JLabel dayLabel = new JLabel(dagar[i], JTextField.CENTER);
			dayLabel.setFont(newFont);
			dayNamePanel.add(dayLabel);

		}

		JPanel[] dayPanel = new JPanel[7];

		for (int i = 0; i < dayPanel.length; i++) {

			dayPanel[i] = new JPanel();
			if (i == 0) {

				dayPanel[i].setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, new Color(160, 160, 160)));

			} else {

				dayPanel[i].setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, new Color(160, 160, 160)));

			}

			if (activities[i].length < 6) {
				dayPanel[i].setLayout(new GridLayout(6, 1));
			} else {
				dayPanel[i].setLayout(new GridLayout(activities[i].length, 1));
			}

			for (int j = 0; j < activities[i].length; j++) {

				dayPanel[i].add(activities[i][j]);
			}

			activityPanel.add(dayPanel[i]);
		}

		Font f = new Font("Arial", Font.BOLD, 13);

		cPanel.add(dayNamePanel, BorderLayout.NORTH);

		JScrollPane dayViewScroll = new JScrollPane(activityPanel);
		cPanel.add(dayViewScroll, BorderLayout.CENTER);
		contentPane.add(cPanel);
		contentPane.repaint();
		pack();
		super.repaint();

	}

	public void kalenderSystem_inviteUserFrame(int ID, String calendarOrEvent) {

		currentFrame = "inviteUser";
		if (iufOpen) {
			inviteUserFrame.setVisible(false);
		}
		inviteUserFrame = new JFrame();
		Point p = super.getLocation();
		p.setLocation((int) p.getX() + 50, (int) p.getY() + 50);
		inviteUserFrame.setLocation(p);
		inviteUserFrame.setPreferredSize(new Dimension(500, 500));
		inviteUserFrame.setTitle("Bjud in användare");
		inviteUserFrame.setLayout(new BorderLayout());

		JPanel panel = new JPanel();

		panel.setLayout(new GridLayout(3, 1));

		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new GridLayout(4, 1));

		if (calendarOrEvent.equals("calendar")) {

			searchPanel.setLayout(new GridLayout(6, 1));
			searchPanel.add(new JLabel(""));
			calendars = new Vector<String>();
			Object[][] matrix = kalenderSystem_getCalendars();

			for (int i = 0; i < matrix.length; i++) {

				calendars.add((String) matrix[i][1]);
				calendarIDs.add((int) matrix[i][0]);
			}

			JComboBox calendar = new JComboBox(calendars);
			searchPanel.add(calendar);
		}

		String[] labelText = { "", "Sök användare" };
		JLabel[] labels = new JLabel[labelText.length];

		for (int i = 0; i < labels.length; i++) {

			labels[i] = new JLabel(labelText[i], SwingConstants.CENTER);
			labels[i].setFont(newFont);
			searchPanel.add(labels[i]);

			if (labelText[i].equals("Sök användare")) {

				JTextField textField = new JTextField();
				textField.setFont(newFont);
				textField.setName("searchUser");
				textField.setActionCommand("searchUser");
				textField.addKeyListener(this);
				searchPanel.add(textField);

			}

		}

		String SQL = "SELECT userID, username, firstName, lastName FROM users WHERE userID != ? and userID != ?  ORDER BY username ASC";
		String types = "ii";
		Object[] params = { 0, userID };

		Object[][] matrix = kalenderSystem_getData("kalendersystem_getData.php", SQL, types, params);

		JPanel userPanel = new JPanel();
		userPanel.setLayout(new GridLayout(matrix.length, 1));

		for (int i = 0; i < matrix.length; i++) {

			JCheckBox choice = new JCheckBox();
			choice.setText((String) matrix[i][1]);
			choice.setActionCommand("" + (matrix[i][0]));

			userPanel.add(choice);

		}

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(5, 1));

		JButton button = new JButton("Bjud in användare");
		button.setActionCommand("inviteUser");
		button.setName(calendarOrEvent + ID);
		button.addActionListener(this);

		buttonPanel.add(new JLabel(""));
		buttonPanel.add(button);

		info.setText("");
		searchPanel.add(info);

		JScrollPane scroll = new JScrollPane(userPanel);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		panel.add(searchPanel);
		panel.add(scroll);
		panel.add(buttonPanel);

		JPanel fPane = new JPanel();
		fPane.setPreferredSize(new Dimension(80, 20));
		JPanel fPane2 = new JPanel();
		fPane2.setPreferredSize(new Dimension(80, 20));
		panel.setPreferredSize(new Dimension(300, 400));

		inviteUserFrame.add(panel, BorderLayout.CENTER);
		inviteUserFrame.add(fPane, BorderLayout.WEST);
		inviteUserFrame.add(fPane2, BorderLayout.EAST);

		inviteUserFrame.pack();
		inviteUserFrame.setVisible(true);
		iufOpen = true;

	}

	public void kalenderSystem_showInfoBoard(String message) {

		infoBoard = new JFrame();
		infoBoard.setLocation((int) super.getLocation().getX() + 50, (int) super.getLocation().getY() + 50);
		infoBoard.setLayout(new BorderLayout());
		infoBoard.setName("infoBoard");
		infoBoard.setTitle("Information");
		infoBoard.addWindowListener(this);
		infoBoard.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(5, 1));

		panel.add(new JLabel(""));

		JPanel infoPanel = new JPanel();
		info = new JLabel("", JLabel.CENTER);
		message = message.replaceAll("\n", "<br />");
		info.setText("<html><p>" + message + "</p></html>");
		info.setFont(newFont);

		infoPanel.add(info);
		JScrollPane scroll = new JScrollPane(infoPanel);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		panel.add(info);

		JButton button = new JButton();
		button.addActionListener(this);
		button.setActionCommand("infoOk");
		button.setText("Ok");

		panel.add(new JLabel(""));
		panel.add(button);

		JPanel fPane = new JPanel();
		fPane.setPreferredSize(new Dimension(80, 20));
		JPanel fPane2 = new JPanel();
		fPane2.setPreferredSize(new Dimension(80, 20));
		panel.setPreferredSize(new Dimension(300, 400));

		infoBoard.add(panel, BorderLayout.CENTER);
		infoBoard.add(fPane, BorderLayout.WEST);
		infoBoard.add(fPane2, BorderLayout.EAST);

		infoBoard.pack();
		infoBoard.setVisible(true);
	}

	public void kalenderSystem_loggedInMenuPane() {

		menuPanel.removeAll();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0;
		c.weighty = 1;
		x = 0;
		y = 0;
		c.gridx = x;
		c.gridy = y;
		c.gridwidth = 1;
		c.insets = new Insets(15, 0, 0, 0);
		c.anchor = GridBagConstraints.NORTHWEST;

		menuPanel.setLayout(new GridBagLayout());

		Font f = new Font("Arial", Font.BOLD, 13);
		c.gridx = x;
		c.gridy = y;
		menuPanel.add(new JLabel(""), c);
		y++;

		String[] menuNames = { "Månadsvy", "Veckovy", "Skapa Aktivitet", "Skapa Kalender", "Inbjudningar", "Bjud In",
				"Logga ut" };
		JButton[] buttons = new JButton[menuNames.length];

		for (int i = 0; i < buttons.length; i++) {

			c.gridx = x;
			c.gridy = y;

			buttons[i] = new JButton(menuNames[i]);
			buttons[i].addActionListener(this);
			buttons[i].setActionCommand("menu" + menuNames[i]);
			buttons[i].setFont(f);

			menuPanel.add(buttons[i], c);
			y++;

			if (buttons[i].getActionCommand().equals("menuVeckovy")) {

				c.gridy = y;
				menuPanel.add(new JLabel(""), c);

				y++;

			} else if (buttons[i].getActionCommand().equals("menuInbjudningar")) {

				String SQL = "SELECT eventInviteID FROM eventinvites WHERE userID = ? AND declined = ?";
				String types = "ii";
				Object[] params = { userID, 0 };

				Object[][] matrix = kalenderSystem_getData("kalenderSystem_getData.php", SQL, types, params);

				int invites = matrix.length;

				if (matrix.length > 0) {

					buttons[i].setForeground(new Color(225, 0, 0));

				}

				SQL = "SELECT calendarInviteID FROM calendarinvites WHERE userID = ? AND declined = ?";
				types = "ii";
				params = new Object[] { userID, 0 };

				matrix = kalenderSystem_getData("kalenderSystem_getData.php", SQL, types, params);

				if (matrix.length > 0) {

					buttons[i].setForeground(new Color(225, 0, 0));
					invites += matrix.length;
				}

				if (invites > 0) {
					buttons[i].setText("Inbjudningar (" + invites + ")");
				}

				c.gridy = y;
				menuPanel.add(new JLabel(""), c);
				y++;

				c.gridy = y;
				JLabel user = new JLabel(username + "", SwingConstants.CENTER);
				user.setFont(f);
				menuPanel.add(user, c);
				y++;

			}

		}

	}

	public void kalenderSystem_inviteUser(String[] str_userIDs, String calendarOrEvent, int ID) {

		int[] userIDs = new int[str_userIDs.length];

		for (int i = 0; i < userIDs.length; i++) {

			userIDs[i] = Integer.parseInt(str_userIDs[i]);

		}

		for (int i = 0; i < userIDs.length; i++) {

			String SQL = "SELECT " + calendarOrEvent + "InviteID FROM " + calendarOrEvent + "invites WHERE "
					+ calendarOrEvent + "ID = ? AND userID = ? AND declined = ?";
			String types = "iii";
			Object[] params = { ID, userIDs[i], 0 };

			Object[][] matrix = kalenderSystem_getData("kalenderSystem_getData.php", SQL, types, params);

			if (matrix.length == 0) {

				if (calendarOrEvent.equals("calendar")) {

					SQL = "SELECT acceptedCalendarsID FROM accepted" + calendarOrEvent
							+ "s WHERE calendarID = ? AND userID = ?";
					types = "ii";
					params = new Object[] { ID, userIDs[i] };

					matrix = kalenderSystem_getData("kalenderSystem_getData.php", SQL, types, params);

				} else if (calendarOrEvent.equals("event")) {

					SQL = "SELECT acceptedEventsID FROM accepted" + calendarOrEvent
							+ "s WHERE eventID = ? AND userID = ?";
					types = "ii";
					params = new Object[] { ID, userIDs[i] };
					matrix = kalenderSystem_getData("kalenderSystem_getData.php", SQL, types, params);

				}

				if (matrix.length == 0) {

					SQL = "INSERT INTO " + calendarOrEvent + "invites(" + calendarOrEvent
							+ "ID, userID, declined) VALUES(?, ?, ?)";
					types = "iii";

					params = new Object[] { ID, userIDs[i], 0 };

					System.out.println(SQL + " " + ID + " " + userIDs[i]);

					kalenderSystem_sendData("kalenderSystem_sendData.php", SQL, types, params);

					inviteUserFrame.setVisible(false);

				}

			}

		}

	}

	public void kalenderSystem_searchUser(String searchPhrase, JPanel userPanel) {

		Component[] comps = userPanel.getComponents();
		for (int i = 0; i < comps.length; i++) {

			if (comps[i].getClass().getSimpleName().equals("JCheckBox")) {

				if (!((JCheckBox) comps[i]).isSelected()) {

					userPanel.remove(comps[i]);

				}

			}

		}

		searchPhrase = "%" + searchPhrase + "%";
		searchPhrase.replaceAll("%", "");

		Vector<Integer> userIDs = new Vector<Integer>();

		String SQL = "SELECT userID FROM users WHERE username LIKE ? OR firstName LIKE ? OR lastName LIKE ? ORDER BY username ASC";
		// System.out.println(SQL);
		String types = "sss";
		Object[] params = { searchPhrase, searchPhrase, searchPhrase };

		Object[][] matrix = kalenderSystem_getData("kalenderSystem_getData.php", SQL, types, params);

		for (int i = 0; i < matrix.length; i++) {

			userIDs.add((int) matrix[i][0]);

		}

		userIDs.removeElement(userID);

		matrix = new Object[userIDs.size()][];
		for (int i = 0; i < userIDs.size(); i++) {

			SQL = "SELECT userID, username, firstName, lastName FROM users WHERE userID = ?";
			types = "i";
			params = new Object[] { userIDs.get(i) };

			Object[][] temp = kalenderSystem_getData("kalenderSystem_getData.php", SQL, types, params);
			matrix[i] = temp[0];

		}

		comps = userPanel.getComponents();
		Vector<String> names = new Vector<String>();

		for (int i = 0; i < comps.length; i++) {

			if (comps[i].getClass().getSimpleName().equals("JCheckBox")) {

				if (((JCheckBox) comps[i]).isSelected()) {

					names.add(((JCheckBox) comps[i]).getText());

				}

			}

		}

		int checkBoxes = 0;

		for (int i = 0; i < matrix.length; i++) {
			if (names.indexOf(matrix[i][1]) < 0) {
				checkBoxes++;
			}
		}

		userPanel.setLayout(new GridLayout(names.size() + checkBoxes, 1));

		for (int i = 0; i < matrix.length; i++) {
			if (names.indexOf(matrix[i][1]) < 0) {
				JCheckBox coice = new JCheckBox();
				coice.setText((String) matrix[i][1]);

				userPanel.add(coice);
			}
		}

		inviteUserFrame.pack();
		inviteUserFrame.repaint();

	}

	public boolean kalenderSystem_deleteActivity(int eventID) {

		String SQL = "UPDATE events SET calendarID = ? WHERE eventID=?";
		String types = "ii";
		Object[] params = { 0, eventID };

		kalenderSystem_sendData("kalenderSystem_sendData.php", SQL, types, params);

		return false;

	}

	/*
	 * Används för att hämta alla aktiviteter i en kalender Inputs: - Outputs.
	 * Object[][] matrix, innehåller all information om eventen i en kalender.
	 * Object[][0] eventID Object[][1] name Object[][2] StartDate Object[][3]
	 * endDate object[][4] creatorID
	 */
	public Object[][] kalenderSystem_getActivities(Calendar cal) {

		String SQL = "SELECT calendarID FROM acceptedcalendars WHERE userID=?";
		String types = "i";
		Object[] params = { userID };

		Object[][] temp = kalenderSystem_getData("kalenderSystem_getData.php", SQL, types, params);

		Vector<Integer> eventIDs = new Vector<Integer>();

		for (int i = 0; i < temp.length; i++) {

			SQL = "SELECT eventID FROM events WHERE calendarID = ?";
			types = "i";
			params = new Object[] { temp[i][0] };

			Object[][] matrix = kalenderSystem_getData("kalenderSystem_getData.php", SQL, types, params);

			for (int j = 0; j < matrix.length; j++) {

				eventIDs.add((int) matrix[j][0]);
			}

		}

		SQL = "SELECT eventID FROM acceptedevents WHERE userID=?";
		types = "i";
		params = new Object[] { userID };

		Object[][] matrix = kalenderSystem_getData("kalenderSystem_getData.php", SQL, types, params);

		for (int i = 0; i < matrix.length; i++) {

			eventIDs.add((int) matrix[i][0]);

		}

		SQL = "SELECT eventID FROM events WHERE creatorID=?";
		types = "i";
		params[0] = userID;
		matrix = kalenderSystem_getData("kalenderSystem_getData.php", SQL, types, params);

		for (int i = 0; i < matrix.length; i++) {

			eventIDs.add((int) matrix[i][0]);

		}

		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);

		Date startDate = cal.getTime();

		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.MILLISECOND, 999);

		Date endDate = cal.getTime();

		String str_startDate = df.format(startDate);
		String str_endDate = df.format(endDate);

		Vector<Object[]> events = new Vector<Object[]>();
		for (int i = 0; i < eventIDs.size(); i++) {

			int eventID = eventIDs.get(i);
			SQL = "SELECT eventID, name, startTime, endTime, creatorID FROM events WHERE eventID=? AND startTime >= ? AND endTime <= ? AND calendarID != ?";
			types = "issi";
			params = new Object[4];

			params[0] = eventID;
			params[1] = str_startDate;
			params[2] = str_endDate;
			params[3] = 0;

			temp = kalenderSystem_getData("kalenderSystem_getData.php", SQL, types, params);

			if (temp.length > 0) {
				events.add(temp[0]);
			}

		}

		matrix = new Object[events.size()][];

		for (int i = 0; i < matrix.length; i++) {

			matrix[i] = events.get(i);

		}

		return matrix;

	}

	/*
	 * Används för att lägga till en aktivitet i en kalender Inputs: -int
	 * calendarID, id:t på den kalendern man vill lägga till aktiviteten i -String
	 * eventName, Namnet på den aktivitet man vill lägga till -Date startTime,
	 * startdatumet och tiden på det event man lägger till -Date endTime,
	 * slutdatumet och tiden på det event man lägger till.
	 * 
	 * Outputs. boolean, returnerar true om aktiviteten lades till, annars
	 * returnerar den false
	 */
	public boolean kalenderSystem_createActivity(int calendarID, String eventName, Date startTime, Date endTime) {

		if (startTime.compareTo(endTime) < 0) {

			String SQL = "SELECT eventID FROM acceptedevents WHERE userID=?";
			String types = "i";
			Object[] params = { userID };

			Object[][] matrix = kalenderSystem_getData("kalenderSystem_getData.php", SQL, types, params);

			Vector<Integer> eventIDs = new Vector<Integer>();

			for (int i = 0; i < matrix.length; i++) {

				eventIDs.add((int) matrix[i][0]);

			}

			SQL = "SELECT eventID FROM events WHERE creatorID=?";
			types = "i";
			params[0] = userID;
			matrix = kalenderSystem_getData("kalenderSystem_getData.php", SQL, types, params);

			for (int i = 0; i < matrix.length; i++) {

				eventIDs.add((int) matrix[i][0]);

			}

			matrix = new Object[eventIDs.size()][];

			for (int i = 0; i < eventIDs.size(); i++) {

				int eventID = eventIDs.get(i);

				SQL = "SELECT name, startTime, endTime, creatorID FROM events WHERE eventID=? AND calendarID = ?";
				types = "ii";
				params = new Object[2];
				params[0] = eventID;
				params[1] = calendarID;

				Object[][] temp = kalenderSystem_getData("kalenderSystem_getData.php", SQL, types, params);

				if (temp.length > 0) {
					matrix[i] = temp[0];

					try {

						matrix[i][1] = df.parse((String) matrix[i][1]);
						matrix[i][2] = df.parse((String) matrix[i][2]);

					} catch (Exception e) {

						e.printStackTrace();
						return false;
					}

					if (startTime.compareTo((Date) matrix[i][1]) >= 0 && startTime.compareTo((Date) matrix[i][2]) < 0) {

						kalenderSystem_showInfoBoard("Du försöker dubbelboka dig! \nDär ligger redan " + matrix[i][0]
								+ " som börjar " + df.format((Date) matrix[i][1]) + " och slutar "
								+ df.format((Date) matrix[i][2]));

						return false;

					} else if (endTime.compareTo((Date) matrix[i][1]) > 0
							&& endTime.compareTo((Date) matrix[i][2]) <= 0) {

						kalenderSystem_showInfoBoard("Du försöker dubbelboka dig! \nDär ligger redan " + matrix[i][0]
								+ " som börjar " + df.format((Date) matrix[i][1]) + " och slutar "
								+ df.format((Date) matrix[i][2]));
						return false;

					} else if (startTime.compareTo((Date) matrix[i][1]) < 0
							&& endTime.compareTo((Date) matrix[i][2]) > 0) {

						kalenderSystem_showInfoBoard("Du försöker dubbelboka dig! \nDär ligger redan " + matrix[i][0]
								+ " som börjar " + df.format((Date) matrix[i][1]) + " och slutar "
								+ df.format((Date) matrix[i][2]));
						return false;

					} else if (startTime.compareTo((Date) matrix[i][1]) == 0
							&& endTime.compareTo((Date) matrix[i][2]) == 0) {

						kalenderSystem_showInfoBoard("Du försöker dubbelboka dig! \nDär ligger redan " + matrix[i][0]
								+ " som börjar " + df.format((Date) matrix[i][1]) + " och slutar "
								+ df.format((Date) matrix[i][2]));
						return false;

					} else {

					}
				}

			}

			SQL = "INSERT INTO events(creatorID, calendarID, name, startTime, endTime) values(?, ?, ?, ?, ?)";
			types = "iisss";
			params = new Object[5];
			params[0] = userID;
			params[1] = calendarID;
			params[2] = eventName;
			params[3] = df.format(startTime);
			params[4] = df.format(endTime);

			kalenderSystem_sendData("kalenderSystem_sendData.php", SQL, types, params);
			return true;

		}

		return false;
	}

	public Object[][] kalenderSystem_getCalendars() {

		String SQL = "SELECT calendarID FROM acceptedcalendars WHERE userID=? AND calendarID != ?";
		String types = "ii";
		Object[] params = { userID, 0 };

		Object[][] matrix = kalenderSystem_getData("kalenderSystem_getData.php", SQL, types, params);

		Vector<Integer> calendarIDs = new Vector<Integer>();
		SQL = "SELECT calendarID FROM calendars WHERE creatorID=? AND calendarID != ?";
		types = "ii";
		params[0] = userID;
		params[1] = 0;
		matrix = kalenderSystem_getData("kalenderSystem_getData.php", SQL, types, params);

		for (int i = 0; i < matrix.length; i++) {

			calendarIDs.add((int) matrix[i][0]);

		}

		matrix = new Object[calendarIDs.size()][];

		for (int i = 0; i < calendarIDs.size(); i++) {

			int calendarID = calendarIDs.get(i);

			SQL = "SELECT calendarID, name, creatorID FROM calendars WHERE calendarID=?";
			types = "i";
			params = new Object[1];
			params[0] = calendarID;

			Object[][] temp = kalenderSystem_getData("kalenderSystem_getData.php", SQL, types, params);
			if (temp.length > 0) {
				matrix[i] = temp[0];
			}

		}

		return matrix;

	}

	/*
	 * Skapar en kalender med namnet name Inputs: -String name, Nament på kalendern
	 * som ska skapas Outputs: -
	 */
	public void kalenderSystem_createCalendar(String name) {

		String SQL = "SELECT calendarID FROM calendars WHERE name = ? AND creatorID = ?";
		String types = "si";
		Object[] params = { name, userID };

		Object[][] matrix = kalenderSystem_getData("kalenderSystem_getData.php", SQL, types, params);

		if (matrix.length == 0) {

			SQL = "SELECT calendarID FROM calendars WHERE calendarID>=? ORDER BY calendarID DESC";
			types = "i";
			params = new Object[1];
			params[0] = 0;

			matrix = kalenderSystem_getData("kalenderSystem_getData.php", SQL, types, params);

			int calID = (int) matrix[0][0];
			calID++;

			SQL = "INSERT INTO calendars(calendarID, name, creatorID) VALUES(?, ?, ?)";
			types = "isi";
			params = new Object[3];

			params[0] = calID;
			params[1] = name;
			params[2] = userID;
			kalenderSystem_sendData("kalenderSystem_sendData.php", SQL, types, params);
			addCalendarFrame.setVisible(false);
			acfOpen = false;

			kalenderSystem_showCalendarPane();

		} else {

			kalenderSystem_showInfoBoard("Du har redan en kalender med det namnet!");

		}

	}

	/*
	 * Används för att logga in en ny användare Inputs: -String username, det
	 * användarnamn man försäker logga in med -String password, det lösenord man
	 * försäker logga in med
	 * 
	 * Outputs: -boolean, för true om inloggningen lyckats annars returnar den false
	 */
	public boolean kalenderSystem_login(String username, String password) {

		String str_url = "http://localhost/kalenderSystem_server/login.php";
		String query = "?username=" + username + "&password=" + password;

		str_url = str_url + query;

		try {
			URL url = new URL(str_url);
			URLConnection conn = url.openConnection();
			InputStream is = conn.getInputStream();

			String retval = "";
			while (is.available() > 0) {

				retval = retval + (char) is.read();

			}
			is.close();

			if (!retval.equals("0")) {
				try {
					JSONObject json = new JSONObject(retval);
					userID = json.getInt("0");

					hashkey = json.getString("1");

					String SQL = "SELECT hashID FROM hash WHERE hash = ? AND userID = ?";
					String types = "si";
					Object[] params = { hashkey, userID };

					Object[][] matrix = kalenderSystem_getData("kalenderSystem_getData.php", SQL, types, params);

					if (matrix.length == 1) {

						SQL = "SELECT calendarID FROM calendars WHERE calendarID=?";
						types = "i";
						params = new Object[1];
						params[0] = 0;

						matrix = kalenderSystem_getData("kalenderSystem_getData.php", SQL, types, params);

						if (matrix.length < 1) {

							SQL = "INSERT INTO calendars(calendarID, name, creatorID) values(?, ?, ?)";
							types = "isi";
							params = new Object[3];
							params[0] = 0;
							params[1] = "DeletedEvents";
							params[2] = 1;

							kalenderSystem_sendData("kalenderSystem_sendData.php", SQL, types, params);

						}
						return true;

					} else {

						userID = -1;
						hashkey = "";

						return false;
					}

				} catch (Exception e) {

					System.out.println("Login:" + retval);
					e.printStackTrace();

				}
			} else {
				return false;
			}

		} catch (IOException e) {

			e.printStackTrace();
		}

		return false;
	}

	public void kalenderSystem_tryLogin() {

		String username = usernameTextField.getText();
		char[] char_password = passwordTextField.getPassword();
		String password = "";

		for (int i = 0; i < char_password.length; i++) {

			password = password + char_password[i];
		}

		if (kalenderSystem_login(username, password)) {

			contentPane.removeAll();
			info.setText("");
			this.username = username;
			kalenderSystem_showCalendarPane();

		} else {
			this.username = "";
			info.setText("Fel användarnamn/lösenord");

		}
	}

	public void kalenderSystem_logout() {

		String SQL = "DELETE FROM hash WHERE userID=?";
		String types = "i";
		Object[] values = { userID };

		kalenderSystem_sendData("kalenderSystem_sendData.php", SQL, types, values);

		kalenderSystem_showLoginPane();

	}

	/*
	 * Används för att registrera en ny användare Inputs: -String username, det
	 * användarnamn man vill ha -String password, det Lösenord man vill ha -String
	 * email, den email man vill registrera sig med -String firstName, förnamnet på
	 * personen som ska registreras -String lastName, efternamnet på personen som
	 * ska registreras
	 * 
	 * Outputs: -boolean, för true om registreringen lyckats annars returnar den
	 * false
	 */
	public boolean kalenderSystem_register(String username, String password, String email, String firstName,
			String lastName) {

		/*
		 * String SQL =
		 * "INSERT INTO users(username, password, email, firstName, lastName) VALUES(?, ?, ?, ?, ?)"
		 * ; String types = "sssss"; Object[] params = {username, password, email,
		 * firstName, lastName}; kalenderSystem_sendData("kalenderSystem_register.php",
		 * SQL, types, params);
		 */
		String str_url = "http://localhost/kalenderSystem_server/kalenderSystem_register.php";
		String query = "?username=" + username + "&password=" + password + "&email=" + email + "&firstName=" + firstName
				+ "&lastName=" + lastName;

		str_url = str_url + query;

		try {

			URL url = new URL(str_url);
			URLConnection conn = url.openConnection();
			InputStream is = conn.getInputStream();

			String retval = "";

			while (is.available() > 0) {

				retval = retval + (char) is.read();

			}
			is.close();

			if (retval.equals("1")) {

				kalenderSystem_showInfoBoard("Du är nu registrerad");
				kalenderSystem_showLoginPane();
				return true;

			} else {

				kalenderSystem_showInfoBoard("Någon är redan registrerad med det användarnamnet eller emailen!");
				System.out.println("Register: " + retval);
				return false;

			}

		} catch (Exception e) {

			e.printStackTrace();

		}
		return false;
	}

	/*
	 * Används för att skicka data till filen 'path' på localhost.
	 * 
	 * Inputs: -String path, filnamet på den fil man vill komma åt från servern
	 * -String SQL, SQL-uttrycket som man vill exekvera -String types, En textsträng
	 * där typerna av påfäljande variabler är där s = string, i = int, d = double
	 * och b = blob
	 * 
	 * Outputs: -boolean, för true om frågan lyckats.
	 * 
	 */
	public boolean kalenderSystem_sendData(String path, String SQL, String types, Object[] params) {

		String str_url = "http://localhost/kalenderSystem_server/" + path;
		String query = "?";

		if (types.equals("ÃÂ¤")) {

			query = query + "SQL=" + SQL + "&types=" + types;

		} else {

			JSONArray sendParams = new JSONArray(params);

			String[] index = new String[params.length];

			for (int i = 0; i < index.length; i++) {

				index[i] = "" + i;
			}
			JSONArray JSONIndex = new JSONArray(index);
			String values = sendParams.toJSONObject(JSONIndex).toString();
			query = query + "SQL=" + SQL + "&types=" + types + "&values=" + values;
		}

		query = query + "&userID=" + userID + "&hashkey=" + hashkey;
		str_url = str_url + query;
		str_url = str_url.replace(" ", "%20");

		try {

			URL url = new URL(str_url);
			URLConnection conn = url.openConnection();
			InputStream is = conn.getInputStream();

			String retval = "";
			while (is.available() > 0) {

				retval = retval + (char) is.read();

			}
			is.close();

			if (retval.equals("1")) {

				return true;

			} else {
				System.out.println("SendData: " + retval);
				return false;

			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return false;
	}

	/*
	 * Används för att skicka data till filen 'path' på localhost.
	 * 
	 * Inputs: -String path, filnamet på den fil man vill komma åt från servern
	 * -String SQL, SQL-uttrycket som man vill exekvera -String types, En textsträng
	 * där typerna av påfäljande variabler är där s = string, i = int, d = double
	 * och b = blob
	 * 
	 * Outputs: -Object[][] matrix som innehåller all den data man för från SQL
	 * frågan.
	 */
	public Object[][] kalenderSystem_getData(String path, String SQL, String types, Object[] params) {

		String str_url = "http://localhost/kalenderSystem_server/" + path;
		String query = "?";

		JSONArray sendParams = new JSONArray(params);
		String[] index = new String[params.length];

		for (int i = 0; i < index.length; i++) {

			index[i] = "" + i;
		}
		JSONArray JSONIndex = new JSONArray(index);
		String values = sendParams.toJSONObject(JSONIndex).toString();
		query = query + "SQL=" + SQL + "&types=" + types + "&values=" + values;

		query = query + "&userID=" + userID + "&hashkey=" + hashkey;
		str_url = str_url + query;
		str_url = str_url.replaceAll("%", "%25");
		str_url = str_url.replace(" ", "%20");

		try {
			URL url = new URL(str_url);
			URLConnection conn = url.openConnection();
			InputStream is = conn.getInputStream();

			String retval = "";
			while (is.available() > 0) {

				retval = retval + (char) is.read();

			}
			is.close();

			try {
				JSONObject jsons = new JSONObject(retval);
				int iMax = 0;
				int jMax = 0;
				for (int i = 0; !jsons.isNull("" + i); i++) {
					for (int j = 0; !((JSONObject) jsons.get("" + i)).isNull("" + j); j++) {

						if (iMax < i) {

							iMax = i;

						}

						if (jMax < j) {

							jMax = j;

						}
					}
				}

				iMax++;
				jMax++;
				Object[][] matrix = new Object[iMax][jMax];
				for (int i = 0; i < matrix.length; i++) {
					for (int j = 0; j < matrix[i].length; j++) {

						matrix[i][j] = ((JSONObject) jsons.get("" + i)).get("" + j);

					}
				}

				if (matrix.length == 1) {

					if (matrix[0].length == 1) {

						if (matrix[0][0].equals("null")) {

							matrix = new Object[0][0];

						}
					}
				}
				return matrix;

			} catch (Exception e) {
				System.out.println("getData:" + retval);
				e.printStackTrace();
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return null;
	}

	public void kalenderSystem_connectToServer() {

		try {
			URL url = new URL("http://localhost/kalenderSystem_server");
			URLConnection conn = url.openConnection();
			conn.getInputStream();

		} catch (Exception e) {

			JOptionPane.showMessageDialog(null, "Saknar anslutning till webbservern");
			e.printStackTrace();
			System.exit(0);

		}

	}

	public void componentHidden(ComponentEvent arg) {

	}

	public void componentMoved(ComponentEvent arg) {

	}

	public void componentResized(ComponentEvent arg) {

		String argName = arg.getSource().getClass().getSimpleName();

		double difference = startWidth - ((JFrame) arg.getSource()).getSize().getWidth();

		int width = (int) (initialXSize - (difference / 2));
		if (width <= 0) {

			width = 0;
		}

		containerFiller1.setPreferredSize(new Dimension(width, initialYSize));
		containerFiller2.setPreferredSize(new Dimension(width, initialYSize));
		contentPane.setPreferredSize(new Dimension(400, 600));

		super.repaint();

	}

	public void componentShown(ComponentEvent arg) {

	}

	public void actionPerformed(ActionEvent arg) {

		String component = arg.getSource().getClass().getSimpleName();

		switch (component) {
		case ("JButton"):

			int acceptDeclineID = 0;
			JButton button = (JButton) arg.getSource();
			if (button.getActionCommand().contains("accept")) {

				String acc = button.getActionCommand();

				if (acc.contains("Event")) {

					acc = acc.replaceAll("acceptEvent", "");

					acceptDeclineID = Integer.parseInt(acc);

					String SQL = "UPDATE eventinvites SET declined = ? WHERE eventID = ? and userID = ?";
					String types = "iii";
					Object[] params = { 2, acceptDeclineID, userID };

					kalenderSystem_sendData("kalenderSystem_sendData.php", SQL, types, params);

					SQL = "INSERT INTO acceptedevents(eventID, userID) values(?, ?)";
					types = "ii";
					params = new Object[] { acceptDeclineID, userID };

					kalenderSystem_sendData("kalenderSystem_sendData.php", SQL, types, params);

				} else if (acc.contains("Calendar")) {

					acc = acc.replaceAll("acceptCalendar", "");

					acceptDeclineID = Integer.parseInt(acc);

					String SQL = "UPDATE calendarinvites SET declined = ? WHERE calendarID = ? and userID = ?";
					String types = "iii";
					Object[] params = { 2, acceptDeclineID, userID };

					kalenderSystem_sendData("kalenderSystem_sendData.php", SQL, types, params);

					SQL = "INSERT INTO acceptedcalendars(calendarID, userID) values(?, ?)";
					types = "ii";
					params = new Object[] { acceptDeclineID, userID };

					kalenderSystem_sendData("kalenderSystem_sendData.php", SQL, types, params);
				}

				kalenderSystem_loggedInMenuPane();
				kalenderSystem_showInvitesPane();

			} else if (button.getActionCommand().contains("decline")) {

				String acc = button.getActionCommand();
				if (acc.contains("Event")) {

					acc = acc.replaceAll("declineEvent", "");

					acceptDeclineID = Integer.parseInt(acc);

					String SQL = "UPDATE eventinvites SET declined = ? WHERE eventID = ? and userID = ?";
					String types = "iii";
					Object[] params = { 1, acceptDeclineID, userID };

					kalenderSystem_sendData("kalenderSystem_sendData.php", SQL, types, params);

				} else if (acc.contains("Calendar")) {

					acc = acc.replaceAll("declineCalendar", "");

					acceptDeclineID = Integer.parseInt(acc);

					String SQL = "UPDATE calendarinvites SET declined = ? WHERE calendarID = ? and userID = ?";
					String types = "iii";
					Object[] params = { 1, acceptDeclineID, userID };

					kalenderSystem_sendData("kalenderSystem_sendData.php", SQL, types, params);

				}

				kalenderSystem_loggedInMenuPane();
				kalenderSystem_showInvitesPane();

			}

			switch (button.getActionCommand()) {
			case ("Logga in"):

				kalenderSystem_tryLogin();

				break;

			case ("menuRegistrera dig"):
				kalenderSystem_showRegisterPane();
				break;

			case ("Registrera dig"):

				String username = usernameTextField.getText();

				char[] chr_password = passwordTextField.getPassword();
				String password = "";
				for (int i = 0; i < chr_password.length; i++) {
					password = password + chr_password[i];
				}

				String email = emailTextField.getText();
				String fname = firstNameTextField.getText();
				String lname = lastNameTextField.getText();

				kalenderSystem_register(username, password, email, fname, lname);
				break;

			case ("menuLogga in"):

				kalenderSystem_showLoginPane();
				break;

			case ("menuLogga ut"):

				kalenderSystem_logout();
				break;

			case ("menuSkapa Aktivitet"):

				kalenderSystem_addActivityFrame();
				break;

			case ("menuInbjudningar"):

				kalenderSystem_showInvitesPane();
				break;

			case ("removeActivity"):

				int eventIDremove = Integer.parseInt(removeActivity.getName());
				kalenderSystem_deleteActivity(eventIDremove);
				kalenderSystem_showDayView(Calendar.getInstance().get(Calendar.YEAR),
						Calendar.getInstance().get(Calendar.WEEK_OF_YEAR));
				showActivityFrame.setVisible(false);
				safOpen = false;

				break;

			case ("Skapa aktivitet"):

				Component[] comp = ((JPanel) ((JButton) arg.getSource()).getParent()).getComponents();

				Vector<Integer> time = new Vector<Integer>();
				Vector<Date> dates = new Vector<Date>();
				String title = "";
				String calendar = "";
				for (int i = 0; i < comp.length; i++) {
					String className = comp[i].getClass().getSimpleName();

					if (className.equals("JComboBox")) {

						calendar = (String) ((JComboBox) comp[i]).getSelectedItem();

					} else if (className.equals("JTextField")) {

						title = ((JTextField) comp[i]).getText();

					} else if (className.equals("JXDatePicker")) {

						dates.add(((JXDatePicker) comp[i]).getDate());

					} else if (className.equals("JPanel")) {

						Component[] temp = ((JPanel) comp[i]).getComponents();

						for (int j = 0; j < temp.length; j++) {

							time.add((int) ((kalenderSystem_spinner) temp[j]).getModel().getValue());

						}

					}
				}
				GregorianCalendar cal = new GregorianCalendar();
				cal.setTime(dates.get(0));
				cal.set(Calendar.HOUR_OF_DAY, time.get(0));
				cal.set(Calendar.MINUTE, time.get(1));
				Date startDate = cal.getTime();

				cal.setTime(dates.get(0));
				cal.set(Calendar.HOUR_OF_DAY, time.get(2));
				cal.set(Calendar.MINUTE, time.get(3));
				Date endDate = cal.getTime();

				if (endDate.getTime() > startDate.getTime()) {
					int calendarID = calendarIDs.get(calendars.indexOf(calendar));
					if (calendarID > 0) {
						boolean temp = kalenderSystem_createActivity(calendarID, title, startDate, endDate);

						if (temp) {

							addActivityFrame.setVisible(false);
							aafOpen = false;
							cal = (GregorianCalendar) Calendar.getInstance();
							kalenderSystem_showDayView(cal.get(Calendar.YEAR), cal.get(Calendar.WEEK_OF_YEAR));

						}

					} else {
						info.setText("Vänligen välj en kalender!");
					}

				} else {

					info.setText("Starttiden måste vara före sluttiden");
				}
				break;

			case ("showActivitiesOfDay"):
				String name = button.getName();

				try {
					Date d = df.parse(name);

					GregorianCalendar weekCal = new GregorianCalendar();
					weekCal.setTime(d);

					int year = weekCal.get(Calendar.YEAR);
					int week = weekCal.get(Calendar.WEEK_OF_YEAR);

					kalenderSystem_showDayView(year, week);

				} catch (Exception e) {

					e.printStackTrace();
				}

				break;

			case ("menuSkapa Kalender"):

				kalenderSystem_addCalendarFrame();

				break;

			case ("Skapa kalender"):

				Component[] comps = ((JButton) arg.getSource()).getParent().getComponents();

				for (int i = 0; i < comps.length; i++) {

					String compName = comps[i].getName();

					if (compName != null) {

						if (compName.equals("calendarName")) {

							String calendarName = ((JTextField) comps[i]).getText();
							kalenderSystem_createCalendar(calendarName);
						}
					}

				}

				break;

			case ("infoOk"):

				infoBoard.setVisible(false);

				break;

			case ("menuVeckovy"):

				kalenderSystem_showDayView(Calendar.getInstance().get(Calendar.YEAR),
						Calendar.getInstance().get(Calendar.WEEK_OF_YEAR));
				break;

			case ("menuMånadsvy"):

				kalenderSystem_showMonthView(Calendar.getInstance().get(Calendar.YEAR),
						Calendar.getInstance().get(Calendar.MONTH));
				break;

			case ("menuBjud In"):

				kalenderSystem_inviteUserFrame(0, "calendar");
				break;

			case ("viewActivity"):

				String str_eventID = button.getName();

				int activityID = Integer.parseInt(str_eventID);

				kalenderSystem_showActivityPane(activityID);

				break;

			case ("checkCalendar"):

				String str_calendarID = button.getName();
				int calendarID = Integer.parseInt(str_calendarID);

				kalenderSystem_showCalendarInvitePane(calendarID);

				break;

			case ("inviteUsers"):

				str_eventID = button.getName();

				activityID = Integer.parseInt(str_eventID);

				kalenderSystem_inviteUserFrame(activityID, "event");

				break;

			case ("inviteUser"):

				JPanel p = (JPanel) button.getParent().getParent();
				JScrollPane pane = (JScrollPane) p.getComponent(1);
				JPanel panel = (JPanel) ((JViewport) pane.getComponent(0)).getComponent(0);

				Component[] inviteComponents = panel.getComponents();

				Vector<JCheckBox> checkBoxes = new Vector<JCheckBox>();

				for (int i = 0; i < inviteComponents.length; i++) {

					if (inviteComponents[i].getClass().getSimpleName().equals("JCheckBox")) {

						checkBoxes.add((JCheckBox) inviteComponents[i]);

					}

				}

				int selected = 0;

				for (int i = 0; i < checkBoxes.size(); i++) {

					if (checkBoxes.get(i).isSelected()) {

						selected++;

					}

				}

				String[] userIDs = new String[selected];

				int counter = 0;

				for (int i = 0; i < checkBoxes.size(); i++) {

					if (checkBoxes.get(i).isSelected()) {

						userIDs[counter] = checkBoxes.get(i).getActionCommand();
						counter++;
					}

				}

				if (button.getName().contains("event")) {

					str_eventID = button.getName();
					str_eventID = str_eventID.replaceAll("event", "");
					int eventID = Integer.parseInt(str_eventID);
					kalenderSystem_inviteUser(userIDs, "event", eventID);

				} else if (button.getName().contains("calendar")) {

					p = (JPanel) button.getParent().getParent();
					panel = (JPanel) p.getComponent(0);

					JComboBox box = (JComboBox) panel.getComponent(1);
					calendarID = calendarIDs.get(box.getSelectedIndex());

					kalenderSystem_inviteUser(userIDs, "calendar", calendarID);

				}

				break;

			default:

				System.out.println(button.getActionCommand());
				break;
			}
			break;

		case ("JComboBox"):

			JComboBox comboBox = ((JComboBox) arg.getSource());
			if (comboBox.getName().equals("monthYear")) {

				int y = (int) year.getSelectedItem();
				int m = month.getSelectedIndex();
				kalenderSystem_showMonthView(y, m);

			} else if (comboBox.getName().equals("monthMonth")) {

				int y = (int) year.getSelectedItem();
				int m = month.getSelectedIndex();
				kalenderSystem_showMonthView(y, m);

			} else if (comboBox.getName().equals("weekYear")) {

				int y = (int) year.getSelectedItem();
				JComboBox weeks = (JComboBox) breadCrumb.getComponent(1);
				int w = (int) weeks.getSelectedItem();
				kalenderSystem_showDayView(y, w);

			} else if (comboBox.getName().equals("weekWeek")) {

				int y = (int) year.getSelectedItem();
				int w = (int) comboBox.getSelectedItem();
				kalenderSystem_showDayView(y, w);

			}

			break;

		default:
			System.out.println(arg);
			break;
		}
	}

	public void keyPressed(KeyEvent arg) {

	}

	public void keyReleased(KeyEvent arg) {

		if (currentFrame.equals("Register")) {

			redopassword = false;
			redoemail = false;
			registerButton.setEnabled(false);

			Component[] comps = contentPane.getComponents();
			char[] password = new char[0];
			int passwordIndex = 0;
			char[] passwordConfirm = new char[0];
			int passwordConfirmIndex = 0;

			for (int i = 0; i < comps.length; i++) {
				String className = comps[i].getClass().getSimpleName();

				if (className.equals("JPasswordField")) {
					if (comps[i].getName().equals("Lösenord")) {

						password = ((JPasswordField) comps[i]).getPassword();
						passwordIndex = i;

					} else if (comps[i].getName().equals("Bekräfta lösenord")) {

						passwordConfirm = ((JPasswordField) comps[i]).getPassword();
						passwordConfirmIndex = i;

					}
				}
			}

			if (password.length == passwordConfirm.length && password.length > 0) {

				String str_password = "";
				String str_passwordConfirm = "";
				for (int i = 0; i < password.length; i++) {

					str_password = str_password + password[i];
					str_passwordConfirm = str_passwordConfirm + passwordConfirm[i];
				}

				contentPane.getComponent(passwordIndex).setBackground(new Color(230, 255, 230));
				contentPane.getComponent(passwordConfirmIndex).setBackground(new Color(230, 255, 230));
				redopassword = true;

			} else {

				contentPane.getComponent(passwordIndex).setBackground(new Color(255, 230, 230));
				contentPane.getComponent(passwordConfirmIndex).setBackground(new Color(255, 230, 230));
			}

			Component[] comps1 = contentPane.getComponents();
			for (int i = 0; i < comps.length; i++) {

				String className = comps1[i].getClass().getSimpleName();
				if (className.equals("JTextField")) {

					if (comps1[i].getName() != null) {

						if (comps1[i].getName().equals("E-Mail")) {

							String email1 = ((JTextField) comps1[i]).getText();
							if (email1.contains("@")) {

								if (email1.split("@").length == 2) {

									contentPane.getComponent(i).setBackground(new Color(230, 255, 230));
									redoemail = true;
								} else {

									contentPane.getComponent(i).setBackground(new Color(255, 230, 230));
								}
							} else {

								contentPane.getComponent(i).setBackground(new Color(255, 230, 230));
							}
						}
					}
				}
			}

			if (redopassword && redoemail) {

				registerButton.setEnabled(true);

			}

		} else if (currentFrame.equals("Login")) {

			if (arg.getKeyCode() == 10) {

				kalenderSystem_tryLogin();
			}

		} else if (currentFrame.equals("inviteUser")) {

			JTextField field = ((JTextField) arg.getSource());
			String searchPhrase = field.getText();

			if (searchPhrase.length() > 0) {
				JPanel p = (JPanel) field.getParent().getParent();
				JScrollPane pane = (JScrollPane) p.getComponent(1);
				JPanel panel = (JPanel) ((JViewport) pane.getComponent(0)).getComponent(0);
				kalenderSystem_searchUser(searchPhrase, panel);
			} else {

				JPanel p = (JPanel) field.getParent().getParent();
				JScrollPane pane = (JScrollPane) p.getComponent(1);
				JPanel panel = (JPanel) ((JViewport) pane.getComponent(0)).getComponent(0);

				Component[] comps = panel.getComponents();
				Vector<String> names = new Vector<String>();
				for (int i = 0; i < comps.length; i++) {

					if (comps[i].getClass().getSimpleName().equals("JCheckBox")) {

						if (!((JCheckBox) comps[i]).isSelected()) {

							panel.remove(comps[i]);

						} else {

							names.add(((JCheckBox) comps[i]).getText());

						}

					}

				}

				String SQL = "SELECT userID, username, firstName, lastName FROM users WHERE userID != ? and userID != ? ORDER BY username ASC";
				String types = "ii";
				Object[] params = { 0, userID };

				Object[][] matrix = kalenderSystem_getData("kalendersystem_getData.php", SQL, types, params);
				panel.setLayout(new GridLayout(matrix.length, 1));
				for (int i = 0; i < matrix.length; i++) {
					if (names.indexOf(matrix[i][1]) < 0) {

						JCheckBox coice = new JCheckBox();
						coice.setText((String) matrix[i][1]);

						panel.add(coice);
					}
				}

				inviteUserFrame.pack();
				inviteUserFrame.repaint();

			}

		}

	}

	public void keyTyped(KeyEvent arg) {

	}

	public void windowActivated(WindowEvent arg0) {

	}

	public void windowClosed(WindowEvent arg0) {
		String name = ((JFrame) arg0.getSource()).getName();
		if (name != null) {

			if (name.equals("main")) {

				kalenderSystem_logout();
				System.exit(0);

			} else if (name.equals("infoBoard")) {

				infoBoard.setVisible(false);

			}

		}

	}

	public void windowClosing(WindowEvent arg0) {

	}

	public void windowDeactivated(WindowEvent arg0) {

	}

	public void windowDeiconified(WindowEvent arg0) {

	}

	public void windowIconified(WindowEvent arg0) {

	}

	public void windowOpened(WindowEvent arg0) {

	}

}
