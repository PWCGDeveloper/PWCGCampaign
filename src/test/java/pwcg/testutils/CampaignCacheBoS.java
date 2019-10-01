package pwcg.testutils;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignGenerator;
import pwcg.campaign.CampaignGeneratorModel;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;

public class CampaignCacheBoS extends CampaignCacheBase implements ICampaignCache
{    
    protected Campaign makeCampaignFromModel(CampaignGeneratorModel generatorModel) throws PWCGException
    {
        CampaignGenerator generator = new CampaignGenerator(generatorModel);
        Campaign campaign = generator.generate();          
        PWCGContextManager.getInstance().setCampaign(campaign);
        return campaign;
    }

    protected void initialize() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        PWCGContextManager.getInstance();
        if (campaignProfiles.isEmpty())
        {
            loadCampaignProfiles();
        }
    }

    @Override
    protected void loadCampaignProfiles() throws PWCGException
    {
        makeProfile(SquadrontTestProfile.REGIMENT_503_PROFILE);
        makeProfile(SquadrontTestProfile.JG_51_PROFILE_MOSCOW);
        makeProfile(SquadrontTestProfile.JG_51_PROFILE_STALINGRAD);
        makeProfile(SquadrontTestProfile.JG_51_PROFILE_WEST);
        makeProfile(SquadrontTestProfile.KG53_PROFILE);
        makeProfile(SquadrontTestProfile.STG77_PROFILE);
        makeProfile(SquadrontTestProfile.TG2_PROFILE);
        makeProfile(SquadrontTestProfile.FG_362_PROFILE);
        makeProfile(SquadrontTestProfile.RAF_184_PROFILE);
        makeProfile(SquadrontTestProfile.COOP_COMPETITIVE_PROFILE);
        makeProfile(SquadrontTestProfile.REGIMENT_11_PROFILE);
        makeProfile(SquadrontTestProfile.REGIMENT_321_PROFILE);
    }
}
