package pwcg.gui.rofmap.editmap;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class FrontActionPopup extends JPopupMenu
{
	private static final long serialVersionUID = 1L;

	public FrontActionPopup(FrontLinesEditorMapPanel parent)
    {
    	JMenuItem addWpItem = new JMenuItem("Add Front Point");
    	JMenuItem delWpItem = new JMenuItem("Remove Front Point");
    	JMenuItem cancelWpItem = new JMenuItem("Cancel");
    	
    	add(addWpItem);
    	add(delWpItem);
    	add(cancelWpItem);

    	addWpItem.addActionListener(parent);
    	delWpItem.addActionListener(parent);    	
    	cancelWpItem.addActionListener(parent);    	
    }
}
