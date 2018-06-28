package pwcg.gui.rofmap.brief;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.Logger;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.MonitorSupport;
import pwcg.gui.rofmap.MapPanelBase;
import pwcg.gui.utils.MapPointInfoPopup;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.VirtualWaypointPlotter;
import pwcg.mission.flight.waypoint.VirtualWayPointCoordinate;

public class BriefingMapPanel extends MapPanelBase implements ActionListener
{
	private static final long serialVersionUID = 1L;
	public static int NO_MAP_POINT_SELECTED = -1;
	
	private BriefingFlightParameters briefParametersContext;
    private List <FlightMap> alliedVirtualPoints = new ArrayList<FlightMap>();
    private List <FlightMap> axisVirtualPoints = new ArrayList<FlightMap>();

	private BriefingMapGUI parent = null;

	public BriefingMapPanel(BriefingMapGUI parent, BriefingFlightParameters briefParametersContext) throws PWCGException 
	{
		super(parent);
		
		this.parent = parent;
		this.briefParametersContext = briefParametersContext;
	}

	public void paintComponent(Graphics g)
	{
		try
		{
			super.paintBaseMapWithMajorGroups(g);
            			                
            g.setColor(Color.black);
            drawPoints(g);
            
            g.setColor(ColorMap.RUSSIAN_RED);
            drawVirtualPoints(g, alliedVirtualPoints, true);            
            
            g.setColor(ColorMap.ITALIAN_GOLD);
            drawVirtualPoints(g, axisVirtualPoints, true);            
		}
		catch (Exception e)
		{
			Logger.logException(e);
		}
	}

    private void drawPoints(Graphics g) throws PWCGException 
    {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));
                
        List<BriefingMapPoint> drawPoints = sortWaypoints();
        
        Color requestedColor = g.getColor();
 
        BriefingMapPoint prev = null;
        for (int i = 0; i < drawPoints.size(); ++i)
        {
            BriefingMapPoint mapPoint = drawPoints.get(i);
            
            g.setColor(requestedColor);
            
            if (prev != null)
            {
                Point prevPoint = super.coordinateToPoint(prev.coord);
                Point point = super.coordinateToPoint(mapPoint.coord);
                
                g2.draw(new Line2D.Float(prevPoint.x, prevPoint.y, point.x, point.y));
            }

            Font font = MonitorSupport.getPrimaryFont();
            g.setFont(font);
            Point point = super.coordinateToPoint(mapPoint.coord);

            // Grey out uneditable mission WPs
            if (mapPoint.editable == false && g.getColor() == Color.BLACK)
            {
                g.setColor(Color.GRAY);
            }

            // Mark targets as red
            if (mapPoint.desc.equals("Target"))
            {
                g.setColor(Color.RED);
            }

            Ellipse2D.Double circle = new Ellipse2D.Double(point.x - 4, point.y - 4, 8, 8);
            g2.fill(circle);

            g.drawString(mapPoint.desc, point.x + 4, point.y);

            prev = mapPoint;
        }
    }

    private List<BriefingMapPoint> sortWaypoints()
    {
        List <BriefingMapPoint> drawPoints = new ArrayList <BriefingMapPoint>();

        for (EditorWaypointGroup editorGroup : briefParametersContext.getWaypointEditorGroups())
        {
            drawPoints.add(editorGroup.getBriefingMapPoint());
        }
        
        return drawPoints;
    }

    private void drawVirtualPoints(Graphics g, List<FlightMap> flightMaps, boolean connect) throws PWCGException 
    {
        ConfigManagerCampaign configManager = PWCGContextManager.getInstance().getCampaign().getCampaignConfigManager();
        int showAllFlightsInBreifingKey = configManager.getIntConfigParam(ConfigItemKeys.ShowAllFlightsInBreifingKey);
        
        if (showAllFlightsInBreifingKey != 1)
        {
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));
                
        Color requestedColor = g.getColor();
 
        for (FlightMap flightMap : flightMaps)
        {
            BriefingMapPoint prevMapPoint = null;
            
           for (int i = 0; i < flightMap.mapPoints.size(); ++i)
            {
                BriefingMapPoint mapPoint = flightMap.mapPoints.get(i);

                g.setColor(requestedColor);

                if (i == 0)
                {
                    Font font = MonitorSupport.getPrimaryFont();
                    g.setFont(font);
                    Point point = super.coordinateToPoint(mapPoint.coord);
                    g.drawString(flightMap.flightType, point.x + 4, point.y);
                }
                
                
                if (i == 0)
                {
                    g.setColor(Color.GREEN);
                }
                if (i == (flightMap.mapPoints.size()-1) )
                {
                    g.setColor(Color.CYAN);
                }
    
                Point point = super.coordinateToPoint(mapPoint.coord);
                Ellipse2D.Double circle = new Ellipse2D.Double(point.x - 4, point.y - 4, 8, 8);
                g2.fill(circle);
                
                if (prevMapPoint != null && connect)
                {
                    Point prevPoint = super.coordinateToPoint(prevMapPoint.coord);
                    
                    g2.draw(new Line2D.Float(prevPoint.x, prevPoint.y, point.x, point.y));
                }

                prevMapPoint = mapPoint;
            }
        }
    }

    public void  clearVirtualPoints()
    {
        alliedVirtualPoints.clear();
        axisVirtualPoints.clear();        
    }

    public void makeMapPanelVirtualPoints(Flight flight) throws PWCGException
    {
        FlightMap flightMap = new FlightMap();
        flightMap.flightType = flight.getFlightType().toString();
        
        // VWPs are usually created later.  If we display this we have to make them now
        VirtualWaypointPlotter virtualWaypointPlotter = new VirtualWaypointPlotter();
        List<VirtualWayPointCoordinate> plotCoordinates = virtualWaypointPlotter.plotCoordinatesByMinute(flight);
        
        for (VirtualWayPointCoordinate vwp : plotCoordinates)
        {
            BriefingMapPoint mapPoint = virtualWaypointToMapPoint(vwp);
            flightMap.mapPoints.add(mapPoint);
        }
        
        if (flight.getCountry().getSideNoNeutral() == Side.ALLIED)
        {
            alliedVirtualPoints.add(flightMap);
        }
        else
        {
            axisVirtualPoints.add(flightMap);
        }
    }

	public void mouseMovedCallback(MouseEvent e) 
	{
		if (parent.getBriefingMissionHandler().getMission().isFinalized())
		{
			return;			
		}
		
		int movedIndex = determineSelectedWaypointIndex(e.getX(), e.getY());
		if (movedIndex == NO_MAP_POINT_SELECTED)
		{
			setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		}
		else
		{
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}
		
        super.mouseDraggedCallback(e);

	}

	public void mouseDraggedCallback(MouseEvent mouseEvent)
	{
	    if (!movementEnabled)
	    {
	        if (!parent.getBriefingMissionHandler().getMission().isFinalized())
	        {
    	    	BriefingMapPoint selectedMapPoint = briefParametersContext.getSelectedMapPoint();
    	    	if (selectedMapPoint != null)
    	    	{
    		        Point point = new Point();
    		        point.x = mouseEvent.getX();
    		        point.y = mouseEvent.getY();
    
    		        try
    		        {
    		            Coordinate newCoord = this.pointToCoordinate(point);
    		            selectedMapPoint.coord.setXPos(newCoord.getXPos());
    		            selectedMapPoint.coord.setZPos(newCoord.getZPos());
    		        }
    		        catch (Exception e)
    		        {
    		        }
    
    		        refresh();
    	    	}
		    }
		}
		else
		{
			super.mouseDraggedCallback(mouseEvent);
		}
	}

	public void leftClickCallback(MouseEvent mouseEvent)
	{
		if (parent.getBriefingMissionHandler().getMission().isFinalized())
		{
            super.leftClickCallback(mouseEvent);
		}
		else
		{
			int selectedMapPointIndex = determineSelectedWaypointIndex(mouseEvent.getX(), mouseEvent.getY());
			briefParametersContext.setSelectedMapPointIndex(selectedMapPointIndex);    			
    		if (selectedMapPointIndex == NO_MAP_POINT_SELECTED)
    		{
    			super.leftClickCallback(mouseEvent);
    		}
		}
	}

	public void rightClickCallback(MouseEvent e)
	{
		if (!parent.getBriefingMissionHandler().getMission().isFinalized())
		{
    		int selectedMapPointIndex = determineSelectedWaypointIndex(e.getX(), e.getY());
			briefParametersContext.setActionMapPointIndex(selectedMapPointIndex);    			
    		if (selectedMapPointIndex >= 0)
    		{
    			WaypointActionPopup menu = new WaypointActionPopup(this);
    			menu.show(e.getComponent(), e.getX(), e.getY());
    		}
		}
		else
		{
    		try
            {
                Point point = new Point();
                point.x = e.getX();
                point.y = e.getY();
                Coordinate coordinate = pointToCoordinate(point);
                MapPointInfoPopup menu = new MapPointInfoPopup(this, coordinate);
                menu.show(e.getComponent(), e.getX(), e.getY());
            }
            catch (Exception exp)
            {
                Logger.logException(exp);
            }
		}

	}	

	public void leftClickReleasedCallback(MouseEvent mouseEvent) throws PWCGException 
	{		
		super.leftClickReleasedCallback(mouseEvent);

		if (parent.getBriefingMissionHandler().getMission().isFinalized())
		{
			return;			
		}

		if (briefParametersContext.getSelectedMapPointIndex() != NO_MAP_POINT_SELECTED)
	    {
	        Point point = new Point();
	        point.x = mouseEvent.getX();
	        point.y = mouseEvent.getY();
        	Coordinate newCoord = this.pointToCoordinate(point);
        	briefParametersContext.updatePosition(newCoord);

			parent.waypointChangedNotification();

	        refresh();
	    }


		briefParametersContext.setSelectedMapPointIndex(NO_MAP_POINT_SELECTED);
	}

	public void rightClickReleasedCallback(MouseEvent e) 
	{
		if (parent.getBriefingMissionHandler().getMission().isFinalized())
		{
			return;			
		}

	}

    private BriefingMapPoint virtualWaypointToMapPoint(VirtualWayPointCoordinate vwp)
    {
        BriefingMapPoint mapPoint = new BriefingMapPoint();
        mapPoint.desc = "";
        mapPoint.coord = vwp.getCoordinate().copy();
        
        return mapPoint;
    }

	private int determineSelectedWaypointIndex(int x, int y)
	{
		int selectedMapPointIndex = NO_MAP_POINT_SELECTED;
		int lastValidIndex = NO_MAP_POINT_SELECTED;
		for (EditorWaypointGroup editorWaypointGroup :  briefParametersContext.getWaypointEditorGroups())
		{
			BriefingMapPoint mapPoint = editorWaypointGroup.getBriefingMapPoint();			
			++lastValidIndex;
			
			Point point = super.coordinateToPoint(mapPoint.coord);

			if ((Math.abs(point.x - x) < 10) && 
				(Math.abs(point.y - y) < 10))
			{
				if(mapPoint.editable)
				{
					selectedMapPointIndex = lastValidIndex;
				}
				break;
			}
		}
		
		return selectedMapPointIndex;
	}
	
	/**
	 * @return
	 */
	public Point upperLeft()
	{
		Point upperLeft = new Point();
		
		upperLeft.x = 10000000;
		upperLeft.y = 10000000;
		
		for (EditorWaypointGroup editorWaypointGroup: briefParametersContext.getWaypointEditorGroups())
		{
			BriefingMapPoint mapPoint= editorWaypointGroup.getBriefingMapPoint();
			Point point = super.coordinateToPoint(mapPoint.coord);
			
			if (point.x < upperLeft.x)
			{
				upperLeft.x = point.x;
			}
			if (point.y < upperLeft.y)
			{
				upperLeft.y = point.y;
			}
		}
		
		return upperLeft;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		try
		{
			String action = arg0.getActionCommand();
			if (action.contains("Add"))
			{
				if (briefParametersContext.getActionMapPointIndex() >= 0)
				{
					//briefParametersContext.addWayPoint(briefParametersContext.getActionMapPointIndex());
					//parent.waypointChangedNotification();
				}
			}
			else if (action.contains("Remove"))
			{
				if (briefParametersContext.getActionMapPointIndex() >= 0)
				{
					briefParametersContext.removeWayPoint(briefParametersContext.getActionMapPointIndex());
					parent.waypointChangedNotification();
				}
			}
			else if (action.contains("Cancel"))
			{
			}
		}
		catch (Exception e)
		{
			Logger.logException(e);
		}
		
	}
	
	private class FlightMap
	{
	    String flightType;
        List<BriefingMapPoint> mapPoints = new ArrayList<BriefingMapPoint>();
	}
}
