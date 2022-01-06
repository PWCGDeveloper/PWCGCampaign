package pwcg.aar.inmission.phase3.reconcile.victories.singleplayer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;

@ExtendWith(MockitoExtension.class)
public class PlayerClaimPlaneNameFinderTest
{
    public PlayerClaimPlaneNameFinderTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
    }

    @Test
    public void testPlaneNameFoundByVictoryResult() throws PWCGException
    {
        LogPlane victim = new LogPlane(1);
        victim.setVehicleType("se5a");

        LogVictory victory = new LogVictory(2);
        victory.setVictim(victim);
        
        PlayerVictoryDeclaration playerDeclaration = new PlayerVictoryDeclaration();

        PlayerClaimPlaneNameFinder planeNameFinder = new PlayerClaimPlaneNameFinder();
        String planeDisplayName = planeNameFinder.getShotDownPlaneDisplayName(playerDeclaration, victory);
        
        Assertions.assertTrue (planeDisplayName.equals("S.E.5a"));
    }

    @Test
    public void testPlaneNameFoundByDeclaration() throws PWCGException
    {
        LogVictory victory = new LogVictory(1);
         
        PlayerVictoryDeclaration playerDeclaration = new PlayerVictoryDeclaration();
        playerDeclaration.setAircraftType("se5a");

        PlayerClaimPlaneNameFinder planeNameFinder = new PlayerClaimPlaneNameFinder();
        String planeDisplayName = planeNameFinder.getShotDownPlaneDisplayName(playerDeclaration, victory);
        
        Assertions.assertTrue (planeDisplayName.equals("S.E.5a"));
    }

    @Test
    public void testPlaneNameNotFound() throws PWCGException
    {
        LogVictory victory = new LogVictory(1);
         
        PlayerVictoryDeclaration playerDeclaration = new PlayerVictoryDeclaration();

        PlayerClaimPlaneNameFinder planeNameFinder = new PlayerClaimPlaneNameFinder();
        String planeDisplayName = planeNameFinder.getShotDownPlaneDisplayName(playerDeclaration, victory);
        
        Assertions.assertTrue (planeDisplayName.isEmpty());
    }

}
