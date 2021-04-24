package pwcg.testutils;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.CoordinateBox;
import pwcg.mission.Mission;
import pwcg.mission.MissionHumanParticipants;
import pwcg.mission.MissionProfile;
import pwcg.mission.StructureBorderBuilder;
import pwcg.mission.options.MissionOptions;
import pwcg.mission.options.MissionWeather;

public class TestMissionBuilderUtility
{

    public static Mission createTestMission(Campaign campaign, MissionHumanParticipants participatingPlayers, CoordinateBox missionBorders,
            MissionProfile missionProfile) throws PWCGException
    {
        MissionOptions missionOptions = new MissionOptions(campaign.getDate(), missionProfile);
        missionOptions.createFlightSpecificMissionOptions();

        campaign.getCampaignConfigManager().setParam(ConfigItemKeys.UseRealisticWeatherKey, "0");
        MissionWeather weather = new MissionWeather(campaign, missionOptions.getMissionHour());
        weather.createMissionWeather();
        
        StructureBorderBuilder structureBorderBuilder = new StructureBorderBuilder(campaign, participatingPlayers, missionBorders);
        CoordinateBox structureBorders = structureBorderBuilder.getBordersForStructures();
        
        Mission mission = new Mission(campaign, missionProfile, participatingPlayers, missionBorders, structureBorders, weather, null, missionOptions);
        campaign.setCurrentMission(mission);
        return mission;
    }

    public static MissionHumanParticipants buildTestParticipatingHumans(Campaign campaign) throws PWCGException
    {
        MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();
        for (SquadronMember player: campaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList())
        {
            participatingPlayers.addSquadronMember(player);
        }
        return participatingPlayers;
    }
}
