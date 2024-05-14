import DAO.Dao
import dataclass.Ctfs
import dataclass.Grupos


interface Iconsola {
    fun showMessage(message: String, lineBreak: Boolean=true)
    //fun show(userList: List<Book>?,message: String = "All book:")
    fun elegirOpcion(): Int
    fun ctfsInsertado(fuenteDeDato:Dao,ctfs: Ctfs?)
    fun grupoInsertado(grupo: Grupos?)
}