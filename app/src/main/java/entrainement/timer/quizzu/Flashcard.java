package entrainement.timer.quizzu;


import java.util.List;


public class Flashcard {

    private int id;
    private String theme;
    private String name;
    private List<Flashcard> sous_groupe;
    private List<Flashcard> parent;
    private int incremment =0;

    public Flashcard(int id, String theme, String name, List<Flashcard> sous_groupe) {
        this.id = incremment++;
        this.theme = theme;
        this.name = name;
        this.sous_groupe = sous_groupe;
    }
    public Flashcard(String theme, String name, List<Flashcard> sous_groupe,List<Flashcard> parent) {
        this.id = incremment++;
        this.theme = theme;
        this.name = name;
        this.sous_groupe = sous_groupe;
        this.parent = parent;
    }
    public Flashcard(String theme, String name, List<Flashcard> sous_groupe) {
        this.id = incremment++;
        this.theme = theme;
        this.name = name;
        this.sous_groupe = sous_groupe;
    }

    public Flashcard(int id, String name, String theme) {
        this.id = incremment++;
        this.theme = theme;
        this.name = name;
    }

    public Flashcard(String theme, String name) {
        this.id = incremment++;
        this.theme = theme;
        this.name = name;
    }

    public List<Flashcard> getParent() {

        return parent;
    }

    public void setParent(List<Flashcard> parent) {
        this.parent = parent;
    }

    public void setSous_groupe(List<Flashcard> sous_groupe) {
        this.sous_groupe = sous_groupe;
    }

    public List<Flashcard> getSous_groupe() {
        return sous_groupe;
    }

    public Flashcard() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
