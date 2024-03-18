
import org.postgresql.util.PSQLException
import java.sql.Connection
import java.sql.DriverManager


data class Usuario(val dni:String,val apenom:String, val direc:String,val pobla:String, val telef:String)

val usuariosNuevos = listOf(
    Usuario("98765432", "Gómez Pérez, Juan", "C/Gran Vía, 10", "Barcelona", "932345678"),
    Usuario("34567891", "Martínez López, Ana", "Avda. Libertad, 15", "Valencia", "963456789"),
    Usuario("45678901", "Sánchez Rodríguez, Pablo", "C/Alcalá, 45", "Madrid", "910111213")
)

fun main() {
    val jdbcUrl = "jdbc:postgresql://localhost:5432/school"
    val connection = DriverManager.getConnection(jdbcUrl, "sjo", "")
    Exercici5(connection)
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
        println("ID :: $id Cognom i Nom :: $apenom Direcció :: " +
                "$direc Població :: $pobla Telèfon :: $telef")
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

fun Exercici3(connection:Connection) {
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

fun Exercici4(connection:Connection) {
    val query = connection.prepareStatement("INSERT INTO alumnos VALUES (?,?,?,?,?)")
    var quantitatInserts:Int = 0



    try {
        for (usuario in usuariosNuevos) {
            query.setString(1,usuario.dni)
            query.setString(2,usuario.apenom)
            query.setString(3,usuario.direc)
            query.setString(4,usuario.pobla)
            query.setString(5,usuario.telef)
            quantitatInserts += query.executeUpdate()
        }
        println("S'han fet una quantitat de $quantitatInserts")
    } catch (e:PSQLException){
        println("Probablement has intentat inserir un DNI ja existent")
    } finally {
        query.close()
    }
}

fun Exercici5(connection:Connection) {
    val asignaturas:MutableList<Int> = mutableListOf()

    var query = connection.createStatement()


    val asignaturasABuscar = listOf("FOL","RET")
    val result = query.executeQuery("SELECT * FROM asignaturas WHERE nombre = '${asignaturasABuscar[0]}' OR nombre = '${asignaturasABuscar[1]}'")


    while (result.next()) {
        val idAssignatura = result.getInt("cod")
        asignaturas.add(idAssignatura)
    }

    query = connection.prepareStatement("INSERT INTO notas VALUES (?,?,?)")
    var quantitatInserts:Int = 0


    try {

        for (usuario in usuariosNuevos) {
            query.setString(1,usuario.dni)
            query.setInt(2, asignaturas[0])
            query.setInt(3,8)
            quantitatInserts += query.executeUpdate()

            query.setString(1,usuario.dni)
            query.setInt(2, asignaturas[1])
            query.setInt(3,8)
            quantitatInserts += query.executeUpdate()
        }


        println("S'han fet una quantitat de $quantitatInserts")
    } catch (e:PSQLException){
        println("Probablement el usuari no existeix o has duplicat el dni")
        println(e.message)
    } finally {
        query.close()
    }
}