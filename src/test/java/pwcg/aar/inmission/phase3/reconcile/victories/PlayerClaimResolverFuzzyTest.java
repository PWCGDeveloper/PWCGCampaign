package pwcg.aar.inmission.phase3.reconcile.victories;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.aar.inmission.phase3.reconcile.victories.singleplayer.PlayerClaimResolverFuzzy;
import pwcg.aar.inmission.phase3.reconcile.victories.singleplayer.PlayerVictoryDeclaration;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.core.exception.PWCGException;

@RunWith(MockitoJUnitRunner.class)
public class PlayerClaimResolverFuzzyTest
{
    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(true);
    }

    @Test
    public void testPlayerFuzzyVictoryFound() throws PWCGException
    {
        LogPlane victim = new LogPlane(1);
        victim.setVehicleType("se5a");

        LogPlane victor = new LogPlane(2);
        victor.setVehicleType("albatrosd3");
        victor.setPilotSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);

        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictim(victim);
        resultVictory.setVictor(victor);
        
        PlayerVictoryDeclaration playerDeclaration = new PlayerVictoryDeclaration();
        playerDeclaration.setAircraftType("se5a");

        PlayerClaimResolverFuzzy claimResolverFuzzy = new PlayerClaimResolverFuzzy();
        String planeDisplayName = claimResolverFuzzy.getShotDownPlaneDisplayNameAsFuzzy(playerDeclaration, resultVictory);
        
        assert (planeDisplayName.equals("S.E.5a"));
    }
    

    @Test
    public void testPlayerFuzzyVictoryNotFoundBecausePlaneMismatch() throws PWCGException
    {
        LogPlane victim = new LogPlane(1);
        victim.setVehicleType("se5a");

        LogPlane victor = new LogPlane(2);
        victor.setVehicleType("albatrosd3");
        victor.setPilotSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);

        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictim(victim);
        resultVictory.setVictor(victor);
        
        PlayerVictoryDeclaration playerDeclaration = new PlayerVictoryDeclaration();
        playerDeclaration.setAircraftType("sopcamel");

        PlayerClaimResolverFuzzy claimResolverFuzzy = new PlayerClaimResolverFuzzy();
        String planeDisplayName = claimResolverFuzzy.getShotDownPlaneDisplayNameAsFuzzy(playerDeclaration, resultVictory);
        
        assert (planeDisplayName.isEmpty());
    }

    @Test
    public void testPlayerFuzzyVictoryNotFoundBecauseVictoryPlaneNotFound() throws PWCGException
    {
        LogPlane victim = new LogPlane(1);
        victim.setVehicleType("notarealplane");

        LogPlane victor = new LogPlane(2);
        victor.setVehicleType("albatrosd3");
        victor.setPilotSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);

        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictim(victim);
        resultVictory.setVictor(victor);
        
        PlayerVictoryDeclaration playerDeclaration = new PlayerVictoryDeclaration();
        playerDeclaration.setAircraftType("se5a");

        PlayerClaimResolverFuzzy claimResolverFuzzy = new PlayerClaimResolverFuzzy();
        String planeDisplayName = claimResolverFuzzy.getShotDownPlaneDisplayNameAsFuzzy(playerDeclaration, resultVictory);
        
        assert (planeDisplayName.isEmpty());
    }

    @Test
    public void testPlayerFuzzyVictoryNotFoundBecauseClaimPlaneNotFound() throws PWCGException
    {
        LogPlane victim = new LogPlane(1);
        victim.setVehicleType("se5a");

        LogPlane victor = new LogPlane(2);
        victor.setVehicleType("albatrosd3");
        victor.setPilotSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);

        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictim(victim);
        resultVictory.setVictor(victor);
        
        PlayerVictoryDeclaration playerDeclaration = new PlayerVictoryDeclaration();
        playerDeclaration.setAircraftType("notarealplane");

        PlayerClaimResolverFuzzy claimResolverFuzzy = new PlayerClaimResolverFuzzy();
        String planeDisplayName = claimResolverFuzzy.getShotDownPlaneDisplayNameAsFuzzy(playerDeclaration, resultVictory);
        
        assert (planeDisplayName.isEmpty());
    }

    @Test
    public void testPlayerFuzzyVictoryNotFoundBecauseVictoryAlreadyConfirmed() throws PWCGException
    {
        LogPlane victim = new LogPlane(1);
        victim.setVehicleType("se5a");

        LogPlane victor = new LogPlane(2);
        victor.setVehicleType("albatrosd3");
        victor.setPilotSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);

        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictim(victim);
        resultVictory.setVictor(victor);
        resultVictory.setConfirmed(true);
        
        PlayerVictoryDeclaration playerDeclaration = new PlayerVictoryDeclaration();
        playerDeclaration.setAircraftType("se5a");

        PlayerClaimResolverFuzzy claimResolverFuzzy = new PlayerClaimResolverFuzzy();
        String planeDisplayName = claimResolverFuzzy.getShotDownPlaneDisplayNameAsFuzzy(playerDeclaration, resultVictory);
        
        assert (planeDisplayName.isEmpty());
    }

}
