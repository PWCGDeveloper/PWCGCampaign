package pwcg.coop;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignInitialWriter;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGDirectoryUserManager;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.io.json.CoopUserIOJson;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberReplacer;
import pwcg.campaign.squadmember.SquadronMemberStatus;
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
    private static final String personaName = "My Pilot";
    private SquadronMember newSquadronMember;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        coopCampaign = CampaignCache.makeCampaign(SquadronTestProfile.COOP_COMPETITIVE_PROFILE);
        CampaignInitialWriter.doInitialCampaignWrite(coopCampaign);
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
        createCoopPersonaSquadronMember();
        removeUser();
    }

    private void createCoopUser() throws PWCGException
    {
        CoopUserManager.getIntance().buildCoopUser(coopuser);
        String coopUserDir = PWCGDirectoryUserManager.getInstance().getPwcgCoopDir();                    
        File coopUserFile = new File(coopUserDir + coopuser + ".json");
        assert(coopUserFile.exists());
    }
    
    private void createCoopPersonaSquadronMember() throws Exception
    {
        SquadronMemberReplacer squadronMemberReplacer = new SquadronMemberReplacer(coopCampaign);
        newSquadronMember = squadronMemberReplacer.createPersona(personaName, "Leutnant", "II./St.G.77", coopuser);
        coopCampaign.write();
        
        verifyNewSquadronMember();        
        verifyNewCoopPersona();        
    }

    private void verifyNewSquadronMember() throws PWCGException
    {
        SquadronMember squadronMemberFromPersonnel = coopCampaign.getPersonnelManager().getAnyCampaignMember(newSquadronMember.getSerialNumber());
        assert(squadronMemberFromPersonnel != null);
        assert(squadronMemberFromPersonnel.getSquadronId() == 20122077);
        assert(squadronMemberFromPersonnel.getPilotActiveStatus() == SquadronMemberStatus.STATUS_ACTIVE);
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
                    if (persona == newSquadronMember.getSerialNumber())
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

        SquadronMember squadronMemberFromPersonnel = coopCampaign.getPersonnelManager().getAnyCampaignMember(newSquadronMember.getSerialNumber());
        assert(squadronMemberFromPersonnel != null);
        assert(squadronMemberFromPersonnel.getSquadronId() == 20122077);
        assert(squadronMemberFromPersonnel.getPilotActiveStatus() == SquadronMemberStatus.STATUS_RETIRED);
    }

}
