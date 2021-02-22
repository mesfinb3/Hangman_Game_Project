package sample;

/**
 * Justin Hoyle, Bonna Mesfin, Juan Depazgonzalez, Diana Nguyen
 * *******
 * Creates a playable hangman game gui with a chosen list of words
 * *******
 * Change file locations for sound files on lines 270, 315, 325, & 339
 * *******
 * List of words can be changed to any list of words, baring the words aren't too long
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import java.io.File;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class HangmanGame extends Application {

    //Creates textFields and label
    private TextField tfGuess = new TextField();
    private TextField tfLettersGuessed = new TextField();
    private Label lblGuessesRemaining;

    //Declares texts and shapes
    private Text[] wordText;
    private Text win;
    private Text lose;
    private List<Shape> body;
    private Button reset;
    private boolean playStartup = true;

    //Declares variables
    private StringBuilder guessedLetters;
    private String word;
    private int guessesRemaining;

    // creates the body segments
    private Line torso = createLine(200, 200, 200, 150, 5);
    private Line lftArm = createLine(150, 215, 200, 165, 5);
    private Line rtArm = createLine(250, 215, 200, 165, 5);
    private Line lftLeg = createLine(200, 200, 175, 275, 5);
    private Line rtLeg = createLine(200, 200, 225, 275, 5);

    private String[] wordList = {"community", "mustard", "medic", "phone", "dust",
            "dynamite", "laser", "thunder", "lightning", "updog"};

    //Create line method for body
    private Line createLine(int p1, int p2, int p3, int p4, int strokeWidth) {
        Line line = new Line(p1, p2, p3, p4);
        line.setStroke(Color.BLACK);
        line.setStrokeWidth(strokeWidth);
        return line;
    }

    //Creates line method for gallows
    private Line createLineGallows(int p1, int p2, int p3, int p4, int strokeWidth) {
        Line line = new Line(p1, p2, p3, p4);
        line.setStroke(Color.SADDLEBROWN);
        line.setStrokeWidth(strokeWidth);
        return line;
    }

    private void sound(String x)
    {
        File sound = new File(x);
        PlaySound(sound);
    }

    //Takes the file name as a string and plays it
    private static void PlaySound(File Sound)
    {
        try{
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(Sound));
            clip.start();

            Thread.sleep(clip.getMicrosecondLength()/1000);
        }catch(Exception e)
        {
        }
    }

    private void cleanup() {
        Random rand = new Random();

        guessesRemaining = 6;
        torso.setVisible(false);
        lftArm.setVisible(false);
        rtArm.setVisible(false);
        lftLeg.setVisible(false);
        rtLeg.setVisible(false);

        lose.setVisible(false);
        win.setVisible(false);

        guessedLetters.setLength(0);
        tfLettersGuessed.setText("");

        for (Text text : wordText) {
            text.setVisible(false);
        }
        tfGuess.setDisable(false);
        tfLettersGuessed.setDisable(false);

        word = wordList[rand.nextInt(9)];

        playStartup = false;
    }

    private void restart(Stage stage) {
        cleanup();
        start(stage);
    }

    //Method for gui
    @Override
    public void start(Stage primaryStage) {
        Pane pane = new Pane();

        List<Node> shapes = pane.getChildren();

        //List of words and random to choose word
        Random rand = new Random();
        word = wordList[rand.nextInt(9)];

        //Creates guessedLetters as a StringBuilder for easier appends
        guessedLetters = new StringBuilder();

        // initialize and draw the gallows
        shapes.add(createLineGallows(100, 25, 200, 25, 6)); // gallow part 1
        shapes.add(createLineGallows(100, 25, 100, 300, 6)); // gallow part 2
        shapes.add(createLineGallows(175, 300, 25, 300, 8)); // gallow part 3
        shapes.add(createLineGallows(140, 25, 100, 65, 4)); //gallow part 4
        shapes.add(createLineGallows(200, 25, 200, 75, 3)); // rope

        // initialize the body
        body = new ArrayList<>();
        Ellipse head = new Ellipse(200, 112, 35, 35);
        head.setStroke(Color.BLACK);
        head.setFill(Color.WHITE);
        head.setStrokeWidth(5);
        head.setVisible(false);
        shapes.add(head);
        body.add(head);

        //Makes the body not visible
        torso.setVisible(false);
        lftArm.setVisible(false);
        rtArm.setVisible(false);
        lftLeg.setVisible(false);
        rtLeg.setVisible(false);

        //Adds to shapes
        shapes.add(torso);
        shapes.add(lftArm);
        shapes.add(rtArm);
        shapes.add(lftLeg);
        shapes.add(rtLeg);

        //Adds to body
        body.add(torso);
        body.add(lftArm);
        body.add(rtArm);
        body.add(lftLeg);
        body.add(rtLeg);

        //Creates the blank spaces
        Line[] blanks = new Line[word.length()];
        int xStart = 375;
        int lineLength = 25;
        int lineSpacing = 35;
        for (int i = 0; i < blanks.length; i++) {
            // calculate the starting point of the line segment
            int xcoord = xStart + (lineSpacing * i);
            blanks[i] = createLine(xcoord, 225, xcoord - lineLength, 225, 3);
            shapes.add(blanks[i]);
        }

        //Sets the coordinates for the dashes
        wordText = new Text[word.length()];
        int xStartw = 355;
        int lineSpacingw = 35;
        for (int i = 0; i < wordText.length; i++) {
            // Calculate the starting point of the line segment
            int xcoordw = xStartw + (lineSpacingw * i);
            wordText[i] = new Text(word.substring(i, i + 1));
            wordText[i].setFont(new Font(30));
            wordText[i].setX(xcoordw);
            wordText[i].setY(220);
            wordText[i].setVisible(false);
            shapes.add(wordText[i]);
        }

        //You win text
        win = new Text();
        win.setFont(new Font(50));
        win.setX((250));
        win.setY(350);
        win.setText("You Win!");
        shapes.add(win);
        win.setVisible(false);

        //You lose text
        lose = new Text();
        lose.setFont(new Font(50));
        lose.setX((250));
        lose.setY(350);
        lose.setText("You Lose");
        shapes.add(lose);
        lose.setVisible(false);

        reset = new Button("Reset");
        reset.setLayoutX(550);
        reset.setLayoutY(323);
        shapes.add(reset);
        reset.setVisible(false);

        // initialize the remaining guesses
        guessesRemaining = 6;

        //Creates a grid pane for the text boxes
        GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        gridPane.add(new Label("Enter a letter:"), 0, 0);
        gridPane.add(tfGuess, 1, 0);
        gridPane.add(new Label("Letters guessed:"), 0, 1);
        gridPane.add(tfLettersGuessed, 1, 1);
        gridPane.add(new Label("Guesses remaining: "), 0, 2);
        lblGuessesRemaining = new Label(String.valueOf(guessesRemaining));
        gridPane.add(lblGuessesRemaining, 0, 3);

        //Sets a border
        BorderPane thing = new BorderPane();
        thing.setRight(gridPane);
        thing.setCenter(pane);

        // Sets the background
        BackgroundImage myBI= new BackgroundImage(new Image("https://wallpaperaccess.com/full/90280.jpg",
                700,400,false,true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        thing.setBackground(new Background(myBI));

        //Calls playGame whenever a letter is entered
        tfGuess.setOnAction(e -> playGame());

        //Sets the scene size
        Scene scene = new Scene(thing, 700, 400);
        primaryStage.setTitle("Hangman Game");
        primaryStage.setScene(scene);
        primaryStage.show();
        if(playStartup) {
            sound("/Users/justinhoyle/IntelliJ Workspace/Hangman/src/startup.wav");
        }

        reset.setOnAction(e -> {
            restart(primaryStage);
        });
    }

    private void playGame() {

        String guess = tfGuess.getText();
        if (guess.length() == 0) {
            return;
        }
        if (guess.length() > 1) {
            guess = guess.substring(0, 1);
        }

        guess = guess.toUpperCase();
        tfGuess.setText("");

        // check if the letter is already guessed
        if (guessedLetters.length() > 0) {
            //Already guessed
            if (guessedLetters.indexOf(guess) > -1) {
                return;
            }
            // Not guessed
            else {
                guessedLetters.append(guess);
            }
        }
        //Not guessed
        else {
            guessedLetters.append(guess);
        }
        tfLettersGuessed.setText(guessedLetters.toString());

        // check if the letter is in the word
        boolean correctGuess = false;
        for (int i = 0; i < word.length(); i++) {
            if (guess.equalsIgnoreCase(word.substring(i, i + 1))) {
                wordText[i].setVisible(true);
                correctGuess = true;
                //Change file location
                sound("/Users/justinhoyle/IntelliJ Workspace/Hangman/src/ding.wav");
            }
        }

        // if it's not a correct guess, draw one more part
        if (!correctGuess) {
            body.get(6 - guessesRemaining).setVisible(true);
            guessesRemaining--;
            lblGuessesRemaining.setText(String.valueOf(guessesRemaining));
            //Change file location
            sound("/Users/justinhoyle/IntelliJ Workspace/Hangman/src/errorSound.wav");
        }

        // Uses stream and map stuff to compare the array and check if the entire word was guessed
        boolean userWon = Arrays.stream(wordText)
                .reduce(true, (xs, x) -> xs && x.isVisible(), (x, y) -> x && y);

        //if the user won, disable the text fields and display "You win!"
        if (userWon) {
            reset.setVisible(true);
            tfGuess.setDisable(true);
            tfLettersGuessed.setDisable(true);
            win.setVisible(true);
            //Change file location
            sound("/Users/justinhoyle/IntelliJ Workspace/Hangman/src/tada.wav");
        }

        //If the user is out of guesses, disable the text fields and display "You lose"
        if(guessesRemaining == 0)
        {
            reset.setVisible(true);
            for (Text text : wordText) {
                text.setVisible(true);
            }
            tfGuess.setDisable(true);
            tfLettersGuessed.setDisable(true);
            lose.setVisible(true);
        }

    }

    //Runs the program
    public static void main(String[] args) {
        Application.launch(args);
    }
}