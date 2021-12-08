package pwcg.aar.inmission.phase2.logeval.victory;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogUnknown;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.aar.inmission.phase3.reconcile.victories.singleplayer.UnknownVictoryAssignments;
import pwcg.core.exception.PWCGException;
import pwcg.core.logfiles.AARLogEventData;
import pwcg.core.logfiles.event.IATypeBase;

@ExtendWith(MockitoExtension.class)
public class AARRandomAssignmentTest
{
    @Mock
    private AARLogEventData logEventData;
    
    @Mock
    private AARRandomAssignmentCalculator randomAssignmentCalculator;
    
    private LogVictory victoryResult;
    
    @BeforeEach
    public void setupTest()
    {
        victoryResult = new LogVictory(10);
    }

    @Test
    public void testMarkTrue () throws PWCGException
    {
        Mockito.when(randomAssignmentCalculator.shouldBeMarkedForRandomAssignment(ArgumentMatchers.<List<IATypeBase>>any(), ArgumentMatchers.<String>any())).thenReturn(true);

        AARRandomAssignment randomAssignment = new AARRandomAssignment(logEventData, randomAssignmentCalculator);
        LogUnknown markedForAssignment = randomAssignment.markForRandomAssignment(victoryResult);
        
        assert(markedForAssignment.getUnknownVictoryAssignment() == UnknownVictoryAssignments.RANDOM_ASSIGNMENT);
    }

    @Test
    public void testMarkFalse () throws PWCGException
    {
        Mockito.when(randomAssignmentCalculator.shouldBeMarkedForRandomAssignment(ArgumentMatchers.<List<IATypeBase>>any(), ArgumentMatchers.<String>any())).thenReturn(false);

        AARRandomAssignment randomAssignment = new AARRandomAssignment(logEventData, randomAssignmentCalculator);
        LogUnknown markedForAssignment = randomAssignment.markForRandomAssignment(victoryResult);
        
        assert(markedForAssignment == null);
    }

}
