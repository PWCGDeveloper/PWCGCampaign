package pwcg.campaign.squadron;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SquadronManagerTest
{
    Campaign campaign;
    
    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_MOSCOW);
    }

    @Test
    public void getSquadronTest() throws PWCGException
    {
        SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
        Squadron squadron = squadronManager.getSquadron(20111052);
        assert(squadron.determineDisplayName(campaign.getDate()).equals("I./JG52"));
    }

    @Test
    public void getActiveSquadronsTest() throws PWCGException
    {
        SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
        List<Squadron> squadrons = squadronManager.getActiveSquadrons(campaign.getDate());
        
        boolean foundJG52 = false;
        boolean foundStg2 = false;
        boolean found132Reg = false;
        boolean foundHs129 = false;
        for (Squadron squadron : squadrons)
        {
            String squadronName = squadron.determineDisplayName(campaign.getDate());
            if (squadronName.equals("I./JG52"))
            {
                foundJG52 = true;
            }
            else if (squadronName.equals("II./St.G.2"))
            {
                foundStg2 = true;
            }
            else if (squadronName.equals("132nd Bomber Air Regiment"))
            {
                found132Reg = true;
            }
            else if (squadronName.equals("IV(Pz)./Sch.G.2"))
            {
                foundHs129 = true;
            }
        }
        assert(foundJG52);
        assert(foundStg2);
        assert(found132Reg);
        assert(!foundHs129);
    }

    @Test
    public void getActiveSquadronsForSideTest() throws PWCGException
    {
        SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
        List<Squadron> squadrons = squadronManager.getActiveSquadronsForSide(campaign.getDate(), Side.AXIS);
        
        boolean foundJG52 = false;
        boolean foundStg2 = false;
        boolean found132Reg = false;
        boolean foundHs129 = false;
        for (Squadron squadron : squadrons)
        {
            String squadronName = squadron.determineDisplayName(campaign.getDate());
            if (squadronName.equals("I./JG52"))
            {
                foundJG52 = true;
            }
            else if (squadronName.equals("II./St.G.2"))
            {
                foundStg2 = true;
            }
            else if (squadronName.equals("132nd Bomber Air Regiment"))
            {
                found132Reg = true;
            }
            else if (squadronName.equals("IV(Pz)./Sch.G.2"))
            {
                foundHs129 = true;
            }
        }
        assert(foundJG52);
        assert(foundStg2);
        assert(!found132Reg);
        assert(!foundHs129);
    }

    @Test
    public void getViableSquadronsTest() throws PWCGException
    {
        int II_StG2_id = 20122002;
        SquadronPersonnel personnel = campaign.getPersonnelManager().getSquadronPersonnel(II_StG2_id);
        int numSaved = 0;
        for (SquadronMember squadronMember : personnel.getSquadronMembers().getSquadronMemberList())
        {
            if (numSaved > 4)
            {
                squadronMember.setPilotActiveStatus(SquadronMemberStatus.STATUS_KIA, campaign.getDate(), null);
            }
            ++numSaved;
        }
        
        SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
        List<Squadron> squadrons = squadronManager.getViableSquadrons(campaign);
        
        boolean foundJG52 = false;
        boolean foundStg2 = false;
        boolean found132Reg = false;
        boolean foundHs129 = false;
        for (Squadron squadron : squadrons)
        {
            String squadronName = squadron.determineDisplayName(campaign.getDate());
            if (squadronName.equals("I./JG52"))
            {
                foundJG52 = true;
            }
            else if (squadronName.equals("II./St.G.2"))
            {
                foundStg2 = true;
            }
            else if (squadronName.equals("132nd Bomber Air Regiment"))
            {
                found132Reg = true;
            }
            else if (squadronName.equals("IV(Pz)./Sch.G.2"))
            {
                foundHs129 = true;
            }
        }
        assert(foundJG52);
        assert(!foundStg2);
        assert(found132Reg);
        assert(!foundHs129);
    }
}
