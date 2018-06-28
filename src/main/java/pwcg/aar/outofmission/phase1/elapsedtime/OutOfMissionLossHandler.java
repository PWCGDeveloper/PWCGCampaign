package pwcg.aar.outofmission.phase1.elapsedtime;

import java.util.Map;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.AARPersonnelLosses;
import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;

public class OutOfMissionLossHandler
{
    private Campaign campaign;
    private AARContext aarContext;
    private AARPersonnelLosses outOfMissionPersonnelLosses;

    public OutOfMissionLossHandler(Campaign campaign, AARContext aarContext)
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
        outOfMissionPersonnelLosses = new AARPersonnelLosses();
    }

    public AARPersonnelLosses personellLosses(Map<Integer, SquadronMember> shotDownPilots) throws PWCGException
    {
        calculateHistoricalAceLosses();
        calculateShotDownLosses(shotDownPilots);
        calculateAAALosses();
        return outOfMissionPersonnelLosses;
    }

    private void calculateHistoricalAceLosses() throws PWCGException, PWCGException
    {
        OutOfMissionAceLossCalculator aceLossHandler = new OutOfMissionAceLossCalculator(campaign, aarContext);
        Map<Integer, Ace> acesKilled = aceLossHandler.acesKilledHistorically();
        outOfMissionPersonnelLosses.mergeAcesKilled(acesKilled);
    }    

    private void calculateShotDownLosses(Map<Integer, SquadronMember> shotDownPilots) throws PWCGException
    {
        PersonnelOutOfMissionStatusHandler personnelOutOfMissionHandler = new PersonnelOutOfMissionStatusHandler();
        personnelOutOfMissionHandler.determineFateOfShotDownPilots(shotDownPilots);
        
        outOfMissionPersonnelLosses.mergePersonnelCaptured(personnelOutOfMissionHandler.getAiCaptured());
        outOfMissionPersonnelLosses.mergePersonnelKilled(personnelOutOfMissionHandler.getAiKilled());
        outOfMissionPersonnelLosses.mergePersonnelMaimed(personnelOutOfMissionHandler.getAiMaimed());
    }
    
    private void calculateAAALosses() throws PWCGException
    {
        OutOfMissionAAAOddsCalculator oddsShotDownByAAACalculator = new OutOfMissionAAAOddsCalculator(campaign);
        OutOfMissionAAALossCalculator aaaLossCalculator = new OutOfMissionAAALossCalculator(campaign, aarContext, oddsShotDownByAAACalculator);
        Map<Integer, SquadronMember> lossesDueToAAA = aaaLossCalculator.pilotsLostToAAA();

        PersonnelOutOfMissionStatusHandler personnelOutOfMissionHandler = new PersonnelOutOfMissionStatusHandler();
        personnelOutOfMissionHandler.determineFateOfShotDownPilots(lossesDueToAAA);
        
        outOfMissionPersonnelLosses.mergePersonnelCaptured(personnelOutOfMissionHandler.getAiCaptured());
        outOfMissionPersonnelLosses.mergePersonnelKilled(personnelOutOfMissionHandler.getAiKilled());
        outOfMissionPersonnelLosses.mergePersonnelMaimed(personnelOutOfMissionHandler.getAiMaimed());
    }

	public AARPersonnelLosses getPersonnelLosses()
	{
		return outOfMissionPersonnelLosses;
	}
}
