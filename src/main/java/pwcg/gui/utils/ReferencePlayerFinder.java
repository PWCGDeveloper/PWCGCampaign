package pwcg.gui.utils;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;

public class ReferencePlayerFinder
{
    public static SquadronMember findReferencePlayer(Campaign campaign) throws PWCGException
    {
        CampaignPersonnelManager personnelManager = campaign.getPersonnelManager();
        return personnelManager.getAnyCampaignMember(campaign.getCampaignData().getReferencePlayerSerialNumber());
    }
}
