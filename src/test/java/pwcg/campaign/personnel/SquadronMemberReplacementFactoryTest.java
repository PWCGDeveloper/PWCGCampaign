package pwcg.campaign.personnel;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@RunWith(MockitoJUnitRunner.class)
public class SquadronMemberReplacementFactoryTest
{
    @Mock 
    private Campaign campaign;
    
    private Date campaignDate;
    private SerialNumber serialNumber = new SerialNumber();
    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        campaignDate = DateUtils.getDateYYYYMMDD("19420801");
        Mockito.when(campaign.getDate()).thenReturn(campaignDate);
        Mockito.when(campaign.getSerialNumber()).thenReturn(serialNumber);
    }

    @Test
    public void testCreateReplacementPilot() throws PWCGException
    {                
        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(20101);

        SquadronMemberReplacementFactory squadronMemberFactory = new  SquadronMemberReplacementFactory (campaign, service);
        SquadronMember replacement = squadronMemberFactory.createAIReplacementPilot();
        
        assert(replacement.isPlayer() == false);
        assert(replacement.getSerialNumber() >= SerialNumber.AI_STARTING_SERIAL_NUMBER);
        assert(replacement.getRank().equals("Oberfeldwebel") || replacement.getRank().equals("Leutnant"));
        assert(replacement.getSquadronId() == Squadron.REPLACEMENT);
    }
    

}
