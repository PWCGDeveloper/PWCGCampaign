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
public class AARCoordinatorInMissionFCTest
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
        PWCGContext.setProduct(PWCGProduct.FC);
        campaign = TestCampaignFactoryBuilder.makeCampaign(this.getClass().getCanonicalName(), SquadronTestProfile.JASTA_04_PROFILE);
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
        
        FCAARResultValidator resultValidator = new FCAARResultValidator(expectedResults);
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
        SquadronForMissionBuilderFC squadronForMissionBuilder = new SquadronForMissionBuilderFC(campaign);
        squadronsInMission = squadronForMissionBuilder.makeSquadronsInMission();
    }

    private void makePreliminary() throws PWCGException
    {
        TestPreliminaryDataBuilderFC preliminaryDataBuilder = new TestPreliminaryDataBuilderFC(campaign, squadronsInMission);
        AARPreliminaryData preliminaryData = preliminaryDataBuilder.makePreliminaryForTestMission();
        aarCoordinator.getAarContext().setPreliminaryData(preliminaryData);
    }

    private void makePlayerDeclarations() throws PWCGException
    {
        SquadronMember player = campaign.getPersonnelManager().getFlyingPlayers().getSquadronMemberList().get(0);
        PlayerDeclarationsBuilder  declarationsBuilder = new PlayerDeclarationsBuilder();
        playerDeclarations = declarationsBuilder.makePlayerDeclarations(player, "Nieuport 17.C1", "Farman F.40");
    }

    private void makeMissionLogEvents() throws PWCGException
    {
        FCMissionLogEventsBuilder missionLogEventsBuilder = new FCMissionLogEventsBuilder(campaign, 
                aarCoordinator.getAarContext().getPreliminaryData(), expectedResults);
        LogEventData missionLogRawData = missionLogEventsBuilder.makeLogEvents();
        aarCoordinator.getAarContext().setLogEventData(missionLogRawData);
    }
}
