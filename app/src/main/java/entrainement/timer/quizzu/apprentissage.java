package entrainement.timer.quizzu;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class apprentissage extends AppCompatActivity {
    List<Flashcard> FlashcardList = new ArrayList<>();
    int PICKFILE_RESULT_CODE = 101;
    private Adapteur adapter;
    private RecyclerView recyclerView;
    private GridLayoutManager grid;
    private Boolean isclicked = false;
    private FloatingActionButton floatingActionButton;
    private FloatingActionButton reveal;
    private String text = " ";
    private TextToSpeech textToSpeech;
    private String testeux = "manger; boire; popo;  titi;pipi;caca";
    private List<String> list_test_grt = new ArrayList<>();
    private String[] prepocessed_file;
    private String texte_brute;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apprentissage);
        recyclerView = findViewById(R.id.Recyclerview_main5);
        floatingActionButton = findViewById(R.id.fab5);
        prepocessed_file = preprocessing(texte_brute);
        List<Integer> list = process_string(prepocessed_file);
        transformation(list);
        RecyclerView("f5", FlashcardList);
        selectionner_fichier();
    }


    // And then somewhere, in your activity:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKFILE_RESULT_CODE && resultCode == RESULT_OK) {
            Uri content_describer = data.getData();
            BufferedReader reader = null;
            try {
                InputStream in = getContentResolver().openInputStream(content_describer);
                texte_brute=reading_file(in);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }


    private String reading_file(InputStream inputStream) {
//        InputStream inputStream = getResources().openRawResource(R.raw.texmpot);
        String value = "";
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String eachline = null;
        try {
            eachline = bufferedReader.readLine();
            while (eachline != null) {
                // `the words in the file are separated by space`, so to get each words
                String[] words = eachline.split(" ");
                eachline = bufferedReader.readLine();
                value = value + "\n" + eachline;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

    private void selectionner_fichier() {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
        chooseFile.setType("*/*");
        startActivityForResult(Intent.createChooser(chooseFile, "Choose a file"), PICKFILE_RESULT_CODE);
    }

    private String[] preprocessing(String texte_brute) {
        String[] cut = texte_brute.split("\n");
        for (int i = 0; i < cut.length; i++) {
            cut[i] = cut[i].replace(" ", "");
            cut[i] = cut[i].replace("\t", " ");
        }

        return cut;
    }

    private void transformation(List<Integer> list) {
        Flashcard branche = null;
        Flashcard branchePArent;

        for (int i = 0; i < list.size(); i++) {
            int rang = list.get(i);
            if (rang == 0) {
                FlashcardList.add(new Flashcard(String.valueOf(rang), prepocessed_file[i], new ArrayList<Flashcard>()));
                branchePArent = branche = FlashcardList.get(0);
            }
            if (rang != 0) if (i > 0) {
                if (rang > list.get(i - 1)) {
                    branchePArent = branche;
                    branche.getSous_groupe().add(new Flashcard(String.valueOf(rang), prepocessed_file[i], new ArrayList<Flashcard>(), new ArrayList<Flashcard>()));
                    branche.getSous_groupe().get(branche.getSous_groupe().size() - 1).getParent().add(branchePArent);
                    branche = branche.getSous_groupe().get(0);
                }
                if (rang == list.get(i - 1)) {
                    if (branche.getParent() != null) {
                        branche = branche.getParent().get(0);
                        branchePArent = branche;
                        branche.getSous_groupe().add(new Flashcard(String.valueOf(rang), prepocessed_file[i], new ArrayList<Flashcard>(), new ArrayList<Flashcard>()));
                        branche.getSous_groupe().get(branche.getSous_groupe().size() - 1).getParent().add(branchePArent);
                        branche = branche.getSous_groupe().get(branche.getSous_groupe().size() - 1);
                    } else {
                        FlashcardList.add(new Flashcard(String.valueOf(rang), prepocessed_file[i], new ArrayList<Flashcard>(), new ArrayList<Flashcard>()));
                        branche = FlashcardList.get(branche.getSous_groupe().size() - 1);
                        branchePArent = FlashcardList.get(branche.getSous_groupe().size() - 1);
                    }
                }
                if (rang < list.get(i - 1)) {
                    int descente = Math.abs(list.get(i - 1) - rang) + 1;
                    Boolean breackActive = false;
                    for (int k = descente; k >= 1; k--) {
                        if (branche.getParent() != null) {
                            branche = branche.getParent().get(0);
                        } else {
                            if (k == 1) {
                                FlashcardList.add(new Flashcard(String.valueOf(rang), prepocessed_file[i], new ArrayList<Flashcard>(), new ArrayList<Flashcard>()));
                                branchePArent = branche = FlashcardList.get(branche.getSous_groupe().size() - 1);
                                breackActive = true;
                            }
                        }
                    }
                    if (!breackActive) {
                        branchePArent = branche;
                        branche.getSous_groupe().add(new Flashcard(String.valueOf(rang), prepocessed_file[i], new ArrayList<Flashcard>(), new ArrayList<Flashcard>()));
                        branche.getSous_groupe().get(branche.getSous_groupe().size() - 1).getParent().add(branchePArent);
                        branche = branche.getSous_groupe().get(branche.getSous_groupe().size() - 1);
                    }
                }
            }
        }
    }

    private List<Integer> process_string(String[] phrase) {
        List<Integer> list_counter = new ArrayList<>();
        for (int i = 0; i < phrase.length; i++) {
            int countBlank = 0;
            for (int j = 0; j < phrase[i].length(); j++) {
                if (phrase[i].charAt(j) == ' ') {
                    countBlank = countBlank + 1;
                }
            }
            list_counter.add(countBlank);
        }
        return list_counter;
    }

    private Adapteur RecyclerView(final String f, final List<Flashcard> list) {
        adapter = new Adapteur(this, list, f);
        grid = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(grid);
        recyclerView.setAdapter(adapter);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((list != null) && (list.size() > 0) && (list.get(0).getParent() != null)) {
                    if ((list.get(0).getParent().get(0).getParent() != null)) {
                        RecyclerView("f5", list.get(0).getParent().get(0).getParent().get(0).getSous_groupe());
                    } else {
                        RecyclerView("f5", FlashcardList);
                    }
                } else {
                    RecyclerView("f5", FlashcardList);
                }
                if (list.get(0).getParent() == null) {
                    RecyclerView("f5", FlashcardList);
                }
            }
        });
        adapter.setWhenClickListener(new Adapteur.OnItemsClickListener() {
            @Override
            public void onItemClick(final Flashcard Flashcard, final List<Flashcard> Flashcards_list) {
                RecyclerView("f5", Flashcards_list);
            }
        });
        return adapter;
    }

}
