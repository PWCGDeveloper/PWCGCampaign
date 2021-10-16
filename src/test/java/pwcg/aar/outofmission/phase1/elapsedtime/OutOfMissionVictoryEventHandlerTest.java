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
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadmember.Victory;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OutOfMissionVictoryEventHandlerTest
{
    private Campaign campaign;

    @Mock private SquadronMember squadronMember;    
    @Mock private AARContext aarContext;
    @Mock private AARPreliminaryData preliminaryData;

    private SquadronMembers outOfMissionSquadronMembers;

    @BeforeEach
    public void setupTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.ESC_103_PROFILE);
        
        Mockito.when(aarContext.getPreliminaryData()).thenReturn(preliminaryData);
        
        outOfMissionSquadronMembers = new SquadronMembers();
        outOfMissionSquadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(campaign.getPersonnelManager().
                getSquadronPersonnel(SquadronTestProfile.JASTA_16_PROFILE.getSquadronId()).getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());
    }

    @Test
    public void testOutOfMissionVictoriesAwardedForVictories () throws PWCGException
    {     
        try (MockedStatic<CampaignMembersOutOfMissionFinder> mocked = Mockito.mockStatic(CampaignMembersOutOfMissionFinder.class)) 
        {
            mocked.when(() -> CampaignMembersOutOfMissionFinder.getAllCampaignMembersNotInMission(Mockito.any(), Mockito.any())).thenReturn(outOfMissionSquadronMembers);
            mocked.when(() -> CampaignMembersOutOfMissionFinder.getActiveCampaignMembersNotInMission(Mockito.any(), Mockito.any())).thenReturn(outOfMissionSquadronMembers);

            OutOfMissionVictoryEventHandler victoryGenerator = new OutOfMissionVictoryEventHandler(campaign, aarContext);
        
            int outOfMissionVictoriesAwarded = 0;
            for (int i = 0; i < 1000; ++i)
            {
                OutOfMissionVictoryData victoriesOutOMission = victoryGenerator.generateOutOfMissionVictories();
                if (!victoriesOutOMission.getVictoryAwardsBySquadronMember().isEmpty())
                {
                    for (List<Victory> victories : victoriesOutOMission.getVictoryAwardsBySquadronMember().values())
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
