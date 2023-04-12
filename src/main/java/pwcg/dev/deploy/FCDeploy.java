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

        // Service specific pilot images
        directoriesToCopy.put("GAS", null);
        directoriesToCopy.put("RFC", null);
        directoriesToCopy.put("RNAS", null);

        // Maps
        directoriesToCopy.put("WesternFront", null);

		// Moving Front Dates
        directoriesToCopy.put("19170601", null);
        directoriesToCopy.put("19170607", null);
        directoriesToCopy.put("19170621", null);
        directoriesToCopy.put("19170720", null);
        directoriesToCopy.put("19171020", null);
        directoriesToCopy.put("19171030", null);
        directoriesToCopy.put("19171115", null);
        directoriesToCopy.put("19171215", null);
        directoriesToCopy.put("19180310", null);
        directoriesToCopy.put("19180329", null);
        directoriesToCopy.put("19180404", null);
        directoriesToCopy.put("19180429", null);
        directoriesToCopy.put("19180531", null);
        directoriesToCopy.put("19180604", null);
        directoriesToCopy.put("19180612", null);
        directoriesToCopy.put("19180918", null);
        directoriesToCopy.put("19180925", null);
        directoriesToCopy.put("19181004", null);
        directoriesToCopy.put("19181015", null);
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
