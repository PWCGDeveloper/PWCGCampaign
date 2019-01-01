package pwcg.campaign.io.json;

import java.util.List;

import org.junit.Test;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.coop.model.CoopUser;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.FileUtils;

public class CoopUserIOJsonTest
{
    @Test
    public void campaignIOJsonTest() throws PWCGException
    {
        PWCGContextManager.setRoF(false);

        deleteCoopUser();
        writeCoopUser();
        readCoopUser();
        deleteCoopUser();
    }


    private void writeCoopUser() throws PWCGException
    {
        CoopUser coopUser = new CoopUser();
        coopUser.setUsername("Test User");
        coopUser.setApproved(true);
        coopUser.setNote("I want to play");
        coopUser.setPassword("password");
        
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
                assert (coopUser.isApproved() == true);
                assert (coopUser.getNote().equals("I want to play"));
                assert (coopUser.getPassword().equals("password"));
                pilotFound = true;
            }
        }
        
        assert(pilotFound);
    }
    
    private void deleteCoopUser()
    {
        FileUtils fileUtils = new FileUtils();
        String coopUserPath = PWCGContextManager.getInstance().getDirectoryManager().getPwcgCoopDir() + "Users\\Test User.json";
        fileUtils.deleteFile(coopUserPath);
    }
}
