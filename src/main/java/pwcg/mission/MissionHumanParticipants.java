package pwcg.mission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.api.Side;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.MathUtils;

public class MissionHumanParticipants 
{
	private Map<Integer, List<CrewMember>> participatingPlayers = new HashMap<>();

	public void addCrewMembers(List<CrewMember> participatingPlayers)
	{
		for (CrewMember participatingPlayer : participatingPlayers)
		{
			addCrewMember(participatingPlayer);
		}
	}

	public void addCrewMember(CrewMember participatingPlayer)
	{
		if (!participatingPlayers.containsKey(participatingPlayer.getCompanyId()))
		{
			List<CrewMember> participatingPlayersForSquadron = new ArrayList<>();
			participatingPlayers.put(participatingPlayer.getCompanyId(), participatingPlayersForSquadron);
		}
		
		List<CrewMember> participatingPlayersForSquadron = participatingPlayers.get(participatingPlayer.getCompanyId());
		participatingPlayersForSquadron.add(participatingPlayer);
	}
	
	public List<CrewMember> getParticipatingPlayersForSquadron (int squadronId)
	{
		List<CrewMember> participatingPlayersForSquadron = participatingPlayers.get(squadronId);
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
	
	public boolean isSquadronInMission(Company squadron)
	{
	       return participatingPlayers.containsKey(squadron.getCompanyId());
	}
	
	public boolean isPlayerInMission(Company squadron, CrewMember player)
	{
		List<CrewMember> playersForSquadron =  getParticipatingPlayersForSquadron(squadron.getCompanyId());
		for (CrewMember playerForSquadron : playersForSquadron)
		{
			if (playerForSquadron.getSerialNumber() == player.getSerialNumber())
			{
				return true;
			}
		}
		
		return false;
	}
    
    public List<CrewMember> getAllParticipatingPlayers()
    {
        List<CrewMember> allParticipatingPlayers = new ArrayList<>();
        for (List<CrewMember> playersForSquadron : participatingPlayers.values())
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
            Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(playersSquadronId);
            totalPlayerDistanceToTarget += MathUtils.calcDist(squadron.determineCurrentPosition(mission.getCampaign().getDate()), mission.getMissionBorders().getCenter());
        }
        
        double averagePlayerDistanceToTarget = totalPlayerDistanceToTarget / participatingPlayers.size();
        return averagePlayerDistanceToTarget;
    }

    public List<Company> getMissionPlayerSquadrons() throws PWCGException
    {
        Map<Integer, Company> playerSquadronsMap = new HashMap<>();
        for (int playersSquadronId : participatingPlayers.keySet())
        {
            Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(playersSquadronId);
            playerSquadronsMap.put(playersSquadronId, squadron);
        }
        
        List<Company> squadrons = new ArrayList<>(playerSquadronsMap.values());
        return squadrons;
    }

    public List<Side> getMissionPlayerSides() throws PWCGException
    {
        Map<Side, Side> playerSideMap = new HashMap<>();
        for (int playersSquadronId : participatingPlayers.keySet())
        {
            Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(playersSquadronId);
            playerSideMap.put(squadron.determineSide(), squadron.determineSide());
        }
        
        List<Side> playerSides = new ArrayList<>(playerSideMap.values());
        return playerSides;
    }
}
