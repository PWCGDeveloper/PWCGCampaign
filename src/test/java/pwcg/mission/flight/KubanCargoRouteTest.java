package pwcg.mission.flight;

import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.skirmish.DynamicSkirmishBuilder;
import pwcg.campaign.skirmish.Skirmish;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.MissionHumanParticipants;
import pwcg.mission.MissionProfile;
import pwcg.mission.MissionSquadronFlightTypes;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.target.TargetType;
import pwcg.mission.utils.MissionFlightValidator;
import pwcg.mission.utils.MissionInformationUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class KubanCargoRouteTest
{
    private Campaign campaign;    

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.STG77_KUBAN_PROFILE);
    }

    @Test
    public void hasDiveBombCargoRouteTest() throws PWCGException
    {
        campaign.setDate(DateUtils.getDateYYYYMMDD("19431009"));
        
        MissionHumanParticipants playerParticipants = TestMissionBuilderUtility.buildTestParticipatingHumans(campaign);
                
        DynamicSkirmishBuilder dynamicSkirmishBuilder = new DynamicSkirmishBuilder(campaign, playerParticipants);
        Skirmish cargoRouteSkirmish = dynamicSkirmishBuilder.buildSkirmishForCargoRoute();
                
        MissionSquadronFlightTypes playerFlightTypes = new MissionSquadronFlightTypes();
        for (CrewMember player : playerParticipants.getAllParticipatingPlayers())
        {
            playerFlightTypes.add(player.determineSquadron(), FlightTypes.DIVE_BOMB);
        }

        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeTestMissionFromFlightTypeWithSkirmish(playerParticipants, playerFlightTypes, MissionProfile.DAY_TACTICAL_MISSION, cargoRouteSkirmish);

        Assertions.assertTrue (mission.getSkirmish() != null);
        Assertions.assertTrue (mission.getSkirmish().getSkirmishName().startsWith("Cargo"));
        Assertions.assertTrue (mission.getSkirmish().getAttackerAir() == Side.AXIS);
        boolean shipsFound = false;
        for (GroundUnitCollection groundUnitCollection : mission.getGroundUnitBuilder().getAllMissionGroundUnits())
        {
            if (groundUnitCollection.getTargetType() == TargetType.TARGET_SHIPPING)
            {
                shipsFound = true;
            }
        }
        Assertions.assertTrue (shipsFound);

        boolean diveBombFlightFound = MissionInformationUtils.verifyFlightTypeInMission(mission, FlightTypes.DIVE_BOMB, Side.AXIS);
        boolean groundAttackFlightFound = MissionInformationUtils.verifyFlightTypeInMission(mission, FlightTypes.GROUND_ATTACK, Side.AXIS);
        Assertions.assertTrue (diveBombFlightFound || groundAttackFlightFound);

        boolean diveBombFlightTargetFound = MissionInformationUtils.verifyFlightTargets(mission, FlightTypes.DIVE_BOMB, TargetType.TARGET_SHIPPING, Side.AXIS);
        boolean groundAttackFlightTargetFound = MissionInformationUtils.verifyFlightTargets(mission, FlightTypes.GROUND_ATTACK, TargetType.TARGET_SHIPPING, Side.AXIS);
        Assertions.assertTrue (diveBombFlightTargetFound || groundAttackFlightTargetFound);

        MissionFlightValidator.validateMission(mission);        
    }

    @Test
    public void doesNotHaveSkirmishBeforeTest() throws PWCGException
    {
        noSkirmish(DateUtils.getDateYYYYMMDD("19431007"));
    }

    private void noSkirmish(Date date) throws PWCGException
    {
        campaign.setDate(date);
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        
        Assertions.assertTrue (mission.getSkirmish() == null);
        
        MissionFlightValidator.validateMission(mission);
    }
}
