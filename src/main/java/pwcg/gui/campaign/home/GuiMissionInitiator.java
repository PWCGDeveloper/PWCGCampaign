package pwcg.gui.campaign.home;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.group.AirfieldManager;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.MissionHumanParticipants;

public class GuiMissionInitiator 
{
	private Campaign campaign;
    private MissionHumanParticipants participatingPlayers;

	public GuiMissionInitiator(Campaign campaign, MissionHumanParticipants participatingPlayers)
	{
		this.campaign = campaign;
		this.participatingPlayers = participatingPlayers;
	}

	public Mission makeMission(boolean isLoneWolf) throws PWCGException 
    {
        Mission mission = null;

        if (!(campaign.getDate().before(DateUtils.getEndOfWar())))
        {
            throw new PWCGUserException ("The war is over.  Go home.");
        }
        else if (endOfEast())
        {
            throw new PWCGUserException ("Eastern front operations have ended.  Transfer or go home.");
        }
        else
        {
            if (campaign.getCurrentMission() == null)
            {
                MissionGenerator missionGenerator = new MissionGenerator(campaign);
                if (isLoneWolf)
                {
                    mission = missionGenerator.makeLoneWolfMission(participatingPlayers);
                }
                else
                {
                    mission = missionGenerator.makeMission(participatingPlayers);                    
                }
            }
            else
            {
                mission = campaign.getCurrentMission();
            }
        }

        return mission;
    }

    private boolean endOfEast() throws PWCGException 
    {
        // We want to know if we are on the Galician map.
        // Get the maps for the airfield.  If the airfield is on the Galician
        // map then it can only be on that map (some Western airfields are
        // on both the France and Channel maps)
    	
    	SquadronMember referencePlayer = PWCGContextManager.getInstance().getReferencePlayer();
        String airfieldName = referencePlayer.determineSquadron().determineCurrentAirfieldName(campaign.getDate());
        List<FrontMapIdentifier> mapsForAirfield =  AirfieldManager.getMapIdForAirfield(airfieldName);
        FrontMapIdentifier mapId = mapsForAirfield.get(0);

        if (mapId == FrontMapIdentifier.GALICIA_MAP)
        {
            if (campaign.getDate().after(DateUtils.getEndOfWWIRussia()))
            {
                return true;
            }
        }
        return false;
    }
}
