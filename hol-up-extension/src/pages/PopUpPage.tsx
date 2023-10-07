import {
    Table,
    TableBody,
    TableCaption,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from "@/components/ui/table"
import React, { useState } from "react"
import { useNavigate } from "react-router-dom"

const PopUpPage = () => {
    const navigator = useNavigate()

    const [items, setItems] = useState([
        { name: "Facebook", time: 30, used: 10 },
        { name: "Instagram", time: 40, used: 20 },
    ])
    const [filterText, setFilterText] = useState("")

    // Function to handle category filter change
    const handleFilterTextChange = (event: { target: { value: React.SetStateAction<string> } }) => {
        setFilterText(event.target.value)
    }

    // Filter items based on the typed text
    const filteredItems = items.filter((item) =>
        item.name.toLowerCase().includes(filterText.toLowerCase()),
    )

    return (
        <Table>
            <TableCaption>A list of your recent invoices.</TableCaption>
            <TableHeader>
                <TableRow>
                    <TableHead className="w-[100px]">Invoice</TableHead>
                    <TableHead>Status</TableHead>
                    <TableHead>Method</TableHead>
                    <TableHead className="text-right">Amount</TableHead>
                </TableRow>
            </TableHeader>
            <TableBody>
                <TableRow>
                    <TableCell className="font-medium">INV001</TableCell>
                    <TableCell>Paid</TableCell>
                    <TableCell>Credit Card</TableCell>
                    <TableCell className="text-right">$250.00</TableCell>
                </TableRow>
            </TableBody>
        </Table>
    )
}

export default PopUpPage
