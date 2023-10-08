import { Input } from "@/components/ui/input"
import {
    Table,
    TableBody,
    TableCaption,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from "@/components/ui/table"
import { db } from "@/firebase"
import { BlockedAppApiResponse } from "@/types/BlockedApp"
import { collection, deleteDoc, getDocs, query, where } from "firebase/firestore"
import { useEffect, useState } from "react"
import { AiFillDelete } from "react-icons/ai"

export function PopUpPage() {
    useEffect(() => {
        getBlockedApps()
    }, [])

    const [HolUp, setHolUp] = useState<BlockedAppApiResponse[]>([])
    const [filterText, setFilterText] = useState<string>("")

    const handleFilterTextChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setFilterText(event.target.value)
    }

    const filteredItems = HolUp.filter((item) =>
        item.name.toLowerCase().includes(filterText.toLowerCase()),
    )

    const getBlockedApps = async () => {
        const q = query(collection(db, "blockedApps"), where("userId", "==", "010"))
        const querySnapshot = await getDocs(q)

        const blockedApps: BlockedAppApiResponse[] = []

        querySnapshot.forEach((doc) => {
            const data = doc.data() as BlockedAppApiResponse
            blockedApps.push({ ...data, docId: doc.id })
        })

        setHolUp(blockedApps)
    }

    const deleteUser = async (userId: string, name: string) => {
        try {
            const q = query(
                collection(db, "blockedApps"),
                where("userId", "==", userId),
                where("name", "==", name),
            )
            const querySnapshot = await getDocs(q)

            querySnapshot.forEach(async (doc) => {
                await deleteDoc(doc.ref)
            })

            // After deletion, re-fetch the data
            getBlockedApps()
        } catch (error) {
            console.error("Error deleting document:", error)
        }
    }

    return (
        <div className="w-full p-4">
            <Input
                placeholder="Website"
                type="text"
                value={filterText}
                onChange={handleFilterTextChange}
            />
            <div className="mt-4 rounded-md border">
                <Table>
                    <TableCaption>A list of your Blocked websites.</TableCaption>
                    <TableHeader>
                        <TableRow>
                            <TableHead className="text-center">Logo</TableHead>
                            <TableHead className="text-center">website</TableHead>
                            <TableHead className="text-center">Cooldown</TableHead>
                            <TableHead className="text-center">Delete</TableHead>
                        </TableRow>
                    </TableHeader>
                    <TableBody>
                        {filteredItems.map((name) => (
                            <TableRow key={name.name}>
                                <TableCell className="font-medium">
                                    <img
                                        className="h-8 w-8"
                                        src={`https://t3.gstatic.com/faviconV2?client=SOCIAL&type=FAVICON&fallback_opts=TYPE,SIZE,URL&url=https://www.${name.name}.com/&size=256`}
                                        alt={`Logo for ${name.name}`}
                                    />
                                </TableCell>
                                <TableCell>{name.name}</TableCell>
                                <TableCell>{name.cooldown} seconds</TableCell>
                                <TableCell
                                    className="text-center"
                                    onClick={() => deleteUser(name.userId, name.name)}>
                                    <AiFillDelete className="m-auto" size={25} />
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </div>
        </div>
    )
}

export default PopUpPage
