package kalenderSystem;

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

import jdk.nashorn.internal.parser.JSONParser;

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
		
		
		String SQL = "SELECT * FROM col where en=?";
		String types = "s";
		Object[] params = {"ett"};
		
		Object[][] matrix = getDataFromServer("kalenderSystem_getData.php", SQL, types, params);
		
		
		System.out.println(matrix[0][1]);
		/*while(run) {
			
			super.repaint();
			
		}*/
		
		
	}
	
	public Object[][] getDataFromServer(String path, String SQL, String types, Object[] params) {
		
		String str_url = "http://localhost:0080/kalenderSystem_server/"+path;
		String query = "?";
		
		if(types.equals("")) {
			
			query = query+"SQL="+SQL;
			
		} else {
			
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
			
			System.out.println(str_url);
			URL url = new URL(str_url);
			URLConnection conn = url.openConnection();
			InputStream is = conn.getInputStream();
			
			String retval = "";
			
			while(is.available()>0) {
				
				retval = retval+(char)is.read();
			
			}
			
			System.out.println(retval);
			JSONObject jsons = new JSONObject(retval);
			//System.out.println(jsons.toString());
			
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
