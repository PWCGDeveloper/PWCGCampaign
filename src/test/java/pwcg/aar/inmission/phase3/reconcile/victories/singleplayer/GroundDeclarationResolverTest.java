package pwcg.aar.inmission.phase3.reconcile.victories.singleplayer;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.aar.inmission.phase3.reconcile.victories.common.ConfirmedVictories;
import pwcg.aar.inmission.phase3.reconcile.victories.common.GroundDeclarationResolver;
import pwcg.aar.inmission.phase3.reconcile.victories.common.VictorySorter;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;

@ExtendWith(MockitoExtension.class)
public class GroundDeclarationResolverTest
{

    @Mock
    private VictorySorter victorySorter;

    public GroundDeclarationResolverTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
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
        Assertions.assertTrue (groundVictories.getConfirmedVictories().size() == 3);
    }

    @Test
    public void testNoGroundVictories()
    {
        List<LogVictory> victories = new ArrayList<>();
        Mockito.when(victorySorter.getFirmGroundVictories()).thenReturn(victories);

        GroundDeclarationResolver groundDeclarationResolver = new GroundDeclarationResolver(victorySorter);
        ConfirmedVictories groundVictories = groundDeclarationResolver.determineGroundResults();
        Assertions.assertTrue (groundVictories.getConfirmedVictories().size() == 0);
    }

}
