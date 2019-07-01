package pwcg.aar.inmission.phase3.reconcile.victories.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.Victory;
import pwcg.campaign.squadmember.VictoryBuilder;
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
    			SquadronMember pilot = campaign.getPersonnelManager().getAnyCampaignMember(victorPlanePlane.getPilotSerialNumber());
    			if (pilot != null)
    			{			    
                    addVictoryForPilot(resultVictory, pilot);
    			}
            }
		}
		
		return victories;
	}

    private void addVictoryForPilot(LogVictory logVictory, SquadronMember pilot) throws PWCGException
    {
        VictoryBuilder victoryBuilder = new VictoryBuilder(campaign);
        Victory victory = victoryBuilder.buildVictory(campaign.getDate(), logVictory);

        if (!victories.containsKey(pilot.getSerialNumber()))
        {
            List<Victory> victoriesForPilot = new ArrayList<Victory>();
            victories.put(pilot.getSerialNumber(), victoriesForPilot);
        }
        
        List<Victory> victoriesForPilot = victories.get(pilot.getSerialNumber());
        victoriesForPilot.add(victory);
    }
}
