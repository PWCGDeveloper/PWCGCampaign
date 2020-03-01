package pwcg.campaign;

import pwcg.campaign.personnel.InitialReplacementStaffer;
import pwcg.campaign.personnel.PersonnelReplacementsService;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.resupply.depot.EquipmentDepot;
import pwcg.campaign.resupply.depot.EquipmentDepotInitializer;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;

public class CampaignEquipmentGenerator
{
    private Campaign campaign;
    private ArmedService armedService;
    
    public CampaignEquipmentGenerator(Campaign campaign, ArmedService armedService)
    {
        this.campaign = campaign;
        this.armedService = armedService;
    }
    
    public void createReplacements() throws PWCGException
    {
        createPersonnelReplacements();
        createEquipmentDepot();
    }

    private void createPersonnelReplacements() throws PWCGException
    {
        InitialReplacementStaffer initialReplacementStaffer = new InitialReplacementStaffer(campaign, armedService);
        SquadronMembers squadronMembers = initialReplacementStaffer.staffReplacementsForService();
        
        PersonnelReplacementsService replacementsForService = new PersonnelReplacementsService();
        replacementsForService.setReplacements(squadronMembers);
        replacementsForService.setServiceId(armedService.getServiceId());
        replacementsForService.setDailyReplacementRate(armedService.getDailyPersonnelReplacementRate());
        replacementsForService.setLastReplacementDate(campaign.getDate());
        campaign.getPersonnelManager().addPersonnelReplacementsService(armedService.getServiceId(), replacementsForService);
    }

    private void createEquipmentDepot() throws PWCGException
    {
        EquipmentDepotInitializer depotInitializer = new EquipmentDepotInitializer(campaign, armedService);
        Equipment equipment = depotInitializer.createReplacementPoolForService();
        EquipmentDepot depot = new EquipmentDepot();
        depot.setEquipmentPoints(armedService.getDailyEquipmentReplacementRate() * 2);
        depot.setLastReplacementDate(campaign.getDate());
        depot.setEquippment(equipment);
        campaign.getEquipmentManager().addEquipmentDepotForService(armedService.getServiceId(), depot);
    }
}
