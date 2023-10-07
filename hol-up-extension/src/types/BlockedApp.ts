import { z } from "zod"

export const BlockedAppSchema = z.object({
    userId: z.string(),
    name: z
        .string({ required_error: "Name of app/website is required" })
        .min(1, { message: "Name of app/website is required" }),
    cooldown: z.coerce
        .number()
        .int()
        .min(0)
        .max(60, { message: "Cooldown can only be a maximum of 60 seconds" }),
    timeAllowed: z.coerce.number().int().min(0),
    timeUsed: z.coerce.number().int().min(0),
})

export type BlockedApp = z.infer<typeof BlockedAppSchema>
