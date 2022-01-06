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
import pwcg.campaign.crewmember.AirToGroundVictoryBuilder;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.crewmember.GroundVictimGenerator;
import pwcg.campaign.crewmember.Victory;
import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OutOfMissionGroundVictoryBuilderTest
{
    private Campaign campaign;
    private static CrewMember crewMember;

    @Mock private AARContext aarContext;
    @Mock private ArmedService service;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.GROSS_DEUTSCHLAND_PROFILE);
        
        for (CrewMember crewMember : campaign.getPersonnelManager().getCompanyPersonnel(SquadronTestProfile.GROSS_DEUTSCHLAND_PROFILE.getSquadronId()).getActiveAiCrewMembers().getCrewMemberList())
        {
            if (crewMember.getCrewMemberActiveStatus() == CrewMemberStatus.STATUS_ACTIVE && !crewMember.isPlayer())
            {
                crewMember = crewMember;
                break;
            }
        }
    }

    @Test
    public void testVictoryAwarded () throws PWCGException
    {     
        GroundVictimGenerator duringCampaignVictimGenerator = new GroundVictimGenerator(campaign.getDate(), crewMember);
        IVehicle victimVehicle = duringCampaignVictimGenerator.generateVictimVehicle();

        AirToGroundVictoryBuilder victoryGenerator = new AirToGroundVictoryBuilder(crewMember, victimVehicle);
        Victory victory = victoryGenerator.generateOutOfMissionVictory(campaign.getDate());
        
        Assertions.assertTrue (victory.getVictim().getAirOrGround() == Victory.VEHICLE);
    }
}
