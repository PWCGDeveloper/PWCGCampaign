package pwcg.aar.inmission.phase3.reconcile.victories.coop;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.aar.inmission.phase3.reconcile.victories.common.ConfirmedVictories;
import pwcg.aar.inmission.phase3.reconcile.victories.common.VictorySorter;

public class CoopAirDeclarationResolver
{
    private ConfirmedVictories confirmedCoopAirVictories = new ConfirmedVictories();
    private VictorySorter victorySorter;

    public CoopAirDeclarationResolver (VictorySorter victorySorter)
    {
        this.victorySorter = victorySorter;
    }
    
    public ConfirmedVictories determineCoopAirResults()
    {
        for (LogVictory resultVictory : victorySorter.getFirmAirVictories())
        {
            confirmedCoopAirVictories.addVictory(resultVictory);
        }
        
        return confirmedCoopAirVictories;
    }
}
