package pwcg.aar.outofmission.phase1.elapsedtime;

import java.util.Map;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.AAREquipmentLosses;
import pwcg.aar.data.AARPersonnelLosses;
import pwcg.campaign.Campaign;
import pwcg.campaign.plane.EquippedPlane;
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

    public void lossesOutOfMission(Map<Integer, SquadronMember> shotDownPilots, Map<Integer, EquippedPlane> shotDownPlanes) throws PWCGException
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
    }

    private void calculateShotDownEquipmentLosses(Map<Integer, EquippedPlane> shotDownPlanes)
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
        Map<Integer, EquippedPlane> planeLossesDueToAAA = aaaLossCalculator.getPlanesLostDueToAAA();
        mergePlaneLosses(planeLossesDueToAAA);
    }

    private void mergePlaneLosses(Map<Integer, EquippedPlane> planeLossesDueToAAA)
    {
        AAREquipmentLosses equipmentLosses = new AAREquipmentLosses();
        for (EquippedPlane planeShotDown : planeLossesDueToAAA.values())
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
