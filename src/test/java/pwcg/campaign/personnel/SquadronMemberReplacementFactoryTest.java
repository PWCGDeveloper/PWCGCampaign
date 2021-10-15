package pwcg.campaign.personnel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
public class SquadronMemberReplacementFactoryTest
{
    @Mock 
    private Campaign campaign;
    
    private SerialNumber serialNumber = new SerialNumber();
    
    @BeforeEach
    public void setupTest() throws PWCGException
     {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Mockito.when(campaign.getSerialNumber()).thenReturn(serialNumber);
    }

    @Test
    public void testCreateReplacementPilot() throws Exception
    {                
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420601"));

        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(20101);

        SquadronMemberReplacementFactory squadronMemberFactory = new  SquadronMemberReplacementFactory (campaign, service);
        SquadronMember replacement = squadronMemberFactory.createAIReplacementPilot();
        
        assert(replacement.isPlayer() == false);
        assert(replacement.getSerialNumber() >= SerialNumber.AI_STARTING_SERIAL_NUMBER);
        assert(replacement.getRank().equals("Oberfeldwebel") || replacement.getRank().equals("Leutnant"));
        assert(replacement.getSquadronId() == Squadron.REPLACEMENT);
        assert(replacement.getAiSkillLevel() == AiSkillLevel.COMMON);
    }

    @Test
    public void testCreateNoviceReplacementPilot() throws Exception
    {                
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19440602"));

        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(20101);

        SquadronMemberReplacementFactory squadronMemberFactory = new  SquadronMemberReplacementFactory (campaign, service);
        SquadronMember replacement = squadronMemberFactory.createAIReplacementPilot();
        
        assert(replacement.isPlayer() == false);
        assert(replacement.getSerialNumber() >= SerialNumber.AI_STARTING_SERIAL_NUMBER);
        assert(replacement.getRank().equals("Oberfeldwebel") || replacement.getRank().equals("Leutnant"));
        assert(replacement.getSquadronId() == Squadron.REPLACEMENT);
        assert(replacement.getAiSkillLevel() == AiSkillLevel.NOVICE);
    }
    

}
