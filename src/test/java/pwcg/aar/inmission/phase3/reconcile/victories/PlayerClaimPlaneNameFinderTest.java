package pwcg.aar.inmission.phase3.reconcile.victories;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;

@RunWith(MockitoJUnitRunner.class)
public class PlayerClaimPlaneNameFinderTest
{

    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(true);
    }

    @Test
    public void testPlaneNameFoundByVictoryResult() throws PWCGException
    {
        LogPlane victim = new LogPlane();
        victim.setVehicleType("se5a");

        LogVictory victory = new LogVictory();
        victory.setVictim(victim);
        
        PlayerVictoryDeclaration playerDeclaration = new PlayerVictoryDeclaration();

        PlayerClaimPlaneNameFinder planeNameFinder = new PlayerClaimPlaneNameFinder();
        String planeDisplayName = planeNameFinder.getShotDownPlaneDisplayName(playerDeclaration, victory);
        
        assert (planeDisplayName.equals("S.E.5a"));
    }

    @Test
    public void testPlaneNameFoundByDeclaration() throws PWCGException
    {
        LogVictory victory = new LogVictory();
         
        PlayerVictoryDeclaration playerDeclaration = new PlayerVictoryDeclaration();
        playerDeclaration.setAircraftType("se5a");

        PlayerClaimPlaneNameFinder planeNameFinder = new PlayerClaimPlaneNameFinder();
        String planeDisplayName = planeNameFinder.getShotDownPlaneDisplayName(playerDeclaration, victory);
        
        assert (planeDisplayName.equals("S.E.5a"));
    }

    @Test
    public void testPlaneNameNotFound() throws PWCGException
    {
        LogVictory victory = new LogVictory();
         
        PlayerVictoryDeclaration playerDeclaration = new PlayerVictoryDeclaration();

        PlayerClaimPlaneNameFinder planeNameFinder = new PlayerClaimPlaneNameFinder();
        String planeDisplayName = planeNameFinder.getShotDownPlaneDisplayName(playerDeclaration, victory);
        
        assert (planeDisplayName.isEmpty());
    }

}
