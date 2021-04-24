package pwcg.mission.flight;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.skirmish.DynamicSkirmishBuilder;
import pwcg.campaign.skirmish.Skirmish;
import pwcg.campaign.squadmember.SquadronMember;
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

public class KubanShippingZoneTest
{
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
    }

    @Test
    public void hasDiveBombShippingZoneTest() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.STG77_KUBAN_PROFILE);
        campaign.setDate(DateUtils.getDateYYYYMMDD("19431009"));
        
        MissionHumanParticipants playerParticipants = TestMissionBuilderUtility.buildTestParticipatingHumans(campaign);
                
        DynamicSkirmishBuilder dynamicSkirmishBuilder = new DynamicSkirmishBuilder(campaign, playerParticipants);
        Skirmish shippingZoneSkirmish = dynamicSkirmishBuilder.buildSkirmishForShippingEncounter();
                
        MissionSquadronFlightTypes playerFlightTypes = new MissionSquadronFlightTypes();
        for (SquadronMember player : playerParticipants.getAllParticipatingPlayers())
        {
            playerFlightTypes.add(player.determineSquadron(), FlightTypes.DIVE_BOMB);
        }

        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeTestMissionFromFlightTypeWithSkirmish(playerParticipants, playerFlightTypes, MissionProfile.DAY_TACTICAL_MISSION, shippingZoneSkirmish);

        assert (mission.getSkirmish() != null);
        assert (mission.getSkirmish().getSkirmishName().startsWith("Ship Encounter"));
        boolean axisShipsFound = false;
        boolean alliedShipsFound = false;
        for (GroundUnitCollection groundUnitCollection : mission.getMissionGroundUnitBuilder().getAllMissionGroundUnits())
        {
            if (groundUnitCollection.getTargetType() == TargetType.TARGET_SHIPPING)
            {
                if (groundUnitCollection.getGroundUnits().get(0).getCountry().getSide() == Side.ALLIED)
                {
                    alliedShipsFound = true;
                }
                else
                {
                    axisShipsFound = true;
                }
            }
        }
        assert (alliedShipsFound);
        assert (axisShipsFound);

        boolean diveBombFlightFound = MissionInformationUtils.verifyFlightTypeInMission(mission, FlightTypes.DIVE_BOMB, Side.AXIS);
        boolean groundAttackFlightFound = MissionInformationUtils.verifyFlightTypeInMission(mission, FlightTypes.GROUND_ATTACK, Side.AXIS);
        assert (diveBombFlightFound || groundAttackFlightFound);

        boolean diveBombFlightTargetFound = MissionInformationUtils.verifyFlightTargets(mission, FlightTypes.DIVE_BOMB, TargetType.TARGET_SHIPPING, Side.AXIS);
        boolean groundAttackFlightTargetFound = MissionInformationUtils.verifyFlightTargets(mission, FlightTypes.GROUND_ATTACK, TargetType.TARGET_SHIPPING, Side.AXIS);
        assert (diveBombFlightTargetFound || groundAttackFlightTargetFound);

        MissionFlightValidator.validateMission(mission);
    }

    @Test
    public void doesNotHaveSkirmishBeforeTest() throws PWCGException
    {
        noSkirmish(DateUtils.getDateYYYYMMDD("19431007"));
    }

    private void noSkirmish(Date date) throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.STG77_KUBAN_PROFILE);
        campaign.setDate(date);
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        
        assert (mission.getSkirmish() == null);
        
        MissionFlightValidator.validateMission(mission);
    }
}
