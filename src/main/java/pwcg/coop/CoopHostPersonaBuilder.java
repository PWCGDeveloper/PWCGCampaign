package pwcg.coop;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.coop.model.CoopPersona;
import pwcg.coop.model.CoopUser;
import pwcg.core.exception.PWCGException;

class CoopPersonaHostBuilder 
{
    
    static CoopPersona buildHostPersona(Campaign campaign) throws PWCGException
    {
        SquadronMember hostPilot = campaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList().get(0);
    	CoopUser coopHostRecord = CoopUserManager.getIntance().getCoopHost();
    	
    	CoopPersona hostPersona = new CoopPersona();
    	hostPersona.setApproved(true);
    	hostPersona.setCampaignName(campaign.getCampaignData().getName());
    	hostPersona.setNote("Auto create host pilot");
    	hostPersona.setPilotName(hostPilot.getName());
    	hostPersona.setPilotRank(hostPilot.getRank());
    	hostPersona.setSerialNumber(hostPilot.getSerialNumber());
    	hostPersona.setSquadronId(hostPersona.getSquadronId());
    	hostPersona.setUsername(coopHostRecord.getUsername());
    	hostPersona.setApproved(true);

        return hostPersona;
    }
}
