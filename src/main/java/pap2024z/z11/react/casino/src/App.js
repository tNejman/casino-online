import React, { useState } from "react";
import "./App.css";
import Roulette from "./components/RouletteComponents/Roulette/Roulette";
import Navbar from "./components/PageComponents/Navbar/Navbar";
import GameList from "./components/PageComponents/SideBars/GameList";
import Leaderboard from "./components/PageComponents/SideBars/Leaderboard";
import AuthForm from "./components/Auth/AuthForm";

const App = () => {
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [isCodeVerified, setIsCodeVerified] = useState(false);
    const [isResetPassword, setIsResetPassword] = useState(false);
    const [currentUser, setCurrentUser] = useState(null); // ({name, email, money})
    const [currentGame, setCurrentGame] = useState("Roulette");
    const [currentView, setCurrentView] = useState("Home");
    const [authCode, setAuthCode] = useState("");
    const [codeInput, setCodeInput] = useState("");
    const [isLoginMode, setIsLoginMode] = useState(true);

    const updateMoney = (newMoney) => {
        setCurrentUser(prevState => ({
            ...prevState,   // zachowuje poprzednie właściwości
            money: newMoney // aktualizuje tylko pole 'money'
        }));
    };

    const updateName = (newName) => {
        setCurrentUser(prevState => ({
            ...prevState,   // zachowuje poprzednie właściwości
            username: newName // aktualizuje tylko pole 'money'
        }));
    };

    const updateEmail = (newEmail) => {
        setCurrentUser(prevState => ({
            ...prevState,   // zachowuje poprzednie właściwości
            email: newEmail // aktualizuje tylko pole 'money'
        }));
    };

    const getMoney = async (name) => {
        try {
            const response = await fetch(`http://localhost:8081/api/player/${name}/money/get`, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                },
            });
            if (!response.ok) {
                const errorMessage = await response.text();
                console.error(`Błąd pobierania pieniędzy: ${errorMessage}`);
                alert("Nie udało się pobrać informacji o saldzie.");
                return;
            }
    
            const money = await response.text();
            console.log(`Pieniądze gracza ${name}: ${money}`);
    
            return money;
        } catch (error) {
            console.error("Błąd podczas wykonywania zapytania:", error);
            alert("Wystąpił problem z pobieraniem salda.");
        }
    };

    const setMoney = async (name, amount) => {
        try {
            const response = await fetch(`http://localhost:8081/api/player/${name}/money/set/${amount}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
            });
            if (!response.ok) {
                const errorMessage = await response.text();
                console.error(`Błąd ustawiania pieniędzy: ${errorMessage}`);
                alert("Nie udało się zaktualizować salda.");
                return;
            }
            const result = await response.text();
            updateMoney(result);
            
        } catch (error) {
            console.error("Błąd podczas wykonywania zapytania:", error);
            alert("Wystąpił problem z ustawieniem salda.");
        }
    };

    const sendAuthenticationEmail = async (sessionId) => {
        try {
            const response = await fetch(`http://localhost:8081/api/player/sendAuthenticationEmail/${sessionId}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
            });
            if (!response.ok) {
                const errorMessage = await response.text();
                console.error(`Error fetching sessionId: ${errorMessage}`);
                alert("Error fetching sessionId.");
                return;
            }
            
        } catch (error) {
            console.error("Couldn't fetch sessionId:", error);
            alert("Couldn't fetch sessionId.");
        }
    };


    const getEmail = async (name) => {
        try {
            const response = await fetch(`http://localhost:8081/api/player/${name}/email/get`, {
                method: "GET", 
                headers: {
                    "Content-Type": "application/json",
                },
            });
    
            if (!response.ok) {
                const errorMessage = await response.text();
                console.error(`Error getting email: ${errorMessage}`);
                alert("Couldn't get email.");
                return;
            }
    
            const email = await response.text();
            console.log(`Email of player ${name}: ${email}`);
    
            return email;
        } catch (error) {
            console.error("Error with getting API:", error);
            alert("Error with getting API.");
        }
    };

    const addMoney = async (name, amount) => {
        try {
            const response = await fetch(`http://localhost:8081/api/player/${name}/money/add/${amount}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
            });
            if (!response.ok) {
                const errorMessage = await response.text();
                console.error(`Błąd ustawiania pieniędzy: ${errorMessage}`);
                alert("Nie udało się zaktualizować salda.");
                return;
            }
            const result = await response.text();
            updateMoney(result);
            
        } catch (error) {
            console.error("Błąd podczas wykonywania zapytania:", error);
            alert("Wystąpił problem z ustawieniem salda.");
        }
    };

    const endSession = async () => {
        try {
            const response = await fetch(`http://localhost:8081/api/player/endSession/${authCode}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
            });
            if (!response.ok) {
                const errorMessage = await response.text();
                console.error(`Błąd zakończenia sesji: ${errorMessage}`);
                alert("Nie udało się zakończyć sesji.");
                return;
            }
            
            const result = await response.text();
    
            if (result === -1) {
                alert("Wystąpił problem z zakończeniem sesji. Spróbuj ponownie.");
                console.log(result);
            } else {
                alert("Sesja została zakończona pomyślnie.");
                handleBackToAuth();
            }
        } catch (error) {
            console.error("Błąd podczas wykonywania żądania zakończenia sesji:", error);
            alert("Wystąpił problem z zakończeniem sesji.");
        }
    };

    const handleLogin = async ({ username, password }) => {
        try {
            const requestBody = {
                login: username,
                password: password,
            };
            const response = await fetch("http://localhost:8081/api/player/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(requestBody),
            });
    
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            const sessionId = await response.json();
            console.log(sessionId);
            if (sessionId > 0) {
                setAuthCode(sessionId);
                setIsAuthenticated(true);
                let money = await getMoney(username);
                let email = await getEmail(username);
                setCurrentUser({ username, email, money });
                sendAuthenticationEmail(sessionId);
            } else {
                alert("Niepoprawne dane logowania");
            }
        } catch (error) {
            console.error("Błąd logowania:", error);
            alert("Wystąpił problem podczas logowania.");
        }
    };

    const handleRegister = async ({ email, username, password, age }) => {
        try {
            const requestBody = {
                name: username,
                password: password,
                email: email,
                age: age,
            };
            const response = await fetch("http://localhost:8081/api/player/create", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(requestBody),
            });
            if (!response.ok) {
                const errorMessage = await response.text();
                alert(`Rejestracja nie powiodła się: ${errorMessage}`);
                return;
            }
            const successMessage = await response.text();
            alert(successMessage);
            handleBackToAuth();
            setIsLoginMode(true);
        } catch (error) {
            console.error("Błąd podczas rejestracji:", error);
            alert("Wystąpił problem podczas rejestracji.");
        }
    };
    
    const handleResetPassword = async (email) => {
        if (!email) {
            alert("Proszę podać adres e-mail.");
            return;
        }
        try {
            const response = await fetch(`http://localhost:8081/api/player/sendResetPasswordEmail/${email}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
            });
            if (!response.ok) {
                const errorMessage = await response.text();
                alert(`Nie udało się zresetować hasła: ${errorMessage}`);
                return;
            }
            alert(`Nowe hasło zostało wysłane na adres e-mail: ${email}`);

        } catch (error) {
            console.error("Błąd podczas resetowania hasła:", error);
            alert("Wystąpił problem podczas resetowania hasła.");
        }
    };      

    const verifyCode = (e) => {
        if (codeInput === authCode.toString()) {
            setIsCodeVerified(true);
        } else {
            alert("Nieprawidłowy kod. Spróbuj ponownie.");
        }
        return;
    };

    const handleBackToAuth = () => {
        setIsAuthenticated(false);
        setIsCodeVerified(false);
        setCurrentUser(null);
        setAuthCode("");
    };

    const handleGoBack = () => {
        endSession(authCode);
        handleBackToAuth();
    };

    if (!isAuthenticated) {
        return <AuthForm onLogin={handleLogin} onRegister={handleRegister} onResetPassword={handleResetPassword} isLoginMode={isLoginMode} setIsLoginMode={setIsLoginMode}/>;
    }

    if (!isCodeVerified) {
        return (
            <div className="cont">
                <div className="verification">
                    <h2>Enter veryfication code</h2>
                    <form onSubmit={verifyCode} className="centrujforma">
                        <input
                            type="text"
                            placeholder="Your code is on your email"
                            value={codeInput}
                            onChange={(e) => setCodeInput(e.target.value)}
                            required
                        />
                        <button type="submit">SUBMIT</button>
                    </form>
                    <button onClick={handleGoBack}>Go back</button>
                </div>
                
            </div>
        );
    }

    return (
        <div className="app">
            <Navbar setCurrentView={setCurrentView} currentUser={currentUser} addMoney={addMoney} setIsAuthenticated={setIsAuthenticated} getMoney={getMoney} endSession={endSession} updateMoney={updateMoney}/>
            <div className="main-content">
                {currentView === "Home" ? (
                    <>
                        <GameList setGame={setCurrentGame} />
                        <GameArea currentGame={currentGame} currentUser={currentUser} setMoney={setMoney} getMoney={getMoney} updateMoney={updateMoney}/>
                        <Leaderboard />
                    </>
                ) : currentView === "AccountOverview" ? (
                    <AccountOverview  currentUser={currentUser} addMoney={addMoney} />
                ) : currentView === "Settings" ? (
                    <Settings  
                        currentUser={currentUser} 
                        updateName={updateName}
                        updateEmail={updateEmail}
                    />
                ) : null}
            </div>
        </div>
    );
};

const AccountOverview = ({ currentUser, addMoney }) => (
    <div className="account-menu">
        <h2>Account Overview</h2>
        <p><strong>Name:</strong> {currentUser.username}</p>
        <p><strong>E-mail:</strong> {currentUser.email}</p>
        <p className="balance"><strong>Balance:</strong> {currentUser.money} $</p>
        <button className="add-money-btn" onClick={() => addMoney(currentUser.username, "1000")}>+</button>
    </div>
);

const Settings = ({ currentUser, updateName, updateEmail}) => {
    const [email, setEmail] = useState(currentUser.email); // stan dla email
    const [new_password, setNewPassword] = useState(""); // stan dla nowego hasła
    const [old_password, setOldPassword] = useState(""); // stan dla starego hasła
    const [name, setName] = useState(currentUser.username); // stan dla username

    const handleSaveName = async (newName) => {
        try {
            const response = await fetch(`http://localhost:8081/api/player/${currentUser.username}/name/set/${newName}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
            });
            if (!response.ok) {
                const errorMessage = await response.text();
                console.error(`Error changing name: ${errorMessage}`);
                alert("Couldn't change name.");
                return;
            }
            alert(`Name has been changed!`);
            updateName(newName);
            
        } catch (error) {
            console.error("Error:", error);
            alert("Error requesting data.");
        }
    };

    const handleSaveEmail = async (newEmail) => {
        try {
            const response = await fetch(`http://localhost:8081/api/player/${currentUser.username}/email/set/${newEmail}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
            });
            if (!response.ok) {
                const errorMessage = await response.text();
                console.error(`Error changing email: ${errorMessage}`);
                alert("Couldn't change email.");
                return;
            }
            const result = await response.text();
            if (result) {
                alert(`Email has been changed!`);
                updateEmail(newEmail);
            } else {
                alert("Couldn't change email.");
            }
        } catch (error) {
            console.error("Error:", error);
            alert("Error requesting data.");
        }
    };

    const handleSavePassword = async (old_password, new_password) => {
        try {
            const response = await fetch(`http://localhost:8081/api/player/${currentUser.username}/password/set/${old_password}/${new_password}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
            });
            if (!response.ok) {
                const errorMessage = await response.text();
                console.error(`Error changing password: ${errorMessage}`);
                alert("Couldn't change password.");
                return;
            }
            const result = await response.text();
            if (result) {
                alert(`Password has been changed!`);
            } else {
                alert("Couldn't change password.");
            }
        } catch (error) {
            console.error("Error:", error);
            alert("Error requesting data.");
        }
    };

    return (
        <div className="settings">
            <h2>Account Settings</h2>
            <div>
                <label>Name:</label>
                <input
                    type="text"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                />
            </div>
            <button className="settings-button" onClick={() => handleSaveName(name)}>SAVE NAME</button>
            <div>
                <label>E-mail:</label>
                <input
                    type="text"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                />
            </div>
            <button className="settings-button" onClick={() => handleSaveEmail(email)}>SAVE EMAIL</button>
            <div>
                <label>Old Password:</label>
                <input
                    type="password"
                    value={old_password}
                    onChange={(e) => setOldPassword(e.target.value)}
                />
            </div>
            <div>
                <label>New Password:</label>
                <input
                    type="password"
                    value={new_password}
                    onChange={(e) => setNewPassword(e.target.value)}
                />
            </div>
            <button className="settings-button" onClick={() => handleSavePassword(old_password, new_password)}>SAVE PASSWORD</button>
        </div>
    );
};

const GameArea = ({ currentGame, currentUser, setMoney, getMoney, updateMoney }) => (
    <div className="game-area">
        <h2>{currentGame}</h2>
        {currentGame === "Roulette" ? (
            <Roulette currentUser={currentUser} setMoney={setMoney} getMoney={getMoney} updateMoney={updateMoney}/>
        ) : currentGame === "Blackjack" ? (
            <p>Placeholder</p>
        ) : currentGame === "Poker" ? (
            <p>Placeholder</p>
        ) : currentGame === "Slots" ? (
            <p>Placeholder</p>
        ) : null}
    </div>
);

export default App;
