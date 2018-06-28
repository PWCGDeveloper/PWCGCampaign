package pwcg.mission;

import java.util.Map;
import java.util.TreeMap;

/**
 * Used for subtitle and icon descriptions
 * 
 * @author Patrick Wilson
 *
 */
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
    
    /**
     * @param subTitle
     */
    public void clear()
    {
        subTitleText.clear();
    }
    
    /**
     * @param subTitle
     */
    public void registerMissionText(int lcTextIndex, String lcText)
    {
        subTitleText.put(lcTextIndex, lcText);
    }

    /**
     * @return the subTitleText
     */
    public Map<Integer, String> getMissionText()
    {
        return this.subTitleText;
    }

}
