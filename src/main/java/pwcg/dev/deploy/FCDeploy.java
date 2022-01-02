package pwcg.dev.deploy;

import java.util.HashMap;

public class FCDeploy extends DeployBase
{
    public FCDeploy()
    {
        sourceRootDir = "D:\\PWCG\\workspacePwcg\\PWCGCampaign";
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

        // Service specific crewMember images
        directoriesToCopy.put("GAS", null);
        directoriesToCopy.put("RFC", null);
        directoriesToCopy.put("RNAS", null);

        // Maps
        directoriesToCopy.put("Arras", null);

		// Moving Front Dates
    	directoriesToCopy.put("19170601", null);
        
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
