package pwcg.mission;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.target.TargetType;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestCampaignFactoryBuilder;
import pwcg.testutils.TestMissionBuilderUtility;

public class MissionGroundUnitEast1944Test
{    
    public MissionGroundUnitEast1944Test() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
    }
    
    @Test
    public void verifySmallerDistanceToFront () throws PWCGException
    {
        Campaign campaign = TestCampaignFactoryBuilder.makeCampaign(this.getClass().getCanonicalName(), SquadronTestProfile.EAST1944_PROFILE);
        FrontMapIdentifier map = PWCGContext.getInstance().getMap(campaign.getCampaignMap()).getMapIdentifier();
        assert(map == FrontMapIdentifier.EAST1944_MAP);
        
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeTestSingleMissionFromFlightType(
                TestMissionBuilderUtility.buildTestParticipatingHumans(campaign), FlightTypes.GROUND_ATTACK, MissionProfile.DAY_TACTICAL_MISSION);

        List<Side> sides = Arrays.asList(Side.ALLIED, Side.AXIS);
        for (Side side : sides)
        {
            List<TargetType> availableGroundUnitTypes = mission.getGroundUnitBuilder().getAvailableGroundUnitTargetTypesForMissionForSide(side);
            
            List<TargetType> expectedGroundUnitTypes = Arrays.asList(TargetType.TARGET_INFANTRY, TargetType.TARGET_TRANSPORT, TargetType.TARGET_TRAIN);
            boolean allExist = validateExpectedGroundUnits(side , availableGroundUnitTypes, expectedGroundUnitTypes);
            assert(allExist);
            
            List<TargetType> unexpectedGroundUnitTypes = Arrays.asList(TargetType.TARGET_DRIFTER, TargetType.TARGET_SHIPPING, TargetType.TARGET_BALLOON);
            boolean allDoNotExist = validateUnexpectedGroundUnits(side , availableGroundUnitTypes, unexpectedGroundUnitTypes);
            assert(allDoNotExist);
        }
    }
    
    private boolean validateExpectedGroundUnits(Side side, List<TargetType> availableGroundUnitTypes, List<TargetType> expectedGroundUnitTypes)
    {
        boolean allExist = true;
        for (TargetType expectedGroundUnitType : expectedGroundUnitTypes)
        {
            boolean thisTargetExists = false;
            for (TargetType availableGroundUnitType : availableGroundUnitTypes)
            {
                if (expectedGroundUnitType == availableGroundUnitType)
                {
                    thisTargetExists = true;
                }
            }
            
            if (!thisTargetExists)
            {
                allExist = false;
                System.out.println("No target type for side " + side + " Type: " + expectedGroundUnitType);
            }
            
        }
        return allExist;
    }
    
    
    private boolean validateUnexpectedGroundUnits(Side side, List<TargetType> availableGroundUnitTypes, List<TargetType> unexpectedGroundUnitTypes)
    {
        boolean allDoNotExist = true;
        for (TargetType unexpectedGroundUnitType : unexpectedGroundUnitTypes)
        {
            boolean thisTargetExists = false;
            for (TargetType availableGroundUnitType : availableGroundUnitTypes)
            {
                if (unexpectedGroundUnitType == availableGroundUnitType)
                {
                    thisTargetExists = true;
                }
            }
            
            if (thisTargetExists)
            {
                allDoNotExist = false;
                System.out.println("Unexpected target type for side " + side + " Type: " + unexpectedGroundUnitType);
            }
            
        }
        return allDoNotExist;
    }

}
