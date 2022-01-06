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
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

public class MissionGroundUnitStalingradTest
{
    public MissionGroundUnitStalingradTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
    }
    
    @Test
    public void verifySmallerDistanceToFront () throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.GROSS_DEUTSCHLAND_PROFILE);
        FrontMapIdentifier map = PWCGContext.getInstance().getCurrentMap().getMapIdentifier();
        assert(map == FrontMapIdentifier.STALINGRAD_MAP);

        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeTestSingleMissionFromFlightType(
                TestMissionBuilderUtility.buildTestParticipatingHumans(campaign), FlightTypes.GROUND_ATTACK, MissionProfile.DAY_TACTICAL_MISSION);

        verifyAllied(mission);
        verifyAxis(mission);
    }

    private void verifyAllied(Mission mission) throws PWCGException
    {
        Side side = Side.ALLIED;
        List<TargetType> availableGroundUnitTypes = mission.getGroundUnitBuilder().getAvailableGroundUnitTargetTypesForMissionForSide(side);
        
        List<TargetType> expectedGroundUnitTypes = Arrays.asList(TargetType.TARGET_INFANTRY, TargetType.TARGET_TRANSPORT, TargetType.TARGET_TRAIN, TargetType.TARGET_DRIFTER);
        boolean allExist = validateExpectedGroundUnits(side , availableGroundUnitTypes, expectedGroundUnitTypes);
        assert(allExist);
        
        List<TargetType> unexpectedGroundUnitTypes = Arrays.asList(TargetType.TARGET_SHIPPING, TargetType.TARGET_BALLOON);
        boolean allDoNotExist = validateUnexpectedGroundUnits(side , availableGroundUnitTypes, unexpectedGroundUnitTypes);
        assert(allDoNotExist);
    }

    private void verifyAxis(Mission mission) throws PWCGException
    {
        Side side = Side.AXIS;
        List<TargetType> availableGroundUnitTypes = mission.getGroundUnitBuilder().getAvailableGroundUnitTargetTypesForMissionForSide(side);
        
        List<TargetType> expectedGroundUnitTypes = Arrays.asList(TargetType.TARGET_INFANTRY, TargetType.TARGET_TRANSPORT, TargetType.TARGET_TRAIN);
        boolean allExist = validateExpectedGroundUnits(side , availableGroundUnitTypes, expectedGroundUnitTypes);
        assert(allExist);
        
        List<TargetType> unexpectedGroundUnitTypes = Arrays.asList(TargetType.TARGET_SHIPPING, TargetType.TARGET_BALLOON, TargetType.TARGET_DRIFTER);
        boolean allDoNotExist = validateUnexpectedGroundUnits(side , availableGroundUnitTypes, unexpectedGroundUnitTypes);
        assert(allDoNotExist);
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
