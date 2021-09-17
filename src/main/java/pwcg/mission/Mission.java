package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.aar.prelim.PwcgMissionData;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignMode;
import pwcg.campaign.api.IMissionFile;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.io.json.CampaignMissionIOJson;
import pwcg.campaign.skin.Skin;
import pwcg.campaign.skin.SkinsInUse;
import pwcg.campaign.skirmish.Skirmish;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.mission.data.PwcgGeneratedMission;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.ground.MissionGroundUnitBuilder;
import pwcg.mission.ground.builder.IndirectFireAssignmentHandler;
import pwcg.mission.ground.unittypes.GroundUnitEngagableAAAEvaluator;
import pwcg.mission.ground.vehicle.VehicleDefinition;
import pwcg.mission.ground.vehicle.VehicleSetBuilderComprehensive;
import pwcg.mission.io.MissionDescriptionFile;
import pwcg.mission.io.MissionFileFactory;
import pwcg.mission.mcu.group.MissionObjectiveGroup;
import pwcg.mission.mcu.group.StopAttackingNearAirfieldSequence;
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
    private MissionFrontLineIconBuilder frontLines;

    private MissionWeather weather;
    private MissionObjectiveGroup missionObjectiveSuccess = new MissionObjectiveGroup();
    private MissionObjectiveGroup missionObjectiveFailure = new MissionObjectiveGroup();

    private MissionBlocks missionBlocks = null;
    private MissionAirfields missionAirfields = null;
    
    private MissionFlights missionFlights;
    private MissionPlayerVehicle missionPlayerVehicles = new MissionPlayerVehicle();
    private VehicleDefinition playerVehicleDefinition = null;
    private MissionVirtualEscortHandler virtualEscortHandler = new MissionVirtualEscortHandler();
    private SkinsInUse skinsInUse = new SkinsInUse();
    private List<StopAttackingNearAirfieldSequence> stopSequenceForMission = new ArrayList<>();

    private MissionBattleManager battleManager = new MissionBattleManager();
    private MissionGroundUnitBuilder groundUnitBuilder;
    private MissionGroundUnitResourceManager groundUnitManager;
    private VehicleSetBuilderComprehensive vehicleSetBuilder = new VehicleSetBuilderComprehensive();

    private MissionWaypointIconBuilder waypointIconBuilder = new MissionWaypointIconBuilder();
    private MissionAirfieldIconBuilder airfieldIconBuilder = new MissionAirfieldIconBuilder();
    private MissionSquadronIconBuilder squadronIconBuilder;
    private MissionAssaultIconBuilder assaultIconBuilder = new MissionAssaultIconBuilder();
    private MissionSquadronRegistry missionSquadronRegistry = new MissionSquadronRegistry();
    private Skirmish skirmish;
    
    private MissionEffects missionEffects = new MissionEffects();
    private boolean isFinalized = false;

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
        frontLines = new MissionFrontLineIconBuilder(campaign);
        squadronIconBuilder = new MissionSquadronIconBuilder(campaign);
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

    public int getGroundUnitCount() throws PWCGException
    {
        int unitCountMissionGroundUnits = groundUnitBuilder.getUnitCount();        
        int unitCountInFlights = 0;

        int unitCountInMission = 0;
        unitCountInMission += unitCountInFlights;
        unitCountInMission += unitCountMissionGroundUnits;

        PWCGLogger.log(LogLevel.INFO, "unit count flights : " + unitCountInFlights);
        PWCGLogger.log(LogLevel.INFO, "unit count misson : " + unitCountMissionGroundUnits);
        PWCGLogger.log(LogLevel.INFO, "unit count total : " + unitCountInMission);

        return unitCountInMission;
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
        IMissionDescription missionDescription = MissionDescriptionFactory.buildMissionDescription(campaign, this);
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

    private void setMissionScript(MissionOptions missionOptions) throws PWCGException
    {
        List<PlaneMcu> playerPlanes = missionFlights.getReferencePlayerFlight().getFlightPlanes().getPlayerPlanes();
        String playerScript = playerPlanes.get(0).getScript();
        missionOptions.setPlayerConfig(playerScript);
    }

    public void finalizeMission() throws PWCGException
    {
        if (!isFinalized)
        {
            setMissionScript(missionOptions);

            missionFlights.finalizeMissionFlights();
            groundUnitBuilder.finalizeGroundUnits();
            
            frontLines.buildFrontLineIcons();
            waypointIconBuilder.createWaypointIcons(missionFlights.getPlayerFlights());
            airfieldIconBuilder.createWaypointIcons(campaign, this);
            assaultIconBuilder.createAssaultIcons(battleManager.getMissionAssaultDefinitions());
            missionBlocks.adjustBlockDamageAndSmoke();

            setGroundUnitTriggers();
            setFreeHuntTriggers();
            assignIndirectFireTargets();
            setEngagableAAA();

            if (missionFlights.getPlayerFlights().size() > 1)
            {
                squadronIconBuilder.createSquadronIcons(missionFlights.getPlayerFlights());
            }

            if (campaign.getCampaignData().getCampaignMode() == CampaignMode.CAMPAIGN_MODE_SINGLE)
            {
                finalizeForSinglePlayer();
            }
            
            stopAttackingNearAirfield();

            if (PWCGContext.getProduct() == PWCGProduct.FC)
            {
                FCBugHandler.fcBugs(this);
            }

            MissionAnalyzer analyzer = new MissionAnalyzer();
            analyzer.analyze(this);
        }

        getGroundUnitCount();
        
        isFinalized = true;
    }

    private void setFreeHuntTriggers() throws PWCGException
    {
        MissionFreeHuntTriggerBuilder freeHuntTriggerBuidler = new MissionFreeHuntTriggerBuilder(this);
        freeHuntTriggerBuidler.buildFreeHuntTriggers();
    }

    private void setGroundUnitTriggers() throws PWCGException
    {
        MissionCheckZoneTriggerBuilder missionCheckZoneTriggerBuilder = new MissionCheckZoneTriggerBuilder(this);
        missionCheckZoneTriggerBuilder.triggerGroundUnits();
    }

    private void setEngagableAAA()
    {
        GroundUnitEngagableAAAEvaluator.setAAUnitsEngageableStatus(this);        
    }

    private void stopAttackingNearAirfield() throws PWCGException
    {
        for (IFlight flight : this.getMissionFlights().getAllAerialFlights())
        {
            StopAttackingNearAirfield stopAttackingNearAirfield = new StopAttackingNearAirfield(flight, missionAirfields.getFieldsForPatrol());
            List<StopAttackingNearAirfieldSequence> stopSequenceForFlight = stopAttackingNearAirfield.stopAttackingAirfields();
            stopSequenceForMission.addAll(stopSequenceForFlight);
        }
    }

    private void assignIndirectFireTargets() throws PWCGException
    {
        IndirectFireAssignmentHandler indirectFireAssignmentHandler = new IndirectFireAssignmentHandler(this);
        indirectFireAssignmentHandler.makeIndirectFireAssignments();
    }

    private void finalizeForSinglePlayer() throws PWCGException
    {
        if (campaign.getCampaignData().getCampaignMode() == CampaignMode.CAMPAIGN_MODE_SINGLE)
        {
            missionObjectiveSuccess.createSuccessMissionObjective(campaign, this);
            missionObjectiveFailure.createFailureMissionObjective(campaign, this);
        }
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

    public boolean isFinalized()
    {
        return isFinalized;
    }

    public MissionGroundUnitResourceManager getMissionGroundUnitManager()
    {
        return groundUnitManager;
    }

    public MissionObjectiveGroup getMissionObjectiveSuccess()
    {
        return missionObjectiveSuccess;
    }

    public MissionObjectiveGroup getMissionObjectiveFailure()
    {
        return missionObjectiveFailure;
    }

    public MissionGroundUnitBuilder getMissionGroundUnitBuilder()
    {
        return groundUnitBuilder;
    }

    public MissionFlights getMissionFlights()
    {
        return missionFlights;
    }

    public MissionFrontLineIconBuilder getMissionFrontLineIconBuilder()
    {
        return frontLines;
    }

    public MissionWaypointIconBuilder getMissionWaypointIconBuilder()
    {
        return waypointIconBuilder;
    }

    public MissionAirfieldIconBuilder getMissionAirfieldIconBuilder()
    {
        return airfieldIconBuilder;
    }

    public MissionAssaultIconBuilder getMissionAssaultIconBuilder()
    {
        return assaultIconBuilder;
    }

    public MissionSquadronIconBuilder getMissionSquadronIconBuilder()
    {
        return squadronIconBuilder;
    }

    public MissionBattleManager getMissionBattleManager()
    {
        return battleManager;
    }

    public MissionVirtualEscortHandler getMissionVirtualEscortHandler()
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

    public List<StopAttackingNearAirfieldSequence> getStopSequenceForMission()
    {
        return stopSequenceForMission;
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
}
