package pwcg.aar.outofmission.phase1.elapsedtime;

import java.util.Map;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.AAREquipmentLosses;
import pwcg.aar.data.AARPersonnelLosses;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;

public class OutOfMissionLossHandler
{
    private Campaign campaign;
    private AARContext aarContext;
    private AARPersonnelLosses outOfMissionPersonnelLosses;
    private AAREquipmentLosses outOfMissionEquipmentLosses;

    public OutOfMissionLossHandler(Campaign campaign, AARContext aarContext)
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
        outOfMissionPersonnelLosses = new AARPersonnelLosses();
        outOfMissionEquipmentLosses = new AAREquipmentLosses();
    }

    public void lossesOutOfMission(Map<Integer, SquadronMember> shotDownPilots, Map<Integer, LogPlane> shotDownPlanes) throws PWCGException
    {
        calculateHistoricalAceLosses();
        calculateShotDownPersonnelLosses(shotDownPilots);
        calculateShotDownEquipmentLosses(shotDownPlanes);
        calculateAAALosses();
    }

    private void calculateHistoricalAceLosses() throws PWCGException, PWCGException
    {
        OutOfMissionAceLossCalculator aceLossHandler = new OutOfMissionAceLossCalculator(campaign, aarContext);
        Map<Integer, Ace> acesKilled = aceLossHandler.acesKilledHistorically();
        outOfMissionPersonnelLosses.mergeAcesKilled(acesKilled);
    }    

    private void calculateShotDownPersonnelLosses(Map<Integer, SquadronMember> shotDownPilots) throws PWCGException
    {
        PersonnelOutOfMissionStatusHandler personnelOutOfMissionHandler = new PersonnelOutOfMissionStatusHandler();
        personnelOutOfMissionHandler.determineFateOfShotDownPilots(shotDownPilots);
        
        outOfMissionPersonnelLosses.mergePersonnelCaptured(personnelOutOfMissionHandler.getAiCaptured());
        outOfMissionPersonnelLosses.mergePersonnelKilled(personnelOutOfMissionHandler.getAiKilled());
        outOfMissionPersonnelLosses.mergePersonnelMaimed(personnelOutOfMissionHandler.getAiMaimed());
        outOfMissionPersonnelLosses.mergePersonnelWounded(personnelOutOfMissionHandler.getAiWounded());
    }

    private void calculateShotDownEquipmentLosses(Map<Integer, LogPlane> shotDownPlanes)
    {
        mergePlaneLosses(shotDownPlanes);
    }

    private void calculateAAALosses() throws PWCGException
    {
        OutOfMissionAAAOddsCalculator oddsShotDownByAAACalculator = new OutOfMissionAAAOddsCalculator(campaign);
        OutOfMissionAAALossCalculator aaaLossCalculator = new OutOfMissionAAALossCalculator(campaign, aarContext, oddsShotDownByAAACalculator);
        aaaLossCalculator.lostToAAA();
        
        calculatePersonnelLossesToAAA(aaaLossCalculator);
        calculateEquipmentLossesToAAA(aaaLossCalculator);
    }

    private void calculateEquipmentLossesToAAA(OutOfMissionAAALossCalculator aaaLossCalculator)
    {
        Map<Integer, LogPlane> planeLossesDueToAAA = aaaLossCalculator.getPlanesLostDueToAAA();
        mergePlaneLosses(planeLossesDueToAAA);
    }

    private void mergePlaneLosses(Map<Integer, LogPlane> planeLossesDueToAAA)
    {
        AAREquipmentLosses equipmentLosses = new AAREquipmentLosses();
        for (LogPlane planeShotDown : planeLossesDueToAAA.values())
        {
            equipmentLosses.addPlaneDestroyed(planeShotDown);
        }
        outOfMissionEquipmentLosses.merge(equipmentLosses);
    }

    private void calculatePersonnelLossesToAAA(OutOfMissionAAALossCalculator aaaLossCalculator) throws PWCGException
    {
        Map<Integer, SquadronMember> pilotLossesDueToAAA = aaaLossCalculator.getPilotsLostDueToAAA();
        PersonnelOutOfMissionStatusHandler personnelOutOfMissionHandler = new PersonnelOutOfMissionStatusHandler();
        personnelOutOfMissionHandler.determineFateOfShotDownPilots(pilotLossesDueToAAA);
        outOfMissionPersonnelLosses.mergePersonnelCaptured(personnelOutOfMissionHandler.getAiCaptured());
        outOfMissionPersonnelLosses.mergePersonnelKilled(personnelOutOfMissionHandler.getAiKilled());
        outOfMissionPersonnelLosses.mergePersonnelMaimed(personnelOutOfMissionHandler.getAiMaimed());
        outOfMissionPersonnelLosses.mergePersonnelWounded(personnelOutOfMissionHandler.getAiWounded());
    }

	public AARPersonnelLosses getOutOfMissionPersonnelLosses()
	{
		return outOfMissionPersonnelLosses;
	}

    public AAREquipmentLosses getOutOfMissionEquipmentLosses()
    {
        return outOfMissionEquipmentLosses;
    }
}
