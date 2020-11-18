package pwcg.gui.campaign.pilot;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.medals.Medal;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.image.ImageRetriever;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PwcgBorderFactory;

public class CampaignPaperDollPanel extends ImageResizingPanel
{
    private static final long serialVersionUID = 1L;
    private SquadronMember pilot;

    public CampaignPaperDollPanel(SquadronMember pilot)
    {
        super("");
        this.setLayout(new BorderLayout());

        this.pilot = pilot;
    }

    public void makePaperDollPanel() throws PWCGException
    {
        ICountry country = CountryFactory.makeCountryByCountry(pilot.getCountry());
        String paperDollDirectory = ContextSpecificImages.imagesPaperDoll() + country.getCountryName();

        BufferedImage paperDoll = buildPaperDollWithOverlay(paperDollDirectory);
        this.setImage(paperDoll);
        this.setBorder(PwcgBorderFactory.createDocumentBorderWithExtraSpaceFromTop());
    }

    private BufferedImage buildPaperDollWithOverlay(String paperDollDirectory) throws PWCGException
    {
        String paperDollPath = paperDollDirectory + "\\PaperDoll.png";
        BufferedImage paperDoll = ImageRetriever.getImageFromFile(paperDollPath);

        paperDoll = addDefaultMedals(paperDollDirectory, paperDoll, pilot.getMedals());
        paperDoll = addKnightsCross(paperDollDirectory, paperDoll, pilot.getMedals());

        return paperDoll;
    }

    private BufferedImage addDefaultMedals(String paperDollDirectory, BufferedImage paperDoll, List<Medal> medals)
    {
        for (Medal medal : pilot.getMedals())
        {
            if (!medal.getMedalName().contains("Knights Cross"))
            {
                paperDoll = addMedal(paperDollDirectory, paperDoll, medal);
            }
        }

        return paperDoll;
    }

    private BufferedImage addKnightsCross(String paperDollDirectory, BufferedImage paperDoll, List<Medal> medals)
    {
        Medal knightCross = null;
        for (Medal medal : pilot.getMedals())
        {
            if (medal.getMedalName().contains("Knights Cross"))
            {
                knightCross = medal;
            }
        }
        
        if (knightCross != null)
        {
            paperDoll = addMedal(paperDollDirectory, paperDoll, knightCross);
        }

        return paperDoll;
    }

    private BufferedImage addMedal(String paperDollDirectory, BufferedImage paperDoll, Medal medal)
    {
        try
        {
            String medalPath = paperDollDirectory + "\\" + medal.getMedalName() + ".png";
            BufferedImage medalOverlay = ImageRetriever.getImageFromFile(medalPath);
            if (medalOverlay != null)
            {
                paperDoll = combineOverlayWithMap(paperDoll, medalOverlay);
            }
        }
        catch (Exception ex)
        {
            PWCGLogger.logException(ex);
        }
        return paperDoll;
    }

    private BufferedImage combineOverlayWithMap(BufferedImage paperDollImage, BufferedImage medalOverlay)
    {
        BufferedImage result = new BufferedImage(paperDollImage.getWidth(), paperDollImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = result.getGraphics();
        g.drawImage(paperDollImage, 0, 0, null);
        g.drawImage(medalOverlay, 0, 0, null);
        return result;
    }
}
