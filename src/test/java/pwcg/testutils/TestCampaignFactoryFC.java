package pwcg.testutils;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignGenerator;
import pwcg.campaign.CampaignGeneratorModel;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;

public class TestCampaignFactoryFC extends TestCampaignFactoryBase
{
    public static final String JASTA_11_PROFILE = "Jasta11Profile";
    public static final String ESC_103_PROFILE = "Esc103Profile";
    public static final String RFC_2_PROFILE = "RFC2Profile";
       
    protected void initialize() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
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
        return campaign;
    }

    @Override
    protected void loadCampaignProfiles() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        makeProfile(SquadronTestProfile.JASTA_11_PROFILE);
        makeProfile(SquadronTestProfile.ESC_103_PROFILE);
        makeProfile(SquadronTestProfile.ESC_3_PROFILE);
        makeProfile(SquadronTestProfile.RFC_2_PROFILE);
        makeProfile(SquadronTestProfile.RFC_46_PROFILE);
    }
}
