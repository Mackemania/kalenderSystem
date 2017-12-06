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
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.json.JSONArray;
import org.json.JSONObject;

public class kalenderSystem_window extends JFrame implements ComponentListener, ActionListener, KeyListener{
	
	private JPanel breadCrumb;
	private JPanel contentPane;
	private JPanel containerFiller1;
	private JPanel containerFiller2;
	JScrollPane contentScroll;
	private JTextField usernameTextField;
	private JPasswordField passwordTextField;
	private JLabel info;
	private int startWidth = 1200;
	
	GridBagConstraints c = new GridBagConstraints();
	private int x = 0;
	private int y = 0;
	private String hashkey = "";
	private int userID = -1;
	Thread thread;
	private JButton registerButton;
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public kalenderSystem_window() {
		super("Kalender");
		super.setDefaultCloseOperation(EXIT_ON_CLOSE);

		super.setLocation(2080, 100);
		super.setPreferredSize(new Dimension(1200, 750));

		super.setLayout(new BorderLayout());
		super.addComponentListener(this);
		/*
		try {
		
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		} catch (Exception e) {
		
			e.printStackTrace();
		
		}
		*/
		
		kalenderSystem_connectToServer();
		
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0;
		c.weighty = 1;
		c.gridx = x;
		c.gridy = y;
		c.gridwidth = 1;
		c.insets = new Insets(15, 0, 0, 0);
		c.anchor = GridBagConstraints.NORTHWEST;
		
		
		JPanel menuPanel = new JPanel();
		menuPanel.setLayout(new GridBagLayout());
		
		//c.insets = new Insets(15, 15, 0, 15);
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
		
		//startWidth = (int) contentPane.getSize().getWidth();
		
		kalenderSystem_register("Mackemania", "Admin", "admin@cals.se", "Test", "Test");
		kalenderSystem_login("Mackemania", "Admin");
		//kalenderSystem_createCalendar("calendar");
		
		try {
			Date start = df.parse("2018-06-04 07:50:00");
			Date end = df.parse("2018-06-04 08:45:00");
			//kalenderSystem_addActivity(1, "", start, end);
		
		} catch (Exception e) {
		
			e.printStackTrace();
		
		}
	}
	
	
	/* Ritar ut login formuläret på fönstret
	 * 
	 * Inputs:
	 * 		-
	 * Outputs:
	 * 		-
	 */
	public void kalenderSystem_showLoginPane() {
		
		
		contentPane.setLayout(new GridLayout(15,1));
		
		String[] loginText= {"", "", "KalenderSystem", "Logga In", "", "Användarnamn", "Lösenord"};
		JLabel[] loginLabels= new JLabel[loginText.length];
		
		usernameTextField= new JTextField();
		passwordTextField= new JPasswordField();
		
		for(int i=0; i<loginText.length; i++) {
			
			loginLabels[i]= new JLabel(loginText[i], SwingConstants.CENTER);
			Font newFont = new Font("Arial", 0, 32);
			loginLabels[i].setFont(newFont);
			contentPane.add(loginLabels[i]);
			
			
			if(loginText[i].equals("Användarnamn")) {
				
				newFont = new Font("Arial", 0, 20);
				loginLabels[i].setFont(newFont);
				loginLabels[i].setHorizontalAlignment(SwingConstants.LEFT);
				usernameTextField.setFont(newFont);
				contentPane.add(usernameTextField);
				
			} else if(loginText[i].equals("Lösenord")) {
				
				newFont = new Font("Arial", 0, 20);
				loginLabels[i].setFont(newFont);
				loginLabels[i].setHorizontalAlignment(SwingConstants.LEFT);
				passwordTextField.setFont(newFont);
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
		super.repaint();
		
	}
	
	public void kalenderSystem_showRegisterPane() {
		
		contentPane.setLayout(new GridLayout(19,1));

		String[] registerText= {"", "KalenderSystem", "Registrera dig", "", "Användarnamn", "Lösenord", "Bekräfta lösenord", "E-Mail", "Förnamn", "Efternamn", ""};

		JLabel[] registerLabels= new JLabel[registerText.length];
		
		usernameTextField= new JTextField();
		passwordTextField= new JPasswordField();
		
		JPasswordField passwordConfirm = new JPasswordField();
		JTextField emailTextField = new JTextField();
		JTextField firstNameTextField = new JTextField();
		JTextField lastNameTextField = new JTextField();
		
		passwordTextField.addKeyListener(this);
		passwordConfirm.addKeyListener(this);
		
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
			
			
			if(registerText[i].equals("Användarnamn")) {
				
				registerLabels[i].setFont(newFont);
				registerLabels[i].setHorizontalAlignment(SwingConstants.LEFT);
				usernameTextField.setFont(newFont);
				contentPane.add(usernameTextField);
				
			} else if(registerText[i].equals("Lösenord")) {
				
				registerLabels[i].setFont(newFont);
				registerLabels[i].setHorizontalAlignment(SwingConstants.LEFT);
				passwordTextField.setFont(newFont);
				passwordTextField.setName(registerText[i]);
				System.out.println(passwordTextField.getName());
				contentPane.add(passwordTextField);
				
			} else if(registerText[i].equals("Bekräfta lösenord")) {
				
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
				
			} else if(registerText[i].equals("Förnamn")) {
				
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
	
	public boolean kalenderSystem_deleteActivity(int eventID) {
		
		String SQL = "UPDATE event SET calendarID = ? WHERE eventID=?";
		String types = "ii";
		Object[] params = {0, eventID};
		
		kalenderSystem_sendData("kalenderSystem_sendData.php", SQL, types, params);
		
		
		return false;
		
	}
	
	
	/* Används för att hämta alla aktiviteter i en kalender
	 * Inputs:
	 * 		-
	 * Outputs.
	 * 		Object[][] matrix, Innehåller all information om eventen i en kalender.
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
	
	
	/* Används för att lägga till en aktivitet i en kalender
	 * Inputs:
	 * 		-int calendarID, id:t på den kalendern man vill lägga till aktiviteten i
	 * 		-String eventName, Namnet på den aktivitet man vill lägga till
	 * 		-Date startTime, startdatumet och tiden på det event man lägger till
	 * 		-Date endTime, slutdatumet och tiden på det event man lägger till.
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
			 * Today ska ändras till startTime
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
					
					JOptionPane.showMessageDialog(null, "Du försöker dubbelboka dig! \nDär ligger redan "+matrix[i][0]+" som börjar "+df.format((Date)matrix[i][1])+" och slutar "+df.format((Date)matrix[i][2]));
					return false;
					
				} else if (endTime.compareTo((Date)matrix[i][1])>0 && endTime.compareTo((Date) matrix[i][2])<=0) {
					
					JOptionPane.showMessageDialog(null, "Du försöker dubbelboka dig! \nDär ligger redan "+matrix[i][0]+" som börjar "+df.format((Date)matrix[i][1])+" och slutar "+df.format((Date)matrix[i][2]));
					return false;
					
				} else if (startTime.compareTo((Date)matrix[i][1]) <0 && endTime.compareTo((Date)matrix[i][2])>0) {
					
					JOptionPane.showMessageDialog(null, "Du försöker dubbelboka dig! \nDär ligger redan "+matrix[i][0]+" som börjar "+df.format((Date)matrix[i][1])+" och slutar "+df.format((Date)matrix[i][2]));
					return false;
				
				} else if (startTime.compareTo((Date)matrix[i][1]) == 0 && endTime.compareTo((Date)matrix[i][2]) == 0) {
					
					JOptionPane.showMessageDialog(null, "Du försöker dubbelboka dig! \nDär ligger redan "+matrix[i][0]+" som börjar "+df.format((Date)matrix[i][1])+" och slutar "+df.format((Date)matrix[i][2]));
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
	
	/* Används för att logga in en ny användare
	 * Inputs:
	 * 		-String username, det användarnamn man försöker logga in med
	 * 		-String password, det lösenord man försöker logga in med
	 * 
	 * Outputs:
	 * 		-boolean, får true om inloggningen lyckats annars returnar den false 
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
			
			//Hämtar allt som står på "webbsidan"
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
	
	
	/* Används för att registrera en ny användare
	 * Inputs:
	 * 		-String username, det användarnamn man vill ha
	 * 		-String password, det lösenord man vill ha
	 * 		-String email, den email man vill registrera sig med
	 * 		-String firstName, förnamnet på personen som ska registreras
	 * 		-String lastName, efternamnet på personen som ska registreras
	 * 
	 * Outputs:
	 * 		-boolean, får true om registreringen lyckats annars returnar den false 
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
			
			//Hämtar allt som står på "webbsidan"
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
	
	
	/* Används för att skicka data till filen 'path' på localhost.
	 * 
	 * Inputs:
	 * 		-String path, filnamet på den fil man vill komma åt från servern
	 * 		-String SQL, SQL-uttrycket som man vill exekvera
	 * 		-String types, En textsträng där typerna av påföljande variabler är där s = string, i = int, d = double och b = blob
	 * 
	 * Outputs:
	 * 		-boolean, får true om frågan lyckats.
	 * 
	 */
	public boolean kalenderSystem_sendData(String path, String SQL, String types, Object[] params) {
		
		//Skapar en url som leder till valfri fil på localhost
		String str_url = "http://localhost/kalenderSystem_server/"+path;
		String query = "?";
		
		//Skapar en fråga som servern kommer ta emot.
		if(types.equals("¤")) {
			
			query = query+"SQL="+SQL+"&types="+types;
			
		} else {
			

			//Formaterar om frågan till ett JSON object
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
			
			//Hämtar allt som står på "webbsidan"
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
	
	
	/* Används för att skicka data till filen 'path' på localhost.
	 * 
	 * Inputs:
	 * 		-String path, filnamet på den fil man vill komma åt från servern
	 * 		-String SQL, SQL-uttrycket som man vill exekvera
	 * 		-String types, En textsträng där typerna av påföljande variabler är där s = string, i = int, d = double och b = blob
	 * 		
	 * Outputs:
	 * 		-Object[][] matrix som innehåller all den data man får från SQL frågan.
	 */
	public Object[][] kalenderSystem_getData(String path, String SQL, String types, Object[] params) {
		
		//Skapar en url som leder till valfri fil på localhost
		String str_url = "http://localhost/kalenderSystem_server/"+path;
		String query = "?";
		
		//Skapar en fråga som servern kommer ta emot.
		if(types.equals("¤")) {
			
			query = query+"SQL="+SQL+"&types="+types;
			
		} else {
			
			//Formaterar om frågan till ett JSON object
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
			//Connectar till servern
			//System.out.println(str_url);
			URL url = new URL(str_url);
			URLConnection conn = url.openConnection();
			InputStream is = conn.getInputStream();
			
			String retval = "";
			
			//hämntar allt som står på "webbsidan"
			while(is.available()>0) {
				
				retval = retval+(char)is.read();
			
			}
			is.close();
			
			
			//System.out.println(retval);
				try {
				JSONObject jsons = new JSONObject(retval);
				//System.out.println(jsons.toString());
				
				//Gör om JSONObjektet till en Object-matris
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
						String username = usernameTextField.getText();
						char[] char_password = passwordTextField.getPassword();
						String password = "";
						
						for(int i=0; i<char_password.length; i++) {
						
							password = password+char_password[i];
						}
						//System.out.println(username+" "+password);
						
						if(kalenderSystem_login(username, password)) {
							
							info.setText("");
							
							
						} else {
							
							info.setText("Du loggades inte in");
							
						}
						
						break;
						
					case("menuRegistrera dig"):
						contentPane.removeAll();
						kalenderSystem_showRegisterPane();
						break;
						
					case("Registrera dig"):
						Component[] comps = contentPane.getComponents();
						String anvandarnamn= comps[4].getClass().getSimpleName();
						System.out.println(anvandarnamn);
						
						//kalenderSystem_register(comps[4], comps[5], comps[7], comps[8], comps[9]);
						break;
						
					case("menuLogga in"):
						
						contentPane.removeAll();
						kalenderSystem_showLoginPane();
						break;
					
					default:
						
						break;
				}
			break;
			
		default:
			
			break;
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		
		Component[] comps = contentPane.getComponents();
		char[]password = new char[0];
		int passwordIndex = 0;
		char[]passwordConfirm = new char[0];
		int passwordConfirmIndex = 0;
		for(int i=0; i<comps.length; i++) {
			String className = comps[i].getClass().getSimpleName();
			
			if(className.equals("JPasswordField")) {
				if(comps[i].getName().equals("Lösenord")) {
					
					password = ((JPasswordField)comps[i]).getPassword();
					passwordIndex = i;
					
				} else if(comps[i].getName().equals("Bekräfta lösenord")) {
					
					passwordConfirm = ((JPasswordField)comps[i]).getPassword();
					passwordConfirmIndex = i;
				}
			}
		}
		
		if(password.length == passwordConfirm.length) {
			
			String str_password = "";
			String str_passwordConfirm = "";
			for(int i=0; i<password.length; i++) {
				
				str_password = str_password+password[i];
				str_passwordConfirm = str_passwordConfirm+passwordConfirm[i];
			}
			
			if(str_password.equals(str_passwordConfirm)) {
				
				contentPane.getComponent(passwordIndex).setBackground(new Color(230, 255, 230));
				contentPane.getComponent(passwordConfirmIndex).setBackground(new Color(230, 255, 230));
				registerButton.setEnabled(true);
			} else {
				
				contentPane.getComponent(passwordIndex).setBackground(new Color(255, 230, 230));
				contentPane.getComponent(passwordConfirmIndex).setBackground(new Color(255, 230, 230));
				registerButton.setEnabled(false);
			}
		} else {
			
			contentPane.getComponent(passwordIndex).setBackground(new Color(255, 230, 230));
			contentPane.getComponent(passwordConfirmIndex).setBackground(new Color(255, 230, 230));
			registerButton.setEnabled(false);
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
		
	}

}
