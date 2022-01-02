package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignMode;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.ground.builder.IndirectFireAssignmentHandler;
import pwcg.mission.ground.unittypes.GroundUnitEngagableAAAEvaluator;
import pwcg.mission.mcu.group.MissionObjectiveGroup;
import pwcg.mission.mcu.group.StopAttackingNearAirfieldSequence;
import pwcg.mission.options.MissionOptions;

public class MissionFinalizer
{
    private Mission mission;
    private Campaign campaign;
    private MissionFlights flights;
    private boolean isFinalized = false;
    private MissionFrontLineIconBuilder frontLineIconBuilder;
    private MissionWaypointIconBuilder waypointIconBuilder = new MissionWaypointIconBuilder();
    private MissionAirfieldIconBuilder airfieldIconBuilder = new MissionAirfieldIconBuilder();
    private MissionSquadronIconBuilder squadronIconBuilder;
    private MissionAssaultIconBuilder assaultIconBuilder = new MissionAssaultIconBuilder();
    private List<StopAttackingNearAirfieldSequence> stopSequenceForMission = new ArrayList<>();
    private MissionObjectiveGroup missionObjectiveSuccess = new MissionObjectiveGroup();
    private MissionObjectiveGroup missionObjectiveFailure = new MissionObjectiveGroup();

    public MissionFinalizer(Mission mission)
    {
        this.mission = mission;
        this.campaign = mission.getCampaign();
        this.flights = mission.getFlights();
    }

    public void finalizeMission() throws PWCGException
    {
        if (!isFinalized)
        {
            frontLineIconBuilder = new MissionFrontLineIconBuilder(campaign);
            squadronIconBuilder = new MissionSquadronIconBuilder(campaign);

            setMissionScript(mission.getMissionOptions());

            mission.getFlights().finalizeMissionFlights();
            mission.getGroundUnitBuilder().finalizeGroundUnits();
            
            frontLineIconBuilder.buildFrontLineIcons();
            waypointIconBuilder.createWaypointIcons(flights.getPlayerFlights());
            airfieldIconBuilder.createWaypointIcons(campaign, mission);
            assaultIconBuilder.createAssaultIcons(mission.getBattleManager().getMissionAssaultDefinitions());
            mission.getMissionBlocks().adjustBlockDamageAndSmoke();

            setGroundUnitTriggers();
            setFreeHuntTriggers();
            assignIndirectFireTargets();
            setEngagableAAA();

            if (flights.getPlayerFlights().size() > 1)
            {
                squadronIconBuilder.createSquadronIcons(flights.getPlayerFlights());
            }

            if (mission.getCampaign().getCampaignData().getCampaignMode() == CampaignMode.CAMPAIGN_MODE_SINGLE)
            {
                finalizeForSinglePlayer();
            }
            
            if (PWCGContext.getProduct() == PWCGProduct.FC)
            {
                FCBugHandler.fcBugs(mission);
            }

            MissionAnalyzer analyzer = new MissionAnalyzer();
            analyzer.analyze(mission);
        }

        getGroundUnitCount();
        
        isFinalized = true;
    }

    private void setMissionScript(MissionOptions missionOptions) throws PWCGException
    {
        List<PlaneMcu> playerPlanes = flights.getReferencePlayerFlight().getFlightPlanes().getPlayerPlanes();
        String playerScript = playerPlanes.get(0).getScript();
        missionOptions.setPlayerConfig(playerScript);
    }

    private void setGroundUnitTriggers() throws PWCGException
    {
        MissionCheckZoneTriggerBuilder missionCheckZoneTriggerBuilder = new MissionCheckZoneTriggerBuilder(mission);
        missionCheckZoneTriggerBuilder.triggerGroundUnits();
    }

    private void setFreeHuntTriggers() throws PWCGException
    {
        MissionFreeHuntTriggerBuilder freeHuntTriggerBuidler = new MissionFreeHuntTriggerBuilder(mission);
        freeHuntTriggerBuidler.buildFreeHuntTriggers();
    }

    private void assignIndirectFireTargets() throws PWCGException
    {
        IndirectFireAssignmentHandler indirectFireAssignmentHandler = new IndirectFireAssignmentHandler(mission);
        indirectFireAssignmentHandler.makeIndirectFireAssignments();
    }

    private void setEngagableAAA()
    {
        GroundUnitEngagableAAAEvaluator.setAAUnitsEngageableStatus(mission);        
    }

    private void finalizeForSinglePlayer() throws PWCGException
    {
        if (campaign.getCampaignData().getCampaignMode() == CampaignMode.CAMPAIGN_MODE_SINGLE)
        {
            missionObjectiveSuccess.createSuccessMissionObjective(campaign, mission);
            missionObjectiveFailure.createFailureMissionObjective(campaign, mission);
        }
    }

    private int getGroundUnitCount() throws PWCGException
    {
        int unitCountMissionGroundUnits = mission.getGroundUnitBuilder().getUnitCount();        
        int unitCountInFlights = 0;

        int unitCountInMission = 0;
        unitCountInMission += unitCountInFlights;
        unitCountInMission += unitCountMissionGroundUnits;

        PWCGLogger.log(LogLevel.INFO, "unit count flights : " + unitCountInFlights);
        PWCGLogger.log(LogLevel.INFO, "unit count misson : " + unitCountMissionGroundUnits);
        PWCGLogger.log(LogLevel.INFO, "unit count total : " + unitCountInMission);

        return unitCountInMission;
    }

    public MissionFrontLineIconBuilder getFrontLineIconBuilder()
    {
        return frontLineIconBuilder;
    }

    public void setFrontLineIconBuilder(MissionFrontLineIconBuilder frontLineIconBuilder)
    {
        this.frontLineIconBuilder = frontLineIconBuilder;
    }

    public MissionAirfieldIconBuilder getAirfieldIconBuilder()
    {
        return airfieldIconBuilder;
    }

    public void setAirfieldIconBuilder(MissionAirfieldIconBuilder airfieldIconBuilder)
    {
        this.airfieldIconBuilder = airfieldIconBuilder;
    }

    public MissionAssaultIconBuilder getAssaultIconBuilder()
    {
        return assaultIconBuilder;
    }

    public void setAssaultIconBuilder(MissionAssaultIconBuilder assaultIconBuilder)
    {
        this.assaultIconBuilder = assaultIconBuilder;
    }

    public boolean isFinalized()
    {
        return isFinalized;
    }

    public MissionWaypointIconBuilder getWaypointIconBuilder()
    {
        return waypointIconBuilder;
    }

    public MissionSquadronIconBuilder getSquadronIconBuilder()
    {
        return squadronIconBuilder;
    }

    public List<StopAttackingNearAirfieldSequence> getStopSequenceForMission()
    {
        return stopSequenceForMission;
    }

    public MissionObjectiveGroup getMissionObjectiveSuccess()
    {
        return missionObjectiveSuccess;
    }

    public MissionObjectiveGroup getMissionObjectiveFailure()
    {
        return missionObjectiveFailure;
    }
}
