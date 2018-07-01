package pwcg.campaign;

import pwcg.campaign.personnel.InitialReplacementStaffer;
import pwcg.campaign.personnel.PersonnelReplacementsService;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.InitialReplacementEquipper;
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
        createEquipmentReplacements();
    }

    private void createPersonnelReplacements() throws PWCGException
    {
        InitialReplacementStaffer initialReplacementStaffer = new InitialReplacementStaffer(campaign, armedService);
        SquadronMembers squadronMembers = initialReplacementStaffer.staffReplacementsForService();
        
        PersonnelReplacementsService replacementsForService = new PersonnelReplacementsService();
        replacementsForService.setReplacements(squadronMembers);
        replacementsForService.setServiceId(armedService.getServiceId());
        replacementsForService.setDailyReplacementRate(armedService.getDailyReplacementRate());
        replacementsForService.setLastReplacementDate(campaign.getDate());
        campaign.getPersonnelManager().addPersonnelReplacementsService(armedService.getServiceId(), replacementsForService);
    }

    private void createEquipmentReplacements() throws PWCGException
    {
        InitialReplacementEquipper replacementEquipper = new InitialReplacementEquipper(campaign, armedService);
        Equipment equipment = replacementEquipper.createReplacementPoolForService();
        campaign.getEquipmentManager().addEquipmentReplacementsForService(armedService.getServiceId(), equipment);
    }
}
