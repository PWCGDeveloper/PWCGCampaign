package pwcg.aar.outofmission.phase1.elapsedtime;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.ww2.country.BoSServiceManager;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@RunWith(MockitoJUnitRunner.class)
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
    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420801"));
        Mockito.when(campaign.getCampaignConfigManager()).thenReturn(configManager);
        Mockito.when(configManager.getIntConfigParam(Mockito.any())).thenReturn(5);
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
        assert(odds == 54);
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
        assert(odds == 34);
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
        assert(odds == 5);
    }
}
