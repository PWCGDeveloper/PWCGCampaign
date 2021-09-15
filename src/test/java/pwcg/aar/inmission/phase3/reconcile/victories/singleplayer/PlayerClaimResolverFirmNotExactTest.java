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
import pwcg.campaign.plane.PwcgRoleCategory;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.product.fc.country.FCCountry;

@RunWith(MockitoJUnitRunner.class)
public class PlayerClaimResolverFirmNotExactTest
{
    @Mock private SquadronMember player;

    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        Mockito.when(player.getCountry()).thenReturn(Country.GERMANY);
    }

    @Test
    public void testPlayerFirmNotExactVictoryFoundWithExactMatch() throws PWCGException
    {
        LogPlane victim = new LogPlane(1);
        victim.setRoleCategory(PwcgRoleCategory.FIGHTER);
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
        String planeDisplayName = claimResolverFirm.getShotDownPlaneDisplayNameAsFirmNotExact(player, playerDeclaration, resultVictory);
        
        assert (planeDisplayName.equals("S.E.5a"));
    }

    @Test
    public void testPlayerFirmNotExactVictoryFoundWithNotExactMatch() throws PWCGException
    {
        LogPlane victim = new LogPlane(1);
        victim.setRoleCategory(PwcgRoleCategory.FIGHTER);
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
        String planeDisplayName = claimResolverFirm.getShotDownPlaneDisplayNameAsFirmNotExact(player, playerDeclaration, resultVictory);
        
        assert (planeDisplayName.equals("S.E.5a"));
    }

    @Test
    public void testPlayerFirmNotExactVictoryNotFoundBecauseRoleIsDifferent() throws PWCGException
    {
        LogPlane victim = new LogPlane(1);
        victim.setRoleCategory(PwcgRoleCategory.BOMBER);
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
        String planeDisplayName = claimResolverFirm.getShotDownPlaneDisplayNameAsFirmNotExact(player, playerDeclaration, resultVictory);
        
        assert (planeDisplayName.isEmpty());
    }

    @Test
    public void testPlayerFirmNotExactVictoryNotFoundBecauseClaimPlaneNotFound() throws PWCGException
    {
        LogPlane victim = new LogPlane(1);
        victim.setRoleCategory(PwcgRoleCategory.FIGHTER);
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
        String planeDisplayName = claimResolverFirm.getShotDownPlaneDisplayNameAsFirmNotExact(player, playerDeclaration, resultVictory);
        
        assert (planeDisplayName.isEmpty());
    }

    @Test
    public void testPlayerFirmNotExactVictoryNotFoundBecauseVictoryAlreadyConfirmed() throws PWCGException
    {
        LogPlane victim = new LogPlane(1);
        victim.setRoleCategory(PwcgRoleCategory.FIGHTER);
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
        String planeDisplayName = claimResolverFirm.getShotDownPlaneDisplayNameAsFirmNotExact(player, playerDeclaration, resultVictory);
        
        assert (planeDisplayName.isEmpty());
    }   
    
    @Test
    public void testNoFriendlyVictories () throws PWCGException
    {   
        LogPlane victim = new LogPlane(1);
        victim.setRoleCategory(PwcgRoleCategory.FIGHTER);
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
        String planeDisplayName = claimResolverFirm.getShotDownPlaneDisplayNameAsFirmNotExact(player, playerDeclaration, resultVictory);
        
        assert (planeDisplayName.isEmpty());
    }
}
