package pwcg.campaign.squadron;

import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGMap;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestCampaignFactoryBuilder;

public class SquadronAirfieldTest
{
    Campaign campaign;

    @Test
    public void getSquadronTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = TestCampaignFactoryBuilder.makeCampaign(this.getClass().getCanonicalName(), SquadronTestProfile.RAF_BOB_PROFILE);

        while (DateUtils.isDateInRange(campaign.getDate(), DateUtils.getBeginningOfGame(), DateUtils.getEndOfWar()))
        {
            testSquadronAirfieldValidity();
            Date nextDay = DateUtils.advanceTimeDays(campaign.getDate(), 1); 
            campaign.setDate(nextDay);
        }
    }
    
    private void testSquadronAirfieldValidity() throws PWCGException
    {
        for (Squadron squadron : PWCGContext.getInstance().getSquadronManager().getActiveSquadrons(campaign.getDate()))
        {
            PWCGMap map =  squadron.getMapForAirfield(campaign.getDate());
            Assertions.assertNotNull(map);
        }
    }
}
