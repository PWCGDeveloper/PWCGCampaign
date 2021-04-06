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
            return buildAFlightInformation(flightBuildInformation.getMission(), flightBuildInformation.getSquadron(), flightType);
        }
    }
    
    private static FlightInformation buildPlayerFlightInformation(Mission mission, Squadron squadron, FlightTypes flightType) throws PWCGException
    {    	
        FlightInformation playerFlightInformation = new FlightInformation(mission);
        playerFlightInformation.setFlightType(flightType);
        playerFlightInformation.setMission(mission);
        playerFlightInformation.setCampaign(mission.getCampaign());
        playerFlightInformation.setSquadron(squadron);
        playerFlightInformation.setPlayerFlight(true);
        playerFlightInformation.setEscortForPlayerFlight(false);
        playerFlightInformation.setEscortedByPlayerFlight(false);
        playerFlightInformation.setTargetSearchStartLocation(mission.getMissionBorders().getCenter());
        FlightPlaneBuilder.buildPlanes (playerFlightInformation);
        playerFlightInformation.calculateAltitude();
        
        return playerFlightInformation;
    }

    private static FlightInformation buildAFlightInformation(Mission mission, Squadron squadron, FlightTypes flightType) throws PWCGException
    {
        FlightInformation aFlightInformation = new FlightInformation(mission);
        aFlightInformation.setFlightType(flightType);
        aFlightInformation.setMission(mission);
        aFlightInformation.setCampaign(mission.getCampaign());
        aFlightInformation.setSquadron(squadron);
        aFlightInformation.setPlayerFlight(false);
        aFlightInformation.setEscortForPlayerFlight(false);
        aFlightInformation.setEscortedByPlayerFlight(false);
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
