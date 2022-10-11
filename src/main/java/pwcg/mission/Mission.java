package pwcg.mission;

import java.util.List;

import pwcg.aar.prelim.PwcgMissionData;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.IMissionFile;
import pwcg.campaign.api.Side;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.io.json.CampaignMissionIOJson;
import pwcg.campaign.skin.Skin;
import pwcg.campaign.skin.SkinsInUse;
import pwcg.campaign.skirmish.Skirmish;
import pwcg.campaign.skirmish.SkirmishProfileType;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.data.PwcgGeneratedMission;
import pwcg.mission.flight.IFlight;
import pwcg.mission.ground.MissionGroundUnitBuilder;
import pwcg.mission.ground.vehicle.VehicleDefinition;
import pwcg.mission.ground.vehicle.VehicleSetBuilderComprehensive;
import pwcg.mission.io.MissionDescriptionFile;
import pwcg.mission.io.MissionFileFactory;
import pwcg.mission.options.MissionOptions;
import pwcg.mission.options.MissionType;
import pwcg.mission.options.MissionWeather;
import pwcg.mission.target.AssaultDefinition;

public class Mission
{
    private Campaign campaign;
    private MissionHumanParticipants participatingPlayers;
    private CoordinateBox missionBorders;
    private CoordinateBox structureBorders;
    private MissionProfile missionProfile = MissionProfile.DAY_TACTICAL_MISSION;
    private MissionOptions missionOptions;

    private MissionWeather weather;

    private MissionBlocks missionBlocks;
    private MissionAirfields missionAirfields;
    private MissionFinalizer finalizer;

    private MissionFlights missionFlights;
    private MissionPlayerVehicle missionPlayerVehicles = new MissionPlayerVehicle();
    private VehicleDefinition playerVehicleDefinition;
    private MissionVirtualEscortHandler virtualEscortHandler = new MissionVirtualEscortHandler();
    private SkinsInUse skinsInUse = new SkinsInUse();

    private MissionBattleManager battleManager = new MissionBattleManager();
    private MissionGroundUnitBuilder groundUnitBuilder;
    private MissionGroundUnitResourceManager groundUnitManager;
    private VehicleSetBuilderComprehensive vehicleSetBuilder = new VehicleSetBuilderComprehensive();

    private MissionSquadronRegistry missionSquadronRegistry = new MissionSquadronRegistry();
    private Skirmish skirmish;
    
    private MissionEffects missionEffects = new MissionEffects();

    public Mission(
            Campaign campaign, 
            MissionProfile missionProfile, 
            MissionHumanParticipants participatingPlayers, 
            VehicleDefinition playerVehicleDefinition,
            CoordinateBox missionBorders, 
            MissionWeather weather,
            Skirmish skirmish,
            MissionOptions missionOptions)
            throws PWCGException
    {
        this.campaign = campaign;
        this.participatingPlayers = participatingPlayers;
        this.playerVehicleDefinition = playerVehicleDefinition;
        this.missionProfile = missionProfile;
        this.missionBorders = missionBorders;
        this.weather = weather;
        this.skirmish = skirmish;
        this.missionOptions = missionOptions;

        initialize();
    }

    private void initialize() throws PWCGException
    {
        PWCGLogger.eraseLog();
        MissionStringHandler subtitleHandler = MissionStringHandler.getInstance();
        subtitleHandler.clear();

        groundUnitManager = new MissionGroundUnitResourceManager();
        groundUnitBuilder = new MissionGroundUnitBuilder(this);
        missionFlights = new MissionFlights(this);
        finalizer = new MissionFinalizer(this);
    }

    public void generate(MissionSquadronFlightTypes playerFlightTypes) throws PWCGException
    {
        validate();
        createStructuresBoxForMission();
        createGroundUnits();
        createPlayerVehicle();
        generateFlights(playerFlightTypes);
    }

    private void createPlayerVehicle() throws PWCGException
    {
        if (isAAATruckMission())
        {
            missionPlayerVehicles.buildPlayerVehicle(this, playerVehicleDefinition, participatingPlayers.getMissionPlayerSquadrons().get(0).determineSide(), campaign.getDate());
        }
    }

    private void createStructuresBoxForMission() throws PWCGException
    {        
        buildStructureBorders();
        MissionBlockBuilder blockBuilder = new MissionBlockBuilder(this, structureBorders);
        missionBlocks = blockBuilder.buildFixedPositionsForMission();

        MissionAirfieldBuilder airfieldBuilder = new MissionAirfieldBuilder(this, structureBorders);
        missionAirfields = airfieldBuilder.buildFieldsForPatrol();
        
        MissionBlockEntityBuilder missionBlockEntityBuilder = new MissionBlockEntityBuilder(this);
        missionBlockEntityBuilder.buildEntitiesForTargetStructures(missionBlocks);
    }

    private void buildStructureBorders() throws PWCGException
    {
        StructureBorderBuilder structureBorderBuilder = new StructureBorderBuilder(campaign, participatingPlayers, missionBorders);
        structureBorders = structureBorderBuilder.getBordersForStructuresConsideringFlights( missionFlights.getPlayerFlights());
    }

    private void generateFlights(MissionSquadronFlightTypes playerFlightTypes) throws PWCGException
    {
        missionFlights.generateFlights(playerFlightTypes);
        createFirePots();
    }

    private void validate() throws PWCGException
    {
        if (participatingPlayers.getAllParticipatingPlayers().size() == 0)
        {
            throw new PWCGException("No participating players for mission");
        }

        if (missionBorders == null || missionBorders.getCenter() == null)
        {
            throw new PWCGException("No mission borders for mission");
        }

        if (campaign == null)
        {
            throw new PWCGException("No campaign for mission");
        }
    }

    public void generateAllGroundUnitTypesForTest() throws PWCGException
    {
        vehicleSetBuilder = new VehicleSetBuilderComprehensive();
        vehicleSetBuilder.makeOneOfEachType();
        vehicleSetBuilder.scatterAroundPosition(new Coordinate(100, 0, 100));
    }

    public double getPlayerDistanceToTarget() throws PWCGException
    {
        return participatingPlayers.getPlayerDistanceToTarget(this);
    }

    private void createGroundUnits() throws PWCGException, PWCGException
    {
        groundUnitBuilder.generateGroundUnitsForMission();
    }

    public void write() throws PWCGException
    {
        String missionDescriptionText = writeGameMissionFiles();
        writePwcgMissionData(missionDescriptionText);
    }

    public String writeGameMissionFiles() throws PWCGException
    {
        IMissionFile missionFile = MissionFileFactory.createMissionFile(this);
        missionFile.writeMission();
        String missionDescriptionText = writeMissionDescriptionFile();
        return missionDescriptionText;
    }

    private String writeMissionDescriptionFile() throws PWCGException
    {
        IMissionDescription missionDescription = MissionDescriptionFactory.buildMissionDescription(this);
        String missionDescriptionText = missionDescription.createDescription();

        MissionDescriptionFile missionDescriptionFile = new MissionDescriptionFile();
        missionDescriptionFile.writeMissionDescription(missionDescription, campaign);
        return missionDescriptionText;
    }

    private void writePwcgMissionData(String missionDescriptionText) throws PWCGException
    {
        if (!campaign.isInMemory())
        {
            StringBuffer missionDescriptionBuffer = new StringBuffer("");
            missionDescriptionBuffer.append("Mission: \n");
            missionDescriptionBuffer.append(missionDescriptionText);
    
            PwcgGeneratedMission pwcgMission = new PwcgGeneratedMission(campaign);
            PwcgMissionData pwcgMissionData = pwcgMission.generateMissionData(this);
            pwcgMissionData.setMissionDescription(missionDescriptionBuffer.toString());
    
            CampaignMissionIOJson.writeJson(campaign, pwcgMissionData);
        }
    }

    private void createFirePots() throws PWCGException
    {
        if (isNightMission())
        {
            missionEffects.createFirePots(this);
        }
    }

    public void finalizeMission() throws PWCGException
    {
        finalizer.finalizeMission();
    }

    public Side getMissionSide() throws PWCGException
    {
        boolean hasPlayerAllied = false;
        boolean hasPlayerAxis = false;
        for (IFlight flight : missionFlights.getPlayerFlights())
        {
            if (flight.getSquadron().determineSide() == Side.ALLIED)
            {
                hasPlayerAllied = true;
            }
            else
            {
                hasPlayerAxis = true;
            }
        }
        
        if (hasPlayerAllied && hasPlayerAxis)
        {
            return Side.NEUTRAL;
        }
        else if (hasPlayerAllied)
        {
            return Side.ALLIED;
        }
        else if (hasPlayerAxis)
        {
            return Side.AXIS;
        }
        
        return Side.NEUTRAL;
    }
    
    public boolean isAAATruckMission()
    {
        if (missionOptions.getMissionType() == MissionType.SINGLE_AAA_MISSION || missionOptions.getMissionType() == MissionType.COOP_AAA_MISSION)
        {
            return true;
        }
        return false;
    }

    public void registerAssault(AssaultDefinition missionBattle)
    {
        battleManager.addMissionBattle(missionBattle);
    }

    public Campaign getCampaign()
    {
        return campaign;
    }

    public void setCampaign(Campaign campaign)
    {
        this.campaign = campaign;
    }

    public MissionEffects getMissionEffects()
    {
        return missionEffects;
    }

    public MissionGroundUnitResourceManager getMissionGroundUnitManager()
    {
        return groundUnitManager;
    }

    public MissionGroundUnitBuilder getGroundUnitBuilder()
    {
        return groundUnitBuilder;
    }

    public MissionFlights getFlights()
    {
        return missionFlights;
    }

    public MissionBattleManager getBattleManager()
    {
        return battleManager;
    }

    public MissionVirtualEscortHandler getVirtualEscortHandler()
    {
        return virtualEscortHandler;
    }

    public VehicleSetBuilderComprehensive getVehicleSetBuilder()
    {
        return vehicleSetBuilder;
    }

    public boolean isNightMission()
    {
        return missionProfile.isNightMission();
    }

    public MissionProfile getMissionProfile()
    {
        return missionProfile;
    }

    public MissionHumanParticipants getParticipatingPlayers()
    {
        return participatingPlayers;
    }

    public CoordinateBox getMissionBorders()
    {
        return missionBorders;
    }

    public CoordinateBox getStructureBorders()
    {
        return structureBorders;
    }

    public MissionOptions getMissionOptions()
    {
        return missionOptions;
    }

    public void addSkinInUse(Skin skin)
    {
        skinsInUse.addSkinInUse(skin);        
    }

    public SkinsInUse getSkinsInUse()
    {
        return skinsInUse;
    }

    public MissionWeather getWeather()
    {
        return weather;
    }

    public List<Airfield> getFieldsForPatrol() throws PWCGException
    {
        return missionAirfields.getFieldsForPatrol();
    }

    public MissionBlocks getMissionBlocks() throws PWCGException
    {
        return missionBlocks;
    }

    public Skirmish getSkirmish()
    {
        return skirmish;
    }

    public MissionSquadronRegistry getMissionSquadronRegistry()
    {
        return missionSquadronRegistry;
    }

    public MissionPlayerVehicle getMissionAAATrucks()
    {
        return missionPlayerVehicles;
    }

    public MissionFinalizer getFinalizer()
    {
        return finalizer;
    }

    public boolean isSeaSkirmish()
    {
        if (skirmish != null && 
            skirmish.getProfileType() == SkirmishProfileType.SKIRMISH_PROFILE_ANTI_SHIPPING)
        {
            return true;
        }
        return false;
    }

    public Side getSideForLimitedAA()
    {
        if (skirmish == null)
        {
            return Side.NEUTRAL;
        }
        
        if (skirmish.getProfileType() == SkirmishProfileType.SKIRMISH_PROFILE_INVASION)
        {
            return skirmish.getAttackerGround().getOppositeSide();
        }
        
        if (skirmish.getProfileType() == SkirmishProfileType.SKIRMISH_PROFILE_ANTI_SHIPPING)
        {
            return skirmish.getAttackerAir();
        }
        
        if (skirmish.getProfileType() == SkirmishProfileType.SKIRMISH_PROFILE_CARPET_BOMB)
        {
            return skirmish.getAttackerAir().getOppositeSide();
        }
        
        return Side.NEUTRAL;
    }
}
