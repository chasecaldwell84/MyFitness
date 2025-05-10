package MyFitness.Statistics;

import MyFitness.App;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FilterPage extends JPanel {

    private static final int CATEGORY_COUNT = 3;

    private final App frame;
    private final StatisticsPage statisticsPage;

    private JPanel titlePanel;
    private JPanel mainPanel;
    private JPanel buttonPanel;

    public FilterPage(App frame, StatisticsPage statisticsPage) {
        this.frame = frame;
        this.statisticsPage = statisticsPage;
        setLayout(new BorderLayout(50, 50));
        createFilterGUI();
    }

    private void createFilterGUI(){
        titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.add(Box.createVerticalStrut(80));
        JLabel titleLabel = new JLabel("Filter Options", SwingConstants.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(App.titleFont);
        titlePanel.add(titleLabel);

        // Main filter panel with border
        mainPanel = new JPanel(new GridLayout(1, CATEGORY_COUNT, 10, 0));
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(45, 45, 90, 45),
                BorderFactory.createLineBorder(Color.BLACK, 5)
        ));

        for (int cat = 0; cat < CATEGORY_COUNT; cat++) {

            JPanel categoryPanel = new JPanel();

            categoryPanel.setLayout(new BoxLayout(categoryPanel, BoxLayout.Y_AXIS));
            TitledBorder catBorderTitle = new TitledBorder(StatisticsPage.StatLabels[0][cat]);
            catBorderTitle.setTitleFont(App.titleFont);
            categoryPanel.setBorder(catBorderTitle);

            JPanel headerPanel = new JPanel(new GridLayout(1, 3));

            JLabel metricLabel = new JLabel(" Metric");
            metricLabel.setFont(App.boldLabelFontLarge);
            headerPanel.add(metricLabel);
            JLabel showLabel = new JLabel(" Show");
            showLabel.setFont(App.boldLabelFontLarge);
            headerPanel.add(showLabel);
            categoryPanel.add(headerPanel);

            for (int i = 0; i < StatisticsPage.StatLabels[cat+1].length; i++){
                String metric = StatisticsPage.StatLabels[cat+1][i];
                JPanel rowPanel = new JPanel(new GridLayout(1, StatisticsPage.StatLabels[cat+1].length));
                JLabel metricNameLabel = new JLabel(metric);
                metricNameLabel.setFont(App.labelFontLarge);
                rowPanel.add(metricNameLabel);

                JCheckBox selectBox = new JCheckBox();
                selectBox.setName(cat+1 + ":" + i);
                selectBox.setSelected(StatisticsPage.selectedStat[cat+1][i]);
                selectBox.addActionListener(new FilterSelectStat(selectBox));
                rowPanel.add(selectBox);

                categoryPanel.add(rowPanel);
            }
            mainPanel.add(categoryPanel);
        }

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> backToStatistics());
        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(e -> applyFilters());
        buttonPanel.add(backButton);
        buttonPanel.add(applyButton);

        add(titlePanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void backToStatistics() {
        frame.getContentPane().remove(this);
        frame.add(statisticsPage);
        frame.revalidate();
        frame.repaint();
    }

    private void applyFilters() {
        statisticsPage.updateStatsGUI();
        backToStatistics();
    }

    static class FilterSelectStat implements ActionListener {
        private final JCheckBox box;

        public FilterSelectStat(JCheckBox box) {
            this.box = box;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String[] split = box.getName().split(":");
            int category = Integer.parseInt(split[0]);
            int index = Integer.parseInt(split[1]);
            StatisticsPage.selectedStat[category][index] = box.isSelected();
        }
    }
}
