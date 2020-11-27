package pwcg.product.bos.map.kuban;

import pwcg.mission.options.MapSeasonalParameters;
import pwcg.product.bos.map.IMapSeason;
import pwcg.product.bos.map.MapSeasonBase;

public class KubanMapSeason extends MapSeasonBase implements IMapSeason
{
    public KubanMapSeason()
    {
        super();
    }

	protected void makeWinter()
	{
		winter.setHeightMap("graphics\\LANDSCAPE_Kuban_sp\\height.hini");
    	winter.setTextureMap("graphics\\LANDSCAPE_Kuban_sp\\textures.tini");
    	winter.setForrestMap("graphics\\LANDSCAPE_Kuban_sp\\trees\\woods.wds");
    	winter.setGuiMap("kuban-spring");
    	winter.setSeasonAbbreviation(MapSeasonalParameters.SUMMER_ABREV);
    	winter.setSeason(MapSeasonalParameters.SUMMER_STRING);
	}

	protected void makeSpring()
	{
		spring.setHeightMap("graphics\\LANDSCAPE_Kuban_sp\\height.hini");
		spring.setTextureMap("graphics\\LANDSCAPE_Kuban_sp\\textures.tini");
    	spring.setForrestMap("graphics\\LANDSCAPE_Kuban_sp\\trees\\woods.wds");
    	spring.setGuiMap("kuban-spring");
    	spring.setSeasonAbbreviation(MapSeasonalParameters.SUMMER_ABREV);
    	spring.setSeason(MapSeasonalParameters.SUMMER_STRING);
	}

	protected void makeSummer()
	{
		summer.setHeightMap("graphics\\LANDSCAPE_Kuban_s\\height.hini");
		summer.setTextureMap("graphics\\LANDSCAPE_Kuban_s\\textures.tini");
		summer.setForrestMap("graphics\\LANDSCAPE_Kuban_s\\trees\\woods.wds");
    	summer.setGuiMap("kuban-summer");
    	summer.setSeasonAbbreviation(MapSeasonalParameters.SUMMER_ABREV);
    	summer.setSeason(MapSeasonalParameters.SUMMER_STRING);
	}

	protected void makeAutumn()
	{
		autumn.setHeightMap("graphics\\LANDSCAPE_Kuban_a\\height.hini");
		autumn.setTextureMap("graphics\\LANDSCAPE_Kuban_a\\textures.tini");
		autumn.setForrestMap("graphics\\LANDSCAPE_Kuban_a\\trees\\woods.wds");
		autumn.setGuiMap("kuban-autumn");
    	autumn.setSeasonAbbreviation(MapSeasonalParameters.SUMMER_ABREV);
    	autumn.setSeason(MapSeasonalParameters.SUMMER_STRING);
	}
}
