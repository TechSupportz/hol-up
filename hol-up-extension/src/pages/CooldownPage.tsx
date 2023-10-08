import { useState, useEffect } from "react"
import holdup from "../assets/hold-up-meme.png"
import { Button } from "@/components/ui/button"
import { useSearchParams } from "react-router-dom"

const CooldownPage = () => {
    const queryParams = new URLSearchParams(window.location.hash.split("?")[1])

    const hostName = queryParams.get("hostname")!
    const redirectUrl = queryParams.get("redirectUrl")!
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
        sessionStorage.setItem("isProcrastinating", "true")
    }

    const handleProductiveClick = () => {
        // @ts-ignore
        chrome.tabs.create({ url: "about://newtab", active: true })
        window.close()
    }

    useEffect(() => {
        console.log(">>>", { cooldown, hostName, redirectUrl })

        return () => {
            clearInterval(intervalId)
        }
    }, [])

    return (
        <div className="h-full w-full">
            <div className=" flex h-full w-full flex-col items-center justify-center gap-6">
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
        </div>
    )
}

export default CooldownPage
