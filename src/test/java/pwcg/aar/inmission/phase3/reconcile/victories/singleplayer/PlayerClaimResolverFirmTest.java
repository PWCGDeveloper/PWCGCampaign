package pwcg.aar.inmission.phase3.reconcile.victories.singleplayer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.plane.PwcgRole;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.product.fc.country.FCCountry;

@RunWith(MockitoJUnitRunner.class)
public class PlayerClaimResolverFirmTest
{    
    @Mock private SquadronMember player;

    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        Mockito.when(player.isPlayer()).thenReturn(true);
        Mockito.when(player.getSerialNumber()).thenReturn(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        Mockito.when(player.getCountry()).thenReturn(Country.GERMANY);
    }

    @Test
    public void testPlayerFirmVictoryFound() throws PWCGException
    {
        LogPlane victim = new LogPlane(1);
        victim.setVehicleType("se5a");
        victim.setCountry(new FCCountry(Country.BRITAIN));

        LogPlane victor = new LogPlane(2);
        victor.setVehicleType("albatrosd5");
        victor.setPilotSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        victor.setCountry(new FCCountry(Country.GERMANY));

        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictim(victim);
        resultVictory.setVictor(victor);
        
        PlayerVictoryDeclaration playerDeclaration = new PlayerVictoryDeclaration();
        playerDeclaration.setAircraftType("se5a");

        PlayerClaimResolverFirm claimResolverFirm = new PlayerClaimResolverFirm();
        String planeDisplayName = claimResolverFirm.getShotDownPlaneDisplayNameAsFirm(player, playerDeclaration, resultVictory);
        
        assert (planeDisplayName.equals("S.E.5a"));
    }

    @Test
    public void testPlayerFirmVictoryNotFoundBecausePlaneMismatch() throws PWCGException
    {
        LogPlane victim = new LogPlane(1);
        victim.setVehicleType("se5a");
        victim.setCountry(new FCCountry(Country.BRITAIN));

        LogPlane victor = new LogPlane(2);
        victor.setVehicleType("albatrosd5");
        victor.setPilotSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        victor.setCountry(new FCCountry(Country.GERMANY));

        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictim(victim);
        resultVictory.setVictor(victor);
        
        PlayerVictoryDeclaration playerDeclaration = new PlayerVictoryDeclaration();
        playerDeclaration.setAircraftType("sopcamel");

        PlayerClaimResolverFirm claimResolverFirm = new PlayerClaimResolverFirm();
        String planeDisplayName = claimResolverFirm.getShotDownPlaneDisplayNameAsFirm(player, playerDeclaration, resultVictory);
        
        assert (planeDisplayName.isEmpty());
    }

    @Test
    public void testPlayerFirmVictoryNotFoundBecauseVictoryPlaneNotFound() throws PWCGException
    {
        LogPlane victim = new LogPlane(1);
        victim.setVehicleType("notarealplane");
        victim.setCountry(new FCCountry(Country.BRITAIN));

        LogPlane victor = new LogPlane(2);
        victor.setVehicleType("albatrosd5");
        victor.setPilotSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        victor.setCountry(new FCCountry(Country.GERMANY));

        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictim(victim);
        resultVictory.setVictor(victor);
        
        PlayerVictoryDeclaration playerDeclaration = new PlayerVictoryDeclaration();
        playerDeclaration.setAircraftType("se5a");

        PlayerClaimResolverFirm claimResolverFirm = new PlayerClaimResolverFirm();
        String planeDisplayName = claimResolverFirm.getShotDownPlaneDisplayNameAsFirm(player, playerDeclaration, resultVictory);
        
        assert (planeDisplayName.isEmpty());
    }

    @Test
    public void testPlayerFirmVictoryNotFoundBecauseClaimPlaneNotFound() throws PWCGException
    {
        LogPlane victim = new LogPlane(1);
        victim.setVehicleType("se5a");
        victim.setCountry(new FCCountry(Country.BRITAIN));

        LogPlane victor = new LogPlane(2);
        victor.setVehicleType("albatrosd5");
        victor.setPilotSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        victor.setCountry(new FCCountry(Country.GERMANY));

        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictim(victim);
        resultVictory.setVictor(victor);
        
        PlayerVictoryDeclaration playerDeclaration = new PlayerVictoryDeclaration();
        playerDeclaration.setAircraftType("notarealplane");

        PlayerClaimResolverFirm claimResolverFirm = new PlayerClaimResolverFirm();
        String planeDisplayName = claimResolverFirm.getShotDownPlaneDisplayNameAsFirm(player, playerDeclaration, resultVictory);
        
        assert (planeDisplayName.isEmpty());
    }

    @Test
    public void testPlayerFirmVictoryNotFoundBecauseVictoryAlreadyConfirmed() throws PWCGException
    {
        LogPlane victim = new LogPlane(1);
        victim.setVehicleType("se5a");
        victim.setCountry(new FCCountry(Country.BRITAIN));

        LogPlane victor = new LogPlane(2);
        victor.setVehicleType("albatrosd5");
        victor.setPilotSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        victor.setCountry(new FCCountry(Country.GERMANY));

        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictim(victim);
        resultVictory.setVictor(victor);
        resultVictory.setConfirmed(true);
        
        PlayerVictoryDeclaration playerDeclaration = new PlayerVictoryDeclaration();
        playerDeclaration.setAircraftType("se5a");

        PlayerClaimResolverFirm claimResolverFirm = new PlayerClaimResolverFirm();
        String planeDisplayName = claimResolverFirm.getShotDownPlaneDisplayNameAsFirm(player, playerDeclaration, resultVictory);
        
        assert (planeDisplayName.isEmpty());
    }
    
    @Test
    public void testNoFriendlyVictories () throws PWCGException
    {   
        LogPlane victim = new LogPlane(1);
        victim.setRole(PwcgRole.ROLE_FIGHTER);
        victim.setVehicleType("albatrosd5");
        victim.setCountry(new FCCountry(Country.GERMANY));

        LogPlane victor = new LogPlane(2);
        victor.setVehicleType("albatrosd5");
        victor.setPilotSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        victim.setCountry(new FCCountry(Country.GERMANY));

        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictim(victim);
        resultVictory.setVictor(victor);
        
        PlayerVictoryDeclaration playerDeclaration = new PlayerVictoryDeclaration();
        playerDeclaration.setAircraftType("se5a");

        PlayerClaimResolverFirm claimResolverFirm = new PlayerClaimResolverFirm();
        String planeDisplayName = claimResolverFirm.getShotDownPlaneDisplayNameAsFirm(player, playerDeclaration, resultVictory);
        
        assert (planeDisplayName.isEmpty());
    }

}
