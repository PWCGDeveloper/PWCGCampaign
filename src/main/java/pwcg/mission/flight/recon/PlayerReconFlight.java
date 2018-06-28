package pwcg.mission.flight.recon;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.Logger;
import pwcg.core.utils.Logger.LogLevel;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.mcu.McuDeactivate;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;

public class PlayerReconFlight extends ReconFlight
{
	List<ReconPhotoMcuSet> photoReconSets = new ArrayList<>();
	
	protected McuDeactivate deactivateAAAEntity = new McuDeactivate();
	protected McuTimer reactivateAAATimer = new McuTimer();
	
	static public int PLAYER_RECON_MAX_ALT = 6000;
	static public int PLAYER_RECON_MIN_ALT = 1600;

	public PlayerReconFlight() 
	{
		super ();
	}

	public void initialize(
				Mission mission, 
				Campaign campaign, 
				Coordinate targetCoords, 
				Squadron squad, 
	            MissionBeginUnit missionBeginUnit,
				boolean isPlayerFlight) throws PWCGException 
	{
		super.initialize (mission, campaign, FlightTypes.RECON, targetCoords, squad, missionBeginUnit, isPlayerFlight);
	}

	@Override
    protected void createFlightSpecificTargetAssociations() throws PWCGException
	{
        linkWPToPlane(getLeadPlane(), waypointPackage.getWaypointsForLeadPlane());

		if (waypointPackage.getWaypointsForLeadPlane().size() > 0)
		{            
            // TL each waypoint to the next one
            McuWaypoint prevWP = null;
            for (McuWaypoint nextWP : waypointPackage.getWaypointsForLeadPlane())
            {
                if (prevWP != null)
                {
    				if (prevWP.getName().contains(WaypointType.RECON_WAYPOINT.getName()))
    				{
    					ReconPhotoMcuSet photoMcuSet = new ReconPhotoMcuSet();
    					photoMcuSet.createPhotography(prevWP.getPosition());
    					photoMcuSet.createTargetAssociationsForPhotography(prevWP, nextWP);
    					photoReconSets.add(photoMcuSet);
    				}
    				else
    				{
    					prevWP.setTarget(nextWP.getIndex());
    				}
                }
                
				prevWP = nextWP;
			}
		}
	}

	public void finalizeFlight() throws PWCGException  
	{
		super.finalizeFlight();

		if (waypointPackage.getWaypointsForLeadPlane().size() > 0)
		{
			for (McuWaypoint waypoint : waypointPackage.getWaypointsForLeadPlane())
			{
			    Logger.log(LogLevel.DEBUG, waypoint.getName());
				if (waypoint.getName().contains(WaypointType.RECON_WAYPOINT.getName()) ||
				    waypoint.getName().contains(WaypointType.RECON_WAYPOINT.getName()))
				{
					if (waypoint.getPosition().getYPos() > PLAYER_RECON_MAX_ALT)
					{
						waypoint.getPosition().setYPos(PLAYER_RECON_MAX_ALT);
					}
					else if (waypoint.getPosition().getYPos() < PLAYER_RECON_MIN_ALT)
					{
						waypoint.getPosition().setYPos(PLAYER_RECON_MIN_ALT);
					}
				}
			}
		}	
	}

	@Override
	public void write(BufferedWriter writer) throws PWCGException 
	{
		super.write(writer);
		
		for (ReconPhotoMcuSet photoReconSet : photoReconSets)
		{
			photoReconSet.write(writer);
		}
	}

	public List<ReconPhotoMcuSet> getPhotoReconSets() 
	{
		return photoReconSets;
	}
}
