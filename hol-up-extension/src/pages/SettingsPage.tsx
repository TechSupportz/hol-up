import React from "react"
import { useNavigate } from "react-router-dom"

const SettingsPage = () => {
    const navigator = useNavigate()

    return (
        <div>
            <button
                onClick={() => {
                    navigator("/cooldown")
                }}>
                CLICKM E
            </button>
        </div>
    )
}

export default SettingsPage
