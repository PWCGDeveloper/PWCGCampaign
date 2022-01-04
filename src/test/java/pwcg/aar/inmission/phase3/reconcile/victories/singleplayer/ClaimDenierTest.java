package pwcg.aar.inmission.phase3.reconcile.victories.singleplayer;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.aar.ui.events.model.ClaimDeniedEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.plane.TankType;
import pwcg.campaign.plane.TankTypeFactory;
import pwcg.core.exception.PWCGException;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ClaimDenierTest
{
    @Mock private PlayerVictoryDeclaration declaration;
    @Mock private Campaign campaign;
    @Mock private CampaignPersonnelManager personnelManager;
    @Mock private CrewMembers playerMembers;
    @Mock private CrewMember player;
    @Mock private CrewMember crewMember;
    @Mock private Company squadron;
    @Mock private TankTypeFactory planeFactory;
    @Mock private TankType planeType;
   
    private List<CrewMember> players = new ArrayList<>();

    @BeforeEach
    public void setupTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        
        players = new ArrayList<>();
        players.add(player);

        Mockito.when(campaign.getPersonnelManager()).thenReturn(personnelManager);
        Mockito.when(personnelManager.getAnyCampaignMember(ArgumentMatchers.<Integer>any())).thenReturn(crewMember);
    }

    @Test
    public void testClamAccepted() throws PWCGException
    {
        Mockito.when(declaration.isConfirmed()).thenReturn(true);

        ClaimDenier claimDenier = new ClaimDenier(campaign, planeFactory);
        ClaimDeniedEvent claimDeniedEvent = claimDenier.determineClaimDenied(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER, declaration);
        Assertions.assertTrue (claimDeniedEvent == null);
    }
    
    @Test 
    public void testClaimDeniedPlane() throws PWCGException
    {

        Mockito.when(declaration.isConfirmed()).thenReturn(false);
        Mockito.when(declaration.getAircraftType()).thenReturn("Albatros D.III");
        Mockito.when(planeFactory.createTankTypeByAnyName(ArgumentMatchers.<String>any())).thenReturn(planeType);
        Mockito.when(planeType.getDisplayName()).thenReturn("Albatros D.III");
        
        ClaimDenier claimDenier = new ClaimDenier(campaign, planeFactory);
        ClaimDeniedEvent claimDeniedEvent = claimDenier.determineClaimDenied(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER, declaration);
        Assertions.assertTrue (claimDeniedEvent.getType().equals("Albatros D.III"));
    }
    
    @Test 
    public void testClaimDeniedBalloon() throws PWCGException
    {

        Mockito.when(declaration.isConfirmed()).thenReturn(false);
        Mockito.when(declaration.getAircraftType()).thenReturn(TankType.BALLOON);
        
        ClaimDenier claimDenier = new ClaimDenier(campaign, planeFactory);
        ClaimDeniedEvent claimDeniedEvent = claimDenier.determineClaimDenied(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER, declaration);
        Assertions.assertTrue (claimDeniedEvent.getType().equals(TankType.BALLOON));
    }
}
