package pwcg.aar;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.squadron.SquadronViability;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AARDepeletionTest
{
    private Campaign campaign;    
    private static AARCoordinator aarCoordinator;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        PWCGContext.getInstance().changeContext(FrontMapIdentifier.BODENPLATTE_MAP);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.RAF_184_PROFILE);
        aarCoordinator = AARCoordinator.getInstance();
    }

    @Test
    public void runMissionAARLeave () throws PWCGException
    {
        for (int i = 0; i < 180; ++i)
        {
            System.out.println("Iteration " + (i+1));
            System.out.println(DateUtils.getDateStringPretty(campaign.getDate()));

            aarCoordinator.reset(campaign);
            aarCoordinator.submitLeave(campaign, 1);
            int numDepeletedSquadrons = 0;
            for (Squadron squadron : PWCGContext.getInstance().getSquadronManager().getActiveSquadrons(campaign.getDate()))
            {
                if (!SquadronViability.isSquadronViable(squadron, campaign))
                {
                    SquadronPersonnel squadronpersonnel = campaign.getPersonnelManager().getSquadronPersonnel(squadron.getSquadronId());
                    int numActivePilots = squadronpersonnel.getSquadronMembers().getActiveCount(campaign.getDate());
                    
                    Equipment squadronEquipment = campaign.getEquipmentManager().getEquipmentForSquadron(squadron.getSquadronId());
                    int numActivePlanes = squadronEquipment.getActiveEquippedPlanes().size();
                    
                    System.out.println("    " + squadron.getSquadronId() + " " + squadron.determineDisplayName(campaign.getDate()));
                    System.out.println("        Pilots: " + numActivePilots);
                    System.out.println("        Planes: " + numActivePlanes);
                    
                    ++numDepeletedSquadrons;
                }
            }
            
            assert(numDepeletedSquadrons < 15);
        }
    }
    
    
}
