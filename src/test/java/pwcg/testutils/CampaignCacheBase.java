package pwcg.testutils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignGeneratorModel;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.SquadronManager;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public abstract class CampaignCacheBase implements ICampaignCache
{
    public static final String TEST_CAMPAIGN_NAME = "Test Campaign";
    public static final String TEST_PLAYER_NAME = "Test Player";

    protected Map<String, Campaign> campaignCache = new HashMap<>();
    protected Map<String, CampaignGeneratorModel> campaignProfiles = new HashMap<>();
    protected abstract void loadCampaignProfiles() throws PWCGException;
    protected abstract Campaign makeCampaignFromModel(CampaignGeneratorModel model) throws PWCGException;
    protected abstract void initialize() throws PWCGException;


    protected void makeProfile(SquadrontTestProfile profile) throws PWCGException
    {
        CampaignGeneratorModel generatorModel = makeCampaignModelForProfile (profile.getDateString(), profile.getSquadronId());
        campaignProfiles.put(profile.getKey(), generatorModel);
    }

    @Override
    public Campaign makeCampaign(SquadrontTestProfile profile) throws PWCGException
    {
        initialize();
        
        Campaign campaign = null;
        if (campaignCache.containsKey(profile.getKey()))
        {
            campaign = campaignCache.get(profile.getKey());
        }
        else
        {          
            campaign = makeCampaignForceCreation(profile);
        }
        
        PWCGContextManager.getInstance().setCampaign(campaign);
        validateCampaign(profile, campaign);
        
        return campaign;
    }
    
    private void validateCampaign(SquadrontTestProfile profile, Campaign campaign) throws PWCGException
    {
        List<SquadronMember> squadronMembers = campaign.getPersonnelManager().getSquadronPersonnel(profile.getSquadronId()).getSquadronMembers().getSquadronMemberList();
        for (SquadronMember squadronMember : squadronMembers)
        {
            if (squadronMember.isPlayer())
            {
                return;
            }
        }
        
        throw new PWCGException("No players in player squadron " + profile.getSquadronId());
    }

    @Override
    public Campaign makeCampaignForceCreation(SquadrontTestProfile profile) throws PWCGException
    {
        initialize();
        if (campaignProfiles.containsKey(profile.getKey()))
        {
            CampaignGeneratorModel model = campaignProfiles.get(profile.getKey());
            Campaign campaign = makeCampaignFromModel(model);
            campaignCache.put(profile.getKey(), campaign);
            return campaign;
        }
        
        throw new PWCGException("No campaign found for profile " + profile.getKey());
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
        generatorModel.setCampaignName(CampaignCacheRoF.TEST_CAMPAIGN_NAME);
        generatorModel.setPlayerName(CampaignCacheRoF.TEST_PLAYER_NAME);
        generatorModel.setPlayerRank(rankName);
        generatorModel.setPlayerRegion("");
        generatorModel.setService(service);
        generatorModel.setSquadronName(squadronName);

        return generatorModel;
    }

}
