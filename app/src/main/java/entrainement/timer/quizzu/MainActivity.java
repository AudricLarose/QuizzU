package entrainement.timer.quizzu;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    List<String> reponse = new ArrayList<>();
    private Button reponseA, reponseB, reponseC, reponseD;
    private TextView question;
    private List<String> cap=new ArrayList<>();
    private String exemple;
    private String bonne_reponse;
    private String contenu_fichier_texte;
    private int matiere=R.raw.wsh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ecran_borderless();
        question = findViewById(R.id.question);
        reponseA = findViewById(R.id.reponseA);
        reponseB = findViewById(R.id.reponseB);
        reponseC = findViewById(R.id.reponseC);
        reponseD = findViewById(R.id.reponseD);

        contenu_fichier_texte=reading_file();
        lancer_la_partie();
//        SwitchActivity();
    }

//    private void SwitchActivity() {
//        lvldifficile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this,guessr.class);
//                startActivity(intent);
//            }
//        });
//    }

    private int number_generator(){
        int maximum=cap.size()-1;
        int minimum=0;
        Random rn = new Random();
        int range = maximum - minimum + 1;
        int randomNum =  rn.nextInt(range) + minimum;
        return randomNum;
    }

    private String reading_file(){
        InputStream inputStream = getResources().openRawResource(matiere);
        String value="";
        BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(inputStream));
        String eachline = null;
        try {
            eachline = bufferedReader.readLine();
            while (eachline != null) {
                eachline = bufferedReader.readLine();
                value=value+" "+eachline;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

    private List<String> recuperation_sac_de_reponse(List<String> string_care) {
        List<String> temp=new ArrayList<>();
        List<String> bag_reponse=new ArrayList<>();
        for (int i = 0; i < string_care.size(); i++) {
            try {
                String stringcar = (string_care.get(i)).split("-")[0];
                if (!stringcar.equals(bonne_reponse)) {
                    temp.add(stringcar);
                }
            } catch (Exception e){
                continue;
            }
        }
        Collections.shuffle(temp);
        for (int i = 0; i < 3; i++) {
            reponse.add(temp.get(i));
        }
        bag_reponse.add(bonne_reponse);
        Collections.shuffle(bag_reponse);
        return bag_reponse;
    }

    private void j_enregistre_la_reponse(List<String> string_care) {
        int  nombre_au_hasard=number_generator();
        exemple=(string_care.get(nombre_au_hasard)).split("-")[1];
        bonne_reponse=(string_care.get(nombre_au_hasard)).split("-")[0];
    }

    private void lancer_la_partie() {
        String[] liste_mmodele= contenu_fichier_texte.split(";");
        for (int i = 0; i < liste_mmodele.length; i++) {
            cap.add(liste_mmodele[i]);
        }
        take(cap);
        affichage_du_texte(reponse);
        clique_sur_bouton(reponseA);
        clique_sur_bouton(reponseB);
        clique_sur_bouton(reponseC);
        clique_sur_bouton(reponseD);
    }

    private void take(List<String> string_care){
        j_enregistre_la_reponse(string_care);
        List<String> reponse_vrac=recuperation_sac_de_reponse(string_care);
        reponse.addAll(reponse_vrac);
    }

    private void ecran_borderless() {
        getSupportActionBar().hide();
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    private void affichage_du_texte(List<String> reponse) {
        reponse = je_melange();
        association_reponses_adaptateur(reponse);
    }

    private List<String> je_melange() {
        Collections.shuffle(reponse);
        return reponse;
    }

    private void association_reponses_adaptateur(List<String> reponse) {
        question.setText(exemple);
        reponseA.setText(reponse.get(0));
        reponseB.setText(reponse.get(1));
        reponseC.setText(reponse.get(2));
        reponseD.setText(reponse.get(3));
    }

    private void clique_sur_bouton(Button bouton) {
        bouton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                revelation_des_reponses(bouton.getText().toString(),bouton);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void revelation_des_reponses(String proposition, Button bouton) {
        if (bonne_reponse.equals(proposition)) {
            bouton.setBackgroundColor(Color.GREEN);
            Toast.makeText(this, "Bonne Réponse", Toast.LENGTH_SHORT).show();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            reset_bouton_question(reponseA);
            reset_bouton_question(reponseB);
            reset_bouton_question(reponseC);
            reset_bouton_question(reponseD);
            lancer_la_partie();
        } else {
            bouton.setBackgroundColor(Color.MAGENTA);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void reset_bouton_question(Button bouton) {
        bouton.setBackgroundColor(Color.WHITE);
        bouton.setText(" ");
        question.setText(" ");
    }
}