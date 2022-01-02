package pwcg.mission.skin;

import java.util.Date;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.skin.Skin;
import pwcg.campaign.squadron.Company;
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
            CrewMember crewMember = plane.getCrewMember();
            setSkinForPlayerSquadron(crewMember, flight.getFlightInformation().getSquadron(), plane, flight.getCampaign().getDate());
        }
    }

    private void setSkinForPlayerSquadron(CrewMember crewMember, Company company, PlaneMcu plane, Date date)
    {
        MissionSkinInitializer.intitializeSkin(missionSkinSet, company, plane, date);
        setUserAssignedCrewMemberSkin(crewMember, plane);
    }

	private void setUserAssignedCrewMemberSkin(CrewMember crewMember, PlaneMcu plane) 
	{
        Campaign campaign = PWCGContext.getInstance().getCampaign();
        
		for (Skin crewMemberSkin : crewMember.getSkins())
		{
		    if (crewMemberSkin.getStartDate().after(campaign.getDate()) || crewMemberSkin.getEndDate().before(campaign.getDate()))
		    {
		        continue;
		    }
	            
		    try
		    {
    		    if (crewMemberSkin.getPlane() != null && 
    		        crewMemberSkin.getPlane().length() > 0 &&
                    !crewMemberSkin.getPlane().equals(plane.getType()))
    		    {
    		        continue;
    		    }
    
                plane.setPlaneSkin(crewMemberSkin);
                flight.getMission().addSkinInUse(crewMemberSkin);
		    }
		    catch (Exception exp)
		    {
	            PWCGLogger.logException(exp);
		    }
		}
	}
}
