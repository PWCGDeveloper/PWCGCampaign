package integration;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import pwcg.aar.AARCoordinator;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.squadron.SquadronViability;
import pwcg.campaign.utils.TestDriver;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.TestCampaignFactoryBuilder;
import pwcg.testutils.SquadronTestProfile;

@Tag("BOS")
public class AARDepeletionTest
{
    @Test
    public void runMissionAARLeave () throws PWCGException
    {
        TestDriver.getInstance().enableTestDriver();
        
        PWCGContext.setProduct(PWCGProduct.BOS);
        Campaign campaign = TestCampaignFactoryBuilder.makeCampaign(this.getClass().getCanonicalName(), SquadronTestProfile.RAF_184_PROFILE);
        AARCoordinator aarCoordinator = AARCoordinator.getInstance();

        for (int i = 0; i < 180; ++i)
        {
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
                    
                    printIterationResults(campaign, squadron, numActivePilots, numActivePlanes);
                    
                    ++numDepeletedSquadrons;
                }
            }
            
            assert(numDepeletedSquadrons < 15);
        }
    }

    private void printIterationResults(Campaign campaign, Squadron squadron, int numActivePilots, int numActivePlanes) throws PWCGException
    {
        System.out.println("    " + squadron.getSquadronId() + " " + squadron.determineDisplayName(campaign.getDate()));
        System.out.println("        Pilots: " + numActivePilots);
        System.out.println("        Planes: " + numActivePlanes);
    } 
}
