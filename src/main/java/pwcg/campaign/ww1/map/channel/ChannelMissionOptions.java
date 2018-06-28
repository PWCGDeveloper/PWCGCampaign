package pwcg.campaign.ww1.map.channel;

import pwcg.mission.options.MapSeasonalParameters;
import pwcg.mission.options.MissionOptions;


public class ChannelMissionOptions extends MissionOptions
{
    public ChannelMissionOptions()
    {
        super();

    	makeWinter();
    	makeSpring();
    	makeSummer();
        makeAutumn();
    }

	private void makeWinter()
	{
		winter.setHeightMap("graphics\\landscape_channel\\height.ini");
    	winter.setTextureMap("graphics\\landscape_channel\\textures.ini");
    	winter.setForrestMap("graphics\\landscape_channel\\trees\\woods.wds");
    	winter.setGuiMap("swf\\data\\maps\\channel_summer.info");
    	winter.setSeasonAbbreviation(MapSeasonalParameters.SUMMER_ABREV);
    	winter.setSeason(MapSeasonalParameters.SUMMER_STRING);
	}

	private void makeSpring()
	{
		spring.setHeightMap("graphics\\landscape_channel\\height.ini");
    	spring.setTextureMap("graphics\\landscape_channel\\textures.ini");
    	spring.setForrestMap("graphics\\landscape_channel\\trees\\woods.wds");
    	spring.setGuiMap("swf\\data\\maps\\channel_summer.info");
    	spring.setSeasonAbbreviation(MapSeasonalParameters.SUMMER_ABREV);
    	spring.setSeason(MapSeasonalParameters.SUMMER_STRING);
	}

	private void makeSummer()
	{
		summer.setHeightMap("graphics\\landscape_channel\\height.ini");
    	summer.setTextureMap("graphics\\landscape_channel\\textures.ini");
    	summer.setForrestMap("graphics\\landscape_channel\\trees\\woods.wds");
    	summer.setGuiMap("swf\\data\\maps\\channel_summer.info");
    	summer.setSeasonAbbreviation(MapSeasonalParameters.SUMMER_ABREV);
    	summer.setSeason(MapSeasonalParameters.SUMMER_STRING);
	}

	private void makeAutumn()
	{
		autumn.setHeightMap("graphics\\landscape_channel\\height.ini");
    	autumn.setTextureMap("graphics\\landscape_channel\\textures.ini");
    	autumn.setForrestMap("graphics\\landscape_channel\\trees\\woods.wds");
    	autumn.setGuiMap("swf\\data\\maps\\channel_summer.info");
    	autumn.setSeasonAbbreviation(MapSeasonalParameters.SUMMER_ABREV);
    	autumn.setSeason(MapSeasonalParameters.SUMMER_STRING);
	}
}
