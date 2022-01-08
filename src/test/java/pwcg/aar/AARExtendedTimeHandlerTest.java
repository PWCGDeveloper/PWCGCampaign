package pwcg.aar;

import java.util.Date;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;

import pwcg.aar.data.AARContext;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogCrewMember;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignMode;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PlaneStatus;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AARExtendedTimeHandlerTest
{
    private Campaign campaign;
    private AARContext aarContext;
    
    @Mock LogCrewMember playerLogCrewMember;
    
    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_MOSCOW);
        campaign.getCampaignData().setCampaignMode(CampaignMode.CAMPAIGN_MODE_SINGLE);
    }

    @BeforeEach
    public void setupTest() throws PWCGException
    {        
        aarContext = new AARContext(campaign);
    }

    @Test
    public void testPersonnelReplacedWhenTimePassed () throws PWCGException
    {
        CrewMembers squadronMembers = CrewMemberFilter.filterActiveAIAndPlayerAndAces(
                        campaign.getPersonnelManager().getCompanyPersonnel(SquadronTestProfile.JG_51_PROFILE_MOSCOW.getCompanyId()).getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());
        for (CrewMember crewMember : squadronMembers.getCrewMemberList())
        {
            if (!crewMember.isPlayer())
            {
                crewMember.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_KIA, campaign.getDate(), null);
            }
            CrewMembers squadronMembersLeft = CrewMemberFilter.filterActiveAIAndPlayerAndAces(
                            campaign.getPersonnelManager().getCompanyPersonnel(SquadronTestProfile.JG_51_PROFILE_MOSCOW.getCompanyId()).getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());
            if (squadronMembersLeft.getCrewMemberCollection().size() < 3)
            {
                break;
            }
        }
        
        CrewMembers squadronMembersLeft = CrewMemberFilter.filterActiveAIAndPlayerAndAces(
                        campaign.getPersonnelManager().getCompanyPersonnel(SquadronTestProfile.JG_51_PROFILE_MOSCOW.getCompanyId()).getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());
        
        int numLeaves = 0;
        int numCrewMembers = squadronMembersLeft.getCrewMemberCollection().size();
        while (numCrewMembers < 9)
        {
            AARExtendedTimeHandler extendedTimeHandler = new AARExtendedTimeHandler(campaign, aarContext);
            extendedTimeHandler.timePassedForLeave(21);
            if (numLeaves == 10)
            {
                break;
            }
            else
            {
                ++numLeaves;
                squadronMembersLeft = CrewMemberFilter.filterActiveAIAndPlayerAndAces(
                                campaign.getPersonnelManager().getCompanyPersonnel(SquadronTestProfile.JG_51_PROFILE_MOSCOW.getCompanyId()).getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());
                numCrewMembers = squadronMembersLeft.getCrewMemberCollection().size();
            }
        }
        
        Assertions.assertTrue (numLeaves < 10);
        CrewMembers squadronMembersAfter = CrewMemberFilter.filterActiveAIAndPlayerAndAces(
                        campaign.getPersonnelManager().getCompanyPersonnel(SquadronTestProfile.JG_51_PROFILE_MOSCOW.getCompanyId()).getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());
        assert(squadronMembersAfter.getCrewMemberCollection().size() >= 9);
    }

    
    @Test
    public void testEquipmentReplacedWhenTimePassed () throws PWCGException
    {
        Map<Integer, EquippedTank> activePlanes = campaign.getEquipmentManager().getEquipmentForCompany(SquadronTestProfile.JG_51_PROFILE_MOSCOW.getCompanyId()).getActiveEquippedTanks();
        for (EquippedTank equippedPlane : activePlanes.values())
        {
            equippedPlane.setPlaneStatus(TankStatus.STATUS_DESTROYED);
            Map<Integer, EquippedTank> activePlanesLeft = campaign.getEquipmentManager().getEquipmentForCompany(SquadronTestProfile.JG_51_PROFILE_MOSCOW.getCompanyId()).getActiveEquippedTanks();
            if (activePlanesLeft.size() < 3)
            {
                break;
            }
        }
        
        Map<Integer, EquippedTank> activePlanesLeft = campaign.getEquipmentManager().getEquipmentForCompany(SquadronTestProfile.JG_51_PROFILE_MOSCOW.getCompanyId()).getActiveEquippedTanks();
        int numLeaves = 0;
        int numPlanes = activePlanesLeft.size();
        while (numPlanes < 9)
        {
            AARExtendedTimeHandler extendedTimeHandler = new AARExtendedTimeHandler(campaign, aarContext);
            extendedTimeHandler.timePassedForLeave(21);
            if (numLeaves == 10)
            {
                break;
            }
            else
            {
                ++numLeaves;
                activePlanesLeft = campaign.getEquipmentManager().getEquipmentForCompany(SquadronTestProfile.JG_51_PROFILE_MOSCOW.getCompanyId()).getActiveEquippedTanks();
                numPlanes = activePlanesLeft.size();
            }
        }
        
        Assertions.assertTrue (numLeaves < 10);
        CrewMembers squadronMembersAfter = CrewMemberFilter.filterActiveAIAndPlayerAndAces(
                        campaign.getPersonnelManager().getCompanyPersonnel(SquadronTestProfile.JG_51_PROFILE_MOSCOW.getCompanyId()).getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());
        assert(squadronMembersAfter.getCrewMemberCollection().size() >= 9);
    }

    @Test
    public void testTimePassedForLeave () throws PWCGException
    {
        Date startCampaignDate = campaign.getDate();
        AARExtendedTimeHandler extendedTimeHandler = new AARExtendedTimeHandler(campaign, aarContext);
        extendedTimeHandler.timePassedForLeave(21);
        Date endCampaignDate = campaign.getDate();
        
        assert(endCampaignDate.after(startCampaignDate));
        int daysPassed = DateUtils.daysDifference(startCampaignDate, endCampaignDate);
        assert(daysPassed > 19);
        assert(daysPassed < 23);
    }
    
    @Test
    public void testTimePassedForTransfer () throws PWCGException
    {
        Date startCampaignDate = campaign.getDate();
        AARExtendedTimeHandler extendedTimeHandler = new AARExtendedTimeHandler(campaign, aarContext);
        extendedTimeHandler.timePassedForTransfer(10);
        Date endCampaignDate = campaign.getDate();
        
        assert(endCampaignDate.after(startCampaignDate));
        int daysPassed = DateUtils.daysDifference(startCampaignDate, endCampaignDate);
        assert(daysPassed >= 9);
        assert(daysPassed <= 13);
    }
}
