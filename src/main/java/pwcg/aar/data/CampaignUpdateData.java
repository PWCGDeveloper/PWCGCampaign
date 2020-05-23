package pwcg.aar.data;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.outofmission.phase2.resupply.AARResupplyData;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignEquipmentManager;
import pwcg.core.exception.PWCGException;

public class CampaignUpdateData
{
    private AARPersonnelLosses personnelLosses = new AARPersonnelLosses();
    private AAREquipmentLosses equipmentLosses = new AAREquipmentLosses();
    private AARResupplyData resupplyData = new AARResupplyData();
    private AARPersonnelAwards personnelAwards = new AARPersonnelAwards();
    private AARLogEvents logEvents;
    private Campaign campaign;

    public CampaignUpdateData(Campaign campaign)
    {
        this.campaign = campaign;
        this.logEvents = new AARLogEvents(campaign);
    }

    public void merge(CampaignUpdateData sourceCampaignUpdateData) throws PWCGException
	{
        CampaignEquipmentManager equipmentManager = campaign.getEquipmentManager();
        for (LogPlane planeDestroyed : sourceCampaignUpdateData.getEquipmentLosses().getPlanesDestroyed().values())
        {
            if (equipmentManager.getAnyPlane(planeDestroyed.getPlaneSerialNumber()) == null)
            {
                System.out.println("Destroyed plane is in depo");
            }
        }
        
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

    public AARPersonnelAwards getPersonnelAwards()
    {
        return personnelAwards;
    }

    public AARLogEvents getLogEvents()
    {
        return logEvents;
    }
}
