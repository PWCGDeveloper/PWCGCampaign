package pwcg.aar.inmission.phase3.reconcile;

import pwcg.aar.data.AAREquipmentLosses;
import pwcg.aar.data.AARPersonnelAwards;
import pwcg.aar.data.AARPersonnelLosses;
import pwcg.aar.inmission.phase3.reconcile.victories.ReconciledVictoryData;

public class ReconciledInMissionData
{
    private ReconciledVictoryData reconciledVictoryData = new ReconciledVictoryData();
    private AARPersonnelLosses personnelLossesInMission = new AARPersonnelLosses();
    private AAREquipmentLosses equipmentLossesInMission = new AAREquipmentLosses();
    private AARPersonnelAwards personnelAwards = new AARPersonnelAwards();
    
    public ReconciledVictoryData getReconciledVictoryData()
    {
        return reconciledVictoryData;
    }
    
    public void setReconciledVictoryData(ReconciledVictoryData reconciledVictoryData)
    {
        this.reconciledVictoryData = reconciledVictoryData;
    }
    
    public AARPersonnelLosses getPersonnelLossesInMission()
    {
        return personnelLossesInMission;
    }
    
    public void setPersonnelLossesInMission(AARPersonnelLosses personnelResultsInMission)
    {
        this.personnelLossesInMission = personnelResultsInMission;
    }

    public AAREquipmentLosses getEquipmentLossesInMission()
    {
        return equipmentLossesInMission;
    }

    public void setEquipmentLossesInMission(AAREquipmentLosses equipmentLossesInMission)
    {
        this.equipmentLossesInMission = equipmentLossesInMission;
    }

    public AARPersonnelAwards getPersonnelAwards()
    {
        return personnelAwards;
    }

    public void setPersonnelAwards(AARPersonnelAwards personnelAwards)
    {
        this.personnelAwards = personnelAwards;
    }
}
