import React, { useState } from "react";
import "./AuthForm.css";

const AuthForm = ({ onLogin, onRegister, onResetPassword, isLoginMode, setIsLoginMode }) => {
    const [isResetPasswordMode, setIsResetPasswordMode] = useState(false);
    const [formData, setFormData] = useState({
        email: "",
        username: "",
        password: "",
        age: "",
    });

    const toggleMode = () => {
        setIsLoginMode((prevMode) => !prevMode);
        setIsResetPasswordMode(false);
        setFormData({ email: "", username: "", password: "" }); // clear form
    };

    const toggleResetPasswordMode = () => {
        setIsResetPasswordMode((prevMode) => !prevMode);
        setFormData({ email: "" }); // clear form
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prevData) => ({
            ...prevData,
            [name]: value,
        }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        if (isResetPasswordMode) {
            onResetPassword(formData.email);
            toggleResetPasswordMode();
        } else if (isLoginMode) {
            onLogin({ username: formData.username, password: formData.password });
        } else {
            onRegister(formData);
        }
    };

    return (
        <div className="cont">
            <div className="auth-form">
                <a className="gov-link" href="https://sip.lex.pl/akty-prawne/dzu-dziennik-ustaw/kodeks-karny-skarbowy-16852901/art-107" target="_blank" rel="noreferrer">Why should you love gambling?</a>
                <h1 className="title">CASINO MEGA GIGA WIN</h1>
                <h2>{isResetPasswordMode ? "Reset password" : isLoginMode ? "Login" : "Sign in"}</h2>
                <form onSubmit={handleSubmit}>
                    {isResetPasswordMode ? (
                        <>
                            <input
                                type="email"
                                name="email"
                                placeholder="Your e-mail"
                                value={formData.email}
                                onChange={handleChange}
                                required
                            />
                            <button type="submit">Send link</button>
                        </>
                    ) : (
                        <>
                            <input
                                type="text"
                                name="username"
                                placeholder="Login"
                                value={formData.username}
                                onChange={handleChange}
                                required
                            />
                            <input
                                type="password"
                                name="password"
                                placeholder="Password"
                                value={formData.password}
                                onChange={handleChange}
                                required
                            />
                            {!isLoginMode && (
                                <>
                                    <input
                                        type="email"
                                        name="email"
                                        placeholder="Email"
                                        value={formData.email}
                                        onChange={handleChange}
                                        required
                                    />
                                    <input
                                        type="text"
                                        name="age"
                                        placeholder="Age"
                                        value={formData.age}
                                        onChange={handleChange}
                                        required
                                    />
                                </>
                            )}
                            <button type="submit">{isLoginMode ? "Log in" : "Sign in"}</button>
                        </>
                    )}
                </form>
                <div className="wrapper">
                    {!isResetPasswordMode ? (
                        <>
                            {isLoginMode ? (
                                <div style={{ marginTop: "5px" }}>
                                    No account?{" "}
                                    <span className="toggle-link" onClick={toggleMode}>
                                        Sign in now!
                                    </span>
                                    <div style={{ marginBottom: "5px" }}></div>
                                    Forgot password?{" "}
                                    <span className="toggle-link" onClick={toggleResetPasswordMode}>
                                        Reset it
                                    </span>
                                </div>
                            ) : (
                                <div>
                                    Already have account?{" "}
                                    <span className="toggle-link" onClick={toggleMode}>
                                        Log in!
                                    </span>
                                </div>
                            )}
                        </>
                    ) : (
                        <div style={{ marginTop: "5px" }}>
                            <span className="toggle-link" onClick={toggleResetPasswordMode}>
                                Go back to log in
                            </span>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
};

export default AuthForm;
