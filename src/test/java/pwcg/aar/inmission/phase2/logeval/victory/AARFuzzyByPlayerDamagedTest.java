package pwcg.aar.inmission.phase2.logeval.victory;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.inmission.phase2.logeval.AARDamageStatusEvaluator;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogAIEntity;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogDamage;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.aar.inmission.phase2.logeval.victory.AARFuzzyByPlayerDamaged;
import pwcg.core.exception.PWCGException;

@RunWith(MockitoJUnitRunner.class)
public class AARFuzzyByPlayerDamagedTest
{
    @Mock
    private AARDamageStatusEvaluator aarDamageStatusEvaluator = null;

    private LogPlane playerPlane = new LogPlane(1);

    private LogPlane shotDownPlane1 = new LogPlane(2);
    
    private LogPlane shotDownPlane2 = new LogPlane(3);
    
    private LogPlane shotDownPlane3 = new LogPlane(4);

    private LogPlane shotDownPlane4 = new LogPlane(5);

    private LogPlane otherPlane = new LogPlane(6);

    
    @Before
    public void setup()
    {
        playerPlane.setId("11111");
        shotDownPlane1.setId("77777");
        shotDownPlane2.setId("88888");
        shotDownPlane3.setId("99999");
        shotDownPlane4.setId("66666");
        otherPlane.setId("22222");
        
        List<LogDamage> damageEventsPlane1 = new ArrayList<>();
        LogDamage logDamage1 = new LogDamage(100);
        logDamage1.setVictor(playerPlane);
        logDamage1.setVictim(shotDownPlane1);
        damageEventsPlane1.add(logDamage1);

        List<LogDamage> damageEventsPlane2 = new ArrayList<>();
        LogDamage logDamage2 = new LogDamage(101);
        logDamage2.setVictor(playerPlane);
        logDamage2.setVictim(shotDownPlane2);
        damageEventsPlane2.add(logDamage2);

        List<LogDamage> damageEventsPlane4 = new ArrayList<>();
        LogDamage logDamage3 = new LogDamage(102);
        logDamage3.setVictor(playerPlane);
        logDamage3.setVictim(shotDownPlane4);
        logDamage3.addDamage(0.1);
        damageEventsPlane4.add(logDamage3);

        LogDamage logDamage4 = new LogDamage(103);
        logDamage4.setVictor(otherPlane);
        logDamage4.setVictim(shotDownPlane4);
        logDamage4.addDamage(0.05);
        damageEventsPlane4.add(logDamage4);

        Mockito.when(aarDamageStatusEvaluator.getDamageEventsForVehicle(shotDownPlane1.getId())).thenReturn(damageEventsPlane1);
        Mockito.when(aarDamageStatusEvaluator.getDamageEventsForVehicle(shotDownPlane2.getId())).thenReturn(damageEventsPlane2);
        Mockito.when(aarDamageStatusEvaluator.getDamageEventsForVehicle(shotDownPlane4.getId())).thenReturn(damageEventsPlane4);
    }
    
    @Test
    public void testSetVehiclesCreditedByDamage () throws PWCGException
    {
        
        LogVictory victoryResult = makeShotDownPlane(shotDownPlane1.getId());        
        AARFuzzyByPlayerDamaged fuzzyByPlayerDamaged = new AARFuzzyByPlayerDamaged(aarDamageStatusEvaluator);
        LogAIEntity victor = fuzzyByPlayerDamaged.getVictorBasedOnDamage(victoryResult);
        
        assert(victor != null);
    }
    
    @Test
    public void testSetVehiclesNotCreditedByDamage () throws PWCGException
    {
        LogVictory victoryResult = makeShotDownPlane(shotDownPlane3.getId());        
        
        AARFuzzyByPlayerDamaged fuzzyByPlayerDamaged = new AARFuzzyByPlayerDamaged(aarDamageStatusEvaluator);
        LogAIEntity victor = fuzzyByPlayerDamaged.getVictorBasedOnDamage(victoryResult);
        
        assert(victor == null);

    }
    
    @Test
    public void testSetVehiclesCreditedByDamageTwoPlanes () throws PWCGException
    {
        LogVictory victoryResult = makeShotDownPlane(shotDownPlane1.getId());        
        AARFuzzyByPlayerDamaged fuzzyByPlayerDamaged = new AARFuzzyByPlayerDamaged(aarDamageStatusEvaluator);
        LogAIEntity victor = fuzzyByPlayerDamaged.getVictorBasedOnDamage(victoryResult);
        assert(victor != null);

        
        victoryResult = makeShotDownPlane(shotDownPlane2.getId());        
        fuzzyByPlayerDamaged = new AARFuzzyByPlayerDamaged(aarDamageStatusEvaluator);
        victor = fuzzyByPlayerDamaged.getVictorBasedOnDamage(victoryResult);        
        assert(victor != null);
    }
    
    @Test
    public void testSetVehiclesCreditedOneTrueOneFalse () throws PWCGException
    {
        LogVictory victoryResult = makeShotDownPlane(shotDownPlane1.getId());        
        AARFuzzyByPlayerDamaged fuzzyByPlayerDamaged = new AARFuzzyByPlayerDamaged(aarDamageStatusEvaluator);
        LogAIEntity victor = fuzzyByPlayerDamaged.getVictorBasedOnDamage(victoryResult);
        assert(victor != null);
        
        victoryResult = makeShotDownPlane(shotDownPlane3.getId());        
        fuzzyByPlayerDamaged = new AARFuzzyByPlayerDamaged(aarDamageStatusEvaluator);
        victor = fuzzyByPlayerDamaged.getVictorBasedOnDamage(victoryResult);
        assert(victor == null);
    }

    @Test
    public void testSetVehiclesCreditedByDamageTwoAttackers () throws PWCGException
    {
        LogVictory victoryResult = makeShotDownPlane(shotDownPlane4.getId());
        AARFuzzyByPlayerDamaged fuzzyByPlayerDamaged = new AARFuzzyByPlayerDamaged(aarDamageStatusEvaluator);
        LogAIEntity victor = fuzzyByPlayerDamaged.getVictorBasedOnDamage(victoryResult);

        assert(victor == playerPlane);
    }

    private LogVictory makeShotDownPlane(String id)
    {
        LogPlane shotDownPlane1 = new LogPlane(5);
        shotDownPlane1.setId(id);
        LogVictory victoryResult = new LogVictory(10);
        victoryResult.setVictim(shotDownPlane1);
        return victoryResult;
    }
}
