package pwcg.aar.campaign.update;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.io.json.CampaignEquipmentIOJson;
import pwcg.campaign.resupply.InitialCompanyEquipper;
import pwcg.campaign.resupply.depot.EquipmentWeightCalculator;
import pwcg.campaign.tank.Equipment;
import pwcg.core.exception.PWCGException;

public class CampaignUpdateNewCompanyEquipper
{
    private Campaign campaign;
    private List<Integer> squadronsEquipped = new ArrayList<>();
    
    public CampaignUpdateNewCompanyEquipper (Campaign campaign) 
    {
        this.campaign = campaign;
    }

    public List<Integer> equipNewSquadrons() throws PWCGException
    {
        CompanyManager squadronManager = PWCGContext.getInstance().getCompanyManager();
        for (Company squadron : squadronManager.getActiveCompanies(campaign.getDate()))
        {
            if (campaign.getEquipmentManager().getEquipmentForCompany(squadron.getCompanyId()) == null)
            {
                EquipmentWeightCalculator equipmentWeightCalculator = new EquipmentWeightCalculator(campaign.getDate());
                InitialCompanyEquipper equipmentStaffer = new InitialCompanyEquipper(campaign, squadron, equipmentWeightCalculator);
                Equipment squadronEquipment = equipmentStaffer.generateEquipment();
                campaign.getEquipmentManager().addEquipmentForCompany(squadron.getCompanyId(), squadronEquipment);
                squadronsEquipped.add(squadron.getCompanyId());
                CampaignEquipmentIOJson.writeEquipmentForSquadron(campaign, squadron.getCompanyId());
            }
        }
        
        return squadronsEquipped;
    }
}
