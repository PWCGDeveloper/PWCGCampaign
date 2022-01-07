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
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.campaign.resupply.ResupplyNeedBuilder;
import pwcg.campaign.resupply.personnel.SquadronTransferData;
import pwcg.campaign.resupply.personnel.TransferHandler;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.product.fc.country.FCServiceManager;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransferHandlerTest
{
    private Campaign campaign;
    
    @Mock private ArmedService armedService;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
    }

    @BeforeEach
    public void setupTest() throws PWCGException
    {        
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.JASTA_11_PROFILE);
        Mockito.when(armedService.getServiceId()).thenReturn(FCServiceManager.DEUTSCHE_LUFTSTREITKRAFTE);
    }

    @Test
    public void testTransfersInForLostCampaignMembers() throws PWCGException
    {
        ResupplyNeedBuilder transferNeedBuilder = new ResupplyNeedBuilder(campaign, armedService);
        TransferHandler squadronTransferHandler = new TransferHandler(campaign, transferNeedBuilder);
        
        deactivateCampaignPersonnel();
      
        SquadronTransferData squadronTransferData = squadronTransferHandler.determineCrewMemberTransfers(armedService);
        Assertions.assertTrue (squadronTransferData.getTransferCount() == 3);
    }

    private void deactivateCampaignPersonnel() throws PWCGException
    {
        int numInactivated = 0;
        CrewMembers allActiveCampaignMembers = CrewMemberFilter.filterActiveAI(campaign.getPersonnelManager().getAllCampaignMembers(), campaign.getDate());
        for (CrewMember crewMember : allActiveCampaignMembers.getCrewMemberList())
        {
            Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(crewMember.getCompanyId());
            if (!crewMember.isPlayer() && squadron.getService() == armedService.getServiceId())
            {
                Date inactiveDate = DateUtils.removeTimeDays(campaign.getDate(), 10);
                crewMember.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_KIA, inactiveDate, null);
                ++numInactivated;
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
        ResupplyNeedBuilder transferNeedBuilder = new ResupplyNeedBuilder(campaign, armedService);
        TransferHandler squadronTransferHandler = new TransferHandler(campaign, transferNeedBuilder);
        
        deactivateSquadronPersonnel();
      
        SquadronTransferData squadronTransferData = squadronTransferHandler.determineCrewMemberTransfers(armedService);
        Assertions.assertTrue (squadronTransferData.getTransferCount() == 3);
    }

    private void deactivateSquadronPersonnel() throws PWCGException
    {
        int numInactivated = 0;
        CrewMembers allActiveCampaignMembers = CrewMemberFilter.filterActiveAI(campaign.getPersonnelManager().getAllCampaignMembers(), campaign.getDate());
        for (CrewMember crewMember : allActiveCampaignMembers.getCrewMemberList())
        {
            Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(crewMember.getCompanyId());
            if (!crewMember.isPlayer() && squadron.getCompanyId() == SquadronTestProfile.JASTA_11_PROFILE.getCompanyId())
            {
                Date inactiveDate = DateUtils.removeTimeDays(campaign.getDate(), 10);
                crewMember.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_KIA, inactiveDate, null);
                ++numInactivated;
            }
            
            if (numInactivated == 3)
            {
                break;
            }
        }
    }
}
