package pwcg.mission.flight.packages;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.SquadronRolePeriod;
import pwcg.campaign.company.SquadronRoleSet;
import pwcg.campaign.company.SquadronRoleWeight;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.plane.PwcgRole;
import pwcg.campaign.utils.TestDriver;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.Mission;
import pwcg.mission.MissionFlights;
import pwcg.mission.MissionGenerator;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.NecessaryFlightType;
import pwcg.mission.target.TargetType;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TargetVicinityValidator;
import pwcg.testutils.TestMissionBuilderUtility;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GroundAttackAntiArmorPackageTest
{
    private Campaign campaign;
     
    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.STG77_KUBAN_PROFILE);
    }
    
    @Test
    public void groundAttackTankBustRoleTest() throws PWCGException
    {        
        Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(SquadronTestProfile.STG77_KUBAN_PROFILE.getCompanyId());
        
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

        MissionFlights missionFlights = mission.getFlights();
        
        IFlight playerFlight = missionFlights.getUnits().get(0);
        assert(playerFlight.getTargetDefinition().getTargetType() == TargetType.TARGET_ARMOR);
        
        List<IFlight> escortFlights = missionFlights.getNecessaryFlightsByType(NecessaryFlightType.PLAYER_ESCORT);
        assert(escortFlights.size() == 1);
        
        List<IFlight> opposingFlights = missionFlights.getNecessaryFlightsByType(NecessaryFlightType.OPPOSING_FLIGHT);
        assert(opposingFlights.size() == 0);
        
        assert(playerFlight.getAssociatedFlight() != null);
        assert(playerFlight.getAssociatedFlight().getFlightInformation().getNecessaryFlightType() == NecessaryFlightType.PLAYER_ESCORT);

        TargetVicinityValidator.verifyProximityToTargetUnit(playerFlight);

        TestDriver.getInstance().reset();
    }
}
