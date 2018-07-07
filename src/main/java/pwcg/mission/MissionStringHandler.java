package pwcg.mission;

import java.util.Map;
import java.util.TreeMap;

public class MissionStringHandler
{
    private static MissionStringHandler instance = new MissionStringHandler();
    
    private Map<Integer, String> subTitleText = new TreeMap<Integer, String>();

    private MissionStringHandler()
    {
    }
    
    public static  MissionStringHandler getInstance()
    {
        return instance;
    }

    public void clear()
    {
        subTitleText.clear();
    }

    public void registerMissionText(int lcTextIndex, String lcText)
    {
        subTitleText.put(lcTextIndex, lcText);
    }

    public Map<Integer, String> getMissionText()
    {
        return this.subTitleText;
    }

}
