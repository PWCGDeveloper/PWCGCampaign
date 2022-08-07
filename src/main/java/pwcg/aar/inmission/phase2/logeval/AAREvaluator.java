package pwcg.aar.inmission.phase2.logeval;

import pwcg.aar.AARFactory;
import pwcg.aar.data.AARContext;
import pwcg.aar.inmission.phase2.logeval.equipmentstatus.AAREquipmentStatusEvaluator;
import pwcg.aar.inmission.phase2.logeval.pilotstatus.AARPilotStatusEvaluator;
import pwcg.aar.inmission.phase2.logeval.victory.AARVictoryEvaluator;
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
        aarVehicleBuilder.buildVehicleListsByVehicleType(aarContext.getLogEventData());
        
        aarDamageStatusEvaluator = new AARDamageStatusEvaluator(aarContext.getLogEventData(), aarVehicleBuilder);
        aarDamageStatusEvaluator.buildDamagedList();
        
        aarDestroyedStatusEvaluator = new AARDestroyedStatusEvaluator(aarContext.getLogEventData(), aarVehicleBuilder);
        aarDestroyedStatusEvaluator.buildDeadLists();
        
        aarPilotStatusEvaluator = new AARPilotStatusEvaluator(
                campaign, aarContext.getPreliminaryData().getPwcgMissionData(), aarDestroyedStatusEvaluator, aarContext.getLogEventData(), aarVehicleBuilder);
        aarPilotStatusEvaluator.determineFateOfCrewsInMission();
        
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
        evaluationData.setPilotsInMission(crewBuilder.buildPilotsFromLogPlanes());
        evaluationData.setChronologicalEvents(aarChronologicalEventListBuilder.getChronologicalEvents());

        return evaluationData;
    }
    
    private AARVehicleBuilder createAARVehicleBuilder() throws PWCGException
    {
        return AARFactory.makeAARVehicleBuilder(campaign, aarContext.getPreliminaryData(), aarContext.getLogEventData());
    }
    
    private AARVictoryEvaluator createAARVictoryEvaluator()
    {
        return new AARVictoryEvaluator(campaign, aarContext.getPreliminaryData().getPwcgMissionData(), aarDestroyedStatusEvaluator);
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

