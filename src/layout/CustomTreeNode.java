package layout;

import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;

public class CustomTreeNode{

	ImageIcon icon;
	String text;
	String payload;
	
	public CustomTreeNode(String text, ImageIcon icon, String payload) {
		this.text = text;
		this.icon = icon;
		this.payload = payload;
	}
	
	public String getPayload() {
		return payload;
	}
	
	public ImageIcon getImageIcon() {
		return icon;
	}
	
	public void setImageIcon(ImageIcon icon) {
		this.icon = icon;
	}
	
	public String toString() {
		return text;
	}
	
}
