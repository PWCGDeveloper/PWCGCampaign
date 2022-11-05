package pwcg.aar.outofmission.phase1.elapsedtime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.data.AARContext;
import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadmember.AirToGroundVictoryBuilder;
import pwcg.campaign.squadmember.GroundVictimGenerator;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.campaign.squadmember.Victory;
import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OutOfMissionGroundVictoryBuilderTest
{
    private Campaign campaign;
    private static SquadronMember squadronMember;

    @Mock private AARContext aarContext;
    @Mock private ArmedService service;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_STALINGRAD);
        
        for (SquadronMember pilot : campaign.getPersonnelManager().getSquadronPersonnel(SquadronTestProfile.JG_51_PROFILE_STALINGRAD.getSquadronId()).getActiveAiSquadronMembers().getSquadronMemberList())
        {
            if (pilot.getPilotActiveStatus() == SquadronMemberStatus.STATUS_ACTIVE && !pilot.isPlayer())
            {
                squadronMember = pilot;
                break;
            }
        }
    }

    @Test
    public void testVictoryAwarded () throws PWCGException
    {     
        GroundVictimGenerator duringCampaignVictimGenerator = new GroundVictimGenerator(campaign, squadronMember);
        IVehicle victimVehicle = duringCampaignVictimGenerator.generateVictimVehicle();

        AirToGroundVictoryBuilder victoryGenerator = new AirToGroundVictoryBuilder(campaign, squadronMember, victimVehicle);
        Victory victory = victoryGenerator.generateOutOfMissionVictory(campaign.getDate());
        
        Assertions.assertTrue (victory.getVictim().getAirOrGround() == Victory.VEHICLE);
    }
}
