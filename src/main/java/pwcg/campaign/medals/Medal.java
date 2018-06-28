package pwcg.campaign.medals;

import java.util.Date;

public class Medal implements Comparable<Medal>
{
	private Date medalDate = null;
	private String medalName = "";
	private String medalImage = "";

	public Medal()
	{
	}

	public Medal(String medal, String image)
	{
		this.medalName = medal;
		this.medalImage = image;
	}

	public Date getMedalDate()
	{
		return medalDate;
	}

	public void setMedalDate(Date date)
	{
		this.medalDate = date;
	}

	public String getMedalName()
	{
		return medalName;
	}

	public void setMedalName(String medalName)
	{
		this.medalName = medalName;
	}

	public String getMedalImage()
	{
		return medalImage;
	}

	public void setMedalImage(String image)
	{
		this.medalImage = image;
	}

	@Override
	public int compareTo(Medal otherMedal)
	{
		if (this.medalDate.before(otherMedal.medalDate))
		{
			return -1;
		} else if (this.medalDate.after(otherMedal.medalDate))
		{
			return 1;
		}
		return 0;
	}
}
