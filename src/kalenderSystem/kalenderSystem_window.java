package kalenderSystem;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import sun.net.www.http.HttpClient;

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
		
		while(run) {
			super.repaint();
			
			
		}
		
		
	}
	
	public String getDataFromServer(String path) {
		
		String url = "http://localhost:0080/"+path;
		String charset = "UTF-8";
		
		try {
			
			
		} catch (Exception e) {
			
			System.out.println(e);
		}
		
		
		return "";
	}
}
