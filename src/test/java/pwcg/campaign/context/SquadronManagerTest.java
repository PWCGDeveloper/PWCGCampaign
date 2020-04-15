package pwcg.campaign.context;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class SquadronManagerTest
{
    Campaign campaign;
    
    @Before
    public void setup() throws PWCGException
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
        Squadron germanSquadron = squadronManager.getSquadron(20111052);
        Side side = germanSquadron.determineSide();
        List<Squadron> squadrons = squadronManager.getActiveSquadronsForSide(side, campaign.getDate());
        
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
    public void getSquadronByProximityAndRoleAndSideTest() throws PWCGException
    {
        SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
        List<Squadron> squadrons = squadronManager.getActiveSquadrons(campaign.getDate());

        for (Squadron squadron : squadrons)
        {
            Squadron nearbySquadron = squadronManager.getSquadronByProximityAndRoleAndSide(
                    campaign, squadron.determineCurrentPosition(campaign.getDate()), Role.ROLE_BOMB, Side.ALLIED);
            assert(nearbySquadron != null);
            assert(nearbySquadron.determineSide() == Side.ALLIED);
            assert(nearbySquadron.getSquadronRoles().isSquadronThisRole(campaign.getDate(), Role.ROLE_BOMB) == true);
            
            nearbySquadron = squadronManager.getSquadronByProximityAndRoleAndSide(
                    campaign, squadron.determineCurrentPosition(campaign.getDate()), Role.ROLE_FIGHTER, Side.AXIS);
            assert(nearbySquadron != null);
            assert(nearbySquadron.determineSide() == Side.AXIS);
            assert(nearbySquadron.getSquadronRoles().isSquadronThisRole(campaign.getDate(), Role.ROLE_FIGHTER) == true);
        }
    }
}
