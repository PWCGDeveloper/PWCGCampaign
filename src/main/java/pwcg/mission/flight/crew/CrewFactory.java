package pwcg.mission.flight.crew;

import java.util.HashMap;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class CrewFactory
{
    private Campaign campaign;
    private Squadron squadron;
    private Map <Integer, SquadronMember> crewsForSquadron = new HashMap <>();
    
	public CrewFactory(Campaign campaign, Squadron squadron)
	{
        this.campaign = campaign;
        this.squadron = squadron;
	}

    public void createCrews() throws PWCGException 
    {
        createCrewsForSquadron();
        ensurePlayerIsAssigned();
    }

    private void createCrewsForSquadron() throws PWCGException
    {
        Map<Integer, SquadronMember> pilots = campaign.getPersonnelManager().getSquadronPersonnel(squadron.getSquadronId()).getActiveSquadronMembersWithAces().getSquadronMemberCollection();        
        for (SquadronMember pilot : pilots.values())
        {
            crewsForSquadron.put(pilot.getSerialNumber(), pilot);
        }
    }

    private void ensurePlayerIsAssigned() throws PWCGException
    {
        if (squadron.getSquadronId() == campaign.getSquadronId())
        {
            SquadronMember player = campaign.getPlayer();
            if (crewsForSquadron.containsKey(player.getSerialNumber()))
            {
                return;
            }
    
            for (SquadronMember squadronMemberToBeReplaced : crewsForSquadron.values())
            {
                crewsForSquadron.put(player.getSerialNumber(), player);
                crewsForSquadron.remove(squadronMemberToBeReplaced);
                break;
            }
        }
    }

    public Map<Integer, SquadronMember> getCrewsForSquadron()
    {
        return crewsForSquadron;
    }
}
