package pwcg.gui.maingui;

import java.awt.Color;
import java.awt.Insets;

import javax.swing.UIManager;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.utils.TestDriver;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;

public class PwcgMain
{
	public static void main(String[] args) 
	{
        PwcgMain pwcg = new PwcgMain();
        pwcg.startPWCG(args);
	}

	public PwcgMain() 
	{
	}
	

	private void startPWCG(String[] args)
	{
        try
        {
            validatetestDriverNotEnabled();            
            setProduct(args);
            initializePWCGStaticData();
            setupUIManager();
            
            PwcgMainScreen campaignMainScreen = new PwcgMainScreen();
            campaignMainScreen.makePanels();
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
        }
	}

    private void validatetestDriverNotEnabled()
    {
        TestDriver testDriver = TestDriver.getInstance();
        if (testDriver.isEnabled())
        {
            ErrorDialog.userError("PWCG test driver is enabled - PWCG will not function normally");
        }
    }

    private void setProduct(String[] args) throws PWCGException
    {
        if (args.length > 0)
        {
            if (args[0].equals("BoS"))
            {
                PWCGContext.setProduct(PWCGProduct.BOS);
                PWCGLogger.log(LogLevel.INFO, "Running BoS");
            }
            else if (args[0].equals("FC"))
            {
                PWCGContext.setProduct(PWCGProduct.BOS);
                PWCGLogger.log(LogLevel.INFO, "Running FC");
            }
        }
        else
        {
            PWCGLogger.log(LogLevel.ERROR, "PWCG Product not provided");
            throw new PWCGException("PWCG Product not provided");
        }
    }

    private void initializePWCGStaticData()
    {
        PWCGContext.getInstance();
    }

    private void setupUIManager() throws PWCGException
    {
        Color tabSelectedColor = ColorMap.PAPER_BACKGROUND;
        UIManager.put("TabbedPane.selected", tabSelectedColor);
        UIManager.put("TabbedPane.contentOpaque", false);
        
        Insets insets = UIManager.getInsets("TabbedPane.contentBorderInsets");
        insets.top = -1;
        UIManager.put("TabbedPane.contentBorderInsets", insets);
        
        UIManager.put("OptionPane.background", ColorMap.NEWSPAPER_BACKGROUND);
        UIManager.getLookAndFeelDefaults().put("Panel.background", ColorMap.NEWSPAPER_BACKGROUND);
    }
}
