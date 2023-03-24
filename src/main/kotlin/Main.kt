import org.sqlite.mc.SQLiteMCSqlCipherConfig
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import kotlin.system.exitProcess


fun main() {
    val dir = getLocalAppDataDirectory()
    val password = "123qwe123"
    val newPassword = ""
    try {
        val connection: Connection =
            DriverManager.getConnection(
                "jdbc:sqlite:$dir/umag-pos-info.db",
                SQLiteMCSqlCipherConfig.getDefault().withKey(password).build().toProperties()
            )
        connection.prepareStatement("PRAGMA key='$password'").execute()
        connection.prepareStatement("PRAGMA rekey='$newPassword'").execute()
    } finally {
        exitProcess(1)
    }
}

fun getLocalAppDataDirectory(): String {
    val path: String = if (System.getProperty("os.name").uppercase().contains("WIN")) {
        System.getenv("LOCALAPPDATA")
    } else {
        System.getProperty("user.home") + ("/Library/Application Support")
    }
    val directoryName = "$path/Umag"
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