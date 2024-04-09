package com.example.triviagame;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class triviagame extends Application {

    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private Label questionLabel;
    private ImageView imageView;
    private MediaView mediaView;
    private List<Button> optionButtons;
    private Label feedbackLabel;
    private Label scoreLabel;
    private Timeline timeline;
    private int timeSeconds = 15; // Time for each question in seconds

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Create questions
        createQuestions();

        // Create UI elements
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.getStyleClass().add("root"); // root style class

        // Top: Score and Timer
        HBox topBox = new HBox(20);
        topBox.setAlignment(Pos.CENTER);
        topBox.getStyleClass().add("box-top"); // top box style class
        scoreLabel = new Label("Score: 0");
        scoreLabel.getStyleClass().add("label-score"); // score label style class
        Label timerLabel = new Label("Time: " + timeSeconds);
        timerLabel.getStyleClass().add("label-timer"); // timer label style class
        topBox.getChildren().addAll(scoreLabel, timerLabel);
        root.setTop(topBox);

        // Center: Question and Image/Media
        VBox centerBox = new VBox(10);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.getStyleClass().add("box-center"); // Add center box style class
        questionLabel = new Label();
        questionLabel.getStyleClass().add("label-question"); // Add question label style class
        imageView = new ImageView();
        imageView.setFitWidth(300);
        imageView.setFitHeight(100);
        mediaView = new MediaView();
        mediaView.getStyleClass().add("media-view"); // Add media view style class
        mediaView.setFitWidth(400);
        mediaView.setFitHeight(200);
        centerBox.getChildren().addAll(questionLabel, imageView, mediaView);
        root.setCenter(centerBox);

        // Bottom: Options and Feedback
        VBox bottomBox = new VBox(10);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.getStyleClass().add("box-bottom"); // Add bottom box style class
        optionButtons = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Button optionButton = new Button();
            optionButton.setPrefWidth(200);
            optionButton.getStyleClass().add("button-option"); // Add option button style class
            optionButton.setOnAction(event -> handleOptionButtonClick(optionButton));
            optionButtons.add(optionButton);
            bottomBox.getChildren().add(optionButton);
        }
        feedbackLabel = new Label();
        feedbackLabel.getStyleClass().add("label-feedback"); // Add feedback label style class
        bottomBox.getChildren().add(feedbackLabel);
        root.setBottom(bottomBox);

        // Initialize the game
        showQuestion();

        // Create timer
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timeSeconds--;
            timerLabel.setText("Time: " + timeSeconds);
            if (timeSeconds <= 0) {
                handleTimeUp();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        // Create scene and show stage
        Scene scene = new Scene(root, 1100, 700);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm()); // Add CSS file.
        primaryStage.setTitle("Lesotho Trivia Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private void createQuestions() {
        questions = new ArrayList<>();
        questions.add(new Question(
                "What is the capital city of Lesotho?",
                "Maseru",
                "Roma",
                "Mohale's Hoek",
                "Leribe",
                "Maseru",
                "Maseru.jpg",
                "C:\\Users\\Admin\\IdeaProjects\\triviaGame\\src\\main\\java\\com\\example\\triviagame\\Maseru.mp4"
        ));
        questions.add(new Question(
                "Which mountain inspired the design of Basotho hat?",
                "Mount Qiloane",
                "The Atlas Mountains",
                "The Andes",
                "The Rockies",
                "Mount Qiloane",
                "qiloane.jpg",
                "C:\\Users\\Admin\\IdeaProjects\\triviaGame\\src\\main\\java\\com\\example\\triviagame\\qiloane.mp4"
        ));
        questions.add(new Question(
                "What is the traditional Basotho hat called?",
                "Fes",
                "Turban",
                "Mokorotlo",
                "Fedora",
                "Mokorotlo",
                "hat.jpg",
                "C:\\Users\\Admin\\IdeaProjects\\triviaGame\\src\\main\\java\\com\\example\\triviagame\\hat.mp4"
        ));
        questions.add(new Question(
                "What is the name of Lesotho's highest peak?",
                "Mount Qiloane",
                "Thabana Ntlenyana",
                "Mount Afadja",
                "Mount Cameroon",
                "Thabana Ntlenyana",
                "thaba.jpg",
                "C:\\Users\\Admin\\IdeaProjects\\triviaGame\\src\\main\\java\\com\\example\\triviagame\\thaba.mp4"
        ));
        questions.add(new Question(
                "Which river forms the border between Lesotho and South Africa?",
                "Caledon River",
                "Zambezi River",
                "Limpopo River",
                "Niger River",
                "Caledon River",
                "caledon.jpg",
                "C:\\Users\\Admin\\IdeaProjects\\triviaGame\\src\\main\\java\\com\\example\\triviagame\\Caledon.mp4"
        ));

        // Shuffle the questions for randomness
        Collections.shuffle(questions);
    }

    private void showQuestion() {
        Question currentQuestion = questions.get(currentQuestionIndex);
        questionLabel.setText(currentQuestion.getQuestion());
        imageView.setImage(new Image(new File(currentQuestion.getImagePath()).toURI().toString()));
        if (currentQuestion.hasVideo()) {
            Media media = new Media(new File(currentQuestion.getVideoPath()).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop indefinitely

            mediaPlayer.play();

            // Stop the video after 5 seconds
            Timeline videoTimer = new Timeline(new KeyFrame(Duration.seconds(5), e -> {
                mediaPlayer.stop();
            }));
            videoTimer.setCycleCount(5);
            videoTimer.play();
        } else {
            mediaView.setMediaPlayer(null);
        }

        List<String> options = currentQuestion.getOptions();
        Collections.shuffle(options);
        for (int i = 0; i < 4; i++) {
            optionButtons.get(i).setText(options.get(i));
        }
    }

    private void handleOptionButtonClick(Button button) {
        String selectedAnswer = button.getText();
        Question currentQuestion = questions.get(currentQuestionIndex);
        if (currentQuestion.isCorrectAnswer(selectedAnswer)) {
            score++;
            feedbackLabel.setText("Correct!");
        } else {
            feedbackLabel.setText("Incorrect. The correct answer is: " + currentQuestion.getCorrectAnswer());
        }

        scoreLabel.setText("Score: " + score);

        // Move to the next question or end the game
        currentQuestionIndex++;
        if (currentQuestionIndex < questions.size()) {
            showQuestion();
            timeSeconds = 15; // Reset time for each question
        } else {
            endGame();
        }
    }

    private void handleTimeUp() {
        feedbackLabel.setText("Time's up! The correct answer is: " + questions.get(currentQuestionIndex).getCorrectAnswer());
        scoreLabel.setText("Score: " + score);

        // Move to the next question or end the game
        currentQuestionIndex++;
        if (currentQuestionIndex < questions.size()) {
            showQuestion();
            timeSeconds = 15; // Reset time for each question
        } else {
            endGame();
        }
    }

    private void endGame() {
        timeline.stop();
        questionLabel.setText("Game Over! Thank You For Your Participation!");
        imageView.setImage(null);
        mediaView.setMediaPlayer(null);
        feedbackLabel.setText("Final Score: " + score + " / " + questions.size()+"\n" + "*** KHOTSO! PULA! NALA! ***");
        for (Button button : optionButtons) {
            button.setVisible(false);
        }
    }

    public static class Question {
        private String question;
        private List<String> options;
        private String correctAnswer;
        private String imagePath;
        private String videoPath;

        public Question(String question, String option1, String option2, String option3, String option4, String correctAnswer, String imagePath, String videoPath) {
            this.question = question;
            this.options = new ArrayList<>();
            this.options.add(option1);
            this.options.add(option2);
            this.options.add(option3);
            this.options.add(option4);
            this.correctAnswer = correctAnswer;
            this.imagePath = imagePath;
            this.videoPath = videoPath;
        }

        public String getQuestion() {
            return question;
        }

        public List<String> getOptions() {
            return options;
        }

        public String getCorrectAnswer() {
            return correctAnswer;
        }

        public String getImagePath() {
            return imagePath;
        }

        public String getVideoPath() {
            return videoPath;
        }

        public boolean hasVideo() {
            return videoPath != null && !videoPath.isEmpty();
        }

        public boolean isCorrectAnswer(String answer) {
            return correctAnswer.equals(answer);
        }
    }
}
