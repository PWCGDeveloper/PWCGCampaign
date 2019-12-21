package pwcg.gui.utils;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;

public class UIUtils
{
    public static SquadronMember getPilotFromAction(Campaign campaign, String action) throws PWCGException
    {
        int index = action.indexOf(":");
        String pilotSerialNumberString = action.substring(index + 1);
        Integer serialNumber = Integer.valueOf(pilotSerialNumberString);

        SquadronMember pilot = campaign.getPersonnelManager().getAnyCampaignMember(serialNumber);
        if (pilot == null)
        {
            pilot = campaign.getPersonnelManager().getCampaignAces().retrieveAceBySerialNumber(serialNumber);
        }

        
        return pilot;
    }
}
