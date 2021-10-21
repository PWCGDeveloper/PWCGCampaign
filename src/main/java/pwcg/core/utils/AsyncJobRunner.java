package pwcg.core.utils;

import java.awt.Font;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.PWCGLabelFactory;

public class AsyncJobRunner {
    private ThreadPoolExecutor executorService;
    private boolean haveTasks = false;
    private JDialog progressDialog;

    public interface Job
    {
        void run() throws Exception;
    }

    public AsyncJobRunner(String header)
    {
        progressDialog = new JDialog((Frame)null, header, true);
        progressDialog.setLayout(new BoxLayout(progressDialog.getContentPane(), BoxLayout.Y_AXIS));
        progressDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        progressDialog.setLocationRelativeTo(null);

        int availableProcessors = Runtime.getRuntime().availableProcessors();
        executorService = new ThreadPoolExecutor(availableProcessors, availableProcessors, 0, TimeUnit.DAYS, new LinkedBlockingQueue<>()) {
            @Override
            protected void terminated() {
                progressDialog.dispose();
            }
        };
    }

    public void add(String desc, Job job)
    {
        ArrayList<Job> list = new ArrayList<>();
        list.add(job);
        add(desc, true, list);
    }

    public void add(String desc, List<Job> jobs)
    {
        add(desc, false, jobs);
    }

    public void add(String desc, boolean indeterminate, List<Job> jobs)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JProgressBar progressBar = new JProgressBar(0, jobs.size());
        progressBar.setIndeterminate(indeterminate);
        progressBar.setStringPainted(true);
        if (indeterminate)
        {
            progressBar.setString("");
        }
        
        try
        {
            Font font = PWCGMonitorFonts.getTypewriterFont();
            JLabel statusLabel = PWCGLabelFactory.makeLabel(desc, ColorMap.PAPER_BACKGROUND, ColorMap.PAPER_FOREGROUND, font, SwingConstants.LEFT);
            panel.add(statusLabel);
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
        }
        
        panel.add(progressBar);

        progressDialog.add(panel);

        for (Job job : jobs)
        {
            SwingWorker<Void, Void> sw = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    job.run();
                    return null;
                }

                @Override
                protected void done() {
                    try {
                        get();
                    }
                    catch (ExecutionException e)
                    {
                        PWCGLogger.logException(e.getCause());
                    }
                    catch (InterruptedException e) {
                        PWCGLogger.logException(e);
                    }

                    progressBar.setValue(progressBar.getValue() + 1);
                    if (progressBar.isIndeterminate() && progressBar.getValue() == progressBar.getMaximum())
                    {
                        progressBar.setString("Done!");
                        progressBar.setIndeterminate(false);
                    }
                }
            };

            executorService.submit(sw);
            haveTasks = true;
        }
    }

    public void finish()
    {
        executorService.shutdown();

        if (haveTasks)
        {
            progressDialog.pack();
            progressDialog.setVisible(true);
        }
    }
}