import { crx } from "@crxjs/vite-plugin"
import react from "@vitejs/plugin-react"
import { defineConfig } from "vite"
import manifest from "./manifest.json"
import path from "path"

// https://vitejs.dev/config/
export default defineConfig({
    plugins: [react(), crx({ manifest })],
    resolve: {
        alias: {
            "@": path.resolve(__dirname, "./src"),
        },
    },
})
