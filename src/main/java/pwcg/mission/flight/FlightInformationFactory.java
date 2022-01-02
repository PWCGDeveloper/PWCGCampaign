package pwcg.mission.flight;

import pwcg.campaign.squadron.Company;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.plane.FlightPlaneBuilder;

public class FlightInformationFactory
{
    public static FlightInformation buildFlightInformation(FlightBuildInformation flightBuildInformation, FlightTypes flightType) throws PWCGException
    {
        if (flightBuildInformation.isPlayerFlight())
        {
            return buildPlayerFlightInformation(flightBuildInformation.getMission(), flightBuildInformation.getSquadron(), flightType);
        }
        else
        {
            return buildAiFlightInformation(flightBuildInformation.getMission(), flightBuildInformation.getSquadron(), flightBuildInformation.getNecessaryFlightType(), flightType);
        }
    }
    
    private static FlightInformation buildPlayerFlightInformation(Mission mission, Company squadron, FlightTypes flightType) throws PWCGException
    {    	
        FlightInformation playerFlightInformation = new FlightInformation(mission, NecessaryFlightType.PLAYER_FLIGHT);
        playerFlightInformation.setFlightType(flightType);
        playerFlightInformation.setCampaign(mission.getCampaign());
        playerFlightInformation.setSquadron(squadron);
        playerFlightInformation.setTargetSearchStartLocation(mission.getMissionBorders().getCenter());
        FlightPlaneBuilder.buildPlanes (playerFlightInformation);
        playerFlightInformation.calculateAltitude();
        
        return playerFlightInformation;
    }

    private static FlightInformation buildAiFlightInformation(Mission mission, Company squadron, NecessaryFlightType necessaryFlightType, FlightTypes flightType) throws PWCGException
    {
        FlightInformation aFlightInformation = new FlightInformation(mission, necessaryFlightType);
        aFlightInformation.setFlightType(flightType);
        aFlightInformation.setCampaign(mission.getCampaign());
        aFlightInformation.setSquadron(squadron);
        aFlightInformation.setTargetSearchStartLocation(mission.getMissionBorders().getCenter());
        FlightPlaneBuilder.buildPlanes (aFlightInformation);
        aFlightInformation.calculateAltitude();
        
        return aFlightInformation;
    }
}
