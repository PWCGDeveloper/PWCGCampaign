package integration;

import java.util.Date;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.aar.AAROutOfMissionStepper;
import pwcg.aar.data.AARContext;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.tank.EquippedTank;
import pwcg.campaign.tank.TankStatus;
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

    private Map<Integer, CrewMember> personnelLosses;
    private Map<Integer, LogPlane> equipmentLosses;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaignOnDisk(SquadronTestProfile.JG_51_PROFILE_MOSCOW);
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
        for (CrewMember lostCrewMember : personnelLosses.values())
        {
            System.out.println("CrewMember Lost: " + lostCrewMember.getNameAndRank());
            CrewMember lostCrewMemberFromPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(lostCrewMember.getCompanyId()).getCrewMember(lostCrewMember.getSerialNumber());
            assert (lostCrewMemberFromPersonnel != null);
            assert (lostCrewMemberFromPersonnel.getCrewMemberActiveStatus() <= CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED);
        }
    }

    private void validateEquipmentLossesInMemory() throws PWCGException
    {
        for (LogPlane lostPlane : equipmentLosses.values())
        {
            EquippedTank lostPlaneFromEquipment = campaign.getEquipmentManager().getEquipmentForCompany(lostPlane.getSquadronId()).getEquippedTank(lostPlane.getPlaneSerialNumber());
            assert (lostPlaneFromEquipment != null);
            assert (lostPlaneFromEquipment.getPlaneStatus() == TankStatus.STATUS_DESTROYED);
        }
    }
}
