package pwcg.aar.inmission.phase3.reconcile.victories.singleplayer;

import java.util.HashMap;
import java.util.Map;

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

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.aar.inmission.phase3.reconcile.victories.IClaimResolver;
import pwcg.aar.inmission.phase3.reconcile.victories.ReconciledMissionVictoryData;
import pwcg.aar.inmission.phase3.reconcile.victories.common.ConfirmedVictories;
import pwcg.aar.ui.events.model.ClaimDeniedEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ClaimResolverTest
{
    @Mock private Campaign campaign;
    @Mock private CampaignPersonnelManager personnelManager;
    @Mock private CrewMembers playerMembers;
    @Mock private CrewMember crewMember;
    @Mock private VerifiedVictoryGenerator verifiedVictoryGenerator;
    @Mock private ClaimDenier claimDenier;

    private ConfirmedVictories verifiedVictories;
    private Map<Integer, PlayerDeclarations> playerDeclarations = new HashMap<>();
    private PlayerDeclarations playerDeclarationSet;

    
    @BeforeEach
    public void setupTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);

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
        Mockito.when(personnelManager.getAnyCampaignMember(ArgumentMatchers.<Integer>any())).thenReturn(crewMember);
        Mockito.when(personnelManager.getAnyCampaignMember(ArgumentMatchers.<Integer>any())).thenReturn(crewMember);
        Mockito.when(crewMember.getSerialNumber()).thenReturn(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        Mockito.when(verifiedVictoryGenerator.createVerifiedictories(playerDeclarations)).thenReturn(verifiedVictories);
    }

    @Test
    public void testClaimAccepted() throws PWCGException
    {
        for (int i = 0; i < 3; ++i)
        {
            LogPlane victor = new LogPlane(10+1);
            victor.setCrewMemberSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
            victor.setCompanyId(SquadronTestProfile.JASTA_11_PROFILE.getCompanyId());
            victor.intializeCrewMember(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
            
            LogPlane victim = new LogPlane(100+i);
            victim.setCrewMemberSerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER);
            victim.setSquadronId(302056);
            victim.intializeCrewMember(SerialNumber.AI_STARTING_SERIAL_NUMBER);

            LogVictory missionResultVictory = new LogVictory(1000+i);
            missionResultVictory.setLocation(new Coordinate(100000.0, 0.0, 100000.0));
            missionResultVictory.setVictor(victor);
            missionResultVictory.setVictim(victim);
            verifiedVictories.addVictory(missionResultVictory);
        }

        IClaimResolver claimResolver = new ClaimResolverSinglePlayer(campaign, verifiedVictoryGenerator, claimDenier, playerDeclarations);
        ReconciledMissionVictoryData reconciledMissionData = claimResolver.resolvePlayerClaims();
        Assertions.assertTrue (reconciledMissionData.getVictoryAwardsByCrewMember().size() == 1);
        Assertions.assertTrue (reconciledMissionData.getVictoryAwardsByCrewMember().get(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER).size() == 3);
        Assertions.assertTrue (reconciledMissionData.getPlayerClaimsDenied().size() == 0);
    }

    @Test
    public void testClaimNotAccepted() throws PWCGException
    {
        IClaimResolver claimResolver = new ClaimResolverSinglePlayer(campaign, verifiedVictoryGenerator, claimDenier, playerDeclarations);
        ReconciledMissionVictoryData reconciledMissionData = claimResolver.resolvePlayerClaims();
        Assertions.assertTrue (reconciledMissionData.getVictoryAwardsByCrewMember().size() == 0);
        Assertions.assertTrue (reconciledMissionData.getPlayerClaimsDenied().size() == 0);
    }

    @Test
    public void testClaimDenied() throws PWCGException
    {
        boolean isNewsworthy = false;
        ClaimDeniedEvent claimDenied = new ClaimDeniedEvent(campaign, "Any Plane", SquadronTestProfile.ESC_103_PROFILE.getCompanyId(), SerialNumber.PLAYER_STARTING_SERIAL_NUMBER, campaign.getDate(), isNewsworthy);

        Mockito.when(claimDenier.determineClaimDenied(ArgumentMatchers.<Integer>any(), ArgumentMatchers.<PlayerVictoryDeclaration>any())).thenReturn(claimDenied);

        IClaimResolver claimResolver = new ClaimResolverSinglePlayer(campaign, verifiedVictoryGenerator, claimDenier, playerDeclarations);
        ReconciledMissionVictoryData reconciledMissionData = claimResolver.resolvePlayerClaims();
        Assertions.assertTrue (reconciledMissionData.getVictoryAwardsByCrewMember().size() == 0);
        Assertions.assertTrue (reconciledMissionData.getPlayerClaimsDenied().size() == 3);
    }

}
