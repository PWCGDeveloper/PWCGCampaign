package pwcg.aar.outofmission.phase1.elapsedtime;

import pwcg.aar.data.AAREquipmentLosses;
import pwcg.aar.data.AARPersonnelAwards;
import pwcg.aar.data.AARPersonnelLosses;
import pwcg.aar.outofmission.phase2.resupply.AARResupplyData;

public class ReconciledOutOfMissionData
{
    private AARPersonnelLosses personnelLossesOutOfMission = new AARPersonnelLosses();
    private AAREquipmentLosses equipmentLossesOutOfMission = new AAREquipmentLosses();
    private HistoricalAceAwards historicalAceEvents = new HistoricalAceAwards();
    private AARPersonnelAwards personnelAwards = new AARPersonnelAwards();
    private ElapsedTimeEvents elapsedTimeEvents = new ElapsedTimeEvents();
    private AARResupplyData resupplyData = new AARResupplyData();
    
    public void merge(ReconciledOutOfMissionData source)
    {        
        personnelLossesOutOfMission.merge(source.getPersonnelLossesOutOfMission());
        equipmentLossesOutOfMission.merge(source.getEquipmentLossesOutOfMission());
        personnelAwards.merge(source.getPersonnelAwards());
        resupplyData.merge(source.getResupplyData());
        historicalAceEvents.merge(source.getHistoricalAceEvents());
    }

    public AARPersonnelLosses getPersonnelLossesOutOfMission()
    {
        return personnelLossesOutOfMission;
    }

    public AAREquipmentLosses getEquipmentLossesOutOfMission()
    {
        return equipmentLossesOutOfMission;
    }

    public HistoricalAceAwards getHistoricalAceEvents()
    {
        return historicalAceEvents;
    }

    public AARPersonnelAwards getPersonnelAwards()
    {
        return personnelAwards;
    }

    public ElapsedTimeEvents getElapsedTimeEvents()
    {
        return elapsedTimeEvents;
    }

    public void setElapsedTimeEvents(ElapsedTimeEvents elapsedTimeEvents)
    {
        this.elapsedTimeEvents = elapsedTimeEvents;
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
