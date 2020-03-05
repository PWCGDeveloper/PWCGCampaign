package pwcg.aar;

import java.util.Date;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.data.AARContext;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class AARCoordinatorLossAndReplacementAnalyzer
{
    private Campaign campaign;    
    private AARContext aarContext;

    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaignForceCreation(SquadronTestProfile.JG_51_PROFILE_MOSCOW);
    }

    @Test
    public void runMissionAARLeave () throws PWCGException
    {
        aarContext = new AARContext(campaign);
    	Date newDate = DateUtils.getDateYYYYMMDD("19420801");

    	int totalVictories = 0;
    	int totalPersonnelLosses = 0;
        int totalAlliedPersonnelLosses = 0;
        int totalAxisPersonnelLosses = 0;
        int totalEquipmentLosses = 0;
        int totalAlliedEquipmentLosses = 0;
        int totalAxisEquipmentLosses = 0;
        int totalMedalsAwarded = 0;
        int totalPromotionsAwarded = 0;
        int totalTransfers = 0;
        int cycleNum = 0;
    	
	    do 
	    {
	    	++cycleNum;
	    	int activeSquadrons = PWCGContext.getInstance().getSquadronManager().getActiveSquadrons(campaign.getDate()).size();
	    	int victories = aarContext.getReconciledOutOfMissionData().getPersonnelAwards().getTotalAirToAirVictories();
	    	int losses = aarContext.getReconciledOutOfMissionData().getPersonnelLossesOutOfMission().getSquadMembersLost().size();
	    	int replacements = campaign.getPersonnelManager().getReplacementCount();
	    	int medalsAwarded = aarContext.getReconciledOutOfMissionData().getPersonnelAwards().getMedalsAwarded().size();
            int promotionsAwarded = aarContext.getReconciledOutOfMissionData().getPersonnelAwards().getPromotions().size();
            int transfers = aarContext.getReconciledOutOfMissionData().getResupplyData().getSquadronTransferData().getSquadronMembersTransferred().size();
            
            Map<Integer, SquadronMember> allCampaignMembers = campaign.getPersonnelManager().getAllCampaignMembers();  
            SquadronMembers activeAiCampaignMembers = SquadronMemberFilter.filterActiveAI(allCampaignMembers, campaign.getDate());
            int numAiPilots = activeAiCampaignMembers.getSquadronMemberList().size();
            int equipmentLosses = aarContext.getReconciledOutOfMissionData().getEquipmentLossesOutOfMission().getPlanesDestroyed().size();

            totalVictories += victories;
            totalPersonnelLosses += losses;
            totalMedalsAwarded += medalsAwarded;
            totalPromotionsAwarded += promotionsAwarded;
            totalTransfers += transfers;
            totalEquipmentLosses += equipmentLosses;
            
	    	int axisPersonnelLosses = 0;
	    	int alliedPersonnelLosses = 0;
	    	for (SquadronMember lostPilot : aarContext.getReconciledOutOfMissionData().getPersonnelLossesOutOfMission().getSquadMembersLost().values())
	    	{
	    		if (lostPilot.determineCountry(campaign.getDate()).getSide() == Side.ALLIED)
	    		{
	    			++alliedPersonnelLosses;	    			
	    			++totalAlliedPersonnelLosses;	    			
	    		}
	    		else
	    		{
	    			++axisPersonnelLosses;
	    			++totalAxisPersonnelLosses;
	    		}
	    	}
            
            int alliedEquipmentLosses = 0;
            int axisEquipmentLosses = 0;
            for (EquippedPlane lostPlane : aarContext.getReconciledOutOfMissionData().getEquipmentLossesOutOfMission().getPlanesDestroyed().values())
            {
                if (lostPlane.getSide() == Side.ALLIED)
                {
                    ++alliedEquipmentLosses;                 
                    ++totalAlliedEquipmentLosses;                    
                }
                else
                {
                    ++axisEquipmentLosses;
                    ++totalAxisEquipmentLosses;
                }
            }

            AAROutOfMissionStepper stepper = new AAROutOfMissionStepper(campaign, aarContext);
            stepper.oneStep();
            
            printShortHandedSquadrons();
	    	
            System.out.println("=====================================================");
            System.out.println("Cycle: " + cycleNum);
	        System.out.println("Victories: " + victories);
	        System.out.println("Losses: " + losses);
            System.out.println("  Allied Personnel: " + alliedPersonnelLosses);
            System.out.println("  Axis Personnel: " + axisPersonnelLosses);
            System.out.println("  Personnel Replacements Available: " + campaign.getPersonnelManager().getReplacementCount());
            System.out.println("  Allied Equipment: " + alliedEquipmentLosses);
            System.out.println("  Axis Equipment: " + axisEquipmentLosses);
            System.out.println("  Equipment Replacements Available: " + campaign.getEquipmentManager().getReplacementCount());
	        System.out.println("Replacement: " + replacements);
            System.out.println("Squadrons: " + activeSquadrons);
            System.out.println("Medals: " + medalsAwarded);
            System.out.println("Promotions: " + promotionsAwarded);
            System.out.println("Transfers: " + transfers);
            System.out.println("Num Ai Pilots: " + numAiPilots);
            System.out.println("End of cycle " + cycleNum);
            System.out.println("=====================================================");
	    }
	    while(campaign.getDate().before(newDate));
	    
        
        System.out.println("Total Personnel Allied: " + totalAlliedPersonnelLosses);
        System.out.println("Total Personnel Axis: " + totalAxisPersonnelLosses);
        System.out.println("Total Personnel Losses: " + totalPersonnelLosses);
        System.out.println("");
        
        System.out.println("Total Equipment Allied: " + totalAlliedEquipmentLosses);
        System.out.println("Total Equipment Axis: " + totalAxisEquipmentLosses);
        System.out.println("Total Equipment Losses: " + totalEquipmentLosses);
        System.out.println("");

        System.out.println("Total Victories: " + totalVictories);
        System.out.println("Total Medals: " + totalMedalsAwarded);
        System.out.println("Total Promotions: " + totalPromotionsAwarded);
        System.out.println("Total Transfers: " + totalTransfers);
        
        
        assert(totalVictories > 0);
        assert(totalPersonnelLosses > 0);
        assert(totalAlliedPersonnelLosses > 0);
        assert(totalAxisPersonnelLosses > 0);
        assert(totalMedalsAwarded > 0);
        assert(totalPromotionsAwarded > 0);
        assert(totalTransfers > 0);

    }

    private void printShortHandedSquadrons() throws PWCGException
    {
        System.out.println("=====================================================");
        for (SquadronPersonnel squadronPersonnel : campaign.getPersonnelManager().getAllSquadronPersonnel())
        {
            System.out.println(squadronPersonnel.getSquadron().determineDisplayName(campaign.getDate()));
            System.out.println(" Personnel size is " + squadronPersonnel.getSquadronMembersWithAces().getActiveCount(campaign.getDate()));
            
            Equipment equipment = campaign.getEquipmentManager().getEquipmentForSquadron(squadronPersonnel.getSquadron().getSquadronId());
            System.out.println(" Equipment size is " + equipment.getActiveEquippedPlanes().size());
        }
    }
}
