package org.sball.sar;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import javax.swing.text.JTextComponent;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.fife.ui.autocomplete.*;


/**
 * Created by sball on 14/04/2015.
 */
public class Demo extends JFrame
{

    public Demo()
    {
        JPanel contentPane = new JPanel(new BorderLayout(4, 4));

        final JTextField textField = new JSearchTextField();
        textField.setPreferredSize(new Dimension(160, 20));
        Box hbox = Box.createHorizontalBox();
        hbox.add(new JButton("Button"));
        hbox.add(Box.createHorizontalStrut(4));
        hbox.add(textField);
        Box vbox = Box.createVerticalBox();
        vbox.add(hbox);
        vbox.add(Box.createHorizontalGlue());
        contentPane.add(vbox);

        CompletionProvider completionProvider = createCompletionProvider();

        final AutoCompletion autoCompletion = new AutoCompletion(completionProvider);
        autoCompletion.setAutoCompleteEnabled(true);
        autoCompletion.setAutoCompleteSingleChoices(false);
        autoCompletion.setAutoActivationEnabled(true);
        autoCompletion.setAutoActivationDelay(100);

        autoCompletion.install(textField);

        setContentPane(contentPane);
        setTitle("Demo");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }


    private CompletionProvider createCompletionProvider()
    {
        DefaultCompletionProvider defaultCompletionProvider = new DefaultCompletionProvider()
        {
            /**
             * Additionally include whitespace in autocompletion text.
             * <br>
             * {@inheritDoc}
             */
            @Override
            protected boolean isValidChar(char ch)
            {
                return Character.isWhitespace(ch) || super.isValidChar(ch);
            }


            /**
             * {@inheritDoc}
             */
            @Override
            protected List<Completion> getCompletionsImpl(JTextComponent comp)
            {
                List<Completion> completionList = super.getCompletionsImpl(comp);
                List<Completion> filteredCompletions = new ArrayList<>(completionList.size());

                // Remove duplicates with the same replacement text
                for (Completion completion : completionList)
                {
                    final String replacement = completion.getReplacementText();
                    if (!CollectionUtils.exists(filteredCompletions, new Predicate<Completion>()
                    {
                        @Override
                        public boolean evaluate(Completion completion)
                        {
                            return completion.getReplacementText().equalsIgnoreCase(replacement);
                        }
                    }))
                    {
                        filteredCompletions.add(completion);
                    }
                }

                return filteredCompletions;
            }
        };

        // Autocomplete for any letter with using hotkey
        defaultCompletionProvider.setAutoActivationRules(true, null);

        defaultCompletionProvider.addCompletions(getCompletions(defaultCompletionProvider));

        return defaultCompletionProvider;
    }


    private List<Completion> getCompletions(CompletionProvider provider)
    {
        Map<String, String> map = new HashMap<>();

        String editWells = "Edit Wells";
        String editLogs = "Edit Logs";
        String editVolumes = "Edit Volumes";
        String importVolume2D = "Import Volume 2D";
        String importVolume3D = "Import Volume 3D";

        map.put("Edit Wells", editWells);

        map.put("Edit Logs", editLogs);

        map.put("Edit Volumes", editVolumes);

        map.put("Import Volume 2D", importVolume2D);
        map.put("2D Volume Import", importVolume2D);
        map.put("Import 2D Volumes", importVolume2D);

        map.put("Import Volume 3D", importVolume3D);
        map.put("3D Volume Import", importVolume3D);
        map.put("Import 3D Volumes", importVolume3D);



        List<Completion> completions = new ArrayList<>(map.size());

        for (Map.Entry<String, String> entry : map.entrySet())
        {
            completions.add(new ShorthandCompletion(provider, entry.getKey().toString(), entry.getValue().toString()));
        }

        return completions;
    }

    public static Map<String, String> combinations(String root, String replacement)
    {
        String[] split = root.split(" ");
        Map<String , String> map = new HashMap<>(split.length);

        //TODO

        return map;

    }

    public static void main(String[] args)
    {
        // Instantiate GUI on the EDT.
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                try
                {
                    String laf = UIManager.getSystemLookAndFeelClassName();
                    UIManager.setLookAndFeel(laf);
                }
                catch (Exception e)
                {
                    /* Never happens */
                }
                new Demo().setVisible(true);
            }
        });
    }

}
