import { useEffect } from "react"

const Background = () => {
    useEffect(() => {
        console.log(">>> Background", window.location.href)

        if (window.location.href.includes("tiktok")) {
            window.location.href =
                "chrome-extension://kfobgbkpbhomkogojhcphamlmpmojded/index.html#/blocked"
        }
    })

    return <></>
}

export default Background
