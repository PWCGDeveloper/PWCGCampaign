package pwcg.gui.rofmap;

import java.awt.Point;
import java.util.Date;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;
import pwcg.gui.PwcgGuiContext;

public abstract class MapGUI extends PwcgGuiContext
{
	private static final long serialVersionUID = 1L;

	protected MapScroll mapScroll = null;
	protected Date mapDate = null;

	public MapGUI(Date mapDate) throws PWCGException
	{
	    super();
	    setMapDate(mapDate);
	}
	
	public MapScroll getMapScroll() {
		return mapScroll;
	}

	public void setMapScroll(MapScroll mapScroll) {
		this.mapScroll = mapScroll;
	}

	public void centerMapAt(Point initialPosition)
	{
		if (initialPosition == null)
		{
		    initialPosition = PWCGContextManager.getInstance().getCurrentMap().getMapCenter();
		}
		else
		{
            initialPosition.x -= 300;
            initialPosition.y -= 200;
		}
		
		if (mapScroll != null)
		{
			mapScroll.setScrollRange();
			mapScroll.setScrollBarPosition(initialPosition);
		}
	}

	public Date getMapDate()
	{
		return mapDate;
	}

	public void setMapDate(Date mapDate) throws PWCGException
	{
		this.mapDate = mapDate;
	}
}
