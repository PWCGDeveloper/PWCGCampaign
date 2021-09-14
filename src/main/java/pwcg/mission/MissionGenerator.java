package pwcg.mission;

import java.util.HashMap;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.plane.PwcgRole;
import pwcg.campaign.skirmish.Skirmish;
import pwcg.campaign.skirmish.SkirmishBuilder;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.CoordinateBox;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.ground.vehicle.VehicleDefinition;
import pwcg.mission.options.MissionOptions;
import pwcg.mission.options.MissionType;
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
        MissionProfile missionProfile = generateProfile(participatingPlayers);

        MissionOptions missionOptions = new MissionOptions(campaign.getDate(), missionProfile);
        missionOptions.createFlightSpecificMissionOptions();

        MissionWeather weather = new MissionWeather(campaign, missionOptions.getMissionHour());
        weather.createMissionWeather();

        Skirmish skirmish = getSkirmishForMission(participatingPlayers);
        MissionSquadronFlightTypes playerFlightTypes = PlayerFlightTypeBuilder.buildPlayerFlightTypes(campaign, participatingPlayers, missionProfile, weather, skirmish, squadronRoleOverride);
        
        VehicleDefinition playerVehicleDefinition = null;
        Mission mission = buildMission(participatingPlayers, playerFlightTypes, playerVehicleDefinition, missionProfile, weather, skirmish, missionOptions);
        
        return mission;
    }

    public Mission makeAAAMission(MissionHumanParticipants participatingPlayers, VehicleDefinition playerVehicleDefinition) throws PWCGException
    {
        MissionProfile missionProfile = generateProfile(participatingPlayers);

        MissionOptions missionOptions = new MissionOptions(campaign.getDate(), missionProfile);
        missionOptions.createFlightSpecificMissionOptions();
        missionOptions.setMissionType(MissionType.SINGLE_AAA_MISSION);

        MissionWeather weather = new MissionWeather(campaign, missionOptions.getMissionHour());
        weather.createMissionWeather();

        Skirmish skirmish = getSkirmishForMission(participatingPlayers);
        MissionSquadronFlightTypes playerFlightTypes = PlayerFlightTypeBuilder.buildPlayerFlightTypes(campaign, participatingPlayers, missionProfile, weather, skirmish, new HashMap<>());
        
        Mission mission = buildMission(participatingPlayers, playerFlightTypes, playerVehicleDefinition, missionProfile, weather, skirmish, missionOptions);
        
        return mission;
    }

    public Mission makeMissionwithSpecifiedType(MissionHumanParticipants participatingPlayers, FlightTypes playerFlightType) throws PWCGException
    {
        MissionProfile missionProfile = MissionProfile.DAY_TACTICAL_MISSION;
        
        MissionOptions missionOptions = new MissionOptions(campaign.getDate(), missionProfile);
        missionOptions.createFlightSpecificMissionOptions();

        MissionWeather weather = new MissionWeather(campaign, missionOptions.getMissionHour());
        weather.createMissionWeather();

        Squadron playerSquadron = participatingPlayers.getAllParticipatingPlayers().get(0).determineSquadron();
        MissionSquadronFlightTypes playerFlightTypes = MissionSquadronFlightTypes.buildPlayerFlightType(playerFlightType, playerSquadron);

        Skirmish skirmish = null;
        VehicleDefinition playerVehicleDefinition = null;
        Mission mission = buildMission(participatingPlayers, playerFlightTypes, playerVehicleDefinition, MissionProfile.DAY_TACTICAL_MISSION, weather, skirmish, missionOptions);
        return mission;
    }

    public Mission makeTestSingleMissionFromFlightType(MissionHumanParticipants participatingPlayers, FlightTypes playerFlightType, MissionProfile missionProfile) throws PWCGException
    {
        MissionOptions missionOptions = new MissionOptions(campaign.getDate(), missionProfile);
        missionOptions.createFlightSpecificMissionOptions();

        campaign.getCampaignConfigManager().setParam(ConfigItemKeys.UseRealisticWeatherKey, "0");
        MissionWeather weather = new MissionWeather(campaign, missionOptions.getMissionHour());
        weather.createMissionWeather();

        Squadron playerSquadron = participatingPlayers.getAllParticipatingPlayers().get(0).determineSquadron();
        MissionSquadronFlightTypes playerFlightTypes = MissionSquadronFlightTypes.buildPlayerFlightType(playerFlightType, playerSquadron);
        
        Skirmish skirmish = null;
        VehicleDefinition playerVehicleDefinition = null;
        Mission mission = buildMission(participatingPlayers, playerFlightTypes, playerVehicleDefinition, missionProfile, weather, skirmish, missionOptions);
        return mission;
    }

    public Mission makeTestCoopMissionFromFlightType(MissionHumanParticipants participatingPlayers, MissionSquadronFlightTypes playerFlightTypes, MissionProfile missionProfile) throws PWCGException
    {
        MissionOptions missionOptions = new MissionOptions(campaign.getDate(), missionProfile);
        missionOptions.createFlightSpecificMissionOptions();

        campaign.getCampaignConfigManager().setParam(ConfigItemKeys.UseRealisticWeatherKey, "0");
        MissionWeather weather = new MissionWeather(campaign, missionOptions.getMissionHour());
        weather.createMissionWeather();

        Skirmish skirmish = null;
        VehicleDefinition playerVehicleDefinition = null;
        Mission mission = buildMission(participatingPlayers, playerFlightTypes, playerVehicleDefinition, missionProfile, weather, skirmish, missionOptions);
        return mission;
    }

    public Mission makeTestMissionFromFlightTypeWithSkirmish(
            MissionHumanParticipants participatingPlayers, 
            MissionSquadronFlightTypes playerFlightTypes,
            MissionProfile missionProfile,
            Skirmish skirmish) throws PWCGException
    {
        MissionOptions missionOptions = new MissionOptions(campaign.getDate(), missionProfile);
        missionOptions.createFlightSpecificMissionOptions();

        campaign.getCampaignConfigManager().setParam(ConfigItemKeys.UseRealisticWeatherKey, "0");
        MissionWeather weather = new MissionWeather(campaign, missionOptions.getMissionHour());
        weather.createMissionWeather();

        VehicleDefinition playerVehicleDefinition = null;
        Mission mission = buildMission(participatingPlayers, playerFlightTypes, playerVehicleDefinition, missionProfile, weather, skirmish, missionOptions);
        return mission;
    }

    private MissionProfile generateProfile(MissionHumanParticipants participatingPlayers) throws PWCGException
    {
        MissionProfileGenerator missionProfileGenerator = new MissionProfileGenerator(campaign, participatingPlayers);
        MissionProfile missionProfile = missionProfileGenerator.generateMissionProfile();
        return missionProfile;
    }

    private Mission buildMission(
            MissionHumanParticipants participatingPlayers, 
            MissionSquadronFlightTypes playerFlightTypes, 
            VehicleDefinition playerVehicleDefinition,
            MissionProfile missionProfile,
            MissionWeather weather, 
            Skirmish skirmish,
            MissionOptions missionOptions) throws PWCGException
    {
        campaign.setCurrentMission(null);
        
        CoordinateBox missionBorders = buildMissionBorders(missionProfile, participatingPlayers, skirmish, playerFlightTypes);
        Mission mission = new Mission(campaign, missionProfile, participatingPlayers, playerVehicleDefinition, missionBorders, weather, skirmish, missionOptions);
        campaign.setCurrentMission(mission);
        mission.generate(playerFlightTypes);

        return mission;
    }
    
    private Skirmish getSkirmishForMission(MissionHumanParticipants participatingPlayers) throws PWCGException
    {
        SkirmishBuilder skirmishBuilder = new SkirmishBuilder(campaign, participatingPlayers);
        return skirmishBuilder.chooseBestSkirmish();
    }

    private CoordinateBox buildMissionBorders(MissionProfile missionProfile, MissionHumanParticipants participatingPlayers, Skirmish skirmish, MissionSquadronFlightTypes playerFlightTypes) throws PWCGException
    {
        MissionBorderBuilder missionBorderBuilder = new MissionBorderBuilder(campaign, participatingPlayers, skirmish, playerFlightTypes);
        CoordinateBox missionBorders = missionBorderBuilder.buildCoordinateBox();
        return missionBorders;
    }
}
