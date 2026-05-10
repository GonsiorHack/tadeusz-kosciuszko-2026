<script>
  import { onMount } from 'svelte';

  let showConfirm = $state(false);

  // ── Intro cutscene ──
  let phase = $state('intro');
  let introIndex = $state(0);
  let introDisplayed = $state('');
  let introTyping = $state(false);
  let introTypeInterval = undefined;

  const introMessages = [
    { speaker: 'king', text: "Dobra... jeszcze spora droga przed nami, ale widzę, że czynisz postępy, pionku!" },
    { speaker: 'king', text: "Razem odzy^sk@my mój tron. Nikt n1e będzie m0gł nam st@nąć na drodze, gdy ty opan0wujesz...", partialGlitch: true },
    { speaker: 'king', text: "daskldhaskladasdj asdjaskld askdal #4%^& !@#$&*", fullGlitch: true },
    { speaker: 'player', text: "Królu?" },
    { speaker: 'queen', text: "HAHAHAHA! Niespodziewiłeś się? Twój «król» jest teraz mój! A ty, pionku, jesteś następny.", queenEnter: true },
  ];

  let currentIntro = $derived(introMessages[introIndex]);

  function typeIntro() {
    if (introTypeInterval) clearInterval(introTypeInterval);
    introTyping = true;
    introDisplayed = '';
    const msg = introMessages[introIndex];
    if (msg.fullGlitch) {
      introDisplayed = msg.text;
      introTyping = false;
      return;
    }
    const fullText = msg.text;
    let charIndex = 0;
    introTypeInterval = setInterval(() => {
      if (charIndex < fullText.length) {
        introDisplayed += fullText[charIndex];
        charIndex++;
      } else {
        clearInterval(introTypeInterval);
        introTyping = false;
      }
    }, msg.partialGlitch ? 35 : 50);
  }

  function advanceIntro() {
    if (introTyping) {
      clearInterval(introTypeInterval);
      introDisplayed = introMessages[introIndex].text;
      introTyping = false;
      return;
    }
    if (introIndex < introMessages.length - 1) {
      introIndex++;
      typeIntro();
    } else {
      phase = 'fight';
    }
  }

  // Stan quizu
  let currentQuestionIndex = $state(0);
  let selectedOption = $state(null);
  let isAnswered = $state(false);
  let score = $state(0);
  let quizFinished = $state(false);
  const MAX_HP = 3;
  let playerHP = $state(MAX_HP);

  // Zestaw pytań na finałową walkę
  const questions = [
    {
      question: "Jak wyglądałaby wiadomość 'CYBER' po zaszyfrowaniu Szyfrem Cezara (klucz: +3)?",
      options: ["FBEHU", "ZXABO", "DBAFS", "FDFJW"],
      correctIndex: 0,
      explanation: "C staje się F, Y wraca na początek jako B, B to E, E to H, a R to U."
    },
    {
      question: "W jakiej dziedzinie wykorzystuje się obecnie kryptografię?",
      options: [
        "Tylko w bankowości elektronicznej",
        "Tylko do zabezpieczania komunikatorów",
        "Tylko do ukrywania plików na dysku",
        "We wszystkich powyższych przykładach"
      ],
      correctIndex: 3,
      explanation: "Kryptografia to fundament całego dzisiejszego cyfrowego świata."
    },
    {
      question: "Zaznacz PRAWDZIWE zdanie dotyczące szyfrów:",
      options: [
        "W szyfrach asymetrycznych tylko Ty masz dostęp do publicznej kłódki",
        "Szyfry symetryczne nie są już w ogóle używane",
        "W szyfrach symetrycznych używamy tego samego klucza do szyfrowania i deszyfrowania",
        "AES to najstarszy szyfr asymetryczny"
      ],
      correctIndex: 2,
      explanation: "Symetria oznacza dokładnie ten sam klucz po obu stronach."
    },
    {
      question: "Dlaczego AES nazywamy szyfrem „blokowym”?",
      options: [
        "Ponieważ blokuje dostęp do internetu podczas szyfrowania",
        "Ponieważ szyfruje dane w małych, równych porcjach (blokach)",
        "Ponieważ jest zablokowany dla zwykłych użytkowników",
        "Ponieważ wymyślono go w bloku mieszkalnym"
      ],
      correctIndex: 1,
      explanation: "AES dzieli dane na równe paczki i wielokrotnie je ze sobą miesza."
    },
    {
      question: "Szyfrowanie ECC (Krzywe Eliptyczne):",
      options: [
        "Wyróżnia się użyciem dwóch ogromnych liczb pierwszych (mnożenie)",
        "Jest najstarszym sposobem szyfrowania na świecie",
        "Jest wykorzystywane m.in. w WhatsAppie i oszczędza baterię",
        "Jest klasycznym przykładem szyfrowania symetrycznego"
      ],
      correctIndex: 2,
      explanation: "ECC używa geometrii, dzięki czemu klucze są krótkie, a obliczenia nie obciążają baterii smartfonów."
    },
    {
      question: "Jaka jest właściwa kolejność wysyłania tajnej wiadomości do znajomego (asymetrycznie)?",
      options: [
        "Piszesz tekst ➔ Szyfrujesz SWOIM prywatnym kluczem ➔ Wysyłasz",
        "Pobierasz kłódkę (Klucz Publiczny znajomego) ➔ Szyfrujesz ➔ Wysyłasz",
        "Wysyłasz tekst ➔ Znajomy szyfruje kłódką ➔ Znajomy odsyła",
        "Szyfrujesz hasłem '123' ➔ Wysyłasz pocztą ➔ Zmieniasz hasło"
      ],
      correctIndex: 1,
      explanation: "Zawsze musisz wziąć publiczną kłódkę odbiorcy, by zamknąć w niej wiadomość."
    },
    {
      question: "Dlaczego w ogóle używamy szyfrowania asymetrycznego (kłódka/klucz), skoro szyfry symetryczne (AES) są szybsze?",
      options: [
        "Bo w internecie ciężko jest bezpiecznie przekazać komuś ten sam tajny klucz.",
        "Ponieważ szyfry asymetryczne są ładniejsze wizualnie w kodzie.",
        "To kłamstwo, szyfry symetryczne są dużo wolniejsze.",
        "Ponieważ wymaga tego nowa dyrektywa Unii Europejskiej."
      ],
      correctIndex: 0,
      explanation: "Kryptografia asymetryczna genialnie rozwiązuje problem przekazywania kluczy na odległość!"
    }
  ];

  // Kwestie królowej przed odpowiedzią
  const taunts = [
    "A król gdzie? Zostawił cię samego?",
    "Jesteś zwykłym pionkiem. Pionki nie wygrywają.",
    "Myślisz, że wiedza cię uratuje?",
    "Widziałam lepszych od ciebie w śmietniku historii.",
    "Skąd wziąłeś tę odwagę? Skład apteczny?",
    "Twoja linia obrony jest śmieszna.",
    "Nikt ci nie pomóże. Jesteś sam.",
  ];

  // Dynamiczny dymek postaci w zależności od sytuacji
  let bossDialogue = $derived(() => {
    if (quizFinished) {
      return playerHP > 0 ? "Niemożliwe! Pokonałeś mnie! Znasz moje sekrety!" : "Szach-mat, pionku. Kończ się tu twoja historia.";
    }
    if (!isAnswered) return taunts[currentQuestionIndex % taunts.length];
    if (selectedOption === questions[currentQuestionIndex].correctIndex) return "Agh! Jak mogłeś to wiedzieć?!";
    return "Hahaha! Błędna odpowiedź! Mój szyfr jest nie do złamania!";
  });

  function selectOption(index) {
    if (isAnswered) return;
    selectedOption = index;
    isAnswered = true;

    if (index === questions[currentQuestionIndex].correctIndex) {
      score++;
    } else {
      playerHP = Math.max(0, playerHP - 1);
    }
  }

  function nextQuestion() {
    if (playerHP <= 0) {
      quizFinished = true;
      return;
    }
    if (currentQuestionIndex < questions.length - 1) {
      currentQuestionIndex++;
      selectedOption = null;
      isAnswered = false;
    } else {
      quizFinished = true;
    }
  }

  function resetGame() {
    currentQuestionIndex = 0;
    selectedOption = null;
    isAnswered = false;
    score = 0;
    quizFinished = false;
    playerHP = MAX_HP;
  }

  function handleKeydown(e) {
    if (phase === 'intro') {
      if (e.key === ' ' || e.key === 'Enter') { e.preventDefault(); advanceIntro(); }
      return;
    }
    if (showConfirm) {
      if (e.key === 'Escape') { e.preventDefault(); showConfirm = false; }
      return;
    }
    if (e.key === ' ' || e.key === 'Enter') {
      e.preventDefault();
      if (isAnswered && !quizFinished) {
        nextQuestion();
      } else if (quizFinished && playerHP > 0) {
        location.href = '/strona10';
      } else if (quizFinished && playerHP <= 0) {
        resetGame();
      }
    }
  }

  onMount(() => {
    typeIntro();
    window.addEventListener('keydown', handleKeydown);
    return () => {
      if (introTypeInterval) clearInterval(introTypeInterval);
      window.removeEventListener('keydown', handleKeydown);
    };
  });
</script>

{#if phase === 'intro'}
<main class="intro-main" class:intro-queen-active={currentIntro?.queenEnter}>
  <nav class="glass-nav" class:intro-nav-queen={currentIntro?.queenEnter}>
    <div class="nav-container">
      <button class="back-button" onclick={() => location.href='/strona8'} aria-label="Cofnij">
        <svg width="20" height="20" viewBox="0 0 20 20" fill="none">
          <path d="M12.5 15L7.5 10L12.5 5" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
      </button>
      <button class="page-title-btn" onclick={() => showConfirm = true}>
        {import.meta.env.VITE_APP_NAME}
      </button>
      <div style="width: 80px"></div>
    </div>
  </nav>

  {#if showConfirm}
  <div class="confirm-overlay"
       role="dialog"
       aria-modal="true"
       onkeydown={(e) => e.key === 'Escape' && (showConfirm = false)}>
    <div class="confirm-box">
      <p class="confirm-msg">Czy jesteś pewny? Postęp tej sesji zostanie utracony.</p>
      <div class="confirm-btns">
        <button class="confirm-yes" onclick={() => { sessionStorage.clear(); location.href = '/'; }}>Tak, wróć do menu</button>
        <button class="confirm-no" onclick={() => showConfirm = false}>Nie, kontynuuj</button>
      </div>
    </div>
  </div>
  {/if}

  <section class="story-section">
    <div class="chess-pattern-bg"></div>

    <div class="right-panel intro-right-panel">
      <div class="intro-dialogue"
           onclick={advanceIntro}
           onkeydown={(e) => (e.key === 'Enter' || e.key === ' ') && advanceIntro()}
           role="button"
           tabindex="0">
        <div class="speech-bubble"
             class:intro-bubble-glitch={currentIntro?.fullGlitch}
             class:intro-bubble-partial={currentIntro?.partialGlitch}
             class:intro-bubble-queen={currentIntro?.queenEnter}
             class:intro-bubble-player={currentIntro?.speaker === 'player'}>
          <p class="bubble-text"
             class:glitch-text-anim={currentIntro?.fullGlitch}
             class:queen-intro-text={currentIntro?.queenEnter}>
            {introDisplayed}{introTyping && !currentIntro?.fullGlitch ? '◌' : ''}
          </p>
        </div>
        <p class="hint-text">
          {introTyping ? 'Skip' : introIndex < introMessages.length - 1 ? 'Kontynuuj →' : 'Do walki →'}
        </p>
      </div>
    </div>

    {#if currentIntro?.queenEnter}
      <div class="queen-intro-wrap">
        <div class="queen-intro-glow"></div>
        <img class="queen-intro-img" src="/redQueen.svg" alt="Wroga Królowa" />
      </div>
    {:else}
      <div class="king-abs-wrap"
           class:king-glitching={currentIntro?.fullGlitch}
           class:king-partial-glitch={currentIntro?.partialGlitch}>
        <div class="king-abs-glow"></div>
        <img class="king-abs-img" src="/whiteKing.svg" alt="Biały Król" />
      </div>
    {/if}
  </section>
</main>
{:else}
<main class="boss-fight-bg">
  <nav class="glass-nav boss-nav">
    <div class="nav-container">
      <button class="back-button boss-back" aria-label="Cofnij" disabled>
        <svg width="20" height="20" viewBox="0 0 20 20" fill="none">
          <path d="M12.5 15L7.5 10L12.5 5" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
      </button>
      <span class="boss-title">
        <span class="boss-title-crown" aria-hidden="true">♛</span>
        <span class="boss-title-text">WALKA Z KRÓLOWĄ</span>
        <span class="boss-title-crown" aria-hidden="true">♛</span>
      </span>
      <div class="hp-hearts">
        {#each Array(MAX_HP) as _, i}
          <span class="heart" class:heart-lost={i >= playerHP}>
            <svg width="26" height="26" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z" fill="#e05a5a"/>
            </svg>
          </span>
        {/each}
      </div>
    </div>
  </nav>

  {#if showConfirm}
  <div class="confirm-overlay" role="dialog" aria-modal="true" onkeydown={(e) => e.key === 'Escape' && (showConfirm = false)}>
    <div class="confirm-box">
      <p class="confirm-msg">Czy jesteś pewny? Ucieczka z pola walki zresetuje Twój postęp.</p>
      <div class="confirm-btns">
        <button class="confirm-yes" onclick={() => { sessionStorage.clear(); location.href = '/'; }}>Uciekaj</button>
        <button class="confirm-no" onclick={() => showConfirm = false}>Walcz dalej</button>
      </div>
    </div>
  </div>
  {/if}

  <section class="story-section">
    <div class="chess-pattern-bg"></div>

    <div class="right-panel">
      <div class="workspace" style="border-color: rgba(224, 90, 90, 0.4); box-shadow: 0 10px 40px rgba(224, 90, 90, 0.15);">
        
        {#if !quizFinished}
          <span class="ws-tag" style="color: #e05a5a;">Pytanie {currentQuestionIndex + 1} z {questions.length}</span>
          <h2 class="ws-title" style="color: #2a2a2a; margin-bottom: 1.5rem; font-size: 1.3rem;">
            {questions[currentQuestionIndex].question}
          </h2>

          <div style="display: flex; flex-direction: column; gap: 0.8rem; flex: 1;">
            {#each questions[currentQuestionIndex].options as option, index}
              <button 
                class="quiz-option"
                class:selected={selectedOption === index}
                class:correct={isAnswered && index === questions[currentQuestionIndex].correctIndex}
                class:wrong={isAnswered && selectedOption === index && index !== questions[currentQuestionIndex].correctIndex}
                disabled={isAnswered}
                onclick={() => selectOption(index)}
              >
                <div class="option-letter">{['A', 'B', 'C', 'D'][index]}</div>
                <div class="option-text">{option}</div>
              </button>
            {/each}
          </div>

          {#if isAnswered}
            <div class="feedback-box" class:feedback-hit={selectedOption === questions[currentQuestionIndex].correctIndex} class:feedback-miss={selectedOption !== questions[currentQuestionIndex].correctIndex}>
              <span class="feedback-label">
                {selectedOption === questions[currentQuestionIndex].correctIndex ? '⚔️ Celny cios! Królowa dostała cios!' : '💀 Chybiony! Straciłeś życie!'}
              </span>
              <span class="feedback-explain">{questions[currentQuestionIndex].explanation}</span>
            </div>

            <button onclick={nextQuestion} class="next-btn">
              {playerHP <= 0 ? 'Koniec walki…' : currentQuestionIndex < questions.length - 1 ? 'Następne pytanie ⚔️' : 'Zakończ walkę ♛'}
            </button>
          {/if}

        {:else}
          <div style="text-align: center; display: flex; flex-direction: column; justify-content: center; align-items: center; height: 100%; gap: 0.5rem;">
            {#if playerHP <= 0}
              <p class="szach-mat-label">SZACH-MAT</p>
            {:else}
              <span style="font-size: 4rem; margin-bottom: 0.5rem;">🏆</span>
            {/if}
            <h2 style="font-size: 2rem; color: {playerHP > 0 ? '#2a6e3f' : '#b83a3a'}; margin-bottom: 0.25rem;">
              {playerHP > 0 ? 'Zwycięstwo!' : 'Porażka...'}
            </h2>
            <p style="font-size: 1rem; color: var(--color-text); opacity: 0.75;">
              Poprawne odpowiedzi: <strong>{score} / {questions.length}</strong>
            </p>
            <div style="display: flex; gap: 6px; margin: 0.5rem 0 1.5rem;">
              {#each Array(MAX_HP) as _, i}
                <span class="heart" class:heart-lost={i >= playerHP}>
                  <svg width="28" height="28" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                    <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z" fill="#e05a5a"/>
                  </svg>
                </span>
              {/each}
            </div>
            <button 
              onclick={() => playerHP > 0 ? location.href = '/strona10' : resetGame()}
              style="padding: 1rem 2rem; background: {playerHP > 0 ? 'var(--color-primary)' : '#e05a5a'}; color: white; border-radius: 8px; font-weight: bold; border: none; cursor: pointer; text-transform: uppercase; letter-spacing: 1px;"
            >
              {playerHP > 0 ? 'Dalej →' : 'Spróbuj jeszcze raz'}
            </button>
          </div>
        {/if}

      </div>

      {#if isAnswered && !quizFinished}
        <p class="hint-text" style="color: #e05a5a; text-align: center;">Kliknij Spację, by zadać kolejny cios →</p>
      {/if}
    </div>

    <div class="queen-scene">
      {#key currentQuestionIndex + '_' + (isAnswered ? selectedOption : -1)}
        <div class="queen-speech">
          <p class="queen-bubble-text">{bossDialogue()}</p>
        </div>
      {/key}
      <div class="queen-glow"></div>
      <img class="queen-img boss-img" src="/redQueen.svg" alt="Wroga Królowa" />
    </div>
  </section>
</main>
{/if}

<style>
:global(.boss-fight-bg) {
    background: linear-gradient(135deg, #f5eaea 0%, #e8d8d8 50%, #f0e4e4 100%) !important;
  }
  :global(.boss-fight-bg .chess-pattern-bg) {
    background-image:
      linear-gradient(45deg, rgba(224,90,90,0.1) 25%, transparent 25%),
      linear-gradient(-45deg, rgba(224,90,90,0.1) 25%, transparent 25%),
      linear-gradient(45deg, transparent 75%, rgba(224,90,90,0.1) 75%),
      linear-gradient(-45deg, transparent 75%, rgba(224,90,90,0.1) 75%) !important;
  }
  
  .boss-img {
    animation: floatBoss 4s ease-in-out infinite !important;
  }
  @keyframes floatBoss {
    0%, 100% { transform: translateY(0) scale(1); }
    50% { transform: translateY(-15px) scale(1.02); }
  }

  .quiz-option {
    display: flex;
    align-items: center;
    gap: 1rem;
    padding: 1rem;
    background: white;
    border: 2px solid rgba(106,117,155,0.2);
    border-radius: 12px;
    cursor: pointer;
    transition: all 0.2s ease;
    text-align: left;
  }
  .quiz-option:not(:disabled):hover {
    border-color: var(--color-primary);
    background: rgba(106,117,155,0.05);
    transform: translateX(5px);
  }
  
  .option-letter {
    width: 32px;
    height: 32px;
    display: flex;
    align-items: center;
    justify-content: center;
    background: rgba(106,117,155,0.1);
    color: var(--color-primary);
    font-weight: 800;
    border-radius: 8px;
    flex-shrink: 0;
  }
  .option-text {
    font-size: 0.95rem;
    color: var(--color-text);
    font-weight: 500;
    line-height: 1.4;
  }

  .quiz-option.correct {
    border-color: #3cb464;
    background: rgba(60, 180, 100, 0.08);
  }
  .quiz-option.correct .option-letter {
    background: #3cb464;
    color: white;
  }
  .quiz-option.wrong {
    border-color: #e05a5a;
    background: rgba(224, 90, 90, 0.08);
    opacity: 0.8;
  }
  .quiz-option.wrong .option-letter {
    background: #e05a5a;
    color: white;
  }
:root {
  --color-primary: #6A759B;
  --color-text: #373A40;
  --font-family: 'Inter', sans-serif;
}
* { margin: 0; padding: 0; box-sizing: border-box; font-family: var(--font-family); }

main {
  height: 100vh;
  background: linear-gradient(135deg, #f5f5f5 0%, #e8e8e8 50%, #f0f0f0 100%);
  position: relative;
  overflow: hidden;
}

.glass-nav {
  position: fixed;
  top: 0; left: 0; right: 0;
  z-index: 1000;
  backdrop-filter: blur(10px);
  background: rgba(255,255,255,0.7);
  border-bottom: 1px solid rgba(255,255,255,0.5);
  box-shadow: 0 8px 32px rgba(0,0,0,0.1);
}
.nav-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.8rem 2rem;
}
.back-button {
  display: flex;
  align-items: center;
  background: transparent;
  color: var(--color-text);
  border: 2px solid var(--color-primary);
  padding: 0.5rem 0.75rem;
  border-radius: 50px;
  cursor: pointer;
  transition: all 0.3s ease;
}
.back-button:hover { background: var(--color-primary); color: white; transform: translateX(-3px); }

.page-title-btn {
  font-size: 1.6rem;
  font-weight: 800;
  font-family: 'Georgia', 'Palatino Linotype', serif;
  color: #3e4770;
  letter-spacing: -0.5px;
  font-style: italic;
  background: transparent;
  border: none;
  cursor: pointer;
  padding: 0.3rem 0.6rem;
  border-radius: 8px;
  transition: background 0.2s;
}
.page-title-btn:hover { background: rgba(106,117,155,0.1); }

.page-counter { font-size: 1rem; color: var(--color-text); font-weight: 600; opacity: 0.7; }

.confirm-overlay {
  position: fixed;
  inset: 0;
  z-index: 2000;
  background: rgba(0,0,0,0.45);
  backdrop-filter: blur(6px);
  display: flex;
  align-items: center;
  justify-content: center;
}
.confirm-box {
  background: white;
  border-radius: 20px;
  padding: 2rem 2.5rem;
  max-width: 420px;
  width: 90%;
  box-shadow: 0 20px 60px rgba(0,0,0,0.2);
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  text-align: left;
}
.confirm-msg {
  font-size: 1.15rem;
  color: var(--color-text);
  font-weight: 500;
  line-height: 1.5;
}
.confirm-btns {
  display: flex;
  gap: 1rem;
  justify-content: center;
  flex-wrap: wrap;
}
.confirm-yes {
  background: #e05a5a;
  color: white;
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: 50px;
  font-size: 0.95rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}
.confirm-yes:hover { background: #c94444; transform: translateY(-2px); }
.confirm-no {
  background: transparent;
  color: var(--color-primary);
  border: 2px solid var(--color-primary);
  padding: 0.75rem 1.5rem;
  border-radius: 50px;
  font-size: 0.95rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}
.confirm-no:hover { background: rgba(106,117,155,0.1); transform: translateY(-2px); }

.story-section {
  position: relative;
  height: 100vh;
  padding-top: 70px;
  display: flex;
  align-items: flex-end;
  justify-content: center;
}

.chess-pattern-bg {
  position: absolute; top: 0; left: 0; right: 0; bottom: 0;
  opacity: 0.03;
  background-image:
    linear-gradient(45deg, #6A759B 25%, transparent 25%),
    linear-gradient(-45deg, #6A759B 25%, transparent 25%),
    linear-gradient(45deg, transparent 75%, #6A759B 75%),
    linear-gradient(-45deg, transparent 75%, #6A759B 75%);
  background-size: 100px 100px;
  background-position: 0 0, 0 50px, 50px -50px, -50px 0;
}


.right-panel {
  position: absolute;
  top: 80px;
  left: calc(4vw + 320px + 2rem);
  right: 2.5rem;
  bottom: 0;
  display: flex;
  flex-direction: column;
  gap: 1rem;
  padding-bottom: 1.5rem;
  z-index: 2;
}

.workspace {
  flex: 1;
  background: rgba(255,255,255,0.87);
  backdrop-filter: blur(14px);
  border: 1.5px solid rgba(106,117,155,0.14);
  border-radius: 24px;
  padding: 1.8rem 2.2rem;
  display: flex;
  flex-direction: column;
  gap: 1.1rem;
  overflow-y: auto;
  box-shadow: 0 8px 40px rgba(106,117,155,0.12);
  animation: wsFadeIn 0.4s cubic-bezier(0.22,1,0.36,1) both;
}
@keyframes wsFadeIn {
  from { opacity: 0; transform: translateY(-10px) scale(0.985); }
  to   { opacity: 1; transform: translateY(0)     scale(1); }
}
.ws-tag {
  font-size: 0.72rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.12em;
  color: var(--color-primary);
  opacity: 0.5;
}
.ws-title {
  font-size: 1.45rem;
  font-weight: 800;
  color: var(--color-primary);
  line-height: 1.2;
  margin: 0;
}
.ws-content {
  font-size: 0.97rem;
  color: var(--color-text);
  line-height: 1.75;
  opacity: 0.82;
}

.ws-list {
  display: flex;
  flex-direction: column;
  gap: 0.4rem;
  padding-left: 0;
  list-style: none;
}
.ws-item {
  font-size: 0.94rem;
  color: var(--color-text);
  line-height: 1.5;
  padding: 0.55rem 1rem 0.55rem 1.1rem;
  border-left: 3px solid rgba(106,117,155,0.3);
  background: rgba(106,117,155,0.05);
  border-radius: 0 10px 10px 0;
  animation: itemSlide 0.35s ease both;
}
.ws-item:nth-child(1) { animation-delay: 0.04s; border-left-color: #6A759B; }
.ws-item:nth-child(2) { animation-delay: 0.09s; border-left-color: #7e8fba; }
.ws-item:nth-child(3) { animation-delay: 0.14s; border-left-color: #9aaad0; }
.ws-item:nth-child(4) { animation-delay: 0.19s; border-left-color: #b5c3e0; }
@keyframes itemSlide {
  from { transform: translateX(-10px); opacity: 0; }
  to   { transform: none; opacity: 1; }
}

.ws-steps {
  display: flex;
  flex-direction: column;
  gap: 0;
  padding-left: 0;
  list-style: none;
  counter-reset: step-counter;
}
.ws-step {
  counter-increment: step-counter;
  font-size: 0.94rem;
  color: var(--color-text);
  line-height: 1.5;
  padding: 0.45rem 0.9rem 0.45rem 3rem;
  position: relative;
  margin-bottom: 0.55rem;
}
.ws-step::before {
  content: counter(step-counter);
  position: absolute;
  left: 0;
  top: 0.35rem;
  width: 1.65rem; height: 1.65rem;
  border-radius: 50%;
  background: var(--color-primary);
  color: white;
  font-size: 0.72rem;
  font-weight: 800;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1;
}
.ws-step::after {
  content: '';
  position: absolute;
  left: 0.78rem;
  top: 2rem;
  bottom: -0.55rem;
  width: 2px;
  background: linear-gradient(to bottom, rgba(106,117,155,0.35), rgba(106,117,155,0.04));
}
.ws-step:last-child::after { display: none; }

.ws-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 0.65rem;
}
.ws-card {
  background: rgba(106,117,155,0.06);
  border-radius: 14px;
  padding: 0.9rem 1.1rem;
  display: flex;
  flex-direction: column;
  gap: 0.3rem;
  border-top: 3px solid transparent;
  transition: transform 0.18s ease, box-shadow 0.18s ease;
}
.ws-card:nth-child(4n+1) { border-top-color: #6A759B; }
.ws-card:nth-child(4n+2) { border-top-color: #8e9fc5; }
.ws-card:nth-child(4n+3) { border-top-color: #5a6b92; }
.ws-card:nth-child(4n+4) { border-top-color: #a0b0d4; }
.ws-card:hover { transform: translateY(-2px); box-shadow: 0 6px 20px rgba(106,117,155,0.14); }
.ws-card-head { font-size: 0.88rem; font-weight: 700; color: var(--color-primary); }
.ws-card-body { font-size: 0.82rem; color: var(--color-text); opacity: 0.75; line-height: 1.4; }

.ws-stat-block {
  display: flex;
  flex-direction: column;
  gap: 0.1rem;
  padding: 1.1rem 1.5rem;
  background: linear-gradient(135deg, rgba(106,117,155,0.09), rgba(106,117,155,0.04));
  border-radius: 18px;
  border-left: 4px solid var(--color-primary);
  position: relative;
  overflow: hidden;
}
.ws-stat-block::after {
  content: '';
  position: absolute;
  right: -25px; top: -25px;
  width: 110px; height: 110px;
  border-radius: 50%;
  background: rgba(106,117,155,0.07);
  pointer-events: none;
}
.ws-stat {
  font-size: 3rem;
  font-weight: 900;
  color: var(--color-primary);
  line-height: 1;
  animation: statPop 0.55s cubic-bezier(0.34,1.56,0.64,1) both;
  animation-delay: 0.15s;
}
.ws-stat-label { font-size: 0.88rem; color: var(--color-text); opacity: 0.6; font-weight: 600; }
@keyframes statPop {
  from { transform: scale(0.55) translateY(6px); opacity: 0; }
  to   { transform: scale(1) translateY(0); opacity: 1; }
}

.ws-examples {
  display: flex;
  gap: 0;
  position: relative;
  align-items: stretch;
  border-radius: 14px;
  overflow: hidden;
}
.ws-examples::after {
  content: 'VS';
  position: absolute;
  left: 50%; top: 50%;
  transform: translate(-50%, -50%);
  font-size: 0.68rem;
  font-weight: 900;
  color: var(--color-primary);
  opacity: 0.35;
  letter-spacing: 0.12em;
  z-index: 1;
  background: white;
  border-radius: 50%;
  width: 1.5rem; height: 1.5rem;
  display: flex; align-items: center; justify-content: center;
  box-shadow: 0 2px 8px rgba(106,117,155,0.15);
}
.ws-example { flex: 1; padding: 1rem 1.4rem; display: flex; flex-direction: column; gap: 0.4rem; border: none; }
.ws-bad  { background: rgba(200,70,70,0.08); }
.ws-good { background: rgba(60,180,100,0.08); }
.ws-ex-label { font-size: 0.7rem; font-weight: 700; text-transform: uppercase; letter-spacing: 0.1em; opacity: 0.55; }
.ws-bad  .ws-ex-label { color: #b83a3a; }
.ws-good .ws-ex-label { color: #2e8c52; }
.ws-ex-value { font-family: 'Courier New', monospace; font-size: 1rem; font-weight: 700; color: var(--color-text); padding: 0.3rem 0.6rem; background: rgba(0,0,0,0.05); border-radius: 6px; display: inline-block; word-break: break-all; }

.ws-bars { display: flex; flex-direction: column; gap: 0.75rem; }
.ws-bar-row { display: grid; grid-template-columns: 9rem 1fr 2.8rem; align-items: center; gap: 0.75rem; }
.ws-bar-label { font-size: 0.85rem; color: var(--color-text); font-weight: 500; text-align: right; opacity: 0.85; }
.ws-bar-track { height: 10px; background: rgba(106,117,155,0.1); border-radius: 99px; overflow: hidden; }
.ws-bar-fill {
  height: 100%;
  border-radius: 99px;
  background: linear-gradient(90deg, #5a6b92, #8e9fc5);
  transform-origin: left;
  animation: barGrow 0.65s cubic-bezier(0.22,1,0.36,1) both;
  animation-delay: 0.2s;
}
.ws-bar-val { font-size: 0.78rem; color: var(--color-primary); font-weight: 700; opacity: 0.8; }
@keyframes barGrow { from { transform: scaleX(0); } to { transform: scaleX(1); } }

.ws-note { font-size: 0.86rem; color: var(--color-primary); font-style: italic; opacity: 0.72; padding: 0.55rem 1rem; background: rgba(106,117,155,0.06); border-radius: 10px; border-left: 3px solid rgba(106,117,155,0.28); }

.queen-scene {
  position: absolute;
  bottom: 0;
  left: 4vw;
  display: flex;
  flex-direction: column;
  align-items: center;
  pointer-events: none;
  z-index: 3;
}
.queen-img {
  width: 320px;
  height: auto;
  filter: drop-shadow(0 10px 40px rgba(200,30,30,0.45));
}
.queen-glow {
  position: absolute;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  width: 180px;
  height: 80px;
  background: radial-gradient(ellipse, rgba(200,30,30,0.22) 0%, transparent 70%);
  border-radius: 50%;
  animation: pulse 4s ease-in-out infinite;
  z-index: -1;
}
.queen-speech {
  background: rgba(20, 5, 5, 0.88);
  border: 1.5px solid rgba(224,90,90,0.6);
  border-radius: 14px;
  padding: 0.75rem 1.1rem;
  max-width: 300px;
  min-width: 180px;
  box-shadow: 0 6px 24px rgba(200,30,30,0.3), inset 0 1px 0 rgba(255,255,255,0.05);
  position: relative;
  margin-bottom: 14px;
  animation: queenSpeechPop 0.35s cubic-bezier(0.34,1.56,0.64,1) both;
}
.queen-speech::after {
  content: '';
  position: absolute;
  top: 100%;
  left: 50%;
  transform: translateX(-50%);
  border-left: 10px solid transparent;
  border-right: 10px solid transparent;
  border-top: 12px solid rgba(20,5,5,0.88);
}
.queen-speech::before {
  content: '';
  position: absolute;
  top: calc(100% + 2px);
  left: 50%;
  transform: translateX(-50%);
  border-left: 12px solid transparent;
  border-right: 12px solid transparent;
  border-top: 13px solid rgba(224,90,90,0.6);
}
@keyframes queenSpeechPop {
  from { transform: scale(0.8) translateY(8px); opacity: 0; }
  to   { transform: scale(1) translateY(0); opacity: 1; }
}
.queen-bubble-text {
  font-size: 0.92rem;
  color: #f5d0d0;
  font-weight: 600;
  line-height: 1.45;
  text-align: center;
  font-style: italic;
}

.feedback-box {
  margin-top: 1rem;
  padding: 0.9rem 1.2rem;
  border-radius: 14px;
  display: flex;
  flex-direction: column;
  gap: 0.4rem;
  animation: wsFadeIn 0.3s ease both;
  border-left: 4px solid transparent;
}
.feedback-hit {
  background: rgba(40, 160, 90, 0.1);
  border-left-color: #3cb464;
}
.feedback-miss {
  background: rgba(200, 40, 40, 0.09);
  border-left-color: #e05a5a;
}
.feedback-label {
  display: block;
  font-size: 1rem;
  font-weight: 800;
  letter-spacing: 0.03em;
}
.feedback-hit .feedback-label { color: #2a7a48; }
.feedback-miss .feedback-label { color: #b83a3a; }
.feedback-explain {
  font-size: 0.88rem;
  color: var(--color-text);
  opacity: 0.8;
  line-height: 1.5;
}
.next-btn {
  margin-top: 0.75rem;
  width: 100%;
  padding: 0.9rem 1rem;
  background: linear-gradient(135deg, #2a0808, #4a1010);
  color: #f5d0d0;
  border-radius: 10px;
  font-weight: 800;
  border: 1px solid rgba(224,90,90,0.4);
  cursor: pointer;
  text-transform: uppercase;
  letter-spacing: 2px;
  font-size: 0.85rem;
  transition: all 0.2s;
  box-shadow: 0 4px 16px rgba(200,30,30,0.25);
}
.next-btn:hover { background: linear-gradient(135deg, #3a0a0a, #5e1414); transform: translateY(-1px); box-shadow: 0 6px 20px rgba(200,30,30,0.35); }
.hint-text {
  color: var(--color-primary);
  font-size: 0.9rem;
  font-style: italic;
  opacity: 0.75;
  min-height: 1.2em;
  animation: fadeInOut 2s ease-in-out infinite;
  text-align: center;
}

@keyframes fadeInOut { 0%, 100% { opacity: 0.4; } 50% { opacity: 0.9; } }
@keyframes float { 0%, 100% { transform: translateY(0); } 50% { transform: translateY(-12px); } }
@keyframes pulse { 0%, 100% { opacity: 0.5; transform: scale(1); } 50% { opacity: 0.9; transform: scale(1.1); } }

.boss-nav {
  background: rgba(30, 10, 10, 0.88) !important;
  border-bottom: 1px solid rgba(224, 90, 90, 0.4) !important;
  box-shadow: 0 4px 32px rgba(200, 30, 30, 0.25) !important;
}
.boss-back {
  border-color: rgba(224,90,90,0.4);
  color: rgba(224,90,90,0.5);
  cursor: not-allowed;
  opacity: 0.45;
}
.boss-title {
  display: flex;
  align-items: center;
  gap: 0.55rem;
  font-family: 'Georgia', 'Palatino Linotype', serif;
  font-size: 1.4rem;
  font-weight: 900;
  letter-spacing: 0.18em;
  text-transform: uppercase;
  color: #e05a5a;
  text-shadow: 0 0 18px rgba(224,90,90,0.7), 0 0 40px rgba(180,20,20,0.4);
  animation: bossFlicker 3s ease-in-out infinite;
  user-select: none;
}
.boss-title-crown {
  font-size: 1.5rem;
  animation: crownBob 2s ease-in-out infinite;
  display: inline-block;
  filter: drop-shadow(0 0 6px rgba(224,90,90,0.8));
}
.boss-title-crown:last-child {
  animation-delay: -1s;
}
@keyframes bossFlicker {
  0%, 100% { text-shadow: 0 0 18px rgba(224,90,90,0.7), 0 0 40px rgba(180,20,20,0.4); opacity: 1; }
  45% { text-shadow: 0 0 8px rgba(224,90,90,0.4), 0 0 20px rgba(180,20,20,0.2); opacity: 0.85; }
  50% { text-shadow: 0 0 28px rgba(224,90,90,1), 0 0 60px rgba(200,30,30,0.6); opacity: 1; }
  95% { text-shadow: 0 0 14px rgba(224,90,90,0.5), 0 0 30px rgba(180,20,20,0.3); opacity: 0.9; }
}
@keyframes crownBob {
  0%, 100% { transform: translateY(0) rotate(-5deg); }
  50% { transform: translateY(-4px) rotate(5deg); }
}

.szach-mat-label {
  font-family: 'Georgia', 'Palatino Linotype', serif;
  font-size: 3.5rem;
  font-weight: 900;
  letter-spacing: 0.12em;
  color: #b83a3a;
  text-shadow: 0 0 30px rgba(200,30,30,0.6), 0 4px 0 rgba(0,0,0,0.18);
  animation: szachMatPop 0.6s cubic-bezier(0.34,1.56,0.64,1) both;
  margin-bottom: 0.25rem;
}
@keyframes szachMatPop {
  from { transform: scale(0.5) rotate(-8deg); opacity: 0; }
  to   { transform: scale(1) rotate(0deg); opacity: 1; }
}

.hp-hearts {
  display: flex;
  gap: 4px;
  align-items: center;
}
.heart {
  display: inline-flex;
  transition: transform 0.35s cubic-bezier(0.36, 0.07, 0.19, 0.97), opacity 0.35s ease;
  transform-origin: center;
}
.heart-lost {
  opacity: 0;
  transform: scale(0) rotate(-30deg);
}

@media (max-width: 768px) {
  .king-abs-img { width: 150px; }
  .bubble-text { font-size: 1.2rem; }
  .speech-bubble { padding: 1.4rem 1.8rem; }
  .dialogue-area { margin-bottom: 2rem; padding: 0 1rem; }
}

.speech-bubble {
  width: 100%;
  background: rgba(255,255,255,0.95);
  backdrop-filter: blur(10px);
  border: 2px solid rgba(106,117,155,0.2);
  border-radius: 20px;
  padding: 1.8rem 2.5rem;
  box-shadow: 0 10px 40px rgba(106,117,155,0.15);
  cursor: pointer;
  transition: box-shadow 0.3s ease, border-color 0.3s ease;
  position: relative;
}
.speech-bubble:hover { box-shadow: 0 15px 50px rgba(106,117,155,0.25); border-color: rgba(106,117,155,0.4); }
.bubble-text {
  font-size: 1.45rem;
  color: var(--color-text);
  font-weight: 500;
  line-height: 1.5;
  text-align: center;
}

/* ── INTRO CUTSCENE ── */
.intro-main {
  height: 100vh;
  background: linear-gradient(135deg, #f5f5f5 0%, #e8e8e8 50%, #f0f0f0 100%);
  position: relative;
  overflow: hidden;
  transition: background 0.6s ease;
}
.intro-queen-active {
  background: linear-gradient(135deg, #f5eaea 0%, #e8d8d8 50%, #f0e4e4 100%);
}
.intro-nav-queen {
  background: rgba(40, 10, 10, 0.82) !important;
  border-bottom-color: rgba(200, 60, 60, 0.5) !important;
  box-shadow: 0 4px 24px rgba(180, 20, 20, 0.25) !important;
}
.intro-right-panel {
  left: calc(4vw + 210px + 2rem) !important;
  justify-content: flex-end;
}
.intro-dialogue {
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  cursor: pointer;
}

/* Bubble variants for intro */
.intro-bubble-glitch {
  border-color: rgba(200, 40, 40, 0.75) !important;
  background: rgba(255, 236, 236, 0.97) !important;
  animation: bubbleShake 0.11s linear infinite !important;
}
.intro-bubble-partial {
  border-color: rgba(180, 90, 90, 0.45) !important;
}
.intro-bubble-queen {
  background: rgba(18, 4, 4, 0.94) !important;
  border-color: rgba(224, 90, 90, 0.85) !important;
  box-shadow: 0 0 55px rgba(200, 30, 30, 0.45), 0 10px 40px rgba(180,20,20,0.3) !important;
}
.intro-bubble-player .bubble-text {
  font-style: italic;
  color: #555 !important;
  font-size: 2rem !important;
  font-weight: 600 !important;
  text-align: center;
}
.queen-intro-text {
  color: #f5c0c0 !important;
  font-weight: 700;
}

/* Glitch text animation */
@keyframes glitchText {
  0%   { transform: none; text-shadow: none; color: #b83a3a; }
  18%  { transform: translate(-3px, 1px) skewX(-4deg); text-shadow: 3px 0 #ff0033, -3px 0 #0033ff; color: #ff2200; }
  36%  { transform: translate(3px, -1px) skewX(3deg); text-shadow: -2px 0 #ff0033; color: #333; }
  54%  { transform: translate(-2px, 2px); text-shadow: 2px 0 #00ff99; color: #aa0000; }
  72%  { transform: translate(2px, -2px) skewX(-2deg); text-shadow: -1px 0 #ff0033; color: #ff4400; }
  100% { transform: none; text-shadow: none; color: #b83a3a; }
}
.glitch-text-anim {
  animation: glitchText 0.28s steps(1) infinite;
  font-family: 'Courier New', monospace;
}
@keyframes bubbleShake {
  0%, 100% { transform: translate(0); }
  25%  { transform: translate(-3px, -1px) rotate(-0.5deg); }
  75%  { transform: translate(3px, 1px) rotate(0.5deg); }
}

/* King glitch effects */
.king-abs-img {
  width: 210px;
  height: auto;
  filter: drop-shadow(0 10px 30px rgba(106,117,155,0.3));
  animation: float 6s ease-in-out infinite;
}
.king-abs-glow {
  position: absolute;
  bottom: 30px;
  left: 50px;
  width: 110px;
  height: 110px;
  background: radial-gradient(circle, rgba(106,117,155,0.18) 0%, transparent 70%);
  border-radius: 50%;
  animation: pulse 4s ease-in-out infinite;
}
.king-abs-wrap {
  position: absolute;
  bottom: 0;
  left: 4vw;
  display: flex;
  align-items: flex-end;
  pointer-events: none;
  z-index: 1;
}
.king-glitching .king-abs-img {
  animation: kingGlitch 0.32s steps(1) infinite !important;
}
.king-partial-glitch .king-abs-img {
  filter: drop-shadow(0 10px 30px rgba(160, 60, 60, 0.5)) !important;
}
@keyframes kingGlitch {
  0%   { filter: drop-shadow(0 10px 30px rgba(200,30,30,0.3)) brightness(0.9); transform: translate(0); }
  20%  { filter: drop-shadow(-5px 0 0 #ff0033) drop-shadow(5px 0 0 #0033ff) brightness(1.45); transform: translate(-4px, 2px); }
  40%  { filter: drop-shadow(0 10px 30px rgba(200,30,30,0.7)) brightness(0.6); transform: translate(3px, -2px); }
  60%  { filter: drop-shadow(5px 0 10px #ff3300) brightness(1.25); transform: translate(-2px, 1px); }
  80%  { filter: drop-shadow(0 0 28px rgba(200,30,30,0.95)) brightness(0.75); transform: translate(2px, 3px); }
  100% { filter: drop-shadow(0 10px 30px rgba(200,30,30,0.3)) brightness(0.9); transform: translate(0); }
}

/* Queen dramatic entrance */
.queen-intro-wrap {
  position: absolute;
  bottom: 0;
  left: 4vw;
  display: flex;
  flex-direction: column;
  align-items: center;
  pointer-events: none;
  z-index: 3;
}
.queen-intro-img {
  width: 260px;
  height: auto;
  filter: drop-shadow(0 10px 40px rgba(200,30,30,0.55));
  animation: queenBurst 0.55s cubic-bezier(0.34, 1.56, 0.64, 1) both, floatBoss 4s ease-in-out 0.55s infinite;
}
@keyframes queenBurst {
  from { transform: translateX(-130%) scale(0.65) rotate(-14deg); opacity: 0; }
  to   { transform: translateX(0) scale(1) rotate(0deg); opacity: 1; }
}
.queen-intro-glow {
  width: 150px;
  height: 55px;
  background: radial-gradient(ellipse, rgba(200,30,30,0.3) 0%, transparent 70%);
  border-radius: 50%;
  margin-bottom: -20px;
  animation: pulse 4s ease-in-out infinite;
}
</style>
