package pwcg.mission;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class TargetSide 
{
	public static Side ambientTargetSide(Campaign campaign) throws PWCGException
	{
		if (campaign.getCampaignData().isCoop())
		{
			return chooseRandomSide();
		}
		else
		{
			return determineSinglePlayerCampaignSide(campaign);
		}		
	}
	
	private static Side determineSinglePlayerCampaignSide(Campaign campaign) throws PWCGException
	{
		SquadronMembers allPlayers = campaign.getPersonnelManager().getAllActivePlayers();
		return allPlayers.getSquadronMemberList().get(0).determineCountry(campaign.getDate()).getSide().getOppositeSide();
	}
	
	private static Side chooseRandomSide()
	{
		Side ambientTargetSide = Side.ALLIED;
		int diceRoll = RandomNumberGenerator.getRandom(100);
		if (diceRoll < 50)
		{
			ambientTargetSide = Side.AXIS;
		}
		
		return ambientTargetSide;
	}
}
