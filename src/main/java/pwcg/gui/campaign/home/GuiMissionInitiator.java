package pwcg.gui.campaign.home;

import pwcg.campaign.Campaign;
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
}
