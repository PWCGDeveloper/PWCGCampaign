package pwcg.gui.sound;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerGlobal;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;


public class SoundManager extends Thread
{
	private static SoundManager instance = null;
	
    private SoundPlayer musicPlayer = new SoundPlayer();
    private SoundPlayer soundPlayer = new SoundPlayer();
    private List<String> musicQueue = new ArrayList<String>();
    private int musicCurrentVolume = 10;
    private int musicTargetVolume = 10;
    private int soundVolume = 10;
    private int playMusic = 1;
    private int playSounds = 1;
    private String currentSong = "";

	private SoundManager ()
	{
	}

	public static SoundManager getInstance()
	{
		if (instance == null)
		{
			instance = new SoundManager();
			instance.initialize();
			instance.start();
		}
		
		return instance;
	}
	
	public void initialize()
	{
	    try
	    {
	        musicTargetVolume = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.MusicVolumeKey);
            soundVolume = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.SoundVolumeKey);
            playMusic = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.PlayMusicKey);
            playSounds = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.PlaySoundsKey);

            toggleMusic();
	    }
	    catch (Exception exp)
	    {
	        
	    }
	}
	
	private void toggleMusic()
	{
        if (playMusic != 1)
        {
            stopMusic();        }
	}

    @Override
    public void run()
    {
        while (true)
        {
            int sleepTime = 5000;
            try
            {
                // Avoid simultaneous access to the queue
                synchronized(this)
                {
        	        if (musicQueue.size() > 0)
                    {                    
                        sleepTime = transitionToNewSong(sleepTime);
                    }
                    else if (musicPlayer.isClipRunning() && (musicCurrentVolume < musicTargetVolume))
                    {
                        sleepTime = fadeMusicIn();
                    }
                    else if (musicPlayer.isClipRunning() && (musicCurrentVolume > musicTargetVolume))
                    {
                        sleepTime = fadeMusicOut();
                    }
                }

                Thread.sleep(sleepTime);
            }
            catch (Exception exp)
            {
            	Logger.logException(exp);
            }
        }
    }

	private int transitionToNewSong(int sleepTime) throws PWCGException
	{
		if (playMusic == 1)
		{
			if (musicPlayer.isClipRunning() && musicCurrentVolume > 1)
			{
		        sleepTime = fadeMusicOut();
			}
		else
			{
		        sleepTime = playNewSong();
			}
		}
		return sleepTime;
	}

	private int playNewSong() throws PWCGException
	{
		int sleepTime;
		String nextSong = musicQueue.remove(0);
		if (nextSong != null)
		{
		    musicPlayer.stopPlay();
		    musicPlayer.playClipFormatted(nextSong, musicCurrentVolume, true);
		    currentSong = nextSong;
		}

		musicCurrentVolume = 1;
		musicPlayer.changeVolume(musicCurrentVolume);

		sleepTime = 5000;
		return sleepTime;
	}

	private int fadeMusicOut() throws PWCGException
	{
		int sleepTime;
		--musicCurrentVolume;
		musicPlayer.changeVolume(musicCurrentVolume);
		sleepTime = 1000;
		return sleepTime;
	}

	private int fadeMusicIn() throws PWCGException
	{
		int sleepTime;
		++musicCurrentVolume;
		musicPlayer.changeVolume(musicCurrentVolume);
		sleepTime = 1000;
		return sleepTime;
	}

    /**
     * @param audioFile
     */
    public void playSound(String audioFile)
    {
        try
        {
            if (playSounds == 1)
            {
                soundPlayer.stopPlay();
                if (audioFile != null)
                {
                    soundPlayer.playClipFormatted(audioFile, soundVolume, false);
                }
            }
        }
        catch (Exception exp)
        {
        }
    }
    
    public void playMusic(String audioFile)
    {
        try
        {
            // Avoid simultaneous access to the queue
            synchronized(this)
            {
                // Only one song in the queue at a time.
                // This way we will always be playing the best song per context
                musicQueue.clear();
                
                // Do not queue the same song that we are already playing.  Let the current instance loop.
                if (!audioFile.equals(currentSong))
                {
                    musicQueue.add(audioFile);
                }
            }
        }
        catch (Exception exp)
        {
             Logger.logException(exp);;
        }
    }
    
    
    
    public void stopMusic()
    {
        try
        {
            musicQueue.clear();
            currentSong = "";
            musicPlayer.stopPlay();
        }
        catch (Exception exp)
        {

        }
    }

    
    public int getMusicVolume()
    {
        return musicTargetVolume;
    }

    public void setMusicVolume(int musicTargetVolume) throws PWCGException
    {
    	if (musicTargetVolume < 0)
    	{
    		musicTargetVolume = 0;
    	}
    	
    	if (musicTargetVolume > 10)
    	{
    		musicTargetVolume = 10;
    	}
    	
        this.musicTargetVolume = musicTargetVolume;
    }

    public int getSoundVolume()
    {
        return soundVolume;
    }

    public void setSoundVolume(int soundVolume)
    {
        this.soundVolume = soundVolume;
        soundPlayer.changeVolume(soundVolume);
    }

    public int getPlayMusic()
	{
		return playMusic;
	}

	public void setPlayMusic(int playMusic)
	{
		this.playMusic = playMusic;
        toggleMusic();
	}

	public int getPlaySounds()
	{
		return playSounds;
	}

	public void setPlaySounds(int playSounds)
	{
		this.playSounds = playSounds;
	}

 }
