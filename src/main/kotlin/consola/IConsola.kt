import dataclassEntity.Ctfs
import dataclassEntity.Grupos
import service.IGruposService


interface Iconsola {
    fun showMessage(message: String, lineBreak: Boolean=true)
    //fun show(userList: List<Book>?,message: String = "All book:")
    fun elegirOpcion(): Int
    fun ctfsInsertado(fuenteDeDato: IGruposService, ctfs: Ctfs?)
    fun grupoInsertado(grupo: Grupos?)
    abstract fun participacionEliminadas(nombreGrupo: String, lista: List<Ctfs>?)
}