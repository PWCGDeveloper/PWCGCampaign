package pwcg.campaign.squadmember;

import pwcg.campaign.Campaign;
import pwcg.campaign.io.json.CoopPilotIOJson;
import pwcg.coop.model.CoopPilot;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;

public class SquadronMemberReplacerCoop extends SquadronMemberReplacer  implements ISquadronMemberReplacer
{    
    public SquadronMemberReplacerCoop(Campaign campaign)
    {
        super(campaign);
    }
	
    public SquadronMember createPilot(String playerPilotName, String rank, String squadronName, String coopUser) throws PWCGUserException, Exception
    {        
        SquadronMember newSquadronMewmber = super.createPilot(playerPilotName, rank, squadronName, coopUser);
        createCoopPilot(newSquadronMewmber, coopUser);
        return newSquadronMewmber;
    }

	private void createCoopPilot(SquadronMember newSquadronMewmber, String coopUser) throws PWCGException 
	{
        CoopPilot coopPilot = new CoopPilot();
        coopPilot.setCampaignName(campaign.getCampaignData().getName());
        coopPilot.setNote("Created by PWCG");
        coopPilot.setPilotName(newSquadronMewmber.getName());
        coopPilot.setPilotRank(newSquadronMewmber.getRank());
        coopPilot.setSerialNumber(newSquadronMewmber.getSerialNumber());
        coopPilot.setSquadronId(newSquadronMewmber.getSquadronId());
        coopPilot.setUsername(coopUser);
        coopPilot.setApproved(true);
	
        CoopPilotIOJson.writeJson(coopPilot);
	}
}
