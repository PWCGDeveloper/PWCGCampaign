package pwcg.aar.outofmission.phase2.resupply;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.data.AARPersonnelLosses;
import pwcg.aar.outofmission.phase2.resupply.SquadronTransferData;
import pwcg.aar.outofmission.phase2.resupply.TransferHandler;
import pwcg.aar.outofmission.phase2.resupply.ResupplyNeedBuilder;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CampaignCacheRoF;

@RunWith(MockitoJUnitRunner.class)
public class TransferHandlerTest
{
    private Campaign campaign;
    
    @Mock private AARPersonnelLosses lossesInMissionData;

    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(true);
        campaign = CampaignCache.makeCampaign(CampaignCacheRoF.JASTA_11_PROFILE);
     }

    @Test
    public void testTransfersInForLostSquadronMembers() throws PWCGException
    {
        ResupplyNeedBuilder transferNeedBuilder = new ResupplyNeedBuilder(campaign);
        TransferHandler squadronTransferHandler = new TransferHandler(campaign, transferNeedBuilder);
        
        deactivateSquadronPersonnel();
      
        SquadronTransferData squadronTransferData = squadronTransferHandler.determineSquadronMemberTransfers();
        assert (squadronTransferData.getTransferCount() == 3);
    }

    private void deactivateSquadronPersonnel() throws PWCGException
    {
        int numInactivated = 0;
        for (SquadronMember squadronMember : campaign.getPersonnelManager().getAllCampaignMembers().values())
        {
            if (!squadronMember.isPlayer())
            {
                squadronMember.setPilotActiveStatus(SquadronMemberStatus.STATUS_KIA, campaign.getDate(), null);
                Date inactiveDate = DateUtils.removeTimeDays(campaign.getDate(), 9);
                squadronMember.setInactiveDate(inactiveDate);
                ++numInactivated;
            }
            
            if (numInactivated == 3)
            {
                break;
            }
        }
    }
}
