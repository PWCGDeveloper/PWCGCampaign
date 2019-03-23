package pwcg.campaign;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.personnel.InitialReplacementStaffer;
import pwcg.campaign.personnel.PersonnelReplacementsService;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadrontTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class CampaignPersonnelManagerTest
{
    Campaign campaign;
    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        campaign = CampaignCache.makeCampaign(SquadrontTestProfile.REGIMENT_503_PROFILE);
    }

    @Test
    public void testAceRetrieval() throws PWCGException
    {
        SquadronMember ace = campaign.getPersonnelManager().getAnyCampaignMember(201001);
        assert(ace.getName().equals("Hermann Graff"));
    }

    @Test
    public void testSquadronMemberRetrieval() throws PWCGException
    {
        SquadronMember aiPilot = campaign.getPersonnelManager().getAnyCampaignMember(SerialNumber.AI_STARTING_SERIAL_NUMBER + 10);
        assert(aiPilot != null);
    }

    @Test
    public void testPlayerRetrieval() throws PWCGException
    {
    	assert(campaign.getPersonnelManager().getAllPlayers().getSquadronMemberList().size() == 1);
    	SquadronMember playerByPlayers = campaign.getPersonnelManager().getAllPlayers().getSquadronMemberList().get(0);
        SquadronMember playerBySerialNumber = campaign.getPersonnelManager().getAnyCampaignMember(playerByPlayers.getSerialNumber());
        assert(playerBySerialNumber.isPlayer());
    }

    @Test
    public void testReplacementRetrieval() throws PWCGException
    {
        List<ArmedService> armedServices = ArmedServiceFactory.createServiceManager().getAllArmedServices();
        for (ArmedService service : armedServices)
        {
            PersonnelReplacementsService replacementsForService = campaign.getPersonnelManager().getPersonnelReplacementsService(service.getServiceId());
            assert(replacementsForService.getReplacements().getActiveCount(campaign.getDate()) == InitialReplacementStaffer.NUM_INITIAL_REPLACEMENTS);
        }
    }
}
