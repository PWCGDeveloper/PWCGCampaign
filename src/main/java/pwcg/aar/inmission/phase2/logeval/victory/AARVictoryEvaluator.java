package pwcg.aar.inmission.phase2.logeval.victory;

import java.util.ArrayList;
import java.util.List;

import pwcg.aar.inmission.phase2.logeval.AARDestroyedStatusEvaluator;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.BehindEnemyLines;
import pwcg.core.exception.PWCGException;

/**
 * Evaluate and assign victories
 * The rules:
 * 1. If it was definitely shot down by a known entity, credit that entity
 * 2. If the victor is unknown but the player damaged it, credit the player.
 * 3. If the victor is unknown, if the entity was destroyed near the squadrons route,
 * mark it for random assignment.
 * 
 * @author Patrick Wilson
 *
 */
public class AARVictoryEvaluator 
{
    private List <LogVictory> victoryResults = new ArrayList <LogVictory>();
    
    private Campaign campaign;
    private AARDestroyedStatusEvaluator aarDestroyedStatusEvaluator;
    
    public AARVictoryEvaluator(
    		Campaign campaign,
    		AARDestroyedStatusEvaluator aarDestroyedStatusEvaluator)
    {
        this.campaign = campaign;
        this.aarDestroyedStatusEvaluator = aarDestroyedStatusEvaluator;
    }

    public void evaluateVictories() throws PWCGException
    {
        buildVictoryList();        
        victoryResults.addAll(aarDestroyedStatusEvaluator.getDeadLogVehicleList());
    }
    
    private void buildVictoryList() throws PWCGException 
    {
        for (LogVictory logVictory : aarDestroyedStatusEvaluator.getDeadLogVehicleList())
        {
            determinePlaneCrashedInReportingRangeOfLines(logVictory);
        }
    }

    private void determinePlaneCrashedInReportingRangeOfLines(LogVictory missionResultVictory) throws PWCGException
    {
        if (missionResultVictory.getVictim() instanceof LogPlane)
        {
            LogPlane victimPlane = (LogPlane)missionResultVictory.getVictim();
            BehindEnemyLines pilotCapture = new BehindEnemyLines(campaign);
            boolean inReportingRange = pilotCapture.inReportingRange(missionResultVictory.getLocation(), victimPlane.getCountry().getSide());
            victimPlane.setCrashedInSight(inReportingRange);
        }
    }


    public List<LogVictory> getVictoryResults()
    {
        return victoryResults;
    }

    public void setVictoryResults(List<LogVictory> victoryResults)
    {
        this.victoryResults = victoryResults;
    }

}

