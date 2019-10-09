package pwcg.aar;

import java.util.Date;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.AAROutOfMissionStepper;
import pwcg.aar.data.AARContext;
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
import pwcg.testutils.SquadrontTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class AARCoordinatorCampaignUpdateIntegrityValidator
{
    private Campaign campaign;    
    private AARContext aarContext;

    private Map<Integer, SquadronMember> personnelLosses;
    private Map<Integer, EquippedPlane> equipmentLosses;

    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaignForceCreation(SquadrontTestProfile.JG_51_PROFILE_MOSCOW);
    }

    @Test
    public void runMissionAARLeave () throws PWCGException
    {
        aarContext = new AARContext(campaign);
    	Date newDate = DateUtils.getDateYYYYMMDD("19420801");
	    do 
	    {
            personnelLosses = aarContext.getReconciledOutOfMissionData().getPersonnelLossesOutOfMission().getSquadMembersLost();
            equipmentLosses = aarContext.getReconciledOutOfMissionData().getEquipmentLossesOutOfMission().getPlanesDestroyed();
            if (personnelLosses.size() > 0)
            {
                validatePersonnelLossesInMemory();
                validateEquipmentLossesInMemory();
                
                campaign.write();
                campaign.open(campaign.getCampaignData().getName());
                
                validatePersonnelLossesInMemory();
                validateEquipmentLossesInMemory();
                
                CampaignRemover campaignRemover = new CampaignRemover();
                campaignRemover.deleteCampaign(campaign.getCampaignData().getName());
                
                break;
            }
            
            AAROutOfMissionStepper stepper = new AAROutOfMissionStepper(campaign, aarContext);
            stepper.oneStep();
            
	    }
	    while(campaign.getDate().before(newDate));
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
        for (EquippedPlane lostPlane : equipmentLosses.values())
        {
            System.out.println("Plane Lost: " + lostPlane.getDisplayName() + " " + lostPlane.getSerialNumber());
            EquippedPlane lostPlaneFromEquipment = campaign.getEquipmentManager().getEquipmentForSquadron(lostPlane.getSquadronId()).getEquippedPlane(lostPlane.getSerialNumber());
            assert (lostPlaneFromEquipment != null);
            assert (lostPlaneFromEquipment.getPlaneStatus() == PlaneStatus.STATUS_DESTROYED);
        }
    }
}
