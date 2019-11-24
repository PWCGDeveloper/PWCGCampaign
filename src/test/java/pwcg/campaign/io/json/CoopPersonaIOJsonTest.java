package pwcg.campaign.io.json;

import java.util.List;

import org.junit.Test;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.coop.model.CoopPersona;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.FileUtils;

public class CoopPersonaIOJsonTest
{
    @Test
    public void campaignIOJsonTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);

        deleteCoopPersona();
        writeCoopPersona();
        readCoopPersona();
        deleteCoopPersona();
    }


    private void writeCoopPersona() throws PWCGException
    {
        CoopPersona coopPersona = new CoopPersona();
        coopPersona.setPilotName("Test Pilot");
        coopPersona.setCampaignName("Coop Campaign");

        coopPersona.setApproved(true);
        coopPersona.setNote("I want a new pilot");
        coopPersona.setSerialNumber(12345);
        coopPersona.setSquadronId(999);
        coopPersona.setUsername("Me");
        coopPersona.setApproved(true);
        
        CoopPersonaIOJson.writeJson(coopPersona);
    }

    private void readCoopPersona() throws PWCGException
    {
        boolean pilotFound = false;
        List<CoopPersona> coopPersonas = CoopPersonaIOJson.readCoopPersonas();
        for (CoopPersona coopPersona: coopPersonas)
        {
            if (coopPersona.getPilotName().equals("Test Pilot") && coopPersona.getCampaignName().equals("Coop Campaign"))
            {
                assert (coopPersona.isApproved() == true);
                assert (coopPersona.getNote().equals("I want a new pilot"));
                assert (coopPersona.getSerialNumber() == 12345);
                assert (coopPersona.getSquadronId() == 999);
                assert (coopPersona.getUsername().equals("Me"));
                assert (coopPersona.isApproved() == true);
                pilotFound = true;
            }
        }
        
        assert(pilotFound);
    }
    
    private void deleteCoopPersona()
    {
        FileUtils fileUtils = new FileUtils();
        String coopPersonaPath = PWCGContext.getInstance().getDirectoryManager().getPwcgCoopDir() + "Pilots\\Test Pilot.json";
        fileUtils.deleteFile(coopPersonaPath);
    }
}
