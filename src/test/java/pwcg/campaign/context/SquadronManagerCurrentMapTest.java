package pwcg.campaign.context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.squadron.SquadronManager;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class SquadronManagerCurrentMapTest
{
    Campaign campaign;
    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
    }

    @Test
    public void getSingleViableAiSquadronByRoleAndSideAndCurrentMapTest() throws PWCGException
    {
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.RAF_184_PROFILE);
        SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();

        List<Role> roles = new ArrayList<Role>(Arrays.asList(Role.ROLE_BOMB));
        Squadron nearbySquadron = squadronManager.getSingleViableAiSquadronByRoleAndSideAndCurrentMap(campaign, roles, Side.ALLIED, new ArrayList<>());
        assert(nearbySquadron != null);
        assert(nearbySquadron.determineSide() == Side.ALLIED);
        assert(nearbySquadron.getSquadronRoles().isSquadronThisRole(campaign.getDate(), Role.ROLE_BOMB) == true);
        
        roles = new ArrayList<Role>(Arrays.asList(Role.ROLE_FIGHTER));
        nearbySquadron = squadronManager.getSingleViableAiSquadronByRoleAndSideAndCurrentMap(campaign, roles, Side.AXIS, new ArrayList<>());
        assert(nearbySquadron != null);
        assert(nearbySquadron.determineSide() == Side.AXIS);
        assert(nearbySquadron.getSquadronRoles().isSquadronThisRole(campaign.getDate(), Role.ROLE_FIGHTER) == true);
    }

    @Test
    public void getViableAiSquadronsForCurrentMapAndSideTest() throws PWCGException
    {
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.RAF_184_PROFILE);
        SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();

        List<Squadron> squadrons = squadronManager.getViableAiSquadronsForCurrentMapAndSide(campaign, Side.ALLIED);
        
        boolean foundJG52 = false;
        boolean foundStg2 = false;
        boolean found132Reg = false;
        boolean foundHs129 = false;

        boolean foundRAF184 = false;
        boolean found352FG = false;
        boolean foundJG77 = false;
        boolean foundKG51 = false;
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
            else if (squadronName.equals("184 Squadron"))
            {
                foundRAF184 = true;
            }
            else if (squadronName.equals("352nd Fighter Group"))
            {
                found352FG = true;
            }
            else if (squadronName.equals("I./KG51"))
            {
                foundKG51 = true;
            }
            else if (squadronName.equals("I./JG77"))
            {
                foundJG77 = true;
            }
        }
        
        assert(!foundJG52);
        assert(!foundStg2);
        assert(!found132Reg);
        assert(!foundHs129);
        
        assert(!foundRAF184);
        assert(found352FG);
        assert(!foundKG51);
        assert(!foundJG77);
    }

    @Test
    public void getViableAiSquadronsForCurrentMapAndSideAndRoleAlliedTest() throws PWCGException
    {
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.RAF_184_PROFILE);
        SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();

        List<Role> roles = new ArrayList<Role>(Arrays.asList(Role.ROLE_BOMB, Role.ROLE_FIGHTER, Role.ROLE_ATTACK));
        List<Squadron> squadrons = squadronManager.getViableAiSquadronsForCurrentMapAndSideAndRole(campaign, roles, Side.ALLIED);
        
        boolean foundJG52 = false;
        boolean foundStg2 = false;
        boolean found132Reg = false;
        boolean foundHs129 = false;

        boolean foundRAF184 = false;
        boolean found352FG = false;
        boolean foundJG77 = false;
        boolean foundKG51 = false;
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
            else if (squadronName.equals("184 Squadron"))
            {
                foundRAF184 = true;
            }
            else if (squadronName.equals("352nd Fighter Group"))
            {
                found352FG = true;
            }
            else if (squadronName.equals("I./KG51"))
            {
                foundKG51 = true;
            }
            else if (squadronName.equals("I./JG77"))
            {
                foundJG77 = true;
            }
        }
        
        assert(!foundJG52);
        assert(!foundStg2);
        assert(!found132Reg);
        assert(!foundHs129);
        
        assert(!foundRAF184);
        assert(found352FG);
        assert(!foundKG51);
        assert(!foundJG77);
    }
    
    @Test
    public void getViableAiSquadronsForCurrentMapAndSideAndRoleAxisTest() throws PWCGException
    {
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.RAF_184_PROFILE);
        SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();

        List<Role> roles = new ArrayList<Role>(Arrays.asList(Role.ROLE_BOMB, Role.ROLE_FIGHTER));
        List<Squadron> squadrons = squadronManager.getViableAiSquadronsForCurrentMapAndSideAndRole(campaign, roles, Side.AXIS);
        
        boolean foundJG52 = false;
        boolean foundStg2 = false;
        boolean found132Reg = false;
        boolean foundHs129 = false;

        boolean foundRAF184 = false;
        boolean found352FG = false;
        boolean foundJG77 = false;
        boolean foundKG51 = false;
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
            else if (squadronName.equals("184 Squadron"))
            {
                foundRAF184 = true;
            }
            else if (squadronName.equals("352nd Fighter Group"))
            {
                found352FG = true;
            }
            else if (squadronName.equals("I./KG51"))
            {
                foundKG51 = true;
            }
            else if (squadronName.equals("I./JG77"))
            {
                foundJG77 = true;
            }
        }
        
        assert(!foundJG52);
        assert(!foundStg2);
        assert(!found132Reg);
        assert(!foundHs129);
        
        assert(!foundRAF184);
        assert(!found352FG);
        assert(!foundKG51);
        assert(foundJG77);
    }
}
