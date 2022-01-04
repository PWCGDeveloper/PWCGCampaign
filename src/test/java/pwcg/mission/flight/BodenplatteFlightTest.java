package pwcg.mission.flight;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.core.utils.DateUtils;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.target.TargetType;
import pwcg.mission.utils.MissionFlightValidator;
import pwcg.mission.utils.MissionInformationUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

public class BodenplatteFlightTest
{
    public BodenplatteFlightTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
    }

    @Test
    public void hasSkirmishAndAirfieldAttackForGermanTest() throws PWCGException
    {        
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_26_PROFILE_WEST);
        campaign.setDate(DateUtils.getDateYYYYMMDD("19450101"));
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        mission.finalizeMission();

        Assertions.assertTrue (mission.getSkirmish() != null);

        assert(MissionInformationUtils.verifyFlightTypeInMission(mission, FlightTypes.GROUND_ATTACK, Side.AXIS));
        assert(MissionInformationUtils.verifyFlightTypeInMission(mission, FlightTypes.SCRAMBLE, Side.ALLIED));
        assert(MissionInformationUtils.verifyFlightTargets(mission, FlightTypes.GROUND_ATTACK, TargetType.TARGET_AIRFIELD, Side.AXIS));
        assert(MissionInformationUtils.verifyFlightTargets(mission, FlightTypes.BOMB, TargetType.TARGET_AIRFIELD, Side.AXIS));
        MissionFlightValidator.validateMission(mission);
    }

    @Test
    public void hasAttackOnOphovenTest() throws PWCGException
    {        
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.FG_354_BODENPLATTE_PROFILE);
        campaign.setDate(DateUtils.getDateYYYYMMDD("19450101"));
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        mission.finalizeMission();

        Assertions.assertTrue (mission.getSkirmish() != null);
        Assertions.assertTrue (mission.getSkirmish().getSkirmishName().contentEquals("Bodenplatte-Ophoven"));

        CoordinateBox missionBox = mission.getMissionBorders();
        Company playerSquadron = PWCGContext.getInstance().getCompanyManager().getCompany(campaign.getReferencePlayer().getCompanyId());
        Coordinate playerLocation = playerSquadron.determineCurrentPosition(campaign.getDate());
        assert(missionBox.isInBox(playerLocation));

        assert(MissionInformationUtils.verifyFlightTypeInMission(mission, FlightTypes.GROUND_ATTACK, Side.AXIS));
        assert(MissionInformationUtils.verifyFlightTypeInMission(mission, FlightTypes.SCRAMBLE, Side.ALLIED));
        assert(MissionInformationUtils.verifyFlightTargets(mission, FlightTypes.GROUND_ATTACK, TargetType.TARGET_AIRFIELD, Side.AXIS));
        assert(MissionInformationUtils.verifyFlightTargets(mission, FlightTypes.BOMB, TargetType.TARGET_AIRFIELD, Side.AXIS));
        MissionFlightValidator.validateMission(mission);
    }

    @Test
    public void hasAttackOnVolkelTest() throws PWCGException
    {        
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.RAF_326_BODENPLATTE_PROFILE);
        campaign.setDate(DateUtils.getDateYYYYMMDD("19450101"));
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        mission.finalizeMission();

        Assertions.assertTrue (mission.getSkirmish() != null);
        Assertions.assertTrue (mission.getSkirmish().getSkirmishName().contentEquals("Bodenplatte-Volkel"));

        CoordinateBox missionBox = mission.getMissionBorders();
        Company playerSquadron = PWCGContext.getInstance().getCompanyManager().getCompany(campaign.getReferencePlayer().getCompanyId());
        Coordinate playerLocation = playerSquadron.determineCurrentPosition(campaign.getDate());
        assert(missionBox.isInBox(playerLocation));

        assert(MissionInformationUtils.verifyFlightTypeInMission(mission, FlightTypes.GROUND_ATTACK, Side.AXIS));
        assert(MissionInformationUtils.verifyFlightTypeInMission(mission, FlightTypes.SCRAMBLE, Side.ALLIED));
        assert(MissionInformationUtils.verifyFlightTargets(mission, FlightTypes.GROUND_ATTACK, TargetType.TARGET_AIRFIELD, Side.AXIS));
        assert(MissionInformationUtils.verifyFlightTargets(mission, FlightTypes.BOMB, TargetType.TARGET_AIRFIELD, Side.AXIS));
        MissionFlightValidator.validateMission(mission);
    }
    
    
    
}
