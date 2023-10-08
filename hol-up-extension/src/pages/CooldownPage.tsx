import { useState, useEffect } from "react"
import holdup from "../assets/hold-up-meme.png"
import { Button } from "@/components/ui/button"

const CooldownPage = () => {
    const hostName = "tiktok"
    const redirectUrl = "https://www.tiktok.com/explore"
    const cooldown = 3
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

    useEffect(() => {
        return () => {
            clearInterval(intervalId)
        }
    }, [])

    return (
        <div className="align-center m-auto mt-2 flex w-full flex-col justify-center gap-6">
            <h1 className="text-6xl font-bold">Hold up!</h1>
            <img src={holdup} className="hold-up w-3/12"></img>

            {countdown <= 0 ? (
                <div className="flex w-1/4 flex-col justify-center gap-4">
                    <div className="text-3xl">
                        {todayCount} tries to view {hostName} today
                    </div>
                    <Button variant="outline">I want to procrastinate. LET ME IN</Button>
                    <Button>Okay I'll go be productive</Button>
                </div>
            ) : (
                <></>
            )}
        </div>
    )
}

export default CooldownPage
