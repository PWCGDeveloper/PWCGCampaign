package pwcg.aar.data;

import java.util.Date;

import pwcg.aar.outofmission.phase2.transfer.AARTransferData;

public class CampaignUpdateData
{
    private Date newDate;
    private AARPersonnelLosses personnelLosses = new AARPersonnelLosses();
    private AAREquipmentLosses equipmentLosses = new AAREquipmentLosses();
    private AARTransferData transferData = new AARTransferData();
    private AARPersonnelAwards personnelAwards = new AARPersonnelAwards();
    private AARLogEvents logEvents = new AARLogEvents();

    public CampaignUpdateData()
    {
        personnelLosses = new AARPersonnelLosses();
    }

    public void merge(CampaignUpdateData sourceCampaignUpdateData)
	{
		newDate = sourceCampaignUpdateData.getNewDate();
		personnelAwards.merge(sourceCampaignUpdateData.getPersonnelAwards()); 
        personnelLosses.merge(sourceCampaignUpdateData.getPersonnelLosses());
        transferData.merge(sourceCampaignUpdateData.getTransferData());
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

    public AARTransferData getTransferData()
    {
        return transferData;
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
