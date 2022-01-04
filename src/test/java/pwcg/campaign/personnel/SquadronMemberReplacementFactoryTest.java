package pwcg.campaign.personnel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
public class CrewMemberReplacementFactoryTest
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
    public void testCreateReplacementCrewMember() throws Exception
    {                
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420601"));

        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(20101);

        CrewMemberReplacementFactory squadronMemberFactory = new  CrewMemberReplacementFactory (campaign, service);
        CrewMember replacement = squadronMemberFactory.createAIReplacementCrewMember();
        
        assert(replacement.isPlayer() == false);
        assert(replacement.getSerialNumber() >= SerialNumber.AI_STARTING_SERIAL_NUMBER);
        assert(replacement.getRank().equals("Oberfeldwebel") || replacement.getRank().equals("Leutnant"));
        assert(replacement.getCompanyId() == Company.REPLACEMENT);
        assert(replacement.getAiSkillLevel() == AiSkillLevel.COMMON);
    }

    @Test
    public void testCreateNoviceReplacementCrewMember() throws Exception
    {                
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19440602"));

        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(20101);

        CrewMemberReplacementFactory squadronMemberFactory = new  CrewMemberReplacementFactory (campaign, service);
        CrewMember replacement = squadronMemberFactory.createAIReplacementCrewMember();
        
        assert(replacement.isPlayer() == false);
        assert(replacement.getSerialNumber() >= SerialNumber.AI_STARTING_SERIAL_NUMBER);
        assert(replacement.getRank().equals("Oberfeldwebel") || replacement.getRank().equals("Leutnant"));
        assert(replacement.getCompanyId() == Company.REPLACEMENT);
        assert(replacement.getAiSkillLevel() == AiSkillLevel.NOVICE);
    }
    

}
