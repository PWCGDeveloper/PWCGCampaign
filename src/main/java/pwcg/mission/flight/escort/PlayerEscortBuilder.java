package pwcg.mission.flight.escort;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightInformationFactory;

public class PlayerEscortBuilder
{
    public Flight createEscortForPlayerFlight(Flight playerFlight) throws PWCGException 
    {
        FlightInformation playerFlightInformation = playerFlight.getFlightInformation();
        
        Squadron friendlyFighterSquadron = PWCGContextManager.getInstance().getSquadronManager().getSquadronByProximityAndRoleAndSide(
                        playerFlightInformation.getCampaign(), 
                        playerFlightInformation.getSquadron().determineCurrentPosition(playerFlightInformation.getCampaign().getDate()), 
                        Role.ROLE_FIGHTER, 
                        playerFlightInformation.getSquadron().determineSquadronCountry(playerFlightInformation.getCampaign().getDate()).getSide());

        if (friendlyFighterSquadron != null)
        {
            MissionBeginUnit missionBeginUnitEscort = new MissionBeginUnit(playerFlightInformation.getDepartureAirfield().getPosition());
    
            FlightInformation escortFlightInformation = FlightInformationFactory.buildEscortForPlayerFlight(playerFlight.getFlightInformation(), friendlyFighterSquadron);
            EscortForPlayerFlight escortForPlayerFlight = new EscortForPlayerFlight(escortFlightInformation, missionBeginUnitEscort, playerFlight);
            escortForPlayerFlight.createUnitMission();       
            return escortForPlayerFlight;
        }
        
        return null;
    }
}
