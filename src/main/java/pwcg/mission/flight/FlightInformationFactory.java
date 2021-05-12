package pwcg.mission.flight;

import pwcg.campaign.squadron.Squadron;
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
            return buildAiFlightInformation(flightBuildInformation.getMission(), flightBuildInformation.getSquadron(), flightType);
        }
    }
    
    private static FlightInformation buildPlayerFlightInformation(Mission mission, Squadron squadron, FlightTypes flightType) throws PWCGException
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

    private static FlightInformation buildAiFlightInformation(Mission mission, Squadron squadron, FlightTypes flightType) throws PWCGException
    {
        FlightInformation aFlightInformation = new FlightInformation(mission, NecessaryFlightType.NONE);
        aFlightInformation.setFlightType(flightType);
        aFlightInformation.setCampaign(mission.getCampaign());
        aFlightInformation.setSquadron(squadron);
        aFlightInformation.setTargetSearchStartLocation(mission.getMissionBorders().getCenter());
        FlightPlaneBuilder.buildPlanes (aFlightInformation);
        aFlightInformation.calculateAltitude();

        if (flightType == FlightTypes.SCRAMBLE)
        {
            aFlightInformation.setAiTriggeredTakeoff(true);
        }
        
        return aFlightInformation;
    }
}
