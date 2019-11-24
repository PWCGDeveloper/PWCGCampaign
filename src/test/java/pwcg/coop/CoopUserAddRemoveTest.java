package pwcg.coop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignInitialWriter;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberReplacer;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.coop.model.CoopPersona;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CampaignCacheBase;
import pwcg.testutils.SquadronTestProfile;

public class CoopUserAddRemoveTest
{
    private Campaign coopCampaign;
    private static final String coopuser = "New Coop";
    private static final String personaName = "My Pilot";
    private SquadronMember newSquadronMember;

    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        coopCampaign = CampaignCache.makeCampaignForceCreation(SquadronTestProfile.COOP_COMPETITIVE_PROFILE);
        CampaignInitialWriter.doInitialCampaignWrite(coopCampaign);
    }

    @Test
    public void testCoopCampaignLifeCycle() throws Exception
    {
        createCoopUser();
        createCoopPersonaSquadronMember();
        verifyCoopUserState();
        verifyCoopPersonaState();
        removeUser();
    }

    private void createCoopUser() throws PWCGException
    {
        CoopUserManager.getIntance().buildCoopUser(coopuser);
        String coopUserDir = PWCGContext.getInstance().getDirectoryManager().getPwcgCoopDir() + "Users\\";                    
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
        CoopPersona persona = CoopPersonaManager.getIntance().getCoopPersona(personaName);
        assert(persona != null);
        assert(persona.getCampaignName().equals(coopCampaign.getCampaignData().getName()));

        String coopPersonaDir = PWCGContext.getInstance().getDirectoryManager().getPwcgCoopDir() + "Pilots\\";                    
        File coopPersonaFile = new File(coopPersonaDir + personaName + ".json");
        assert(coopPersonaFile.exists());
    }

    private void verifyCoopUserState() throws PWCGException
    {
        assert(CoopUserManager.getIntance().isDuplicateUser(CoopUserManager.HOST_USER_NAME) == true);
        assert(CoopUserManager.getIntance().isDuplicateUser(coopuser) == true);
        assert(CoopUserManager.getIntance().isDuplicateUser("Not A User") == false);
        
        assert(CoopUserManager.getIntance().getCoopHost().getUsername().equals(CoopUserManager.HOST_USER_NAME));
        assert(CoopUserManager.getIntance().getCoopUser(coopuser).getUsername().equals(coopuser));
        assert(CoopUserManager.getIntance().getCoopUser("Not A User") == null);

        int coopUserCountWithHost = CoopUserManager.getIntance().getAllCoopUsers().size();
        int coopUserCountWithoutHost = CoopUserManager.getIntance().getCoopUsersExceptHost().size();        
        assert(coopUserCountWithHost >= 1);
        assert(coopUserCountWithoutHost >= 1);
        assert((coopUserCountWithHost - coopUserCountWithoutHost) == 1);
        
        CoopUserManager.getIntance().setHostPassword("NewPassword");
        assert(CoopUserManager.getIntance().getCoopHost().getPassword().equals("NewPassword"));
        
        List<String> acceptedUsers = new ArrayList<>();
        List<String> rejectedUsers = new ArrayList<>();
        rejectedUsers.add(coopuser);
        CoopUserManager.getIntance().setUserAcceptedStatus(acceptedUsers, rejectedUsers);
        assert(CoopUserManager.getIntance().getCoopUser(coopuser).isApproved() == false);
        
        acceptedUsers = new ArrayList<>();
        rejectedUsers = new ArrayList<>();
        acceptedUsers.add(coopuser);
        CoopUserManager.getIntance().setUserAcceptedStatus(acceptedUsers, rejectedUsers);
        assert(CoopUserManager.getIntance().getCoopUser(coopuser).isApproved() == true);
    }

    private void verifyCoopPersonaState() throws PWCGException
    {
        assert(CoopPersonaManager.getIntance().getCoopPersona(personaName).getPilotName().equals(personaName));
        assert(CoopPersonaManager.getIntance().getCoopPersona(personaName).getCampaignName().equals(coopCampaign.getCampaignData().getName()));
        assert(CoopPersonaManager.getIntance().getCoopPersona(personaName).getUsername().equals(coopuser));
        
        assert(CoopPersonaManager.getIntance().getCoopPersonasForCampaign(coopCampaign).size() == 2);

        int coopPersonaCountWithHost = CoopPersonaManager.getIntance().getAllCoopPersonas().size();
        
        assert(coopPersonaCountWithHost >= 2);
    }


    private void removeUser() throws PWCGException
    {
        CoopUserManager.getIntance().removeCoopUser(coopuser);
        
        coopCampaign = new Campaign();
        coopCampaign.open(CampaignCacheBase.TEST_CAMPAIGN_NAME);

        String coopUserDir = PWCGContext.getInstance().getDirectoryManager().getPwcgCoopDir() + "Users\\";                    
        File coopUserFile = new File(coopUserDir + coopuser + ".json");
        assert(!coopUserFile.exists());

        String coopPersonaDir = PWCGContext.getInstance().getDirectoryManager().getPwcgCoopDir() + "Pilots\\";                    
        File coopPersonaFile = new File(coopPersonaDir + personaName + ".json");
        assert(!coopPersonaFile.exists());

        assert(CoopUserManager.getIntance().getCoopUser(coopuser) == null);

        SquadronMember squadronMemberFromPersonnel = coopCampaign.getPersonnelManager().getAnyCampaignMember(newSquadronMember.getSerialNumber());
        assert(squadronMemberFromPersonnel != null);
        assert(squadronMemberFromPersonnel.getSquadronId() == 20122077);
        assert(squadronMemberFromPersonnel.getPilotActiveStatus() == SquadronMemberStatus.STATUS_RETIRED);
    }

}
