package pwcg.testutils;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignGenerator;
import pwcg.campaign.CampaignGeneratorModel;
import pwcg.campaign.context.PWCGContextManager;
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
        PWCGContextManager.setRoF(true);
        PWCGContextManager.getInstance();
        if (campaignProfiles.isEmpty())
        {
           loadCampaignProfiles();
        }
    }
    
    protected Campaign makeCampaignFromModel(CampaignGeneratorModel generatorModel) throws PWCGException
    {
        CampaignGenerator generator = new CampaignGenerator(generatorModel);
        Campaign campaign = generator.generate();
        PWCGContextManager.getInstance().setCampaign(campaign);
        return campaign;
    }

    @Override
    protected void loadCampaignProfiles() throws PWCGException
    {
        PWCGContextManager.setRoF(true);
        makeProfile(SquadrontTestProfile.JASTA_11_PROFILE);
        makeProfile(SquadrontTestProfile.ESC_103_PROFILE);
        makeProfile(SquadrontTestProfile.ESC_124_PROFILE);
        makeProfile(SquadrontTestProfile.RFC_2_PROFILE);
        makeProfile(SquadrontTestProfile.ESC_2_PROFILE);


        makeJasta11Profile();
        makeEsc124Profile();
        makeEsc103Profile();
        makeEsc2Profile();
        makeRFC2Profile();
    }
    
    private void makeRFC2Profile() throws PWCGException
    {
        CampaignGeneratorModel generatorModel = makeCampaignModelForProfile ("19180331", 102002);
        campaignProfiles.put(RFC_2_PROFILE, generatorModel);
    }

    private void makeEsc124Profile() throws PWCGException
    {
        CampaignGeneratorModel generatorModel = makeCampaignModelForProfile ("19180218", 101124);
        campaignProfiles.put(ESC_124_PROFILE, generatorModel);
    }

    private void makeJasta11Profile() throws PWCGException
    {
        CampaignGeneratorModel generatorModel = makeCampaignModelForProfile ("19170501", 501011);
        campaignProfiles.put(JASTA_11_PROFILE, generatorModel);
    }
    
    private void makeEsc103Profile() throws PWCGException
    {
        CampaignGeneratorModel generatorModel = makeCampaignModelForProfile ("19170701", 101103);
        campaignProfiles.put(ESC_103_PROFILE, generatorModel);
    }
    
    private void makeEsc2Profile() throws PWCGException
    {
        CampaignGeneratorModel generatorModel = makeCampaignModelForProfile ("19170801", 101002);
        campaignProfiles.put(ESC_2_PROFILE, generatorModel);
    }
}
