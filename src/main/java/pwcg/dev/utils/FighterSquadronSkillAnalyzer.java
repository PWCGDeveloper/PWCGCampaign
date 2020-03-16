package pwcg.dev.utils;

import java.util.Date;
import java.util.List;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
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
		List<Squadron> allSq =  PWCGContext.getInstance().getSquadronManager().getAllSquadrons();
		int totalSkill = 0;
		int totalFighterSquads = 0;
		for (Squadron squad : allSq)
		{
		    Role primaryRole = squad.determineSquadronPrimaryRole(DateUtils.getEndOfWar());

		    if (primaryRole == Role.ROLE_FIGHTER)
		    {
		        int squadronQuality = squad.determineSquadronSkill(startDate);
		        totalSkill += squadronQuality;
		        ++totalFighterSquads;
		    }
		}
		
		int averageSkill = totalSkill / totalFighterSquads;
        PWCGLogger.log(LogLevel.INFO, "Average fighter skill: " + averageSkill);
	
	}
}
