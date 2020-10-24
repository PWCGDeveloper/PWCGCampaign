package pwcg.mission.flight.validate;

import java.util.List;

public class IndexLinkValidator
{
    public static boolean isIndexInTargetList(List<String>targetsFromMCU, int targetIndexToFind) 
    {
        boolean isIndexInTargetList = false;
        for (String targetIndex : targetsFromMCU)
        {
            if (targetIndex.equals(new String("" + targetIndexToFind)))
            {
                isIndexInTargetList = true; 
            }
        }
        return isIndexInTargetList;
    }

}
