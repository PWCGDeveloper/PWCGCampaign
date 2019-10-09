package pwcg.product.fc.map;

import pwcg.gui.rofmap.MapLoader;

public class FCMapLoader extends MapLoader
{
    public FCMapLoader()
    {
        super();
    }

    @Override
    public void run() 
    {
        loadMap("ArrasMap100.jpg");
        
        loadMap("ArrasMap125.jpg");

        loadMap("ArrasMap075.jpg");

        loadMap("ArrasMap150.jpg");

        loadMap("ArrasMap050.jpg");
    }
}
