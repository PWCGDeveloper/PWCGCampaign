package pwcg.aar.integration;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.AAROutOfMissionStepper;
import pwcg.aar.data.AARContext;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CampaignCacheBoS;

@RunWith(MockitoJUnitRunner.class)
public class AARCoordinatorLossAndReplacementAnalyzer
{
    private Campaign campaign;    
    private AARContext aarContext;

    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        PWCGContextManager.getInstance().changeContext(FrontMapIdentifier.MOSCOW_MAP);
        campaign = CampaignCache.makeCampaignForceCreation(CampaignCacheBoS.JG_51_PROFILE);
    }

    @Test
    public void runMissionAARLeave () throws PWCGException
    {
        aarContext = new AARContext(campaign);
    	Date newDate = DateUtils.getDateYYYYMMDD("19420801");

    	int totalVictories = 0;
    	int totalLosses = 0;
    	int totalAlliedLosses = 0;
    	int totalAxisLosses = 0;
        int totalMedalsAwarded = 0;
        int totalPromotionsAwarded = 0;
        int totalTransfers = 0;
        int cycleNum = 0;
    	
	    do 
	    {
	    	++cycleNum;
	    	int activeSquadrons = PWCGContextManager.getInstance().getSquadronManager().getAllActiveSquadrons(campaign.getDate()).size();
	    	int victories = aarContext.getReconciledOutOfMissionData().getPersonnelAwards().getTotalAirToAirVictories();
	    	int losses = aarContext.getReconciledOutOfMissionData().getPersonnelLosses().getSquadMembersLost().size();
	    	int replacements = campaign.getPersonnelManager().getReplacementCount();
	    	int medalsAwarded = aarContext.getReconciledOutOfMissionData().getPersonnelAwards().getMedalsAwarded().size();
            int promotionsAwarded = aarContext.getReconciledOutOfMissionData().getPersonnelAwards().getPromotions().size();
            int transfers = aarContext.getReconciledOutOfMissionData().getTransferData().getSquadronTransferData().getSquadronMembersTransferred().size();
            int numAiPilots = campaign.getPersonnelManager().getAllCampaignMembers().size();

            totalVictories += victories;
            totalLosses += losses;
            totalMedalsAwarded += medalsAwarded;
            totalPromotionsAwarded += promotionsAwarded;
            totalTransfers += transfers;
	    	
	    	int axisLosses = 0;
	    	int alliedLosses = 0;
	    	for (SquadronMember lostPilot : aarContext.getReconciledOutOfMissionData().getPersonnelLosses().getSquadMembersLost().values())
	    	{
	    		if (lostPilot.determineCountry(campaign.getDate()).getSide() == Side.ALLIED)
	    		{
	    			++alliedLosses;	    			
	    			++totalAlliedLosses;	    			
	    		}
	    		else
	    		{
	    			++axisLosses;
	    			++totalAxisLosses;
	    		}
	    	}

            AAROutOfMissionStepper stepper = new AAROutOfMissionStepper(campaign, aarContext);
            stepper.oneStep();
            
            printShortHandedSquadrons();
	    	
	        System.out.println("Cycle: " + cycleNum);
	        System.out.println("Victories: " + victories);
	        System.out.println("Losses: " + losses);
	        System.out.println("Allied: " + alliedLosses);
	        System.out.println("Axis: " + axisLosses);
	        System.out.println("Replacement: " + replacements);
            System.out.println("Squadrons: " + activeSquadrons);
            System.out.println("Medals: " + medalsAwarded);
            System.out.println("Promotions: " + promotionsAwarded);
            System.out.println("Transfers: " + transfers);
            System.out.println("Num Ai Pilots: " + numAiPilots);
            System.out.println("End of cycle " + cycleNum + "\n\n\n\n");
	    }
	    while(campaign.getDate().before(newDate));
	    
        System.out.println("Total Victories: " + totalVictories);
        System.out.println("Total Losses: " + totalLosses);
        System.out.println("Total Allied: " + totalAlliedLosses);
        System.out.println("Total Axis: " + totalAxisLosses);
        System.out.println("Total Medals: " + totalMedalsAwarded);
        System.out.println("Total Promotions: " + totalPromotionsAwarded);
        System.out.println("Total Transfers: " + totalTransfers);
        
        assert(totalVictories > 0);
        assert(totalLosses > 0);
        assert(totalAlliedLosses > 0);
        assert(totalAxisLosses > 0);
        assert(totalMedalsAwarded > 0);
        assert(totalPromotionsAwarded > 0);
        assert(totalTransfers > 0);

    }

    private void printShortHandedSquadrons() throws PWCGException
    {
        for (SquadronPersonnel squadronPersonnel : campaign.getPersonnelManager().getAllSquadronPersonnel())
        {
            System.out.println(squadronPersonnel.getSquadron().determineDisplayName(campaign.getDate()) +
                    " size is " + squadronPersonnel.getActiveSquadronMembers().getActiveCount(campaign.getDate()));
        }
    }
}
