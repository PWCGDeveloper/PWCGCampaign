package pwcg.aar.inmission.phase2.logeval.victory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.aar.inmission.phase2.logeval.AARDamageStatusEvaluator;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogAIEntity;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.core.exception.PWCGException;

@RunWith(MockitoJUnitRunner.class)
public class AARFuzzyByPlayerDamagedTest
{
    @Mock private AARDamageStatusEvaluator aarDamageStatusEvaluator;
    @Mock private LogVictory victoryResult; 
    
    private LogPlane playerPlane = new LogPlane(1);
    private LogPlane shotDownPlane1 = new LogPlane(2);

    @Before
    public void setup() throws PWCGException
    {
        playerPlane.setId("11111");
        shotDownPlane1.setId("77777");
    }

    @Test
    public void testSetVehiclesCreditedByDamage () throws PWCGException
    {
        Mockito.when(victoryResult.getVictim()).thenReturn(shotDownPlane1);
        Mockito.when(aarDamageStatusEvaluator.getVictorByDamage(Mockito.any(LogAIEntity.class))).thenReturn(playerPlane);
        AARFuzzyByAccumulatedDamaged fuzzyByPlayerDamaged = new AARFuzzyByAccumulatedDamaged(aarDamageStatusEvaluator);
        LogAIEntity victor = fuzzyByPlayerDamaged.getVictorBasedOnDamage(victoryResult);
        assert(victor != null);
    }

    @Test
    public void testVictimNotFoundInDamageList () throws PWCGException
    {
        Mockito.when(victoryResult.getVictim()).thenReturn(shotDownPlane1);
        Mockito.when(aarDamageStatusEvaluator.getVictorByDamage(Mockito.any(LogAIEntity.class))).thenReturn(null);
        AARFuzzyByAccumulatedDamaged fuzzyByPlayerDamaged = new AARFuzzyByAccumulatedDamaged(aarDamageStatusEvaluator);
        LogAIEntity victor = fuzzyByPlayerDamaged.getVictorBasedOnDamage(victoryResult);
        assert(victor == null);
    }
    
    @Test
    public void testVictimNotFoundInVictory () throws PWCGException
    {
        Mockito.when(victoryResult.getVictim()).thenReturn(null);
        AARFuzzyByAccumulatedDamaged fuzzyByPlayerDamaged = new AARFuzzyByAccumulatedDamaged(aarDamageStatusEvaluator);
        LogAIEntity victor = fuzzyByPlayerDamaged.getVictorBasedOnDamage(victoryResult);
        assert(victor == null);
    }
}
