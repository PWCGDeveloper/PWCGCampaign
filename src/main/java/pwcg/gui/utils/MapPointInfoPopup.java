package pwcg.gui.utils;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import pwcg.core.location.Coordinate;
import pwcg.gui.rofmap.MapPanelBase;

public class MapPointInfoPopup extends JPopupMenu
{
	private static final long serialVersionUID = 1L;

	public MapPointInfoPopup(MapPanelBase parent, Coordinate coordinate)
    {
        JMenuItem coordinateitem = new JMenuItem("Coordinates: " + coordinate.getXPos() + ", " + coordinate.getZPos());
        add(coordinateitem);
        coordinateitem.addActionListener(parent);
    }
}
