package pwcg.testutils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignGeneratorModel;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.SquadronManager;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public abstract class CampaignCacheBase implements ICampaignCache
{
    public static final String TEST_CAMPAIGN_NAME = "Test Campaign";

    protected Map<String, Campaign> campaignCache = new HashMap<>();
    protected Map<String, CampaignGeneratorModel> campaignProfiles = new HashMap<>();
    protected abstract void loadCampaignProfiles() throws PWCGException;
    protected abstract Campaign makeCampaignFromModel(CampaignGeneratorModel model) throws PWCGException;
    protected abstract void initialize() throws PWCGException;

    @Override
    public Campaign makeCampaign(String campaignProfileName) throws PWCGException
    {
        initialize();
        if (campaignCache.containsKey(campaignProfileName))
        {
            return campaignCache.get(campaignProfileName);
        }
        
        return makeCampaignForceCreation(campaignProfileName);
    }

    @Override
    public Campaign makeCampaignForceCreation(String campaignProfileName) throws PWCGException
    {
        initialize();
        if (campaignProfiles.containsKey(campaignProfileName))
        {
            CampaignGeneratorModel model = campaignProfiles.get(campaignProfileName);
            Campaign campaign = makeCampaignFromModel(model);
            campaignCache.put(campaignProfileName, campaign);
            return campaign;
        }
        
        throw new PWCGException("No campaign found for profile " + campaignProfileName);
    }
    
    public static CampaignGeneratorModel makeCampaignModelForProfile(String dateYYMMDD, int squadronId) throws PWCGException
    {
        SquadronManager squadronManager = PWCGContextManager.getInstance().getSquadronManager();
        Squadron squadron = squadronManager.getSquadron(squadronId);
        
        Date campaignDate = DateUtils.getDateYYYYMMDD(dateYYMMDD);
        ArmedService service = squadron.determineServiceForSquadron(campaignDate);
        String squadronName = squadron.determineDisplayName(campaignDate);
        
        IRankHelper rank = RankFactory.createRankHelper();
        String rankName = rank.getRankByService(2, service);
    
        CampaignGeneratorModel generatorModel = new CampaignGeneratorModel();
        generatorModel.setCampaignDate(campaignDate);
        generatorModel.setPlayerName(CampaignCacheRoF.TEST_CAMPAIGN_NAME);
        generatorModel.setPlayerRank(rankName);
        generatorModel.setPlayerRegion("");
        generatorModel.setService(service);
        generatorModel.setSquadronName(squadronName);

        return generatorModel;
    }

}
