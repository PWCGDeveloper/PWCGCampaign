package pwcg.aar.inmission.phase3.reconcile.victories.coop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.inmission.phase3.reconcile.victories.IClaimResolver;
import pwcg.aar.inmission.phase3.reconcile.victories.ReconciledMissionVictoryData;
import pwcg.aar.inmission.phase3.reconcile.victories.singleplayer.LogVictoryHelper;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
public class ClaimResolverCoopTest
{
    @Mock private Campaign campaign;
    @Mock private CampaignPersonnelManager personnelManager;
    @Mock private CrewMembers playerMembers;
    @Mock private CrewMember crewMember;

    private LogVictoryHelper logVictoryHelper = new LogVictoryHelper();
    
    @BeforeEach
    public void setupTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);

        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getBeginningOfWar());
        Mockito.when(campaign.getPersonnelManager()).thenReturn(personnelManager);
        Mockito.when(personnelManager.getAnyCampaignMember(ArgumentMatchers.<Integer>any())).thenReturn(crewMember);
        Mockito.when(personnelManager.getAnyCampaignMember(ArgumentMatchers.<Integer>any())).thenReturn(crewMember);
        Mockito.when(crewMember.getSerialNumber()).thenReturn(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        
        logVictoryHelper = new LogVictoryHelper();
        logVictoryHelper.createPlaneVictory();
        logVictoryHelper.createPlaneVictory();
        logVictoryHelper.createPlaneVictory();
        logVictoryHelper.createBalloonVictory();
        logVictoryHelper.createGroundVictory();
        logVictoryHelper.createGroundVictory();
        logVictoryHelper.createFuzzyPlaneVictory();
        logVictoryHelper.createFuzzyPlaneVictory();
        logVictoryHelper.createFuzzyBalloonVictory();
    }

    @Test
    public void testClaimAccepted() throws PWCGException
    {
        IClaimResolver claimResolver = new ClaimResolverCompetitiveCoop(campaign, logVictoryHelper.getLogVictories());
        ReconciledMissionVictoryData reconciledMissionData = claimResolver.resolvePlayerClaims();
        Assertions.assertTrue (reconciledMissionData.getVictoryAwardsByCrewMember().size() == 1);
        Assertions.assertTrue (reconciledMissionData.getVictoryAwardsByCrewMember().get(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER).size() == 5);
        Assertions.assertTrue (reconciledMissionData.getPlayerClaimsDenied().size() == 0);
    }
}
