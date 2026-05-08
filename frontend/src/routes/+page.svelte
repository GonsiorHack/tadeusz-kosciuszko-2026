<script>
    import { onMount } from 'svelte';

    let message = 'Ładowanie...';

    // onMount: Funkcja uruchamiająca się, gdy komponent pojawia się w przeglądarce.
    onMount(async () => {
        try {
            // fetch: Wysyła żądanie sieciowe do Springa.
            const response = await fetch('http://localhost:8080/api/hello');
            
            // .json(): Metoda parsująca odpowiedź z formatu JSON na obiekt JavaScript.
            const data = await response.json();
            
            // Przypisanie wartości z pola 'message' (z JSONa) do zmiennej w Svelte.
            message = data.message;
        } catch (error) {
            console.error('Błąd:', error);
            message = 'Błąd połączenia!';
        }
    });
</script>

<main class="p-8">
    <h1 class="text-2xl font-bold text-blue-600">
        {message}
    </h1>
</main>

<h1>Welcome to SvelteKit</h1>
<p>Visit <a href="https://svelte.dev/docs/kit">svelte.dev/docs/kit</a> to read the documentation</p>
