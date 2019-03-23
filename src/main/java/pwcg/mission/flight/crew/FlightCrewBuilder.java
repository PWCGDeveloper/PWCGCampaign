package pwcg.mission.flight.crew;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberSorter;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class FlightCrewBuilder
{
    private Campaign campaign;
    private Squadron squadron;

    private Map <Integer, SquadronMember> assignedCrewMap = new HashMap <>();
    private Map <Integer, SquadronMember> unassignedCrewMap = new HashMap <>();
    
	public FlightCrewBuilder(Campaign campaign, Squadron squadron)
	{
        this.campaign = campaign;
        this.squadron = squadron;
	}

    public List<SquadronMember> createCrewAssignmentsForFlight(int numCrewNeeded) throws PWCGException 
    {
        CrewFactory crewFactory = new CrewFactory(campaign, squadron);
        crewFactory.createCrews();
        unassignedCrewMap = crewFactory.getCrewsForSquadron();
        
        assignPlayerToCrew();
        assignAiPilotsToPlanes(numCrewNeeded);
        
        return sortCrewsByRank();
    }

    private void assignPlayerToCrew() throws PWCGException
    {
        if (Squadron.isPlayerSquadron(campaign, squadron.getSquadronId()))
        {
            SquadronMember playerCrew = null;
            for (SquadronMember pilot : unassignedCrewMap.values())
            {
                if (pilot.isPlayer())
                {
                    playerCrew = pilot;
                    break;
                }
            }

            if (playerCrew != null)
            {
                assignedCrewMap.put(playerCrew.getSerialNumber(), playerCrew);
                unassignedCrewMap.remove(playerCrew.getSerialNumber());
            }
            else
            {
                throw new PWCGException("Unable to find player crew in players squadron");
            }
        }
    }

    private void assignAiPilotsToPlanes(int numCrewNeeded) throws PWCGException
    {
        while (assignedCrewMap.size() < numCrewNeeded)
        {
            List<Integer> unassignedCrewSerialNumbers = new ArrayList<>(unassignedCrewMap.keySet());
            int crewIndex = RandomNumberGenerator.getRandom(unassignedCrewSerialNumbers.size());
            int selectedSerialNumber = unassignedCrewSerialNumbers.get(crewIndex);
            SquadronMember crewToAssign = unassignedCrewMap.get(selectedSerialNumber);
            assignedCrewMap.put(crewToAssign.getSerialNumber(), crewToAssign);
            unassignedCrewMap.remove(crewToAssign.getSerialNumber());
        }
    }
    
    private List<SquadronMember> sortCrewsByRank() throws PWCGException
    {
        return SquadronMemberSorter.sortSquadronMembers(campaign, assignedCrewMap);
    }
}
