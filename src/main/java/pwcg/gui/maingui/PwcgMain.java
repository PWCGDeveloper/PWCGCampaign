package pwcg.gui.maingui;

import javafx.application.Application;
import javafx.stage.Stage;
import pwcg.campaign.CoopToV2;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.utils.TestDriver;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;

public class PwcgMain extends Application
{
	public static void main(String[] args) 
	{
	    launch(args);
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
            PwcgDirectoryRestructure.restructureDirectories(PWCGProduct.BOS);
            CoopToV2.moveToCoopV2();

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
                PWCGContext.setProduct(PWCGProduct.FC);
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
        // Set tab background
        
        // set tab insets
    }

    @Override
    public void start(Stage pwcgStage) throws Exception
    {
        pwcgStage.setTitle("PWCG");
        
    }
}
