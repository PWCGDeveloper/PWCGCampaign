package pwcg.gui.rofmap.brief;

import pwcg.core.exception.PWCGException;
import pwcg.gui.rofmap.brief.builder.BriefingDataBuilder;
import pwcg.gui.rofmap.brief.model.BriefingData;
import pwcg.mission.Mission;

public class BriefingContext
{
    private static BriefingContext instance = new BriefingContext();
    private BriefingData briefingData;
    
    public static BriefingContext getInstance()
    {
        return instance;
    }
    
    public void buildBriefingData(Mission mission) throws PWCGException
    {
        BriefingDataBuilder briefingDataBuilder = new BriefingDataBuilder(mission);
        briefingData = briefingDataBuilder.buildBriefingData();
    }

    public BriefingData getBriefingData()
    {
        return briefingData;
    }
}
