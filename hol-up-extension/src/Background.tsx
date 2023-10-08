import { useEffect, useState } from "react"
import { collection, query, where, getDocs, updateDoc, getDoc, doc } from "firebase/firestore"
import { db } from "./firebase"
import { BlockedApp, BlockedAppApiResponse } from "./types/BlockedApp"

const Background = () => {
    const [blockedApps, setBlockedApps] = useState<BlockedAppApiResponse[]>([])

    useEffect(() => {
        const isProcrastinate = window.location.href.split("procrastinate=")[1] === "true"

        if (isProcrastinate) {
            return
        }

        getBlockedApps()
    }, [])

    useEffect(() => {
        const hostname = window.location.hostname.split(".")[1]
        const redirectUrl = `${window.location.href}#procrastinate=true`

        console.log(">>>", { hostname, redirectUrl, blockedApps })

        if (blockedApps) {
            const blockedApp = blockedApps.find((app) =>
                app.name.toLowerCase().includes(hostname.toLowerCase()),
            )

            if (!blockedApp) return

            incrementTimesBlocked(blockedApp.docId)

            window.location.href = `http://localhost:3000/#/cooldown?hostname=${hostname}&redirectUrl=${redirectUrl}&docId=${
                blockedApp.docId
            }&timeUsed=${blockedApp.timeAllowed - (blockedApp.timeUsed + 1)}`
        }
    }, [blockedApps])

    const incrementTimesBlocked = async (docId: string) => {
        const blockedAppRef = doc(db, "blockedApps", docId)
        const blockedAppDoc = await getDoc(blockedAppRef)

        if (blockedAppDoc.exists()) {
            const blockedApp = blockedAppDoc.data() as BlockedApp
            const { timeUsed } = blockedApp

            await updateDoc(blockedAppRef, {
                timeUsed: timeUsed + 1,
            })
        }
    }

    const getBlockedApps = async () => {
        // FIXME - get actual userId from local storage
        const q = query(collection(db, "blockedApps"), where("userId", "==", "008"))
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
