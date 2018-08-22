package pwcg.aar.inmission.phase3.reconcile.victories;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;

@RunWith(MockitoJUnitRunner.class)
public class GroundDeclarationResolverTest
{

    @Mock
    private VictorySorter victorySorter;

    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
    }

    @Test
    public void testMultipleGroundVictories()
    {
        List<LogVictory> victories = new ArrayList<>();
        for (int i = 0; i < 3; ++i)
        {
            LogVictory victory = new LogVictory(10);
            victory.setLocation(new Coordinate());
            victories.add(victory);
        }

        Mockito.when(victorySorter.getFirmGroundVictories()).thenReturn(victories);

        GroundDeclarationResolver groundDeclarationResolver = new GroundDeclarationResolver(victorySorter);
        ConfirmedVictories groundVictories = groundDeclarationResolver.determineGroundResults();
        assert (groundVictories.getConfirmedVictories().size() == 3);
    }

    @Test
    public void testNoGroundVictories()
    {
        List<LogVictory> victories = new ArrayList<>();
        Mockito.when(victorySorter.getFirmGroundVictories()).thenReturn(victories);

        GroundDeclarationResolver groundDeclarationResolver = new GroundDeclarationResolver(victorySorter);
        ConfirmedVictories groundVictories = groundDeclarationResolver.determineGroundResults();
        assert (groundVictories.getConfirmedVictories().size() == 0);
    }

}
