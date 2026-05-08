# Ściąga jakby co

Instrukcja działania

---

## 1. Konfiguracja środowiska

Przed rozpoczęciem prac należy zainstalować zależności oraz przygotować pliki konfiguracyjne.

### Instalacja bibliotek
W folderze frontend wykonaj komendę:

```
npm install
```

### Widoczność plików konfiguracyjnych (.env)
Pliki zaczynające się od kropki są domyślnie ukryte w systemowych menedżerach plików. Aby je zobaczyć:
- macOS: Skrót 
```
CMD + Shift + . 
```
(ta kropka tez).

### Ustawienie zmiennych środowiskowych
1. Plik "zmiennazwena'kropka'env.txt" zmień na .env
2. Domyślny adres API dla prac lokalnych: VITE_API_URL=http://localhost:8080/api.
3. Adres ten będzie modyfikowany w zależności od IP hosta backendu podczas pracy zespołowej.

---

## 2. Uruchamianie projektu

Serwer deweloperski uruchamiamy z flagą udostępniania w sieci lokalnej:
```
npm run dev -- --host
```
Umożliwia to podgląd postępów innym członkom zespołu pod adresem IP wyświetlonym w terminalu.

---

## 3. Workflow Git

Aby zminimalizować ryzyko konfliktów w kodzie, należy przestrzegać poniższego schematu pracy. 
<br>
**Zasada: Nie pushujemy zmian bezpośrednio do gałęzi main.**

### Zarządzanie gałęziami (branchami)
- Sprawdzenie, na którym branchu obecnie pracujesz (aktywny jest oznaczony gwiazdką):
  ```
  git branch
  ```
- Tworzenie nowej gałęzi dla nowego zadania:
```
  git checkout -b nazwa-zadania
```
- Przełączanie się na już istniejącą gałąź:
  ```
  git checkout nazwa-zadania

### Pobieranie zmian
Zawsze przed startem pracy pobierz najnowszą wersję projektu z main:
1. git checkout main
2. git pull origin main
3. git checkout nazwa-twojego-brancha
4. git merge main (wciągnięcie nowych zmian do Twojej pracy)

### Publikacja zmian
Po zakończeniu pracy na swoim branchu:
1. git add .
2. git commit -m "Krótki opis zmian (np. dodanie walidacji formularza)"
3. git push origin nazwa-twojego-brancha

Po wypchnięciu zmian poinformuj resztę zespołu o konieczności sprawdzenia i połączenia Twojego brancha z gałęzią główną (main).

---

## 4. Standardy i struktura

- Komponenty: Nowe elementy interfejsu (przyciski, karty) należy umieszczać w src/components/.
- Style: Korzystamy z biblioteki Tailwind CSS bezpośrednio w klasach HTML (class="...").
- API: Nie należy wpisywać adresów IP bezpośrednio w kodzie. Należy korzystać z import.meta.env.VITE_API_URL.
