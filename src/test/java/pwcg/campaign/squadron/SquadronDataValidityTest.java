package pwcg.campaign.squadron;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;

public class SquadronDataValidityTest
{
    Campaign campaign;

    @Test
    public void getSquadronTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();

        boolean failed = false;
        for (Squadron squadron :squadronManager.getAllSquadrons())
        {
            if (squadron.getSquadronTacticalCodeColorOverride() == null)
            {
                System.out.println("No squadron tactical color " + squadron.getFileName());
                failed = true;
            }
            
            if (squadron.determineUnitIdCode(null) == null)
            {
                System.out.println("No squadron unit code " + squadron.getFileName());
                failed = true;
            }
            
            if (squadron.determineSubUnitIdCode(null) == null)
            {
                System.out.println("No squadron subunit code " + squadron.getFileName());
                failed = true;
            }
        }
        Assertions.assertFalse(failed);
    }
}
