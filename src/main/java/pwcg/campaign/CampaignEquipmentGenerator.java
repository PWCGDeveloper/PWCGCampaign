package pwcg.campaign;

import pwcg.campaign.personnel.InitialReplacementStaffer;
import pwcg.campaign.personnel.PersonnelReplacementsService;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.resupply.depo.EquipmentDepo;
import pwcg.campaign.resupply.depo.EquipmentDepoInitializer;
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
        createEquipmentDepo();
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

    private void createEquipmentDepo() throws PWCGException
    {
        EquipmentDepoInitializer depoInitializer = new EquipmentDepoInitializer(campaign, armedService);
        Equipment equipment = depoInitializer.createReplacementPoolForService();
        EquipmentDepo depo = new EquipmentDepo();
        depo.setEquipmentPoints(armedService.getDailyEquipmentReplacementRate() * 2);
        depo.setLastReplacementDate(campaign.getDate());
        depo.setEquippment(equipment);
        campaign.getEquipmentManager().addEquipmentDepoForService(armedService.getServiceId(), depo);
    }
}
