package pwcg.mission;

import java.util.List;

import pwcg.aar.prelim.PwcgMissionData;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignMode;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.IMissionFile;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.io.json.CampaignMissionIOJson;
import pwcg.campaign.skin.SkinTemplate.SkinTemplateInstance;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.ambient.AmbientGroundUnitBuilder;
import pwcg.mission.data.PwcgGeneratedMission;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.ground.builder.IndirectFireAssignmentHandler;
import pwcg.mission.ground.vehicle.VehicleSetBuilderComprehensive;
import pwcg.mission.io.MissionDescriptionFile;
import pwcg.mission.io.MissionFileFactory;
import pwcg.mission.mcu.group.MissionObjectiveGroup;
import pwcg.mission.options.MissionOptions;
import pwcg.mission.target.AssaultDefinition;

public class Mission
{
    private Campaign campaign;
    private MissionHumanParticipants participatingPlayers;
    private CoordinateBox missionBorders;

    private MissionFlightBuilder missionFlightBuilder;
    private SinglePlayerMissionPlaneLimiter missionPlaneLimiter = new SinglePlayerMissionPlaneLimiter();
    private MissionObjectiveGroup missionObjectiveSuccess = new MissionObjectiveGroup();
    private MissionObjectiveGroup missionObjectiveFailure = new MissionObjectiveGroup();
    private MissionBattleManager missionBattleManager = new MissionBattleManager();
    private MissionGroundUnitResourceManager missionGroundUnitManager;
    private AmbientBalloonBuilder ambientBalloonBuilder;
    private AmbientGroundUnitBuilder ambientGroundUnitBuilder;
    private MissionWaypointIconBuilder missionWaypointIconBuilder = new MissionWaypointIconBuilder();
    private MissionAirfieldIconBuilder missionAirfieldIconBuilder = new MissionAirfieldIconBuilder();
    private MissionSquadronIconBuilder missionSquadronIconBuilder;
    private MissionAssociateFlightBuilder missionAssociateFlightBuilder = new MissionAssociateFlightBuilder();
    private MissionFrontLineIconBuilder missionFrontLines;
    private MissionEffects missionEffects = new MissionEffects();
    private VehicleSetBuilderComprehensive vehicleSetBuilder = new VehicleSetBuilderComprehensive();
    private boolean isFinalized = false;
    private MissionProfile missionProfile = MissionProfile.DAY_TACTICAL_MISSION;
    private MissionOptions missionOptions;
    private List<SkinTemplateInstance> skinsToGenerate;

    public Mission(Campaign campaign, MissionProfile missionProfile, MissionHumanParticipants participatingPlayers, CoordinateBox missionBorders) throws PWCGException
    {
        this.campaign = campaign;
        this.participatingPlayers = participatingPlayers;
        this.missionProfile = missionProfile;
        this.missionBorders = missionBorders;
    	
        initialize();
    }

    private void initialize() throws PWCGException 
    {
        PWCGLogger.eraseLog();
        MissionStringHandler subtitleHandler = MissionStringHandler.getInstance();
        subtitleHandler.clear();
        
        PWCGContext.getInstance().getSkinManager().clearSkinsInUse();

        missionGroundUnitManager = new MissionGroundUnitResourceManager();
        ambientBalloonBuilder = new AmbientBalloonBuilder(this);
        ambientGroundUnitBuilder = new AmbientGroundUnitBuilder(campaign, this);
        missionFlightBuilder = new MissionFlightBuilder(campaign, this);
        missionFrontLines = new MissionFrontLineIconBuilder(campaign);
        missionSquadronIconBuilder = new MissionSquadronIconBuilder(campaign);
    }

    public void generate(List<FlightTypes> playerFlightTypes) throws PWCGException 
    {
        validate();
        generateFlights(playerFlightTypes);
        createAmbientUnits();
    }

    private void generateFlights(List<FlightTypes> playerFlightTypes) throws PWCGException 
    {
    	missionFlightBuilder.generateFlights(participatingPlayers, playerFlightTypes);
    	missionAssociateFlightBuilder.buildAssociatedFlights(this);
        createFirePots();

        missionOptions = PWCGContext.getInstance().getCurrentMap().getMissionOptions();
        missionOptions.createFlightSpecificMissionOptions(this);
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
    
    private void createAmbientUnits() throws PWCGException, PWCGException
    {
        ambientBalloonBuilder.createAmbientBalloons();
        ambientGroundUnitBuilder = new AmbientGroundUnitBuilder(campaign, this);
        ambientGroundUnitBuilder.generateAmbientGroundUnits();
    }

    public void write() throws PWCGException 
    {
        IMissionFile missionFile = MissionFileFactory.createMissionFile(this);
                 
        IMissionDescription missionDescription = MissionDescriptionFactory.buildMissionDescription(campaign, this);
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
        List<PlaneMcu> playerPlanes = missionFlightBuilder.getReferencePlayerFlight().getFlightPlanes().getPlayerPlanes();
        String playerScript = playerPlanes.get(0).getScript();
        missionOptions.setPlayerConfig(playerScript);
    }

    public void finalizeMission() throws PWCGException 
    {
        if (!isFinalized)
        {
            MissionOptions missionOptions = PWCGContext.getInstance().getCurrentMap().getMissionOptions();
            setMissionScript(missionOptions);
            
            missionFlightBuilder.finalizeMissionFlights();
        	missionFrontLines.buildFrontLineIcons();
            missionPlaneLimiter.createPlaneCountersToLimitPlanesSpawned(this);
            missionWaypointIconBuilder.createWaypointIcons(missionFlightBuilder.getPlayerFlights());
            missionAirfieldIconBuilder.createWaypointIcons(campaign, this);
            
            MissionCheckZoneTriggerBuilder missionCheckZoneTriggerBuilder = new MissionCheckZoneTriggerBuilder(this);
            missionCheckZoneTriggerBuilder.triggerGroundUnitsOnPlayerProximity();

            assignIndirectFireTargets();

            if (missionFlightBuilder.getPlayerFlights().size() > 1) {
                missionSquadronIconBuilder.createSquadronIcons(missionFlightBuilder.getPlayerFlights());
            }

        	if (campaign.getCampaignData().getCampaignMode() == CampaignMode.CAMPAIGN_MODE_SINGLE)
        	{
        		finalizeForSinglePlayer();
        	}
        	
        	if (PWCGContext.getProduct() == PWCGProduct.FC)
        	{
        	    FCBugHandler.fcBugs(this);
        	}
        	
            MissionAnalyzer analyzer = new MissionAnalyzer();
            analyzer.analyze(this);
        }

        isFinalized = true;
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

    public List<IAirfield> getFieldsForPatrol() throws PWCGException 
    {
        MissionAirfieldBuilder airfieldBuilder = new MissionAirfieldBuilder(campaign, this);
        return airfieldBuilder.getFieldsForPatrol();
    }
    
    public void registerAssault(AssaultDefinition missionBattle)
    {
        missionBattleManager.addMissionBattle(missionBattle);
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
        return missionGroundUnitManager;
    }

    public SinglePlayerMissionPlaneLimiter getMissionPlaneCalculator()
    {
        return missionPlaneLimiter;
    }

    public MissionObjectiveGroup getMissionObjectiveSuccess()
    {
        return missionObjectiveSuccess;
    }

    public MissionObjectiveGroup getMissionObjectiveFailure()
    {
        return missionObjectiveFailure;
    }

    public AmbientGroundUnitBuilder getAmbientGroundUnitBuilder()
    {
        return ambientGroundUnitBuilder;
    }

	public MissionFlightBuilder getMissionFlightBuilder()
	{
		return missionFlightBuilder;
	}

	public MissionFrontLineIconBuilder getMissionFrontLineIconBuilder()
	{
		return missionFrontLines;
	}

	public MissionWaypointIconBuilder getMissionWaypointIconBuilder()
	{
		return missionWaypointIconBuilder;
	}

	public MissionAirfieldIconBuilder getMissionAirfieldIconBuilder()
	{
		return missionAirfieldIconBuilder;
	}

    public MissionSquadronIconBuilder getMissionSquadronIconBuilder() {
        return missionSquadronIconBuilder;
    }

    public MissionBattleManager getMissionBattleManager()
    {
        return missionBattleManager;
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

    public void setMissionOptions(MissionOptions missionOptions)
    {
        this.missionOptions = missionOptions;
    }

    public AmbientBalloonBuilder getAmbientBalloonBuilder()
    {
        return ambientBalloonBuilder;
    }

    public void setSkinsToGenerate(List<SkinTemplateInstance> skinsToGenerate)
    {
        this.skinsToGenerate = skinsToGenerate;
    }

    public List<SkinTemplateInstance> getSkinsToGenerate() {
        return skinsToGenerate;
    }
}
