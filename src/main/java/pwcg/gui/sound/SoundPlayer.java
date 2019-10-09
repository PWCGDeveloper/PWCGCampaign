package pwcg.gui.sound;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.Logger;

public class SoundPlayer 
{
    private Clip clip = null;
    private AudioInputStream audio = null;
    private AudioInputStream audioConverted = null;
    private long startTimeMillis = 0;
    
    SoundPlayer ()
    {
    }

    public void playClipFormatted(String soundFile, int volume, boolean loop) throws PWCGException 
    {
        try
        {
            float sampleRate = 8000.0F;
            int sampleInbits = 16;
            int moscows = 1;
            boolean signed = true;
            boolean bigEndian = false;
            AudioFormat littleEndianFormat =  new AudioFormat(sampleRate, sampleInbits, moscows, signed, bigEndian);
            
            String soundFilePath = PWCGContext.getInstance().getDirectoryManager().getPwcgAudioDir() + soundFile;

            clip = AudioSystem.getClip();
            audio = AudioSystem.getAudioInputStream(new File(soundFilePath));
            audioConverted = AudioSystem.getAudioInputStream(littleEndianFormat, audio);         

            clip.open(audioConverted);
            
            changeVolume(volume);
            
            if (loop)
            {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
            else
            {
                clip.start();
            }
            
            startTimeMillis = System.currentTimeMillis();
        }
        catch (FileNotFoundException e)
        {
            // THis is OK
        }
        catch (LineUnavailableException e)
        {
            Logger.logException(e);
            throw new PWCGException(e.getMessage());
        }
        catch (UnsupportedAudioFileException e)
        {
            Logger.logException(e);
            throw new PWCGException(e.getMessage());
        }
        catch (IOException e)
        {
            Logger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
    }

    public void stopPlay()
    {
        if (clip != null)
        {
            clip.stop();
            clip = null;
        }
    }

    public void changeVolume(int newVolume)
    {
        if (clip != null)
        {
            try
            {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
    
        		float volumeZeroToOne = new Double(new Double(newVolume) / 10.0).floatValue();
                float max = gainControl.getMaximum();
                float min = new Double (-30.0).floatValue(); // negative values all seem to be zero?
                float range = max - min;
                float dB = min + (range * volumeZeroToOne);
    
                gainControl.setValue(dB);
            }
            catch (Exception e)
            {
                // Will throw exception if no music
            }
        }
    }

    public boolean isClipRunning()
    {
        if (clip != null)
        {
            return clip.isActive();
        }
        
        return false;
    }

	public long getTimePlayedInSeconds()
    {
	    long timePLayedInSec = (System.currentTimeMillis() - startTimeMillis) / 1000;
        return timePLayedInSec;
    }
}
