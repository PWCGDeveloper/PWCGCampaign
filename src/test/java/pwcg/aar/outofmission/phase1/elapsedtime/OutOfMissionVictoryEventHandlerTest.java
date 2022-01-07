package pwcg.aar.outofmission.phase1.elapsedtime;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.data.AARContext;
import pwcg.aar.prelim.AARPreliminaryData;
import pwcg.aar.prelim.CampaignMembersOutOfMissionFinder;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.crewmember.Victory;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OutOfMissionVictoryEventHandlerTest
{
    private Campaign campaign;

    @Mock private CrewMember crewMember;    
    @Mock private AARContext aarContext;
    @Mock private AARPreliminaryData preliminaryData;

    private CrewMembers outOfMissionCrewMembers;

    @BeforeEach
    public void setupTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.ESC_103_PROFILE);
        
        Mockito.when(aarContext.getPreliminaryData()).thenReturn(preliminaryData);
        
        outOfMissionCrewMembers = new CrewMembers();
        outOfMissionCrewMembers = CrewMemberFilter.filterActiveAIAndPlayerAndAces(campaign.getPersonnelManager().
                getCompanyPersonnel(SquadronTestProfile.JASTA_16_PROFILE.getCompanyId()).getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());
    }

    @Test
    public void testOutOfMissionVictoriesAwardedForVictories () throws PWCGException
    {     
        try (MockedStatic<CampaignMembersOutOfMissionFinder> mocked = Mockito.mockStatic(CampaignMembersOutOfMissionFinder.class)) 
        {
            mocked.when(() -> CampaignMembersOutOfMissionFinder.getAllCampaignMembersNotInMission(Mockito.any(), Mockito.any())).thenReturn(outOfMissionCrewMembers);
            mocked.when(() -> CampaignMembersOutOfMissionFinder.getActiveCampaignMembersNotInMission(Mockito.any(), Mockito.any())).thenReturn(outOfMissionCrewMembers);

            OutOfMissionVictoryEventHandler victoryGenerator = new OutOfMissionVictoryEventHandler(campaign, aarContext);
        
            int outOfMissionVictoriesAwarded = 0;
            for (int i = 0; i < 1000; ++i)
            {
                OutOfMissionVictoryData victoriesOutOMission = victoryGenerator.generateOutOfMissionVictories();
                if (!victoriesOutOMission.getVictoryAwardsByCrewMember().isEmpty())
                {
                    for (List<Victory> victories : victoriesOutOMission.getVictoryAwardsByCrewMember().values())
                    {
                        outOfMissionVictoriesAwarded += victories.size();
                        if (outOfMissionVictoriesAwarded > 1)
                        {
                            break;
                        }
                    }
                }
            }
    
            Assertions.assertTrue (outOfMissionVictoriesAwarded > 1);
        }
    }
}
