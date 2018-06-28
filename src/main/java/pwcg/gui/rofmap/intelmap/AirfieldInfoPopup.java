package pwcg.gui.rofmap.intelmap;

import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class AirfieldInfoPopup extends JPopupMenu
{
	private static final long serialVersionUID = 1L;

	public AirfieldInfoPopup(IntelMapPanel parent, List<IntelAirfieldMapPoint>mapPoints)
    {
		for (IntelAirfieldMapPoint mapPoint : mapPoints)
		{
			JMenuItem mapPointItem = new JMenuItem(mapPoint.name);
			mapPointItem.setActionCommand("Airfield:" + mapPoint.name);
	    	add(mapPointItem);
	    	mapPointItem.addActionListener(parent);
		}
    }
}
