package pwcg.gui.utils;

import java.awt.Font;
import java.awt.FontMetrics;
import java.text.BreakIterator;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class WrappingLabel extends JLabel 
{
	private static final long serialVersionUID = 1L;
	
	public WrappingLabel (String text, int height, int width, Font font)
	{
		super(text);
		this.setFont(font);
		this.setSize(height, width);
		wrapLabelText(text);
	}
	
	public WrappingLabel (String text, int height, int width)
	{
		super(text);
		this.setSize(height, width);
		wrapLabelText(text);
	}
	
	private void wrapLabelText(String text) 
	{
		FontMetrics fm = this.getFontMetrics(this.getFont());
		int containerWidth = this.getWidth();

		BreakIterator boundary = BreakIterator.getWordInstance();
		boundary.setText(text);

		StringBuffer trial = new StringBuffer();
		StringBuffer real = new StringBuffer("<html>");

		int start = boundary.first();
		for (int end = boundary.next(); end != BreakIterator.DONE;
			start = end, end = boundary.next()) 
		{
			String word = text.substring(start,end);
			trial.append(word);
			int trialWidth = SwingUtilities.computeStringWidth(fm,
				trial.toString());
			if (trialWidth > containerWidth) {
				trial = new StringBuffer(word);
				real.append("<br>");
			}
			real.append(word);
		}

		real.append("</html>");

		this.setText(real.toString());
	}
}
