const playerURL = "http://localhost:8081/api/player";

        // PLAYER INIT

        async function playerCreate() {
            const name = document.getElementById("playerNameCreate").value;
            const password = document.getElementById("playerPasswordCreate").value;
            const email = document.getElementById("playerEmailCreate").value;
            const age = document.getElementById("playerAgeCreate").value;
            const response = await fetch(`${playerURL}/create`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ 
                    name: name,
                    password: password,
                    email: email,
                    age: age
                 }),
            });
            alert(await response.text());
        }

        // Log in
        
        async function playerLogin() {
            const login = document.getElementById("playerLogin").value;
            const password = document.getElementById("playerPassword").value;
            const response = await fetch(`${playerURL}/login`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ 
                    login: login,
                    password: password
                 }),

            });
            alert(await response.text());
        }

        // add money

        async function playerAddMoney() {
            const name = document.getElementById("playerMoneyAdd").value;
            const amount = document.getElementById("playerMoneyAddValue").value;

            const response = await fetch(`${playerURL}/${name}/money/add/${amount}`, {
                method: "POST",
                headers: { "Content-Type": "application/json" }
            });

            alert(await response.text());
        }

        /*  =======================================================================
       *                                SETTERS
        ======================================================================= */

        async function playerSetName() {
            const currentName = document.getElementById("playerGetCurrentName").value;
            const newName = document.getElementById("playerSetNewName").value;
            const response = await fetch(`${playerURL}/${currentName}/name/set/${newName}`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ name: newName }),
            });
            alert(await response.text());
        }

        async function playerSetMoney() {
            const name = document.getElementById("playerNameMoney").value;
            const amount = document.getElementById("playerMoney").value;

            const response = await fetch(`${playerURL}/${name}/money/set?amount=${amount}`, {
                method: "POST",
                headers: { "Content-Type": "application/json" }
            });

            alert(await response.text());
        }

        async function setIfInGame() {
            const name = document.getElementById("setIfInGameName").value.trim();
            const ifInGame = document.getElementById("setIfInGameStatus").value;

            const response = await fetch(`${playerURL}/${encodeURIComponent(name)}/in-game/set?ifInGame=${ifInGame}`, {
                method: "POST",
                headers: { "Content-Type": "application/json" }
            });

            const resultText = await response.text();
            document.getElementById("setIfInGameResults").textContent = response.ok ? `Success: ${resultText}` : `Error: ${resultText}`;
        }

        async function setIfReady() {
            const name = document.getElementById("setIfReadyName").value.trim();
            const ifReady = document.getElementById("setIfReadyStatus").value;

            const response = await fetch(`${playerURL}/${encodeURIComponent(name)}/ready/set?ifReady=${ifReady}`, {
                method: "POST",
                headers: { "Content-Type": "application/json" }
            });

            const resultText = await response.text();
            document.getElementById("setIfReadyResults").textContent = response.ok ? `Success: ${resultText}` : `Error: ${resultText}`;
        }

        /*  =======================================================================
        *                                GETTERS
        ======================================================================= */

        async function playerGet() {
            const name = document.getElementById("playerGetName").value;
            const response = await fetch(`${playerURL}/${name}`);
            const data = await response.json();
            document.getElementById("playerData").textContent = JSON.stringify(data, null, 2);
        }

        async function playerGetMoney() {
            const name = document.getElementById("playerGetMoney").value;
            const response = await fetch(`${playerURL}/${name}/money/get`);

            if (response.ok) {
                const data = await response.json();
                document.getElementById("playerMoneyGet").textContent = `Player's money: ${data}`;
            } else {
                const errorText = await response.text();
                document.getElementById("playerMoneyGet").textContent = `Error: ${errorText}`;
            }
        }

        async function playerGetHand() {
            const name = document.getElementById("playerGetHand").value;
            const response = await fetch(`${playerURL}/${name}/hand/get`);

            if (response.ok) {
                const data = await response.json();
                document.getElementById("HandResults").textContent = `Player's hand: ${data}`;
            } else {
                const errorText = await response.text();
                document.getElementById("HandResults").textContent = `Error: ${errorText}`;
            }
        }

        async function playerGetHandSize() {
            const name = document.getElementById("playerGetHandSize").value;
            const response = await fetch(`${playerURL}/${name}/hand/get/size`);

            if (response.ok) {
                const data = await response.json();
                document.getElementById("HandSizeResults").textContent = `Player's hand size: ${data}`;
            } else {
                const errorText = await response.text();
                document.getElementById("HandSizeResults").textContent = `Error: ${errorText}`;
            }
        }

        async function playerIsHandEmpty() {
            const name = document.getElementById("playerIsHandEmpty").value;
            const response = await fetch(`${playerURL}/${name}/isHandEmpty`);

            if (response.ok) {
                const data = await response.json();
                document.getElementById("IsHandEmptyResults").textContent = `Is Hand Empty?: ${data}`;
            } else {
                const errorText = await response.text();
                document.getElementById("IsHandEmptyResults").textContent = `Error: ${errorText}`;
            }
        }

        async function playerIsInGame() {
            const name = document.getElementById("playerIsInGame").value;
            const response = await fetch(`${playerURL}/${name}/isInGame`);

            if (response.ok) {
                const data = await response.json();
                document.getElementById("IsInGameResults").textContent = `Is In Game?: ${data}`;
            } else {
                const errorText = await response.text();
                document.getElementById("IsInGameResults").textContent = `Error: ${errorText}`;
            }
        }

        async function playerIsReady() {
            const name = document.getElementById("playerIsReady").value;
            const response = await fetch(`${playerURL}/${name}/isReady`);

            if (response.ok) {
                const data = await response.json();
                document.getElementById("IsReadyResults").textContent = `Is In Ready?: ${data}`;
            } else {
                const errorText = await response.text();
                document.getElementById("IsReadyResults").textContent = `Error: ${errorText}`;
            }
        }

        async function joinGame() {
            const name = document.getElementById("gamePlayerName").value;
            const gameType = document.getElementById("gameType").value;
            const response = await fetch(`${playerURL}/${name}/joinGame?gameType=${gameType}`, { method: "POST" });
            alert(await response.text());
        }

        async function leaveGame() {
            const name = document.getElementById("leavePlayerName").value;
            const response = await fetch(`${playerURL}/${name}/leaveGame`, { method: "POST" });
            alert(await response.text());
        }

        async function getPlayerHand() {
            const name = document.getElementById("getPlayerHand").value;
            const response = await fetch(`${playerURL}/${name}/hand/get`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ name: name }),
            });
            alert(await response.text());
        }