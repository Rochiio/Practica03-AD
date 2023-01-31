import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.ksp.generated.defaultModule
import view.Vista
fun main(args: Array<String>): Unit = runBlocking {
    startKoin {
        defaultModule()
    }
    KoinApp().run()

}

class KoinApp : KoinComponent {
    val vista: Vista by inject()

    fun run() {
        vista.principal()

    }
}