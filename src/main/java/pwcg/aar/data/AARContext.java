package pwcg.aar.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.aar.data.ui.UIDebriefData;
import pwcg.aar.inmission.phase1.parse.AARMissionLogRawData;
import pwcg.aar.inmission.phase2.logeval.AARMissionEvaluationData;
import pwcg.aar.inmission.phase3.reconcile.victories.ReconciledMissionVictoryData;
import pwcg.aar.outofmission.phase2.awards.HistoricalAceAwards;
import pwcg.aar.outofmission.phase3.resupply.AARResupplyData;
import pwcg.aar.outofmission.phase4.ElapsedTIme.ElapsedTimeEvents;
import pwcg.aar.prelim.AARPreliminaryData;
import pwcg.aar.tabulate.combatreport.UICombatReportData;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class AARContext
{
    private Campaign campaign;

    private AARPreliminaryData preliminaryData = new AARPreliminaryData();
    private Date newDate;

    // Inputs
    private AARMissionLogRawData missionLogRawData = new AARMissionLogRawData();
    private AARMissionEvaluationData missionEvaluationData = new AARMissionEvaluationData();
    

    // Outputs
    private ReconciledMissionVictoryData reconciledMissionVictoryData = new ReconciledMissionVictoryData();
    private AARPersonnelLosses personnelLosses = new AARPersonnelLosses();
    private AAREquipmentLosses equipmentLosses = new AAREquipmentLosses();
    
    private AARPersonnelAwards personnelAwards = new AARPersonnelAwards();
    private AARPersonnelAcheivements personnelAcheivements = new AARPersonnelAcheivements();
    private AARResupplyData resupplyData = new AARResupplyData();
    private HistoricalAceAwards historicalAceEvents = new HistoricalAceAwards();
    private ElapsedTimeEvents elapsedTimeEvents = new ElapsedTimeEvents();

    // Tabulated
    private CampaignUpdateData campaignUpdateData;
    private List<UICombatReportData> uiCombatReportData = new ArrayList<>();
    private UIDebriefData uiDebriefData = new UIDebriefData();
    
    private int outOfMissionEventSequenceNumber = 100000;

    public AARContext(Campaign campaign)
    {
        this.campaign = campaign;
        this.campaignUpdateData = new CampaignUpdateData(campaign);
    }

    public void resetContextForNextTimeIncrement() throws PWCGException
    {
        missionLogRawData = new AARMissionLogRawData();
        missionEvaluationData = new AARMissionEvaluationData();
    }
    
    public UICombatReportData findUiCombatReportDataForSquadron(int squadronId) throws PWCGException
    {
        for (UICombatReportData uiCombatReportDataForSquadron : uiCombatReportData)
        {
            if (uiCombatReportDataForSquadron.getSquadronId() == squadronId)
            {
                return uiCombatReportDataForSquadron;
            }
        }
        return new UICombatReportData(squadronId);
    }

    public AARPreliminaryData getPreliminaryData()
    {
        return preliminaryData;
    }

    public AARMissionLogRawData getMissionLogRawData()
    {
        return missionLogRawData;
    }

    public AARMissionEvaluationData getMissionEvaluationData()
    {
        return missionEvaluationData;
    }

    public CampaignUpdateData getCampaignUpdateData()
    {
        return campaignUpdateData;
    }

    public void mergeDebriefUiData(UIDebriefData uiDebriefData) throws PWCGException
    {
        this.uiDebriefData.merge(campaign, uiDebriefData);
    }

    public int getNextOutOfMissionEventSequenceNumber()
    {
        ++outOfMissionEventSequenceNumber;
        return outOfMissionEventSequenceNumber;
    }

    public ReconciledMissionVictoryData getReconciledMissionVictoryData()
    {
        return reconciledMissionVictoryData;
    }

    public AARPersonnelLosses getPersonnelLosses()
    {
        return personnelLosses;
    }

    public AAREquipmentLosses getEquipmentLosses()
    {
        return equipmentLosses;
    }

    public AARPersonnelAwards getPersonnelAwards()
    {
        return personnelAwards;
    }

    public AARPersonnelAcheivements getPersonnelAcheivements()
    {
        return personnelAcheivements;
    }

    public ElapsedTimeEvents getElapsedTimeEvents()
    {
        return elapsedTimeEvents;
    }
    
    public HistoricalAceAwards getHistoricalAceEvents()
    {
        return historicalAceEvents;
    }

    public AARResupplyData getResupplyData()
    {
        return resupplyData;
    }    

    public UIDebriefData getUiDebriefData()
    {
        return uiDebriefData;
    }

    public void setPreliminaryData(AARPreliminaryData preliminaryData)
    {
        this.preliminaryData = preliminaryData;
    }

    public void setMissionLogRawData(AARMissionLogRawData missionLogRawData)
    {
        this.missionLogRawData = missionLogRawData;        
    }

    public void setNewDate(Date newDate)
    {
        this.newDate = newDate;                
    }

    public void setMissionEvaluationData(AARMissionEvaluationData missionEvaluationData)
    {
        this.missionEvaluationData = missionEvaluationData;
    }

    public void setReconciledMissionVictoryData(ReconciledMissionVictoryData reconciledMissionVictoryData)
    {
        this.reconciledMissionVictoryData = reconciledMissionVictoryData;        
    }

    public void addPersonnelLosses(AARPersonnelLosses newPersonnelLosses)
    {
        personnelLosses.merge(newPersonnelLosses);
    }

    public void addEquipmentLossesInMission(AAREquipmentLosses newEquipmentLosses)
    {
        equipmentLosses.merge(newEquipmentLosses);
        
    }

    public Date getNewDate()
    {
        return newDate;
    }

    public void addElapsedTimeEvents(ElapsedTimeEvents elapsedTimeEvents)
    {
        this.elapsedTimeEvents.merge(elapsedTimeEvents);
    }

    public void addResupplyData(AARResupplyData resupplyData)
    {
        this.resupplyData.merge(resupplyData);
    }

    public void addUiCombatReportData(List<UICombatReportData> combatReportUiDataSet)
    {
        uiCombatReportData.addAll(combatReportUiDataSet);
    }

    public void setCampaignUpdateData(CampaignUpdateData campaignUpdateData)
    {
        this.campaignUpdateData = campaignUpdateData;                        
    }
}