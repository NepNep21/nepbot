package me.nepnep.nepbot.message.command.commands.animals

import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.ICommand
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import java.util.concurrent.ThreadLocalRandom

class RandomChimpEvent : ICommand {
    override fun execute(args: List<String>, event: GuildMessageReceivedEvent) {
        val monkeys = arrayOf(
            "https://bigrat.monster/media/monkeys/CwQRLGK.mp4?.gif",
            "https://bigrat.monster/media/monkeys/Monkey_Orange.gif",
            "https://bigrat.monster/media/monkeys/ShallowUnhealthyDeermouse-size_restricted.gif",
            "https://bigrat.monster/media/monkeys/chimp.gif",
            "https://bigrat.monster/media/monkeys/curiouswhat.gif",
            "https://bigrat.monster/media/monkeys/monkeyboat.gif",
            "https://bigrat.monster/media/monkeys/monkeydabke.gif",
            "https://bigrat.monster/media/monkeys/monkeylick.gif",
            "https://bigrat.monster/media/monkeys/monkeymoney.gif",
            "https://bigrat.monster/media/monkeys/orangutan.gif",
            "https://bigrat.monster/media/monkeys/piere.gif",
            "https://bigrat.monster/media/monkeys/telephonemonkey.gif",
            "https://cdn.discordapp.com/attachments/634010567107149824/756703349541437469/video0.mp4",
            "https://cdn.discordapp.com/attachments/634510679465918474/761378483515293706/video0.mp4",
            "https://cdn.discordapp.com/attachments/634510679465918474/761683469884981288/video0.mp4",
            "https://cdn.discordapp.com/attachments/710891361615740948/762005749588426762/video0.mp4",
            "https://cdn.discordapp.com/attachments/710891361615740948/762005767351566336/video0.mp4",
            "https://cdn.discordapp.com/attachments/710891361615740948/762005798041026640/video0.mp4",
            "https://cdn.discordapp.com/attachments/710891361615740948/762005826574745680/video0.mp4",
            "https://cdn.discordapp.com/attachments/710891361615740948/762005840240443402/video0.mov",
            "https://cdn.discordapp.com/attachments/710891361615740948/762005845424734238/video0.mp4",
            "https://cdn.discordapp.com/attachments/710891361615740948/762005728479150110/video0.mp4",
            "https://cdn.discordapp.com/attachments/710891361615740948/762014906039271524/video0.mp4"
        )
        event.channel.sendMessage(monkeys[ThreadLocalRandom.current().nextInt(0, monkeys.size)]).queue()
    }

    override fun getInvoke(): String {
        return "randomchimpevent"
    }

    override fun getCategory(): Category {
        return Category.ANIMALS
    }

    override fun getDescription(): String {
        return "Monkeys"
    }
}