package pwcg.aar.inmission.phase3.reconcile.victories.singleplayer;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.aar.inmission.phase3.reconcile.victories.IClaimResolver;
import pwcg.aar.inmission.phase3.reconcile.victories.ReconciledVictoryData;
import pwcg.aar.inmission.phase3.reconcile.victories.common.ConfirmedVictories;
import pwcg.aar.inmission.phase3.reconcile.victories.singleplayer.ClaimDenier;
import pwcg.aar.inmission.phase3.reconcile.victories.singleplayer.ClaimResolverSinglePlayer;
import pwcg.aar.inmission.phase3.reconcile.victories.singleplayer.PlayerDeclarations;
import pwcg.aar.inmission.phase3.reconcile.victories.singleplayer.PlayerVictoryDeclaration;
import pwcg.aar.inmission.phase3.reconcile.victories.singleplayer.VerifiedVictoryGenerator;
import pwcg.aar.ui.events.model.ClaimDeniedEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.DateUtils;

@RunWith(MockitoJUnitRunner.class)
public class ClaimResolverTest
{
    @Mock private Campaign campaign;
    @Mock private CampaignPersonnelManager personnelManager;
    @Mock private SquadronMembers playerMembers;
    @Mock private SquadronMember pilot;
    @Mock private VerifiedVictoryGenerator verifiedVictoryGenerator;
    @Mock private ClaimDenier claimDenier;

    private ConfirmedVictories verifiedVictories;
    private Map<Integer, PlayerDeclarations> playerDeclarations = new HashMap<>();
    private PlayerDeclarations playerDeclarationSet;

    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(true);

        verifiedVictories = new ConfirmedVictories();
        playerDeclarationSet = new PlayerDeclarations();
        for (int i = 0; i < 3; ++i)
        {
            PlayerVictoryDeclaration declaration = new PlayerVictoryDeclaration();
            declaration.confirmDeclaration(true, "SE.5a");
            playerDeclarationSet.addDeclaration(declaration);
        }
        
        playerDeclarations.clear();
        playerDeclarations.put(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER, playerDeclarationSet);

        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getBeginningOfWar());
        Mockito.when(campaign.getPersonnelManager()).thenReturn(personnelManager);
        Mockito.when(personnelManager.getAnyCampaignMember(Matchers.<Integer>any())).thenReturn(pilot);
        Mockito.when(personnelManager.getAnyCampaignMember(Matchers.<Integer>any())).thenReturn(pilot);
        Mockito.when(pilot.getSerialNumber()).thenReturn(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        Mockito.when(verifiedVictoryGenerator.createVerifiedictories(playerDeclarations)).thenReturn(verifiedVictories);
    }

    @Test
    public void testClaimAccepted() throws PWCGException
    {
        for (int i = 0; i < 3; ++i)
        {
            LogPlane victor = new LogPlane(10+1);
            victor.setPilotSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
            victor.setSquadronId(501011);
            victor.intializePilot(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
            
            LogPlane victim = new LogPlane(100+i);
            victim.setPilotSerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER);
            victim.setSquadronId(102056);
            victim.intializePilot(SerialNumber.AI_STARTING_SERIAL_NUMBER);

            LogVictory missionResultVictory = new LogVictory(1000+i);
            missionResultVictory.setLocation(new Coordinate(100000.0, 0.0, 100000.0));
            missionResultVictory.setVictor(victor);
            missionResultVictory.setVictim(victim);
            verifiedVictories.addVictory(missionResultVictory);
        }

        IClaimResolver claimResolver = new ClaimResolverSinglePlayer(campaign, verifiedVictoryGenerator, claimDenier, playerDeclarations);
        ReconciledVictoryData reconciledMissionData = claimResolver.resolvePlayerClaims();
        assert (reconciledMissionData.getVictoryAwardsByPilot().size() == 1);
        assert (reconciledMissionData.getVictoryAwardsByPilot().get(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER).size() == 3);
        assert (reconciledMissionData.getPlayerClaimsDenied().size() == 0);
    }

    @Test
    public void testClaimNotAccepted() throws PWCGException
    {
        IClaimResolver claimResolver = new ClaimResolverSinglePlayer(campaign, verifiedVictoryGenerator, claimDenier, playerDeclarations);
        ReconciledVictoryData reconciledMissionData = claimResolver.resolvePlayerClaims();
        assert (reconciledMissionData.getVictoryAwardsByPilot().size() == 0);
        assert (reconciledMissionData.getPlayerClaimsDenied().size() == 0);
    }

    @Test
    public void testClaimDenied() throws PWCGException
    {
        Mockito.when(claimDenier.determineClaimDenied(Matchers.<Integer>any(), Matchers.<PlayerVictoryDeclaration>any())).thenReturn(new ClaimDeniedEvent(101103));

        IClaimResolver claimResolver = new ClaimResolverSinglePlayer(campaign, verifiedVictoryGenerator, claimDenier, playerDeclarations);
        ReconciledVictoryData reconciledMissionData = claimResolver.resolvePlayerClaims();
        assert (reconciledMissionData.getVictoryAwardsByPilot().size() == 0);
        assert (reconciledMissionData.getPlayerClaimsDenied().size() == 3);
    }

}
