
import java.sql.Connection
import java.sql.DriverManager

fun main() {
    val jdbcUrl = "jdbc:postgresql://localhost:5432/school"
    val connection = DriverManager.getConnection(jdbcUrl, "sjo", "")

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


    val query = connection.prepareStatement("SELECT * FROM notas " +
            "                                   WHERE dni = ?")
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
