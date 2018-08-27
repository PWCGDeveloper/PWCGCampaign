package pwcg.mission.flight;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.escort.EscortForPlayerFlight;

public class PlayerEscortBuilder
{
    public Flight createEscortForPlayerFlight(Flight playerFlight) throws PWCGException 
    {
        FlightInformation playerFlightInformation = playerFlight.getFlightInformation();
        
        Squadron friendlyFighterSquadron = PWCGContextManager.getInstance().getSquadronManager().getNearbyFriendlySquadronByRole(
                playerFlightInformation.getCampaign(),
                playerFlightInformation.getSquadron().determineSquadronCountry(playerFlightInformation.getCampaign().getDate()), 
                Role.ROLE_FIGHTER);
        
        if (friendlyFighterSquadron != null)
        {
            MissionBeginUnit missionBeginUnitEscort = new MissionBeginUnit();
            missionBeginUnitEscort.initialize(playerFlightInformation.getDepartureAirfield().getPosition());
    
            FlightInformation escortFlightInformation = FlightInformationFactory.buildEscortForPlayerFlight(playerFlight.getFlightInformation(), friendlyFighterSquadron);
            EscortForPlayerFlight escortForPlayerFlight = new EscortForPlayerFlight(escortFlightInformation, missionBeginUnitEscort, playerFlight);
            escortForPlayerFlight.createUnitMission();       
            return escortForPlayerFlight;
        }
        
        return null;
    }
}
