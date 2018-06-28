package pwcg.aar.outofmission.phase1.elapsedtime;

import java.util.HashMap;
import java.util.Map;

import pwcg.aar.data.AARContext;
import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class OutOfMissionAAALossCalculator
{
    private Campaign campaign;
    private AARContext aarContext;
    private Map<Integer, SquadronMember> lossesDueToAAA = new HashMap<>();
    private OutOfMissionAAAOddsCalculator oddsShotDownByAAACalculator;

    public OutOfMissionAAALossCalculator (Campaign campaign, AARContext aarContext, OutOfMissionAAAOddsCalculator oddsShotDownByAAACalculator)
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
        this.oddsShotDownByAAACalculator = oddsShotDownByAAACalculator;
    }
    
    public Map<Integer, SquadronMember> pilotsLostToAAA() throws PWCGException
    {
        for (SquadronMember squadronMember : aarContext.getPreliminaryData().getCampaignMembersOutOfMission().getSquadronMembers().values())
        {
            if (campaign.getPersonnelManager().getSquadronPersonnel(squadronMember.getSquadronId()).isSquadronViable())
            {
                calculatePilotShotDownByAAA(squadronMember);
            }
        }

        return lossesDueToAAA;
    }

    private void calculatePilotShotDownByAAA(SquadronMember squadronMember) throws PWCGException
    {
        if (OutOfMissionPilotSelector.shouldPilotBeEvaluated(campaign, squadronMember)) 
        {
            int oddsShotDown = oddsShotDownByAAACalculator.oddsShotDownByAAA(squadronMember);
            
            int shotDownDiceRoll = RandomNumberGenerator.getRandom(1000);
            if (shotDownDiceRoll <= oddsShotDown)
            {
                lossesDueToAAA.put(squadronMember.getSerialNumber(), squadronMember);
            }
        }
    }
}
