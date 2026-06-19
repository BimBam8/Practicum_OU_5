package presentatie;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import domein.PrikToGo;

/**
 * View GUI voor de Applicatie
 * 
 * @author Niels
 * @author Sem
 */
public class View extends JFrame{
    private final PrikToGo ptg;
    private JComboBox<String> vestigingenComboBox;
    private JList<String> klantenLijst;
    private DefaultListModel<String> klantenLijstModel;
    private JPanel klantenPanel;
    private JLabel totaalLabel;
    private boolean isLaden;


    public View(PrikToGo ptg) {
        this.ptg = ptg;
        initialiseerVenster();
        initialiseerComponenten();
        laadVestigingen();
        setVisible(true);
    }

    private void initialiseerVenster() {
        setTitle("Prik2Go - Klanten per vestiging");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);

        // Gebruik beeindigOverzicht bij sluiten van het venster
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                beeindigOverzicht();
            }
        });
    }

    // Maakt en plaatst alle GUI-componenten in het frame
    private void initialiseerComponenten() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Dropdownmenu voor vestigingen bovenaan
        JPanel dropDownPanel = new JPanel(new BorderLayout(5,5));
        JLabel vestigingenLabel = new JLabel("Selecteer een vestiging:");
        dropDownPanel.add(vestigingenLabel, BorderLayout.WEST);
        vestigingenComboBox = new JComboBox<>();
        vestigingenComboBox.addActionListener(e -> toonKlanten());
        dropDownPanel.add(vestigingenComboBox, BorderLayout.CENTER);

        // Veld voor totaal aantal klanten
        totaalLabel = new JLabel("Totaal klanten: 0");

        // Lijst met klanten per vestiging
        klantenPanel = new JPanel(new BorderLayout(5,5));
        JLabel klantenLabel = new JLabel("Klantnummers:");
        klantenPanel.add(klantenLabel, BorderLayout.NORTH);
        
        klantenLijstModel = new DefaultListModel<>();
        klantenLijst = new JList<>(klantenLijstModel);
        JScrollPane klantenScrollPane = new JScrollPane(klantenLijst);
        klantenScrollPane.setPreferredSize(new Dimension(0, 300));
        klantenPanel.add(klantenScrollPane, BorderLayout.CENTER);

        JPanel middenPanel = new JPanel(new BorderLayout(5,5));
        middenPanel.add(totaalLabel, BorderLayout.NORTH);
        middenPanel.add(klantenPanel, BorderLayout.CENTER);
        
        //Knop om de interactie te beeindigen
        JPanel beeindigPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton beeindigButton = new JButton("Sluit af");
        beeindigButton.addActionListener(e -> beeindigOverzicht());
        beeindigPanel.add(beeindigButton);

        // plaatst de componenten op de juiste posities in het hoofdvenster
        mainPanel.add(dropDownPanel, BorderLayout.NORTH);
        mainPanel.add(middenPanel, BorderLayout.CENTER);
        mainPanel.add(beeindigPanel, BorderLayout.SOUTH);

        // Verberg klantenlijst en totaallabel bij start
        totaalLabel.setVisible(false);
        klantenPanel.setVisible(false);

        add(mainPanel);
    }

    // Laadt de vestigingen in het dropdownmenu bij het starten van de applicatie
    // en toont een foutmelding als er iets misgaat
    private void laadVestigingen() {
        String[] vestigingen = ptg.getOverzichtVestigingen();
        if (vestigingen == null) {
            JOptionPane.showMessageDialog(this, "Fout bij laden vestigingen.", "Fout", JOptionPane.ERROR_MESSAGE);
            dispose();
        }
        isLaden = true;// Voorkomt toonKlanten door ActionListener Combobox tijdens laden van vestigingen
        vestigingenComboBox.setModel(new DefaultComboBoxModel<>(vestigingen));
        vestigingenComboBox.setSelectedIndex(-1); // Geen selectie bij start
        isLaden = false;// Laden voltooid, ActionListener mag weer
    }

    // Toont de klanten van de geselecteerde vestiging in de lijst,
    //  zet de zichtbaarheid van totaal en klantenlijst aan of 
    // toont een foutmelding als er iets misgaat
    private void toonKlanten() {
        if (isLaden) {
            return; // Label en panel niet zichtbaar tijdens laden vestigingen
        }   
        int geselecteerdeIndex = vestigingenComboBox.getSelectedIndex();
        if (geselecteerdeIndex >= 0) {
            String[] klanten = ptg.selecteerVestiging(geselecteerdeIndex);
            if (klanten == null) {
                JOptionPane.showMessageDialog(this, "Fout bij laden klanten.", "Fout", JOptionPane.ERROR_MESSAGE);
                return;
            }
            klantenLijstModel.clear();
            for (String klant : klanten) {
                klantenLijstModel.addElement(klant);
            }
            totaalLabel.setText("Totaal klanten: " + klanten.length);
        } else {
            klantenLijstModel.clear();
            totaalLabel.setText("Totaal klanten: 0");
        }
        totaalLabel.setVisible(true);
        klantenPanel.setVisible(true);
    }

    private void beeindigOverzicht() {
        int confirm = JOptionPane.showConfirmDialog(this, "Weet je zeker dat je het overzicht wilt beeindigen?", "Bevestiging", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
        }
    }
}