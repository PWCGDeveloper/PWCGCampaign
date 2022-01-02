package pwcg.aar.outofmission.phase1.elapsedtime;

import java.util.List;
import java.util.Map;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.AAREquipmentLosses;
import pwcg.aar.data.AARPersonnelLosses;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.exception.PWCGException;

public class OutOfMissionLossHandler
{
    private Campaign campaign;
    private AARContext aarContext;
    private AARPersonnelLosses outOfMissionPersonnelLosses = new AARPersonnelLosses();
    private AAREquipmentLosses outOfMissionEquipmentLosses = new AAREquipmentLosses();

    public OutOfMissionLossHandler(Campaign campaign, AARContext aarContext)
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
    }

    public void lossesOutOfMission(Map<Integer, CrewMember> shotDownCrewMembers, Map<Integer, LogPlane> shotDownPlanes) throws PWCGException
    {
        calculateHistoricalAceLosses();
        calculateShotDownPersonnelLosses(shotDownCrewMembers);
        calculateShotDownEquipmentLosses(shotDownPlanes);
        calculateAAALosses();
    }

    private void calculateHistoricalAceLosses() throws PWCGException, PWCGException
    {
        OutOfMissionAceLossCalculator aceLossHandler = new OutOfMissionAceLossCalculator(campaign, aarContext);
        List<CrewMember> acesKilled = aceLossHandler.acesKilledHistorically();
        for (CrewMember deadAce : acesKilled)
        {
            outOfMissionPersonnelLosses.addPersonnelKilled(deadAce);
        }
    }    

    private void calculateShotDownPersonnelLosses(Map<Integer, CrewMember> shotDownCrewMembers) throws PWCGException
    {
        PersonnelOutOfMissionStatusHandler personnelOutOfMissionHandler = new PersonnelOutOfMissionStatusHandler();
        AARPersonnelLosses outOfMissionPersonnelLossesForTheDay = personnelOutOfMissionHandler.determineFateOfShotDownCrewMembers(shotDownCrewMembers);        
        outOfMissionPersonnelLosses.merge(outOfMissionPersonnelLossesForTheDay);
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
        Map<Integer, CrewMember> crewMemberLossesDueToAAA = aaaLossCalculator.getCrewMembersLostDueToAAA();
        PersonnelOutOfMissionStatusHandler personnelOutOfMissionHandler = new PersonnelOutOfMissionStatusHandler();
        AARPersonnelLosses outOfMissionPersonnelLossesForDay = personnelOutOfMissionHandler.determineFateOfShotDownCrewMembers(crewMemberLossesDueToAAA);
        outOfMissionPersonnelLosses.merge(outOfMissionPersonnelLossesForDay);
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
