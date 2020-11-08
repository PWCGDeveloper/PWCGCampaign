package pwcg.aar.campaign.update;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.io.json.CampaignEquipmentIOJson;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.resupply.InitialSquadronEquipper;
import pwcg.campaign.resupply.depot.EquipmentWeightCalculator;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.squadron.SquadronManager;
import pwcg.core.exception.PWCGException;

public class CampaignUpdateNewSquadronEquipper
{
    private Campaign campaign;
    private List<Integer> squadronsEquipped = new ArrayList<>();
    
    public CampaignUpdateNewSquadronEquipper (Campaign campaign) 
    {
        this.campaign = campaign;
    }

    public List<Integer> equipNewSquadrons() throws PWCGException
    {
        SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
        for (Squadron squadron : squadronManager.getActiveSquadrons(campaign.getDate()))
        {
            if (campaign.getEquipmentManager().getEquipmentForSquadron(squadron.getSquadronId()) == null)
            {
                EquipmentWeightCalculator equipmentWeightCalculator = new EquipmentWeightCalculator(campaign.getDate());
                InitialSquadronEquipper equipmentStaffer = new InitialSquadronEquipper(campaign, squadron, equipmentWeightCalculator);
                Equipment squadronEquipment = equipmentStaffer.generateEquipment();
                campaign.getEquipmentManager().addEquipmentForSquadron(squadron.getSquadronId(), squadronEquipment);
                squadronsEquipped.add(squadron.getSquadronId());
                CampaignEquipmentIOJson.writeEquipmentForSquadron(campaign, squadron.getSquadronId());
            }
        }
        
        return squadronsEquipped;
    }
}
