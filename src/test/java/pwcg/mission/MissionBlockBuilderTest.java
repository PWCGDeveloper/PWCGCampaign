package pwcg.mission;

import org.junit.jupiter.api.Test;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.group.Block;
import pwcg.campaign.group.ScriptedFixedPosition;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.ground.building.PwcgBuildingIdentifier;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

public class MissionBlockBuilderTest
{
    public MissionBlockBuilderTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
    }

    @Test
    public void verifyBlocksIncludedTest() throws PWCGException
    {

        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_26_PROFILE_WEST);
        campaign.setDate(DateUtils.getDateYYYYMMDD("19450101"));
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        mission.finalizeMission();
        
        boolean airfieldStructuresFound = false;
        for (ScriptedFixedPosition blockForPatrol : mission.getMissionBlocks().getAllStructuresForMission())
        {
            if (blockForPatrol instanceof Block)
            {
                Block block = (Block)blockForPatrol;
                if (PwcgBuildingIdentifier.isAirfield(block))
                {
                    airfieldStructuresFound = true;
                }
            }
        }
        
        assert(airfieldStructuresFound);
    }
}
