package pwcg.aar.inmission.phase2.logeval;

import pwcg.aar.data.AARContext;
import pwcg.aar.inmission.phase1.parse.AARLogEventData;
import pwcg.aar.inmission.phase2.logeval.equipmentstatus.AAREquipmentStatusEvaluator;
import pwcg.aar.inmission.phase2.logeval.pilotstatus.AARPilotStatusEvaluator;
import pwcg.aar.inmission.phase2.logeval.victory.AARAreaOfCombat;
import pwcg.aar.inmission.phase2.logeval.victory.AARFuzzyByPlayerDamaged;
import pwcg.aar.inmission.phase2.logeval.victory.AARFuzzyVictoryEvaluator;
import pwcg.aar.inmission.phase2.logeval.victory.AARRandomAssignment;
import pwcg.aar.inmission.phase2.logeval.victory.AARRandomAssignmentCalculator;
import pwcg.aar.inmission.phase2.logeval.victory.AARVictoryEvaluator;
import pwcg.aar.prelim.PwcgMissionDataEvaluator;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class AAREvaluator 
{
    private AARDestroyedStatusEvaluator aarDestroyedStatusEvaluator;
    private AARDamageStatusEvaluator aarDamageStatusEvaluator;
    private AARVehicleBuilder aarVehicleBuilder;
    private AARVictoryEvaluator aarVictoryEvaluator;
    private AARPilotStatusEvaluator aarPilotStatusEvaluator;
    private AAREquipmentStatusEvaluator aarEquipmentStatusEvaluator;
    private AARChronologicalEventListBuilder aarChronologicalEventListBuilder;
    private AARWaypointBuilder waypointBuilder;
    
    private Campaign campaign;
    private AARContext aarContext;

    public AAREvaluator(Campaign campaign, AARContext aarContext)
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
    }

    public AARMissionEvaluationData determineMissionResults() throws PWCGException 
    {
        aarVehicleBuilder = createAARVehicleBuilder();
        aarVehicleBuilder.buildVehicleListsByVehicleType(aarContext.getMissionLogRawData().getLogEventData());
        
        aarDamageStatusEvaluator = new AARDamageStatusEvaluator(aarContext.getMissionLogRawData().getLogEventData(), aarVehicleBuilder);
        aarDamageStatusEvaluator.buildDamagedList();
        
        aarDestroyedStatusEvaluator = new AARDestroyedStatusEvaluator(aarContext.getMissionLogRawData().getLogEventData(), aarVehicleBuilder, aarDamageStatusEvaluator);
        aarDestroyedStatusEvaluator.buildDeadLists();
        
        aarPilotStatusEvaluator = new AARPilotStatusEvaluator(
                campaign, aarContext.getPreliminaryData().getPwcgMissionData(), aarDestroyedStatusEvaluator, aarContext.getMissionLogRawData().getLogEventData(), aarVehicleBuilder);
        aarPilotStatusEvaluator.determineFateOfCrewsInMission();
        
        aarEquipmentStatusEvaluator = new AAREquipmentStatusEvaluator(campaign, aarContext.getMissionLogRawData().getLogEventData(), aarVehicleBuilder);
        aarEquipmentStatusEvaluator.determineFateOfPlanesInMission();
        
        aarVictoryEvaluator = createAARVictoryEvaluator();
        aarVictoryEvaluator.evaluateVictories();

        waypointBuilder = new AARWaypointBuilder(aarContext.getMissionLogRawData().getLogEventData());
        aarChronologicalEventListBuilder = new AARChronologicalEventListBuilder(this, waypointBuilder);
        aarChronologicalEventListBuilder.buildChronologicalList();
                
        return createMissionEvaluation();
    }

    
    private AARMissionEvaluationData createMissionEvaluation() throws PWCGException
    {
        AARCrewBuilder crewBuilder= new AARCrewBuilder(aarVehicleBuilder.getLogPlanes());

        AARMissionEvaluationData evaluationData = new AARMissionEvaluationData();
        evaluationData.setPlaneAiEntities(aarVehicleBuilder.getLogPlanes());
        evaluationData.setVictoryResults(aarVictoryEvaluator.getVictoryResults());
        evaluationData.setPilotsInMission(crewBuilder.buildPilotsFromLogPlanes());
        evaluationData.setAceCrewsInMission(crewBuilder.buildAcesFromLogPlanes());
        evaluationData.setChronologicalEvents(aarChronologicalEventListBuilder.getChronologicalEvents());

        return evaluationData;
    }
    
    private AARVehicleBuilder createAARVehicleBuilder() throws PWCGException
    {
        AARBotVehicleMapper botPlaneMapper = new AARBotVehicleMapper(aarContext.getMissionLogRawData().getLogEventData());
        AARVehiclePlaneLanded landedMapper = new AARVehiclePlaneLanded(aarContext.getMissionLogRawData().getLogEventData());
        PwcgMissionDataEvaluator pwcgMissionDataEvaluator = new PwcgMissionDataEvaluator(campaign, aarContext.getPreliminaryData());
        return new AARVehicleBuilder(botPlaneMapper, landedMapper, pwcgMissionDataEvaluator);
    }
    
    private AARVictoryEvaluator createAARVictoryEvaluator()
    {
        AARAreaOfCombat areaOfCombat = new AARAreaOfCombat(aarDestroyedStatusEvaluator.getDeadLogVehicleList());
        AARFuzzyVictoryEvaluator fuzzyVictoryEvaluator = createAARFuzzyVictoryEvaluator(areaOfCombat);
        return new AARVictoryEvaluator(campaign, aarContext.getPreliminaryData().getPwcgMissionData(), fuzzyVictoryEvaluator, aarDestroyedStatusEvaluator);
    }
    
    private AARFuzzyVictoryEvaluator createAARFuzzyVictoryEvaluator(AARAreaOfCombat areaOfCombat)
    {
        AARFuzzyByPlayerDamaged fuzzyByPlayerDamaged = new AARFuzzyByPlayerDamaged(aarDamageStatusEvaluator);
        AARRandomAssignment randomAssignment = createAARRandomAssignment(aarContext.getMissionLogRawData().getLogEventData(), areaOfCombat);
        return new AARFuzzyVictoryEvaluator(aarVehicleBuilder, fuzzyByPlayerDamaged, randomAssignment);        
    }
    
    private AARRandomAssignment createAARRandomAssignment(AARLogEventData logEventData, AARAreaOfCombat areaOfCombat)
    {
        AARRandomAssignmentCalculator randomAssignmentCalculator = new AARRandomAssignmentCalculator(areaOfCombat);
        return new AARRandomAssignment(logEventData, randomAssignmentCalculator);
    }

    public AARDamageStatusEvaluator getAarDamageStatusEvaluator()
    {
        return aarDamageStatusEvaluator;
    }

    public AARVehicleBuilder getAarVehicleBuilder()
    {
        return aarVehicleBuilder;
    }

    public AARVictoryEvaluator getAarVictoryEvaluator()
    {
        return aarVictoryEvaluator;
    }

    public AARPilotStatusEvaluator getAarPilotStatusEvaluator()
    {
        return aarPilotStatusEvaluator;
    }
}

