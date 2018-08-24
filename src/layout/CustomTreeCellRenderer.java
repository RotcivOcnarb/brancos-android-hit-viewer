package layout;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import de.javasoft.plaf.synthetica.plain.TreeCellRenderer;

public class CustomTreeCellRenderer extends TreeCellRenderer{
	
	static ArrayList<String> hit_types;
	
	static {
		hit_types = new ArrayList<String>();
		hit_types.add("event");
		hit_types.add("pageview");
	}
	
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus){
		CustomTreeNode nodeobj = (CustomTreeNode) ((DefaultMutableTreeNode) value).getUserObject();
		setIcon(nodeobj.getImageIcon());
		setText(nodeobj.text);
		return this;
	}

}
