package pwcg.aar.outofmission.phase2.resupply;

import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PlaneStatus;
import pwcg.campaign.resupply.ResupplyNeedBuilder;
import pwcg.campaign.resupply.equipment.EquipmentReplacementHandler;
import pwcg.campaign.resupply.equipment.EquipmentResupplyData;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EquipmentReplacementHandlerTest
{
    private Campaign campaign;
    
    @Mock private ArmedService armedService;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_MOSCOW);
     }

    @BeforeEach
    public void setupTest() throws PWCGException
    {        
        Mockito.when(armedService.getServiceId()).thenReturn(20101);
    }

    @Test
    public void testTransfersInForLostCampaignMembers() throws PWCGException
    {
        ResupplyNeedBuilder equipmentNeedBuilder = new ResupplyNeedBuilder(campaign, armedService);
        EquipmentReplacementHandler squadronTransferHandler = new EquipmentReplacementHandler(campaign, equipmentNeedBuilder);
        
        deactivateCampaignEquipment();
      
        EquipmentResupplyData equipmentTransferData = squadronTransferHandler.resupplyForLosses(armedService);
        Assertions.assertTrue (equipmentTransferData.getTransferCount() == 3);
    }

    private void deactivateCampaignEquipment() throws PWCGException
    {
        Date inactiveDate = DateUtils.removeTimeDays(campaign.getDate(), 10);
        Company playerSquadron = campaign.determinePlayerCompanies().get(0);
        int numInactivated = 0;
        for (Equipment equipment: campaign.getEquipmentManager().getEquipmentAllCompanies().values())
        {
            for (EquippedTank equippedPlane : equipment.getActiveEquippedTanks().values())
            {
                Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(equippedPlane.getCompanyId());
                if (squadron.getService() == armedService.getServiceId())
                {
                    if (playerSquadron.getCompanyId() != equippedPlane.getCompanyId())
                    {
                        equippedPlane.setPlaneStatus(TankStatus.STATUS_DESTROYED);
                        equippedPlane.setDateRemovedFromService(inactiveDate);
                        ++numInactivated;
                    }
                }
                
                break;
            }

            if (numInactivated == 3)
            {
                break;
            }
        }
    }

    @Test
    public void testTransfersInForLostCrewMembers() throws PWCGException
    {
        ResupplyNeedBuilder equipmentNeedBuilder = new ResupplyNeedBuilder(campaign, armedService);
        EquipmentReplacementHandler squadronTransferHandler = new EquipmentReplacementHandler(campaign, equipmentNeedBuilder);
        
        deactivateSquadronEquipment();
      
        EquipmentResupplyData equipmentTransferData = squadronTransferHandler.resupplyForLosses(armedService);
        Assertions.assertTrue (equipmentTransferData.getTransferCount() == 3);
    }

    private void deactivateSquadronEquipment() throws PWCGException
    {
        Date inactiveDate = DateUtils.removeTimeDays(campaign.getDate(), 10);

        int numInactivated = 0;
        Company playerSquadron = campaign.determinePlayerCompanies().get(0);
        Equipment equipment = campaign.getEquipmentManager().getEquipmentForCompany(playerSquadron.getCompanyId());
        for (EquippedTank equippedPlane : equipment.getActiveEquippedTanks().values())
        {
            Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(playerSquadron.getCompanyId());
            if (squadron.getCompanyId() == equippedPlane.getCompanyId())
            {                
                equippedPlane.setPlaneStatus(TankStatus.STATUS_DESTROYED);
                equippedPlane.setDateRemovedFromService(inactiveDate);
                ++numInactivated;
            }
                            
            if (numInactivated == 3)
            {
                break;
            }
        }
    }
}
