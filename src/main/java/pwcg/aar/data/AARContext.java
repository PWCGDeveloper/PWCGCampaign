package pwcg.aar.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.aar.data.ui.UIDebriefData;
import pwcg.aar.inmission.phase1.parse.AARMissionLogRawData;
import pwcg.aar.inmission.phase2.logeval.AARMissionEvaluationData;
import pwcg.aar.inmission.phase3.reconcile.ReconciledInMissionData;
import pwcg.aar.outofmission.phase1.elapsedtime.ReconciledOutOfMissionData;
import pwcg.aar.prelim.AARPreliminaryData;
import pwcg.aar.tabulate.combatreport.UICombatReportData;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class AARContext
{
    private Campaign campaign;

    private AARPreliminaryData preliminaryData = new AARPreliminaryData();
    private Date newDate;
    private ExtendedTimeReason reasonForExtendedTime = ExtendedTimeReason.NO_EXTENDED_TIME;

    // In mission
    private AARMissionLogRawData missionLogRawData = new AARMissionLogRawData();
    private AARMissionEvaluationData missionEvaluationData = new AARMissionEvaluationData();
    private ReconciledInMissionData reconciledInMissionData = new ReconciledInMissionData();

    // Out of mission
    private ReconciledOutOfMissionData reconciledOutOfMissionData = new ReconciledOutOfMissionData();

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
        reconciledInMissionData = new ReconciledInMissionData();
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

    public void setPreliminaryData(AARPreliminaryData preliminaryData)
    {
        this.preliminaryData = preliminaryData;
    }

    public void setMissionLogRawData(AARMissionLogRawData missionLogRawData)
    {
        this.missionLogRawData = missionLogRawData;
    }

    public void setMissionEvaluationData(AARMissionEvaluationData missionEvaluationData)
    {
        this.missionEvaluationData = missionEvaluationData;
    }

    public void setReconciledInMissionData(ReconciledInMissionData reconciledInMissionData)
    {
        this.reconciledInMissionData = reconciledInMissionData;
    }

    public ReconciledInMissionData getReconciledInMissionData()
    {
        return reconciledInMissionData;
    }

    public Date getNewDate()
    {
        return newDate;
    }

    public void setNewDate(Date newDate)
    {
        this.newDate = newDate;
    }

    public ExtendedTimeReason getReasonForExtendedTime()
    {
        return reasonForExtendedTime;
    }

    public void setReasonForExtendedTime(ExtendedTimeReason reasonForExtendedTime)
    {
        this.reasonForExtendedTime = reasonForExtendedTime;
    }

    public CampaignUpdateData getCampaignUpdateData()
    {
        return campaignUpdateData;
    }

    public void setCampaignUpdateData(CampaignUpdateData campaignUpdateData)
    {
        this.campaignUpdateData = campaignUpdateData;
    }

    public List<UICombatReportData> getUiCombatReportData()
    {
        return uiCombatReportData;
    }

    public void setUiCombatReportData(List<UICombatReportData> uiCombatReportData)
    {
        this.uiCombatReportData = uiCombatReportData;
    }

    public UIDebriefData getUiDebriefData()
    {
        return uiDebriefData;
    }

    public void mergeDebriefUiData(UIDebriefData uiDebriefData) throws PWCGException
    {
        this.uiDebriefData.merge(campaign, uiDebriefData);
    }

    public void setReconciledOutMissionData(ReconciledOutOfMissionData reconciledOutOfMissionDataForTimeStep)
    {
        this.reconciledOutOfMissionData = reconciledOutOfMissionDataForTimeStep;
    }

    public ReconciledOutOfMissionData getReconciledOutOfMissionData()
    {
        return reconciledOutOfMissionData;
    }

    public int getNextOutOfMissionEventSequenceNumber()
    {
        ++outOfMissionEventSequenceNumber;
        return outOfMissionEventSequenceNumber;
    }
}