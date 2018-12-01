package com.example.bmairtoek.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;


public class FullscreenActivity extends AppCompatActivity {
    private int saved_pick=100;
    private Button pick;
    private ImageButton Surrender;
    private Button buttons[]=new Button[100];
    private Drawable foggy;
    private Computer komputer;
    private int uklad_statkow[] = new int[20];
    private ArrayList<Integer> do_zamalownia;
    private Boolean lvl;

    private static final int[] idArray={R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9, R.id.button10, R.id.button11, R.id.button12, R.id.button13, R.id.button14, R.id.button15, R.id.button16, R.id.button17, R.id.button18, R.id.button19, R.id.button20, R.id.button21, R.id.button22, R.id.button23, R.id.button24, R.id.button25, R.id.button26, R.id.button27, R.id.button28, R.id.button29, R.id.button30, R.id.button31, R.id.button32, R.id.button33, R.id.button34, R.id.button35, R.id.button36, R.id.button37, R.id.button38, R.id.button39, R.id.button40, R.id.button41, R.id.button42, R.id.button43, R.id.button44, R.id.button45, R.id.button46, R.id.button47, R.id.button48, R.id.button49, R.id.button50, R.id.button51, R.id.button52, R.id.button53, R.id.button54, R.id.button55, R.id.button56, R.id.button57, R.id.button58, R.id.button59, R.id.button60, R.id.button61, R.id.button62, R.id.button63, R.id.button64, R.id.button65, R.id.button66, R.id.button67, R.id.button68, R.id.button69, R.id.button70, R.id.button71, R.id.button72, R.id.button73, R.id.button74, R.id.button75, R.id.button76, R.id.button77, R.id.button78, R.id.button79, R.id.button80, R.id.button81, R.id.button82, R.id.button83, R.id.button84, R.id.button85, R.id.button86, R.id.button87, R.id.button88, R.id.button89, R.id.button90, R.id.button91, R.id.button92, R.id.button93, R.id.button94, R.id.button95, R.id.button96, R.id.button97, R.id.button98, R.id.button99, R.id.button100};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        View mContentView = findViewById(R.id.frame);
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        Intent ten = getIntent();
        lvl = ten.getBooleanExtra("lvl", true);
        foggy = getResources().getDrawable(R.drawable.fog);
        int i=0;
        for (int id:idArray) {
            buttons[i] = findViewById(id);
            final int n=i;
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ruch(n);
                }
            });
            buttons[i].setBackground(foggy);
            buttons[i].setAlpha(0.7f);
            i++;
        }
        pick=findViewById(R.id.ok);
        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String odpowiedz[];
                if(saved_pick!=100){
                    int mapa_gracza[][] = komputer.podaj_mape();
                    odpowiedz=komputer.informuj(saved_pick);
                    if(odpowiedz[2].equals("zatopiony")){   //[0]=wartość pod podanym polem, [1]=strzał komputera, [2]=czy zatopiony
                        do_zamalownia=komputer.co_zamalowac();
                        int x;
                        int y;
                        do {
                            x=do_zamalownia.remove(0);
                            y=do_zamalownia.remove(0);
                            maluj_dookola(x+y*10);
                        } while (!do_zamalownia.isEmpty());
                        if(komputer.czyWygralem()){
                            koniec_gry();
                        }
                    }
                    if (odpowiedz[0].equals("1"))
                        buttons[saved_pick].setAlpha(0.15f);
                    else
                        buttons[saved_pick].setBackgroundColor(0xff3b1c00);
                    saved_pick = 100;
                    if (!odpowiedz[1].equals("trafiony")) {
                        Intent intent = new Intent(FullscreenActivity.this, EnemyTurn.class);
                        Bundle mapa = new Bundle();
                        for (int k = 0; k < 10; k++) {
                            for (int l = 0; l < 10; l++)
                                mapa.putInt(Integer.toString(k * 10 + l), mapa_gracza[l][k]);
                        }
                        mapa.putString("nowy", odpowiedz[1]);
                        mapa.putBoolean("czyPrzegralem", komputer.czyPrzegralem());
                        intent.putExtras(mapa);
                        startActivityForResult(intent, 11);
                    }
                } else
                    Toast.makeText(FullscreenActivity.this, "Wybierz pole!", Toast.LENGTH_LONG).show();
            }
        });
        Surrender=findViewById(R.id.surrender);
        Surrender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        pick.setEnabled(false);
        Intent intent = new Intent(FullscreenActivity.this, StartingActivity.class);
        startActivityForResult(intent, 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case(10): {
                if (resultCode == RESULT_OK && data != null) {
                    uklad_statkow[0] = data.getIntExtra("i1", 0);
                    uklad_statkow[1] = data.getIntExtra("i2", 0);
                    uklad_statkow[2] = data.getIntExtra("i3", 0);
                    uklad_statkow[3] = data.getIntExtra("i4", 0);
                    uklad_statkow[4] = data.getIntExtra("i5", 0);
                    uklad_statkow[5] = data.getIntExtra("i6", 0);
                    uklad_statkow[6] = data.getIntExtra("i7", 0);
                    uklad_statkow[7] = data.getIntExtra("i8", 0);
                    uklad_statkow[8] = data.getIntExtra("i9", 0);
                    uklad_statkow[9] = data.getIntExtra("i10", 0);
                    uklad_statkow[10] = data.getIntExtra("i11", 0);
                    uklad_statkow[11] = data.getIntExtra("i12", 0);
                    uklad_statkow[12] = data.getIntExtra("i13", 0);
                    uklad_statkow[13] = data.getIntExtra("i14", 0);
                    uklad_statkow[14] = data.getIntExtra("i15", 0);
                    uklad_statkow[15] = data.getIntExtra("i16", 0);
                    uklad_statkow[16] = data.getIntExtra("i17", 0);
                    uklad_statkow[17] = data.getIntExtra("i18", 0);
                    uklad_statkow[18] = data.getIntExtra("i19", 0);
                    uklad_statkow[19] = data.getIntExtra("i20", 0);
                    komputer = new Computer(uklad_statkow,lvl);
                    new Thread(komputer).start();
                    pick.setEnabled(true);
                } else
                    finish();
            }break;
            case(11):{
                if (resultCode==RESULT_CANCELED)
                    finish();
            }
        }
    }

    private void ruch(int i){
        float alpha = 0.4f;
        if(i==saved_pick&&buttons[i].getAlpha()==alpha) {
            buttons[i].setAlpha(0.7f);
            saved_pick=100;
        } else if(buttons[i].getAlpha()==0.7f) {
            buttons[i].setAlpha(alpha);
            if(saved_pick!=100)
                buttons[saved_pick].setAlpha(0.7f);
            saved_pick = i;
        }
    }

    private void maluj_dookola(int przycisk){
        int x = przycisk%10;
        int y = (przycisk-przycisk%10)/10;
        buttons[przycisk].setAlpha(0.15f);
        if(x!=0){
            buttons[przycisk-1].setAlpha(0.15f);
            if(y!=0)
                buttons[przycisk-11].setAlpha(0.15f);
            if(y!=9)
                buttons[przycisk+9].setAlpha(0.15f);
        }
        if(x!=9) {
            buttons[przycisk+1].setAlpha(0.15f);
            if(y!=0)
                buttons[przycisk-9].setAlpha(0.15f);
            if(y!=9)
                buttons[przycisk+11].setAlpha(0.15f);
        }
        if(y!=0)
            buttons[przycisk-10].setAlpha(0.15f);
        if(y!=9)
            buttons[przycisk+10].setAlpha(0.15f);
    }

    private void koniec_gry() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Wygrana!");

        // add a button
        builder.setPositiveButton("Koniec", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean exit = false;
    @Override
    public void onBackPressed() {
        if (exit) {
            finish();
        } else {
            Toast.makeText(this, "Naciśnij ponownie żeby wyjść",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new CountDownTimer(3000,1000) {

                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    exit = false;
                }
            }.start();
        }

    }
}
