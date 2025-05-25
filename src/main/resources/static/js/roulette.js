const rouletteURL = "http://localhost:8081/api/roulette";

        // PLAYER INIT

        async function rouletteReset() {
            try {
                const response = await fetch(`${rouletteURL}/reset`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                });
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                alert(await response.text());
            } catch (error) {
                alert(`Error: ${error.message}`);
            }
        }

        async function roulettePlaceBet() {
            const betName = document.getElementById("betName").value;

            try {
                const response = await fetch(`${rouletteURL}/bet/place?betName=${encodeURIComponent(betName)}`, {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    }
                });

                if (response.ok) {
                    const result = await response.text();
                    document.getElementById("roulettePlaceBetResults").textContent = result;
                } else {
                    const errorText = await response.text();
                    document.getElementById("roulettePlaceBetResults").textContent = `Error: ${errorText}`;
                }
            } catch (error) {
                document.getElementById("roulettePlaceBetResults").textContent = `Error: ${error.message}`;
            }
        }

        async function rouletteAddPlayer() {
            const name = document.getElementById("rouletteAddPlayer").value;

            const response = await fetch(`${rouletteURL}/add/player/${name}`, {
                method: "POST",
                headers: { "Content-Type": "application/json" }
            });

            alert(await response.text());
        }

        async function rouletteSpin() {
            const response = await fetch(`${rouletteURL}/spin`, {
                method: "POST",
                headers: { "Content-Type": "application/json" }
            });

            alert(await response.text());
        }

        /*  =======================================================================
        *                               SETTERS
        ======================================================================= */

        async function rouletteSetSelectedPockets() {
            const input = document.getElementById("rouletteSetSelectedPockets").value;

            // convert string to array of numbers
            const pockets = input.split(',').map(Number);

            const response = await fetch(`${rouletteURL}/pockets/set`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(pockets),
            });

            if (response.ok) {
                const data = await response.text();
                document.getElementById("rouletteSetSelectedPocketsResults").textContent = `Success: ${data}`;
            } else {
                const errorText = await response.text();
                document.getElementById("rouletteSetSelectedPocketsResults").textContent = `Error: ${errorText}`;
            }
        }

        async function rouletteSetBetAmount() {
            const amount = document.getElementById("rouletteSetBetAmount").value;
            const response = await fetch(`${rouletteURL}/bet/amount/set?amount=${amount}`, {
                method: "POST",
                headers: { "Content-Type": "application/json" }
            });

            alert(await response.text());
        }

        async function rouletteSetWinningPocket() {
            const amount = document.getElementById("rouletteSetWinningPocket").value;
            const response = await fetch(`${rouletteURL}/pocket/winning/set?amount=${amount}`, {
                method: "POST",
                headers: { "Content-Type": "application/json" }
            });

            alert(await response.text());
        }

        async function rouletteSetName() {
            const name = document.getElementById("rouletteSetName").value;

            const response = await fetch(`${rouletteURL}/name/set/${name}`, {
                method: "POST",
                headers: { "Content-Type": "application/json" }
            });

            alert(await response.text());
        }

        async function rouletteSetPlayerMoney() {
            const name = document.getElementById("rouletteSetPlayerMoney").value;
            const amount = document.getElementById("rouletteSetPlayerMoneyAmount").value;

            const response = await fetch(`${rouletteURL}/player/${name}/money/set?amount=${amount}`, {
                method: "POST",
                headers: { "Content-Type": "application/json" }
            });

            alert(await response.text());
        }

        async function rouletteAddPlayerMoney() {
            const name = document.getElementById("rouletteAddPlayerMoney").value;
            const amount = document.getElementById("rouletteAddPlayerMoneyAmount").value;

            const response = await fetch(`${rouletteURL}/player/${name}/money/add?amount=${amount}`, {
                method: "POST",
                headers: { "Content-Type": "application/json" }
            });

            alert(await response.text());
        }

        /*  =======================================================================
        *                                GETTERS
        ======================================================================= */

        async function rouletteGetName() {
            const response = await fetch(`${rouletteURL}/name/get`);

            if (response.ok) {
                const data = await response.text(); // We have to change json format to text for String values
                document.getElementById("rouletteGetNameResults").textContent = `Roulette name: ${data}`;
            } else {
                const errorText = await response.text();
                document.getElementById("rouletteGetNameResults").textContent = `Error: ${errorText}`;
            }
        }

        async function rouletteGetPlayers() {
            const response = await fetch(`${rouletteURL}/players`);
            const data = await response.json();
            document.getElementById("rouletteGetPlayersResults").textContent = JSON.stringify(data, null, 2);
        }

        async function rouletteGetPlayerCount() {
            const response = await fetch(`${rouletteURL}/players/count`);

            if (response.ok) {
                const data = await response.json();
                document.getElementById("rouletteGetPlayerCountResults").textContent = `Player count: ${data}`;
            } else {
                const errorText = await response.text();
                document.getElementById("rouletteGetPlayerCountResults").textContent = `Error: ${errorText}`;
            }
        }

        async function rouletteGetPlayerMoney() {
            const name = document.getElementById("rouletteGetPlayerMoney").value;
            const response = await fetch(`${rouletteURL}/player/${name}/money/get`);

            if (response.ok) {
                const data = await response.json();
                document.getElementById("rouletteGetPlayerMoneyResults").textContent = `Player's money: ${data}`;
            } else {
                const errorText = await response.text();
                document.getElementById("rouletteGetPlayerMoneyResults").textContent = `Error: ${errorText}`;
            }
        }

        async function rouletteGetBetRatio() {
            const betName = document.getElementById("rouletteGetBetRatio").value.trim();
            const response = await fetch(`${rouletteURL}/bet/ratio/get?betName=${encodeURIComponent(betName)}`);

            if (response.ok) {
                const data = await response.json();
                document.getElementById("rouletteGetBetRatioResults").textContent = `Bet: ${betName}, Ratio: ${data}`;
            } else {
                const errorText = await response.text();
                document.getElementById("rouletteGetBetRatioResults").textContent = `Error: ${errorText}`;
            }
        }

        async function rouletteGetSelectedPockets() {
            const response = await fetch(`${rouletteURL}/pockets/get`);

            if (response.ok) {
                const data = await response.json();
                document.getElementById("rouletteGetSelectedPocketsResults").textContent = `Selected pockets: ${data.join(', ')}`;
            } else {
                const errorText = await response.text();
                document.getElementById("rouletteGetSelectedPocketsResults").textContent = `Error: ${errorText}`;
            }
        }

        async function getOutcome() {
            const response = await fetch(`${rouletteURL}/outcome/get`);

            if (response.ok) {
                const outcome = await response.text();
                document.getElementById("rouletteGetOutcomeResults").textContent = `Outcome: ${outcome === "true" ? "Win" : "Loss"}`;
            } else {
                const errorText = await response.text();
                document.getElementById("rouletteGetOutcomeResults").textContent = `Error: ${errorText}`;
            }
        }

        async function rouletteGetPayout() {
            const response = await fetch(`${rouletteURL}/payout/get`);

            if (response.ok) {
                const data = await response.json();
                document.getElementById("rouletteGetPayoutResults").textContent = `Won: ${data}`;
            } else {
                const errorText = await response.text();
                document.getElementById("rouletteGetPayoutResults").textContent = `Error: ${errorText}`;
            }
        }

        async function rouletteGetPocketColor() {
            const number = document.getElementById("rouletteGetPocketColor").value;
            const response = await fetch(`${rouletteURL}/pocket/color/get?number=${number}`);

            if (response.ok) {
                const data = await response.text(); // We have to change json format to text for String values
                document.getElementById("rouletteGetPocketColorResults").textContent = `Pocket color: ${data}`;
            } else {
                const errorText = await response.text();
                document.getElementById("rouletteGetPocketColorResults").textContent = `Error: ${errorText}`;
            }
        }

        async function rouletteGetWinningPocket() {
            const response = await fetch(`${rouletteURL}/pocket/winning/get`);

            if (response.ok) {
                const data = await response.json();
                document.getElementById("rouletteGetWinningPocketResults").textContent = `Winning Pocket: ${data}`;
            } else {
                const errorText = await response.text();
                document.getElementById("rouletteGetWinningPocketResults").textContent = `Error: ${errorText}`;
            }
        }

        async function rouletteGetIfWon() {
            const response = await fetch(`${rouletteURL}/ifWon`);

            if (response.ok) {
                const data = await response.json();
                document.getElementById("rouletteGetIfWonResults").textContent = `Won: ${data}`;
            } else {
                const errorText = await response.text();
                document.getElementById("rouletteGetIfWonResults").textContent = `Error: ${errorText}`;
            }
        }
