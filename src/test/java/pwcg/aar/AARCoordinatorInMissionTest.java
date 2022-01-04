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
import pwcg.campaign.company.Company;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.plane.SquadronPlaneAssignment;
import pwcg.core.exception.PWCGException;
import pwcg.core.logfiles.LogEventData;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AARCoordinatorInMissionTest
{
    private Campaign campaign;    
    private static AARCoordinator aarCoordinator;
    private static ExpectedResults expectedResults;
    private static int playerMissionsFlown = 0;

    private List<Company> squadronsInMission = new ArrayList<>();
    private Map<Integer, PlayerDeclarations> playerDeclarations;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        PWCGContext.getInstance().changeContext(FrontMapIdentifier.MOSCOW_MAP);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_MOSCOW);
        expectedResults = new ExpectedResults(campaign);
        aarCoordinator = AARCoordinator.getInstance();
        aarCoordinator.reset(campaign);
        
        playerMissionsFlown = campaign.getPersonnelManager().getPlayersInMission().getCrewMemberList().get(0).getBattlesFought();
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
        CrewMember player = campaign.getPersonnelManager().getPlayersInMission().getCrewMemberList().get(0);
        PlayerDeclarationsBuilder  declarationsBuilder = new PlayerDeclarationsBuilder();
        playerDeclarations = declarationsBuilder.makePlayerDeclarations(player);
    }

    private void makeMissionLogEvents() throws PWCGException
    {
        MissionLogEventsBuilder missionLogEventsBuilder = new MissionLogEventsBuilder(campaign, 
                aarCoordinator.getAarContext().getPreliminaryData(), expectedResults);
        LogEventData missionLogRawData = missionLogEventsBuilder.makeLogEvents();
        aarCoordinator.getAarContext().setLogEventData(missionLogRawData);
    }
    
    public static CompanyTankAssignment getPlaneForSquadron(int SquadronId) throws PWCGException
    {
        Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(SquadronId);
        List<CompanyTankAssignment> squadronPlaneAssignments = squadron.getPlaneAssignments();
        return squadronPlaneAssignments.get(0);
    }

}
