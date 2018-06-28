package pwcg.gui.rofmap.infoMap;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class InfoMapPopup extends JPopupMenu
{
	private static final long serialVersionUID = 1L;

	public InfoMapPopup(InfoMapPanel parent, String text)
    {
        JMenuItem coordinateitem = new JMenuItem(text);
        add(coordinateitem);
        coordinateitem.addActionListener(parent);
    }
}
