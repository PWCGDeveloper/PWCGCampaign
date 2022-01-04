package pwcg.gui.maingui.campaigngenerate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.campaign.CampaignAces;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.context.AceManager;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.MapForAirfieldFinder;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.TankAce;
import pwcg.campaign.tank.PwcgRoleCategory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class CampaignGeneratorSquadronFilter
{
	public List<String> makeSquadronChoices(Date campaignDate, ArmedService dateCorrectedService, FrontMapIdentifier selectedCampaignMap, String selectedRole, boolean playerIsCommander) throws PWCGException 
	{
		List<String> validSquadrons = new ArrayList<>();
	    try
	    {
    		CompanyManager squadManager =  PWCGContext.getInstance().getCompanyManager();
    		List<Company> squadronList = squadManager.getPlayerCompaniesByService(dateCorrectedService, campaignDate);
            PWCGLogger.log(LogLevel.DEBUG, "makeSquadronChoices squadron list size: " + squadronList.size());
    		for (Company squadron : squadronList)
    		{
                PWCGLogger.log(LogLevel.DEBUG, squadron.determineDisplayName(campaignDate) + " makeSquadronChoices evaluate squadron");

       			if (rejectBecauseWrongRole(squadron, campaignDate, selectedRole))
    			{
	                PWCGLogger.log(LogLevel.DEBUG, squadron.determineDisplayName(campaignDate) + ": Cannot fly  - incorrect role");
    				continue;
    			}
    			
       			if (rejectBecauseCommandConflict(squadron, campaignDate, playerIsCommander))
    			{
                    PWCGLogger.log(LogLevel.DEBUG, squadron.determineDisplayName(campaignDate) + ": Cannot fly  - commaned by ace");
    				continue;
    			}
    			
       			if (rejectBecauseWrongMap(squadron, campaignDate, selectedCampaignMap))
    			{
                    PWCGLogger.log(LogLevel.DEBUG, squadron.determineDisplayName(campaignDate) + ": Cannot fly  - commaned by ace");
    				continue;
    			}
    			
				String squadronDisplayName = squadron.determineDisplayName(campaignDate);
				validSquadrons.add(squadronDisplayName);
    		}
	    }
	    catch (Exception exp)
	    {
            PWCGLogger.logException(exp);
            throw exp;
	    }
	    
	    return validSquadrons;
	}
	
	private boolean rejectBecauseWrongRole(Company company, Date campaignDate, String roleDesc) throws PWCGException
	{
	    PwcgRoleCategory role = PwcgRoleCategory.getRoleCategoryFromDescription(roleDesc);
        PwcgRoleCategory squadronRole = company.determineSquadronPrimaryRoleCategory(campaignDate);
        if (role == squadronRole)
        {
            return false;
        }
		
		return true;
	}
	
	private boolean rejectBecauseCommandConflict(Company company, Date campaignDate, boolean playerIsCommander) throws PWCGException
	{
		AceManager aceManager = PWCGContext.getInstance().getAceManager();
		CampaignAces aces =  aceManager.loadFromHistoricalAces(campaignDate);
		List<TankAce> squadronAces =  aceManager.getActiveAcesForSquadron(aces, campaignDate, company.getCompanyId());
		if (squadronAces.size() > 0)
		{
			if (playerIsCommander && company.isCommandedByAce(squadronAces, campaignDate))
			{
				return true;
			}
		}    					
		
		return false;
	}
	
	private boolean rejectBecauseWrongMap(Company company, Date campaignDate, FrontMapIdentifier selectedCampaignMap) throws PWCGException
	{
		if (selectedCampaignMap == null)
		{
			return false;
		}
		
    	String airfieldName = company.determineCurrentAirfieldName(campaignDate);
    	List<FrontMapIdentifier> airfieldMaps = MapForAirfieldFinder.getMapForAirfield(airfieldName);
    	
    	for (FrontMapIdentifier airfieldMap : airfieldMaps)
    	{
	    	if (airfieldMap == selectedCampaignMap)
	    	{
				return false;
	    	}
    	}
		
		return true;
	}

}
