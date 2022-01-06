package pwcg.aar.outofmission.phase2.resupply;

import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.campaign.resupply.ISquadronNeed;
import pwcg.campaign.resupply.ServiceResupplyNeed;
import pwcg.campaign.resupply.SquadronNeedFactory;
import pwcg.campaign.resupply.SquadronNeedFactory.SquadronNeedType;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.product.fc.country.FCServiceManager;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ServiceTransferNeedTest
{
    private Campaign campaign;
    private static final int JASTA_16 = 401016;
    
    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.JASTA_11_PROFILE);
     }

    @Test
    public void testTransfersWithNoCrewMembers() throws PWCGException
    {
        deactivateSquadronPersonnel();
        
        SquadronNeedFactory squadronNeedFactory = new SquadronNeedFactory(SquadronNeedType.PERSONNEL);
        ServiceResupplyNeed serviceTransferNeed = new ServiceResupplyNeed(campaign, FCServiceManager.DEUTSCHE_LUFTSTREITKRAFTE, squadronNeedFactory);
        serviceTransferNeed.determineResupplyNeed();
        
        Assertions.assertTrue (serviceTransferNeed.hasNeedySquadron() == true);
        
        boolean jasta16Needs = false;
        while (serviceTransferNeed.hasNeedySquadron())
        {
            ISquadronNeed selectedSquadronNeed = serviceTransferNeed.chooseNeedySquadron();
            if (selectedSquadronNeed.getSquadronId() == JASTA_16)
            {
                jasta16Needs = true;
                break;
            }
        }
        Assertions.assertTrue (jasta16Needs);
    }
    

    private void deactivateSquadronPersonnel() throws PWCGException
    {
        CrewMembers jasta16CrewMembers = CrewMemberFilter.filterActiveAI(campaign.getPersonnelManager().getCompanyPersonnel(JASTA_16).getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());
        int numInactivated = 0;
        for (CrewMember crewMember : jasta16CrewMembers.getCrewMemberList())
        {
            if (!crewMember.isPlayer())
            {
                crewMember.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_KIA, campaign.getDate(), null);
                Date inactiveDate = DateUtils.removeTimeDays(campaign.getDate(), 9);
                crewMember.setInactiveDate(inactiveDate);
                ++numInactivated;                
            }
            
            if (numInactivated == 3)
            {
                break;
            }
        }
    }
}
