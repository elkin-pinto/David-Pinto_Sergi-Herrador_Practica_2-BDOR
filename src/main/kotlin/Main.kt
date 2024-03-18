import org.postgresql.util.PSQLException
import java.sql.Connection
import java.sql.DriverManager

fun main() {
    val jdbcUrl = "jdbc:postgresql://localhost:5432/school"

    val connection = DriverManager.getConnection(jdbcUrl, "sjo", "")

    try {
        println("Exercici 1"); Exercici1(connection)
        println("Exercici 2"); Exercici2(connection)
        println("Exercici 3"); Exercici3(connection)
        println("Exercici 6"); Exercici6(connection)
        println("Exercici 7"); Exercici7(connection)
        println("Exercici 8"); Exercici8(connection)

    } catch (e: PSQLException) {
        println(e.message)
    }

    connection.close()
}


fun Exercici1(connection: Connection) {

    val query = connection.createStatement()
    val sql = "SELECT * FROM alumnos"
    val result = query.executeQuery(sql)

    while (result.next()) {
        val id = result.getString("dni")
        val apenom = result.getString("apenom")
        val direc = result.getString("direc")
        val pobla = result.getString("pobla")
        val telef = result.getString("telef")
        println("ID :: $id Cognom i Nom :: $apenom Direcció :: $direc Població :: $pobla Telèfon :: $telef")
    }

    result.close()
    query.close()
}

fun Exercici2(connection: Connection) {
    val query = connection.createStatement()
    val sql = "SELECT * FROM notas"
    val result = query.executeQuery(sql)

    while (result.next()) {
        val dni = result.getString("dni")
        val cod = result.getInt("cod")
        val nota = result.getInt("nota")
        println("DNI :: $dni Cod :: $cod Nota :: $nota")
    }

    result.close()
    query.close()
}

fun Exercici3(connection: Connection) {
    val dni = "4448242"

    val query = connection.prepareStatement("SELECT * FROM notas WHERE dni = ?")
    query.setString(1, dni) // dni es la variable 4448242

    val result = query.executeQuery() // executem la query amb el

    // mostrem dades amb una iteració
    println("Notes de l'alumne amb DNI: $dni")

    while (result.next()) {
        val cod = result.getInt("cod")
        val nota = result.getInt("nota")
        println("Codi: $cod Nota: $nota")
    }

    result.close()
    query.close()
}

fun Exercici6(connection: Connection) {
    val dni = "4448242"
    val codiFol = 4 // FOL codi
    val codiRet = 5 // RET codi
    val notaNova = 9

    // Update FOL grade
    val updateFOLStatement = connection.prepareStatement("UPDATE notas SET nota = ? WHERE dni = ? AND cod = ?")
    updateFOLStatement.setInt(1, notaNova) // posem la nova nota en el set
    updateFOLStatement.setString(2, dni)
    updateFOLStatement.setInt(3, codiFol)
    val folUpdated = updateFOLStatement.executeUpdate()
    updateFOLStatement.close()

    // Update RET grade
    val updateRETQuery = "UPDATE notas SET nota = ? WHERE dni = ? AND cod = ?"
    val updateRETStatement = connection.prepareStatement(updateRETQuery)
    updateRETStatement.setInt(1, notaNova)
    updateRETStatement.setString(2, dni)
    updateRETStatement.setInt(3, codiRet)
    val retUpdated = updateRETStatement.executeUpdate()
    updateRETStatement.close()

    if (folUpdated > 0 && retUpdated > 0) {
        println("S'ha fet l'actualització correctament")
    } else {
        println("No s'ha actualitzat ningun registre")
    }
}

// 7.- Modificar el teléfon de l’alumne amb DNI = 12344345, el nou teléfon és 934885237.
fun Exercici7(connection: Connection) {
    val dni = "12344345"
    val newPhoneNumber = "934885237"

    val sqlString = "UPDATE alumnos SET telef = ? WHERE dni = ?"
    val query = connection.prepareStatement(sqlString)
    query.setString(1, newPhoneNumber)
    query.setString(2, dni)

    val columnesActualitzdes = query.executeUpdate()

    if (columnesActualitzdes > 0) {
        println("El telefòn del estudiant amb DNI: $dni s'actualitzat a $newPhoneNumber.")
    } else {
        println("No s'ha pogut actualitzar")
    }

    query.close()
}

// 8.- Eliminar l’alumne que viu a "Mostoles".
fun Exercici8(connection: Connection) {
    val poblacioAEsborrar = "Mostoles"
    val deleteQuery = "DELETE FROM alumnos WHERE pobla = '$poblacioAEsborrar'"

    val deleteStatement = connection.createStatement()

    val rowsAffected = deleteStatement.executeUpdate(deleteQuery)

    if (rowsAffected > 0) {
        println("Estudiants vivint en $poblacioAEsborrar s'ha esborrat")
    } else {
        println("No s'ha trobat ningú estudiant vivint en: $poblacioAEsborrar.")
    }

    deleteStatement.close()
}