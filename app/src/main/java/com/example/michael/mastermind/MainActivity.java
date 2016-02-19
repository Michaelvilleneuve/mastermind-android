package com.example.michael.mastermind;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private List<Integer> chosenColors = new ArrayList<Integer>();
    private List<Integer> colors = new ArrayList<Integer>();
    private List<Integer> secret = new ArrayList<Integer>();
    private EditText editText;
    private Button red;
    private Button orange;
    private Button blue;
    private Button green;
    private Button yellow;
    private Button violet;
    private Button secretbutton;
    private Button newgame;
    private TextView proposition;
    private TextView result;
    private TextView oldprop;
    private TextView result2;
    private String current_proposition;
    private Boolean clear = false;
    private int countNumbersOk = 0;
    private int countNumbersWrong = 0;
    private String oldpropval;
    private String playerNumber;
    private String computerNumber;
    private String yourProposition = "";
    private String computerResult = "";
    private char[] charArrayForResult;
    private int x = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        colors = Arrays.asList(1,2,3,4,5,6);

        editText = (EditText) findViewById(R.id.editText);


        defineResultsBlocksInView();

        getRandomSecret();

        defineButtonsInView();

        addListenersForButtons();

        displayStartMessage();
    }
    private void defineResultsBlocksInView() {
        proposition = (TextView) findViewById(R.id.proposition);
        result = (TextView) findViewById(R.id.result);
        result2 = (TextView) findViewById(R.id.result2);
        oldprop = (TextView) findViewById(R.id.oldprop);
    }

    private void defineButtonsInView() {
        red = (Button) findViewById(R.id.red);
        orange = (Button) findViewById(R.id.orange);
        blue = (Button) findViewById(R.id.blue);
        green = (Button) findViewById(R.id.green);
        yellow = (Button) findViewById(R.id.yellow);
        violet = (Button) findViewById(R.id.violet);
        secretbutton = (Button) findViewById(R.id.secretbutton);
        newgame = (Button) findViewById(R.id.newgame);
    }

    private void addListenersForButtons() {
        red.setOnClickListener(addColor);
        orange.setOnClickListener(addColor);
        blue.setOnClickListener(addColor);
        green.setOnClickListener(addColor);
        yellow.setOnClickListener(addColor);
        violet.setOnClickListener(addColor);
        secretbutton.setOnClickListener(setSecretOnClick);
        newgame.setOnClickListener(newGame);
    }

    View.OnClickListener setSecretOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            getRandomSecret();
        }
    };

    View.OnClickListener newGame = new View.OnClickListener() {
        public void onClick(View v) {
            startNewGame();
        }
    };

    View.OnClickListener addColor = new View.OnClickListener() {
        public void onClick(View v) {
            Button button = (Button)v;
            generateProposition(button.getText().toString());
        }
    };

    private void generateProposition(String id) {

        setProposition();

        appendNumberToPropositionInView(id);
        showResultIfLengthIsFour(id);
    }

    private void setProposition() {
        if(clear) {
            current_proposition = "";
            countNumbersOk = 0;
            clear = false;
        } else {
            current_proposition = proposition.getText().toString();
        }
    }

    private void appendNumberToPropositionInView(String id) {
        if (chosenColors.size() < 4) {
            chosenColors.add(Integer.parseInt(id));
            proposition.setText(current_proposition + id);

        }
    }

    private void showResultIfLengthIsFour(String id) {
        if (chosenColors.size() == 4) {
            showResult();
            chosenColors.clear();
            proposition.setText(current_proposition + id);
            oldprop.setText("Dernier essai : " + current_proposition + id);
            clear = true;
        }
    }


    private void showResult() {
        defineComputerAndUserStrings();

        getResults();

        result.setText(String.valueOf(countNumbersOk) + " chiffres correctement placés +");
        result2.setText(String.valueOf(countNumbersWrong) + " chiffres bon mais mal placés");

        Log.d(this.getClass().getName(), "Computer : ");
        Log.d(this.getClass().getName(), computerResult);
    }

    private void defineComputerAndUserStrings() {
        yourProposition = "";
        computerResult = "";

        for (int i=0; i<4; i++) {
            yourProposition = yourProposition + String.valueOf(chosenColors.get(i));
            computerResult = computerResult + String.valueOf(secret.get(i));
        }
    }

    private void getResults() {
        countNumbersOk = 0;
        countNumbersWrong = 0;

        checkIfStringsAreSimilar();

        charArrayForResult = computerResult.toCharArray();


        checkPerfectsNumbers();

        checkCorrectsButNotPositionned();

    }

    private void checkIfStringsAreSimilar() {
        if(computerResult.equals(yourProposition)) {
            vibrate();
            displayBravoMessage();
            startNewGame();
        }
    }

    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {10, 300,30,200,30,170,30,600};
        v.vibrate(pattern, -1);
    }


    private void checkPerfectsNumbers() {
        for(int i = 0; i < 4; i++) {
            computerNumber = String.valueOf(computerResult.charAt(i));
            playerNumber = String.valueOf(yourProposition.charAt(i));

            incrementOkCountIfSame(i);
        }
    }

    private void incrementOkCountIfSame(Integer i) {
        if (computerNumber.equals(playerNumber)) {
            countNumbersOk++;

            // It's the same, we remove it, so we don't increment countNumbersWrong in "checkIfThisNumberIsInTheResult"
            charArrayForResult[i] = 0;
        }
    }

    private void checkCorrectsButNotPositionned() {
                for(int i = 0; i < 4; i++) {
                    checkIfThisNumberIsInTheResult(i);
                }
            }

    private void checkIfThisNumberIsInTheResult(int i) {
        x = 0;
        // Loop through result to check if it contains the current number
        for (char charOfResult : charArrayForResult) {
            // If it's the same number and number as not been removed
            if (charOfResult == yourProposition.charAt(i) && charArrayForResult[x] != 0) {
                charArrayForResult[x] = 0;
                countNumbersWrong++;
                break;
            }
            x++;
        }
    }

    private void displayBravoMessage() {
        new AlertDialog.Builder(this)
            .setTitle("BRAVO !!! :)")
                .setMessage("Vous pouvez rejouer")
            .show();
    }

    private void getRandomSecret() {
        if(isEmpty(editText)) {
            setRandomSecret();
        } else {
            setSecretFromUserInput();
        }
    }

    private void setRandomSecret() {
        secret.clear();

        Random random = new Random();
        int index;
        for (int i=0; i<4; i++) {
            index = random.nextInt(colors.size());
            secret.add(colors.get(index));
        }
    }

    private void setSecretFromUserInput() {
        secret.clear();

        for (int i=0; i<4; i++) {
            secret.add(Integer.parseInt(String.valueOf(editText.getText().charAt(i))));
        }
    }

    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0) {
            return false;
        } else {
            return true;
        }
    }

    private void startNewGame() {
        clearResults();
        setRandomSecret();
    }

    private void clearResults() {
        chosenColors.clear();
        editText.setText("");
        proposition.setText("");
        result.setText("");
        result2.setText("");
    }

    private void displayStartMessage() {
        new AlertDialog.Builder(this)
                .setTitle("Hello,")
                .setMessage("Cliquez sur les couleurs souhaitées pour démarrer. Vous pouvez également tester une combinaison (pour Jean-pierre),  en définissant un résultat.")
                .show();
    }

}
