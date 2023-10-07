import { useEffect, useState } from "react"
import { collection, query, where, getDocs } from "firebase/firestore"
import { db } from "./firebase"
import { BlockedApp } from "./types/BlockedApp"

const Background = () => {
    const [blockedApps, setBlockedApps] = useState<string[]>([])

    useEffect(() => {
        getBlockedApps()
    }, [])

    useEffect(() => {
        const hostname = window.location.hostname.split(".")[1]
        const redirectUrl = window.location.href

		console.log(">>>", {hostname, redirectUrl, blockedApps})

        if (blockedApps && blockedApps.includes(hostname)) {
            window.location.href = `http://localhost:3000/#/cooldown?hostname=${hostname}&redirectUrl=${redirectUrl}`
        }
    }, [blockedApps])

    const getBlockedApps = async () => {
        // FIXME - get actual userId from local storage
        const q = query(collection(db, "blockedApps"), where("userId", "==", "007"))
        const querySnapshot = await getDocs(q)

        const blockedApps: string[] = []

        querySnapshot.forEach((doc) => {
            const data = doc.data() as BlockedApp
			console.log(">>>", data)
            blockedApps.push(data.name.toLowerCase())
        })

        setBlockedApps(blockedApps)
    }

    return <></>
}

export default Background
