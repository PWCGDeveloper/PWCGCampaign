package pwcg.gui.iconicbattles;

import java.util.Date;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignGenerator;
import pwcg.campaign.CampaignGeneratorModel;
import pwcg.campaign.CampaignMode;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.skirmish.IconicMissionsManager;
import pwcg.campaign.skirmish.IconicSingleMission;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.dialogs.HelpDialog;
import pwcg.gui.rofmap.brief.BriefingDescriptionScreen;
import pwcg.gui.rofmap.brief.CampaignHomeGuiBriefingWrapper;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.MissionHumanParticipants;
import pwcg.mission.aaatruck.AAATruckMissionPostProcessor;
import pwcg.mission.ground.vehicle.VehicleDefinition;
import pwcg.mission.ground.vehicle.VehicleDefinitionManager;
import pwcg.mission.io.MissionFileWriter;

public class IconicBattlesGenerator 
{
    private IconicBattlesGeneratorData iconicBattleData = new IconicBattlesGeneratorData();

    public IconicBattlesGenerator(IconicBattlesGeneratorData iconicBattleData)
    {
        this.iconicBattleData = iconicBattleData;
    }
    
    public void generateIconicMission() throws PWCGException
    {
        if (iconicBattleData.getSelectedSquadron() > 0)
        {
            setRequestedVehicle();
            Campaign campaign = generateCampaign();
            if (isAAATruckMission())
            {
                validateAAATruckMission();
                Mission mission = generateMission(campaign);
                finishAAATruckMission(mission);
                new  HelpDialog("AAA vehicle mission has been saved:" + MissionFileWriter.getMissionFileName(campaign));
            }
            else
            {
                CampaignHomeGuiBriefingWrapper campaignHomeGuiBriefingWrapper = new CampaignHomeGuiBriefingWrapper(null);
                Mission mission = generateMission(campaign);
                BriefingDescriptionScreen briefingMap = new BriefingDescriptionScreen(campaignHomeGuiBriefingWrapper, mission);
                briefingMap.makePanels();
                CampaignGuiContextManager.getInstance().pushToContextStack(briefingMap);
            }
        }
    }

    private void validateAAATruckMission() throws PWCGException
    {
        if (iconicBattleData.getIconicBattleKey().contains("19431020"))
        {
            throw new PWCGException("How are you going to do anti shipping from a tank?");
        }
    }

    private Campaign generateCampaign() throws PWCGException
    {
        CampaignGeneratorModel model = makeCampaignModelForProfile();        
        CampaignGenerator generator = new CampaignGenerator(model);
        Campaign campaign = generator.generate();          
        PWCGContext.getInstance().setCampaign(campaign);
        return campaign;
    }

    private CampaignGeneratorModel makeCampaignModelForProfile() throws PWCGException
    {
        IconicSingleMission iconicMission = IconicMissionsManager.getInstance().getSelectedMissionProfile(iconicBattleData.getIconicBattleKey());
        Squadron squadron = findPlayerSquadronForMission();
        Date campaignDate = DateUtils.getDateYYYYMMDD(iconicBattleData.getIconicBattleKey());

        ArmedService service = squadron.determineServiceForSquadron(campaignDate);
        String squadronName = squadron.determineDisplayName(campaignDate);
        
        IRankHelper rank = RankFactory.createRankHelper();
        String rankName = rank.getRankByService(2, service);
    
        CampaignGeneratorModel generatorModel = new CampaignGeneratorModel();
        generatorModel.setCampaignDate(campaignDate);
        generatorModel.setCampaignName(iconicMission.getMapName() + " " + iconicMission.getCampaignName());
        generatorModel.setPlayerName("Iconic Player");
        generatorModel.setPlayerRank(rankName);
        generatorModel.setPlayerRegion("");
        generatorModel.setService(service);
        generatorModel.setSquadronName(squadronName);
        generatorModel.setCampaignMode(CampaignMode.CAMPAIGN_MODE_SINGLE);

        return generatorModel;
    }

    private Squadron findPlayerSquadronForMission() throws PWCGException
    {
        Squadron playerSquadron = null;
        if (isAAATruckMission())
        {
            playerSquadron = getSquadronForAAATruck();
        }
        else
        {
            playerSquadron = PWCGContext.getInstance().getSquadronManager().getSquadron(iconicBattleData.getSelectedSquadron());
        }
        
        return playerSquadron;
    }

    private Squadron getSquadronForAAATruck() throws PWCGException
    {
        Side truckSide = getTruckSide();
        IconicSingleMission iconicMission = IconicMissionsManager.getInstance().getSelectedMissionProfile(iconicBattleData.getIconicBattleKey());
        for (Integer squadronId : iconicMission.getIconicBattleParticipants())
        {
            Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(squadronId);
            if (squadron.determineSide() == truckSide)
            {
                return squadron;
            }
        }
        throw new PWCGException("Could not find stand in squadron for Truck AAA mission");
    }

    private MissionHumanParticipants buildTestParticipatingHumans(Campaign campaign) throws PWCGException
    {
        MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();
        for (SquadronMember player: campaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList())
        {
            participatingPlayers.addSquadronMember(player);
        }
        return participatingPlayers;
    }

    private Mission generateMission(Campaign campaign) throws PWCGException
    {
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = null;
        if (isAAATruckMission())
        {
            mission = missionGenerator.makeAAAMission(buildTestParticipatingHumans(campaign), iconicBattleData.getPlayerVehicleDefinition());
        }
        else
        {
            mission = missionGenerator.makeMission(buildTestParticipatingHumans(campaign));
        }
                
        return mission;
    }

    private void finishAAATruckMission(Mission mission) throws PWCGException
    {
        Side vehicleSide = getTruckSide();
        mission.finalizeMission();
        
        AAATruckMissionPostProcessor aaaPostProcessor = new AAATruckMissionPostProcessor(mission);
        aaaPostProcessor.convertToPlayerVehicleMission(vehicleSide);
        mission.writeGameMissionFiles();
    }
    
    
    private void setRequestedVehicle() throws PWCGException
    {
        VehicleDefinitionManager vehicleDefinitionManager = PWCGContext.getInstance().getVehicleDefinitionManager();
        for (VehicleDefinition vehicleDefinition : vehicleDefinitionManager.getAllVehicleDefinitions())
        {
            if (vehicleDefinition != null && vehicleDefinition.getVehicleName().startsWith(iconicBattleData.getSelectedVehicleOrSquadron()))
            {
                iconicBattleData.setPlayerVehicleDefinition(vehicleDefinition);
            }
        }
    }

    private Side getTruckSide()
    {
        Side truckSide = Side.AXIS;
        if (iconicBattleData.getSelectedSquadron() == IconicBattlesGUI.ALLIED_STAND_IN_SQUADRON_ID)
        {
            truckSide = Side.ALLIED;
        }
        return truckSide;
    }
    
    private boolean isAAATruckMission()
    {
        if (iconicBattleData.getSelectedSquadron() == IconicBattlesGUI.ALLIED_STAND_IN_SQUADRON_ID || 
            iconicBattleData.getSelectedSquadron() == IconicBattlesGUI.AXIS_STAND_IN_SQUADRON_ID)
        {
            return true;
        }
        return false;
    }
}
