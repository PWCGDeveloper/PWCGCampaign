package pwcg.gui.rofmap.debrief;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogBase;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogDamage;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTurret;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogUnknown;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogWaypoint;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.Victory;
import pwcg.campaign.crewmember.VictoryBuilder;
import pwcg.campaign.crewmember.VictoryDescription;
import pwcg.campaign.tank.TankType;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.rofmap.MapPanelBase;
import pwcg.gui.rofmap.debrief.DebriefMapPoint.EventTypes;

public class DebriefMapPanel  extends MapPanelBase 
{
	private static final long serialVersionUID = 1L;

	
	private DebriefMapGUI parent = null;
	private List <DebriefMapPoint> eventPoints = new ArrayList<DebriefMapPoint>();
	private DebriefStates debriefState = DebriefStates.PAUSE;
	private int atEvent = -1;

	enum DebriefStates
	{
	    PAUSE,
        NEXT
	}

	public DebriefMapPanel(DebriefMapGUI parent) throws PWCGException 
	{
		super(parent);

		this.parent = parent;
	}

	private void draw() 
	{

		if (debriefState == DebriefStates.NEXT)
		{
			if (atEvent < (eventPoints.size() - 1))
			{
				++atEvent;
			}
			else if (atEvent < 0)
			{
				atEvent = 0;
			}
		}       
		else if (debriefState == DebriefStates.PAUSE)
		{
			debriefState = DebriefStates.PAUSE;
		}

		refresh();
	}

	public void zoomChanged()
	{
		
	}

	public void paintComponent(Graphics g)
	{
		try
		{
			paintBaseMapWithMajorGroups(g);
			
	    	drawEvents(g);
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
		}
	}

	private void drawEvents(Graphics g) 
	{
		String eventText = "";
		if(atEvent >= 0)
		{
			for (int i = 0; i < atEvent + 1; ++i)
			{
	
		        Graphics2D g2 = (Graphics2D) g;
		        g2.setStroke(new BasicStroke(3));
		        int size = 8;
		
		    	DebriefMapPoint debriefPoint= eventPoints.get(i);
		
		    	if (debriefPoint.eventType  == EventTypes.DAMAGE)
		    	{
		    		g.setColor(Color.PINK);
		    		size = 10;
			    	eventText += debriefPoint.desc + "\n";	    		
		    	}
		
		    	if (debriefPoint.eventType  == EventTypes.CRASH)
		    	{
		    		g.setColor(Color.RED);
		    		size = 20;
			    	eventText += debriefPoint.desc + "\n";
		    	}
		
		
		    	if (debriefPoint.eventType  == EventTypes.PILOT || debriefPoint.eventType  == EventTypes.WP)
		    	{
		    		g.setColor(Color.BLUE);
		    		size = 10;
		    	}
		
		    	Point point = super.coordinateToPoint(debriefPoint.coord);

		    	Ellipse2D.Double circle = new Ellipse2D.Double(point.x - 4, point.y - 4, size, size);
		    	g2.fill(circle);
		    	
			}
		}
		
		parent.setText(eventText);
	}
	

	public Point upperLeft()
	{
		Point upperLeft = new Point();
		
		upperLeft.x = 10000000;
		upperLeft.y = 10000000;
		for (DebriefMapPoint mapPoint: eventPoints)
		{
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

	public Dimension getMapSize()
	{
		Dimension mapSize = new Dimension();
		mapSize.height =  image.getHeight(null);
		mapSize.width =  image.getWidth(null);
		return mapSize;
	}


	public void createMapEvents(List<LogBase> logEvents) throws PWCGException
    {
	    eventPoints.clear();
	    atEvent = -1;
	    
        LogDamage lastDamageEvent = null;
        for (LogBase event : logEvents)
        {
            if (event instanceof LogDamage)
            {
                LogDamage thisDamageEvent = (LogDamage)event;
                
                if (lastDamageEvent == null || 
                    !(thisDamageEvent.getVictim().getId().equals(lastDamageEvent.getVictim().getId())))
                {
                    addEvent(event);
                    lastDamageEvent = thisDamageEvent;
                }
            }
            else
            {
                addEvent(event);
            }
        }
    }

	private void addEvent(LogBase event) throws PWCGException 
	{
		DebriefMapPoint mapPoint = new DebriefMapPoint();
		
		if (event instanceof LogDamage)
		{
		    if (parent.displayMaxInfo())
		    {
		        createDamagedDisplay(event, mapPoint);
		    }
		}
		else if (event instanceof LogVictory)
		{
			createDestroyedDisplay(event, mapPoint);
		}
		else if (event instanceof LogWaypoint)
		{
			addWaypointReachedEvent(event, mapPoint);
		}
	}

    private void addWaypointReachedEvent(LogBase event, DebriefMapPoint mapPoint)
    {
        LogWaypoint wpEvent = (LogWaypoint)event;
        mapPoint.eventType = EventTypes.WP;
        mapPoint.coord = wpEvent.getLocation().copy();
        mapPoint.desc = " Location reached" + "\n";
        eventPoints.add(mapPoint);
    }

    private void createDestroyedDisplay(LogBase event, DebriefMapPoint mapPoint) throws PWCGException
    {
        LogVictory victoryEvent = (LogVictory)event;
        if (!parent.displayMaxInfo())
        {
            TankType victimVehicle = PWCGContext.getInstance().getTankTypeFactory().createTankTypeByAnyName(victoryEvent.getVictim().getVehicleType());
            TankType victorVehicle = PWCGContext.getInstance().getTankTypeFactory().createTankTypeByAnyName(victoryEvent.getVictor().getVehicleType());
            if (victimVehicle == null && victorVehicle == null)
            {
                return;
            }
        }
        
        mapPoint.eventType = EventTypes.CRASH;
        mapPoint.coord = victoryEvent.getLocation().copy();
        
        String displayVictim = victoryEvent.getVictim().getName();
        
        if (displayVictim.length() > 0)
        {
            Campaign campaign = PWCGContext.getInstance().getCampaign();
            
            VictoryBuilder victoryBuilder = new VictoryBuilder(campaign);
            Victory victory = victoryBuilder.buildVictory(campaign.getDate(), victoryEvent);

            VictoryDescription victoryDescription = new VictoryDescription(campaign, victory);
            String victoryDescriptionText = victoryDescription.createVictoryDescription();
            mapPoint.desc = victoryDescriptionText  + "\n\n";
            eventPoints.add(mapPoint);
        }
    }

    private void createDamagedDisplay(LogBase event, DebriefMapPoint mapPoint) throws PWCGException
    {
        LogDamage damageEvent = (LogDamage)event;
        mapPoint.eventType = EventTypes.DAMAGE;
        mapPoint.coord = damageEvent.getLocation().copy();
        
        String displayVictim = damageEvent.getVictim().getName();
        String displayVictor = "Unknown";
        if (damageEvent.getVictor() instanceof LogTurret)
        {
            LogTurret logTurret = (LogTurret)damageEvent.getVictor();
            displayVictor = "a gunner flying with " + logTurret.getParent().getName();
        }
        else if (!(damageEvent.getVictor() instanceof LogUnknown))
        {
        	displayVictor = damageEvent.getVictor().getName();
        }
        
        if (!displayVictor.isEmpty() && !displayVictor.equals("Unknown"))
        {
            mapPoint.desc = displayVictim + " damaged by " + displayVictor + "\n";
        }
        else
        {
            mapPoint.desc = displayVictim + " damaged" + "\n";
        }
        
        eventPoints.add(mapPoint);
    }

	public DebriefStates getDebriefState() 
	{
		return debriefState;
	}

	public int setDebriefState(DebriefStates debriefState) 
	{
    	int waitTime = -1;

		this.debriefState = debriefState;
		
		try
		{
			draw();
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
		}
		
		// Speed up the path
		if (atEvent >= 0)
		{
		    if (eventPoints.size() > 0)
		    {
    	    	DebriefMapPoint debriefPoint= eventPoints.get(atEvent);
    			
    	    	if (debriefPoint.eventType  == EventTypes.PILOT || debriefPoint.eventType  == EventTypes.WP)
    	    	{
    	    		waitTime = 100; 
    	    	}
		    }
		}
		
		return waitTime;
	}

	public List<DebriefMapPoint> getEventPoints() 
	{
		return eventPoints;
	}

	public int getAtEvent() 
	{
		return atEvent;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) 
	{		
	}

	@Override
	public void mouseMovedCallback(MouseEvent e) 
	{		
	}

    @Override
    public void rightClickCallback(MouseEvent e) 
    {
    }

    @Override
    public void centerClickCallback(MouseEvent e) 
    {
    }

	@Override
	public void rightClickReleasedCallback(MouseEvent e)  
	{		
	}
}
