package pwcg.aar.inmission.phase3.reconcile.victories.common;

import java.util.ArrayList;
import java.util.List;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;

public class ConfirmedVictories
{
    private List<LogVictory> confirmedVictories = new ArrayList<LogVictory>();

    public List<LogVictory> getConfirmedVictories()
    {
        return confirmedVictories;
    }

    public void addVictory(LogVictory playerVictory)
    {
        this.confirmedVictories.add(playerVictory);
    }
    
    public void addConfirmedVictories(ConfirmedVictories additionalVictories)
    {
        confirmedVictories.addAll(additionalVictories.getConfirmedVictories());
    }
    
}
