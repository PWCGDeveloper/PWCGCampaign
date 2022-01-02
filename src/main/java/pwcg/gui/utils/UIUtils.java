package pwcg.gui.utils;

import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.exception.PWCGException;

public class UIUtils
{
    public static CrewMember getCrewMemberFromAction(Campaign campaign, String action) throws PWCGException
    {
        int index = action.indexOf(":");
        String crewMemberSerialNumberString = action.substring(index + 1);
        Integer serialNumber = Integer.valueOf(crewMemberSerialNumberString);

        CrewMember crewMember = campaign.getPersonnelManager().getAnyCampaignMember(serialNumber);
        if (crewMember == null)
        {
            crewMember = campaign.getPersonnelManager().getCampaignAces().retrieveAceBySerialNumber(serialNumber);
        }

        
        return crewMember;
    }
}
