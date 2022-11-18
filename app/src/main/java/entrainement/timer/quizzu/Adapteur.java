package entrainement.timer.quizzu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import entrainement.timer.quizzu.Flashcard;

public class Adapteur extends RecyclerView.Adapter<Adapteur.Leholder>{
        public List<Flashcard> listFlashcard = new ArrayList<>();
        Context context;
        private String f;
        private boolean isClicked = false;
        private View view;
        public int position_real;
        private OnItemsClickListener listener;


        public Adapteur(Context context, List<Flashcard> listFlashcard, String f) {
            this.context = context;
            this.listFlashcard = listFlashcard;
            this.f = f;
        }

        @NonNull
        @Override
        public Leholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = null;
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
            return new Leholder(view,listFlashcard);
        }

        @Override
        public void onBindViewHolder(@NonNull Leholder holder, @SuppressLint("RecyclerView") int position) {
//        Collections.shuffle(listFlashcard);
            final Flashcard Flashcard = listFlashcard.get(position);

            position_real=position;
            holder.text.setText(Flashcard.getTheme());
            holder.theme.setText(Flashcard.getName());
            if (!f.equals("f5")) {
                holder.text.setVisibility(View.GONE);
            }
            if (f.equals("r") || (f.equals("rf2"))  || f.equals("rf3")  ) {
                holder.text.setVisibility(View.VISIBLE);
            }
            holder.cardView.setBackgroundColor(Color.CYAN);
            if ((Flashcard.getSous_groupe()==null)||(Flashcard.getSous_groupe().size()==0))  {
                {
                    holder.cardView.setBackgroundColor(Color.WHITE);
                }
            }
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        if ((Flashcard.getSous_groupe()!=null)&&(Flashcard.getSous_groupe().size()>0)&&(Flashcard.getSous_groupe().get(0).getParent()==null) && (Flashcard.getSous_groupe()!=null))  {
                            Flashcard.getSous_groupe().get(0).setParent(listFlashcard);

                        }

                        if ((Flashcard.getSous_groupe()!=null) && (Flashcard.getSous_groupe()!=null) && (Flashcard.getSous_groupe().size()>0) ) {
                            listener.onItemClick(Flashcard.getSous_groupe().get(0), Flashcard.getSous_groupe());
                        }
                    }

                }
            });
        }

        @Override
        public int getItemCount() {
            if (listFlashcard != null) {
                return listFlashcard.size();
            } else {
                return 0;
            }
        }
        public void setWhenClickListener(OnItemsClickListener listener){
            this.listener = listener;
        }

        public interface OnItemsClickListener{
            void onItemClick(Flashcard Flashcard,List<Flashcard> Flashcards_list);
        }

        public List<String> getString() {
            List<String> allIngredient= new ArrayList<>();
            for (int i = 0; i < listFlashcard.size(); i++) {
                allIngredient.add(listFlashcard.get(i).getName());
            }
            return allIngredient;
        }



        public static class Leholder extends RecyclerView.ViewHolder {
            private TextView text;
            private TextView theme;
            private CardView cardView;

            public Leholder(@NonNull View itemView, List<Flashcard> listFlashcard) {
                super(itemView);
                text = itemView.findViewById(R.id.text_row);
                theme = itemView.findViewById(R.id.theme);
                cardView = itemView.findViewById(R.id.card);
                returnCard(text, theme,listFlashcard);
            }

            private void returnCard(final TextView text, final TextView theme, final List<Flashcard> listFlashcard) {
                final Boolean[] isClicked = {true};
            }
        }
    }

