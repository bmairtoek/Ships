package com.example.bmairtoek.myapplication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Computer implements Runnable{
    //0-mgła, 1-pudło, 2-statek, 3-zatopiony statek
    private int playerMap[][] = new int[10][10];
    private int compMap[][] = new int[10][10];
    private int do_wysylki[][];
    private ArrayList<Integer> list = new ArrayList<>(100);
    private ArrayList<Integer> do_zamalowania_gracza = new ArrayList<>();  //tak na prawdę to podaję tutaj trafione statki wokół których maluję
    private ArrayList<Integer> do_zamalowania_komputera = new ArrayList<>();
    private int last_hit[] = new int[2];
    private boolean lvl;

    public Computer(int dane[], boolean level){
        for(int i=0; i<10; i++){
            for(int j=0; j<10; j++){
                playerMap[i][j]=0;
            }
        }
        for(int dana:dane)
            playerMap[dana%10][(dana-dana%10)/10]=2;
        make_compMap();
        last_hit[0]=10;
        last_hit[1]=10;
        lvl=level;
        do_wysylki=playerMap.clone();
    }

    private void make_compMap() {
        Random r = new Random();
        int rotation;
        int positionx;
        int positiony;
        int ilosc;

        for(int i = 0; i < 100; i++) {
            list.add(i);
        }

        boolean ocupied_space = true;
        rotation = r.nextInt(2);
        if (rotation == 0) {    //losowanie czteromasztowca
            positionx = r.nextInt(7);
            positiony = r.nextInt(10);
            compMap[positionx][positiony] = 2;
            compMap[positionx + 1][positiony] = 2;
            compMap[positionx + 2][positiony] = 2;
            compMap[positionx + 3][positiony] = 2;
        } else {
            positionx = r.nextInt(10);
            positiony = r.nextInt(7);
            compMap[positionx][positiony] = 2;
            compMap[positionx][positiony + 1] = 2;
            compMap[positionx][positiony + 2] = 2;
            compMap[positionx][positiony + 3] = 2;
        }
        for (ilosc = 0; ilosc < 2; ilosc++) {   //losowanie trójmasztowców
            rotation = r.nextInt(2);
            if (rotation == 0) {
                while (ocupied_space) {
                    positionx = r.nextInt(8);
                    positiony = r.nextInt(10);
                    if (czy_wolne(positionx, positiony) && czy_wolne(positionx + 2, positiony))
                        ocupied_space = false;
                }
                ocupied_space = true;
                compMap[positionx][positiony] = 2;
                compMap[positionx + 1][positiony] = 2;
                compMap[positionx + 2][positiony] = 2;
            } else{
                while (ocupied_space) {
                    positionx = r.nextInt(10);
                    positiony = r.nextInt(8);
                    if (czy_wolne(positionx, positiony) && czy_wolne(positionx, positiony + 2))
                        ocupied_space = false;
                }
                ocupied_space = true;
                compMap[positionx][positiony] = 2;
                compMap[positionx][positiony + 1] = 2;
                compMap[positionx][positiony + 2] = 2;
            }
        }
        for (ilosc = 0; ilosc < 3; ilosc++) {   //losowanie dwómasztowców
            rotation = r.nextInt(2);
            if (rotation == 0) {
                while (ocupied_space) {
                    positionx = r.nextInt(9);
                    positiony = r.nextInt(10);
                    if (czy_wolne(positionx, positiony) && czy_wolne(positionx + 1, positiony))
                        ocupied_space = false;
                }
                ocupied_space = true;
                compMap[positionx][positiony] = 2;
                compMap[positionx + 1][positiony] = 2;
            } else{
                while (ocupied_space) {
                    positionx = r.nextInt(10);
                    positiony = r.nextInt(9);
                    if (czy_wolne(positionx, positiony) && czy_wolne(positionx, positiony + 1))
                        ocupied_space = false;
                }
                ocupied_space = true;
                compMap[positionx][positiony] = 2;
                compMap[positionx][positiony + 1] = 2;
            }
        }
        for (ilosc = 0; ilosc < 4; ilosc++) {   //losowanie dwómasztowców
            rotation = r.nextInt(2);
            if (rotation == 0) {
                while (ocupied_space) {
                    positionx = r.nextInt(9 + 1);
                    positiony = r.nextInt(9 + 1);
                    if (czy_wolne(positionx, positiony))
                        ocupied_space = false;
                }
                ocupied_space = true;
                compMap[positionx][positiony] = 2;
            } else{
                while (ocupied_space) {
                    positionx = r.nextInt(9 + 1);
                    positiony = r.nextInt(9 + 1);
                    if (czy_wolne(positionx, positiony))
                        ocupied_space = false;
                }
                ocupied_space = true;
                compMap[positionx][positiony] = 2;
            }
        }
    }

    private boolean czy_wolne(int x, int y){
        if(compMap[x][y]==2)
            return false;
        if(x!=0&&compMap[x-1][y]==2)
            return false;
        if(x!=9&&compMap[x+1][y]==2)
            return false;
        if(y!=0&&compMap[x][y-1]==2)
            return false;
        if(y!=9&&compMap[x][y+1]==2)
            return false;
        if(x!=0&&y!=0&&compMap[x-1][y-1]==2)
            return false;
        if(x!=0&&y!=9&&compMap[x-1][y+1]==2)
            return false;
        if(x!=9&&y!=0&&compMap[x+1][y-1]==2)
            return false;
        if(x!=9&&y!=9&&compMap[x+1][y+1]==2)
            return false;
        return true;
    }

    public String[] informuj(int i){
        String odpowiedz[] = new String[3];
        if(compMap[i%10][(i-i%10)/10]==0) {
            compMap[i % 10][(i - i % 10) / 10] = 1;
            odpowiedz[0] = "1";
            synchronized (this) {
                try {
                    wait(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            odpowiedz[1] = strzelaj();
            odpowiedz[2] = "nie";
        }
        else{
            compMap[i%10][(i-i%10)/10]=3;
            odpowiedz[0]="3";
            odpowiedz[1]="trafiony";
            if(czy_zatopiony(false, i%10, (i-i%10)/10, 0)) {
                odpowiedz[2] = "zatopiony";
            }
            else
                odpowiedz[2]="nie";
        }
        return odpowiedz;
    }

    private String strzelaj() {
        int index;
        boolean czy_trafiony = true;
        StringBuilder odpowiedz = new StringBuilder();
        Random r = new Random();
        while (czy_trafiony) {
            update_komputer_data();
            if(last_hit[0]==10||last_hit[1]==10) {
                int ktory_strzal=0;
                do {
                    do {
                        if (list.size() > 0) {
                            index = r.nextInt(list.size());
                            index = list.remove(index);
                        } else {
                            return odpowiedz.toString();
                        }
                    }
                    while (playerMap[index % 10][(index - index % 10) / 10] == 1 || playerMap[index % 10][(index - index % 10) / 10] == 3);
                    if (playerMap[index % 10][(index - index % 10) / 10] == 0) {
                        if(lvl&&ktory_strzal==0)
                            list.add(index);
                        else {
                            playerMap[index % 10][(index - index % 10) / 10] = 1;
                            czy_trafiony = false;
                            last_hit[0] = 10;
                            last_hit[1] = 10;
                        }
                        ktory_strzal += 1;
                    }
                    if (playerMap[index % 10][(index - index % 10) / 10] == 2) {
                        playerMap[index % 10][(index - index % 10) / 10] = 3;
                        ktory_strzal+=2;
                        if (!czy_zatopiony(true, index % 10, (index - index % 10) / 10, 0)) {
                            do_zamalowania_gracza.clear();
                            last_hit[0] = index % 10;
                            last_hit[1] = (index - index % 10) / 10;
                        } else {
                            last_hit[0] = 10;
                            last_hit[1] = 10;
                        }
                    }
                } while(lvl&&ktory_strzal<2);
            } else {
                if(last_hit[0]!=0&&playerMap[last_hit[0]-1][last_hit[1]]==3){         //lewy był zatopiony
                    if(last_hit[0]!=9&&playerMap[last_hit[0]+1][last_hit[1]]!=1){         //jeśli prawy to mgła
                        index=last_hit[0]+1+last_hit[1]*10;
                        if (playerMap[last_hit[0]+1][last_hit[1]]==2){                        //trafiam
                            playerMap[last_hit[0]+1][last_hit[1]]=3;
                            if (!czy_zatopiony(true, last_hit[0], last_hit[1], 0)) {
                                do_zamalowania_gracza.clear();
                                last_hit[0]+=1;
                            } else {                                                        //zatapiam
                                last_hit[0] = 10;
                                last_hit[1] = 10;
                            }
                        } else {                                                            //pudłuję
                            playerMap[last_hit[0]+1][last_hit[1]]=1;
                            czy_trafiony = false;
                        }
                    } else {                                                            //jeśli jesteśmy przy prawej granicy, lub prawy to pudło
                        int tmp=last_hit[0]-2;
                        while (playerMap[tmp][last_hit[1]]==3)                            //przesuwam się w lewo aż do najbliższej mgły
                            tmp-=1;
                        index=tmp+last_hit[1]*10;
                        if (playerMap[tmp][last_hit[1]]==2){                                  //trafiam
                            playerMap[tmp][last_hit[1]]=3;
                            if (!czy_zatopiony(true, last_hit[0], last_hit[1], 0)) {
                                do_zamalowania_gracza.clear();
                                last_hit[0]=tmp;
                            } else {                                                        //zatapiam
                                last_hit[0] = 10;
                                last_hit[1] = 10;
                            }
                        } else {                                                            //pudłuję
                            playerMap[tmp][last_hit[1]]=1;
                            czy_trafiony = false;
                        }
                    }
                } else if(last_hit[0]!=9&&playerMap[last_hit[0]+1][last_hit[1]]==3){      //prawy był zatopiony
                    if(last_hit[0]!=0&&playerMap[last_hit[0]-1][last_hit[1]]!=1){             //jeśli lewy to mgła
                        index=last_hit[0]-1+last_hit[1]*10;
                        if (playerMap[last_hit[0]-1][last_hit[1]]==2){                        //trafiam
                            playerMap[last_hit[0]-1][last_hit[1]]=3;
                            if (!czy_zatopiony(true, last_hit[0], last_hit[1], 0)) {
                                do_zamalowania_gracza.clear();
                                last_hit[0]-=1;
                            } else {                                                        //zatapiam
                                last_hit[0] = 10;
                                last_hit[1] = 10;
                            }
                        } else {                                                            //pudłuję
                            playerMap[last_hit[0]-1][last_hit[1]]=1;
                            czy_trafiony = false;
                        }
                    } else {                                                            //jeśli jesteśmy przy lewej granicy, lub lewy to pudło
                        int tmp=last_hit[0]+2;
                        while (playerMap[tmp][last_hit[1]]==3)                            //przesuwam się w prawo aż do najbliższej mgły
                            tmp+=1;
                        index=tmp+last_hit[1]*10;
                        if (playerMap[tmp][last_hit[1]]==2){                                  //trafiam
                            playerMap[tmp][last_hit[1]]=3;
                            if (!czy_zatopiony(true, last_hit[0], last_hit[1], 0)) {
                                do_zamalowania_gracza.clear();
                                last_hit[0]=tmp;
                            } else {                                                        //zatapiam
                                last_hit[0] = 10;
                                last_hit[1] = 10;
                            }
                        } else {                                                            //pudłuję
                            playerMap[tmp][last_hit[1]]=1;
                            czy_trafiony = false;
                        }
                    }
                } else if(last_hit[1]!=0&&playerMap[last_hit[0]][last_hit[1]-1]==3){  //górny był zatopiony
                    if(last_hit[1]!=9&&playerMap[last_hit[0]][last_hit[1]+1]!=1){         //jeśli dolny to mgła
                        index=last_hit[0]+(last_hit[1]+1)*10;
                        if (playerMap[last_hit[0]][last_hit[1]+1]==2){                        //trafiam
                            playerMap[last_hit[0]][last_hit[1]+1]=3;
                            if (!czy_zatopiony(true, last_hit[0], last_hit[1], 0)) {
                                do_zamalowania_gracza.clear();
                                last_hit[1]+=1;
                            } else {                                                        //zatapiam
                                last_hit[0] = 10;
                                last_hit[1] = 10;
                            }
                        } else {                                                            //pudłuję
                            playerMap[last_hit[0]][last_hit[1]+1]=1;
                            czy_trafiony = false;
                        }
                    } else {                                                            //jeśli jesteśmy przy dolnej granicy, lub dolny to pudło
                        int tmp=last_hit[1]-2;
                        while (playerMap[last_hit[0]][tmp]==3)                            //przesuwam się w górę aż do najbliższej mgły
                            tmp-=1;
                        index=last_hit[0]+tmp*10;
                        if (playerMap[last_hit[0]][tmp]==2){                                  //trafiam
                            playerMap[last_hit[0]][tmp]=3;
                            if (!czy_zatopiony(true, last_hit[0], last_hit[1], 0)) {
                                do_zamalowania_gracza.clear();
                                last_hit[1]=tmp;
                            } else {                                                        //zatapiam
                                last_hit[0] = 10;
                                last_hit[1] = 10;
                            }
                        } else {                                                            //pudłuję
                            playerMap[last_hit[0]][tmp]=1;
                            czy_trafiony = false;
                        }
                    }
                } else if(last_hit[1]!=9&&playerMap[last_hit[0]][last_hit[1]+1]==3){      //dolny był zatopiony
                    if(last_hit[1]!=0&&playerMap[last_hit[0]][last_hit[1]-1]!=1){             //jeśli górny to mgła
                        index=last_hit[0]+(last_hit[1]-1)*10;
                        if (playerMap[last_hit[0]][last_hit[1]-1]==2){                        //trafiam
                            playerMap[last_hit[0]][last_hit[1]-1]=3;
                            if (!czy_zatopiony(true, last_hit[0], last_hit[1], 0)) {
                                do_zamalowania_gracza.clear();
                                last_hit[1]-=1;
                            } else {                                                        //zatapiam
                                last_hit[0] = 10;
                                last_hit[1] = 10;
                            }
                        } else {                                                            //pudłuję
                            playerMap[last_hit[0]][last_hit[1]-1]=1;
                            czy_trafiony = false;
                        }
                    } else {                                                            //jeśli jesteśmy przy górnej granicy, lub górny to pudło
                        int tmp=last_hit[1]+2;
                        while (playerMap[last_hit[0]][tmp]==3)                            //przesuwam się w dół aż do najbliższej mgły
                            tmp+=1;
                        index=last_hit[0]+tmp*10;
                        if (playerMap[last_hit[0]][tmp]==2){                                  //trafiam
                            playerMap[last_hit[0]][tmp]=3;
                            if (!czy_zatopiony(true, last_hit[0], last_hit[1], 0)) {
                                do_zamalowania_gracza.clear();
                                last_hit[1]=tmp;
                            } else {                                                        //zatapiam
                                last_hit[0] = 10;
                                last_hit[1] = 10;
                            }
                        } else {                                                            //pudłuję
                            playerMap[last_hit[0]][tmp]=1;
                            czy_trafiony = false;
                        }
                    }
                } else {
                    int kierunek;
                    int delta_x;
                    int delta_y;
                    ArrayList<Integer> kierunki = new ArrayList<>();
                    if(last_hit[0]!=0)
                        kierunki.add(0);
                    if(last_hit[0]!=9)
                        kierunki.add(1);
                    if(last_hit[1]!=0)
                        kierunki.add(2);
                    if(last_hit[1]!=9)
                        kierunki.add(3);
                    do {
                        kierunek = r.nextInt(kierunki.size());
                        kierunek = kierunki.remove(kierunek)*2-1;
                        if (kierunek<2){
                            delta_x=kierunek;
                            delta_y=0;
                        } else {
                            delta_x=0;
                            delta_y=kierunek-4;
                        }
                    } while(playerMap[last_hit[0]+delta_x][last_hit[1]+delta_y]==1);
                    index=last_hit[0]+delta_x+(last_hit[1]+delta_y)*10;
                    if (playerMap[last_hit[0]+delta_x][last_hit[1]+delta_y]==2){                                  //trafiam
                        playerMap[last_hit[0]+delta_x][last_hit[1]+delta_y]=3;
                        if (!czy_zatopiony(true, last_hit[0], last_hit[1], 0)) {
                            do_zamalowania_gracza.clear();
                            last_hit[0]+=delta_x;
                            last_hit[1]+=delta_y;
                        } else {                                                        //zatapiam
                            last_hit[0] = 10;
                            last_hit[1] = 10;
                        }
                    } else {                                                            //pudłuję
                        playerMap[last_hit[0]+delta_x][last_hit[1]+delta_y]=1;
                        czy_trafiony = false;
                    }
                }
            }
            odpowiedz.append(index%10).append((index-index%10)/10);
        }
        return odpowiedz.toString();
    }

    private boolean czy_zatopiony(boolean kto, int x, int y, int kierunek){ //true-komputer, false-gracz
        if(kto) {                                                           //kierunek: <-, gora, ->, dol, 0-poczatek
            if (playerMap[x][y] == 3) {
                if (x != 0 && (kierunek==1 || kierunek==0) && !czy_zatopiony(true, x - 1, y, 1))
                    return false;
                if (x != 9 && (kierunek==3 || kierunek==0) && !czy_zatopiony(true, x + 1, y, 3))
                    return false;
                if (y != 0 && (kierunek==2 || kierunek==0) &&  !czy_zatopiony(true, x, y - 1,2))
                    return false;
                if (y != 9 && (kierunek==4 || kierunek==0) &&  !czy_zatopiony(true, x, y + 1, 4))
                    return false;
                do_zamalowania_gracza.add(x);
                do_zamalowania_gracza.add(y);
            }
            if(playerMap[x][y] == 2)
                return false;
        }else{
            if (compMap[x][y] == 3) {
                if (x != 0 && kierunek!=3 && !czy_zatopiony(false, x - 1, y, 1))
                    return false;
                if (x != 9 && kierunek!=1 && !czy_zatopiony(false, x + 1, y, 3))
                    return false;
                if (y != 0 && kierunek!=4 &&  !czy_zatopiony(false, x, y - 1,2))
                    return false;
                if (y != 9 && kierunek!=2 &&  !czy_zatopiony(false, x, y + 1, 4))
                    return false;
                do_zamalowania_komputera.add(x);
                do_zamalowania_komputera.add(y);
            }
            if(compMap[x][y] == 2)
                return false;
        }
        return true;
    }

    public int[][] podaj_mape() {
        for(int i=0; i<playerMap.length; i++)
            do_wysylki[i]= Arrays.copyOf(playerMap[i], playerMap[i].length);
        return do_wysylki;
    }

    private void update_komputer_data(){
        while (!do_zamalowania_gracza.isEmpty()) {
            int x = do_zamalowania_gracza.remove(0);
            int y = do_zamalowania_gracza.remove(0);
            maluj_dookola(true, x, y);
        }
    }

    public ArrayList<Integer> co_zamalowac(){
        int x;
        int y;
        for(int index=0; index<do_zamalowania_komputera.size()/2; index++) {
            x = do_zamalowania_komputera.get(2*index);
            y = do_zamalowania_komputera.get(2*index+1);
            maluj_dookola(false, x, y);
        }
        return do_zamalowania_komputera;
    }

    private void maluj_dookola(boolean kto, int x,int y){
        if(kto) {
            if (y != 0 && playerMap[x][y - 1] == 0) {
                playerMap[x][y - 1] = 1;
                list.remove(Integer.valueOf(x+10*(y-1)));
            }
            if (y != 9 && playerMap[x][y + 1] == 0) {
                playerMap[x][y + 1] = 1;
                list.remove(Integer.valueOf(x+10*(y+1)));
            }
            if (x != 0) {
                if (playerMap[x - 1][y] == 0){
                    playerMap[x - 1][y] = 1;
                    list.remove(Integer.valueOf(x-1+10*y));
                }
                if (y != 0 && playerMap[x - 1][y - 1] == 0) {
                    playerMap[x - 1][y - 1] = 1;
                    list.remove(Integer.valueOf(x-1+10*(y-1)));
                }
                if (y != 9 && playerMap[x - 1][y + 1] == 0) {
                    playerMap[x - 1][y + 1] = 1;
                    list.remove(Integer.valueOf(x-1+10*(y+1)));
                }
            }
            if (x != 9) {
                if (playerMap[x + 1][y] == 0) {
                    playerMap[x + 1][y] = 1;
                    list.remove(Integer.valueOf(x+1+10*y));
                }
                if (y != 0 && playerMap[x + 1][y - 1] == 0) {
                    playerMap[x + 1][y - 1] = 1;
                    list.remove(Integer.valueOf(x+1+10*(y-1)));
                }
                if (y != 9 && playerMap[x + 1][y + 1] == 0) {
                    playerMap[x + 1][y + 1] = 1;
                    list.remove(Integer.valueOf(x+1+10*(y+1)));
                }
            }
        }else{
            if (y != 0 && compMap[x][y - 1] == 0)
                compMap[x][y - 1] = 1;
            if (y != 9 && compMap[x][y + 1] == 0)
                compMap[x][y + 1] = 1;
            if (x != 0) {
                if (compMap[x - 1][y] == 0)
                    compMap[x - 1][y] = 1;
                if (y != 0 && compMap[x - 1][y - 1] == 0)
                    compMap[x - 1][y - 1] = 1;
                if (y != 9 && compMap[x - 1][y + 1] == 0)
                    compMap[x - 1][y + 1] = 1;
            }
            if (x != 9) {
                if (compMap[x + 1][y] == 0)
                    compMap[x + 1][y] = 1;
                if (y != 0 && compMap[x + 1][y - 1] == 0)
                    compMap[x + 1][y - 1] = 1;
                if (y != 9 && compMap[x + 1][y + 1] == 0)
                    compMap[x + 1][y + 1] = 1;
            }
        }
    }

    public boolean czyWygralem(){
        int l1=0;
        for(int i=0; i<10; i++){
            for(int j=0; j<10; j++){
                if(compMap[j][i]==2)
                    l1++;
            }
        }
        return l1==0;
    }

    public boolean czyPrzegralem(){
        int l1=0;
        for(int i=0; i<10; i++){
            for(int j=0; j<10; j++){
                if(playerMap[j][i]==2)
                    l1++;
            }
        }
        return l1==0;
    }

    @Override
    public void run() {}
}
