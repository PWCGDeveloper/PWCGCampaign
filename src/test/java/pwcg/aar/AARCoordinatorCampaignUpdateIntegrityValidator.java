package pwcg.aar;

import java.util.Date;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.aar.data.AARContext;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PlaneStatus;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.CampaignRemover;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AARCoordinatorCampaignUpdateIntegrityValidator
{
    private Campaign campaign;    
    private AARContext aarContext;

    private Map<Integer, SquadronMember> personnelLosses;
    private Map<Integer, LogPlane> equipmentLosses;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_MOSCOW);
    }

    @Test
    public void testVerifyMissionStatisticsArePersisted () throws PWCGException
    {
        campaign.write();
        boolean lossesRecorded = false;
        
        aarContext = new AARContext(campaign);
    	Date newDate = DateUtils.getDateYYYYMMDD("19420801");
	    do 
	    {
            System.out.println("iteration date: " + DateUtils.getDateStringDashDelimitedYYYYMMDD(campaign.getDate()));

            personnelLosses = aarContext.getPersonnelLosses().getSquadMembersLostAndInjured();
            equipmentLosses = aarContext.getEquipmentLosses().getPlanesDestroyed();
            if (personnelLosses.size() > 0)
            {
                validatePersonnelLossesInMemory();
                validateEquipmentLossesInMemory();
                
                campaign.write();
                campaign.open(campaign.getCampaignData().getName());
                
                validatePersonnelLossesInMemory();
                validateEquipmentLossesInMemory();
                
                lossesRecorded = true;
                                
                break;
            }
            
            AAROutOfMissionStepper stepper = new AAROutOfMissionStepper(campaign, aarContext);
            stepper.oneStep();
	    }
	    while(campaign.getDate().before(newDate));

	    CampaignRemover.deleteCampaign(campaign.getCampaignData().getName());
	    
	    assert(lossesRecorded);
    }

    private void validatePersonnelLossesInMemory() throws PWCGException
    {
        for (SquadronMember lostPilot : personnelLosses.values())
        {
            System.out.println("Pilot Lost: " + lostPilot.getNameAndRank());
            SquadronMember lostPilotFromPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(lostPilot.getSquadronId()).getSquadronMember(lostPilot.getSerialNumber());
            assert (lostPilotFromPersonnel != null);
            assert (lostPilotFromPersonnel.getPilotActiveStatus() <= SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED);
        }
    }

    private void validateEquipmentLossesInMemory() throws PWCGException
    {
        for (LogPlane lostPlane : equipmentLosses.values())
        {
            EquippedPlane lostPlaneFromEquipment = campaign.getEquipmentManager().getEquipmentForSquadron(lostPlane.getSquadronId()).getEquippedPlane(lostPlane.getPlaneSerialNumber());
            assert (lostPlaneFromEquipment != null);
            assert (lostPlaneFromEquipment.getPlaneStatus() == PlaneStatus.STATUS_DESTROYED);
        }
    }
}
