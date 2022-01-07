package pwcg.campaign.context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.plane.PwcgRole;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.escort.EscortSquadronSelector;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SquadronManagerCurrentMapAlliedTest
{
    Campaign campaign;
    
    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.RAF_184_PROFILE);
    }

    @Test
    public void getEscortOrEscortedSquadronAlliedTest() throws PWCGException
    {

        Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(SquadronTestProfile.RAF_184_PROFILE.getCompanyId());
        
        Company nearbySquadron = EscortSquadronSelector.getEscortedSquadron(campaign, squadron, squadron.determineCurrentPosition(campaign.getDate()));

        assert(nearbySquadron != null);
        assert(nearbySquadron.determineSide() == Side.ALLIED);
        boolean isBomb = nearbySquadron.getSquadronRoles().isSquadronThisRole(campaign.getDate(), PwcgRole.ROLE_BOMB);
        boolean isAttack = nearbySquadron.getSquadronRoles().isSquadronThisRole(campaign.getDate(), PwcgRole.ROLE_ATTACK);
        assert(isBomb || isAttack);
    }

    @Test
    public void getViableAiSquadronsForCurrentMapAndSideTest() throws PWCGException
    {
        CompanyManager squadronManager = PWCGContext.getInstance().getCompanyManager();

        List<Company> squadrons = squadronManager.getViableAiCompaniesForCurrentMapAndSide(campaign, Side.ALLIED);
        
        boolean foundJG52 = false;
        boolean foundStg2 = false;
        boolean found132Reg = false;
        boolean foundHs129 = false;

        boolean foundRAF184 = false;
        boolean found352FG = false;
        boolean foundJG77 = false;
        boolean foundKG51 = false;
        for (Company squadron : squadrons)
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
        CompanyManager squadronManager = PWCGContext.getInstance().getCompanyManager();

        List<PwcgRole> roles = new ArrayList<PwcgRole>(Arrays.asList(PwcgRole.ROLE_BOMB, PwcgRole.ROLE_FIGHTER, PwcgRole.ROLE_ATTACK));
        List<Company> squadrons = squadronManager.getViableAiCompaniesForCurrentMapAndSideAndRole(campaign, roles, Side.ALLIED);
        
        boolean foundJG52 = false;
        boolean foundStg2 = false;
        boolean found132Reg = false;
        boolean foundHs129 = false;

        boolean foundRAF184 = false;
        boolean found352FG = false;
        boolean foundJG77 = false;
        boolean foundKG51 = false;
        for (Company squadron : squadrons)
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
        CompanyManager squadronManager = PWCGContext.getInstance().getCompanyManager();

        List<PwcgRole> roles = new ArrayList<PwcgRole>(Arrays.asList(PwcgRole.ROLE_BOMB, PwcgRole.ROLE_FIGHTER));
        List<Company> squadrons = squadronManager.getViableAiCompaniesForCurrentMapAndSideAndRole(campaign, roles, Side.AXIS);
        
        boolean foundJG52 = false;
        boolean foundStg2 = false;
        boolean found132Reg = false;
        boolean foundHs129 = false;

        boolean foundRAF184 = false;
        boolean found352FG = false;
        boolean foundJG77 = false;
        boolean foundKG51 = false;
        for (Company squadron : squadrons)
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
