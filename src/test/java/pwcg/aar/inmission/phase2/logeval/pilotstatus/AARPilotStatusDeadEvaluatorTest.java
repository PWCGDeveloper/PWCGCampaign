package pwcg.aar.inmission.phase2.logeval.crewMemberstatus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.aar.inmission.phase2.logeval.AARDestroyedStatusEvaluator;
import pwcg.aar.inmission.phase2.logeval.crewmemberstatus.AARCrewMemberStatusDeadEvaluator;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogCrewMember;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.logfiles.LogParser;
import pwcg.core.logfiles.event.AType3;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AARCrewMemberStatusDeadEvaluatorTest
{
    @Mock private Campaign campaign;
    @Mock private LogParser aarLogParser;
    @Mock private Coordinate downAt;
    @Mock private LogCrewMember resultCrewmember;
    @Mock private AARDestroyedStatusEvaluator destroyedStatusEvaluator;
    @Mock private AType3 destroyedEventForPlane;
    @Mock private CrewMember crewMember;
    @Mock private Company squadron;
    @Mock private CampaignPersonnelManager personnelManager;
    
    
    @BeforeEach
    public void setupTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Mockito.when(resultCrewmember.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER+1);
        
        Mockito.when(campaign.getPersonnelManager()).thenReturn(personnelManager);
        Mockito.when(personnelManager.getAnyCampaignMember(Mockito.anyInt())).thenReturn(crewMember);
        Mockito.when(crewMember.determineSquadron()).thenReturn(squadron);
        
    }

    @Test
    public void testNoDeath () throws PWCGException
    {
        Coordinate downAt = new Coordinate();
        downAt.setXPos(100.0);
        downAt.setZPos(100.0);

        int oddsOfDeathDueToAiStupidity = 30;
        
        AARCrewMemberStatusDeadEvaluator aarCrewMemberStatusDeadEvaluator = new AARCrewMemberStatusDeadEvaluator(campaign, destroyedStatusEvaluator);
        aarCrewMemberStatusDeadEvaluator.initialize(
                        downAt, 
                        resultCrewmember,
                        null, 
                        oddsOfDeathDueToAiStupidity);
        
        boolean dead = aarCrewMemberStatusDeadEvaluator.isCrewMemberDead();
        assert(dead == false);
    }

    @Test
    public void testPlayerKilledByKnownVictor () throws PWCGException
    {
        Coordinate downAt = new Coordinate();
        downAt.setXPos(100.0);
        downAt.setZPos(100.0);
        
        Mockito.when(resultCrewmember.getSerialNumber()).thenReturn(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        Mockito.when(destroyedStatusEvaluator.didCrewMemberDie(resultCrewmember.getSerialNumber())).thenReturn(true);

        int oddsOfDeathDueToAiStupidity = 30;
        
        AARCrewMemberStatusDeadEvaluator aarCrewMemberStatusDeadEvaluator = new AARCrewMemberStatusDeadEvaluator(campaign, destroyedStatusEvaluator);
        aarCrewMemberStatusDeadEvaluator.initialize(
                        downAt, 
                        resultCrewmember,
                        destroyedEventForPlane, 
                        oddsOfDeathDueToAiStupidity);
        
        boolean dead = aarCrewMemberStatusDeadEvaluator.isCrewMemberDead();
        assert(dead == true);
    }

    @Test
    public void testPlayerKilledByAccident () throws PWCGException
    {
        Coordinate downAt = new Coordinate();
        downAt.setXPos(100.0);
        downAt.setZPos(100.0);
        
        Mockito.when(resultCrewmember.getSerialNumber()).thenReturn(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        Mockito.when(destroyedStatusEvaluator.didCrewMemberDie(resultCrewmember.getSerialNumber())).thenReturn(true);

        int oddsOfDeathDueToAiStupidity = 30;
        
        AARCrewMemberStatusDeadEvaluator aarCrewMemberStatusDeadEvaluator = new AARCrewMemberStatusDeadEvaluator(campaign, destroyedStatusEvaluator);
        aarCrewMemberStatusDeadEvaluator.initialize(
                        downAt, 
                        resultCrewmember,
                        destroyedEventForPlane, 
                        oddsOfDeathDueToAiStupidity);
        
        boolean dead = aarCrewMemberStatusDeadEvaluator.isCrewMemberDead();
        assert(dead == true);
    }

    @Test
    public void testAiKilledByKnownVictor () throws PWCGException
    {
        Coordinate downAt = new Coordinate();
        downAt.setXPos(100.0);
        downAt.setZPos(100.0);
        
        Mockito.when(destroyedEventForPlane.getVictor()).thenReturn("Joe Blow");
        Mockito.when(destroyedStatusEvaluator.didCrewMemberDie(resultCrewmember.getSerialNumber())).thenReturn(true);

        int oddsOfDeathDueToAiStupidity = 30;
        
        AARCrewMemberStatusDeadEvaluator aarCrewMemberStatusDeadEvaluator = new AARCrewMemberStatusDeadEvaluator(campaign, destroyedStatusEvaluator);
        aarCrewMemberStatusDeadEvaluator.initialize(
                        downAt, 
                        resultCrewmember,
                        destroyedEventForPlane, 
                        oddsOfDeathDueToAiStupidity);
        
        boolean dead = aarCrewMemberStatusDeadEvaluator.isCrewMemberDead();
        assert(dead == true);
    }

    @Test
    public void testAiKilledByAccident () throws PWCGException
    {
        Coordinate downAt = new Coordinate();
        downAt.setXPos(100.0);
        downAt.setZPos(100.0);
        
        Mockito.when(destroyedEventForPlane.getVictor()).thenReturn(LogParser.UNKNOWN_MISSION_LOG_ENTITY);
        Mockito.when(destroyedStatusEvaluator.didCrewMemberDie(resultCrewmember.getSerialNumber())).thenReturn(true);

        Coordinate fieldAt = new Coordinate();
        downAt.setXPos(5000.0);
        downAt.setZPos(5000.0);
        Mockito.when(squadron.determineCurrentPosition(Mockito.any())).thenReturn(fieldAt);

        int oddsOfDeathDueToAiStupidity = 100;
        
        AARCrewMemberStatusDeadEvaluator aarCrewMemberStatusDeadEvaluator = new AARCrewMemberStatusDeadEvaluator(campaign, destroyedStatusEvaluator);
        aarCrewMemberStatusDeadEvaluator.initialize(
                        downAt, 
                        resultCrewmember,
                        destroyedEventForPlane, 
                        oddsOfDeathDueToAiStupidity);
        
        boolean dead = aarCrewMemberStatusDeadEvaluator.isCrewMemberDead();
        assert(dead == true);
    }

    @Test
    public void testCrewMemberCrashedButNotKilled () throws PWCGException
    {
        Coordinate downAt = new Coordinate();
        downAt.setXPos(100.0);
        downAt.setZPos(100.0);
        
        downAt.setXPos(100.0);
        downAt.setZPos(100.0);

        int oddsOfDeathDueToAiStupidity = 30;
        
        AARCrewMemberStatusDeadEvaluator aarCrewMemberStatusDeadEvaluator = new AARCrewMemberStatusDeadEvaluator(campaign, destroyedStatusEvaluator);
        aarCrewMemberStatusDeadEvaluator.initialize(
                        downAt, 
                        resultCrewmember,
                        destroyedEventForPlane, 
                        oddsOfDeathDueToAiStupidity);
        
        boolean dead = aarCrewMemberStatusDeadEvaluator.isCrewMemberDead();
        assert(dead == false);
    }

    @Test
    public void testCrewMemberKilledByKnownVictor () throws PWCGException
    {
        Coordinate downAt = new Coordinate();
        downAt.setXPos(100.0);
        downAt.setZPos(100.0);
        
        Mockito.when(destroyedEventForPlane.getVictor()).thenReturn("Joe Blow");
        Mockito.when(destroyedStatusEvaluator.didCrewMemberDie(resultCrewmember.getSerialNumber())).thenReturn(true);

        int oddsOfDeathDueToAiStupidity = 30;
        
        AARCrewMemberStatusDeadEvaluator aarCrewMemberStatusDeadEvaluator = new AARCrewMemberStatusDeadEvaluator(campaign, destroyedStatusEvaluator);
        aarCrewMemberStatusDeadEvaluator.initialize(
                        downAt, 
                        resultCrewmember,
                        destroyedEventForPlane, 
                        oddsOfDeathDueToAiStupidity);
        
        boolean dead = aarCrewMemberStatusDeadEvaluator.isCrewMemberDead();
        assert(dead == true);
    }

    @Test
    public void testCrewMemberSurvivedAccidentNearField () throws PWCGException
    {
        Coordinate downAt = new Coordinate();
        downAt.setXPos(100.0);
        downAt.setZPos(100.0);
        
        Mockito.when(destroyedEventForPlane.getVictor()).thenReturn(LogParser.UNKNOWN_MISSION_LOG_ENTITY);
        Mockito.when(destroyedStatusEvaluator.didCrewMemberDie(resultCrewmember.getSerialNumber())).thenReturn(true);

        Coordinate fieldAt = new Coordinate();
        fieldAt.setXPos(100.0);
        fieldAt.setZPos(100.0);
        Mockito.when(squadron.determineCurrentPosition(Mockito.any())).thenReturn(fieldAt);

        int oddsOfDeathDueToAiStupidity = 30;
        
        AARCrewMemberStatusDeadEvaluator aarCrewMemberStatusDeadEvaluator = new AARCrewMemberStatusDeadEvaluator(campaign, destroyedStatusEvaluator);
        aarCrewMemberStatusDeadEvaluator.initialize(
                        downAt, 
                        resultCrewmember,
                        destroyedEventForPlane, 
                        oddsOfDeathDueToAiStupidity);
        
        boolean dead = aarCrewMemberStatusDeadEvaluator.isCrewMemberDead();
        assert(dead == false);
    }

    @Test
    public void testCrewMemberKilledByAccidentAwayFromField () throws PWCGException
    {
        Coordinate downAt = new Coordinate();
        downAt.setXPos(100.0);
        downAt.setZPos(100.0);
        
        Mockito.when(destroyedEventForPlane.getVictor()).thenReturn(LogParser.UNKNOWN_MISSION_LOG_ENTITY);
        Mockito.when(destroyedStatusEvaluator.didCrewMemberDie(resultCrewmember.getSerialNumber())).thenReturn(true);

        Coordinate fieldAt = new Coordinate();
        fieldAt.setXPos(10000.0);
        fieldAt.setZPos(10000.0);
        Mockito.when(squadron.determineCurrentPosition(Mockito.any())).thenReturn(fieldAt);

        int oddsOfDeathDueToAiStupidity = 50;
        
        AARCrewMemberStatusDeadEvaluator aarCrewMemberStatusDeadEvaluator = new AARCrewMemberStatusDeadEvaluator(campaign, destroyedStatusEvaluator);
        aarCrewMemberStatusDeadEvaluator.initialize(
                        downAt, 
                        resultCrewmember,
                        destroyedEventForPlane, 
                        oddsOfDeathDueToAiStupidity);
        
        boolean wasKilledAtLeastOnce = false;
        boolean survivedAtLeastOnce = false;
        for(int i = 0; i < 100; ++i)
        {
            boolean dead = aarCrewMemberStatusDeadEvaluator.isCrewMemberDead();
            if (dead)
            {
                wasKilledAtLeastOnce = true;
            }
            else
            {
                survivedAtLeastOnce = true;
            }
        }

        assert(wasKilledAtLeastOnce == true);
        assert(survivedAtLeastOnce == true);
    }

}
