package pwcg.campaign.personnel;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;

public class AiPilotSkillGenerator
{
	public AiSkillLevel calculatePilotQualityByRankAndService(Campaign campaign, Squadron squadron, String rank) throws PWCGException
	{
        ArmedService service = squadron.determineServiceForSquadron(campaign.getDate());

        IRankHelper rankObj = RankFactory.createRankHelper();
        int rankPos = rankObj.getRankPosByService(rank, service);

        int squadronQuality = squadron.determineSquadronSkill(campaign.getDate());
        int serviceQuality = service.getServiceQuality().getQuality(campaign.getDate()).getQualityValue();

    	AiSkillLevel pilotQuality = AiSkillLevel.NOVICE;
		if (rankPos == 4)
		{
			pilotQuality = getJuniorPilotQuality(serviceQuality, squadronQuality);
		}
		if (rankPos == 3)
		{
			pilotQuality = getJuniorOfficerPilotQuality(serviceQuality, squadronQuality);
		}
		if (rankPos == 2)
		{
			pilotQuality = getOfficerPilotQuality(serviceQuality, squadronQuality);
		}
		if (rankPos == 1)
		{
			pilotQuality = getExecutiveOfficerPilotQuality(serviceQuality, squadronQuality);
		}
		if (rankPos == 0)
		{
			pilotQuality = getCommanderPilotQuality(serviceQuality, squadronQuality);
		}
		
		return pilotQuality;
	}

	private AiSkillLevel getCommanderPilotQuality(int serviceQualityValue, int squadronQualityValue)
	{
		AiSkillLevel pilotQuality = AiSkillLevel.COMMON;
		if (squadronQualityValue > 30)
		{
			if (serviceQualityValue > 25)
			{
				pilotQuality = AiSkillLevel.VETERAN;
			}
		}
		if (squadronQualityValue > 60)
		{
			if (serviceQualityValue > 55)
			{
				pilotQuality = AiSkillLevel.ACE;
			}
		}
		return pilotQuality;
	}

	private AiSkillLevel getExecutiveOfficerPilotQuality(int serviceQualityValue, int squadronQualityValue)
	{
		AiSkillLevel pilotQuality = AiSkillLevel.NOVICE;
		if (squadronQualityValue > 30)
		{
			if (serviceQualityValue > 25)
			{
				pilotQuality = AiSkillLevel.COMMON;
			}
		}
		
		if (squadronQualityValue > 50)
		{
			if (serviceQualityValue > 55)
			{
				pilotQuality = AiSkillLevel.VETERAN;
			}
			
			if (serviceQualityValue > 78)
			{
				pilotQuality = AiSkillLevel.ACE;
			}
		}
		
		return pilotQuality;
	}

	private AiSkillLevel getOfficerPilotQuality(int serviceQualityValue, int squadronQualityValue)
	{
		AiSkillLevel pilotQuality = AiSkillLevel.NOVICE;
		if (squadronQualityValue > 40)
		{
			if (serviceQualityValue > 35)
			{
				pilotQuality = AiSkillLevel.COMMON;
			}
		}
		
		if (squadronQualityValue > 60)
		{
			if (serviceQualityValue > 55)
			{
				pilotQuality = AiSkillLevel.VETERAN;
			}
		}
		return pilotQuality;
	}

	private AiSkillLevel getJuniorOfficerPilotQuality(int serviceQualityValue, int squadronQualityValue)
	{
		AiSkillLevel pilotQuality = AiSkillLevel.NOVICE;
		if (squadronQualityValue > 60)
		{
			if (serviceQualityValue > 60)
			{
				pilotQuality = AiSkillLevel.COMMON;
			}
		}
		return pilotQuality;
	}

	private AiSkillLevel getJuniorPilotQuality(int serviceQualityValue, int squadronQualityValue)
	{
		AiSkillLevel pilotQuality = AiSkillLevel.NOVICE;
		if (squadronQualityValue > 70)
		{
			if (serviceQualityValue > 75)
			{
				pilotQuality = AiSkillLevel.COMMON;
			}
		}
		
		return pilotQuality;
	}
}
