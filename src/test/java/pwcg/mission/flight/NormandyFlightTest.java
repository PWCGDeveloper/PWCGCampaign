package pwcg.mission.flight;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.skirmish.SkirmishProfileType;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.target.TargetType;
import pwcg.mission.utils.MissionFlightValidator;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.FlightTypeFinder;
import pwcg.testutils.GroundUnitTypeFinder;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

public class NormandyFlightTest
{
    public NormandyFlightTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
    }

    @Test
    public void buildUSEarlyTest() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.USAAF_NORMANDY);
        campaign.setDate(DateUtils.getDateYYYYMMDD("19430601"));
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        mission.finalizeMission();

        Assertions.assertTrue(mission.getSkirmish() == null);

        MissionFlightValidator.validateMission(mission);
    }

    @Test
    public void buildLWEarly() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.LW_BOB_PROFILE);
        campaign.setDate(DateUtils.getDateYYYYMMDD("19410601"));
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        mission.finalizeMission();

        Assertions.assertTrue(mission.getSkirmish() != null);

        MissionFlightValidator.validateMission(mission);
    }

    @Test
    public void dunkirkTest() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.RAF_BOB_PROFILE);
        campaign.setDate(DateUtils.getDateYYYYMMDD("19410601"));
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        mission.finalizeMission();

        Assertions.assertTrue(mission.getSkirmish() != null);
        if (mission.getSkirmish().getProfileType() == SkirmishProfileType.SKIRMISH_PROFILE_ANTI_SHIPPING)
        {
            Assertions.assertTrue(GroundUnitTypeFinder.hasGroundUnitType(mission, TargetType.TARGET_SHIPPING));
            Assertions.assertTrue(FlightTypeFinder.hasFlightWithTargetType(mission, TargetType.TARGET_SHIPPING));
        }
        else
        {
            Assertions.assertTrue(GroundUnitTypeFinder.hasGroundUnitType(mission, TargetType.TARGET_INFANTRY));
            Assertions.assertTrue(FlightTypeFinder.hasFlightWithTargetType(mission, TargetType.TARGET_INFANTRY));
        }

        MissionFlightValidator.validateMission(mission);
    }

    @Test
    public void bobPauseTest() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.RAF_BOB_PROFILE);
        campaign.setDate(DateUtils.getDateYYYYMMDD("19410612"));
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        mission.finalizeMission();

        Assertions.assertTrue(mission.getSkirmish() == null);
        Assertions.assertFalse(GroundUnitTypeFinder.hasGroundUnitType(mission, TargetType.TARGET_INFANTRY));
        Assertions.assertFalse(FlightTypeFinder.hasFlightWithTargetType(mission, TargetType.TARGET_INFANTRY));

        MissionFlightValidator.validateMission(mission);
    }

    @Test
    public void bobShippingTest() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.LW_BOB_PROFILE);
        campaign.setDate(DateUtils.getDateYYYYMMDD("19410718"));
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        mission.finalizeMission();

        Assertions.assertTrue(mission.getSkirmish() != null);
        Assertions.assertEquals(SkirmishProfileType.SKIRMISH_PROFILE_ANTI_SHIPPING, mission.getSkirmish().getProfileType());
        Assertions.assertTrue(GroundUnitTypeFinder.hasGroundUnitType(mission, TargetType.TARGET_SHIPPING));
        Assertions.assertTrue(FlightTypeFinder.hasFlightWithTargetType(mission, TargetType.TARGET_SHIPPING));

        MissionFlightValidator.validateMission(mission);
    }

    @Test
    public void bobAirfieldTest() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.LW_BOB_PROFILE);
        campaign.setDate(DateUtils.getDateYYYYMMDD("19410820"));
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        mission.finalizeMission();

        Assertions.assertTrue(mission.getSkirmish() != null);
        Assertions.assertEquals(SkirmishProfileType.SKIRMISH_PROFILE_AIRFIELD, mission.getSkirmish().getProfileType());
        Assertions.assertTrue(FlightTypeFinder.hasFlightWithTargetType(mission, TargetType.TARGET_AIRFIELD));

        MissionFlightValidator.validateMission(mission);
    }

    @Test
    public void bobCityTest() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.LW_BOB_PROFILE);
        campaign.setDate(DateUtils.getDateYYYYMMDD("19410920"));
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        mission.finalizeMission();

        Assertions.assertTrue(mission.getSkirmish() != null);
        Assertions.assertEquals(SkirmishProfileType.SKIRMISH_PROFILE_CARPET_BOMB, mission.getSkirmish().getProfileType());
        Assertions.assertTrue(FlightTypeFinder.hasFlightWithTargetType(mission, TargetType.TARGET_CITY));

        MissionFlightValidator.validateMission(mission);
    }

    @Test
    public void dieppeTest() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.RAF_BOB_PROFILE);
        campaign.setDate(DateUtils.getDateYYYYMMDD("19420819"));
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        mission.finalizeMission();

        Assertions.assertTrue(mission.getSkirmish() != null);
        Assertions.assertEquals(SkirmishProfileType.SKIRMISH_PROFILE_INVASION, mission.getSkirmish().getProfileType());
        Assertions.assertTrue(GroundUnitTypeFinder.hasGroundUnitType(mission, TargetType.TARGET_INFANTRY) ||
                FlightTypeFinder.hasFlightWithTargetType(mission, TargetType.TARGET_ARMOR) ||
                FlightTypeFinder.hasFlightWithTargetType(mission, TargetType.TARGET_SHIPPING));
        Assertions.assertTrue(FlightTypeFinder.hasFlightType(mission, FlightTypes.GROUND_ATTACK) ||
                FlightTypeFinder.hasFlightType(mission, FlightTypes.ANTI_SHIPPING));

        MissionFlightValidator.validateMission(mission);
    }

    @Test
    public void usEntersTest() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.USAAF_NORMANDY);
        campaign.setDate(DateUtils.getDateYYYYMMDD("19430701"));
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        mission.finalizeMission();

        Assertions.assertTrue(mission.getSkirmish() == null);
        Assertions.assertFalse(GroundUnitTypeFinder.hasGroundUnitType(mission, TargetType.TARGET_INFANTRY));
        Assertions.assertFalse(FlightTypeFinder.hasFlightWithTargetType(mission, TargetType.TARGET_INFANTRY) ||
                FlightTypeFinder.hasFlightWithTargetType(mission, TargetType.TARGET_ARMOR));

        MissionFlightValidator.validateMission(mission);
    }

    @Test
    public void preNormandyTest() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.USAAF_NORMANDY);
        campaign.setDate(DateUtils.getDateYYYYMMDD("19440510"));
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        mission.finalizeMission();

        Assertions.assertTrue(mission.getSkirmish() == null);
        Assertions.assertFalse(GroundUnitTypeFinder.hasGroundUnitType(mission, TargetType.TARGET_INFANTRY));
        Assertions.assertFalse(FlightTypeFinder.hasFlightWithTargetType(mission, TargetType.TARGET_INFANTRY) ||
                FlightTypeFinder.hasFlightWithTargetType(mission, TargetType.TARGET_ARMOR));

        MissionFlightValidator.validateMission(mission);
    }

    @Test
    public void ddayTest() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.USAAF_NORMANDY);
        campaign.setDate(DateUtils.getDateYYYYMMDD("19440606"));
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        mission.finalizeMission();

        Assertions.assertTrue(mission.getSkirmish() != null);
        Assertions.assertEquals(SkirmishProfileType.SKIRMISH_PROFILE_INVASION, mission.getSkirmish().getProfileType());
        Assertions.assertTrue(GroundUnitTypeFinder.hasGroundUnitType(mission, TargetType.TARGET_INFANTRY) ||
                FlightTypeFinder.hasFlightWithTargetType(mission, TargetType.TARGET_ARMOR) ||
                FlightTypeFinder.hasFlightWithTargetType(mission, TargetType.TARGET_SHIPPING));
        Assertions.assertTrue(FlightTypeFinder.hasFlightWithTargetType(mission, TargetType.TARGET_INFANTRY) ||
                FlightTypeFinder.hasFlightWithTargetType(mission, TargetType.TARGET_ARMOR) ||
                FlightTypeFinder.hasFlightWithTargetType(mission, TargetType.TARGET_SHIPPING));

        MissionFlightValidator.validateMission(mission);
    }

    @Test
    public void cherbourgTest() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.USAAF_NORMANDY);
        campaign.setDate(DateUtils.getDateYYYYMMDD("19440701"));
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        mission.finalizeMission();

        Assertions.assertTrue(mission.getSkirmish() != null);
        Assertions.assertEquals(SkirmishProfileType.SKIRMISH_PROFILE_ANTI_INFANTRY, mission.getSkirmish().getProfileType());
        Assertions.assertTrue(GroundUnitTypeFinder.hasGroundUnitType(mission, TargetType.TARGET_INFANTRY));
        Assertions.assertTrue(FlightTypeFinder.hasFlightWithTargetType(mission, TargetType.TARGET_INFANTRY) ||
                FlightTypeFinder.hasFlightWithTargetType(mission, TargetType.TARGET_ARMOR));

        MissionFlightValidator.validateMission(mission);
    }

    @Test
    public void mortainTest() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.USAAF_NORMANDY);
        campaign.setDate(DateUtils.getDateYYYYMMDD("19440810"));
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        mission.finalizeMission();

        Assertions.assertTrue(mission.getSkirmish() != null);
        Assertions.assertEquals(SkirmishProfileType.SKIRMISH_PROFILE_ANTI_INFANTRY, mission.getSkirmish().getProfileType());
        Assertions.assertTrue(GroundUnitTypeFinder.hasGroundUnitType(mission, TargetType.TARGET_INFANTRY));
        Assertions.assertTrue(FlightTypeFinder.hasFlightWithTargetType(mission, TargetType.TARGET_INFANTRY) ||
                FlightTypeFinder.hasFlightWithTargetType(mission, TargetType.TARGET_ARMOR));

        MissionFlightValidator.validateMission(mission);
    }

    @Test
    public void falaiseTest() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.USAAF_NORMANDY);
        campaign.setDate(DateUtils.getDateYYYYMMDD("19440810"));
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        mission.finalizeMission();

        Assertions.assertTrue(mission.getSkirmish() != null);
        Assertions.assertEquals(SkirmishProfileType.SKIRMISH_PROFILE_ANTI_INFANTRY, mission.getSkirmish().getProfileType());
        Assertions.assertTrue(GroundUnitTypeFinder.hasGroundUnitType(mission, TargetType.TARGET_INFANTRY));
        Assertions.assertTrue(FlightTypeFinder.hasFlightWithTargetType(mission, TargetType.TARGET_INFANTRY) ||
                FlightTypeFinder.hasFlightWithTargetType(mission, TargetType.TARGET_ARMOR));

        MissionFlightValidator.validateMission(mission);
    }
}
