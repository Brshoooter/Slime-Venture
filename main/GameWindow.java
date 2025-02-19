package main;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

// Clasa GameWindow extinde clasa JFrame și reprezintă fereastra principală a jocului
public class GameWindow extends JFrame {

    // Constructorul clasei
    public GameWindow(GamePanel gamePanel) {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Setarea operației de închidere a ferestrei
        add(gamePanel); // Adăugarea panelului jocului în fereastră
        setResizable(false); // Dezactivarea posibilității de redimensionare a ferestrei
        pack(); // Împachetarea componentelor în fereastră
        setLocationRelativeTo(null); // Centrarea ferestrei pe ecran
        setVisible(true); // Setarea ferestrei ca vizibilă
        // Adăugarea unui ascultător pentru evenimentele de focalizare a ferestrei
        addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                // Metoda apelată atunci când fereastra își recâștigă focalizarea
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                // Metoda apelată atunci când fereastra își pierde focalizarea
                gamePanel.getGame().windowFocusLost(); // Apelarea metodei de gestionare a pierderii focalizării jocului
            }
        });
    }

}