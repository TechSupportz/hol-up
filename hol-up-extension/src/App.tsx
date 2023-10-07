import { HashRouter, Route, Routes } from "react-router-dom"

import { Toaster } from "./components/ui/toaster"
import AddAppPage from "./pages/AddAppPage"
import BlockedPage from "./pages/BlockedPage"
import CooldownPage from "./pages/CooldownPage"
import PopUpPage from "./pages/PopUpPage"

function App() {
    console.log("App")

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
