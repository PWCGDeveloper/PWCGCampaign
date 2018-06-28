package pwcg.gui.rofmap;

import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JScrollPane;

import pwcg.gui.utils.ScrollBarWrapper;

public class MapScroll
{
	private JScrollPane mapScrollPane = null;
	private MapPanelBase mapPanel;
	
	public MapScroll (MapPanelBase mapPanel)
	{
		this.mapPanel = mapPanel;

        mapScrollPane = ScrollBarWrapper.makeScrollPane(mapPanel);
	}
	
	/**
	 * 
	 */
	public void setScrollBarPosition(Point position)
	{
		mapScrollPane.getHorizontalScrollBar().setValue(position.x);
		mapScrollPane.getVerticalScrollBar().setValue(position.y);
	}

	
	/**
	 * 
	 */
	public void moveScrollBarPosition(int x, int y)
	{
		Point scrollPosition = new Point();
		scrollPosition.x = mapScrollPane.getHorizontalScrollBar().getValue() - x;
		scrollPosition.y = mapScrollPane.getVerticalScrollBar().getValue() - y;

		setScrollBarPosition(scrollPosition);
	}

	/**
	 * 
	 */
	public void setScrollRange()
	{
		Dimension mapSize = mapPanel.getMapSize();
		mapScrollPane.getHorizontalScrollBar().setMaximum(mapSize.width);
		mapScrollPane.getVerticalScrollBar().setMaximum(mapSize.height);
		
	}

	public JScrollPane getMapScrollPane() {
		return mapScrollPane;
	}

	public void setMapScrollPane(JScrollPane mapScrollPane) {
		this.mapScrollPane = mapScrollPane;
	}

	public MapPanelBase getMapPanel() {
		return mapPanel;
	}

	public void setMapPanel(MapPanelBase mapPanel) {
		this.mapPanel = mapPanel;
	}
}
