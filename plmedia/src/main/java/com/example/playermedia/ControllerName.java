package com.example.playermedia;

//13 июня. Регулировка громкости. ДФ -- добавлена библиотека
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;


public class ControllerName {
    @FXML
    private ResourceBundle resources;

    @FXML
    private Slider songSlider;

    @FXML
    private Slider volumeSlider;

    @FXML
    private URL location;

    @FXML
    private ImageView heart;

    @FXML
    private ImageView menu;

    @FXML
    private ImageView next;

    @FXML
    private ImageView pause;

    @FXML
    private ImageView play;

    @FXML
    private ImageView prev;

    @FXML
    private ImageView repeat;

    @FXML
    private ImageView shuffle;

    public ControllerName() {
        mediaPlay();

    }

    @FXML
    void onButtonClickOpenList(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("list.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("List");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (Exception e) {
            System.out.println("Can't load new window");

        }
    }

    @FXML
    void play(MouseEvent event){
        play();

    }

    @FXML
    void pause(MouseEvent event){
        pause();

    }

    //13 июня. Регулировка громкости. ДФ -- вызов метода
    //volume связан с изменением в fxml файле
    @FXML
    void volume(MouseEvent event){
        volume();
    }
    
    @FXML
    void initialize() {
        menu.setOnMouseClicked(MouseEvent -> {
                    onButtonClickOpenList(MouseEvent);
                }
        );
    }
    @FXML
    private void mediaPlay(){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("select mp3", "*.mp3");
        fileChooser.getExtensionFilters().add(filter);
        String filePath = fileChooser.showOpenDialog(null).toURI().toString();
        File f = new File(filePath);
        Media media = new Media(filePath);
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
        //labelVolume.setText("10%");
        //volumeSlider.setValue(10.0);
        mediaPlayer.setVolume(10.0 * 0.01);
        beginTimer();


        mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observableValue, Duration oldValue, Duration newValue) {
                songSlider.setValue(newValue.toSeconds());
            }
        });



    }

    private boolean isPlaying;
    private Timer timer;
    private TimerTask task;
    private File directory;
    private File[] files;
    private MediaPlayer mediaPlayer;


    private void play() {
        System.out.println("Воспроизведение");
        beginTimer();
        mediaPlayer.play();
        isPlaying = true;
    }

    private void pause() {
        System.out.println("Пауза");
        mediaPlayer.pause();
        cancelTimer();
        isPlaying = false;
    }

    //13 июня. Регулировка громкости. ДФ -- метод
    private void volume() {
        volumeSlider.setValue(mediaPlayer.getVolume() * 100);
        volumeSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                mediaPlayer.setVolume(volumeSlider.getValue() / 100);
            }
        });
    }
    
    public void beginTimer() {
        timer = new Timer();
        task = new TimerTask() {
            public void run() {
                isPlaying = true;
                double current = mediaPlayer.getCurrentTime().toSeconds();
                double end = mediaPlayer.getTotalDuration().toSeconds();
                songSlider.setValue(current / end);
                if (current / end == 1) {
                    cancelTimer();
                }
            }
        };
    }
    public void cancelTimer() {
        isPlaying = false;
        timer.cancel();
    }


}
