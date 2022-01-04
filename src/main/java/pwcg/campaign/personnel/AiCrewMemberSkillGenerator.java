package pwcg.campaign.personnel;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.company.Company;
import pwcg.campaign.factory.RankFactory;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;

public class AiCrewMemberSkillGenerator
{
	public AiSkillLevel calculateCrewMemberQualityByRankAndService(Campaign campaign, Company squadron, String rank) throws PWCGException
	{
        ArmedService service = squadron.determineServiceForSquadron(campaign.getDate());

        IRankHelper rankObj = RankFactory.createRankHelper();
        int rankPos = rankObj.getRankPosByService(rank, service);

        int squadronQuality = squadron.determineSquadronSkill(campaign.getDate());
        int serviceQuality = service.getServiceQuality().getQuality(campaign.getDate()).getQualityValue();

    	AiSkillLevel crewMemberQuality = AiSkillLevel.NOVICE;
		if (rankPos == 4)
		{
			crewMemberQuality = getJuniorCrewMemberQuality(serviceQuality, squadronQuality);
		}
		if (rankPos == 3)
		{
			crewMemberQuality = getJuniorOfficerCrewMemberQuality(serviceQuality, squadronQuality);
		}
		if (rankPos == 2)
		{
			crewMemberQuality = getOfficerCrewMemberQuality(serviceQuality, squadronQuality);
		}
		if (rankPos == 1)
		{
			crewMemberQuality = getExecutiveOfficerCrewMemberQuality(serviceQuality, squadronQuality);
		}
		if (rankPos == 0)
		{
			crewMemberQuality = getCommanderCrewMemberQuality(serviceQuality, squadronQuality);
		}
		
		return crewMemberQuality;
	}

	private AiSkillLevel getCommanderCrewMemberQuality(int serviceQualityValue, int squadronQualityValue)
	{
		AiSkillLevel crewMemberQuality = AiSkillLevel.COMMON;
		if (squadronQualityValue > 30)
		{
			if (serviceQualityValue > 25)
			{
				crewMemberQuality = AiSkillLevel.VETERAN;
			}
		}
		if (squadronQualityValue > 60)
		{
			if (serviceQualityValue > 55)
			{
				crewMemberQuality = AiSkillLevel.ACE;
			}
		}
		return crewMemberQuality;
	}

	private AiSkillLevel getExecutiveOfficerCrewMemberQuality(int serviceQualityValue, int squadronQualityValue)
	{
		AiSkillLevel crewMemberQuality = AiSkillLevel.NOVICE;
		if (squadronQualityValue > 30)
		{
			if (serviceQualityValue > 25)
			{
				crewMemberQuality = AiSkillLevel.COMMON;
			}
		}
		
		if (squadronQualityValue > 50)
		{
			if (serviceQualityValue > 55)
			{
				crewMemberQuality = AiSkillLevel.VETERAN;
			}
		}
		
		return crewMemberQuality;
	}

	private AiSkillLevel getOfficerCrewMemberQuality(int serviceQualityValue, int squadronQualityValue)
	{
		AiSkillLevel crewMemberQuality = AiSkillLevel.NOVICE;
		if (squadronQualityValue > 40)
		{
			if (serviceQualityValue > 35)
			{
				crewMemberQuality = AiSkillLevel.COMMON;
			}
		}
		
		if (squadronQualityValue > 60)
		{
            if (serviceQualityValue > 78)
			{
				crewMemberQuality = AiSkillLevel.VETERAN;
			}
		}
		return crewMemberQuality;
	}

	private AiSkillLevel getJuniorOfficerCrewMemberQuality(int serviceQualityValue, int squadronQualityValue)
	{
		AiSkillLevel crewMemberQuality = AiSkillLevel.NOVICE;
		if (squadronQualityValue > 60)
		{
			if (serviceQualityValue > 60)
			{
				crewMemberQuality = AiSkillLevel.COMMON;
			}
		}
		return crewMemberQuality;
	}

	private AiSkillLevel getJuniorCrewMemberQuality(int serviceQualityValue, int squadronQualityValue)
	{
		AiSkillLevel crewMemberQuality = AiSkillLevel.NOVICE;
		if (squadronQualityValue > 70)
		{
			if (serviceQualityValue > 75)
			{
				crewMemberQuality = AiSkillLevel.COMMON;
			}
		}
		
		return crewMemberQuality;
	}
}
