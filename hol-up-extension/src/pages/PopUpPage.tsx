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
import { collection, getDocs, query, where } from "firebase/firestore"
import { useEffect, useState } from "react"
import { AiFillDelete } from "react-icons/ai"

export function PopUpPage() {
    useEffect(() => {
        getBlockedApps()
    }, [])
    const [HolUp, setHolUp] = useState<BlockedAppApiResponse[]>([])

    const [filterText, setFilterText] = useState<string>("")

    // Function to handle text input change
    const handleFilterTextChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setFilterText(event.target.value)
    }

    // Filter items based on the typed text
    const filteredItems = HolUp.filter((item) =>
        item.name.toLowerCase().includes(filterText.toLowerCase()),
    )

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

        setHolUp(blockedApps)
        console.log(blockedApps)
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
                                        src={`https://t3.gstatic.com/faviconV2?client=SOCIAL&type=FAVICON&fallback_opts=TYPE,SIZE,URL&url=https://www.${name.name}.com/&size=256`}></img>
                                </TableCell>
                                <TableCell>{name.name}</TableCell>
                                <TableCell>{name.cooldown} seconds</TableCell>
                                <TableCell
                                    className="text-center"
                                    // onClick={openScreen}
                                >
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
