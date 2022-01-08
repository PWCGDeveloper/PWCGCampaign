package pwcg.aar.outofmission.phase1.elapsedtime;

import java.util.HashMap;
import java.util.Map;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.AARContextEventSequence;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.prelim.CampaignMembersOutOfMissionFinder;
import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyViability;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.tank.EquippedTank;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class OutOfMissionAAALossCalculator
{
    private Campaign campaign;
    private AARContext aarContext;
    private Map<Integer, CrewMember> crewMembersLostDueToAAA = new HashMap<>();
    private Map<Integer, LogPlane> planesLostDueToAAA = new HashMap<>();
    private OutOfMissionAAAOddsCalculator oddsShotDownByAAACalculator;

    public OutOfMissionAAALossCalculator (Campaign campaign, AARContext aarContext, OutOfMissionAAAOddsCalculator oddsShotDownByAAACalculator)
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
        this.oddsShotDownByAAACalculator = oddsShotDownByAAACalculator;
    }
    
    public void lostToAAA() throws PWCGException
    {
        CrewMembers campaignMembersNotInMission = CampaignMembersOutOfMissionFinder.getActiveCampaignMembersNotInMission(
                campaign, aarContext.getPreliminaryData().getCampaignMembersInMission());
        for (CrewMember crewMember : campaignMembersNotInMission.getCrewMemberList())
        {
            Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(crewMember.getCompanyId());
            if (CompanyViability.isCompanyViable(squadron, campaign))
            {
                calculateCrewMemberShotDownByAAA(crewMember);
            }
        }
    }

    private void calculateCrewMemberShotDownByAAA(CrewMember crewMember) throws PWCGException
    {
        if (OutOfMissionCrewMemberSelector.shouldCrewMemberBeEvaluated(campaign, crewMember)) 
        {
            int oddsShotDown = oddsShotDownByAAACalculator.oddsShotDownByAAA(crewMember);
            
            int shotDownDiceRoll = RandomNumberGenerator.getRandom(1000);
            if (shotDownDiceRoll <= oddsShotDown)
            {
                EquippedTank planeShotDownByAAA = campaign.getEquipmentManager().destroyTankFromCompany(crewMember.getCompanyId(), campaign.getDate());

                LogPlane logPlane = new LogPlane(AARContextEventSequence.getNextOutOfMissionEventSequenceNumber());
                logPlane.initializeFromOutOfMission(campaign, planeShotDownByAAA, crewMember);
                
                crewMembersLostDueToAAA.put(crewMember.getSerialNumber(), crewMember);
                planesLostDueToAAA.put(planeShotDownByAAA.getSerialNumber(), logPlane);
            }
        }
    }

    public Map<Integer, CrewMember> getCrewMembersLostDueToAAA()
    {
        return crewMembersLostDueToAAA;
    }

    public Map<Integer, LogPlane> getPlanesLostDueToAAA()
    {
        return planesLostDueToAAA;
    }
}
