package pwcg.aar.inmission.phase3.reconcile.victories.singleplayer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.product.fc.country.FCCountry;

@RunWith(MockitoJUnitRunner.class)
public class PlayerClaimResolverFuzzyNotExactTest
{
    @Mock private SquadronMember player;

    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        Mockito.when(player.getCountry()).thenReturn(Country.GERMANY);
    }

    @Test
    public void testPlayerFuzzyNotExactVictoryFoundWithExactMatch() throws PWCGException
    {
        LogPlane victim = new LogPlane(1);
        victim.setRole(Role.ROLE_FIGHTER);
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

        PlayerClaimResolverFuzzy claimResolverFuzzy = new PlayerClaimResolverFuzzy();
        String planeDisplayName = claimResolverFuzzy.getShotDownPlaneDisplayNameAsFuzzyNotExact(player, playerDeclaration, resultVictory);
        
        assert (planeDisplayName.equals("S.E.5a"));
    }

    @Test
    public void testPlayerFuzzyNotExactVictoryFoundWithNotExactMatch() throws PWCGException
    {
        LogPlane victim = new LogPlane(1);
        victim.setRole(Role.ROLE_FIGHTER);
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

        PlayerClaimResolverFuzzy claimResolverFuzzy = new PlayerClaimResolverFuzzy();
        String planeDisplayName = claimResolverFuzzy.getShotDownPlaneDisplayNameAsFuzzyNotExact(player, playerDeclaration, resultVictory);
        
        assert (planeDisplayName.equals("S.E.5a"));
    }

    @Test
    public void testPlayerFuzzyNotExactVictoryNotFoundBecauseRoleIsDifferent() throws PWCGException
    {
        LogPlane victim = new LogPlane(1);
        victim.setRole(Role.ROLE_BOMB);
        victim.setVehicleType("notarealplane");

        LogPlane victor = new LogPlane(2);
        victor.setVehicleType("albatrosd5");
        victor.setPilotSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);

        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictim(victim);
        resultVictory.setVictor(victor);
        
        PlayerVictoryDeclaration playerDeclaration = new PlayerVictoryDeclaration();
        playerDeclaration.setAircraftType("se5a");

        PlayerClaimResolverFuzzy claimResolverFuzzy = new PlayerClaimResolverFuzzy();
        String planeDisplayName = claimResolverFuzzy.getShotDownPlaneDisplayNameAsFuzzyNotExact(player, playerDeclaration, resultVictory);
        
        assert (planeDisplayName.isEmpty());
    }

    @Test
    public void testPlayerFuzzyNotExactVictoryNotFoundBecauseClaimPlaneNotFound() throws PWCGException
    {
        LogPlane victim = new LogPlane(1);
        victim.setRole(Role.ROLE_FIGHTER);
        victim.setVehicleType("se5a");

        LogPlane victor = new LogPlane(2);
        victor.setVehicleType("albatrosd5");
        victor.setPilotSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);

        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictim(victim);
        resultVictory.setVictor(victor);
        
        PlayerVictoryDeclaration playerDeclaration = new PlayerVictoryDeclaration();
        playerDeclaration.setAircraftType("notarealplane");

        PlayerClaimResolverFuzzy claimResolverFuzzy = new PlayerClaimResolverFuzzy();
        String planeDisplayName = claimResolverFuzzy.getShotDownPlaneDisplayNameAsFuzzyNotExact(player, playerDeclaration, resultVictory);
        
        assert (planeDisplayName.isEmpty());
    }

    @Test
    public void testPlayerFuzzyNotExactVictoryNotFoundBecauseVictoryAlreadyConfirmed() throws PWCGException
    {
        LogPlane victim = new LogPlane(1);
        victim.setRole(Role.ROLE_FIGHTER);
        victim.setVehicleType("se5a");

        LogPlane victor = new LogPlane(2);
        victor.setVehicleType("albatrosd5");
        victor.setPilotSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);

        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictim(victim);
        resultVictory.setVictor(victor);
        resultVictory.setConfirmed(true);
        
        PlayerVictoryDeclaration playerDeclaration = new PlayerVictoryDeclaration();
        playerDeclaration.setAircraftType("se5a");

        PlayerClaimResolverFuzzy claimResolverFuzzy = new PlayerClaimResolverFuzzy();
        String planeDisplayName = claimResolverFuzzy.getShotDownPlaneDisplayNameAsFuzzyNotExact(player, playerDeclaration, resultVictory);
        
        assert (planeDisplayName.isEmpty());
    }
    
    
    @Test
    public void testNoFriendlyVictories () throws PWCGException
    {   
        LogPlane victim = new LogPlane(1);
        victim.setRole(Role.ROLE_FIGHTER);
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

        PlayerClaimResolverFuzzy claimResolverFuzzy = new PlayerClaimResolverFuzzy();
        String planeDisplayName = claimResolverFuzzy.getShotDownPlaneDisplayNameAsFuzzyNotExact(player, playerDeclaration, resultVictory);
        
        assert (planeDisplayName.isEmpty());
    }

}
