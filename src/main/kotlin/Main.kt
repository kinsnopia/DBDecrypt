import org.sqlite.mc.SQLiteMCRC4Config
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.sql.Connection
import java.sql.DriverManager
import kotlin.system.exitProcess


fun main() {
    val dir = getLocalAppDataDirectory()
    createUnlockedFile(dir)
    val password = "123qwe123"
    val newPassword = ""
    try {
        val connection: Connection =
            DriverManager.getConnection(
                "jdbc:sqlite:$dir/umag-pos-info-unlocked.db",
                SQLiteMCRC4Config.getDefault().withKey(password).build().toProperties()
            )
        connection.prepareStatement("PRAGMA key='$password'").execute()
        connection.prepareStatement("PRAGMA rekey='$newPassword'").execute()
    } finally {
        exitProcess(1)
    }
}

private fun getLocalAppDataDirectory(): String {
    val path: String = if (System.getProperty("os.name").uppercase().contains("WIN")) {
        System.getenv("LOCALAPPDATA")
    } else {
        System.getProperty("user.home") + ("/Library/Application Support")
    }
    val directoryName = "$path\\Umag"
    val directory = File(directoryName)
    if (!directory.exists()) {
        directory.mkdir().let {
            if (!it) {
                return path
            }
        }
    }
    return directoryName
}

private fun createUnlockedFile(dir: String) {
    val dbDir = "$dir\\umag-pos-info.db"
    val copyDir = "$dir\\umag-pos-info-unlocked.db"
    if (File(dbDir).exists()) {
        try {
            Files.copy(Paths.get(dbDir), Paths.get(copyDir))
        } catch (e: Exception) {
            println(e)
            exitProcess(3)
        }
    } else {
        exitProcess(2)
    }
}