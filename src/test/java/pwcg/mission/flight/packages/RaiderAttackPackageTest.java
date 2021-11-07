package pwcg.mission.flight.packages;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.utils.TestDriver;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.mission.Mission;
import pwcg.mission.MissionFlights;
import pwcg.mission.MissionHumanParticipants;
import pwcg.mission.MissionProfile;
import pwcg.mission.MissionSquadronFlightTypes;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.NecessaryFlightType;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.missionpoint.MissionPoint;
import pwcg.mission.target.TargetType;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.PwcgTestBase;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

public class RaiderAttackPackageTest extends PwcgTestBase
{
    public RaiderAttackPackageTest() throws PWCGException
    {
        super (PWCGProduct.BOS);
    }

    @Test
    public void raiderAttackAirfieldTargetTest() throws PWCGException
    {
        TestDriver.getInstance().enableTestDriver();
        TestDriver.getInstance().setTestPlayerTacticalTargetType(TargetType.TARGET_AIRFIELD);
        
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.FG_362_PROFILE);
        MissionFlights missionFlights = buildFlight(campaign);
        IFlight playerFlight = missionFlights.getPlayerFlights().get(0);
        assert(playerFlight.getTargetDefinition().getTargetType() == TargetType.TARGET_AIRFIELD);

        List<IFlight> escortFlights = missionFlights.getNecessaryFlightsByType(NecessaryFlightType.PLAYER_ESCORT);
        assert(escortFlights.size() == 0);

        verifyLowAltitude(playerFlight);

        TestDriver.getInstance().reset();
    }

    private MissionFlights buildFlight(Campaign campaign) throws PWCGException
    {
        MissionHumanParticipants participatingPlayers = TestMissionBuilderUtility.buildTestParticipatingHumans(campaign);

        Squadron playerSquadron = participatingPlayers.getAllParticipatingPlayers().get(0).determineSquadron();
        MissionSquadronFlightTypes playerFlightTypes = MissionSquadronFlightTypes.buildPlayerFlightType(FlightTypes.RAID, playerSquadron);

        CoordinateBox missionBorders = CoordinateBox.coordinateBoxFromCenter(new Coordinate(150000,  0, 150000), 100000);

        Mission mission = TestMissionBuilderUtility.createTestMission(campaign, participatingPlayers, missionBorders, MissionProfile.DAY_TACTICAL_MISSION);
        mission.generate(playerFlightTypes);

        campaign.setCurrentMission(mission);
        return mission.getFlights();
    }

    private void verifyLowAltitude(IFlight flight) throws PWCGException
    {
        for (MissionPoint missionPoint : flight.getWaypointPackage().getFlightMissionPoints())
        {
            if (missionPoint.getAction() == WaypointAction.WP_ACTION_TARGET_FINAL || missionPoint.getAction() == WaypointAction.WP_ACTION_ATTACK)
            {
                Assertions.assertTrue (missionPoint.getPosition().getYPos() < 1000);
            }
            else
            {
                Assertions.assertTrue (missionPoint.getPosition().getYPos() < 700);
            }
        }
    }
}
