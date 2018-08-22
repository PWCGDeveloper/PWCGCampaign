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
public class PlayerClaimResolverFirmNotExactTest
{
    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(true);
    }

    @Test
    public void testPlayerFirmNotExactVictoryFoundWithExactMatch() throws PWCGException
    {
        LogPlane victim = new LogPlane(1);
        victim.setRole(Role.ROLE_FIGHTER);
        victim.setVehicleType("se5a");

        LogPlane victor = new LogPlane(2);
        victor.setVehicleType("albatrosd3");
        victor.setPilotSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);

        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictim(victim);
        resultVictory.setVictor(victor);
        
        PlayerVictoryDeclaration playerDeclaration = new PlayerVictoryDeclaration();
        playerDeclaration.setAircraftType("se5a");

        PlayerClaimResolverFirm claimResolverFirm = new PlayerClaimResolverFirm();
        String planeDisplayName = claimResolverFirm.getShotDownPlaneDisplayNameAsFirmNotExact(playerDeclaration, resultVictory);
        
        assert (planeDisplayName.equals("S.E.5a"));
    }

    @Test
    public void testPlayerFirmNotExactVictoryFoundWithNotExactMatch() throws PWCGException
    {
        LogPlane victim = new LogPlane(1);
        victim.setRole(Role.ROLE_FIGHTER);
        victim.setVehicleType("se5a");

        LogPlane victor = new LogPlane(2);
        victor.setVehicleType("albatrosd3");
        victor.setPilotSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);

        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictim(victim);
        resultVictory.setVictor(victor);
        
        PlayerVictoryDeclaration playerDeclaration = new PlayerVictoryDeclaration();
        playerDeclaration.setAircraftType("sopcamel");

        PlayerClaimResolverFirm claimResolverFirm = new PlayerClaimResolverFirm();
        String planeDisplayName = claimResolverFirm.getShotDownPlaneDisplayNameAsFirmNotExact(playerDeclaration, resultVictory);
        
        assert (planeDisplayName.equals("S.E.5a"));
    }

    @Test
    public void testPlayerFirmNotExactVictoryNotFoundBecauseRoleIsDifferent() throws PWCGException
    {
        LogPlane victim = new LogPlane(1);
        victim.setRole(Role.ROLE_BOMB);
        victim.setVehicleType("notarealplane");

        LogPlane victor = new LogPlane(2);
        victor.setVehicleType("albatrosd3");
        victor.setPilotSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);

        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictim(victim);
        resultVictory.setVictor(victor);
        
        PlayerVictoryDeclaration playerDeclaration = new PlayerVictoryDeclaration();
        playerDeclaration.setAircraftType("se5a");

        PlayerClaimResolverFirm claimResolverFirm = new PlayerClaimResolverFirm();
        String planeDisplayName = claimResolverFirm.getShotDownPlaneDisplayNameAsFirmNotExact(playerDeclaration, resultVictory);
        
        assert (planeDisplayName.isEmpty());
    }

    @Test
    public void testPlayerFirmNotExactVictoryNotFoundBecauseClaimPlaneNotFound() throws PWCGException
    {
        LogPlane victim = new LogPlane(1);
        victim.setRole(Role.ROLE_FIGHTER);
        victim.setVehicleType("se5a");

        LogPlane victor = new LogPlane(2);
        victor.setVehicleType("albatrosd3");
        victor.setPilotSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);

        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictim(victim);
        resultVictory.setVictor(victor);
        
        PlayerVictoryDeclaration playerDeclaration = new PlayerVictoryDeclaration();
        playerDeclaration.setAircraftType("notarealplane");

        PlayerClaimResolverFirm claimResolverFirm = new PlayerClaimResolverFirm();
        String planeDisplayName = claimResolverFirm.getShotDownPlaneDisplayNameAsFirmNotExact(playerDeclaration, resultVictory);
        
        assert (planeDisplayName.isEmpty());
    }

    @Test
    public void testPlayerFirmNotExactVictoryNotFoundBecauseVictoryAlreadyConfirmed() throws PWCGException
    {
        LogPlane victim = new LogPlane(1);
        victim.setRole(Role.ROLE_FIGHTER);
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

        PlayerClaimResolverFirm claimResolverFirm = new PlayerClaimResolverFirm();
        String planeDisplayName = claimResolverFirm.getShotDownPlaneDisplayNameAsFirmNotExact(playerDeclaration, resultVictory);
        
        assert (planeDisplayName.isEmpty());
    }
}
