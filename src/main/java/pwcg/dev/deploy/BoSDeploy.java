package pwcg.dev.deploy;

import java.util.HashMap;

public class BoSDeploy extends DeployBase
{
    static public void main (String[] args)
    {
        BoSDeploy bosDeploy = new BoSDeploy();
        bosDeploy.doDeploy();
    }

	public BoSDeploy()
	{
        targetFinalDir = "D:\\PWCG\\Deploy\\PWCGBoS";
	}

    @Override
	protected HashMap<String, Object> loadDirectoriesToCopyPWCG() 
	{		
		super.loadDirectoriesToCopyPWCG();

        directoriesToCopy.put("BoSData", null);
        directoriesToCopy.put("Coop", null);

        // Maps
        directoriesToCopy.put("Stalingrad", null);
        directoriesToCopy.put("Moscow", null);
        directoriesToCopy.put("Kuban", null);
        directoriesToCopy.put("East1944", null);
        directoriesToCopy.put("East1945", null);
        directoriesToCopy.put("Bodenplatte", null);        

		// Moscow dates
        directoriesToCopy.put("19411001", null);
        directoriesToCopy.put("19411020", null);
        directoriesToCopy.put("19411110", null);
        directoriesToCopy.put("19411120", null);
        directoriesToCopy.put("19411215", null);
        directoriesToCopy.put("19420110", null);
        
		// Stalingrad dates
        directoriesToCopy.put("19420301", null);
        directoriesToCopy.put("19420801", null);
        directoriesToCopy.put("19420906", null);
        directoriesToCopy.put("19421011", null);
        directoriesToCopy.put("19421123", null);
        directoriesToCopy.put("19421223", null);
        directoriesToCopy.put("19430120", null);
        
		// Kuban dates
        directoriesToCopy.put("19420601", null);
        directoriesToCopy.put("19420624", null);
        directoriesToCopy.put("19420709", null);
        directoriesToCopy.put("19420721", null);
        directoriesToCopy.put("19430301", null);
        directoriesToCopy.put("19430330", null);
        directoriesToCopy.put("19430418", null);
        directoriesToCopy.put("19430918", null);
        directoriesToCopy.put("19430927", null);
        directoriesToCopy.put("19431004", null);
        directoriesToCopy.put("19431008", null);
        
        // East Front 1944 dates
        directoriesToCopy.put("19440101", null);
        directoriesToCopy.put("19440201", null);
        directoriesToCopy.put("19440301", null);
        directoriesToCopy.put("19440401", null);
        directoriesToCopy.put("19440501", null);
        directoriesToCopy.put("19440601", null);
        directoriesToCopy.put("19440701", null);
        directoriesToCopy.put("19440801", null);
        directoriesToCopy.put("19440901", null);
        directoriesToCopy.put("19441001", null);
        directoriesToCopy.put("19441101", null);
        directoriesToCopy.put("19441201", null);

        // Bodenplatte dates
        directoriesToCopy.put("19440901", null);
        directoriesToCopy.put("19441001", null);
        directoriesToCopy.put("19441101", null);
        directoriesToCopy.put("19441220", null);
        directoriesToCopy.put("19441225", null);
        directoriesToCopy.put("19441229", null);
        directoriesToCopy.put("19450207", null);
        directoriesToCopy.put("19450310", null);
        directoriesToCopy.put("19450404", null);

		return directoriesToCopy;
	}
	
	   
    @Override
    protected HashMap<String, Object> loadUnwantedFiles() 
    {
        super.loadUnwantedFiles();
        unwantedFiles.put("PWCGFC.ico", null);
        return unwantedFiles;
    }
}
