import React from "react";
import "./Navbar.css"
import { useEffect, useRef, useState } from 'react';

/**
 * Component with information about Player and access to Account Details
 * 
 * @param {function} setCurrentView - changes window between options
 * @return {JSX} - div
 */
const Navbar = (props) => {
    const [dropdownOpen, setDropdownOpen] = useState(false);
    const dropdownRef = useRef(null);
  
    // toggle dropdown visibility on profile icon click
    const toggleDropdown = () => {
        setDropdownOpen((prev) => !prev);
    };
  
    // close the dropdown if clicking outside
    useEffect(() => {
        const handleClickOutside = (event) => {
            if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
                setDropdownOpen(false);
            }
        };
        document.addEventListener('mousedown', handleClickOutside);
        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, []);
  
      return (
          <div className="navbar">
              <div className="navbar-title" onClick={() => props.setCurrentView("Home")}>CASINO MEGA GIGA WIN</div>
              <div className="account-overview">
                  <div className="money-count">{props.currentUser.money} $</div>
                  <div className="profile-icon" onClick={toggleDropdown} ref={dropdownRef}>
                      <img src={require("../../../images/ludzik2.png")} alt="Profile" className="profile-img"/>
                      {dropdownOpen && (
                          <div className="dropdown-menu">
                              <ul>
                                  <li onClick={() => props.setCurrentView("Home")}>Home</li>
                                  <li onClick={() => props.setCurrentView("AccountOverview")}>Account Overview</li>
                                  <li onClick={() => props.setCurrentView("Settings")}>Settings</li>
                                  <li onClick={() => props.endSession()}>Log Out</li>
                              </ul>
                          </div>
                      )}
                  </div>
              </div>
          </div>
      );
  };

export default Navbar;