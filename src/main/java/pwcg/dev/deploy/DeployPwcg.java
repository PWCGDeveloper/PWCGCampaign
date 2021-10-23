package pwcg.dev.deploy;

import pwcg.campaign.utils.TestDriver;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class DeployPwcg extends DeployBase
{
    static public void main (String[] args)
    {
        try
        {
            PWCGLogger.log(LogLevel.INFO, "************  STARTED  **********");

            if (TestDriver.getInstance().isEnabled())
            {
                PWCGLogger.log(LogLevel.ERROR, "************  NO DEPLOY - TEST DRIVER ENABLED  **********");
                return;
            }
    
            BoSDeploy bosDeploy = new BoSDeploy();
            bosDeploy.doDeploy();
            
            FCDeploy fcDeploy = new FCDeploy();
            fcDeploy.doDeploy();
            
            PWCGJarCopy.copyJar();

            PWCGLogger.log(LogLevel.INFO, "************  DONE  **********");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
