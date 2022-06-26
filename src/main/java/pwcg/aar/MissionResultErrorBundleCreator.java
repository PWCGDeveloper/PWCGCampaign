package pwcg.aar;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGErrorBundler;

public class MissionResultErrorBundleCreator
{
    public String createErrorBundle(Campaign campaign) throws PWCGException 
    {
        PWCGErrorBundler errorBundler = new PWCGErrorBundler(campaign);
        errorBundler.bundleDebuggingData();
        String errorBundleFileName =  errorBundler.getTargetErrorFileName() + ".zip";
        
        return errorBundleFileName;  
    }
}
