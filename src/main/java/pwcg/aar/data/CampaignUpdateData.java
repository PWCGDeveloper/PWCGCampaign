package pwcg.aar.data;

import java.util.Date;

import pwcg.aar.outofmission.phase3.resupply.AARResupplyData;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class CampaignUpdateData
{
    private AARPersonnelLosses personnelLosses = new AARPersonnelLosses();
    private AAREquipmentLosses equipmentLosses = new AAREquipmentLosses();
    private AARResupplyData resupplyData = new AARResupplyData();
    private AARPersonnelAwards personnelAwards = new AARPersonnelAwards();
    private AARPersonnelAcheivements personnelAcheivements = new AARPersonnelAcheivements();
    private AARLogEvents logEvents;
    private Date newDate;

    public CampaignUpdateData(Campaign campaign)
    {
        this.logEvents = new AARLogEvents(campaign);
    }

    public void merge(CampaignUpdateData sourceCampaignUpdateData) throws PWCGException
	{
        personnelLosses.merge(sourceCampaignUpdateData.getPersonnelLosses());
        equipmentLosses.merge(sourceCampaignUpdateData.getEquipmentLosses());
        resupplyData.merge(sourceCampaignUpdateData.getResupplyData());
        personnelAwards.merge(sourceCampaignUpdateData.getPersonnelAwards()); 
        logEvents.merge(sourceCampaignUpdateData.getLogEvents());
	}

    public AARPersonnelLosses getPersonnelLosses()
    {
        return personnelLosses;
    }

    public AAREquipmentLosses getEquipmentLosses()
    {
        return equipmentLosses;
    }

    public AARResupplyData getResupplyData()
    {
        return resupplyData;
    }

    public AARPersonnelAcheivements getPersonnelAcheivements()
    {
        return personnelAcheivements;
    }

    public AARPersonnelAwards getPersonnelAwards()
    {
        return personnelAwards;
    }

    public AARLogEvents getLogEvents()
    {
        return logEvents;
    }

    public Date getNewDate()
    {
        return newDate;
    }

    public void setNewDate(Date newDate)
    {
        this.newDate = newDate;
    }
}
