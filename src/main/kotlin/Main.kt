import org.postgresql.util.PSQLException
import java.sql.Connection
import java.sql.DriverManager


data class Usuario(val dni: String, val apenom: String, val direc: String, val pobla: String, val telef: String)


fun main() {
    val jdbcUrl = "jdbc:postgresql://localhost:5432/school"

    try {
        val connection = DriverManager.getConnection(jdbcUrl, "sjo", "")

        println("Exercici 1"); Exercici1(connection)
        println("Exercici 2"); Exercici2(connection)
        println("Exercici 3"); Exercici3(connection)
        println("Exercici 4"); Exercici4(connection)
        println("Exercici 5"); Exercici5(connection)
        println("Exercici 6"); Exercici6(connection)
        println("Exercici 7"); Exercici7(connection)
        println("Exercici 8"); Exercici8(connection)

        connection.close()
    } catch (e: PSQLException) {
        println(e.message)
    }
}

// 1.- Mostrar les dades de la taula ALUMNOS.
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

// 2.- Mostrar totes les dades de la taula NOTAS
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

// 3.- Mostrar les notes de l’alumne amb DNI 4448242 de la taula NOTAS utilitzant el Prepared Statement.
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

// 4.- Insertar 3 alumnes nous. Inventat les dades dels alumnes nous.

//Llista de nous usuaris
val usuariosNuevos = listOf(
    Usuario("98765432", "Gómez Pérez, Juan", "C/Gran Vía, 10", "Barcelona", "932345678"),
    Usuario("34567891", "Martínez López, Ana", "Avda. Libertad, 15", "Valencia", "963456789"),
    Usuario("45678901", "Sánchez Rodríguez, Pablo", "C/Alcalá, 45", "Madrid", "910111213")
)


fun Exercici4(connection: Connection) {
    val query = connection.createStatement()
    var quantitatInserts = 0

    try {
        for (usuario in usuariosNuevos) {
            val sqlString = "INSERT INTO alumnos VALUES (${usuario.dni},${usuario.apenom},${usuario.direc},${usuario.pobla},${usuario.telef})"
            quantitatInserts += query.executeUpdate(sqlString)
        }
        println("S'han fet una quantitat de $quantitatInserts inserts")

    } catch (e: PSQLException) {
        println("Probablement has intentat inserir un DNI ja existent")
    } finally {
        query.close()
    }
}

// 5.- Insertar les notes per aquests 3 nous alumnes de les assignatures FOL i RET. Tots han tret un 8 en les dues
// assignatures. Utilitza el Prepared Statement.

fun Exercici5(connection: Connection) {
    val asignaturas: MutableList<Int> = mutableListOf()

    var query = connection.createStatement()
    val asignaturasABuscar = listOf("FOL", "RET")
    val result =
        query.executeQuery("SELECT * FROM asignaturas WHERE nombre = '${asignaturasABuscar[0]}' OR nombre = '${asignaturasABuscar[1]}'")

    while (result.next()) {
        val idAssignatura = result.getInt("cod")
        asignaturas.add(idAssignatura)
    }

    query = connection.prepareStatement("INSERT INTO notas VALUES (?,?,?)")
    var quantitatInserts = 0

    try {
        for (usuario in usuariosNuevos) {
            query.setString(1, usuario.dni)
            query.setInt(2, asignaturas[0])
            query.setInt(3, 8)
            quantitatInserts += query.executeUpdate()

            query.setString(1, usuario.dni)
            query.setInt(2, asignaturas[1])
            query.setInt(3, 8)
            quantitatInserts += query.executeUpdate()
        }


        println("S'han fet una quantitat de $quantitatInserts inserts")
    } catch (e: PSQLException) {
        println("Probablement l'usuari no existeix o el dni està duplicat")
        println(e.message)
    } finally {
        query.close()
    }
}


// 6.- Modificar les notes de l’alumne "Cerrato Vela, Luis" de FOL i RET, ha tret un 9.
fun Exercici6(connection: Connection) {
    val dni = "4448242"
    val notaNova = 9

    val updateStatement = connection.createStatement()

    // Ejecutamos la consulta
    val rowsUpdated = updateStatement.executeUpdate(
        "UPDATE notas " +
                "SET nota = $notaNova " +
                "FROM asignaturas " +
                "WHERE notas.cod = asignaturas.cod AND notas.dni = $dni AND (asignaturas.nombre = 'FOL' OR asignaturas.nombre = 'RET')"
    )
    println("Filas actualizadas: $rowsUpdated")
}


// 7.- Modificar el teléfon de l’alumne amb DNI = 12344345, el nou teléfon és 934885237.
fun Exercici7(connection: Connection) {
    val dni = "12344345"
    val newPhoneNumber = "934885237"

    val sqlString = "UPDATE alumnos SET telef = '$newPhoneNumber' WHERE dni = '$dni'"
    val query = connection.createStatement()

    val columnesActualitzdes = query.executeUpdate(sqlString)

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
