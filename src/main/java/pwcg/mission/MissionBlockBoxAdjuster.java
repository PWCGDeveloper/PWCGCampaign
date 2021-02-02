package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManager;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;

public class MissionBlockBoxAdjuster
{
    
    public static CoordinateBox getBordersForStructures(Mission mission) throws PWCGException
    {
        ConfigManagerCampaign configManager = mission.getCampaign().getCampaignConfigManager();
        String currentCpuAllowanceSetting = configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigStructuresKey);
        if   (currentCpuAllowanceSetting.equals(ConfigSimple.CONFIG_LEVEL_LOW))
        {
            return getLowStructureKeepBox(mission);
        }
        else if (currentCpuAllowanceSetting.equals(ConfigSimple.CONFIG_LEVEL_MED))
        {
            return getMediumStructureKeepBox(mission);
        }
        else if (currentCpuAllowanceSetting.equals(ConfigSimple.CONFIG_LEVEL_HIGH))
        {
            return getHighStructureKeepBox(mission);
        }
    
        return getLowStructureKeepBox(mission);
    }

    private static CoordinateBox getLowStructureKeepBox(Mission mission) throws PWCGException
    {
        CoordinateBox missionBorders = CoordinateBox.copy(mission.getMissionBorders());

        ConfigManager configManager = mission.getCampaign().getCampaignConfigManager();
        int keepGroupSpread = configManager.getIntConfigParam(ConfigItemKeys.KeepGroupSpreadKey);
        missionBorders = missionBorders.expandBox(keepGroupSpread);

        return missionBorders;
    }

    private static CoordinateBox getMediumStructureKeepBox(Mission mission) throws PWCGException
    {
        CoordinateBox missionBorders = expandMissionBordersForPlayerFields(mission);
        ConfigManager configManager = mission.getCampaign().getCampaignConfigManager();
        int keepGroupSpread = configManager.getIntConfigParam(ConfigItemKeys.KeepGroupSpreadKey);
        missionBorders = missionBorders.expandBox(keepGroupSpread);

        return missionBorders;
    }

    private static CoordinateBox getHighStructureKeepBox(Mission mission) throws PWCGException
    {
        CoordinateBox missionBorders = expandMissionBordersForPlayerFields(mission);
        ConfigManager configManager = mission.getCampaign().getCampaignConfigManager();
        int keepGroupSpread = configManager.getIntConfigParam(ConfigItemKeys.KeepGroupSpreadKey) * 3;
        missionBorders = missionBorders.expandBox(keepGroupSpread);

        return missionBorders;
    }

    private static CoordinateBox expandMissionBordersForPlayerFields(Mission mission) throws PWCGException
    {
        CoordinateBox missionBorders = CoordinateBox.copy(mission.getMissionBorders());
        
        List<Coordinate> airfieldCoordinates = new ArrayList<>();
        for (SquadronMember player : mission.getParticipatingPlayers().getAllParticipatingPlayers())
        {
            Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(player.getSquadronId());
            Airfield airfield = squadron.determineCurrentAirfieldAnyMap(mission.getCampaign().getDate());
            airfieldCoordinates.add(airfield.getPosition());
        }
        
        missionBorders.expandBoxCornersFromCoordinates(airfieldCoordinates);
        return missionBorders;
    }
}
