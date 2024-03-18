
import java.sql.DriverManager

fun main() {
    val jdbcUrl = "jdbc:postgresql://localhost:5432/school"
    val connection = DriverManager.getConnection(jdbcUrl, "sjo", "")

    val query = connection.prepareStatement("INSERT INTO alumnos VALUES(?,?,?,?,?)")
    val result = query.executeQuery()

    while (result.next()) {
        val dni = result.getString("dni")
        val cod = result.getInt("cod")
        val nota = result.getInt("nota")
        println("DNI :: $dni Cod :: $cod Nota :: $nota")
    }

    result.close()
    query.close()
    connection.close()
}