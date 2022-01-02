package pwcg.coop;

import java.util.List;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.io.json.CoopUserIOJson;
import pwcg.coop.model.CoopUser;
import pwcg.core.exception.PWCGException;

public class CoopUserCampaignMassUpdate
{
    public static void updateCoopUserRecordsForUserSelectionMakeANewClassAndTestIt(Campaign campaign, Map<String, List<Integer>> usersForPersonas) throws PWCGException
    {
        for (CoopUser coopUser : CoopUserManager.getIntance().getAllCoopUsers())
        {
            if (coopUser.getCoopCampaignPersona(campaign.getName()) != null)
            {
                for (int coopPersona : coopUser.getUserPersonas(campaign.getName()))
                {
                    coopUser.removePersona(campaign.getName(), coopPersona);
                }
            }
        }

        for (String username : usersForPersonas.keySet())
        {
            CoopUser coopUser = CoopUserManager.getIntance().getCoopUser(username);
            if (coopUser != null)
            {
                List<Integer> personaByUser = usersForPersonas.get(username);
                for (int serialNumber : personaByUser)
                {
                    CrewMember crewMember = campaign.getPersonnelManager().getAnyCampaignMember(serialNumber);
                    coopUser.addPersona(campaign.getName(), crewMember.getSerialNumber(), crewMember.getName());
                }
                CoopUserIOJson.writeJson(coopUser);
            }
        }        

    }

}
