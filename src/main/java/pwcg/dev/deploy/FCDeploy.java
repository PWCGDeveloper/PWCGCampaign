package pwcg.dev.deploy;

import java.util.HashMap;

public class FCDeploy extends DeployBase
{
    public FCDeploy()
    {
        sourceRootDir = "D:\\PWCG\\workspacePwcg2023\\PWCGCampaign";
        deployDir = "D:\\PWCG\\Deploy";
        targetDir = "D:\\PWCG\\Deploy\\PWCGCampaign";
        targetFinalDir = "D:\\PWCG\\Deploy\\PWCGFC";
    }

    @Override
	protected HashMap<String, Object> loadDirectoriesToCopyPWCG() 
	{		
		super.loadDirectoriesToCopyPWCG();
		
        directoriesToCopy.put("FCData", null);
        directoriesToCopy.put("Coop", null);

        // Service specific pilot images
        directoriesToCopy.put("GAS", null);
        directoriesToCopy.put("RFC", null);
        directoriesToCopy.put("RNAS", null);

        // Maps
        directoriesToCopy.put("WesternFront", null);

		// Moving Front Dates
        directoriesToCopy.put("19160101", null);
        directoriesToCopy.put("19180301", null);
        directoriesToCopy.put("19180401", null);
        directoriesToCopy.put("19180501", null);
        directoriesToCopy.put("19180601", null);
        directoriesToCopy.put("19180701", null);
        directoriesToCopy.put("19180901", null);
        directoriesToCopy.put("19181001", null);
        directoriesToCopy.put("19181101", null);
        
		// From Input/Aces
		directoriesToCopy.put("Pictures", null);
		
		return directoriesToCopy;
	}
	
	@Override
    protected HashMap<String, Object> loadUnwantedFiles() 
    {
        super.loadUnwantedFiles();
        unwantedFiles.put("PWCGBoS.ico", null);
        return unwantedFiles;
    }
}
