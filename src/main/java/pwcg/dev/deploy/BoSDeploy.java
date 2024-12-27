package pwcg.dev.deploy;

import java.util.HashMap;

public class BoSDeploy extends DeployBase
{
	public BoSDeploy()
	{
        targetFinalDir = "C:\\PWCG\\Deploy\\PWCGBoS";
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
        directoriesToCopy.put("Normandy", null);        
        directoriesToCopy.put("Bodenplatte", null);        

        // Normandy dates
        directoriesToCopy.put("19410601", null);
        directoriesToCopy.put("19410610", null);
        directoriesToCopy.put("19410717", null);
        directoriesToCopy.put("19410813", null);
        directoriesToCopy.put("19410902", null);
        directoriesToCopy.put("19411101", null);
        directoriesToCopy.put("19420819", null);
        directoriesToCopy.put("19420820", null);
        directoriesToCopy.put("19430601", null);
        directoriesToCopy.put("19440501", null);
        directoriesToCopy.put("19440606", null);
        directoriesToCopy.put("19440609", null);
        directoriesToCopy.put("19440620", null);
        directoriesToCopy.put("19440627", null);
        directoriesToCopy.put("19440721", null);
        directoriesToCopy.put("19440731", null);
        directoriesToCopy.put("19440808", null);
        directoriesToCopy.put("19440813", null);
        directoriesToCopy.put("19440821", null);
        directoriesToCopy.put("19440901", null);

        // Bodenplatte dates
        directoriesToCopy.put("19440901", null);
        directoriesToCopy.put("19440917", null);
        directoriesToCopy.put("19440925", null);
        directoriesToCopy.put("19440928", null);
        directoriesToCopy.put("19440930", null);
        directoriesToCopy.put("19441101", null);
        directoriesToCopy.put("19441220", null);
        directoriesToCopy.put("19441225", null);
        directoriesToCopy.put("19441229", null);
        directoriesToCopy.put("19450207", null);
        directoriesToCopy.put("19450310", null);
        directoriesToCopy.put("19450323", null);
        directoriesToCopy.put("19450324", null);
        directoriesToCopy.put("19450404", null);

		// Moscow dates
        directoriesToCopy.put("19411001", null);
        directoriesToCopy.put("19411005", null);
        directoriesToCopy.put("19411011", null);
        directoriesToCopy.put("19411016", null);
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
        directoriesToCopy.put("19431101", null);
        directoriesToCopy.put("19431111", null);
        
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
        
        // East Front 1945 dates
        directoriesToCopy.put("19450101", null);
        directoriesToCopy.put("19450201", null);
        directoriesToCopy.put("19450301", null);
        directoriesToCopy.put("19450401", null);
        directoriesToCopy.put("19450501", null);
        
        // Service Themes
        directoriesToCopy.put("Luftwaffe", null);
        directoriesToCopy.put("Royal Air Force", null);
        directoriesToCopy.put("Voyenno-Vozdushnye Sily", null);
        directoriesToCopy.put("Female", null);
        directoriesToCopy.put("PaperDoll", null);

        directoriesToCopy.put("Germany", null);
        directoriesToCopy.put("Britain", null);
        directoriesToCopy.put("Russia", null);
        directoriesToCopy.put("USA", null);

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
