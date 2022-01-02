package integration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.aar.AARCoordinator;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.squadron.Company;
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
        campaign = CampaignCache.makeCampaignOnDisk(SquadronTestProfile.RAF_184_PROFILE);
        aarCoordinator = AARCoordinator.getInstance();
    }

    @Test
    public void runMissionAARLeave () throws PWCGException
    {
        for (int i = 0; i < 180; ++i)
        {
            System.out.println(DateUtils.getDateStringPretty(campaign.getDate()));

            aarCoordinator.reset(campaign);
            aarCoordinator.submitLeave(campaign, 1);
            int numDepeletedSquadrons = 0;
            for (Company squadron : PWCGContext.getInstance().getSquadronManager().getActiveSquadrons(campaign.getDate()))
            {
                if (!SquadronViability.isSquadronViable(squadron, campaign))
                {
                    CompanyPersonnel squadronpersonnel = campaign.getPersonnelManager().getCompanyPersonnel(squadron.getSquadronId());
                    int numActiveCrewMembers = squadronpersonnel.getCrewMembers().getActiveCount(campaign.getDate());
                    
                    Equipment squadronEquipment = campaign.getEquipmentManager().getEquipmentForSquadron(squadron.getSquadronId());
                    int numActivePlanes = squadronEquipment.getActiveEquippedPlanes().size();
                    
                    printIterationResults(squadron, numActiveCrewMembers, numActivePlanes);
                    
                    ++numDepeletedSquadrons;
                }
            }
            
            assert(numDepeletedSquadrons < 15);
        }
    }

    private void printIterationResults(Company squadron, int numActiveCrewMembers, int numActivePlanes) throws PWCGException
    {
        System.out.println("    " + squadron.getSquadronId() + " " + squadron.determineDisplayName(campaign.getDate()));
        System.out.println("        CrewMembers: " + numActiveCrewMembers);
        System.out.println("        Planes: " + numActivePlanes);
    } 
}
