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
import pwcg.campaign.skirmish.CargoRouteSkirmishBuilder;
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
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestCampaignFactoryBuilder;
import pwcg.testutils.TestMissionBuilderUtility;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class KubanShippingZoneTest
{
    private Campaign campaign;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = TestCampaignFactoryBuilder.makeCampaign(this.getClass().getCanonicalName(), SquadronTestProfile.STG77_KUBAN_PROFILE);
    }

    @Test
    public void hasDiveBombShippingZoneTest() throws PWCGException
    {
        campaign.setDateWithMapUpdate(DateUtils.getDateYYYYMMDD("19431009"));
        
        MissionHumanParticipants playerParticipants = TestMissionBuilderUtility.buildTestParticipatingHumans(campaign);
                
        CargoRouteSkirmishBuilder dynamicSkirmishBuilder = new CargoRouteSkirmishBuilder(campaign, playerParticipants);
        Skirmish shippingZoneSkirmish = dynamicSkirmishBuilder.buildSkirmishForShippingEncounter();
                
        MissionSquadronFlightTypes playerFlightTypes = new MissionSquadronFlightTypes();
        for (SquadronMember player : playerParticipants.getAllParticipatingPlayers())
        {
            playerFlightTypes.add(player.determineSquadron(), FlightTypes.DIVE_BOMB);
        }

        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMissionFromFlightTypeWithSkirmish(playerParticipants, playerFlightTypes, MissionProfile.DAY_TACTICAL_MISSION, shippingZoneSkirmish);
        mission.finalizeMission();
        
        Assertions.assertTrue (mission.getSkirmish() != null);
        Assertions.assertTrue (mission.getSkirmish().getSkirmishName().startsWith("Ship Encounter"));
        boolean axisShipsFound = false;
        boolean alliedShipsFound = false;
        for (GroundUnitCollection groundUnitCollection : mission.getGroundUnitBuilder().getAllMissionGroundUnits())
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
        Assertions.assertTrue (alliedShipsFound);
        Assertions.assertTrue (axisShipsFound);

        boolean shippingTargetFound = MissionInformationUtils.verifyFlightTargets(mission, TargetType.TARGET_SHIPPING, Side.AXIS);
        Assertions.assertTrue (shippingTargetFound);

        MissionFlightValidator.validateMission(mission);
    }

    @Test
    public void doesNotHaveSkirmishBeforeTest() throws PWCGException
    {
        noSkirmish(DateUtils.getDateYYYYMMDD("19431007"));
    }

    private void noSkirmish(Date date) throws PWCGException
    {
        campaign.setDateWithMapUpdate(date);
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        mission.finalizeMission();

        Assertions.assertTrue (mission.getSkirmish() == null);
        
        MissionFlightValidator.validateMission(mission);
    }
}
