import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
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
import java.util.Date;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.jdesktop.swingx.JXDatePicker;
import org.json.JSONArray;
import org.json.JSONObject;

public class kalenderSystem_window extends JFrame implements ComponentListener, ActionListener, KeyListener, WindowListener{
	
	private JPanel breadCrumb;
	private JPanel contentPane;
	private JPanel containerFiller1;
	private JPanel containerFiller2;
	JScrollPane contentScroll;
	private JTextField usernameTextField;
	private JPasswordField passwordTextField;
	private JLabel info;
	private int startWidth = 1200;
	private JPasswordField passwordConfirm;
	private JTextField emailTextField;
	private JTextField firstNameTextField;
	private JTextField lastNameTextField;
	private boolean redopassword;
	private boolean redoemail;
	private JPanel menuPanel;
	private JFrame addActivityFrame;
	private boolean aafOpen = false;
	private Font newFont = new Font("Arial", 0, 18);
	
	GridBagConstraints c = new GridBagConstraints();
	private int x = 0;
	private int y = 0;
	private String hashkey = "";
	private int userID = -1;
	private String username = "";
	Thread thread;
	private JButton registerButton;
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private String currentFrame = "Login";
	
	public kalenderSystem_window() {
		super("Kalender");
		super.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		super.addWindowListener(this);

		super.setLocation(100, 100);
		super.setPreferredSize(new Dimension(1200, 750));

		super.setLayout(new BorderLayout());
		super.addComponentListener(this);
		
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
		String[] menuNames = {"Logga in", "Registrera dig"};
		JButton[] buttons = new JButton[menuNames.length];
		
		for(int i = 0; i<buttons.length; i++) {
			
			c.gridx = x;
			c.gridy = y;
			
			buttons[i] = new JButton(menuNames[i]);
			buttons[i].addActionListener(this);
			buttons[i].setActionCommand("menu"+menuNames[i]);
			
			menuPanel.add(buttons[i], c);
			
			y++;
			
		}
		
		JPanel temp = new JPanel();
		temp.setLayout(new BorderLayout());
		temp.add(menuPanel, BorderLayout.NORTH);
		
		JScrollPane mainMenuPanel = new JScrollPane(temp);
		mainMenuPanel.setPreferredSize(new Dimension((int)temp.getPreferredSize().getWidth()+30, (int)temp.getPreferredSize().getHeight()));
		super.add(mainMenuPanel, BorderLayout.WEST);
		
		JPanel container = new JPanel();
		container.setLayout(new BorderLayout());
		
		JPanel top = new JPanel();
		top.setLayout(new GridLayout(1,2));
		
		breadCrumb = new JPanel();
		breadCrumb.setPreferredSize(new Dimension(1000,50));
		breadCrumb.setBackground(new Color(0, 150, 0));
		breadCrumb.setLayout(new GridLayout(1, 10));
		
		JPanel breadCrumbFiller= new JPanel();
		
		contentPane= new JPanel();
		contentPane.setPreferredSize(new Dimension(345, 450));
		
		kalenderSystem_showLoginPane();
		
		containerFiller1= new JPanel();
		containerFiller1.setPreferredSize(new Dimension(300, 575));
		containerFiller2= new JPanel();
		containerFiller2.setPreferredSize(new Dimension(300, 575));
		
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
		
		
		//kalenderSystem_register("Mackemania", "Admin", "admin@cals.se", "Test", "Test");
		
		
		try {
			Date start = df.parse("2018-06-04 07:50:00");
			Date end = df.parse("2018-06-04 08:45:00");
			//kalenderSystem_addActivity(1, "", start, end);
		
		} catch (Exception e) {
		
			e.printStackTrace();
		
		}
	}
	
	
	/* Ritar ut login formul�ret p� f�nstret
	 * 
	 * Inputs:
	 * 		-
	 * Outputs:
	 * 		-
	 */
	public void kalenderSystem_showLoginPane() {
		

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
		String[] menuNames = {"Logga in", "Registrera dig", "Skapa Aktivitet"};
		JButton[] buttons = new JButton[menuNames.length];
		
		for(int i = 0; i<buttons.length; i++) {
			
			c.gridx = x;
			c.gridy = y;
			
			buttons[i] = new JButton(menuNames[i]);
			buttons[i].addActionListener(this);
			buttons[i].setActionCommand("menu"+menuNames[i]);
			
			menuPanel.add(buttons[i], c);
			
			y++;
			
		}
		
		contentPane.removeAll();
		contentPane.setLayout(new GridLayout(17,1));
		currentFrame = "Login";
		String[] loginText= {"", "", "KalenderSystem", "Logga In", "", "Anv�ndarnamn", "L�senord"};
		JLabel[] loginLabels= new JLabel[loginText.length];
		
		usernameTextField= new JTextField();
		passwordTextField= new JPasswordField();
		
		for(int i=0; i<loginText.length; i++) {
			
			loginLabels[i]= new JLabel(loginText[i], SwingConstants.CENTER);
			Font newFont = new Font("Arial", 0, 32);
			loginLabels[i].setFont(newFont);
			contentPane.add(loginLabels[i]);
			
			
			if(loginText[i].equals("Anv�ndarnamn")) {
				
				newFont = new Font("Arial", 0, 20);
				loginLabels[i].setFont(newFont);
				loginLabels[i].setHorizontalAlignment(SwingConstants.LEFT);
				usernameTextField.setFont(newFont);
				usernameTextField.addKeyListener(this);
				contentPane.add(usernameTextField);
				
			} else if(loginText[i].equals("L�senord")) {
				
				newFont = new Font("Arial", 0, 20);
				loginLabels[i].setFont(newFont);
				loginLabels[i].setHorizontalAlignment(SwingConstants.LEFT);
				passwordTextField.setFont(newFont);
				passwordTextField.addKeyListener(this);
				contentPane.add(passwordTextField);
				
			}
		}
		
		JButton loginButton= new JButton("Logga in");
		loginButton.addActionListener(this);
		loginButton.setActionCommand("Logga in");
		contentPane.add(loginButton);
		
		Font newFont = new Font("Arial", 0, 20);
		info = new JLabel("");
		info.setFont(newFont);
		contentPane.add(info);
		
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
		String[] menuNames = {"Logga in", "Registrera dig"};
		JButton[] buttons = new JButton[menuNames.length];
		
		for(int i = 0; i<buttons.length; i++) {
			
			c.gridx = x;
			c.gridy = y;
			
			buttons[i] = new JButton(menuNames[i]);
			buttons[i].addActionListener(this);
			buttons[i].setActionCommand("menu"+menuNames[i]);
			
			menuPanel.add(buttons[i], c);
			
			y++;
			
		}
		
		contentPane.removeAll();
		contentPane.setLayout(new GridLayout(19,1));
		currentFrame = "Register";
		
		String[] registerText= {"", "KalenderSystem", "Registrera dig", "", "Anv�ndarnamn", "L�senord", "Bekr�fta l�senord", "E-Mail", "F�rnamn", "Efternamn", ""};

		JLabel[] registerLabels= new JLabel[registerText.length];
		
		usernameTextField= new JTextField();
		passwordTextField= new JPasswordField();
		passwordConfirm = new JPasswordField();
		emailTextField = new JTextField();
		firstNameTextField = new JTextField();
		lastNameTextField = new JTextField();
		
		passwordTextField.addKeyListener(this);
		passwordConfirm.addKeyListener(this);
		emailTextField.addKeyListener(this);
		
		for(int i=0; i<registerText.length; i++) {
			
			Font newFont;
			if(i<=2) {
				
				newFont = new Font("Arial", 0, 32);
			} else {
				
				newFont = new Font("Arial", 0, 20);
			}
			
			registerLabels[i]= new JLabel(registerText[i], SwingConstants.CENTER);
			
			registerLabels[i].setFont(newFont);
			contentPane.add(registerLabels[i]);
			
			
			if(registerText[i].equals("Anv�ndarnamn")) {
				
				registerLabels[i].setFont(newFont);
				registerLabels[i].setHorizontalAlignment(SwingConstants.LEFT);
				usernameTextField.setFont(newFont);
				contentPane.add(usernameTextField);
				
			} else if(registerText[i].equals("L�senord")) {
				
				registerLabels[i].setFont(newFont);
				registerLabels[i].setHorizontalAlignment(SwingConstants.LEFT);
				passwordTextField.setFont(newFont);
				passwordTextField.setName(registerText[i]);
				System.out.println(passwordTextField.getName());
				contentPane.add(passwordTextField);
				
			} else if(registerText[i].equals("Bekr�fta l�senord")) {
				
				registerLabels[i].setFont(newFont);
				registerLabels[i].setHorizontalAlignment(SwingConstants.LEFT);
				passwordConfirm.setName(registerText[i]);
				passwordConfirm.setFont(newFont);
				passwordConfirm.setName(registerText[i]);
				contentPane.add(passwordConfirm);
				
			} else if(registerText[i].equals("E-Mail")) {
				
				registerLabels[i].setFont(newFont);
				registerLabels[i].setHorizontalAlignment(SwingConstants.LEFT);
				emailTextField.setFont(newFont);
				emailTextField.setName(registerText[i]);
				contentPane.add(emailTextField);
				
			} else if(registerText[i].equals("F�rnamn")) {
				
				registerLabels[i].setFont(newFont);
				registerLabels[i].setHorizontalAlignment(SwingConstants.LEFT);
				firstNameTextField.setFont(newFont);
				firstNameTextField.setName(registerText[i]);
				contentPane.add(firstNameTextField);
				
			}
			 else if(registerText[i].equals("Efternamn")) {
					
				registerLabels[i].setFont(newFont);
				registerLabels[i].setHorizontalAlignment(SwingConstants.LEFT);
				lastNameTextField.setFont(newFont);
				lastNameTextField.setName(registerText[i]);
				contentPane.add(lastNameTextField);
					
			}
		}
		
		registerButton= new JButton("Registrera dig");
		registerButton.addActionListener(this);
		registerButton.setActionCommand("Registrera dig");
		contentPane.add(registerButton);
		registerButton.setEnabled(false);
		
		Font newFont = new Font("Arial", 0, 20);
		info = new JLabel("");
		info.setFont(newFont);
		contentPane.add(info);
		
		contentPane.repaint();
		pack();
		super.repaint();
	}
	
	public void kalenderSystem_showCalendarPane() {
		
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
		String[] menuNames = {"�rsvy", "M�nadsvy", "Dagsvy", "Skapa Aktivitet", "Skapa Kalender", "Inst�llningar", "Logga ut"};
		JButton[] buttons = new JButton[menuNames.length];
		
		
		for(int i = 0; i<buttons.length; i++) {
			
			c.gridx = x;
			c.gridy = y;
			
			buttons[i] = new JButton(menuNames[i]);
			buttons[i].addActionListener(this);
			buttons[i].setActionCommand("menu"+menuNames[i]);
			
			menuPanel.add(buttons[i], c);
			y++;
			
			if(buttons[i].getActionCommand().equals("menuDagsvy")) {
				
				c.gridy = y;
				menuPanel.add(new JLabel(""), c);
				
				y++;
			} else if(buttons[i].getActionCommand().equals("menuInst�llningar")) {
				
				c.gridy = y;
				menuPanel.add(new JLabel(""), c);
				y++;
				
				c.gridy = y;
				menuPanel.add(new JLabel(username+"", SwingConstants.CENTER), c);
				y++;
				
			}
			
		}
		
		contentPane.removeAll();
		contentPane.setLayout(new GridLayout(1,1));
		
		Font newFont = new Font("Arial", 0, 20);
		
		contentPane.repaint();
		pack();
		super.repaint();
	}
	
	public void kalenderSystem_addActivityPane() {
		
		if(aafOpen) {
			addActivityFrame.setVisible(false);
		}
		addActivityFrame = new JFrame();
		addActivityFrame.setLocation(-1790, 200);
		addActivityFrame.setPreferredSize(new Dimension(500, 500));
		addActivityFrame.setTitle("Skapa ny aktivitet");
		addActivityFrame.setLayout(new BorderLayout());
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(16, 1));
		
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
		
		Component[] com =  p1.getComponents();
		((JButton)com[1]).setIcon(null);
		((JButton)com[1]).setText("V�lj ett datum");
		
		
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
		
		com =  p2.getComponents();
		((JButton)com[1]).setIcon(null);
		((JButton)com[1]).setText("V�lj ett datum");
		

		String[] labelText = {"V�lj startdatum", "V�lj slutdatum"};
		JLabel[] labels = new JLabel[labelText.length];
		
		for(int i = 0; i<labels.length; i++) {
			
			labels[i] = new JLabel(labelText[i], SwingConstants.CENTER);
			labels[i].setFont(newFont);
			panel.add(labels[i]);
			
			if(i==0) {
				panel.add(p1);
			} else {
				panel.add(p2);
			}
		}
		
		
		JPanel fPane = new JPanel();
		JPanel fPane2 = new JPanel();
		
		addActivityFrame.add(panel, BorderLayout.CENTER);
		addActivityFrame.add(fPane, BorderLayout.WEST);
		addActivityFrame.add(fPane2, BorderLayout.EAST);
		addActivityFrame.pack();
		addActivityFrame.setVisible(true);
		aafOpen = true;
		
  }
	
	
	
	public boolean kalenderSystem_deleteActivity(int eventID) {
		
		String SQL = "UPDATE event SET calendarID = ? WHERE eventID=?";
		String types = "ii";
		Object[] params = {0, eventID};
		
		kalenderSystem_sendData("kalenderSystem_sendData.php", SQL, types, params);
		
		
		return false;
		
	}
	
	
	/* Anv�nds f�r att h�mta alla aktiviteter i en kalender
	 * Inputs:
	 * 		-
	 * Outputs.
	 * 		Object[][] matrix, Inneh�ller all information om eventen i en kalender.
	 */
	public Object[][] kalenderSystem_getActivities() {


		String SQL = "SELECT eventID FROM acceptedevents WHERE userID=?";
		String types = "i";
		Object[] params = {userID};
		
		Object[][] matrix = kalenderSystem_getData("kalenderSystem_getData.php", SQL, types, params);
		
		Vector<Integer> eventIDs = new Vector<Integer>();
		
		for(int i= 0; i<matrix.length; i++) {
			
			eventIDs.add((int)matrix[i][0]);
			
		}
		
		SQL = "SELECT eventID FROM events WHERE creatorID=?";
		types = "i";
		params[0] = userID;
		matrix = kalenderSystem_getData("kalenderSystem_getData.php", SQL, types, params);
		
		for(int i= 0; i<matrix.length; i++) {
			
			eventIDs.add((int)matrix[i][0]);
			
		}
		
		for(int i = 0; i<eventIDs.size(); i++) {
			
			int eventID = eventIDs.get(i);
			
			SQL = "SELECT name, startTime, endTime, creatorID FROM events WHERE eventID=?";
			types = "i";
			params = new Object[1];
			params[0] = eventID;
			
			Object[][]temp = kalenderSystem_getData("kalenderSystem_getData.php", SQL, types, params);
			matrix[i] = temp[0];
			
		}
		
		return matrix;
		
	}
	
	
	/* Anv�nds f�r att l�gga till en aktivitet i en kalender
	 * Inputs:
	 * 		-int calendarID, id:t p� den kalendern man vill l�gga till aktiviteten i
	 * 		-String eventName, Namnet p� den aktivitet man vill l�gga till
	 * 		-Date startTime, startdatumet och tiden p� det event man l�gger till
	 * 		-Date endTime, slutdatumet och tiden p� det event man l�gger till.
	 * 
	 * Outputs.
	 * 		boolean, returnerar true om aktiviteten lades till, annars returnerar den false
	 */
	public boolean kalenderSystem_addActivity(int calendarID, String eventName, Date startTime, Date endTime) {
		
		if(startTime.compareTo(endTime)<0) {
		
			String SQL = "SELECT eventID FROM acceptedevents WHERE userID=?";
			String types = "i";
			Object[] params = {userID};
			
			Object[][] matrix = kalenderSystem_getData("kalenderSystem_getData.php", SQL, types, params);
			
			Vector<Integer> eventIDs = new Vector<Integer>();
			
			for(int i= 0; i<matrix.length; i++) {
				
				eventIDs.add((int)matrix[i][0]);
				
			}
			
			SQL = "SELECT eventID FROM events WHERE creatorID=?";
			types = "i";
			params[0] = userID;
			matrix = kalenderSystem_getData("kalenderSystem_getData.php", SQL, types, params);
			
			for(int i= 0; i<matrix.length; i++) {
				
				eventIDs.add((int)matrix[i][0]);
				
			}
			
			
			matrix = new Object[eventIDs.size()][];
			
			
			/*
			 * Today ska �ndras till startTime
			 * 
			 */
			
			//System.out.println(str_date);
			
			
			for(int i = 0; i<eventIDs.size(); i++) {
				
				int eventID = eventIDs.get(i);
				
				SQL = "SELECT name, startTime, endTime, creatorID FROM events WHERE eventID=?";
				types = "i";
				params = new Object[1];
				params[0] = eventID;
				
				Object[][]temp = kalenderSystem_getData("kalenderSystem_getData.php", SQL, types, params);
				matrix[i] = temp[0];
				
				try {
					
					matrix[i][1] = df.parse((String) matrix[i][1]);
					matrix[i][2] = df.parse((String) matrix[i][2]);
				
				} catch(Exception e) {
					
					e.printStackTrace();
					return false;
				}
				
				/*
				System.out.println(df.format(startTime));
				System.out.println(df.format(endTime));
				System.out.println("**************************************");
				System.out.println(df.format((Date)matrix[i][1]));
				System.out.println(df.format((Date)matrix[i][2]));
				
				*/
				//System.out.println(startTime.compareTo((Date)matrix[i][1]));
				//System.out.println(endTime.compareTo((Date)matrix[i][2]));
				
				if(startTime.compareTo((Date)matrix[i][1])>=0 && startTime.compareTo((Date)matrix[i][2])<0) {
					
					JOptionPane.showMessageDialog(null, "Du f�rs�ker dubbelboka dig! \nD�r ligger redan "+matrix[i][0]+" som b�rjar "+df.format((Date)matrix[i][1])+" och slutar "+df.format((Date)matrix[i][2]));
					return false;
					
				} else if (endTime.compareTo((Date)matrix[i][1])>0 && endTime.compareTo((Date) matrix[i][2])<=0) {
					
					JOptionPane.showMessageDialog(null, "Du f�rs�ker dubbelboka dig! \nD�r ligger redan "+matrix[i][0]+" som b�rjar "+df.format((Date)matrix[i][1])+" och slutar "+df.format((Date)matrix[i][2]));
					return false;
					
				} else if (startTime.compareTo((Date)matrix[i][1]) <0 && endTime.compareTo((Date)matrix[i][2])>0) {
					
					JOptionPane.showMessageDialog(null, "Du f�rs�ker dubbelboka dig! \nD�r ligger redan "+matrix[i][0]+" som b�rjar "+df.format((Date)matrix[i][1])+" och slutar "+df.format((Date)matrix[i][2]));
					return false;
				
				} else if (startTime.compareTo((Date)matrix[i][1]) == 0 && endTime.compareTo((Date)matrix[i][2]) == 0) {
					
					JOptionPane.showMessageDialog(null, "Du f�rs�ker dubbelboka dig! \nD�r ligger redan "+matrix[i][0]+" som b�rjar "+df.format((Date)matrix[i][1])+" och slutar "+df.format((Date)matrix[i][2]));
					return false;
				
				} else {
				
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
			
		
		}
		return false;
	}
	
	
	/* Skapar en kalender med namnet name
	 * Inputs:
	 * 		-String name, Nament p� kalendern som ska skapas
	 * Outputs:
	 * 		-
	 */
	public void kalenderSystem_createCalendar(String name) {
		
		String SQL = "SELECT calendarID FROM calendars WHERE calendarID>=? ORDER BY calendarID DESC";
		String types = "i";
		Object[] params = {0};
		
		Object[][] matrix = kalenderSystem_getData("kalenderSystem_getData.php", SQL, types, params);
		
		int calID = (int) matrix[0][0];
		calID++;
		
		SQL = "INSERT INTO calendars(calendarID, name, creatorID) VALUES(?, ?, ?)";
		types = "isi";
		params = new Object[3];
		
		params[0] = calID;
		params[1] = name;
		params[2] = userID;
		kalenderSystem_sendData("kalenderSystem_sendData.php", SQL, types, params);
		
	}
	
	
	/* Anv�nds f�r att logga in en ny anv�ndare
	 * Inputs:
	 * 		-String username, det anv�ndarnamn man f�rs�ker logga in med
	 * 		-String password, det l�senord man f�rs�ker logga in med
	 * 
	 * Outputs:
	 * 		-boolean, f�r true om inloggningen lyckats annars returnar den false 
	 */
	public boolean kalenderSystem_login(String username, String password) {
		
		String str_url = "http://localhost/kalenderSystem_server/login.php";
		String query = "?username="+username+"&password="+password;
		
		str_url = str_url+query;
		
		
		try {
			//System.out.println(str_url);
			URL url = new URL(str_url);
			URLConnection conn = url.openConnection();
			InputStream is = conn.getInputStream();
			
			String retval = "";
			
			//H�mtar allt som st�r p� "webbsidan"
			while(is.available()>0) {
				
				retval = retval+(char)is.read();
			
			}
			is.close();
			
			if(!retval.equals("0")) {
				//System.out.println(retval);
				try {
					JSONObject json = new JSONObject(retval);
					userID = json.getInt("0");
					
					hashkey = json.getString("1");
					
					//System.out.println(hashkey);
					
					String SQL = "SELECT hashID FROM hash WHERE hash = ? AND userID = ?";
					String types = "si";
					Object[] params = {hashkey, userID};
					
					Object[][] matrix = kalenderSystem_getData("kalenderSystem_getData.php", SQL, types, params);
					
					if(matrix.length==1) {
						
						SQL = "SELECT calendarID FROM calendars WHERE calendarID=?";
						types = "i";
						params = new Object[1];
						params[0] = 0;
						
						matrix = kalenderSystem_getData("kalenderSystem_getData.php", SQL, types, params);
						
						if(matrix.length<1) {
							
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
				
				} catch(Exception e) {
				
					System.out.println("Login:"+retval);
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
		
		for(int i=0; i<char_password.length; i++) {
		
			password = password+char_password[i];
		}
		//System.out.println(username+" "+password);
		
		if(kalenderSystem_login(username, password)) {
			
			contentPane.removeAll();
			info.setText("");
			this.username = username;
			kalenderSystem_showCalendarPane();
			
		} else {
			this.username = "";
			info.setText("Fel anv�ndarnamn/l�senord");
			
		}
	}
	
	public void kalenderSystem_logout() {
		
		String SQL = "DELETE FROM hash WHERE userID=?";
		String types = "i";
		Object[] values = {userID};
		
		kalenderSystem_sendData("kalenderSystem_sendData.php", SQL, types, values);
		
		kalenderSystem_showLoginPane();
		
		
	}
	
	/* Anv�nds f�r att registrera en ny anv�ndare
	 * Inputs:
	 * 		-String username, det anv�ndarnamn man vill ha
	 * 		-String password, det l�senord man vill ha
	 * 		-String email, den email man vill registrera sig med
	 * 		-String firstName, f�rnamnet p� personen som ska registreras
	 * 		-String lastName, efternamnet p� personen som ska registreras
	 * 
	 * Outputs:
	 * 		-boolean, f�r true om registreringen lyckats annars returnar den false 
	 */
	public boolean kalenderSystem_register(String username, String password, String email, String firstName, String lastName) {
		
		String SQL = "INSERT INTO users(username, password, email, firstName, lastName) VALUES(?, ?, ?, ?, ?)";
		String types = "sssss";
		Object[] params = {username, password, email, firstName, lastName};
		kalenderSystem_sendData("kalenderSystem_register.php", SQL, types, params);
		
		String str_url = "http://localhost/kalenderSystem_server/kalenderSystem_register.php";
		String query = "?username="+username+"&password="+password+"&email="+email+"&firstName="+firstName+"&lastName="+lastName;
		
		str_url = str_url+query;
		
		try {
			
			//System.out.println(str_url);
			URL url = new URL(str_url);
			URLConnection conn = url.openConnection();
			InputStream is = conn.getInputStream();
			
			String retval = "";
			
			//H�mtar allt som st�r p� "webbsidan"
			while(is.available()>0) {
				
				retval = retval+(char)is.read();
			
			}
			is.close();
			
			//System.out.println(retval);
			
			if(retval.equals("1")) {
				
				return true;
			
			} else {
				
				System.out.println("Register: "+retval);
				return false;
			
			}
			
		
		} catch (Exception e) {
			
			e.printStackTrace();
		
		}
		return false;
	}
	
	
	/* Anv�nds f�r att skicka data till filen 'path' p� localhost.
	 * 
	 * Inputs:
	 * 		-String path, filnamet p� den fil man vill komma �t fr�n servern
	 * 		-String SQL, SQL-uttrycket som man vill exekvera
	 * 		-String types, En textstr�ng d�r typerna av p�f�ljande variabler �r d�r s = string, i = int, d = double och b = blob
	 * 
	 * Outputs:
	 * 		-boolean, f�r true om fr�gan lyckats.
	 * 
	 */
	public boolean kalenderSystem_sendData(String path, String SQL, String types, Object[] params) {
		
		//Skapar en url som leder till valfri fil p� localhost
		String str_url = "http://localhost/kalenderSystem_server/"+path;
		String query = "?";
		
		//Skapar en fr�ga som servern kommer ta emot.
		if(types.equals("Â¤")) {
			
			query = query+"SQL="+SQL+"&types="+types;
			
		} else {
			

			//Formaterar om fr�gan till ett JSON object
			JSONArray sendParams = new JSONArray(params);
			//System.out.println(sendParams.toString());
			
			String[] index = new String[params.length];
			
			for(int i = 0; i<index.length; i++) {
				
				index[i] = ""+i;
				//System.out.println(index[i]);
			}
			JSONArray JSONIndex = new JSONArray(index);
			//System.out.println(JSONIndex.toString());
			String values = sendParams.toJSONObject(JSONIndex).toString();
			query = query+"SQL="+SQL+"&types="+types+"&values="+values;
		}
		
		query = query+"&userID="+userID+"&hashkey="+hashkey;
		str_url = str_url+query;
		str_url = str_url.replace(" ", "%20");
		
		try {
			
			//Connectar till url:en
			//System.out.println(str_url);
			URL url = new URL(str_url);
			URLConnection conn = url.openConnection();
			InputStream is = conn.getInputStream();
			
			String retval = "";
			
			//H�mtar allt som st�r p� "webbsidan"
			while(is.available()>0) {
				
				retval = retval+(char)is.read();
			
			}
			is.close();
			
			if(retval.equals("1")) {
				
				return true;
			
			} else {
				System.out.println("SendData: "+retval);
				return false;
			
			}
			//System.out.println(retval);
		
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	/* Anv�nds f�r att skicka data till filen 'path' p� localhost.
	 * 
	 * Inputs:
	 * 		-String path, filnamet p� den fil man vill komma �t fr�n servern
	 * 		-String SQL, SQL-uttrycket som man vill exekvera
	 * 		-String types, En textstr�ng d�r typerna av p�f�ljande variabler �r d�r s = string, i = int, d = double och b = blob
	 * 		
	 * Outputs:
	 * 		-Object[][] matrix som inneh�ller all den data man f�r fr�n SQL fr�gan.
	 */
	public Object[][] kalenderSystem_getData(String path, String SQL, String types, Object[] params) {
		
		//Skapar en url som leder till valfri fil p� localhost
		String str_url = "http://localhost/kalenderSystem_server/"+path;
		String query = "?";
		
		//Skapar en fr�ga som servern kommer ta emot.
		
		//Formaterar om fr�gan till ett JSON object
		JSONArray sendParams = new JSONArray(params);
		//System.out.println(sendParams.toString());
		
		String[] index = new String[params.length];
		
		for(int i = 0; i<index.length; i++) {
			
			index[i] = ""+i;
			//System.out.println(index[i]);
		}
		JSONArray JSONIndex = new JSONArray(index);
		//System.out.println(JSONIndex.toString());
		String values = sendParams.toJSONObject(JSONIndex).toString();
		query = query+"SQL="+SQL+"&types="+types+"&values="+values;
	
	
		query = query+"&userID="+userID+"&hashkey="+hashkey;
		str_url = str_url+query;
		str_url = str_url.replace(" ", "%20");
		
		try {
			//Connectar till servern
			//System.out.println(str_url);
			URL url = new URL(str_url);
			URLConnection conn = url.openConnection();
			InputStream is = conn.getInputStream();
			
			String retval = "";
			
			//h�mntar allt som st�r p� "webbsidan"
			while(is.available()>0) {
				
				retval = retval+(char)is.read();
			
			}
			is.close();
			
			
			//System.out.println(retval);
				try {
				JSONObject jsons = new JSONObject(retval);
				//System.out.println(jsons.toString());
				
				//G�r om JSONObjektet till en Object-matris
				int iMax = 0;
				int jMax = 0;
				//System.out.println(jsons.get("0"));
				for(int i = 0; !jsons.isNull(""+i); i++) {
					for(int j = 0; !((JSONObject)jsons.get(""+i)).isNull(""+j); j++) { 
						
						
						//System.out.println(i+" "+j);
						if(iMax<i) {
							
							iMax = i;
							
						}
						
						if(jMax<j) {
							
							jMax = j;
							
						}
					}
				}
				
				iMax++;
				jMax++;
				Object[][] matrix = new Object[iMax][jMax];
				//System.out.println(iMax);
				//System.out.println(jMax);
				for(int i = 0; i<matrix.length; i++) {
					for(int j = 0; j<matrix[i].length; j++) { 
						
						matrix[i][j] = ((JSONObject)jsons.get(""+i)).get(""+j);
						
					}
				}
				//System.out.println(matrix[0][0]);
				
				if(matrix.length == 1) {
					
					if(matrix[0].length == 1) {
						
						if(matrix[0][0].equals("null")) {
							
							matrix = new Object[0][0];
							
						}
					}
				}
				return matrix;
				
			} catch (Exception e) {
				System.out.println("getData:"+retval);
				e.printStackTrace();
			}
		
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	
	public void kalenderSystem_connectToServer() {
		
		try {
			//Connectar till servern
			//System.out.println(str_url);
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
		

		//System.out.println(arg);
		
		String argName = arg.getSource().getClass().getSimpleName();
		
		double difference = startWidth - ((JFrame)arg.getSource()).getSize().getWidth();
		//System.out.println(difference);
		
		int width = (int) (300-(difference/2));
		if(width<=0) {
			
			width = 0;
		}
		
		//System.out.println("w"+width);
		containerFiller1.setPreferredSize(new Dimension(width, 600));
		containerFiller2.setPreferredSize(new Dimension(width, 600));
		contentPane.setPreferredSize(new Dimension(400, 600));
		
		super.repaint();
		
		
	}

	public void componentShown(ComponentEvent arg) {
		
	
	}

	
	public void actionPerformed(ActionEvent arg) {
		
		String component= arg.getSource().getClass().getSimpleName();
		
		switch(component) {
			case("JButton"):
			
				JButton button = (JButton)arg.getSource();
				
				switch(button.getActionCommand()) {
					case("Logga in"):
						
						kalenderSystem_tryLogin();
						
						break;
						
					case("menuRegistrera dig"):
						kalenderSystem_showRegisterPane();
						break;
						
					case("Registrera dig"):
						
						String anvandarnamn= usernameTextField.getText();
						String losenord= passwordTextField.getText();
						String email= emailTextField.getText();
						String fnamn= firstNameTextField.getText();
						String enamn= lastNameTextField.getText();
						
						kalenderSystem_register(anvandarnamn, losenord, email, fnamn, enamn);
						break;
						
					case("menuLogga in"):
						
						kalenderSystem_showLoginPane();
						break;
					
					case("menuLogga ut"):
						
						kalenderSystem_logout();
						break;
						
					case("menuSkapa Aktivitet"):
						
						kalenderSystem_addActivityPane();
						break;
					default:
						
						break;
				}
			break;
			
		default:
			
			break;
		}
	}

	
	public void keyPressed(KeyEvent arg) {
		
		
	}
	
	public void keyReleased(KeyEvent arg) {
		
		if(currentFrame.equals("Register")) {
			
			redopassword = false;
			redoemail = false;
			registerButton.setEnabled(false);
			
			Component[] comps = contentPane.getComponents();
			char[]password = new char[0];
			int passwordIndex = 0;
			char[]passwordConfirm = new char[0];
			int passwordConfirmIndex = 0;
			
			for(int i=0; i<comps.length; i++) {
				String className = comps[i].getClass().getSimpleName();
				
				if(className.equals("JPasswordField")) {
					if(comps[i].getName().equals("L�senord")) {
						
						password = ((JPasswordField)comps[i]).getPassword();
						passwordIndex = i;
						
					} else if(comps[i].getName().equals("Bekr�fta l�senord")) {
						
						passwordConfirm = ((JPasswordField)comps[i]).getPassword();
						passwordConfirmIndex = i;
	
					}
				}
			}
				
			if(password.length == passwordConfirm.length && password.length>0) {
				
				String str_password = "";
				String str_passwordConfirm = "";
				for(int i=0; i<password.length; i++) {
					
					str_password = str_password+password[i];
					str_passwordConfirm = str_passwordConfirm+passwordConfirm[i];
				}
				
				contentPane.getComponent(passwordIndex).setBackground(new Color(230, 255, 230));
				contentPane.getComponent(passwordConfirmIndex).setBackground(new Color(230, 255, 230));
				redopassword = true;

			} else {
				
				contentPane.getComponent(passwordIndex).setBackground(new Color(255, 230, 230));
				contentPane.getComponent(passwordConfirmIndex).setBackground(new Color(255, 230, 230));
			}
			
			Component[] comps1 = contentPane.getComponents();
			for(int i=0; i<comps.length; i++) {
				
				String className = comps1[i].getClass().getSimpleName();
				if(className.equals("JTextField")) {
					
					if(comps1[i].getName() != null) {
						
						if(comps1[i].getName().equals("E-Mail")) {
							
							String email1 = ((JTextField)comps1[i]).getText();
							if(email1.contains("@")) {
								
								if(email1.split("@").length == 2) {
								
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
			
			if(redopassword && redoemail) {
					
				registerButton.setEnabled(true);
	
			}
		
		} else if (currentFrame.equals("Login")) {
			
			if(arg.getKeyCode() == 10) {
				
				kalenderSystem_tryLogin();
			}
		}
	}
	
	public void keyTyped(KeyEvent arg) {
		
		
	}


	
	
	public void windowActivated(WindowEvent arg0) {
		
		
	}


	
	public void windowClosed(WindowEvent arg0) {
		
		kalenderSystem_logout();
		System.exit(0);
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
