package pwcg.aar.outofmission.phase1.elapsedtime;

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
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.product.bos.country.BoSServiceManager;

@ExtendWith(MockitoExtension.class)
public class OutOfMissionAAALossCalculatorTest
{
    @Mock
    private SquadronMember squadronMember;

    @Mock
    private Campaign campaign;

    @Mock
    private ConfigManagerCampaign configManager;

    @Mock
    private ArmedService service;
    
    @BeforeEach
    public void setupTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420801"));
    }
    
    @Test
    public void testPilotLostToAAAOddsGroundAttackRussianNovice() throws PWCGException
    {
        Mockito.when(squadronMember.getSquadronId()).thenReturn(10121312);
        Mockito.when(squadronMember.getAiSkillLevel()).thenReturn(AiSkillLevel.NOVICE);
        Mockito.when(squadronMember.determineService(campaign.getDate())).thenReturn(service);
        Mockito.when(service.getServiceId()).thenReturn(BoSServiceManager.VVS);
        
        OutOfMissionAAAOddsCalculator aaaLossOddsCalculator = new OutOfMissionAAAOddsCalculator(campaign);
        int odds = aaaLossOddsCalculator.oddsShotDownByAAA(squadronMember);
        assert(odds == 52);
    }
    
    @Test
    public void testPilotLostToAAAOddsGroundAttackGermanNovice() throws PWCGException
    {
        Mockito.when(squadronMember.getSquadronId()).thenReturn(10121312);
        Mockito.when(squadronMember.getAiSkillLevel()).thenReturn(AiSkillLevel.NOVICE);
        Mockito.when(squadronMember.determineService(campaign.getDate())).thenReturn(service);
        Mockito.when(service.getServiceId()).thenReturn(BoSServiceManager.LUFTWAFFE);
        
        OutOfMissionAAAOddsCalculator aaaLossOddsCalculator = new OutOfMissionAAAOddsCalculator(campaign);
        int odds = aaaLossOddsCalculator.oddsShotDownByAAA(squadronMember);
        assert(odds == 32);
    }
    
    @Test
    public void testPilotLostToAAAOddsDiveBombGermanVeteran() throws PWCGException
    {
        Mockito.when(squadronMember.getSquadronId()).thenReturn(20121077);
        Mockito.when(squadronMember.getAiSkillLevel()).thenReturn(AiSkillLevel.VETERAN);
        Mockito.when(squadronMember.determineService(campaign.getDate())).thenReturn(service);
        Mockito.when(service.getServiceId()).thenReturn(BoSServiceManager.LUFTWAFFE);
        
        OutOfMissionAAAOddsCalculator aaaLossOddsCalculator = new OutOfMissionAAAOddsCalculator(campaign);
        int odds = aaaLossOddsCalculator.oddsShotDownByAAA(squadronMember);
        assert(odds == 8);
    }
}
