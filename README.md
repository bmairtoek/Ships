# Statki
pvp i pve gra na Android 6.0+

### Cel pracy
Za zadanie było stworzenie aplikacji w języku programowania Java. W tym celu napisano grę „Statki”, która działa na systemach Android 6.0 i nowszych. Głównym założeniem aplikacji było działanie w trybie pojedynczego gracza oraz dwóch graczy na osobnych urządzeniach.
### Zastosowane narzędzia/ biblioteki
W celu zrealizowania założeń zastosowano następujące narzędzia i/lub biblioteki:
*	android.bluetooth.BluetoothAdapter, .BluetoothDevice, .BluetoothSocket –  zarządzanie połączeniem Bluetooth; 
*	android.content.BroadcastReceiver – odczytywanie wiadomości typu broadcast (tutaj informacje o zmianie stanu Bluetooth);
*	android.content.IntentFilter – filtr informacji dla BroadcastReceiver;
*	permisions – wymagane są zezwolenia użytkownika na uruchomienie usługi Blutooth:
    *	REQUEST_ENABLE_BT – zezwolenie na uruchomienie modułu Bluetooth,
    *	ACCESS_COARSE_LOCATION – zezwolenie na dostęp do lokalizacji (wymagane aby używać Bluetooth),
    *	ACTION_REQUEST_DISCOVERABLE – zezwolenie na widoczność dla innych urządzeń Bluetooth.
*	android.support.v7.app.AlertDialog – wyskakujące okienka decyzyjne;
*	android.widget.Toast – wyskakujące okienko informacyjne;
*	android.os.CountDownTimer – funkcja wykonująca określoną akcję co okres;
*	android.util.Log – biblioteka wykorzystana do testowania aplikacji;
*	java.util.Random;
*	pl.droidsonroids.gif:android-gif-drawable:1.2 – biblioteka niestandardowa używana do animacji plików *.gif;
*	inne.
### Obsługa interfejsu aplikacji
Działanie aplikacji rozpoczyna się w głównym menu, z którego można uruchomić tryb dla jednego lub dwóch graczy. Po wybraniu pierwszej opcji i poziomu trudności użytkownik zostaje przeniesiony do wyboru układu okrętów, a po zatwierdzeniu rozpoczyna się właściwa rozgrywka. W momencie, gdy gracz wyraża chęć zakończenia rozgrywki, powinien dwukrotnie nacisnąć przycisk „wstecz”, zostanie wtedy przeniesiony do głównego menu. Po wybraniu drugiej opcji użytkownik musi wyrazić zgodę na uruchomienie modułu Bluetooth (czasem także na dostęp do lokalizacji), oraz wybrać czy chce być klientem czy hostem, w przeciwnym razie gra się nie rozpocznie. Po nawiązaniu połączenia rozpoczyna się gra, tak samo jak w trybie jednoosobowym. W przypadku zerwania połączenia obaj gracze otrzymają stosowne powiadomienie i wrócą do głównego menu.
# Zasady gry
Na początku rozgrywki należy stworzyć układ okrętów na mapie. Aby to zrobić należy zaznaczyć 1 czteromasztowiec, 2 trójmasztowce, 3 dwumasztowce, 4 jednomasztowce. Żaden okręt nie może się stykać z innym okrętem oraz nie może skręcać. Po wybraniu układu okrętów rozpoczyna się gra (w trybie dla jednego gracza rozpoczyna gracz, natomiast w trybie dla dwóch graczy rozpoczyna host). W trakcie tury jeden gracz strzela, aż do pierwszego „pudła”. Gra trwa do zestrzelenia wszystkich okrętów jednego z graczy. Po zakończeniu każdy z graczy otrzymuje powiadomienie o wynikach gry.
