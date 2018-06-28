package pwcg.aar.inmission.phase3.reconcile;

import pwcg.aar.data.AARPersonnelLosses;
import pwcg.aar.inmission.phase3.reconcile.victories.ReconciledVictoryData;

public class ReconciledInMissionData
{
    private ReconciledVictoryData reconciledVictoryData = new ReconciledVictoryData();
    private AARPersonnelLosses personnelResultsInMission = new AARPersonnelLosses();
    
    public ReconciledVictoryData getReconciledVictoryData()
    {
        return reconciledVictoryData;
    }
    
    public void setReconciledVictoryData(ReconciledVictoryData reconciledVictoryData)
    {
        this.reconciledVictoryData = reconciledVictoryData;
    }
    
    public AARPersonnelLosses getPersonnelLosses()
    {
        return personnelResultsInMission;
    }
    
    public void setPersonnelResultsInMission(AARPersonnelLosses personnelResultsInMission)
    {
        this.personnelResultsInMission = personnelResultsInMission;
    }
}
