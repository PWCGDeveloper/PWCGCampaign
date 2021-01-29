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
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.data.PwcgGeneratedMission;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.ground.MissionGroundUnitBuilder;
import pwcg.mission.ground.builder.IndirectFireAssignmentHandler;
import pwcg.mission.ground.unittypes.GroundUnitEngagableAAAEvaluator;
import pwcg.mission.ground.vehicle.VehicleSetBuilderComprehensive;
import pwcg.mission.io.MissionDescriptionFile;
import pwcg.mission.io.MissionFileFactory;
import pwcg.mission.mcu.group.MissionObjectiveGroup;
import pwcg.mission.mcu.group.StopAttackingNearAirfieldSequence;
import pwcg.mission.options.MissionOptions;
import pwcg.mission.options.MissionWeather;
import pwcg.mission.target.AssaultDefinition;

public class Mission
{
    private Campaign campaign;
    private MissionHumanParticipants participatingPlayers;
    private CoordinateBox missionBorders;
    private MissionProfile missionProfile = MissionProfile.DAY_TACTICAL_MISSION;
    private MissionOptions missionOptions;
    private MissionFrontLineIconBuilder frontLines;

    private MissionWeather weather;
    private MissionObjectiveGroup missionObjectiveSuccess = new MissionObjectiveGroup();
    private MissionObjectiveGroup missionObjectiveFailure = new MissionObjectiveGroup();

    private MissionSquadronChooser squadronChooser = new MissionSquadronChooser();
    private MissionFlightBuilder flightBuilder;
    private MissionVirtualEscortHandler virtualEscortHandler = new MissionVirtualEscortHandler();
    private SkinsInUse skinsInUse = new SkinsInUse();
    private List<StopAttackingNearAirfieldSequence> stopSequenceForMission = new ArrayList<>();

    private MissionBlockBuilder blockBuilder;
    private MissionAirfieldBuilder airfieldBuilder;

    private MissionBattleManager battleManager = new MissionBattleManager();
    private MissionGroundUnitBuilder groundUnitBuilder;
    private MissionGroundUnitResourceManager groundUnitManager;
    private VehicleSetBuilderComprehensive vehicleSetBuilder = new VehicleSetBuilderComprehensive();

    private MissionWaypointIconBuilder waypointIconBuilder = new MissionWaypointIconBuilder();
    private MissionAirfieldIconBuilder airfieldIconBuilder = new MissionAirfieldIconBuilder();
    private MissionSquadronIconBuilder squadronIconBuilder;
    private MissionAssaultIconBuilder assaultIconBuilder = new MissionAssaultIconBuilder();
    
    private MissionEffects missionEffects = new MissionEffects();
    private boolean isFinalized = false;

    public Mission(
            Campaign campaign, 
            MissionProfile missionProfile, 
            MissionHumanParticipants participatingPlayers, 
            CoordinateBox missionBorders, 
            MissionWeather weather,
            MissionOptions missionOptions)
            throws PWCGException
    {
        this.campaign = campaign;
        this.participatingPlayers = participatingPlayers;
        this.missionProfile = missionProfile;
        this.missionBorders = missionBorders;
        this.weather = weather;
        this.missionOptions = missionOptions;

        initialize();
    }

    private void initialize() throws PWCGException
    {
        PWCGLogger.eraseLog();
        MissionStringHandler subtitleHandler = MissionStringHandler.getInstance();
        subtitleHandler.clear();

        groundUnitManager = new MissionGroundUnitResourceManager();
        groundUnitBuilder = new MissionGroundUnitBuilder(campaign, this);
        flightBuilder = new MissionFlightBuilder(campaign, this);
        blockBuilder = new MissionBlockBuilder(this);
        airfieldBuilder = new MissionAirfieldBuilder(campaign, this);
        frontLines = new MissionFrontLineIconBuilder(campaign);
        squadronIconBuilder = new MissionSquadronIconBuilder(campaign);
    }

    public void generate(MissionSquadronFlightTypes playerFlightTypes) throws PWCGException
    {
        validate();
        createStructures();
        createGroundUnits();
        createAirfields();
        generateFlights(playerFlightTypes);

    }
    
    public int getGroundUnitCount() throws PWCGException
    {
        int unitCountMissionGroundUnits = groundUnitBuilder.getUnitCount();        
        int unitCountInFlights = 0;
        
        int unitCountInAirfields = 0;
        for (Airfield field : getFieldsForPatrol())
        {
            unitCountInAirfields += field.getUnitCount();
        }


        int unitCountInMission = 0;
        unitCountInMission += unitCountInFlights;
        unitCountInMission += unitCountMissionGroundUnits;
        unitCountInMission += unitCountInAirfields;

        System.out.println("unit count flights : " + unitCountInFlights);
        System.out.println("unit count misson : " + unitCountMissionGroundUnits);
        System.out.println("unit count airfields : " + unitCountInAirfields);
        System.out.println("unit count total : " + unitCountInMission);

        return unitCountInMission;
    }

    private void createAirfields() throws PWCGException
    {
        airfieldBuilder.buildFieldsForPatrol();;
    }

    private void createStructures() throws PWCGException
    {
        blockBuilder.buildFixedPositionsForMission();
    }

    private void generateFlights(MissionSquadronFlightTypes playerFlightTypes) throws PWCGException
    {
        flightBuilder.generateFlights(playerFlightTypes);
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
        groundUnitBuilder = new MissionGroundUnitBuilder(campaign, this);
        groundUnitBuilder.generateGroundUnitsForMission();
    }

    public void write() throws PWCGException
    {
        IMissionFile missionFile = MissionFileFactory.createMissionFile(this);

        IMissionDescription missionDescription = MissionDescriptionFactory.buildMissionDescription(campaign, this, flightBuilder.getReferencePlayerFlight());
        String missionDescriptionText = missionDescription.createDescription();

        MissionDescriptionFile missionDescriptionFile = new MissionDescriptionFile();
        missionDescriptionFile.writeMissionDescription(missionDescription, campaign);

        missionFile.writeMission();

        writePwcgMissionData(missionDescriptionText);
    }

    private void writePwcgMissionData(String missionDescriptionText) throws PWCGException
    {
        StringBuffer missionDescriptionBuffer = new StringBuffer("");
        missionDescriptionBuffer.append("Mission: \n");
        missionDescriptionBuffer.append(missionDescriptionText);

        PwcgGeneratedMission pwcgMission = new PwcgGeneratedMission(campaign);
        PwcgMissionData pwcgMissionData = pwcgMission.generateMissionData(this);
        pwcgMissionData.setMissionDescription(missionDescriptionBuffer.toString());

        CampaignMissionIOJson.writeJson(campaign, pwcgMissionData);
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
        List<PlaneMcu> playerPlanes = flightBuilder.getReferencePlayerFlight().getFlightPlanes().getPlayerPlanes();
        String playerScript = playerPlanes.get(0).getScript();
        missionOptions.setPlayerConfig(playerScript);
    }

    public void finalizeMission() throws PWCGException
    {
        if (!isFinalized)
        {
            setMissionScript(missionOptions);

            flightBuilder.finalizeMissionFlights();
            frontLines.buildFrontLineIcons();
            waypointIconBuilder.createWaypointIcons(flightBuilder.getPlayerFlights());
            airfieldIconBuilder.createWaypointIcons(campaign, this);
            assaultIconBuilder.createAssaultIcons(battleManager.getMissionAssaultDefinitions());

            MissionCheckZoneTriggerBuilder missionCheckZoneTriggerBuilder = new MissionCheckZoneTriggerBuilder(this);
            missionCheckZoneTriggerBuilder.triggerGroundUnits();

            assignIndirectFireTargets();
            setEngagableAAA();

            if (flightBuilder.getPlayerFlights().size() > 1)
            {
                squadronIconBuilder.createSquadronIcons(flightBuilder.getPlayerFlights());
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

    private void setEngagableAAA()
    {
        GroundUnitEngagableAAAEvaluator.setAAUnitsEngageableStatus(this);        
    }

    private void stopAttackingNearAirfield() throws PWCGException
    {
        for (IFlight flight : this.getMissionFlightBuilder().getAllAerialFlights())
        {
            StopAttackingNearAirfield stopAttackingNearAirfield = new StopAttackingNearAirfield(flight, getFieldsForPatrol());
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

    public List<Airfield> getFieldsForPatrol() throws PWCGException
    {
        return airfieldBuilder.getFieldsForPatrol();
    }

    public Side getMissionSide() throws PWCGException
    {
        boolean hasPlayerAllied = false;
        boolean hasPlayerAxis = false;
        for (IFlight flight : flightBuilder.getPlayerFlights())
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

    public MissionFlightBuilder getMissionFlightBuilder()
    {
        return flightBuilder;
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

    public MissionSquadronChooser getMissionSquadronChooser()
    {
        return squadronChooser;
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

    public MissionBlockBuilder getMissionBlockBuilder()
    {
        return blockBuilder;
    }

    public MissionAirfieldBuilder getMissionAirfieldBuilder()
    {
        return airfieldBuilder;
    }
}
