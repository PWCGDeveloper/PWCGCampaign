package pwcg.testutils;

import java.io.File;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignGenerator;
import pwcg.campaign.CampaignGeneratorModel;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.FlightTypes;

public class CampaignCacheBoS extends CampaignCacheBase implements ICampaignCache
{
    public static final String REG_503_PROFILE = "503rdGroundAttackRegiment";
    public static final String JG_51_PROFILE = "Jasta51";
    public static final String JG_51_PROFILE_2 = "Jasta51_2";
    public static final String JG_51_PROFILE_3 = "Jasta51_3";
    public static final String KG53_PROFILE = "KG53";
    public static final String STG77_PROFILE = "StG77";
    public static final String TG2_PROFILE = "TG2";
    public static final String FG_362_PROFILE = "362FG";
    public static final String WRITTEN = "Written";
    
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
        make503GroundAttackProfile();
        makeJasta51Profile();
        makeJasta51Profile2();
        makeJasta51Profile3();
        make362FGProfile();
        makeKG53Profile();
        makeStG77Profile();
        makeTG2Profile();
        makeWrittenProfile();
    }
    
    private void makeWrittenProfile() throws PWCGException
    {
        String pwcgCampaignDir = PWCGContextManager.getInstance().getDirectoryManager().getPwcgCampaignsDir();
        String writtenDir = pwcgCampaignDir + TEST_CAMPAIGN_NAME;
        File writtenDirFile = new File(writtenDir);
        if (!writtenDirFile.exists())
        {
            Campaign writtenCampaign = makeCampaign(JG_51_PROFILE);
            writtenCampaign.write();
            
            Mission mission = new Mission();
            mission.initialize(writtenCampaign);
            mission.generate(FlightTypes.PATROL);
            mission.finalizeMission();
        }
    }

    private void make503GroundAttackProfile() throws PWCGException
    {
        CampaignGeneratorModel generatorModel = makeCampaignModelForProfile ("19420801", 10121503);
        campaignProfiles.put(REG_503_PROFILE, generatorModel);
    }
    
    private void makeJasta51Profile() throws PWCGException
    {
        CampaignGeneratorModel generatorModel = makeCampaignModelForProfile ("19411101", 20111051);
        campaignProfiles.put(JG_51_PROFILE, generatorModel);
    }
    
    private void makeJasta51Profile2() throws PWCGException
    {
        CampaignGeneratorModel generatorModel = makeCampaignModelForProfile ("19420501", 20111051);
        campaignProfiles.put(JG_51_PROFILE_2, generatorModel);
    }
    
    private void makeJasta51Profile3() throws PWCGException
    {
        CampaignGeneratorModel generatorModel = makeCampaignModelForProfile ("19440801", 20111051);
        campaignProfiles.put(JG_51_PROFILE_3, generatorModel);
    }
    
    private void make362FGProfile() throws PWCGException
    {
        CampaignGeneratorModel generatorModel = makeCampaignModelForProfile ("19440801", 102362377);
        campaignProfiles.put(FG_362_PROFILE, generatorModel);
    }

    private void makeKG53Profile() throws PWCGException
    {
        CampaignGeneratorModel generatorModel = makeCampaignModelForProfile ("19430301", 20131053);
        campaignProfiles.put(KG53_PROFILE, generatorModel);
    }
    
    private void makeStG77Profile() throws PWCGException
    {
        CampaignGeneratorModel generatorModel = makeCampaignModelForProfile ("19420906", 20121077);
        campaignProfiles.put(STG77_PROFILE, generatorModel);
    }
    
    private void makeTG2Profile() throws PWCGException
    {
        CampaignGeneratorModel generatorModel = makeCampaignModelForProfile ("19420906", 20142002);
        campaignProfiles.put(TG2_PROFILE, generatorModel);
    }
    
}
