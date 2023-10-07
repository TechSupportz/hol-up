import React from "react"
import ReactDOM from "react-dom/client"
import App from "./App"
import Background from "./Background"

const root = document.createElement("div")
root.id = "crx-root"
document.body.appendChild(root)

ReactDOM.createRoot(root).render(<Background />)
