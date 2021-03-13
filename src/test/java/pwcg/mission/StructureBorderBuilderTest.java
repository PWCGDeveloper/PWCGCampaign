package pwcg.mission;

import org.junit.Before;
import org.junit.Test;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManager;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.CoordinateBox;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

public class StructureBorderBuilderTest
{
    @Before
    public void fighterFlightTests() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
    }

    @Test
    public void structureLevelLow() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_STALINGRAD);
        PWCGContext.getInstance().setCampaign(campaign);
        MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();

        campaign.getCampaignConfigManager().setConfigParam(ConfigItemKeys.SimpleConfigStructuresKey, ConfigSimple.CONFIG_LEVEL_LOW);
        
        SquadronMember player = campaign.findReferencePlayer();
        participatingPlayers.addSquadronMember(player);

        MissionBorderBuilder missionBorderBuilder = new MissionBorderBuilder(campaign, participatingPlayers);
        CoordinateBox missionBorders = missionBorderBuilder.buildCoordinateBox();

        StructureBorderBuilder structureBorderBuilder = new StructureBorderBuilder(campaign, participatingPlayers, missionBorders);
        CoordinateBox structureBorder = structureBorderBuilder.getBordersForStructures();

        ConfigManager configManager = campaign.getCampaignConfigManager();
        int keepGroupSpread = configManager.getIntConfigParam(ConfigItemKeys.KeepGroupSpreadKey);

        assert((missionBorders.getBoxHeight() + keepGroupSpread) <= structureBorder.getBoxHeight());
        assert((missionBorders.getBoxWidth() + keepGroupSpread) <= structureBorder.getBoxWidth());
        assert(missionBorders.getCenter().getXPos() == structureBorder.getCenter().getXPos());
    }

    @Test
    public void structureLevelMedium() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_STALINGRAD);
        PWCGContext.getInstance().setCampaign(campaign);
        MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();

        campaign.getCampaignConfigManager().setConfigParam(ConfigItemKeys.SimpleConfigStructuresKey, ConfigSimple.CONFIG_LEVEL_MED);
        
        SquadronMember player = campaign.findReferencePlayer();
        participatingPlayers.addSquadronMember(player);

        MissionBorderBuilder missionBorderBuilder = new MissionBorderBuilder(campaign, participatingPlayers);
        CoordinateBox missionBorders = missionBorderBuilder.buildCoordinateBox();

        StructureBorderBuilder structureBorderBuilder = new StructureBorderBuilder(campaign, participatingPlayers, missionBorders);
        CoordinateBox structureBorder = structureBorderBuilder.getBordersForStructures();

        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(player.getSquadronId());
        assert(structureBorder.isInBox(squadron.determineCurrentPosition(campaign.getDate())));

        ConfigManager configManager = campaign.getCampaignConfigManager();
        int keepGroupSpread = configManager.getIntConfigParam(ConfigItemKeys.KeepGroupSpreadKey) * 2;

        assert((missionBorders.getBoxHeight() + keepGroupSpread) <= structureBorder.getBoxHeight());
        assert((missionBorders.getBoxWidth() + keepGroupSpread) <= structureBorder.getBoxWidth());
    }

    @Test
    public void structureLevelHigh() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_STALINGRAD);
        PWCGContext.getInstance().setCampaign(campaign);
        MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();

        campaign.getCampaignConfigManager().setConfigParam(ConfigItemKeys.SimpleConfigStructuresKey, ConfigSimple.CONFIG_LEVEL_HIGH);
        
        SquadronMember player = campaign.findReferencePlayer();
        participatingPlayers.addSquadronMember(player);

        MissionBorderBuilder missionBorderBuilder = new MissionBorderBuilder(campaign, participatingPlayers);
        CoordinateBox missionBorders = missionBorderBuilder.buildCoordinateBox();

        StructureBorderBuilder structureBorderBuilder = new StructureBorderBuilder(campaign, participatingPlayers, missionBorders);
        CoordinateBox structureBorder = structureBorderBuilder.getBordersForStructures();

        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(player.getSquadronId());
        assert(structureBorder.isInBox(squadron.determineCurrentPosition(campaign.getDate())));

        ConfigManager configManager = campaign.getCampaignConfigManager();
        int keepGroupSpread = configManager.getIntConfigParam(ConfigItemKeys.KeepGroupSpreadKey) * 3;

        assert((missionBorders.getBoxHeight() + keepGroupSpread) <= structureBorder.getBoxHeight());
        assert((missionBorders.getBoxWidth() + keepGroupSpread) <= structureBorder.getBoxWidth());
    }

    @Test
    public void structureLevelCompare() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_STALINGRAD);
        PWCGContext.getInstance().setCampaign(campaign);
        MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();
        
        SquadronMember player = campaign.findReferencePlayer();
        participatingPlayers.addSquadronMember(player);

        MissionBorderBuilder missionBorderBuilder = new MissionBorderBuilder(campaign, participatingPlayers);
        CoordinateBox missionBorders = missionBorderBuilder.buildCoordinateBox();

        StructureBorderBuilder structureBorderBuilder = new StructureBorderBuilder(campaign, participatingPlayers, missionBorders);
        
        campaign.getCampaignConfigManager().setConfigParam(ConfigItemKeys.SimpleConfigStructuresKey, ConfigSimple.CONFIG_LEVEL_LOW);
        CoordinateBox structureBorderLow = structureBorderBuilder.getBordersForStructures();
        
        campaign.getCampaignConfigManager().setConfigParam(ConfigItemKeys.SimpleConfigStructuresKey, ConfigSimple.CONFIG_LEVEL_MED);
        CoordinateBox structureBorderMed = structureBorderBuilder.getBordersForStructures();
        
        campaign.getCampaignConfigManager().setConfigParam(ConfigItemKeys.SimpleConfigStructuresKey, ConfigSimple.CONFIG_LEVEL_HIGH);
        CoordinateBox structureBorderHigh = structureBorderBuilder.getBordersForStructures();

        assert(structureBorderLow.getBoxHeight() <= structureBorderMed.getBoxHeight());
        assert(structureBorderLow.getBoxWidth() <= structureBorderMed.getBoxWidth());

        assert(structureBorderMed.getBoxHeight() <= structureBorderHigh.getBoxHeight());
        assert(structureBorderMed.getBoxWidth() <= structureBorderHigh.getBoxWidth());
        
        ConfigManager configManager = campaign.getCampaignConfigManager();
        int keepGroupSpread = configManager.getIntConfigParam(ConfigItemKeys.KeepGroupSpreadKey);

        assert((structureBorderLow.getBoxHeight() + keepGroupSpread) <= structureBorderMed.getBoxHeight());
        assert((structureBorderLow.getBoxWidth() + keepGroupSpread) <= structureBorderMed.getBoxWidth());

        assert((structureBorderMed.getBoxHeight() + keepGroupSpread) <= structureBorderHigh.getBoxHeight());
        assert((structureBorderMed.getBoxWidth() + keepGroupSpread) <= structureBorderHigh.getBoxWidth());
    }

}
