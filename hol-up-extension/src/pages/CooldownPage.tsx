import { useState, useEffect } from "react"
import holdup from "../assets/hold-up-meme.png"
import { Button } from "@/components/ui/button"

const CooldownPage = () => {
    const hostName = "tiktok"
    const redirectUrl = "https://www.tiktok.com/explore"
    const cooldown = 6
    const todayCount = 1

    const [countdown, setCountdown] = useState(cooldown)

    const intervalId = setInterval(() => {
        if (countdown > 0) {
            setCountdown(countdown - 1)
            console.log(countdown)
            clearInterval(intervalId)
            return
        }
    }, 1000)

    const handleProcrastinateClick = () => {
        window.location.href = redirectUrl
    }

    const handleProductiveClick = () => {
        // @ts-ignore
        chrome.tabs.create({ url: "about://newtab", active: true })
        window.close()
    }

    useEffect(() => {
        return () => {
            clearInterval(intervalId)
        }
    }, [])

    return (
        <div className="align-center m-auto mt-2 flex w-full flex-col justify-center gap-6">
            <h1 className="text-6xl font-bold">Hold up!</h1>
            <img src={holdup} className="hold-up w-2/12"></img>
            <div className="text-3xl">
                {todayCount} tries to view {hostName} today. Let's slow down a bit.
            </div>
            {countdown <= 0 ? (
                <div className="flex w-1/4 flex-col justify-center gap-4">
                    <Button variant="outline" onClick={handleProcrastinateClick}>
                        I want to procrastinate. LET ME IN
                    </Button>
                    <Button onClick={handleProductiveClick}>Okay I'll go be productive</Button>
                </div>
            ) : (
                <></>
            )}
        </div>
    )
}

export default CooldownPage
