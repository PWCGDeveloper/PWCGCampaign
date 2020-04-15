package pwcg.aar.inmission.phase3.reconcile.victories.coop;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.aar.inmission.phase3.reconcile.victories.IClaimResolver;
import pwcg.aar.inmission.phase3.reconcile.victories.ReconciledVictoryData;
import pwcg.aar.inmission.phase3.reconcile.victories.singleplayer.LogVictoryHelper;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@RunWith(MockitoJUnitRunner.class)
public class ClaimResolverCoopTest
{
    @Mock private Campaign campaign;
    @Mock private CampaignPersonnelManager personnelManager;
    @Mock private SquadronMembers playerMembers;
    @Mock private SquadronMember pilot;

    private LogVictoryHelper logVictoryHelper = new LogVictoryHelper();
    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);

        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getBeginningOfWar());
        Mockito.when(campaign.getPersonnelManager()).thenReturn(personnelManager);
        Mockito.when(personnelManager.getAnyCampaignMember(ArgumentMatchers.<Integer>any())).thenReturn(pilot);
        Mockito.when(personnelManager.getAnyCampaignMember(ArgumentMatchers.<Integer>any())).thenReturn(pilot);
        Mockito.when(pilot.getSerialNumber()).thenReturn(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        
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
        ReconciledVictoryData reconciledMissionData = claimResolver.resolvePlayerClaims();
        assert (reconciledMissionData.getVictoryAwardsByPilot().size() == 1);
        assert (reconciledMissionData.getVictoryAwardsByPilot().get(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER).size() == 5);
        assert (reconciledMissionData.getPlayerClaimsDenied().size() == 0);
    }
}
