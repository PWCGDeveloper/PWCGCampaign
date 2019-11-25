package pwcg.aar.inmission.phase3.reconcile.victories.common;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.group.BlockDefinition;
import pwcg.campaign.group.BlockDefinitionManager;

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
            BlockDefinition blockDefinition = BlockDefinitionManager.getInstance().getBlockDefinition(resultVictory.getVictim().getVehicleType());
            if (blockDefinition == null || blockDefinition.getType().getIsTarget())
            {
                confirmedGroundVictories.addVictory(resultVictory);
            }
        }
        
        return confirmedGroundVictories;
    }
}
