package pwcg.campaign.squadmember;

import pwcg.campaign.Campaign;
import pwcg.coop.CoopUserManager;
import pwcg.core.exception.PWCGUserException;

public class SquadronMemberReplacerCoop extends SquadronMemberReplacer  implements ISquadronMemberReplacer
{    
    public SquadronMemberReplacerCoop(Campaign campaign)
    {
        super(campaign);
    }
	
    public SquadronMember createPersona(String playerPilotName, String rank, String squadronName, String coopUsername) throws PWCGUserException, Exception
    {        
        SquadronMember newSquadronMewmber = super.createPersona(playerPilotName, rank, squadronName, coopUsername);
        if (campaign.isCoop())
        {
            CoopUserManager.getIntance().createCoopPersona(campaign.getName(), newSquadronMewmber, coopUsername);
        }
        return newSquadronMewmber;
    }
}
