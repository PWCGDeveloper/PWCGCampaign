package pwcg.aar.inmission.phase3.reconcile.victories;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.inmission.phase3.reconcile.victories.ClaimDenier;
import pwcg.aar.inmission.phase3.reconcile.victories.PlayerVictoryDeclaration;
import pwcg.aar.ui.events.model.ClaimDeniedEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.PlaneTypeFactory;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

@RunWith(MockitoJUnitRunner.class)
public class ClaimDenierTest
{
    @Mock 
    private PlayerVictoryDeclaration playerDeclaration;
    
    @Mock 
    private Campaign campaign;
    
    @Mock
    private CampaignPersonnelManager personnelManager;
    
    @Mock
    private SquadronMember player;
    
    @Mock
    private SquadronMember pilot;
    
    @Mock
    private Squadron squadron;
 
    @Mock 
    private PlaneTypeFactory planeFactory;
    
    @Mock
    private PlaneType planeType;
   
    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(false);

        Mockito.when(campaign.getPlayer()).thenReturn(player);
        Mockito.when(campaign.getPersonnelManager()).thenReturn(personnelManager);
        Mockito.when(personnelManager.getActiveCampaignMember(Matchers.<Integer>any())).thenReturn(pilot);
        Mockito.when(pilot.determineSquadron()).thenReturn(squadron);
        Mockito.when(squadron.determineDisplayName(Mockito.any())).thenReturn("Esc 3");
        
        Mockito.when(pilot.determineSquadron()).thenReturn(squadron);
        Mockito.when(player.determineSquadron()).thenReturn(squadron);
    }

    @Test
    public void testClamAccepted() throws PWCGException
    {
        Mockito.when(playerDeclaration.isConfirmed()).thenReturn(true);

        ClaimDenier claimDenier = new ClaimDenier(campaign, planeFactory);
        ClaimDeniedEvent claimDeniedEvent = claimDenier.determineClaimDenied(playerDeclaration);
        assert (claimDeniedEvent == null);
    }
    
    @Test 
    public void testClaimDeniedPlane() throws PWCGException
    {

        Mockito.when(playerDeclaration.isConfirmed()).thenReturn(false);
        Mockito.when(playerDeclaration.getAircraftType()).thenReturn("Albatros D.III");
        Mockito.when(planeFactory.getPlaneTypeByAnyName(Matchers.<String>any())).thenReturn(planeType);
        Mockito.when(planeType.getDisplayName()).thenReturn("Albatros D.III");
        
        ClaimDenier claimDenier = new ClaimDenier(campaign, planeFactory);
        ClaimDeniedEvent claimDeniedEvent = claimDenier.determineClaimDenied(playerDeclaration);
        assert (claimDeniedEvent.getType().equals("Albatros D.III"));
    }
    
    @Test 
    public void testClaimDeniedBalloon() throws PWCGException
    {

        Mockito.when(playerDeclaration.isConfirmed()).thenReturn(false);
        Mockito.when(playerDeclaration.getAircraftType()).thenReturn(PlaneType.BALLOON);
        Mockito.when(planeFactory.getPlaneTypeByAnyName(Matchers.<String>any())).thenReturn(planeType);
        Mockito.when(planeType.getDisplayName()).thenReturn("Albatros D.III");
        
        ClaimDenier claimDenier = new ClaimDenier(campaign, planeFactory);
        ClaimDeniedEvent claimDeniedEvent = claimDenier.determineClaimDenied(playerDeclaration);
        assert (claimDeniedEvent.getType().equals(PlaneType.BALLOON));
    }
}
