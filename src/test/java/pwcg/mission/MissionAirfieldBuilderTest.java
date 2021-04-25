package pwcg.mission;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.utils.TestDriver;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.mission.target.TargetType;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

public class MissionAirfieldBuilderTest
{
    @Mock Mission mission;
    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
    }

    @Test
    public void groundAttackInfantryTargetTest() throws PWCGException
    {
        TestDriver.getInstance().setEnabled(true);
        TestDriver.getInstance().setTestPlayerTacticalTargetType(TargetType.TARGET_INFANTRY);
        
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.FG_362_PROFILE);

        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(SquadronTestProfile.FG_362_PROFILE.getSquadronId());
        Coordinate squadronLocation = squadron.determineCurrentPosition(campaign.getDate());
        Mockito.when(mission.getCampaign()).thenReturn(campaign);
        CoordinateBox structureBox = CoordinateBox.coordinateBoxFromCenter(squadronLocation, 40000);

        MissionAirfieldBuilder MissionAirfieldBuilder = new MissionAirfieldBuilder(mission);
    }


}
