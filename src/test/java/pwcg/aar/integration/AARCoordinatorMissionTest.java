package pwcg.aar.integration;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.AARCoordinator;
import pwcg.aar.inmission.phase1.parse.AARMissionLogRawData;
import pwcg.aar.inmission.phase3.reconcile.victories.PlayerDeclarations;
import pwcg.aar.prelim.AARPreliminaryData;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.plane.SquadronPlaneAssignment;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CampaignCacheBoS;

@RunWith(MockitoJUnitRunner.class)
public class AARCoordinatorMissionTest
{
    private Campaign campaign;    
    private AARCoordinator aarCoordinator;
    private List<Squadron> squadronsInMission = new ArrayList<>();
    private PlayerDeclarations playerDeclarations;
    private ExpectedResults expectedResults;
    private int playerMissionsFlown = 0;

    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        PWCGContextManager.getInstance().changeContext(FrontMapIdentifier.MOSCOW_MAP);
        campaign = CampaignCache.makeCampaignForceCreation(CampaignCacheBoS.JG_51_PROFILE);
        expectedResults = new ExpectedResults(campaign);
        aarCoordinator = AARCoordinator.getInstance();
        aarCoordinator.reset(campaign);
        
        playerMissionsFlown = campaign.getPlayer().getMissionFlown();
    }

    @Test
    public void runMissionAAR () throws PWCGException
    {
        createArtifacts ();
        aarCoordinator.evaluateInMission(playerDeclarations);
        expectedResults.buildExpectedResultsFromAARContext(aarCoordinator.getAarContext());
        
        AARResultValidator resultValidator = new AARResultValidator(expectedResults);
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
        SquadronForMissionBuilder squadronForMissionBuilder = new SquadronForMissionBuilder(campaign);
        squadronsInMission = squadronForMissionBuilder.makeSquadronsInMission();
    }

    private void makePreliminary() throws PWCGException
    {
        PreliminaryDataBuilderForTest preliminaryDataBuilder = new PreliminaryDataBuilderForTest(campaign, squadronsInMission);
        AARPreliminaryData preliminaryData = preliminaryDataBuilder.makePreliminaryForTestMission();
        aarCoordinator.getAarContext().setPreliminaryData(preliminaryData);
    }

    private void makePlayerDeclarations()
    {
        PlayerDeclarationsBuilder  declarationsBuilder = new PlayerDeclarationsBuilder();
        playerDeclarations = declarationsBuilder.makePlayerDeclarations();
    }

    private void makeMissionLogEvents() throws PWCGException
    {
        MissionLogEventsBuilder missionLogEventsBuilder = new MissionLogEventsBuilder(campaign, 
                aarCoordinator.getAarContext().getPreliminaryData(), expectedResults);
        AARMissionLogRawData missionLogRawData = missionLogEventsBuilder.makeLogEvents();
        aarCoordinator.getAarContext().setMissionLogRawData(missionLogRawData);
    }
    
    public static SquadronPlaneAssignment getPlaneForSquadron(int SquadronId) throws PWCGException
    {
        Squadron squadron = PWCGContextManager.getInstance().getSquadronManager().getSquadron(SquadronId);
        List<SquadronPlaneAssignment> squadronPlaneAssignments = squadron.getPlaneAssignments();
        return squadronPlaneAssignments.get(0);
    }

}
