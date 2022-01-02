package integration.campaign.io.json;

import java.util.List;

import org.junit.jupiter.api.Test;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGDirectoryUserManager;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.io.json.CoopUserIOJson;
import pwcg.coop.model.CoopUser;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.FileUtils;

public class CoopUserIOJsonTest
{
    @Test
    public void campaignIOJsonTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);

        deleteCoopUser();
        writeCoopUser();
        readCoopUser();
        deleteCoopUser();
    }


    private void writeCoopUser() throws PWCGException
    {
        CoopUser coopUser = new CoopUser("Test User", CoopUser.COOP_FORMAT_VERSION);
        CoopUserIOJson.writeJson(coopUser);
    }

    private void readCoopUser() throws PWCGException
    {
        boolean crewMemberFound = false;
        List<CoopUser> coopUsers = CoopUserIOJson.readCoopUsers();
        for (CoopUser coopUser: coopUsers)
        {
            if (coopUser.getUsername().equals("Test User"))
            {
                crewMemberFound = true;
            }
        }
        
        assert(crewMemberFound);
    }
    
    private void deleteCoopUser()
    {
        String coopUserPath = PWCGDirectoryUserManager.getInstance().getPwcgCoopDir() + "Test User.json";
        FileUtils.deleteFile(coopUserPath);
    }
}
