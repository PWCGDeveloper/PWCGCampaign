package pwcg.campaign;

import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.personnel.InitialCompanyStaffer;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.resupply.InitialSquadronEquipper;
import pwcg.campaign.resupply.depot.EquipmentWeightCalculator;
import pwcg.campaign.squadron.Company;
import pwcg.core.exception.PWCGException;

public class CampaignSquadronGenerator 
{
    private Campaign campaign;
    private Company squadron;
    
    public CampaignSquadronGenerator(Campaign campaign, Company squadron)
    {
        this.campaign = campaign;
        this.squadron = squadron;
    }
    
    public void createSquadron(CampaignGeneratorModel generatorModel) throws PWCGException
    {
        createSquadronStaff(generatorModel);
        createSquadronEquipment(generatorModel);
    }

    public void createSquadronStaff(CampaignGeneratorModel generatorModel) throws PWCGException
    {
        InitialCompanyStaffer squadronStaffer = new InitialCompanyStaffer(campaign, squadron);
        if (squadron.getSquadronId() == generatorModel.getCampaignSquadron().getSquadronId())
        {
            squadronStaffer.addPlayerToCampaign(generatorModel);
        }
        CompanyPersonnel squadronPersonnel = squadronStaffer.generatePersonnel();
        campaign.getPersonnelManager().addPersonnelForSquadron(squadronPersonnel);
    }

    public void createSquadronEquipment(CampaignGeneratorModel generatorModel) throws PWCGException
    {
        EquipmentWeightCalculator equipmentWeightCalculator = new EquipmentWeightCalculator(campaign.getDate());
        InitialSquadronEquipper equipmentStaffer = new InitialSquadronEquipper(campaign, squadron, equipmentWeightCalculator);
        Equipment squadronEquipment = equipmentStaffer.generateEquipment();
        campaign.getEquipmentManager().addEquipmentForSquadron(squadron.getSquadronId(), squadronEquipment);
    }
}
