package pwcg.mission.playerunit.crew;

import java.util.HashMap;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.core.exception.PWCGException;
import pwcg.mission.MissionHumanParticipants;
import pwcg.mission.playerunit.PlayerUnitInformation;

public class CrewFactory
{
    private Campaign campaign;
    private Company squadron;
    private MissionHumanParticipants participatingPlayers;
    private Map <Integer, CrewMember> crewsForSquadron = new HashMap <>();
    
	public CrewFactory(PlayerUnitInformation unitInformation)
	{
        this.campaign = unitInformation.getCampaign();
        this.squadron = unitInformation.getCompany();
        this.participatingPlayers = unitInformation.getMission().getParticipatingPlayers();
	}

    public Map <Integer, CrewMember> createCrews() throws PWCGException 
    {
        createCrewsForSquadron();
        ensurePlayerIsAssigned();
        return crewsForSquadron;
    }

    private void createCrewsForSquadron() throws PWCGException
    {
        CrewMembers companyMembers = CrewMemberFilter.filterActiveAIAndPlayerAndAcesNoWounded(
                campaign.getPersonnelManager().getCompanyPersonnel(squadron.getCompanyId()).getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());
        for (CrewMember crewMember : companyMembers.getCrewMemberList())
        {
            crewsForSquadron.put(crewMember.getSerialNumber(), crewMember);
        }
    }

    private void ensurePlayerIsAssigned() throws PWCGException
    {
        for (CrewMember player : participatingPlayers.getAllParticipatingPlayers())
        {
            addPlayerToMissionIfNeeded(player);
        }
    }

    private void addPlayerToMissionIfNeeded(CrewMember player)
    {
        if (player.getCompanyId() != squadron.getCompanyId())
        {
            return;
        }
         
        if (crewsForSquadron.containsKey(player.getSerialNumber()))
        {
            return;
        }
         
        for (CrewMember squadronMemberToBeReplaced : crewsForSquadron.values())
        {
            if (!squadronMemberToBeReplaced.isPlayer())
            {
                crewsForSquadron.put(player.getSerialNumber(), player);
                crewsForSquadron.remove(squadronMemberToBeReplaced.getSerialNumber());
                break;
            }
        }
    }
}
