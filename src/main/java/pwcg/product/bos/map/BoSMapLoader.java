package pwcg.product.bos.map;

import pwcg.gui.rofmap.MapLoader;

public class BoSMapLoader extends MapLoader
{
    public BoSMapLoader()
    {
        super();
    }

    @Override
    public void run() 
    {

        // Do load order based on most likely use
        loadMap("StalingradMap100.jpg");
        loadMap("MoscowMap100.jpg");
        
        loadMap("StalingradMap125.jpg");
        loadMap("MoscowMap125.jpg");

        loadMap("StalingradMap075.jpg");
        loadMap("MoscowMap075.jpg");

        loadMap("StalingradMap150.jpg");
        loadMap("MoscowMap150.jpg");

        loadMap("StalingradMap050.jpg");
        loadMap("MoscowMap050.jpg");
    }
}
