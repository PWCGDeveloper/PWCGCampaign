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
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.image.ImageCache;
import pwcg.gui.rofmap.MapPanelBase;
import pwcg.gui.rofmap.brief.builder.BriefingMapPointFactory;
import pwcg.gui.rofmap.brief.model.BriefingData;
import pwcg.gui.rofmap.brief.model.BriefingFlightParameters;
import pwcg.gui.rofmap.brief.model.BriefingMapPoint;
import pwcg.gui.utils.MapPointInfoPopup;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.missionpoint.MissionPoint;
import pwcg.mission.target.AssaultDefinition;

public class BriefingMapPanel extends MapPanelBase implements ActionListener
{
	private static final long serialVersionUID = 1L;
	public static int NO_MAP_POINT_SELECTED = -1;
	
    private Mission mission;
    private BriefingMapGUI parent;
	
    private List <FlightMap> alliedVirtualPoints = new ArrayList<FlightMap>();
    private List <FlightMap> axisVirtualPoints = new ArrayList<FlightMap>();
    private CoordinateBox missionBorders = new CoordinateBox();
    
	public BriefingMapPanel(BriefingMapGUI parent) throws PWCGException
    {
        super(parent);
        
        this.parent = parent;
        this.mission = BriefingContext.getInstance().getBriefingData().getMission();
    }

    public void paintComponent(Graphics g)
	{
		try
		{
			super.paintBaseMapWithMajorGroups(g);
            			                
            g.setColor(Color.black);
            drawFlightWaypoints(g);
            
            g.setColor(ColorMap.RUSSIAN_RED);
            drawAiFlightWaypoints(g, alliedVirtualPoints);            
            
            g.setColor(ColorMap.FRENCH_NAVY_BLUE);
            drawAiFlightWaypoints(g, axisVirtualPoints);            
            
            drawAssaults(g);            
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
		}
	}

    private void drawAssaults(Graphics g) throws PWCGException
    {                
        for (AssaultDefinition assaultDefinition : mission.getBattleManager().getMissionAssaultDefinitions())
        {
            BufferedImage arrowImage = null;
            String imagePath = PWCGContext.getInstance().getDirectoryManager().getPwcgImagesDir() + "Overlay\\";
            if (assaultDefinition.getAssaultingCountry().getSide() == Side.ALLIED)
            {
                imagePath += "RedArrow.png";
            }
            else
            {
                imagePath += "BlueArrow.png";
            }

            double assaultToDefenseDirection = MathUtils.calcAngle(assaultDefinition.getAssaultPosition(), assaultDefinition.getDefensePosition());
            arrowImage = ImageCache.getInstance().getRotatedImage(imagePath, Double.valueOf(assaultToDefenseDirection).intValue());

            Point mapAssaultPointRectangleFrontPoint = coordinateToPoint(assaultDefinition.getDefensePosition());
            mapAssaultPointRectangleFrontPoint.x -= (arrowImage.getHeight() / 2);
            mapAssaultPointRectangleFrontPoint.y -= (arrowImage.getHeight() / 2);            
            
            g.drawImage(arrowImage, mapAssaultPointRectangleFrontPoint.x, mapAssaultPointRectangleFrontPoint.y, null);

        }
    }

    private void drawFlightWaypoints(Graphics g) throws PWCGException 
    {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));
        Color requestedColor = g.getColor();
 
        BriefingMapPoint previousMapPoint = null;
        BriefingFlightParameters briefingFlightParameters = BriefingContext.getInstance().getBriefingData().getActiveBriefingFlight().getBriefingFlightParameters();
        for (BriefingMapPoint mapPoint : briefingFlightParameters.getBriefingMapMapPoints())
        {            
            g.setColor(requestedColor);
            
            if (previousMapPoint != null)
            {
                Point prevPoint = super.coordinateToPoint(previousMapPoint.getPosition());
                Point point = super.coordinateToPoint(mapPoint.getPosition());
                
                g2.draw(new Line2D.Float(prevPoint.x, prevPoint.y, point.x, point.y));
            }

            Font font = PWCGMonitorFonts.getPrimaryFont();
            g.setFont(font);
            Point point = super.coordinateToPoint(mapPoint.getPosition());

            if (mapPoint.isEditable() == false && g.getColor() == Color.BLACK)
            {
                g.setColor(Color.GRAY);
            }

            if (mapPoint.getDesc().equals("Target"))
            {
                g.setColor(Color.RED);
            }

            Ellipse2D.Double circle = new Ellipse2D.Double(point.x - 4, point.y - 4, 8, 8);
            g2.fill(circle);

            g.drawString(mapPoint.getDesc(), point.x + 4, point.y);

            previousMapPoint = mapPoint;
        }
    }

    private void drawAiFlightWaypoints(Graphics g, List<FlightMap> flightMaps) throws PWCGException 
    {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));
                
        Color requestedColor = g.getColor();
 
        BriefingData briefingData = BriefingContext.getInstance().getBriefingData();
        for (FlightMap flightMap : flightMaps)
        {
            if (briefingData.getAiFlightsToDisplay().containsKey(flightMap.squadronId))
            {
                paintWaypointLines(g, g2, requestedColor, flightMap);
            }
        }
        
        for (FlightMap flightMap : flightMaps)
        {
            if (briefingData.getAiFlightsToDisplay().containsKey(flightMap.squadronId))
            {
                paintWaypoints(g, g2, flightMap);
            }
        }
        
        drawMissionBorders(g, g2);
    }

    private void paintWaypoints(Graphics g, Graphics2D g2, FlightMap flightMap) throws PWCGException
    {
        for (int i = 0; i < flightMap.mapPoints.size(); ++i)
        {
            BriefingMapPoint mapPoint = flightMap.mapPoints.get(i);
            if (i == 0)
            {
                g.setColor(Color.GREEN);
                Point point = super.coordinateToPoint(mapPoint.getPosition());
                Ellipse2D.Double circle = new Ellipse2D.Double(point.x - 8, point.y - 8, 16, 16);
                g2.fill(circle);
                
                g.setColor(Color.BLACK);
                Font font = PWCGMonitorFonts.getPrimaryFont();
                g.setFont(font);
                g.drawString((flightMap.flightType + "/" + flightMap.planeType), point.x + 4, point.y);
            }
            if (i == (flightMap.mapPoints.size()-1) )
            {
                g.setColor(Color.RED);
                Point point = super.coordinateToPoint(mapPoint.getPosition());
                Ellipse2D.Double circle = new Ellipse2D.Double(point.x - 8, point.y - 8, 16, 16);
                g2.fill(circle);
            }
            if (mapPoint.isTarget())
            {
                g.setColor(Color.RED);
                Point point = super.coordinateToPoint(mapPoint.getPosition());
                Ellipse2D.Double circle = new Ellipse2D.Double(point.x - 8, point.y - 8, 16, 16);
                g2.fill(circle);
            }
        }
    }

    private void paintWaypointLines(Graphics g, Graphics2D g2, Color requestedColor, FlightMap flightMap)
    {
        BriefingMapPoint prevMapPoint = null;
        
         for (int i = 0; i < flightMap.mapPoints.size(); ++i)
         {
            BriefingMapPoint mapPoint = flightMap.mapPoints.get(i);
            g.setColor(requestedColor);

            Point point = super.coordinateToPoint(mapPoint.getPosition());
            Ellipse2D.Double circle = new Ellipse2D.Double(point.x - 4, point.y - 4, 8, 8);
            g2.fill(circle);

            g2.drawString(mapPoint.getDesc(), point.x + 4, point.y);

            if (prevMapPoint != null)
            {
                Point prevPoint = super.coordinateToPoint(prevMapPoint.getPosition());
                
                g2.draw(new Line2D.Float(prevPoint.x, prevPoint.y, point.x, point.y));
            }

            prevMapPoint = mapPoint;
        }
    }

    private void drawMissionBorders(Graphics g, Graphics2D g2)
    {
        Coordinate sw = missionBorders.getSW().copy();
        Coordinate se = new Coordinate(missionBorders.getNE().getXPos(), 0.0, missionBorders.getSW().getZPos());
        Coordinate ne = missionBorders.getNE().copy();
        Coordinate nw = new Coordinate(missionBorders.getSW().getXPos(), 0.0, missionBorders.getNE().getZPos());

        Point swPoint = super.coordinateToPoint(sw);
        Point sePoint = super.coordinateToPoint(se);
        Point nePoint = super.coordinateToPoint(ne);
        Point nwPoint = super.coordinateToPoint(nw);
        
        g.setColor(Color.YELLOW);
        g2.draw(new Line2D.Float(swPoint.x, swPoint.y, sePoint.x, sePoint.y));
        g2.draw(new Line2D.Float(sePoint.x, sePoint.y, nePoint.x, nePoint.y));
        g2.draw(new Line2D.Float(nePoint.x, nePoint.y, nwPoint.x, nwPoint.y));
        g2.draw(new Line2D.Float(nwPoint.x, nwPoint.y, swPoint.x, swPoint.y));
    }

    public void  clearVirtualPoints()
    {
        alliedVirtualPoints.clear();
        axisVirtualPoints.clear();        
    }

    public void makeMapPanelVirtualPoints(IFlight flight) throws PWCGException
    {       
        FlightMap flightMap = buildFlightMap(flight);
        if (flight.getFlightInformation().getCountry().getSideNoNeutral() == Side.ALLIED)
        {
            alliedVirtualPoints.add(flightMap);
        }
        else
        {
            axisVirtualPoints.add(flightMap);
        }
    }

    private FlightMap buildFlightMap(IFlight flight) throws PWCGException
    {
        FlightMap flightMap = new FlightMap();
        flightMap.flightType = flight.getFlightType().toString();
        flightMap.planeType = flight.getFlightPlanes().getFlightLeader().getDisplayName();
        flightMap.squadronId = flight.getSquadron().getCompanyId();
        
        for (MissionPoint waypoint : flight.getWaypointPackage().getFlightMissionPoints())
        {
            BriefingMapPoint mapPoint = BriefingMapPointFactory.missionPointToMapPoint(waypoint);
            flightMap.mapPoints.add(mapPoint);
        }
        
        return flightMap;
    }

	public void mouseMovedCallback(MouseEvent e) 
	{
		if (mission.getFinalizer().isFinalized())
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
	        if (!mission.getFinalizer().isFinalized())
	        {
	            BriefingFlightParameters briefingFlightParameters = BriefingContext.getInstance().getBriefingData().getActiveBriefingFlight().getBriefingFlightParameters();
    	    	BriefingMapPoint selectedMapPoint = briefingFlightParameters.getSelectedMapPoint();
    	    	if (selectedMapPoint != null)
    	    	{
    		        Point point = new Point();
    		        point.x = mouseEvent.getX();
    		        point.y = mouseEvent.getY();
    
    		        try
    		        {
    		            Coordinate updatedPosition = this.pointToCoordinate(point);
    		            briefingFlightParameters.updatePosition(updatedPosition);
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
		if (mission.getFinalizer().isFinalized())
		{
            super.leftClickCallback(mouseEvent);
		}
		else
		{
			int selectedMapPointIndex = determineSelectedWaypointIndex(mouseEvent.getX(), mouseEvent.getY());
	        BriefingFlightParameters briefingFlightParameters = BriefingContext.getInstance().getBriefingData().getActiveBriefingFlight().getBriefingFlightParameters();
			briefingFlightParameters.setSelectedMapPointIndex(selectedMapPointIndex);    			
    		if (selectedMapPointIndex == NO_MAP_POINT_SELECTED)
    		{
    			super.leftClickCallback(mouseEvent);
    		}
		}
	}

    public void rightClickCallback(MouseEvent e)
    {
        try
        {
            int selectedMapPointIndex = determineSelectedWaypointIndex(e.getX(), e.getY());
            if (selectedMapPointIndex != NO_MAP_POINT_SELECTED)
            {
                WaypointInformationPopup menu = new WaypointInformationPopup(selectedMapPointIndex);
                menu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
        catch (Exception exp)
        {
            PWCGLogger.logException(exp);
        }
    }

	public void centerClickCallback(MouseEvent e)
	{
		if (!mission.getFinalizer().isFinalized())
		{
    		int selectedMapPointIndex = determineSelectedWaypointIndex(e.getX(), e.getY());
            BriefingFlightParameters briefingFlightParameters = BriefingContext.getInstance().getBriefingData().getActiveBriefingFlight().getBriefingFlightParameters();
			briefingFlightParameters.setActionMapPointIndex(selectedMapPointIndex);    			
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
                PWCGLogger.logException(exp);
            }
		}

	}	

	public void leftClickReleasedCallback(MouseEvent mouseEvent) throws PWCGException 
	{		
		super.leftClickReleasedCallback(mouseEvent);

		if (mission.getFinalizer().isFinalized())
		{
			return;			
		}

        BriefingFlightParameters briefingFlightParameters = BriefingContext.getInstance().getBriefingData().getActiveBriefingFlight().getBriefingFlightParameters();
		if (briefingFlightParameters.getSelectedMapPointIndex() != NO_MAP_POINT_SELECTED)
	    {
	        Point point = new Point();
	        point.x = mouseEvent.getX();
	        point.y = mouseEvent.getY();
        	Coordinate newCoord = this.pointToCoordinate(point);
        	briefingFlightParameters.updatePosition(newCoord);

	        refresh();
	    }


		briefingFlightParameters.setSelectedMapPointIndex(NO_MAP_POINT_SELECTED);
	}

	public void rightClickReleasedCallback(MouseEvent e) 
	{
		if (mission.getFinalizer().isFinalized())
		{
			return;			
		}

	}

	private int determineSelectedWaypointIndex(int x, int y)
	{
		int selectedMapPointIndex = NO_MAP_POINT_SELECTED;
		int lastValidIndex = NO_MAP_POINT_SELECTED;
        BriefingFlightParameters briefingFlightParameters = BriefingContext.getInstance().getBriefingData().getActiveBriefingFlight().getBriefingFlightParameters();
		for (BriefingMapPoint mapPoint :  briefingFlightParameters.getBriefingMapMapPoints())
		{
			++lastValidIndex;
			
			Point point = super.coordinateToPoint(mapPoint.getPosition());

			if ((Math.abs(point.x - x) < 10) && 
				(Math.abs(point.y - y) < 10))
			{
				if(mapPoint.isEditable())
				{
					selectedMapPointIndex = lastValidIndex;
				}
				break;
			}
		}
		
		return selectedMapPointIndex;
	}

	public Point upperLeft()
	{
		Point upperLeft = new Point();
		
		upperLeft.x = 10000000;
		upperLeft.y = 10000000;
		
        BriefingFlightParameters briefingFlightParameters = BriefingContext.getInstance().getBriefingData().getActiveBriefingFlight().getBriefingFlightParameters();
        for (BriefingMapPoint mapPoint :  briefingFlightParameters.getBriefingMapMapPoints())
		{
			Point point = super.coordinateToPoint(mapPoint.getPosition());
			
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
	        BriefingFlightParameters briefingFlightParameters = BriefingContext.getInstance().getBriefingData().getActiveBriefingFlight().getBriefingFlightParameters();
			if (action.contains("Add"))
			{
				if (briefingFlightParameters.getActionMapPointIndex() >= 0)
				{
                    BriefingMapPoint selectedActionPoint = briefingFlightParameters.getSelectedActionMapPoint();
                    parent.waypointAddedNotification(selectedActionPoint.getWaypointID());
				}
			}
			else if (action.contains("Remove"))
			{
				if (briefingFlightParameters.getActionMapPointIndex() >= 0)
				{
				    BriefingMapPoint selectedActionPoint = briefingFlightParameters.getSelectedActionMapPoint();
                    parent.waypointRemovedNotification(selectedActionPoint.getWaypointID());
				}
			}
			else if (action.contains("Cancel"))
			{
			}
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
		}
		
	}
	
	private class FlightMap
	{
        String flightType;
        String planeType;
        int squadronId;
        List<BriefingMapPoint> mapPoints = new ArrayList<BriefingMapPoint>();
	}

    public void setMissionBorders(CoordinateBox missionBorders)
    {
        this.missionBorders = missionBorders;
    }
	
	
}
