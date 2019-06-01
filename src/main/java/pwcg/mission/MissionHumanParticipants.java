package pwcg.mission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.squadmember.SquadronMember;

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
		return participatingPlayers.get(squadronId);
	}
	
	public List<Integer> getParticipatingSquadronIds ()
	{
		return new ArrayList<Integer>(participatingPlayers.keySet());
	}
}
