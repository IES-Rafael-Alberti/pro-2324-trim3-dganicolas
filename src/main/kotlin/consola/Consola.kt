package consola

class Consola : Iconsola {

    override fun showMessage(message:String, lineBreak:Boolean){
        if(lineBreak) println(message)
        else print(message)
    }
}