package pwcg.aar.inmission.phase3.reconcile.victories;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.core.exception.PWCGException;

@RunWith(MockitoJUnitRunner.class)
public class PlayerClaimResolverFuzzyNotExactTest
{
    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(true);
    }

    @Test
    public void testPlayerFuzzyNotExactVictoryFoundWithExactMatch() throws PWCGException
    {
        LogPlane victim = new LogPlane();
        victim.setRole(Role.ROLE_FIGHTER);
        victim.setVehicleType("se5a");

        LogPlane victor = new LogPlane();
        victor.setVehicleType("albatrosd3");
        victor.setPilotSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);

        LogVictory resultVictory = new LogVictory();
        resultVictory.setVictim(victim);
        resultVictory.setVictor(victor);
        
        PlayerVictoryDeclaration playerDeclaration = new PlayerVictoryDeclaration();
        playerDeclaration.setAircraftType("se5a");

        PlayerClaimResolverFuzzy claimResolverFuzzy = new PlayerClaimResolverFuzzy();
        String planeDisplayName = claimResolverFuzzy.getShotDownPlaneDisplayNameAsFuzzyNotExact(playerDeclaration, resultVictory);
        
        assert (planeDisplayName.equals("S.E.5a"));
    }

    @Test
    public void testPlayerFuzzyNotExactVictoryFoundWithNotExactMatch() throws PWCGException
    {
        LogPlane victim = new LogPlane();
        victim.setRole(Role.ROLE_FIGHTER);
        victim.setVehicleType("se5a");

        LogPlane victor = new LogPlane();
        victor.setVehicleType("albatrosd3");
        victor.setPilotSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);

        LogVictory resultVictory = new LogVictory();
        resultVictory.setVictim(victim);
        resultVictory.setVictor(victor);
        
        PlayerVictoryDeclaration playerDeclaration = new PlayerVictoryDeclaration();
        playerDeclaration.setAircraftType("sopcamel");

        PlayerClaimResolverFuzzy claimResolverFuzzy = new PlayerClaimResolverFuzzy();
        String planeDisplayName = claimResolverFuzzy.getShotDownPlaneDisplayNameAsFuzzyNotExact(playerDeclaration, resultVictory);
        
        assert (planeDisplayName.equals("S.E.5a"));
    }

    @Test
    public void testPlayerFuzzyNotExactVictoryNotFoundBecauseRoleIsDifferent() throws PWCGException
    {
        LogPlane victim = new LogPlane();
        victim.setRole(Role.ROLE_BOMB);
        victim.setVehicleType("notarealplane");

        LogPlane victor = new LogPlane();
        victor.setVehicleType("albatrosd3");
        victor.setPilotSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);

        LogVictory resultVictory = new LogVictory();
        resultVictory.setVictim(victim);
        resultVictory.setVictor(victor);
        
        PlayerVictoryDeclaration playerDeclaration = new PlayerVictoryDeclaration();
        playerDeclaration.setAircraftType("se5a");

        PlayerClaimResolverFuzzy claimResolverFuzzy = new PlayerClaimResolverFuzzy();
        String planeDisplayName = claimResolverFuzzy.getShotDownPlaneDisplayNameAsFuzzyNotExact(playerDeclaration, resultVictory);
        
        assert (planeDisplayName.isEmpty());
    }

    @Test
    public void testPlayerFuzzyNotExactVictoryNotFoundBecauseClaimPlaneNotFound() throws PWCGException
    {
        LogPlane victim = new LogPlane();
        victim.setRole(Role.ROLE_FIGHTER);
        victim.setVehicleType("se5a");

        LogPlane victor = new LogPlane();
        victor.setVehicleType("albatrosd3");
        victor.setPilotSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);

        LogVictory resultVictory = new LogVictory();
        resultVictory.setVictim(victim);
        resultVictory.setVictor(victor);
        
        PlayerVictoryDeclaration playerDeclaration = new PlayerVictoryDeclaration();
        playerDeclaration.setAircraftType("notarealplane");

        PlayerClaimResolverFuzzy claimResolverFuzzy = new PlayerClaimResolverFuzzy();
        String planeDisplayName = claimResolverFuzzy.getShotDownPlaneDisplayNameAsFuzzyNotExact(playerDeclaration, resultVictory);
        
        assert (planeDisplayName.isEmpty());
    }

    @Test
    public void testPlayerFuzzyNotExactVictoryNotFoundBecauseVictoryAlreadyConfirmed() throws PWCGException
    {
        LogPlane victim = new LogPlane();
        victim.setRole(Role.ROLE_FIGHTER);
        victim.setVehicleType("se5a");

        LogPlane victor = new LogPlane();
        victor.setVehicleType("albatrosd3");
        victor.setPilotSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);

        LogVictory resultVictory = new LogVictory();
        resultVictory.setVictim(victim);
        resultVictory.setVictor(victor);
        resultVictory.setConfirmed(true);
        
        PlayerVictoryDeclaration playerDeclaration = new PlayerVictoryDeclaration();
        playerDeclaration.setAircraftType("se5a");

        PlayerClaimResolverFuzzy claimResolverFuzzy = new PlayerClaimResolverFuzzy();
        String planeDisplayName = claimResolverFuzzy.getShotDownPlaneDisplayNameAsFuzzyNotExact(playerDeclaration, resultVictory);
        
        assert (planeDisplayName.isEmpty());
    }
}
