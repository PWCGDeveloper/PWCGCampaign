package pwcg.aar.inmission.phase3.reconcile.victories;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;

public class GroundDeclarationResolver
{
    private ConfirmedVictories confirmedGroundVictories = new ConfirmedVictories();
    private VictorySorter victorySorter;

    public GroundDeclarationResolver (VictorySorter victorySorter)
    {
        this.victorySorter = victorySorter;
    }
    
    public ConfirmedVictories determineGroundResults()
    {
        for (LogVictory resultVictory : victorySorter.getFirmGroundVictories())
        {
            confirmedGroundVictories.addVictory(resultVictory);
        }
        
        return confirmedGroundVictories;
    }
}
