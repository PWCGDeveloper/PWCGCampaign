package pwcg.mission;

import java.util.List;

import pwcg.aar.prelim.PwcgMissionData;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.IMissionFile;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.factory.MissionFileFactory;
import pwcg.campaign.io.json.CampaignMissionIOJson;
import pwcg.campaign.io.mission.MissionDescriptionFile;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;
import pwcg.mission.data.PwcgGeneratedMission;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.plane.PlaneMCU;
import pwcg.mission.mcu.group.MissionObjectiveGroup;
import pwcg.mission.options.MissionOptions;

public class Mission
{
    private Campaign campaign;

    private MissionFlightBuilder missionFlightBuilder;
    private MissionSquadronFinder missionParameters;
    private MissionPlaneLimiter missionPlaneLimiter = new MissionPlaneLimiter();
    private MissionObjectiveGroup missionObjectiveSuccess = new MissionObjectiveGroup();
    private MissionObjectiveGroup missionObjectiveFailure = new MissionObjectiveGroup();
    private MissionBalloons missionBalloons = new MissionBalloons();
    private MissionBattleManager missionBattleManager = new MissionBattleManager();
    private MissionGroundUnitResourceManager missionGroundUnitManager;
    private AmbientGroundUnitBuilder ambientGroundUnitBuilder;
    private MissionWaypointIconBuilder missionWaypointIconBuilder = new MissionWaypointIconBuilder();
    private MissionAirfieldIconBuilder missionAirfieldIconBuilder = new MissionAirfieldIconBuilder();
    private MissionFrontLineIconBuilder missionFrontLines;
    private MissionEffects missionEffects = new MissionEffects();
    private boolean isFinalized = false;

    public Mission()
    {
        MissionStringHandler subtitleHandler = MissionStringHandler.getInstance();
        subtitleHandler.clear();
    }

    public void initialize(Campaign campaign) throws PWCGException 
    {
        Logger.eraseLog();

        this.campaign = campaign;
        missionGroundUnitManager = new MissionGroundUnitResourceManager();
        ambientGroundUnitBuilder = new AmbientGroundUnitBuilder(campaign, this);
        missionFlightBuilder = new MissionFlightBuilder(campaign, this);
        missionFrontLines = new MissionFrontLineIconBuilder(campaign, this);
        PWCGContextManager.getInstance().getSkinManager().clearSkinsInUse();
        createMissionParameters();
    }

    private void createMissionParameters() throws PWCGException
    {
        MissionParameterBuilder missionParameterBuilder = new MissionParameterBuilder();        
        missionParameters = missionParameterBuilder.createMissionParameters(campaign);
    }

    public void generate(FlightTypes flightType) throws PWCGException 
    {
    	missionFlightBuilder.generateFlights(flightType);

        createFirePots();

        MissionOptions missionOptions = PWCGContextManager.getInstance().getCurrentMap().getMissionOptions();
        missionOptions.createFlightSpecificMissionOptions(missionFlightBuilder.getPlayerFlight());
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

        // Mission description
        MissionDescription missionDescription = new MissionDescription(campaign, this);
        missionDescription.createDescription();
        
        MissionDescriptionFile missionDescriptionFile = new MissionDescriptionFile();
        missionDescriptionFile.writeMissionDescription(missionDescription, campaign);

        writePwcgMissionData(missionDescription);
    }

    private void writePwcgMissionData(MissionDescription missionDescription) throws PWCGException
    {
        StringBuffer missionDescriptionText = new StringBuffer("");
        missionDescriptionText.append("Mission: \n");
        missionDescriptionText.append(missionDescription.getDesc());

        PwcgGeneratedMission pwcgMission = new PwcgGeneratedMission(campaign);
        PwcgMissionData pwcgMissionData = pwcgMission.generateMissionData(this);
        pwcgMissionData.setMissionDescription(missionDescriptionText.toString());
        
        CampaignMissionIOJson.writeJson(campaign, pwcgMissionData);
    }

    private void createFirePots() throws PWCGException 
    {
        if (missionFlightBuilder.getPlayerFlight().isNightFlight())
        {
            missionEffects.createFirePots(campaign.getPlayerAirfield());
        }
    }

    private void setMissionScript(MissionOptions missionOptions) throws PWCGException
    {
        PlaneMCU playerPlane = missionFlightBuilder.getPlayerFlight().getPlayerPlane();
        String playerScript = playerPlane.getScript();

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
        	missionWaypointIconBuilder.createWaypointIcons(missionFlightBuilder.getPlayerFlight());
        	missionAirfieldIconBuilder.createWaypointIcons(campaign, this);

            missionObjectiveSuccess.createSuccessMissionObjective();
            missionObjectiveFailure.createFailureMissionObjective(missionFlightBuilder.getPlayerFlight().getPlayerPlane());
            missionPlaneLimiter.createPlaneCountersToLimitPlanesSpawned(this, missionFlightBuilder.getPlayerFlight().getPlanes().size());

            MissionAnalyzer analyzer = new MissionAnalyzer();
            analyzer.analyze(this);

            this.write();
        }

        isFinalized = true;
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

    public MissionPlaneLimiter getMissionPlaneCalculator()
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

    public MissionSquadronFinder getMissionParameters()
    {
        return missionParameters;
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
}
