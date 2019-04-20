package pwcg.aar.inmission.phase3.reconcile.victories.singleplayer;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;

class GroundDeclarationResolver
{
    private ConfirmedVictories confirmedGroundVictories = new ConfirmedVictories();
    private VictorySorter victorySorter;

    GroundDeclarationResolver (VictorySorter victorySorter)
    {
        this.victorySorter = victorySorter;
    }
    
    ConfirmedVictories determineGroundResults()
    {
        for (LogVictory resultVictory : victorySorter.getFirmGroundVictories())
        {
            confirmedGroundVictories.addVictory(resultVictory);
        }
        
        return confirmedGroundVictories;
    }
}
