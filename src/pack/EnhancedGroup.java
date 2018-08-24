package pack;

import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;

import layout.CustomTreeNode;

public class EnhancedGroup {
	
	HashMap<String, HitNameInfo> parameters;
	HashMap<String, EnhancedGroup> childs;
	
	static ImageIcon icon_coin;
	
	public EnhancedGroup() {
		
		if(icon_coin == null) {
			try {
			icon_coin = new ImageIcon(MyPanel.getScaledImage(ImageIO.read(getClass().getResourceAsStream("/moeda.png")), 16, 16));
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		parameters = new HashMap<String, HitNameInfo>();
		childs = new HashMap<String, EnhancedGroup>();
	}
	
	public void put(String key, HitNameInfo value) {
		parameters.put(key, value);
	}
	
	public void put(String key, EnhancedGroup value) {
		childs.put(key, value);
	}
	
	public HitNameInfo getParameter(String key) {
		return parameters.get(key);
	}
	
	public EnhancedGroup getChild(String key) {
		return childs.get(key);
	}
	
	public boolean containsChild(String child) {
		return childs.containsKey(child);
	}

	public boolean containsParameter(String param) {
		return parameters.containsKey(param);
	}
	
	public boolean isEmpty() {
		return parameters.size() + childs.size() <= 0;
	}
	
	public DefaultMutableTreeNode getNode(String name) {
		
		DefaultMutableTreeNode node = new DefaultMutableTreeNode();
		node.setUserObject(new CustomTreeNode(name, icon_coin, "enhanced ecommerce"));
		
		for(String s : parameters.keySet()) {
			DefaultMutableTreeNode param = new DefaultMutableTreeNode(new CustomTreeNode(parameters.get(s).fullName, MyPanel.icon_parameter, parameters.get(s).raw_name));
			param.add(new DefaultMutableTreeNode(new CustomTreeNode(parameters.get(s).getValue(), MyPanel.icon_value, parameters.get(s).getValue())));
			node.add(param);
		}
		
		for(String s : childs.keySet()) {
			node.add(childs.get(s).getNode(s));
		}
		
		return node;
	}
	
	public String toString() {
		String txt = "=== Grupo ===\n";
		txt += "\t" + parameters.size() + " parametros:\n";
		for(String s : parameters.keySet()) {
			txt += "\t" + parameters.get(s).toString() + "\n";
		}
		txt += "\t" + childs.size() + " subgrupos:\n";
		for(String s : childs.keySet()) {
			txt += "\t" + childs.get(s).toString() + "\n";
		}
		return txt;
	}
}
