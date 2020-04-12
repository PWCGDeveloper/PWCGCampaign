package pwcg.campaign.io.json;

import java.util.List;

import org.junit.Test;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
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
        CoopUser coopUser = new CoopUser();
        coopUser.setUsername("Test User");        
        CoopUserIOJson.writeJson(coopUser);
    }

    private void readCoopUser() throws PWCGException
    {
        boolean pilotFound = false;
        List<CoopUser> coopUsers = CoopUserIOJson.readCoopUsers();
        for (CoopUser coopUser: coopUsers)
        {
            if (coopUser.getUsername().equals("Test User"))
            {
                pilotFound = true;
            }
        }
        
        assert(pilotFound);
    }
    
    private void deleteCoopUser()
    {
        String coopUserPath = PWCGContext.getInstance().getDirectoryManager().getPwcgCoopDir() + "Users\\Test User.json";
        FileUtils.deleteFile(coopUserPath);
    }
}
