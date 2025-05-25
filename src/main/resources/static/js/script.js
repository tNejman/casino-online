async function loadPage(page) {
    try {
        const response = await fetch(page);
        if (!response.ok) {
            throw new Error("Page not found");
        }
        const html = await response.text();
        document.getElementById("content").innerHTML = html;
    } catch (error) {
        document.getElementById("content").innerHTML = `<p>Error loading page: ${error.message}</p>`;
    }
}