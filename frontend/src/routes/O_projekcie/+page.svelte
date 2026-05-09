<script>
  // About page
</script>

<main>
    <nav class="glass-nav">
        <div class="nav-container">
            <button class="back-button" onclick={() => (location.href = '/')}>
                <svg width="20" height="20" viewBox="0 0 20 20" fill="none">
                    <path d="M12.5 15L7.5 10L12.5 5" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
                Powrót
            </button>
            <h2 class="page-title">{import.meta.env.VITE_APP_NAME}</h2>
            <div class="nav-spacer"></div>
        </div>
    </nav>

    <section class="about-section">
        <div class="chess-pattern-bg"></div>
        
        <div class="about-content">
            <div class="about-header">
                <div class="header-icon">♔</div>
                <h1 class="about-title">O Projekcie</h1>
                <p class="about-subtitle">Interaktywna nauka cyberbezpieczeństwa</p>
            </div>

            <div class="info-cards">
                <div class="info-card">
                    <div class="card-icon">🎯</div>
                    <h3 class="card-title">Nasza Misja</h3>
                    <p class="card-text">
                        Projekt {import.meta.env.VITE_APP_NAME} ma na celu edukację w zakresie cyberbezpieczeństwa 
                        poprzez innowacyjne, interaktywne podejście inspirowane grą w szachy. Naszym celem jest 
                        uczynienie nauki o bezpieczeństwie w sieci dostępnej i angażującej dla wszystkich.
                    </p>
                </div>

                <div class="info-card">
                    <div class="card-icon">🔐</div>
                    <h3 class="card-title">Czego Się Nauczysz</h3>
                    <p class="card-text">
                        Podstawy kryptografii, bezpieczne zarządzanie hasłami, rozpoznawanie zagrożeń w sieci, 
                        ochrona danych osobowych, oraz praktyczne umiejętności cyberbezpieczeństwa potrzebne 
                        w codziennym korzystaniu z internetu.
                    </p>
                </div>

                <div class="info-card">
                    <div class="card-icon">🎮</div>
                    <h3 class="card-title">Gamifikacja</h3>
                    <p class="card-text">
                        Poprzez metaforę szachów i interaktywną narrację, kompleksowe zagadnienia 
                        cyberbezpieczeństwa stają się przystępne i łatwe do zrozumienia. Każdy krok 
                        to nowa przygoda w świecie bezpiecznej informatyki.
                    </p>
                </div>

                <div class="info-card">
                    <div class="card-icon">👥</div>
                    <h3 class="card-title">Dla Kogo</h3>
                    <p class="card-text">
                        Projekt skierowany jest do każdego, kto chce podnieść swoje kompetencje w zakresie 
                        cyberbezpieczeństwa - od początkujących użytkowników internetu po osoby pragnące 
                        pogłębić swoją wiedzę w tej dziedzinie.
                    </p>
                </div>
            </div>

            <div class="tech-stack">
                <h3 class="stack-title">Technologie</h3>
                <div class="tech-badges">
                    <span class="tech-badge">SvelteKit</span>
                    <span class="tech-badge">TypeScript</span>
                    <span class="tech-badge">Vite</span>
                    <span class="tech-badge">CSS3</span>
                </div>
            </div>
        </div>
    </section>
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
    overflow-x: hidden;
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

.back-button {
    display: flex;
    align-items: center;
    gap: 0.5rem;
    background: transparent;
    color: var(--color-text);
    border: 2px solid var(--color-primary);
    padding: 0.7rem 1.5rem;
    font-size: 1rem;
    border-radius: 50px;
    cursor: pointer;
    font-weight: 600;
    transition: all 0.3s ease;
}

.back-button:hover {
    background: var(--color-primary);
    color: white;
    transform: translateX(-3px);
}

.page-title {
    font-size: 1.5rem;
    font-weight: 700;
    color: var(--color-primary);
    letter-spacing: -0.5px;
}

.nav-spacer {
    width: 100px;
}

.about-section {
    min-height: 100vh;
    padding: 120px 2rem 3rem;
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

.about-content {
    max-width: 1200px;
    margin: 0 auto;
    z-index: 1;
    position: relative;
}

.about-header {
    text-align: center;
    margin-bottom: 4rem;
    animation: fadeInDown 0.6s ease;
}

@keyframes fadeInDown {
    from {
        opacity: 0;
        transform: translateY(-30px);
    }
    to {
        opacity: 1;
        transform: translateY(0);
    }
}

.header-icon {
    font-size: 5rem;
    margin-bottom: 1rem;
}

.about-title {
    font-size: 3rem;
    font-weight: 700;
    color: var(--color-primary);
    margin-bottom: 1rem;
    letter-spacing: -1px;
}

.about-subtitle {
    font-size: 1.3rem;
    color: var(--color-text);
    opacity: 0.8;
}

.info-cards {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
    gap: 2rem;
    margin-bottom: 3rem;
}

.info-card {
    background: rgba(255, 255, 255, 0.8);
    backdrop-filter: blur(10px);
    border: 2px solid rgba(106, 117, 155, 0.2);
    border-radius: 25px;
    padding: 2.5rem;
    box-shadow: 0 10px 40px rgba(106, 117, 155, 0.1);
    transition: all 0.3s ease;
    animation: fadeInUp 0.6s ease backwards;
}

.info-card:nth-child(1) { animation-delay: 0.1s; }
.info-card:nth-child(2) { animation-delay: 0.2s; }
.info-card:nth-child(3) { animation-delay: 0.3s; }
.info-card:nth-child(4) { animation-delay: 0.4s; }

@keyframes fadeInUp {
    from {
        opacity: 0;
        transform: translateY(30px);
    }
    to {
        opacity: 1;
        transform: translateY(0);
    }
}

.info-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 15px 50px rgba(106, 117, 155, 0.2);
    border-color: rgba(106, 117, 155, 0.4);
}

.card-icon {
    font-size: 3rem;
    margin-bottom: 1rem;
}

.card-title {
    font-size: 1.6rem;
    font-weight: 700;
    color: var(--color-primary);
    margin-bottom: 1rem;
}

.card-text {
    font-size: 1.05rem;
    color: var(--color-text);
    line-height: 1.7;
}

.tech-stack {
    text-align: center;
    background: rgba(106, 117, 155, 0.1);
    padding: 2.5rem;
    border-radius: 25px;
    animation: fadeIn 1s ease;
}

@keyframes fadeIn {
    from { opacity: 0; }
    to { opacity: 1; }
}

.stack-title {
    font-size: 1.8rem;
    font-weight: 700;
    color: var(--color-primary);
    margin-bottom: 1.5rem;
}

.tech-badges {
    display: flex;
    justify-content: center;
    gap: 1rem;
    flex-wrap: wrap;
}

.tech-badge {
    background: var(--color-primary);
    color: white;
    padding: 0.7rem 1.5rem;
    border-radius: 50px;
    font-weight: 600;
    font-size: 1rem;
    box-shadow: 0 4px 15px rgba(106, 117, 155, 0.3);
    transition: all 0.3s ease;
}

.tech-badge:hover {
    transform: translateY(-3px);
    box-shadow: 0 6px 20px rgba(106, 117, 155, 0.4);
}

@media (max-width: 968px) {
    .about-title {
        font-size: 2.2rem;
    }
    
    .about-subtitle {
        font-size: 1.1rem;
    }
    
    .info-cards {
        grid-template-columns: 1fr;
    }
}

@media (max-width: 640px) {
    .glass-nav {
        width: 95%;
        top: 10px;
    }
    
    .nav-container {
        padding: 0.8rem 1rem;
    }
    
    .page-title {
        font-size: 1.2rem;
    }
    
    .about-section {
        padding: 100px 1rem 2rem;
    }
    
    .about-title {
        font-size: 1.8rem;
    }
    
    .info-card {
        padding: 1.5rem;
    }
}
</style>
