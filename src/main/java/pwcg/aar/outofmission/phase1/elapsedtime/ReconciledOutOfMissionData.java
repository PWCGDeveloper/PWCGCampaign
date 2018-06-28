package pwcg.aar.outofmission.phase1.elapsedtime;

import pwcg.aar.data.AARPersonnelAwards;
import pwcg.aar.data.AARPersonnelLosses;
import pwcg.aar.outofmission.phase2.transfer.AARTransferData;

public class ReconciledOutOfMissionData
{
    private AARPersonnelLosses personnelLosses = new AARPersonnelLosses();
    private HistoricalAceAwards historicalAceEvents = new HistoricalAceAwards();
    private AARPersonnelAwards personnelAwards = new AARPersonnelAwards();
    private ElapsedTimeEvents elapsedTimeEvents = new ElapsedTimeEvents();
    private AARTransferData transferData = new AARTransferData();
    
    public void merge(ReconciledOutOfMissionData source)
    {
        personnelLosses.merge(source.getPersonnelLosses());
        personnelAwards.merge(source.getPersonnelAwards());
        transferData.merge(source.getTransferData());
        historicalAceEvents.merge(source.getHistoricalAceEvents());
    }

    public AARPersonnelLosses getPersonnelLosses()
    {
        return personnelLosses;
    }

    public void setPersonnelResults(AARPersonnelLosses personnelResults)
    {
        this.personnelLosses = personnelResults;
    }

    public HistoricalAceAwards getHistoricalAceEvents()
    {
        return historicalAceEvents;
    }

    public void setHistoricalAceEvents(HistoricalAceAwards historicalAceEvents)
    {
        this.historicalAceEvents = historicalAceEvents;
    }

    public AARPersonnelAwards getPersonnelAwards()
    {
        return personnelAwards;
    }

    public void setCampaignMemberAwards(AARPersonnelAwards campaignMemberAwards)
    {
        this.personnelAwards = campaignMemberAwards;
    }

    public ElapsedTimeEvents getElapsedTimeEvents()
    {
        return elapsedTimeEvents;
    }

    public void setElapsedTimeEvents(ElapsedTimeEvents elapsedTimeEvents)
    {
        this.elapsedTimeEvents = elapsedTimeEvents;
    }

    public AARTransferData getTransferData()
    {
        return transferData;
    }

    public void setTransferData(AARTransferData transferData)
    {
        this.transferData = transferData;
    }
}
