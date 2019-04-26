package pwcg.aar.campaign.update;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadrontTestProfile;

public class CampaignUpdateNewSquadronStafferTest
{
    private Campaign campaign;

    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        
        campaign = CampaignCache.makeCampaign(SquadrontTestProfile.JG_51_PROFILE_MOSCOW);
    }


    @Test
    public void testSquadronAdded() throws PWCGException
    {
        Date newDate = DateUtils.getDateYYYYMMDD("19420801");
        campaign.setDate(newDate);
        
        CampaignUpdateNewSquadronStaffer newSquadronStaffer = new CampaignUpdateNewSquadronStaffer(campaign);
        List<Integer> squadronsAdded = newSquadronStaffer.staffNewSquadrons();
        assert(squadronsAdded.size() > 0);
        for (int squadronId : squadronsAdded)
        {
            Squadron squadron = PWCGContextManager.getInstance().getSquadronManager().getSquadron(squadronId);
            assert(squadron.isCanFly(newDate));
            
            SquadronPersonnel squadronPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(squadronId);
            assert(squadronPersonnel != null);

            SquadronMembers squadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(squadronPersonnel.getSquadronMembersWithAces().getSquadronMemberCollection(),campaign.getDate());
            assert(squadronMembers != null);
            assert(squadronMembers.getActiveCount(campaign.getDate()) >= Squadron.SQUADRON_STAFF_SIZE);
        }
    }
}
