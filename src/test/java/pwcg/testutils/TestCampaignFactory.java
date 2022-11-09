package pwcg.testutils;

import java.util.Date;
import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignGenerator;
import pwcg.campaign.CampaignGeneratorModel;
import pwcg.campaign.CampaignHumanPilotHandler;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.squadmember.AiPilotRemovalChooser;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.squadron.SquadronManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class TestCampaignFactory implements ITestCampaignFactory
{
    @Override
    public Campaign makeCampaign(String campaignName, SquadronTestProfile profile) throws PWCGException
    {
        System.out.println("Create Test Campaign " + profile.getKey());

        CampaignGeneratorModel model = buildProfile(profile.getKey());
        Campaign campaign = makeCampaignFromModel(campaignName, model);

        if (profile.isCompetitive())
        {
            List<SquadronMember> players = campaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList();
            assert (players.size() == 1);

            addMorePilotsForCoop(campaign, "Squadron Mate", "Leutnant", SquadronTestProfile.COOP_COMPETITIVE_PROFILE.getSquadronId());
            players = campaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList();
            assert (players.size() == 2);

            addMorePilotsForCoop(campaign, "Friendly Fighter", "Leutnant", 20112052);
            players = campaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList();
            assert (players.size() == 3);

            addMorePilotsForCoop(campaign, "Friendly Bombermaj", "Major", 20131053);
            players = campaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList();
            assert (players.size() == 4);

            addMorePilotsForCoop(campaign, "Friendly Bombercpt", "Hauptmann", 20131053);
            players = campaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList();
            assert (players.size() == 5);

            addMorePilotsForCoop(campaign, "Friendly Divebomber", "Oberleutnant", 20122077);
            players = campaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList();
            assert (players.size() == 6);

            addMorePilotsForCoop(campaign, "Enemy Fighter", "Leyitenant", 10111011);
            players = campaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList();
            assert (players.size() == 7);

            addMorePilotsForCoop(campaign, "Enemy Bomber", "Major", 10131132);
            players = campaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList();
            assert (players.size() == 8);

            addMorePilotsForCoop(campaign, "Enemy Bomber", "Kapitan", 10131132);
            players = campaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList();
            assert (players.size() == 9);

            addMorePilotsForCoop(campaign, "Enemy Groundattack", "Starshyi Leyitenant", 10121503);
            players = campaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList();
            assert (players.size() == 10);

        }

        return campaign;
    }
    
    
    private Campaign makeCampaignFromModel(String campaignName, CampaignGeneratorModel generatorModel) throws PWCGException
    {
        generatorModel.setCampaignName(campaignName);
        CampaignGenerator generator = new CampaignGenerator(generatorModel);
        Campaign campaign = generator.generate(generatorModel.getProduct());          
        return campaign;
    }

    private CampaignGeneratorModel buildProfile(String profileKey) throws PWCGException
    {
        for (SquadronTestProfile profile : SquadronTestProfile.values())
        {
            if (profile.getKey().equals(profileKey))
            {
                CampaignGeneratorModel generatorModel = makeCampaignModelForProfile(profile);
                return generatorModel;
            }
        }
        throw new PWCGException("No profile for key " + profileKey) ;        
    }


    private void addMorePilotsForCoop(Campaign campaign, String name, String rank, int squadronId) throws PWCGException
    {
        AiPilotRemovalChooser pilotRemovalChooser = new AiPilotRemovalChooser(campaign);
        SquadronMember squadronMemberToReplace = pilotRemovalChooser.findAiPilotToRemove(rank, squadronId);

        CampaignHumanPilotHandler humanPilotHandler = new CampaignHumanPilotHandler(campaign);
        humanPilotHandler.addNewPilot(name, rank, squadronMemberToReplace.getSerialNumber(), squadronId);
    }

    private CampaignGeneratorModel makeCampaignModelForProfile(SquadronTestProfile profile) throws PWCGException
    {
        SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
        Squadron squadron = squadronManager.getSquadron(profile.getSquadronId());

        Date campaignDate = DateUtils.getDateYYYYMMDD(profile.getDateString());
        ArmedService service = squadron.determineServiceForSquadron(campaignDate);
        String squadronName = squadron.determineDisplayName(campaignDate);

        IRankHelper rank = RankFactory.createRankHelper();
        String rankName = rank.getRankByService(2, service);

        CampaignGeneratorModel generatorModel = new CampaignGeneratorModel(FrontMapIdentifier.getProductForMap(profile.getMapIdentifier()));
        generatorModel.setCampaignDate(campaignDate);
        generatorModel.setPlayerName("Johnny Player");
        generatorModel.setPlayerRank(rankName);
        generatorModel.setPlayerRegion("");
        generatorModel.setService(service);
        generatorModel.setSquadronName(squadronName);
        generatorModel.setCampaignMode(profile.getCampaignMode());

        return generatorModel;
    }

}
