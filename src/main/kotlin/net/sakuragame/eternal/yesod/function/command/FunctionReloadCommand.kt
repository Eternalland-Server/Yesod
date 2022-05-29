package net.sakuragame.eternal.yesod.function.command

import taboolib.common.platform.command.command

object FunctionReloadCommand : ICommand {

    override fun i() {
        command("yreload", permission = "admin") {

        }
    }
}