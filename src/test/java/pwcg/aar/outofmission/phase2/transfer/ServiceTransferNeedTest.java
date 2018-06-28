package pwcg.aar.outofmission.phase2.transfer;

import java.util.Date;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CampaignCacheRoF;

@RunWith(MockitoJUnitRunner.class)
public class ServiceTransferNeedTest
{
    private Campaign campaign;
    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(true);
        campaign = CampaignCache.makeCampaign(CampaignCacheRoF.JASTA_11_PROFILE);
     }

    @Test
    public void testTransfersWithNoSquadronMembers() throws PWCGException
    {
        deactivateSquadronPersonnel();
        
        ServiceTransferNeed serviceTransferNeed = new ServiceTransferNeed(campaign, 50101);
        serviceTransferNeed.determineTransferNeed();
        
        assert (serviceTransferNeed.hasNeedySquadron() == true);
        
        boolean jasta12Needs = false;
        while (serviceTransferNeed.hasNeedySquadron())
        {
            int squadronInNeed = serviceTransferNeed.chooseNeedySquadron();
            if (squadronInNeed == 501012)
            {
                jasta12Needs = true;
            }
        }
        assert (jasta12Needs);
    }
    

    private void deactivateSquadronPersonnel() throws PWCGException
    {
        Map<Integer, SquadronMember> jasta12SquadronMembers = campaign.getPersonnelManager().getSquadronPersonnel(501012).getActiveSquadronMembers().getSquadronMembers();
        int numInactivated = 0;
        for (SquadronMember squadronMember : jasta12SquadronMembers.values())
        {
            if (!squadronMember.isPlayer())
            {
                squadronMember.setPilotActiveStatus(SquadronMemberStatus.STATUS_KIA, campaign.getDate());
                Date inactiveDate = DateUtils.removeTimeDays(campaign.getDate(), 9);
                squadronMember.setInactiveDate(inactiveDate);
                ++numInactivated;
                
                System.out.println(squadronMember.getName() + " deactivated for test");
            }
            
            if (numInactivated == 3)
            {
                break;
            }
        }
    }
}
