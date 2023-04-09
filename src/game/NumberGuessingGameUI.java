package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.border.EmptyBorder;

//This class implements the UI for a number guessing game.

public class NumberGuessingGameUI implements ActionListener {
    private JFrame frame;
    private JPanel panel;
    private JLabel label;
    private JTextField textField;
    private JButton button;
    private JTextArea textArea;
    private JScrollPane scrollPane;
    private Random random;
    private int rangeStart;
    private int rangeEnd;
    private int secretNumber;
    private int attempts;
    private int maxAttempts;
    private int roundScore;
    private int totalScore;
    private List<Integer> scores;
    
    // Constructor for the UI.
    
    public NumberGuessingGameUI() {
        frame = new JFrame("Number Guessing Game");       
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        panel = new JPanel();
        label = new JLabel("Guess a Number Between 1 and 100:");
        label.setFont(new Font("Georgia", Font.BOLD, 18));
        textField = new JTextField(10);
        textField.setFont(new Font("Arial", Font.PLAIN, 18));        
        button = new JButton("Guess");
        button.setPreferredSize(new Dimension(100, 30));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(60, 179, 113));
        button.setForeground(Color.white);
        button.setFocusPainted(false);
        textArea = new JTextArea();
        textArea.setPreferredSize(new Dimension(180, 60));
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        textArea.setEditable(false);
        textArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        textArea.setForeground(Color.darkGray);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(220, 100)); 
        EmptyBorder border = new EmptyBorder(5, 5, 5, 5);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(scrollPane.getBorder(), border));
        random = new Random();
        rangeStart = 1;
        rangeEnd = 100;
        maxAttempts = 2;
        scores = new ArrayList<>();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.white);       
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new FlowLayout());
        northPanel.setBackground(Color.white);
        northPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0)); 
        northPanel.add(label);
        northPanel.add(textField);
        northPanel.add(button);
        panel.add(northPanel, BorderLayout.NORTH);
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setBackground(Color.gray);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        centerPanel.add(scrollPane, gbc); 
        panel.add(centerPanel, BorderLayout.CENTER);
        frame.add(panel);
        button.addActionListener(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null); 
        frame.setVisible(true);
        newGame();
    }
 //Handles actions performed by the user, such as button clicks.
  
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            int guess = Integer.parseInt(textField.getText());
            playGame(guess);
            textField.setText("");
        }
    }
  
    //generating a new secret number
    
    private void newGame() {
        attempts = 0;
        roundScore = 0;
        secretNumber = random.nextInt(rangeEnd - rangeStart + 1) + rangeStart;
        textArea.setText("New game! Guess a number between 1 and 100.\nYou have " + maxAttempts + " attempts.\n\n");
    
    }

    // handles the logic of the game when a guess is made.
    
    private void playGame(int guess) {
        attempts++;

        if (guess == secretNumber) {
            textArea.append("Congratulations! You guessed the number " + secretNumber + " in " + attempts + " attempts.\n");

            roundScore = 100 / attempts;
            totalScore += roundScore;
            scores.add(roundScore);
            textArea.append("Round score: " + roundScore + "\n");
            int response = JOptionPane.showConfirmDialog(null, "Do you want to play again?", "Game Over",
                    JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                newGame();
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("\nFinal score: ").append(totalScore);
                if (scores.size() > 1) {
                    sb.append(" (");
                    for (int i = 0; i < scores.size(); i++) {
                        sb.append(scores.get(i));
                        if (i < scores.size() - 1) {
                            sb.append(" + ");
                        }
                    }
                    sb.append(")");
                }
                sb.append("\nRound scores: ");
                for (int i = 0; i < scores.size(); i++) {
                    sb.append(scores.get(i));
                    if (i < scores.size() - 1) {
                        sb.append(", ");
                    }
                }
                textArea.append(sb.toString());

                button.setEnabled(false);
            }
        } else if (guess < secretNumber) {
            textArea.append(guess + " is too low.\n");
        } else {
            textArea.append(guess + " is too high.\n");
        }

        if (attempts ==maxAttempts) {
        	textArea.append("Sorry, you have used all your attempts.\nThe secret number was " + secretNumber + ".\n");
            roundScore = 0; // set round score to 0 since the player did not guess correctly
            scores.add(roundScore); // add the round score to the list of scores

            int response = JOptionPane.showConfirmDialog(null, "Do you want to play again?", "Game Over",
                    JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                newGame();
            } else {
                totalScore += roundScore; // add the round score to the total score
                StringBuilder sb = new StringBuilder();
                sb.append("\nFinal score: ").append(totalScore).append("\n");
                sb.append("Round scores: ");

                for (int score : scores) {
                    sb.append(score).append(" ");
                }

                textArea.append(sb.toString());
                textField.setEditable(false);

                button.setEnabled(false);
            }
        }
    }

   
    
    public static void main(String[] args) {
        NumberGuessingGameUI game = new NumberGuessingGameUI();
    }

    }
