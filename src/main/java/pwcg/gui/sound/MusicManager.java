package pwcg.gui.sound;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;
import pwcg.gui.dialogs.ErrorDialog;

public class MusicManager
{

	public static void playTitleTheme() 
	{
        playTheme("titletheme.wav");
	}

	public static void playCampaignTheme(Campaign campaign) throws PWCGException 
	{
		if (campaign.determineCountry().getSide() == Side.ALLIED)
		{
			playTheme("alliedcampaign.wav");
		}
		else
		{
			playTheme("axiscampaign.wav");
		}
	}

	
	public static void playMissionBriefingTheme() 
	{
		playTheme("battleprepnohorn.wav");
	}

	public static void playMissionStatusTheme(boolean missionWasSuccessful) 
	{
		if (missionWasSuccessful)
		{
			playTheme("win.wav");
		}
		else
		{
			playTheme("lose.wav");
		}
	}

	private static void playTheme(String musicFile) 
	{

		try
		{
			SoundManager.getInstance().playMusic(musicFile);
		}
		catch (Exception e)
		{
			Logger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}
}
