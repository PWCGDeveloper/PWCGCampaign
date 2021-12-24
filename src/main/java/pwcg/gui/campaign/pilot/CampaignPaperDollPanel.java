package pwcg.gui.campaign.pilot;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.factory.MedalManagerFactory;
import pwcg.campaign.medals.IMedalManager;
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
    private Campaign campaign;
    private SquadronMember pilot;

    public CampaignPaperDollPanel(Campaign campaign, SquadronMember pilot)
    {
        super("");
        this.setLayout(new BorderLayout());

        this.campaign = campaign;
        this.pilot = pilot;
    }

    public void makePaperDollPanel() throws PWCGException
    {
        BufferedImage paperDoll = getPaperDollImage();
        if (paperDoll != null)
        {
            buildPaperDollWithOverlay(paperDoll);
        }
    }

    private BufferedImage getPaperDollImage() throws PWCGException
    {
        BufferedImage paperDoll = null;
        if (pilot.isPlayer())
        {
            paperDoll = getPlayerPaperDollImage();
        }
        
        if (paperDoll == null)
        {
            paperDoll = getGenericPaperDollImage();
        }
        
        return paperDoll;
    }

    private BufferedImage getPlayerPaperDollImage() throws PWCGException
    {
        ICountry country = CountryFactory.makeCountryByCountry(pilot.getCountry());
        String paperDollDirectory = ContextSpecificImages.imagesPlayerPaperDoll() + country.getCountryName();
        String paperDollPath = paperDollDirectory + "\\PaperDoll.png";
        BufferedImage paperDoll = ImageRetriever.getImageFromFile(paperDollPath);
        return paperDoll;
    }

    private BufferedImage getGenericPaperDollImage() throws PWCGException
    {
        BufferedImage paperDoll;
        ICountry country = CountryFactory.makeCountryByCountry(pilot.getCountry());
        String paperDollDirectory = ContextSpecificImages.imagesPaperDoll() + country.getCountryName();
        String paperDollPath = paperDollDirectory + "\\PaperDoll.png";
        paperDoll = ImageRetriever.getImageFromFile(paperDollPath);
        return paperDoll;
    }

    private void buildPaperDollWithOverlay(BufferedImage paperDoll) throws PWCGException
    {
        ICountry country = CountryFactory.makeCountryByCountry(pilot.getCountry());
        String paperDollDirectory = ContextSpecificImages.imagesPaperDoll() + country.getCountryName();

        paperDoll = addRank(paperDollDirectory, paperDoll, pilot.getMedals());
        paperDoll = addMedals(paperDollDirectory, paperDoll, pilot.getMedals());
        paperDoll = addFauxCollar(paperDollDirectory, paperDoll, pilot.getMedals());
        this.setImage(paperDoll);
        this.setBorder(PwcgBorderFactory.createDocumentBorderWithExtraSpaceFromTop());
    }

    private BufferedImage addFauxCollar(String paperDollDirectory, BufferedImage paperDoll, List<Medal> medals)
    {
        if (pilot.getCountry() == Country.USA)
        {
            paperDoll = addPaperDollElement(paperDollDirectory, paperDoll, "FauxLapel");
        }
        return paperDoll;
    }

    private BufferedImage addRank(String paperDollDirectory, BufferedImage paperDoll, List<Medal> medals)
    {
        String rank = pilot.getRank();
        paperDoll = addPaperDollElement(paperDollDirectory, paperDoll, rank);
        
        if (pilot.getCountry() == Country.GERMANY)
        {
            paperDoll = addPaperDollElement(paperDollDirectory, paperDoll, "SB" + rank);
        }
        
        return paperDoll;
    }

    private BufferedImage addMedals(String paperDollDirectory, BufferedImage paperDoll, List<Medal> medals) throws PWCGException
    {
        ICountry country = CountryFactory.makeCountryByCountry(pilot.getCountry());
        IMedalManager medalManager = MedalManagerFactory.createMedalManager(country, campaign);
        List<Medal> highestOrderMedals = medalManager.getMedalsWithHighestOrderOnly(pilot.getMedals());
        for (Medal medal : highestOrderMedals)
        {
            paperDoll = addPaperDollElement(paperDollDirectory, paperDoll, medal.getMedalName());
        }

        return paperDoll;
    }

    private BufferedImage addPaperDollElement(String paperDollDirectory, BufferedImage paperDoll, String imageName)
    {
        try
        {
            String medalPath = paperDollDirectory + "\\" + imageName + ".png";
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
