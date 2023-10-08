import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import {
	Form,
	FormControl,
	FormDescription,
	FormField,
	FormItem,
	FormLabel,
	FormMessage,
} from "@/components/ui/form"
import { Input } from "@/components/ui/input"
import { toast } from "@/components/ui/use-toast"
import { db } from "@/firebase"
import { ManageAppForm, ManageAppFormSchema } from "@/types/ManageAppForm"
import { zodResolver } from "@hookform/resolvers/zod"
import { addDoc, collection } from "firebase/firestore"
import { useForm } from "react-hook-form"
import { useNavigate } from "react-router-dom"

const AddAppPage = () => {
    const navigate = useNavigate()

    const form = useForm<ManageAppForm>({
        defaultValues: {
            name: "",
            timeAllowed: 60,
            cooldown: 10,
        },
        resolver: zodResolver(ManageAppFormSchema),
        mode: "onTouched",
    })

    const onSubmit = async (data: ManageAppForm) => {
        console.log(data)

        try {
            await addDoc(collection(db, "blockedApps"), {
                userId: "008",
                name: data.name,
                cooldown: data.cooldown,
                timeAllowed: data.timeAllowed,
                timeUsed: 0,
            })

            navigate("/")
            toast({
                title: `${data.name} has been blocked!`,
                description: `You can now only use ${data.name} for ${data.timeAllowed} minutes with a ${data.cooldown} second cooldown timer`,
                className: "bg-purple-100 border-purple-300 border-2 shadow-purple-400/30",
                duration: 2500,
            })
        } catch (e) {
            console.error(e)
            toast({
                title: `Oops! Something went wrong`,
                description: (
                    <>
                        We couldn't block {data.name} for some reason <br />
                        <sub>(Pls don't use this as an excuse to procrastinate)</sub>
                    </>
                ),
                className: "bg-red-100 border-red-300 border-2 shadow-red-400/30",
                duration: 2500,
            })
        }
    }

    const onError = (errors: any) => {
        console.error(errors)
    }

    return (
        <div className="flex h-full w-full flex-col items-center justify-center">
            <Card className="shadow-md">
                <CardHeader>
                    <CardTitle>Block a new app</CardTitle>
                    <CardDescription className="max-w-md">
                        Is there a app/website that's taking up too much of you time? Add it here
                        and we'll help you stay on track
                    </CardDescription>
                </CardHeader>
                <CardContent>
                    <Form {...form}>
                        <form
                            className="flex flex-col gap-4"
                            onSubmit={form.handleSubmit(onSubmit, onError)}>
                            <FormField
                                control={form.control}
                                name="name"
                                render={({ field }) => (
                                    <FormItem>
                                        <FormLabel>App Name</FormLabel>
                                        <FormDescription>
                                            The name of the website/app you want to block (eg.
                                            TikTok)
                                        </FormDescription>
                                        <FormControl>
                                            <Input {...field} />
                                        </FormControl>

                                        <FormMessage />
                                    </FormItem>
                                )}
                            />
                            <FormField
                                control={form.control}
                                name="timeAllowed"
                                render={({ field }) => (
                                    <FormItem>
                                        <FormLabel>Time allowed (minutes)</FormLabel>
                                        <FormDescription>
                                            This is the total time you can spend on the app
                                        </FormDescription>
                                        <FormControl>
                                            <Input
                                                className="w-1/5"
                                                type="number"
                                                min={0}
                                                {...field}
                                            />
                                        </FormControl>

                                        <FormMessage />
                                    </FormItem>
                                )}
                            />
                            <FormField
                                control={form.control}
                                name="cooldown"
                                render={({ field }) => (
                                    <FormItem>
                                        <FormLabel>Cooldown time (seconds)</FormLabel>
                                        <FormDescription>
                                            This is the cooldown timer that will appear before you
                                            get to use the app
                                        </FormDescription>
                                        <FormControl>
                                            <Input
                                                className="w-1/5"
                                                type="number"
                                                min={0}
                                                {...field}
                                            />
                                        </FormControl>

                                        <FormMessage />
                                    </FormItem>
                                )}
                            />
                            <Button className="mt-2" type="submit">
                                Block it!
                            </Button>
                        </form>
                    </Form>
                </CardContent>
            </Card>
        </div>
    )
}

export default AddAppPage
