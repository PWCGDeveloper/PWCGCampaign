package pwcg.campaign;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.io.json.CoopPilotIOJson;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.coop.model.CoopPilot;
import pwcg.coop.model.CoopUser;
import pwcg.core.exception.PWCGException;

public class CoopHostPilotBuilder 
{
    
    public void buildHostPilotForCoop(Campaign campaign) throws PWCGException
    {
        SquadronMember hostPilot = campaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList().get(0);
        	
    	CoopHostUserBuilder hostBuilder = new CoopHostUserBuilder();
    	CoopUser coopHostRecord = hostBuilder.getHostUser();
    	
    	CoopPilot hostPilotRecord = new CoopPilot();
    	hostPilotRecord.setApproved(true);
    	hostPilotRecord.setCampaignName(campaign.getCampaignData().getName());
    	hostPilotRecord.setNote("Auto create host pilot");
    	hostPilotRecord.setPilotName(hostPilot.getName());
    	hostPilotRecord.setPilotRank(hostPilot.getRank());
    	hostPilotRecord.setSerialNumber(hostPilot.getSerialNumber());
    	hostPilotRecord.setSquadronId(hostPilotRecord.getSquadronId());
    	hostPilotRecord.setUsername(coopHostRecord.getUsername());
    	hostPilotRecord.setApproved(true);

    	CoopPilotIOJson.writeJson(hostPilotRecord);

    	PWCGContext.getInstance().setCampaign(campaign);
    }
}
