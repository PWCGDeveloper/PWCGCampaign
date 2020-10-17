package pwcg.campaign;

import java.util.List;

import pwcg.campaign.context.PWCGDirectoryUserManager;
import pwcg.campaign.io.json.CoopUserIOJson;
import pwcg.campaign.io.json.CoopUserIOJsonOlde;
import pwcg.coop.CoopUserManager;
import pwcg.coop.model.CoopPersonaOlde;
import pwcg.coop.model.CoopUser;
import pwcg.coop.model.CoopUserOlde;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.FileUtils;

public class CoopToV2
{
    public static void main(String[] args)
    {
        try
        {
            CoopToV2.moveToCoopV2();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void moveToCoopV2() throws PWCGException
    {
        List<CoopUser> coopUsers = CoopUserManager.getIntance().getAllCoopUsers();
        for (CoopUser coopUser : coopUsers)
        {
            if (coopUser.getFormatVersion() == null || !coopUser.getFormatVersion().equals("V2"))
            {
                fixCoopUser(coopUser.getUsername());
            }
        }
    }

    private static void fixCoopUser(String username) throws PWCGException
    {
        CoopUserOlde coopUserOlde =  CoopUserIOJsonOlde.readCoopUser(username);
        if (coopUserOlde != null)
        {
            if (coopUserOlde.getUserPersonas().size() > 0)
            {
                CoopUser coopUser = new CoopUser(username, CoopUser.COOP_FORMAT_VERSION);
                for (CoopPersonaOlde coopPersonaOlde : coopUserOlde.getUserPersonas())
                {
                    coopUser.addPersona(coopPersonaOlde.getCampaignName(), coopPersonaOlde.getSerialNumber(), "");
                }
                CoopUserIOJson.writeJson(coopUser);
            }
            else
            {
                String coopUserDir = PWCGDirectoryUserManager.getInstance().getPwcgCoopDir();                    
                String filename = coopUserDir + username + ".json";
                FileUtils.deleteFile(filename);
            }
        }
    }
}
