package pwcg.gui.utils;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

public class PWCGScrollPane extends JScrollPane
{
    private static final long serialVersionUID = 1L;

    public PWCGScrollPane (JComponent innerComponent)
    {
        super(innerComponent);
        
        PWCGScrollBarUI yourUI = new PWCGScrollBarUI();

        getVerticalScrollBar().setUI(yourUI);
        getHorizontalScrollBar().setUI(yourUI);

        setOpaque(false);
        getViewport().setOpaque(false);
        setBorder(null);
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }
}
