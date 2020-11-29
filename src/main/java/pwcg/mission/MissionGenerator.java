package pwcg.mission;

import java.util.Arrays;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.plane.Role;
import pwcg.campaign.utils.TestDriver;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.CoordinateBox;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.options.MissionOptions;
import pwcg.mission.options.MissionWeather;

public class MissionGenerator
{
    private Campaign campaign = null;

    public MissionGenerator(Campaign campaign)
    {
        this.campaign = campaign;
    }

    public Mission makeMission(MissionHumanParticipants participatingPlayers) throws PWCGException
    {
        MissionProfile missionProfile = generateProfile(participatingPlayers);

        MissionOptions missionOptions = new MissionOptions(campaign.getDate(), missionProfile);
        missionOptions.createFlightSpecificMissionOptions();

        MissionWeather weather = new MissionWeather(campaign, missionOptions.getMissionHour());
        weather.createMissionWeather();

        List<FlightTypes> playerFlightTypes = PlayerFlightTypeBuilder.finalizePlayerFlightTypes(campaign, participatingPlayers, missionProfile, weather);

        if (TestDriver.getInstance().isEnabled())
        {
            playerFlightTypes = Arrays.asList(TestDriver.getInstance().getTestFlightTypeForRole(Role.ROLE_FIGHTER).playerFlightType);
        }

        Mission mission = buildMission(participatingPlayers, playerFlightTypes, missionProfile, weather, missionOptions);
        return mission;
    }

    public Mission makeLoneWolfMission(MissionHumanParticipants participatingPlayers) throws PWCGException
    {
        MissionProfile missionProfile = MissionProfile.DAY_TACTICAL_MISSION;
        
        MissionOptions missionOptions = new MissionOptions(campaign.getDate(), missionProfile);
        missionOptions.createFlightSpecificMissionOptions();

        MissionWeather weather = new MissionWeather(campaign, missionOptions.getMissionHour());
        weather.createMissionWeather();

        List<FlightTypes> playerFlightTypes = Arrays.asList(FlightTypes.LONE_WOLF);
        Mission mission = buildMission(participatingPlayers, playerFlightTypes, MissionProfile.DAY_TACTICAL_MISSION, weather, missionOptions);
        return mission;
    }

    public Mission makeTestSingleMissionFromFlightType(MissionHumanParticipants participatingPlayers, FlightTypes playerFlightType,
            MissionProfile missionProfile) throws PWCGException
    {
        MissionOptions missionOptions = new MissionOptions(campaign.getDate(), missionProfile);
        missionOptions.createFlightSpecificMissionOptions();

        campaign.getCampaignConfigManager().setParam(ConfigItemKeys.UseRealisticWeatherKey, "0");
        MissionWeather weather = new MissionWeather(campaign, missionOptions.getMissionHour());
        weather.createMissionWeather();

        List<FlightTypes> playerFlightTypes = Arrays.asList(playerFlightType);
        Mission mission = buildMission(participatingPlayers, playerFlightTypes, missionProfile, weather, missionOptions);
        return mission;
    }

    public Mission makeTestCoopMissionFromFlightType(MissionHumanParticipants participatingPlayers, List<FlightTypes> playerFlightTypes,
            MissionProfile missionProfile) throws PWCGException
    {
        MissionOptions missionOptions = new MissionOptions(campaign.getDate(), missionProfile);
        missionOptions.createFlightSpecificMissionOptions();

        campaign.getCampaignConfigManager().setParam(ConfigItemKeys.UseRealisticWeatherKey, "0");
        MissionWeather weather = new MissionWeather(campaign, missionOptions.getMissionHour());
        weather.createMissionWeather();

        Mission mission = buildMission(participatingPlayers, playerFlightTypes, missionProfile, weather, missionOptions);
        return mission;
    }

    private MissionProfile generateProfile(MissionHumanParticipants participatingPlayers) throws PWCGException
    {
        MissionProfileGenerator missionProfileGenerator = new MissionProfileGenerator(campaign, participatingPlayers);
        MissionProfile missionProfile = missionProfileGenerator.generateMissionProfile();
        return missionProfile;
    }

    private Mission buildMission(MissionHumanParticipants participatingPlayers, List<FlightTypes> playerFlightTypes, MissionProfile missionProfile,
            MissionWeather weather, MissionOptions missionOptions) throws PWCGException
    {
        campaign.setCurrentMission(null);
        CoordinateBox missionBorders = buildMissionBorders(missionProfile, participatingPlayers);
        Mission mission = new Mission(campaign, missionProfile, participatingPlayers, missionBorders, weather, missionOptions);
        campaign.setCurrentMission(mission);
        mission.generate(playerFlightTypes);

        return mission;
    }

    private CoordinateBox buildMissionBorders(MissionProfile missionProfile, MissionHumanParticipants participatingPlayers) throws PWCGException
    {
        MissionBorderBuilder missionBorderBuilder = new MissionBorderBuilder(campaign, participatingPlayers);
        CoordinateBox missionBorders = missionBorderBuilder.buildCoordinateBox();
        return missionBorders;
    }
}
