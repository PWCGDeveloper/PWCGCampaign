package pwcg.gui.rofmap.brief;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class WaypointActionPopup extends JPopupMenu
{
	private static final long serialVersionUID = 1L;

	public WaypointActionPopup(BriefingMapPanel parent)
    {
    	JMenuItem addWpItem = new JMenuItem("Add WP");
    	JMenuItem delWpItem = new JMenuItem("Remove WP");
    	JMenuItem cancelWpItem = new JMenuItem("Cancel");
    	
    	add(addWpItem);
    	add(delWpItem);
    	add(cancelWpItem);

    	addWpItem.addActionListener(parent);
    	delWpItem.addActionListener(parent);    	
    	cancelWpItem.addActionListener(parent);    	
    }
}
