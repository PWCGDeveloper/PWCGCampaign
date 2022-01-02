package pwcg.aar.inmission.phase2.logeval;

import pwcg.aar.AARFactory;
import pwcg.aar.data.AARContext;
import pwcg.aar.inmission.phase2.logeval.crewmemberstatus.AARCrewMemberStatusEvaluator;
import pwcg.aar.inmission.phase2.logeval.equipmentstatus.AAREquipmentStatusEvaluator;
import pwcg.aar.inmission.phase2.logeval.victory.AARAreaOfCombat;
import pwcg.aar.inmission.phase2.logeval.victory.AARFuzzyByAccumulatedDamaged;
import pwcg.aar.inmission.phase2.logeval.victory.AARFuzzyVictoryEvaluator;
import pwcg.aar.inmission.phase2.logeval.victory.AARRandomAssignment;
import pwcg.aar.inmission.phase2.logeval.victory.AARRandomAssignmentCalculator;
import pwcg.aar.inmission.phase2.logeval.victory.AARVictoryEvaluator;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.logfiles.LogEventData;

public class AAREvaluator 
{
    private AARDestroyedStatusEvaluator aarDestroyedStatusEvaluator;
    private AARDamageStatusEvaluator aarDamageStatusEvaluator;
    private AARVehicleBuilder aarVehicleBuilder;
    private AARVictoryEvaluator aarVictoryEvaluator;
    private AARCrewMemberStatusEvaluator aarCrewMemberStatusEvaluator;
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
        aarVehicleBuilder.buildVehicleListsByVehicleType(aarContext.getLogEventData());
        
        aarDamageStatusEvaluator = new AARDamageStatusEvaluator(aarContext.getLogEventData(), aarVehicleBuilder);
        aarDamageStatusEvaluator.buildDamagedList();
        
        aarDestroyedStatusEvaluator = new AARDestroyedStatusEvaluator(aarContext.getLogEventData(), aarVehicleBuilder, aarDamageStatusEvaluator);
        aarDestroyedStatusEvaluator.buildDeadLists();
        
        aarCrewMemberStatusEvaluator = new AARCrewMemberStatusEvaluator(
                campaign, aarDestroyedStatusEvaluator, aarContext.getLogEventData(), aarVehicleBuilder);
        aarCrewMemberStatusEvaluator.determineFateOfCrewsInMission();
        
        aarEquipmentStatusEvaluator = new AAREquipmentStatusEvaluator(campaign, aarContext.getLogEventData(), aarVehicleBuilder);
        aarEquipmentStatusEvaluator.determineFateOfPlanesInMission();
        
        aarVictoryEvaluator = createAARVictoryEvaluator();
        aarVictoryEvaluator.evaluateVictories();

        waypointBuilder = new AARWaypointBuilder(aarContext.getLogEventData());
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
        evaluationData.setCrewMembersInMission(crewBuilder.buildCrewMembersFromLogPlanes());
        evaluationData.setChronologicalEvents(aarChronologicalEventListBuilder.getChronologicalEvents());

        return evaluationData;
    }
    
    private AARVehicleBuilder createAARVehicleBuilder() throws PWCGException
    {
        return AARFactory.makeAARVehicleBuilder(campaign, aarContext.getPreliminaryData(), aarContext.getLogEventData());
    }
    
    private AARVictoryEvaluator createAARVictoryEvaluator()
    {
        AARAreaOfCombat areaOfCombat = new AARAreaOfCombat(aarDestroyedStatusEvaluator.getDeadLogVehicleList());
        AARFuzzyVictoryEvaluator fuzzyVictoryEvaluator = createAARFuzzyVictoryEvaluator(areaOfCombat);
        return new AARVictoryEvaluator(campaign, aarContext.getPreliminaryData().getPwcgMissionData(), fuzzyVictoryEvaluator, aarDestroyedStatusEvaluator);
    }
    
    private AARFuzzyVictoryEvaluator createAARFuzzyVictoryEvaluator(AARAreaOfCombat areaOfCombat)
    {
        AARFuzzyByAccumulatedDamaged fuzzyByPlayerDamaged = new AARFuzzyByAccumulatedDamaged(aarDamageStatusEvaluator);
        AARRandomAssignment randomAssignment = createAARRandomAssignment(aarContext.getLogEventData(), areaOfCombat);
        return new AARFuzzyVictoryEvaluator(aarVehicleBuilder, fuzzyByPlayerDamaged, randomAssignment);        
    }
    
    private AARRandomAssignment createAARRandomAssignment(LogEventData logEventData, AARAreaOfCombat areaOfCombat)
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

    public AARCrewMemberStatusEvaluator getAarCrewMemberStatusEvaluator()
    {
        return aarCrewMemberStatusEvaluator;
    }
}

