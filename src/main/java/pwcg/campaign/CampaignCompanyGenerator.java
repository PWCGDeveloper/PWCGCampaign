package pwcg.campaign;

import pwcg.campaign.company.Company;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.personnel.InitialCompanyStaffer;
import pwcg.campaign.resupply.InitialCompanyEquipper;
import pwcg.campaign.resupply.depot.EquipmentWeightCalculator;
import pwcg.campaign.tank.Equipment;
import pwcg.core.exception.PWCGException;

public class CampaignCompanyGenerator 
{
    private Campaign campaign;
    private Company company;
    
    public CampaignCompanyGenerator(Campaign campaign, Company company)
    {
        this.campaign = campaign;
        this.company = company;
    }
    
    public void createSquadron(CampaignGeneratorModel generatorModel) throws PWCGException
    {
        createCompanyStaff(generatorModel);
        createCompanyEquipment(generatorModel);
    }

    public void createCompanyStaff(CampaignGeneratorModel generatorModel) throws PWCGException
    {
        InitialCompanyStaffer companyStaffer = new InitialCompanyStaffer(campaign, company);
        if (company.getCompanyId() == generatorModel.getCampaignCompany().getCompanyId())
        {
            companyStaffer.addPlayerToCampaign(generatorModel);
        }
        CompanyPersonnel companyPersonnel = companyStaffer.generatePersonnel();
        campaign.getPersonnelManager().addPersonnelForCompany(companyPersonnel);
    }

    public void createCompanyEquipment(CampaignGeneratorModel generatorModel) throws PWCGException
    {
        EquipmentWeightCalculator equipmentWeightCalculator = new EquipmentWeightCalculator(campaign.getDate());
        InitialCompanyEquipper equipmentStaffer = new InitialCompanyEquipper(campaign, company, equipmentWeightCalculator);
        Equipment companyEquipment = equipmentStaffer.generateEquipment();
        campaign.getEquipmentManager().addEquipmentForCompany(company.getCompanyId(), companyEquipment);
    }
}
