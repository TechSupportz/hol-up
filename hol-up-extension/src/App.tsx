import { useState } from "react"
import { HashRouter, Route, Routes } from "react-router-dom"

import BlockedPage from "./pages/BlockedPage"
import CooldownPage from "./pages/CooldownPage"
import SettingsPage from "./pages/SettingsPage"
import { Toaster } from "./components/ui/toaster"

function App() {
    const [count, setCount] = useState(0)

    return (
        <>
            <HashRouter>
                <Routes>
                    <Route path="/" element={<BlockedPage />} />
                    <Route path="/settings" element={<SettingsPage />} />
                    <Route path="/blocked" element={<BlockedPage />} />
                    <Route path="/cooldown" element={<CooldownPage />} />
                </Routes>
            </HashRouter>
            <Toaster />
        </>
    )
}

export default App
