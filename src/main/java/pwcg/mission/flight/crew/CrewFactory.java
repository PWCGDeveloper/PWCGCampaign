package pwcg.mission.flight.crew;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
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
        SquadronMembers squadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAcesNoWounded(campaign.getPersonnelManager().getSquadronPersonnel(squadron.getSquadronId()).getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());
        for (SquadronMember pilot : squadronMembers.getSquadronMemberList())
        {
            crewsForSquadron.put(pilot.getSerialNumber(), pilot);
        }
    }

    // TODO COOP Need to assign player in mission from squadron to crew
    private void ensurePlayerIsAssigned() throws PWCGException
    {
        if (!campaign.getCampaignData().isCoop())
        {
            if (Squadron.isPlayerSquadron(campaign, squadron.getSquadronId()))
            {
                List<SquadronMember> playersForSquadron = campaign.getPersonnelManager().getSquadronPersonnel(squadron.getSquadronId()).getSquadronMembers().getSquadronMemberList();
                for (SquadronMember player : playersForSquadron)
                {
                    if (crewsForSquadron.containsKey(player.getSerialNumber()))
                    {
                        return;
                    }
            
                    for (SquadronMember squadronMemberToBeReplaced : crewsForSquadron.values())
                    {
                        if (!squadronMemberToBeReplaced.isPlayer())
                        {
                            crewsForSquadron.put(player.getSerialNumber(), player);
                            crewsForSquadron.remove(squadronMemberToBeReplaced);
                            break;
                        }
                    }
                }
            }
        }
    }

    public Map<Integer, SquadronMember> getCrewsForSquadron()
    {
        return crewsForSquadron;
    }
}
