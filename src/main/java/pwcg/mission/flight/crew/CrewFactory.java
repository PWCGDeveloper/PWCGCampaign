package pwcg.mission.flight.crew;

import java.util.HashMap;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.MissionHumanParticipants;
import pwcg.mission.flight.IFlightInformation;

public class CrewFactory
{
    private Campaign campaign;
    private Squadron squadron;
    private MissionHumanParticipants participatingPlayers;
    private Map <Integer, SquadronMember> crewsForSquadron = new HashMap <>();
    
	public CrewFactory(IFlightInformation flightInformation)
	{
        this.campaign = flightInformation.getCampaign();
        this.squadron = flightInformation.getSquadron();
        this.participatingPlayers = flightInformation.getMission().getParticipatingPlayers();
	}

    public Map <Integer, SquadronMember> createCrews() throws PWCGException 
    {
        createCrewsForSquadron();
        ensurePlayerIsAssigned();
        return crewsForSquadron;
    }

    private void createCrewsForSquadron() throws PWCGException
    {
        SquadronMembers squadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAcesNoWounded(
                campaign.getPersonnelManager().getSquadronPersonnel(squadron.getSquadronId()).getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());
        for (SquadronMember pilot : squadronMembers.getSquadronMemberList())
        {
            crewsForSquadron.put(pilot.getSerialNumber(), pilot);
        }
    }

    private void ensurePlayerIsAssigned() throws PWCGException
    {
        for (SquadronMember player : participatingPlayers.getAllParticipatingPlayers())
        {
            addPlayerToMissionIfNeeded(player);
        }
    }

    private void addPlayerToMissionIfNeeded(SquadronMember player)
    {
        if (player.getSquadronId() != squadron.getSquadronId())
        {
            return;
        }
         
        if (crewsForSquadron.containsKey(player.getSerialNumber()))
        {
            return;
        }
         
        for (SquadronMember squadronMemberToBeReplaced : crewsForSquadron.values())
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
