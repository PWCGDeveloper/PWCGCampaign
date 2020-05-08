package pwcg.testutils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignGeneratorModel;
import pwcg.campaign.CampaignHumanPilotHandler;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.SquadronManager;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.squadmember.AiPilotRemovalChooser;
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


    protected void makeProfile(SquadronTestProfile profile) throws PWCGException
    {
        CampaignGeneratorModel generatorModel = makeCampaignModelForProfile (profile);
        campaignProfiles.put(profile.getKey(), generatorModel);
    }

    @Override
    public Campaign makeCampaign(SquadronTestProfile profile) throws PWCGException
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
        
        PWCGContext.getInstance().setCampaign(campaign);
        validateCampaign(profile, campaign);
        
        return campaign;
    }
    
    private void validateCampaign(SquadronTestProfile profile, Campaign campaign) throws PWCGException
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
    public Campaign makeCampaignForceCreation(SquadronTestProfile profile) throws PWCGException
    {
        initialize();
        if (campaignProfiles.containsKey(profile.getKey()))
        {
            CampaignGeneratorModel model = campaignProfiles.get(profile.getKey());
            Campaign campaign = makeCampaignFromModel(model);
            campaignCache.put(profile.getKey(), campaign);
            
            if (profile.isCompetitive())
            {
	            List<SquadronMember> players = campaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList();
	            assert(players.size() == 1);
	            
	            addMorePilotsForCoop(campaign, "Squadron Mate", "Leutnant", SquadronTestProfile.COOP_COMPETITIVE_PROFILE.getSquadronId());
	            players = campaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList();
	            assert(players.size() == 2);
	            
	            addMorePilotsForCoop(campaign, "Friendly Fighter", "Leutnant", 20112052);
	            players = campaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList();
	            assert(players.size() == 3);
	            
	            addMorePilotsForCoop(campaign, "Friendly Bombermaj", "Major", 20131053);
	            players = campaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList();
	            assert(players.size() == 4);
	            
	            addMorePilotsForCoop(campaign, "Friendly Bombercpt", "Hauptmann", 20131053);
	            players = campaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList();
	            assert(players.size() == 5);
	            
	            addMorePilotsForCoop(campaign, "Friendly Divebomber", "Oberleutnant", 20122077);
	            players = campaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList();
	            assert(players.size() == 6);
	            
	            addMorePilotsForCoop(campaign, "Enemy Fighter", "Leyitenant", 10111011);
	            players = campaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList();
	            assert(players.size() == 7);
	            
	            addMorePilotsForCoop(campaign, "Enemy Bomber", "Major", 10131132);
	            players = campaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList();
	            assert(players.size() == 8);
	            
	            addMorePilotsForCoop(campaign, "Enemy Bomber", "Kapitan", 10131132);
	            players = campaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList();
	            assert(players.size() == 9);
	            
	            addMorePilotsForCoop(campaign, "Enemy Groundattack", "Starshyi Leyitenant", 10121503);
	            players = campaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList();
	            assert(players.size() == 10);
	            
            }
            return campaign;
        }
        
        throw new PWCGException("No campaign found for profile " + profile.getKey());
    }
    
    protected void addMorePilotsForCoop(Campaign campaign, String name, String rank, int squadronId) throws PWCGException
    {
        AiPilotRemovalChooser pilotRemovalChooser = new AiPilotRemovalChooser(campaign);
        SquadronMember squadronMemberToReplace = pilotRemovalChooser.findAiPilotToRemove(rank, squadronId);
        
        CampaignHumanPilotHandler humanPilotHandler = new CampaignHumanPilotHandler(campaign);
        humanPilotHandler.addNewPilot(
                name, 
                rank, 
                squadronMemberToReplace.getSerialNumber(), 
                squadronId);
    }
    
	public static CampaignGeneratorModel makeCampaignModelForProfile(SquadronTestProfile profile) throws PWCGException
    {
        SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
        Squadron squadron = squadronManager.getSquadron(profile.getSquadronId());
        
        Date campaignDate = DateUtils.getDateYYYYMMDD(profile.getDateString());
        ArmedService service = squadron.determineServiceForSquadron(campaignDate);
        String squadronName = squadron.determineDisplayName(campaignDate);
        
        IRankHelper rank = RankFactory.createRankHelper();
        String rankName = rank.getRankByService(2, service);
    
        CampaignGeneratorModel generatorModel = new CampaignGeneratorModel();
        generatorModel.setCampaignDate(campaignDate);
        generatorModel.setCampaignName(TEST_CAMPAIGN_NAME);
        generatorModel.setPlayerName(TEST_PLAYER_NAME);
        generatorModel.setPlayerRank(rankName);
        generatorModel.setPlayerRegion("");
        generatorModel.setService(service);
        generatorModel.setSquadronName(squadronName);
        generatorModel.setCampaignMode(profile.getCampaignMode());

        return generatorModel;
    }

}
