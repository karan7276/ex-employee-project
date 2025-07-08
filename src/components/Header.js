// src/components/Header.js
import React from "react";
import logo from "../images/argano-logo.png"; // Make sure the logo is placed in src/images/

const headerStyle = {
  padding: "12px 30px",
  borderBottom: "1px solid #ddd",
  boxShadow: "0 2px 6px rgba(0,0,0,0.1)",
  backgroundColor: "#fff",
  display: "flex",
  alignItems: "center"
};

const logoStyle = {
  height: "40px"
};

export default function Header() {
  return (
    <div style={headerStyle}>
      <img src={logo} alt="Argano Logo" style={logoStyle} />
    </div>
  );
}
