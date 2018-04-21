package com.example.bmairtoek.myapplication;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class StartingActivity extends AppCompatActivity {
    Button pick;
    Button buttons[]=new Button[100];
    Drawable foggy;
    Drawable normal;
    int pozycje[] = new int[20];

    private static final int[] idArray={R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9, R.id.button10, R.id.button11, R.id.button12, R.id.button13, R.id.button14, R.id.button15, R.id.button16, R.id.button17, R.id.button18, R.id.button19, R.id.button20, R.id.button21, R.id.button22, R.id.button23, R.id.button24, R.id.button25, R.id.button26, R.id.button27, R.id.button28, R.id.button29, R.id.button30, R.id.button31, R.id.button32, R.id.button33, R.id.button34, R.id.button35, R.id.button36, R.id.button37, R.id.button38, R.id.button39, R.id.button40, R.id.button41, R.id.button42, R.id.button43, R.id.button44, R.id.button45, R.id.button46, R.id.button47, R.id.button48, R.id.button49, R.id.button50, R.id.button51, R.id.button52, R.id.button53, R.id.button54, R.id.button55, R.id.button56, R.id.button57, R.id.button58, R.id.button59, R.id.button60, R.id.button61, R.id.button62, R.id.button63, R.id.button64, R.id.button65, R.id.button66, R.id.button67, R.id.button68, R.id.button69, R.id.button70, R.id.button71, R.id.button72, R.id.button73, R.id.button74, R.id.button75, R.id.button76, R.id.button77, R.id.button78, R.id.button79, R.id.button80, R.id.button81, R.id.button82, R.id.button83, R.id.button84, R.id.button85, R.id.button86, R.id.button87, R.id.button88, R.id.button89, R.id.button90, R.id.button91, R.id.button92, R.id.button93, R.id.button94, R.id.button95, R.id.button96, R.id.button97, R.id.button98, R.id.button99, R.id.button100};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statring);
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
            final int n=i;
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pick(n);
                }
            });
            buttons[i].setBackground(foggy);
            buttons[i].setAlpha(0.7f);
            i++;
        }

        Button test=findViewById(R.id.test);    //funkcja do szybkiego testowania działania apki, wpisuje ustaloną konigurację okrętów
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pick(0);
                pick(1);
                pick(2);
                pick(3);
                pick(5);
                pick(6);
                pick(7);
                pick(20);
                pick(21);
                pick(22);
                pick(24);
                pick(25);
                pick(27);
                pick(28);
                pick(41);
                pick(43);
                pick(45);
                pick(47);
                pick(61);
                pick(62);
            }
        });

        pick=findViewById(R.id.ok);
        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checker()) {
                    int j=0;
                    for(int i=0;i<100;i++){
                        if(!buttons[i].getBackground().equals(normal)) {
                            pozycje[j] = i;
                            j++;
                        }
                    }
                    Intent output = new Intent();
                    output.putExtra("i1", pozycje[0]);
                    output.putExtra("i2", pozycje[1]);
                    output.putExtra("i3", pozycje[2]);
                    output.putExtra("i4", pozycje[3]);
                    output.putExtra("i5", pozycje[4]);
                    output.putExtra("i6", pozycje[5]);
                    output.putExtra("i7", pozycje[6]);
                    output.putExtra("i8", pozycje[7]);
                    output.putExtra("i9", pozycje[8]);
                    output.putExtra("i10", pozycje[9]);
                    output.putExtra("i11", pozycje[10]);
                    output.putExtra("i12", pozycje[11]);
                    output.putExtra("i13", pozycje[12]);
                    output.putExtra("i14", pozycje[13]);
                    output.putExtra("i15", pozycje[14]);
                    output.putExtra("i16", pozycje[15]);
                    output.putExtra("i17", pozycje[16]);
                    output.putExtra("i18", pozycje[17]);
                    output.putExtra("i19", pozycje[18]);
                    output.putExtra("i20", pozycje[19]);
                    setResult(RESULT_OK, output);
                    finish();
                }
                else
                    error_box();
            }
        });
        normal=buttons[1].getBackground();
    }

    private void pick(int i){
        if(buttons[i].getBackground() == normal) {
            buttons[i].setBackgroundColor(0xFF9b1600);
        }
        else {
            buttons[i].setBackground(normal);
        }
    }

    private boolean checker(){
        int jedynki=0;
        int krance=0;
        int srodki=0;
        int trojki=0;
        int kadluby=0;
        int bledy=0;
        for(int i=0;i<100;i++){
            if(!buttons[i].getBackground().equals(normal)){
                kadluby+=1;
                if(sasiedzi(i)==0)
                    jedynki+=1;
                else if(sasiedzi(i)==1)
                    krance+=1;
                else if(sasiedzi(i)==2) {
                    if(((i%10==0||buttons[i-1].getBackground().equals(normal))&&(i%10==9||buttons[i+1].getBackground().equals(normal))&&sasiedzi(i-10)==1&&sasiedzi(i+10)==1)||((i<10||buttons[i-10].getBackground().equals(normal))&&(i>89||buttons[i+10].getBackground().equals(normal))&&sasiedzi(i-1)==1&&sasiedzi(i+1)==1))
                        trojki+=1;
                    srodki += 1;
                }else
                    bledy+=1;
            }
        }
        return (jedynki==4&&krance==12&&srodki==4&&trojki==2&&kadluby==20&&bledy==0);

    }

    private int sasiedzi(int i){
        int licznik=0;
        if((i-11>=0&&i-11<=99&&i%10!=0)&&!buttons[i-11].getBackground().equals(normal))
            licznik+=11;
        if((i-10>=0&&i-10<=99)&&!buttons[i-10].getBackground().equals(normal))
            licznik+=1;
        if((i-9>=0&&i-9<=99&&i%10!=9)&&!buttons[i-9].getBackground().equals(normal))
            licznik+=11;
        if((i-1>=0&&i-1<=99&&i%10!=0)&&!buttons[i-1].getBackground().equals(normal))
            licznik+=1;
        if((i+1>=0&&i+1<=99&&i%10!=9)&&!buttons[i+1].getBackground().equals(normal))
            licznik+=1;
        if((i+9>=0&&i+9<=99&&i%10!=0)&&!buttons[i+9].getBackground().equals(normal))
            licznik+=11;
        if((i+10>=0&&i+10<=99)&&!buttons[i+10].getBackground().equals(normal))
            licznik+=1;
        if((i+11>=0&&i+11<=99&&i%10!=9)&&!buttons[i+11].getBackground().equals(normal))
            licznik+=11;
        return licznik;
    }

    private void error_box(){
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Zły układ statków");
        builder.setMessage("Twoja flota powinna składać się z:\n4x Jednomasztowców\n3x Dwómasztowców\n2x Trójmasztowców\n1x Czteromasztowca\nPonad to statki nie mogą się stykać, ani zakręcać!");

        // add a button
        builder.setPositiveButton("OK", null);

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
