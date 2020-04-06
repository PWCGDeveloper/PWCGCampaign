package pwcg.mission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.MathUtils;

public class MissionHumanParticipants 
{
	private Map<Integer, List<SquadronMember>> participatingPlayers = new HashMap<>();

	public void addSquadronMembers(List<SquadronMember> participatingPlayers)
	{
		for (SquadronMember participatingPlayer : participatingPlayers)
		{
			addSquadronMember(participatingPlayer);
		}
	}

	public void addSquadronMember(SquadronMember participatingPlayer)
	{
		if (!participatingPlayers.containsKey(participatingPlayer.getSquadronId()))
		{
			List<SquadronMember> participatingPlayersForSquadron = new ArrayList<>();
			participatingPlayers.put(participatingPlayer.getSquadronId(), participatingPlayersForSquadron);
		}
		
		List<SquadronMember> participatingPlayersForSquadron = participatingPlayers.get(participatingPlayer.getSquadronId());
		participatingPlayersForSquadron.add(participatingPlayer);
	}
	
	public List<SquadronMember> getParticipatingPlayersForSquadron (int squadronId)
	{
		List<SquadronMember> participatingPlayersForSquadron = participatingPlayers.get(squadronId);
		if (participatingPlayersForSquadron == null)
		{
			return new ArrayList<>();
		}
		return participatingPlayersForSquadron;
	}
	
	public List<Integer> getParticipatingSquadronIds ()
	{
		return new ArrayList<Integer>(participatingPlayers.keySet());
	}
	
	public boolean isSquadronInMission(Squadron squadron)
	{
	       return participatingPlayers.containsKey(squadron.getSquadronId());
	}
	
	public boolean isPlayerInMission(Squadron squadron, SquadronMember player)
	{
		List<SquadronMember> playersForSquadron =  getParticipatingPlayersForSquadron(squadron.getSquadronId());
		for (SquadronMember playerForSquadron : playersForSquadron)
		{
			if (playerForSquadron.getSerialNumber() == player.getSerialNumber())
			{
				return true;
			}
		}
		
		return false;
	}
    
    public List<SquadronMember> getAllParticipatingPlayers()
    {
        List<SquadronMember> allParticipatingPlayers = new ArrayList<>();
        for (List<SquadronMember> playersForSquadron : participatingPlayers.values())
        {
            allParticipatingPlayers.addAll(playersForSquadron);
        }
        
        return allParticipatingPlayers;
    }
    
    public double getPlayerDistanceToTarget(Mission mission) throws PWCGException
    {
        double totalPlayerDistanceToTarget = 0.0;
        for (int playersSquadronId : participatingPlayers.keySet())
        {
            Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(playersSquadronId);
            totalPlayerDistanceToTarget += MathUtils.calcDist(squadron.determineCurrentPosition(mission.getCampaign().getDate()), mission.getMissionBorders().getCenter());
        }
        
        double averagePlayerDistanceToTarget = totalPlayerDistanceToTarget / participatingPlayers.size();
        return averagePlayerDistanceToTarget;
    }

}
