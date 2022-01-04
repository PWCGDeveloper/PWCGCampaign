package pwcg.dev.utils;

import java.util.Date;
import java.util.List;

import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.tank.PwcgRoleCategory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class FighterSquadronSkillAnalyzer 
{
	static public void main (String[] args)
	{
		try
		{
			FighterSquadronSkillAnalyzer skillAnalyzer = new FighterSquadronSkillAnalyzer();
			
			Date startDate = DateUtils.getEndOfWar();
			
			skillAnalyzer.findPlane(startDate);
		}
		catch (Throwable e)
		{
			 PWCGLogger.logException(e);;
		}
	}

	
	private void findPlane(Date startDate) throws PWCGException  
	{		
		List<Company> allSq =  PWCGContext.getInstance().getCompanyManager().getAllCompanies();
		int totalSkill = 0;
		int totalFighterSquads = 0;
		for (Company company : allSq)
		{
		    PwcgRoleCategory squadronPrimaryRole = company.determineSquadronPrimaryRoleCategory(DateUtils.getEndOfWar());
            if (squadronPrimaryRole == PwcgRoleCategory.FIGHTER)
		    {
		        int squadronQuality = company.determineSquadronSkill(startDate);
		        totalSkill += squadronQuality;
		        ++totalFighterSquads;
		    }
		}
		
		int averageSkill = totalSkill / totalFighterSquads;
        PWCGLogger.log(LogLevel.INFO, "Average fighter skill: " + averageSkill);
	
	}
}
