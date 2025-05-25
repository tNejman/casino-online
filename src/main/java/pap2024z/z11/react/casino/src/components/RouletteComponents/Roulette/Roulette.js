import React, { Component } from 'react';
import './Roulette.css';
import Board from '../Board/Board';
import LastSpinResult from '../LastSpinResult/LastSpinResult';
import BettingPanel from '../BettingPanel/BettingPanel';

class Roulette extends Component {
    state = {
        spin: false,
        spin_complete: false,
        btn_disable: true,
        currentUser: this.props.currentUser,
        last_win: null, // number to pass to LastSpinResult
        spinResult: null, // number from API
        last_color: null, // color to pass to LastSpinResult
        colorResult: null, // color from API
        offset: null,
    }

    create_obj() {
        let obj = {};
        for (let i = 0; i < 37; i++) {
            if (i === 0) {
                obj[0] = [5600, 5650];
            } else {
                let last = obj[i - 1][1];
                obj[i] = [Math.round((last + 5) * 100) / 100, Math.round((last + 56) * 100) / 100];
            }

        };
        return obj;
    }

    async fetch_spin() {
        const apiUrl = 'http://localhost:8081/api/roulette/spin';
    
        try {
            const response = await fetch(apiUrl, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
            });
    
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
    
            const data = await response.text();
            console.log('API Response:', data);
    
            this.setState({ spinResult: data });
            return data;
        } catch (error) {
            console.error('Error fetching roulette spin:', error);
        }
    }

    async fetch_color() {
        const apiUrl = 'http://localhost:8081/api/roulette/pocket/color/getColor';
        try {
            const response = await fetch(apiUrl, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
            });
    
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
    
            const color = await response.text();
            console.log('Fetched color:', color);
            this.setState({ colorResult: color });
            return color;
        } catch (error) {
            console.error('Error fetching pocket color:', error);
            return null;
        }
    }

    reset_info = () => {
        document.getElementById('spin').innerText = 'empty';
    }

    async pick_random_offset(obj, random_key) {
        try {
            console.log("Random key: ", random_key );
            if (random_key !== null && obj[random_key]) {
                let range_arr = obj[random_key];
                let random_offset = Math.random() * (range_arr[1] - range_arr[0]) + range_arr[0];
                console.log("Random offset: ", random_offset );
                return random_offset;
            } else {
                console.log('Invalid random key or range array not found in the object');
                return null;
            }
        } catch (error) {
            console.error('Error setting random_key', error);
        }
    }

    reset = () => {
        this.setState({ spin: false, spin_complete: false, btn_disable: false })
    }

    update_last = (number) => {
        try {
            this.setState({ last_win: number })
        } catch (error) {
            console.log("Error in methods update_last: ", error );
            this.setState({ last_win: 1});
        }
    }

    get_color = () => {
        return this.state.colorResult;
    }


    spin = async () => {
        try {
            this.setState({ spin: true, btn_disable: true });
            const result = await this.fetch_spin();
            const color = await this.fetch_color(result);
            const number_obj = this.create_obj();
            let offset = await this.pick_random_offset(number_obj, result);
            console.log("Random offset inted: ", offset );
            if (!result) {
                console.warn('Invalid result from API. Spin cancelled.');
                this.setState({ spin: false, btn_disable: false });
                return;
            }
    
    
            this.setState({
                chosen: offset,
                chosen_num: parseInt(result),
                pendingResult: result, // Tymczasowo przechowujemy wynik
                pendingColor: color
            });
    
            setTimeout(() => {
                // Po zakończeniu animacji aktualizujemy last_win
                this.setState({
                    spin_complete: true,
                    spin: false,
                    last_win: parseInt(this.state.pendingResult), // Przeniesienie wyniku
                    last_color: this.state.pendingColor,
                    pendingResult: null, // Czyszczenie tymczasowego wyniku
                    pendingColor: null,
                });
    
                setTimeout(() => {
                    this.reset();
                }, 1000);
            }, 8500); // Czas trwania animacji
        } catch (error) {
            console.error("Error in spin:", error);
            this.setState({ spin: false, btn_disable: false });
        }
    };
    


    render() {
        let btn_dis_style = null;
        if(this.state.btn_disable){
            btn_dis_style = {
                cursor: 'not-allowed'
            }
        }
        let disabledBool = (this.state.btn_disable) ? "disabled" : "";
        const black_numbers = [2, 4, 6, 8, 10, 11, 13, 15,17,20,22,24,26,28,29,31,33,35]
        return (
            <div className={"gameWindow"}>
                <div className={"centre"}></div>
                <Board
                    spin={this.state.spin}
                    complete={this.state.spin_complete}
                    chosen_number={this.state.chosen}
                    black_numbers={black_numbers}
                />
                <div className={"mid_container"}>
                    <LastSpinResult
                        last_win={this.state.last_win}
                        color={this.state.last_color}
                    />
                    <BettingPanel
                        spin={this.spin}
                        complete={this.state.spin_complete}
                        disabledBool={disabledBool}
                        btn_dis_style={btn_dis_style}
                        get_color={this.get_color}
                        currentUser={this.props.currentUser}
                        setMoney={this.props.setMoney}
                        getMoney={this.props.getMoney}
                        updateMoney={this.props.updateMoney}
                    />
                </div>
            </div>
        )
    }
}

export default Roulette;
