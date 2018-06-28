package pwcg.gui.maingui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.campaign.CampaignAces;
import pwcg.campaign.context.AceManager;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.context.SquadronManager;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;
import pwcg.core.utils.Logger.LogLevel;

public class CampaignGeneratorSquadronFilter
{
	public List<String> makeSquadronChoices(Date campaignDate, ArmedService dateCorrectedService, FrontMapIdentifier selectedCampaignMap, String selectedRole, boolean playerIsCommander) throws PWCGException 
	{
		List<String> validSquadrons = new ArrayList<>();
	    try
	    {
    		SquadronManager squadManager =  PWCGContextManager.getInstance().getSquadronManager();
    		List<Squadron> squadronList = squadManager.getFlyableSquadronsByService(dateCorrectedService, campaignDate);
            Logger.log(LogLevel.DEBUG, "makeSquadronChoices squadron list size: " + squadronList.size());
    		for (Squadron squad : squadronList)
    		{
                Logger.log(LogLevel.DEBUG, squad.determineDisplayName(campaignDate) + " makeSquadronChoices evaluate squadron");
    
       			if (rejectBecauseSquadronCannotFly(squad, campaignDate))
    			{
    	            Logger.log(LogLevel.DEBUG, squad.determineDisplayName(campaignDate) + ": Cannot fly  - not aded to list");
    				continue;
    			}
       			
       			if (rejectBecauseWrongRole(squad, campaignDate, selectedRole))
    			{
	                Logger.log(LogLevel.DEBUG, squad.determineDisplayName(campaignDate) + ": Cannot fly  - incorrect role");
    				continue;
    			}
    			
       			if (rejectBecauseCommandConflict(squad, campaignDate, playerIsCommander))
    			{
                    Logger.log(LogLevel.DEBUG, squad.determineDisplayName(campaignDate) + ": Cannot fly  - commaned by ace");
    				continue;
    			}
    			
       			if (rejectBecauseWrongMap(squad, campaignDate, selectedCampaignMap))
    			{
                    Logger.log(LogLevel.DEBUG, squad.determineDisplayName(campaignDate) + ": Cannot fly  - commaned by ace");
    				continue;
    			}
    			
				String squadronDisplayName = squad.determineDisplayName(campaignDate);
				validSquadrons.add(squadronDisplayName);
    		}
	    }
	    catch (Exception exp)
	    {
            Logger.logException(exp);
            throw exp;
	    }
	    
	    return validSquadrons;
	}
	
	private boolean rejectBecauseSquadronCannotFly(Squadron squad, Date campaignDate) throws PWCGException
	{
		if (squad.isCanFly(campaignDate))
		{
			return false;
		}
		
		return true;
	}
	
	private boolean rejectBecauseWrongRole(Squadron squad, Date campaignDate, String roleDesc) throws PWCGException
	{
        Role role = Role.descToRole(roleDesc);
        if (squad.isSquadronThisPrimaryRole(campaignDate, role))
        {
            return false;
        }
		
		return true;
	}
	
	private boolean rejectBecauseCommandConflict(Squadron squad, Date campaignDate, boolean playerIsCommander) throws PWCGException
	{
		AceManager aceManager = PWCGContextManager.getInstance().getAceManager();
		CampaignAces aces =  aceManager.loadFromHistoricalAces(campaignDate);
		List<Ace> squadronAces =  aceManager.getActiveAcesForSquadron(aces, campaignDate, squad.getSquadronId());
		if (squadronAces.size() > 0)
		{
			if (playerIsCommander && squad.isCommandedByAce(squadronAces, campaignDate))
			{
				return true;
			}
		}    					
		
		return false;
	}
	
	private boolean rejectBecauseWrongMap(Squadron squad, Date campaignDate, FrontMapIdentifier selectedCampaignMap) throws PWCGException
	{
		if (selectedCampaignMap == null)
		{
			return false;
		}
		
    	String airfieldName = squad.determineCurrentAirfieldName(campaignDate);
    	List<FrontMapIdentifier> airfieldMaps = PWCGContextManager.getInstance().getMapForAirfield(airfieldName);
    	
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
