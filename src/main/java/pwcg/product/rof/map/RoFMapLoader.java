package pwcg.product.rof.map;

import pwcg.gui.rofmap.MapLoader;

public class RoFMapLoader extends MapLoader
{
    public RoFMapLoader()
    {
        super();
    }

    @Override
    public void run() 
    {

        // Do load order based on most likely use
        loadMap("FranceMap100.jpg");
        loadMap("ChannelMap100.jpg");
        loadMap("GaliciaMap100.jpg");
        
        loadMap("FranceMap125.jpg");
        loadMap("ChannelMap125.jpg");
        loadMap("GaliciaMap125.jpg");

        loadMap("FranceMap075.jpg");
        loadMap("ChannelMap075.jpg");
        loadMap("GaliciaMap075.jpg");

        loadMap("FranceMap150.jpg");
        loadMap("ChannelMap150.jpg");
        loadMap("GaliciaMap150.jpg");

        loadMap("FranceMap050.jpg");
        loadMap("ChannelMap050.jpg");
        loadMap("GaliciaMap050.jpg");


    }
}
