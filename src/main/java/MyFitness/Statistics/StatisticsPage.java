
// Author: Larry O'Connor
package MyFitness.Statistics;

import MyFitness.App;
import MyFitness.ExperienceTracker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import MyFitness.Database;

public class StatisticsPage extends JPanel {

    static final String[][] StatLabels = {
            {"Sleep & Calories", "Workout", "Classes & Goals"},
            {"Average Sleep","Total Sleep","Total Calories Consumed", "Average Calories Consumed"},
            {"Total Workout Time", "Average Workout Length","Total Reps", "Average Reps", "Total Miles Ran","Average Miles Ran"},
            {"Classes Attended","Total Class Hours","Average Class Hours","Goals Started","Goals Completed"}
    };

    public static boolean[][] selectedStat;
    public static List<Statistic> allStats;

    private JPanel infoPanel;
    private JPanel titlePanel;
    private JPanel buttonPanel;
    private final App frame;

    private final FilterPage filterPage;

    public StatisticsPage(App frame) {
        this.frame = frame;
        initializeStats();
        frame.setTitle("Statistics Page");
        setLayout(new BorderLayout(50, 50));
        createStatsGUI();
        filterPage = new FilterPage(frame, this);
    }

    private void initializeStats(){
        allStats = Database.getInstance().getAllStats(frame.getUser().getUserName());
        int categories = StatLabels.length;
        selectedStat = new boolean[categories][];
        for (int cat = 1; cat < categories; cat++) {
            int metrics = StatLabels[cat].length;
            selectedStat[cat] = new boolean[metrics];
            for (int i = 0; i < metrics; i++) {
                selectedStat[cat][i] = true;
            }
        }
    }

    private void createStatsGUI(){
        SwingUtilities.invokeLater(() -> {

            titlePanel = new JPanel();
            titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
            JLabel titleLabel = new JLabel("Statistics", SwingConstants.CENTER);

            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            titleLabel.setFont(App.titleFont);

            titlePanel.add(Box.createVerticalStrut(80));
            titlePanel.add(titleLabel);

            infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

            updateStatsPanel();
            infoPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(45, 45, 90, 45),
                    BorderFactory.createLineBorder(Color.BLACK, 5)
            ));

            buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
            JButton experienceButton = new JButton("Experience");
            experienceButton.addActionListener(new openExperienceUI());
            JButton filterButton = new JButton("Filter");
            filterButton.addActionListener(new openFilterUI(this));

            buttonPanel.add(experienceButton);
            buttonPanel.add(filterButton);

            add(titlePanel, BorderLayout.NORTH);
            add(infoPanel, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);

            revalidate();
            repaint();
        });
    }

    public void updateStatsGUI(){
        infoPanel.removeAll();
        updateStatsPanel();
        infoPanel.repaint();
        revalidate();
        repaint();
    }

    private void updateStatsPanel() {
        for (int cat = 1; cat < StatLabels.length; cat++) {
            for (int i = 0; i < StatLabels[cat].length; i++) {
                if(selectedStat[cat][i]) {
                    String metric = StatLabels[cat][i];
                    infoPanel.add(Box.createVerticalStrut(10));
                    JLabel avgLabel = new JLabel(metric + ": ");
                    avgLabel.setFont(App.boldLabelFontLarge);
                    avgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                    infoPanel.add(avgLabel);
                }
            }
        }
    }

    class openFilterUI implements ActionListener {
        private final StatisticsPage statsPage;
        public openFilterUI(StatisticsPage statsPage) { this.statsPage = statsPage; }
        @Override
        public void actionPerformed(ActionEvent e) {
            frame.getContentPane().remove(statsPage);
            frame.getContentPane().add(filterPage);
            frame.revalidate();
            frame.repaint();
        }
    }

    static class openExperienceUI implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ExperienceTracker xpUI = new ExperienceTracker();
        }
    }
}
