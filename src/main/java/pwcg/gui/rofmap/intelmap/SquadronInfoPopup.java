package pwcg.gui.rofmap.intelmap;

import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class SquadronInfoPopup extends JPopupMenu
{
	private static final long serialVersionUID = 1L;

	public SquadronInfoPopup(IntelMapPanel parent, List<IntelSquadronMapPoint>mapPoints)
    {
		for (IntelSquadronMapPoint mapPoint : mapPoints)
		{
			JMenuItem mapPointItem = new JMenuItem(mapPoint.desc);
			mapPointItem.setActionCommand("Squadron:" + mapPoint.desc);
	    	add(mapPointItem);
	    	mapPointItem.addActionListener(parent);
		}
    }
}
