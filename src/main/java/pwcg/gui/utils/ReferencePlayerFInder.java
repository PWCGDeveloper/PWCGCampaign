package pwcg.gui.utils;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.utils.Logger;

public class ReferencePlayerFInder
{
    public static SquadronMember findReferencePlayer(Campaign campaign)
    {
        try
        {
            if (campaign.getCampaignData().isCoop())
            {
                // TODO Coop - need screen to choose campaign member
                return null;
            }
            else
            {
                return campaign.getPersonnelManager().getAllPlayers().getSquadronMemberList().get(0);
            }
        }
        catch (Exception e)
        {
            Logger.logException(e);
        }

        return null;
    }
}
