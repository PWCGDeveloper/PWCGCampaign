package pwcg.campaign.squadron;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.exception.PWCGException;

public class FCSquadronAirfieldExistsTest
{
    Campaign campaign;

    @Test
    public void checkAirfieldsForEachSquadronTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        boolean allAirfieldsValid = true;
        for (Squadron squadron : PWCGContext.getInstance().getSquadronManager().getAllSquadrons())
        {
            if (!getAirfieldsForSquadron(squadron))
            {
                allAirfieldsValid = false;
            }
        }
        
        Assertions.assertTrue(allAirfieldsValid);
    }

    private boolean getAirfieldsForSquadron(Squadron squadron)
    {
        boolean squadronAirfieldsValid = true;
        for (String airfieldName : squadron.getAirfields().values()) 
        {
            if (!getAirfieldForSquadron(squadron, squadronAirfieldsValid, airfieldName))
            {
                squadronAirfieldsValid = false;
            }
        }
        return squadronAirfieldsValid;
    }

    private boolean getAirfieldForSquadron(Squadron squadron, boolean squadronAirfieldsValid, String airfieldName)
    {
        Airfield airfield = PWCGContext.getInstance().getAirfieldAllMaps(airfieldName);
        if (airfield == null)
        {
            System.out.println(squadron.getFileName() + " has invalid airfield " + airfieldName);
            squadronAirfieldsValid = false;
        }
        return squadronAirfieldsValid;
    }
}
