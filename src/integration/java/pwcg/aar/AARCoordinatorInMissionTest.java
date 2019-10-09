package pwcg.aar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.inmission.phase1.parse.AARMissionLogRawData;
import pwcg.aar.inmission.phase3.reconcile.victories.singleplayer.PlayerDeclarations;
import pwcg.aar.prelim.AARPreliminaryData;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.plane.SquadronPlaneAssignment;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadrontTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class AARCoordinatorInMissionTest
{
    private Campaign campaign;    
    private AARCoordinator aarCoordinator;
    private List<Squadron> squadronsInMission = new ArrayList<>();
    private Map<Integer, PlayerDeclarations> playerDeclarations;
    private ExpectedResults expectedResults;
    private int playerMissionsFlown = 0;

    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        PWCGContext.getInstance().changeContext(FrontMapIdentifier.MOSCOW_MAP);
        campaign = CampaignCache.makeCampaignForceCreation(SquadrontTestProfile.JG_51_PROFILE_MOSCOW);
        expectedResults = new ExpectedResults(campaign);
        aarCoordinator = AARCoordinator.getInstance();
        aarCoordinator.reset(campaign);
        
        playerMissionsFlown = campaign.getPersonnelManager().getFlyingPlayers().getSquadronMemberList().get(0).getMissionFlown();
    }

    @Test
    public void runMissionAAR () throws PWCGException
    {
        createArtifacts ();
        aarCoordinator.performMissionAAR(playerDeclarations);
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
        TestPreliminaryDataBuilder preliminaryDataBuilder = new TestPreliminaryDataBuilder(campaign, squadronsInMission);
        AARPreliminaryData preliminaryData = preliminaryDataBuilder.makePreliminaryForTestMission();
        aarCoordinator.getAarContext().setPreliminaryData(preliminaryData);
    }

    private void makePlayerDeclarations() throws PWCGException
    {
        SquadronMember player = campaign.getPersonnelManager().getFlyingPlayers().getSquadronMemberList().get(0);
        PlayerDeclarationsBuilder  declarationsBuilder = new PlayerDeclarationsBuilder();
        playerDeclarations = declarationsBuilder.makePlayerDeclarations(player);
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
        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(SquadronId);
        List<SquadronPlaneAssignment> squadronPlaneAssignments = squadron.getPlaneAssignments();
        return squadronPlaneAssignments.get(0);
    }

}
