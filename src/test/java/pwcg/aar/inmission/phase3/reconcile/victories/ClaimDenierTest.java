package pwcg.aar.inmission.phase3.reconcile.victories;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.ui.events.model.ClaimDeniedEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.PlaneTypeFactory;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

@RunWith(MockitoJUnitRunner.class)
public class ClaimDenierTest
{
    @Mock private PlayerVictoryDeclaration declaration;
    @Mock private Campaign campaign;
    @Mock private CampaignPersonnelManager personnelManager;
    @Mock private SquadronMember player;
    @Mock private SquadronMember pilot;
    @Mock private Squadron squadron;
    @Mock private PlaneTypeFactory planeFactory;
    @Mock private PlaneType planeType;
   
    private List<SquadronMember> players = new ArrayList<>();

    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        
        players = new ArrayList<>();
        players.add(player);

        Mockito.when(campaign.getPlayers()).thenReturn(players);
        Mockito.when(campaign.getPersonnelManager()).thenReturn(personnelManager);
        Mockito.when(personnelManager.getAnyCampaignMember(Matchers.<Integer>any())).thenReturn(pilot);
        Mockito.when(pilot.determineSquadron()).thenReturn(squadron);
        Mockito.when(squadron.determineDisplayName(Mockito.any())).thenReturn("Esc 3");
        
        Mockito.when(pilot.determineSquadron()).thenReturn(squadron);
        Mockito.when(player.determineSquadron()).thenReturn(squadron);
    }

    @Test
    public void testClamAccepted() throws PWCGException
    {
        Mockito.when(declaration.isConfirmed()).thenReturn(true);

        ClaimDenier claimDenier = new ClaimDenier(campaign, planeFactory);
        ClaimDeniedEvent claimDeniedEvent = claimDenier.determineClaimDenied(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER, declaration);
        assert (claimDeniedEvent == null);
    }
    
    @Test 
    public void testClaimDeniedPlane() throws PWCGException
    {

        Mockito.when(declaration.isConfirmed()).thenReturn(false);
        Mockito.when(declaration.getAircraftType()).thenReturn("Albatros D.III");
        Mockito.when(planeFactory.createPlaneTypeByAnyName(Matchers.<String>any())).thenReturn(planeType);
        Mockito.when(planeType.getDisplayName()).thenReturn("Albatros D.III");
        
        ClaimDenier claimDenier = new ClaimDenier(campaign, planeFactory);
        ClaimDeniedEvent claimDeniedEvent = claimDenier.determineClaimDenied(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER, declaration);
        assert (claimDeniedEvent.getType().equals("Albatros D.III"));
    }
    
    @Test 
    public void testClaimDeniedBalloon() throws PWCGException
    {

        Mockito.when(declaration.isConfirmed()).thenReturn(false);
        Mockito.when(declaration.getAircraftType()).thenReturn(PlaneType.BALLOON);
        Mockito.when(planeFactory.createPlaneTypeByAnyName(Matchers.<String>any())).thenReturn(planeType);
        Mockito.when(planeType.getDisplayName()).thenReturn("Albatros D.III");
        
        ClaimDenier claimDenier = new ClaimDenier(campaign, planeFactory);
        ClaimDeniedEvent claimDeniedEvent = claimDenier.determineClaimDenied(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER, declaration);
        assert (claimDeniedEvent.getType().equals(PlaneType.BALLOON));
    }
}
