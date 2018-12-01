package com.example.bmairtoek.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Multi_Grid extends AppCompatActivity {
    private static final int[] idArray={R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9, R.id.button10, R.id.button11, R.id.button12, R.id.button13, R.id.button14, R.id.button15, R.id.button16, R.id.button17, R.id.button18, R.id.button19, R.id.button20, R.id.button21, R.id.button22, R.id.button23, R.id.button24, R.id.button25, R.id.button26, R.id.button27, R.id.button28, R.id.button29, R.id.button30, R.id.button31, R.id.button32, R.id.button33, R.id.button34, R.id.button35, R.id.button36, R.id.button37, R.id.button38, R.id.button39, R.id.button40, R.id.button41, R.id.button42, R.id.button43, R.id.button44, R.id.button45, R.id.button46, R.id.button47, R.id.button48, R.id.button49, R.id.button50, R.id.button51, R.id.button52, R.id.button53, R.id.button54, R.id.button55, R.id.button56, R.id.button57, R.id.button58, R.id.button59, R.id.button60, R.id.button61, R.id.button62, R.id.button63, R.id.button64, R.id.button65, R.id.button66, R.id.button67, R.id.button68, R.id.button69, R.id.button70, R.id.button71, R.id.button72, R.id.button73, R.id.button74, R.id.button75, R.id.button76, R.id.button77, R.id.button78, R.id.button79, R.id.button80, R.id.button81, R.id.button82, R.id.button83, R.id.button84, R.id.button85, R.id.button86, R.id.button87, R.id.button88, R.id.button89, R.id.button90, R.id.button91, R.id.button92, R.id.button93, R.id.button94, R.id.button95, R.id.button96, R.id.button97, R.id.button98, R.id.button99, R.id.button100};
    private Drawable normal;
    private int statek;
    private int zatopiony;
    private Button buttons[]=new Button[100];
    private Button zatwierdz;
    private TextView tura;
    private ConnectedThread transfer;
    private Boolean kto;
    private int[] moja_mapa = new int[100];
    private int[] obca_mapa = new int[100];
    private int saved_pick=100;
    private ArrayList<Integer> do_zamalowania_mojej = new ArrayList<>();
    private ArrayList<Integer> do_zamalowania_obcej = new ArrayList<>();

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)||BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                errorBox();
            }
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)){
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR);
                if (state == BluetoothAdapter.STATE_TURNING_OFF || state == BluetoothAdapter.STATE_OFF){
                    errorBox();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi__grid);
        View mContentView = findViewById(R.id.frame);
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        kto = getIntent().getBooleanExtra("Kto", true);
        tura=findViewById(R.id.Tura_przeciwnika);
        zatwierdz=findViewById(R.id.ok2);
        zatwierdz.setEnabled(false);
        zatwierdz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(saved_pick!=100){
                    if (obca_mapa[saved_pick]==0) {
                        buttons[saved_pick].setAlpha(0.15f);
                        obca_mapa[saved_pick]=1;
                    } else {
                        buttons[saved_pick].setBackgroundColor(0xff3b1c00);
                        buttons[saved_pick].setAlpha(0.7f);
                        obca_mapa[saved_pick]=3;
                    }
                    int tmp = obca_mapa[saved_pick];
                    do_zamalowania_obcej.clear();
                    if(zatop(false, saved_pick))
                        wykonaj(false);
                    transfer.write(saved_pick);
                    if(tmp==1) {
                        saved_pick = 100;
                        zatwierdz.setEnabled(false);
                        new CountDownTimer(1000, 1000) {

                            @Override
                            public void onTick(long l) {

                            }

                            @Override
                            public void onFinish() {
                                zagraj();
                            }
                        }.start();
                    }
                } else
                    Toast.makeText(Multi_Grid.this, "Wybierz pole!", Toast.LENGTH_LONG).show();
            }
        });

        Drawable foggy = getResources().getDrawable(R.drawable.fog);
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
        normal = buttons[1].getBackground();

        statek = (0xffff9933);
        zatopiony = (0xff3b1c00);

        transfer = new ConnectedThread(SocketHandler.getSocket());
        transfer.start();

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        this.registerReceiver(mReceiver, filter);

        Intent intent = new Intent(Multi_Grid.this, StartingActivity.class);
        startActivityForResult(intent, 10);
    }

    private void zagraj(){
        for(Button button:buttons){
            button.setClickable(false);
        }
        zatwierdz.setVisibility(View.INVISIBLE);
        tura.setVisibility(View.VISIBLE);
        for(int j=0; j<100; j++){
            maluj_moje(j);
        }
        Thread reader = new Thread(){
            @Override
            public void run() {
                boolean czyPudlo=false;
                do {
                    final int strzal = transfer.read();
                    if(strzal==-1){
                        Multi_Grid.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                errorBox();
                            }
                        });
                        break;
                    }else if (moja_mapa[strzal] == 0)
                        czyPudlo = true;
                    final boolean pudlo=czyPudlo;
                    Multi_Grid.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            kontynuuj(strzal, pudlo);
                        }
                    });
                } while(!czyPudlo);
            }
        };
        reader.start();
    }

    private void ruch(int i){
        float alpha = 0.4f;
        if(i==saved_pick&&buttons[i].getAlpha()==alpha) {
            buttons[i].setAlpha(0.7f);
            saved_pick=100;
        } else if(buttons[i].getBackground().equals(normal)&&buttons[i].getAlpha()==0.7f) {
            buttons[i].setAlpha(alpha);
            if(saved_pick!=100)
                buttons[saved_pick].setAlpha(0.7f);
            saved_pick = i;
        }
    }

    private void kontynuuj(int strzal, boolean czyPudlo){
        if(strzal==-1){

        } else if(moja_mapa[strzal]==0){
            moja_mapa[strzal]=1;
            buttons[strzal].setBackground(normal);
            buttons[strzal].setAlpha(0.15f);
        } else {
            moja_mapa[strzal]=3;
            buttons[strzal].setBackgroundColor(zatopiony);
            do_zamalowania_mojej.clear();
            if(zatop(true, strzal))
                wykonaj(true);
        }
        if(czyPudlo) {
            new CountDownTimer(1000, 1000) {

                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    for (Button button : buttons) {
                        button.setClickable(true);
                    }
                    zatwierdz.setEnabled(true);
                    zatwierdz.setVisibility(View.VISIBLE);
                    tura.setVisibility(View.INVISIBLE);
                    for (int k = 0; k < 100; k++) {
                        maluj_wroga(k);
                    }
                }
            }.start();
        }
    }

    private void maluj_moje(int pozycja){
        if(moja_mapa[pozycja]==0){
            buttons[pozycja].setBackground(normal);
            buttons[pozycja].setAlpha(0.7f);
        } else if(moja_mapa[pozycja]==1){
            buttons[pozycja].setBackground(normal);
            buttons[pozycja].setAlpha(0.15f);
        } else if(moja_mapa[pozycja]==2){
            buttons[pozycja].setBackgroundColor(statek);
            buttons[pozycja].setAlpha(0.7f);
        } else {
            buttons[pozycja].setBackgroundColor(zatopiony);
            buttons[pozycja].setAlpha(0.7f);
        }
    }

    private void maluj_wroga(int pozycja){
        if(obca_mapa[pozycja]==0){
            buttons[pozycja].setBackground(normal);
            buttons[pozycja].setAlpha(0.7f);
        } else if(obca_mapa[pozycja]==1){
            buttons[pozycja].setBackground(normal);
            buttons[pozycja].setAlpha(0.15f);
        } else if(obca_mapa[pozycja]==2){
            buttons[pozycja].setBackground(normal);
            buttons[pozycja].setAlpha(0.7f);
        } else {
            buttons[pozycja].setBackgroundColor(zatopiony);
            buttons[pozycja].setAlpha(0.7f);
        }
    }

    private boolean zatop(boolean mapa, int strzal){
        return zatop(mapa, strzal%10, (strzal-strzal%10)/10, 0);
    }

    private boolean zatop(boolean mapa, int x, int y, int kierunek){
        int strzal=10*y+x;
        if(mapa) {                                                           //kierunek: <-, gora, ->, dol, 0-poczatek
            if (moja_mapa[strzal] == 3) {
                if (x != 0 && (kierunek==1 || kierunek==0) && !zatop(true, x - 1, y, 1))
                    return false;
                if (x != 9 && (kierunek==3 || kierunek==0) && !zatop(true, x + 1, y, 3))
                    return false;
                if (y != 0 && (kierunek==2 || kierunek==0) &&  !zatop(true, x, y - 1,2))
                    return false;
                if (y != 9 && (kierunek==4 || kierunek==0) &&  !zatop(true, x, y + 1, 4))
                    return false;
                do_zamalowania_mojej.add(x);
                do_zamalowania_mojej.add(y);
            }
            return !(moja_mapa[strzal] == 2);
        }else{
            if (obca_mapa[strzal] == 3) {
                if (x != 0 && kierunek!=3 && !zatop(false, x - 1, y, 1))
                    return false;
                if (x != 9 && kierunek!=1 && !zatop(false, x + 1, y, 3))
                    return false;
                if (y != 0 && kierunek!=4 &&  !zatop(false, x, y - 1,2))
                    return false;
                if (y != 9 && kierunek!=2 &&  !zatop(false, x, y + 1, 4))
                    return false;
                do_zamalowania_obcej.add(x);
                do_zamalowania_obcej.add(y);
            }
            return !(obca_mapa[strzal] == 2);
        }
    }

    private void wykonaj(boolean kto){
        if(kto){
            while(!do_zamalowania_mojej.isEmpty()) {
                int x = do_zamalowania_mojej.remove(0);
                int y = do_zamalowania_mojej.remove(0);
                int przycisk = 10*y+x;
                if (x != 0) {
                    if(moja_mapa[przycisk-1]==0) {
                        buttons[przycisk - 1].setAlpha(0.15f);
                        moja_mapa[przycisk - 1] = 1;
                    }
                    if (y != 0) {
                        if(moja_mapa[przycisk-11]==0) {
                            buttons[przycisk - 11].setAlpha(0.15f);
                            moja_mapa[przycisk - 11] = 1;
                        }
                    }
                    if (y != 9) {
                        if(moja_mapa[przycisk+9]==0) {
                            buttons[przycisk + 9].setAlpha(0.15f);
                            moja_mapa[przycisk + 9] = 1;
                        }
                    }
                }
                if (x != 9) {
                    if(moja_mapa[przycisk+1]==0) {
                        buttons[przycisk + 1].setAlpha(0.15f);
                        moja_mapa[przycisk + 1] = 1;
                    }
                    if (y != 0) {
                        if(moja_mapa[przycisk-9]==0) {
                            buttons[przycisk - 9].setAlpha(0.15f);
                            moja_mapa[przycisk - 9] = 1;
                        }
                    }
                    if (y != 9) {
                        if(moja_mapa[przycisk+11]==0) {
                            buttons[przycisk + 11].setAlpha(0.15f);
                            moja_mapa[przycisk + 11] = 1;
                        }
                    }
                }
                if (y != 0) {
                    if(moja_mapa[przycisk-10]==0) {
                        buttons[przycisk - 10].setAlpha(0.15f);
                        moja_mapa[przycisk - 10] = 1;
                    }
                }
                if (y != 9) {
                    if(moja_mapa[przycisk+10]==0) {
                        buttons[przycisk + 10].setAlpha(0.15f);
                        moja_mapa[przycisk + 10] = 1;
                    }
                }
            }
            czyPrzegralem();
        } else {
            while(!do_zamalowania_obcej.isEmpty()) {
                int x = do_zamalowania_obcej.remove(0);
                int y = do_zamalowania_obcej.remove(0);
                int przycisk = 10*y+x;
                if (x != 0) {
                    if(obca_mapa[przycisk-1]==0) {
                        buttons[przycisk - 1].setAlpha(0.15f);
                        obca_mapa[przycisk - 1] = 1;
                    }
                    if (y != 0) {
                        if(obca_mapa[przycisk-11]==0) {
                            buttons[przycisk - 11].setAlpha(0.15f);
                            obca_mapa[przycisk - 11] = 1;
                        }
                    }
                    if (y != 9) {
                        if(obca_mapa[przycisk+9]==0) {
                            buttons[przycisk + 9].setAlpha(0.15f);
                            obca_mapa[przycisk + 9] = 1;
                        }
                    }
                }
                if (x != 9) {
                    if(obca_mapa[przycisk+1]==0) {
                        buttons[przycisk + 1].setAlpha(0.15f);
                        obca_mapa[przycisk + 1] = 1;
                    }
                    if (y != 0) {
                        if(obca_mapa[przycisk-9]==0) {
                            buttons[przycisk - 9].setAlpha(0.15f);
                            obca_mapa[przycisk - 9] = 1;
                        }
                    }
                    if (y != 9) {
                        if(obca_mapa[przycisk+11]==0) {
                            buttons[przycisk + 11].setAlpha(0.15f);
                            obca_mapa[przycisk + 11] = 1;
                        }
                    }
                }
                if (y != 0) {
                    if(obca_mapa[przycisk-10]==0) {
                        buttons[przycisk - 10].setAlpha(0.15f);
                        obca_mapa[przycisk - 10] = 1;
                    }
                }
                if (y != 9) {
                    if(obca_mapa[przycisk+10]==0) {
                        buttons[przycisk + 10].setAlpha(0.15f);
                        obca_mapa[przycisk + 10] = 1;
                    }
                }
            }
            czyWygralem();
        }
    }

    public void czyWygralem(){
        int l1=0;
        for(int i=0; i<100; i++) {
            if (obca_mapa[i] == 2)
                l1++;
        }
        if(l1==0){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Wygrana!");
            builder.setCancelable(false);
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
    }

    public void czyPrzegralem(){
        int l1=0;
        for(int i=0; i<100; i++) {
            if (moja_mapa[i] == 2)
                l1++;
        }
        if(l1==0){
            transfer.cancel();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Porażka!");
            builder.setCancelable(false);
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            moja_mapa[data.getIntExtra("i1", 0)] = 2;
            moja_mapa[data.getIntExtra("i2", 0)] = 2;
            moja_mapa[data.getIntExtra("i3", 0)] = 2;
            moja_mapa[data.getIntExtra("i4", 0)] = 2;
            moja_mapa[data.getIntExtra("i5", 0)] = 2;
            moja_mapa[data.getIntExtra("i6", 0)] = 2;
            moja_mapa[data.getIntExtra("i7", 0)] = 2;
            moja_mapa[data.getIntExtra("i8", 0)] = 2;
            moja_mapa[data.getIntExtra("i9", 0)] = 2;
            moja_mapa[data.getIntExtra("i10", 0)] = 2;
            moja_mapa[data.getIntExtra("i11", 0)] = 2;
            moja_mapa[data.getIntExtra("i12", 0)] = 2;
            moja_mapa[data.getIntExtra("i13", 0)] = 2;
            moja_mapa[data.getIntExtra("i14", 0)] = 2;
            moja_mapa[data.getIntExtra("i15", 0)] = 2;
            moja_mapa[data.getIntExtra("i16", 0)] = 2;
            moja_mapa[data.getIntExtra("i17", 0)] = 2;
            moja_mapa[data.getIntExtra("i18", 0)] = 2;
            moja_mapa[data.getIntExtra("i19", 0)] = 2;
            moja_mapa[data.getIntExtra("i20", 0)] = 2;

            transfer.write(data.getIntExtra("i1", 0));
            transfer.write(data.getIntExtra("i2", 0));
            transfer.write(data.getIntExtra("i3", 0));
            transfer.write(data.getIntExtra("i4", 0));
            transfer.write(data.getIntExtra("i5", 0));
            transfer.write(data.getIntExtra("i6", 0));
            transfer.write(data.getIntExtra("i7", 0));
            transfer.write(data.getIntExtra("i8", 0));
            transfer.write(data.getIntExtra("i9", 0));
            transfer.write(data.getIntExtra("i10", 0));
            transfer.write(data.getIntExtra("i11", 0));
            transfer.write(data.getIntExtra("i12", 0));
            transfer.write(data.getIntExtra("i13", 0));
            transfer.write(data.getIntExtra("i14", 0));
            transfer.write(data.getIntExtra("i15", 0));
            transfer.write(data.getIntExtra("i16", 0));
            transfer.write(data.getIntExtra("i17", 0));
            transfer.write(data.getIntExtra("i18", 0));
            transfer.write(data.getIntExtra("i19", 0));
            transfer.write(data.getIntExtra("i20", 0));

            Thread starter = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        if (transfer.avalaible() != 0)
                            break;
                    }
                    int odebrane;
                    for (int i = 0; i < 20; i++) {
                        odebrane = transfer.read();
                        if(odebrane == -1 || odebrane == 255)
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    errorBox();
                                }
                            });
                        else
                            obca_mapa[odebrane] = 2;
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            zatwierdz.setEnabled(true);
                        }
                    });

                    if (!kto) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < 100; i++)
                                    maluj_moje(i);
                            }
                        });
                        boolean czyPudlo = false;
                        do {
                            final int strzal = transfer.read();
                            if (strzal == -1)
                                break;
                            else if (moja_mapa[strzal] == 0)
                                czyPudlo = true;
                            final boolean pudlo = czyPudlo;
                            Multi_Grid.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    kontynuuj(strzal, pudlo);
                                }
                            });
                        } while (!czyPudlo);
                    }
                }
            });
            starter.start();

            if (kto) {
                tura.setVisibility(View.INVISIBLE);
            } else {
                zatwierdz.setVisibility(View.INVISIBLE);
            }
        } else {
            transfer.write(255);
            finish();
        }
    }

    private void errorBox(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Wygrana?");
        builder.setMessage("Utracono połączenie!");
        builder.setCancelable(false);
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
        transfer.cancel();
    }

    private boolean exit = false;
    @Override
    public void onBackPressed() {
        if (exit) {
            finish();
        } else {
            Toast.makeText(this, "Naciśnij ponownie żeby wyjść", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        transfer.cancel();
        unregisterReceiver(mReceiver);
        SocketHandler.setSocket(null);
    }
}