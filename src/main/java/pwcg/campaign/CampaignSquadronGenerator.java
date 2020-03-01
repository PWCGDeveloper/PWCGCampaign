package pwcg.campaign;

import pwcg.campaign.personnel.InitialSquadronStaffer;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.resupply.InitialSquadronEquipper;
import pwcg.campaign.resupply.depot.EquipmentWeightCalculator;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class CampaignSquadronGenerator 
{
    private Campaign campaign;
    private Squadron squadron;
    
    public CampaignSquadronGenerator(Campaign campaign, Squadron squadron)
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
        InitialSquadronStaffer squadronStaffer = new InitialSquadronStaffer(campaign, squadron);
        if (squadron.getSquadronId() == generatorModel.getCampaignSquadron().getSquadronId())
        {
            squadronStaffer.addPlayerToCampaign(generatorModel);
        }
        SquadronPersonnel squadronPersonnel = squadronStaffer.generatePersonnel();
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
