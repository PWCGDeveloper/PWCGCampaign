package pwcg.campaign;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.personnel.InitialReplacementStaffer;
import pwcg.campaign.personnel.PersonnelReplacementsService;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.TestCampaignFactoryBuilder;
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
        campaign = TestCampaignFactoryBuilder.makeCampaign(this.getClass().getCanonicalName(), SquadronTestProfile.REGIMENT_503_PROFILE);
    }

    @Test
    public void testAceRetrieval() throws PWCGException
    {
        SquadronMember ace = campaign.getPersonnelManager().getAnyCampaignMember(201001);
        assert(ace.getName().equals("Hermann Graf"));
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
    	assert(campaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList().size() == 1);
        SquadronMember player = campaign.findReferencePlayer();
        SquadronMember playerBySerialNumber = campaign.getPersonnelManager().getAnyCampaignMember(player.getSerialNumber());
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
