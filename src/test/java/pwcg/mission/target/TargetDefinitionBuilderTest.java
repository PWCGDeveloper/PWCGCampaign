package pwcg.mission.target;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.PwcgRole;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.squadron.SquadronRolePeriod;
import pwcg.campaign.squadron.SquadronRoleSet;
import pwcg.campaign.squadron.SquadronRoleWeight;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.flight.IFlight;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

@RunWith(MockitoJUnitRunner.class)
public class TargetDefinitionBuilderTest
{
    @Before
    public void setup()
    {
    }
    
    @Test
    public void randomTargetTypeTest() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.FG_362_PROFILE);

        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        
        IFlight playerFlight = mission.getMissionFlights().getPlayerFlights().get(0);

        TargetDefinitionBuilder targetDefinitionBuilder = new TargetDefinitionBuilder(playerFlight.getFlightInformation()); 
        TargetDefinition targetDefinition =  targetDefinitionBuilder.buildTargetDefinition();
        assert(targetDefinition.getTargetType() != TargetType.TARGET_NONE);
    }
    
    @Test
    public void tankBustTargetTypeTest() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_26_PROFILE_WEST);
        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(SquadronTestProfile.JG_26_PROFILE_WEST.getSquadronId());
        
        SquadronRoleWeight squadronRoleWeight = new SquadronRoleWeight();
        squadronRoleWeight.setRole(PwcgRole.ROLE_TANK_BUSTER);
        squadronRoleWeight.setWeight(100);
        
        SquadronRolePeriod squadronRolePeriod = new SquadronRolePeriod();
        squadronRolePeriod.setStartDate(DateUtils.getDateYYYYMMDD("19400101"));
        squadronRolePeriod.setWeightedRoles(Arrays.asList(squadronRoleWeight));

        SquadronRoleSet squadronRoleSet = squadron.getSquadronRoles();
        squadronRoleSet.overrideRolesForTest(Arrays.asList(squadronRolePeriod));

        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        
        IFlight playerFlight = mission.getMissionFlights().getPlayerFlights().get(0);

        TargetDefinitionBuilder targetDefinitionBuilder = new TargetDefinitionBuilder(playerFlight.getFlightInformation()); 
        TargetDefinition targetDefinition =  targetDefinitionBuilder.buildTargetDefinition();
        assert(targetDefinition.getTargetType() == TargetType.TARGET_ARMOR);
    }
    
    @Test
    public void trainBustTargetTypeTest() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.FG_362_PROFILE);
        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(SquadronTestProfile.FG_362_PROFILE.getSquadronId());
        
        SquadronRoleWeight squadronRoleWeight = new SquadronRoleWeight();
        squadronRoleWeight.setRole(PwcgRole.ROLE_TRAIN_BUSTER);
        squadronRoleWeight.setWeight(100);
        
        SquadronRolePeriod squadronRolePeriod = new SquadronRolePeriod();
        squadronRolePeriod.setStartDate(DateUtils.getDateYYYYMMDD("19400101"));
        squadronRolePeriod.setWeightedRoles(Arrays.asList(squadronRoleWeight));

        SquadronRoleSet squadronRoleSet = squadron.getSquadronRoles();
        squadronRoleSet.overrideRolesForTest(Arrays.asList(squadronRolePeriod));

        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        
        IFlight playerFlight = mission.getMissionFlights().getPlayerFlights().get(0);

        TargetDefinitionBuilder targetDefinitionBuilder = new TargetDefinitionBuilder(playerFlight.getFlightInformation()); 
        TargetDefinition targetDefinition =  targetDefinitionBuilder.buildTargetDefinition();
        assert(targetDefinition.getTargetType() == TargetType.TARGET_TRAIN);
    }
    
    @Test
    public void antiShippingTargetTypeTest() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.STG77_KUBAN_PROFILE);
        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(SquadronTestProfile.STG77_KUBAN_PROFILE.getSquadronId());
        
        SquadronRoleWeight squadronRoleWeight = new SquadronRoleWeight();
        squadronRoleWeight.setRole(PwcgRole.ROLE_ANTI_SHIPPING);
        squadronRoleWeight.setWeight(100);
        
        SquadronRolePeriod squadronRolePeriod = new SquadronRolePeriod();
        squadronRolePeriod.setStartDate(DateUtils.getDateYYYYMMDD("19400101"));
        squadronRolePeriod.setWeightedRoles(Arrays.asList(squadronRoleWeight));

        SquadronRoleSet squadronRoleSet = squadron.getSquadronRoles();
        squadronRoleSet.overrideRolesForTest(Arrays.asList(squadronRolePeriod));

        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        
        IFlight playerFlight = mission.getMissionFlights().getPlayerFlights().get(0);

        TargetDefinitionBuilder targetDefinitionBuilder = new TargetDefinitionBuilder(playerFlight.getFlightInformation()); 
        TargetDefinition targetDefinition =  targetDefinitionBuilder.buildTargetDefinition();
        assert(targetDefinition.getTargetType() == TargetType.TARGET_SHIPPING);
    }
}
