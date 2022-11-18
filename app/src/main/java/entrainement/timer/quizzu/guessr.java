package entrainement.timer.quizzu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class guessr extends AppCompatActivity {
    private EditText propose;
    private TextView reponse_devoile;
    private TextView question, scoretext;
    private Button valider;
    private List<String> cap = new ArrayList<>();
    private String exemple;
    private String bonne_reponse;
    private String contenu_fichier_texte;
    private int score = 0;
    private int matiere=R.raw.wsh;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guessr);
        ecran_borderless();
        valider = findViewById(R.id.valider);
        propose = findViewById(R.id.reponse);
        question = findViewById(R.id.question);
        scoretext = findViewById(R.id.score);
        reponse_devoile = findViewById(R.id.reponsez);
        contenu_fichier_texte = reading_file(matiere);
        demarrage_en_cours();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.manu_choix, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Toast.makeText(this, "item1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.mode_facile:
                SwitchActivity(MainActivity.class);
                break;
            case R.id.mode_difficile:
                Toast.makeText(this, "Vous etes deja en mode difficile", Toast.LENGTH_SHORT).show();
                break;
            case R.id.apprentissage:
                SwitchActivity(apprentissage.class);
                break;
            case R.id.wmic:
                contenu_fichier_texte = reading_file(R.raw.wsh);
                demarrage_en_cours();
                break;
            case R.id.wmi:
                contenu_fichier_texte = reading_file(R.raw.wmi);
                demarrage_en_cours();
                break;
            case R.id.WSH:
                contenu_fichier_texte = reading_file(R.raw.wsh);
                demarrage_en_cours();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void SwitchActivity(Class activity) {
        Intent intent = new Intent(guessr.this, activity);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void demarrage_en_cours() {
        reset_bouton_question();
        lancer_la_partie();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void reset_bouton_question() {
        reponse_devoile.setText(" ");
        question.setText(" ");
        scoretext.setText("0");
        score = 0;
        scoretext.setBackgroundColor(Color.BLACK);
        propose.setText("");
        propose.setHint("Votre réponse ...");
        reponse_devoile.setVisibility(View.INVISIBLE);
    }

    private void ecran_borderless() {
//        getSupportActionBar().hide();
//        Window w = getWindow();
//        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    private int number_generator() {
        int maximum = cap.size() - 1;
        int minimum = 0;
        Random rn = new Random();
        int range = maximum - minimum + 1;
        int randomNum = rn.nextInt(range) + minimum;
        return randomNum;
    }

    private String reading_file(int matiere) {
        InputStream inputStream = getResources().openRawResource(this.matiere);
        String value = "";
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String eachline = null;
        try {
            eachline = bufferedReader.readLine();
            while (eachline != null) {
                eachline = bufferedReader.readLine();
                value = value + "\n" + eachline;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }


    private void j_enregistre_la_reponse(List<String> string_care) {
        int nombre_au_hasard = number_generator();
        exemple = (string_care.get(nombre_au_hasard)).split("-")[1];
        bonne_reponse = (string_care.get(nombre_au_hasard)).split("-")[0];
        question.setText(exemple);
        reponse_devoile.setText(bonne_reponse);
        reponse_devoile.setVisibility(View.INVISIBLE);
    }

    private void lancer_la_partie() {
        String[] liste_questions = contenu_fichier_texte.split(";");
        cap.addAll(Arrays.asList(liste_questions));
        j_enregistre_la_reponse(cap);
        clique_sur_bouton();
    }

    private void clique_sur_bouton() {
        valider.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                try {
                    revelation_des_reponses(propose.getText().toString());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void revelation_des_reponses(String proposition) throws InterruptedException {
        if (bonne_reponse.trim().equals(proposition.trim())) {
            Toast.makeText(this, "Bonne Réponse", Toast.LENGTH_SHORT).show();
            reset_bouton_question();
            Thread.sleep(3000);
            lancer_la_partie();
        } else {
            String presque = bonne_reponse.trim().toUpperCase(Locale.ROOT);
            if (presque.equals(proposition.trim().toUpperCase(Locale.ROOT))) {
                Toast.makeText(this, "PRESQUE !!", Toast.LENGTH_SHORT).show();
            } else {
                score += 1;
                propose.setText(" ");
                scoretext.setText(String.valueOf(score));
                scoretext.setBackgroundColor(Color.RED);
                if (score == 3) {
                    reponse_devoile.setVisibility(View.VISIBLE);
                }
            }
        }
    }


}
