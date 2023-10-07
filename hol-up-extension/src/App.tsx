import { useState } from "react"
import { HashRouter, Route, Routes } from "react-router-dom"

import { Toaster } from "./components/ui/toaster"
import BlockedPage from "./pages/BlockedPage"
import CooldownPage from "./pages/CooldownPage"
import PopUpPage from "./pages/PopUpPage"
import AddAppPage from "./pages/AddAppPage"

function App() {
    const [count, setCount] = useState(0)

    return (
        <>
            <HashRouter>
                <Routes>
                    <Route path="/*" element={<PopUpPage />} />
                    <Route path="/add" element={<AddAppPage />} />
                    <Route path="/blocked" element={<BlockedPage />} />
                    <Route path="/cooldown" element={<CooldownPage />} />
                </Routes>
            </HashRouter>
            <Toaster />
        </>
    )
}

export default App
