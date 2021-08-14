package taboolib.platform

import taboolib.common.platform.*
import taboolib.common.platform.command.*

/**
 * TabooLib
 * taboolib.platform.SpongeCommand
 *
 * @author sky
 * @since 2021/7/4 2:45 下午
 */
@Awake
@PlatformSide([Platform.SPONGE_API_8])
class Sponge8Command : PlatformCommand {

    override fun registerCommand(
        command: CommandStructure,
        executor: CommandExecutor,
        completer: CommandCompleter,
        commandBuilder: CommandBuilder.CommandBase.() -> Unit,
    ) {
        // TODO: 2021/7/15 Not Support
    }

    override fun unregisterCommand(command: String) {
    }

    override fun unregisterCommands() {
    }

    override fun unknownCommand(sender: ProxyCommandSender, command: String, state: Int) {
    }
}