package pwcg.mission;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManager;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.CoordinateBox;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StructureBorderBuilderTest
{
    private Campaign campaign;
    
    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        PWCGContext.getInstance().setCurrentMap(FrontMapIdentifier.STALINGRAD_MAP);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.GROSS_DEUTSCHLAND_PROFILE);
    }

    @Test
    public void structureLevelLow() throws PWCGException
    {
        PWCGContext.getInstance().setCampaign(campaign);
        MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();

        campaign.getCampaignConfigManager().setConfigParam(ConfigItemKeys.SimpleConfigStructuresKey, ConfigSimple.CONFIG_LEVEL_LOW);
        
        CrewMember player = campaign.findReferencePlayer();
        participatingPlayers.addCrewMember(player);

        MissionBorderBuilder missionBorderBuilder = new MissionBorderBuilder(campaign, participatingPlayers, null);
        CoordinateBox missionBorders = missionBorderBuilder.buildCoordinateBox();

        StructureBorderBuilder structureBorderBuilder = new StructureBorderBuilder(campaign, missionBorders);
        CoordinateBox structureBorder = structureBorderBuilder.buildBorderForMission();

        ConfigManager configManager = campaign.getCampaignConfigManager();
        int keepGroupSpread = configManager.getIntConfigParam(ConfigItemKeys.KeepGroupSpreadKey);

        assert((missionBorders.getBoxHeight() + keepGroupSpread) <= structureBorder.getBoxHeight());
        assert((missionBorders.getBoxWidth() + keepGroupSpread) <= structureBorder.getBoxWidth());
    }

    @Test
    public void structureLevelMedium() throws PWCGException
    {
        PWCGContext.getInstance().setCampaign(campaign);
        MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();

        campaign.getCampaignConfigManager().setConfigParam(ConfigItemKeys.SimpleConfigStructuresKey, ConfigSimple.CONFIG_LEVEL_MED);
        
        CrewMember player = campaign.findReferencePlayer();
        participatingPlayers.addCrewMember(player);

        MissionBorderBuilder missionBorderBuilder = new MissionBorderBuilder(campaign, participatingPlayers, null);
        CoordinateBox missionBorders = missionBorderBuilder.buildCoordinateBox();

        StructureBorderBuilder structureBorderBuilder = new StructureBorderBuilder(campaign, missionBorders);
        CoordinateBox structureBorder = structureBorderBuilder.buildBorderForMission();

        ConfigManager configManager = campaign.getCampaignConfigManager();
        int keepGroupSpread = configManager.getIntConfigParam(ConfigItemKeys.KeepGroupSpreadKey) * 2;

        assert((missionBorders.getBoxHeight() + keepGroupSpread) <= structureBorder.getBoxHeight());
        assert((missionBorders.getBoxWidth() + keepGroupSpread) <= structureBorder.getBoxWidth());
    }

    @Test
    public void structureLevelHigh() throws PWCGException
    {
        PWCGContext.getInstance().setCampaign(campaign);
        MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();

        campaign.getCampaignConfigManager().setConfigParam(ConfigItemKeys.SimpleConfigStructuresKey, ConfigSimple.CONFIG_LEVEL_MED);
        
        CrewMember player = campaign.findReferencePlayer();
        participatingPlayers.addCrewMember(player);

        MissionBorderBuilder missionBorderBuilder = new MissionBorderBuilder(campaign, participatingPlayers, null);
        CoordinateBox missionBorders = missionBorderBuilder.buildCoordinateBox();

        StructureBorderBuilder structureBorderBuilder = new StructureBorderBuilder(campaign, missionBorders);
        CoordinateBox structureBorder = structureBorderBuilder.buildBorderForMission();

        ConfigManager configManager = campaign.getCampaignConfigManager();
        int keepGroupSpread = configManager.getIntConfigParam(ConfigItemKeys.KeepGroupSpreadKey) * 3;

        assert((missionBorders.getBoxHeight() + keepGroupSpread) <= structureBorder.getBoxHeight());
        assert((missionBorders.getBoxWidth() + keepGroupSpread) <= structureBorder.getBoxWidth());
    }
}
