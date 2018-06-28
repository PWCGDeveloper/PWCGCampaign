package pwcg.aar;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGErrorBundler;

public class MissionResultErrorBundleCreator
{
    public String createErrorBundle() throws PWCGException 
    {
        PWCGErrorBundler errorBundler = new PWCGErrorBundler();
        errorBundler.bundleDebuggingData();
        String errorBundleFileName =  errorBundler.getTargetErrorFileName() + ".zip";
        
        return errorBundleFileName;  
    }
}
