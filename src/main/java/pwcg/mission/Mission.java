package pwcg.mission;

import java.util.List;

import pwcg.aar.prelim.PwcgMissionData;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignMode;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.IMissionFile;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.io.json.CampaignMissionIOJson;
import pwcg.campaign.ww2.ground.vehicle.VehicleSetBuilderComprehensive;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.core.utils.Logger;
import pwcg.mission.ambient.AmbientGroundUnitBuilder;
import pwcg.mission.data.PwcgGeneratedMission;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.plane.PlaneMCU;
import pwcg.mission.io.MissionDescriptionFile;
import pwcg.mission.io.MissionFileFactory;
import pwcg.mission.mcu.group.MissionObjectiveGroup;
import pwcg.mission.options.MissionOptions;

public class Mission
{
    private Campaign campaign;
    private MissionHumanParticipants participatingPlayers;
    private CoordinateBox missionBorders;

    private MissionFlightBuilder missionFlightBuilder;
    private SinglePlayerMissionPlaneLimiter missionPlaneLimiter = new SinglePlayerMissionPlaneLimiter();
    private MissionObjectiveGroup missionObjectiveSuccess = new MissionObjectiveGroup();
    private MissionObjectiveGroup missionObjectiveFailure = new MissionObjectiveGroup();
    private MissionBalloons missionBalloons = new MissionBalloons();
    private MissionBattleManager missionBattleManager = new MissionBattleManager();
    private MissionGroundUnitResourceManager missionGroundUnitManager;
    private AmbientGroundUnitBuilder ambientGroundUnitBuilder;
    private MissionWaypointIconBuilder missionWaypointIconBuilder = new MissionWaypointIconBuilder();
    private MissionAirfieldIconBuilder missionAirfieldIconBuilder = new MissionAirfieldIconBuilder();
    private MissionAssociateFlightBuilder missionAssociateFlightBuilder = new MissionAssociateFlightBuilder();
    private MissionFrontLineIconBuilder missionFrontLines;
    private MissionEffects missionEffects = new MissionEffects();
    private VehicleSetBuilderComprehensive vehicleSetBuilder = new VehicleSetBuilderComprehensive();
    private boolean isFinalized = false;
    private MissionProfile missionProfile = MissionProfile.DAY_TACTICAL_MISSION;
    private MissionOptions missionOptions;

    public Mission(Campaign campaign, MissionHumanParticipants participatingPlayers, CoordinateBox missionBorders) throws PWCGException
    {
        this.campaign = campaign;
        this.participatingPlayers = participatingPlayers;
        this.missionBorders = missionBorders;
    	
        initialize();
    }

    private void initialize() throws PWCGException 
    {
        Logger.eraseLog();
        MissionStringHandler subtitleHandler = MissionStringHandler.getInstance();
        subtitleHandler.clear();
        
        PWCGContextManager.getInstance().getSkinManager().clearSkinsInUse();

        missionGroundUnitManager = new MissionGroundUnitResourceManager();
        ambientGroundUnitBuilder = new AmbientGroundUnitBuilder(campaign, this);
        missionFlightBuilder = new MissionFlightBuilder(campaign, this);
        missionFrontLines = new MissionFrontLineIconBuilder(campaign, this);
    }

    void generate(FlightTypes overrideFlightType) throws PWCGException 
    {
        validate();

    	missionFlightBuilder.generateFlights(participatingPlayers, overrideFlightType);
    	missionAssociateFlightBuilder.buildAssociatedFlights(this);
        createFirePots();

        missionOptions = PWCGContextManager.getInstance().getCurrentMap().getMissionOptions();
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

    private void createAmbientUnits() throws PWCGException, PWCGException
    {
        if (PWCGContextManager.isRoF())
        {
            missionBalloons.createAmbientBalloons(this);
        }
        
        ambientGroundUnitBuilder = new AmbientGroundUnitBuilder(campaign, this);
        ambientGroundUnitBuilder.generateAmbientGroundUnits();
    }

    private void write() throws PWCGException 
    {
        IMissionFile missionFile = MissionFileFactory.createMissionFile(this);
                 
        missionFile.writeMission();

        IMissionDescription missionDescription = MissionDescriptionFactory.buildMissionDescription(campaign, this);
        String missionDescriptionText = missionDescription.createDescription();
        
        MissionDescriptionFile missionDescriptionFile = new MissionDescriptionFile();
        missionDescriptionFile.writeMissionDescription(missionDescription, campaign);

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
        	for (Flight flight: this.missionFlightBuilder.getPlayerFlights())
        	{
        		IAirfield airfield = flight.getSquadron().determineCurrentAirfieldAnyMap(campaign.getDate());
        		missionEffects.createFirePots(airfield);
        	}
        }
    }

    private void setMissionScript(MissionOptions missionOptions) throws PWCGException
    {
        List<PlaneMCU> playerPlanes = missionFlightBuilder.getReferencePlayerFlight().getPlayerPlanes();
        String playerScript = playerPlanes.get(0).getScript();
        missionOptions.setPlayerConfig(playerScript);
    }

    public void finalizeMission() throws PWCGException 
    {
        if (!isFinalized)
        {
            MissionOptions missionOptions = PWCGContextManager.getInstance().getCurrentMap().getMissionOptions();
            setMissionScript(missionOptions);

            createAmbientUnits();
            
            missionFlightBuilder.finalizeMissionFlights();
        	missionFrontLines.buildFrontLineIcons();
            missionPlaneLimiter.createPlaneCountersToLimitPlanesSpawned(this);
            missionWaypointIconBuilder.createWaypointIcons(missionFlightBuilder.getPlayerFlights());
            missionAirfieldIconBuilder.createWaypointIcons(campaign, this);

        	if (campaign.getCampaignData().getCampaignMode() == CampaignMode.CAMPAIGN_MODE_SINGLE)
        	{
        		finalizeForSinglePlayer();
        	}
        	
            MissionAnalyzer analyzer = new MissionAnalyzer();
            analyzer.analyze(this);

            this.write();
        }

        isFinalized = true;
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
    
    public void registerAssault(AssaultInformation missionBattle)
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

    public MissionBalloons getMissionBalloons()
    {
        return missionBalloons;
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

    public MissionBattleManager getMissionBattleManager()
    {
        return missionBattleManager;
    }

    public SinglePlayerMissionPlaneLimiter getMissionPlaneLimiter()
    {
        return missionPlaneLimiter;
    }

    public VehicleSetBuilderComprehensive getVehicleSetBuilder()
    {
        return vehicleSetBuilder;
    }

	public boolean isNightMission() 
	{
	    if (missionProfile == MissionProfile.NIGHT_TACTICAL_MISSION || missionProfile == MissionProfile.NIGHT_STRATEGIC_MISSION)
	    {
	        return true;
	    }
	    
		return false;
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
}
