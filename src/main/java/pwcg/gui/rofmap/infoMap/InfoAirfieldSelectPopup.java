package pwcg.gui.rofmap.infoMap;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class InfoAirfieldSelectPopup extends JPopupMenu
{
    private static final long serialVersionUID = 1L;

    public InfoAirfieldSelectPopup(InfoMapPanel parent, String airfield)
    {
        JMenuItem airfieldMenuItem = new JMenuItem("Select Target Airfield:"+airfield);
        airfieldMenuItem.addActionListener(parent);
        add(airfieldMenuItem);
        
        JMenuItem cancelWpItem = new JMenuItem("Cancel");
        add(cancelWpItem);
        cancelWpItem.addActionListener(parent);     
    }

}
