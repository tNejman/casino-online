import React, { Component } from 'react';
import BalancePanel from './BalancePanel/BalancePanel'
import './BettingPanel.css';

/**
 * Class holding betting panel
 * 
 * @param {object} props - (spin, complete, chosen_number, disabledBool, btn_dis_style, black_numbers)
 * @return {JSX} - div
 */
class BettingPanel extends Component {
    state = {
        bet: 0,
        error: "",
        disable: true,
        currentUser: this.props.currentUser,
    }
    
    updateMoney = (newMoney) => {
        console.log("updated money to: ", newMoney);
        this.setState((prevState) => ({
            currentUser: {
                ...prevState.currentUser,
                money: newMoney,
            },
        }));
    };

    // used to handle the click event for betting buttons
    checkBet = (event) => {
        if(event.target.value > parseInt(this.state.currentUser.money)){
            this.setState({error: "You cannot bet more than your balance.",disable: true, bet:0})
        } else if(event.target.value <= 0){
            this.setState({ error: "You cannot bet less than 0.",disable: true, bet:0 })
        } else{
            this.setState({ bet: event.target.value, error: "", disable: false })
        }
    }

    // used to get the colour from a winning number
    // getWinningColour = (win) => {
    //     let black_numbers = this.props.black_numbers.map(String);
    //     let colour="";
    //     if (black_numbers.includes(win.toString())) colour = 'Black';
    //     else if (win.toString() === "0") colour = 'Green';
    //     else colour = 'Red';
    //     return colour
    // }

    async setSelectedPocket() {
        try {
            const response = await fetch(`http://localhost:8081/api/roulette/pockets/set`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
            });
            if (!response.ok) {
                const errorMessage = await response.text();
                console.error(`Error fetching pockets (badResponse): ${errorMessage}`);
                alert("Error fetching pockets (badResponse)");
                return;
            }
            console.log(`Successfully fetched pockets`);
        } catch (error) {
            console.error("Couldn't fetch pockets:", error);
            alert("Couldn't fetch pockets.");
        }
    };

    async setBetAmount(bet) {
        try {
            const response = await fetch(`http://localhost:8081/api/roulette/bet/amount/set/${bet}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
            });
            if (!response.ok) {
                const errorMessage = await response.text();
                console.error(`Error fetching bet (badResponse): ${errorMessage}`);
                alert("Error fetching bet (badResponse)");
                return;
            }
            
            const pre_winning = await this.props.getMoney(this.state.currentUser.username);
            console.log(`Successfully fetched bet, pre_winnings: `, pre_winning);
            this.props.updateMoney(pre_winning);
        } catch (error) {
            console.error("Couldn't fetch bet:", error);
            alert("Couldn't fetch bet.");
        }
    };

    async roulettePlaceBet(color) {
        console.log("roulettePlace start");
        try {
            const response = await fetch(`http://localhost:8081/api/roulette/bet/place/${color}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
            });
            if (!response.ok) {
                const errorMessage = await response.text();
                console.error(`Error fetching color (badResponse): ${errorMessage}`);
                alert("Error fetching color (badResponse)");
                return;
            }
            console.log(`Successfully fetched color`);
            console.log("Response: ",response.text());
        } catch (error) {
            console.error("Couldn't fetch color:", error);
            alert("Couldn't fetch color.");
        }
    };

    async getPlayerMoney() {
        console.log("getPlayerMoney start");
        try {
            const response = await fetch(`http://localhost:8081/api/roulette/player/${this.state.currentUser.username}/getPayout`, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                },
            });
            if (!response.ok) {
                const errorMessage = await response.text();
                console.error(`Error fetching money (badResponse): ${errorMessage}`);
                alert("Error fetching money (badResponse)");
                return;
            }
            const winnings = await response.json();
            const newMoney = await this.props.getMoney(this.state.currentUser.username);
            this.props.updateMoney(newMoney);
            this.updateMoney(newMoney);
            console.log(`Successfully fetched money, winnings: ${winnings}, newMoney: ${newMoney}`);
        } catch (error) {
            console.error("Couldn't fetch money:", error);
            alert("Couldn't fetch money.");
        }
    };



    // used to handle a bet - update balance
    betHandler = (color) => {
        // 1. wysylamy wybor (setSelectedPocket)
        // 2. wysylamy bet (setBetAmount)
        // 3. wysylamy potwierdzenie zakladu (roulettePlaceBet)
        // 4. dostajemy wygrane (getPlayerMoney [Roulette Class]) -- wygrane lub przegrane
        setTimeout(() => {
            this.setSelectedPocket();
            console.log("Bet passed: ", this.state.bet);
            this.setBetAmount(this.state.bet);
            console.log("Color passed: ", color);
            this.roulettePlaceBet(color);
            setTimeout(() => {
                let winning_colour = this.props.get_color();
                console.log("Winning color is: ", winning_colour);
                this.getPlayerMoney();
            }, 8500)
        }, 500);
    }
    
    // used for holding buttons and current bet/balance
    clickHandle = (choice) => {
        if (this.state.error === "") {
            this.props.spin();
            this.setState({disable: true});
            this.betHandler(choice);
        }

    }

    render() {
        let error = null;
        if(this.state.error){
            error = (
                <p className={"error"}>{this.state.error}</p>
            )
        }
        let buttons = null;
        if(!this.state.disable){
            buttons = (
                <div className={"betButtons"}>
                    <button className={"redBet"} style={this.props.btn_dis_style} disabled={this.state.disable} onClick={this.clickHandle.bind(this, 'Red')}>Bet Red (X2)</button>
                    <button className={"greenBet"} style={this.props.btn_dis_style} disabled={this.state.disable} onClick={this.clickHandle.bind(this, 'Green')}>Bet Green (X14)</button>
                    <button className={"blackBet"} style={this.props.btn_dis_style} disabled={this.state.disable} onClick={this.clickHandle.bind(this, 'Black')}>Bet Black (X2)</button>
                </div>
            )
        }
        return (
            <div className={"bettingPanel"}>
                <h1>Place Your bets!</h1>
                <BalancePanel>{this.state.bet.toLocaleString()}</BalancePanel>
                <input className={"betInput"} type="number" placeholder="Bet..." onChange={this.checkBet} />
                {error}
                {buttons}
            </div>
        )
    }

}

export default BettingPanel
