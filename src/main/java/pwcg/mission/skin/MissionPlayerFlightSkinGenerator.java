package pwcg.mission.skin;

import java.util.Date;

import pwcg.campaign.Campaign;
import pwcg.campaign.skin.Skin;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;

public class MissionPlayerFlightSkinGenerator 
{
    private IFlight flight;
    private MissionSkinSet missionSkinSet;
    
    public MissionPlayerFlightSkinGenerator(IFlight flight, MissionSkinSet missionSkinSet)
    {
        this.flight = flight;
        this.missionSkinSet = missionSkinSet;
    }

    public void applyPlayerSkin()
    {
        for (PlaneMcu plane : flight.getFlightPlanes().getPlanes())
        {
            SquadronMember squadronMember = plane.getPilot();
            setSkinForPlayerSquadron(squadronMember, flight.getFlightInformation().getSquadron(), plane, flight.getCampaign().getDate());
        }
    }

    private void setSkinForPlayerSquadron(SquadronMember pilot, Squadron squad, PlaneMcu plane, Date date)
    {
        MissionSkinInitializer.intitializeSkin(missionSkinSet, squad, plane, date);
        setUserAssignedPilotSkin(pilot, plane);
    }

	private void setUserAssignedPilotSkin(SquadronMember pilot, PlaneMcu plane) 
	{
        Campaign campaign = flight.getCampaign();
        
		for (Skin pilotSkin : pilot.getSkins())
		{
		    if (pilotSkin.getStartDate().after(campaign.getDate()) || pilotSkin.getEndDate().before(campaign.getDate()))
		    {
		        continue;
		    }
	            
		    try
		    {
    		    if (pilotSkin.getPlane() != null && 
    		        pilotSkin.getPlane().length() > 0 &&
                    !pilotSkin.getPlane().equals(plane.getType()))
    		    {
    		        continue;
    		    }
    
                plane.setPlaneSkin(pilotSkin);
                flight.getMission().addSkinInUse(pilotSkin);
		    }
		    catch (Exception exp)
		    {
	            PWCGLogger.logException(exp);
		    }
		}
	}
}
