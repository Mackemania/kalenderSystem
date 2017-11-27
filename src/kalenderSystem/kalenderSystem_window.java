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

import org.json.JSONObject;

import jdk.nashorn.internal.parser.JSONParser;

public class kalenderSystem_window extends JFrame {
	
	GridBagConstraints c = new GridBagConstraints();
	private int x = 0;
	private int y = 0;
	
	public kalenderSystem_window() {
		super("Kalender");
		super.setDefaultCloseOperation(EXIT_ON_CLOSE);
		super.setLocation(-1800, 100);
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
		
		String[] temps = {"SQL"};
		String SQL = "SELECT * FROM col";
		Object[] objs = {SQL};
		
		
		getDataFromServer("kalenderSystem_getData.php", temps, objs);
		
		while(run) {
			super.repaint();
			
			
		}
		
		
	}
	
	public String getDataFromServer(String path, String[] varNames, Object[] params) {
		
		String str_url = "http://localhost:0080/kalenderSystem_server/"+path;
		String query = "?";
		
		for(int i = 0; i<varNames.length; i++) {
			
			if(i==0) {
				
				query = query+varNames[i]+"="+params[i];
			
			} else {
				
				query = query+"&"+varNames[i]+"="+params[i];
			}
			
		}
		
		//System.out.println(query);
		
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
			Object[][] matrix = new Object[0][0];
			
			for(int i = 0; !jsons.isNull(""+i); i++) {
				for(int j = 0; !jsons.isNull(""+(j+1)); j++) { 
					
					System.out.println(i+" "+j);
					System.out.println(((JSONObject)jsons.get(""+i)).get(""+j));
				
				}
			}
			
			
			
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		
		return "";
	}
}
