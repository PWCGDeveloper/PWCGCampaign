package pwcg.aar.inmission.phase3.reconcile.victories.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.Victory;
import pwcg.campaign.crewmember.VictoryBuilder;
import pwcg.core.exception.PWCGException;

public class VictoryEventHandler 
{
    private Campaign campaign;
    private Map<Integer, List<Victory>> victories = new HashMap<>();

	public VictoryEventHandler (Campaign campaign) 
	{
	    this.campaign = campaign;
	}

	public Map<Integer, List<Victory>> recordVictories(ConfirmedVictories missionVictories) throws PWCGException 
	{
		for (LogVictory resultVictory : missionVictories.getConfirmedVictories())
		{
            if (resultVictory.getVictor() instanceof LogPlane)
            {
                LogPlane victorPlanePlane = (LogPlane)resultVictory.getVictor();
    			CrewMember crewMember = campaign.getPersonnelManager().getAnyCampaignMember(victorPlanePlane.getCrewMemberSerialNumber());
    			if (crewMember != null)
    			{			    
                    addVictoryForCrewMember(resultVictory, crewMember);
    			}
            }
		}
		
		return victories;
	}

    private void addVictoryForCrewMember(LogVictory logVictory, CrewMember crewMember) throws PWCGException
    {
        VictoryBuilder victoryBuilder = new VictoryBuilder(campaign);
        Victory victory = victoryBuilder.buildVictory(campaign.getDate(), logVictory);

        if (!victories.containsKey(crewMember.getSerialNumber()))
        {
            List<Victory> victoriesForCrewMember = new ArrayList<Victory>();
            victories.put(crewMember.getSerialNumber(), victoriesForCrewMember);
        }
        
        List<Victory> victoriesForCrewMember = victories.get(crewMember.getSerialNumber());
        victoriesForCrewMember.add(victory);
    }
}
