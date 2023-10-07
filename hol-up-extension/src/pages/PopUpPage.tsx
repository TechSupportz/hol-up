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
import { useState } from "react"
import { GrEdit } from "react-icons/gr"

export function PopUpPage() {
    const HolUp = [
        {
            name: "Facebook",
            website: "Facebook",
            timeLeft: 30,
        },
        {
            name: "Instagram",
            website: "Instagram",
            timeLeft: 40,
        },
    ]
    const [filterText, setFilterText] = useState<string>("")

    // Function to handle text input change
    const handleFilterTextChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setFilterText(event.target.value)
    }

    // Filter items based on the typed text
    const filteredItems = HolUp.filter((item) =>
        item.name.toLowerCase().includes(filterText.toLowerCase()),
    )
    return (
        <div className="w-full p-4">
            <Input
                placeholder="Email"
                type="text"
                value={filterText}
                onChange={handleFilterTextChange}
            />
            <div className="rounded-md border mt-4">
                <Table>
                    <TableCaption>A list of your Blocked websites.</TableCaption>
                    <TableHeader>
                        <TableRow>
                            <TableHead className="text-center">Logo</TableHead>
                            <TableHead className="text-center">website</TableHead>
                            <TableHead className="text-center">Time Left</TableHead>
                            <TableHead className="text-center">Edit</TableHead>
                        </TableRow>
                    </TableHeader>
                    <TableBody>
                        {filteredItems.map((name) => (
                            <TableRow key={name.name}>
                                <TableCell className="font-medium">{name.name}</TableCell>
                                <TableCell>{name.website}</TableCell>
                                <TableCell>{name.timeLeft} min</TableCell>
                                <TableCell className="text-right">
                                    <GrEdit />
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
