package pwcg.mission;

import java.util.Date;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.skin.Skin;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightData;
import pwcg.mission.flight.plane.PlaneMcu;

public class MissionSkinGenerator 
{

    public void assignSkinsForFlight(FlightData flight) throws PWCGException
    {
        if (flight.getFlightInformation().isPlayerFlight())
        {
            for (PlaneMcu plane : flight.getFlightPlanes().getPlanes())
            {
                SquadronMember squadronMember = plane.getPilot();
                setSkinForPlayerSquadron(squadronMember, flight.getFlightInformation().getSquadron(), plane, flight.getCampaign().getDate());
            }
        }
        else
        {
            for (PlaneMcu plane : flight.getFlightPlanes().getPlanes())
            {
                setAISkin(flight.getFlightInformation().getSquadron(), plane, flight.getCampaign().getDate());
            }
        }
    }

    private void setSkinForPlayerSquadron(SquadronMember pilot, Squadron squad, PlaneMcu plane, Date date)
    {
        plane.setPlaneSkin(null);
        setSquadronSkin(squad, plane, date);
        setUserAssignedPilotSkin(pilot, plane);
    }

    private void setAISkin(Squadron squad, PlaneMcu plane, Date date) throws PWCGException 
    {        
        // Start with setting the squadron livery
        setSquadronSkin(squad, plane, date);

        if (plane.getAiLevel() != AiSkillLevel.NOVICE)
        {            
            // Choose a personal skin for that squadron if one is available
            List<Skin> squadronSkins = PWCGContext.getInstance().getSkinManager().getSkinsByPlaneSquadronDateInUse(plane.getType(), squad.getSquadronId(), date);
            Skin skin = chooseSquadronPersonalSkin(plane, squadronSkins);

            // Non squadron personal skin is a less good option
            if (skin == null)
            {
                List<Skin> nonSquadronPersonalSkin = PWCGContext.getInstance().getSkinManager().getPersonalSkinsByPlaneCountryDateInUse(
                                plane.getType(), 
                                squad.determineSquadronCountry(date).getCountryName(), 
                                date);
                
                skin = chooseNonSquadronPersonalSkin(plane, nonSquadronPersonalSkin);
            }
        }
        else
        {
            // For generic skins we don't care if they are in use
            List<Skin> factorySkins = PWCGContext.getInstance().getSkinManager().getSkinsBySquadronPlaneDate(plane.getType(), Skin.FACTORY_GENERIC, date);
            chooseNoviceSkin(plane, factorySkins);
        }
     }

    private Skin chooseSquadronPersonalSkin(PlaneMcu plane, List<Skin> squadronSkins)
    {
        // If we don't have a skin try squadron personal
        Skin skin = pickSkin(plane.getType(), squadronSkins);
        if (skin != null)
        {
            plane.setPlaneSkin(skin);
            PWCGContext.getInstance().getSkinManager().getSkinsInUse().addSkinInUse(skin);
            PWCGLogger.log(LogLevel.DEBUG, "SKIN: Assign squadron personal: " + skin.getSkinName());
        }
        else
        {
            PWCGLogger.log(LogLevel.DEBUG, "SKIN: no squadron personal skin available");
        }
        
        return skin;
    }

    private Skin chooseNonSquadronPersonalSkin(PlaneMcu plane, List<Skin> personalSkins)
    {
        Skin skin = null;
        
        if (!(plane.getAiLevel().lessThan(AiSkillLevel.VETERAN)))
        {
            skin = pickSkin(plane.getType(), personalSkins);
            if (skin != null)
            {
                plane.setPlaneSkin(skin);
                PWCGContext.getInstance().getSkinManager().getSkinsInUse().addSkinInUse(skin);
                PWCGLogger.log(LogLevel.DEBUG, "SKIN: Assign non squadron personal: " + skin.getSkinName());
            }
            else
            {
                PWCGLogger.log(LogLevel.DEBUG, "SKIN: no non squadron personal skin available");
            }
        }
        else
        {
            PWCGLogger.log(LogLevel.DEBUG, "SKIN: non squadron personal skin - must be veteran or higher");
        }

        return skin;
    }

    private void chooseNoviceSkin(PlaneMcu plane, List<Skin> genericSkins)
    {
        PWCGLogger.log(LogLevel.DEBUG, "SKIN: Choose novice skin");
        
        // Novice pilots get either the squadron skin or an unmarked
        int genericRoll = RandomNumberGenerator.getRandom(100);
        if (genericRoll < 40)
        {
            Skin skin = pickSkin(plane.getType(), genericSkins);
            if (skin != null)
            {
                // This is a factory generic skin so no need to declare it to be in use
                plane.setPlaneSkin(skin);
                PWCGLogger.log(LogLevel.DEBUG, "SKIN: Assign generic skin: " + skin.getSkinName());
            }
            else
            {
                PWCGLogger.log(LogLevel.DEBUG, "SKIN: novice skin - no generic available");
            }
        }
        else
        {
            PWCGLogger.log(LogLevel.DEBUG, "SKIN: retain novice skin");
        }
    }

    private Skin pickSkin(String planeName, List<Skin> skinSet)
    {              
        Skin skin = null;
        
        if (skinSet.size() > 0)
        {
            int squadronSkinIndex = RandomNumberGenerator.getRandom(skinSet.size());
            skin = skinSet.get(squadronSkinIndex);
        }
        
        return skin;

    }

    private void setSquadronSkin(Squadron squadron, PlaneMcu plane, Date date) 
    {
        Skin selectedSkin = null;
        
        // Start by setting to squadron livery
        List<Skin> squadronSkins = PWCGContext.getInstance().getSkinManager().getSquadronSkinsByPlaneSquadronDate(plane.getType(), squadron.getSquadronId(), date);
        for (Skin squadSkin : squadronSkins)
        {
            if (squadSkin.getPlane() == null)
            {
                continue;
            }
            
            if (squadSkin.getPlane().isEmpty())
            {
                continue;
            }

            // Right plane type
            if (!squadSkin.getPlane().equalsIgnoreCase(plane.getType()))
            {
                continue;
            }

            // Skin should be in date range
            if (squadSkin.getStartDate().after(date) || squadSkin.getEndDate().before(date))
            {
                continue;
            }
            
            selectedSkin = squadSkin;
        }

        if (selectedSkin != null)
        {
            // This is a squadron default skin so no need to declare it to be in use
            plane.setPlaneSkin(selectedSkin);
        }
    }

	private void setUserAssignedPilotSkin(SquadronMember pilot, PlaneMcu plane) 
	{
        Campaign campaign = PWCGContext.getInstance().getCampaign();
        
		// Specific pilot designated skins override generic skins
		for (Skin pilotSkin : pilot.getSkins())
		{
		    if (pilotSkin.getStartDate().after(campaign.getDate()) || pilotSkin.getEndDate().before(campaign.getDate()))
		    {
		        continue;
		    }
	            
		    try
		    {
    		    // Wrong plane
    		    if (pilotSkin.getPlane() != null && 
    		        pilotSkin.getPlane().length() > 0 &&
                    !pilotSkin.getPlane().equals(plane.getType()))
    		    {
    		        continue;
    		    }
    
                plane.setPlaneSkin(pilotSkin);
                PWCGContext.getInstance().getSkinManager().getSkinsInUse().addSkinInUse(pilotSkin);
		    }
		    catch (Exception exp)
		    {
	            PWCGLogger.logException(exp);
		    }
		}
	}
}
