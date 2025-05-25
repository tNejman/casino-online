let playerData = [];
let pageNumber = 0;
let currentN = 0;
// bounds both inclusive: [0,4] gets 0,1,2,3,4

async function getNPlayers(n) {
    try {
        const select = document.getElementById('player-select');
        select.innerHTML = '';
        let resultTwoDimList = [];
        let local_n = n;
        do {
            let textN = local_n.toString();
            const response = await fetch(`http://localhost:8081/api/player/admin/getNPlayers/${textN}`, {
                method: "POST",
                headers: { "Content-Type": "application/json" }
            });

            if (!response.ok) {
                throw new Error(`Http error. Error message: ${response.message}`);
            }
            resultTwoDimList = await response.json();
            local_n -= 5;
            pageNumber -=1;
        } while (resultTwoDimList.length === 0 && local_n>=0);
        local_n += 5;
        pageNumber +=1;
        console.log(resultTwoDimList);
        if (typeof resultTwoDimList === "undefined") {
            throw new Error("Fetch getAllPlayers returned undefined");
        }
        playerData = resultTwoDimList;
        let emptyEntries = 5 - playerData.length % 5;
        if (emptyEntries === 5) { emptyEntries = 0; };
        for (emptyEntries; emptyEntries > 0; emptyEntries--) {
            playerData.push(["","","","","","","",""]);
        }
        currentN = local_n;
        document.getElementById("pageNumber").textContent = `Page number: ${pageNumber}`;
        await printPlayers();
        document.getElementById("table").style.display = "block";
        return playerData;

    } catch (e) {
        console.log(`Error fetching players: ${e.message}`);
        return [];
    }
}

async function loadData(){
    document.getElementById(`prev_button`).style.display = "flex";
    document.getElementById(`next_button`).style.display = "flex";
    document.getElementById(`load_button`).style.display = "none";
    await getNPlayers(0);
}

async function getTenPlayers() {
    try {
        const response1 = await fetch(`http://localhost:8081/api/player/admin/getNPlayers/0`, {
            method: "POST",
            headers: { "Content-Type": "application/json" }
        });
        const response2 = await fetch(`http://localhost:8081/api/player/admin/getNPlayers/5`, {
            method: "POST",
            headers: { "Content-Type": "application/json" }
        });
        if (!response1.ok) {
            throw new Error(`Http error. Error message: ${response1.message}`);
        }
        if (!response2.ok) {
            throw new Error(`Http error. Error message: ${response2.message}`);
        }
        const res1 = await response1.json();
        console.log(res1);
        const res2 = await response2.json();
        console.log(res2);
        const sum = res1.concat(res2);
        console.log(sum);
        const labels = sum.map(player => player[1]);
        const balances = sum.map(player => parseInt(player[3]));
        return {
            labels : labels,
            balances : balances
        };

    } catch (e) {
        console.log(`Error fetching players: ${e.message}`);
        return [];
    }
}

async function printPlayers() {
    try {
        if (typeof playerData === "undefined") {
            throw new Error("playerData is undefined");
        }
        if (playerData.length != 5) {
            console.log(`No players to print. Current playerData.lenght: ${playerData.length}`);

            return 0;
        }
        // indexing from 1 is needed
        for (let i = 1; i < 6; i++) {
            let singlePlayerData = playerData[i-1];

            document.getElementById(`id_${i}`).textContent = singlePlayerData[0];
            document.getElementById(`name_${i}`).textContent = singlePlayerData[1];
            document.getElementById(`age_${i}`).textContent = singlePlayerData[2];
            document.getElementById(`moneyAmount_${i}`).textContent = singlePlayerData[3];
            document.getElementById(`loanAmount_${i}`).textContent = singlePlayerData[4];
            document.getElementById(`email_${i}`).textContent = singlePlayerData[5];
            document.getElementById(`accountCreationDate_${i}`).textContent = singlePlayerData[6].substring(0, 19);
            document.getElementById(`accountModifiedDate_${i}`).textContent = singlePlayerData[7].substring(0, 19);          
            if(singlePlayerData[0] != "") document.getElementById (`delete_${i}`).style.display = "block";
            else document.getElementById (`delete_${i}`).style.display = "none";
            if(singlePlayerData[0] != "") populatePlayerList(singlePlayerData[1]);
        }
    } catch (e) {
        console.error("Error printing players:", e.message);
        return;
    }
}

async function setPage(which) {
    console.log(`Setting page: ${which}, currentN: ${currentN}`);
    if (which !== "next" && which !== "prev") {
        console.log(`When setting page expected 'next' or 'prev' but got '${which}' instead`);
        return -1;
    } else if (which === "next") {
        await getNPlayers(currentN + 5); 
        if (playerData.length != 5 || playerData[0][0] === " ") {
            console.log("Already on the last page");
            currentN -= 5;
            return -1;
        }
        pageNumber++;
        // currentN += 5;
        await printPlayers();
        document.getElementById("pageNumber").textContent = `Page number: ${pageNumber}`;

        return 0;
    } else if (which === "prev") {
        if (pageNumber <= 0) {
            console.log("Already on the first page");
            return -1;
        }
        await getNPlayers(currentN - 5);
        pageNumber--;
        // currentN -= 5;
        await printPlayers();
        document.getElementById("pageNumber").textContent = `Page number: ${pageNumber}`;

        return 0;
    } else {
        console.log("Unexpected error setting page");
        return -1;
    }
}

// async function gotoPage(num) {
//     if (num < 0) {
//         console.log("Cannot goto negative page");
//         return -1;
//     } else if (num > maxPage) {
//         console.log("Cannot goto page beyond maximum");
//         return -1;
//     } else if (!Number.isInteger(num)) {
//         console.log("Page number must be an integer");
//         return -1;
//     } else if (typeof playerData === "undefined") {
//         console.log("Cannot set page before loading in players");
//         return -1;
//     }
//     currentN -= 5;
//     pageNumber = num;
//     printPlayers();
//     document.getElementById("pageNumber").textContent =  `Page number: ${pageNumber}`;
//     return 0;
// }


// Add loan
// Set player money
async function deletePlayer(n) {
    try {
        let PlayerLogin = playerData[n-1][1];
        const response = await fetch(`http://localhost:8081/api/player/${PlayerLogin}/delete`, {
            method: "DELETE",
            headers: { "Content-Type": "class java.lang.Integer" }
        });
        if (!response.ok) {
            throw new Error(`Error fetching delete player! status: ${response.status}`);
        }
        console.log("Player deleted successfully");
        getAllPlayers();
        return 0;
    } catch (e) {
        await getNPlayers(currentN);
        console.log("Deleted player");
        return 0;
    }
}

async function addPlayerMoney(playerLogin, amount) {
    try {
        const response = await fetch(`http://localhost:8081/api/player/${playerLogin}/money/add/${amount}`, {
            method: "POST",
            headers: { "Content-Type": "application/json" }
        });
        if (!response.ok) {
            throw new Error(`Error fetching add money! status: ${response.status}`);
        }
        console.log("Money added successfully");
        await getNPlayers(currentN);
        return 0;

    } catch (e) {
        console.error("Error adding money:", e.message);
        return -1;
    }
}

async function setPlayerMoney(playerLogin, amount) {
    try {
        const response = await fetch(`http://localhost:8081/api/player/${playerLogin}/money/set/${amount}`, {
            method: "POST",
            headers: { "Content-Type": "application/json" }
        });
        if (!response.ok) {
            throw new Error(`Error fetching set money! status: ${response.status}`);
        }
        console.log("Money set successfully");
        await getNPlayers(currentN);
        return 0;

    } catch (e) {
        console.error("Error setting money:", e.message);
        return -1;
    }
}

async function updatePlayerBalance() {
    const name = document.getElementById("player-select").value;
    const balance = document.getElementById("amount-input").value;
    addPlayerMoney(name, balance);
}


let pieChartInstance;

// charts

// Funkcja do rysowania wykresu
const drawPieChart = async () => {
    const chartData = await getTenPlayers();
    if(!chartData) return;

    const canvas = document.getElementById('chart1');
    if (!canvas) {
        console.error('no <canvas>, id="chart1"');
        return;
    }

    if (pieChartInstance) {
        pieChartInstance.destroy();
    }

    const ctx = canvas.getContext('2d');

    pieChartInstance = new Chart(ctx, {
        type: 'pie',
        data: {
            labels: chartData.labels,
            datasets: [{
                data: chartData.balances,
            }]
        },
        options: {
            title: {
              display: true,
              text: "Players' Money"
            }
          }
    });
}

const fetchPlayers = async () => {
    try {
        const response = await fetch('http://localhost:8081/api/player/getAllPlayers', {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            },
        });

        if (!response.ok) {
            const errorMessage = await response.json();
            console.error(`Error getting players: ${errorMessage}`);
            return;
        }

        const data = await response.json();
        console.log("API response data:", data);

        if (Array.isArray(data)) {
            console.log("First player data:", data[0]);
        }

        const labels = data.map(player => player[0]);
        const balances = data.map(player => parseInt(player[1]));

        console.log("Labels:", labels);
        console.log("Balances:", balances);

        return {
            labels: labels,
            balances: balances
        };

    } catch (error) {
        console.error("Error with fetching API:", error);
    }
};


// to be used
const fetchPlayerCountOverTime = async () => {
    try {
        const response = await fetch('http://localhost:8081/api/player/admin/getPlayerCountOverTime', {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
        });

        if (!response.ok) {
            const errorMessage = await response.json();
            console.error(`Error getting player count: ${errorMessage}`);
            return;
        }

        const data = await response.json();
        console.log("Player count data:", data);
        return data;

    } catch (error) {
        console.error("Error with fetching API:", error);
    }
};

let lineChartInstance;

const drawLineChart = async () => {
    const chartData = await fetchPlayerCountOverTime();
    
    if (!chartData) return;
    const ctx2 = document.getElementById('chart2').getContext('2d');
    const labels = chartData.map(entry => entry[0].split(' ')[0]);
    const playerCounts = chartData.map(entry => entry[1]);
    console.log(chartData);
    console.log(labels);
    console.log(playerCounts);
    if (lineChartInstance) {
        lineChartInstance.destroy();
    }

    lineChartInstance = new Chart(ctx2, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [{
                label: 'Liczba graczy',
                data: playerCounts,
                borderColor: '#36A2EB',
                backgroundColor: 'rgba(54, 162, 235, 0.2)',
                borderWidth: 2,
                tension: 0.4
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'top',
                }
            },
            scales: {
                x: {
                    title: {
                        display: true,
                        text: 'Data',
                    },
                },
                y: {
                    title: {
                        display: true,
                        text: 'Liczba graczy',
                    },
                    beginAtZero: true,
                }
            }
        }
    });
};

// account management

function populatePlayerList(playerName) {
    const select = document.getElementById('player-select');
    const option = document.createElement('option');
    option.value = playerName;
    option.textContent = `${playerName}`;
    const exists = Array.from(select.options).some(option => option.value === playerName);
    if(!exists) select.appendChild(option);
}