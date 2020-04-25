package pwcg.gui.rofmap.debrief;

import pwcg.core.utils.PWCGLogger;


public class DebriefEventDisplayInitiator implements Runnable 
{
	private DebriefMapPanel mapPanel = null;

	public DebriefEventDisplayInitiator(DebriefMapPanel mapPanel)
	{
		this.mapPanel = mapPanel;
	}

	@Override
	public void run() 
	{
		try
		{
			Thread driverThreadprev = null;
			for (int i = 0; i < mapPanel.getEventPoints().size(); ++i)
			{
				DebriefEventDisplayDriver driver = new DebriefEventDisplayDriver(mapPanel, driverThreadprev);
				Thread driverThread = new Thread(driver);
				driverThreadprev = driverThread;
				driverThread.start();
				Thread.sleep(100);
			}		
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
		}
	}

}
