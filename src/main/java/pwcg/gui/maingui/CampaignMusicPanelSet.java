package pwcg.gui.maingui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerGlobal;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.PwcgThreePanelUI;
import pwcg.gui.UiImageResolver;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.sound.MusicManager;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;

public class CampaignMusicPanelSet extends PwcgThreePanelUI implements ActionListener, ChangeListener
{    
    private static final long serialVersionUID = 1L;
    
    private JCheckBox playMusicCheckBox;
    private JCheckBox playSoundsCheckBox;
    private JSlider playMusicVolume;
    private JSlider playSoundsVolume;
    private CampaignMainGUI parent = null;

    public CampaignMusicPanelSet(CampaignMainGUI parent)
    {
        super(ImageResizingPanel.NO_IMAGE);
        this.parent = parent;
    }

    public void makePanels() 
    {
        try
        {
            setLeftPanel(makeButtonPanel());
            setCenterPanel(makeCampaignSelectPanel());
        }
        catch (Throwable e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private JPanel makeButtonPanel() throws PWCGException
    {
        String imagePath = UiImageResolver.getSideImageMain("MusicLeft.jpg");
        
        ImageResizingPanel configPanel = new ImageResizingPanel(imagePath);
        configPanel.setLayout(new BorderLayout());
        configPanel.setOpaque(true);
        
        JPanel buttonPanel = new JPanel(new GridLayout(6,1));
        buttonPanel.setOpaque(false);

        JButton createCampaignButton = PWCGButtonFactory.makeMenuButton("Accept Changes", "AcceptChanges", this);
        buttonPanel.add(createCampaignButton);
        
        JLabel dummyLabel3 = new JLabel("     ");       
        dummyLabel3.setOpaque(false);
        buttonPanel.add(dummyLabel3);
        
        JButton cancelChanges = PWCGButtonFactory.makeMenuButton("Cancel", "Cancel", this);
        buttonPanel.add(cancelChanges);

        configPanel.add(buttonPanel, BorderLayout.NORTH);
     
        return configPanel;
    }

    private JPanel makeCampaignSelectPanel() throws PWCGException
    {
        String imagePath = ContextSpecificImages.menuPathMain() + "MusicCenter.jpg";

        ImageResizingPanel musicControlPanel = new ImageResizingPanel(imagePath);
        musicControlPanel.setLayout(new BorderLayout());
        musicControlPanel.setOpaque(true);
        
        JPanel musicControlGrid = new JPanel(new GridLayout(0,1));
        musicControlGrid.setOpaque(false);        
        makePlayMusic();
        makePlaySound();
	    playMusicVolume = makeVolumeSlider();
	    playSoundsVolume = makeVolumeSlider();
        musicControlGrid.add(playSoundsCheckBox);
        musicControlGrid.add(playSoundsVolume);
        musicControlGrid.add(new JLabel(""));
        musicControlGrid.add(playMusicCheckBox);
        musicControlGrid.add(playMusicVolume);

        musicControlPanel.add(musicControlGrid, BorderLayout.NORTH);
        
        initializeWidgetValues();
        
        return musicControlPanel;
    }

	private void makePlaySound() throws PWCGException
	{
        Font font = PWCGMonitorFonts.getPrimaryFontLarge();

        playSoundsCheckBox = new JCheckBox();
        playSoundsCheckBox.setText("Play Sounds");
        playSoundsCheckBox.setSelected(false);
        playSoundsCheckBox.setOpaque(false);
        playSoundsCheckBox.setFont(font);
        playSoundsCheckBox.addChangeListener(this);
	}

	private void makePlayMusic() throws PWCGException
	{
        Font font = PWCGMonitorFonts.getPrimaryFontLarge();

        playMusicCheckBox = new JCheckBox();
        playMusicCheckBox.setText("Play Music");
        playMusicCheckBox.setSelected(false);
        playMusicCheckBox.setOpaque(false);
        playMusicCheckBox.setFont(font);
        playMusicCheckBox.addChangeListener(this);
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
			
			if (evt.getSource() instanceof JCheckBox)
			{
				JCheckBox checkBox = (JCheckBox) evt.getSource();
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
