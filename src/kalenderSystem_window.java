import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.json.JSONArray;
import org.json.JSONObject;

public class kalenderSystem_window extends JFrame {
	
	GridBagConstraints c = new GridBagConstraints();
	private int x = 0;
	private int y = 0;
	private String hashkey = "";
	private int userID = -1;
	Thread thread;
	
	public kalenderSystem_window() {
		super("Kalender");
		super.setDefaultCloseOperation(EXIT_ON_CLOSE);
		super.setLocation(100, 100);
		super.setPreferredSize(new Dimension(1200, 675));
		super.setLayout(new BorderLayout());
		
		/*
		try {
		
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		} catch (Exception e) {
		
			e.printStackTrace();
		
		}
		*/
		
		
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
		String[] menuNames = {"Startsida", "Logga in"};
		JButton[] buttons = new JButton[menuNames.length];
		
		
		for(int i = 0; i<buttons.length; i++) {
			
			c.gridx = x;
			c.gridy = y;
			
			buttons[i] = new JButton(menuNames[i]);
			menuPanel.add(buttons[i], c);
			
			y++;
			
		}
		
		JPanel temp = new JPanel();
		temp.setLayout(new BorderLayout());
		temp.add(menuPanel, BorderLayout.NORTH);
		
		//Scroll width = 12
		JScrollPane mainMenuPanel = new JScrollPane(temp);
		mainMenuPanel.setPreferredSize(new Dimension((int)temp.getPreferredSize().getWidth()+30, (int)temp.getPreferredSize().getHeight()));
		mainMenuPanel.setBorder(null);
		super.add(mainMenuPanel, BorderLayout.WEST);
		
		pack();
		//super.setVisible(true);
		
		boolean run = true;
		
		//System.out.println(kalenderSystem_register("Tobben", "Admin", "admisnn@cals.se", "Test", "Test"));
		kalenderSystem_login("Mackemania", "Admin");
		kalenderSystem_addActivity(1, "", null, null);
		
		/*thread = new Thread();
		while(run) {
			
			repaint();
			
			try {
				
				Thread.sleep(10);
			
			} catch (InterruptedException e) {
				
				e.printStackTrace();
				
			}
		}*/
		
		
	}
	
	public boolean kalenderSystem_addActivity(int calendarID, String name, Date startTime, Date endTime) {
		
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
			
			System.out.println(eventIDs.get(i));
		}
		
		return false;
	}
	
	public boolean kalenderSystem_login(String username, String password) {
		
		String str_url = "http://localhost:0080/kalenderSystem_server/login.php";
		String query = "?username="+username+"&password="+password;
		
		str_url = str_url+query;
		
		
		try {
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
			JSONObject json = new JSONObject(retval);
			userID = json.getInt("0");
			hashkey = json.getString("1");
			
			//System.out.println(hashkey);
			
			String SQL = "SELECT hashID FROM hash WHERE hash = ? AND userID = ?";
			String types = "si";
			Object[] params = {hashkey, userID};
			
			Object[][] matrix = kalenderSystem_getData("kalenderSystem_getData.php", SQL, types, params);
			
			if((int) matrix[0][0] == 0) {
				
				userID = -1;
				hashkey = "";
				
				return false;
				
			} else {
				
				return true;
			}
		
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	public boolean kalenderSystem_register(String username, String password, String email, String firstName, String lastName) {
		
		String SQL = "INSERT INTO users(username, password, email, firstName, lastName) VALUES(?, ?, ?, ?, ?)";
		String types = "sssss";
		Object[] params = {username, password, email, firstName, lastName};
		kalenderSystem_sendData("kalenderSystem_register.php", SQL, types, params);
		
		String str_url = "http://localhost:0080/kalenderSystem_server/kalenderSystem_register.php";
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
	 */
	public boolean kalenderSystem_sendData(String path, String SQL, String types, Object[] params) {
		
		//Skapar en url som leder till valfri fil p� localhost
		String str_url = "http://localhost:0080/kalenderSystem_server/"+path;
		String query = "?";
		
		//Skapar en fr�ga som servern kommer ta emot.
		if(types.equals("�")) {
			
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
		String str_url = "http://localhost:0080/kalenderSystem_server/"+path;
		String query = "?";
		
		//Skapar en fr�ga som servern kommer ta emot.
		if(types.equals("�")) {
			
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
			
			e.printStackTrace();
		}
		
		
		return null;
	}
}
