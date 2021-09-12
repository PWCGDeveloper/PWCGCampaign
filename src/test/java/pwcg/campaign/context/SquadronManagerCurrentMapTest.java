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
import pwcg.campaign.plane.PwcgRole;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.squadron.SquadronManager;
import pwcg.core.exception.PWCGException;
import pwcg.mission.MissionSquadronRegistry;
import pwcg.mission.flight.escort.EscortSquadronSelector;
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
    public void getEscortOrEscortedSquadronAlliedTest() throws PWCGException
    {
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.RAF_184_PROFILE);

        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(SquadronTestProfile.RAF_184_PROFILE.getSquadronId());
        
        Squadron nearbySquadron = EscortSquadronSelector.getEscortedSquadron(campaign, squadron, squadron.determineCurrentPosition(campaign.getDate()));

        assert(nearbySquadron != null);
        assert(nearbySquadron.determineSide() == Side.ALLIED);
        boolean isBomb = nearbySquadron.getSquadronRoles().isSquadronThisRole(campaign.getDate(), PwcgRole.ROLE_BOMB);
        boolean isAttack = nearbySquadron.getSquadronRoles().isSquadronThisRole(campaign.getDate(), PwcgRole.ROLE_ATTACK);
        assert(isBomb || isAttack);
    }

    @Test
    public void getEscortOrEscortedSquadronAxisTest() throws PWCGException
    {
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_MOSCOW);

        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(SquadronTestProfile.JG_51_PROFILE_MOSCOW.getSquadronId());
                 
        Squadron nearbySquadron = EscortSquadronSelector.getEscortSquadron(campaign, squadron, squadron.determineCurrentPosition(campaign.getDate()), new MissionSquadronRegistry());
        assert(nearbySquadron != null);
        assert(nearbySquadron.determineSide() == Side.AXIS);
        assert(nearbySquadron.getSquadronRoles().isSquadronThisRole(campaign.getDate(), PwcgRole.ROLE_FIGHTER) == true);
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

        List<PwcgRole> roles = new ArrayList<PwcgRole>(Arrays.asList(PwcgRole.ROLE_BOMB, PwcgRole.ROLE_FIGHTER, PwcgRole.ROLE_ATTACK));
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

        List<PwcgRole> roles = new ArrayList<PwcgRole>(Arrays.asList(PwcgRole.ROLE_BOMB, PwcgRole.ROLE_FIGHTER));
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
