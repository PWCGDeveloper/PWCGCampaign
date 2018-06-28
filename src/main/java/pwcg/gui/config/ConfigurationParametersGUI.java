package pwcg.gui.config;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import pwcg.core.config.ConfigItem;
import pwcg.core.config.ConfigManager;
import pwcg.core.config.ConfigSet;
import pwcg.core.exception.PWCGException;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.MonitorSupport;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImagePanel;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ToolTipManager;

public class ConfigurationParametersGUI extends ImageResizingPanel
{
	private static final long serialVersionUID = 1L;
    private ConfigSet configSet;
    private ConfigManager configManager;
	private Map<String, ConfigTextField> configTextFields = new HashMap<String, ConfigTextField>();
	
	public ConfigurationParametersGUI(ImagePanel parent, ConfigManager configManager, ConfigSet configSet) 
	{		
        super(ContextSpecificImages.imagesMisc() + "Paper.jpg");
        setLayout(new BorderLayout());
        this.configSet = configSet;
        this.configManager = configManager;
	}

	public void makeGUI() throws PWCGException 
	{
		if (configSet == null)
		{
			return;
		}
		
		Color bgColor = ColorMap.PAPER_BACKGROUND;
		
		JPanel mainPanel = new JPanel (new BorderLayout());
		mainPanel.setOpaque(false);

		JPanel descPanel = new JPanel (new GridLayout(0,1));
		descPanel.setOpaque(false);
		
		JPanel valuePanel = new JPanel (new GridLayout(0,1));
		valuePanel.setOpaque(false);
		
		Font font = MonitorSupport.getPrimaryFontSmall();
		
		for (String parameterKey : configSet.getConfigItemNames())
		{
			ConfigItem item = configSet.getConfigItem(parameterKey);
			
			String keyString = item.getLabelText() + " : ";
			JLabel label = new JLabel(keyString, JLabel.LEFT);
			label.setBackground(bgColor);
			label.setOpaque(false);
			label.setFont(font);
			descPanel.add(label);
			
	        ToolTipManager.setToolTip(label, item.getHelp());
			
			JTextField textField = new JTextField(50);
			textField.setBackground(bgColor);
			textField.setOpaque(false);
			textField.setFont(font);
			textField.setText(item.getValue());
			
			valuePanel.add(textField);
			
			ConfigTextField configTextField = new ConfigTextField(parameterKey, textField, 50, item.getLabelText(), item.getHelp());
			configTextFields.put(parameterKey, configTextField);
		}
		
		mainPanel.add(descPanel, BorderLayout.WEST);
		mainPanel.add(valuePanel, BorderLayout.CENTER);

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
