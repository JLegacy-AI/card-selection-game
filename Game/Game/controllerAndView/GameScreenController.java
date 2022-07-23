package Game.controllerAndView;

import Game.model.Card;
import Game.model.CardCommand;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class GameScreenController implements Initializable {

    public Text nameLabel;                                  //Text-Variable for name
    public Text timerLabel;                                 //Text-Variable for Timer

    public BorderPane BORDERPANE;                           //Main Screen BorderPane

    public GridPane GRIDPANE;                               //Grid-Variable for Cards

    public Integer timeSelection = 20;                      //Integer-Variable Store Time for Selection
    public Integer timeView = 2;                            //Integer-Variable Store Time for Cards View
    public String playerName;
    public boolean started = false;

    public ArrayList<CardCommand> cardList= new ArrayList<>();     //ArrayList-Variable for Storing Cards

    public ArrayList<Card> selectedCard = new ArrayList<>();//ArrayList-Variable for Selected Cards

    public Timeline timelineCardShow = new Timeline();      //TimeLine Control Counters
    public Timeline timelineCardSelection = new Timeline(); //TimeLine Control Counters

    private String startDescription= "------> Select Three Similar Card You will Win\n" +
            "------> If you win You will get a Fact against reward\n" +
            "------> If you Loss Don't Worry Play Again and Again.\n" +
            "Enter Your Name below.";
    private String[] facts ={"Human Body facts:\nYour heart beats around 100000 times a day,\n 36500000 times a year and over a billion times if you live beyond 30.",
    "Human Body facts:\nThe left side of your body is controlled by the right side of your\n brain while the right side of your body is controlled by the left side of your brain.",
    "Pluto :\n It's tough being the little guy and no one knows this better than Pluto who\n isn't even considered a planet anymore.",
    "Jupiter :\n It's big, its angry and its home to some of the most extreme conditions\n in our solar system"};


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getPlayerName();            //Get Player Name
        setGRIDPANE();              //Setting Card GridPane
        cardsShowTimeLine();        //Setting TimeLine
    }


    //Method to reset Complete Game
    public void retryOnAction() {
        resetTimeCards();           //On Retry Button
    }


    //Method to Close Game
    public void exitOnAction() throws IOException {
        System.exit(0);
    }


    //Method for adding Cards in GridPane
    public void setGRIDPANE(){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {

                Canvas cv = new Canvas();
                cv.setWidth(180);
                cv.setHeight(180);

                Card card = new Card.Builder(5)
                        .setCode((int) (Math.random() * 100)%4+1)
                        .setCv(cv)
                        .setStateHide(true)
                        .build();

                card.flip();

                BorderPane cardHolder = new BorderPane();
                cardHolder.setCenter(card.getCv());

                cardHolder.setOnMouseClicked(e -> {   //If Mouse Clicked on Card

                    if(card.isStateHide()){
                        card.flip();
                        selectedCard.add(card);
                        try {
                            if(!cardsCheck()){
                                try {
                                    retry(false);
                                    resetTimeCards();
                                } catch (IOException ioException) {
                                    ioException.printStackTrace();
                                }
                            }
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }

                });
                cardList.add(card);
                GRIDPANE.add(cardHolder, i, j);
            }
        }
    }

    //Method for CardSelection TimeLine
    public void cardsSelectionTimeLine(){
        timelineCardSelection.setCycleCount(Timeline.INDEFINITE);
        timelineCardSelection.getKeyFrames().add(new KeyFrame(Duration.seconds(1), e-> {
            timeSelection--;
            timerLabel.setText(""+timeSelection);
            if(timeSelection <= 0 ){
                timelineCardSelection.stop();
                try {
                    retry(false);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }));

    }

    //Method for Card Show Timeline
    public void cardsShowTimeLine(){
        timelineCardShow.setCycleCount(Timeline.INDEFINITE);
        timelineCardShow.getKeyFrames().add(new KeyFrame(Duration.seconds(1), e-> {
            timeView--;
            timerLabel.setText(""+timeView);
            if(timeView == 0 ){
                timelineCardShow.stop();
                flipCards();
                if(!started){
                    cardsSelectionTimeLine();
                    timelineCardSelection.playFromStart();
                    started=true;
                }else{
                    timelineCardSelection.playFromStart();
                }
            }
        }));
        timelineCardShow.playFromStart();
    }

    //Method to flip All Cards
    public void flipCards(){
        for (CardCommand c:  cardList){
            c.flip();
        }
    }

    //Method check After All Card Selection
    public boolean cardsCheck() throws IOException {
        int code=-1;
        for (Card card : selectedCard) {
            if(code!=-1 && code!=card.getCode()){
                return false;
            }
                code = card.getCode();
        }
        if(selectedCard.size()==3){
            retry(true);
            resetTimeCards();
        }
        return true;
    }



    //Reset Cards
    public void resetTimeCards(){
        timeSelection = 20;
        timeView = 2;
        timelineCardShow.playFromStart();
        selectedCard.clear();
        setGRIDPANE();
    }


    //Method to get Game Player Name
    public void getPlayerName(){
        TextInputDialog tid = new TextInputDialog();
        tid.setContentText(null);
        tid.setHeaderText(startDescription);
        tid.setGraphic(null);
        tid.showAndWait();
        System.out.println(tid.getResult());
        playerName = tid.getResult();
        if(playerName.isEmpty()){
            getPlayerName();
        }
        nameLabel.setText(playerName.split(" ")[0]);
    }

    //Method Which Confirm after every Game Over
    public void retry(boolean win) throws IOException {
        if(win){
            showAlert("You Win:\n"+facts[(int)(Math.random()*100)%facts.length]);
        }else{
            showAlert("You Lose:\nDo You want to run this program?\nPress OK to start and Cancel to cancel");
        }

    }

    public void showAlert(String msg) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(msg);
        alert.setContentText(null);
        Optional<ButtonType> option = alert.showAndWait();
        if ( option.get() == ButtonType.CANCEL) {
            exitOnAction();
        } else if (option.get() == ButtonType.OK) {
            resetTimeCards();
        }
    }

}
