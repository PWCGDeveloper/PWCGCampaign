package pwcg.campaign.skirmish;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.io.json.IconicMissionsIOJson;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;

public class IconicMissionsManager
{
	private List<IconicMission> iconicMissions = new ArrayList<>();
    
    private static IconicMissionsManager instance = null; 

    public static IconicMissionsManager getInstance()
    {
        if (instance == null)
        {
            instance = new IconicMissionsManager();
            instance.initialize();
        }
        return instance;
    }
    
    private IconicMissionsManager ()
    {
    }
    
    private void initialize()
	{
        try
        {
            iconicMissions = IconicMissionsIOJson.readJson();
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
        }
	}

    public IconicMission getSelectedMissionProfile(String missionKey) throws PWCGException 
    {     
        for (IconicMission iconicMission : iconicMissions)
        {
            if (iconicMission.getDateString().equals(missionKey))
            {
                return iconicMission;
            }
        }
        throw new PWCGException("No iconic mission found for key " + missionKey);
    }

    public List<IconicMission> getIconicMissions()
    {
        return iconicMissions;
    }
}
