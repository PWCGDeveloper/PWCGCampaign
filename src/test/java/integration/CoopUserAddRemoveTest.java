package integration;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGDirectoryUserManager;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberReplacer;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.io.json.CoopUserIOJson;
import pwcg.coop.CoopUserManager;
import pwcg.coop.model.CoopUser;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.CampaignRemover;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CampaignCacheBase;
import pwcg.testutils.SquadronTestProfile;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CoopUserAddRemoveTest
{
    private static Campaign coopCampaign;
    private static final String coopuser = "New Coop";
    private static final String personaName = "My CrewMember";
    private CrewMember newCrewMember;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        coopCampaign = CampaignCache.makeCampaignOnDisk(SquadronTestProfile.COOP_COMPETITIVE_PROFILE);
    }

    @AfterEach
    public void cleanup()
    {
        CampaignRemover.deleteCampaign(coopCampaign.getCampaignData().getName());
    }
    
    @Test
    public void testCoopCampaignLifeCycle() throws Exception
    {
        createCoopUser();
        createCoopPersonaCrewMember();
        removeUser();
    }

    private void createCoopUser() throws PWCGException
    {
        CoopUserManager.getIntance().buildCoopUser(coopuser);
        String coopUserDir = PWCGDirectoryUserManager.getInstance().getPwcgCoopDir();                    
        File coopUserFile = new File(coopUserDir + coopuser + ".json");
        assert(coopUserFile.exists());
    }
    
    private void createCoopPersonaCrewMember() throws Exception
    {
        CrewMemberReplacer squadronMemberReplacer = new CrewMemberReplacer(coopCampaign);
        newCrewMember = squadronMemberReplacer.createPersona(personaName, "Leutnant", "II./St.G.77", coopuser);
        coopCampaign.write();
        
        verifyNewCrewMember();        
        verifyNewCoopPersona();        
    }

    private void verifyNewCrewMember() throws PWCGException
    {
        CrewMember squadronMemberFromPersonnel = coopCampaign.getPersonnelManager().getAnyCampaignMember(newCrewMember.getSerialNumber());
        assert(squadronMemberFromPersonnel != null);
        assert(squadronMemberFromPersonnel.getCompanyId() == 20122077);
        assert(squadronMemberFromPersonnel.getCrewMemberActiveStatus() == CrewMemberStatus.STATUS_ACTIVE);
    }

    private void verifyNewCoopPersona() throws PWCGException
    {
        List<CoopUser> coopUsers = CoopUserIOJson.readCoopUsers();        
        boolean coopUserFileExists = false;
        boolean coopPersonaExists = false;
        for (CoopUser coopUser : coopUsers)
        {
            if (coopUser.getUsername().contentEquals(coopuser))
            {
                coopUserFileExists = true;
                for (int persona : coopUser.getUserPersonas(coopCampaign.getName()))
                {
                    if (persona == newCrewMember.getSerialNumber())
                    {
                        coopPersonaExists = true;
                    }
                }
            }
        }
        assert(coopUserFileExists);
        assert(coopPersonaExists);
    }


    private void removeUser() throws PWCGException
    {
        CoopUserManager.getIntance().removeCoopUser(coopuser);
        
        coopCampaign = new Campaign();
        coopCampaign.open(CampaignCacheBase.TEST_CAMPAIGN_NAME);

        String coopUserDir = PWCGDirectoryUserManager.getInstance().getPwcgCoopDir();                    
        File coopUserFile = new File(coopUserDir + coopuser + ".json");
        assert(!coopUserFile.exists());

        assert(CoopUserManager.getIntance().getCoopUser(coopuser) == null);

        CrewMember squadronMemberFromPersonnel = coopCampaign.getPersonnelManager().getAnyCampaignMember(newCrewMember.getSerialNumber());
        assert(squadronMemberFromPersonnel != null);
        assert(squadronMemberFromPersonnel.getCompanyId() == 20122077);
        assert(squadronMemberFromPersonnel.getCrewMemberActiveStatus() == CrewMemberStatus.STATUS_RETIRED);
    }

}
