<script>
  const categories = [
    {
      title: "Podstawy Kryptografii",
      items: ["Wprowadzenie do szyfrowania", "Klucze publiczne i prywatne", "Certyfikaty SSL/TLS"]
    },
    {
      title: "Bezpieczeństwo Haseł",
      items: ["Silne hasła", "Menedżery haseł", "Uwierzytelnianie dwuskładnikowe"]
    },
    {
      title: "Ataki i Zagrożenia",
      items: ["Phishing", "Man-in-the-Middle", "Ransomware"]
    }
  ];

  let showScenarioPopup = $state(false);

  function openScenarioPopup() {
    showScenarioPopup = true;
  }

  function closeScenarioPopup() {
    showScenarioPopup = false;
  }
</script>

<main>
    <nav class="glass-nav">
        <div class="nav-container">
            <button class="nav-brand" onclick={() => (location.href = '/')}>
                <span class="nav-title">{import.meta.env.VITE_APP_NAME}</span>
            </button>
            <ul class="nav-menu">
                <li><button class="nav-link nav-link-active" onclick={() => (location.href = '/Biblioteka')}>{import.meta.env.VITE_NAV_2}</button></li>
                <li><button class="nav-link" onclick={openScenarioPopup}>{import.meta.env.VITE_NAV_3}</button></li>
                <li><button class="nav-link" onclick={() => (location.href = '/O_projekcie')}>{import.meta.env.VITE_NAV_4}</button></li>
            </ul>
        </div>
    </nav>

    <section class="library-section">
        <div class="chess-pattern-bg"></div>
        
        <div class="library-content">
            <div class="library-header">
                <div class="header-icon">📚</div>
                <h1 class="library-title">Biblioteka Wiedzy</h1>
                <p class="library-subtitle">Materiały edukacyjne o cyberbezpieczeństwie i kryptografii</p>
            </div>

            <div class="categories-grid">
                {#each categories as category}
                <div class="category-card">
                    <h3 class="category-title">{category.title}</h3>
                    <ul class="category-items">
                        {#each category.items as item}
                        <li class="category-item">
                            <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
                                <path d="M6 8L8 10L10 6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                            </svg>
                            {item}
                        </li>
                        {/each}
                    </ul>
                    <button class="explore-button">Przeglądaj materiały</button>
                </div>
                {/each}
            </div>

            <div class="coming-soon">
                <p>🚧 Więcej materiałów wkrótce! 🚧</p>
            </div>
        </div>
    </section>

    {#if showScenarioPopup}
    <div class="modal-overlay"
         role="button"
         tabindex="0"
         onclick={closeScenarioPopup}
         onkeydown={(e) => e.key === 'Escape' && closeScenarioPopup()}>
        <div class="modal-content"
             role="dialog"
             onclick={(e) => e.stopPropagation()}
             onkeydown={(e) => e.stopPropagation()}>
            <button class="modal-close"
                    onclick={closeScenarioPopup}
                    aria-label="Zamknij okno">
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
                    <path d="M18 6L6 18M6 6L18 18" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
                </svg>
            </button>
            <div class="modal-icon">⚠️</div>
            <h2 class="modal-title">Scenariusze zablokowane</h2>
            <p class="modal-message">Musisz najpierw ukończyć szkolenie początkowe aby przejść do następnych scenariuszy</p>
            <button class="modal-button" onclick={closeScenarioPopup}>Rozumiem</button>
        </div>
    </div>
    {/if}
</main>

<style>
:root {
    --color-background: #eeeeee;
    --color-primary: #6A759B;
    --color-text: #373A40;
    --font-family: 'Inter', sans-serif;
}

* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
    font-family: var(--font-family);
}

main {
    min-height: 100vh;
    background: linear-gradient(135deg, #f5f5f5 0%, #e8e8e8 50%, #f0f0f0 100%);
    position: relative;
    overflow: hidden;
}

.glass-nav {
    position: fixed;
    top: 20px;
    left: 50%;
    transform: translateX(-50%);
    width: 90%;
    max-width: 1200px;
    z-index: 1000;
    backdrop-filter: blur(10px);
    background: rgba(255, 255, 255, 0.7);
    border: 1px solid rgba(255, 255, 255, 0.5);
    border-radius: 20px;
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
}

.nav-container {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 1rem 2rem;
}

.nav-brand {
    display: flex;
    align-items: center;
    background: transparent;
    border: none;
    cursor: pointer;
    padding: 0;
}

.nav-title {
    font-size: 1.6rem;
    font-weight: 800;
    font-family: 'Georgia', 'Palatino Linotype', serif;
    color: #3e4770;
    letter-spacing: -0.5px;
    font-style: italic;
}

.nav-menu {
    display: flex;
    align-items: center;
    gap: 0.5rem;
    list-style: none;
}

.nav-link {
    background: transparent;
    border: none;
    cursor: pointer;
    font-size: 0.95rem;
    font-weight: 600;
    color: var(--color-text);
    padding: 0.55rem 1.1rem;
    border-radius: 50px;
    transition: all 0.25s ease;
    opacity: 0.75;
}

.nav-link:hover { background: rgba(106,117,155,0.12); opacity: 1; }
.nav-link-active { background: var(--color-primary); color: white; opacity: 1; }
.nav-link-active:hover { background: #5a6487; color: white; }

.story-section {
    display: flex;
    align-items: center;
    justify-content: center;
    min-height: 100vh;
    padding: 120px 2rem 2rem;
    position: relative;
}

.chess-pattern-bg {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    opacity: 0.03;
    background-image: 
        linear-gradient(45deg, #6A759B 25%, transparent 25%),
        linear-gradient(-45deg, #6A759B 25%, transparent 25%),
        linear-gradient(45deg, transparent 75%, #6A759B 75%),
        linear-gradient(-45deg, transparent 75%, #6A759B 75%);
    background-size: 100px 100px;
    background-position: 0 0, 0 50px, 50px -50px, -50px 0;
}

.story-content {
    display: grid;
    grid-template-columns: 450px 1fr;
    align-items: center;
    gap: 4rem;
    max-width: 1200px;
    width: 100%;
    z-index: 1;
}

.king-container {
    position: relative;
    width: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
}

.story-king {
    width: 100%;
    max-width: 400px;
    height: auto;
    filter: drop-shadow(0 20px 60px rgba(106, 117, 155, 0.3));
    animation: float 6s ease-in-out infinite;
    will-change: transform;
}

.king-glow {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 280px;
    height: 280px;
    background: radial-gradient(circle, rgba(106, 117, 155, 0.2) 0%, transparent 70%);
    border-radius: 50%;
    animation: pulse 4s ease-in-out infinite;
}

@keyframes float {
    0%, 100% { transform: translateY(0px); }
    50% { transform: translateY(-20px); }
}

@keyframes pulse {
    0%, 100% { opacity: 0.5; transform: translate(-50%, -50%) scale(1); }
    50% { opacity: 0.8; transform: translate(-50%, -50%) scale(1.1); }
}

.chat-bubble-container {
    flex: 1;
    cursor: pointer;
    transition: all 0.3s ease;
}

.chat-bubble {
    position: relative;
    background: rgba(255, 255, 255, 0.9);
    backdrop-filter: blur(10px);
    border: 2px solid rgba(106, 117, 155, 0.2);
    border-radius: 25px;
    padding: 2.5rem 3rem;
    min-width: 350px;
    min-height: 150px;
    display: flex;
    align-items: center;
    justify-content: center;
    box-shadow: 0 10px 40px rgba(106, 117, 155, 0.15);
    transition: all 0.3s ease;
}

.chat-bubble-container:hover .chat-bubble {
    transform: scale(1.02);
    box-shadow: 0 15px 50px rgba(106, 117, 155, 0.25);
    border-color: rgba(106, 117, 155, 0.4);
}

.chat-bubble-container:active .chat-bubble {
    transform: scale(0.98);
}

.bubble-text {
    font-size: 1.6rem;
    color: var(--color-text);
    margin: 0;
    font-weight: 500;
    line-height: 1.5;
}

.cursor {
    animation: blink 1s infinite;
    margin-left: 2px;
    color: var(--color-primary);
}

@keyframes blink {
    0%, 49% { opacity: 1; }
    50%, 100% { opacity: 0; }
}

.bubble-tail {
    position: absolute;
    left: -18px;
    top: 50%;
    transform: translateY(-50%);
    width: 0;
    height: 0;
    border-top: 15px solid transparent;
    border-bottom: 15px solid transparent;
    border-right: 20px solid rgba(255, 255, 255, 0.9);
}

.hint-text {
    text-align: center;
    color: var(--color-primary);
    font-size: 1rem;
    margin-top: 1.5rem;
    opacity: 0.8;
    font-style: italic;
    animation: fadeInOut 2s ease-in-out infinite;
}

@keyframes fadeInOut {
    0%, 100% { opacity: 0.5; }
    50% { opacity: 1; }
}

.modal-overlay {
    position: fixed; inset: 0; z-index: 2000;
    background: rgba(0,0,0,0.45);
    backdrop-filter: blur(4px);
    display: flex; align-items: center; justify-content: center;
    padding: 1rem;
    animation: fadeInModal 0.2s ease;
}
.modal-content {
    position: relative;
    background: white; border-radius: 24px;
    padding: 2.5rem 2rem 2rem;
    max-width: 420px; width: 100%;
    box-shadow: 0 24px 64px rgba(0,0,0,0.2);
    text-align: center;
    animation: slideUpModal 0.25s ease;
}
.modal-close {
    position: absolute; top: 1rem; right: 1rem;
    background: transparent; border: none; cursor: pointer;
    color: var(--color-text); opacity: 0.5;
    transition: opacity 0.2s;
}
.modal-close:hover { opacity: 1; }
.modal-icon { font-size: 3rem; margin-bottom: 1rem; }
.modal-title {
    font-size: 1.5rem; font-weight: 700;
    color: var(--color-primary); margin-bottom: 0.75rem;
}
.modal-message {
    font-size: 1rem; color: var(--color-text);
    line-height: 1.65; opacity: 0.8; margin-bottom: 1.75rem;
}
.modal-button {
    background: var(--color-primary); color: white;
    border: none; border-radius: 50px;
    padding: 0.75rem 2rem; font-size: 1rem; font-weight: 600;
    cursor: pointer; transition: background 0.2s;
}
.modal-button:hover { background: #5a6487; }
@keyframes fadeInModal {
    from { opacity: 0; } to { opacity: 1; }
}
@keyframes slideUpModal {
    from { opacity: 0; transform: translateY(20px); }
    to   { opacity: 1; transform: translateY(0); }
}

@media (max-width: 968px) {
    .story-content {
        grid-template-columns: 1fr;
        gap: 2rem;
    }
    
    .king-container {
        order: -1;
    }
    
    .chat-bubble {
        min-width: 300px;
    }
    
    .nav-container {
        padding: 0.8rem 1rem;
    }
    
    .page-title {
        font-size: 1.2rem;
    }
}

@media (max-width: 640px) {
    .glass-nav {
        width: 95%;
        top: 10px;
    }
    
    .story-king {
        max-width: 280px;
    }
    
    .chat-bubble {
        min-width: 250px;
        padding: 2rem;
    }
    
    .bubble-text {
        font-size: 1.3rem;
    }
}
</style>
