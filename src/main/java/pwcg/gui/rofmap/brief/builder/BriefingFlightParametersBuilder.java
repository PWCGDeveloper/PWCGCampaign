package pwcg.gui.rofmap.brief.builder;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.gui.rofmap.brief.model.BriefingFlightParameters;
import pwcg.gui.rofmap.brief.model.BriefingMapPoint;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.mcu.McuWaypoint;

public class BriefingFlightParametersBuilder
{
	private IFlight playerFlight;
	private BriefingFlightParameters briefingFlightParameters;

	public BriefingFlightParametersBuilder (IFlight playerFlight)
	{
        this.playerFlight = playerFlight;
        briefingFlightParameters = new BriefingFlightParameters();
	}
	
	public BriefingFlightParameters buildBriefParametersContext() throws PWCGException
	{
		addTakeoff();
		setWaypoints();
		addLanding();
		
		return briefingFlightParameters;
	}
	
	private void setWaypoints() throws PWCGException
	{
		McuWaypoint prevWaypoint = null;
		for (McuWaypoint waypoint :  playerFlight.getWaypointPackage().getAllWaypoints())
		{				
		     addPlayerFlightWaypoint(prevWaypoint, waypoint);

		     if (playerFlight.getFlightType() == FlightTypes.ESCORT)
		     {
		         if (waypoint.getWpAction() == WaypointAction.WP_ACTION_RENDEZVOUS)
		         {
		             updateEscortWaypointsOnMap();
		         }
		     }

		     if (waypoint.getWpAction() == WaypointAction.WP_ACTION_TARGET_FINAL)
		     {
		    	 addAttackPoint(playerFlight.getTargetDefinition().getPosition());
		     }
		     		     
		     prevWaypoint = waypoint;
		}
	}

	private void addPlayerFlightWaypoint(McuWaypoint prevWaypoint, McuWaypoint waypoint) throws PWCGException
	{
        BriefingMapPoint briefingMapPoint = BriefingMapPointFactory.waypointToMapPoint(waypoint);
	    briefingFlightParameters.addBriefingMapMapPoints(briefingMapPoint);
	}

	private void addTakeoff() throws PWCGException 
	{
        BriefingMapPoint briefingMapTakeoff = BriefingMapPointFactory.createTakeoff(playerFlight);
        if (briefingMapTakeoff != null)
		{
            BriefingMapPoint briefingMapPoint = BriefingMapPointFactory.createTakeoff(playerFlight);
            briefingFlightParameters.addBriefingMapMapPoints(briefingMapPoint);
		}
	}
	
	private void addLanding() throws PWCGException 
	{
        BriefingMapPoint briefingMapLanding = BriefingMapPointFactory.createLanding(playerFlight);
		if (briefingMapLanding != null)
		{
            BriefingMapPoint briefingMapPoint = BriefingMapPointFactory.createLanding(playerFlight);
            briefingFlightParameters.addBriefingMapMapPoints(briefingMapPoint);
		}
	}
	
	private void addAttackPoint(Coordinate targetLocation) throws PWCGException 
	{
        BriefingMapPoint briefingMapPoint = BriefingMapPointFactory.createAttackPoint(targetLocation);
        briefingFlightParameters.addBriefingMapMapPoints(briefingMapPoint);
	}
	
    private void updateEscortWaypointsOnMap() throws PWCGException
    {
        IFlight escortedByPlayerFlight = playerFlight.getAssociatedFlight();
        if (escortedByPlayerFlight != null)
	    {
	        for (McuWaypoint waypoint : escortedByPlayerFlight.getWaypointPackage().getAllWaypoints())
	        {
	            if (waypoint.getWpAction() == WaypointAction.WP_ACTION_LANDING_APPROACH)
	            {
	                continue;
	            }
	            
	            addEscortPoint(waypoint);
	        }               
	    }
    }
    
    private void addEscortPoint(McuWaypoint escortWaypoint)
	{
        BriefingMapPoint briefingMapPoint = BriefingMapPointFactory.createEscortPoint(escortWaypoint);
        briefingFlightParameters.addBriefingMapMapPoints(briefingMapPoint);
	}
}
