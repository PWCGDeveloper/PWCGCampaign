package pwcg.aar.data;

import pwcg.aar.outofmission.phase3.resupply.AARResupplyData;

public class AARContextDailyData
{
    private AARPersonnelLosses personnelLosses = new AARPersonnelLosses();
    private AAREquipmentLosses equipmentLosses = new AAREquipmentLosses();

    private AARPersonnelAwards personnelAwards = new AARPersonnelAwards();
    private AARPersonnelAcheivements personnelAcheivements = new AARPersonnelAcheivements();
    private AARResupplyData resupplyData = new AARResupplyData();

    public AARPersonnelLosses getPersonnelLosses()
    {
        return personnelLosses;
    }

    public void setPersonnelLosses(AARPersonnelLosses personnelLosses)
    {
        this.personnelLosses = personnelLosses;
    }

    public AAREquipmentLosses getEquipmentLosses()
    {
        return equipmentLosses;
    }

    public void setEquipmentLosses(AAREquipmentLosses equipmentLosses)
    {
        this.equipmentLosses = equipmentLosses;
    }

    public AARPersonnelAwards getPersonnelAwards()
    {
        return personnelAwards;
    }

    public void setPersonnelAwards(AARPersonnelAwards personnelAwards)
    {
        this.personnelAwards = personnelAwards;
    }

    public AARPersonnelAcheivements getPersonnelAcheivements()
    {
        return personnelAcheivements;
    }

    public void setPersonnelAcheivements(AARPersonnelAcheivements personnelAcheivements)
    {
        this.personnelAcheivements = personnelAcheivements;
    }

    public AARResupplyData getResupplyData()
    {
        return resupplyData;
    }

    public void setResupplyData(AARResupplyData resupplyData)
    {
        this.resupplyData = resupplyData;
    }

}