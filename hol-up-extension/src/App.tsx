import { useState } from "react"
import { HashRouter, Route, Routes } from "react-router-dom"

import BlockedPage from "./pages/BlockedPage"
import CooldownPage from "./pages/CooldownPage"
import SettingsPage from "./pages/SettingsPage"

function App() {
    console.log("App")

    return (
        <>
            <HashRouter>
                <Routes>
                    <Route path="/*" element={<SettingsPage />} />
                    <Route path="/blocked" element={<BlockedPage />} />
                    <Route path="/cooldown" element={<CooldownPage />} />
                </Routes>
            </HashRouter>
        </>
    )
}

export default App
