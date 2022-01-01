package pwcg.aar.inmission.phase2.logeval.pilotstatus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.aar.inmission.phase2.logeval.AARDestroyedStatusEvaluator;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPilot;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.logfiles.LogParser;
import pwcg.core.logfiles.event.AType3;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AARPilotStatusDeadEvaluatorTest
{
    @Mock private Campaign campaign;
    @Mock private LogParser aarLogParser;
    @Mock private Coordinate downAt;
    @Mock private LogPilot resultCrewmember;
    @Mock private AARDestroyedStatusEvaluator destroyedStatusEvaluator;
    @Mock private AType3 destroyedEventForPlane;
    @Mock private SquadronMember pilot;
    @Mock private Squadron squadron;
    @Mock private CampaignPersonnelManager personnelManager;
    
    
    @BeforeEach
    public void setupTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Mockito.when(resultCrewmember.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER+1);
        
        Mockito.when(campaign.getPersonnelManager()).thenReturn(personnelManager);
        Mockito.when(personnelManager.getAnyCampaignMember(Mockito.anyInt())).thenReturn(pilot);
        Mockito.when(pilot.determineSquadron()).thenReturn(squadron);
        
    }

    @Test
    public void testNoDeath () throws PWCGException
    {
        Coordinate downAt = new Coordinate();
        downAt.setXPos(100.0);
        downAt.setZPos(100.0);

        int oddsOfDeathDueToAiStupidity = 30;
        
        AARPilotStatusDeadEvaluator aarPilotStatusDeadEvaluator = new AARPilotStatusDeadEvaluator(campaign, destroyedStatusEvaluator);
        aarPilotStatusDeadEvaluator.initialize(
                        downAt, 
                        resultCrewmember,
                        null, 
                        oddsOfDeathDueToAiStupidity);
        
        boolean dead = aarPilotStatusDeadEvaluator.isCrewMemberDead();
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
        
        AARPilotStatusDeadEvaluator aarPilotStatusDeadEvaluator = new AARPilotStatusDeadEvaluator(campaign, destroyedStatusEvaluator);
        aarPilotStatusDeadEvaluator.initialize(
                        downAt, 
                        resultCrewmember,
                        destroyedEventForPlane, 
                        oddsOfDeathDueToAiStupidity);
        
        boolean dead = aarPilotStatusDeadEvaluator.isCrewMemberDead();
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
        
        AARPilotStatusDeadEvaluator aarPilotStatusDeadEvaluator = new AARPilotStatusDeadEvaluator(campaign, destroyedStatusEvaluator);
        aarPilotStatusDeadEvaluator.initialize(
                        downAt, 
                        resultCrewmember,
                        destroyedEventForPlane, 
                        oddsOfDeathDueToAiStupidity);
        
        boolean dead = aarPilotStatusDeadEvaluator.isCrewMemberDead();
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
        
        AARPilotStatusDeadEvaluator aarPilotStatusDeadEvaluator = new AARPilotStatusDeadEvaluator(campaign, destroyedStatusEvaluator);
        aarPilotStatusDeadEvaluator.initialize(
                        downAt, 
                        resultCrewmember,
                        destroyedEventForPlane, 
                        oddsOfDeathDueToAiStupidity);
        
        boolean dead = aarPilotStatusDeadEvaluator.isCrewMemberDead();
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
        
        AARPilotStatusDeadEvaluator aarPilotStatusDeadEvaluator = new AARPilotStatusDeadEvaluator(campaign, destroyedStatusEvaluator);
        aarPilotStatusDeadEvaluator.initialize(
                        downAt, 
                        resultCrewmember,
                        destroyedEventForPlane, 
                        oddsOfDeathDueToAiStupidity);
        
        boolean dead = aarPilotStatusDeadEvaluator.isCrewMemberDead();
        assert(dead == true);
    }

    @Test
    public void testSquadronMemberCrashedButNotKilled () throws PWCGException
    {
        Coordinate downAt = new Coordinate();
        downAt.setXPos(100.0);
        downAt.setZPos(100.0);
        
        downAt.setXPos(100.0);
        downAt.setZPos(100.0);

        int oddsOfDeathDueToAiStupidity = 30;
        
        AARPilotStatusDeadEvaluator aarPilotStatusDeadEvaluator = new AARPilotStatusDeadEvaluator(campaign, destroyedStatusEvaluator);
        aarPilotStatusDeadEvaluator.initialize(
                        downAt, 
                        resultCrewmember,
                        destroyedEventForPlane, 
                        oddsOfDeathDueToAiStupidity);
        
        boolean dead = aarPilotStatusDeadEvaluator.isCrewMemberDead();
        assert(dead == false);
    }

    @Test
    public void testSquadronMemberKilledByKnownVictor () throws PWCGException
    {
        Coordinate downAt = new Coordinate();
        downAt.setXPos(100.0);
        downAt.setZPos(100.0);
        
        Mockito.when(destroyedEventForPlane.getVictor()).thenReturn("Joe Blow");
        Mockito.when(destroyedStatusEvaluator.didCrewMemberDie(resultCrewmember.getSerialNumber())).thenReturn(true);

        int oddsOfDeathDueToAiStupidity = 30;
        
        AARPilotStatusDeadEvaluator aarPilotStatusDeadEvaluator = new AARPilotStatusDeadEvaluator(campaign, destroyedStatusEvaluator);
        aarPilotStatusDeadEvaluator.initialize(
                        downAt, 
                        resultCrewmember,
                        destroyedEventForPlane, 
                        oddsOfDeathDueToAiStupidity);
        
        boolean dead = aarPilotStatusDeadEvaluator.isCrewMemberDead();
        assert(dead == true);
    }

    @Test
    public void testSquadronMemberSurvivedAccidentNearField () throws PWCGException
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
        
        AARPilotStatusDeadEvaluator aarPilotStatusDeadEvaluator = new AARPilotStatusDeadEvaluator(campaign, destroyedStatusEvaluator);
        aarPilotStatusDeadEvaluator.initialize(
                        downAt, 
                        resultCrewmember,
                        destroyedEventForPlane, 
                        oddsOfDeathDueToAiStupidity);
        
        boolean dead = aarPilotStatusDeadEvaluator.isCrewMemberDead();
        assert(dead == false);
    }

    @Test
    public void testSquadronMemberKilledByAccidentAwayFromField () throws PWCGException
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
        
        AARPilotStatusDeadEvaluator aarPilotStatusDeadEvaluator = new AARPilotStatusDeadEvaluator(campaign, destroyedStatusEvaluator);
        aarPilotStatusDeadEvaluator.initialize(
                        downAt, 
                        resultCrewmember,
                        destroyedEventForPlane, 
                        oddsOfDeathDueToAiStupidity);
        
        boolean wasKilledAtLeastOnce = false;
        boolean survivedAtLeastOnce = false;
        for(int i = 0; i < 100; ++i)
        {
            boolean dead = aarPilotStatusDeadEvaluator.isCrewMemberDead();
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
