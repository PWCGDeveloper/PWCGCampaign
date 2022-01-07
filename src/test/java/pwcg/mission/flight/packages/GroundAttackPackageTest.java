package pwcg.mission.flight.packages;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.utils.TestDriver;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.mission.Mission;
import pwcg.mission.MissionHumanParticipants;
import pwcg.mission.MissionSquadronFlightTypes;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.target.TargetType;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GroundAttackPackageTest
{
    private Campaign campaign;
     
    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.GROSS_DEUTSCHLAND_PROFILE);
    }

    @Test
    public void groundAttackInfantryTargetTest() throws PWCGException
    {
        TestDriver.getInstance().enableTestDriver();
        TestDriver.getInstance().setTestPlayerTacticalTargetType(TargetType.TARGET_INFANTRY);
        
        buildFlight(campaign);
    }

    private void buildFlight(Campaign campaign) throws PWCGException
    {
        MissionHumanParticipants participatingPlayers = TestMissionBuilderUtility.buildTestParticipatingHumans(campaign);

        CoordinateBox missionBorders = CoordinateBox.coordinateBoxFromCenter(new Coordinate(150000,  0, 150000), 100000);

        Mission mission = TestMissionBuilderUtility.createTestMission(campaign, participatingPlayers, missionBorders);
        mission.generate();

        campaign.setCurrentMission(mission);
    }
}
