package pwcg.mission.flight.escort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;

public class VirtualEscortFlightBuilder
{
    public Flight createVirtualEscortFlight(Flight escortedFlight) throws PWCGException 
    {
        List<Role> fighterRole = new ArrayList<Role>(Arrays.asList(Role.ROLE_FIGHTER));
        List<Squadron> friendlyFighterSquadrons = PWCGContext.getInstance().getSquadronManager().getNearestSquadronsByRole(
                escortedFlight.getCampaign(),
                escortedFlight.getPosition(),
                1,
                50000.0,
                fighterRole,
                escortedFlight.getSquadron().determineSquadronCountry(escortedFlight.getCampaign().getDate()).getSide(), 
                escortedFlight.getCampaign().getDate());
        
        if (friendlyFighterSquadrons != null && friendlyFighterSquadrons.size() > 0)
        {
            int index = RandomNumberGenerator.getRandom(friendlyFighterSquadrons.size());
            Squadron escortFighterSquadron = friendlyFighterSquadrons.get(index);
            
            MissionBeginUnit missionBeginUnitEscort = new MissionBeginUnit(escortedFlight.getPosition());

            FlightInformation escortFlightInformation = FlightInformationFactory.buildAiFlightInformation(
                    escortFighterSquadron, escortedFlight.getMission(), FlightTypes.ESCORT);
            VirtualEscortFlight virtualEscortFlight = new VirtualEscortFlight(escortFlightInformation, missionBeginUnitEscort, escortedFlight);
            virtualEscortFlight.createUnitMission();
            virtualEscortFlight.createEscortPositionCloseToFirstWP();
            return virtualEscortFlight;
        }
        
        return null;
    }

}
