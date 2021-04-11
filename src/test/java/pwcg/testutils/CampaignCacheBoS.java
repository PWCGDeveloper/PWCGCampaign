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
        makeProfile(SquadronTestProfile.REGIMENT_503_PROFILE);
        makeProfile(SquadronTestProfile.JG_51_PROFILE_MOSCOW);
        makeProfile(SquadronTestProfile.JG_51_PROFILE_STALINGRAD);
        makeProfile(SquadronTestProfile.JG_51_PROFILE_STALINGRAD_FW190);
        makeProfile(SquadronTestProfile.JG_52_PROFILE_STALINGRAD);
        makeProfile(SquadronTestProfile.JG_26_PROFILE_WEST);
        makeProfile(SquadronTestProfile.KG53_PROFILE);
        makeProfile(SquadronTestProfile.KG53__STALINGRAD_PROFILE);
        makeProfile(SquadronTestProfile.STG77_PROFILE);
        makeProfile(SquadronTestProfile.TG2_PROFILE);
        makeProfile(SquadronTestProfile.FG_362_PROFILE);
        makeProfile(SquadronTestProfile.RAF_184_PROFILE);
        makeProfile(SquadronTestProfile.COOP_COMPETITIVE_PROFILE);
        makeProfile(SquadronTestProfile.REGIMENT_11_PROFILE);
        makeProfile(SquadronTestProfile.REGIMENT_321_PROFILE);
        makeProfile(SquadronTestProfile.EAST1944_PROFILE);
        makeProfile(SquadronTestProfile.EAST1945_PROFILE);
    }
}
