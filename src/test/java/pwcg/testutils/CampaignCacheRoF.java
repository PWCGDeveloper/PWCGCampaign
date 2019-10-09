package pwcg.testutils;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignGenerator;
import pwcg.campaign.CampaignGeneratorModel;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;

public class CampaignCacheRoF extends CampaignCacheBase
{
    public static final String JASTA_11_PROFILE = "Jasta11Profile";
    public static final String ESC_103_PROFILE = "Esc103Profile";
    public static final String ESC_124_PROFILE = "Esc124Profile";
    public static final String RFC_2_PROFILE = "RFC2Profile";
    public static final String ESC_2_PROFILE = "Esc2Profile";
       
    protected void initialize() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.ROF);
        PWCGContext.getInstance();
        if (campaignProfiles.isEmpty())
        {
           loadCampaignProfiles();
        }
    }
    
    protected Campaign makeCampaignFromModel(CampaignGeneratorModel generatorModel) throws PWCGException
    {
        CampaignGenerator generator = new CampaignGenerator(generatorModel);
        Campaign campaign = generator.generate();
        PWCGContext.getInstance().setCampaign(campaign);
        return campaign;
    }

    @Override
    protected void loadCampaignProfiles() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.ROF);
        makeProfile(SquadrontTestProfile.JASTA_11_PROFILE);
        makeProfile(SquadrontTestProfile.ESC_103_PROFILE);
        makeProfile(SquadrontTestProfile.ESC_124_PROFILE);
        makeProfile(SquadrontTestProfile.RFC_2_PROFILE);
        makeProfile(SquadrontTestProfile.ESC_2_PROFILE);
    }
}
