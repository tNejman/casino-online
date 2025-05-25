import React from 'react';

/**
 * Functional component for the panel whcih shows the users balance
 * 
 * @param {object} props - {}
 * @return {JSX} - div
 */
const BalancePanel = (props) => {
    return (
        <div style={{fontSize: 20}}>Your bet: {props.children} $</div>
    )
}

export default BalancePanel