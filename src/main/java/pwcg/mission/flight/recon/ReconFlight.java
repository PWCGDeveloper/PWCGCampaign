package pwcg.mission.flight.recon;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.mcu.McuWaypoint;

public class ReconFlight extends Flight
{
    ReconFlightTypes reconFlightType = ReconFlightTypes.RECON_FLIGHT_FRONT;
    
    public static enum ReconFlightTypes
    {
        RECON_FLIGHT_FRONT,
        RECON_FLIGHT_TRANSPORT,
        RECON_FLIGHT_AIRFIELD,
    }
    
	public ReconFlight() 
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
	public int calcNumPlanes() throws PWCGException 
	{
		ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
		
		int ReconMinimum = configManager.getIntConfigParam(ConfigItemKeys.ReconMinimumKey);
		int ReconAdditional = configManager.getIntConfigParam(ConfigItemKeys.ReconAdditionalKey) + 1;
		numPlanesInFlight = ReconMinimum + RandomNumberGenerator.getRandom(ReconAdditional);
		
        return modifyNumPlanes(numPlanesInFlight);
	}

	@Override
	public List<McuWaypoint> createWaypoints(Mission mission, Coordinate startPosition) throws PWCGException 
	{
	    ReconWaypoints waypoints = null;
	    
	    if (reconFlightType == ReconFlightTypes.RECON_FLIGHT_TRANSPORT)
	    {
	        waypoints = new ReconWaypointsTransport(startPosition, targetCoords, this, mission);
	    }
	    else if (reconFlightType == ReconFlightTypes.RECON_FLIGHT_AIRFIELD)
        {
	        waypoints = new ReconWaypointsAirfield(startPosition, targetCoords, this, mission);
        }
	    else
	    {
            waypoints = new ReconWaypointsFront(startPosition, targetCoords, this, mission);
	    }
		
		return waypoints.createWaypoints();
	}

	public String getMissionObjective() throws PWCGException 
	{
	    String objective = "";

        String objectiveName =  formMissionObjectiveLocation(targetCoords.copy());
	    if (reconFlightType == ReconFlightTypes.RECON_FLIGHT_TRANSPORT)
	    {
	        if (!objectiveName.isEmpty())
            {
                objective = "Perform reconnaissance at the specified transport hubs near " + objectiveName + 
                                ".  Photograph any troop movements or other items of interest.";
            }
            else
            {
                objective = "Perform reconnaissance at the specified transport hubs" + 
                                ".  Photograph any troop movements or other items of interest.";                
            }
	    }
	    else if (reconFlightType == ReconFlightTypes.RECON_FLIGHT_AIRFIELD)
	    {
	        if (!objectiveName.isEmpty())
            {
                objective = "Perform reconnaissance at the specified airfields near " + objectiveName + 
                                ".  Photograph any aerial activity.";
            }
            else
            {
                objective = "Perform reconnaissance at the specified airfields" + 
                                ".  Photograph any aerial activity.";
            }
	    }
	    else
	    {
	        if (!objectiveName.isEmpty())
	        {
	            objective = "Perform reconnaissance near " + objectiveName + 
	                            ".  Photograph any troop concentrations or other items of interest.";
	        }
	        else
	        {
	            objective = "Perform reconnaissance at the specified front location.  " + 
	                            "Photograph any troop concentrations or other items of interest.";	            
	        }
	    }
	    
	    return objective;
	}

    @Override
    protected void createFlightSpecificTargetAssociations() throws PWCGException
    {
        this.createSimpleTargetAssociations();
    }

    public ReconFlightTypes getReconFlightType()
    {
        return reconFlightType;
    }

    public void setReconFlightType(ReconFlightTypes reconFlightType)
    {
        this.reconFlightType = reconFlightType;
    }

}
