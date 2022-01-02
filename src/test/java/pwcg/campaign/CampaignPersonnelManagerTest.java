package pwcg.campaign;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.personnel.InitialReplacementStaffer;
import pwcg.campaign.personnel.PersonnelReplacementsService;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CampaignPersonnelManagerTest
{
    private Campaign campaign;    
    
    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.REGIMENT_503_PROFILE);
    }

    @Test
    public void testAceRetrieval() throws PWCGException
    {
        CrewMember ace = campaign.getPersonnelManager().getAnyCampaignMember(201001);
        assert(ace.getName().equals("Hermann Graf"));
    }

    @Test
    public void testCrewMemberRetrieval() throws PWCGException
    {
        CrewMember aiCrewMember = campaign.getPersonnelManager().getAnyCampaignMember(SerialNumber.AI_STARTING_SERIAL_NUMBER + 10);
        assert(aiCrewMember != null);
    }

    @Test
    public void testPlayerRetrieval() throws PWCGException
    {
    	assert(campaign.getPersonnelManager().getAllActivePlayers().getCrewMemberList().size() == 1);
        CrewMember player = campaign.findReferencePlayer();
        CrewMember playerBySerialNumber = campaign.getPersonnelManager().getAnyCampaignMember(player.getSerialNumber());
        assert(playerBySerialNumber.isPlayer());
    }

    @Test
    public void testReplacementRetrieval() throws PWCGException
    {
        List<ArmedService> armedServices = ArmedServiceFactory.createServiceManager().getAllActiveArmedServices(campaign.getDate());
        for (ArmedService service : armedServices)
        {
            PersonnelReplacementsService replacementsForService = campaign.getPersonnelManager().getPersonnelReplacementsService(service.getServiceId());
            assert(replacementsForService.getReplacements().getActiveCount(campaign.getDate()) == InitialReplacementStaffer.NUM_INITIAL_REPLACEMENTS);
        }
    }
}
