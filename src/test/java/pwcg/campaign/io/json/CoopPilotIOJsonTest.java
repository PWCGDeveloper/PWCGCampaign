package pwcg.campaign.io.json;

import java.util.List;

import org.junit.Test;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.coop.model.CoopPilot;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.FileUtils;

public class CoopPilotIOJsonTest
{
    @Test
    public void campaignIOJsonTest() throws PWCGException
    {
        PWCGContextManager.setRoF(false);

        deleteCoopPilot();
        writeCoopPilot();
        readCoopPilot();
        deleteCoopPilot();
    }


    private void writeCoopPilot() throws PWCGException
    {
        CoopPilot coopPilot = new CoopPilot();
        coopPilot.setPilotName("Test Pilot");
        coopPilot.setCampaignName("Coop Campaign");

        coopPilot.setApproved(true);
        coopPilot.setNote("I want a new pilot");
        coopPilot.setSerialNumber(12345);
        coopPilot.setSquadronId(999);
        coopPilot.setUsername("Me");
        
        CoopPilotIOJson.writeJson(coopPilot);
    }

    private void readCoopPilot() throws PWCGException
    {
        boolean pilotFound = false;
        List<CoopPilot> coopPilots = CoopPilotIOJson.readCoopPilots();
        for (CoopPilot coopPilot: coopPilots)
        {
            if (coopPilot.getPilotName().equals("Test Pilot") && coopPilot.getCampaignName().equals("Coop Campaign"))
            {
                assert (coopPilot.isApproved() == true);
                assert (coopPilot.getNote().equals("I want a new pilot"));
                assert (coopPilot.getSerialNumber() == 12345);
                assert (coopPilot.getSquadronId() == 999);
                assert (coopPilot.getUsername().equals("Me"));
                pilotFound = true;
            }
        }
        
        assert(pilotFound);
    }
    
    private void deleteCoopPilot()
    {
        FileUtils fileUtils = new FileUtils();
        String coopPilotPath = PWCGContextManager.getInstance().getDirectoryManager().getPwcgCoopDir() + "Pilots\\Test Pilot.json";
        fileUtils.deleteFile(coopPilotPath);
    }
}
