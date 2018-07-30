package pwcg.mission.flight;

import pwcg.campaign.squadron.Squadron;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;

public class FlightInformationFactory
{

    public static FlightInformation buildPlayerFlightInformation(Squadron squadron, Mission mission, FlightTypes flightType, Coordinate targetCoords)
    {
        FlightInformation playerFlightInformation = new FlightInformation(mission.getCampaign());
        playerFlightInformation.setFlightType(flightType);
        playerFlightInformation.setMission(mission);
        playerFlightInformation.setSquadron(squadron);
        playerFlightInformation.setTargetCoords(targetCoords);
        playerFlightInformation.setPlayerFlight(true);
        playerFlightInformation.setEscortForPlayerFlight(false);
        playerFlightInformation.setEscortedByPlayerFlight(false);
        
        return playerFlightInformation;
    }

    public static FlightInformation buildAiFlightInformation(Squadron squadron, Mission mission, FlightTypes flightType, Coordinate targetCoords)
    {
        FlightInformation playerFlightInformation = new FlightInformation(mission.getCampaign());
        playerFlightInformation.setFlightType(flightType);
        playerFlightInformation.setMission(mission);
        playerFlightInformation.setSquadron(squadron);
        playerFlightInformation.setTargetCoords(targetCoords);
        playerFlightInformation.setPlayerFlight(false);
        playerFlightInformation.setEscortForPlayerFlight(false);
        playerFlightInformation.setEscortedByPlayerFlight(false);
        
        return playerFlightInformation;
    }

    public static FlightInformation buildEscortForPlayerFlight(FlightInformation playerFlightInformation, Squadron friendlyFighterSquadron)
    {
        FlightInformation escortFlightInformation = new FlightInformation(playerFlightInformation.getCampaign());
        escortFlightInformation.setFlightType(FlightTypes.ESCORT);
        escortFlightInformation.setMission(escortFlightInformation.getMission());
        escortFlightInformation.setSquadron(friendlyFighterSquadron);
        escortFlightInformation.setTargetCoords(playerFlightInformation.getTargetCoords());
        escortFlightInformation.setPlayerFlight(false);
        escortFlightInformation.setEscortForPlayerFlight(true);
        escortFlightInformation.setEscortedByPlayerFlight(false);
        
        return escortFlightInformation;
    }

}
