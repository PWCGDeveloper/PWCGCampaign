package pwcg.gui.rofmap;

import java.awt.BorderLayout;
import java.awt.Point;
import java.util.Date;

import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.gui.UiImageResolver;
import pwcg.gui.utils.ImageResizingPanel;

public abstract class MapGUI extends ImageResizingPanel
{
	private static final long serialVersionUID = 1L;

	protected MapScroll mapScroll = null;
	protected Date mapDate = null;

	public MapGUI(Date mapDate) throws PWCGException
	{
        super("");
        String imagePath = UiImageResolver.getImageMain("BrickWall.jpg");
        this.setImage(imagePath);
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

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
            Coordinate centerMapCoordinate = PWCGContext.getInstance().getCurrentMap().getMapCenter();
		    initialPosition = mapScroll.getMapPanel().coordinateToPoint(centerMapCoordinate);
		}
		else
		{
            initialPosition.x -= 700;
            initialPosition.y -= 500;
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
