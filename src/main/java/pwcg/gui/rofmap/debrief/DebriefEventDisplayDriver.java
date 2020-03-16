package pwcg.gui.rofmap.debrief;

import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerGlobal;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.rofmap.debrief.DebriefMapPanel.DebriefStates;

public class DebriefEventDisplayDriver implements Runnable 
{
	private DebriefMapPanel mapPanel = null;
	private Thread prev;

	public DebriefEventDisplayDriver(DebriefMapPanel mapPanel, Thread prev)
	{
		this.mapPanel = mapPanel;
		this.prev = prev;
	}

	@Override
	public void run() 
	{
		try
		{
			if (prev != null)
			{
				prev.join(3000);
			}
			
			int waitTime = mapPanel.setDebriefState(DebriefStates.NEXT);
			if (waitTime == -1)
			{
				int debriefSpeed = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.DebriefSpeedKey);
				waitTime = debriefSpeed * 1000;
			}
			
			Thread.sleep(waitTime);
		}
		catch (InterruptedException e)
		{
			// we expect this
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
		}
	}
}
