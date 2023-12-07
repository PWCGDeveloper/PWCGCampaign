package pwcg.aar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.aar.inmission.phase3.reconcile.victories.singleplayer.PlayerDeclarations;
import pwcg.aar.prelim.AARPreliminaryData;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.logfiles.LogEventData;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestCampaignFactoryBuilder;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AARCoordinatorInMissionBoSTest
{
    private Campaign campaign;    
    private static AARCoordinator aarCoordinator;
    private static AARTestExpectedResults expectedResults;
    private static int playerMissionsFlown = 0;

    private List<Squadron> squadronsInMission = new ArrayList<>();
    private Map<Integer, PlayerDeclarations> playerDeclarations;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = TestCampaignFactoryBuilder.makeCampaign(this.getClass().getCanonicalName(), SquadronTestProfile.JG_51_PROFILE_MOSCOW);
        expectedResults = new AARTestExpectedResults(campaign);
        aarCoordinator = AARCoordinator.getInstance();
        aarCoordinator.reset(campaign);
        
        playerMissionsFlown = campaign.getPersonnelManager().getFlyingPlayers().getSquadronMemberList().get(0).getMissionFlown();
    }

    @Test
    public void runMissionAARZManyTimes () throws PWCGException
    {
        runMissionAAR();
    }
    
    public void runMissionAAR () throws PWCGException
    {
        createArtifacts ();
        aarCoordinator.performMissionAAR(playerDeclarations);
        expectedResults.buildExpectedResultsFromAARContext(aarCoordinator.getAarContext());
        
        BoSAARResultValidator resultValidator = new BoSAARResultValidator(expectedResults);
        resultValidator.validateInMission(playerMissionsFlown, 2);
    }

    public void createArtifacts () throws PWCGException
    {               
        makeSquadronsInMission();
        makePreliminary();
        makePlayerDeclarations();
        makeMissionLogEvents();
    }

    private void makeSquadronsInMission() throws PWCGException
    {
        SquadronForMissionBuilderBoS squadronForMissionBuilder = new SquadronForMissionBuilderBoS(campaign);
        squadronsInMission = squadronForMissionBuilder.makeSquadronsInMission();
    }

    private void makePreliminary() throws PWCGException
    {
        TestPreliminaryDataBuilderBoS preliminaryDataBuilder = new TestPreliminaryDataBuilderBoS(campaign, squadronsInMission);
        AARPreliminaryData preliminaryData = preliminaryDataBuilder.makePreliminaryForTestMission();
        aarCoordinator.getAarContext().setPreliminaryData(preliminaryData);
    }

    private void makePlayerDeclarations() throws PWCGException
    {
        SquadronMember player = campaign.getPersonnelManager().getFlyingPlayers().getSquadronMemberList().get(0);
        PlayerDeclarationsBuilder  declarationsBuilder = new PlayerDeclarationsBuilder();
        playerDeclarations = declarationsBuilder.makePlayerDeclarations(player, "yak1s69", "il2m41");
    }

    private void makeMissionLogEvents() throws PWCGException
    {
        BoSMissionLogEventsBuilder missionLogEventsBuilder = new BoSMissionLogEventsBuilder(campaign, 
                aarCoordinator.getAarContext().getPreliminaryData(), expectedResults);
        LogEventData missionLogRawData = missionLogEventsBuilder.makeLogEvents();
        aarCoordinator.getAarContext().setLogEventData(missionLogRawData);
    }
}
