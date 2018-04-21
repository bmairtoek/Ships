package com.example.bmairtoek.myapplication;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class EnemyTurn extends AppCompatActivity {

    private static final int[] idArray={R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9, R.id.button10, R.id.button11, R.id.button12, R.id.button13, R.id.button14, R.id.button15, R.id.button16, R.id.button17, R.id.button18, R.id.button19, R.id.button20, R.id.button21, R.id.button22, R.id.button23, R.id.button24, R.id.button25, R.id.button26, R.id.button27, R.id.button28, R.id.button29, R.id.button30, R.id.button31, R.id.button32, R.id.button33, R.id.button34, R.id.button35, R.id.button36, R.id.button37, R.id.button38, R.id.button39, R.id.button40, R.id.button41, R.id.button42, R.id.button43, R.id.button44, R.id.button45, R.id.button46, R.id.button47, R.id.button48, R.id.button49, R.id.button50, R.id.button51, R.id.button52, R.id.button53, R.id.button54, R.id.button55, R.id.button56, R.id.button57, R.id.button58, R.id.button59, R.id.button60, R.id.button61, R.id.button62, R.id.button63, R.id.button64, R.id.button65, R.id.button66, R.id.button67, R.id.button68, R.id.button69, R.id.button70, R.id.button71, R.id.button72, R.id.button73, R.id.button74, R.id.button75, R.id.button76, R.id.button77, R.id.button78, R.id.button79, R.id.button80, R.id.button81, R.id.button82, R.id.button83, R.id.button84, R.id.button85, R.id.button86, R.id.button87, R.id.button88, R.id.button89, R.id.button90, R.id.button91, R.id.button92, R.id.button93, R.id.button94, R.id.button95, R.id.button96, R.id.button97, R.id.button98, R.id.button99, R.id.button100};
    Drawable foggy;
    Drawable normal;
    Drawable miss;
    Button buttons[]=new Button[100];
    int do_namalowania[] = new int[101];
    String strzaly;
    Boolean czyPrzegralem;
    int wspolrzedne[];


    Thread t1=new Thread(){
        @Override
        public void run() {
            synchronized (this) {
                try {
                    wait(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (int l = 0; l < wspolrzedne.length / 2; l++) {
                    final int omg=l;
                    EnemyTurn.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int number=wspolrzedne[2*omg]+wspolrzedne[2*omg+1]*10;
                            if (buttons[number].getBackground().equals(normal))
                                buttons[number].setAlpha(0.15f);
                            else
                                buttons[number].setBackgroundColor(0xff261800);
                        }
                    });

                    try {
                        wait(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            EnemyTurn.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (czyPrzegralem){
                        przegralem();
                    }else {
                        setResult(RESULT_OK);
                        finish();
                    }
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enemy_turn);
        View mContentView = findViewById(R.id.frame);
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        foggy = getResources().getDrawable(R.drawable.fog);
        int i=0;
        for (int id:idArray) {
            buttons[i] = findViewById(id);
            buttons[i].setBackground(foggy);
            buttons[i].setAlpha(0.7f);
            i++;
        }
        normal = buttons[1].getBackground();
        Bundle mapa = getIntent().getExtras();
        for(int j=0; j<100; j++){
            do_namalowania[j]=mapa.getInt(Integer.toString(j));
        }
        strzaly=mapa.getString("nowy");
        wspolrzedne = new int[strzaly.length()];
        for (int cyfra=0; cyfra<strzaly.length(); cyfra++){
            wspolrzedne[cyfra]=strzaly.charAt(cyfra)-'0';
        }
        czyPrzegralem=mapa.getBoolean("czyPrzegralem");
        Button dummy = new Button(this);
        dummy.setBackground(normal);
        dummy.setAlpha(0.15f);
        miss=dummy.getBackground();

        for(int k=0; k<100; k++)
            malarz(k);

        for(int coordinates=0; coordinates<wspolrzedne.length/2; coordinates++){
            if(buttons[wspolrzedne[2*coordinates]+wspolrzedne[2*coordinates+1]*10].getBackground().equals(miss))
                buttons[wspolrzedne[2*coordinates]+wspolrzedne[2*coordinates+1]*10].setAlpha(0.7f);
            else
                buttons[wspolrzedne[2*coordinates]+wspolrzedne[2*coordinates+1]*10].setBackgroundColor(0xffff9933);
        }
        t1.start();
    }
    private void malarz(int k){
        if(do_namalowania[k]==0){}
        else if(do_namalowania[k]==1)
            buttons[k].setAlpha(0.15f);
        else if(do_namalowania[k]==2)
            buttons[k].setBackgroundColor(0xffff9933);
        else
            buttons[k].setBackgroundColor(0xff261800);
    }

    private void przegralem(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Porażka!");
        builder.setPositiveButton("Koniec", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    @Override
    public void onBackPressed() {
    }
}