package pwcg.aar.inmission.phase2.logeval.missionresultentity;

public class LogEntityPlaneResolver 
{

	public static LogPlane getPlaneForEntity(LogAIEntity victor) {
    	LogPlane victorPlane = null;
		if (victor instanceof LogPlane) 
		{
			victorPlane = (LogPlane)victor;
		}
		else if (victor instanceof LogTurret)
		{
			LogTurret logTurret = (LogTurret)victor;
			if (logTurret.getParent() instanceof LogPlane)
			{
				victorPlane = (LogPlane)logTurret.getParent();
			}
		}
		
		return victorPlane;
	}

}
