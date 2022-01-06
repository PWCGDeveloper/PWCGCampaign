package pwcg.testutils;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignGenerator;
import pwcg.campaign.CampaignGeneratorModel;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;

public class CampaignCacheBoS extends CampaignCacheBase implements ICampaignCache
{    
    protected Campaign makeCampaignFromModel(CampaignGeneratorModel generatorModel) throws PWCGException
    {
        CampaignGenerator generator = new CampaignGenerator(generatorModel);
        Campaign campaign = generator.generate();          
        PWCGContext.getInstance().setCampaign(campaign);
        return campaign;
    }

    protected void initialize() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        PWCGContext.getInstance();
        if (campaignProfiles.isEmpty())
        {
            loadCampaignProfiles();
        }
    }

    @Override
    protected void loadCampaignProfiles() throws PWCGException
    {
        makeProfile(SquadronTestProfile.COOP_COMPETITIVE_PROFILE);
       makeProfile(SquadronTestProfile.GROSS_DEUTSCHLAND_PROFILE);        
    }
}
