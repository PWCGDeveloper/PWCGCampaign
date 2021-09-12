package pwcg.gui.maingui;

import java.awt.BorderLayout;
import javafx.scene.text.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javafx.scene.control.Button;
import javax.swing.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerGlobal;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.sound.MusicManager;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ButtonFactory;

public class PwcgMusicConfigScreen extends ImageResizingPanel implements ActionListener, ChangeListener
{    
    private static final long serialVersionUID = 1L;
    
    private CheckBox playMusicCheckBox;
    private CheckBox playSoundsCheckBox;
    private JSlider playMusicVolume;
    private JSlider playSoundsVolume;
    private PwcgMainScreen parent = null;

    public PwcgMusicConfigScreen(PwcgMainScreen parent)
    {
        super("");
        this.setLayout(new BorderLayout());

        this.parent = parent;
    }

    public void makePanels() 
    {
        try
        {
            String imagePath = UiImageResolver.getImage(ScreenIdentifier.PwcgMusicConfigScreen);
            this.setImageFromName(imagePath);
            
            this.add(BorderLayout.WEST, makeButtonPanel());
            this.add(BorderLayout.CENTER, makeCampaignSelectPanel());
        }
        catch (Throwable e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private Pane makeButtonPanel() throws PWCGException
    {
        Pane navPanel = new Pane(new BorderLayout());
        navPanel.setOpaque(false);


        Pane buttonPanel = new Pane(new GridLayout(6,1));
        buttonPanel.setOpaque(false);

        Button acceptButton = ButtonFactory.makeTranslucentMenuButton("Accept", "AcceptChanges", "Accept music configuration", this);
        buttonPanel.add(acceptButton);
        
        Label dummyLabel3 = new Label("     ");       
        dummyLabel3.setOpaque(false);
        buttonPanel.add(dummyLabel3);
        
        Button cancelButton = ButtonFactory.makeTranslucentMenuButton("Cancel", "Cancel", "Cancel music configuration changes", this);
        buttonPanel.add(cancelButton);

        navPanel.add(buttonPanel, BorderLayout.NORTH);
     
        return navPanel;
    }

    private Pane makeCampaignSelectPanel() throws PWCGException
    {
        Pane musicControlPanel = new Pane(new BorderLayout());
        musicControlPanel.setOpaque(false);

        Pane musicControlGrid = new Pane(new GridLayout(0,1));
        musicControlGrid.setOpaque(false);        
        makePlayMusic();
        makePlaySound();
	    playMusicVolume = makeVolumeSlider();
	    playSoundsVolume = makeVolumeSlider();
        musicControlGrid.add(playSoundsCheckBox);
        musicControlGrid.add(playSoundsVolume);
        musicControlGrid.add(new Label(""));
        musicControlGrid.add(playMusicCheckBox);
        musicControlGrid.add(playMusicVolume);

        musicControlPanel.add(musicControlGrid, BorderLayout.NORTH);
        
        initializeWidgetValues();
        
        return musicControlPanel;
    }

	private void makePlaySound() throws PWCGException
	{
        Font font = PWCGMonitorFonts.getPrimaryFontLarge();

        playSoundsCheckBox = new CheckBox();
        playSoundsCheckBox.setText("Play Sounds");
        playSoundsCheckBox.setSelected(false);
        playSoundsCheckBox.setOpaque(false);
        playSoundsCheckBox.setFont(font);
        playSoundsCheckBox.addChangeListener(this);
        playSoundsCheckBox.setForeground(ColorMap.CHALK_FOREGROUND);
	}

	private void makePlayMusic() throws PWCGException
	{
        Font font = PWCGMonitorFonts.getPrimaryFontLarge();

        playMusicCheckBox = new CheckBox();
        playMusicCheckBox.setText("Play Music");
        playMusicCheckBox.setSelected(false);
        playMusicCheckBox.setOpaque(false);
        playMusicCheckBox.setFont(font);
        playMusicCheckBox.addChangeListener(this);
        playMusicCheckBox.setForeground(ColorMap.CHALK_FOREGROUND);
	}
	
	  public JSlider makeVolumeSlider() 
	  {
		    JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 10, 0);
		    slider.setMinorTickSpacing(2);
		    slider.setMajorTickSpacing(10);
		    slider.setPaintTicks(true);
		    slider.setPaintLabels(true);
		    slider.setLabelTable(slider.createStandardLabels(10));
		    slider.addChangeListener(this);

		    return slider;
	  }

	private void initializeWidgetValues() throws PWCGException
	{
        int playMusic = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.PlayMusicKey);
        if (playMusic == 1)
        {
        	playMusicCheckBox.setSelected(true);
        }
        
        int playSounds = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.PlaySoundsKey);
        if (playSounds == 1)
        {
        	playSoundsCheckBox.setSelected(true);
        }

		int musicVolume = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.MusicVolumeKey);
		playMusicVolume.setValue(musicVolume);
		
        int soundVolume = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.SoundVolumeKey);
        playSoundsVolume.setValue(soundVolume);
	}

    @Override
    public void actionPerformed(ActionEvent ae)
    {        
        try
        {
            String action = ae.getActionCommand();
            
            if (action.equalsIgnoreCase("Cancel"))
            {
                CampaignGuiContextManager.getInstance().popFromContextStack();
            }
            else if (action.equalsIgnoreCase("AcceptChanges"))
            {
                setMusicPaameters();
                parent.refresh();

                CampaignGuiContextManager.getInstance().popFromContextStack();
            }
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private void setMusicPaameters() throws PWCGException
    {
        int playMusic = 0;
        if (playMusicCheckBox.isSelected())
        {
        	playMusic = 1;
        }
        ConfigManagerGlobal.getInstance().setParam(ConfigItemKeys.PlayMusicKey, "" + playMusic);
        
        int playSounds = 0;
        if (playSoundsCheckBox.isSelected())
        {
        	playSounds = 1;
            ConfigManagerGlobal.getInstance().setParam(ConfigItemKeys.PlaySoundsKey, "" + playSounds);
        }

		int musicVolume = playMusicVolume.getValue();
        ConfigManagerGlobal.getInstance().setParam(ConfigItemKeys.MusicVolumeKey, "" + musicVolume);

		int soundVolume = playSoundsVolume.getValue();
        ConfigManagerGlobal.getInstance().setParam(ConfigItemKeys.SoundVolumeKey, "" + soundVolume);
        
        ConfigManagerGlobal.getInstance().write();
        
        SoundManager.getInstance().setMusicVolume(musicVolume);
        SoundManager.getInstance().setSoundVolume(soundVolume);
        SoundManager.getInstance().setPlayMusic(playMusic);
        SoundManager.getInstance().setPlaySounds(playSounds);
    }

	@Override
	public void stateChanged(ChangeEvent evt)
	{
        try
		{
			if (evt.getSource() instanceof JSlider)
			{
		        JSlider slider = (JSlider) evt.getSource();
		        if (slider.equals(playMusicVolume))
		        {
		    		int musicVolume = playMusicVolume.getValue();
						SoundManager.getInstance().setMusicVolume(musicVolume);
		        }
		        
		        if (slider.equals(playSoundsVolume))
		        {
		    		int soundVolume = playSoundsVolume.getValue();
		            SoundManager.getInstance().setMusicVolume(soundVolume);
		        }		
			}
			
			if (evt.getSource() instanceof CheckBox)
			{
				CheckBox checkBox = (CheckBox) evt.getSource();
		        if (checkBox.equals(playMusicCheckBox))
		        {
		            int playMusic = 0;
		            if (playMusicCheckBox.isSelected())
		            {
		            	playMusic = 1;
		            }
		            SoundManager.getInstance().setPlayMusic(playMusic);
		            MusicManager.playTitleTheme();
		        }
		        
		        if (checkBox.equals(playSoundsCheckBox))
		        {
		            int playSounds = 0;
		            if (playSoundsCheckBox.isSelected())
		            {
		            	playSounds = 1;
		            }
		            SoundManager.getInstance().setPlayMusic(playSounds);
		        }		
			}
		}
		catch (PWCGException e)
		{
			PWCGLogger.logException(e);
		}
	}

 }
