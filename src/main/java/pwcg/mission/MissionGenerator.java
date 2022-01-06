package pwcg.mission;

import java.util.HashMap;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.skirmish.Skirmish;
import pwcg.campaign.skirmish.SkirmishBuilder;
import pwcg.campaign.tank.PwcgRole;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.CoordinateBox;
import pwcg.mission.ground.vehicle.VehicleDefinition;
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
        return this.makeMission(participatingPlayers, new HashMap<>());
    }
    
    public Mission makeMission(MissionHumanParticipants participatingPlayers, Map<Integer, PwcgRole> squadronRoleOverride) throws PWCGException
    {
        MissionOptions missionOptions = new MissionOptions(campaign.getDate());
        missionOptions.createFlightSpecificMissionOptions();

        MissionWeather weather = new MissionWeather(campaign, missionOptions.getMissionHour());
        weather.createMissionWeather();

        Skirmish skirmish = getSkirmishForMission(participatingPlayers);
        MissionSquadronFlightTypes playerFlightTypes = PlayerFlightTypeBuilder.buildPlayerFlightTypes(campaign, participatingPlayers, weather, skirmish, squadronRoleOverride);
        
        VehicleDefinition playerVehicleDefinition = null;
        Mission mission = buildMission(participatingPlayers, playerFlightTypes, playerVehicleDefinition, weather, skirmish, missionOptions);
        
        return mission;
    }

    public Mission makeTestMissionFromFlightTypeWithSkirmish(
            MissionHumanParticipants participatingPlayers, 
            MissionSquadronFlightTypes playerFlightTypes,
            Skirmish skirmish) throws PWCGException
    {
        MissionOptions missionOptions = new MissionOptions(campaign.getDate());
        missionOptions.createFlightSpecificMissionOptions();

        campaign.getCampaignConfigManager().setParam(ConfigItemKeys.UseRealisticWeatherKey, "0");
        MissionWeather weather = new MissionWeather(campaign, missionOptions.getMissionHour());
        weather.createMissionWeather();

        VehicleDefinition playerVehicleDefinition = null;
        Mission mission = buildMission(participatingPlayers, playerFlightTypes, playerVehicleDefinition, weather, skirmish, missionOptions);
        return mission;
    }

    private Mission buildMission(
            MissionHumanParticipants participatingPlayers, 
            MissionSquadronFlightTypes playerFlightTypes, 
            VehicleDefinition playerVehicleDefinition,
            MissionWeather weather, 
            Skirmish skirmish,
            MissionOptions missionOptions) throws PWCGException
    {
        campaign.setCurrentMission(null);
        
        CoordinateBox missionBorders = buildMissionBorders(participatingPlayers, skirmish, playerFlightTypes);
        Mission mission = new Mission(campaign, participatingPlayers, playerVehicleDefinition, missionBorders, weather, skirmish, missionOptions);
        campaign.setCurrentMission(mission);
        mission.generate(playerFlightTypes);

        return mission;
    }
    
    private Skirmish getSkirmishForMission(MissionHumanParticipants participatingPlayers) throws PWCGException
    {
        SkirmishBuilder skirmishBuilder = new SkirmishBuilder(campaign, participatingPlayers);
        return skirmishBuilder.chooseBestSkirmish();
    }

    private CoordinateBox buildMissionBorders(MissionHumanParticipants participatingPlayers, Skirmish skirmish, MissionSquadronFlightTypes playerFlightTypes) throws PWCGException
    {
        MissionBorderBuilder missionBorderBuilder = new MissionBorderBuilder(campaign, participatingPlayers, skirmish, playerFlightTypes);
        CoordinateBox missionBorders = missionBorderBuilder.buildCoordinateBox();
        return missionBorders;
    }
}
