import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

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
	
	public kalenderSystem_window() {
		super("Kalender");
		super.setDefaultCloseOperation(EXIT_ON_CLOSE);
		super.setLocation(100, 100);
		super.setSize(1200, 675);
		super.setLayout(new BorderLayout());
		
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
		
		super.setVisible(true);
		
		boolean run = true;
		
		kalenderSystem_registerUser("Mackemania", "Admin", "admin@cal.se", "Test", "Test");
		
		//sendDataToServer("kalenderSystem_sendData.php", SQL, types, params);
		//Object[][] matrix = getDataFromServer("kalenderSystem_getData.php", SQL, types, params);
		//System.out.println(matrix[0][1]);
		
		/*while(run) {
			
			super.repaint();
			
		}*/
		
		
	}
	
	public void kalenderSystem_registerUser(String username, String password, String email, String firstName, String lastName) {
		
		String SQL = "INSERT INTO users(username, password, email, firstName, lastName) VALUES(?, ?, ?, ?, ?)";
		String types = "sssss";
		Object[] params = {username, password, email, firstName, lastName};
		kalenderSystem_sendDataToServer("kalenderSystem_sendData.php", SQL, types, params);
		
		
	}
	
	public void kalenderSystem_logInUser() {
		
		
	}
	
	/* Anv�nds f�r att skicka data till filen 'path' p� localhost.
	 * 
	 * Inputs:
	 * 		-String path, filnamet p� den fil man vill komma �t fr�n servern
	 * 		-String SQL, SQL-uttrycket som man vill exekvera
	 * 		-String types, En textstr�ng d�r typerna av p�f�ljande variabler �r d�r s = string, i = int, d = double och b = blob
	 */
	public void kalenderSystem_sendDataToServer(String path, String SQL, String types, Object[] params) {
		
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
		
		
		str_url = str_url+query;
		str_url = str_url.replace(" ", "%20");
		
		try {
			
			//Connectar till url:en
			System.out.println(str_url);
			URL url = new URL(str_url);
			URLConnection conn = url.openConnection();
			InputStream is = conn.getInputStream();
			
			String retval = "";
			
			//H�mtar allt som st�r p� "webbsidan"
			while(is.available()>0) {
				
				retval = retval+(char)is.read();
			
			}
			
			System.out.println(retval);
		
		} catch (Exception e) {
			
			e.printStackTrace();
		}
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
	public Object[][] kalenderSystem_getDataFromServer(String path, String SQL, String types, Object[] params) {
		
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
		
		
		str_url = str_url+query;
		str_url = str_url.replace(" ", "%20");
		
		try {
			//Connectar till servern
			System.out.println(str_url);
			URL url = new URL(str_url);
			URLConnection conn = url.openConnection();
			InputStream is = conn.getInputStream();
			
			String retval = "";
			
			//h�mntar allt som st�r p� "webbsidan"
			while(is.available()>0) {
				
				retval = retval+(char)is.read();
			
			}
			
			System.out.println(retval);
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
			
			
			return matrix;
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		
		return null;
	}
}