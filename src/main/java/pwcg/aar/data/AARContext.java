package pwcg.aar.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.aar.data.ui.UIDebriefData;
import pwcg.aar.inmission.phase1.parse.AARMissionLogRawData;
import pwcg.aar.inmission.phase2.logeval.AARMissionEvaluationData;
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
    private AARContextDailyData dailyData = new AARContextDailyData();
    
    private HistoricalAceAwards historicalAceEvents = new HistoricalAceAwards();
    private ElapsedTimeEvents elapsedTimeEvents = new ElapsedTimeEvents();

    // Tabulated
    private CampaignUpdateData campaignUpdateData;
    private List<UICombatReportData> uiCombatReportData = new ArrayList<>();
    private UIDebriefData uiDebriefData = new UIDebriefData();
    
    public AARContext(Campaign campaign)
    {
        this.campaign = campaign;
        this.campaignUpdateData = new CampaignUpdateData(campaign);
        
        AARContextEventSequence.reset();
    }

    public void resetContextForNextTimeIncrement() throws PWCGException
    {
        this.missionLogRawData = new AARMissionLogRawData();
        this.missionEvaluationData = new AARMissionEvaluationData();
        this.dailyData = new AARContextDailyData();
        this.campaignUpdateData = new CampaignUpdateData(campaign);
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

    public AARPersonnelLosses getPersonnelLosses()
    {
        return dailyData.getPersonnelLosses();
    }

    public AAREquipmentLosses getEquipmentLosses()
    {
        return dailyData.getEquipmentLosses();
    }

    public AARPersonnelAwards getPersonnelAwards()
    {
        return dailyData.getPersonnelAwards();
    }

    public AARPersonnelAcheivements getPersonnelAcheivements()
    {
        return dailyData.getPersonnelAcheivements();
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
        return dailyData.getResupplyData();
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

    public void addPersonnelLosses(AARPersonnelLosses newPersonnelLosses)
    {
        dailyData.getPersonnelLosses().merge(newPersonnelLosses);
    }

    public void addEquipmentLossesInMission(AAREquipmentLosses newEquipmentLosses)
    {
        dailyData.getEquipmentLosses().merge(newEquipmentLosses);
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
        dailyData.getResupplyData().merge(resupplyData);
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