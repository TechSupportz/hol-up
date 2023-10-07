import { useState } from "react"
import { MemoryRouter, Routes, Route, HashRouter } from "react-router-dom"
import logo from "./logo.svg"
import "./App.css"
import { Button } from "./components/ui/button"

import SettingsPage from "./pages/SettingsPage"
import CooldownPage from "./pages/CooldownPage"
import BlockedPage from "./pages/BlockedPage"

function App() {
    const [count, setCount] = useState(0)

    return (
        <div className="App">
            <HashRouter>
                <Routes>
                    <Route path="/*" element={<SettingsPage />} />
                    <Route path="/blocked" element={<BlockedPage />} />
                    <Route path="/cooldown" element={<CooldownPage />} />
                </Routes>
            </HashRouter>
        </div>
    )
}

export default App
