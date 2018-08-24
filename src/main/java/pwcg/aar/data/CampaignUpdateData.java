package pwcg.aar.data;

import java.util.Date;

import pwcg.aar.outofmission.phase2.resupply.AARResupplyData;
import pwcg.campaign.Campaign;

public class CampaignUpdateData
{
    private Date newDate;
    private AARPersonnelLosses personnelLosses = new AARPersonnelLosses();
    private AAREquipmentLosses equipmentLosses = new AAREquipmentLosses();
    private AARResupplyData resupplyData = new AARResupplyData();
    private AARPersonnelAwards personnelAwards = new AARPersonnelAwards();
    private AARLogEvents logEvents;

    public CampaignUpdateData(Campaign campaign)
    {
        this.logEvents = new AARLogEvents(campaign);
    }

    public void merge(CampaignUpdateData sourceCampaignUpdateData)
	{
		newDate = sourceCampaignUpdateData.getNewDate();
        personnelLosses.merge(sourceCampaignUpdateData.getPersonnelLosses());
        equipmentLosses.merge(sourceCampaignUpdateData.getEquipmentLosses());
        resupplyData.merge(sourceCampaignUpdateData.getResupplyData());
        personnelAwards.merge(sourceCampaignUpdateData.getPersonnelAwards()); 
        logEvents.merge(sourceCampaignUpdateData.getLogEvents());
	}

    public void setNewDate(Date newDate)
    {
        this.newDate = newDate;
    }

    public Date getNewDate()
    {
        return newDate;
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

    public AARPersonnelAwards getPersonnelAwards()
    {
        return personnelAwards;
    }

    public AARLogEvents getLogEvents()
    {
        return logEvents;
    }
}
