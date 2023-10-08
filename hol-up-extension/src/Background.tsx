import { useEffect, useState } from "react"
import { collection, query, where, getDocs } from "firebase/firestore"
import { db } from "./firebase"
import { BlockedApp, BlockedAppApiResponse } from "./types/BlockedApp"

const Background = () => {
    const [blockedApps, setBlockedApps] = useState<BlockedAppApiResponse[]>([])

    useEffect(() => {
        getBlockedApps()
    }, [])

    useEffect(() => {
        const hostname = window.location.hostname.split(".")[1]
        const redirectUrl = window.location.href

        console.log(">>>", { hostname, redirectUrl, blockedApps })

        if (blockedApps) {
            const blockedApp = blockedApps.find((app) =>
                app.name.toLowerCase().includes(hostname.toLowerCase()),
            )

            if (!blockedApp) return

            window.location.href = `http://localhost:3000/#/cooldown?hostname=${hostname}&redirectUrl=${redirectUrl}&docId=${blockedApp.docId}`
        }
    }, [blockedApps])

    const getBlockedApps = async () => {
        // FIXME - get actual userId from local storage
        const q = query(collection(db, "blockedApps"), where("userId", "==", "007"))
        const querySnapshot = await getDocs(q)

        const blockedApps: BlockedAppApiResponse[] = []

        querySnapshot.forEach((doc) => {
            const data = doc.data() as BlockedAppApiResponse
            console.log(">>>", data)
            blockedApps.push({ ...data, docId: doc.id })
        })

        setBlockedApps(blockedApps)
    }

    return <></>
}

export default Background
