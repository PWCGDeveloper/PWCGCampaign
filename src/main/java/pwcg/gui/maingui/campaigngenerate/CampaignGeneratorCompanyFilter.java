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

public class CampaignGeneratorCompanyFilter
{
	public List<String> makeSquadronChoices(Date campaignDate, ArmedService dateCorrectedService, FrontMapIdentifier selectedCampaignMap, String selectedRole, boolean playerIsCommander) throws PWCGException 
	{
		List<String> validSquadrons = new ArrayList<>();
	    try
	    {
    		CompanyManager companyManager =  PWCGContext.getInstance().getCompanyManager();
    		List<Company> companyList = companyManager.getPlayerCompaniesByService(dateCorrectedService, campaignDate);
            PWCGLogger.log(LogLevel.DEBUG, "makeSquadronChoices company list size: " + companyList.size());
    		for (Company company : companyList)
    		{
                PWCGLogger.log(LogLevel.DEBUG, company.determineDisplayName(campaignDate) + " makeSquadronChoices evaluate company");

       			if (rejectBecauseWrongRole(company, campaignDate, selectedRole))
    			{
	                PWCGLogger.log(LogLevel.DEBUG, company.determineDisplayName(campaignDate) + ": Cannot fly  - incorrect role");
    				continue;
    			}
    			
       			if (rejectBecauseCommandConflict(company, campaignDate, playerIsCommander))
    			{
                    PWCGLogger.log(LogLevel.DEBUG, company.determineDisplayName(campaignDate) + ": Cannot fly  - commaned by ace");
    				continue;
    			}
    			
       			if (rejectBecauseWrongMap(company, campaignDate, selectedCampaignMap))
    			{
                    PWCGLogger.log(LogLevel.DEBUG, company.determineDisplayName(campaignDate) + ": Cannot fly  - commaned by ace");
    				continue;
    			}
    			
				String companyDisplayName = company.determineDisplayName(campaignDate);
				validSquadrons.add(companyDisplayName);
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
        PwcgRoleCategory companyRole = company.determineSquadronPrimaryRoleCategory(campaignDate);
        if (role == companyRole)
        {
            return false;
        }
		
		return true;
	}
	
	private boolean rejectBecauseCommandConflict(Company company, Date campaignDate, boolean playerIsCommander) throws PWCGException
	{
		AceManager aceManager = PWCGContext.getInstance().getAceManager();
		CampaignAces aces =  aceManager.loadFromHistoricalAces(campaignDate);
		List<TankAce> companyAces =  aceManager.getActiveAcesForSquadron(aces, campaignDate, company.getCompanyId());
		if (companyAces.size() > 0)
		{
			if (playerIsCommander && company.isCommandedByAce(companyAces, campaignDate))
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
