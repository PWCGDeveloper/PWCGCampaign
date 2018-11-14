package pwcg.aar.inmission.phase2.logeval.victory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.inmission.phase2.logeval.AARVehicleBuilder;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogUnknown;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.aar.inmission.phase2.logeval.victory.AARFuzzyByPlayerDamaged;
import pwcg.aar.inmission.phase2.logeval.victory.AARFuzzyVictoryEvaluator;
import pwcg.core.exception.PWCGException;

@RunWith(MockitoJUnitRunner.class)
public class AARFuzzyVictoryEvaluatorTest
{
    @Mock
    private AARVehicleBuilder vehicleBuilder;

    @Mock
    private AARFuzzyByPlayerDamaged fuzzyByPlayerDamaged;
    
    private LogVictory victoryResult;
    
    @Mock
    private LogPlane victor;
    
    private LogPlane victim;
    
    private LogUnknown unknownVictorEntity = new LogUnknown();
    
    @Before
    public void setup()
    {
        victoryResult = new LogVictory(10);
        
        victim = new LogPlane(1);
    }
    
    @Test
    public void testUseFuzzyVictoryTrue () throws PWCGException
    {
        victoryResult.setVictim(new LogPlane(2));
        victim.setId("11111");
        Mockito.when(vehicleBuilder.getVehicle(Matchers.<String>any())).thenReturn(victim);
        Mockito.when(fuzzyByPlayerDamaged.getVictorBasedOnDamage(victoryResult)).thenReturn(unknownVictorEntity);
        unknownVictorEntity.setId("11111");
        
        AARFuzzyVictoryEvaluator fuzzyVictoryEvaluator= new AARFuzzyVictoryEvaluator(
                        vehicleBuilder, 
                        fuzzyByPlayerDamaged);
        
        fuzzyVictoryEvaluator.applyFuzzyVictoryMethods(victoryResult);
        
        assert(victoryResult.getVictor().getId() == "11111");
    }
    
    @Test
    public void testUseFuzzyVictoryFalseBecauseNullVictim () throws PWCGException
    {
        victoryResult.setVictim(null);
        victim.setId("11111");
        unknownVictorEntity.setId("11111");

        Mockito.when(vehicleBuilder.getVehicle(Matchers.<String>any())).thenReturn(victim);
        Mockito.when(fuzzyByPlayerDamaged.getVictorBasedOnDamage(victoryResult)).thenReturn(unknownVictorEntity);
        
        AARFuzzyVictoryEvaluator fuzzyVictoryEvaluator= new AARFuzzyVictoryEvaluator(
                        vehicleBuilder, 
                        fuzzyByPlayerDamaged);
        
        fuzzyVictoryEvaluator.applyFuzzyVictoryMethods(victoryResult);
        
        assert(victoryResult.getVictor() instanceof LogUnknown);
    }
    
    @Test
    public void testUseFuzzyVictoryFalseBecauseUnknownVictor () throws PWCGException
    {
        victoryResult.setVictim(null);
        victim.setId("11111");
        unknownVictorEntity.setId("11111");

        Mockito.when(vehicleBuilder.getVehicle(Matchers.<String>any())).thenReturn(victim);
        Mockito.when(fuzzyByPlayerDamaged.getVictorBasedOnDamage(victoryResult)).thenReturn(unknownVictorEntity);
        
        AARFuzzyVictoryEvaluator fuzzyVictoryEvaluator= new AARFuzzyVictoryEvaluator(
                        vehicleBuilder, 
                        fuzzyByPlayerDamaged);
        
        fuzzyVictoryEvaluator.applyFuzzyVictoryMethods(victoryResult);
        
        assert(victoryResult.getVictor() instanceof LogUnknown);
    }
    
    @Test
    public void testUseFuzzyVictoryFalseBecauseNotNullVictor () throws PWCGException
    {
        victoryResult.setVictor(new LogPlane(3));
        victoryResult.setVictim(new LogPlane(4));
        victim.setId("11111");
        unknownVictorEntity.setId("11111");

        Mockito.when(vehicleBuilder.getVehicle(Matchers.<String>any())).thenReturn(victim);
        Mockito.when(fuzzyByPlayerDamaged.getVictorBasedOnDamage(victoryResult)).thenReturn(unknownVictorEntity);
        
        AARFuzzyVictoryEvaluator fuzzyVictoryEvaluator= new AARFuzzyVictoryEvaluator(
                        vehicleBuilder, 
                        fuzzyByPlayerDamaged);
        
        fuzzyVictoryEvaluator.applyFuzzyVictoryMethods(victoryResult);
        
        assert(victoryResult.getVictor().getId() == "");
    }
    
    @Test
    public void testUseFuzzyVictoryFalseBecauseVehicleBuilderCouldNotIdentifyVictim () throws PWCGException
    {
        victoryResult.setVictim(new LogPlane(5));
        victim.setId("11111");
        Mockito.when(vehicleBuilder.getVehicle(Matchers.<String>any())).thenReturn(null);
        Mockito.when(fuzzyByPlayerDamaged.getVictorBasedOnDamage(victoryResult)).thenReturn(unknownVictorEntity);
        unknownVictorEntity.setId("11111");
        
        AARFuzzyVictoryEvaluator fuzzyVictoryEvaluator= new AARFuzzyVictoryEvaluator(
                        vehicleBuilder, 
                        fuzzyByPlayerDamaged);
        
        fuzzyVictoryEvaluator.applyFuzzyVictoryMethods(victoryResult);
        
        assert(victoryResult.getVictor() instanceof LogUnknown);
    }
    
}
