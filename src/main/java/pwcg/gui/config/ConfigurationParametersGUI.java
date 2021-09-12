package pwcg.gui.config;

import java.awt.BorderLayout;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javax.swing.JTextField;

import pwcg.core.config.ConfigItem;
import pwcg.core.config.ConfigManager;
import pwcg.core.config.ConfigSet;
import pwcg.core.exception.PWCGException;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ToolTipManager;

public class ConfigurationParametersGUI extends ImageResizingPanel
{
	private static final long serialVersionUID = 1L;
    private ConfigSet configSet;
    private ConfigManager configManager;
	private Map<String, ConfigTextField> configTextFields = new HashMap<String, ConfigTextField>();
	
	public ConfigurationParametersGUI(Pane configurationGlobalGUI, ConfigManager configManager, ConfigSet configSet) 
	{		
        super("");
        setLayout(new BorderLayout());
        this.configSet = configSet;
        this.configManager = configManager;
	}

	public void makeGUI() throws PWCGException 
	{
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.Document);
        this.setImageFromName(imagePath);
        this.setBorder(BorderFactory.createEmptyBorder(50,50,50,100));

		if (configSet == null)
		{
			return;
		}
		
		Color bgColor = ColorMap.PAPER_BACKGROUND;
		
		Pane mainPanel = new Pane (new BorderLayout());
		mainPanel.setOpaque(false);

		Pane descPanel = new Pane (new GridLayout(0,3));
		descPanel.setOpaque(false);
		
		Font font = PWCGMonitorFonts.getTypewriterFont();
		
		for (String parameterKey : configSet.getConfigItemNames())
		{
			ConfigItem item = configSet.getConfigItem(parameterKey);

			String keyString = item.getLabelText() + " : ";
			Label label = new Label(keyString, Label.LEFT);
			label.setBackground(bgColor);
			label.setOpaque(false);
			label.setFont(font);
			descPanel.add(label);
			descPanel.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));

	        ToolTipManager.setToolTip(label, item.getHelp());
			
			JTextField textField = new JTextField(20);
			textField.setBackground(bgColor);
			textField.setOpaque(false);
			textField.setFont(font);
			textField.setText(item.getValue());
			textField.setSize(100, 30);
			descPanel.add(textField);
			
            Label spacerRight = new Label("");
            descPanel.add(spacerRight);

			ConfigTextField configTextField = new ConfigTextField(parameterKey, textField, 50, item.getLabelText(), item.getHelp());
			configTextFields.put(parameterKey, configTextField);
		}
		
		mainPanel.add(descPanel, BorderLayout.WEST);
		add(mainPanel, BorderLayout.NORTH);
	}
	
	public void recordChanges() throws PWCGException 
	{
		for (ConfigTextField configTextField : configTextFields.values())
		{
		    configSet.modifyConfigItemValue(configTextField.keyText, configTextField.text.getText());
		}
		
		configManager.incorporateEditedConfigSet(configSet);
	}

	public void resetChanges() 
	{
		for (String parameterKey : configSet.getConfigItemNames())
		{
			ConfigItem item = configSet.getConfigItem(parameterKey);
			ConfigTextField configTextField =  configTextFields.get(parameterKey);
			configTextField.text.setText(item.getValue());
		}
	}
	
	public class ConfigTextField
	{
		
		public String keyText;
		public JTextField text;
		public int textSize = 0;
		public String labelText;
		public String help;
		
		public ConfigTextField (String keyText,
								JTextField text,
								int textSize,
								String labelText,
								String help)
		{
			this.keyText = keyText;
			this.text = text;
			this.textSize = textSize;
			this.labelText = labelText;
			this.help = help;
		}
	}
}
