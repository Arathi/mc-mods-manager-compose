// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.undsf.mc.modsmgr.MainForm
import com.undsf.mc.modsmgr.curseforge.ApiClient

fun main(args: Array<String>) = application {
    var gui = false
    var apiKey = ""

    for (arg in args) {
        if ("--gui" == arg) {
            println("窗口模式")
            gui = true
        }

        if (arg.startsWith("--api-key=")) {
            apiKey = arg.substring(10)
            println("x-api-key=${apiKey}")
        }
    }

    if (gui) {
        Window(onCloseRequest = ::exitApplication,
            title = "Minecraft Mods Manager") {
            val curseForgeApiClient = ApiClient(apiKey)
            val mainForm = MainForm(curseForgeApiClient)
            mainForm.app()
        }
    }
    else {
        println("命令行模式")
    }
}
